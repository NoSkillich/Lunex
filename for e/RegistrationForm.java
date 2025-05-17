import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;

public class RegistrationForm extends ValidForm {
    static {
        try {
            UIManager.setLookAndFeel(new NimbusLookAndFeel());
            UIManager.put("control", new Color(50, 50, 50));
            UIManager.put("info", new Color(60, 60, 60));
            UIManager.put("nimbusBase", new Color(30, 30, 30));
            UIManager.put("nimbusAlertYellow", new Color(248,187,0));
            UIManager.put("nimbusDisabledText", new Color(128, 128, 128));
            UIManager.put("nimbusFocus", new Color(115, 164, 209));
            UIManager.put("nimbusGreen", new Color(176, 179, 50));
            UIManager.put("nimbusInfoBlue", new Color(66, 139, 221));
            UIManager.put("nimbusLightBackground", new Color(50, 50, 50));
            UIManager.put("nimbusOrange", new Color(191, 98, 4));
            UIManager.put("nimbusRed", new Color(169, 46, 34));
            UIManager.put("nimbusSelectedText", new Color(255, 255, 255));
            UIManager.put("nimbusSelectionBackground", new Color(104, 93, 156));
            UIManager.put("text", new Color(230, 230, 230));
        } catch(UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
    }
    private JTextField firstName, lastName, email;
    private JFormattedTextField phone;
    private JPasswordField password;
    private ReactionPanel reaction;
    // <-- Объявляем массив полей для сброса
    private Component[] fields;

    public RegistrationForm() {
        super("Registration");
        setSize(400, 380);
        setLayout(new BorderLayout());

        // Добавляем panel из ValidForm на центр
        add(panel, BorderLayout.CENTER);

        // Создаём поля
        firstName = addTextField(0, "First Name", "Your first name", 15);
        HistoryManager.attach(firstName, "firstName");
        lastName  = addTextField(1, "Last Name",  "Your last name", 15);
        HistoryManager.attach(lastName, "lastName");
        email     = addTextField(2, "Email",      "example@mail.com", 30);
        phone     = addMaskedField(3, "Phone", "+7 (###) ###-##-##", "+7 (___) ___-__-__");
        password  = addPasswordField(4, "Password", "••••••••", 10);

        // Инициализируем массив полей
        fields = new Component[]{ firstName, lastName, email, phone, password };

        // Кнопки
        addButton(5, "Register", e -> doRegister());
        addButton(6, "Reset",    e -> resetForm(fields));

        // Панель реакции — снизу
        reaction = new ReactionPanel();
        add(reaction, BorderLayout.SOUTH);
    }

    private void doRegister() {
        String fn = firstName.getText().trim();
        String ln = lastName.getText().trim();
        String em = email.getText().trim();
        String ph = phone.getText();
        String pw = new String(password.getPassword());

        // Валидация (используйте ValidationUtils при наличии)
        if (fn.isEmpty() || ln.isEmpty() || em.isEmpty() || pw.isEmpty() || ph.contains("_")) {
            reaction.showMessage("Please fill in all fields correctly.", false);
            return;
        }

        // Успешная регистрация
        reaction.showMessage("Registration successful!", true);
        // Переход к ContactForm
        SwingUtilities.invokeLater(() -> {
            dispose();
            new ContactForm(fn, ln, ph).setVisible(true);
        });
    }
}
