package lab_6;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import java.awt.Color;
import java.awt.Font;
import java.awt.Cursor;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

public class SteamStyle {
    
    public static final Color STEAM_DARK = new Color(23, 26, 33);
    public static final Color STEAM_DARKER = new Color(18, 20, 25);
    public static final Color STEAM_GRAY = new Color(42, 71, 94);
    public static final Color STEAM_GRAY_LIGHT = new Color(55, 90, 115);
    public static final Color STEAM_TEXT = new Color(199, 213, 224);
    public static final Color STEAM_TEXT_DARK = new Color(150, 170, 185);
    public static final Color STEAM_BLUE = new Color(66, 151, 255);
    public static final Color STEAM_BLUE_DARK = new Color(45, 125, 220);
    public static final Color STEAM_LIGHT_BLUE = new Color(100, 180, 255);
    public static final Color STEAM_GREEN = new Color(92, 184, 92);
    public static final Color STEAM_RED = new Color(220, 53, 69);
    
    public static JPanel createSteamPanel() {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(java.awt.Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                GradientPaint gp = new GradientPaint(0, 0, STEAM_DARKER, 0, getHeight(), STEAM_DARK);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
                g2d.dispose();
            }
        };
        panel.setOpaque(false);
        return panel;
    }
    
    public static void styleTitleLabel(JLabel label) {
        label.setForeground(STEAM_LIGHT_BLUE);
        label.setFont(new Font("Segoe UI", Font.BOLD, 32));
    }
    
    public static void styleSubtitleLabel(JLabel label) {
        label.setForeground(STEAM_TEXT);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 16));
    }
    
    public static void styleLabel(JLabel label) {
        label.setForeground(STEAM_TEXT);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 13));
    }
    
    public static void styleTextField(JTextField field) {
        field.setBackground(new Color(35, 45, 55));
        field.setForeground(STEAM_TEXT);
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBorder(javax.swing.BorderFactory.createCompoundBorder(
            javax.swing.BorderFactory.createLineBorder(new Color(60, 80, 100), 1),
            javax.swing.BorderFactory.createEmptyBorder(10, 12, 10, 12)
        ));
        field.setCaretColor(STEAM_LIGHT_BLUE);
        field.setSelectionColor(STEAM_BLUE);
        field.setSelectedTextColor(Color.WHITE);
    }
    
    public static void stylePasswordField(JPasswordField field) {
        field.setBackground(new Color(35, 45, 55));
        field.setForeground(STEAM_TEXT);
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBorder(javax.swing.BorderFactory.createCompoundBorder(
            javax.swing.BorderFactory.createLineBorder(new Color(60, 80, 100), 1),
            javax.swing.BorderFactory.createEmptyBorder(10, 12, 10, 12)
        ));
        field.setCaretColor(STEAM_LIGHT_BLUE);
        field.setSelectionColor(STEAM_BLUE);
        field.setSelectedTextColor(Color.WHITE);
    }
    
    public static void styleButton(JButton button) {
        button.setBackground(STEAM_BLUE);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setOpaque(true);
        
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(STEAM_LIGHT_BLUE);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(STEAM_BLUE);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                button.setBackground(STEAM_BLUE_DARK);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                button.setBackground(STEAM_BLUE);
            }
        });
    }
    
    public static void styleSecondaryButton(JButton button) {
        button.setBackground(STEAM_GRAY);
        button.setForeground(STEAM_TEXT);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setOpaque(true);
        
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(STEAM_GRAY_LIGHT);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(STEAM_GRAY);
            }
        });
    }
}
