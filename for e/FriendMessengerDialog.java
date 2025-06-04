import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Простейший диалог-мессенджер для переписки с другом,
 * с интегрированным селектором эмодзи.
 */
public class FriendMessengerDialog extends JDialog {
    private final JTextArea chatArea;
    private final JTextField inputField;   // теперь инициализируется раньше
    private final JButton sendButton;      // теперь инициализируется раньше

    public FriendMessengerDialog(JFrame parent, String friendName) {
        super(parent, "Chat with " + friendName, true);
        setSize(400, 350);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(5,5));

        // 1) Область истории сообщений
        chatArea = new JTextArea();
        chatArea.setEditable(false);
        chatArea.setLineWrap(true);
        chatArea.setWrapStyleWord(true);
        add(new JScrollPane(chatArea), BorderLayout.CENTER);

        // 2) Сначала создаём inputField и sendButton
        inputField = new JTextField();
        sendButton = new JButton("Send");

        // 3) Теперь панель эмодзи, которая может безопасно ссылаться на inputField
        JPanel emojiPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 3));
        String[] emojis = {"😊", "😂", "👍", "❤️", "🎉"};
        for (String emo : emojis) {
            JButton b = new JButton(emo);
            b.setMargin(new Insets(2,2,2,2));
            b.setFont(b.getFont().deriveFont(18f));
            b.addActionListener(e -> {
                // inputField уже проинициализирован
                inputField.setText(inputField.getText() + emo);
            });
            emojiPanel.add(b);
        }
        add(emojiPanel, BorderLayout.NORTH);

        // 4) Панель ввода и кнопка Send
        JPanel pnlInput = new JPanel(new BorderLayout(5,0));
        pnlInput.add(inputField, BorderLayout.CENTER);
        pnlInput.add(sendButton, BorderLayout.EAST);
        add(pnlInput, BorderLayout.SOUTH);

        // 5) Логика отправки
        ActionListener sendAction = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String text = inputField.getText().trim();
                if (text.isEmpty()) return;
                chatArea.append("Me: " + text + "\n");
                inputField.setText("");
                // Имитируем ответ друга
                SwingUtilities.invokeLater(() ->
                    chatArea.append(friendName + ": I got: \"" + text + "\"\n")
                );
            }
        };
        sendButton.addActionListener(sendAction);
        inputField.addActionListener(sendAction);
    }
}
