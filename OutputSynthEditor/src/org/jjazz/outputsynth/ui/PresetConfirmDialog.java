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
package org.jjazz.outputsynth.ui;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JRootPane;
import javax.swing.KeyStroke;
import org.jjazz.util.api.ResUtil;

/**
 * A custom user confirmation dialog before applying an Output Synth preset.
 */
public class PresetConfirmDialog extends javax.swing.JDialog
{

    private boolean exitYes = false;

    /**
     * Creates new form PresetConfirmDialog
     */
    public PresetConfirmDialog(java.awt.Frame parent, boolean modal, String presetName, String helpText)
    {
        super(parent, modal);
        initComponents();
        lbl_title.setText(ResUtil.getString(getClass(),"CTL_ApplyPreset", presetName));
        if (helpText == null || helpText.isBlank())
        {
            getContentPane().remove(scrollPane_helpArea);
        } else
        {
            hlp_area.setText(helpText);
            hlp_area.setCaretPosition(0);
        }
        pack();
    }

    public boolean isExitYes()
    {
        return exitYes;
    }

    /**
     * Overridden to add global key bindings
     *
     * @return
     */
    @Override
    protected JRootPane createRootPane()
    {
        JRootPane contentPane = new JRootPane();
        contentPane.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(KeyStroke.getKeyStroke("ENTER"), "actionOk");   //NOI18N
        contentPane.getActionMap().put("actionOk", new AbstractAction("OK")
        {

            @Override
            public void actionPerformed(ActionEvent e)
            {
                btn_YesActionPerformed(null);
            }
        });

        contentPane.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(KeyStroke.getKeyStroke("ESCAPE"), "actionCancel");   //NOI18N
        contentPane.getActionMap().put("actionCancel", new AbstractAction("Cancel")
        {

            @Override
            public void actionPerformed(ActionEvent e)
            {
                btn_NoActionPerformed(null);
            }
        });
        return contentPane;
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The content of this
     * method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        lbl_image = new javax.swing.JLabel();
        scrollPane_helpArea = new javax.swing.JScrollPane();
        hlp_area = new org.jjazz.ui.utilities.api.HelpTextArea();
        btn_No = new javax.swing.JButton();
        btn_Yes = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        lbl_title = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle(org.openide.util.NbBundle.getMessage(PresetConfirmDialog.class, "PresetConfirmDialog.title")); // NOI18N

        lbl_image.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/jjazz/outputsynth/ui/resources/QuestionMarkBlueCircle.png"))); // NOI18N

        scrollPane_helpArea.setBorder(null);

        hlp_area.setColumns(20);
        hlp_area.setRows(4);
        hlp_area.setText("Sample"); // NOI18N
        scrollPane_helpArea.setViewportView(hlp_area);

        org.openide.awt.Mnemonics.setLocalizedText(btn_No, org.openide.util.NbBundle.getMessage(PresetConfirmDialog.class, "PresetConfirmDialog.btn_No.text")); // NOI18N
        btn_No.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btn_NoActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(btn_Yes, org.openide.util.NbBundle.getMessage(PresetConfirmDialog.class, "PresetConfirmDialog.btn_Yes.text")); // NOI18N
        btn_Yes.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btn_YesActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(jLabel1, org.openide.util.NbBundle.getMessage(PresetConfirmDialog.class, "PresetConfirmDialog.jLabel1.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(lbl_title, "Apply preset: ''xxx''"); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 384, Short.MAX_VALUE)
                    .addComponent(lbl_title, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lbl_title)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel1)
                .addContainerGap(21, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btn_Yes)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btn_No))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(lbl_image)
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(scrollPane_helpArea)
                            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {btn_No, btn_Yes});

        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addComponent(lbl_image))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(scrollPane_helpArea, javax.swing.GroupLayout.DEFAULT_SIZE, 78, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btn_Yes)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(btn_No)
                        .addContainerGap())))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btn_YesActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btn_YesActionPerformed
    {//GEN-HEADEREND:event_btn_YesActionPerformed
        exitYes = true;
        this.setVisible(false);
    }//GEN-LAST:event_btn_YesActionPerformed

    private void btn_NoActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btn_NoActionPerformed
    {//GEN-HEADEREND:event_btn_NoActionPerformed
        exitYes = false;
        this.setVisible(false);
    }//GEN-LAST:event_btn_NoActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_No;
    private javax.swing.JButton btn_Yes;
    private org.jjazz.ui.utilities.api.HelpTextArea hlp_area;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel lbl_image;
    private javax.swing.JLabel lbl_title;
    private javax.swing.JScrollPane scrollPane_helpArea;
    // End of variables declaration//GEN-END:variables
}
