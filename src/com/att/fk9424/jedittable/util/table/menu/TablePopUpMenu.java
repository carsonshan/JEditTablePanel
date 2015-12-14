/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.att.fk9424.jedittable.util.table.menu;

import com.att.fk9424.jedittable.events.TableMenuEvent;
import interfaces.AlertRowListener;
import java.awt.Window;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.swing.JDialog;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import com.att.fk9424.jedittable.listeners.TableMenuListener;
import com.att.fk9424.jedittable.util.table.TablePanel;
import java.util.Date;
import java.util.prefs.Preferences;
import javax.swing.JOptionPane;

/**
 * This class provide a popup menu to a jtable (data not to the header) which
 * can be then customize with table model to proceed action from the menu.
 * @author fk9424
 */
public class TablePopUpMenu{
    private JPopupMenu pMenu;
    private int colIndex;
    private int rowIndex;
    private int mouseX;
    private int mouseY;
    private int rowHeight;
    private Window diag;
    private ArrayList<TableMenuListener> tableMenuListener = new ArrayList<TableMenuListener>();
    private ResourceBundle label = ResourceBundle.getBundle("com.att.fk9424.jedittable.view.labels/TableModelLabels", Locale.getDefault());
    private ArrayList<AlertRowListener> alertRowListeners;
    private JTable table;
    private boolean alertAble = false;
    private AddAlertMenuItem mAlertAdd;
    private DelAlertMenuItem mAlertDel;
    private DelAllAlertMenuItem mAlertAllDel;
    private DelRowMenuItem mRowDel;
    private DelAllRowsMenuItem mRowAllDel;
    private ArrayList<Integer> alertRowValue;
    private Preferences alertAblePref;
    private final String PREF_ALERTABLE = "alertrowidvalue";
    private int alertColIdNumber = -1;//unique identifier to get the alert row ...
    private TablePanel tPanel;
    private int alertDateColNum;
    private int alertInfoColNum;
    
    public TablePopUpMenu(JTable table, Window diag, boolean alertAble, Preferences alertAblePref, int alertColIdNumber, TablePanel tPanel){
        this.table = table;
        this.diag = diag;
        this.alertAble = alertAble;
        this.alertColIdNumber = alertColIdNumber;
        this.tPanel = tPanel;
        this.alertAblePref = alertAblePref;
    }
    public void initMenu(){
        createMenu();
        table.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseReleased(MouseEvent e){
                if (e.isPopupTrigger()){
                    JTable source = (JTable)e.getSource();
                    int row = source.rowAtPoint(e.getPoint());
                    int column = source.columnAtPoint(e.getPoint());
                    
                    setMouseX(source.getParent().getLocationOnScreen().x);
                    setMouseY(source.getParent().getLocationOnScreen().y);
                    setRowHeight(source.getRowHeight());
                    if (! source.isRowSelected(row))
                        source.changeSelection(row, column, false, false);
                    setRowIndex(source.convertRowIndexToModel(row));
                    setColIndex(source.convertColumnIndexToModel(column));
                    if (alertAble){
                        mRowDel.setEnabled(!TablePopUpMenu.this.isRowAlertable(source.convertRowIndexToModel(row)));
                        mRowAllDel.setEnabled((TablePopUpMenu.this.alertRowValue.size() > 0) ? false : true);
                        mAlertAdd.setEnabled(!TablePopUpMenu.this.isRowAlertable(source.convertRowIndexToModel(row)));
                        mAlertDel.setEnabled(TablePopUpMenu.this.isRowAlertable(source.convertRowIndexToModel(row)));
                        mAlertAllDel.setEnabled((TablePopUpMenu.this.alertRowValue.size() > 0) ? true : false);
                    }
                    pMenu.show(e.getComponent(), e.getX(), e.getY());
                }
            }
        });
    }
    /*
     * create pop up menu for the table so user can get through the menu with
     * a right click on the a table row/cell.
     */
    private void createMenu() {
        ResourceBundle label = ResourceBundle.getBundle("com.att.fk9424.jedittable.view.labels/MenuLabels", Locale.getDefault());
        pMenu = new JPopupMenu();
        
        mRowDel = new DelRowMenuItem(label.getString("DROW"), this);
        pMenu.add(mRowDel);

        mRowAllDel = new DelAllRowsMenuItem(label.getString("DAROW"), this);
        pMenu.add(mRowAllDel);        
        
        if (alertAble){
            mAlertAdd = new AddAlertMenuItem(label.getString("ADDALERT"), this);
            pMenu.add(mAlertAdd);
            mAlertDel = new DelAlertMenuItem(label.getString("DELALERT"), this);
            pMenu.add(mAlertDel);
            mAlertAllDel = new DelAllAlertMenuItem(label.getString("DELALLALERT"), this);
            pMenu.add(mAlertAllDel);
        }
    }
    
    public void addAlertRowListener(AlertRowListener l){
        if (alertRowListeners == null)
            alertRowListeners = new ArrayList<AlertRowListener>();
        alertRowListeners.add(l);
    }
    /*
     * adding listeners (mainly table model and menu item)
     */
    public void addTableMenuListener(TableMenuListener l){
        tableMenuListener.add(l);
    }
    
    /*
     * this would send request to all listener to process a Row Deleting request
     * Event would provide rowIndex. also this is mainly TableModel would should
     * be listening to this to update table.
     */
    public void fireRowDeleting(){
        int option = JOptionPane.showConfirmDialog(null, label.getString("MSGDEL"), label.getString("TITLEDEL"), JOptionPane.YES_NO_OPTION);
        if (option == JOptionPane.YES_OPTION){
            if ((alertRowValue != null) && (alertRowValue.size() > 0)){//need to remove alert first
                fireAlertRowDeleting();
            }        
            Iterator<TableMenuListener> listeners = tableMenuListener.iterator();
            TableMenuEvent event = new TableMenuEvent(
                    this,
                    null,
                    0,
                    this.getRowIndex(),
                    0,
                    0,
                    this.getDiag()
                    );
            while(listeners.hasNext()){
                ((TableMenuListener) listeners.next()).tableMenuDelRow(event);
            }
        }        
    }
    /*
     * this would send request to all listener to process a full delete request
     * Also this is mainly TableModel would should be listening to this to 
     * update table.
     */
    public void fireRowDeletingAll(){
        int option = JOptionPane.showConfirmDialog(null, label.getString("MSGDELALL"), label.getString("TITLEDELALL"), JOptionPane.YES_NO_OPTION);
        if (option == JOptionPane.YES_OPTION){
            Iterator<TableMenuListener> listeners = tableMenuListener.iterator();
            while(listeners.hasNext()){
                ((TableMenuListener) listeners.next()).tableMenuDelAllRows();
            }
        }        
    }
    
    public void fireAlertRowAdding(){      
        if (alertRowValue == null)
            alertRowValue = new ArrayList<Integer>();                
        alertRowValue.add(this.getAlertColIdValue(rowIndex));
        this.setPreferences();
        int alertIndexValue = (Integer)tPanel.getTheModel().getValueAt(rowIndex, this.alertColIdNumber);
        Date alertDateValue = (Date)tPanel.getTheModel().getValueAt(rowIndex, this.alertDateColNum);
        String alertInfoValue = (String)tPanel.getTheModel().getValueAt(rowIndex, this.alertInfoColNum);
        if (alertRowListeners != null){
            Iterator itr = alertRowListeners.iterator();
            while(itr.hasNext())
                ((AlertRowListener)itr.next()).alertRowAdded(alertIndexValue, alertDateValue, alertInfoValue);
        }
    }

    public void fireAlertRowDeleting() {      
        if ((alertRowValue.size() > 0) && (alertRowValue != null)){
            int indexValue = this.getAlertColIdValue(rowIndex);
            for (int i = 0 ; i < alertRowValue.size() ; i++){                
                if(alertRowValue.get(i).equals(indexValue)){                  
                    alertRowValue.remove(i);
                }
            }
            this.setPreferences();            
            Iterator itr = alertRowListeners.iterator();
            while(itr.hasNext())
                ((AlertRowListener)itr.next()).alertRowDeleted(indexValue);
        }
     }    
    
    void fireAlertAllRowDeleting() {
        Iterator itr = alertRowListeners.iterator();
        while(itr.hasNext()){
            ((AlertRowListener)itr.next()).alertRowDeletedAll();
        }
        alertRowValue = null;
        this.setPreferences();
    }

    public void setAlertRowNumberFromPref(Preferences alertAblePref){
        if (alertAblePref != null){
            this.alertAblePref = alertAblePref;
            alertRowValue = new ArrayList<Integer>();
            try{
            String[] arrayAlertStringNumber = alertAblePref.get(PREF_ALERTABLE, null).split("\\|");
            if (arrayAlertStringNumber != null){
                for(int i = 0; i < arrayAlertStringNumber.length ; i++){
                    alertRowValue.add(Integer.parseInt(arrayAlertStringNumber[i]));
                }
            }
            }catch(NullPointerException e){
                //ignore it
            }
        }
    }
    
    public boolean isRowAlertable(int rowIndex){
        if ((alertRowValue.size() > 0) && (alertRowValue != null)){
            int indexValue = this.getAlertColIdValue(rowIndex);
            for (int i = 0 ; i < alertRowValue.size() ; i++){               
                if(alertRowValue.get(i).equals(indexValue)){                  
                    return true;                
                }
            }
        }
        return false;//no alert
    }
    
    private void setPreferences(){
        String stringData = null;
        try {
            for(int i=0; i < alertRowValue.size(); i++){            
                if (i == 0)
                    stringData = String.valueOf(alertRowValue.get(i));
                else
                    stringData += "|" + String.valueOf(alertRowValue.get(i));
            }
            alertAblePref.put(PREF_ALERTABLE, stringData);
        }catch(NullPointerException e){         
            //stringData == null so remove pref ...
            alertAblePref.remove(PREF_ALERTABLE);
        }
    }
    
    public int getAlertColIdValue(int rowIndex){
        try {
            int indexValue = (Integer)tPanel.getTheModel().getValueAt(rowIndex, this.alertColIdNumber);
            return indexValue;
        }catch(IndexOutOfBoundsException e){
            return -1;
        }catch(NullPointerException ex){
            return -1;
        }
    }
    
    public int getAlertColIdRowFromValue(int indexValue){
        for(int i = 0; i < tPanel.getTheModel().getRowCount() ; i++){
            if (this.getAlertColIdValue(i) == indexValue)
                return i;
        }
        return -1;
    }
    
    public void setAlertDateColNum(int alertDateColNumber){
        this.alertDateColNum = alertDateColNumber;
    }
    
    public int getAlertDateColNumber(){
        return this.alertDateColNum;
    }
    
    public void setAlertInfoColNum(int alertInfoColNumber){
        this.alertInfoColNum = alertInfoColNumber;
    }
    
    public int getAlertInfoColNumber(){
        return this.alertInfoColNum;
    }
    
    /**
     * @return the colIndex
     */
    public int getColIndex() {
        return colIndex;
    }
    /**
     * @return the rowIndex
     */
    public int getRowIndex() {
        return rowIndex;
    }
    /**
     * @param rowIndex the rowIndex to set
     */
    public void setRowIndex(int rowIndex) {
        this.rowIndex = rowIndex;
    }
    /**
     * @param colIndex the colIndex to set
     */
    public void setColIndex(int colIndex) {
        this.colIndex = colIndex;
    }
    /**
     * @return the mouseX
     */
    public int getMouseX() {
        return mouseX;
    }
    /**
     * @return the mouseY
     */
    public int getMouseY() {
        return mouseY;
    }
    /**
     * @param mouseX the mouseX to set
     */
    public void setMouseX(int mouseX) {
        this.mouseX = mouseX;
    }
    /**
     * @param mouseY the mouseY to set
     */
    public void setMouseY(int mouseY) {
        this.mouseY = mouseY;
    }
    /**
     * @return the rowHeight
     */
    public int getRowHeight() {
        return rowHeight;
    }
    /**
     * @param rowHeight the rowHeight to set
     */
    public void setRowHeight(int rowHeight) {
        this.rowHeight = rowHeight;
    }
    /**
     * @return the diag
     */
    public Window getDiag() {
        return diag;
    }
    /**
     * @param diag the diag to set
     */
    public void setDiag(JDialog diag) {
        this.diag = diag;
    }
    /**
     * @return the table
     */
    public JTable getTable() {
        return table;
    }
}
