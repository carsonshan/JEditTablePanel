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
public class DelAllAlertMenuItem extends JMenuItem implements ActionListener {
    private TablePopUpMenu menu;
    
    public DelAllAlertMenuItem(String title, TablePopUpMenu menu){
        super(title);
        this.menu = menu;
        this.setEnabled(false);
        this.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        menu.fireAlertAllRowDeleting();
    }
}