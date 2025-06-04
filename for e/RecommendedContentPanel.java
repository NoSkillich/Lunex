// RecommendedContentPanel.java

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

/**
 * Панель «Other Posts» с рекомендуемым контентом.
 */
public class RecommendedContentPanel extends JPanel {
    public RecommendedContentPanel() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(new TitledBorder("Other Posts"));
        setPreferredSize(new Dimension(200, 0)); // Фиксированная ширина

        // Пример списка постов
        String[] posts = {
            "Ethan: Just tried a new recipe today!",
            "Isabella: Shared a travel photo from Bali.",
            "Mason: Reading up on modern AI trends.",
            "Charlotte: Posted a DIY home decor guide.",
            "Logan: Learned a new guitar chord progression."
        };

        JList<String> postList = new JList<>(posts);
        postList.setVisibleRowCount(8);
        postList.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

        JScrollPane scrollPane = new JScrollPane(postList);
        scrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);
        add(scrollPane);
    }
}
