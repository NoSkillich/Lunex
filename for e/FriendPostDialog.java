// FriendPostDialog.java
import javax.swing.*;
import java.awt.*;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;


/**
 * Диалог показа «ленты» постов друга.
 */
public class FriendPostDialog extends JDialog {
    private static class Post {
        final String time;
        final String content;
        Post(String time, String content) {
            this.time = time;
            this.content = content;
        }
        @Override public String toString() {
            return "[" + time + "] " + content;
        }
    }

    // Здесь — заглушка: у каждого друга свой список постов
    private static final Map<String, List<Post>> POSTS = new HashMap<>();
    static {
        POSTS.put("Alice", Arrays.asList(
            new Post("08:15", "Morning jog in the park 🏃‍♀️"),
            new Post("12:30", "Lunch at my favorite cafe ☕️")
        ));
        POSTS.put("Bob", Arrays.asList(
            new Post("09:00", "Just finished a great book! 📚"),
            new Post("21:45", "Late night coding session 💻")
        ));
        POSTS.put("Carol", Arrays.asList(
            new Post("14:20", "Sunny afternoon by the lake 🌅")
        ));
        POSTS.put("Dave", Arrays.asList(
            new Post("11:11", "Just Downloaded app!"), new Post("20:30", "Cokking session is ready!")
        ));
    }

    public FriendPostDialog(Frame owner, String friendName) {
        super(owner, friendName + "’s Posts", true);
        setSize(400, 300);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout(5,5));

        DefaultListModel<String> model = new DefaultListModel<>();
        List<Post> feed = POSTS.getOrDefault(friendName, Collections.emptyList());
        for (Post p : feed) {
            model.addElement(p.toString());
        }
        JList<String> list = new JList<>(model);
        list.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        add(new JScrollPane(list), BorderLayout.CENTER);

        JButton btnClose = new JButton("Close");
        btnClose.addActionListener(e -> dispose());
        JPanel pnl = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pnl.add(btnClose);
        add(pnl, BorderLayout.SOUTH);
    }
}
