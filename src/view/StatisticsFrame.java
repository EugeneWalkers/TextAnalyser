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
    private final Map<String, Integer> tagStatistics;

    {
        tableData = new Vector<>();
        tagStatistics = new HashMap<>();
        ok = new JButton("Готово");
        refresh = new JButton("Обновить");
        table = new JTable();
        model = new DefaultTableModel() {

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 0) {
                    return String.class;
                } else {
                    return Integer.class;
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
        headers.add("Тег");
        headers.add("Расшифровка");

        model.setColumnIdentifiers(headers);
    }

    public StatisticsFrame() {
        super("Статистика");
        setupComponents();
        setListeners();
    }


    private void addOneByOneTag(final TagCountData tag){
        final String key = tag.getName();

        if (!tagStatistics.containsKey(key)) {
            tagStatistics.put(key, tag.getCount());
        } else {
            int a = tagStatistics.get(key);
            tagStatistics.put(key, a + tag.getCount());
        }
    }

    private void addSeveralTags(final List<TagCountData> tags){
        final String key = tagListToStringWithoutNumb(tags);
        int count = 0;

        for (int i=0; i<tags.size(); i++){
            count+=tags.get(i).getCount();
        }

        if (!tagStatistics.containsKey(key)) {
            tagStatistics.put(key, count);
        } else {
            int a = tagStatistics.get(key);
            tagStatistics.put(key, a + count);
        }
    }

    public void recalculate() {
        tagStatistics.clear();

        final List<WordData> data = Controller.getInstance().getData();

        for (final WordData tempData : data) {

            final List<TagCountData> tags = tempData.getWordTag();

            for (final TagCountData tag : tags) {
                addOneByOneTag(tag);
            }

            if (tags.size() > 1){
                addSeveralTags(tags);
            }
        }

        tableData.clear();

        for (final Map.Entry<String, Integer> entry : tagStatistics.entrySet()) {
            final Vector temp = new Vector();
            temp.add(entry.getKey());
            temp.add(entry.getValue());

            tableData.add(temp);
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
