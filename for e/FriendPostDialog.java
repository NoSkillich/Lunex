// FriendPostDialog.java
import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Окно‐диалог для просмотра одного поста друга и добавления к нему комментариев.
 * Комментарии выбираются из выпадающего списка авторов.
 */
public class FriendPostDialog extends JDialog {
    private final JComboBox<String>        authorCombo;
    private final JTextField               commentField;
    private final DefaultListModel<String> commentModel;

    // Статическая карта: имя друга → список комментариев
    private static final Map<String, List<String>> commentMap = new HashMap<>();
    // Статическая карта «фейковых постов» (имя друга → текст его поста)
    private static final Map<String, String> dummyPosts = new HashMap<>();

    static {
        // Заполняем dummyPosts вручную
        dummyPosts.put("Alice", "Just had a great lunch!");
        dummyPosts.put("Bob",   "Check out my new code snippet.");
        dummyPosts.put("Carol", "Sunny day at the park ☀️");
        dummyPosts.put("Dave",  "Anyone up for a game tonight?");

        // Примеры уже существующих комментариев
        commentMap.put("Alice", new ArrayList<>(Arrays.asList(
            "Dave: Классно!",
            "Bob: Звучит вкусно!"
        )));
        commentMap.put("Bob", new ArrayList<>(Arrays.asList(
            "Alice: Интересно!",
            "Carol: Поделись кодом."
        )));
        // Инициализируем пустые списки для остальных
        commentMap.putIfAbsent("Carol", new ArrayList<>());
        commentMap.putIfAbsent("Dave",  new ArrayList<>());
    }

    /**
     * @param parent     – родительское окно (ContactForm).
     * @param friendName – имя друга, чей пост отображаем.
     */
    public FriendPostDialog(JFrame parent, String friendName) {
        super(parent, "Post from " + friendName, true);
        setSize(450, 350);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));

        // ===== Верх: сам «пост» друга =====
        String postText = dummyPosts.getOrDefault(friendName, "No posts available.");
        JLabel postLabel = new JLabel("<html><b>" + friendName + ": " + postText + "</b></html>");
        postLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(postLabel, BorderLayout.NORTH);

        // ===== Центр: список комментариев =====
        commentModel = new DefaultListModel<>();
        JList<String> commentList = new JList<>(commentModel);
        JScrollPane scrollPane = new JScrollPane(commentList);
        scrollPane.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createEtchedBorder(),
            "Comments",
            TitledBorder.LEFT,
            TitledBorder.TOP
        ));
        add(scrollPane, BorderLayout.CENTER);

        // Подгружаем существующие комментарии
        List<String> comments = commentMap.computeIfAbsent(friendName, k -> new ArrayList<>());
        for (String c : comments) {
            commentModel.addElement(c);
        }

        // ===== Низ: панель ввода нового комментария =====
        JPanel commentPanel = new JPanel(new BorderLayout(5, 5));
        commentPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));

        // Слева: автор комментария
        authorCombo = new JComboBox<>(new String[]{"Me", "Alice", "Bob", "Carol", "Dave"});
        authorCombo.setPreferredSize(new Dimension(100, 24));

        // По центру: само поле ввода
        commentField = new JTextField();

        // Справа: кнопка добавить
        JButton btnAddComment = new JButton("Add Comment");

        // Собираем внутр. панель
        JPanel inner = new JPanel(new BorderLayout(5, 0));
        inner.add(authorCombo, BorderLayout.WEST);
        inner.add(commentField, BorderLayout.CENTER);
        inner.add(btnAddComment, BorderLayout.EAST);

        commentPanel.add(inner, BorderLayout.NORTH);
        add(commentPanel, BorderLayout.SOUTH);

        // ===== Логика добавления =====
        btnAddComment.addActionListener(e -> {
            String author = (String) authorCombo.getSelectedItem();
            String text   = commentField.getText().trim();
            if (!text.isEmpty()) {
                String fullComment = author + ": " + text;
                comments.add(fullComment);
                commentModel.addElement(fullComment);
                commentField.setText("");
            }
        });
    }
}
