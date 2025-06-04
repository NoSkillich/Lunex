import javax.swing.*;
import java.awt.*;
import java.time.LocalTime;
import java.util.EnumSet;
import java.util.Set;
public class AvailabilityPicker extends JPanel {
    public enum Day { MON, TUE, WED, THU, FRI, SAT, SUN }

    private final JCheckBox[] dayChecks = new JCheckBox[7];
    private final JSpinner startSpinner;
    private final JSpinner endSpinner;

    public AvailabilityPicker() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(2,2,2,2);
        gbc.anchor = GridBagConstraints.WEST;

        // Дни недели
        gbc.gridx = 0; gbc.gridy = 0;
        add(new JLabel("Days:"), gbc);
        JPanel daysPanel = new JPanel(new GridLayout(1,7,5,0));
        Day[] days = Day.values();
        for (int i = 0; i < days.length; i++) {
            dayChecks[i] = new JCheckBox(days[i].name());
            daysPanel.add(dayChecks[i]);
        }
        gbc.gridx = 1; gbc.gridwidth = 2;
        add(daysPanel, gbc);

        // Время начала
        gbc.gridy = 1; gbc.gridx = 0; gbc.gridwidth = 1;
        add(new JLabel("From:"), gbc);
        startSpinner = makeTimeSpinner();
        gbc.gridx = 1;
        add(startSpinner, gbc);

        // Время окончания
        gbc.gridx = 2;
        add(new JLabel("To:"), gbc);
        endSpinner = makeTimeSpinner();
        gbc.gridx = 3;
        add(endSpinner, gbc);
    }

    private JSpinner makeTimeSpinner() {
        SpinnerDateModel model = new SpinnerDateModel();
        JSpinner spinner = new JSpinner(model);
        // формат только часы-минуты
        JSpinner.DateEditor editor = new JSpinner.DateEditor(spinner, "HH:mm");
        spinner.setEditor(editor);
        // задаём диапазон с шагом 15 минут:
        model.setCalendarField(java.util.Calendar.MINUTE);
        return spinner;
    }

    /** 
     * @return множество выбранных дней 
     */
    public Set<Day> getSelectedDays() {
        Set<Day> result = EnumSet.noneOf(Day.class);
        for (int i = 0; i < dayChecks.length; i++) {
            if (dayChecks[i].isSelected()) {
                result.add(Day.values()[i]);
            }
        }
        return result;
    }

    /**
     * @return LocalTime начала
     */
    public LocalTime getStartTime() {
        java.util.Date d = (java.util.Date)startSpinner.getValue();
        return LocalTime.of(d.getHours(), d.getMinutes());
    }

    /**
     * @return LocalTime окончания
     */
    public LocalTime getEndTime() {
        java.util.Date d = (java.util.Date)endSpinner.getValue();
        return LocalTime.of(d.getHours(), d.getMinutes());
    }
}
