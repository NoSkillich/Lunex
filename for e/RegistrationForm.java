// RegistrationForm.java
import javax.swing.*;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;
import java.awt.*;
import java.time.LocalDateTime;

public class RegistrationForm extends ValidForm {
    static {
        try {
            UIManager.setLookAndFeel(new NimbusLookAndFeel());
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
    }

    private JTextField           firstName, lastName, email;
    private JFormattedTextField  phone;
    private JPasswordField       password;
    private PasswordStrengthMeter passwordStrengthMeter;
    private DateOfBirthPicker    dateOfBirth;
    private ReactionPanel        reaction;
    private Component[]          fields;
    private ScheduledPostDialog  schedDlg;

    public RegistrationForm() {
        super("Registration");
        setSize(400, 540);
        setLayout(new BorderLayout(0, 10));

        // —— Заголовок приложения (сверху) ——
        JLabel titleLabel = new JLabel("MyApp Registration", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Serif", Font.BOLD | Font.ITALIC, 24));
        titleLabel.setForeground(new Color(45, 45, 45));
        add(titleLabel, BorderLayout.NORTH);

        // —— Центральная панель полей ——
        add(panel, BorderLayout.CENTER);

        // 1) Вводные поля
        firstName = addTextField(0, "First Name", "Your first name", 15);
        lastName  = addTextField(1, "Last Name",  "Your last name", 15);
        email     = addTextField(2, "Email",      "example@mail.com", 30);
        phone     = addMaskedField(3, "Phone", "+7 (###) ###-##-##", "+7 (___) ___-__-__");
        password  = addPasswordField(4, "Password", "••••••••", 10);

        // 2) Индикатор надёжности пароля (gridy = 5)
        gbc.gridx     = 0;
        gbc.gridy     = 5;
        gbc.gridwidth = 2;
        gbc.anchor    = GridBagConstraints.CENTER;
        passwordStrengthMeter = new PasswordStrengthMeter(password);
        panel.add(passwordStrengthMeter, gbc);

        // 3) Поле «Дата рождения» (gridy = 6)
        gbc.gridy = 6;
        dateOfBirth = new DateOfBirthPicker();
        panel.add(dateOfBirth, gbc);

        // 4) Сбрасываем gridwidth и anchor, собираем массив полей для Reset
        gbc.gridwidth = 1;
        gbc.anchor    = GridBagConstraints.WEST;
        fields = new Component[]{ firstName, lastName, email, phone, password };

        // 5) Кнопки Register / Reset (gridy = 7)
        gbc.gridy = 7;
        gbc.gridx = 0;
        addButton(7, "Register", e -> doRegister());
        gbc.gridx = 1;
        addButton(8, "Reset",    e -> resetForm(fields));

        // 6) Кнопка AddPost (gridy = 8, gridx = 0) — для примера
        gbc.gridy = 8;
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.WEST;
        JButton postBtn = new JButton("Add Post");
        postBtn.addActionListener(e -> doPost());
        panel.add(postBtn, gbc);

        // 7) Панель реакции (внизу)
        reaction = new ReactionPanel();
        add(reaction, BorderLayout.SOUTH);

        // 8) Адаптивный дизайн
        AdaptiveLayout.install(this, 400);
    }

    /**
     * Метод, вызываемый при нажатии на кнопку «Register».
     * Добавлена проверка длины пароля: минимум 6, максимум 14 символов.
     * После успешной регистрации добавляем новое имя в UserStore.
     */
    private void doRegister() {
        String fn = firstName.getText().trim();
        String ln = lastName.getText().trim();
        String em = email.getText().trim();
        String ph = phone.getText().trim();
        String pw = new String(password.getPassword()).trim();

        // 1) Базовая проверка на пустые поля
        if (fn.isEmpty() || ln.isEmpty() || em.isEmpty() || pw.isEmpty() || ph.contains("_")) {
            reaction.showMessage("Please fill in all fields correctly.", false);
            return;
        }

        // 2) Проверка длины пароля
        if (pw.length() < 6 || pw.length() > 14) {
            reaction.showMessage("Password must be between 6 and 14 characters.", false);
            return;
        }

        // 3) Проверка формата email
        if (!EmailValidator.isFormatValid(em)) {
            reaction.showMessage("Invalid email format.", false);
            return;
        }
        if (!EmailValidator.domainExists(em)) {
            reaction.showMessage("Email domain does not exist.", false);
            return;
        }

        // 4) Попытка зарегистрировать пользователя в AuthManager (ваша логика)
        if (!AuthManager.register(em, pw)) {
            reaction.showMessage("This email is already registered.", false);
            return;
        }

        // 5) Успешная регистрация
        reaction.showMessage("Registration successful!", true);

        // 6) Добавляем имя пользователя (firstName) в наш статический UserStore
        UserStore.addUser(fn);

        // 7) Запоминаем момент регистрации
        LocalDateTime registrationTime = LocalDateTime.now();

        // 8) Открываем ContactForm (передаём fn, ln, ph, registrationTime)
        SwingUtilities.invokeLater(() -> {
            new ContactForm(fn, ln, ph, registrationTime).setVisible(true);
            dispose();
        });
    }

    /** Логика «Add Post» — просто пример, не влияет на UserStore. */
    private void doPost() {
        AddPostDialog dlg = new AddPostDialog(this);
        dlg.setVisible(true);
        if (!dlg.isSubmitted()) return;

        String content = dlg.getPostText();
        String type    = dlg.getPostType();
        if (content.isEmpty()) {
            reaction.showMessage("Cannot post empty message.", false);
            return;
        }

        System.out.println("New post [" + type + "]: " + content);
        reaction.showMessage("Posted a new " + type + " post!", true);
    }

    // Вложенный класс AddPostDialog (остался без изменений)…
    private static class AddPostDialog extends JDialog {
        private final JTextArea         textArea;
        private final JComboBox<String> typeCombo;
        private boolean                 submitted = false;

        public AddPostDialog(JFrame parent) {
            super(parent, "Create New Post", true);
            setSize(400, 300);
            setLocationRelativeTo(parent);
            setLayout(new BorderLayout(5,5));

            // 1) Выбор типа поста
            JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
            top.add(new JLabel("Post Type:"));
            typeCombo = new JComboBox<>(new String[]{"Entertainment", "Educational"});
            top.add(typeCombo);
            add(top, BorderLayout.NORTH);

            // 2) Текст поста
            textArea = new JTextArea(8, 30);
            textArea.setLineWrap(true);
            textArea.setWrapStyleWord(true);
            add(new JScrollPane(textArea), BorderLayout.CENTER);

            // 3) Кнопки Post / Cancel
            JPanel btns = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            JButton btnSubmit = new JButton("Post");
            JButton btnCancel = new JButton("Cancel");
            btns.add(btnSubmit);
            btns.add(btnCancel);
            add(btns, BorderLayout.SOUTH);

            btnSubmit.addActionListener(e -> {
                submitted = true;
                dispose();
            });
            btnCancel.addActionListener(e -> dispose());
        }

        public boolean isSubmitted()       { return submitted; }
        public String  getPostText()       { return textArea.getText().trim(); }
        public String  getPostType()       { return (String) typeCombo.getSelectedItem(); }
    }
}
