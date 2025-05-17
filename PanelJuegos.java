import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class PanelJuegos extends JPanel {
    private Image fondoPantalla;
    private String nombreCarta;
    private Poker5CardDraw juego;

    public PanelJuegos() {
        setPreferredSize(new Dimension(1000, 600));
        setLayout(null);
        setBackground(Color.WHITE);
        setFocusable(true);
        ImageIcon imagen = new ImageIcon("boton5CardPoker.png");
        Image imagenEscalada = imagen.getImage().getScaledInstance(256, 128, Image.SCALE_SMOOTH);
        ImageIcon imagenBoton5CardPoker = new ImageIcon(imagenEscalada);
        // Fondo de la pantalla
        fondoPantalla = new ImageIcon("Portada.png").getImage();

        JButton botonPoker5Hands = new JButton(imagenBoton5CardPoker);
        botonPoker5Hands.addActionListener(e -> {
            int cantidadDeJugadores = 0;
            boolean entradaValida = false;

            while (!entradaValida) {
                String entrada = JOptionPane.showInputDialog(this, "Introduzca la cantidad de jugadores (2-7)", "Numero de Jugadores", JOptionPane.QUESTION_MESSAGE);

                if (entrada == null) return;

                try {
                    cantidadDeJugadores = Integer.parseInt(entrada);
                    if (cantidadDeJugadores >= 2 && cantidadDeJugadores <= 7) {
                        entradaValida = true;
                    } else {
                        JOptionPane.showMessageDialog(
                                this,
                                "Cantidad inválida. Debe ser entre 2 y 7.",
                                "Error",
                                JOptionPane.ERROR_MESSAGE
                        );
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(
                            this,
                            "Ingrese un número válido.",
                            "Error",
                            JOptionPane.ERROR_MESSAGE
                    );
                }
            }
            // Ocultamos el botón una vez haya sido exitosamente usado
            botonPoker5Hands.setVisible(false);
            // Iniciamos el juego y mostrar manos en consola (por ahora)
            Poker5CardDraw juego = new Poker5CardDraw(cantidadDeJugadores);
            juego.mostrarManos();
            ArrayList<Carta> manoJugador = juego.getJugadores().get(0).getMano(); // solo primer jugador

            for (int i = 0; i < manoJugador.size(); i++) {
                Carta carta = manoJugador.get(i);
                String nombreArchivo = obtenerNombreArchivoCarta(carta);

                ImageIcon iconoCarta = new ImageIcon(nombreArchivo);
                Image imagenCartaEscalada = iconoCarta.getImage().getScaledInstance(100, 150, Image.SCALE_SMOOTH);
                ImageIcon iconoCartaEscalado = new ImageIcon(imagenCartaEscalada);

                JButton cartaBtn = new JButton(iconoCartaEscalado);
                cartaBtn.setBounds(100 + (i * 120), 100, 100, 150); // Posición separada
                cartaBtn.setOpaque(true);
                cartaBtn.setBackground(Color.WHITE);
                cartaBtn.setContentAreaFilled(true); // para que se pinte el fondo blanco
                cartaBtn.setBorderPainted(false);
                cartaBtn.setFocusPainted(false);
                add(cartaBtn);
            }

            // Refrescar el panel para que se muestren los nuevos botones, asi como que se oculte el original
            revalidate();
            repaint();
        });
        botonPoker5Hands.setContentAreaFilled(false);
        botonPoker5Hands.setBorderPainted(false);
        botonPoker5Hands.setFocusPainted(false);
        botonPoker5Hands.setBounds(200, 400, 256, 128);
        botonPoker5Hands.setVisible(true);
        add(botonPoker5Hands);
    }
    private String obtenerNombreArchivoCarta(Carta carta) {
        String valorEnString;
        int valor = carta.getValor();
        switch (valor) {
            case 1: valorEnString = "As"; break;
            case 11: valorEnString = "J"; break;
            case 12: valorEnString = "Q"; break;
            case 13: valorEnString = "K"; break;
            default: valorEnString = String.valueOf(valor); break;
        }

        String figura = carta.getFigura().toLowerCase(); // ejemplo: "corazon"
        System.out.println(valorEnString + figura + ".png");
        return "cartas/" + valorEnString + figura + ".png";
    }
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g); // Esto limpia el fondo bien

        g.drawImage(fondoPantalla, 0, 0, getWidth(), getHeight(), this); // Dibuja fondo
    }
}