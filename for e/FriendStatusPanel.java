// FriendStatusPanel.java
import javax.swing.*;
import java.awt.*;

/**
 * Компонент, который показывает текущий статус выбранного друга.
 */
public class FriendStatusPanel extends JPanel {
    private final JLabel lblStatus;
    private final FriendStatusManager manager;

    public FriendStatusPanel(FriendStatusManager mgr) {
        this.manager = mgr;
        setLayout(new FlowLayout(FlowLayout.LEFT, 5, 0));
        add(new JLabel("Status:"));
        lblStatus = new JLabel();
        lblStatus.setFont(lblStatus.getFont().deriveFont(Font.ITALIC, 12f));
        add(lblStatus);
    }

    /** Обновить статус для указанного друга */
    public void updateStatus(String friendName) {
        String txt = manager.getStatusText(friendName);
        lblStatus.setText(txt);
    }
}
