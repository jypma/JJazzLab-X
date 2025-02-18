/*
 *  DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 *  Copyright @2019 Jerome Lelasseux. All rights reserved.
 *
 *  This file is part of the JJazzLabX software.
 *   
 *  JJazzLabX is free software: you can redistribute it and/or modify
 *  it under the terms of the Lesser GNU General Public License (LGPLv3) 
 *  as published by the Free Software Foundation, either version 3 of the License, 
 *  or (at your option) any later version.
 *
 *  JJazzLabX is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 * 
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with JJazzLabX.  If not, see <https://www.gnu.org/licenses/>
 * 
 *  Contributor(s): 
 */
package org.jjazz.ui.ss_editor.actions;

import com.google.common.collect.Iterables;
import org.jjazz.ui.ss_editor.api.SS_ContextActionSupport;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.AbstractAction;
import javax.swing.Action;
import static javax.swing.Action.ACCELERATOR_KEY;
import static javax.swing.Action.NAME;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.jjazz.rhythm.api.Rhythm;
import org.jjazz.rhythm.api.RhythmParameter;
import org.jjazz.songstructure.api.SongPart;
import org.jjazz.ui.ss_editor.api.SS_EditorTopComponent;
import org.jjazz.ui.ss_editor.api.SS_SelectionUtilities;
import org.jjazz.undomanager.api.JJazzUndoManager;
import org.jjazz.undomanager.api.JJazzUndoManagerFinder;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.util.ContextAwareAction;
import org.openide.util.Lookup;
import org.openide.util.Utilities;
import org.jjazz.songstructure.api.SongStructure;
import org.jjazz.ui.ss_editor.api.RpValueCopyBuffer;
import org.jjazz.ui.ss_editor.api.SS_ContextActionListener;
import static org.jjazz.ui.utilities.api.Utilities.getGenericControlKeyStroke;
import org.jjazz.util.api.ResUtil;

/**
 * Paste RhythmParameter values.
 * <p>
 * @todo Improve paste: should work if song part selected, and possible to past 1 value to multiple song parts
 */
@ActionID(category = "JJazz", id = "org.jjazz.ui.ss_editor.actions.pasterpvalue")
@ActionRegistration(displayName = "#CTL_PasteRpValue", lazy = false)
@ActionReferences(
        {
            @ActionReference(path = "Actions/RhythmParameter", position = 30, separatorAfter = 31)
        })
public class PasteRpValue extends AbstractAction implements ContextAwareAction, SS_ContextActionListener, ChangeListener
{

    private Lookup context;
    private SS_ContextActionSupport cap;
    private final String undoText = ResUtil.getString(getClass(), "CTL_PasteRpValue");

    public PasteRpValue()
    {
        this(Utilities.actionsGlobalContext());
    }

    private PasteRpValue(Lookup context)
    {
        this.context = context;
        cap = SS_ContextActionSupport.getInstance(this.context);
        cap.addListener(this);
        putValue(NAME, undoText);
        putValue(ACCELERATOR_KEY, getGenericControlKeyStroke(KeyEvent.VK_V));
        RpValueCopyBuffer buffer = RpValueCopyBuffer.getInstance();
        buffer.addChangeListener(this);
        selectionChange(cap.getSelection());
    }

    @Override
    public Action createContextAwareInstance(Lookup context)
    {
        return new PasteRpValue(context);
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        SS_SelectionUtilities selection = cap.getSelection();
        SongStructure sgs = selection.getModel();

        var buffer = RpValueCopyBuffer.getInstance();
        var r = buffer.getRhythm();
        var rp = buffer.getRhythmParameter();
        List<Object> values = buffer.get();
        assert !values.isEmpty() : "buffer=" + buffer;
        var selSpts = getPastableSongParts(selection, r, rp);


        JJazzUndoManager um = JJazzUndoManagerFinder.getDefault().get(sgs);
        um.startCEdit(undoText);


        if (selSpts.size() == 1)
        {
            // Single spt selection is special case: we try to paste all buffer values on next compatible song parts
            var allSpts = sgs.getSongParts();
            int sptIndex = allSpts.indexOf(selSpts.get(0));

            for (var itValue = values.iterator(); itValue.hasNext();)
            {
                SongPart spt = allSpts.get(sptIndex);


                var crp = RhythmParameter.findFirstCompatibleRp(spt.getRhythm().getRhythmParameters(), rp);
                if (crp != null)
                {
                    Object value = itValue.next();
                    Object newValue = crp.convertValue((RhythmParameter) rp, value);
                    sgs.setRhythmParameterValue(spt, (RhythmParameter) crp, newValue);
                }

                sptIndex++;
                if (sptIndex >= allSpts.size())
                {
                    break;
                }
            }

        } else
        {
            // Multple spt selection : we paste only on the selected song parts, cycling through the buffer values if required
            Iterator<Object> itValue = Iterables.cycle(values).iterator();
            for (var spt : selSpts)
            {
                var crp = RhythmParameter.findFirstCompatibleRp(spt.getRhythm().getRhythmParameters(), rp);
                if (crp != null)
                {
                    Object value = itValue.next();
                    Object newValue = crp.convertValue((RhythmParameter) rp, value);
                    sgs.setRhythmParameterValue(spt, (RhythmParameter) crp, newValue);
                }
            }
        }


        um.endCEdit(undoText);
    }

    @Override
    public void selectionChange(SS_SelectionUtilities selection)
    {
        RpValueCopyBuffer buffer = RpValueCopyBuffer.getInstance();
        boolean b = !getPastableSongParts(selection, buffer.getRhythm(), buffer.getRhythmParameter()).isEmpty();
        setEnabled(b);
    }


    // =======================================================================
    // ChangeListener interface
    // =======================================================================
    /**
     * Called when the RpValueCopyBuffer buffer has changed
     *
     * @param e
     */
    @Override
    public void stateChanged(ChangeEvent e)
    {
        SS_EditorTopComponent tc = SS_EditorTopComponent.getActive();
        if (tc != null)
        {
            SS_SelectionUtilities selection = new SS_SelectionUtilities(tc.getSS_Editor().getLookup());
            selectionChange(selection);
        }
    }


    // =======================================================================
    // Private methods
    // =======================================================================
    private List<SongPart> getPastableSongParts(SS_SelectionUtilities selection, Rhythm r, RhythmParameter<?> rp)
    {
        List<SongPart> res;

        if (selection.isRhythmParameterSelected())
        {
            res = selection.getSelectedSongPartParameters().stream()
                    .filter(spp -> spp.getRp().isCompatibleWith(rp))
                    .map(spp -> spp.getSpt())
                    .collect(Collectors.toList());
        } else
        {
            res = new ArrayList<>();
        }

        return res;
    }

}
