// ContactForm.java
import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.io.File;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ContactForm extends ValidForm {
    private final String expName, expSurname, expPhone;
    private final LocalDateTime registrationTime;

    private AvatarButton       avatarbtn;
    private FriendPanel        friendPanel;
    private FriendStatusPanel  statusPanel;
    private FilePreviewPanel   previewPanel;
    private JTextField         name, surname, email;
    private JFormattedTextField phone;
    private JTextArea          message;
    private AvailabilityPicker availability;
    private ReactionPanel      reaction;
    private Component[]        fields;
    private File               attached;
    private final SpellChecker spellchecker = new SpellChecker();

    // Search field at top
    private JTextField searchField;
    // Model/view for the user's own posts
    private DefaultListModel<String> myPostsModel;
    private JList<String>            myPostsList;
    // Model/view for other people's posts
    private DefaultListModel<String> otherPostsModel;
    private JList<String>            otherPostsList;

    /**
     * Конструктор принимает «имя», «фамилию», «телефон» и время регистрации.
     */
    public ContactForm(String fn, String ln, String ph, LocalDateTime registrationTime) {
        super("Contact Form");
        this.expName          = fn;
        this.expSurname       = ln;
        this.expPhone         =   ph;
        this.registrationTime = registrationTime;

        setSize(900, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(0, 10));

        // —— 1) SEARCH BAR + AVATAR + TOOLBAR —— 

        // 1.a) Search field at top
        JPanel searchPanel = new JPanel(new BorderLayout(5, 0));
        searchPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 0, 5));
        searchPanel.add(new JLabel("Search Posts:"), BorderLayout.WEST);
        searchField = new JTextField();
        searchField.setToolTipText("Type here and press Enter to view all friends' posts...");
        searchPanel.add(searchField, BorderLayout.CENTER);
        add(searchPanel, BorderLayout.NORTH);

        // 1.b) Avatar and toolbar below searchPanel
        avatarbtn = new AvatarButton();
        JPanel avatarPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        avatarPanel.setOpaque(false);
        avatarPanel.add(avatarbtn);

        // Friend status panel
        FriendStatusManager statusMgr = new FriendStatusManager();
        statusPanel = new FriendStatusPanel(statusMgr);

        // Friend list panel
        List<String> dummyFriends = Arrays.asList("Alice", "Bob", "Carol", "Dave");
        friendPanel = new FriendPanel(
            dummyFriends,
            e -> {
                String sel = friendPanel.getSelectedFriend();
                statusPanel.updateStatus(sel);
                new FriendPostDialog(this, sel).setVisible(true);
            },
            e -> {
                String id = e.getActionCommand();
                JOptionPane.showMessageDialog(
                    this, "Search by ID: " + id, "Find Friend", JOptionPane.INFORMATION_MESSAGE
                );
            }
        );

        // Toolbar buttons
        JButton btnEdit = new JButton("Edit Profile");
        btnEdit.addActionListener(e -> {
            ProfileEditorDialog dlg = new ProfileEditorDialog(
                this, name.getText().trim(), email.getText().trim()
            );
            dlg.setVisible(true);
            if (dlg.isSaved()) {
                name.setText(dlg.nameField.getText().trim());
                email.setText(dlg.emailField.getText().trim());
                reaction.showMessage("Profile updated", true);
            }
        });
        JButton btnFaq = new JButton("FAQ");
        btnFaq.addActionListener(e -> new FAQform().setVisible(true));
        JButton btnChat = new JButton("Chat");
        btnChat.addActionListener(e -> {
            String sel = friendPanel.getSelectedFriend();
            if (sel != null) {
                new FriendMessengerDialog(this, sel).setVisible(true);
            } else {
                JOptionPane.showMessageDialog(
                    this,
                    "Please select a friend",
                    "No friend selected",
                    JOptionPane.WARNING_MESSAGE
                );
            }
        });
        JButton btnInvite = new JButton("Invite");
        btnInvite.addActionListener(e -> {
            InviteDialog dlg = new InviteDialog(this);
            dlg.setVisible(true);
        });
        
        JButton btnNotify = new JButton("Notification");
        btnNotify.addActionListener(e -> showNotification());
        JButton btnAddPost = new JButton("Add Post");
        btnAddPost.addActionListener(e -> openAddPostDialog());

        JPanel rightButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        rightButtons.setOpaque(false);
        rightButtons.add(btnEdit);
        rightButtons.add(btnFaq);
        rightButtons.add(btnChat);
        rightButtons.add(btnInvite);
        rightButtons.add(btnNotify);
        rightButtons.add(btnAddPost);
        

        JPanel topBar = new JPanel(new BorderLayout(5, 0));
        topBar.setOpaque(false);
        topBar.add(friendPanel,  BorderLayout.WEST);
        topBar.add(statusPanel,  BorderLayout.CENTER);
        topBar.add(rightButtons, BorderLayout.EAST);

        JPanel headWithAvatar = new JPanel(new BorderLayout(5, 0));
        headWithAvatar.setOpaque(false);
        headWithAvatar.add(avatarPanel, BorderLayout.WEST);
        headWithAvatar.add(topBar,      BorderLayout.CENTER);
        add(headWithAvatar, BorderLayout.AFTER_LAST_LINE);

        // —— 2) INPUT FIELDS —— 
        name    = addTextField(0, "Name",    expName,           20);
        surname = addTextField(1, "Surname", expSurname,        20);
        phone   = addMaskedField(2, "Phone", "+7 (###) ###-##-##", expPhone);
        email   = addTextField(3, "Email",   "example@mail.com", 20);
        message = addTextArea(4, "Message", "Your message...",   40, 5);

        fields = new Component[]{ name, surname, phone, email, message };

        // —— 3) MAIN PANEL —— 
        add(panel, BorderLayout.CENTER);

        // Spellcheck button
        gbc.gridx  = 0;
        gbc.gridy  = 5;
        gbc.anchor = GridBagConstraints.WEST;
        JButton btnCheck = new JButton("Check Spelling");
        btnCheck.addActionListener(e -> doSpellCheck());
        panel.add(btnCheck, gbc);

        // Attach file + preview
        gbc.gridx = 0;
        gbc.gridy = 6;
        panel.add(new JLabel("Attach File:"), gbc);

        gbc.gridx = 1;
        JButton attachBtn = new JButton("Attach...");
        JLabel fileLabel = new JLabel("No file");
        panel.add(attachBtn, gbc);

        gbc.gridx = 2;
        panel.add(fileLabel, gbc);

        previewPanel = new FilePreviewPanel();
        gbc.gridx = 3;
        panel.add(previewPanel, gbc);

        attachBtn.addActionListener(e -> {
            JFileChooser fc = new JFileChooser();
            if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                attached = fc.getSelectedFile();
                fileLabel.setText(attached.getName());
                previewPanel.showFile(attached);
            }
        });

        // Availability picker
        gbc.gridx     = 0;
        gbc.gridy      = 7;
        gbc.gridwidth  = 3;
        gbc.anchor     = GridBagConstraints.CENTER;
        availability   = new AvailabilityPicker();
        panel.add(availability, gbc);

        gbc.gridwidth = 1;
        gbc.anchor    = GridBagConstraints.WEST;

        // Submit / Reset / Feedback
        addButton(8, "Submit", e -> doSubmit());
        addButton(9, "Reset",  e -> resetForm(fields));

        gbc.gridx = 2;
        gbc.gridy = 8;
        JButton btnFeedback = new JButton("Feedback");
        btnFeedback.addActionListener(e -> doFeedback());
        panel.add(btnFeedback, gbc);

        // —— 4) Reaction panel —— 
        reaction = new ReactionPanel();
        add(reaction, BorderLayout.SOUTH);

        // —— 5) Onboarding tips —— 
        Component[] tourTargets = { name, surname, phone, email, message, friendPanel };
        String[]    tourTips    = {
            "Enter your first name.",
            "Enter your last name.",
            "Enter your phone number.",
            "Enter your email address.",
            "Write your message.",
            "Select a friend to view their posts."
        };
        new OnboardingAssistant(this, tourTargets, tourTips);

        // —— 6) Search Posts logic —— 
        searchField.addActionListener(ev -> {
            List<String> friendsList = Arrays.asList("Alice", "Bob", "Carol", "Dave");
            Map<String, String> dummyPosts = new HashMap<>();
            dummyPosts.put("Alice", "Just had a great lunch!");
            dummyPosts.put("Bob",   "Check out my new code snippet.");
            dummyPosts.put("Carol", "Sunny day at the park ☀️");
            dummyPosts.put("Dave",  "Anyone up for a game tonight?");
            new FriendFeedDialog(this, friendsList, dummyPosts).setVisible(true);
        });

        // —— 7) RecommendedContentPanel on the right —— 
        add(new RecommendedContentPanel(), BorderLayout.EAST);

        // —— 8) Posts panels on the right —— 
        JPanel rightPanel =  new JPanel(new GridLayout(2, 1, 5, 5));
        rightPanel.setPreferredSize(new Dimension(200, 0));

        // 8.a) Other People's Posts
        otherPostsModel = new DefaultListModel<>();
        // Dummy “other people” posts
        otherPostsModel.addElement("[Entertainment] Alice: Just baked a cake!");
        otherPostsModel.addElement("[Educational] Bob: Learned a new Java trick today.");
        otherPostsModel.addElement("[Entertainment] Carol: Watching a funny movie tonight!");
        otherPostsModel.addElement("[Educational] Dave: Reading about Swing UI best practices.");

        otherPostsList = new JList<>(otherPostsModel);
        JScrollPane scrollOther = new JScrollPane(otherPostsList);
        scrollOther.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createEtchedBorder(), 
            "Other People’s Posts", 
            TitledBorder.CENTER, 
            TitledBorder.TOP
        ));
        rightPanel.add(scrollOther);

        // 8.b) User's Own Posts
        myPostsModel = new DefaultListModel<>();
        myPostsList  = new JList<>(myPostsModel);
        JScrollPane scrollMy = new JScrollPane(myPostsList);
        scrollMy.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createEtchedBorder(),
            "My Posts",
            TitledBorder.CENTER,
            TitledBorder.TOP
        ));
        rightPanel.add(scrollMy);

        add(rightPanel, BorderLayout.LINE_END);
    }

    /** Удобный конструктор без явного времени регистрации — регаем «сейчас». */
    public ContactForm(String fn, String ln, String ph) {
        this(fn, ln, ph, LocalDateTime.now());
    }

    private void doSubmit() {
        String nm  = name.getText().trim(),
               sr  = surname.getText().trim(),
               ph  = phone.getText().trim(),
               em  = email.getText().trim(),
               msg = message.getText().trim();

        if (nm.isEmpty() || sr.isEmpty() || em.isEmpty() || msg.isEmpty() || ph.contains("_")) {
            reaction.showMessage("Please fill in all fields correctly.", false);
            return;
        }
        if (!nm.equals(expName) || !sr.equals(expSurname) || !ph.equals(expPhone)) {
            reaction.showMessage("Data mismatch.", false);
            return;
        }
        reaction.showMessage("Contact submitted successfully!", true);
    }

    private void doSpellCheck() {
        java.util.List<String> miss = spellchecker.check(message.getText());
        reaction.showMessage(
            miss.isEmpty() ? "No spelling errors!" : "Unknown words: " + String.join(", ", miss),
            miss.isEmpty()
        );
    }

    private void doFeedback() {
        FeedbackDialog dlg = new FeedbackDialog(this);
        dlg.setVisible(true);
        if (dlg.isSubmitted()) {
            reaction.showMessage("Thanks for feedback! Rating: " + dlg.getRating(), true);
        }
    }

    /** Показывает уведомление: когда был вход/регистрация и сколько времени прошло с тех пор. */
    private void showNotification() {
        if (registrationTime == null) {
            JOptionPane.showMessageDialog(this,
                "Registration time is unknown.",
                "Notification",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String regTimeStr = registrationTime.format(formatter);

        LocalDateTime now      = LocalDateTime.now();
        Duration      duration = Duration.between(registrationTime, now);
        long days    = duration.toDays();
        long hours   = duration.toHours()   -  days * 24;
        long minutes = duration.toMinutes() - duration.toHours() * 60;

        StringBuilder howLong = new StringBuilder();
        if (days > 0) {
            howLong.append(days).append(" day").append(days > 1 ? "s " : " ");
        }
        if (hours > 0) {
            howLong.append(hours).append(" hour").append(hours > 1 ? "s " : " ");
        }
        howLong.append(minutes).append(" minute").append(minutes != 1 ? "s" : "");

        JOptionPane.showMessageDialog(this,
            "You registered on: " + regTimeStr + "\n" +
            "You have been using the app for: " + howLong,
            "Notification",
            JOptionPane.INFORMATION_MESSAGE
        );
    }

    /** Открывает диалог «Add Post» для публикации нового поста. */
    private void openAddPostDialog() {
        AddPostDialog dlg = new AddPostDialog(this);
        dlg.setVisible(true);
        if (!dlg.isSubmitted()) return;

        String content = dlg.getPostText();
        String type   = dlg.getPostType();
        if (content.isEmpty()) {
            reaction.showMessage("Cannot post empty message.", false);
            return;
        }

        // Добавляем пост в «My Posts»
        myPostsModel.addElement("[" + type + "] " + content);
        reaction.showMessage("Posted a new " + type + " post!", true);
    }

    /** Диалог для создания «поста» с выбором типа. */
    private static class AddPostDialog extends JDialog {
        private final JTextArea        textArea;
        private final JComboBox<String> typeCombo;
        private boolean        submitted = false;

        public AddPostDialog(JFrame parent) {
            super(parent, "Create New Post", true);
            setSize(400, 300);
            setLocationRelativeTo(parent);
            setLayout(new BorderLayout(5,5));

            // 1) Выбор типа поста
            JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
            top.add(new JLabel("Post Type:"));
            typeCombo = new JComboBox<>(new String[]{"Entertainment", "Educational"});
            top.add(typeCombo);
            add(top, BorderLayout.NORTH);

            // 2) Текст поста
            textArea = new JTextArea(8, 30);
            textArea.setLineWrap(true);
            textArea.setWrapStyleWord(true);
            add(new JScrollPane(textArea), BorderLayout.CENTER);

            // 3) Кнопки Post / Cancel
            JPanel btns = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            JButton btnSubmit = new JButton("Post");
            JButton btnCancel = new JButton("Cancel");
            btns.add(btnSubmit);
            btns.add(btnCancel);
            add(btns, BorderLayout.SOUTH);

            btnSubmit.addActionListener(e -> {
                submitted = true;
                dispose();
            });
            btnCancel.addActionListener(e -> dispose());
        }

        public boolean isSubmitted()     { return submitted; }
        public String  getPostText()     { return textArea.getText().trim(); }
        public String  getPostType()     { return (String) typeCombo.getSelectedItem(); }
    }
}
