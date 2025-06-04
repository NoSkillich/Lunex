import java.util.HashMap;
import java.util.Map;

/**
 * Простая служба аутентификации в памяти.
 */
public class AuthManager {
    // Храним мапу email → пароль
    private static final Map<String, String> users = new HashMap<>();

    /**
     * Регистрирует нового пользователя.
     * @param email email (уникальный ключ)
     * @param password пароль
     * @return true, если регистрация успешна; false, если такой email уже существует
     */
    public static synchronized boolean register(String email, String password) {
        if (users.containsKey(email)) {
            return false; // уже есть
        }
        users.put(email, password);
        return true;
    }
    public static synchronized boolean authenticate(String email, String password) {
        if (!users.containsKey(email)) return false;
        String stored = users.get(email);
        return stored.equals(password);
    }
    public static synchronized String updateProfile(String email, String newPassword) {
        if (!users.containsKey(email)) return null; // нет такого пользователя
        users.put(email, newPassword);
        return email; // возвращаем email для подтверждения
    }
}
