package view;

import controller.Controller;
import javafx.util.Pair;
import textHandlers.Lemmatizer;
import utilities.TagCountData;
import utilities.WordData;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import static utilities.StringUtilities.tagListToStringWithoutNumb;

public class TagPairsStatisticsFrame extends JFrame {

    private final JButton ok;
    private final JButton refresh;
    private final JTable table;
    private final DefaultTableModel model;
    private final Vector<String> headers;
    private final Vector<Vector> tableData;

    private final Controller controller;

    {
        controller = Controller.getInstance();
        tableData = new Vector<>();
        ok = new JButton("Готово");
        refresh = new JButton("Обновить");
        table = new JTable();
        model = new DefaultTableModel() {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 2) {
                    return Integer.class;
                } else {
                    return String.class;
                }
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table.setModel(model);
        table.setRowSorter(new TableRowSorter<>(model));
        headers = new Vector<>();
        headers.add("Тег1");
        headers.add("Тег2");
        headers.add("Количество");

        model.setColumnIdentifiers(headers);
    }

    public TagPairsStatisticsFrame() {
        super("Статистика");
        setupComponents();
        setListeners();
    }

    public void recalculate() {
        final Map<Pair<String, String>, Integer> tagStatistics = controller.getTagPairsFromAllFiles();
        tableData.clear();
        for (Map.Entry<Pair<String, String>, Integer> entry : tagStatistics.entrySet()) {
            final Vector tempData = new Vector();
            tempData.add(entry.getKey().getKey());
            tempData.add(entry.getKey().getValue());
            tempData.add(entry.getValue());
            tableData.add(tempData);
        }
        model.setDataVector(tableData, headers);
        model.fireTableDataChanged();
    }

    private void setupComponents() {
        setSize(600, 300);
        setLocationRelativeTo(null);
        final JScrollPane pane = new JScrollPane(table);
        add(pane, BorderLayout.CENTER);
        final JPanel bottom = new JPanel();
        bottom.add(ok, BorderLayout.WEST);
        bottom.add(refresh, BorderLayout.EAST);
        add(bottom, BorderLayout.PAGE_END);
    }

    private void setListeners() {
        setOkListener();
        setRefreshListener();
    }

    private void setOkListener() {
        ok.addActionListener(e -> setVisible(false));
    }

    private void setRefreshListener() {
        refresh.addActionListener(e -> {
            recalculate();
        });
    }
}
