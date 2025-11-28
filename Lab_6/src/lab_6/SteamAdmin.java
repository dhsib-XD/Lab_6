import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import lab_6.LoginFrame;
import lab_6.SteamStyle;

public class SteamAdmin extends JFrame implements ActionListener {
    private Steam steam;
    private Steam.Jugador currentUser;
    
    private JButton addPlayerButton;
    private JButton modifyPlayerButton;
    private JButton deletePlayerButton;
    private JButton addGameButton;
    private JButton modifyGameButton;
    private JButton deleteGameButton;
    private JButton updatePriceButton;
    private JButton viewGamesButton;
    private JButton viewReportsButton;
    private JButton generateReportButton;
    private JButton viewDownloadsButton;
    private JButton logoutButton;
    
    private final String[] availableImages = {
        "/Images/1.png",
        "/Images/2.png",
        "/Images/3.png",
        "/Images/4.png",
        "/Images/5.png",
        "/Images/6.png",
        "/Images/7.png",
        "/Images/8.png"
    };
    
    public SteamAdmin(Steam steam, Steam.Jugador admin) {
        super("Steam - Panel de Administrador");
        this.steam = steam;
        this.currentUser = admin;
        
        setSize(550, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(SteamStyle.STEAM_DARK);
        
        JPanel mainPanel = SteamStyle.createSteamPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 30, 20));
        
        JLabel titleLabel = new JLabel("PANEL DE ADMINISTRADOR");
        SteamStyle.styleTitleLabel(titleLabel);
        headerPanel.add(titleLabel, BorderLayout.WEST);
        
        JLabel userLabel = new JLabel("Usuario: " + admin.username);
        userLabel.setForeground(SteamStyle.STEAM_TEXT_DARK);
        userLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        headerPanel.add(userLabel, BorderLayout.EAST);
        
        mainPanel.add(headerPanel);
        
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new GridLayout(0, 2, 15, 15));
        buttonsPanel.setOpaque(false);
        buttonsPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 20, 20));
        
        addPlayerButton = createStyledButton("Registrar Player");
        buttonsPanel.add(addPlayerButton);
        
        modifyPlayerButton = createStyledButton("Modificar Player");
        buttonsPanel.add(modifyPlayerButton);
        
        deletePlayerButton = createStyledButton("Eliminar Player");
        buttonsPanel.add(deletePlayerButton);
        
        addGameButton = createStyledButton("Registrar Juego");
        buttonsPanel.add(addGameButton);
        
        modifyGameButton = createStyledButton("Modificar Juego");
        buttonsPanel.add(modifyGameButton);
        
        deleteGameButton = createStyledButton("Eliminar Juego");
        buttonsPanel.add(deleteGameButton);
        
        updatePriceButton = createStyledButton("Cambiar Precio");
        buttonsPanel.add(updatePriceButton);
        
        viewGamesButton = createStyledButton("Ver Lista Juegos");
        buttonsPanel.add(viewGamesButton);
        
        viewReportsButton = createStyledButton("Ver Reportes");
        buttonsPanel.add(viewReportsButton);
        
        generateReportButton = createStyledButton("Generar Reporte");
        buttonsPanel.add(generateReportButton);
        
        viewDownloadsButton = createStyledButton("Ver Descargas");
        buttonsPanel.add(viewDownloadsButton);
        
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
        button.setPreferredSize(new Dimension(250, 50));
        button.setMaximumSize(new Dimension(250, 50));
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.addActionListener(this);
        return button;
    }
    
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addPlayerButton) {
            addPlayer();
        } else if (e.getSource() == modifyPlayerButton) {
            modifyPlayer();
        } else if (e.getSource() == deletePlayerButton) {
            deletePlayer();
        } else if (e.getSource() == addGameButton) {
            addGame();
        } else if (e.getSource() == modifyGameButton) {
            modifyGame();
        } else if (e.getSource() == deleteGameButton) {
            deleteGame();
        } else if (e.getSource() == updatePriceButton) {
            updatePrice();
        } else if (e.getSource() == viewGamesButton) {
            viewGames();
        } else if (e.getSource() == viewReportsButton) {
            viewReports();
        } else if (e.getSource() == generateReportButton) {
            generateClientReport();
        } else if (e.getSource() == viewDownloadsButton) {
            viewDownloads();
        } else if (e.getSource() == logoutButton) {
            logout();
        }
    }
    
    private void addPlayer() {
        JTextField usernameField = new JTextField();
        JTextField passwordField = new JTextField();
        JTextField nameField = new JTextField();
        JTextField dateField = new JTextField();
        JTextField imageField = new JTextField();
        JComboBox<String> tipoCombo = new JComboBox<>(new String[]{"normal", "admin"});
        
        styleDialogFields(usernameField, passwordField, nameField, dateField, imageField);
        
        Object[] inputs = {
            "Username:", usernameField,
            "Password:", passwordField,
            "Nombre:", nameField,
            "Fecha Nacimiento (dd/MM/yyyy):", dateField,
            "Ruta Imagen:", imageField,
            "Tipo Usuario:", tipoCombo
        };
        
        int result = JOptionPane.showConfirmDialog(this, inputs, "Registrar Player", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                String username = usernameField.getText().trim();
                String password = passwordField.getText().trim();
                String name = nameField.getText().trim();
                String dateStr = dateField.getText().trim();
                String imagePath = imageField.getText().trim();
                String tipoUsuario = (String) tipoCombo.getSelectedItem();
                
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                Date birthDate = sdf.parse(dateStr);
                Calendar cal = Calendar.getInstance();
                cal.setTime(birthDate);
                
                steam.addPlayer(username, password, name, cal, imagePath, tipoUsuario);
                JOptionPane.showMessageDialog(this, "Player registrado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } catch (ParseException pe) {
                JOptionPane.showMessageDialog(this, "Error: Formato de fecha incorrecto.", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error al registrar player: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void modifyPlayer() {
        JTextField codeField = new JTextField();
        JTextField usernameField = new JTextField();
        JTextField passwordField = new JTextField();
        JTextField nameField = new JTextField();
        JTextField dateField = new JTextField();
        JTextField imageField = new JTextField();
        JComboBox<String> tipoCombo = new JComboBox<>(new String[]{"normal", "admin"});
        
        styleDialogFields(codeField, usernameField, passwordField, nameField, dateField, imageField);
        
        Object[] inputs = {
            "Código Player:", codeField,
            "Username:", usernameField,
            "Password:", passwordField,
            "Nombre:", nameField,
            "Fecha Nacimiento (dd/MM/yyyy):", dateField,
            "Ruta Imagen:", imageField,
            "Tipo Usuario:", tipoCombo
        };
        
        int result = JOptionPane.showConfirmDialog(this, inputs, "Modificar Player", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                int code = Integer.parseInt(codeField.getText().trim());
                String username = usernameField.getText().trim();
                String password = passwordField.getText().trim();
                String name = nameField.getText().trim();
                String dateStr = dateField.getText().trim();
                String imagePath = imageField.getText().trim();
                String tipoUsuario = (String) tipoCombo.getSelectedItem();
                
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                Date birthDate = sdf.parse(dateStr);
                Calendar cal = Calendar.getInstance();
                cal.setTime(birthDate);
                
                boolean updated = steam.updatePlayer(code, username, password, name, cal, imagePath, tipoUsuario);
                if (updated) {
                    JOptionPane.showMessageDialog(this, "Player modificado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "No se encontró el player con ese código.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (ParseException pe) {
                JOptionPane.showMessageDialog(this, "Error: Formato de fecha incorrecto.", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error al modificar player: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void deletePlayer() {
        JTextField codeField = new JTextField();
        SteamStyle.styleTextField(codeField);
        
        Object[] inputs = {
            "Código del Player a Eliminar:", codeField
        };
        
        int result = JOptionPane.showConfirmDialog(this, inputs, "Eliminar Player", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                int playerCode = Integer.parseInt(codeField.getText().trim());
                boolean deleted = steam.deletePlayer(playerCode);
                if (deleted) {
                    JOptionPane.showMessageDialog(this, "Player eliminado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "No se pudo eliminar el player. Verifique que el código existe.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error al eliminar player: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void addGame() {
        JTextField titleField = new JTextField();
        JTextField osField = new JTextField();
        JTextField ageField = new JTextField();
        JTextField priceField = new JTextField();
        
        styleDialogFields(titleField, osField, ageField, priceField);
        
        JComboBox<String> imageComboBox = new JComboBox<>();
        imageComboBox.addItem("Seleccione una imagen");
        for (int i = 0; i < availableImages.length; i++) {
            imageComboBox.addItem("Imagen " + (i + 1));
        }
        
        JPanel previewPanel = new JPanel();
        previewPanel.setPreferredSize(new Dimension(150, 80));
        previewPanel.setBackground(SteamStyle.STEAM_GRAY);
        previewPanel.setBorder(BorderFactory.createTitledBorder("Vista Previa"));
        JLabel previewLabel = new JLabel("Seleccione una imagen", JLabel.CENTER);
        previewLabel.setForeground(SteamStyle.STEAM_TEXT);
        previewPanel.add(previewLabel);
        
        imageComboBox.addActionListener(e -> {
            int selectedIndex = imageComboBox.getSelectedIndex();
            if (selectedIndex > 0) {
                try {
                    ImageIcon icon = new ImageIcon(getClass().getResource(availableImages[selectedIndex - 1]));
                    Image img = icon.getImage().getScaledInstance(120, 60, Image.SCALE_SMOOTH);
                    previewLabel.setIcon(new ImageIcon(img));
                    previewLabel.setText("");
                } catch (Exception ex) {
                    previewLabel.setIcon(null);
                    previewLabel.setText("No se pudo cargar la imagen");
                }
            } else {
                previewLabel.setIcon(null);
                previewLabel.setText("Seleccione una imagen");
            }
        });
        
        JPanel imagePanel = new JPanel(new BorderLayout());
        imagePanel.setOpaque(false);
        imagePanel.add(imageComboBox, BorderLayout.NORTH);
        imagePanel.add(previewPanel, BorderLayout.CENTER);
        
        Object[] inputs = {
            "Título:", titleField,
            "Sistema Operativo (W/M/L):", osField,
            "Edad Mínima:", ageField,
            "Precio:", priceField,
            "Imagen:", imagePanel
        };
        
        int result = JOptionPane.showConfirmDialog(this, inputs, "Agregar Juego", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                String title = titleField.getText().trim();
                char os = osField.getText().trim().toUpperCase().charAt(0);
                int minAge = Integer.parseInt(ageField.getText().trim());
                double price = Double.parseDouble(priceField.getText().trim());
                
                int selectedImageIndex = imageComboBox.getSelectedIndex();
                String imagePath = (selectedImageIndex > 0) ? availableImages[selectedImageIndex - 1] : availableImages[0];
                
                steam.addGame(title, os, minAge, price, imagePath);
                JOptionPane.showMessageDialog(this, "Juego agregado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error al agregar juego: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void modifyGame() {
        JTextField codeField = new JTextField();
        JTextField titleField = new JTextField();
        JTextField osField = new JTextField();
        JTextField ageField = new JTextField();
        JTextField priceField = new JTextField();
        JTextField imageField = new JTextField();
        
        styleDialogFields(codeField, titleField, osField, ageField, priceField, imageField);
        
        Object[] inputs = {
            "Código Juego:", codeField,
            "Título:", titleField,
            "Sistema Operativo (W/M/L):", osField,
            "Edad Mínima:", ageField,
            "Precio:", priceField,
            "Ruta Imagen:", imageField
        };
        
        int result = JOptionPane.showConfirmDialog(this, inputs, "Modificar Juego", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                int code = Integer.parseInt(codeField.getText().trim());
                String title = titleField.getText().trim();
                char os = osField.getText().trim().toUpperCase().charAt(0);
                int minAge = Integer.parseInt(ageField.getText().trim());
                double price = Double.parseDouble(priceField.getText().trim());
                String imagePath = imageField.getText().trim();
                
                boolean updated = steam.updateGame(code, title, os, minAge, price, imagePath);
                if (updated) {
                    JOptionPane.showMessageDialog(this, "Juego modificado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "No se encontró el juego con ese código.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error al modificar juego: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void deleteGame() {
        JTextField codeField = new JTextField();
        SteamStyle.styleTextField(codeField);
        
        Object[] inputs = {
            "Código del Juego a Eliminar:", codeField
        };
        
        int result = JOptionPane.showConfirmDialog(this, inputs, "Eliminar Juego", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                int gameCode = Integer.parseInt(codeField.getText().trim());
                boolean deleted = steam.deleteGame(gameCode);
                if (deleted) {
                    JOptionPane.showMessageDialog(this, "Juego eliminado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "No se pudo eliminar el juego. Verifique que el código existe.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error al eliminar juego: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void updatePrice() {
        JTextField codeField = new JTextField();
        JTextField priceField = new JTextField();
        
        styleDialogFields(codeField, priceField);
        
        Object[] inputs = {
            "Código del Juego:", codeField,
            "Nuevo Precio:", priceField
        };
        
        int result = JOptionPane.showConfirmDialog(this, inputs, "Actualizar Precio", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                int code = Integer.parseInt(codeField.getText().trim());
                double newPrice = Double.parseDouble(priceField.getText().trim());
                steam.updatePriceFor(code, newPrice);
                JOptionPane.showMessageDialog(this, "Precio actualizado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error al actualizar precio: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void viewGames() {
        try {
            steam.printGames();
            List<Steam.Juego> gamesList = steam.leerTodosLosJuegos();
            JTextArea textArea = new JTextArea(20, 50);
            textArea.setEditable(false);
            textArea.setBackground(SteamStyle.STEAM_GRAY);
            textArea.setForeground(SteamStyle.STEAM_TEXT);
            textArea.setFont(new Font("Consolas", Font.PLAIN, 12));
            
            textArea.append("=== LISTA DE JUEGOS ===\n\n");
            for (Steam.Juego game : gamesList) {
                textArea.append(game.toString() + "\n\n");
            }
            
            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setBorder(BorderFactory.createLineBorder(SteamStyle.STEAM_BLUE, 2));
            JOptionPane.showMessageDialog(this, scrollPane, "Lista de Juegos", JOptionPane.PLAIN_MESSAGE);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error al leer juegos: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void viewReports() {
        try {
            java.io.File currentDir = new java.io.File(".");
            java.io.File[] files = currentDir.listFiles((dir, name) -> 
                name.toLowerCase().endsWith(".txt") && 
                (name.toLowerCase().contains("reporte") || name.toLowerCase().contains("report"))
            );
            
            if (files == null || files.length == 0) {
                JOptionPane.showMessageDialog(this, 
                    "No se encontraron reportes generados.\n" +
                    "Use 'Generar Reporte' para crear reportes de clientes.", 
                    "Sin Reportes", 
                    JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            
            String[] fileNames = new String[files.length];
            for (int i = 0; i < files.length; i++) {
                fileNames[i] = files[i].getName();
            }
            
            String selectedFile = (String) JOptionPane.showInputDialog(this,
                "Seleccione un reporte para ver:",
                "Ver Reportes",
                JOptionPane.QUESTION_MESSAGE,
                null,
                fileNames,
                fileNames[0]);
            
            if (selectedFile != null) {
                java.io.File file = new java.io.File(selectedFile);
                if (file.exists()) {
                    java.io.BufferedReader br = new java.io.BufferedReader(new java.io.FileReader(file));
                    JTextArea textArea = new JTextArea(20, 60);
                    textArea.setEditable(false);
                    textArea.setBackground(SteamStyle.STEAM_GRAY);
                    textArea.setForeground(SteamStyle.STEAM_TEXT);
                    textArea.setFont(new Font("Consolas", Font.PLAIN, 12));
                    String line;
                    while ((line = br.readLine()) != null) {
                        textArea.append(line + "\n");
                    }
                    br.close();
                    JScrollPane scrollPane = new JScrollPane(textArea);
                    scrollPane.setBorder(BorderFactory.createLineBorder(SteamStyle.STEAM_BLUE, 2));
                    JOptionPane.showMessageDialog(this, scrollPane, "Reporte: " + selectedFile, JOptionPane.PLAIN_MESSAGE);
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al leer reportes: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void generateClientReport() {
        JTextField codeField = new JTextField();
        JTextField fileNameField = new JTextField();
        
        styleDialogFields(codeField, fileNameField);
        
        Object[] inputs = {
            "Código del Cliente:", codeField,
            "Nombre Archivo (con ruta):", fileNameField
        };
        
        int result = JOptionPane.showConfirmDialog(this, inputs, "Reporte Cliente", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                int clientCode = Integer.parseInt(codeField.getText().trim());
                String fileName = fileNameField.getText().trim();
                String message = steam.reportForClient(clientCode, fileName);
                JOptionPane.showMessageDialog(this, message, "Reporte Cliente", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error al generar reporte: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void viewDownloads() {
        try {
            List<String> downloads = steam.leerTodasLasDescargas();
            JTextArea textArea = new JTextArea(20, 60);
            textArea.setEditable(false);
            textArea.setBackground(SteamStyle.STEAM_GRAY);
            textArea.setForeground(SteamStyle.STEAM_TEXT);
            textArea.setFont(new Font("Consolas", Font.PLAIN, 12));
            
            textArea.append("=== DESCARGAS GENERADAS ===\n\n");
            for (String download : downloads) {
                textArea.append(download + "\n");
                textArea.append("----------------------------------------\n\n");
            }
            
            if (downloads.isEmpty()) {
                textArea.append("No hay descargas registradas.");
            }
            
            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setBorder(BorderFactory.createLineBorder(SteamStyle.STEAM_BLUE, 2));
            JOptionPane.showMessageDialog(this, scrollPane, "Descargas Generadas", JOptionPane.PLAIN_MESSAGE);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error al leer descargas: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void styleDialogFields(JTextField... fields) {
        for (JTextField field : fields) {
            SteamStyle.styleTextField(field);
        }
    }
    
    private void logout() {
        this.dispose();
        new LoginFrame(steam).setVisible(true);
    }
}