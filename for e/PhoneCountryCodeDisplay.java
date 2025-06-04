
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Панель, которая следит за вводом в JFormattedTextField (телефон)
 * и показывает справа название страны, соответствующее коду.
 */
public class PhoneCountryCodeDisplay extends JPanel {
    private final JTextComponent phoneField;
    private final JLabel codeLabel;
    // Отображает код → название страны
    private static final Map<String, String> CODE_TO_COUNTRY = new LinkedHashMap<>();

    static {
        // Вставьте столько записей, сколько нужно. Порядок важен: сначала более длинные коды.
        CODE_TO_COUNTRY.put("+1",   "USA/Canada");
        CODE_TO_COUNTRY.put("+7",   "Russia");
        CODE_TO_COUNTRY.put("+44",  "United Kingdom");
        CODE_TO_COUNTRY.put("+49",  "Germany");
        CODE_TO_COUNTRY.put("+33",  "France");
        CODE_TO_COUNTRY.put("+34",  "Spain");
        CODE_TO_COUNTRY.put("+61",  "Australia");
        CODE_TO_COUNTRY.put("+81",  "Japan");
        // … при необходимости добавьте свои коды
    }

    /**
     * @param phoneField любое текстовое поле (JFormattedTextField или JTextField),
     *                   в котором пользователь вводит номер телефона вместе с кодом.
     *                   Например: +7 (123) 456-78-90
     */
    public PhoneCountryCodeDisplay(JTextComponent phoneField) {
        this.phoneField = phoneField;
        this.codeLabel = new JLabel(" ");  // по умолчанию пустой «пробел»

        setLayout(new BorderLayout(4, 0));
        setOpaque(false);
        // Просто расскажем пользователю, что это за метка
        codeLabel.setFont(codeLabel.getFont().deriveFont(Font.PLAIN, 12f));
        codeLabel.setForeground(Color.DARK_GRAY);

        // Добавляем слушатель изменений в текстовом поле
        phoneField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) { updateCountry(); }
            @Override
            public void removeUpdate(DocumentEvent e) { updateCountry(); }
            @Override
            public void changedUpdate(DocumentEvent e) { updateCountry(); }
        });

        // Собираем панель: слева поле, справа метка-код
        add(phoneField, BorderLayout.CENTER);
        add(codeLabel,  BorderLayout.EAST);

        // При старте сразу вычисляем, может текст уже был заполнен
        SwingUtilities.invokeLater(this::updateCountry);
    }

    /**
     * Вызывается при любом изменении текста в phoneField.
     * Извлекает первые знаки (например "+7" или "+44") и показывает стране.
     */
    private void updateCountry() {
        String text = phoneField.getText().trim();
        String country = null;

        // Ищем самый длинный код, который является префиксом text
        for (Map.Entry<String, String> entry : CODE_TO_COUNTRY.entrySet()) {
            String code = entry.getKey();
            if (text.startsWith(code)) {
                country = entry.getValue();
                break;
            }
        }

        if (country != null) {
            codeLabel.setText("[" + country + "]");
        } else {
            codeLabel.setText(" ");  
        }
    }
}
