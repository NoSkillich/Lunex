
import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class ScheduledPostDialog extends JDialog {
    private boolean confirmed = false;
    private JSpinner dateSpinner;
    private JSpinner timeSpinner;

    public ScheduledPostDialog(JFrame parent) {
        super(parent, "Schedule Post", true);
        setSize(300, 180);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(5, 5));

        // Панель выбора даты
        JPanel pnlDate = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnlDate.add(new JLabel("Date"));
        dateSpinner = new JSpinner(new SpinnerDateModel());
        dateSpinner.setEditor(new JSpinner.DateEditor(dateSpinner, "yyyy-MM-dd"));
        pnlDate.add(dateSpinner);

        // Панель выбора времени
        JPanel pnlTime = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnlTime.add(new JLabel("Time:"));
        timeSpinner = new JSpinner(new SpinnerDateModel());
        timeSpinner.setEditor(new JSpinner.DateEditor(timeSpinner, "HH:mm"));
        pnlTime.add(timeSpinner);

        // Собираем вместе
        JPanel center = new JPanel(new GridLayout(2,1));
        center.add(pnlDate);
        center.add(pnlTime);
        add(center, BorderLayout.CENTER);

        // Кнопки OK/Cancel
        JPanel btns = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton ok = new JButton("OK");
        JButton cancel = new JButton("Cancel");
        btns.add(ok);
        btns.add(cancel);
        add(btns, BorderLayout.SOUTH);

        ok.addActionListener(e -> {
            confirmed = true;
            dispose();
        });
        cancel.addActionListener(e -> dispose());
    }

    /** Показывает диалог и возвращает запланированное время или null при отмене */
    public LocalDateTime schedule() {
        setVisible(true);
        if (!confirmed) return null;

        Date datePart = (Date) dateSpinner.getValue();
        Date timePart = (Date) timeSpinner.getValue();

        // Объединяем дату и время
        LocalDateTime dt = LocalDateTime.ofInstant(
            datePart.toInstant(), ZoneId.systemDefault()
        ).withHour(
            timePart.getHours()
        ).withMinute(
            timePart.getMinutes()
        ).withSecond(0).withNano(0);

        return dt;
    }
}
