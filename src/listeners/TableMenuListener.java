package listeners;

import events.TableMenuEvent;

public interface TableMenuListener {
    public void tableMenuDelRow(TableMenuEvent e);
    public void tableMenuDelAllRows();
}