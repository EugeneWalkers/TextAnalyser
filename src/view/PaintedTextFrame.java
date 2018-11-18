package view;

import utilities.TagColorData;
import utilities.TagsKeeper;

import javax.swing.*;
import javax.swing.text.*;
import javax.swing.text.html.HTMLDocument;
import java.awt.*;
import java.io.IOException;
import java.util.List;

import static utilities.Constants.TAG_SEPARATOR;

public class PaintedTextFrame extends JFrame{
    private final JButton ok;
    private final JButton help;
    private final JTextPane input;
    private final Style style;

    private List<String> text;
    private Thread thread = null;

    {
        ok = new JButton("Готово");
        help = new JButton("Показать подсказку");
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

    public void setText(List<String> text) {
        this.text = text;
    }

    private void setComponents() {
        //input.setFont(new Font("FreeMono", Font.PLAIN, 20));
        input.setEditable(false);
        //input.setDocument(new DefaultStyledDocument());
        input.setContentType("text/html");
    }

    private void addComponents() {
        final BorderLayout mainBorderLayout = new BorderLayout();
        final GridBagLayout gridBagLayout = new GridBagLayout();
        final GridBagConstraints constraints = new GridBagConstraints();

        setLayout(mainBorderLayout);

        final JPanel panel = new JPanel();
        panel.setLayout(gridBagLayout);

        final JScrollPane jScrollPane = new JScrollPane(input);

        constraints.insets = new Insets(10, 10, 5, 10);
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.anchor = GridBagConstraints.NORTH;
        constraints.weightx = 1;
        constraints.weighty = 5;
        constraints.gridwidth = 2;
        panel.add(jScrollPane, constraints);

        constraints.insets = new Insets(5, 10, 10, 10);
        constraints.gridy = 1;
        constraints.weighty = 1;
        constraints.gridwidth = 1;
        constraints.fill = GridBagConstraints.NONE;
        constraints.anchor = GridBagConstraints.SOUTHWEST;
        panel.add(help, constraints);

        constraints.gridx = 1;
        constraints.anchor = GridBagConstraints.SOUTHEAST;
        panel.add(ok, constraints);


        add(panel, BorderLayout.CENTER);
    }

    private void setListeners() {
        ok.addActionListener(e -> {
            setVisible(false);
            if (thread != null){
                thread.interrupt();
            }
        });
        help.addActionListener(e -> FullTagHelper.getInstance().setVisible(true));
    }

    public void draw() {
        input.setText("<html><p>");
        //appendSimpleText("<html><p>");

        thread = new Thread(new Runnable() {
            @Override
            public void run() {

                for (final String word : text) {
                    if (word.equals("\n")){
                        appendSeparator();
                    } else{
                        final String[] separatedWord = word.split(TAG_SEPARATOR);
                        appendText("<b style=\"color:black\">"+ separatedWord[0]+"</b>");

                        if (separatedWord.length > 1){
                            appendText(TAG_SEPARATOR);
                            appendTag(separatedWord[1]);
                        }
                    }


                }


                appendSimpleText("</p></html>");
                thread = null;
            }
        });

        thread.start();
    }

    private void appendSimpleText(final String text){
        final HTMLDocument doc = (HTMLDocument)input.getStyledDocument();

        try {
            doc.insertAfterEnd(doc.getCharacterElement(doc.getLength()), text);
        } catch (BadLocationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void appendSeparator(){
        final HTMLDocument doc = (HTMLDocument)input.getStyledDocument();

        try {
            doc.insertAfterEnd(doc.getCharacterElement(doc.getLength()), "<br/>");
        } catch (BadLocationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void appendText(final String text) {
        final HTMLDocument doc = (HTMLDocument)input.getStyledDocument();

        try {
            doc.insertAfterEnd(doc.getCharacterElement(doc.getLength()), "<b style=\"color:black\">"+ text+"</b>");
        } catch (BadLocationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //input.setText(input.getText() + "<b style=\"color:black\">"+ text+"</b>");
//        final StyledDocument document = input.getStyledDocument();
//        StyleConstants.setForeground(style, Color.BLACK);
//
//        try {
//            document.insertString(document.getLength(), text, style);
//        } catch (BadLocationException e) {
//            e.printStackTrace();
//        }
    }

    private void appendTag(final String tag) {
        final HTMLDocument doc = (HTMLDocument)input.getStyledDocument();
        final TagColorData data = TagsKeeper.getTagData(tag.trim());

        System.out.println(
                Integer.toHexString(data.getColor().getRGB()).substring(2)
        );

        System.out.println(
                data.getColor().getRed() +":"+data.getColor().getGreen() + ":" + data.getColor().getBlue()
        );


        try {
            doc.insertAfterEnd(
                    doc.getCharacterElement(doc.getLength()),
                    "<b style=\"color: #" +
                            Integer.toHexString(data.getColor().getRGB()).substring(2) +
                            "\">"+ tag+"</b>");
        } catch (BadLocationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
//        final StyledDocument document = input.getStyledDocument();
//        StyleConstants.setForeground(style, data.getColor());
//
//        try {
//            document.insertString(document.getLength(), tag, style);
//        } catch (BadLocationException e) {
//            e.printStackTrace();
//        }

        //input.setText(input.getText() + "<b style=\"color: #" + Integer.toHexString(data.getColor().getRGB()) + "\">"+ tag+"</b>");
    }
}
