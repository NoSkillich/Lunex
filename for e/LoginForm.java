import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
public class LoginForm extends ValidForm {
    private JTextField       emailField;
    private JPasswordField   passwordField;
    private ReactionPanel    reaction;

    public LoginForm() {
        super("Login");
        DarkTheme.apply();  // тёмная тема

        setSize(350, 260);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(0, 10));

        // Поля
        add(panel, BorderLayout.CENTER);
        emailField    = addTextField(0, "Email",    "your@example.com", 30);
        passwordField = addPasswordField(1, "Password", "••••••••", 20);

        // Кнопки
        JButton btnLogin   = makeButton("Login");
        JButton btnBack    = makeButton("Back");
        JButton btnRestore = makeButton("Не можете найти акк?");

        btnLogin.addActionListener(e -> doLogin());
        btnBack.addActionListener(e -> {
            new WelcomeForm().setVisible(true);
            dispose();
        });
        btnRestore.addActionListener(e -> {
            new RestoreForm().setVisible(true);
        });

        JPanel pnlButtons = new JPanel(new GridLayout(3, 1, 0, 5));
        pnlButtons.setOpaque(false);
        pnlButtons.add(btnLogin);
        pnlButtons.add(btnBack);
        pnlButtons.add(btnRestore);

        add(pnlButtons, BorderLayout.SOUTH);

        // Панель реакции
        reaction = new ReactionPanel();
        add(reaction, BorderLayout.NORTH);
    }

    private JButton makeButton(String text) {
        JButton b = new JButton(text);
        b.setPreferredSize(new Dimension(140, 26));
        return b;
    }

    private void doLogin() {
        String em = emailField.getText().trim();
        String pw = new String(passwordField.getPassword()).trim();

        // Валидация полей
        if (em.isEmpty() && pw.isEmpty()) {
            reaction.showMessage("Введите email и пароль.", false);
            return;
        }
        if (em.isEmpty()) {
            reaction.showMessage("Введите email.", false);
            return;
        }
        if (pw.isEmpty()) {
            reaction.showMessage("Введите пароль.", false);
            return;
        }

        // Дополнительно можно проверить формат email
        if (!ValidationUtils.isEmailValid(em)) {
            reaction.showMessage("Неверный формат email.", false);
            return;
        }
        if (!AuthManager.authenticate(em, pw)) {
            reaction.showMessage("invalid email or password", false);
            return;
        }

        // Всё прошло — считаем логин успешным
        reaction.showMessage("Login successful!", true);

        // Открываем ContactForm, передавая LocalDateTime.now() как «время регистрации»:
        SwingUtilities.invokeLater(() -> {
            // fn, ln, ph можно передавать пустыми или брать из данных пользователя
            new ContactForm("", "", "", LocalDateTime.now()).setVisible(true);
            dispose();
        });
    }
}
