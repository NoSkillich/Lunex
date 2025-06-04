import javax.swing.*;
import java.awt.*;
public class DescriptionPanel extends JPanel {
    public DescriptionPanel() {
        setLayout(new BorderLayout());
        setOpaque(false);

        JLabel desc = new JLabel(
            "<html><div style='text-align:center;'>"
          + "Welcome to <b>MyApp</b> – your personal assistant for managing tasks,<br>"
          + "tracking progress and staying organized."
          + "</div></html>",
            SwingConstants.CENTER
        );
        desc.setForeground(Color.LIGHT_GRAY);
        desc.setFont(desc.getFont().deriveFont(12f));

        add(desc, BorderLayout.CENTER);
    }
}
