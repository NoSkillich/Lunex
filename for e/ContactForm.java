import javax.swing.*;
import java.awt.*;
import java.io.File;

public class ContactForm extends ValidForm {
    private JTextField name, surname, email;
    private JFormattedTextField phone;
    private JTextArea message;
    private ReactionPanel reaction;
    private File attached;
    // <-- Объявляем поля для сброса
    private Component[] fields;
    // Ожидаемые значения из регистрации
    private final String expName, expSurname, expPhone;

    public ContactForm(String fn, String ln, String ph) {
        super("Contact Form");
        setSize(550, 480);
        setLayout(new BorderLayout());

        expName    = fn;
        expSurname = ln;
        expPhone   = ph;

        // Панель полей
        add(panel, BorderLayout.CENTER);

        name    = addTextField(0, "Name",    fn,                20);
        surname = addTextField(1, "Surname", ln,                20);
        phone   = addMaskedField(2,"Phone", "+7 (###) ###-##-##", ph);
        email   = addTextField(3, "Email",   "example@mail.com",20);
        message = addTextArea(4, "Message","Your message...",   40,5);

        // Массив для сброса
        fields = new Component[]{ name, surname, phone, email, message };

        // Attach File
        gbc.gridx = 0; gbc.gridy = 5; gbc.anchor = GridBagConstraints.WEST;
        panel.add(new JLabel("Attach File:"), gbc);
        gbc.gridx = 1;
        JButton attachBtn = new JButton("Attach");
        JLabel fileLabel = new JLabel("No file");
        attachBtn.addActionListener(ae -> {
            JFileChooser fc = new JFileChooser();
            if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                attached = fc.getSelectedFile();
                fileLabel.setText(attached.getName());
            }
        });
        panel.add(attachBtn, gbc);
        gbc.gridy = 6; panel.add(fileLabel, gbc);

        // Кнопки
        addButton(7, "Submit", e -> doSubmit());
        addButton(8, "Reset",  e -> resetForm(fields));

        // Панель реакции
        reaction = new ReactionPanel();
        add(reaction, BorderLayout.SOUTH);
    }

    private void doSubmit() {
        String nm = name.getText().trim();
        String sr = surname.getText().trim();
        String ph = phone.getText();
        String em = email.getText().trim();
        String msg= message.getText().trim();

        if (nm.isEmpty() || sr.isEmpty() || em.isEmpty() || msg.isEmpty() || ph.contains("_")) {
            reaction.showMessage("Please fill in all fields correctly.", false);
            return;
        }
        if (!nm.equals(expName) || !sr.equals(expSurname) || !ph.equals(expPhone)) {
            reaction.showMessage("Name, surname or phone do not match registration.", false);
            return;
        }

        reaction.showMessage("Contact submitted successfully!", true);
    }
}
