import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;

/**
 * Компонент-индикатор надёжности пароля.
 */
public class PasswordStrengthMeter extends JPanel {
    private final JProgressBar bar;
    private final JLabel label;

    public PasswordStrengthMeter(JPasswordField passwordField) {
        setLayout(new BorderLayout(5, 0));

        bar = new JProgressBar(0, 100);
        bar.setStringPainted(true);

        label = new JLabel("Strength");

        add(label,   BorderLayout.WEST);
        add(bar,     BorderLayout.CENTER);

        // подписываемся на изменения пароля
        passwordField.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { update(); }
            public void removeUpdate(DocumentEvent e) { update(); }
            public void changedUpdate(DocumentEvent e) { update(); }

            private void update() {
                String pw = new String(passwordField.getPassword());
                int strength = calculateStrength(pw);
                bar.setValue(strength);
                if (strength < 40) {
                    bar.setForeground(Color.RED);
                    bar.setString("Weak");
                } else if (strength < 70) {
                    bar.setForeground(Color.ORANGE);
                    bar.setString("Medium");
                } else {
                    bar.setForeground(new Color(0,128,0));
                    bar.setString("Strong");
                }
            }
        });
    }


    private int calculateStrength(String pw) {
        int score = Math.min(5 * pw.length(), 50);
        if (pw.matches(".*\\d.*"))      score += 15;
        if (pw.matches(".*[A-Z].*"))    score += 15;
        if (pw.matches(".*[^a-zA-Z0-9].*")) score += 20;
        return Math.min(score, 100);
    }
}
