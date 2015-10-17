package events;

import java.util.EventObject;


@SuppressWarnings("serial")
public final class TableCellUpdateEvent extends EventObject {
    private int colIndex;
    private int rowIndex;
    private Object value;

    public TableCellUpdateEvent(Object source, int colIndex, int rowIndex, Object value) {
        super(source);
        this.setColIndex(colIndex);
        this.setRowIndex(rowIndex);
        this.setValue(value);
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
     * @param colIndex the colIndex to set
     */
    public void setColIndex(int colIndex) {
        this.colIndex = colIndex;
    }
    /**
     * @param rowIndex the rowIndex to set
     */
    public void setRowIndex(int rowIndex) {
        this.rowIndex = rowIndex;
    }
    /**
     * @return the value
     */
    public Object getValue() {
        return value;
    }
    /**
     * @param value the value to set
     */
    public void setValue(Object value) {
        this.value = value;
    }
}