import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.text.ParseException;

public abstract class ValidForm extends JFrame {
    protected JPanel panel = new JPanel(new GridBagLayout());
    protected GridBagConstraints gbc = new GridBagConstraints();

    public ValidForm(String title) {
        super(title);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        gbc.insets = new Insets(5,5,5,5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(panel);
    }

    protected JTextField addTextField(int y, String label, String placeholder, int maxChars) {
        gbc.gridx = 0; gbc.gridy = y;
        panel.add(new JLabel(label + ":"), gbc);
        gbc.gridx = 1;
        JTextField tf = new JTextField();
        new GhostText(tf, placeholder);
        ((AbstractDocument)tf.getDocument())
            .setDocumentFilter(new DocumentSizeFilter(maxChars));
        panel.add(tf, gbc);
        return tf;
    }

    protected JPasswordField addPasswordField(int y, String label, String placeholder, int maxChars) {
        gbc.gridx = 0; gbc.gridy = y;
        panel.add(new JLabel(label + ":"), gbc);
        gbc.gridx = 1;
        JPasswordField pf = new JPasswordField();
        new GhostText(pf, placeholder);
        ((AbstractDocument)pf.getDocument())
            .setDocumentFilter(new DocumentSizeFilter(maxChars));
        panel.add(pf, gbc);
        return pf;
    }

    protected JFormattedTextField addMaskedField(int y, String label, String mask, String placeholder) {
        gbc.gridx = 0; gbc.gridy = y;
        panel.add(new JLabel(label + ":"), gbc);
        gbc.gridx = 1;
        JFormattedTextField ftf;
        try {
            MaskFormatter mf = new MaskFormatter(mask);
            mf.setPlaceholderCharacter('_');
            ftf = new JFormattedTextField(mf);
            new GhostText(ftf, placeholder);
        } catch (ParseException ex) {
            ftf = new JFormattedTextField();
        }
        panel.add(ftf, gbc);
        return ftf;
    }

    protected JTextArea addTextArea(int y, String label, String placeholder, int maxCharsPerLine, int maxLines) {
        gbc.gridx = 0; gbc.gridy = y; gbc.anchor = GridBagConstraints.NORTH;
        panel.add(new JLabel(label + ":"), gbc);
        gbc.gridx = 1; gbc.weighty = 1; gbc.fill = GridBagConstraints.BOTH;
        JTextArea area = new JTextArea(5, 20);
        new GhostText(area, placeholder);
        ((AbstractDocument)area.getDocument())
            .setDocumentFilter(new LineLengthFilter(maxCharsPerLine, maxLines));
        JScrollPane scroll = new JScrollPane(area);
        panel.add(scroll, gbc);
        gbc.weighty = 0; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.anchor = GridBagConstraints.CENTER;
        return area;
    }

    protected JButton addButton(int y, String text, ActionListener al) {
        gbc.gridx = 1; gbc.gridy = y;
        JButton btn = new JButton(text);
        btn.addActionListener(al);
        panel.add(btn, gbc);
        return btn;
    }

    protected void resetForm(Component[] comps) {
        for (Component c : comps) {
            if (c instanceof JTextField) ((JTextField)c).setText("");
            if (c instanceof JTextArea)  ((JTextArea)c).setText("");
            if (c instanceof JPasswordField) ((JPasswordField)c).setText("");
        }
    }

    // ---------- Filters ----------

    private static class DocumentSizeFilter extends DocumentFilter {
        private final int max;
        DocumentSizeFilter(int max) { this.max = max; }
        @Override
        public void insertString(FilterBypass fb, int offs, String str, AttributeSet a)
                throws BadLocationException {
            if (fb.getDocument().getLength() + str.length() <= max)
                super.insertString(fb, offs, str, a);
            else Toolkit.getDefaultToolkit().beep();
        }
        @Override
        public void replace(FilterBypass fb, int offs, int len, String str, AttributeSet a)
                throws BadLocationException {
            if (fb.getDocument().getLength() - len + str.length() <= max)
                super.replace(fb, offs, len, str, a);
            else Toolkit.getDefaultToolkit().beep();
        }
    }

    private static class LineLengthFilter extends DocumentFilter {
        private final int maxLine, maxRows;
        LineLengthFilter(int maxLine, int maxRows) { this.maxLine = maxLine; this.maxRows = maxRows; }
        @Override
        public void insertString(FilterBypass fb, int offs, String str, AttributeSet a)
                throws BadLocationException {
            StringBuilder sb = new StringBuilder(fb.getDocument().getText(0, fb.getDocument().getLength()));
            sb.insert(offs, str);
            if (acceptable(sb.toString())) super.insertString(fb, offs, str, a);
            else Toolkit.getDefaultToolkit().beep();
        }
        @Override
        public void replace(FilterBypass fb, int offs, int len, String str, AttributeSet a)
                throws BadLocationException {
            StringBuilder sb = new StringBuilder(fb.getDocument().getText(0, fb.getDocument().getLength()));
            sb.replace(offs, offs + len, str);
            if (acceptable(sb.toString())) super.replace(fb, offs, len, str, a);
            else Toolkit.getDefaultToolkit().beep();
        }
        private boolean acceptable(String text) {
            String[] lines = text.split("\\R", -1);
            if (lines.length > maxRows) return false;
            for (String ln : lines) if (ln.length() > maxLine) return false;
            return true;
        }
    }

    // Placeholder support
    private static class GhostText extends FocusAdapter {
        private final JTextComponent comp;
        private final String ghost;
        private boolean showing = true;

        GhostText(JTextComponent comp, String ghost) {
            this.comp = comp;
            this.ghost = ghost;
            comp.setText(ghost);
            comp.setForeground(Color.GRAY);
            comp.addFocusListener(this);
        }
        @Override
        public void focusGained(FocusEvent e) {
            if (showing) {
                comp.setText("");
                comp.setForeground(Color.BLACK);
                showing = false;
            }
        }
        @Override
        public void focusLost(FocusEvent e) {
            if (comp.getText().isEmpty()) {
                comp.setText(ghost);
                comp.setForeground(Color.GRAY);
                showing = true;
            }
        }
    }
}
