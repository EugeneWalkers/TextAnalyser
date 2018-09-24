import javax.swing.*;
import java.awt.*;
import java.io.*;

class Frame extends JFrame {

    private static final String DIR = "docs";
    private static Frame frame = null;
    private JFileChooser chooser = null;
    private JTextArea originalTextArea;
    private JTextArea resultsTextArea;
    private ReaderFromFile reader = null;
    private JProgressBar bar = null;
    private GridBagConstraints constraints = null;
    private JPanel mainPanel = null;

    private JRadioButton up;
    private JRadioButton down;
    private JRadioButton byName;
    private JRadioButton byPopularity;

    private File file;

    private MapReduce mapReduce;

    private Frame() {
        super("Text Analyzer");
        setFrame();
        init();
        setComponents();
        setMenu();
    }

    static Frame getInstance() {
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

    private void init() {
        chooser = new JFileChooser(DIR);
        reader = new ReaderFromFile();
        constraints = new GridBagConstraints();
        bar = new JProgressBar();
        //bar.setIndeterminate(true);
        mainPanel = new JPanel();
        up = new JRadioButton("По возрастанию");
        up.setSelected(true);
        down = new JRadioButton("По убыванию");
        byName = new JRadioButton("По алфавиту");
        byName.setSelected(true);
        byPopularity = new JRadioButton("По частоте использования");
        resultsTextArea = new JTextArea();
        resultsTextArea.setEditable(false);
        resultsTextArea.setLineWrap(true);

        ButtonGroup typeOfSort = new ButtonGroup();
        typeOfSort.add(byName);
        typeOfSort.add(byPopularity);

        ButtonGroup valueOfSort = new ButtonGroup();
        valueOfSort.add(up);
        valueOfSort.add(down);

        mapReduce = new MapReduce();
    }

    private void setComponents() {
        setLayout(new GridLayout(1, 1));
        add(mainPanel);
        mainPanel.setLayout(new GridBagLayout());

        originalTextArea = new JTextArea();
        originalTextArea.setLineWrap(true);
        //originalTextArea.setEditable(false);
        //originalTextArea.setEditable(false);
        JScrollPane scrollerForOriginalTextArea = new JScrollPane(originalTextArea);
        JScrollPane scrollerForResultsTextArea = new JScrollPane(resultsTextArea);
        JButton handler = new JButton("Handle");
        handler.addActionListener(listener -> {
            new Thread(() -> {
                bar.setIndeterminate(true);

                resultsTextArea.setText("");

                if (file == null) {
                    mapReduce.setText(originalTextArea.getText());
                }

                mapReduce.handle(byName.isSelected(), up.isSelected());

                int r = resultsTextArea.getText().length();
                resultsTextArea.select(r - 1, r);
                resultsTextArea.cut();

                bar.setIndeterminate(false);
            }).start();

        });

        JPanel radioButtons = new JPanel();
        radioButtons.setLayout(new GridLayout(2, 2, 5, 5));
        radioButtons.add(byName);
        radioButtons.add(up);
        radioButtons.add(byPopularity);
        radioButtons.add(down);

        constraints.insets = new Insets(5, 10, 5, 10);
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.anchor = GridBagConstraints.NORTH;
        constraints.weightx = 1;
        constraints.weighty = 5;
        constraints.gridwidth = 2;
        mainPanel.add(scrollerForOriginalTextArea, constraints);

        constraints.gridx = 1;
        constraints.gridwidth = 1;
        //mainPanel.add(scrollerForResultsTextArea, constraints); // TODO: write a table for results

        constraints.gridx = 0;
        constraints.insets = new Insets(5, 10, 0, 5);
        constraints.weighty = 1;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.gridy = 1;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.fill = GridBagConstraints.NONE;
        mainPanel.add(handler, constraints);

        constraints.gridx = 1;
        constraints.insets = new Insets(5, 5, 0, 5);
        mainPanel.add(radioButtons, constraints);

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
                mapReduce.setFile(file);
                originalTextArea.setText("");
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

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
