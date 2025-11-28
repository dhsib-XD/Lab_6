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
import java.util.Calendar;
import java.util.List;

public class SteamClient extends JFrame implements ActionListener {
    private JButton viewCatalogButton;
    private JButton downloadGameButton;
    private JButton viewLibraryButton;
    private JButton configureProfileButton;
    private JButton viewDownloadsCountButton;
    private JButton logoutButton;
    
    private Steam steam;
    private Steam.Jugador currentUser;
    
    public SteamClient(Steam steam, Steam.Jugador user) {
        super("Steam - Panel de Usuario");
        this.steam = steam;
        this.currentUser = user;
        
        setSize(500, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(SteamStyle.STEAM_DARK);
        
        JPanel mainPanel = SteamStyle.createSteamPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 30, 20));
        
        JLabel titleLabel = new JLabel("MI CUENTA STEAM");
        SteamStyle.styleTitleLabel(titleLabel);
        headerPanel.add(titleLabel, BorderLayout.WEST);
        
        JLabel userLabel = new JLabel("Usuario: " + user.username);
        userLabel.setForeground(SteamStyle.STEAM_TEXT_DARK);
        userLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        headerPanel.add(userLabel, BorderLayout.EAST);
        
        mainPanel.add(headerPanel);
        
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new GridLayout(0, 1, 0, 15));
        buttonsPanel.setOpaque(false);
        buttonsPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 20, 20));
        
        viewCatalogButton = createStyledButton("Ver Catálogo de Juegos");
        buttonsPanel.add(viewCatalogButton);
        
        downloadGameButton = createStyledButton("Descargar Juego");
        buttonsPanel.add(downloadGameButton);
        
        viewLibraryButton = createStyledButton("Ver Juegos Descargados");
        buttonsPanel.add(viewLibraryButton);
        
        configureProfileButton = createStyledButton("Configurar Perfil");
        buttonsPanel.add(configureProfileButton);
        
        viewDownloadsCountButton = createStyledButton("Ver Contador Descargas");
        buttonsPanel.add(viewDownloadsCountButton);
        
        logoutButton = createStyledButton("Cerrar Sesión", true);
        logoutButton.setBackground(new Color(180, 50, 50));
        logoutButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                logoutButton.setBackground(new Color(200, 70, 70));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                logoutButton.setBackground(new Color(180, 50, 50));
            }
        });
        buttonsPanel.add(logoutButton);
        
        mainPanel.add(buttonsPanel);
        add(mainPanel);
    }
    
    private JButton createStyledButton(String text) {
        return createStyledButton(text, false);
    }
    
    private JButton createStyledButton(String text, boolean isLogout) {
        JButton button = new JButton(text);
        SteamStyle.styleButton(button);
        button.setPreferredSize(new Dimension(450, 55));
        button.setMaximumSize(new Dimension(450, 55));
        button.setFont(new Font("Segoe UI", Font.BOLD, 15));
        button.addActionListener(this);
        return button;
    }
    
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == viewCatalogButton) {
            try {
                CatalogoJuegos catalogo = new CatalogoJuegos(steam, currentUser.code);
                catalogo.setVisible(true);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error al abrir el catálogo: " + ex.getMessage(),
                                             "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else if (e.getSource() == downloadGameButton) {
            downloadGame();
        } else if (e.getSource() == viewLibraryButton) {
            try {
                Biblioteca biblioteca = new Biblioteca(steam, currentUser.code);
                biblioteca.setVisible(true);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error al abrir la biblioteca: " + ex.getMessage(),
                                             "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else if (e.getSource() == configureProfileButton) {
            configureProfile();
        } else if (e.getSource() == viewDownloadsCountButton) {
            viewDownloadsCount();
        } else if (e.getSource() == logoutButton) {
            logout();
        }
    }
    
    private void downloadGame() {
        try {
            List<Steam.Juego> games = steam.leerTodosLosJuegos();
            if (games.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No hay juegos disponibles.", "Info", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            
            String[] gameOptions = new String[games.size()];
            for (int i = 0; i < games.size(); i++) {
                gameOptions[i] = games.get(i).code + " - " + games.get(i).titulo;
            }
            
            String selectedGame = (String) JOptionPane.showInputDialog(this, "Seleccione un juego:", 
                "Descargar Juego", JOptionPane.QUESTION_MESSAGE, null, gameOptions, gameOptions[0]);
            
            if (selectedGame == null) return;
            
            int gameCode = Integer.parseInt(selectedGame.split(" - ")[0]);
            
            String[] systems = {"Windows (W)", "Mac (M)", "Linux (L)"};
            String selectedOS = (String) JOptionPane.showInputDialog(this, "Seleccione su sistema operativo:", 
                "Sistema Operativo", JOptionPane.QUESTION_MESSAGE, null, systems, systems[0]);
            
            if (selectedOS == null) return;
            
            char os = selectedOS.charAt(selectedOS.indexOf("(") + 1);
            
            boolean success = steam.downloadGame(gameCode, currentUser.code, os);
            if (success) {
                JOptionPane.showMessageDialog(this, "¡Juego descargado exitosamente!", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                currentUser = refreshCurrentUser();
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo descargar el juego. Verifique los requisitos.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error durante la descarga: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void configureProfile() {
        JTextField usernameField = new JTextField(currentUser.username);
        JTextField passwordField = new JTextField(currentUser.password);
        JTextField nameField = new JTextField(currentUser.nombre);
        JTextField imageField = new JTextField(currentUser.rutaImagen);
        
        SteamStyle.styleTextField(usernameField);
        SteamStyle.styleTextField(passwordField);
        SteamStyle.styleTextField(nameField);
        SteamStyle.styleTextField(imageField);
        
        Object[] inputs = {
            "Username:", usernameField,
            "Password:", passwordField,
            "Nombre:", nameField,
            "Ruta Imagen:", imageField
        };
        
        int result = JOptionPane.showConfirmDialog(this, inputs, "Configurar Perfil", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                String username = usernameField.getText().trim();
                String password = passwordField.getText().trim();
                String name = nameField.getText().trim();
                String imagePath = imageField.getText().trim();
                
                Calendar cal = Calendar.getInstance();
                cal.setTimeInMillis(currentUser.nacimiento);
                
                boolean updated = steam.updatePlayer(currentUser.code, username, password, name, cal, imagePath, currentUser.tipoUsuario);
                if (updated) {
                    JOptionPane.showMessageDialog(this, "Perfil actualizado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    currentUser = refreshCurrentUser();
                } else {
                    JOptionPane.showMessageDialog(this, "Error al actualizar perfil.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error al configurar perfil: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void viewDownloadsCount() {
        try {
            currentUser = refreshCurrentUser();
            JPanel panel = new JPanel();
            panel.setBackground(SteamStyle.STEAM_DARK);
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
            panel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
            
            JLabel titleLabel = new JLabel("Contador de Descargas");
            titleLabel.setForeground(SteamStyle.STEAM_LIGHT_BLUE);
            titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
            titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            panel.add(titleLabel);
            
            panel.add(Box.createRigidArea(new Dimension(0, 20)));
            
            JLabel countLabel = new JLabel(String.valueOf(currentUser.contadorDownloads));
            countLabel.setForeground(Color.WHITE);
            countLabel.setFont(new Font("Segoe UI", Font.BOLD, 48));
            countLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            panel.add(countLabel);
            
            JOptionPane.showMessageDialog(this, panel, "Contador de Descargas", JOptionPane.PLAIN_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al obtener contador: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private Steam.Jugador refreshCurrentUser() throws IOException {
        List<Steam.Jugador> players = steam.leerTodosLosJugadores();
        for (Steam.Jugador p : players) {
            if (p.code == currentUser.code) {
                return p;
            }
        }
        return currentUser;
    }
    
    private void logout() {
        this.dispose();
        new LoginFrame(steam).setVisible(true);
    }
}