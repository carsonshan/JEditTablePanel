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
import java.util.Date;
import javax.swing.AbstractCellEditor;
import javax.swing.JFrame;
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
    public DateEditor(JFrame frame){
        button = new JDateButton(frame);
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
        return button;
    }
    /**
     * from JDatePanel to gather the new date selected in the JDateTable ...
     * @param de 
     */
    @Override
    public void updateDate(DateEvent de) {
        theDate = (Date)de.getNewDate().getDate();
        fireEditingStopped();
    }

}