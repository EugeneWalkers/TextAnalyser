package view;

import controller.Controller;
import controller.SimpleTableModel;
import utilities.Constants;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.*;

import static utilities.Constants.HEADERS;

public class DictionaryFrame extends JFrame {

    private static final String DIR = "docs";
    private static DictionaryFrame dictionaryFrame = null;

    private final DefaultTableModel model;
    private final JTable dictionaryTable;
    private final JFileChooser chooser;
    private final JTextArea originalTextArea;
    private final ReaderFromFile reader;
    private final JProgressBar bar;
    private final GridBagConstraints constraints;
    private final JPanel mainPanel;
    private final JButton handler;
    private final JButton cleaner;
    private final JButton adder;
    private final JButton searcher;
    private final DialogInputWord dialogInputWord;
    private final SearchFrame searchFrame;

    private File file;

    private Controller controller;

    {
        chooser = new JFileChooser(DIR);
        reader = new ReaderFromFile();
        constraints = new GridBagConstraints();
        bar = new JProgressBar();
        mainPanel = new JPanel();
        originalTextArea = new JTextArea();
        handler = new JButton("Составить словарь");
        cleaner = new JButton("Удалить словарь");
        adder = new JButton("Добавить слово");
        searcher = new JButton("Найти слово в словаре");
        controller = Controller.getInstance();
        dictionaryTable = new JTable();
        model = new SimpleTableModel();

        dialogInputWord = new DialogInputWord() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                dialogInputWord.setVisible(false);
                controller.addWord(dialogInputWord.getString());
                updateTable();
            }
        };

        searchFrame = SearchFrame.getInstance();
    }

    private DictionaryFrame() {
        super("Анализатор текста");
        init();
        setFrame();
        setComponents();
        setMenu();
    }

    public static DictionaryFrame getInstance() {
        if (dictionaryFrame == null) {
            dictionaryFrame = new DictionaryFrame();
        }

        return dictionaryFrame;
    }

    private void init() {
        setupComponents();
        setListeners();
    }

    private void setupComponents() {
        originalTextArea.setLineWrap(true);

        model.setColumnIdentifiers(HEADERS);
        dictionaryTable.setModel(model);

        final TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
        sorter.setSortsOnUpdates(true);

        dictionaryTable.setRowSorter(sorter);

        model.addTableModelListener(e -> {
            if (e.getColumn() == Constants.WORD) {
                controller.notifyTableChanged(model.getDataVector());
                updateTable();
            }
        });
        model.setDataVector(controller.getDataInVector(), HEADERS);
    }

    private void setListeners() {
        setHandlerListener();
        setCleanerListener();
        setAdderListener();
        setSearcherListener();
    }

    private void setFrame() {
        setSize(1500, 700);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    private void setHandlerListener() {
        handler.addActionListener(listener -> {
            if (!originalTextArea.getText().equals("")) {
                new Thread(() -> {
                    bar.setIndeterminate(true);

                    handler.setEnabled(false);

                    controller.pushFile(file);
                    controller.addTextAndRewriteFile(originalTextArea.getText());
                    controller.handle();

                    model.setDataVector(controller.getDataInVector(), HEADERS);

                    originalTextArea.setText("");

                    file = null;

                    handler.setEnabled(true);

                    bar.setIndeterminate(false);
                }).start();
            }
        });
    }

    private void setCleanerListener() {
        cleaner.addActionListener(listener -> {
            controller.deleteAll();
            updateTable();
        });
    }

    private void setAdderListener() {
        adder.addActionListener(listener -> {
            dialogInputWord.setVisible(true);
        });
    }

    private void setSearcherListener() {
        searcher.addActionListener(listener -> {
            searchFrame.setVisible(true);
        });
    }

    private void updateTable() {
        model.setDataVector(controller.getDataInVector(), HEADERS);
        model.fireTableDataChanged();
    }

    private void setComponents() {
        setLayout(new GridLayout(1, 1));
        add(mainPanel);
        mainPanel.setLayout(new GridBagLayout());

        final JScrollPane scrollerForOriginalTextArea = new JScrollPane(originalTextArea);
        final JScrollPane scrollerForResults = new JScrollPane(dictionaryTable);
        final JPanel leftButtons = new JPanel();
        final JPanel rightButtons = new JPanel();

        final GridLayout borderLayout = new GridLayout(0, 1, 5, 5);
        borderLayout.setVgap(1);
        leftButtons.setLayout(borderLayout);
        leftButtons.add(handler, BorderLayout.NORTH);
        leftButtons.add(adder, BorderLayout.CENTER);
        leftButtons.add(searcher, BorderLayout.SOUTH);

        rightButtons.setLayout(borderLayout);
        rightButtons.add(cleaner);

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
        constraints.weightx = 3;
        constraints.anchor = GridBagConstraints.NORTH;
        mainPanel.add(scrollerForResults, constraints);

        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.insets = new Insets(5, 10, 0, 5);
        constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.fill = GridBagConstraints.NONE;
        mainPanel.add(leftButtons, constraints);

        constraints.gridx = 1;
        constraints.gridy = 1;
        mainPanel.add(rightButtons, constraints);

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
