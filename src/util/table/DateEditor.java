/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package util.table;

import com.att.fk9424.jdatepanel.listeners.DateListener;
import com.att.fk9424.jdatepanel.main.JDateButton;
import com.att.fk9424.jdatepanel.model.JDateDialog;
import com.att.fk9424.jdatepanel.events.DateEvent;
import java.awt.Component;
import java.awt.Window;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.AbstractCellEditor;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

/**
 *
 * @author fk9424
 */
public class DateEditor extends AbstractCellEditor implements TableCellEditor, DateListener {
    private Date theDate = new Date();
    private JDateButton button;
    private JDateDialog dateDialog;
    private SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yy");
    
    public DateEditor(Window win){
        button = new JDateButton(win);
        init();
    }
    
    private void init(){
        dateDialog = button.getDialog();
        dateDialog.addDateListener(this);
    }
    /**
     * 
     * @return 
     */
    @Override
    public Object getCellEditorValue() {
        return theDate;
    }
    /**
     * 
     * @param table
     * @param value
     * @param isSelected
     * @param row
     * @param column
     * @return 
     */
    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        theDate = (Date)value;
        button.setText(formatter.format(theDate));
        return button;
    }
    /**
     * from JDatePanel to gather the new date selected in the JDateTable ...
     * @param de 
     */
    @Override
    public void updateDate(DateEvent de) {
        try {
            theDate = (Date)de.getNewDate().getDate();
        }catch (NullPointerException e){}
        fireEditingStopped();
    }

}