/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.att.fk9424.jedittable.model;

import com.att.fk9424.jedittable.events.TableMenuEvent;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import com.att.fk9424.jedittable.listeners.TableMenuListener;
import interfaces.TableModelColumnType;
import java.util.HashMap;

/**
 * A simple Edit Table Model using AbstractTable Model with an home made table menu listener
 * @author fk9424
 */
public class EditTableModel extends AbstractTableModel implements TableMenuListener, TableModelListener, TableModelColumnType {
    private ArrayList<Object[]> values;
    private String[] columnNames;
    private Class[] columnTypes;
    private ArrayList<Integer> notEditableCol;
    private Preferences prefs;
    private SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yy");
    private ResourceBundle label = ResourceBundle.getBundle("com.att.fk9424.jedittable.view.labels/TableModelLabels", Locale.getDefault());
    private HashMap<Integer, Object> defaultCellValue;
    private int alertColIdNumber = -1;
    
    public EditTableModel(String[] columnNames, Class[] columnTypes){
        this.columnNames = columnNames;
        this.columnTypes = columnTypes;
    }
    
    public void init(Preferences prefs){
        this.prefs = prefs;
        ArrayList<Object[]> val = new ArrayList<Object[]>();
        if ((prefs == null) ||(!hasItems())){ // if we set preferences then data would be retrieve and save in the registry            
            this.values = val;
            this.defaultCellValue = new HashMap<Integer, Object>();
            this.fireTableDataChanged();
        }else{
            this.values = new ArrayList<Object[]>();
            Object[] itemList;
            try {
                itemList = this.prefs.keys();
                for(int i=0;i<itemList.length;i++){
                    String[] data = this.prefs.get((String)itemList[i], null).split("\\|");
                    addEntry(data);
                }
            } catch (BackingStoreException ex) {}
        }        
    }
    /*
     * checking if any todo preference exist.
     */
    private boolean hasItems(){
        try {
            return ((this.prefs.keys().length > 0)? true : false);
        } catch (BackingStoreException ex) {
            return false;
        } catch (NullPointerException en){
            return false;
        }
    }
    /**
     * set preference from a given model values array list
     */
    public void setPreferences(ArrayList<Object[]> data){
        if (data.size() > 0){            
            for(int j = 0 ; j < data.size() ; j++){
                this.prefs.put(String.valueOf(j), this.convertObjectToString(data.get(j)));
            }
        }
    }
    /**
     * 
     * @param data
     * @return 
     */
    private String convertObjectToString(Object[] data){
        String sData = "";
        for(int i = 0; i < data.length ; i++){
            if (this.getColumnType(i) == Date.class){
                sData += formatter.format(data[i])+"|";
            }else {
                sData += data[i]+"|";
            }
        }
        sData = sData.substring(0, sData.length()-1);
        return sData;
    }
    /**
     * From TableModelColumnType
     * @param c
     * @return a class indication the variable type Integer, String, Date etc ..
     */
    @Override
    public Class<?> getColumnType(int c){
        return columnTypes[c];
    }
    /**
     *  Here we do fill in the data (values) from the preferences so not firing insert ...
     * @param data
     *
     */
    public void addEntry(String[] data){
        Object[] val = new Object[this.columnNames.length];        
        for(int i = 0 ; i < this.columnNames.length ; i++){
            if (this.getColumnType(i) == Integer.class)
                val[i] = Integer.parseInt(data[i]);
            if (this.getColumnType(i) == Double.class)
                val[i] = Double.parseDouble(data[i]);
            if (this.getColumnType(i) == Float.class)
                val[i] = Float.parseFloat(data[i]);
            if (this.getColumnType(i) == Boolean.class)
                val[i] = Boolean.parseBoolean(data[i]);
            if (this.getColumnType(i) == Date.class)
                try {
                    val[i] = formatter.parse(data[i]);
                } catch (ParseException ex) {
                   val[i] = new Date();
                }
            if (this.getColumnType(i) == String.class)
                val[i] = data[i].toString();
        }
        this.values.add(val);
    }
    public void setAlertColumnNumber(int colIndex){
        this.alertColIdNumber = colIndex;
    }
    /**
     * 
     * @return a array of object corresponding to the data need for new row
     */
    public Object[] createNewRow(){
        int rowNum;
        if ((this.values != null) && (values.size() > 0)){//we already have data in the table
            if (alertColIdNumber != -1){//get the value
                rowNum = ((Integer)this.getValueAt(this.getRowCount() - 1, alertColIdNumber));
            }else
                rowNum = this.getRowCount();
        }else
            rowNum = 0;
        Object[] data = new Object[this.columnNames.length];        
        for(int i = 0 ; i < this.columnNames.length ; i++){
            if ((defaultCellValue != null)&&(defaultCellValue.containsKey(i))){
                data[i] = defaultCellValue.get(i);              
            }else if (this.getColumnType(i) == Integer.class)
                data[i] = new Integer(rowNum + 1);
            else if (this.getColumnType(i) == Double.class)
                data[i] = new Double(rowNum + 1);
            else if (this.getColumnType(i) == Float.class)
                data[i] = new Float(rowNum + 1);
            else if (this.getColumnType(i) == Date.class)
                data[i] = new Date();
            else if (this.getColumnType(i) == Boolean.class)
                data[i] = new Boolean(false);
            else if (this.getColumnType(i) == String.class)
                data[i] = label.getString("TBD");
        }
        return data;
    }
    /**
     * add a new row to the table model and fire table update
     * @param nRow 
     */
    public void addNewRow(){
        this.values.add(createNewRow());
        this.fireTableRowsInserted(values.size() - 1, values.size() - 1);
    }
    
    public void setDefaultCellValue(Object value, int col){
        if (defaultCellValue == null){
            defaultCellValue = new HashMap<Integer, Object>();
        }
        defaultCellValue.put(col, value);
    }
    /**
     * register column number that should NOT be editable ...
     * @param col 
     */
    public void setNotEditableCol(int col){
        if (notEditableCol == null){
            notEditableCol = new ArrayList<Integer>();
        }
        notEditableCol.add(col);
    }
    /**
     * 
     * @param col
     * @return true if a column has been registered as Editable and False otherwise
     */
    private boolean isColumnEditable(int col){
        if (notEditableCol == null){
            return true;
        }else{
            if (notEditableCol.contains(col)){
                return false;
            }else{
                return true;
            }
        }
    }
    /**
     * 
     * @return the number of row in the table model
     */
    @Override
    public int getRowCount() {
       return this.values.size();
    }
    /**
     * 
     * @return the number of column in the table model
     */
    @Override
    public int getColumnCount() {
        return this.columnNames.length;
    }
    /**
     * 
     * @param col
     * @return columnNames for the table header
     */
    @Override
    public String getColumnName(int col) {
            return columnNames[col];
    }
    /**
     * 
     * @param c
     * @return the class type for a given column number
     */
    @Override
    public Class getColumnClass(int c) {
        if (this.getRowCount() <= 0){
            return Object.class;
        }else {
            return this.getColumnType(c);
//            return getValueAt(0, c).getClass();
        }
    }
    /**
     * 
     * @param rowIndex
     * @param columnIndex
     * @return value of a given row and column for the JTable
     */
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if (this.values.size() <= 0){
            return null;
        }else{
            return this.values.get(rowIndex)[columnIndex];
        }
    }
    /**
     * 
     * @param rowIndex
     * @param columnIndex
     * @return  for column above the first one
     */
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex){
        if (isColumnEditable(columnIndex)){//columnID generated by model
            return true;
        }else{
            return false;
        }
    }
    /**
     * 
     * @param aValue
     * @param rowIndex
     * @param columnIndex 
     */
    @Override
    public  void setValueAt(Object aValue, int rowIndex, int columnIndex)  {
        this.values.get(rowIndex)[columnIndex] = aValue;
        this.fireTableCellUpdated(rowIndex, columnIndex);
    }
    /**
     * from TableMenuListener
     * @param e 
     */
    @Override
    public void tableMenuDelRow(TableMenuEvent e) {
        int rowIndex =e.getRowIndex();        
        this.values.remove(rowIndex);
        this.fireTableRowsDeleted(rowIndex, rowIndex);            
    }
    /**
     * from TableMenuListener 
     * call tableMenuDelRow for all item in the table ...
     */
    @Override
    public void tableMenuDelAllRows() {        
        while(getRowCount()>0){
            this.values.remove(0);
            this.fireTableRowsDeleted(0, 0);
        }
    }
    /**
     * from TableModelListener
     * @param e 
     */
    @Override
    public void tableChanged(TableModelEvent e) {
        if (prefs != null){
            try {
                this.prefs.clear();
                this.setPreferences(values);
            } catch (BackingStoreException ex) {
                this.prefs = null; // remove preference so we don't get the hassle
            }
        }
    }
    /**
     * 
     * @param data 
     */
    public void setDataRows(ArrayList<Object[]> data){
        this.values = data;
        this.fireTableDataChanged();
    }
}
