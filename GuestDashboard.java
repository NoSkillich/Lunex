import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
public class GuestDashboard extends JFrame {
    public GuestDashboard() {
        super("Guest Dashboard");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);

        JLabel lbl = new JLabel(
            "<html><div style='text-align:center;'>"
          + "You are now in guest mode.<br>"
          + "Some features are disabled.<br>"
          + "Please register or login for full access."
          + "</div></html>",
            SwingConstants.CENTER
        );
        lbl.setFont(lbl.getFont().deriveFont(14f));

        // Кнопка «Register» сразу из гостевого окна
        JButton btnRegister = new JButton("Register Now");
        btnRegister.addActionListener(e -> {
            new RegistrationForm().setVisible(true);
            dispose();
        });

        JPanel pnl = new JPanel(new BorderLayout(0,10));
        pnl.add(lbl, BorderLayout.CENTER);

        JPanel btnPnl = new JPanel();
        btnPnl.add(btnRegister);
        pnl.add(btnPnl, BorderLayout.SOUTH);

        add(pnl);

        // В гостевом режиме сразу переходим в ContactForm, но передаём текущий момент
        SwingUtilities.invokeLater(() -> {
            new ContactForm("", "", "", LocalDateTime.now()).setVisible(true);
            dispose();
        });
    }
}
