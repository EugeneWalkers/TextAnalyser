package view;

import utilities.TagData;
import utilities.TagsKeeper;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.Map;
import java.util.Vector;

public class FullTagHelper extends JFrame {

    private static final FullTagHelper INSTANCE = new FullTagHelper();

    private final JButton ok;
    private final Vector<Vector<String>> tags;
    private final JTable table;
    private final DefaultTableModel model;
    private final Vector<String> headers;

    {
        ok = new JButton("Готово");
        table = new JTable();
        model = new DefaultTableModel() {
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
        tags = new Vector<>();

        final Map<String, TagData> tags = TagsKeeper.getAllTags();

        for (final Map.Entry<String, TagData> entry : tags.entrySet()) {
            final Vector<String> tempData = new Vector<>();

            tempData.add(entry.getKey());
            tempData.add(TagsKeeper.getTagData(entry.getKey()).getDescription());

            this.tags.add(tempData);
        }

        model.setDataVector(this.tags, headers);

        final DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(
                    final JTable table,
                    final Object value,
                    final boolean isSelected,
                    final boolean hasFocus,
                    final int row,
                    final int column) {
                final JLabel label = (JLabel)super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                final Color bc = TagsKeeper.getTagData(FullTagHelper.this.tags.get(row).get(0)).getColor();

                label.setForeground(bc);

                return label;
            }
        };

        table.getColumnModel().getColumn(0).setCellRenderer(renderer);

    }

    private FullTagHelper() {
        super("Описание");
        setupComponents();
        setListeners();
    }

    public static FullTagHelper getInstance() {
        return INSTANCE;
    }

    private void setupComponents() {
        setSize(600, 300);
        setLocationRelativeTo(null);
        final JScrollPane pane = new JScrollPane(table);
        add(pane, BorderLayout.CENTER);
        add(ok, BorderLayout.PAGE_END);
    }

    private void setListeners() {
        setOkListener();
    }

    private void setOkListener() {
        ok.addActionListener(e -> setVisible(false));
    }

}
