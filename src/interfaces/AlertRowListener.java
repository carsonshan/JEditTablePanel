/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package interfaces;

import java.util.Date;

/**
 *
 * @author fk9424
 */
public interface AlertRowListener {
    public void alertRowAdded(int alertIdValue, Date alertDate, String alertInfo);
    public void alertRowDeleted(int alertIdValue);
    public void alertRowDeletedAll();
}
