/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.att.fk9424.jedittable.util.table.menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JMenuItem;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

/**
 * this menu item is part to the AddEditDelPopupMenu for JTable. it is used to 
 * create an Editor, linked with a Table Model. 
 * @author fk9424
 */
public class DelAllRowsMenuItem extends JMenuItem implements TableModelListener {
    
    public DelAllRowsMenuItem(String title, final DelPopUpMenu menu){
        super(title);
        this.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                menu.fireRowDeletingAll();
            }
            
        });
    }
    @Override
    public void tableChanged(TableModelEvent e) {
        TableModel source = (TableModel)e.getSource();
        if ((source.getRowCount() == 1) && (source.getValueAt(0, 0).equals(""))){
            // we have one row withoutdata
            this.setEnabled(false);
        }else{
            this.setEnabled(true);
        }
    }
}
