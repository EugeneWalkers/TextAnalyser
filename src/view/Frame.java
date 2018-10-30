package view;

import controller.Controller;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.io.*;
import java.util.List;
import java.util.Vector;

public class Frame extends JFrame {

    private static final String DIR = "docs";
    private static Frame frame = null;
    private final Vector headers;
    private final DefaultTableModel model;
    private final JTable dictionaryTable;
    private final JFileChooser chooser;
    private final JTextArea originalTextArea;
    private final ReaderFromFile reader;
    private final JProgressBar bar;
    private final GridBagConstraints constraints;
    private final JPanel mainPanel;
    private final JButton handler;

    private File file;

    private Controller controller;

    {
        chooser = new JFileChooser(DIR);

        reader = new ReaderFromFile();

        constraints = new GridBagConstraints();

        bar = new JProgressBar();

        mainPanel = new JPanel();

        originalTextArea = new JTextArea();
        originalTextArea.setLineWrap(true);

        handler = new JButton("Составить словарь");
        setHandlerListener();

        controller = new Controller();

        headers = new Vector();
        headers.add("Слово");
        headers.add("Количество повторений");

        dictionaryTable = new JTable();
        model = new DefaultTableModel(){

            @Override
            public Class getColumnClass(int column) {
                switch (column) {
                    default:
                    case 0:
                        return String.class;
                    case 1:
                        return Integer.class;
                }
            }
        };
        model.setColumnIdentifiers(headers);
        dictionaryTable.setModel(model);

        final TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
        sorter.setSortsOnUpdates(true);

        dictionaryTable.setRowSorter(sorter);

        model.addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                if (e.getColumn() == 0) {
                    controller.notifyTableChanged(model.getDataVector());
                    model.setDataVector(controller.getData(), headers);
                    model.fireTableDataChanged();
                }
            }
        });
        model.setDataVector(controller.getData(), headers);
    }

    private Frame() {
        super("Анализатор текста");
        setFrame();
        setComponents();
        setMenu();
    }

    public static Frame getInstance() {
        if (frame == null) {
            frame = new Frame();
        }
        return frame;
    }

    private void setFrame() {
        setSize(1000, 500);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    private void setHandlerListener() {
        handler.addActionListener(listener -> new Thread(() -> {
            bar.setIndeterminate(true);

            handler.setEnabled(false);

            controller.pushFile(file);
            controller.addTextAndRewriteFile(originalTextArea.getText());
            controller.handle();

            model.setDataVector(controller.getData(), headers);

            originalTextArea.setText("");

            file = null;

            handler.setEnabled(true);

            bar.setIndeterminate(false);
        }).start());
    }

    private void setComponents() {
        setLayout(new GridLayout(1, 1));
        add(mainPanel);
        mainPanel.setLayout(new GridBagLayout());

        final JScrollPane scrollerForOriginalTextArea = new JScrollPane(originalTextArea);
        final JScrollPane scrollerForResults = new JScrollPane(dictionaryTable);

        constraints.insets = new Insets(5, 10, 5, 10);
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.anchor = GridBagConstraints.NORTH;
        constraints.weightx = 1;
        constraints.weighty = 5;
        constraints.gridwidth = 1;
        mainPanel.add(scrollerForOriginalTextArea, constraints);

        constraints.gridx = 1;
        constraints.gridwidth = 1;
        constraints.anchor = GridBagConstraints.NORTH;
        mainPanel.add(scrollerForResults, constraints);

        constraints.gridx = 0;
        constraints.insets = new Insets(5, 10, 0, 5);
        constraints.weighty = 1;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.gridy = 1;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.fill = GridBagConstraints.NONE;
        mainPanel.add(handler, constraints);

        constraints.insets = new Insets(0, 10, 10, 10);
        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.gridwidth = 2;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.anchor = GridBagConstraints.SOUTH;
        mainPanel.add(bar, constraints);
    }

    private void setMenu() {
        JMenuBar bar = new JMenuBar();
        JMenu fileMenu = new JMenu("Файл");
        JMenuItem open = new JMenuItem("Открыть");
        JMenuItem exit = new JMenuItem("Выход");
        fileMenu.add(open);
        fileMenu.addSeparator();
        fileMenu.add(exit);
        bar.add(fileMenu);
        setJMenuBar(bar);

        open.addActionListener(listener -> showFileChooser());

        exit.addActionListener(listener -> System.exit(0));
    }

    private void showFileChooser() {
        int res = chooser.showDialog(null, "Открыть файл");
        switch (res) {
            case JFileChooser.APPROVE_OPTION:
                file = chooser.getSelectedFile();
                originalTextArea.setText("");
                handler.setEnabled(false);
                new Thread(reader).start();

                break;
            case JFileChooser.CANCEL_OPTION:

                break;

        }
    }

    private class ReaderFromFile implements Runnable {

        @Override
        public void run() {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)))) {

                StringBuilder builder = new StringBuilder();
                while (!builder.append(reader.readLine()).toString().equals("null")) {
                    originalTextArea.append(builder.toString() + "\n");
                    builder.delete(0, builder.length());
                }

                int r = originalTextArea.getText().length();
                originalTextArea.select(r - 1, r);
                originalTextArea.cut();
                handler.setEnabled(true);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
