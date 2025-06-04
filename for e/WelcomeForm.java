import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class WelcomeForm extends JFrame {
    public WelcomeForm() {
        super("Welcome");
        DarkTheme.apply();

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(360, 420);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(0, 10));

        // 1) Заголовок
        JLabel title = new JLabel("Welcome!", SwingConstants.CENTER);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 20f));
        title.setForeground(Color.WHITE);
        add(title, BorderLayout.NORTH);

        // 2) Информационные вопросы
        JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        infoPanel.setOpaque(false);
        String[] questions = {
            "What is this app for?",
            "Who can use it?",
            "Who created it?"
        };
        String[] answers = {
            "<html>This application helps you manage and submit contact requests.<br>"
          + "It provides registration, login and messaging functionality.</html>",
            "Anyone who needs a simple GUI form to register and send messages<br>"
          + "— no special permissions required.",
            "Developed by Daryn in 2025."
        };
        for (int idx = 0; idx < questions.length; idx++) {
            final String qText = questions[idx];
            final String ansText = answers[idx];
            JButton q = new JButton(qText);
            q.setBorderPainted(false);
            q.setContentAreaFilled(false);
            q.setForeground(Color.CYAN);
            q.addActionListener(ev ->
                JOptionPane.showMessageDialog(
                    WelcomeForm.this,
                    ansText,
                    qText,
                    JOptionPane.INFORMATION_MESSAGE
                )
            );
            infoPanel.add(q);
        }
        add(infoPanel, BorderLayout.CENTER);

        // 3) Кнопки Register / Login / Guest + Footer
        JPanel btnPanel = new JPanel(new GridLayout(1, 3, 10, 0));
        btnPanel.setOpaque(false);
        btnPanel.add(makeButton("Register", e -> openAndDispose(new RegistrationForm())));
        btnPanel.add(makeButton("Login",    e -> openAndDispose(new LoginForm())));
        btnPanel.add(makeButton("Continue as Guest", e -> openAndDispose(new GuestDashboard())));

        JLabel footer = new JLabel("© 2025 Made by Daryn. All rights reserved, extended", SwingConstants.CENTER);
        footer.setForeground(Color.LIGHT_GRAY);
        footer.setFont(footer.getFont().deriveFont(10f));

        JPanel south = new JPanel(new BorderLayout(0, 5));
        south.setOpaque(false);
        south.add(btnPanel, BorderLayout.NORTH);
        south.add(footer,  BorderLayout.SOUTH);

        add(south, BorderLayout.SOUTH);

        getContentPane().setBackground(new Color(45, 45, 45));
    }

    private void openAndDispose(JFrame next) {
        next.setVisible(true);
        dispose();
    }

    private JButton makeButton(String text, ActionListener l) {
        JButton b = new JButton(text);
        b.setPreferredSize(new Dimension(110, 30));
        b.addActionListener(l);
        return b;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new WelcomeForm().setVisible(true));
    }
}
