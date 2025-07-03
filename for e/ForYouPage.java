// ForYouPage.java
import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class ForYouPage extends JDialog {
    /**
     * @param parent     – owner window
     * @param topics     – map: tag → list of posts
     * @param onSelect   – callback, passed the list of posts for the clicked tag
     */
    public ForYouPage(JFrame parent,
                      Map<String,List<String>> topics,
                      Consumer<List<String>> onSelect) {
        super(parent, "For You", true);
        setSize(350, 300);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(5,5));

        // 1) Tag buttons at the top
        JPanel tagBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 5,5));
        for (String tag : topics.keySet()) {
            JButton btn = new JButton("#" + tag);
            btn.addActionListener( e-> {
                onSelect.accept(topics.get(tag));
                dispose();
            });
            tagBar.add(btn);
        }
        add(tagBar, BorderLayout.NORTH);

        // 2) Instruction label
        JLabel info = new JLabel("Select a topic to see related posts");
        info.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        add(info, BorderLayout.CENTER);

        // 3) Close button
        JButton close = new JButton("Close");
        close.addActionListener(e -> dispose());
        JPanel south = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        south.add(close);
        add(south, BorderLayout.SOUTH);
    }
}
