import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class PanelJuegos extends JPanel {
    private Image fondoPantalla;
    public PanelJuegos() {
        setPreferredSize(new Dimension(800, 600));
        setLayout(null);
        setBackground(Color.WHITE);
        setFocusable(true);
        ImageIcon imagen = new ImageIcon("sale Balatrito.PNG");

        // Fondo de la pantalla
        fondoPantalla = new ImageIcon("sale Balatrito.PNG").getImage();

        JButton botonDePrueba = new JButton(imagen);
        botonDePrueba.setContentAreaFilled(false);
        botonDePrueba.setBorderPainted(false);
        botonDePrueba.setFocusPainted(false);
        botonDePrueba.setBounds(400, 500, 128, 256);
        add(botonDePrueba);
    }
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g); // Esto limpia el fondo bien

        g.drawImage(fondoPantalla, 0, 0, getWidth(), getHeight(), this); // Dibuja fondo
    }
}
