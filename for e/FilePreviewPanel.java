// FilePreviewPanel.java
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * FilePreviewPanel — показывает мини-превью изображения
 * или иконку по расширению для других типов файлов.
 */
public class FilePreviewPanel extends JPanel {
    private static final int PREVIEW_SIZE = 64;
    private final JLabel label;

    // Иконки по расширению (можно расширить)
    private static final Map<String, Icon> ICONS = new HashMap<>();
    static {
        ICONS.put("pdf", UIManager.getIcon("FileView.fileIcon"));
        ICONS.put("txt", UIManager.getIcon("FileView.fileIcon"));
        ICONS.put("doc", UIManager.getIcon("FileView.fileIcon"));
        ICONS.put("xls", UIManager.getIcon("FileView.fileIcon"));

    }

    public FilePreviewPanel() {
        setLayout(new BorderLayout());
        label = new JLabel("", SwingConstants.CENTER);
        add(label, BorderLayout.CENTER);
        setPreferredSize(new Dimension(PREVIEW_SIZE + 10, PREVIEW_SIZE + 10));
    }

    /**
     * Обновить панель, показав файл.
     * @param f файл (может быть null)
     */
    public void showFile(File f) {
        if (f == null) {
            label.setIcon(null);
            label.setText("No file");
            return;
        }
        String name = f.getName().toLowerCase();
        try {
            if (name.endsWith(".png") ||
                name.endsWith(".jpg") ||
                name.endsWith(".jpeg")||
                name.endsWith(".gif") ) {
                // Загрузка и масштабирование
                Image img = ImageIO.read(f);
                if (img != null) {
                    Image thumb = img.getScaledInstance(
                        PREVIEW_SIZE, PREVIEW_SIZE, Image.SCALE_SMOOTH
                    );
                    label.setIcon(new ImageIcon(thumb));
                    label.setText("");
                    return;
                }
            }
        } catch (Exception e) {
            // пропустить и показать иконку ниже
        }
        // Не картинка или не удалось загрузить — берем иконку по расширению
        String ext = "";
        int dot = name.lastIndexOf('.');
        if (dot >= 0) ext = name.substring(dot+1);
        Icon ico = ICONS.getOrDefault(ext, UIManager.getIcon("FileView.fileIcon"));
        label.setIcon(ico);
        label.setText("");
    }
}
