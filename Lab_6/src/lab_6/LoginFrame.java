package lab_6;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class LoginFrame extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private JPanel loginPanel;
    private JPanel registerPanel;
    private Steam steam;
    
    public LoginFrame(Steam steam) {
        super("Steam - Iniciar Sesión");
        this.steam = steam;
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(750, 700);
        setResizable(false);
        getContentPane().setBackground(SteamStyle.STEAM_DARK);
        
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        mainPanel.setBackground(SteamStyle.STEAM_DARK);
        
        loginPanel = createLoginPanel();
        registerPanel = createRegisterPanel();
        
        mainPanel.add(loginPanel, "login");
        mainPanel.add(registerPanel, "register");
        
        add(mainPanel);
        setLocationRelativeTo(null);
    }
    
    private JPanel createLoginPanel() {
        JPanel panel = SteamStyle.createSteamPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        
        gbc.insets = new Insets(10, 0, 10, 0);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.CENTER;
        
        JLabel logoLabel = new JLabel("STEAM", SwingConstants.CENTER);
        SteamStyle.styleTitleLabel(logoLabel);
        logoLabel.setFont(new Font("Segoe UI", Font.BOLD, 36));
        panel.add(logoLabel, gbc);
        
        gbc.gridy = 1;
        JLabel subtitleLabel = new JLabel("Iniciar Sesión", SwingConstants.CENTER);
        SteamStyle.styleSubtitleLabel(subtitleLabel);
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        panel.add(subtitleLabel, gbc);
        
        gbc.gridy = 2;
        gbc.insets = new Insets(50, 0, 0, 0);
        panel.add(Box.createRigidArea(new Dimension(0, 0)), gbc);
        
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridBagLayout());
        formPanel.setOpaque(false);
        GridBagConstraints fgbc = new GridBagConstraints();
        fgbc.insets = new Insets(8, 0, 8, 0);
        fgbc.fill = GridBagConstraints.HORIZONTAL;
        fgbc.gridx = 0;
        fgbc.weightx = 1.0;
        
        fgbc.gridy = 0;
        JLabel userLabel = new JLabel("Nombre de Usuario");
        SteamStyle.styleLabel(userLabel);
        userLabel.setHorizontalAlignment(SwingConstants.LEFT);
        formPanel.add(userLabel, fgbc);
        
        fgbc.gridy = 1;
        fgbc.insets = new Insets(0, 0, 20, 0);
        JTextField userField = new JTextField(20);
        SteamStyle.styleTextField(userField);
        userField.setPreferredSize(new Dimension(450, 42));
        formPanel.add(userField, fgbc);
        
        fgbc.gridy = 2;
        fgbc.insets = new Insets(0, 0, 8, 0);
        JLabel passLabel = new JLabel("Contraseña");
        SteamStyle.styleLabel(passLabel);
        passLabel.setHorizontalAlignment(SwingConstants.LEFT);
        formPanel.add(passLabel, fgbc);
        
        fgbc.gridy = 3;
        fgbc.insets = new Insets(0, 0, 30, 0);
        JPasswordField passField = new JPasswordField(20);
        SteamStyle.stylePasswordField(passField);
        passField.setPreferredSize(new Dimension(450, 42));
        formPanel.add(passField, fgbc);
        
        fgbc.gridy = 4;
        fgbc.insets = new Insets(0, 0, 12, 0);
        JButton loginButton = new JButton("Iniciar Sesión");
        SteamStyle.styleButton(loginButton);
        loginButton.setPreferredSize(new Dimension(450, 48));
        formPanel.add(loginButton, fgbc);
        
        fgbc.gridy = 5;
        fgbc.insets = new Insets(0, 0, 12, 0);
        JButton createAccountButton = new JButton("Crear Nueva Cuenta");
        SteamStyle.styleSecondaryButton(createAccountButton);
        createAccountButton.setPreferredSize(new Dimension(450, 42));
        formPanel.add(createAccountButton, fgbc);
        
        fgbc.gridy = 6;
        fgbc.insets = new Insets(0, 0, 0, 0);
        JLabel messageLabel = new JLabel("", SwingConstants.CENTER);
        messageLabel.setForeground(SteamStyle.STEAM_RED);
        messageLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        messageLabel.setPreferredSize(new Dimension(450, 20));
        formPanel.add(messageLabel, fgbc);
        
        gbc.gridy = 3;
        gbc.insets = new Insets(0, 0, 0, 0);
        panel.add(formPanel, gbc);
        
        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String username = userField.getText().trim();
                String password = new String(passField.getPassword()).trim();
                
                try {
                    List<Steam.Jugador> playersList = steam.leerTodosLosJugadores();
                    boolean found = false;
                    Steam.Jugador currentUser = null;
                    
                    for (Steam.Jugador p : playersList) {
                        if (p.username.equals(username) && p.password.equals(password)) {
                            currentUser = p;
                            found = true;
                            break;
                        }
                    }
                    
                    if (found) {
                        messageLabel.setText("Bienvenido " + currentUser.nombre);
                        messageLabel.setForeground(SteamStyle.STEAM_GREEN);
                        dispose();
                        
                        if (currentUser.tipoUsuario.equalsIgnoreCase("admin")) {
                            new SteamAdmin(steam, currentUser).setVisible(true);
                        } else {
                            new SteamClient(steam, currentUser).setVisible(true);
                        }
                    } else {
                        messageLabel.setText("Credenciales incorrectas");
                        messageLabel.setForeground(SteamStyle.STEAM_RED);
                    }
                } catch (IOException ex) {
                    messageLabel.setText("Error en lectura de datos");
                    messageLabel.setForeground(SteamStyle.STEAM_RED);
                    ex.printStackTrace();
                }
            }
        });
        
        createAccountButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(mainPanel, "register");
            }
        });
        
        return panel;
    }
    
    private JPanel createRegisterPanel() {
        JPanel panel = SteamStyle.createSteamPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        
        gbc.insets = new Insets(10, 0, 10, 0);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.CENTER;
        
        JLabel titleLabel = new JLabel("CREAR CUENTA", SwingConstants.CENTER);
        SteamStyle.styleTitleLabel(titleLabel);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        panel.add(titleLabel, gbc);
        
        gbc.gridy = 1;
        gbc.insets = new Insets(30, 0, 0, 0);
        panel.add(Box.createRigidArea(new Dimension(0, 0)), gbc);
        
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridBagLayout());
        formPanel.setOpaque(false);
        GridBagConstraints fgbc = new GridBagConstraints();
        fgbc.insets = new Insets(8, 0, 8, 0);
        fgbc.fill = GridBagConstraints.HORIZONTAL;
        fgbc.gridx = 0;
        fgbc.weightx = 1.0;
        
        fgbc.gridy = 0;
        JLabel userLabel = new JLabel("Nombre de Usuario");
        SteamStyle.styleLabel(userLabel);
        userLabel.setHorizontalAlignment(SwingConstants.LEFT);
        formPanel.add(userLabel, fgbc);
        
        fgbc.gridy = 1;
        fgbc.insets = new Insets(0, 0, 15, 0);
        JTextField userField = new JTextField(20);
        SteamStyle.styleTextField(userField);
        userField.setPreferredSize(new Dimension(450, 42));
        formPanel.add(userField, fgbc);
        
        fgbc.gridy = 2;
        fgbc.insets = new Insets(0, 0, 8, 0);
        JLabel passLabel = new JLabel("Contraseña");
        SteamStyle.styleLabel(passLabel);
        passLabel.setHorizontalAlignment(SwingConstants.LEFT);
        formPanel.add(passLabel, fgbc);
        
        fgbc.gridy = 3;
        fgbc.insets = new Insets(0, 0, 15, 0);
        JPasswordField passField = new JPasswordField(20);
        SteamStyle.stylePasswordField(passField);
        passField.setPreferredSize(new Dimension(450, 42));
        formPanel.add(passField, fgbc);
        
        fgbc.gridy = 4;
        fgbc.insets = new Insets(0, 0, 8, 0);
        JLabel nameLabel = new JLabel("Nombre Completo");
        SteamStyle.styleLabel(nameLabel);
        nameLabel.setHorizontalAlignment(SwingConstants.LEFT);
        formPanel.add(nameLabel, fgbc);
        
        fgbc.gridy = 5;
        fgbc.insets = new Insets(0, 0, 15, 0);
        JTextField nameField = new JTextField(20);
        SteamStyle.styleTextField(nameField);
        nameField.setPreferredSize(new Dimension(450, 42));
        formPanel.add(nameField, fgbc);
        
        fgbc.gridy = 6;
        fgbc.insets = new Insets(0, 0, 8, 0);
        JLabel dateLabel = new JLabel("Fecha de Nacimiento (dd/MM/yyyy)");
        SteamStyle.styleLabel(dateLabel);
        dateLabel.setHorizontalAlignment(SwingConstants.LEFT);
        formPanel.add(dateLabel, fgbc);
        
        fgbc.gridy = 7;
        fgbc.insets = new Insets(0, 0, 25, 0);
        JTextField dateField = new JTextField(20);
        SteamStyle.styleTextField(dateField);
        dateField.setPreferredSize(new Dimension(450, 42));
        formPanel.add(dateField, fgbc);
        
        fgbc.gridy = 8;
        fgbc.insets = new Insets(0, 0, 12, 0);
        JButton registerButton = new JButton("Crear Cuenta");
        SteamStyle.styleButton(registerButton);
        registerButton.setPreferredSize(new Dimension(450, 48));
        formPanel.add(registerButton, fgbc);
        
        fgbc.gridy = 9;
        fgbc.insets = new Insets(0, 0, 12, 0);
        JButton backButton = new JButton("Volver al Login");
        SteamStyle.styleSecondaryButton(backButton);
        backButton.setPreferredSize(new Dimension(450, 42));
        formPanel.add(backButton, fgbc);
        
        fgbc.gridy = 10;
        fgbc.insets = new Insets(0, 0, 0, 0);
        JLabel messageLabel = new JLabel("", SwingConstants.CENTER);
        messageLabel.setForeground(SteamStyle.STEAM_GREEN);
        messageLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        messageLabel.setPreferredSize(new Dimension(450, 20));
        formPanel.add(messageLabel, fgbc);
        
        gbc.gridy = 2;
        gbc.insets = new Insets(0, 0, 0, 0);
        panel.add(formPanel, gbc);
        
        registerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String username = userField.getText().trim();
                String password = new String(passField.getPassword()).trim();
                String name = nameField.getText().trim();
                String dateStr = dateField.getText().trim();
                
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                try {
                    Date birthDate = sdf.parse(dateStr);
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(birthDate);
                    
                    steam.addPlayer(username, password, name, cal, "", "normal");
                    messageLabel.setText("Cuenta creada exitosamente");
                    messageLabel.setForeground(SteamStyle.STEAM_GREEN);
                    
                    userField.setText("");
                    passField.setText("");
                    nameField.setText("");
                    dateField.setText("");
                    
                    Timer timer = new Timer(2000, new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            cardLayout.show(mainPanel, "login");
                        }
                    });
                    timer.setRepeats(false);
                    timer.start();
                    
                } catch (ParseException pe) {
                    messageLabel.setText("Formato de fecha incorrecto");
                    messageLabel.setForeground(SteamStyle.STEAM_RED);
                } catch (IOException ex) {
                    messageLabel.setText("Error al crear cuenta");
                    messageLabel.setForeground(SteamStyle.STEAM_RED);
                    ex.printStackTrace();
                }
            }
        });
        
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(mainPanel, "login");
            }
        });
        
        return panel;
    }
}
