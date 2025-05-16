import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class PanelJuegos extends JPanel {
    private Image fondoPantalla;
    private String nombreCarta;
    public PanelJuegos() {
        setPreferredSize(new Dimension(800, 600));
        setLayout(null);
        setBackground(Color.WHITE);
        setFocusable(true);
        Carta carta = new Carta(13, "corazon", "rojo");
        ImageIcon imagen = new ImageIcon(obtenerNombreCarta(carta) + ".PNG");

        // Fondo de la pantalla
        fondoPantalla = new ImageIcon("sale Balatrito.PNG").getImage();

        JButton botonDePrueba = new JButton(imagen);
        botonDePrueba.setContentAreaFilled(false);
        botonDePrueba.setBorderPainted(false);
        botonDePrueba.setFocusPainted(false);
        botonDePrueba.setBounds(400, 500, 128, 256);
        botonDePrueba.setVisible(true);
        add(botonDePrueba);
    }
    public String obtenerNombreCarta(Carta cartaAMostrar) {
        String nombreImagen = cartaAMostrar.toString(cartaAMostrar.getValor(), cartaAMostrar.getFigura());;
        System.out.println(nombreImagen);
        return nombreImagen;
    }
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g); // Esto limpia el fondo bien

        g.drawImage(fondoPantalla, 0, 0, getWidth(), getHeight(), this); // Dibuja fondo
    }
}
