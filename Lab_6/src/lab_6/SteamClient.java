package lab_6;

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
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setBackground(SteamStyle.STEAM_DARK);
        
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = 900;
        int height = 600;
        setSize(width, height);
        setPreferredSize(new Dimension(width, height));
        setMinimumSize(new Dimension(width, height));
        setMaximumSize(new Dimension(width, height));
        setResizable(false);
        
        int x = (screenSize.width - width) / 2;
        int y = (screenSize.height - height) / 2;
        setLocation(x, y);
        
        JPanel mainPanel = SteamStyle.createSteamPanel();
        mainPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 0, 10, 0);
        gbc.gridx = 0;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        gbc.gridy = 0;
        gbc.insets = new Insets(25, 70, 15, 70);
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        
        JLabel titleLabel = new JLabel("MI CUENTA STEAM");
        SteamStyle.styleTitleLabel(titleLabel);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 30));
        headerPanel.add(titleLabel, BorderLayout.WEST);
        
        JLabel userLabel = new JLabel(user.username);
        userLabel.setForeground(SteamStyle.STEAM_TEXT_DARK);
        userLabel.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        headerPanel.add(userLabel, BorderLayout.EAST);
        
        mainPanel.add(headerPanel, gbc);
        
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 70, 10, 70);
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new GridBagLayout());
        buttonsPanel.setOpaque(false);
        GridBagConstraints bgbc = new GridBagConstraints();
        bgbc.insets = new Insets(0, 0, 10, 0);
        bgbc.gridx = 0;
        bgbc.weightx = 1.0;
        bgbc.fill = GridBagConstraints.HORIZONTAL;
        
        bgbc.gridy = 0;
        viewCatalogButton = createStyledButton("Ver Catálogo de Juegos");
        buttonsPanel.add(viewCatalogButton, bgbc);
        
        bgbc.gridy = 1;
        downloadGameButton = createStyledButton("Descargar Juego");
        buttonsPanel.add(downloadGameButton, bgbc);
        
        bgbc.gridy = 2;
        viewLibraryButton = createStyledButton("Ver Juegos Descargados");
        buttonsPanel.add(viewLibraryButton, bgbc);
        
        bgbc.gridy = 3;
        configureProfileButton = createStyledButton("Configurar Perfil");
        buttonsPanel.add(configureProfileButton, bgbc);
        
        bgbc.gridy = 4;
        viewDownloadsCountButton = createStyledButton("Ver Contador Descargas");
        buttonsPanel.add(viewDownloadsCountButton, bgbc);
        
        bgbc.gridy = 5;
        bgbc.insets = new Insets(15, 0, 0, 0);
        logoutButton = createStyledButton("Cerrar Sesión", true);
        logoutButton.setBackground(SteamStyle.STEAM_RED);
        logoutButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                logoutButton.setBackground(new Color(240, 70, 85));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                logoutButton.setBackground(SteamStyle.STEAM_RED);
            }
        });
        buttonsPanel.add(logoutButton, bgbc);
        
        mainPanel.add(buttonsPanel, gbc);
        
        add(mainPanel);
    }
    
    private JButton createStyledButton(String text) {
        return createStyledButton(text, false);
    }
    
    private JButton createStyledButton(String text, boolean isLogout) {
        JButton button = new JButton(text);
        SteamStyle.styleButton(button);
        button.setPreferredSize(new Dimension(880, 58));
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
            panel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));
            
            JLabel titleLabel = new JLabel("Contador de Descargas");
            titleLabel.setForeground(SteamStyle.STEAM_LIGHT_BLUE);
            titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
            titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            panel.add(titleLabel);
            
            panel.add(Box.createRigidArea(new Dimension(0, 25)));
            
            JLabel countLabel = new JLabel(String.valueOf(currentUser.contadorDownloads));
            countLabel.setForeground(Color.WHITE);
            countLabel.setFont(new Font("Segoe UI", Font.BOLD, 56));
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
