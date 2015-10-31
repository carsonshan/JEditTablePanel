/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.att.fk9424.jedittable.util.table;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableRowSorter;
import com.att.fk9424.jedittable.model.EditTableModel;
import com.att.fk9424.jedittable.util.table.menu.DelPopUpMenu;

/**
 *
 * @author fk9424
 */
public class TablePanel extends JPanel {
    private JTable theTable;
    private EditTableModel theModel;
    private TableRowSorter sorter;
    private ResourceBundle label = ResourceBundle.getBundle("view/labels/ButtonLabels", Locale.getDefault());
    
    
    public TablePanel(String[] columnNames, Class[] columnTypes, Preferences prefs){
        this.setLayout(new BorderLayout());
        theModel = new EditTableModel(columnNames, columnTypes);
        theModel.addTableModelListener(theModel);
        theModel.init(prefs);
        sorter = new TableRowSorter<EditTableModel>(theModel);
        this.theTable = new JTable();
        theTable.setRowSelectionAllowed(true);
        theTable.setColumnSelectionAllowed(false);
        theTable.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        theTable.setRowSorter(sorter);
        theTable.setModel(theModel);
        theTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
    }
    /**
     * 
     * @return TableRowSorter use with this table
     */
    public TableRowSorter getSorter(){
        return this.sorter;
    }
    /**
     * 
     * @param data 
     */
    public void helperSetAllRows(ArrayList<Object[]> data){
        this.theModel.setDataRows(data);
    }
    /**
     * Helper to access the model for setting editable column.
     * @param col 
     */
    public void helperSetNotEditAble(int col){
        this.theModel.setNotEditableCol(col);
    }
    public void helperSetDefaultCellValue(Object value, int col){
        this.theModel.setDefaultCellValue(value, col);
    }
    /**
     * 
     * @return 
     */
    public JComponent createPanel(boolean buttonRow){
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.add(getTheTable().getTableHeader(), BorderLayout.NORTH);
        tablePanel.add(new JScrollPane(getTheTable()), BorderLayout.CENTER);
        Font butFont = new Font("Arial", 0, 9);
        JPanel buttonPanel = new JPanel();
        if (buttonRow){
            buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.LINE_AXIS));
            JButton addRow = new JButton(label.getString("BUTAROW"));
            addRow.setFont(butFont);
            addRow.addActionListener(new ActionListener(){

                @Override
                public void actionPerformed(ActionEvent e) {
                    getTheModel().addNewRow();
                }            
            });
            buttonPanel.add(Box.createHorizontalGlue());
            buttonPanel.add(addRow);
            add(buttonPanel, BorderLayout.NORTH);
        }
        add(tablePanel, BorderLayout.CENTER);
        return this;
    }
    /**
     *  setup the popup menu for the table to enable delete and delete all rows
     * @param frame 
     */
    public void setDelMenu(Window frameOrDialog){
        DelPopUpMenu pMenu = new DelPopUpMenu(theTable, frameOrDialog);
        pMenu.initMenu();
        pMenu.addTableMenuListener(theModel);        
    }
    /**
     * @return the theTable
     */
    public JTable getTheTable() {
        return theTable;
    }

    /**
     * @return the theModel
     */
    public EditTableModel getTheModel() {
        return theModel;
    }
    
}
