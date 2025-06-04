import javax.swing.*;
import java.awt.*;
public class HintTextArea extends JTextArea {
   private final String hint;
   public HintTextArea(String hint, int rows, int cols) {
    super(rows, cols);
    this.hint = hint;
    setLineWrap(true);
    setWrapStyleWord(true);
   }
   @Override
   protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    if(getText().isEmpty()) {
        Graphics g2 = (Graphics2D) g.create();
        g2.setColor(Color.GRAY);
        Insets ins = getInsets();
        FontMetrics fm = g2.getFontMetrics();
        int x = ins.left + 2;
        int y = ins.top + fm.getAscent();
        g2.drawString(hint, x, y);
        g2.dispose();
    }
   }
}