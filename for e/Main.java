import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        // Запускаем GUI-поток и показываем RegistrationForm
        SwingUtilities.invokeLater(() -> {
            RegistrationForm form = new RegistrationForm();
            // Убедимся, что всё настроено
            form.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
            form.setVisible(true);
        });
    }
}
