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
package org.jjazz.ui.zoomablesliders;

import java.awt.Component;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.logging.Logger;
import static org.jjazz.ui.flatcomponents.api.FlatIntegerHorizontalSlider.PROP_HIDE_VALUE;
import static org.jjazz.ui.flatcomponents.api.FlatIntegerHorizontalSlider.PROP_NB_GRADUATION_MARKS;
import org.jjazz.ui.utilities.api.Zoomable;
import org.openide.awt.StatusLineElementProvider;
import org.openide.util.*;
import org.openide.util.lookup.ServiceProvider;

/**
 * A status bar Zoom Y component.
 */
@ServiceProvider(service = StatusLineElementProvider.class, position = 105)
public class ZoomYWidget extends javax.swing.JPanel implements StatusLineElementProvider, PropertyChangeListener
{

    private Lookup context;
    private Zoomable currentZoomable;
    private Lookup.Result<Zoomable> lkpResult;
    private LookupListener lkpListener;
    private static final Logger LOGGER = Logger.getLogger(ZoomYWidget.class.getSimpleName());

    public ZoomYWidget()
    {
        initComponents();

        // Listen to Zoomable object in the lookup
        context = org.openide.util.Utilities.actionsGlobalContext();
        lkpResult = context.lookupResult(Zoomable.class);
        // For WeakReferences to work, we need to keep a strong reference on the listeners (see WeakListeners java doc).
        lkpListener = new LookupListener()
        {
            @Override
            public void resultChanged(LookupEvent le)
            {
                zoomableUpdated();
            }
        };
        // Need to use WeakListeners so than action can be GC'ed
        // See http://forums.netbeans.org/viewtopic.php?t=35921
        lkpResult.addLookupListener(WeakListeners.create(LookupListener.class, lkpListener, lkpResult));

        setEnabled(false);
    }

    @Override
    public void setEnabled(boolean b)
    {
        super.setEnabled(b);
        label.setEnabled(b);
        slider.setEnabled(b);
    }

    @Override
    public Component getStatusLineElement()
    {
        return this;
    }

    private void zoomableUpdated()
    {
        if (currentZoomable != null)
        {
            currentZoomable.removePropertyListener(this);
        }
        currentZoomable = context.lookup(Zoomable.class);
        if (currentZoomable != null && currentZoomable.getZoomCapabilities().equals(Zoomable.Capabilities.X_ONLY))
        {
            currentZoomable = null;
        }
        if (currentZoomable != null)
        {
            currentZoomable.addPropertyListener(this);
            int y = currentZoomable.getZoomYFactor();
            slider.setValue(y);
            slider.setToolTipText(String.valueOf(y));
        }
        setEnabled(currentZoomable != null);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt)
    {
        if (evt.getPropertyName() == Zoomable.PROPERTY_ZOOM_Y)
        {
            int newFactor = (int) evt.getNewValue();
            if (newFactor < 0 || newFactor > 100)
            {
                throw new IllegalStateException("factor=" + newFactor);   //NOI18N
            }
            slider.setValue(newFactor);
            slider.setToolTipText(String.valueOf(newFactor));

        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * <p>
     * WARNING: Do NOT modify this code. The content of this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        label = new javax.swing.JLabel();
        slider = new org.jjazz.ui.flatcomponents.api.FlatIntegerHorizontalSlider();
        slider.putClientProperty(PROP_NB_GRADUATION_MARKS, 0);
        slider.putClientProperty(PROP_HIDE_VALUE, 1);

        setLayout(new java.awt.FlowLayout(1, 0, 0));

        label.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/jjazz/ui/zoomablesliders/resources/zoomYarrow.png"))); // NOI18N
        label.setToolTipText(org.openide.util.NbBundle.getMessage(ZoomYWidget.class, "ZoomYWidget.label.toolTipText")); // NOI18N
        add(label);

        slider.setFaderHeight(4);
        slider.setKnobDiameter(10);
        slider.setMaxValue(100);
        slider.setValue(50);
        slider.addPropertyChangeListener(new java.beans.PropertyChangeListener()
        {
            public void propertyChange(java.beans.PropertyChangeEvent evt)
            {
                sliderPropertyChange(evt);
            }
        });
        add(slider);
    }// </editor-fold>//GEN-END:initComponents

   private void sliderPropertyChange(java.beans.PropertyChangeEvent evt)//GEN-FIRST:event_sliderPropertyChange
   {//GEN-HEADEREND:event_sliderPropertyChange
       if (currentZoomable == null)
       {
           return;
       }
       int newValue = slider.getValue();
       currentZoomable.setZoomYFactor(newValue);
   }//GEN-LAST:event_sliderPropertyChange

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel label;
    private org.jjazz.ui.flatcomponents.api.FlatIntegerHorizontalSlider slider;
    // End of variables declaration//GEN-END:variables
}
