import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class PanelPoker5CardDraw extends JPanel {
    private PanelJuegos panelPrincipal;
    private Poker5CardDraw juego;
    private ArrayList<JButton> botonesCartas;
    private ArrayList<Boolean> cartasSeleccionadas;
    private Image fondoPantalla;
    private int turnoActualDeJugador = 0, faseDeApuestaActual;
    JButton botonPasar, botonApostar, botonIgualar, botonSubir, botonRetirarse, botonCambiarCartas, botonAnalizarCartas, botonTerminarTurno, botonJugar;
    private JLabel labelTurnoJugador, labelCantidadFichas,labelApuestaActual;
    private boolean esFaseDeApuesta, jugadorYaJugo;

    //AGREGAR FICHAS
    public PanelPoker5CardDraw(int cantidadDeJugadores, int cantidadFichas, ArrayList<String> nombresJugadores, PanelJuegos panelPrincipal) {
        this.panelPrincipal = panelPrincipal;
        esFaseDeApuesta=true;

        setLayout(null);
        setPreferredSize(new Dimension(1000, 600));
        fondoPantalla = new ImageIcon("cartas/fondoPantallaPoker.jpg").getImage();
        //FALTA AGREGAR FICHAS
        juego = new Poker5CardDraw(cantidadDeJugadores, nombresJugadores, cantidadFichas);

        labelTurnoJugador = new JLabel();
        labelTurnoJugador.setFont(new Font("Arial", Font.BOLD, 24));
        labelTurnoJugador.setForeground(Color.WHITE);
        labelTurnoJugador.setBounds(450, 20, 500, 30);

        labelCantidadFichas = new JLabel();
        labelCantidadFichas.setFont(new Font("Arial", Font.BOLD, 24));
        labelCantidadFichas.setForeground(Color.WHITE);
        labelCantidadFichas.setBounds(20, 50, 500, 30);

        labelApuestaActual = new JLabel();
        labelApuestaActual.setFont(new Font("Arial", Font.BOLD, 24));
        labelApuestaActual.setForeground(Color.WHITE);
        labelApuestaActual.setBounds(20, 70, 500, 30);

        actualizarLabelTurno(); // Esto pone el nombre del primer jugador

        add(labelTurnoJugador);
        add(labelCantidadFichas);
        add(labelApuestaActual);
        botonesCartas = new ArrayList<>();
        cartasSeleccionadas = new ArrayList<>();

        ImageIcon imagenBotonTerminarTurno = redimensionarImagen("cartas/botonFinalizarTurno.png", 128, 64);
        botonTerminarTurno = new JButton(imagenBotonTerminarTurno);
        botonTerminarTurno.setBounds(800, 20, 128, 64);
        inicializarBotonConImagen(botonTerminarTurno);

        // En el ActionListener del botón Terminar Turno (modificar)
        botonTerminarTurno.addActionListener(e -> {
            // Verificación para la última fase de apuestas
            if (faseDeApuestaActual == 3 && !jugadorYaJugo) {
                JOptionPane.showMessageDialog(this, "Debes pulsar 'Jugar' antes de terminar tu turno", "Aviso", JOptionPane.WARNING_MESSAGE);
                return; // No permite cambiar de turno
            }

            if (!esFaseDeApuesta) {
                Jugador jugadorActual = juego.getJugadores().get(turnoActualDeJugador);

                if (!jugadorActual.getHuboCambioDeCartas()) {
                    jugadorActual.setHuboCambioDeCartas(true);
                }

                juego.incrementarDescartes();

                if (juego.getJugadoresQueDescartaron() >= juego.getJugadoresActivos()) {
                    faseDeApuestaActual++;
                    for (Jugador jugador : juego.getJugadores()) {
                        jugador.setHuboCambioDeCartas(false);
                    }
                    juego.reiniciarDescartes();
                    esFaseDeApuesta = !esFaseDeApuesta;
                    mostrarBotonesDeDiferentesFases(esFaseDeApuesta);
                }
            }

            jugadorYaJugo = false; // Resetear para el siguiente jugador
            pasarAlSiguienteJugador();
        });

        //cambiar name de la imagen
        ImageIcon imagenBotonPasar = redimensionarImagen("cartas/botonPasar.png", 128, 64);
        botonPasar = new JButton(imagenBotonPasar);
        botonPasar.setBounds(200, 300, 128, 64);
        inicializarBotonConImagen(botonPasar);

        //LO CAMBIE PARA NO USAR RETURN
        botonPasar.addActionListener(e -> {
            boolean puedePasar = true;

            if (juego.getApuestaActual() > 0) {
                JOptionPane.showMessageDialog(this, "No puedes hacer 'check' porque ya hay una apuesta activa.", "Aviso", JOptionPane.WARNING_MESSAGE);
                puedePasar = false;
            }

            if (puedePasar) {
                juego.incrementarChecks();

                if (juego.getJugadoresQueHicieronCheck() >= juego.getJugadoresActivos()) {
                    esFaseDeApuesta = !esFaseDeApuesta; // ← AQUÍ cambia la fase
                    faseDeApuestaActual++;
                    juego.reiniciarChecks();
                    mostrarBotonesDeDiferentesFases(esFaseDeApuesta);
                }

                pasarAlSiguienteJugador();
            }
        });


        ImageIcon imagenBotonApostar = redimensionarImagen("cartas/botonApostar.png", 128, 64);
        botonApostar = new JButton(imagenBotonApostar);
        botonApostar.setBounds(350, 300, 128, 64);
        inicializarBotonConImagen(botonApostar);

        //posible cambio
        botonApostar.addActionListener(e -> {
            if (juego.getApuestaActual() > 0) {
                JOptionPane.showMessageDialog(this, "Ya hay una apuesta activa. Usa 'Subir' o 'Igualar'.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String input = JOptionPane.showInputDialog(this, "¿Cuánto quieres apostar?", "Apuesta", JOptionPane.QUESTION_MESSAGE);
            if (input == null) return;

            try {
                int cantidad = Integer.parseInt(input);
                /* El parentesis con el que iniciamos abajo es parte de un concepto llamado "casting", en este caso, le
            indicamos a Java que queremos obtener los metodos y atributos de Jugador5CardDraw
            */
                Jugador5CardDraw jugador = (Jugador5CardDraw) juego.getJugadores().get(turnoActualDeJugador);

                if (cantidad > jugador.getFichas()) {
                    JOptionPane.showMessageDialog(this, "No tienes suficientes fichas.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                jugador.apostar(cantidad);
                juego.setApuestaActual(cantidad);
                juego.agregarAlPozo(cantidad);
                juego.reiniciarChecks(); // Ya no hay checks, se reinicia el conteo

                pasarAlSiguienteJugador();

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Cantidad inválida", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });


        //cambiar name foto
        ImageIcon imagenBotonIgualar = redimensionarImagen("cartas/botonIgualar.png", 128, 64);
        botonIgualar = new JButton(imagenBotonIgualar);
        botonIgualar.setBounds(500, 300, 128, 64);
        inicializarBotonConImagen(botonIgualar);

        //LO CAMBIE PARA NO USAR RETURN
        botonIgualar.addActionListener(e -> {
            boolean puedeIgualar = true;

            if (juego.getApuestaActual() == 0) {
                JOptionPane.showMessageDialog(this, "No hay una apuesta activa. Usa 'Apostar' o 'Pasar'.", "Aviso", JOptionPane.WARNING_MESSAGE);
                puedeIgualar = false;
            }

            Jugador jugador = juego.getJugadores().get(turnoActualDeJugador);
            int apuestaGlobal = juego.getApuestaActual();
            int cantidadParaIgualar = apuestaGlobal - jugador.getApuestaActual();

            if (puedeIgualar) {
                // Permitir igualar incluso si no tiene fichas suficientes (all-in)
                int cantidadReal = Math.min(cantidadParaIgualar, jugador.getFichas());

                jugador.subirYApostar(cantidadParaIgualar);
                juego.agregarAlPozo(cantidadReal);

                System.out.println(jugador.getNombre() + " igualó con " + cantidadReal + " fichas");
                System.out.println("Fichas restantes: " + jugador.getFichas());

                juego.incrementarCalls();

                if (juego.getJugadoresQueHicieronCall() >= juego.getJugadoresActivos() - 1) {
                    esFaseDeApuesta = !esFaseDeApuesta;
                    juego.reiniciarChecks();
                    mostrarBotonesDeDiferentesFases(esFaseDeApuesta);
                    faseDeApuestaActual++;

                    // Resetear apuesta actual al cambiar de fase
                    juego.setApuestaActual(0);
                    for (Jugador j : juego.getJugadores()) {
                        j.setApuestaActual(0);
                    }
                }

                actualizarLabelTurno();
                pasarAlSiguienteJugador();
            }
        });

        //cambiar name de foto
        ImageIcon imagenBotonSubir = redimensionarImagen("cartas/botonSubir.png", 128, 64);
        botonSubir = new JButton(imagenBotonSubir);
        botonSubir.setBounds(650, 300, 128, 64);
        inicializarBotonConImagen(botonSubir);

        //LO CAMBIE PARA NO USAR RETURN
        botonSubir.addActionListener(e -> {
            Jugador jugador = juego.getJugadores().get(turnoActualDeJugador);
            int apuestaActual = juego.getApuestaActual();
            boolean subidaRealizada = false;

            while (!subidaRealizada) {
                String input = JOptionPane.showInputDialog(this,
                        "¿Cuánto quieres subir? (Apuesta actual: " + apuestaActual +
                                "\nTus fichas: " + jugador.getFichas() + ")",
                        "Subir apuesta", JOptionPane.QUESTION_MESSAGE);

                if (input == null || input.trim().isEmpty()) {
                    int opcion = JOptionPane.showConfirmDialog(this, "¿Cancelar subida?", "Subir apuesta", JOptionPane.YES_NO_OPTION);
                    if (opcion == JOptionPane.YES_OPTION) {
                        subidaRealizada = true;
                    }
                } else {
                    try {
                        int cantidad = Integer.parseInt(input);

                        if (cantidad <= apuestaActual) {
                            JOptionPane.showMessageDialog(this, "La nueva apuesta debe ser mayor a la actual.", "Error", JOptionPane.ERROR_MESSAGE);
                        } else {
                            // Permitir all-in
                            int cantidadReal = Math.min(cantidad, jugador.getFichas());

                            jugador.apostar(cantidadReal);
                            juego.agregarAlPozo(cantidadReal);
                            juego.setApuestaActual(cantidadReal);
                            juego.reiniciarChecks();

                            System.out.println("Jugador subió la apuesta a " + cantidadReal);

                            pasarAlSiguienteJugador();
                            subidaRealizada = true;
                        }
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(this, "Cantidad inválida", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });


        //cambie el name de la imagen boton
        ImageIcon imagenBotonRetirarse = redimensionarImagen("cartas/botonRetirarse.png", 128, 64);
        botonRetirarse = new JButton(imagenBotonRetirarse);
        inicializarBotonConImagen(botonRetirarse);
        botonRetirarse.setBounds(800, 20, 128, 64); // Mismo tamaño que "Analizar Cartas"

        botonRetirarse.addActionListener(e -> {
            Jugador jugadorActual = juego.getJugadores().get(turnoActualDeJugador);
            int confirmacion = JOptionPane.showConfirmDialog(
                    this,
                    "¿Seguro que quieres retirarte?",
                    "Confirmar retiro",
                    JOptionPane.YES_NO_OPTION
            );
            if (confirmacion == JOptionPane.YES_OPTION) {
                jugadorActual.retirarse();
                if(juego.getJugadoresActivos() == 1) {
                    Jugador ganador = juego.getJugadorActivoRestante();
                    int confirmarOpcion = JOptionPane.showOptionDialog(
                            this,
                            "¡" + ganador.getNombre() + " gana la ronda!\n¿Qué quieres hacer?",
                            "Fin de la Ronda",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.QUESTION_MESSAGE,
                            null,
                            new String[]{"Volver a jugar", "Menú principal"},
                            "Volver a jugar"
                    );
                    if (confirmarOpcion == JOptionPane.YES_OPTION) {
                        // Reiniciar el juego
                        reiniciarJuego();
                    } else {
                        // Volver al menú principal (esto dependerá de cómo lo tengas estructurado)
                        irAlMenuPrincipal();
                    }
                }
            }else {
                pasarAlSiguienteJugador();
            }
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
            JOptionPane.showMessageDialog(this, resultado);
            mostrarBotonesDeDiferentesFases(esFaseDeApuesta);
        });
        botonJugar = new JButton("Jugar");
        botonJugar.setBounds(500, 250, 200, 128);
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

            String resultado = juego.evaluarMano(cartasSeleccionadasParaAnalizar);
            JOptionPane.showMessageDialog(this, resultado);

            juego.guardarPuntuacion(juego.getJugadores().get(turnoActualDeJugador));
            juego.incrementarJugadas();

            if (juego.getJugadoresQueYaJugaron() >= juego.getJugadoresActivos()) {
                Jugador ganador = juego.compararPuntuaciones();
                juego.setJugadoresQueYaJugaron(0);
                int opcion = JOptionPane.showOptionDialog(
                        this,
                        "¡" + ganador.getNombre() + " gana la ronda usando " + resultado + "!\n¿Qué quieres hacer ahora?",
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

            jugadorYaJugo = true; // Marcamos que este jugador ya jugó
            mostrarBotonesDeDiferentesFases(esFaseDeApuesta);
        });
        JButton botonSalirDelJuego = new JButton("Salir del juego");
        botonSalirDelJuego.setBounds(800, 100, 128, 64);
        botonSalirDelJuego.addActionListener(e -> {
            int opcion = JOptionPane.showOptionDialog(
                    this,
                    "¿Seguro que quieres salir del juego?",
                    "Aviso",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    new String[]{"Si", "No"},
                    "No"
            );
            if (opcion == JOptionPane.YES_OPTION) {
                irAlMenuPrincipal();
            }
        });

        inicializarBotonesCartas(juego.getJugadores().get(turnoActualDeJugador).getMano());
        add(botonPasar);
        add(botonApostar);
        add(botonIgualar);
        add(botonSubir);
        add(botonRetirarse);
        add(botonCambiarCartas);
        add(botonTerminarTurno);
        add(botonAnalizarCartas);
        add(botonJugar);
        add(botonSalirDelJuego);
        mostrarBotonesDeDiferentesFases(getEsFaseDeApuesta());
    }

    private void pasarAlSiguienteJugador() {
        int jugadoresActivos = juego.getJugadoresActivos();
        if (jugadoresActivos == 1) {
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
                reiniciarJuego();
            } else {
                irAlMenuPrincipal();
            }
            return;
        }
        int totalJugadores = juego.getJugadores().size();
        int intentos = 0;

        do {
            turnoActualDeJugador = (turnoActualDeJugador + 1) % totalJugadores;
            intentos++;
        } while (juego.getJugadores().get(turnoActualDeJugador).estaRetirado() && intentos < totalJugadores);

        jugadorYaJugo = false;

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

            ImageIcon imagenCarta = new ImageIcon(nombreArchivo);
            Image imagenCartaEscalada = imagenCarta.getImage().getScaledInstance(100, 150, Image.SCALE_SMOOTH);
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

                // 1. Cambiar la imagen (voltear boca abajo)
                String nombreImagen = seleccionado ? "cartas/reversoCarta.jpeg" : obtenerNombreArchivoCarta(manoJugador.get(finalI));
                ImageIcon nuevaImagen = new ImageIcon(new ImageIcon(nombreImagen).getImage()
                        .getScaledInstance(100, 150, Image.SCALE_SMOOTH));
                cartaBtn.setIcon(nuevaImagen);

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
        botonRetirarse.setVisible(esFaseDeApuesta);
        botonRetirarse.setEnabled(esFaseDeApuesta);

        botonPasar.setVisible(esFaseDeApuesta);
        botonPasar.setEnabled(esFaseDeApuesta);

        botonApostar.setVisible(esFaseDeApuesta);
        botonApostar.setEnabled(esFaseDeApuesta);

        botonIgualar.setVisible(esFaseDeApuesta);
        botonIgualar.setEnabled(esFaseDeApuesta);

        botonIgualar.setVisible(esFaseDeApuesta);
        botonIgualar.setEnabled(esFaseDeApuesta);

        botonSubir.setVisible(esFaseDeApuesta);
        botonSubir.setEnabled(esFaseDeApuesta);

        botonCambiarCartas.setVisible(faseDeApuestaActual == 1);
        botonCambiarCartas.setVisible(faseDeApuestaActual == 1);

        botonJugar.setVisible(faseDeApuestaActual == 3);
        botonJugar.setEnabled(faseDeApuestaActual == 3);

        botonAnalizarCartas.setVisible(!esFaseDeApuesta);
        botonAnalizarCartas.setEnabled(!esFaseDeApuesta);


        botonTerminarTurno.setVisible(!esFaseDeApuesta);
        botonTerminarTurno.setEnabled(!esFaseDeApuesta);
    }

    private void actualizarMano(ArrayList<Carta> mano) {
        inicializarBotonesCartas(mano);
    }

    private void actualizarLabelTurno() {
        Jugador jugadorActual = juego.getJugadores().get(turnoActualDeJugador);

        // Verificar que el jugador no está retirado
        if (jugadorActual.estaRetirado()) {
            pasarAlSiguienteJugador();
            return;
        }

        labelTurnoJugador.setText("Turno: " + jugadorActual.getNombre());
        labelCantidadFichas.setText("Fichas: " + jugadorActual.getFichas());
        labelApuestaActual.setText("Apuesta actual: " + juego.getApuestaActual());
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

    public void reiniciarJuego() {
        // Guardar configuración completa
        ArrayList<String> nombres = new ArrayList<>();
        ArrayList<Integer> fichasIndividuales = new ArrayList<>();

        for (Jugador j : juego.getJugadores()) {
            nombres.add(j.getNombre());
            fichasIndividuales.add(j.getFichas());
        }

        // Crear nueva instancia
        juego = new Poker5CardDraw(nombres.size(), nombres, 0); // Valor base no relevante

        // Asignar fichas individuales manualmente
        for (int i = 0; i < juego.getJugadores().size(); i++) {
            juego.getJugadores().get(i).setFichas(fichasIndividuales.get(i));
        }
        // Resetear variables de estado
        turnoActualDeJugador = 0;
        faseDeApuestaActual = 0;
        esFaseDeApuesta = true;
        jugadorYaJugo = false;

        // Actualizar UI
        inicializarBotonesCartas(juego.getJugadores().get(turnoActualDeJugador).getMano());
        actualizarLabelTurno();
        mostrarBotonesDeDiferentesFases(true);
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