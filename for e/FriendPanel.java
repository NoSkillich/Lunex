import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.*;
/**
 * Панель для управления списком друзей:
 * - JComboBox со списком;
 * - JTextField для ввода и поиска по ID;
 * - кнопки Find, Add, Delete, Block.
 */
public class FriendPanel extends JPanel {
    private final DefaultComboBoxModel<String> model;
    private final JComboBox<String> comboFriends;
    private final JTextField tfSearch;
    private final JButton btnFind;
    private final JButton btnAdd;
    private final JButton btnDelete;
    private final JButton btnBlock;


    public FriendPanel(
        java.util.List<String> initialFriends,
        ActionListener onSelect,
        ActionListener onFind
    ) {
        setLayout(new FlowLayout(FlowLayout.RIGHT, 5, 5));
        setOpaque(false);

        // Модель и комбобокс
        model = new DefaultComboBoxModel<>();
        for (String f : initialFriends) model.addElement(f);
        comboFriends = new JComboBox<>(model);
        comboFriends.setPreferredSize(new Dimension(120, 24));
        comboFriends.addActionListener(onSelect);
        add(comboFriends);

        // Поле ввода
        tfSearch = new JTextField();
        tfSearch.setPreferredSize(new Dimension(100, 24));
        tfSearch.setToolTipText("Enter friend name or ID");
        add(tfSearch);

        // Кнопка Find
        btnFind = new JButton("Find");
        btnFind.addActionListener(e -> {
            String text = tfSearch.getText().trim();
            if (!text.isEmpty()) {
                onFind.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, text));
            }
        });
        add(btnFind);

        // Кнопка Add
        btnAdd = new JButton("Add");
        btnAdd.addActionListener(e -> {
            String name = tfSearch.getText().trim();
            if (!name.isEmpty() && model.getIndexOf(name) == -1) {
                model.addElement(name);
                tfSearch.setText("");
                JOptionPane.showMessageDialog(
                    this,
                    "Friend \"" + name + "\" added.",
                    "Add Friend",
                    JOptionPane.INFORMATION_MESSAGE
                );
            }
        });
        add(btnAdd);

        // Кнопка Delete
        btnDelete = new JButton("Delete");
        btnDelete.addActionListener(e -> {
            String sel = getSelectedFriend();
            if (sel != null) {
                int idx = model.getIndexOf(sel);
                if (idx >= 0) {
                    model.removeElementAt(idx);
                    JOptionPane.showMessageDialog(
                        this,
                        "Friend \"" + sel + "\" deleted.",
                        "Delete Friend",
                        JOptionPane.INFORMATION_MESSAGE
                    );
                }
            }
        });
        add(btnDelete);

        // Кнопка Block
        btnBlock = new JButton("Block");
        btnBlock.addActionListener(e -> {
            String sel = getSelectedFriend();
            if (sel != null) {
                // для простоты — блокируем, удаляя из списка
                int idx = model.getIndexOf(sel);
                if (idx >= 0) {
                    model.removeElementAt(idx);
                    JOptionPane.showMessageDialog(
                        this,
                        "Friend \"" + sel + "\" blocked.",
                        "Block Friend",
                        JOptionPane.WARNING_MESSAGE
                    );
                }
            }
        });
        add(btnBlock);
    }

    /** @return текущее выбранное имя друга */
    public String getSelectedFriend() {
        return (String) comboFriends.getSelectedItem();
    }
    public List<String> getAllFriends() {
        List<String> list = new ArrayList<>();
        for(int i = 0; i < model.getSize(); i++) {
            list.add(model.getElementAt(i));
        }
        return list;
    }
    public JComboBox<String> getComboBox() {
        return comboFriends;
    }
}
