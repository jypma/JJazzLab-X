package org.jjazz.songmemoviewer;

import java.awt.event.ActionEvent;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.logging.Logger;
import java.util.prefs.Preferences;
import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JRootPane;
import javax.swing.KeyStroke;
import org.jjazz.ui.utilities.Utilities;
import org.jjazz.util.ResUtil;
import org.openide.*;
import org.openide.util.Exceptions;
import org.openide.util.NbPreferences;

/**
 * Dialog to let user input a file or internet link.
 */
public class InsertLinkDialog extends javax.swing.JDialog
{

    private static final String PREF_LAST_DIR = "LastDirectory";
    private URL link = null;
    private static final Logger LOGGER = Logger.getLogger(InsertLinkDialog.class.getSimpleName());

    private static Preferences prefs = NbPreferences.forModule(InsertLinkDialog.class);

    /**
     * Creates new form InsertLinkDialog
     */
    public InsertLinkDialog(java.awt.Frame parent, boolean modal)
    {
        super(parent, modal);
        initComponents();

        Utilities.installSelectAllWhenFocused(textfield_internetLink);
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
                btn_insertActionPerformed(null);
            }
        });

        contentPane.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(KeyStroke.getKeyStroke("ESCAPE"), "actionCancel");   //NOI18N
        contentPane.getActionMap().put("actionCancel", new AbstractAction("Cancel")
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                btn_cancelActionPerformed(null);
            }
        });

        return contentPane;
    }

    /**
     * Get the link to insert.
     *
     * @return Null if user cancelled the dialog.
     */
    public URL getLink()
    {
        return link;
    }

    // ========================================================================================
    // Private methods
    // ========================================================================================    
    /**
     * Convert txt to URL.
     * <p>
     * Notify user if conversion could not be done.
     *
     * @param txt
     * @return Null if problem occured
     */
    private URL getURLorNotifyUser(String txt)
    {
        URL url = null;
        try
        {
            url = new URL(txt);
        } catch (MalformedURLException ex)
        {
            String msg = ResUtil.getString(getClass(), "InsertLinkDialog.ERR_InvalidURL");
            NotifyDescriptor d = new NotifyDescriptor.Message(msg, NotifyDescriptor.ERROR_MESSAGE);
            DialogDisplayer.getDefault().notify(d);
        }
        return url;
    }

    /**
     * Get the file from the specified parameter.
     * <p>
     * Notify user if it's empty or no file exists.
     *
     * @param txt
     * @return
     */
    private File getFileOrNotify(String txt)
    {
        File f = null;
        try
        {
            f = new File(new URI(txt));
        } catch (URISyntaxException | IllegalArgumentException ex)
        {
        }
        if (f == null || !f.exists())
        {
            String msg = ResUtil.getString(getClass(), "InsertLinkDialog.ERR_InvalidFile");
            NotifyDescriptor d = new NotifyDescriptor.Message(msg, NotifyDescriptor.ERROR_MESSAGE);
            DialogDisplayer.getDefault().notify(d);
            f = null;
        }

        return f;
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The content of
     * this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        btnGroup = new javax.swing.ButtonGroup();
        rbtn_file = new javax.swing.JRadioButton();
        rbtn_external = new javax.swing.JRadioButton();
        textfield_fileLink = new javax.swing.JTextField();
        btn_select = new javax.swing.JButton();
        textfield_internetLink = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        helpTextArea1 = new org.jjazz.ui.utilities.HelpTextArea();
        btn_cancel = new javax.swing.JButton();
        btn_insert = new javax.swing.JButton();
        btn_testLink = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle(org.openide.util.NbBundle.getBundle(InsertLinkDialog.class).getString("InsertLinkDialog.title")); // NOI18N

        btnGroup.add(rbtn_file);
        rbtn_file.setSelected(true);
        org.openide.awt.Mnemonics.setLocalizedText(rbtn_file, org.openide.util.NbBundle.getBundle(InsertLinkDialog.class).getString("InsertLinkDialog.rbtn_file.text")); // NOI18N
        rbtn_file.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                rbtn_fileActionPerformed(evt);
            }
        });

        btnGroup.add(rbtn_external);
        org.openide.awt.Mnemonics.setLocalizedText(rbtn_external, org.openide.util.NbBundle.getBundle(InsertLinkDialog.class).getString("InsertLinkDialog.rbtn_external.text")); // NOI18N
        rbtn_external.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                rbtn_externalActionPerformed(evt);
            }
        });

        textfield_fileLink.setEditable(false);

        org.openide.awt.Mnemonics.setLocalizedText(btn_select, org.openide.util.NbBundle.getBundle(InsertLinkDialog.class).getString("InsertLinkDialog.btn_select.text")); // NOI18N
        btn_select.setToolTipText(org.openide.util.NbBundle.getBundle(InsertLinkDialog.class).getString("InsertLinkDialog.btn_select.toolTipText")); // NOI18N
        btn_select.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btn_selectActionPerformed(evt);
            }
        });

        textfield_internetLink.setText("http://"); // NOI18N
        textfield_internetLink.setToolTipText(org.openide.util.NbBundle.getBundle(InsertLinkDialog.class).getString("InsertLinkDialog.textfield_internetLink.toolTipText")); // NOI18N
        textfield_internetLink.setEnabled(false);

        jScrollPane2.setBorder(null);

        helpTextArea1.setColumns(20);
        helpTextArea1.setRows(2);
        helpTextArea1.setText(org.openide.util.NbBundle.getBundle(InsertLinkDialog.class).getString("InsertLinkDialog.helpTextArea1.text")); // NOI18N
        jScrollPane2.setViewportView(helpTextArea1);

        org.openide.awt.Mnemonics.setLocalizedText(btn_cancel, org.openide.util.NbBundle.getMessage(InsertLinkDialog.class, "InsertLinkDialog.btn_cancel.text")); // NOI18N
        btn_cancel.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btn_cancelActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(btn_insert, org.openide.util.NbBundle.getBundle(InsertLinkDialog.class).getString("InsertLinkDialog.btn_insert.text")); // NOI18N
        btn_insert.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btn_insertActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(btn_testLink, org.openide.util.NbBundle.getBundle(InsertLinkDialog.class).getString("InsertLinkDialog.btn_testLink.text")); // NOI18N
        btn_testLink.setToolTipText(org.openide.util.NbBundle.getBundle(InsertLinkDialog.class).getString("InsertLinkDialog.btn_testLink.toolTipText")); // NOI18N
        btn_testLink.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btn_testLinkActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(textfield_internetLink)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 398, Short.MAX_VALUE)
                            .addComponent(textfield_fileLink))
                        .addGap(10, 10, 10)
                        .addComponent(btn_select))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(rbtn_external)
                            .addComponent(rbtn_file))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(btn_testLink)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btn_insert)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btn_cancel)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(rbtn_file)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(textfield_fileLink, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_select))
                .addGap(1, 1, 1)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(rbtn_external)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(textfield_internetLink, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 21, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_cancel)
                    .addComponent(btn_insert)
                    .addComponent(btn_testLink))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btn_insertActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btn_insertActionPerformed
    {//GEN-HEADEREND:event_btn_insertActionPerformed
        if (rbtn_file.isSelected())
        {
            File file = getFileOrNotify(textfield_fileLink.getText());
            if (file == null)
            {
                return;
            }
            try
            {
                link = file.toURI().toURL();
            } catch (MalformedURLException ex)
            {
                String msg = "Unexpected error! ex=" + ex.getMessage();
                LOGGER.severe("btn_insertActionPerformed() " + msg);
                NotifyDescriptor d = new NotifyDescriptor.Message(msg, NotifyDescriptor.ERROR_MESSAGE);
                DialogDisplayer.getDefault().notify(d);
                link = null;
                return;
            }
        } else if (rbtn_external.isSelected())
        {
            link = getURLorNotifyUser(textfield_internetLink.getText());
            if (link == null)
            {
                return;
            }
        }

        setVisible(false);

    }//GEN-LAST:event_btn_insertActionPerformed

    private void btn_cancelActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btn_cancelActionPerformed
    {//GEN-HEADEREND:event_btn_cancelActionPerformed
        link = null;
        setVisible(false);
    }//GEN-LAST:event_btn_cancelActionPerformed

    private void rbtn_fileActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_rbtn_fileActionPerformed
    {//GEN-HEADEREND:event_rbtn_fileActionPerformed
        textfield_fileLink.setEnabled(rbtn_file.isSelected());
        textfield_internetLink.setEnabled(!rbtn_file.isSelected());
        btn_select.setEnabled(rbtn_file.isSelected());
    }//GEN-LAST:event_rbtn_fileActionPerformed

    private void rbtn_externalActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_rbtn_externalActionPerformed
    {//GEN-HEADEREND:event_rbtn_externalActionPerformed
        rbtn_fileActionPerformed(evt);
    }//GEN-LAST:event_rbtn_externalActionPerformed

    private void btn_selectActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btn_selectActionPerformed
    {//GEN-HEADEREND:event_btn_selectActionPerformed
        JFileChooser chooser = Utilities.getFileChooserInstance();
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        chooser.setMultiSelectionEnabled(false);
        chooser.setDialogTitle(ResUtil.getString(getClass(), "InsertLinkDialog.SelectFile"));
        String strDir = prefs.get(PREF_LAST_DIR, null);
        File dir = strDir == null ? null : new File(strDir);
        if (dir != null && !dir.exists())
        {
            dir = null;
        }
        chooser.setCurrentDirectory(dir);       // If dir is null set user's home directory
        chooser.resetChoosableFileFilters();
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION)
        {
            File file = chooser.getSelectedFile();
            try
            {
                textfield_fileLink.setText(file.toURI().toURL().toString());
            } catch (MalformedURLException ex)
            {
                // Should never be there
                Exceptions.printStackTrace(ex);
                return;
            }
            prefs.put(PREF_LAST_DIR, file.getParent());
        }
    }//GEN-LAST:event_btn_selectActionPerformed

    private void btn_testLinkActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btn_testLinkActionPerformed
    {//GEN-HEADEREND:event_btn_testLinkActionPerformed
        if (rbtn_file.isSelected())
        {
            File file = getFileOrNotify(textfield_fileLink.getText());
            if (file != null)
            {
                org.jjazz.util.Utilities.openFile(file, true);
            }
        } else
        {
            URL url = getURLorNotifyUser(textfield_internetLink.getText());
            if (url != null)
            {
                org.jjazz.util.Utilities.openInBrowser(url, true);
            }
        }

    }//GEN-LAST:event_btn_testLinkActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup btnGroup;
    private javax.swing.JButton btn_cancel;
    private javax.swing.JButton btn_insert;
    private javax.swing.JButton btn_select;
    private javax.swing.JButton btn_testLink;
    private org.jjazz.ui.utilities.HelpTextArea helpTextArea1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JRadioButton rbtn_external;
    private javax.swing.JRadioButton rbtn_file;
    private javax.swing.JTextField textfield_fileLink;
    private javax.swing.JTextField textfield_internetLink;
    // End of variables declaration//GEN-END:variables
}
