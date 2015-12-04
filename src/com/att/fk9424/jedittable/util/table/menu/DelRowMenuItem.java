/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.att.fk9424.jedittable.util.table.menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JMenuItem;

/**
 *
 * @author fk9424
 */
public class DelRowMenuItem extends JMenuItem {
    
    public DelRowMenuItem(String title, final TablePopUpMenu menu){
        super(title);
        this.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                menu.fireRowDeleting();
            }
            
        });
    }
}