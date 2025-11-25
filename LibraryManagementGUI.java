import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.net.URL;
import java.util.List;
import java.time.LocalDate;

public class LibraryManagementGUI extends JFrame {
    private static LibrarySystem system = new LibrarySystem(); 
    private static final String SAVE_FILE = "system_data.ser"; // Kept for legacy/backup structure
    
    private CardLayout cardLayout;
    private JPanel mainPanel;
    
    // Context Variables
    private Library currentLibrary; 
    private User currentStudent;
    private User eLibraryUser;
    private Library eLibrarySourceLib;

    // Panels
    private JPanel homePanel;
    private JPanel superAdminLoginPanel;
    private JPanel superAdminDashboardPanel;
    private JPanel libraryRegistrationPanel;
    private JPanel librarySelectionPanel;
    private JPanel studentLoginPanel;
    private JPanel studentRegisterPanel;
    private JPanel studentDashboardPanel;
    private JPanel librarianLoginPanel;
    private JPanel librarianDashboardPanel;
    private JPanel seatBookingPanel;
    private JPanel bookSearchPanel;
    private JPanel myBooksPanel;
    
    // eLibrary Panels
    private JPanel eLibraryLoginPanel;
    private JPanel eLibraryDashboardPanel;
    private JPanel eBookReaderPanel;
    private JPanel eCommercePanel;
    private JPanel eLibraryAccountPanel;
    private JPanel superAdminELibManagePanel;

    // --- Modern Dark Theme Colors ---
    private static final Color COLOR_DARK_BG = new Color(37, 37, 37); 
    private static final Color COLOR_DARK_SIDEBAR = new Color(45, 45, 45); 
    private static final Color COLOR_ACCENT_PURPLE = new Color(138, 43, 226); 
    private static final Color COLOR_TEXT_WHITE = new Color(245, 245, 245);
    private static final Color COLOR_TEXT_GRAY = new Color(160, 160, 160);
    private static final Color COLOR_INPUT_BORDER = new Color(100, 100, 100);
    
    // Fonts
    private static final Font FONT_HEADER = new Font("Segoe UI", Font.BOLD, 28);
    private static final Font FONT_LABEL = new Font("Segoe UI", Font.PLAIN, 12);
    private static final Font FONT_INPUT = new Font("Segoe UI", Font.PLAIN, 14);
    
    // Image Paths
    private static final String LEFT_PANEL_IMAGE_PATH = "C:\\Users\\kumar\\Downloads\\ChatGPT Image Jun 2, 2025, 10_08_49 PM.png"; 
    private static final String RIGHT_PANEL_IMAGE_PATH = "C:\\Users\\kumar\\Downloads\\ChatGPT Image Jun 2, 2025, 09_57_02 PM.png";

    /**
     * Custom Image Panel used for backgrounds
     */
    private static class ImagePanel extends JPanel {
        private Image backgroundImage;
        
        public ImagePanel(String imagePath) {
            try {
                File f = new File(imagePath);
                if (f.exists()) {
                    this.backgroundImage = new ImageIcon(f.toURI().toURL()).getImage();
                } else {
                    URL imgUrl = getClass().getResource(imagePath);
                    if(imgUrl != null) this.backgroundImage = new ImageIcon(imgUrl).getImage();
                }
            } catch (Exception e) { e.printStackTrace(); }
            setBackground(COLOR_DARK_SIDEBAR); 
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (backgroundImage != null) {
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                g.setColor(new Color(0, 0, 0, 160)); // Dark Overlay for readability
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        }
    }

    /**
     * Custom Glass Panel (Rounded, Semi-Transparent)
     */
    private static class GlassPanel extends JPanel {
        public GlassPanel() {
            setOpaque(false);
            setBorder(new EmptyBorder(20, 20, 20, 20));
        }
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            // Semi-transparent dark background (Frosted glass effect)
            g2d.setColor(new Color(30, 30, 30, 200)); 
            g2d.fill(new java.awt.geom.RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 30, 30));
            
            // Subtle white border
            g2d.setColor(new Color(255, 255, 255, 30));
            g2d.draw(new java.awt.geom.RoundRectangle2D.Double(0, 0, getWidth()-1, getHeight()-1, 30, 30));
            
            g2d.dispose();
            super.paintComponent(g);
        }
    }

    public LibraryManagementGUI() {
        setTitle("Library Management System");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                try { LibrarySystem.saveData(system, SAVE_FILE); } catch (Exception ex) { ex.printStackTrace(); }
                System.exit(0);
            }
        });
        setSize(1100, 750);
        setLocationRelativeTo(null);

        UIManager.put("OptionPane.background", COLOR_DARK_BG);
        UIManager.put("Panel.background", COLOR_DARK_BG);
        UIManager.put("OptionPane.messageForeground", COLOR_TEXT_WHITE);
        
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        mainPanel.setBackground(COLOR_DARK_BG);

        // Init all panels
        createHomePanel();
        createSuperAdminLoginPanel();
        createSuperAdminDashboardPanel();
        createLibraryRegistrationPanel();
        createLibrarySelectionPanel();
        createStudentLoginPanel();
        createStudentRegisterPanel();
        createStudentDashboardPanel();
        createLibrarianLoginPanel();
        createLibrarianDashboardPanel();
        createSeatBookingPanel();
        createBookSearchPanel();
        createMyBooksPanel();
        createELibraryLoginPanel();
        createELibraryDashboardPanel();
        createEBookReaderPanel();
        createECommercePanel();
        createELibraryAccountPanel();
        createSuperAdminELibManagePanel();

        // Add panels to layout
        mainPanel.add(homePanel, "Home");
        mainPanel.add(superAdminLoginPanel, "SuperAdminLogin");
        mainPanel.add(superAdminDashboardPanel, "SuperAdminDashboard");
        mainPanel.add(libraryRegistrationPanel, "LibraryRegister");
        mainPanel.add(librarySelectionPanel, "LibrarySelect");
        mainPanel.add(studentLoginPanel, "StudentLogin");
        mainPanel.add(studentRegisterPanel, "StudentRegister");
        mainPanel.add(studentDashboardPanel, "StudentDashboard");
        mainPanel.add(librarianLoginPanel, "LibrarianLogin");
        mainPanel.add(librarianDashboardPanel, "LibrarianDashboard");
        mainPanel.add(seatBookingPanel, "SeatBooking");
        mainPanel.add(bookSearchPanel, "BookSearch");
        mainPanel.add(myBooksPanel, "MyBooks");
        mainPanel.add(eLibraryLoginPanel, "ELibLogin");
        mainPanel.add(eLibraryDashboardPanel, "ELibDashboard");
        mainPanel.add(eBookReaderPanel, "EBookReader");
        mainPanel.add(eCommercePanel, "ECommerce");
        mainPanel.add(eLibraryAccountPanel, "ELibAccount");
        mainPanel.add(superAdminELibManagePanel, "SuperAdminELib");

        add(mainPanel);
        setVisible(true);
    }

    // --- STYLING ---
    private void styleMaterialInput(JTextField field) {
        field.setOpaque(false);
        field.setForeground(COLOR_TEXT_WHITE);
        field.setCaretColor(COLOR_TEXT_WHITE);
        field.setFont(FONT_INPUT);
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, COLOR_INPUT_BORDER), 
            BorderFactory.createEmptyBorder(5, 0, 10, 0)
        ));
    }

    private void stylePrimaryBtn(JButton btn) {
        btn.setBackground(COLOR_ACCENT_PURPLE);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) { btn.setBackground(COLOR_ACCENT_PURPLE.brighter()); }
            public void mouseExited(java.awt.event.MouseEvent evt) { btn.setBackground(COLOR_ACCENT_PURPLE); }
        });
    }

    private void styleLinkBtn(JButton btn) {
        btn.setOpaque(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setForeground(COLOR_TEXT_GRAY);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) { btn.setForeground(Color.WHITE); }
            public void mouseExited(java.awt.event.MouseEvent evt) { btn.setForeground(COLOR_TEXT_GRAY); }
        });
    }
    
    private void applyDigitLimitFilter(JTextField textField, int limit) {
        ((AbstractDocument) textField.getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override
            public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
                if (string == null) return;
                if (string.matches("\\d+") && (fb.getDocument().getLength() + string.length()) <= limit) {
                    super.insertString(fb, offset, string, attr);
                }
            }
            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                if (text == null) return;
                if (text.matches("\\d*") && (fb.getDocument().getLength() - length + text.length()) <= limit) { 
                    super.replace(fb, offset, length, text, attrs);
                }
            }
        });
    }

    // --- UI HELPER: Split Pane with Glass Effect ---
    private JPanel createSplitPane(JPanel formPanel, String titleOverlay, String subOverlay) {
        JPanel splitPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        
        // Left Panel (Image 1) - 20% Width
        ImagePanel leftPanel = new ImagePanel(LEFT_PANEL_IMAGE_PATH);
        leftPanel.setLayout(new GridBagLayout());
        GridBagConstraints leftGbc = new GridBagConstraints();
        leftGbc.gridwidth = GridBagConstraints.REMAINDER;
        JLabel lblTitle = new JLabel(titleOverlay);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 36));
        lblTitle.setForeground(Color.WHITE);
        leftPanel.add(lblTitle, leftGbc);
        JLabel lblSub = new JLabel(subOverlay);
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblSub.setForeground(new Color(200, 200, 200));
        leftPanel.add(lblSub, leftGbc);
        
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.weightx = 0.20; // 20% width
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        splitPanel.add(leftPanel, gbc);
        
        // Right Panel (Image 2) - 80% Width
        ImagePanel rightContainer = new ImagePanel(RIGHT_PANEL_IMAGE_PATH);
        rightContainer.setLayout(new GridBagLayout());
        
        // Wrap form in Glass Panel
        GlassPanel glassWrapper = new GlassPanel();
        glassWrapper.setLayout(new GridBagLayout());
        glassWrapper.add(formPanel);
        rightContainer.add(glassWrapper);
        
        gbc.gridx = 1;
        gbc.weightx = 0.80; // 80% width
        splitPanel.add(rightContainer, gbc);
        
        return splitPanel;
    }

    private void addLabelInput(JPanel p, String text, JComponent comp, GridBagConstraints gbc) {
        JLabel l = new JLabel(text.toUpperCase()); 
        l.setFont(FONT_LABEL); 
        l.setForeground(COLOR_TEXT_GRAY);
        p.add(l, gbc);
        p.add(comp, gbc);
    }

    // --- PANELS IMPLEMENTATION ---

    private void createHomePanel() {
        JPanel innerForm = new JPanel(new GridLayout(5, 1, 0, 20));
        innerForm.setOpaque(false);
        
        JButton b1 = new JButton("Student Portal"); stylePrimaryBtn(b1);
        JButton b2 = new JButton("Librarian Portal"); stylePrimaryBtn(b2);
        JButton b3 = new JButton("Super Admin"); stylePrimaryBtn(b3);
        JButton b4 = new JButton("Register New Library"); stylePrimaryBtn(b4);
        JButton b5 = new JButton("eLibrary Portal (PDFs)"); stylePrimaryBtn(b5);
        b5.setBackground(new Color(100, 50, 180));
        
        b1.addActionListener(e -> { refreshLibraryList(); cardLayout.show(mainPanel, "LibrarySelect"); }); 
        b2.addActionListener(e -> { refreshLibraryList(); cardLayout.show(mainPanel, "LibrarySelect"); }); 
        b3.addActionListener(e -> cardLayout.show(mainPanel, "SuperAdminLogin"));
        b4.addActionListener(e -> cardLayout.show(mainPanel, "LibraryRegister"));
        b5.addActionListener(e -> { refreshELibraryLogin(); cardLayout.show(mainPanel, "ELibLogin"); });
        
        innerForm.add(b1); innerForm.add(b2); innerForm.add(b3); innerForm.add(b4); innerForm.add(b5);
        
        homePanel = createSplitPane(innerForm, "Welcome", "Library Management System");
    }

    private void createStudentLoginPanel() {
        JPanel form = new JPanel(new GridBagLayout());
        form.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 0, 10, 0);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        
        JLabel header = new JLabel("Student Login"); 
        header.setFont(FONT_HEADER); header.setForeground(Color.WHITE);
        
        JTextField user = new JTextField(20); styleMaterialInput(user);
        JPasswordField pass = new JPasswordField(20); styleMaterialInput(pass);
        
        JButton login = new JButton("Sign In"); stylePrimaryBtn(login);
        
        JPanel links = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0)); links.setOpaque(false);
        JButton reg = new JButton("Register"); styleLinkBtn(reg);
        JButton back = new JButton("Back"); styleLinkBtn(back);
        links.add(reg); links.add(new JLabel(" | ")); links.add(back);
        
        form.add(header, gbc);
        addLabelInput(form, "USERNAME", user, gbc);
        addLabelInput(form, "PASSWORD", pass, gbc);
        gbc.insets = new Insets(20, 0, 20, 0); form.add(login, gbc);
        form.add(links, gbc);
        
        studentLoginPanel = createSplitPane(form, "Student Area", "Access your books and study space");
        
        login.addActionListener(e -> {
            if(currentLibrary == null) return;
            User u = currentLibrary.authenticateUser(user.getText(), new String(pass.getPassword()));
            if(u != null) {
                currentStudent = u;
                updateStudentDashboard();
                cardLayout.show(mainPanel, "StudentDashboard");
            } else JOptionPane.showMessageDialog(this, "Invalid Credentials");
        });
        reg.addActionListener(e -> cardLayout.show(mainPanel, "StudentRegister"));
        back.addActionListener(e -> cardLayout.show(mainPanel, "LibrarySelect"));
    }

    private void createStudentRegisterPanel() {
        JPanel form = new JPanel(new GridBagLayout());
        form.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 0, 5, 0);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        
        JLabel header = new JLabel("Create Account"); 
        header.setFont(FONT_HEADER); header.setForeground(Color.WHITE);
        form.add(header, gbc);
        
        JTextField u = new JTextField(15); styleMaterialInput(u);
        JPasswordField p = new JPasswordField(15); styleMaterialInput(p);
        JTextField n = new JTextField(15); styleMaterialInput(n);
        JComboBox<String> gender = new JComboBox<>(new String[]{"Male", "Female", "Other"});
        JTextField aadhaar = new JTextField(15); styleMaterialInput(aadhaar); applyDigitLimitFilter(aadhaar, 12);
        JTextField ph = new JTextField(15); styleMaterialInput(ph); applyDigitLimitFilter(ph, 10);
        JTextField addr = new JTextField(15); styleMaterialInput(addr);
        
        addLabelInput(form, "USERNAME", u, gbc);
        addLabelInput(form, "PASSWORD", p, gbc);
        addLabelInput(form, "FULL NAME", n, gbc);
        addLabelInput(form, "GENDER", gender, gbc);
        addLabelInput(form, "AADHAAR (12)", aadhaar, gbc);
        addLabelInput(form, "PHONE (10)", ph, gbc);
        addLabelInput(form, "ADDRESS", addr, gbc);
        
        JButton btn = new JButton("Sign Up"); stylePrimaryBtn(btn);
        gbc.insets = new Insets(20, 0, 10, 0);
        form.add(btn, gbc);
        
        JButton back = new JButton("Back to Sign In"); styleLinkBtn(back);
        form.add(back, gbc);
        
        studentRegisterPanel = createSplitPane(form, "Join Us", "Start your learning journey");
        
        btn.addActionListener(e -> {
            if(currentLibrary != null) {
                try {
                    String res = currentLibrary.registerUser(u.getText(), new String(p.getPassword()), n.getText(), 
                            (String)gender.getSelectedItem(), aadhaar.getText(), ph.getText(), addr.getText());
                    JOptionPane.showMessageDialog(this, res);
                    if(res.startsWith("(+)")) cardLayout.show(mainPanel, "StudentLogin");
                } catch (LibraryActionException ex) {
                    JOptionPane.showMessageDialog(this, ex.getMessage());
                }
            }
        });
        back.addActionListener(e -> cardLayout.show(mainPanel, "StudentLogin"));
    }

    private void createLibraryRegistrationPanel() {
        JPanel form = new JPanel(new GridBagLayout());
        form.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 0, 5, 0);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        
        JLabel header = new JLabel("Register Library"); 
        header.setFont(FONT_HEADER); header.setForeground(Color.WHITE);
        form.add(header, gbc);

        JTextField name = new JTextField(15); styleMaterialInput(name);
        JTextField addr = new JTextField(15); styleMaterialInput(addr);
        JTextField link = new JTextField(15); styleMaterialInput(link);
        JTextField owner = new JTextField(15); styleMaterialInput(owner);
        JTextField phone = new JTextField(15); styleMaterialInput(phone); applyDigitLimitFilter(phone, 10);
        JTextField email = new JTextField(15); styleMaterialInput(email);
        JTextField aadhaar = new JTextField(15); styleMaterialInput(aadhaar); applyDigitLimitFilter(aadhaar, 12);
        
        JButton btnUpload = new JButton("Upload Photo"); styleLinkBtn(btnUpload);
        JLabel lblPhoto = new JLabel("None"); lblPhoto.setForeground(COLOR_TEXT_GRAY);
        btnUpload.addActionListener(e -> {
            JFileChooser fc = new JFileChooser();
            if(fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) lblPhoto.setText(fc.getSelectedFile().getName());
        });
        
        addLabelInput(form, "LIBRARY NAME", name, gbc);
        addLabelInput(form, "ADDRESS", addr, gbc);
        addLabelInput(form, "LOCATION LINK", link, gbc);
        addLabelInput(form, "OWNER NAME", owner, gbc);
        addLabelInput(form, "OWNER PHONE", phone, gbc);
        addLabelInput(form, "OWNER EMAIL", email, gbc);
        addLabelInput(form, "OWNER AADHAAR", aadhaar, gbc);
        
        JPanel photoP = new JPanel(new FlowLayout(FlowLayout.LEFT)); photoP.setOpaque(false);
        photoP.add(new JLabel("PHOTO: ")); photoP.getComponent(0).setForeground(COLOR_TEXT_GRAY);
        photoP.add(btnUpload); photoP.add(lblPhoto);
        form.add(photoP, gbc);
        
        JButton btn = new JButton("Register & Pay ₹20"); stylePrimaryBtn(btn);
        gbc.insets = new Insets(20, 0, 10, 0);
        form.add(btn, gbc);
        
        JButton back = new JButton("Back to Home"); styleLinkBtn(back);
        back.addActionListener(e -> cardLayout.show(mainPanel, "Home"));
        form.add(back, gbc);
        
        libraryRegistrationPanel = createSplitPane(form, "Grow With Us", "Register your library on our platform");
        
        btn.addActionListener(e -> {
            if(name.getText().isEmpty()) { JOptionPane.showMessageDialog(this, "Name required"); return; }
            int confirm = JOptionPane.showConfirmDialog(this, "Pay ₹20?", "Payment", JOptionPane.YES_NO_OPTION);
            if(confirm == JOptionPane.YES_OPTION) {
                LibraryProfile p = system.createRegistrationRequest(name.getText(), addr.getText(), link.getText(), 
                        lblPhoto.getText(), owner.getText(), phone.getText(), email.getText(), aadhaar.getText());
                system.processRegistrationPayment(p.getLibraryId(), 20.0);
                JOptionPane.showMessageDialog(this, "Submitted for Approval!");
                cardLayout.show(mainPanel, "Home");
            }
        });
    }

    private void createSuperAdminLoginPanel() {
        JPanel form = new JPanel(new GridBagLayout());
        form.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        
        JLabel header = new JLabel("Super Admin"); header.setFont(FONT_HEADER); header.setForeground(Color.WHITE);
        form.add(header, gbc);
        
        JTextField u = new JTextField(15); styleMaterialInput(u);
        JPasswordField p = new JPasswordField(15); styleMaterialInput(p);
        
        gbc.insets = new Insets(10, 0, 5, 0);
        addLabelInput(form, "ADMIN USER", u, gbc);
        addLabelInput(form, "PASSWORD", p, gbc);
        
        JButton btn = new JButton("Login"); stylePrimaryBtn(btn);
        gbc.insets = new Insets(20, 0, 10, 0);
        form.add(btn, gbc);
        
        JButton back = new JButton("Back"); styleLinkBtn(back);
        back.addActionListener(e -> cardLayout.show(mainPanel, "Home"));
        form.add(back, gbc);
        
        superAdminLoginPanel = createSplitPane(form, "System Control", "Super Admin Access Only");
        
        btn.addActionListener(e -> {
            if(system.authenticateSuperAdmin(u.getText(), new String(p.getPassword()))) {
                refreshSuperAdminDashboard(); cardLayout.show(mainPanel, "SuperAdminDashboard");
            } else JOptionPane.showMessageDialog(this, "Invalid Credentials");
        });
    }
    
    private void createLibrarianLoginPanel() {
        JPanel form = new JPanel(new GridBagLayout());
        form.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        
        JLabel header = new JLabel("Librarian Login"); header.setFont(FONT_HEADER); header.setForeground(Color.WHITE);
        form.add(header, gbc);
        
        JTextField u = new JTextField(15); styleMaterialInput(u);
        JPasswordField p = new JPasswordField(15); styleMaterialInput(p);
        
        gbc.insets = new Insets(10, 0, 5, 0);
        addLabelInput(form, "USERNAME", u, gbc);
        addLabelInput(form, "PASSWORD", p, gbc);
        
        JButton btn = new JButton("Login"); stylePrimaryBtn(btn);
        gbc.insets = new Insets(20, 0, 10, 0);
        form.add(btn, gbc);
        
        JButton back = new JButton("Change Library"); styleLinkBtn(back);
        back.addActionListener(e -> cardLayout.show(mainPanel, "LibrarySelect"));
        form.add(back, gbc);
        
        librarianLoginPanel = createSplitPane(form, "Manage Library", "Administer books and users");
        
        btn.addActionListener(e -> {
            if(currentLibrary != null && currentLibrary.authenticateLibrarian(u.getText(), new String(p.getPassword()))) {
                createLibrarianDashboardPanel();
                cardLayout.show(mainPanel, "LibrarianDashboard");
            } else JOptionPane.showMessageDialog(this, "Invalid");
        });
    }
    
    private void createELibraryLoginPanel() {
        eLibraryLoginPanel = new JPanel(new BorderLayout());
        eLibraryLoginPanel.setOpaque(false);
    }
    
    private void refreshELibraryLogin() {
        eLibraryLoginPanel.removeAll();
        
        JPanel form = new JPanel(new GridBagLayout());
        form.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        
        JLabel header = new JLabel("eLibrary Access"); header.setFont(FONT_HEADER); header.setForeground(Color.WHITE);
        form.add(header, gbc);
        
        JComboBox<String> libBox = new JComboBox<>();
        system.getAllLibraries().forEach(l -> libBox.addItem(l.getProfile().getLibraryName()));
        gbc.insets = new Insets(10, 0, 5, 0);
        addLabelInput(form, "SELECT LIBRARY", libBox, gbc);
        
        JTextField u = new JTextField(15); styleMaterialInput(u);
        JPasswordField p = new JPasswordField(15); styleMaterialInput(p);
        addLabelInput(form, "USERNAME", u, gbc);
        addLabelInput(form, "PASSWORD", p, gbc);
        
        JButton login = new JButton("Enter"); stylePrimaryBtn(login);
        gbc.insets = new Insets(20, 0, 10, 0);
        form.add(login, gbc);
        
        JButton back = new JButton("Back"); styleLinkBtn(back);
        back.addActionListener(e -> cardLayout.show(mainPanel, "Home"));
        form.add(back, gbc);
        
        JPanel splitContent = createSplitPane(form, "Digital Library", "Read eBooks & Order Physical Copies");
        eLibraryLoginPanel.add(splitContent, BorderLayout.CENTER);
        
        login.addActionListener(e -> {
            if(libBox.getItemCount() > 0) {
                Library l = system.getAllLibraries().get(libBox.getSelectedIndex());
                User user = l.authenticateUser(u.getText(), new String(p.getPassword()));
                if(user != null) {
                    eLibraryUser = user; eLibrarySourceLib = l;
                    cardLayout.show(mainPanel, "ELibDashboard");
                } else JOptionPane.showMessageDialog(this, "Invalid");
            }
        });
        
        eLibraryLoginPanel.revalidate(); 
        eLibraryLoginPanel.repaint();
    }
    
    private void createLibrarySelectionPanel() {
        librarySelectionPanel = new JPanel(new BorderLayout());
        librarySelectionPanel.setOpaque(false);
    }
    
    private void refreshLibraryList() {
        librarySelectionPanel.removeAll();
        
        ImagePanel bg = new ImagePanel(RIGHT_PANEL_IMAGE_PATH);
        bg.setLayout(new GridBagLayout());
        
        GlassPanel glass = new GlassPanel();
        glass.setLayout(new BorderLayout());
        glass.setPreferredSize(new Dimension(600, 500));
        
        JLabel title = new JLabel("Select Your Library", SwingConstants.CENTER);
        title.setFont(FONT_HEADER); title.setForeground(Color.WHITE);
        title.setBorder(new EmptyBorder(20, 0, 20, 0));
        glass.add(title, BorderLayout.NORTH);

        JPanel listPanel = new JPanel(new GridLayout(0, 1, 10, 10));
        listPanel.setOpaque(false);
        
        List<Library> libs = system.getAllLibraries();
        if(libs.isEmpty()){
            JLabel l = new JLabel("No libraries registered yet.", SwingConstants.CENTER);
            l.setForeground(COLOR_TEXT_GRAY);
            listPanel.add(l);
        }

        for(Library lib : libs) {
            JButton btn = new JButton(lib.getProfile().getLibraryName());
            stylePrimaryBtn(btn);
            btn.setBackground(COLOR_DARK_SIDEBAR); 
            btn.addActionListener(e -> {
                currentLibrary = lib;
                String[] options = {"Student", "Librarian"};
                int ch = JOptionPane.showOptionDialog(this, "Login Role?", "Role", 0, 3, null, options, options[0]);
                if(ch == 0) cardLayout.show(mainPanel, "StudentLogin");
                else if(ch == 1) cardLayout.show(mainPanel, "LibrarianLogin");
            });
            listPanel.add(btn);
        }
        glass.add(new JScrollPane(listPanel), BorderLayout.CENTER);
        
        JButton back = new JButton("Back"); styleLinkBtn(back);
        back.addActionListener(e -> cardLayout.show(mainPanel, "Home"));
        glass.add(back, BorderLayout.SOUTH);
        
        bg.add(glass);
        librarySelectionPanel.add(bg, BorderLayout.CENTER);
        librarySelectionPanel.revalidate(); librarySelectionPanel.repaint();
    }

    private void createStudentDashboardPanel() {
        studentDashboardPanel = new JPanel(new BorderLayout());
        ImagePanel bg = new ImagePanel(RIGHT_PANEL_IMAGE_PATH);
        bg.setLayout(new GridBagLayout());
        
        GlassPanel glass = new GlassPanel();
        glass.setLayout(new GridLayout(5, 1, 10, 15));
        glass.setPreferredSize(new Dimension(400, 400));
        
        JLabel title = new JLabel("Student Dashboard", SwingConstants.CENTER); 
        title.setFont(FONT_HEADER); title.setForeground(Color.WHITE);
        glass.add(title);
        
        JButton b1 = new JButton("Search Books / Reserve"); stylePrimaryBtn(b1);
        JButton b2 = new JButton("Book Seat (Study Space)"); stylePrimaryBtn(b2);
        JButton b3 = new JButton("eLibrary Portal"); stylePrimaryBtn(b3);
        JButton b4 = new JButton("View My Borrowed Books"); stylePrimaryBtn(b4);
        JButton b5 = new JButton("Logout"); styleLinkBtn(b5); b5.setForeground(Color.RED);
        
        b1.addActionListener(e -> { updateBookSearchPanel(); cardLayout.show(mainPanel, "BookSearch"); });
        b2.addActionListener(e -> { refreshSeatBookingPanel(); cardLayout.show(mainPanel, "SeatBooking"); });
        b3.addActionListener(e -> { refreshELibraryLogin(); cardLayout.show(mainPanel, "ELibLogin"); });
        b4.addActionListener(e -> { updateMyBooksPanel(); cardLayout.show(mainPanel, "MyBooks"); });
        b5.addActionListener(e -> cardLayout.show(mainPanel, "StudentLogin"));
        
        glass.add(b1); glass.add(b2); glass.add(b3); glass.add(b4); glass.add(b5);
        bg.add(glass);
        studentDashboardPanel.add(bg, BorderLayout.CENTER);
    }
    
    private void updateStudentDashboard() {}

    private void createLibrarianDashboardPanel() {
        if(librarianDashboardPanel == null) librarianDashboardPanel = new JPanel(new BorderLayout());
        librarianDashboardPanel.removeAll();
        
        ImagePanel bg = new ImagePanel(RIGHT_PANEL_IMAGE_PATH);
        bg.setLayout(new GridBagLayout());
        
        GlassPanel glass = new GlassPanel();
        glass.setLayout(new GridLayout(3, 1, 20, 20));
        glass.setPreferredSize(new Dimension(400, 300));
        
        JLabel t = new JLabel("Librarian Dashboard", SwingConstants.CENTER); 
        t.setFont(FONT_HEADER); t.setForeground(Color.WHITE);
        glass.add(t);
        
        JButton export = new JButton("Export Logs (CSV)"); stylePrimaryBtn(export);
        JButton out = new JButton("Logout"); styleLinkBtn(out); out.setForeground(Color.RED);
        
        export.addActionListener(e -> {
            JFileChooser fc = new JFileChooser();
            fc.setSelectedFile(new File("transactions.csv"));
            if(fc.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                String res = currentLibrary.exportTransactionsToCSV(fc.getSelectedFile().getAbsolutePath());
                JOptionPane.showMessageDialog(this, res);
            }
        });
        out.addActionListener(e -> cardLayout.show(mainPanel, "LibrarianLogin"));
        
        glass.add(export); glass.add(out);
        bg.add(glass);
        librarianDashboardPanel.add(bg, BorderLayout.CENTER);
        librarianDashboardPanel.revalidate(); librarianDashboardPanel.repaint();
    }

    private void createSuperAdminDashboardPanel() { superAdminDashboardPanel = new JPanel(new BorderLayout()); superAdminDashboardPanel.setOpaque(false); }
    private void refreshSuperAdminDashboard() {
        superAdminDashboardPanel.removeAll();
        
        ImagePanel bg = new ImagePanel(RIGHT_PANEL_IMAGE_PATH);
        bg.setLayout(new GridBagLayout());
        
        GlassPanel glass = new GlassPanel();
        glass.setLayout(new BorderLayout());
        glass.setPreferredSize(new Dimension(800, 600));
        
        JLabel t = new JLabel("Super Admin Dashboard", SwingConstants.CENTER); 
        t.setForeground(Color.WHITE); t.setFont(FONT_HEADER);
        t.setBorder(new EmptyBorder(20,0,20,0));
        glass.add(t, BorderLayout.NORTH);
        
        DefaultTableModel model = new DefaultTableModel(new String[]{"Library", "Owner", "Status"}, 0);
        JTable table = new JTable(model);
        for(LibraryProfile p : system.getPendingRequests()) model.addRow(new Object[]{p.getLibraryName(), p.getOwnerName(), "Pending"});
        glass.add(new JScrollPane(table), BorderLayout.CENTER);
        
        JPanel btnPanel = new JPanel(); btnPanel.setOpaque(false);
        JButton approve = new JButton("Approve Selected"); stylePrimaryBtn(approve);
        approve.addActionListener(e -> {
            if(table.getSelectedRow() >= 0) {
                LibraryProfile p = system.getPendingRequests().get(table.getSelectedRow());
                JTextField user = new JTextField(); JTextField pass = new JTextField();
                Object[] msg = {"Set Admin User:", user, "Set Admin Pass:", pass};
                if(JOptionPane.showConfirmDialog(this, msg) == JOptionPane.OK_OPTION) {
                    system.approveLibrary(p.getLibraryId(), user.getText(), pass.getText());
                    refreshSuperAdminDashboard();
                }
            }
        });
        btnPanel.add(approve);

        JButton back = new JButton("Logout"); styleLinkBtn(back);
        back.addActionListener(e -> cardLayout.show(mainPanel, "Home"));
        btnPanel.add(back);
        
        glass.add(btnPanel, BorderLayout.SOUTH);
        bg.add(glass);
        
        superAdminDashboardPanel.add(bg, BorderLayout.CENTER);
        superAdminDashboardPanel.revalidate();
    }
    
    private void createSeatBookingPanel() { seatBookingPanel = new JPanel(new BorderLayout()); seatBookingPanel.setOpaque(false); }
    private void refreshSeatBookingPanel() {
        seatBookingPanel.removeAll();
        
        ImagePanel bg = new ImagePanel(RIGHT_PANEL_IMAGE_PATH);
        bg.setLayout(new GridBagLayout());
        
        GlassPanel glass = new GlassPanel(); glass.setLayout(new BorderLayout());
        glass.setPreferredSize(new Dimension(500, 500));
        
        JLabel t = new JLabel("Book Seat", SwingConstants.CENTER); t.setForeground(Color.WHITE); t.setFont(FONT_HEADER);
        glass.add(t, BorderLayout.NORTH);
        
        JPanel slots = new JPanel(new GridLayout(0, 1, 10, 10)); slots.setOpaque(false);
        for(String slot : currentLibrary.getSlotTimings()) {
            int avail = currentLibrary.getAvailableSeats(LocalDate.now(), slot);
            JButton btn = new JButton(slot + " (Available: " + avail + ")");
            stylePrimaryBtn(btn);
            if(avail <= 0) { btn.setEnabled(false); btn.setBackground(Color.GRAY); }
            btn.addActionListener(e -> {
                try {
                    JOptionPane.showMessageDialog(this, currentLibrary.bookSeat(currentStudent, LocalDate.now(), slot));
                    refreshSeatBookingPanel();
                } catch (LibraryActionException ex) {
                    JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
                }
            });
            slots.add(btn);
        }
        glass.add(new JScrollPane(slots), BorderLayout.CENTER);
        
        JButton back = new JButton("Back"); styleLinkBtn(back);
        back.addActionListener(e -> cardLayout.show(mainPanel, "StudentDashboard"));
        glass.add(back, BorderLayout.SOUTH);
        
        bg.add(glass);
        seatBookingPanel.add(bg, BorderLayout.CENTER);
        seatBookingPanel.revalidate();
    }
    
    private void createBookSearchPanel() { bookSearchPanel = new JPanel(new BorderLayout()); bookSearchPanel.setOpaque(false); }
    private void updateBookSearchPanel() {
        bookSearchPanel.removeAll();
        ImagePanel bg = new ImagePanel(RIGHT_PANEL_IMAGE_PATH);
        bg.setLayout(new GridBagLayout());
        
        GlassPanel glass = new GlassPanel(); glass.setLayout(new BorderLayout());
        glass.setPreferredSize(new Dimension(700, 500));
        
        DefaultTableModel m = new DefaultTableModel(new String[]{"Title", "Author", "Status"}, 0);
        JTable t = new JTable(m);
        for(Book b : currentLibrary.getAllBooks()) m.addRow(new Object[]{b.getTitle(), b.getAuthor(), b.isAvailable()?"Available":"Borrowed"});
        glass.add(new JScrollPane(t), BorderLayout.CENTER);
        
        JButton act = new JButton("Borrow/Reserve Selected"); stylePrimaryBtn(act);
        act.addActionListener(e -> {
            if(t.getSelectedRow() >= 0) {
                String title = (String) m.getValueAt(t.getSelectedRow(), 0);
                try {
                    String res = currentLibrary.searchBook(title).isAvailable() ? 
                                 currentLibrary.borrowBook(currentStudent, title) : 
                                 currentLibrary.reserveBook(currentStudent, title);
                    JOptionPane.showMessageDialog(this, res);
                    updateBookSearchPanel();
                } catch (LibraryActionException ex) {
                    JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
                }
            }
        });
        glass.add(act, BorderLayout.NORTH);
        JButton back = new JButton("Back"); styleLinkBtn(back);
        back.addActionListener(e -> cardLayout.show(mainPanel, "StudentDashboard"));
        glass.add(back, BorderLayout.SOUTH);
        
        bg.add(glass);
        bookSearchPanel.add(bg, BorderLayout.CENTER);
        bookSearchPanel.revalidate();
    }
    
    private void createMyBooksPanel() { myBooksPanel = new JPanel(new BorderLayout()); myBooksPanel.setOpaque(false); }
    private void updateMyBooksPanel() {
        myBooksPanel.removeAll();
        ImagePanel bg = new ImagePanel(RIGHT_PANEL_IMAGE_PATH);
        bg.setLayout(new GridBagLayout());
        
        GlassPanel glass = new GlassPanel(); glass.setLayout(new BorderLayout());
        glass.setPreferredSize(new Dimension(700, 500));
        
        JLabel t = new JLabel("My Borrowed Books", SwingConstants.CENTER); t.setForeground(Color.WHITE); t.setFont(FONT_HEADER);
        glass.add(t, BorderLayout.NORTH);
        
        DefaultTableModel m = new DefaultTableModel(new String[]{"Title", "Author", "Due Date"}, 0);
        JTable table = new JTable(m);
        
        // Filter books borrowed by current user
        List<Book> allBooks = currentLibrary.getAllBooks();
        List<Book> myBooks = currentLibrary.filterList(allBooks, 
            b -> !b.isAvailable() && b.getBorrowedBy() != null && b.getBorrowedBy().getUsername().equals(currentStudent.getUsername()));
            
        for(Book b : myBooks) {
            m.addRow(new Object[]{b.getTitle(), b.getAuthor(), b.getBorrowedDate().plusDays(15)});
        }
        
        glass.add(new JScrollPane(table), BorderLayout.CENTER);
        
        JButton btnReturn = new JButton("Return Selected"); stylePrimaryBtn(btnReturn);
        btnReturn.addActionListener(e -> {
            if(table.getSelectedRow() >= 0) {
                String title = (String) m.getValueAt(table.getSelectedRow(), 0);
                try {
                    String res = currentLibrary.returnBook(currentStudent, title);
                    JOptionPane.showMessageDialog(this, res);
                    updateMyBooksPanel(); // Refresh
                } catch (LibraryActionException ex) {
                    JOptionPane.showMessageDialog(this, ex.getMessage());
                }
            }
        });
        
        JPanel bottom = new JPanel(); bottom.setOpaque(false);
        JButton back = new JButton("Back"); styleLinkBtn(back);
        back.addActionListener(e -> cardLayout.show(mainPanel, "StudentDashboard"));
        bottom.add(btnReturn); bottom.add(back);
        glass.add(bottom, BorderLayout.SOUTH);
        
        bg.add(glass);
        myBooksPanel.add(bg, BorderLayout.CENTER);
        myBooksPanel.revalidate();
    }
    
    private void createELibraryDashboardPanel() { 
        eLibraryDashboardPanel = new JPanel(new BorderLayout());
        ImagePanel bg = new ImagePanel(RIGHT_PANEL_IMAGE_PATH);
        bg.setLayout(new GridBagLayout());
        
        GlassPanel glass = new GlassPanel();
        glass.setPreferredSize(new Dimension(600, 400));
        glass.setLayout(new GridLayout(2, 2, 20, 20));
        
        JButton b1 = new JButton("eBooks (PDF)"); stylePrimaryBtn(b1);
        JButton b2 = new JButton("Physical Store"); stylePrimaryBtn(b2);
        JButton b3 = new JButton("My Account"); stylePrimaryBtn(b3);
        JButton b4 = new JButton("Logout"); styleLinkBtn(b4);
        
        b1.addActionListener(e -> { refreshEBookReader(); cardLayout.show(mainPanel, "EBookReader"); });
        b2.addActionListener(e -> { refreshECommerce(); cardLayout.show(mainPanel, "ECommerce"); });
        b3.addActionListener(e -> { refreshELibAccount(); cardLayout.show(mainPanel, "ELibAccount"); });
        b4.addActionListener(e -> cardLayout.show(mainPanel, "Home"));
        
        glass.add(b1); glass.add(b2); glass.add(b3); glass.add(b4);
        bg.add(glass);
        eLibraryDashboardPanel.add(bg, BorderLayout.CENTER);
    }
    
    private void createEBookReaderPanel() { eBookReaderPanel = new JPanel(new BorderLayout()); eBookReaderPanel.setOpaque(false); }
    private void refreshEBookReader() { 
        eBookReaderPanel.removeAll();
        ImagePanel bg = new ImagePanel(RIGHT_PANEL_IMAGE_PATH);
        bg.setLayout(new GridBagLayout());
        
        GlassPanel glass = new GlassPanel(); glass.setLayout(new BorderLayout());
        glass.setPreferredSize(new Dimension(600, 500));
        
        JLabel t = new JLabel("eBooks Repository", SwingConstants.CENTER); t.setForeground(Color.WHITE); t.setFont(FONT_HEADER);
        glass.add(t, BorderLayout.NORTH);
        
        JPanel list = new JPanel(new GridLayout(0, 1, 10, 10)); list.setOpaque(false);
        for(StoreItem item : system.getStoreItems()) {
            if(item.getType() == StoreItem.ItemType.PDF) {
                JButton btn = new JButton(item.getTitle() + " - ₹" + item.getPrice());
                stylePrimaryBtn(btn);
                btn.addActionListener(e -> {
                    if(JOptionPane.showConfirmDialog(this, "Buy " + item.getTitle() + "?") == JOptionPane.YES_OPTION){
                        eLibraryUser.addExpenditure(item.getPrice());
                        JOptionPane.showMessageDialog(this, "Purchased! (Mock Download)");
                    }
                });
                list.add(btn);
            }
        }
        glass.add(new JScrollPane(list), BorderLayout.CENTER);
        JButton back = new JButton("Back"); styleLinkBtn(back);
        back.addActionListener(e -> cardLayout.show(mainPanel, "ELibDashboard"));
        glass.add(back, BorderLayout.SOUTH);
        
        bg.add(glass);
        eBookReaderPanel.add(bg, BorderLayout.CENTER);
        eBookReaderPanel.revalidate();
    }
    
    private void createECommercePanel() { eCommercePanel = new JPanel(new BorderLayout()); eCommercePanel.setOpaque(false); }
    private void refreshECommerce() { 
        eCommercePanel.removeAll();
        ImagePanel bg = new ImagePanel(RIGHT_PANEL_IMAGE_PATH);
        bg.setLayout(new GridBagLayout());
        
        GlassPanel glass = new GlassPanel(); glass.setLayout(new BorderLayout());
        glass.setPreferredSize(new Dimension(600, 500));
        
        JLabel t = new JLabel("Physical Book Store", SwingConstants.CENTER); t.setForeground(Color.WHITE); t.setFont(FONT_HEADER);
        glass.add(t, BorderLayout.NORTH);
        
        JPanel list = new JPanel(new GridLayout(0, 1, 10, 10)); list.setOpaque(false);
        for(StoreItem item : system.getStoreItems()) {
            if(item.getType() == StoreItem.ItemType.PHYSICAL) {
                JButton btn = new JButton(item.getTitle() + " - ₹" + item.getPrice());
                stylePrimaryBtn(btn);
                btn.addActionListener(e -> {
                    String addr = JOptionPane.showInputDialog("Shipping Address:");
                    if(addr != null && !addr.isEmpty()){
                        system.placeOrder(new Order(eLibraryUser.getUsername(), eLibrarySourceLib.getProfile().getLibraryId(), item.getTitle(), item.getPrice(), addr));
                        JOptionPane.showMessageDialog(this, "Ordered!");
                    }
                });
                list.add(btn);
            }
        }
        glass.add(new JScrollPane(list), BorderLayout.CENTER);
        JButton back = new JButton("Back"); styleLinkBtn(back);
        back.addActionListener(e -> cardLayout.show(mainPanel, "ELibDashboard"));
        glass.add(back, BorderLayout.SOUTH);
        
        bg.add(glass);
        eCommercePanel.add(bg, BorderLayout.CENTER);
        eCommercePanel.revalidate();
    }
    
    // --- FIX: Implemented missing refreshELibAccount method ---
    private void createELibraryAccountPanel() { eLibraryAccountPanel = new JPanel(new BorderLayout()); eLibraryAccountPanel.setOpaque(false); }
    private void refreshELibAccount() {
        eLibraryAccountPanel.removeAll();
        
        ImagePanel bg = new ImagePanel(RIGHT_PANEL_IMAGE_PATH);
        bg.setLayout(new GridBagLayout());
        
        GlassPanel glass = new GlassPanel(); 
        glass.setLayout(new BorderLayout());
        glass.setPreferredSize(new Dimension(600, 500));
        
        JLabel t = new JLabel("My Account", SwingConstants.CENTER); 
        t.setForeground(Color.WHITE); t.setFont(FONT_HEADER);
        glass.add(t, BorderLayout.NORTH);
        
        JTextArea area = new JTextArea();
        area.setEditable(false);
        area.setFont(new Font("Monospaced", Font.PLAIN, 14));
        
        if(eLibraryUser != null) {
            area.setText("User: " + eLibraryUser.getFullName() + "\nTotal Spent: ₹" + eLibraryUser.getELibraryExpenditure() + "\n\nOrders:\n");
            for(Order o : eLibraryUser.getMyOrders()) area.append(o.toString() + "\n");
        }
        
        glass.add(new JScrollPane(area), BorderLayout.CENTER);
        
        JButton back = new JButton("Back"); styleLinkBtn(back);
        back.addActionListener(e -> cardLayout.show(mainPanel, "ELibDashboard"));
        glass.add(back, BorderLayout.SOUTH);
        
        bg.add(glass);
        eLibraryAccountPanel.add(bg, BorderLayout.CENTER);
        eLibraryAccountPanel.revalidate();
    }
    
    private void createSuperAdminELibManagePanel() { superAdminELibManagePanel = new JPanel(); }

    public static void main(String[] args) {
        try { LibrarySystem loaded = LibrarySystem.loadData(SAVE_FILE); if(loaded != null) system = loaded; } catch (Exception e) { System.out.println("New System"); }
        SwingUtilities.invokeLater(LibraryManagementGUI::new);
    }
}