package view;

import controller.Controller;
import utilities.TagCountData;
import utilities.WordData;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.util.*;
import java.util.List;

import static utilities.StringUtilities.tagListToStringWithoutNumb;

public class StatisticsFrame extends JFrame {

    private final JButton ok;
    private final JButton refresh;
    private final JTable table;
    private final DefaultTableModel model;
    private final Vector<String> headers;
    private final Vector<Vector> tableData;

    {
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
        headers.add("Слово");
        headers.add("Тег");
        headers.add("Статистика");

        model.setColumnIdentifiers(headers);
    }

    public StatisticsFrame() {
        super("Статистика");
        setupComponents();
        setListeners();
    }


    private void add(final String word, final TagCountData tag){
        final Vector temp = new Vector();
        temp.add(word);
        temp.add(tag.getName());
        temp.add(tag.getCount());

        tableData.add(temp);
    }


    public void recalculate() {
        tableData.clear();

        final List<WordData> data = Controller.getInstance().getData();

        for (final WordData tempData : data) {
            final List<TagCountData> tags = tempData.getWordTag();

            for (final TagCountData tag : tags) {
                add(tempData.getWord(), tag);
            }
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
