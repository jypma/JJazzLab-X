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
package org.jjazz.helpers.midiwizard;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiUnavailableException;
import javax.swing.JPanel;
import org.jjazz.midi.JJazzMidiSystem;
import org.jjazz.musiccontrol.MusicController;
import org.jjazz.rhythmmusicgeneration.MusicGenerationException;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.jjazz.midi.ui.MidiOutDeviceList;

public final class MidiWizardVisualPanelSelectMidiOut extends JPanel
{

    /**
     * Creates new form StartupWizardVisualPanel3
     */
    public MidiWizardVisualPanelSelectMidiOut()
    {
        initComponents();
    }

    public MidiOutDeviceList getOutDeviceList()
    {
        return list_outDevices;
    }

    public MidiDevice getSelectedOutDevice()
    {
        return list_outDevices.getSelectedValue();
    }

    public void setSelectedOutDevice(MidiDevice md)
    {
        list_outDevices.setSelectedValue(md, true);
    }

    public void forceMidiOutListRefresh()
    {
        MidiDevice saveOutDevice = getSelectedOutDevice();
        list_outDevices.rescanMidiDevices();
        if (saveOutDevice != null)
        {
            list_outDevices.setSelectedValue(saveOutDevice, true);
        }
    }

    @Override
    public String getName()
    {
        return "Midi Out device";
    }

    /**
     * Send a few notes to the selected MIDI Out device.
     * <p>
     * Don't change the default JJazzMidiSystem default OUT device.
     */
    private void sendTestNotes()
    {
        MidiDevice selectedDeviceOut = getSelectedOutDevice();
        if (selectedDeviceOut == null)
        {
            String msg = "No Midi Out device selected";
            NotifyDescriptor d = new NotifyDescriptor.Message(msg, NotifyDescriptor.ERROR_MESSAGE);
            DialogDisplayer.getDefault().notify(d);
            return;
        }

        final JJazzMidiSystem jms = JJazzMidiSystem.getInstance();
        final MidiDevice saveDeviceOut = jms.getDefaultOutDevice();
        try
        {
            jms.setDefaultOutDevice(selectedDeviceOut);
        } catch (MidiUnavailableException ex)
        {
            NotifyDescriptor d = new NotifyDescriptor.Message(ex.getLocalizedMessage(), NotifyDescriptor.ERROR_MESSAGE);
            DialogDisplayer.getDefault().notify(d);
            return;
        }

        this.btn_test.setEnabled(false);
        this.list_outDevices.setEnabled(false);
        this.btn_refresh.setEnabled(false);
        Runnable endAction = new Runnable()
        {
            @Override
            public void run()
            {
                // Called when sequence is stopped
                btn_test.setEnabled(true);
                list_outDevices.setEnabled(true);
                btn_refresh.setEnabled(true);
                try
                {
                    jms.setDefaultOutDevice(saveDeviceOut);
                } catch (MidiUnavailableException ex)
                {
                    NotifyDescriptor d = new NotifyDescriptor.Message(ex.getLocalizedMessage(), NotifyDescriptor.ERROR_MESSAGE);
                    DialogDisplayer.getDefault().notify(d);
                }
            }
        };

        MusicController mc = MusicController.getInstance();
        try
        {
            mc.playTestNotes(0, -1, 0, endAction);
        } catch (MusicGenerationException ex)
        {
            NotifyDescriptor d = new NotifyDescriptor.Message(ex.getLocalizedMessage(), NotifyDescriptor.ERROR_MESSAGE);
            DialogDisplayer.getDefault().notify(d);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The content of
     * this method is always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        jScrollPane1 = new javax.swing.JScrollPane();
        list_outDevices = new org.jjazz.midi.ui.MidiOutDeviceList();
        lbl_top = new javax.swing.JLabel();
        btn_test = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        hlptxt = new org.jjazz.ui.utilities.HelpTextArea();
        btn_refresh = new javax.swing.JButton();

        jScrollPane1.setViewportView(list_outDevices);

        org.openide.awt.Mnemonics.setLocalizedText(lbl_top, org.openide.util.NbBundle.getMessage(MidiWizardVisualPanelSelectMidiOut.class, "MidiWizardVisualPanelSelectMidiOut.lbl_top.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(btn_test, org.openide.util.NbBundle.getMessage(MidiWizardVisualPanelSelectMidiOut.class, "MidiWizardVisualPanelSelectMidiOut.btn_test.text")); // NOI18N
        btn_test.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btn_testActionPerformed(evt);
            }
        });

        jScrollPane2.setBorder(null);

        hlptxt.setColumns(20);
        hlptxt.setRows(5);
        hlptxt.setText(org.openide.util.NbBundle.getMessage(MidiWizardVisualPanelSelectMidiOut.class, "MidiWizardVisualPanelSelectMidiOut.hlptxt.text")); // NOI18N
        jScrollPane2.setViewportView(hlptxt);

        org.openide.awt.Mnemonics.setLocalizedText(btn_refresh, org.openide.util.NbBundle.getMessage(MidiWizardVisualPanelSelectMidiOut.class, "MidiWizardVisualPanelSelectMidiOut.btn_refresh.text")); // NOI18N
        btn_refresh.setToolTipText(org.openide.util.NbBundle.getMessage(MidiWizardVisualPanelSelectMidiOut.class, "MidiWizardVisualPanelSelectMidiOut.btn_refresh.toolTipText")); // NOI18N
        btn_refresh.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btn_refreshActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(btn_test)
                                .addGap(0, 164, Short.MAX_VALUE))
                            .addComponent(jScrollPane2)))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btn_refresh)
                            .addComponent(lbl_top))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lbl_top)
                .addGap(21, 21, 21)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btn_test)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_refresh)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btn_refreshActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btn_refreshActionPerformed
    {//GEN-HEADEREND:event_btn_refreshActionPerformed
        forceMidiOutListRefresh();
    }//GEN-LAST:event_btn_refreshActionPerformed

    private void btn_testActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btn_testActionPerformed
    {//GEN-HEADEREND:event_btn_testActionPerformed
        sendTestNotes();
    }//GEN-LAST:event_btn_testActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_refresh;
    private javax.swing.JButton btn_test;
    private org.jjazz.ui.utilities.HelpTextArea hlptxt;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lbl_top;
    private org.jjazz.midi.ui.MidiOutDeviceList list_outDevices;
    // End of variables declaration//GEN-END:variables
}