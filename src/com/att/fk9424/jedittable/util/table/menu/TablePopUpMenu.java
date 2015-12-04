/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.att.fk9424.jedittable.util.table.menu;

import com.att.fk9424.jedittable.events.TableMenuEvent;
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
    private JTable table;
    
    public TablePopUpMenu(JTable table, Window diag){
        this.table = table;
        this.diag = diag;
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
}
