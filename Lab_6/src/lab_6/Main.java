/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lab_6;

/**
 *
 * @author mavasquez
 */
import java.io.IOException;
import java.util.Calendar;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        Steam steam = new Steam();
        
        try {
            List<Steam.Jugador> players = steam.leerTodosLosJugadores();
            Steam.Jugador adminPlayer = null;
            for (Steam.Jugador p : players) {
                if (p.tipoUsuario.equalsIgnoreCase("admin") && p.username.equals("admin")) {
                    adminPlayer = p;
                    break;
                }
            }
            if (adminPlayer == null) {
                Calendar cal = Calendar.getInstance();
                cal.set(1990, Calendar.JANUARY, 1);
                steam.addPlayer("admin", "123", "Administrador", cal, "", "admin");
            } else if (!adminPlayer.password.equals("123")) {
                Calendar cal = Calendar.getInstance();
                cal.setTimeInMillis(adminPlayer.nacimiento);
                steam.updatePlayer(adminPlayer.code, "admin", "123", adminPlayer.nombre, cal, adminPlayer.rutaImagen, "admin");
            }
            
            List<Steam.Juego> juegos = steam.leerTodosLosJuegos();
            String[] titulos = {
                "MarioKart Deluxe 8",
                "The Leyend of Zelda",
                "FC26",
                "ARC Raiders",
                "Bloons TD 8",
                "Expedition 33",
                "Rocket League",
                "Super Smash Bros"
            };
            char[] sistemas = {'W', 'W', 'W', 'M', 'W', 'W', 'W', 'L'};
            int[] edades = {7, 12, 8, 14, 6, 18, 3, 7};
            double[] precios = {59.99, 39.99, 99.99, 49.99, 59.99, 69.99, 49.99, 26.95};
            String[] imagenes = {
                "/Images/1.png",
                "/Images/2.png",
                "/Images/3.png",
                "/Images/4.png",
                "/Images/5.png",
                "/Images/6.png",
                "/Images/7.png",
                "/Images/8.png"
            };
            
            boolean necesitaRecrear = false;
            if (juegos.size() != 8) {
                necesitaRecrear = true;
            } else {
                for (int i = 0; i < 8; i++) {
                    if (i >= juegos.size() || 
                        !juegos.get(i).titulo.equals(titulos[i]) ||
                        juegos.get(i).sistemaOperativo != sistemas[i] ||
                        juegos.get(i).edadMinima != edades[i] ||
                        Math.abs(juegos.get(i).precio - precios[i]) > 0.01 ||
                        !juegos.get(i).rutaImagen.equals(imagenes[i])) {
                        necesitaRecrear = true;
                        break;
                    }
                }
            }
            
            if (necesitaRecrear) {
                for (Steam.Juego juego : juegos) {
                    steam.deleteGame(juego.code);
                }
                
                java.io.RandomAccessFile rafCodes = new java.io.RandomAccessFile("steam/codes.stm", "rw");
                rafCodes.seek(0);
                rafCodes.writeInt(1);
                rafCodes.close();
                
                for (int i = 0; i < 8; i++) {
                    steam.addGame(titulos[i], sistemas[i], edades[i], precios[i], imagenes[i]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        new LoginFrame(steam).setVisible(true);
    }
}
