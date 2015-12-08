/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.att.fk9424.jedittable.util.table.menu;

import com.att.fk9424.jedittable.events.TableMenuEvent;
import com.att.fk9424.jedittable.listeners.AlertRowListener;
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
import java.util.prefs.Preferences;

/**
 * This class provide a popup menu to a jtable (data not to the header) which
 * can be then customize with table model to proceed action from the menu.
 * @author fk9424
 */
public class TablePopUpMenu {
    private JPopupMenu pMenu;
    private int colIndex;
    private int rowIndex;
    private int mouseX;
    private int mouseY;
    private int rowHeight;
    private Window diag;
    private ArrayList<TableMenuListener> tableMenuListener = new ArrayList<TableMenuListener>();
    private ArrayList<AlertRowListener> alertRowListeners;
    private ArrayList<Integer> alertRowNumber;
    private JTable table;
    private boolean alertAble = false;
    private AddAlertMenuItem mAlertAdd;
    private DelAlertMenuItem mAlertDel;
    private Preferences alertAblePref;
    private final String PREF_ALERTABLE = "alertablerow";
    
    public TablePopUpMenu(JTable table, Window diag, boolean alertAble){
        this.table = table;
        this.diag = diag;
        this.alertAble = alertAble;        
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
                    mAlertAdd.setEnabled(!TablePopUpMenu.this.isRowAlertable(getRowIndex()));
                    mAlertDel.setEnabled(TablePopUpMenu.this.isRowAlertable(getRowIndex()));
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
        DelRowMenuItem mRowDel;
        DelAllRowsMenuItem mRowAllDel;

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
    
    public JPopupMenu getPopupMenu(){
        return pMenu;
    }
    
    public void setAlertRowNumberFromPref(Preferences alertAblePref){
        if (alertAblePref != null){
            this.alertAblePref = alertAblePref;
            alertRowNumber = new ArrayList<Integer>();
            try{
            String[] arrayAlertStringNumber = alertAblePref.get(PREF_ALERTABLE, null).split("\\|");
            if (arrayAlertStringNumber != null){
                for(int i = 0; i < arrayAlertStringNumber.length ; i++){
                    alertRowNumber.add(Integer.parseInt(arrayAlertStringNumber[i]));
                }
            }
            }catch(NullPointerException e){
                //ignore it
            }
        }
    }
    
    public boolean isRowAlertable(int rowIndex){
        if (alertRowNumber != null){
            for (int i = 0 ; i < alertRowNumber.size() ; i++){               
                if(alertRowNumber.get(i).equals(rowIndex)){                  
                    return true;                
                }
            }
        }
        return false;
    }
    
    public void fireAlertRowAdding(){
        if (alertRowNumber == null)
            alertRowNumber = new ArrayList<Integer>();
        alertRowNumber.add(this.getRowIndex());
        this.setPreferences();
        if (alertRowListeners != null){
            Iterator itr = alertRowListeners.iterator();
            while(itr.hasNext())
                ((AlertRowListener)itr.next()).alertRowAdded(rowIndex);
        }
    }
    /*
     * this would send request to all listener to process a Row Deleting request
     * Event would provide rowIndex. also this is mainly TableModel would should
     * be listening to this to update table.
     */
    public void fireRowDeleting(){
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
    /*
     * this would send request to all listener to process a full delete request
     * Also this is mainly TableModel would should be listening to this to 
     * update table.
     */
    public void fireRowDeletingAll(){
        Iterator<TableMenuListener> listeners = tableMenuListener.iterator();
        while(listeners.hasNext()){
            ((TableMenuListener) listeners.next()).tableMenuDelAllRows();
        }
        
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

    public void fireAlertRowDeleting() {
        if (alertRowNumber != null){
            for (int i = 0 ; i < alertRowNumber.size() ; i++){                
                if(alertRowNumber.get(i).equals(rowIndex)){                  
                    alertRowNumber.remove(i);
                }
            }
            this.setPreferences();
        }
        if (alertRowListeners != null){
            Iterator itr = alertRowListeners.iterator();
            while(itr.hasNext())
                ((AlertRowListener)itr.next()).alertRowDeleted(rowIndex);
        }
     }
    
    private void setPreferences(){
        String stringData = null;
        for(int i=0; i < alertRowNumber.size(); i++){            
            if (i == 0)
                stringData = String.valueOf(alertRowNumber.get(i));
            else
                stringData += "|" + String.valueOf(alertRowNumber.get(i));
        }
        try{
            alertAblePref.put(PREF_ALERTABLE, stringData);
        }catch(NullPointerException e){
            //ignore it
        }
    }
}
