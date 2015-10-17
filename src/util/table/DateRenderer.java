/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package util.table;

import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author fk9424
 */
public class DateRenderer extends DefaultTableCellRenderer {
    private SimpleDateFormat formatLow = new SimpleDateFormat("dd-MMM-yy");
    private SimpleDateFormat formatFull = new SimpleDateFormat("dd-MMM-yy, hh:mm:ss");
    private boolean fullDate = false;
    public DateRenderer(boolean fullDate){
        super();
        this.fullDate = fullDate;
    }
    
    @Override
    public void setValue(Object value){
        SimpleDateFormat formatter = (fullDate ? formatFull : formatLow);
        setText((value == null) ? formatter.format(new Date()) : formatter.format(value));
    }
}
