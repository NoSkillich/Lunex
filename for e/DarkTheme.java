import javax.swing.*;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;
import java.awt.*;

/**
 * Применяет тёмную тему на базе Nimbus.
 */
public class DarkTheme {
    public static void apply() {
        try {
            UIManager.setLookAndFeel(new NimbusLookAndFeel());
            UIManager.put("control", new Color(50, 50, 50));
            UIManager.put("info", new Color(60, 60, 60));
            UIManager.put("nimbusBase", new Color(30, 30, 30));
            UIManager.put("nimbusDisabledText", new Color(128, 128, 128));
            UIManager.put("nimbusFocus", new Color(115, 164, 209));
            UIManager.put("nimbusLightBackground", new Color(50, 50, 50));
            UIManager.put("nimbusSelectionBackground", new Color(104, 93, 156));
            UIManager.put("text", new Color(230, 230, 230));
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
    }
}
