import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class PanelPoker5CardDraw extends JPanel {
    private PanelJuegos panelPrincipal;
    private Poker5CardDraw juego;
    private ArrayList<JButton> botonesCartas;
    private ArrayList<Boolean> cartasSeleccionadas;
    private Image fondoPantalla;
    private int turnoActualDeJugador = 1, faseDeApuestaActual;
    JButton botonCheck, botonBet, botonCall, botonRaise, botonFold, botonCambiarCartas, botonAnalizarCartas, botonTerminarTurno, botonJugar;
    private JLabel labelTurnoJugador, labelCantidadFichas;
    private boolean esFaseDeApuesta;

    public PanelPoker5CardDraw(int cantidadDeJugadores, ArrayList<String> nombresJugadores, PanelJuegos panelPrincipal) {
        this.panelPrincipal = panelPrincipal;
        esFaseDeApuesta=true;


        setLayout(null);
        setPreferredSize(new Dimension(1000, 600));
        fondoPantalla = new ImageIcon("cartas/fondoPantallaPoker.jpg").getImage();
        juego = new Poker5CardDraw(cantidadDeJugadores, nombresJugadores);
        labelTurnoJugador = new JLabel();
        labelTurnoJugador.setFont(new Font("Arial", Font.BOLD, 24));
        labelTurnoJugador.setForeground(Color.WHITE);
        labelTurnoJugador.setBounds(450, 20, 500, 30);
        labelCantidadFichas = new JLabel();
        labelCantidadFichas.setFont(new Font("Arial", Font.BOLD, 24));
        labelCantidadFichas.setForeground(Color.WHITE);
        labelCantidadFichas.setBounds(20, 50, 500, 30);
        actualizarLabelTurno(); // Esto pone el nombre del primer jugador
        add(labelTurnoJugador);
        add(labelCantidadFichas);
        botonesCartas = new ArrayList<>();
        cartasSeleccionadas = new ArrayList<>();

        ImageIcon imagenBotonTerminarTurno = redimensionarImagen("cartas/botonFinalizarTurno.png", 128, 64);
        botonTerminarTurno = new JButton(imagenBotonTerminarTurno);
        botonTerminarTurno.setBounds(800, 20, 128, 64);
        inicializarBotonConImagen(botonTerminarTurno);

        botonTerminarTurno.addActionListener(e -> {
            if (!esFaseDeApuesta) {
                Jugador jugadorActual = juego.getJugadores().get(turnoActualDeJugador);

                if (jugadorActual.getHuboCambioDeCartas()) {
                    // Ya descartó previamente
                } else {
                    // No quiso descartar
                    jugadorActual.setHuboCambioDeCartas(true);
                }

                juego.incrementarDescartes();

                if (juego.getJugadoresQueDescartaron() >= juego.getJugadoresActivos()) {
                    System.out.println("¡Todos terminaron la fase de descarte!");
                    faseDeApuestaActual++;
                    for (Jugador jugador : juego.getJugadores()) {
                        jugador.setHuboCambioDeCartas(false);
                    }
                    juego.reiniciarDescartes();
                    esFaseDeApuesta = !esFaseDeApuesta;
                    mostrarBotonesDeDiferentesFases(esFaseDeApuesta);
                }
            }

            pasarAlSiguienteJugador();
        });

        ImageIcon imagenBotonPasar = redimensionarImagen("cartas/botonCheck.png", 128, 64);
        botonCheck = new JButton(imagenBotonPasar);
        botonCheck.setBounds(200, 300, 128, 64);
        inicializarBotonConImagen(botonCheck);
        botonCheck.addActionListener(e -> {
            if (juego.getApuestaActual() > 0) {
                JOptionPane.showMessageDialog(this, "No puedes hacer 'check' porque ya hay una apuesta activa.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }

            juego.incrementarChecks();
            System.out.println("Jugador pasó");

            if (juego.getJugadoresQueHicieronCheck() >= juego.getJugadoresActivos()) {
                System.out.println("¡Todos los jugadores hicieron check!");
                esFaseDeApuesta = !esFaseDeApuesta; // ← AQUÍ
                faseDeApuestaActual++;
                juego.reiniciarChecks();
                // avanzar a la siguiente fase aquí
                mostrarBotonesDeDiferentesFases(esFaseDeApuesta);
            }

            pasarAlSiguienteJugador();
        });
        ImageIcon imagenBotonApostar = redimensionarImagen("cartas/botonBet.png", 128, 64);
        botonBet = new JButton(imagenBotonApostar);
        botonBet.setBounds(350, 300, 128, 64);
        inicializarBotonConImagen(botonBet);
        botonBet.addActionListener(e -> {
            if (juego.getApuestaActual() > 0) {
                JOptionPane.showMessageDialog(this, "Ya hay una apuesta activa. Usa 'Subir' o 'Igualar'.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String input = JOptionPane.showInputDialog(this, "¿Cuánto quieres apostar?", "Apuesta", JOptionPane.QUESTION_MESSAGE);
            if (input == null) return;

            try {
                int cantidad = Integer.parseInt(input);
                Jugador5CardDraw jugador = (Jugador5CardDraw) juego.getJugadores().get(turnoActualDeJugador);

                if (cantidad > jugador.getFichas()) {
                    JOptionPane.showMessageDialog(this, "No tienes suficientes fichas.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                jugador.apostar(cantidad);
                juego.setApuestaActual(cantidad);
                juego.reiniciarChecks(); // Ya no hay checks, se reinicia el conteo

                pasarAlSiguienteJugador();

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Cantidad inválida", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        ImageIcon imagenBotonIgualar = redimensionarImagen("cartas/botonCall.png", 128, 64);
        botonCall = new JButton(imagenBotonIgualar);
        botonCall.setBounds(500, 300, 128, 64);
        inicializarBotonConImagen(botonCall);
        botonCall.addActionListener(e -> {
            if (juego.getApuestaActual() == 0) {
                JOptionPane.showMessageDialog(this, "No hay una apuesta activa. Usa 'Apostar' o 'Pasar'.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
            Jugador5CardDraw jugador = (Jugador5CardDraw) juego.getJugadores().get(turnoActualDeJugador);
            int apuestaActual = juego.getApuestaActual();
            int fichasJugador = jugador.getFichas();
            int cantidadParaIgualar = apuestaActual; // si guardas cuánto apostó cada jugador, usa la diferencia aquí

            if (cantidadParaIgualar > fichasJugador) {
                JOptionPane.showMessageDialog(this, "No tienes fichas suficientes para igualar.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            jugador.subirYApostar(apuestaActual, juego.getApuestaActual());
            System.out.println("Jugador igualó con " + cantidadParaIgualar + " fichas");
            juego.incrementarCalls();
            if (juego.getJugadoresQueHicieronCall() >= juego.getJugadoresActivos() - 1) {
                System.out.println("¡Todos igualaron la apuesta!");
                esFaseDeApuesta = !esFaseDeApuesta; // ← AQUÍ
                juego.reiniciarChecks();
                // avanzar a la siguiente fase aquí}
                mostrarBotonesDeDiferentesFases(esFaseDeApuesta);
                faseDeApuestaActual++;
            }
            // Aquí actualiza el estado de la apuesta si tienes alguna variable para eso

            pasarAlSiguienteJugador();
        });

        ImageIcon imagenBotonSubir = redimensionarImagen("cartas/botonRaise.png", 128, 64);
        botonRaise = new JButton(imagenBotonSubir);
        botonRaise.setBounds(650, 300, 128, 64);
        inicializarBotonConImagen(botonRaise);
        botonRaise.addActionListener(e -> {
            if (juego.getApuestaActual() == 0) {
                JOptionPane.showMessageDialog(this, "No hay una apuesta activa. Usa 'Apostar' o 'Pasar'.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
            Jugador5CardDraw jugador = (Jugador5CardDraw) juego.getJugadores().get(turnoActualDeJugador);
            int apuestaActual = juego.getApuestaActual();

            String input = JOptionPane.showInputDialog(this, "¿Cuánto quieres subir (La apuesta actual es de " + apuestaActual + ")?", "Subir apuesta", JOptionPane.QUESTION_MESSAGE);
            if (input == null) return;

            try {
                int cantidad = Integer.parseInt(input);

                if (cantidad <= apuestaActual) {
                    JOptionPane.showMessageDialog(this, "La subida debe ser mayor a la apuesta actual.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (cantidad > jugador.getFichas()) {
                    JOptionPane.showMessageDialog(this, "No tienes suficientes fichas para subir esa cantidad.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                jugador.subirYApostar(cantidad, juego.getApuestaActual());
                juego.setApuestaActual(cantidad);
                juego.reiniciarChecks(); // se resetean los checks porque hubo subida
                System.out.println("Jugador subió la apuesta a " + cantidad);

                pasarAlSiguienteJugador();

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Cantidad inválida", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        ImageIcon imagenBotonFold = redimensionarImagen("cartas/botonRetirarse.png", 128, 64);
        botonFold = new JButton(imagenBotonFold);
        inicializarBotonConImagen(botonFold);
        botonFold.setBounds(800, 20, 128, 64); // Mismo tamaño que "Analizar Cartas"
        botonFold.addActionListener(e -> {
            System.out.println("Jugador se retiró");
            /* El parentesis con el que iniciamos abajo es parte de un concepto llamado "casting", en este caso, le
            indicamos a Java que queremos obtener los metodos y atributos de Jugador5CardDraw, y esto para hacer que el jugador se retire
            */
            juego.getJugadores().get(turnoActualDeJugador).retirarse();
            if (juego.getJugadoresActivos() == 1) {
                // Declarar ganador
                Jugador ganador = juego.getJugadorActivoRestante();
                int opcion = JOptionPane.showOptionDialog(
                        this,
                        "¡" + ganador.getNombre() + " gana la ronda!\n¿Qué quieres hacer?",
                        "Fin de la Ronda",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        new String[]{"Volver a jugar", "Menú principal"},
                        "Volver a jugar"
                );

                if (opcion == JOptionPane.YES_OPTION) {
                    // Reiniciar el juego
                    reiniciarJuego();
                } else {
                    // Volver al menú principal (esto dependerá de cómo lo tengas estructurado)
                    irAlMenuPrincipal();
                }
            }
            pasarAlSiguienteJugador();
        });
        // Botón para confirmar las cartas seleccionadas, luego vamos a borrar o cambiar esto
        ImageIcon imagenBotonCambiarCartas = redimensionarImagen("cartas/botonCambiarCartas.png", 200, 128);
        botonCambiarCartas = new JButton(imagenBotonCambiarCartas);
        inicializarBotonConImagen(botonCambiarCartas);
        botonCambiarCartas.setBounds(500, 250, 200, 128);
        botonCambiarCartas.addActionListener(e -> {
            Jugador jugadorActual = juego.getJugadores().get(turnoActualDeJugador);
            ArrayList<Carta> manoActual = jugadorActual.getMano();
            ArrayList<Integer> indicesSeleccionadosAbajo = new ArrayList<>();

            if (jugadorActual.getHuboCambioDeCartas()) {
                JOptionPane.showMessageDialog(this, "Ya descartaste. No puedes hacerlo de nuevo.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Revisar qué cartas están seleccionadas
            for (int i = 0; i < cartasSeleccionadas.size(); i++) {
                if (cartasSeleccionadas.get(i)) {
                    indicesSeleccionadosAbajo.add(i);
                }
            }

            if (indicesSeleccionadosAbajo.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No seleccionaste cartas para descartar.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Reemplazar cartas
            for (int idx : indicesSeleccionadosAbajo) {
                Carta cartaNueva = juego.sacarCarta();
                manoActual.set(idx, cartaNueva);
            }

            // Limpiar selección
            for (int i : indicesSeleccionadosAbajo) {
                cartasSeleccionadas.set(i, false);
            }

            // Marcar que ya hizo el cambio
            jugadorActual.setHuboCambioDeCartas(true);
            actualizarMano(manoActual);

        });
        botonAnalizarCartas = new JButton("Analizar Cartas");
        botonAnalizarCartas.setBounds(200, 250, 128, 64);
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
            mostrarBotonesDeDiferentesFases(esFaseDeApuesta);
        });
        botonJugar = new JButton("Jugar");
        botonJugar.setBounds(200, 300, 128, 64);
        botonJugar.addActionListener(e -> {
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
            JOptionPane.showMessageDialog(this, resultado);
            mostrarBotonesDeDiferentesFases(esFaseDeApuesta);

            juego.guardarPuntuacion(juego.getJugadores().get(turnoActualDeJugador));
            juego.incrementarJugadas();

            if (juego.getJugadoresQueYaJugaron() >= juego.getJugadoresActivos()) {
                Jugador ganador = juego.compararPuntuaciones();
                JOptionPane.showMessageDialog(this, "¡" + ganador.getNombre() + " gana la ronda con: " + resultado + "!");
                juego.setJugadoresQueYaJugaron(0);
            }
        });


        inicializarBotonesCartas(juego.getJugadores().get(turnoActualDeJugador).getMano());
        add(botonCheck);
        add(botonBet);
        add(botonCall);
        add(botonRaise);
        add(botonFold);
        add(botonCambiarCartas);
        add(botonTerminarTurno);
        add(botonAnalizarCartas);
        add(botonJugar);
        mostrarBotonesDeDiferentesFases(getEsFaseDeApuesta());
    }

    private void pasarAlSiguienteJugador() {
        int totalJugadores = juego.getJugadores().size();
        int intentos = 0;

        do {
            turnoActualDeJugador = (turnoActualDeJugador + 1) % totalJugadores;
            intentos++;
        } while (juego.getJugadores().get(turnoActualDeJugador).estaRetirado() && intentos < totalJugadores);

        // Si todos se retiraron excepto uno, se define el ganador
        actualizarMano(juego.getJugadores().get(turnoActualDeJugador).getMano());
        mostrarBotonesDeDiferentesFases(getEsFaseDeApuesta());
        actualizarLabelTurno();
    }

    private void inicializarBotonesCartas(ArrayList<Carta> manoJugador) {
        for (JButton btn : botonesCartas) {
            remove(btn);
        }

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


        mostrarBotonesDeDiferentesFases(esFaseDeApuesta);
    }

    public void mostrarBotonesDeDiferentesFases(boolean esFaseDeApuesta) {
        botonFold.setVisible(esFaseDeApuesta);
        botonFold.setEnabled(esFaseDeApuesta);

        botonCheck.setVisible(esFaseDeApuesta);
        botonCheck.setEnabled(esFaseDeApuesta);

        botonBet.setVisible(esFaseDeApuesta);
        botonBet.setEnabled(esFaseDeApuesta);

        botonRaise.setVisible(esFaseDeApuesta);
        botonRaise.setEnabled(esFaseDeApuesta);

        botonCall.setVisible(esFaseDeApuesta);
        botonCall.setEnabled(esFaseDeApuesta);

        botonCambiarCartas.setVisible(faseDeApuestaActual == 1);
        botonCambiarCartas.setVisible(faseDeApuestaActual == 1);

        botonAnalizarCartas.setVisible(!esFaseDeApuesta);
        botonAnalizarCartas.setEnabled(!esFaseDeApuesta);

        botonJugar.setVisible(!esFaseDeApuesta);
        botonJugar.setEnabled(!esFaseDeApuesta);

        botonTerminarTurno.setVisible(!esFaseDeApuesta);
        botonTerminarTurno.setEnabled(!esFaseDeApuesta);
    }

    private void actualizarMano(ArrayList<Carta> mano) {
        inicializarBotonesCartas(mano);
    }

    private void actualizarIconoCarta(JButton boton, Carta carta, int ancho, int alto) {
        String ruta = obtenerNombreArchivoCarta(carta);
        ImageIcon icono = new ImageIcon(new ImageIcon(ruta).getImage().getScaledInstance(100, 150, Image.SCALE_SMOOTH));
        boton.setIcon(icono);
    }

    private void actualizarLabelTurno() {
        Jugador jugador = juego.getJugadores().get(turnoActualDeJugador);
        labelTurnoJugador.setText("Turno de: " + jugador.getNombre());
        labelCantidadFichas.setText("Cantidad de Fichas: " + jugador.getFichas());
    }

    private boolean getEsFaseDeApuesta() {
        return esFaseDeApuesta;
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

    private void reiniciarJuego() {
        // Lógica para reiniciar el juego (barajar, repartir, resetear variables, etc.)
        juego.reiniciarPartida(); // si tienes un método así
        turnoActualDeJugador = 1;
        faseDeApuestaActual = 0;
        esFaseDeApuesta = true;
        mostrarBotonesDeDiferentesFases(true);
        actualizarLabelTurno();
        actualizarMano(juego.getJugadores().get(turnoActualDeJugador).getMano());
    }

    private void irAlMenuPrincipal() {
        // Cambia de panel o ventana, según tu arquitectura
        JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
        topFrame.setContentPane(new PanelJuegos()); // Usa tu clase de menú
        topFrame.revalidate();
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