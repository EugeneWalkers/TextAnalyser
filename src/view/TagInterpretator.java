package view;

import utilities.TagsKeeper;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.Vector;

public class TagInterpretator extends JFrame {

    private final JButton ok;
    private List<String> tags;
    private final JTable table;
    private final DefaultTableModel model;
    private final Vector<String> headers;

    {
        ok = new JButton("Готово");
        table = new JTable();
        model = new DefaultTableModel(){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table.setModel(model);
        headers = new Vector<>();
        headers.add("Тег");
        headers.add("Расшифровка");

        model.setColumnIdentifiers(headers);
    }

    public TagInterpretator() {
        super("Описание");
        setupComponents();
        setListeners();
    }

    public void setData(final List<String> data) {
        this.tags = data;
    }

    public void recalculate(){
        final Vector<Vector<String>> data = new Vector<>();

        for (int i=0; i<tags.size(); i++){
            final Vector<String> row = new Vector<>();
            row.add(tags.get(i));
            row.add(TagsKeeper.getTagData(tags.get(i)).getDescription());

            data.add(row);
        }

        model.setDataVector(data, headers);
        model.fireTableDataChanged();
    }


    private void setupComponents() {
        setSize(500, 300);
        setLocationRelativeTo(null);
        final JScrollPane pane = new JScrollPane(table);
        add(pane, BorderLayout.CENTER);
        add(ok, BorderLayout.PAGE_END);
    }

    private void setListeners() {
        setOkListener();
    }

    private void setOkListener() {
        ok.addActionListener(e -> {
            this.setVisible(false);
        });
    }


}
