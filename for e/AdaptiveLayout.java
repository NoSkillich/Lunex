import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

/**
 * Слушатель для адаптации размера шрифтов и отступов при изменении размера окна.
 */
public class AdaptiveLayout {
  
    public static void install(JFrame frame, int baseWidth) {
        frame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                double scale = frame.getWidth() / (double) baseWidth;
                scale = Math.max(0.75, Math.min(scale, 1.25));  // ограничим диапазон
                resizeFonts(frame.getContentPane(), scale);
                frame.validate();
            }
        });
    }

    private static void resizeFonts(Container container, double scale) {
        for (Component c : container.getComponents()) {
            Font f = c.getFont();
            if (f != null) {
                c.setFont(f.deriveFont((float)(f.getSize2D() * scale)));
            }
            if (c instanceof Container) {
                resizeFonts((Container)c, scale);
            }
        }
    }
}
