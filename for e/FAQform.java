import javax.swing.*;
import java.awt.*;
public class FAQform extends JFrame {
    public FAQform() {
        super("FREQUENTLY ASKED QUESTIONS");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(500, 350);
        setLocationRelativeTo(null);
        JTextArea text = new JTextArea();
        text.setEditable(false);
        text.setLineWrap(true);
        text.setWrapStyleWord(true);
        text.setText("1. How to register?\n" +
                "To register, click on the 'Register' button on the main screen and fill in the required information.\n\n" +
                "2. How to reset my password?\n" +
                "If you forget your password, click on the 'Forgot Password?' link on the login screen and follow the instructions.\n\n" +
                "3. How to contact support?\n" +
                "You can contact support by clicking on the 'Contact Us' link in the application or by sending an email to" + "4.How to change my profile information?\n" +
                "To change your profile information, go to the 'Profile' section in the application and edit your details.\n");
                JScrollPane scroll = new JScrollPane(text);
                scroll.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
                getContentPane().add(scroll, BorderLayout.CENTER);
    }
    
};