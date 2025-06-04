import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * Модальный диалог с опцией «Resend confirmation».
 */
public class ConfirmationDialog extends JDialog {
    private final String email;
    private final String subject;
    private final String body;
    private JButton resendBtn;

    public ConfirmationDialog(Frame owner, String email, String subject, String body) {
        super(owner, "Submission Complete", true);
        this.email = email;
        this.subject = subject;
        this.body = body;

        JLabel lbl = new JLabel("<html>Your message has been sent.<br>"
            + "A confirmation email was sent to:<br><b>" + email + "</b></html>");
        lbl.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        resendBtn = new JButton("Resend Confirmation");
        resendBtn.addActionListener(this::onResend);

        JButton closeBtn = new JButton("Close");
        closeBtn.addActionListener(e -> dispose());

        JPanel btns = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        btns.add(resendBtn);
        btns.add(closeBtn);

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(lbl, BorderLayout.CENTER);
        getContentPane().add(btns, BorderLayout.SOUTH);
        pack();
        setLocationRelativeTo(owner);
    }

    private void onResend(ActionEvent e) {
        resendBtn.setEnabled(false);
        resendBtn.setText("Resending...");
        EmailService.sendConfirmation(email, subject, body, error -> {
            if (error == null) {
                JOptionPane.showMessageDialog(this,
                    "Confirmation email resent successfully.",
                    "Email sent",
                    JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this,
                    "Failed to resend: " + error.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
            resendBtn.setText("Resend Confirmation");
            resendBtn.setEnabled(true);
        });
    }
}
