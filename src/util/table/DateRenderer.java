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
    private SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yy");
   
    public DateRenderer(){
        super();
    }
    
    @Override
    public void setValue(Object value){
        setText((value == null) ? formatter.format(new Date()) : formatter.format(value));
    }
}
