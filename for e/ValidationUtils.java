import java.util.regex.Pattern;

/**
 * Утилитарный класс для валидации данных форм.
 */
public class ValidationUtils {
    // Простая регулярка для email
    private static final Pattern EMAIL_PATTERN = 
        Pattern.compile("^[\\w.+\\-]+@[\\w.\\-]+\\.[A-Za-z]{2,}$");

    /**
     * Проверяет, что строка не null и не пуста после trim().
     */
    public static boolean notEmpty(String s) {
        return s != null && !s.trim().isEmpty();
    }

    /**
     * Проверяет, что все переданные строки непустые.
     */
    public static boolean allNotEmpty(String... values) {
        for (String v : values) {
            if (!notEmpty(v)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Проверяет корректность формата email по EMAIL_PATTERN.
     */
    public static boolean isEmailValid(String email) {
        return notEmpty(email) && EMAIL_PATTERN.matcher(email.trim()).matches();
    }

    /**
     * Проверяет, что телефон заполнен полностью (нет placeholder '_' из маски).
     */
    public static boolean isPhoneComplete(String phone) {
        return notEmpty(phone) && !phone.contains("_");
    }

    /**
     * Сравнивает введённые значения имени, фамилии и телефона
     * с ожидаемыми из регистрации.
     */
    public static boolean matchesRegistration(
            String name, String expectedName,
            String surname, String expectedSurname,
            String phone, String expectedPhone) {
        return name != null && expectedName != null
            && surname != null && expectedSurname != null
            && phone != null && expectedPhone != null
            && name.equals(expectedName)
            && surname.equals(expectedSurname)
            && phone.equals(expectedPhone);
    }
}
