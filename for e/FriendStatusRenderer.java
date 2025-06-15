// FriendStatusRenderer.java
import javax.swing.*;
import java.awt.*;

/**
 * Рендерит элемент JComboBox<String> в виде "Имя (статус)".
 */
public class FriendStatusRenderer extends JLabel implements ListCellRenderer<String> {
    private final FriendStatusManager statusMgr;

    public FriendStatusRenderer(FriendStatusManager mgr) {
        this.statusMgr = mgr;
        setOpaque(true);
    }

    @Override
    public Component getListCellRendererComponent(
        JList<? extends String> list,
        String value,
        int index,
        boolean isSelected,
        boolean cellHasFocus
    ) {
        if (value == null || value.isEmpty()) {
            setText("");
        } else {
            String status = statusMgr.getStatusText(value);
            setText(value + " (" + status + ")");
        }
        if (isSelected) {
            setBackground(list.getSelectionBackground());
            setForeground(list.getSelectionForeground());
        } else {
            setBackground(list.getBackground());
            setForeground(list.getForeground());
        }
        return this;
    }
}
