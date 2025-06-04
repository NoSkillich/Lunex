import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

public class SpellChecker {
    private final Set<String> dict = new HashSet<>();

    /**
     * Ожидает, что в resources рядом будет файл dictionary.txt,
     * в котором каждое слово — на своей строке.
     */
    public SpellChecker() {
        try (InputStream in = getClass().getResourceAsStream("/dictionary.txt");
             BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
            String w;
            while ((w = reader.readLine()) != null) {
                dict.add(w.trim().toLowerCase());
            }
        } catch (IOException | NullPointerException e) {
            // Не смогли загрузить словарь — отчитаемся в консоль
            System.err.println("Failed to load dictionary: " + e.getMessage());
        }
    }
    public List<String> check(String text) {
        List<String> errors = new ArrayList<>();
        // Разбиваем по любым не-буквенным символам
        String[] tokens = text.split("[^A-Za-zА-Яа-я]+");
        for (String tk : tokens) {
            if (tk.trim().isEmpty()) {
                continue;
            }
            String word = tk.toLowerCase();
            if (!dict.contains(word) && !errors.contains(word)) {
                errors.add(word);
            }
        }
        return errors;
    }
}
