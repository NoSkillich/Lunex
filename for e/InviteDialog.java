// InviteDialog.java
import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.util.Random;

/**
 * Простое окно‐диалог для генерации пригласительного кода.
 * При открытии создаётся случайный 6‐символьный код, который
 * выводится в текстовом поле, готовый для копирования.
 */
public class InviteDialog extends JDialog {
    private final JTextField codeField;
    private final JButton copyButton;

    /**
     * Конструктор принимает любой объект-наследник Window (окно)
     * — будь то JFrame, JDialog и т.д.
     */
    public InviteDialog(Window parent) {
        super(parent, "Invite a Friend", ModalityType.APPLICATION_MODAL);
        setSize(350, 180);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));

        // 1) Верхняя панель с пояснительным текстом
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));
        JLabel infoLabel = new JLabel("<html>Share this code with your friend to join:<br></html>");
        infoLabel.setFont(infoLabel.getFont().deriveFont(Font.PLAIN, 14f));
        topPanel.add(infoLabel, BorderLayout.NORTH);

        // 2) Генерируем случайный код и показываем его в текстовом поле
        String inviteCode = generateRandomCode(6);
        codeField = new JTextField(inviteCode);
        codeField.setFont(codeField.getFont().deriveFont(Font.BOLD, 16f));
        codeField.setHorizontalAlignment(SwingConstants.CENTER);
        codeField.setEditable(false);
        codeField.setBackground(Color.WHITE);
        topPanel.add(codeField, BorderLayout.CENTER);
        add(topPanel, BorderLayout.CENTER);

        // 3) Кнопки «Copy Code» и «Close»
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        copyButton = new JButton("Copy Code");
        JButton closeButton = new JButton("Close");
        buttonPanel.add(copyButton);
        buttonPanel.add(closeButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // 4) Логика копирования в буфер обмена
        copyButton.addActionListener(e -> {
            StringSelection selection = new StringSelection(inviteCode);
            Toolkit.getDefaultToolkit()
                   .getSystemClipboard()
                   .setContents(selection, selection);
            JOptionPane.showMessageDialog(
                InviteDialog.this,
                "Code copied to clipboard!",
                "Copied",
                JOptionPane.INFORMATION_MESSAGE
            );
        });

        // 5) Закрытие диалога
        closeButton.addActionListener(e -> dispose());
    }

    /**
     * Генерирует случайный альфа-цифровой код указанной длины.
     */
    private String generateRandomCode(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random rnd = new Random();
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(chars.charAt(rnd.nextInt(chars.length())));
        }
        return sb.toString();
    }
}
