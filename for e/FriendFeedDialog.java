import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Map;
/**
 * Простейший диалог, который показывает список постов друзей
 * (каждый пост — на новой строке в текстовой области).
 */
public class FriendFeedDialog extends JDialog {
    public FriendFeedDialog(JFrame parent, List<String> friends, Map<String, String> posts) {
        super(parent, "Friends Feed", true);
        setSize(400, 300);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(5, 5));

        // Текстовая область для отображения ленты
        JTextArea feedArea = new JTextArea();
        feedArea.setEditable(false);
        feedArea.setLineWrap(true);
        feedArea.setWrapStyleWord(true);

        // Собираем содержимое: друг: его_пост
        StringBuilder sb = new StringBuilder();
        for (String friend : friends) {
            String post = posts.get(friend);
            if (post != null && !post.isEmpty()) {
                sb.append(friend)
                  .append(": ")
                  .append(post)
                  .append("\n\n");
            }
        }

        feedArea.setText(sb.toString().trim());
        add(new JScrollPane(feedArea), BorderLayout.CENTER);

        // Кнопка Закрыть
        JButton btnClose = new JButton("Close");
        btnClose.addActionListener(e -> dispose());
        JPanel pnl = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pnl.add(btnClose);
        add(pnl, BorderLayout.SOUTH);
    }
}
