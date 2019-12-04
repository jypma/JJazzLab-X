/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright @2019 Jerome Lelasseux. All rights reserved.
 *
 * This file is part of the JJazzLab-X software.
 *
 * JJazzLab-X is free software: you can redistribute it and/or modify
 * it under the terms of the Lesser GNU General Public License (LGPLv3) 
 * as published by the Free Software Foundation, either version 3 of the License, 
 * or (at your option) any later version.
 *
 * JJazzLab-X is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with JJazzLab-X.  If not, see <https://www.gnu.org/licenses/>
 *
 * Contributor(s): 
 *
 */
package org.jjazz.test;

import javax.sound.midi.Sequencer;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import org.jjazz.activesong.ActiveSongManager;
import org.jjazz.midi.JJazzMidiSystem;
import org.jjazz.song.api.Song;
import org.netbeans.api.settings.ConvertAsProperties;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.windows.TopComponent;
import org.openide.util.NbBundle.Messages;

/**
 * Top component which displays something.
 */
@ConvertAsProperties(
        dtd = "-//org.jjazz.test//TestOnTheFlySeqUpdate//EN",
        autostore = false
)
@TopComponent.Description(
        preferredID = "TestOnTheFlySeqUpdateTopComponent",
        //iconBase="SET/PATH/TO/ICON/HERE",
        persistenceType = TopComponent.PERSISTENCE_ALWAYS
)
@TopComponent.Registration(mode = "explorer", openAtStartup = true)
@ActionID(category = "Window", id = "org.jjazz.test.TestOnTheFlySeqUpdateTopComponent")
@ActionReference(path = "Menu/Window" /*, position = 333 */)
@TopComponent.OpenActionRegistration(
        displayName = "#CTL_TestOnTheFlySeqUpdateAction",
        preferredID = "TestOnTheFlySeqUpdateTopComponent"
)
@Messages(
        {
            "CTL_TestOnTheFlySeqUpdateAction=TestOnTheFlySeqUpdate",
            "CTL_TestOnTheFlySeqUpdateTopComponent=TestOnTheFlySeqUpdate Window",
            "HINT_TestOnTheFlySeqUpdateTopComponent=This is a TestOnTheFlySeqUpdate window"
        })
public final class TestOnTheFlySeqUpdateTopComponent extends TopComponent
{    

    private Context context;
    private DefaultListModel<Integer> listModel = new DefaultListModel<>();
    
    public TestOnTheFlySeqUpdateTopComponent()
    {
        initComponents();
        setName(Bundle.CTL_TestOnTheFlySeqUpdateTopComponent());
        setToolTipText(Bundle.HINT_TestOnTheFlySeqUpdateTopComponent());
        putClientProperty(TopComponent.PROP_MAXIMIZATION_DISABLED, Boolean.TRUE);
        putClientProperty(TopComponent.PROP_CLOSING_DISABLED, Boolean.TRUE);
        putClientProperty(TopComponent.PROP_KEEP_PREFERRED_SIZE_WHEN_SLIDED_IN, Boolean.TRUE);
        putClientProperty(TopComponent.PROP_DND_COPY_DISABLED, Boolean.TRUE);
        putClientProperty(TopComponent.PROP_DRAGGING_DISABLED, Boolean.TRUE);
        putClientProperty(TopComponent.PROP_UNDOCKING_DISABLED, Boolean.FALSE);
        putClientProperty(TopComponent.PROP_SLIDING_DISABLED, Boolean.FALSE);        
    }

    /** This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The content of
     * this method is always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        jScrollPane1 = new javax.swing.JScrollPane();
        list_spts = new JList(listModel);
        btn_init = new javax.swing.JButton();
        btn_play = new javax.swing.JButton();
        btn_stop = new javax.swing.JButton();

        list_spts.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane1.setViewportView(list_spts);

        org.openide.awt.Mnemonics.setLocalizedText(btn_init, org.openide.util.NbBundle.getMessage(TestOnTheFlySeqUpdateTopComponent.class, "TestOnTheFlySeqUpdateTopComponent.btn_init.text")); // NOI18N
        btn_init.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btn_initActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(btn_play, org.openide.util.NbBundle.getMessage(TestOnTheFlySeqUpdateTopComponent.class, "TestOnTheFlySeqUpdateTopComponent.btn_play.text")); // NOI18N
        btn_play.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btn_playActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(btn_stop, org.openide.util.NbBundle.getMessage(TestOnTheFlySeqUpdateTopComponent.class, "TestOnTheFlySeqUpdateTopComponent.btn_stop.text")); // NOI18N
        btn_stop.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btn_stopActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btn_init)
                    .addComponent(btn_play)
                    .addComponent(btn_stop))
                .addContainerGap(167, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btn_init)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btn_play)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn_stop))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(138, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btn_initActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btn_initActionPerformed
    {//GEN-HEADEREND:event_btn_initActionPerformed
        ActiveSongManager asm = ActiveSongManager.getInstance();
        Song song = asm.getActiveSong();
        if (song == null)
        {
            context = null;
            return;
        }
        
        context = new Context(song);

        // Init list
        listModel.removeAllElements();
        for (int i = 0; i < song.getSongStructure().getSongParts().size(); i++)
        {
            listModel.add(i, Integer.valueOf(i));
        }
        list_spts.setSelectedIndex(0);
        
    }//GEN-LAST:event_btn_initActionPerformed

    private void btn_playActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btn_playActionPerformed
    {//GEN-HEADEREND:event_btn_playActionPerformed
        Sequencer sequencer = JJazzMidiSystem.getInstance().getDefaultSequencer();
        sequencer.start();
        
    }//GEN-LAST:event_btn_playActionPerformed

    private void btn_stopActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btn_stopActionPerformed
    {//GEN-HEADEREND:event_btn_stopActionPerformed
        Sequencer sequencer = JJazzMidiSystem.getInstance().getDefaultSequencer();
        sequencer.stop();
        sequencer.setTickPosition(0);
    }//GEN-LAST:event_btn_stopActionPerformed
    
    private class Context
    {
        
        private Song song;
        
        public Context(Song song)
        {
            this.song = song;
        }
        
        public Song getSong()
        {
            return song;
        }
        
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_init;
    private javax.swing.JButton btn_play;
    private javax.swing.JButton btn_stop;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JList<Integer> list_spts;
    // End of variables declaration//GEN-END:variables
    @Override
    public void componentOpened()
    {
        // TODO add custom code on component opening
    }
    
    @Override
    public void componentClosed()
    {
        // TODO add custom code on component closing
    }
    
    void writeProperties(java.util.Properties p)
    {
        // better to version settings since initial version as advocated at
        // http://wiki.apidesign.org/wiki/PropertyFiles
        p.setProperty("version", "1.0");
        // TODO store your settings
    }
    
    void readProperties(java.util.Properties p)
    {
        String version = p.getProperty("version");
        // TODO read your settings according to their version
    }
    
}
