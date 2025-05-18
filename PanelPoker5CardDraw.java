import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class PanelPoker5CardDraw extends JPanel {
    private Poker5CardDraw juego;
    private ArrayList<JButton> botonesCartas;
    private ArrayList<Boolean> cartasSeleccionadas;
    private Image fondoPantalla;
    private int turnoActualDeJugador = 0;

    public PanelPoker5CardDraw(int cantidadDeJugadores) {
        setLayout(null);
        setPreferredSize(new Dimension(1000, 600));
        fondoPantalla = new ImageIcon("cartas/fondoPantallaPoker.jpg").getImage();
        juego = new Poker5CardDraw(cantidadDeJugadores);
        botonesCartas = new ArrayList<>();
        cartasSeleccionadas = new ArrayList<>();

        inicializarBotonesCartas(juego.getJugadores().get(turnoActualDeJugador).getMano());


        JButton botonSiguienteJugador = new JButton("Siguiente jugador");
        botonSiguienteJugador.setBounds(800, 20, 160, 40);
        botonSiguienteJugador.addActionListener(e -> {
            turnoActualDeJugador = (turnoActualDeJugador + 1) % juego.getJugadores().size();
            actualizarMano(juego.getJugadores().get(turnoActualDeJugador).getMano());
        });
        add(botonSiguienteJugador);
    }


    private void inicializarBotonesCartas(ArrayList<Carta> manoJugador) {
        for (JButton btn : botonesCartas) {
            remove(btn);
        }
        // Botón para confirmar las cartas seleccionadas, luego vamos a borrar o cambiar esto
        ImageIcon imagenBotonCambiarCartas = redimensionarImagen("cartas/botonCambiarCartas.png", 200, 128);
        JButton botonCambiarCartas = new JButton(imagenBotonCambiarCartas);
        inicializarBotonConImagen(botonCambiarCartas);
        botonCambiarCartas.setBounds(400, 250, 256, 128);
        JButton botonAnalizarCartas = new JButton("Analizar Cartas");
        botonAnalizarCartas.setBounds(200, 250, 256, 128);

        botonesCartas.clear();
        cartasSeleccionadas.clear();
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
                    cartaBtn.setBounds(cartaBtn.getX(), cartaBtn.getY() - 20, cartaBtn.getWidth(), cartaBtn.getHeight());
                } else {
                    cartaBtn.setBorder(BorderFactory.createEmptyBorder());
                    // Bajarla de nuevo 20 píxeles cuando ya no este seleccionada
                    Rectangle bounds = cartaBtn.getBounds();
                    cartaBtn.setBounds(bounds.x, bounds.y + 20, bounds.width, bounds.height);
                }
            });
            add(cartaBtn);
            revalidate();
            repaint();
        }

        botonAnalizarCartas.addActionListener(e -> {
            ArrayList<Carta> manoActual = juego.getJugadores().get(turnoActualDeJugador).getMano();
            ArrayList<Carta> cartasSeleccionadasParaAnalizar = new ArrayList<>();
            for (int i = 0; i < cartasSeleccionadas.size(); i++) {
                if (cartasSeleccionadas.get(i)) {
                    cartasSeleccionadasParaAnalizar.add(manoActual.get(i));
                }
            }

            if (cartasSeleccionadasParaAnalizar.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No seleccionaste ninguna carta", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String resultado = juego.evaluarMano(cartasSeleccionadasParaAnalizar); // o cartasSeleccionadasParaAnalizar si evalúas solo las seleccionadas
            System.out.println(cartasSeleccionadasParaAnalizar);
            JOptionPane.showMessageDialog(this, resultado);
        });
        botonCambiarCartas.addActionListener(e -> {
            ArrayList<Carta> manoActual = juego.getJugadores().get(turnoActualDeJugador).getMano();
            ArrayList<Integer> indicesSeleccionadosAbajo = new ArrayList<>();

            // Solo tomamos las cartas seleccionadas abajo (mano del jugador)
            for (int i = 0; i < cartasSeleccionadas.size(); i++) {
                if (cartasSeleccionadas.get(i)) {
                    indicesSeleccionadosAbajo.add(i);
                }
            }

            if (indicesSeleccionadosAbajo.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No seleccionaste cartas para descartar.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Descartamos las cartas seleccionadas y las reemplazamos por nuevas cartas del mazo
            for (int i = 0; i < indicesSeleccionadosAbajo.size(); i++) {
                int idx = indicesSeleccionadosAbajo.get(i);

                // Cartas descartadas podrían guardarse en un montón aparte si quieres
                Carta cartaDescartada = manoActual.get(idx);
                // Aquí puedes guardar cartaDescartada en un montón de descarte, si tienes

                // Reemplazar con carta nueva del mazo
                Carta cartaNueva = juego.sacarCarta();
                manoActual.set(idx, cartaNueva);
            }

            // Limpiar selección
            for (int i : indicesSeleccionadosAbajo) {
                cartasSeleccionadas.set(i, false);
            }

            // Actualizar interfaz
            actualizarMano(manoActual);
            // No cambias las cartas de arriba (mesa)
        });

        add(botonCambiarCartas);
        add(botonAnalizarCartas);
    }

    private void actualizarMano(ArrayList<Carta> mano) {
        inicializarBotonesCartas(mano);
    }

    private void actualizarIconoCarta(JButton boton, Carta carta, int ancho, int alto) {
        String ruta = obtenerNombreArchivoCarta(carta);
        ImageIcon icono = new ImageIcon(new ImageIcon(ruta).getImage().getScaledInstance(100, 150, Image.SCALE_SMOOTH));
        boton.setIcon(icono);
    }

    private ImageIcon redimensionarImagen(String rutaImagen, int ancho, int alto) {
        ImageIcon original = new ImageIcon(rutaImagen);
        Image escalada = original.getImage().getScaledInstance(ancho, alto, Image.SCALE_SMOOTH);
        return new ImageIcon(escalada);
    }

    private void inicializarBotonConImagen(JButton boton) {
        boton.setContentAreaFilled(false);
        boton.setFocusPainted(false);
        boton.setBorderPainted(false);
        boton.setVisible(true);
    }

    private String obtenerNombreArchivoCarta(Carta carta) {
        int valor = carta.getValor();
        String valorStr;
        switch (valor) {
            case 1: valorStr = "As"; break;
            case 11: valorStr = "J"; break;
            case 12: valorStr = "Q"; break;
            case 13: valorStr = "K"; break;
            default: valorStr = String.valueOf(valor); break;
        }
        String figura = carta.getFigura().toLowerCase();
        return "cartas/" + valorStr + figura + ".png";
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(fondoPantalla, 0, 0, getWidth(), getHeight(), this);
    }
}