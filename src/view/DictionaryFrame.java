package view;

import controller.Controller;
import controller.SimpleTableModel;
import utilities.Constants;
import utilities.DataKeeper;

import javax.swing.*;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.event.TableModelEvent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

import static utilities.Constants.HEADERS;

import static utilities.StringUtilities.tagListToStringListByName;

public class DictionaryFrame extends JFrame {

    private static final String DIR = "docs";
    private static final String STATUS_NOT_FOUND = "not found";
    private static final String STATUS_LOADING = "loading";
    private static final String STATUS_OPENED = "opened";
    private static final String STATUS_EXIST = "exist";

    private static DictionaryFrame dictionaryFrame = null;
    private final JLabel info;
    private final SimpleTableModel model;
    private final JTable dictionaryTable;
    private final JFileChooser chooser;
    private final JProgressBar bar;
    private final JButton handler;
    private final JButton cleaner;
    private final JButton adder;
    private final JButton searcher;
    private final JButton painter;
    private final JButton allTags;
    private final DialogInputWord dialogInputWord;
    private final SearchFrame searchFrame;
    private final TagInterpretator tagInterpretator;
    private final PaintedTextFrame paintedTextFrame;
    private final StringBuilder text;
    private final JPanel files;
    private final JButton stat;
    private final JButton tagStat;
    private final JButton tagPairStat;
    private final StatisticsFrame stats;
    private final TagStatisticsFrame tagStats;
    private final TagPairsStatisticsFrame tagPairStats;

    private File file;
    private int selectedRow;

    private Controller controller;

    {
        files = new JPanel();
        allTags = new JButton("Посмотреть теги");
        info = new JLabel();
        text = new StringBuilder();
        tagInterpretator = new TagInterpretator();
        chooser = new JFileChooser(DIR);
        bar = new JProgressBar();
        stat = new JButton("Статистика");
        tagStat = new JButton("Статистика по тегам");
        tagPairStat = new JButton("Статистика по парам тегов");
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

        stats = new StatisticsFrame();
        tagStats = new TagStatisticsFrame();
        tagPairStats = new TagPairsStatisticsFrame();

        setAllButtonsEnabled(false);
        cleaner.setEnabled(true);
        stats.setEnabled(true);
        tagStats.setEnabled(true);
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
        info.setOpaque(true);
        redrawInfo(STATUS_NOT_FOUND);

        files.setLayout(new BoxLayout(files, BoxLayout.Y_AXIS));
        stat.addActionListener(e ->{
            stats.recalculate();
            stats.setVisible(true);
        });
        tagStat.addActionListener(e ->{
            tagStats.recalculate();
            tagStats.setVisible(true);
        });
        tagPairStat.addActionListener(e->{
            bar.setIndeterminate(true);

            new Thread(() -> {
                tagPairStats.recalculate();
                tagPairStats.setVisible(true);
                bar.setIndeterminate(false);
            }).start();
        });


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
            tagInterpretator.setData(tagListToStringListByName(controller.getData().get(selectedRow).getWordTag()));
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

        //dictionaryTable.setComponentPopupMenu(popupMenu);

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
        setAllTagsListener();
    }

    private void setFrame() {
        setSize(1366, 730);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    private void setHandlerListener() {
        handler.addActionListener(listener -> {
            if (!text.toString().equals("")) {
                bar.setIndeterminate(true);

                new Thread(() -> {
                    setAllButtonsEnabled(false);

                    controller.handle();

                    model.setDataVector(controller.getDataInVector(), HEADERS);

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

    private void setAllTagsListener() {
        allTags.addActionListener(e -> {
            FullTagHelper.getInstance().setVisible(true);
        });
    }

    private void setPainterListener() {
        painter.addActionListener(listener -> {
            bar.setIndeterminate(true);

            new Thread(() -> {
                setAllButtonsEnabled(false);

                paintedTextFrame.setText(controller.getPaintedText(text.toString()));
                paintedTextFrame.draw();
                paintedTextFrame.setVisible(true);

                bar.setIndeterminate(false);
                setAllButtonsEnabled(true);
            }).start();
        });
    }

    private void setAllButtonsEnabled(final boolean b) {
        adder.setEnabled(b);
        cleaner.setEnabled(b);
        handler.setEnabled(b);
        painter.setEnabled(b);
        searcher.setEnabled(b);
//        allTags.setEnabled(b);
        stats.setEnabled(b);
    }

    private void updateTable() {
        model.setDataVector(controller.getDataInVector(), HEADERS);
        model.fireTableDataChanged();
    }


    private void setComponents() {

        final JScrollPane scrollerForResults = new JScrollPane(dictionaryTable);
        final JPanel right = new JPanel();
        final JPanel buttons = new JPanel();

        right.setLayout(new BorderLayout());

        final GridLayout borderLayout = new GridLayout(0, 1, 10, 10);
        borderLayout.setVgap(1);
        buttons.setLayout(borderLayout);
        buttons.add(handler);
        buttons.add(adder);
        //buttons.add(searcher);
        buttons.add(allTags);
        buttons.add(painter);
        buttons.add(stat);
        buttons.add(tagStat);
        buttons.add(tagPairStat);

        add(scrollerForResults, BorderLayout.CENTER);

        right.add(buttons, BorderLayout.NORTH);

        final JScrollPane paneWithFiles = new JScrollPane(files);
        right.add(paneWithFiles, BorderLayout.CENTER);

        right.add(cleaner, BorderLayout.SOUTH);

        add(bar, BorderLayout.PAGE_END);

        add(info, BorderLayout.PAGE_START);

        add(right, BorderLayout.EAST);
    }

    private void redrawInfo(final String status) {
        switch (status) {
            case STATUS_LOADING:
                info.setText("Открывается файл " + file.getName());
                info.setBackground(Color.YELLOW);
                break;

            case STATUS_OPENED:
                info.setText("Открыт файл " + file.getName());
                info.setBackground(Color.GREEN);
                break;

            case STATUS_EXIST:
//                info.setText("Файл " + file.getName() + " уже открыт");
//                info.setBackground(Color.YELLOW);
//                file = controller.pullFile();
//                try {
//                    Thread.sleep(5000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                redrawInfo(STATUS_OPENED);
                break;

            default:
            case STATUS_NOT_FOUND:
                info.setText("Файл не открыт");
                info.setBackground(Color.RED);
                break;

        }
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

    private void appendToFiles(final String fileName){
        final JLabel newLabel = new JLabel(fileName);
        files.add(newLabel);

        newLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(final MouseEvent e) {
                super.mousePressed(e);
                System.out.println("Смена файла");
                file = controller.findFileByName(newLabel.getText());
                selectFile();
            }
        });

        redrawInfo(STATUS_OPENED);
    }

    private void selectFile(){
        text.setLength(0);
        text.append(DataKeeper.readTextFromFile(file));
        setAllButtonsEnabled(true);
        redrawInfo(STATUS_OPENED);
    }

    private void addFile(){
        if (!controller.isFileContained(file)){
            controller.pushFile(file);
            selectFile();
            appendToFiles(file.getName());
        }
        else{
            redrawInfo(STATUS_EXIST);
        }
    }

    private void showFileChooser() {
        int res = chooser.showDialog(null, "Открыть файл");
        switch (res) {
            case JFileChooser.APPROVE_OPTION:
                file = chooser.getSelectedFile();

                setAllButtonsEnabled(false);
                text.setLength(0);

                redrawInfo(STATUS_LOADING);

                addFile();
                break;

            case JFileChooser.CANCEL_OPTION:

                break;

        }
    }

}
