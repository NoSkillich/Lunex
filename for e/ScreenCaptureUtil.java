import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * Утилита для захвата скриншота экрана.
 */
public class ScreenCaptureUtil {
    public static File captureScreen(File outputFile) throws AWTException, IOException {
        // определяем размер экрана
        Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
        // создаём скриншот
        BufferedImage image = new Robot().createScreenCapture(screenRect);
        // записываем в файл
        ImageIO.write(image, "png", outputFile);
        return outputFile;
    }

    public static File captureScreenTemp() throws AWTException, IOException {
        String tmpDir = System.getProperty("java.io.tmpdir");
        String name   = "screen_" + System.currentTimeMillis() + ".png";
        File out     = new File(tmpDir, name);
        return captureScreen(out);
    }
}
