import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
public class ProfileEditorDialog extends JDialog {
    
    public JTextField nameField, emailField;
    private AvatarButton avatarBtn;
    private boolean saved;

    public ProfileEditorDialog(JFrame parent, String curName, String curEmail) {
        super(parent, "Edit Profile", true);
        setLayout(new BorderLayout(5,5));
        setSize(350, 250);
        setLocationRelativeTo(parent);

        // **Кнопка аватарки**  
        avatarBtn = new AvatarButton();

        // Панель с аватаром и полями
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        top.add(avatarBtn);

        nameField  = new JTextField(curName, 20);
        emailField = new JTextField(curEmail,20);
        JPanel fields = new JPanel(new GridLayout(2,2,5,5));
        fields.add(new JLabel("Name:"));  fields.add(nameField);
        fields.add(new JLabel("Email:")); fields.add(emailField);

        top.add(fields);
        add(top, BorderLayout.CENTER);

        // Кнопки Save / Cancel
        JPanel btns = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnSave = new JButton("Save");
        JButton btnCancel = new JButton("Cancel");
        btns.add(btnSave); btns.add(btnCancel);
        add(btns, BorderLayout.SOUTH);

        btnSave.addActionListener(e -> {
            if (nameField.getText().trim().isEmpty() ||
                emailField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this,
                    "Поля не могут быть пустыми.", "Ошибка", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (avatarBtn.getAvatar() == null) {
                JOptionPane.showMessageDialog(this,
                    "Пожалуйста, загрузите аватар.", "Ошибка", JOptionPane.ERROR_MESSAGE);
                return;
            }
            saved = true;
            dispose();
        });
        btnCancel.addActionListener(e -> dispose());
    }

    public boolean isSaved() { return saved; }
    public BufferedImage getAvatar() { return avatarBtn.getAvatar(); }
}
