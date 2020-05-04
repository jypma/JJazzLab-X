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

import java.awt.event.ActionEvent;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import org.jjazz.rhythm.api.Rhythm;
import static org.jjazz.ui.ss_editor.actions.Bundle.*;
import org.jjazz.ui.ss_editor.api.SS_Editor;
import org.openide.util.NbBundle;
import org.openide.windows.WindowManager;
import org.jjazz.songstructure.api.SongStructure;

//@ActionID(category = "JJazz", id = "org.jjazz.ui.ss_editor.actions.showhiderps")
//@ActionRegistration(displayName = "#CTL_ShowHideRp", lazy=false) // lazy=false to have the tooltip defined
//@ActionReferences(
//        {
////            @ActionReference(path = "Actions/SongPart", position = 3000, separatorBefore = 2990),
////            @ActionReference(path = "Actions/RhythmParameter", position = 3000, separatorBefore = 2990),
////            @ActionReference(path = "Actions/SS_Editor", position = 3000, separatorBefore = 2990),
//            @ActionReference(path = "Actions/SS_EditorToolBar", position = 200, separatorAfter = 201)
//        })

/**
 * The action can't be instanciated declaratively because there must be one action per editor and action is stateful.
 */
@NbBundle.Messages(
        {
            "CTL_ShowHideRp=Parameters",
            "DESC_ShowHideRp=Show/Hide Parameters"
        })
public class ShowHideRp extends AbstractAction
{

    private static final Logger LOGGER = Logger.getLogger(ShowHideRp.class.getSimpleName());
    private static final ImageIcon ICON = new ImageIcon(ShowHideRp.class.getResource("/org/jjazz/ui/ss_editor/actions/resources/VisibleRps.png"));
    private static final ImageIcon ICON_BIS = new ImageIcon(ShowHideRp.class.getResource("/org/jjazz/ui/ss_editor/actions/resources/VisibleRpsBis.png"));
    private SS_Editor editor;

    public ShowHideRp(SS_Editor editor)
    {
        if (editor == null)
        {
            throw new NullPointerException("editor");
        }
        this.editor = editor;
        this.editor.addPropertyChangeListener(SS_Editor.PROP_VISIBLE_RPS, evt -> updateIcon());

        putValue("hideActionText", true);
        putValue(NAME, CTL_ShowHideRp());
        putValue(SHORT_DESCRIPTION, Bundle.DESC_ShowHideRp());
        updateIcon();

        // Maintain the action disabled when no song part 
        this.editor.getSongModel().getSongStructure().addSgsChangeListener(evt ->
        {
            setEnabled(!evt.getSource().getSongParts().isEmpty());
        });
        setEnabled(!editor.getSongModel().getSongStructure().getSongParts().isEmpty());


    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        LOGGER.log(Level.FINE, "actionPerformed()");
        ShowHideRpsDialog dlg = ShowHideRpsDialog.getInstance();
        dlg.setModel(editor);
        dlg.setLocationRelativeTo(WindowManager.getDefault().getMainWindow());
        dlg.setVisible(true);
        if (dlg.isExitOk())
        {
            var res = dlg.getResult();
            for (Rhythm r : res.keySet())
            {
                editor.setVisibleRps(r, res.get(r));
            }
        }
    }

    private int getNbHiddenParameters()
    {
        int res = 0;
        for (Rhythm r : SongStructure.getUniqueRhythms(editor.getModel()))
        {
            res = Math.max(res, r.getRhythmParameters().size() - editor.getVisibleRps(r).size());
        }
        return res;
    }

    private void updateIcon()
    {
        int hidden = getNbHiddenParameters();
        if (hidden > 0)
        {
            putValue(SMALL_ICON, ICON_BIS);
            putValue(SHORT_DESCRIPTION, Bundle.DESC_ShowHideRp() + " (" + hidden + " hidden parameters)");
        } else
        {
            putValue(SMALL_ICON, ICON);
            putValue(SHORT_DESCRIPTION, Bundle.DESC_ShowHideRp());
        }
    }

}
