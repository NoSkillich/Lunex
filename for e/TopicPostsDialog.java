// TopicPostsDialog.java
import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.*;
import java.util.List;
public class TopicPostsDialog extends JDialog {
    private final Map<String, List<String>> topicComments = new HashMap<>();
    private final JComboBox<String> postCombo;
    private final DefaultListModel<String> commentModel;
    private final JTextField commentField;
    private final JLabel countLabel;

    public TopicPostsDialog(Window parent, String topic) {
        super(parent, "Посты темы: " + topic, ModalityType.APPLICATION_MODAL);
        setSize(450, 400);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(5,5));

        // 1) Список постов по теме
        List<String> posts = dummyPosts(topic);
        postCombo = new JComboBox<>(new Vector<>(posts));
        postCombo.setBorder(BorderFactory.createTitledBorder("Выберите пост"));
        add(postCombo, BorderLayout.NORTH);

        // 2) Счётчик комментариев
        countLabel = new JLabel();
        countLabel.setBorder(BorderFactory.createEmptyBorder(5,10,5,10));
        add(countLabel, BorderLayout.AFTER_LAST_LINE);

        // 3) Список комментариев
        commentModel = new DefaultListModel<>();
        JList<String> commentList = new JList<>(commentModel);
        JScrollPane scroll = new JScrollPane(commentList);
        scroll.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createEtchedBorder(),"Комментарии",TitledBorder.LEFT,TitledBorder.TOP));
        add(scroll, BorderLayout.CENTER);

        // Инициализация комментариев для каждого поста
        for (String post : posts) {
            topicComments.put(post, new ArrayList<>());
        }
        loadComments();

        // 4) Панель ввода нового комментария
        JPanel input = new JPanel(new BorderLayout(5,5));
        commentField = new JTextField();
        JButton btnAdd = new JButton("Добавить комментарий");
        input.add(commentField, BorderLayout.CENTER);
        input.add(btnAdd, BorderLayout.EAST);
        input.setBorder(BorderFactory.createEmptyBorder(0,10,10,10));
        add(input, BorderLayout.SOUTH);

        // Слушатель смены поста
        postCombo.addActionListener(e -> loadComments());

        // Слушатель кнопки добавления
        btnAdd.addActionListener(e -> {
            String txt = commentField.getText().trim();
            if (!txt.isEmpty()) {
                String selPost = (String)postCombo.getSelectedItem();
                List<String> coms = topicComments.get(selPost);
                coms.add("Я: " + txt);
                commentModel.addElement("Я: " + txt);
                commentField.setText("");
                updateCount();
            }
        });
    }

    private void loadComments() {
        commentModel.clear();
        String sel = (String)postCombo.getSelectedItem();
        List<String> coms = topicComments.get(sel);
        coms.forEach(commentModel::addElement);
        updateCount();
    }

    private void updateCount() {
        countLabel.setText("Комментариев: " + commentModel.getSize());
    }

    private List<String> dummyPosts(String topic) {
        switch(topic) {
            case "Видеоигры":
                return Arrays.asList(
                    "Alice: Только что прошёл новую RPG!",
                    "Bob: Кто играет в шутеры?"
                );
            case "Музыка":
                return Arrays.asList(
                    "Carol: Слушаю последний альбом Coldplay.",
                    "Dave: Какие треки рекомендуете?"
                );
            case "Сериалы":
                return Arrays.asList(
                    "Eve: Посмотрел новый сезон \"Мандалорца\".",
                    "Frank: Что думаете про финал?"
                );
            default:
                return Collections.emptyList();
        }
    }
}
