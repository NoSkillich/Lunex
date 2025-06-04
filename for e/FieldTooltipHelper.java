import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.ToolTipManager;
public class FieldTooltipHelper {
    public static void attachTooltip(JComponent comp, String message) {
        ToolTipManager.sharedInstance().setInitialDelay(300);
        ToolTipManager.sharedInstance().setDismissDelay(10_000);
        comp.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
            }
        });
    }
}