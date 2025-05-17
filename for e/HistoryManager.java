import javax.swing.*;
import javax.swing.plaf.basic.BasicComboBoxEditor;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.*;
import java.util.List;
/**
 * Менеджер истории автозаполнения для текстовых полей.
 */
public class HistoryManager {
    // Ключ → список введённых значений
    private static final Map<String, List<String>> historyMap = new HashMap<>();

    public static void attach(JTextField field, String historyKey) {
        // Получаем или создаём историю
        List<String> history = historyMap.computeIfAbsent(historyKey, k -> new ArrayList<>());

        // Модель и сам комбобокс
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
        for (String s : history) {
            model.addElement(s);
        }
        JComboBox<String> combo = new JComboBox<>(model);
        combo.setEditable(true);
        combo.setEditor(new BasicComboBoxEditor());

        // Помещаем combo вместо поля в том же контейнере
        Container parent = field.getParent();
        LayoutManager lm = parent.getLayout();
        if (lm instanceof GridBagLayout) {
            GridBagLayout gbl = (GridBagLayout) lm;
            GridBagConstraints gbc = gbl.getConstraints(field);
            parent.remove(field);
            parent.add(combo, gbc);
        } else {
            parent.remove(field);
            parent.add(combo);
        }
        parent.revalidate();
        parent.repaint();

        // Реакция на Enter в редакторе комбобокса
        JTextComponent editorComp = (JTextComponent) combo.getEditor().getEditorComponent();
        editorComp.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    String val = editorComp.getText().trim();
                    if (!val.isEmpty() && !history.contains(val)) {
                        history.add(0, val);
                        model.insertElementAt(val, 0);
                    }
                }
            }
        });
    }
}
