import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class PanelJuegos extends JPanel {
    private Image fondoPantalla;
    private String nombreCarta;
    private Poker5CardDraw juego;
    private ArrayList<Boolean> cartasSeleccionadas;
    private ArrayList<JButton> botonesCartas;

    public PanelJuegos() {
        cartasSeleccionadas = new ArrayList<>();
        botonesCartas = new ArrayList<>();
        setPreferredSize(new Dimension(1000, 600));
        setLayout(null);
        setBackground(Color.WHITE);
        setFocusable(true);
        ImageIcon imagen5Card = new ImageIcon("cartas/boton5CardPoker.png");
        Image imagenEscalada5Card = imagen5Card.getImage().getScaledInstance(256, 128, Image.SCALE_SMOOTH);
        ImageIcon imagenBoton5CardPoker = new ImageIcon(imagenEscalada5Card);

        ImageIcon imagen7Card = new ImageIcon("cartas/boton7CardStud.png");
        Image imagenEscalada7Card = imagen7Card.getImage().getScaledInstance(256, 128, Image.SCALE_SMOOTH);
        ImageIcon imagenBoton7CardStud = new ImageIcon(imagenEscalada7Card);
        // Fondo de la pantalla
        fondoPantalla = new ImageIcon("cartas/Portada.png").getImage();


        JButton botonPoker5Hands = new JButton(imagenBoton5CardPoker);
        JButton botonPoker7CardStud = new JButton(imagenBoton7CardStud);
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
                        fondoPantalla = new ImageIcon("cartas/fondoPantallaPoker.jpg").getImage();
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
            botonPoker7CardStud.setVisible(false);
            // Iniciamos el juego y mostrar manos en consola (por ahora)
            this.juego = new Poker5CardDraw(cantidadDeJugadores);
            juego.mostrarManos();
            ArrayList<Carta> manoJugador = juego.getJugadores().get(0).getMano(); // solo primer jugador
            inicializarBotonesCartas(manoJugador);

            // Refrescar el panel para que se muestren los nuevos botones, asi como que se oculte el original
            revalidate();
            repaint();
        });
        botonPoker7CardStud.addActionListener(e -> {
            int cantidadDeJugadores = 0;
            System.out.println(cantidadDeJugadores + "Funciona bien!!");
        });
        botonPoker5Hands.setContentAreaFilled(false);
        botonPoker5Hands.setBorderPainted(false);
        botonPoker5Hands.setFocusPainted(false);
        botonPoker5Hands.setBounds(200, 400, 256, 128);
        botonPoker7CardStud.setVisible(true);
        botonPoker7CardStud.setContentAreaFilled(false);
        botonPoker7CardStud.setBorderPainted(false);
        botonPoker7CardStud.setFocusPainted(false);
        botonPoker7CardStud.setBounds(500, 400, 256, 128);
        botonPoker7CardStud.setVisible(true);
        add(botonPoker5Hands);
        add(botonPoker7CardStud);
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
    private void inicializarBotonesCartas(ArrayList<Carta> manoJugador) {
        cartasSeleccionadas.clear();
        botonesCartas.clear();
        for (int i = 0; i < manoJugador.size(); i++) {
            Carta carta = manoJugador.get(i);
            String nombreArchivo = obtenerNombreArchivoCarta(carta);

            ImageIcon iconoCarta = new ImageIcon(nombreArchivo);
            Image imagenCartaEscalada = iconoCarta.getImage().getScaledInstance(100, 150, Image.SCALE_SMOOTH);
            ImageIcon iconoCartaEscalado = new ImageIcon(imagenCartaEscalada);

            JButton cartaBtn = new JButton(iconoCartaEscalado);
            cartaBtn.setBounds(200 + (i * 120), 400, 100, 150); // Posición separada
            cartaBtn.setOpaque(true);
            cartaBtn.setBackground(Color.WHITE);
            cartaBtn.setContentAreaFilled(true); // para que se pinte el fondo blanco
            cartaBtn.setBorderPainted(false);
            cartaBtn.setFocusPainted(false);
            cartasSeleccionadas.add(false);
            // como vamos a usar una lambda y no se permite usar valores locales, vamos a crear un valor entero final para encargarnos de las cartas seleccionadas
            int finalI = i;
            botonesCartas.add(cartaBtn);

            cartaBtn.addActionListener(e -> {
                boolean seleccionado = !cartasSeleccionadas.get(finalI);
                cartasSeleccionadas.set(finalI, seleccionado);

                if (seleccionado) {
                    cartaBtn.setBorder(BorderFactory.createLineBorder(Color.GREEN, 3));
                    // Subir la carta 20 pixeles si la seleccionamos
                    Rectangle bounds = cartaBtn.getBounds();
                    cartaBtn.setBounds(bounds.x, bounds.y - 20, bounds.width, bounds.height);
                } else {
                    cartaBtn.setBorder(BorderFactory.createEmptyBorder());
                    // Bajarla de nuevo 20 píxeles cuando ya no este seleccionada
                    Rectangle bounds = cartaBtn.getBounds();
                    cartaBtn.setBounds(bounds.x, bounds.y + 20, bounds.width, bounds.height);
                }
            });
            add(cartaBtn);
        }
        // Botón de confirmar selección
        JButton confirmarBtn = new JButton("Confirmar selección");
        confirmarBtn.setBounds(400, 300, 200, 40);
        confirmarBtn.addActionListener(e -> {
            ArrayList<Carta> cartasSeleccionadasParaAnalizar = new ArrayList<>();
            for (int i = 0; i < cartasSeleccionadas.size(); i++) {
                if (cartasSeleccionadas.get(i)) {
                    cartasSeleccionadasParaAnalizar.add(manoJugador.get(i));
                }
            }
            if (cartasSeleccionadasParaAnalizar.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No seleccionaste ninguna carta", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Llamas a analizarMano con solo las cartas seleccionadas
            juego.analizarMano(cartasSeleccionadasParaAnalizar);

            // Puedes mostrar la puntuación o lo que haga analizarMano
            JOptionPane.showMessageDialog(this, "Mano analizada con las cartas seleccionadas.");
        });
        add(confirmarBtn);
    }
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g); // Esto limpia el fondo bien

        g.drawImage(fondoPantalla, 0, 0, getWidth(), getHeight(), this); // Dibuja fondo
    }
}