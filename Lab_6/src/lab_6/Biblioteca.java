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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Biblioteca extends JFrame {
    private Steam steam;
    private JPanel gamePanel;
    private int playerId;
    
    private final Color BACKGROUND_COLOR = SteamStyle.STEAM_DARK;
    private final Color CARD_COLOR = SteamStyle.STEAM_GRAY;
    private final Color TEXT_COLOR = SteamStyle.STEAM_TEXT;
    private final Color BUTTON_COLOR = SteamStyle.STEAM_BLUE;
    
    public Biblioteca(Steam steam, int playerId) {
        this.steam = steam;
        this.playerId = playerId;
        setupFrame();
        loadPlayerGames();
    }
    
    private void setupFrame() {
        setTitle("Steam - Biblioteca de Juegos");
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
        
        JLabel titleLabel = new JLabel("MI BIBLIOTECA");
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
    
    private void loadPlayerGames() {
        gamePanel.removeAll();
        try {
            List<Steam.Juego> allGames = steam.leerTodosLosJuegos();
            List<Steam.Juego> playerGames = new ArrayList<>();
            List<Steam.Jugador> allPlayers = steam.leerTodosLosJugadores();
            Steam.Jugador currentPlayer = null;
            for (Steam.Jugador p : allPlayers) {
                if (p.code == this.playerId) {
                    currentPlayer = p;
                    break;
                }
            }
            
            if (currentPlayer != null) {
                List<String> allDownloads = steam.leerTodasLasDescargas();
                for (String downloadContent : allDownloads) {
                    String[] lines = downloadContent.split("\n");
                    if (lines.length >= 4) {
                        String downloadLine = lines[3];
                        String detailsLine = lines[4];
                        if (detailsLine.contains(currentPlayer.nombre + " ha bajado")) {
                            String gameTitle = detailsLine.substring(detailsLine.indexOf("ha bajado") + 10);
                            if (gameTitle.contains(" a un precio")) {
                                gameTitle = gameTitle.substring(0, gameTitle.indexOf(" a un precio"));
                            }
                            for (Steam.Juego game : allGames) {
                                if (game.titulo.equals(gameTitle.trim())) {
                                    boolean alreadyAdded = false;
                                    for (Steam.Juego pg : playerGames) {
                                        if (pg.code == game.code) {
                                            alreadyAdded = true;
                                            break;
                                        }
                                    }
                                    if (!alreadyAdded) {
                                        playerGames.add(game);
                                    }
                                    break;
                                }
                            }
                        }
                    }
                }
            }
            
            if (playerGames.isEmpty()) {
                showEmptyLibraryMessage();
            } else {
                for (Steam.Juego game : playerGames) {
                    addGameCard(game);
                }
            }
            
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar la biblioteca: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error inesperado: " + e.getMessage());
            e.printStackTrace();
        }
        gamePanel.revalidate();
        gamePanel.repaint();
    }
    
    private void showEmptyLibraryMessage() {
        JPanel messagePanel = new JPanel(new BorderLayout());
        messagePanel.setBackground(BACKGROUND_COLOR);
        messagePanel.setBorder(BorderFactory.createEmptyBorder(50, 0, 0, 0));
        
        JLabel messageLabel = new JLabel("No tienes juegos en tu biblioteca");
        messageLabel.setForeground(TEXT_COLOR);
        messageLabel.setFont(new Font("Arial", Font.BOLD, 18));
        messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        JButton storeButton = new JButton("Ir a la Tienda");
        storeButton.setBackground(BUTTON_COLOR);
        storeButton.setForeground(Color.WHITE);
        storeButton.setFocusPainted(false);
        storeButton.addActionListener(e -> {
            dispose();
            new CatalogoJuegos(steam).setVisible(true);
        });
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(BACKGROUND_COLOR);
        buttonPanel.add(storeButton);
        
        messagePanel.add(messageLabel, BorderLayout.NORTH);
        messagePanel.add(buttonPanel, BorderLayout.CENTER);
        gamePanel.add(messagePanel);
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
        JLabel osLabel = new JLabel("Sistema: " + osName);
        osLabel.setForeground(SteamStyle.STEAM_TEXT_DARK);
        osLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        osLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        infoPanel.add(osLabel);
        
        return infoPanel;
    }
    
    private String getOSName(char so) {
        switch(so) {
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
            System.out.println("Error al cargar imagen para " + game.titulo + ": " + e.getMessage());
        }
        return null;
    }
    
    private void playGame(Steam.Juego game) {
        JDialog gameDialog = new JDialog(this, game.titulo + " - Jugando", true);
        gameDialog.setSize(500, 300);
        gameDialog.setLocationRelativeTo(this);
        gameDialog.setLayout(new BorderLayout());
        gameDialog.getContentPane().setBackground(BACKGROUND_COLOR);
        
        JLabel playingLabel = new JLabel("Jugando a " + game.titulo);
        playingLabel.setHorizontalAlignment(SwingConstants.CENTER);
        playingLabel.setForeground(TEXT_COLOR);
        playingLabel.setFont(new Font("Arial", Font.BOLD, 18));
        playingLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        
        JButton closeButton = new JButton("Cerrar Juego");
        closeButton.setBackground(BUTTON_COLOR);
        closeButton.setForeground(Color.WHITE);
        closeButton.addActionListener(e -> gameDialog.dispose());
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(BACKGROUND_COLOR);
        buttonPanel.add(closeButton);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        
        gameDialog.add(playingLabel, BorderLayout.CENTER);
        gameDialog.add(buttonPanel, BorderLayout.SOUTH);
        gameDialog.setVisible(true);
    }
}