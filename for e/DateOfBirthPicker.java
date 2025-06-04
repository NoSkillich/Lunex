import javax.swing.*;
import java.awt.*;
import java.util.Calendar;
import java.util.Date;

/**
 * Компонент для выбора даты рождения.
 */
public class DateOfBirthPicker extends JPanel {
    private final JSpinner dateSpinner;

    public DateOfBirthPicker() {
        setLayout(new FlowLayout(FlowLayout.LEFT, 5, 0));

        add(new JLabel("Date of Birth:"));

        // Модель SpinnerDate: от 1900-01-01 до сегодняшнего дня, шаг — 1 день
        Calendar cal = Calendar.getInstance();
        Date today = cal.getTime();
        cal.set(1900, Calendar.JANUARY, 1);
        Date earliest = cal.getTime();

        SpinnerDateModel model = new SpinnerDateModel(today, earliest, today, Calendar.DAY_OF_MONTH);
        dateSpinner = new JSpinner(model);

        // Устанавливаем формат отображения YYYY-MM-DD
        JSpinner.DateEditor editor = new JSpinner.DateEditor(dateSpinner, "yyyy-MM-dd");
        dateSpinner.setEditor(editor);

        add(dateSpinner);
    }

    /** @return выбранная дата рождения */
    public Date getDate() {
        return (Date) dateSpinner.getValue();
    }
}
