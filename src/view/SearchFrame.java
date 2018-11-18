package view;

import controller.Controller;
import controller.SimpleTableModel;
import utilities.Constants;

import static utilities.Constants.HEADERS;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Vector;

class SearchFrame extends JFrame implements TableModelListener {

    private final Controller controller;
    private final JTable searchedTable;
    private final DefaultTableModel model;
    private final JTextField input;
    private final JButton ok;

    {
        controller = Controller.getInstance();
        searchedTable = new JTable();
        model = new SimpleTableModel();
        input = new JTextField();
        ok = new JButton("Готово");
    }

    public SearchFrame() {
        init();
        setComponents();
        setListeners();
    }

    private void init() {
        searchedTable.setModel(model);
        model.setDataVector(new Vector<>(), HEADERS);
    }

    private void setComponents() {
        setSize(700, 400);
        setLocationRelativeTo(null);

        final JPanel panel = new JPanel();
        final GridBagLayout layout = new GridBagLayout();

        panel.setLayout(layout);

        final GridBagConstraints constraints = new GridBagConstraints();
        final JScrollPane jScrollPane = new JScrollPane(searchedTable);

        constraints.insets = new Insets(10, 10, 5, 10);
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.weightx = 1;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.anchor = GridBagConstraints.NORTH;
        panel.add(input, constraints);


        constraints.insets = new Insets(5, 10, 5, 10);
        constraints.gridy = 1;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.anchor = GridBagConstraints.CENTER;
        panel.add(ok, constraints);

        constraints.insets = new Insets(5, 10, 10, 10);
        constraints.gridy = 2;
        constraints.weightx = 3;
        constraints.weighty = 3;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.anchor = GridBagConstraints.SOUTH;
        panel.add(jScrollPane, constraints);

        add(panel, BorderLayout.CENTER);
    }

    private void updateTableByWord(final String word) {
        model.setDataVector(controller.getDataByWordForTable(word), HEADERS);
        model.fireTableDataChanged();
    }

    private void setListeners() {
        setOkListener();
        setTableListener();
    }

    private void setOkListener() {
        ok.addActionListener(listener -> {
            updateTableByWord(input.getText().toLowerCase());
        });
    }

    private void setTableListener(){
        model.addTableModelListener(this);
    }

    private void updateTable() {
        model.setDataVector(controller.getDataByWordForTable(input.getText()), HEADERS);
        model.fireTableDataChanged();
    }

    @Override
    public void tableChanged(TableModelEvent e) {
        if (e.getColumn() == Constants.WORD) {
            controller.notifyTableChanged(model.getDataVector(), input.getText());
            updateTable();
        }
    }

    public void clear(){
        input.setText("");
        model.setDataVector(new Vector<>(), HEADERS);
    }
}
