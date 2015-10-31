package com.att.fk9424.jedittable.listeners;

import com.att.fk9424.jedittable.events.TableMenuEvent;

public interface TableMenuListener {
    public void tableMenuDelRow(TableMenuEvent e);
    public void tableMenuDelAllRows();
}