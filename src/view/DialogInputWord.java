package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public abstract class DialogInputWord extends JFrame implements ActionListener {

    private final JButton ok;
    private final JTextArea input;

    {
        ok = new JButton("Готово");
        input = new JTextArea();
    }

    public DialogInputWord() {
        super("Добавить слово");
        setSize(300, 150);
        setLocationRelativeTo(null);
        addComponents();
        setComponents();
        setListeners();
    }

    private void setComponents() {
        input.setLineWrap(true);
    }

    private void addComponents() {
        final BorderLayout mainBorderLayout = new BorderLayout();
        final GridBagLayout gridBagLayout = new GridBagLayout();
        final GridBagConstraints constraints = new GridBagConstraints();

        mainBorderLayout.setVgap(4);

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
        constraints.weighty = 3;
        constraints.gridwidth = 1;
        panel.add(jScrollPane, constraints);

        constraints.insets = new Insets(5, 10, 10, 10);
        constraints.gridy = 1;
        constraints.weighty = 1;
        constraints.fill = GridBagConstraints.NONE;
        constraints.anchor = GridBagConstraints.SOUTH;
        panel.add(ok, constraints);

        add(panel, BorderLayout.CENTER);
    }

    private void setListeners() {
        ok.addActionListener(this);
    }

    public String getString() {
        final StringBuilder word = new StringBuilder(input.getText().toLowerCase().replaceAll("[\\W,\n]", ""));
        input.setText(null);
        return word.toString();
    }

}
