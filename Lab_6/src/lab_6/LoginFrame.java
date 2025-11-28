/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lab_6;

/**
 *
 * @author mavasquez
 */

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
        setSize(600, 500);
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
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        
        JLabel titleLabel = new JLabel("STEAM", SwingConstants.CENTER);
        SteamStyle.styleTitleLabel(titleLabel);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(titleLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        
        JLabel subtitleLabel = new JLabel("Iniciar Sesión", SwingConstants.CENTER);
        subtitleLabel.setForeground(SteamStyle.STEAM_TEXT_DARK);
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(subtitleLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 40)));
        
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setOpaque(false);
        formPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        formPanel.setMaximumSize(new Dimension(350, Integer.MAX_VALUE));
        
        JLabel userLabel = new JLabel("Nombre de Usuario");
        SteamStyle.styleLabel(userLabel);
        formPanel.add(userLabel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        
        JTextField userField = new JTextField(20);
        SteamStyle.styleTextField(userField);
        formPanel.add(userField);
        formPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        
        JLabel passLabel = new JLabel("Contraseña");
        SteamStyle.styleLabel(passLabel);
        formPanel.add(passLabel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        
        JPasswordField passField = new JPasswordField(20);
        SteamStyle.stylePasswordField(passField);
        formPanel.add(passField);
        formPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        
        JButton loginButton = new JButton("Iniciar Sesión");
        SteamStyle.styleButton(loginButton);
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginButton.setPreferredSize(new Dimension(350, 40));
        loginButton.setMaximumSize(new Dimension(350, 40));
        formPanel.add(loginButton);
        formPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        
        JButton createAccountButton = new JButton("Crear Nueva Cuenta");
        createAccountButton.setBackground(SteamStyle.STEAM_GRAY);
        createAccountButton.setForeground(SteamStyle.STEAM_TEXT);
        createAccountButton.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        createAccountButton.setFocusPainted(false);
        createAccountButton.setBorderPainted(false);
        createAccountButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        createAccountButton.setOpaque(true);
        createAccountButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        createAccountButton.setPreferredSize(new Dimension(350, 35));
        createAccountButton.setMaximumSize(new Dimension(350, 35));
        
        createAccountButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                createAccountButton.setBackground(SteamStyle.STEAM_GRAY.brighter());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                createAccountButton.setBackground(SteamStyle.STEAM_GRAY);
            }
        });
        
        formPanel.add(createAccountButton);
        
        JLabel messageLabel = new JLabel("", SwingConstants.CENTER);
        messageLabel.setForeground(new Color(255, 100, 100));
        messageLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        messageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        formPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        formPanel.add(messageLabel);
        
        panel.add(formPanel);
        
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
                        messageLabel.setForeground(new Color(100, 255, 100));
                        dispose();
                        
                        if (currentUser.tipoUsuario.equalsIgnoreCase("admin")) {
                            new SteamAdmin(steam, currentUser).setVisible(true);
                        } else {
                            new SteamClient(steam, currentUser).setVisible(true);
                        }
                    } else {
                        messageLabel.setText("Credenciales incorrectas");
                        messageLabel.setForeground(new Color(255, 100, 100));
                    }
                } catch (IOException ex) {
                    messageLabel.setText("Error en lectura de datos");
                    messageLabel.setForeground(new Color(255, 100, 100));
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
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        
        JLabel titleLabel = new JLabel("CREAR CUENTA", SwingConstants.CENTER);
        SteamStyle.styleTitleLabel(titleLabel);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(titleLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 30)));
        
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setOpaque(false);
        formPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        formPanel.setMaximumSize(new Dimension(350, Integer.MAX_VALUE));
        
        JLabel userLabel = new JLabel("Nombre de Usuario");
        SteamStyle.styleLabel(userLabel);
        formPanel.add(userLabel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        
        JTextField userField = new JTextField(20);
        SteamStyle.styleTextField(userField);
        formPanel.add(userField);
        formPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        
        JLabel passLabel = new JLabel("Contraseña");
        SteamStyle.styleLabel(passLabel);
        formPanel.add(passLabel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        
        JPasswordField passField = new JPasswordField(20);
        SteamStyle.stylePasswordField(passField);
        formPanel.add(passField);
        formPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        
        JLabel nameLabel = new JLabel("Nombre Completo");
        SteamStyle.styleLabel(nameLabel);
        formPanel.add(nameLabel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        
        JTextField nameField = new JTextField(20);
        SteamStyle.styleTextField(nameField);
        formPanel.add(nameField);
        formPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        
        JLabel dateLabel = new JLabel("Fecha de Nacimiento (dd/MM/yyyy)");
        SteamStyle.styleLabel(dateLabel);
        formPanel.add(dateLabel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        
        JTextField dateField = new JTextField(20);
        SteamStyle.styleTextField(dateField);
        formPanel.add(dateField);
        formPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        
        JButton registerButton = new JButton("Crear Cuenta");
        SteamStyle.styleButton(registerButton);
        registerButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        registerButton.setPreferredSize(new Dimension(350, 40));
        registerButton.setMaximumSize(new Dimension(350, 40));
        formPanel.add(registerButton);
        formPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        
        JButton backButton = new JButton("Volver al Login");
        backButton.setBackground(SteamStyle.STEAM_GRAY);
        backButton.setForeground(SteamStyle.STEAM_TEXT);
        backButton.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        backButton.setFocusPainted(false);
        backButton.setBorderPainted(false);
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backButton.setOpaque(true);
        backButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        backButton.setPreferredSize(new Dimension(350, 35));
        backButton.setMaximumSize(new Dimension(350, 35));
        
        backButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                backButton.setBackground(SteamStyle.STEAM_GRAY.brighter());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                backButton.setBackground(SteamStyle.STEAM_GRAY);
            }
        });
        
        formPanel.add(backButton);
        
        JLabel messageLabel = new JLabel("", SwingConstants.CENTER);
        messageLabel.setForeground(new Color(100, 255, 100));
        messageLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        messageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        formPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        formPanel.add(messageLabel);
        
        panel.add(formPanel);
        
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
                    messageLabel.setForeground(new Color(100, 255, 100));
                    
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
                    messageLabel.setForeground(new Color(255, 100, 100));
                } catch (IOException ex) {
                    messageLabel.setText("Error al crear cuenta");
                    messageLabel.setForeground(new Color(255, 100, 100));
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