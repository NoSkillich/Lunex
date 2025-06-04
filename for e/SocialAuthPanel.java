import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * Панель кнопок социального входа.
 */
public class SocialAuthPanel extends JPanel {
    public SocialAuthPanel(ActionListener onGoogle, ActionListener onFacebook) {
        super(new FlowLayout(FlowLayout.CENTER, 10, 0));
        setOpaque(false);

        JButton btnGoogle = new JButton("Sign in with Google");
        btnGoogle.addActionListener(onGoogle);
        add(btnGoogle);

        JButton btnFacebook = new JButton("Sign in with Facebook");
        btnFacebook.addActionListener(onFacebook);
        add(btnFacebook);

        // Можно добавить иконки:
        // btnGoogle.setIcon(new ImageIcon("path/to/google_icon.png"));
    }
}
