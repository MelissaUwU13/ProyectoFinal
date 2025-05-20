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
    private JLabel labelTurnoJugador, labelCantidadFichas;
    private boolean esFaseDeApuesta, jugadorYaJugo;

    //Constructor de la parte grafica de poker 5
    public PanelPoker5CardDraw(int cantidadDeJugadores, int cantidadFichas, ArrayList<String> nombresJugadores, PanelJuegos panelPrincipal) {
        this.panelPrincipal = panelPrincipal;
        esFaseDeApuesta=true;
        // Creamos otro panel
        setLayout(null);
        setPreferredSize(new Dimension(1000, 600));
        fondoPantalla = new ImageIcon("cartas/fondoPantallaPoker.jpg").getImage();
        juego = new Poker5CardDraw(cantidadDeJugadores, nombresJugadores, cantidadFichas);
        // Creamos dos etiquetas: Una para decir el turno del jugador actual y otra para la cantidad de fichas de este jugador
        labelTurnoJugador = new JLabel();
        labelTurnoJugador.setFont(new Font("Arial", Font.BOLD, 24));
        labelTurnoJugador.setForeground(Color.WHITE);
        labelTurnoJugador.setBounds(450, 20, 500, 30);

        labelCantidadFichas = new JLabel();
        labelCantidadFichas.setFont(new Font("Arial", Font.BOLD, 24));
        labelCantidadFichas.setForeground(Color.WHITE);
        labelCantidadFichas.setBounds(20, 50, 500, 30);

        // Con esto ponemos el nombre y cantidad de fichas del jugador actual
        actualizarLabelTurno();

        add(labelTurnoJugador);
        add(labelCantidadFichas);
        botonesCartas = new ArrayList<>();
        cartasSeleccionadas = new ArrayList<>();
        // Aqui empieza lo bueno: En este constructor, vamos a definir casi todos los botones del panel, por que necesitaba una forma de esconder los botones confiablemente
        // Este boton sirve para terminar el turno del jugador
        ImageIcon imagenBotonTerminarTurno = redimensionarImagen("cartas/botonFinalizarTurno.png", 128, 64);
        botonTerminarTurno = new JButton(imagenBotonTerminarTurno);
        botonTerminarTurno.setBounds(800, 20, 128, 64);
        inicializarBotonConImagen(botonTerminarTurno);
        botonTerminarTurno.addActionListener(e -> {
            // En la última fase, este if sirve para hacer que el jugador deba jugar su mano antes de terminar su turno
            if (faseDeApuestaActual == 3 && !jugadorYaJugo) {
                JOptionPane.showMessageDialog(this, "Debes pulsar 'Jugar' antes de terminar tu turno", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
            // Si estamos en la fase de descarte, tenemos una forma de saber cuando acabar dicha fase basándonos en la cantidad de jugadores que pulsaron este botón
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
            // Reseteamos la variable de la ultima fase para el siguiente jugador
            jugadorYaJugo = false;
            pasarAlSiguienteJugador();
        });

        // Creamos el botón de pasar
        ImageIcon imagenBotonPasar = redimensionarImagen("cartas/botonPasar.png", 128, 64);
        botonPasar = new JButton(imagenBotonPasar);
        botonPasar.setBounds(200, 300, 128, 64);
        inicializarBotonConImagen(botonPasar);
        botonPasar.addActionListener(e -> {
            boolean puedePasar = true;
            // Si la apuesta actual es mayor a 0 (o en otras palabras, si alguien ya apostó), ya no se puede usar este boton
            if (juego.getApuestaActual() > 0) {
                JOptionPane.showMessageDialog(this, "No puedes pasar el turno porque ya hay una apuesta activa.", "Aviso", JOptionPane.WARNING_MESSAGE);
                puedePasar = false;
            }
            // Si el jugador puede pasar, pues dejamos que pase
            if (puedePasar) {
                juego.incrementarPases();
                // Si todos los jugadores pasaron turno, vamos a pasar a la siguiente fase del juego
                if (juego.getJugadoresQuePasaronTurno() >= juego.getJugadoresActivos()) {
                    esFaseDeApuesta = !esFaseDeApuesta;
                    faseDeApuestaActual++;
                    juego.reiniciarPases();
                    mostrarBotonesDeDiferentesFases(esFaseDeApuesta);
                }

                pasarAlSiguienteJugador();
            }
        });

        // Creamos el boton de apostar
        ImageIcon imagenBotonApostar = redimensionarImagen("cartas/botonApostar.png", 128, 64);
        botonApostar = new JButton(imagenBotonApostar);
        botonApostar.setBounds(350, 300, 128, 64);
        inicializarBotonConImagen(botonApostar);
        botonApostar.addActionListener(e -> {
            // Si ya se apostó antes, el botón no hace nada
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
                // Si intentamos apostar una cantidad mayor a la que tenemos, no pasa nada
                if (cantidad > jugador.getFichas()) {
                    JOptionPane.showMessageDialog(this, "No tienes suficientes fichas.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                // Seguido de esto y suponiendo que no hayas sido regresado por algún motivo, ya apostamos la cantidad dada
                jugador.apostar(cantidad);
                juego.setApuestaActual(cantidad);
                juego.agregarAlPozo(cantidad);
                juego.reiniciarPases(); // Reiniciamos el conteo de los jugadores que hicieron pase
                // Ya terminamos nuestro turno
                pasarAlSiguienteJugador();

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Cantidad inválida", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        // Creamos nuestro boton para igualar apuestas
        ImageIcon imagenBotonIgualar = redimensionarImagen("cartas/botonIgualar.png", 128, 64);
        botonIgualar = new JButton(imagenBotonIgualar);
        botonIgualar.setBounds(500, 300, 128, 64);
        inicializarBotonConImagen(botonIgualar);
        botonIgualar.addActionListener(e -> {
            boolean puedeIgualar = true;
            // Si no se ha usado bet, el boton no hace nada
            if (juego.getApuestaActual() == 0) {
                JOptionPane.showMessageDialog(this, "No hay una apuesta activa. Usa 'Apostar' o 'Pasar'.", "Aviso", JOptionPane.WARNING_MESSAGE);
                puedeIgualar = false;
            }

            Jugador jugador = juego.getJugadores().get(turnoActualDeJugador);
            int apuestaGlobal = juego.getApuestaActual();
            int cantidadParaIgualar = apuestaGlobal - jugador.getApuestaActual();
            if (puedeIgualar) {
                // Aquí permitimos igualar incluso si el jugador ya no  tiene fichas, hago uso de las funciones de Java.Math para obtener el menor entre estas dos cantidades
                int cantidadReal = Math.min(cantidadParaIgualar, jugador.getFichas());

                jugador.subirYApostar(cantidadParaIgualar);
                juego.agregarAlPozo(cantidadReal);
                // Incrementamos la cantidad de jugadores que igualaron
                juego.incrementarIgualadas();
                // Comprobamos si los jugadores que igualaron son igual a los jugadores activos, si es la misma, ya pasamos de fase
                if (juego.getJugadoresQueIgualaron() >= juego.getJugadoresActivos() - 1) {
                    esFaseDeApuesta = !esFaseDeApuesta;
                    juego.reiniciarPases();
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

        // Creamos el botón de subir apuesta
        ImageIcon imagenBotonSubir = redimensionarImagen("cartas/botonSubir.png", 128, 64);
        botonSubir = new JButton(imagenBotonSubir);
        botonSubir.setBounds(650, 300, 128, 64);
        inicializarBotonConImagen(botonSubir);
        botonSubir.addActionListener(e -> {
            Jugador jugador = juego.getJugadores().get(turnoActualDeJugador);
            int apuestaActual = juego.getApuestaActual();
            boolean subidaRealizada = false;
            // Aquí si dejé que si no hay apuestas activas, puedas usar el botón, es una de las pocas libertades que tome en este trabajo
            // En lo referente al código, aquí le preguntamos al usuario por medio de un bucle cuanto quiere subir la apuesta
            while (!subidaRealizada) {
                String input = JOptionPane.showInputDialog(this,
                        "¿Cuánto quieres subir? (Apuesta actual: " + apuestaActual +
                                "\nTus fichas: " + jugador.getFichas() + ")",
                        "Subir apuesta", JOptionPane.QUESTION_MESSAGE);
                // Si el usuario se arrepiente, se le pregunta si quiere cancelar la subida
                if (input == null || input.trim().isEmpty()) {
                    int opcion = JOptionPane.showConfirmDialog(this, "¿Cancelar subida?", "Subir apuesta", JOptionPane.YES_NO_OPTION);
                    if (opcion == JOptionPane.YES_OPTION) {
                        subidaRealizada = true;
                    }
                } else {
                    try {
                        int cantidad = Integer.parseInt(input);
                        // Checamos que la nueva cantidad de la apuesta sea mayor a la cantidad actual
                        if (cantidad <= apuestaActual) {
                            JOptionPane.showMessageDialog(this, "La nueva apuesta debe ser mayor a la actual.", "Error", JOptionPane.ERROR_MESSAGE);
                        } else {
                            // Si el jugador ya no tiene muchas fichas, se le permite tirar las que le queden
                            int cantidadReal = Math.min(cantidad, jugador.getFichas());
                            // Seguido de esto ya sumamos la cantidad de fichas al pozo
                            jugador.apostar(cantidadReal);
                            juego.agregarAlPozo(cantidadReal);
                            juego.setApuestaActual(cantidadReal);
                            juego.reiniciarPases();

                            pasarAlSiguienteJugador();
                            subidaRealizada = true;
                        }
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(this, "Cantidad inválida", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
        // Creamos el boton para retirarse
        ImageIcon imagenBotonRetirarse = redimensionarImagen("cartas/botonRetirarse.png", 128, 64);
        botonRetirarse = new JButton(imagenBotonRetirarse);
        inicializarBotonConImagen(botonRetirarse);
        botonRetirarse.setBounds(800, 20, 128, 64); // Mismo tamaño que "Analizar Cartas"
        botonRetirarse.addActionListener(e -> {
            Jugador jugadorActual = juego.getJugadores().get(turnoActualDeJugador);
            // Confirmamos si el jugador va a retirarse
            int confirmacion = JOptionPane.showConfirmDialog(
                    this,
                    "¿Seguro que quieres retirarte?",
                    "Confirmar retiro",
                    JOptionPane.YES_NO_OPTION
            );
            if (confirmacion == JOptionPane.YES_OPTION) {
                // Si dice que si, sacamos al jugador del juego
                jugadorActual.retirarse();
                if(juego.getJugadoresActivos() == 1) {
                    // Si solo queda un jugador, lo volvemos el ganador del juego, y le preguntamos si quiere salir o volver a jugar
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
                        // Reiniciamos el juego si dice que quiere volver a jugar
                        reiniciarJuego();
                    } else {
                        // Volvemos al menu principal si quiere devolverse
                        irAlMenuPrincipal();
                    }
                }
            }else {
                pasarAlSiguienteJugador();
            }
        });
        // Creamos un botón para cambiar las cartas de nuestro mazo
        ImageIcon imagenBotonCambiarCartas = redimensionarImagen("cartas/botonCambiarCartas.png", 200, 128);
        botonCambiarCartas = new JButton(imagenBotonCambiarCartas);
        inicializarBotonConImagen(botonCambiarCartas);
        botonCambiarCartas.setBounds(500, 250, 200, 128);
        botonCambiarCartas.addActionListener(e -> {
            Jugador jugadorActual = juego.getJugadores().get(turnoActualDeJugador);
            ArrayList<Carta> manoActual = jugadorActual.getMano();
            ArrayList<Integer> indicesSeleccionadosAbajo = new ArrayList<>();
            // Si ya descartamos, no va a dejar descartar de nuevo
            if (jugadorActual.getHuboCambioDeCartas()) {
                JOptionPane.showMessageDialog(this, "Ya descartaste. No puedes hacerlo de nuevo.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Revisamos qué cartas están seleccionadas
            for (int i = 0; i < cartasSeleccionadas.size(); i++) {
                if (cartasSeleccionadas.get(i)) {
                    indicesSeleccionadosAbajo.add(i);
                }
            }
            // Si no se seleccionó nada y se presionó descartar, el botón no hace nada
            if (indicesSeleccionadosAbajo.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No seleccionaste cartas para descartar.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Reemplazamos las cartas seleccionadas por nuevas
            for (int idx : indicesSeleccionadosAbajo) {
                Carta cartaNueva = juego.sacarCarta();
                manoActual.set(idx, cartaNueva);
            }

            // Limpiamos el arreglo de cartas seleccionadas
            for (int i : indicesSeleccionadosAbajo) {
                cartasSeleccionadas.set(i, false);
            }

            // Marcamos que ya descartamos cartas, impidiendo que volvamos a descartar al volver a pulsar el botón
            jugadorActual.setHuboCambioDeCartas(true);
            actualizarMano(manoActual);

        });
        ImageIcon imagenBotonAnalizarCartas = redimensionarImagen("cartas/botonAnalizarCartas.png", 200, 128);
        // Creamos otro botón para evaluar una jugada posible en nuestro mazo
        botonAnalizarCartas = new JButton(imagenBotonAnalizarCartas);
        botonAnalizarCartas.setBounds(275, 250, 200, 128);
        inicializarBotonConImagen(botonAnalizarCartas);
        botonAnalizarCartas.addActionListener(e -> {
            // Esto nos sirve para evaluar la jugada que vamos a hacer
            ArrayList<Carta> manoActual = juego.getJugadores().get(turnoActualDeJugador).getMano();
            ArrayList<Carta> cartasSeleccionadasParaAnalizar = new ArrayList<>();
            // Se obtienen las cartas que tú seleccionaste
            for (int i = 0; i < cartasSeleccionadas.size(); i++) {
                if (cartasSeleccionadas.get(i)) {
                    cartasSeleccionadasParaAnalizar.add(manoActual.get(i));
                }
            }
            // Si no seleccionaste ninguna, el juego te avisa
            if (cartasSeleccionadasParaAnalizar.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No seleccionaste ninguna carta", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
            // Evaluamos las manos, e interpretamos el resultado para después mostrarlo por medio de un mensaje
            String resultado = juego.evaluarMano(cartasSeleccionadasParaAnalizar);
            JOptionPane.showMessageDialog(this, resultado);
            mostrarBotonesDeDiferentesFases(esFaseDeApuesta);
        });
        ImageIcon imagenBotonJugar = redimensionarImagen("cartas/botonJugar.png", 200, 128);
        // Creamos el boton de jugar
        botonJugar = new JButton(imagenBotonJugar);
        inicializarBotonConImagen(botonJugar);
        botonJugar.setBounds(500, 250, 200, 128);
        botonJugar.addActionListener(e -> {
            ArrayList<Carta> manoActual = juego.getJugadores().get(turnoActualDeJugador).getMano();
            ArrayList<Carta> cartasSeleccionadasParaAnalizar = new ArrayList<>();
            // Este botón funciona casi igual al de analizar cartas, solo que en vez de mostrar el resultado en la pantalla, vamos a comparar la puntuación con los demás jugadores
            for (int i = 0; i < cartasSeleccionadas.size(); i++) {
                if (cartasSeleccionadas.get(i)) {
                    cartasSeleccionadasParaAnalizar.add(manoActual.get(i));
                }
            }
            // Si no se seleccionó ninguna carta, no pasa nada
            if (cartasSeleccionadasParaAnalizar.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No seleccionaste ninguna carta", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
            // Aquí obtenemos el resultado de la jugada que usó el ganador
            String resultado = juego.evaluarMano(cartasSeleccionadasParaAnalizar);

            juego.guardarPuntuacion(juego.getJugadores().get(turnoActualDeJugador));
            juego.incrementarJugadas();
            // Si la cantidad de jugadores que ya jugó es la misma que la cantidad de jugadores activos, se determina al ganador
            if (juego.getJugadoresQueYaJugaron() >= juego.getJugadoresActivos()) {
                // Comparamos puntuaciones
                Jugador ganador = juego.compararPuntuaciones();
                // Reiniciamos la cantidad de veces que jugaron los jugadores, y anunciamos la victoria posteriormente
                juego.setJugadoresQueYaJugaron(0);
                int opcion = JOptionPane.showOptionDialog(
                        this,
                        "¡" + ganador.getNombre() + " gana la ronda!\n¿Qué quieres hacer ahora?",
                        "Fin de la Ronda",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        new String[]{"Volver a jugar", "Menú principal"},
                        "Volver a jugar"
                );
                // Si el jugador lo desea, puede volver a jugar, o ir al menú principal
                if (opcion == JOptionPane.YES_OPTION) {
                    // Reiniciamos el juego
                    reiniciarJuego();
                } else {
                    // Volvemos al menú principal
                    irAlMenuPrincipal();
                }
            }
            // Marcamos que este jugador ya jugó
            jugadorYaJugo = true;
            mostrarBotonesDeDiferentesFases(esFaseDeApuesta);
        });
        ImageIcon imagenbotonSalirDelJuego = redimensionarImagen("cartas/botonSalirDelJuego.png", 128, 64);
        JButton botonSalirDelJuego = new JButton(imagenbotonSalirDelJuego);
        inicializarBotonConImagen(botonSalirDelJuego);
        botonSalirDelJuego.setBounds(800, 100, 128, 64);
        botonSalirDelJuego.addActionListener(e -> {
            // Preguntamos al jugador si quiere salir, si dice que si, lo sacamos al menú principal, si dice que no, no hacemos nada
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

        // Creamos los botones de las cartas, y añadimos cada botón que creamos al panel
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

    // Con esto cambiamos de turno
    private void pasarAlSiguienteJugador() {
        int jugadoresActivos = juego.getJugadoresActivos();
        if (jugadoresActivos == 1) { // Si solo hay un jugador, lo declaramos como el ganador
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
            // Mismas opciones que en los botones, puede volver al menú principal o reiniciar el juego
            if (opcion == JOptionPane.YES_OPTION) {
                reiniciarJuego();
            } else {
                irAlMenuPrincipal();
            }
            return;
        }
        int totalJugadores = juego.getJugadores().size();
        int intentos = 0;
        // Pasamos el turno del jugador aqui
        do {
            turnoActualDeJugador = (turnoActualDeJugador + 1) % totalJugadores;
            intentos++;
        } while (juego.getJugadores().get(turnoActualDeJugador).getRetirado() && intentos < totalJugadores);

        jugadorYaJugo = false;

        // Si todos se retiraron excepto uno, se define el ganador
        actualizarMano(juego.getJugadores().get(turnoActualDeJugador).getMano());
        mostrarBotonesDeDiferentesFases(getEsFaseDeApuesta());
        actualizarLabelTurno();
    }
    // Esto sirve para crear los botones de las cartas, y actualizarlos en caso de que se cambien las cartas
    private void inicializarBotonesCartas(ArrayList<Carta> manoJugador) {
        for (JButton btn : botonesCartas) {
            remove(btn);
        }
        // Limpiamos el contenido de los botones de carta anteriores, asi como las seleccionadas
        botonesCartas.clear();
        cartasSeleccionadas.clear();
        for (int i = 0; i < manoJugador.size(); i++) {
            Carta carta = manoJugador.get(i);
            String nombreArchivo = obtenerNombreArchivoCarta(carta);
            // Creamos un boton que se vea igual que una carta
            ImageIcon imagenCarta = new ImageIcon(nombreArchivo);
            Image imagenCartaEscalada = imagenCarta.getImage().getScaledInstance(100, 150, Image.SCALE_SMOOTH);
            ImageIcon iconoCartaEscalado = new ImageIcon(imagenCartaEscalada);

            JButton cartaBtn = new JButton(iconoCartaEscalado);
            // Separamos la posición en 120 píxeles para cada carta
            cartaBtn.setBounds(200 + (i * 120), 400, 100, 150);
            // Le dejamos un fondo blanco para que el png de la carta se pueda ver bien
            cartaBtn.setOpaque(true);
            cartaBtn.setBackground(Color.WHITE);
            cartaBtn.setContentAreaFilled(true);
            cartaBtn.setBorderPainted(false);
            cartaBtn.setFocusPainted(false);
            cartasSeleccionadas.add(false);
            // como vamos a usar una lambda y no se permite usar valores locales, vamos a crear un valor entero final para encargarnos de las cartas seleccionadas
            int finalI = i;
            botonesCartas.add(cartaBtn);
            cartaBtn.addActionListener(e -> {
                // Al presionar la carta, esta se marca como seleccionada
                boolean seleccionado = !cartasSeleccionadas.get(finalI);
                cartasSeleccionadas.set(finalI, seleccionado);

                // Cuando se selecciona, cambiamos la imagen para que se vea volteada
                String nombreImagen = seleccionado ? "cartas/reversoCarta.jpeg" : obtenerNombreArchivoCarta(manoJugador.get(finalI));
                ImageIcon nuevaImagen = new ImageIcon(new ImageIcon(nombreImagen).getImage()
                        .getScaledInstance(100, 150, Image.SCALE_SMOOTH));
                cartaBtn.setIcon(nuevaImagen);
                // Si la carta está seleccionada, la subimos 20 pixeles para arriba, aparte de voltearla
                if (seleccionado) {
                    cartaBtn.setBorder(BorderFactory.createLineBorder(Color.GREEN, 3));
                    Rectangle bounds = cartaBtn.getBounds();
                    cartaBtn.setBounds(cartaBtn.getX(), cartaBtn.getY() - 20, cartaBtn.getWidth(), cartaBtn.getHeight());
                } else {
                    // Si no lo está, la bajamos 20 píxeles
                    cartaBtn.setBorder(BorderFactory.createEmptyBorder());
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

    // Este metodo sirve para mostrar o esconder los diferentes botones basandonos si estamos en la fase de apuestas o no
    public void mostrarBotonesDeDiferentesFases(boolean esFaseDeApuesta) {
        // Si estamos en la fase de apuestas, estos 5 botones se van a mostrar
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
        // Si estamos en la segunda parte de la fase de apuestas, se muestran los botones de cambiar cartas
        botonCambiarCartas.setVisible(faseDeApuestaActual == 1);
        botonCambiarCartas.setVisible(faseDeApuestaActual == 1);
        // Si estamos en la última parte del juego, el botón de jugar se muestra
        botonJugar.setVisible(faseDeApuestaActual == 3);
        botonJugar.setEnabled(faseDeApuestaActual == 3);
        // Estos botones se van a mostrar siempre y cuando no se muestren los primeros 5
        botonAnalizarCartas.setVisible(!esFaseDeApuesta);
        botonAnalizarCartas.setEnabled(!esFaseDeApuesta);


        botonTerminarTurno.setVisible(!esFaseDeApuesta);
        botonTerminarTurno.setEnabled(!esFaseDeApuesta);
    }

    // Con esto actualizamos las cartas que tenemos en nuestra mano
    private void actualizarMano(ArrayList<Carta> mano) {
        inicializarBotonesCartas(mano);
    }

    // Esto sirve para actualizar los mensajes que hay en el juego
    private void actualizarLabelTurno() {
        Jugador jugadorActual = juego.getJugadores().get(turnoActualDeJugador);

        // Verificamos que el jugador no se haya retirado
        if (jugadorActual.getRetirado()) {
            pasarAlSiguienteJugador();
            return;
        }

        labelTurnoJugador.setText("Turno: " + jugadorActual.getNombre());
        labelCantidadFichas.setText("Fichas: " + jugadorActual.getFichas());
    }

    // Con esto reiniciamos el juego
    public void reiniciarJuego() {
        // Guardamos los nombres y las fichas de cada jugador
        ArrayList<String> nombres = new ArrayList<>();
        ArrayList<Integer> fichasIndividuales = new ArrayList<>();

        for (Jugador j : juego.getJugadores()) {
            nombres.add(j.getNombre());
            fichasIndividuales.add(j.getFichas());
        }

        // Creamos una nueva instancia de Poker5CardDraw
        juego = new Poker5CardDraw(nombres.size(), nombres, 0); // Valor base no relevante

        // Asignamos las fichas manualmente
        for (int i = 0; i < juego.getJugadores().size(); i++) {
            juego.getJugadores().get(i).setFichas(fichasIndividuales.get(i));
        }
        // Reiniciamos todas las variables de estado
        turnoActualDeJugador = 0;
        faseDeApuestaActual = 0;
        esFaseDeApuesta = true;
        jugadorYaJugo = false;

        // Actualizamos la GUI
        inicializarBotonesCartas(juego.getJugadores().get(turnoActualDeJugador).getMano());
        actualizarLabelTurno();
        mostrarBotonesDeDiferentesFases(true);
    }

    // Esto sirve para devolvernos al panel original, o dicho de otra forma, al menú principal
    private void irAlMenuPrincipal() {
        // Cambiamos de panel al panel original
        JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
        topFrame.setContentPane(new PanelJuegos()); // Usa tu clase de menú
        topFrame.revalidate();
    }

    // Esto nos sirve para adaptar el tamaño de la imagen al botón, la cual puede resultar muy grande o pequeña
    private ImageIcon redimensionarImagen(String rutaImagen, int ancho, int alto) {
        ImageIcon original = new ImageIcon(rutaImagen);
        Image escalada = original.getImage().getScaledInstance(ancho, alto, Image.SCALE_SMOOTH);
        return new ImageIcon(escalada);
    }

    // Esto sirve para adaptar los botones que tengan una imagen, aqui removemos sus esquinas, los recuadros que se iluminen al seleccionarlos y los volvemos visibles
    private void inicializarBotonConImagen(JButton boton) {
        boton.setContentAreaFilled(false);
        boton.setFocusPainted(false);
        boton.setBorderPainted(false);
        boton.setVisible(true);
    }

    // Con esto obtenemos el nombre de archivo para la imagen de la carta
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

    // Getter para la fase de apuestas
    private boolean getEsFaseDeApuesta() {
        return esFaseDeApuesta;
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(fondoPantalla, 0, 0, getWidth(), getHeight(), this);
    }
}