// TopicsDialog.java
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import java.awt.*;
import java.util.*;
import java.util.List;

public class TopicsDialog extends JDialog {
    private final JList<String>                 topicList;
    private final DefaultListModel<String>      topicModel;
    private final JList<String>                 postsList;
    private final DefaultListModel<String>      postsModel;
    private final Map<String, List<String>>     topicToPosts;
    private final Map<String, List<String>>     postComments = new HashMap<>();

    public TopicsDialog(JFrame parent) {
        super(parent, "Темы разделов", true);
        setSize(600, 400);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));

        // ===== 1) Темы и их посты =====
        topicToPosts = new LinkedHashMap<>();
        topicToPosts.put("Video Games", Arrays.asList(
            "Alex: Just beat Elden Ring!",
            "Sam: Anyone up for co-op in Overwatch?",
            "Jamie: New Cyberpunk 2077 update is awesome."
        ));
        topicToPosts.put("Music", Arrays.asList(
            "Rachel: Found a new album by Arctic Monkeys.",
            "Tom: Guitar cover of Hotel California posted.",
            "Lena: Who's going to Coachella this year?"
        ));
        topicToPosts.put("Sitcoms", Arrays.asList(
            "Monica: The Office is the best sitcom ever!",
            "Chandler: Could I BE any funnier?",
            "Joey: How you doin'?"
        ));

        // ===== 2) Список тем =====
        topicModel = new DefaultListModel<>();
        topicToPosts.keySet().forEach(topicModel::addElement);
        topicList = new JList<>(topicModel);
        topicList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane topicScroll = new JScrollPane(topicList);
        topicScroll.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createEtchedBorder(), "Темы", TitledBorder.CENTER, TitledBorder.TOP
        ));

        // ===== 3) Список постов =====
        postsModel = new DefaultListModel<>();
        postsList = new JList<>(postsModel);
        JScrollPane postsScroll = new JScrollPane(postsList);
        postsScroll.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createEtchedBorder(), "Посты", TitledBorder.CENTER, TitledBorder.TOP
        ));

        // ===== 4) Размещение =====
        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, topicScroll, postsScroll);
        split.setDividerLocation(150);
        add(split, BorderLayout.CENTER);

        // ===== 5) Кнопка Закрыть =====
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnClose = new JButton("Закрыть");
        bottom.add(btnClose);
        add(bottom, BorderLayout.SOUTH);
        btnClose.addActionListener(e -> dispose());

        // ===== 6) При выборе темы загружаем посты =====
        topicList.addListSelectionListener((ListSelectionEvent e) -> {
            if (!e.getValueIsAdjusting()) {
                String topic = topicList.getSelectedValue();
                postsModel.clear();
                topicToPosts.getOrDefault(topic, Collections.emptyList())
                             .forEach(postsModel::addElement);
            }
        });

        // ===== 7) При двойном клике по посту — открываем комментарии =====
        postsList.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount()  == 2) {
                    String post = postsList.getSelectedValue();
                    // Получаем (или создаём) список комментариев для этого поста
                    List<String> comments = postComments.computeIfAbsent(post, k -> new ArrayList<>());
                    new PostCommentDialog(TopicsDialog.this, post, comments).setVisible(true);
                }
            }
        });
    }
}
