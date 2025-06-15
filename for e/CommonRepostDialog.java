import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CommonRepostDialog extends JDialog {
    public CommonRepostDialog(JFrame parent, Map<String, List<String>> repostMap) {
        super(parent, "Friends who reposted both Dave & Carol", true);
        setSize(350, 250);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(5,5));

        // Ищем друзей, у которых есть и “Dave”, и “Carol” в списке репостов
        List<String> mutual = new ArrayList<>();
        for (Map.Entry<String, List<String>> e : repostMap.entrySet()) {
            List<String> reposts = e.getValue();
            if (reposts.contains("Dave") && reposts.contains("Carol")) {
                mutual.add(e.getKey());
            }
        }

        // Отображаем результат
        DefaultListModel<String> model = new DefaultListModel<>();
        for (String name : mutual) {
            model.addElement(name);
        }
        JList<String> list = new JList<>(model);
        list.setBorder(BorderFactory.createTitledBorder("Mutual Repostters"));
        add(new JScrollPane(list), BorderLayout.CENTER);

        JButton btnClose = new JButton("Close");
        btnClose.addActionListener(ev -> dispose());
        JPanel pnl = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pnl.add(btnClose);
        add(pnl, BorderLayout.SOUTH);
    }
}
