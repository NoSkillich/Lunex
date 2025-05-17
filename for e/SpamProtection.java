import javax.swing.*;
import java.awt.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Простая защита от спама: ограничивает число отправок в заданный интервал.
 */
public class SpamProtection {

    private final int maxAttempts;
    private final long intervalMillis;
    private AtomicInteger attempts = new AtomicInteger(0);
    private long windowStart = System.currentTimeMillis();

    /**
     * @param maxAttempts    максимально допустимое число попыток в окне
     * @param intervalMillis длина окна в миллисекундах
     */
    public SpamProtection(int maxAttempts, long intervalMillis) {
        this.maxAttempts = maxAttempts;
        this.intervalMillis = intervalMillis;
    }

    /**
     * Регистрирует попытку. Если она укладывается в лимит — возвращает true,
     * иначе — false (спам).
     */
    public synchronized boolean tryUse() {
        long now = System.currentTimeMillis();
        if (now - windowStart > intervalMillis) {
            // начинаем новый период
            windowStart = now;
            attempts.set(0);
        }
        if (attempts.incrementAndGet() <= maxAttempts) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Показывает диалог предупреждения о слишком частых отправках.
     * Вызывается, если tryUse() вернул false.
     */
    public static void showSpamWarning(Component parent) {
        JOptionPane.showMessageDialog(parent,
            "Too many submissions. Please wait a moment before trying again.",
            "Spam Protection",
            JOptionPane.WARNING_MESSAGE);
    }
}
