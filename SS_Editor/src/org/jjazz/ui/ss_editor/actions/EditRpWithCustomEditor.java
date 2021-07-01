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

import java.awt.Point;
import java.awt.Rectangle;
import org.jjazz.ui.ss_editor.api.SS_ContextActionSupport;
import java.awt.event.ActionEvent;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.Action;
import static javax.swing.Action.NAME;
import org.jjazz.rhythm.api.RhythmParameter;
import org.jjazz.ui.ss_editor.api.SS_SelectionUtilities;
import org.jjazz.songstructure.api.SongPartParameter;
import org.jjazz.undomanager.api.JJazzUndoManagerFinder;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.util.ContextAwareAction;
import org.openide.util.Lookup;
import org.openide.util.Utilities;
import org.jjazz.songstructure.api.SongStructure;
import org.jjazz.songstructure.api.SongPart;
import org.jjazz.ui.ss_editor.api.SS_ContextActionListener;
import org.jjazz.util.api.ResUtil;
import org.jjazz.rpcustomeditor.spi.RpCustomEditor;
import org.jjazz.rpcustomeditor.spi.RpCustomEditorProvider;
import org.jjazz.rpcustomeditor.api.RpCustomEditDialog;
import org.jjazz.ui.ss_editor.api.SS_Editor;
import org.jjazz.ui.ss_editor.api.SS_EditorTopComponent;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;

@ActionID(category = "JJazz", id = "org.jjazz.ui.ss_editor.actions.editrpwithcustomeditor")
@ActionRegistration(displayName = "#CTL_EditRhythmParameter", lazy = false)
@ActionReferences(
        {
            @ActionReference(path = "Actions/RhythmParameter", position = 10),
        })
public final class EditRpWithCustomEditor extends AbstractAction implements ContextAwareAction, SS_ContextActionListener
{

    private Lookup context;
    private SS_ContextActionSupport cap;
    private String undoText = ResUtil.getString(getClass(), "CTL_EditRhythmParameter");

    public EditRpWithCustomEditor()
    {
        this(Utilities.actionsGlobalContext());
    }

    public EditRpWithCustomEditor(Lookup context)
    {
        this.context = context;
        cap = SS_ContextActionSupport.getInstance(this.context);
        cap.addListener(this);
        putValue(NAME, undoText);                          // For popupmenu 
    }

    @SuppressWarnings(
            {
                "unchecked", "rawtypes"
            })
    @Override
    public void actionPerformed(ActionEvent e)
    {
        SS_SelectionUtilities selection = cap.getSelection();
        List<SongPartParameter> sptps = selection.getSelectedSongPartParameters();
        RhythmParameter<?> rp = sptps.get(0).getRp();
        SongPart spt = sptps.get(0).getSpt();


        if (rp instanceof RpCustomEditorProvider)
        {
            // Open custom editor if supported
            SS_Editor editor = SS_EditorTopComponent.getActive().getSS_Editor();


            // Prepare the CustomEditor
            RpCustomEditorProvider provider = (RpCustomEditorProvider) rp;
            RpCustomEditor rpEditor = provider.getCustomEditor();
            Object value = spt.getRPValue(rp);
            rpEditor.preset(value, spt);


            // Prepare our dialog
            RpCustomEditDialog dlg = RpCustomEditDialog.getInstance();
            dlg.preset(rpEditor, spt);
            Rectangle r = editor.getRpViewerRectangle(spt, rp);
            Point p = r.getLocation();
            int x = p.x - ((dlg.getWidth() - r.width) / 2);
            int y = p.y - dlg.getHeight();
            dlg.setLocation(Math.max(x, 0), Math.max(y, 0));
            dlg.setVisible(true);


            // Process edit result
            Object newValue = rpEditor.getEditedRpValue();
            if (dlg.isExitOk() && newValue != null && !newValue.equals(value))
            {
                SongStructure sgs = editor.getModel();
                JJazzUndoManagerFinder.getDefault().get(sgs).startCEdit(undoText);

                for (SongPartParameter sptp : sptps)
                {
                    sgs.setRhythmParameterValue(sptp.getSpt(), (RhythmParameter) sptp.getRp(), newValue);
                }

                JJazzUndoManagerFinder.getDefault().get(sgs).endCEdit(undoText);
            }
        } else
        {
            // Just highlight the SptEditor
            TopComponent tcSptEditor = WindowManager.getDefault().findTopComponent("SptEditorTopComponent");
            if (tcSptEditor != null)
            {
                tcSptEditor.requestVisible();
                tcSptEditor.requestAttention(true);
            }
        }

    }

    @Override
    public void selectionChange(SS_SelectionUtilities selection)
    {
        setEnabled(selection.isRhythmParameterSelected());
    }

    @Override
    public Action createContextAwareInstance(Lookup context)
    {
        return new EditRpWithCustomEditor(context);
    }

}
