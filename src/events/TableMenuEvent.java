package events;

import java.awt.Window;
import java.util.EventObject;
import javax.swing.JTable;

// TODO Documentation

@SuppressWarnings("serial")
public final class TableMenuEvent extends EventObject {
    private int colIndex;
    private int rowIndex;
    private int X;
    private int Y;
    private Window diag;
    private JTable table;

    public TableMenuEvent(Object source, JTable table, int colIndex, int rowIndex, int X, int Y, Window diag) {
        super(source);
        this.setTable(table);
        this.setColIndex(colIndex);
        this.setRowIndex(rowIndex);
        this.setX(X);
        this.setY(Y);
        this.setDiag(diag);
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
     * @return the X
     */
    public int getX() {
        return X;
    }

    /**
     * @return the Y
     */
    public int getY() {
        return Y;
    }

    /**
     * @return the diag
     */
    public Window getDiag() {
        return diag;
    }

    /**
     * @return the table
     */
    public JTable getTable() {
        return table;
    }

    /**
     * @param colIndex the colIndex to set
     */
    public void setColIndex(int colIndex) {
        this.colIndex = colIndex;
    }

    /**
     * @param diag the diag to set
     */
    public void setDiag(Window diag) {
        this.diag = diag;
    }

    /**
     * @param rowIndex the rowIndex to set
     */
    public void setRowIndex(int rowIndex) {
        this.rowIndex = rowIndex;
    }

    /**
     * @param table the table to set
     */
    public void setTable(JTable table) {
        this.table = table;
    }

    /**
     * @param X the X to set
     */
    public void setX(int X) {
        this.X = X;
    }

    /**
     * @param Y the Y to set
     */
    public void setY(int Y) {
        this.Y = Y;
    }
}