// RegistrationForm.java
import javax.swing.*;
import javax.swing.event.DocumentListener;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;
import java.awt.*;
import javax.swing.event.DocumentEvent;
import java.time.LocalDateTime;

/**
 * Форма регистрации с кнопкой «Open PostForm», которая открывает
 * вложенный диалог PostForm для ввода «Message To», «Message From»,
 * «Tag Friend Link» и «Message to write».
 */
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
        setSize(400, 580);
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

        // 6) Кнопка AddPost (gridy = 8, gridx = 0) — просто пример
        gbc.gridy = 8;
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.WEST;
        JButton postBtn = new JButton("Add Post");
        postBtn.addActionListener(e -> doPost());
        panel.add(postBtn, gbc);

        // 7) Новая кнопка «PostForm» (gridy = 9, gridx = 0)
        gbc.gridy = 9;
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.WEST;
        JButton btnPostForm = new JButton("Open PostForm");
        btnPostForm.addActionListener(e -> openPostForm());
        panel.add(btnPostForm, gbc);

        // 8) Панель реакции (внизу)
        reaction = new ReactionPanel();
        add(reaction, BorderLayout.SOUTH);

        // 9) Адаптивный дизайн
        AdaptiveLayout.install(this, 400);
    }

    /**
     * Вспомогательный метод-переход:
     * открывает модальное окно PostForm.
     */
    private void openPostForm() {
        PostForm dlg = new PostForm(this);
        dlg.setVisible(true);
        // Если пользователь нажал «Send» и все поля заполнены, dlg.isSubmitted() == true
        if (dlg.isSubmitted()) {
            // При желании можно получить то, что ввёл пользователь:
            String toText   = dlg.getTo();
            String fromText = dlg.getFrom();
            String tagLink  = dlg.getTagLink();
            String msgText  = dlg.getMessageText();
            reaction.showMessage("Message sent to: " + toText + "\nTag: " + tagLink, true);
        }
    }

    /**
     * Метод, вызываемый при нажатии на кнопку «Register».
     * Проверяет все поля, регистрирует (логика AuthManager).
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

        // 6) Запоминаем момент регистрации
        LocalDateTime registrationTime = LocalDateTime.now();

        // 7) Открываем ContactForm (передаём fn, ln, ph, registrationTime)
        SwingUtilities.invokeLater(() -> {
            new ContactForm(fn, ln, ph, registrationTime).setVisible(true);
            dispose();
        });
    }

    /** Логика «Add Post» (демо) */
    private void doPost() {
        JOptionPane.showMessageDialog(
            this,
            "This is just a placeholder for \"Add Post\".",
            "Info",
            JOptionPane.INFORMATION_MESSAGE
        );
    }

    // ===== Вложенный класс PostForm =====

    /**
     * Модальное окно для отправки сообщения:
     * - Message To:
     * - Message From:
     * - Tag Friend Link: (автоматически генерируется по «Message To»)
     * - Message to write:
     */
   private static class PostForm extends JDialog {
        private static final int MAX_CHARS = 50;

        private final JTextField toField;
        private final JTextField fromField;
        private final JTextField tagLinkField;
        private final JTextArea  messageArea;
        private final JLabel     counterLabel;
        private boolean          submitted = false;

        public PostForm(JFrame parent) {
            super(parent, "New Post Message", true);
            setSize(480, 400);
            setLocationRelativeTo(parent);
            setLayout(new BorderLayout(10, 10));

            // Центр: поля ввода
            JPanel center = new JPanel(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(5,5,5,5);
            gbc.anchor = GridBagConstraints.WEST;

            // Message To
            gbc.gridx = 0; gbc.gridy = 0;
            center.add(new JLabel("Message To:"), gbc);
            gbc.gridx = 1;
            toField = new JTextField(20);
            center.add(toField, gbc);

            // Message From
            gbc.gridx = 0; gbc.gridy = 1;
            center.add(new JLabel("Message From:"), gbc);
            gbc.gridx = 1;
            fromField = new JTextField(20);
            center.add(fromField, gbc);

            // Tag Link
            gbc.gridx = 0; gbc.gridy = 2;
            center.add(new JLabel("Tag Friend Link:"), gbc);
            gbc.gridx = 1;
            tagLinkField = new JTextField(20);
            tagLinkField.setEditable(false);
            tagLinkField.setBackground(Color.WHITE);
            center.add(tagLinkField, gbc);

            // Message to write + counter
            gbc.gridx = 0; gbc.gridy = 3;
            gbc.anchor = GridBagConstraints.NORTHWEST;
            center.add(new JLabel("Message to write:"), gbc);
            gbc.gridx = 1;
            messageArea = new JTextArea(6, 20);
            messageArea.setLineWrap(true);
            messageArea.setWrapStyleWord(true);
            JScrollPane scroll = new JScrollPane(messageArea);
            center.add(scroll, gbc);

            // Счётчик символов
            gbc.gridy = 4;
            counterLabel = new JLabel("Осталось символов: " + MAX_CHARS);
            center.add(counterLabel, gbc);

            add(center, BorderLayout.CENTER);

            // Поля-подсказки и автогенерация Link
            toField.getDocument().addDocumentListener(new DocumentListener() {
                void update() {
                    String t = toField.getText().trim();
                    tagLinkField.setText(t.isEmpty() ? "" : "https://myapp.com/user/" + t);
                }
                public void insertUpdate(DocumentEvent e) { update(); }
                public void removeUpdate(DocumentEvent e) { update(); }
                public void changedUpdate(DocumentEvent e) { update(); }
            });

            // Ограничение текста и обновление счётчика
            messageArea.getDocument().addDocumentListener(new DocumentListener() {
                void update() {
                    String txt = messageArea.getText();
                    if (txt.length() > MAX_CHARS) {
                        // обрезаем лишнее
                        SwingUtilities.invokeLater(() -> {
                            messageArea.setText(txt.substring(0, MAX_CHARS));
                        });
                    }
                    int remaining = MAX_CHARS - messageArea.getText().length();
                    counterLabel.setText("Осталось символов: " + remaining);
                }
                public void insertUpdate(DocumentEvent e) { update(); }
                public void removeUpdate(DocumentEvent e) { update(); }
                public void changedUpdate(DocumentEvent e) { update(); }
            });

            // Нижняя панель: Send / Reset / Cancel
            JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
            JButton btnSend  = new JButton("Send");
            JButton btnReset = new JButton("Reset");
            JButton btnCancel= new JButton("Cancel");
            buttons.add(btnSend);
            buttons.add(btnReset);
            buttons.add(btnCancel);
            add(buttons, BorderLayout.SOUTH);

            // Обработчики
            btnSend.addActionListener(e -> {
                if (toField.getText().trim().isEmpty() ||
                    fromField.getText().trim().isEmpty() ||
                    messageArea.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(
                        this,
                        "Все поля (кроме ссылки) должны быть заполнены.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                    );
                    return;
                }
                submitted = true;
                dispose();
            });

            btnReset.addActionListener(e -> {
                toField.setText("");
                fromField.setText("");
                tagLinkField.setText("");
                messageArea.setText("");
                counterLabel.setText("Осталось символов: " + MAX_CHARS);
            });

            btnCancel.addActionListener(e -> dispose());
        }

        public boolean isSubmitted()    { return submitted; }
        public String getTo()           { return toField.getText().trim(); }
        public String getFrom()         { return fromField.getText().trim(); }
        public String getTagLink()      { return tagLinkField.getText().trim(); }
        public String getMessageText()  { return messageArea.getText().trim(); }
    }
}

