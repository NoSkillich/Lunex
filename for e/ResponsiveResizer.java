import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;


public class ResponsiveResizer {

    public static void install(JFrame frame, int baseWidth) {
        frame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                double scale = frame.getWidth() / (double) baseWidth;
                scale = Math.max(0.75, Math.min(scale, 1.25));  // лимиты масштаба
                resizeContainer(frame.getContentPane(), scale);
                frame.revalidate();
            }
        });
    }

    private static void resizeContainer(Container cont, double scale) {
        for (Component comp : cont.getComponents()) {
            Font f = comp.getFont();
            if (f != null) {
                comp.setFont(f.deriveFont((float)(f.getSize2D() * scale)));
            }
            if (comp instanceof Container) {
                resizeContainer((Container) comp, scale);
            }
        }
    }
}
