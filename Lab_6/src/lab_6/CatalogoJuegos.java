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
import java.io.File;
import java.io.IOException;
import java.util.List;

public class CatalogoJuegos extends JFrame {
    private Steam steam;
    private int currentPlayerCode;
    private JPanel gamePanel;
    private final Color BACKGROUND_COLOR = SteamStyle.STEAM_DARK;
    private final Color CARD_COLOR = SteamStyle.STEAM_GRAY;
    private final Color TEXT_COLOR = SteamStyle.STEAM_TEXT;
    private final Color BUTTON_COLOR = SteamStyle.STEAM_BLUE;
    
    public CatalogoJuegos(Steam steam) {
        this(steam, -1);
    }
    
    public CatalogoJuegos(Steam steam, int playerCode) {
        this.steam = steam;
        this.currentPlayerCode = playerCode;
        setupFrame();
        loadGames();
    }
    
    private void setupFrame() {
        setTitle("Steam - Catálogo de Juegos");
        setSize(900, 700);
        setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        
        JPanel mainPanel = SteamStyle.createSteamPanel();
        mainPanel.setLayout(new BorderLayout());
        
        JPanel headerPanel = createHeaderPanel();
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        
        gamePanel = new JPanel();
        gamePanel.setLayout(new BoxLayout(gamePanel, BoxLayout.Y_AXIS));
        gamePanel.setOpaque(false);
        gamePanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JScrollPane scrollPane = new JScrollPane(gamePanel);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUI(new javax.swing.plaf.basic.BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                this.thumbColor = SteamStyle.STEAM_GRAY;
                this.trackColor = SteamStyle.STEAM_DARKER;
            }
        });
        
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        add(mainPanel);
    }
    
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 25, 15, 25));
        
        JLabel titleLabel = new JLabel("TIENDA");
        titleLabel.setForeground(SteamStyle.STEAM_LIGHT_BLUE);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        headerPanel.add(titleLabel, BorderLayout.WEST);
        
        JButton returnButton = new JButton("Regresar al Menú");
        SteamStyle.styleSecondaryButton(returnButton);
        returnButton.setPreferredSize(new Dimension(160, 38));
        returnButton.addActionListener(e -> dispose());
        headerPanel.add(returnButton, BorderLayout.EAST);
        
        return headerPanel;
    }
    
    private void loadGames() {
        gamePanel.removeAll();
        try {
            List<Steam.Juego> allGames = steam.leerTodosLosJuegos();
            
            if (allGames.isEmpty()) {
                JLabel noGamesLabel = new JLabel("No hay juegos disponibles en el catálogo", SwingConstants.CENTER);
                noGamesLabel.setForeground(TEXT_COLOR);
                noGamesLabel.setFont(new Font("Arial", Font.PLAIN, 16));
                gamePanel.add(noGamesLabel);
            } else {
                for (Steam.Juego game : allGames) {
                    addGameCard(game);
                }
            }
            
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar los juegos: " + e.getMessage());
        }
        gamePanel.revalidate();
        gamePanel.repaint();
    }
    
    private void addGameCard(Steam.Juego game) {
        JPanel cardPanel = new JPanel(new BorderLayout(10, 0));
        cardPanel.setBackground(CARD_COLOR);
        cardPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(60, 80, 100), 1),
            BorderFactory.createEmptyBorder(12, 12, 12, 12)
        ));
        cardPanel.setMaximumSize(new Dimension(850, 110));
        
     
        JLabel imageLabel = createImageLabel(game);
        cardPanel.add(imageLabel, BorderLayout.WEST);
        
        JPanel infoPanel = createInfoPanel(game);
        cardPanel.add(infoPanel, BorderLayout.CENTER);
        
        JPanel actionPanel = createActionPanel(game);
        cardPanel.add(actionPanel, BorderLayout.EAST);
        
        gamePanel.add(cardPanel);
        gamePanel.add(Box.createRigidArea(new Dimension(0, 8)));
    }
    
    private JLabel createImageLabel(Steam.Juego game) {
        ImageIcon icon = loadGameImage(game);
        JLabel imageLabel;
        
        if (icon != null) {
            imageLabel = new JLabel(icon);
        } else {
            imageLabel = new JLabel("Sin imagen");
            imageLabel.setOpaque(true);
            imageLabel.setBackground(new Color(35, 60, 81));
            imageLabel.setPreferredSize(new Dimension(184, 69));
            imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
            imageLabel.setForeground(TEXT_COLOR);
        }
        
        return imageLabel;
    }
    
    private JPanel createInfoPanel(Steam.Juego game) {
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(CARD_COLOR);
        infoPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        
        
        JLabel nameLabel = new JLabel(game.titulo);
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 17));
        nameLabel.setForeground(TEXT_COLOR);
        nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        infoPanel.add(nameLabel);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        
        String osName = getOSName(game.sistemaOperativo);
        JLabel osLabel = new JLabel("Sistema: " + osName + " | Edad: " + game.edadMinima + "+");
        osLabel.setForeground(SteamStyle.STEAM_TEXT_DARK);
        osLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        osLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        infoPanel.add(osLabel);
        
        JLabel idLabel = new JLabel("ID: " + game.code);
        idLabel.setForeground(new Color(100, 120, 140));
        idLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        idLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        infoPanel.add(idLabel);
        
        return infoPanel;
    }
    
    private String getOSName(char os) {
        switch(os) {
            case 'W': return "Windows";
            case 'M': return "Mac";
            case 'L': return "Linux";
            default: return "Desconocido";
        }
    }
    
    private JPanel createActionPanel(Steam.Juego game) {
        JPanel actionPanel = new JPanel();
        actionPanel.setLayout(new BoxLayout(actionPanel, BoxLayout.Y_AXIS));
        actionPanel.setBackground(CARD_COLOR);
        actionPanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 5));
        
        JLabel priceLabel = new JLabel("$" + String.format("%.2f", game.precio));
        priceLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        priceLabel.setForeground(Color.WHITE);
        priceLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);
        actionPanel.add(priceLabel);
        actionPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        
        JButton downloadButton = new JButton("Descargar");
        SteamStyle.styleButton(downloadButton);
        downloadButton.setPreferredSize(new Dimension(120, 35));
        downloadButton.setAlignmentX(Component.RIGHT_ALIGNMENT);
        downloadButton.addActionListener(e -> downloadGame(game.code));
        actionPanel.add(downloadButton);
        
        return actionPanel;
    }
    
    private ImageIcon loadGameImage(Steam.Juego game) {
        try {
            if (game.rutaImagen == null || game.rutaImagen.trim().isEmpty()) {
                return null;
            }
            
            String imagePath = game.rutaImagen;
            java.net.URL imgURL = null;
            
            if (imagePath.startsWith("/images/") || imagePath.startsWith("/Images/")) {
                imgURL = getClass().getResource(imagePath);
                if (imgURL == null) {
                    String correctedPath = imagePath.replace("/images/", "/Images/").replace("/Images/", "/Images/");
                    imgURL = getClass().getResource(correctedPath);
                }
            }
            
            if (imgURL == null) {
                String fileName = imagePath.substring(imagePath.lastIndexOf("/") + 1);
                File imgFile = new File("src/Images/" + fileName);
                if (imgFile.exists()) {
                    imgURL = imgFile.toURI().toURL();
                }
            }
            
            if (imgURL != null) {
                ImageIcon icon = new ImageIcon(imgURL);
                Image img = icon.getImage();
                if (img != null && img.getWidth(null) > 0) {
                    Image scaledImg = img.getScaledInstance(184, 69, Image.SCALE_SMOOTH);
                    return new ImageIcon(scaledImg);
                }
            }
        } catch (Exception e) {
            System.out.println("Error cargando imagen: " + game.rutaImagen);
        }
        return null;
    }
    
    private void downloadGame(int gameId) {
        if (currentPlayerCode == -1) {
            JTextField playerIdField = new JTextField();
            SteamStyle.styleTextField(playerIdField);
            
            Object[] inputs = {
                "Código de jugador:", playerIdField
            };
            
            int result = JOptionPane.showConfirmDialog(this, inputs, "Descargar Juego", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                try {
                    currentPlayerCode = Integer.parseInt(playerIdField.getText().trim());
                    showOSDialog(gameId);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "El código de jugador debe ser un número.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            showOSDialog(gameId);
        }
    }
    
    private void showOSDialog(int gameId) {
        String[] systems = {"Windows (W)", "Mac (M)", "Linux (L)"};
        String selectedOS = (String) JOptionPane.showInputDialog(this,
            "Seleccione su sistema operativo:",
            "Sistema Operativo",
            JOptionPane.QUESTION_MESSAGE,
            null,
            systems,
            systems[0]);
        
        if (selectedOS != null) {
            char os = selectedOS.charAt(selectedOS.indexOf("(") + 1);
            processDownload(gameId, currentPlayerCode, os);
        }
    }
    
    private void processDownload(int gameId, int playerId, char os) {
        try {
            boolean success = steam.downloadGame(gameId, playerId, os);
            if (success) {
                JOptionPane.showMessageDialog(this, "¡Juego descargado exitosamente!", "Descarga Exitosa", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo descargar el juego.\nVerifique:\n- Su edad cumple con el requisito\n- El sistema operativo coincide", "Error de Descarga", JOptionPane.ERROR_MESSAGE);
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error durante la descarga: " + ex.getMessage(), "Error de Sistema", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void showMessage(String message, String title) {
        JOptionPane.showMessageDialog(this, message, title, 
            title.contains("Error") ? JOptionPane.ERROR_MESSAGE : JOptionPane.INFORMATION_MESSAGE);
    }
}