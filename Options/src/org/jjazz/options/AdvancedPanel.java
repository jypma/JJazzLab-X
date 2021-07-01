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
package org.jjazz.options;

import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;
import javax.swing.JComboBox;
import org.jjazz.analytics.api.Analytics;
import org.jjazz.base.api.actions.ShowLogWindow;
import org.jjazz.midi.api.JJazzMidiSystem;
import org.jjazz.midi.api.device.MidiFilter;
import org.jjazz.musiccontrol.api.MusicController;
import org.jjazz.ui.utilities.api.Utilities;
import org.jjazz.util.api.ResUtil;
import org.openide.DialogDisplayer;
import org.openide.LifecycleManager;
import org.openide.NotifyDescriptor;
import org.openide.modules.OnStop;
import org.openide.util.Exceptions;
import org.openide.util.NbPreferences;

public final class AdvancedPanel extends javax.swing.JPanel
{

    private static final Level[] ALL_LEVELS = new Level[]
    {
        Level.ALL, Level.FINEST, Level.FINER, Level.FINE, Level.CONFIG, Level.INFO, Level.WARNING, Level.SEVERE, Level.OFF
    };
    private final AdvancedOptionsPanelController controller;
    private static final Logger LOGGER = Logger.getLogger(AdvancedPanel.class.getSimpleName());

    AdvancedPanel(AdvancedOptionsPanelController controller)
    {
        this.controller = controller;

        initComponents();

        Utilities.installSelectAllWhenFocused(tf_loggerName);

        // TODO listen to changes in form fields and call controller.changed()

    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The content of
     * this method is always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        panel_Debug = new javax.swing.JPanel();
        btn_showLog = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        btn_setLogger = new javax.swing.JButton();
        cb_loggerLevel = new JComboBox<>(ALL_LEVELS);
        tf_loggerName = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        cb_logMidiOut = new javax.swing.JCheckBox();
        cb_debugBuiltSequence = new javax.swing.JCheckBox();
        btn_resetSettings = new javax.swing.JButton();
        cb_noAnalytics = new javax.swing.JCheckBox();
        jScrollPane1 = new javax.swing.JScrollPane();
        helpTextArea = new org.jjazz.ui.utilities.api.HelpTextArea();

        panel_Debug.setBorder(javax.swing.BorderFactory.createTitledBorder(org.openide.util.NbBundle.getMessage(AdvancedPanel.class, "AdvancedPanel.panel_Debug.border.title"))); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(btn_showLog, org.openide.util.NbBundle.getMessage(AdvancedPanel.class, "AdvancedPanel.btn_showLog.text")); // NOI18N
        btn_showLog.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btn_showLogActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(jLabel1, org.openide.util.NbBundle.getMessage(AdvancedPanel.class, "AdvancedPanel.jLabel1.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(btn_setLogger, org.openide.util.NbBundle.getMessage(AdvancedPanel.class, "AdvancedPanel.btn_setLogger.text")); // NOI18N
        btn_setLogger.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btn_setLoggerActionPerformed(evt);
            }
        });

        tf_loggerName.setText(org.openide.util.NbBundle.getMessage(AdvancedPanel.class, "AdvancedPanel.tf_loggerName.text")); // NOI18N
        tf_loggerName.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                tf_loggerNameActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(112, 112, 112))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(tf_loggerName, javax.swing.GroupLayout.DEFAULT_SIZE, 222, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cb_loggerLevel, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                .addComponent(btn_setLogger)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cb_loggerLevel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_setLogger)
                    .addComponent(tf_loggerName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        org.openide.awt.Mnemonics.setLocalizedText(cb_logMidiOut, org.openide.util.NbBundle.getMessage(AdvancedPanel.class, "AdvancedPanel.cb_logMidiOut.text")); // NOI18N
        cb_logMidiOut.setToolTipText(org.openide.util.NbBundle.getMessage(AdvancedPanel.class, "AdvancedPanel.cb_logMidiOut.toolTipText")); // NOI18N
        cb_logMidiOut.addChangeListener(new javax.swing.event.ChangeListener()
        {
            public void stateChanged(javax.swing.event.ChangeEvent evt)
            {
                cb_logMidiOutStateChanged(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(cb_debugBuiltSequence, org.openide.util.NbBundle.getMessage(AdvancedPanel.class, "AdvancedPanel.cb_debugBuiltSequence.text")); // NOI18N

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cb_debugBuiltSequence)
                    .addComponent(cb_logMidiOut))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(cb_logMidiOut)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(cb_debugBuiltSequence)
                .addGap(12, 12, 12))
        );

        javax.swing.GroupLayout panel_DebugLayout = new javax.swing.GroupLayout(panel_Debug);
        panel_Debug.setLayout(panel_DebugLayout);
        panel_DebugLayout.setHorizontalGroup(
            panel_DebugLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_DebugLayout.createSequentialGroup()
                .addGroup(panel_DebugLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panel_DebugLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(btn_showLog)
                        .addGap(208, 208, 208)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panel_DebugLayout.setVerticalGroup(
            panel_DebugLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_DebugLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panel_DebugLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panel_DebugLayout.createSequentialGroup()
                        .addComponent(btn_showLog)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        org.openide.awt.Mnemonics.setLocalizedText(btn_resetSettings, org.openide.util.NbBundle.getMessage(AdvancedPanel.class, "AdvancedPanel.btn_resetSettings.text")); // NOI18N
        btn_resetSettings.setToolTipText(org.openide.util.NbBundle.getMessage(AdvancedPanel.class, "AdvancedPanel.btn_resetSettings.toolTipText")); // NOI18N
        btn_resetSettings.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btn_resetSettingsActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(cb_noAnalytics, ResUtil.getString(getClass(),"AdvancedPanel.cb_noAnalytics.text", new Object[] {})); // NOI18N
        cb_noAnalytics.setToolTipText(ResUtil.getString(getClass(),"AdvancedPanel.cb_noAnalytics.toolTipText", new Object[] {})); // NOI18N

        jScrollPane1.setBackground(null);
        jScrollPane1.setBorder(null);

        helpTextArea.setBackground(null);
        helpTextArea.setColumns(20);
        helpTextArea.setRows(5);
        helpTextArea.setText(ResUtil.getString(getClass(),"AdvancedPanel.helpTextArea.text", new Object[] {})); // NOI18N
        jScrollPane1.setViewportView(helpTextArea);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panel_Debug, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btn_resetSettings)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(cb_noAnalytics)
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane1)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btn_resetSettings)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(cb_noAnalytics)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 73, Short.MAX_VALUE)
                        .addComponent(panel_Debug, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btn_showLogActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btn_showLogActionPerformed
    {//GEN-HEADEREND:event_btn_showLogActionPerformed
        ShowLogWindow.actionPerformed();
    }//GEN-LAST:event_btn_showLogActionPerformed

    private void btn_setLoggerActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btn_setLoggerActionPerformed
    {//GEN-HEADEREND:event_btn_setLoggerActionPerformed
        Level level = (Level) cb_loggerLevel.getSelectedItem();
        String name = tf_loggerName.getText().trim();
        if (name.isBlank())
        {
            return;
        }
        Logger logger = LogManager.getLogManager().getLogger(name);
        if (logger != null)
        {
            logger.setLevel(level);
            String msg = ResUtil.getString(getClass(), "CTL_LoggerLevelSet", name, level.toString());
            NotifyDescriptor d = new NotifyDescriptor.Message(msg, NotifyDescriptor.INFORMATION_MESSAGE);
            DialogDisplayer.getDefault().notify(d);
        } else
        {
            String msg = ResUtil.getString(getClass(), "ERR_NoLoggerFound", name);
            NotifyDescriptor d = new NotifyDescriptor.Message(msg, NotifyDescriptor.ERROR_MESSAGE);
            DialogDisplayer.getDefault().notify(d);
        }
    }//GEN-LAST:event_btn_setLoggerActionPerformed

    private void cb_logMidiOutStateChanged(javax.swing.event.ChangeEvent evt)//GEN-FIRST:event_cb_logMidiOutStateChanged
    {//GEN-HEADEREND:event_cb_logMidiOutStateChanged
        controller.changed();
    }//GEN-LAST:event_cb_logMidiOutStateChanged

    private void btn_resetSettingsActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btn_resetSettingsActionPerformed
    {//GEN-HEADEREND:event_btn_resetSettingsActionPerformed

        String msg = ResUtil.getString(getClass(), "CTL_ConfirmUserSettingsRestart");
        NotifyDescriptor d = new NotifyDescriptor.Confirmation(msg, NotifyDescriptor.OK_CANCEL_OPTION);
        Object result = DialogDisplayer.getDefault().notify(d);
        if (NotifyDescriptor.OK_OPTION == result)
        {
            DeleteUserSettingsTask.doIt = true;
            LifecycleManager.getDefault().markForRestart();
            LifecycleManager.getDefault().exit();
        }

    }//GEN-LAST:event_btn_resetSettingsActionPerformed

    private void tf_loggerNameActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_tf_loggerNameActionPerformed
    {//GEN-HEADEREND:event_tf_loggerNameActionPerformed

        String name = tf_loggerName.getText().trim();
        if (name.isBlank())
        {
            return;
        }
        Logger logger = LogManager.getLogManager().getLogger(name);
        if (logger != null)
        {
            cb_loggerLevel.setSelectedItem(logger.getLevel());
        } else
        {
            String msg = ResUtil.getString(getClass(), "ERR_NoLoggerFound", name);
            NotifyDescriptor d = new NotifyDescriptor.Message(msg, NotifyDescriptor.ERROR_MESSAGE);
            DialogDisplayer.getDefault().notify(d);
        }

//        for (var it = LogManager.getLogManager().getLoggerNames().asIterator(); it.hasNext();)
//        {
//            LOGGER.severe("Logger=" + it.next());
//        }
    }//GEN-LAST:event_tf_loggerNameActionPerformed

    void load()
    {
        // TODO read settings and initialize GUI
        // Example:        
        // someCheckBox.setSelected(Preferences.userNodeForPackage(AdvancedPanel.class).getBoolean("someFlag", false));
        // or for org.openide.util with API spec. version >= 7.4:
        // someCheckBox.setSelected(NbPreferences.forModule(AdvancedPanel.class).getBoolean("someFlag", false));
        // or:
        // someTextField.setText(SomeSystemOption.getDefault().getSomeStringProperty());
        cb_logMidiOut.setSelected(JJazzMidiSystem.getInstance().getMidiOutLogConfig().contains(MidiFilter.ConfigLog.LOG_PASSED_MESSAGES));
        cb_debugBuiltSequence.setSelected(MusicController.getInstance().isDebugBuiltSequence());
        cb_noAnalytics.setSelected(!Analytics.getInstance().isEnabled());

    }

    void store()
    {
        // TODO store modified settings
        // Example:
        // Preferences.userNodeForPackage(AdvancedPanel.class).putBoolean("someFlag", someCheckBox.isSelected());
        // or for org.openide.util with API spec. version >= 7.4:
        // NbPreferences.forModule(AdvancedPanel.class).putBoolean("someFlag", someCheckBox.isSelected());
        // or:
        // SomeSystemOption.getDefault().setSomeStringProperty(someTextField.getText());
        if (cb_logMidiOut.isSelected())
        {
            JJazzMidiSystem.getInstance().getMidiOutLogConfig().add(MidiFilter.ConfigLog.LOG_PASSED_MESSAGES);
        } else
        {
            JJazzMidiSystem.getInstance().getMidiOutLogConfig().remove(MidiFilter.ConfigLog.LOG_PASSED_MESSAGES);
        }
        MusicController.getInstance().setDebugBuiltSequence(cb_debugBuiltSequence.isSelected());

        Analytics.getInstance().setEnabled(!cb_noAnalytics.isSelected());
    }

    boolean valid()
    {
        // TODO check whether form is consistent and complete
        return true;
    }

    // ========================================================================================================
    // Private classes
    // ========================================================================================================
    /**
     * The shutdown class to remove the user preferences IF doIt was previously set to true.
     */
    @OnStop
    static public class DeleteUserSettingsTask implements Runnable
    {

        static boolean doIt = false;          // Set to true 

        @Override
        public void run()
        {
            if (doIt)
            {
                Preferences rootPrefs = NbPreferences.root();
                try
                {
                    if (rootPrefs.nodeExists("org"))
                    {
                        rootPrefs.node("org").removeNode();
                        rootPrefs.flush();
                    }

                } catch (BackingStoreException ex)
                {
                    Exceptions.printStackTrace(ex);
                }
            }
        }

    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_resetSettings;
    private javax.swing.JButton btn_setLogger;
    private javax.swing.JButton btn_showLog;
    private javax.swing.JCheckBox cb_debugBuiltSequence;
    private javax.swing.JCheckBox cb_logMidiOut;
    private javax.swing.JComboBox<Level> cb_loggerLevel;
    private javax.swing.JCheckBox cb_noAnalytics;
    private org.jjazz.ui.utilities.api.HelpTextArea helpTextArea;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPanel panel_Debug;
    private javax.swing.JTextField tf_loggerName;
    // End of variables declaration//GEN-END:variables
}
