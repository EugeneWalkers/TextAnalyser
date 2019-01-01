package view;

import controller.Controller;
import utilities.TagColorData;
import utilities.TagData;
import utilities.TagsKeeper;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.util.List;

import static utilities.Constants.LINE_SEPARATOR;
import static utilities.Constants.TAG_SEPARATOR;

public class PaintedTextFrame extends JFrame {
    private final JButton ok;
    private final JTextPane input;
    private final Style style;
    private List<String> text;

    {
        ok = new JButton("Готово");
        input = new JTextPane();
        style = input.addStyle("Colorful", null);
    }

    PaintedTextFrame() {
        super("Раскраска");
        setSize(600, 800);
        setLocationRelativeTo(null);
        addComponents();
        setComponents();
        setListeners();
    }

    public List<String> getText() {
        return text;
    }

    public void setText(final List<String> text) {
        this.text = text;
    }

    private void setComponents() {
        input.setEditable(true);
        input.setFont(new Font("FreeMono", Font.PLAIN, 20));
        input.setEditable(false);
        input.setDocument(new DefaultStyledDocument());
        input.setContentType("text/html");
    }

    private void addComponents() {
        final FlowLayout layout = new FlowLayout();

        final JPanel panel = new JPanel();
        panel.setLayout(layout);

        final JScrollPane jScrollPane = new JScrollPane(input);

        add(jScrollPane, BorderLayout.CENTER);

        panel.add(ok);

        add(panel, BorderLayout.PAGE_END);
    }

    private void setListeners() {
        ok.addActionListener(e -> setVisible(false));
    }

    public void draw() {
        input.setText(null);

        new Thread(() -> {
            for (final String word : text) {
                if (!word.equals(LINE_SEPARATOR)) {
                    final String[] separatedWord = word.split(TAG_SEPARATOR);
                    appendText(separatedWord[0]);

                    if (separatedWord.length > 1) {
                        appendText(TAG_SEPARATOR);
                        appendTag(separatedWord[1]);
                    }
                }
                else{
                    appendText(LINE_SEPARATOR);
                }
            }
        }).start();
    }

    private void appendText(final String text) {
        final StyledDocument document = input.getStyledDocument();
        StyleConstants.setForeground(style, Color.BLACK);

        try {
            document.insertString(document.getLength(), text, style);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    private void appendTag(final String tag) {
        final StyledDocument document = input.getStyledDocument();
        final TagColorData data = TagsKeeper.getTagData(tag.trim());
        StyleConstants.setForeground(style, data.getColor());

        try {
            document.insertString(document.getLength(), tag, style);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }
}
