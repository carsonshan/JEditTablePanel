/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package util.table.menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JMenuItem;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

/**
 *
 * @author fk9424
 */
public class DelRowMenuItem extends JMenuItem implements TableModelListener {
    
    public DelRowMenuItem(String title, final DelPopUpMenu menu){
        super(title);
        this.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                menu.fireRowDeleting();
            }
            
        });
    }

    @Override
    public void tableChanged(TableModelEvent e) {
        TableModel source = (TableModel)e.getSource();
        if ((source.getRowCount() == 1) && (source.getValueAt(0, 0).equals(""))){
            // we have one row withoutdata
            this.setEnabled(false);
        }else{
            this.setEnabled(true);
        }
    }
}