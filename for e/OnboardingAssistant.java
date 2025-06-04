import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Небольшой помощник-онбординг:
 * по нажатию «Next››» последовательно подсвечивает заданные компоненты
 * и показывает текстовую подсказку в маленьком остовом.
 */
public class OnboardingAssistant {
    private final JLayeredPane layer;
    private final Component[] targets;
    private final String[] tips;
    private int step = 0;
    private final JPanel highlight = new JPanel();
    private final JLabel tipLabel = new JLabel("", SwingConstants.CENTER);

    public OnboardingAssistant(JFrame frame, Component[] targets, String[] tips) {
        this.layer = frame.getLayeredPane();
        this.targets = targets;
        this.tips = tips;

        // Настройка highlight панели
        highlight.setBorder(new LineBorder(Color.ORANGE, 3));
        highlight.setOpaque(false);

        // Настройка всплывающей подсказки
        tipLabel.setOpaque(true);
        tipLabel.setBackground(new Color(0,0,0,200));
        tipLabel.setForeground(Color.WHITE);
        tipLabel.setBorder(BorderFactory.createEmptyBorder(5,10,5,10));

        nextStep();  // показать первый шаг
    }

    private void nextStep() {
        if (step >= targets.length) {
            // Завершили — убираем всё
            layer.remove(highlight);
            layer.remove(tipLabel);
            layer.revalidate();
            layer.repaint();
            return;
        }
        Component c = targets[step];
        String txt = tips[step++];
        // подсветка
        Point loc = SwingUtilities.convertPoint(c.getParent(), c.getLocation(), layer);
        highlight.setBounds(loc.x - 5, loc.y - 5, c.getWidth() + 10, c.getHeight() + 10);
        layer.add(highlight, JLayeredPane.POPUP_LAYER);

        // подсказка под компонентом
        tipLabel.setText(txt);
        int tx = loc.x + c.getWidth()/2 - 75;
        int ty = loc.y + c.getHeight() + 10;
        tipLabel.setBounds(tx, ty, 150, 30);
        layer.add(tipLabel, JLayeredPane.POPUP_LAYER);

        // Кнопка «Next» по клику на подсказке
        tipLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                nextStep();
            }
        });
        layer.revalidate();
        layer.repaint();
    }
}
