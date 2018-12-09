package controller;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import static utilities.Constants.*;

public class SimpleTableModel extends DefaultTableModel{
    private TableRowSorter<DefaultTableModel> sorter;
    private DefaultTableCellRenderer centerRenderer;

    public SimpleTableModel() {
        super();
        setSorter();
        setCellRenderer();
    }

    public TableRowSorter<DefaultTableModel> getSorter() {
        return sorter;
    }

    public DefaultTableCellRenderer getCenterRenderer() {
        return centerRenderer;
    }

    private void setSorter() {
        sorter = new TableRowSorter<>(this);
        sorter.setSortsOnUpdates(true);
    }

    private void setCellRenderer() {
        centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        switch (column) {
            case WORD:
                return true;
            case TAG_WORD:
                return true;
            case TAG_LEMMA:
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
