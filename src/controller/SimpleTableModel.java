package controller;

import javax.swing.table.DefaultTableModel;

import static utilities.Constants.COUNT;
import static utilities.Constants.WORD;

public class SimpleTableModel extends DefaultTableModel {

    @Override
    public boolean isCellEditable(int row, int column) {
        switch (column) {
            case WORD:
                return true;
            default:
                return false;
        }
    }

    @Override
    public Class getColumnClass(final int column) {
        switch (column) {
            case COUNT:
                return Integer.class;
            default:
                return String.class;
        }
    }
}
