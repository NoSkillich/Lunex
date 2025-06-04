import javax.swing.*;
import java.awt.*;
public class DeveloperSignaturePanel extends JPanel {
    public DeveloperSignaturePanel() {
        setLayout(new FlowLayout(FlowLayout.CENTER, 0, 5));
        setOpaque(false);
        JLabel signature = new JLabel("© 2025 Devexx by Daryn. All rights reserved, copryright extended.");
        signature.setFont(signature.getFont().deriveFont(Font.ITALIC, 10f));
        add(signature);
    }
}