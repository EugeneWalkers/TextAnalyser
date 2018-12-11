package view;

import controller.Controller;
import controller.SimpleTableModel;
import utilities.Constants;

import javax.swing.*;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.event.TableModelEvent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;

import static utilities.Constants.HEADERS;

public class DictionaryFrame extends JFrame {

    private static final String DIR = "docs";
    private static DictionaryFrame dictionaryFrame = null;

    private final SimpleTableModel model;
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
    private final JButton painter;
    private final DialogInputWord dialogInputWord;
    private final SearchFrame searchFrame;
    private final TagInterpretator tagInterpretator;
    private final PaintedTextFrame paintedTextFrame;

    private File file;
    private int selectedRow;

    private Controller controller;

    {
        tagInterpretator = new TagInterpretator();
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
        painter = new JButton("Раскрасить текст");
        controller = Controller.getInstance();
        dictionaryTable = new JTable();
        model = new SimpleTableModel();
        paintedTextFrame = new PaintedTextFrame();

        dialogInputWord = new DialogInputWord() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                new Thread(() -> {
                    dialogInputWord.setVisible(false);
                    controller.addWord(dialogInputWord.getString());
                    updateTable();
                }).start();
            }
        };

        searchFrame = new SearchFrame() {
            @Override
            public void tableChanged(TableModelEvent e) {
                super.tableChanged(e);
                DictionaryFrame.this.updateTable();
            }
        };
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

        dictionaryTable.setRowSorter(model.getSorter());

        dictionaryTable.setDefaultRenderer(Integer.class, model.getCenterRenderer());

        dictionaryTable.addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON3) {
                    final JTable aTable = (JTable) e.getSource();
                    selectedRow = aTable.rowAtPoint(e.getPoint());
                }
            }

        });

        final JPopupMenu popupMenu = new JPopupMenu();
        final JMenuItem wordTagDescription = new JMenuItem("Описание тега слова");
        final JMenuItem lemmaWordTagDescription = new JMenuItem("Описание тега начальной формы слова");

        wordTagDescription.addActionListener(e -> {
            tagInterpretator.setData(controller.getData().get(selectedRow).getWordTag());
            tagInterpretator.recalculate();
            tagInterpretator.setVisible(true);
        });

        lemmaWordTagDescription.addActionListener(e -> {
            tagInterpretator.setData(controller.getData().get(selectedRow).getLemmaWordTag());
            tagInterpretator.recalculate();
            tagInterpretator.setVisible(true);

        });
        popupMenu.add(wordTagDescription);
        popupMenu.add(lemmaWordTagDescription);
        popupMenu.addPopupMenuListener(new PopupMenuListener() {
            @Override
            public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        int rowAtPoint = dictionaryTable.rowAtPoint(SwingUtilities.convertPoint(popupMenu, new Point(0, 0), dictionaryTable));
                        if (rowAtPoint > -1) {
                            dictionaryTable.setRowSelectionInterval(rowAtPoint, rowAtPoint);
                        }
                    }
                });
            }

            @Override
            public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {

            }

            @Override
            public void popupMenuCanceled(PopupMenuEvent e) {

            }
        });

        dictionaryTable.setComponentPopupMenu(popupMenu);

        model.addTableModelListener(e -> {
            if (
                    e.getColumn() == Constants.WORD ||
                            e.getColumn() == Constants.TAG_LEMMA ||
                            e.getColumn() == Constants.TAG_WORD) {
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
        setPainterListener();
    }

    private void setFrame() {
        setSize(1820, 980);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    private void setHandlerListener() {
        handler.addActionListener(listener -> {
            if (!originalTextArea.getText().equals("")) {
                new Thread(() -> {
                    setAllButtonsEnabled(false);

                    bar.setIndeterminate(true);

                    controller.pushFile(file);
                    controller.rewriteLastFile(originalTextArea.getText());
                    controller.handle();

                    model.setDataVector(controller.getDataInVector(), HEADERS);

                    originalTextArea.setText("");

                    file = null;

                    bar.setIndeterminate(false);

                    setAllButtonsEnabled(true);
                }).start();
            }
        });
    }

    private void setCleanerListener() {
        new Thread(() -> {
            cleaner.addActionListener(listener -> {
                controller.deleteAll();
                updateTable();
            });
        }).start();
    }

    private void setAdderListener() {
        adder.addActionListener(listener -> {
            dialogInputWord.setVisible(true);
        });
    }

    private void setSearcherListener() {
        searcher.addActionListener(listener -> {
            searchFrame.clear();
            searchFrame.setVisible(true);
        });
    }

    private void setPainterListener() {
        painter.addActionListener(listener -> new Thread(() -> {
            setAllButtonsEnabled(false);
            bar.setIndeterminate(true);

            paintedTextFrame.setText(controller.getPaintedText(originalTextArea.getText()));
            paintedTextFrame.draw();
            paintedTextFrame.setVisible(true);

            bar.setIndeterminate(false);
            setAllButtonsEnabled(true);
        }).start());
    }

    private void setAllButtonsEnabled(final boolean b) {
        adder.setEnabled(b);
        cleaner.setEnabled(b);
        handler.setEnabled(b);
        painter.setEnabled(b);
        searcher.setEnabled(b);
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

        final GridLayout borderLayout = new GridLayout(1, 0, 5, 5);
        borderLayout.setVgap(1);
        leftButtons.setLayout(borderLayout);
        leftButtons.add(handler);
        leftButtons.add(adder);
        leftButtons.add(searcher);
        leftButtons.add(painter);

        rightButtons.setLayout(borderLayout);
        rightButtons.add(cleaner);

        constraints.insets = new Insets(5, 10, 5, 10);
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.anchor = GridBagConstraints.WEST;
        constraints.weightx = 1;
        constraints.weighty = 5;
        constraints.gridwidth = 1;
        mainPanel.add(scrollerForOriginalTextArea, constraints);

        constraints.gridx = 1;
        constraints.gridwidth = 10;
        constraints.weightx = 10;
        constraints.anchor = GridBagConstraints.NORTHEAST;
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
        constraints.weightx = 5;
        mainPanel.add(rightButtons, constraints);

        constraints.insets = new Insets(0, 10, 10, 10);
        constraints.weightx = 1;
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
                setAllButtonsEnabled(false);
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

                setAllButtonsEnabled(true);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
