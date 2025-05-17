import javax.swing.*;
import java.awt.*;

public class ReactionPanel extends JPanel {
    private final JLabel messageLabel;
    public ReactionPanel() {
        setLayout(new BorderLayout());
        messageLabel = new JLabel(" ", SwingConstants.CENTER);
        messageLabel.setBorder(BorderFactory.createEmptyBorder(8,8,8,8));
        add(messageLabel, BorderLayout.CENTER);
    }
    /**
     * Shows a message.
     * @param text    the message text
     * @param success true for success (green color), false for error (red)
     */
    public void showMessage(String text, boolean success) {
        messageLabel.setText(text);
        messageLabel.setForeground(success ? new Color(0, 128, 0) : Color.RED);
    }
}
