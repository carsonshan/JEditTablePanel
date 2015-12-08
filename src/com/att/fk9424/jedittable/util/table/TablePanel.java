/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.att.fk9424.jedittable.util.table;

import com.att.fk9424.jedittable.listeners.AddRowListener;
import com.att.fk9424.jedittable.listeners.TableMenuListener;
import java.awt.BorderLayout;
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
import com.att.fk9424.jedittable.util.table.menu.TablePopUpMenu;
import java.util.Iterator;
import javax.swing.JPopupMenu;

/**
 *
 * @author fk9424
 */
public class TablePanel extends JPanel {
    private JTable theTable;
    private EditTableModel theModel;
    private TableRowSorter sorter;
    private TablePopUpMenu pMenu;
    private JButton addRow;
    private ResourceBundle label = ResourceBundle.getBundle("com.att.fk9424.jedittable.view.labels/ButtonLabels", Locale.getDefault());
    private ArrayList<AddRowListener> addRowListeners;
    
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
        theTable.setModel(theModel);
        theTable.setRowSorter(sorter);
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
    
    public JPopupMenu helperGetTableMenu(){        
        return this.pMenu.getPopupMenu();
    }
    /**
     * provide access to the table model to set up default value for a given column for a new add row
     * @param value
     * @param col 
     */
    public void helperSetDefaultCellValue(Object value, int col){
        this.theModel.setDefaultCellValue(value, col);
    }
    
    public void addTableAddRowListener(AddRowListener l){
        if (addRowListeners == null)
            addRowListeners = new ArrayList<AddRowListener>();
        addRowListeners.add(l);
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
            addRow = new JButton(label.getString("BUTAROW"));
            addRow.setFont(butFont);
            addRow.addActionListener(new ActionListener(){

                @Override
                public void actionPerformed(ActionEvent e) {
                    getTheModel().addNewRow();
                     if (addRowListeners != null){
                        fireAddedTableRow(getTheModel().getRowCount() - 1);
                     }
                }            
            });
            buttonPanel.add(Box.createHorizontalGlue());
            buttonPanel.add(addRow);
            add(buttonPanel, BorderLayout.NORTH);
        }
        add(tablePanel, BorderLayout.CENTER);
        return this;
    }
    
    private void fireAddedTableRow(int rowNum){
        Iterator itr = addRowListeners.iterator();
        while (itr.hasNext())
            ((AddRowListener)itr.next()).addedTableRow(rowNum);
    }
    /**
     *  setup the popup menu for the table to enable delete and delete all rows
     * @param frame 
     */
    public void setPopupMenu(Window frameOrDialog, boolean alertAble, Preferences alertAblePref){
        pMenu = new TablePopUpMenu(theTable, frameOrDialog, alertAble);
        if (alertAble){
            pMenu.setAlertRowNumberFromPref(alertAblePref);
            pMenu.addAlertRowListener(theModel);
        }
        pMenu.initMenu();
        pMenu.addTableMenuListener(theModel);
    }
    /**
     * provide access to the TableMenuListener
     * @param l 
     */
    public void tableMenuAddTableMenuListener(TableMenuListener l){
        if (pMenu != null){
            pMenu.addTableMenuListener(l);
        }
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
    
    public boolean isRowAlertable(int rowIndex){
        return pMenu.isRowAlertable(rowIndex);
    }

}
