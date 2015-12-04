/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.att.fk9424.jedittable.util.table.menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JMenuItem;

/**
 * this menu item is part to the AddEditDelPopupMenu for JTable. it is used to 
 * create an Editor, linked with a Table Model. 
 * @author fk9424
 */
public class DelAllRowsMenuItem extends JMenuItem {
    
    public DelAllRowsMenuItem(String title, final TablePopUpMenu menu){
        super(title);
        this.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                menu.fireRowDeletingAll();
            }
            
        });
    }
}
