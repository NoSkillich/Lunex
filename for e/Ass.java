import javax.swing.SwingUtilities;

public class Ass {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            RegistrationForm reg = new RegistrationForm();
            reg.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
            reg.setVisible(true);
        });
    }
}
