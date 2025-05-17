import javax.swing.*;
import java.awt.*;

public class ProgressDialog extends JDialog {
    private JProgressBar progressBar;

    public ProgressDialog(Window owner, String message) {
        super(owner, "Please wait...", ModalityType.APPLICATION_MODAL);
        progressBar = new JProgressBar();
        progressBar.setIndeterminate(true);

        JLabel label = new JLabel(message);
        label.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        getContentPane().add(label, BorderLayout.NORTH);
        getContentPane().add(progressBar, BorderLayout.CENTER);
        setSize(300, 100);
        setLocationRelativeTo(owner);
    }

    public interface Task {
        void run();
    }

  public void execute(Task task) {
    SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
        @Override
        protected Void doInBackground() throws Exception {
            task.run();
            return null;
        }
        @Override
        protected void done() {
            dispose();
        }
    };
    worker.execute();
    setVisible(true);
}

}
