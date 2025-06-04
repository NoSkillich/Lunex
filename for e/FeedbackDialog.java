import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

/**
 * Диалог для сбора отзыва: рейтинг и комментарий с placeholder.
 */
public class FeedbackDialog extends JDialog {
    private final JButton[] stars = new JButton[5];
    private int rating = 0;
    private final JTextArea commentArea;
    private boolean submitted = false;

    public FeedbackDialog(JFrame parent) {
        super(parent, "Your Feedback", true);
        setSize(400, 300);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10,10));

        String[] descriptions = {
            "1 – Очень плохо",
            "2 – Плохо",
            "3 – Хорошо",
            "4 – Неплохо",
            "5 – Отлично!"
        };

        // 1) Звёздочки
        JPanel starBar = new JPanel(new FlowLayout(FlowLayout.CENTER, 5,5));
        for (int i = 0; i < 5; i++) {
            final int idx = i + 1;
            stars[i] = new JButton("☆");
            stars[i].setBorderPainted(false);
            stars[i].setContentAreaFilled(false);
            stars[i].setFont(stars[i].getFont().deriveFont(24f));
            stars[i].setToolTipText(descriptions[i]);
            stars[i].addActionListener(e -> setRating(idx));
            starBar.add(stars[i]);
        }
        add(starBar, BorderLayout.NORTH);

        // 2) Комментарий с placeholder
        String placeholder = "Enter your comment here...";
        commentArea = new JTextArea(5, 30);
        commentArea.setText(placeholder);
        commentArea.setForeground(Color.GRAY);
        commentArea.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (commentArea.getText().equals(placeholder)) {
                    commentArea.setText("");
                    commentArea.setForeground(Color.BLACK);
                }
            }
            @Override
            public void focusLost(FocusEvent e) {
                if (commentArea.getText().trim().isEmpty()) {
                    commentArea.setText(placeholder);
                    commentArea.setForeground(Color.GRAY);
                }
            }
        });
        add(new JScrollPane(commentArea), BorderLayout.CENTER);

        // 3) Кнопки Submit / Cancel
        JPanel btns = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        JButton btnSubmit = new JButton("Submit");
        JButton btnCancel = new JButton("Cancel");
        btns.add(btnSubmit);
        btns.add(btnCancel);
        add(btns, BorderLayout.SOUTH);

        btnSubmit.addActionListener(e -> {
            submitted = true;
            dispose();
        });
        btnCancel.addActionListener(e -> dispose());
    }

    private void setRating(int r) {
        rating = r;
        for (int i = 0; i < 5; i++) {
            stars[i].setText(i < r ? "★" : "☆");
        }
    }

    /** @return был ли нажат Submit */
    public boolean isSubmitted() {
        return submitted;
    }

    /** @return оценка от 0 до 5 */
    public int getRating() {
        return rating;
    }

    /** @return текст комментария */
    public String getComment() {
        String txt = commentArea.getText();
        // не возвращаем placeholder
        if (txt.equals("Enter your comment here...")) return "";
        return txt.trim();
    }
}
