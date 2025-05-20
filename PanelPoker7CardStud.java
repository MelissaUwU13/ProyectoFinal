import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class PanelPoker7CardStud extends JPanel {
    private PanelJuegos panelPrincipal;
    private Poker7CardStud juego;
    private ArrayList<JButton> botonesCartas;
    private ArrayList<Boolean> cartasSeleccionadas;
    private Image fondoPantalla;
    private int turnoActualDeJugador, ronda = 1;
    JButton botonPasar, botonIgualar, botonSubir, botonCompletar, botonJugar;
    private JLabel labelTurnoJugador, labelCantidadFichas, labelRondaActual, labelApuestaActual, labelApuestasTotales;
    private boolean faseStreet, jugadorYaJugo;
    private int jugadorQueSubioUltimo = -1;

    //Constructor de la parte grafica de poker 7
    public PanelPoker7CardStud(int cantidadDeJugadores, int cantidadFichas, ArrayList<String> nombresJugadores, PanelJuegos panelPrincipal) {
        this.panelPrincipal = panelPrincipal;
        faseStreet = true;

        setLayout(null);
        setPreferredSize(new Dimension(1000, 600));
        fondoPantalla = new ImageIcon("Recursos/fondoPantallaPoker.jpg").getImage();

        JOptionPane.showMessageDialog(this, "Antes de iniciar tenemos nuestra primera apuesta obligatoria!", "Ante", JOptionPane.INFORMATION_MESSAGE);
        JOptionPane.showMessageDialog(this, "La apuesta inicial es de 5 fichas!", "Ante", JOptionPane.INFORMATION_MESSAGE);

        juego = new Poker7CardStud(cantidadDeJugadores, cantidadFichas, nombresJugadores);

        //CREACION DE TITULOS
        labelTurnoJugador = new JLabel();
        labelTurnoJugador.setFont(new Font("Arial", Font.BOLD, 24));
        labelTurnoJugador.setForeground(Color.WHITE);
        labelTurnoJugador.setBounds(200, 20, 500, 30);

        labelCantidadFichas = new JLabel();
        labelCantidadFichas.setFont(new Font("Arial", Font.BOLD, 24));
        labelCantidadFichas.setForeground(Color.WHITE);
        labelCantidadFichas.setBounds(20, 80, 500, 30);

        labelRondaActual = new JLabel();
        labelRondaActual.setFont(new Font("Arial", Font.BOLD, 24));
        labelRondaActual.setForeground(Color.WHITE);
        labelRondaActual.setBounds(600, 20, 500, 30);

        labelApuestaActual = new JLabel();
        labelApuestaActual.setFont(new Font("Arial", Font.BOLD, 24));
        labelApuestaActual.setForeground(Color.WHITE);
        labelApuestaActual.setBounds(20, 120, 500, 30);

        labelApuestasTotales = new JLabel();
        labelApuestasTotales.setFont(new Font("Arial", Font.BOLD, 24));
        labelApuestasTotales.setForeground(Color.WHITE);
        labelApuestasTotales.setBounds(20, 160, 500, 30);

        turnoActualDeJugador=juego.obtenerNUMEROJugadorInicial(ronda);
        repartirCartasRondas(ronda);
        actualizarLabelTurno(); // Esto pone el nombre del primer jugador

        add(labelTurnoJugador);
        add(labelCantidadFichas);
        add(labelRondaActual);
        add(labelApuestaActual);
        add(labelApuestasTotales);
        botonesCartas = new ArrayList<>();
        cartasSeleccionadas = new ArrayList<>();


        //Con este boton actua igual que el boton de salir de la ronda, pero te saca del juego y si todos los jugadores se salen, nos da el ganador de forma automatica y nos devuelve al menu

        ImageIcon imagenBotonPasar = redimensionarImagen("Recursos/botonPasar.png", 128, 64);
        botonPasar = new JButton(imagenBotonPasar);
        botonPasar.setBounds(100, 300, 128, 64);
        inicializarBotonConImagen(botonPasar);

        botonPasar.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Jugador se retiro!", "Pasar", JOptionPane.INFORMATION_MESSAGE);
            /* El parentesis con el que iniciamos abajo es parte de un concepto llamado "casting", en este caso, le
            indicamos a Java que queremos obtener los metodos y atributos de Jugador5CardDraw, y esto para hacer que el jugador se retire
            */
            juego.getJugadores().get(turnoActualDeJugador).retirarse();
            if (juego.getJugadoresActivos() == 1) {
                // Declarar ganador
                JOptionPane.showMessageDialog(this, "Gana el jugador!", "Ganador", JOptionPane.INFORMATION_MESSAGE);
                irAlMenuPrincipal();

            }
            pasarAlSiguienteJugador();
        });

        //Este boton sirve para igualar la apuesta y es la bandera o base que nos permite cambiar de ronda una vez todos los jugadores hayan igualado la apuesta

        ImageIcon imagenBotonIgualar = redimensionarImagen("Recursos/botonIgualar.png", 128, 64);
        botonIgualar = new JButton(imagenBotonIgualar);
        botonIgualar.setBounds(248, 300, 128, 64);
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
                pasarAlSiguienteJugador();

            }

        });

        //boton para subir la apuesta actual

        ImageIcon imagenBotonSubir = redimensionarImagen("Recursos/botonSubir.png", 128, 64);
        botonSubir = new JButton(imagenBotonSubir);
        botonSubir.setBounds(396, 300, 128, 64);
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
                            juego.incrementarIgualadas();
                            juego.agregarAlPozo(cantidadReal);
                            jugadorQueSubioUltimo = turnoActualDeJugador;
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


        //boton para el jugadro bring in que actua de forma igual que el boton igualar

        ImageIcon imagenBotonCompletar = redimensionarImagen("Recursos/botonCompletar.png", 128, 64);
        botonCompletar = new JButton(imagenBotonCompletar);
        inicializarBotonConImagen(botonCompletar);
        botonCompletar.setBounds(544, 300, 128, 64); // Mismo tamaño que "Analizar Cartas"

        //comprobar que funciona
        botonCompletar.addActionListener(e -> {
            boolean puedeCompletar = true;

            Jugador jugador = juego.getJugadores().get(turnoActualDeJugador);
            int apuestaGlobal = juego.getApuestaActual();
            int cantidadParaCompletar = apuestaGlobal - jugador.getApuestaActual();

            if (puedeCompletar && cantidadParaCompletar > jugador.getFichas()) {
                JOptionPane.showMessageDialog(this, "No tienes fichas suficientes para igualar.", "Error", JOptionPane.ERROR_MESSAGE);
                puedeCompletar = false;
            }

            if (puedeCompletar) {
                jugador.subirYApostar(cantidadParaCompletar);
                juego.agregarAlPozo(cantidadParaCompletar);
                juego.incrementarIgualadas();
                JOptionPane.showMessageDialog(this, jugador.getNombre() + " completo con " + cantidadParaCompletar + " fichas", "Completar", JOptionPane.INFORMATION_MESSAGE);

                actualizarLabelTurno();
                pasarAlSiguienteJugador();
            }
        });


        //boton jugar que sirve para determinar el ganador de una serie de cartas que los jugadores usaran para ganar, una vez analizadas da un ganador y nos devuelve al menu

        ImageIcon imagenBotonJugar = redimensionarImagen("Recursos/botonJugar.png", 128, 64);
        botonJugar = new JButton(imagenBotonJugar);
        inicializarBotonConImagen(botonJugar);
        botonJugar.setBounds(200, 300, 128, 64);

        botonJugar.addActionListener(e -> {
            ArrayList<Carta> manoActual = juego.getJugadores().get(turnoActualDeJugador).getMano();
            ArrayList<Carta> cartasSeleccionadasParaAnalizar = new ArrayList<>();

            // Reunir las cartas seleccionadas por el jugador actual
            for (int i = 0; i < cartasSeleccionadas.size(); i++) {
                if (cartasSeleccionadas.get(i)) {
                    cartasSeleccionadasParaAnalizar.add(manoActual.get(i));
                }
            }

            // Validar que haya exactamente 5 cartas seleccionadas
            if (cartasSeleccionadasParaAnalizar.size() != 5) {
                JOptionPane.showMessageDialog(this, "Debes seleccionar exactamente 5 cartas", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Evaluar la mano y guardar la puntuación
            String resultado = juego.evaluarMano(cartasSeleccionadasParaAnalizar);
            juego.guardarPuntuacion(juego.getJugadores().get(turnoActualDeJugador));
            juego.incrementarJugadas();

            // Verificar si ya todos los jugadores activos jugaron
            if (juego.getJugadoresQueYaJugaron() >= juego.getJugadoresActivos()) {
                Jugador ganador = juego.compararPuntuaciones();
                juego.setJugadoresQueYaJugaron(0);

                JOptionPane.showMessageDialog(this, "Jugador " + ganador.getNombre() + " ganó!", "Completar", JOptionPane.INFORMATION_MESSAGE);
                irAlMenuPrincipal();
            } else {
                // Pasar al siguiente jugador
                pasarAlSiguienteJugador();
                actualizarMano(juego.getJugadores().get(turnoActualDeJugador).getMano());
                actualizarLabelTurno();
                mostrarBotonesDeDiferentesFases(faseStreet);
            }

            jugadorYaJugo = true;
        });



        inicializarBotonesCartas(juego.getJugadores().get(turnoActualDeJugador).getMano());
        add(botonPasar);
        add(botonIgualar);
        add(botonSubir);
        add(botonCompletar);
        add(botonJugar);

        mostrarBotonesDeDiferentesFases(faseStreet);
    }

    //reparte cartas y les da la vuelta segun la ronda
    public void repartirCartasRondas(int ronda){
        switch (ronda){
            case 1:
                juego.repartirNCartas(2,false);
                juego.repartirNCartas(1,true);
                break;
            case 2:
                juego.repartirNCartas(1,true);

                break;

            case 3:
                juego.repartirNCartas(1,true);

                break;

            case 4:
                juego.repartirNCartas(1,true);

                break;

            case 5:
                juego.repartirNCartas(1,false);
                break;
            case 6:
                for(Jugador jugador : juego.getJugadores()){
                    for(Carta carta : jugador.getMano()){
                        carta.setVisible(true);
                    }
                }
                break;
        }
    }


    //Revisa que todos los jugadores hayan pasado, pasa al siguiente jugador, siguiente ronda y actualiza los botones, y analiza que no se repita el turno de la ultima persona que subio la apuesta
    private void pasarAlSiguienteJugador() {
        int jugadoresTotales = juego.getJugadores().size();
        int contador = 0;

        do {
            turnoActualDeJugador = (turnoActualDeJugador + 1) % jugadoresTotales;
            contador++;

            // Si ya dimos la vuelta completa hasta el que subió
            if (turnoActualDeJugador == jugadorQueSubioUltimo) {
                break;
            }

        } while (
                (juego.getJugadores().get(turnoActualDeJugador).getRetirado() ||
                        juego.getJugadores().get(turnoActualDeJugador).sinFichas() ||
                        juego.getJugadores().get(turnoActualDeJugador).getApuestaActual() == juego.getApuestaActual()) &&
                        contador < jugadoresTotales
        );

        if (turnoActualDeJugador == jugadorQueSubioUltimo || contador >= jugadoresTotales) {
            juego.reiniciarPases();
            juego.reiniciarIgualadas();
            ronda++;
            repartirCartasRondas(ronda);
            inicializarBotonesCartas(juego.getJugadores().get(turnoActualDeJugador).getMano());


            if (ronda == 6) {
                faseStreet = false;
            }

            mostrarBotonesDeDiferentesFases(faseStreet);
            JOptionPane.showMessageDialog(this, "¡Inicia la ronda " + ronda + "!", "Nueva Ronda", JOptionPane.INFORMATION_MESSAGE);

            for (Jugador j : juego.getJugadores()) {
                j.setApuestaActual(0);
            }

            jugadorQueSubioUltimo = -1; // Reiniciar para la nueva ronda
            turnoActualDeJugador = juego.obtenerNUMEROJugadorInicial(ronda);

            while (juego.getJugadores().get(turnoActualDeJugador).getRetirado()) {
                turnoActualDeJugador = (turnoActualDeJugador + 1) % jugadoresTotales;
            }
        }

        actualizarMano(juego.getJugadores().get(turnoActualDeJugador).getMano());
        actualizarLabelTurno();
    }


    //Sirve para crear los botones de carta, si son false los muestra volteados, si son true los muestra boca arriba
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
            if(carta.isVisible()==false){
                iconoCarta = new ImageIcon("Recursos/reversoCarta.jpeg");
            }
            Image imagenCartaEscalada = iconoCarta.getImage().getScaledInstance(100, 150, Image.SCALE_SMOOTH);
            ImageIcon iconoCartaEscalado = new ImageIcon(imagenCartaEscalada);

            JButton cartaBtn = new JButton(iconoCartaEscalado);
            cartaBtn.setBounds(50 + (i * 120), 400, 100, 150); // Posición separada
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

        mostrarBotonesDeDiferentesFases(faseStreet);
    }

    //nos sirve para saber que botones se deben mostrar en X ronda, por ejemplo el boton completar y jugar
    public void mostrarBotonesDeDiferentesFases(boolean faseStreet){
        if (ronda == 1 & turnoActualDeJugador == juego.obtenerNUMEROJugadorInicial(1)) {
            botonCompletar.setVisible(true);
        } else {
            botonCompletar.setVisible(false);
        }

        botonPasar.setVisible(faseStreet);
        botonPasar.setEnabled(faseStreet);

        botonIgualar.setVisible(faseStreet);
        botonIgualar.setEnabled(faseStreet);

        botonSubir.setVisible(faseStreet);
        botonSubir.setEnabled(faseStreet);

        botonJugar.setVisible(!faseStreet);
        botonJugar.setEnabled(!faseStreet);
    }

    //sirve para actualizar la mano de cada jugador
    private void actualizarMano(ArrayList<Carta> mano) {
        inicializarBotonesCartas(mano);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(fondoPantalla, 0, 0, getWidth(), getHeight(), this);
    }

    //sirve para lo grafico de cada boton sea visible
    private void inicializarBotonConImagen(JButton boton) {
        boton.setContentAreaFilled(false);
        boton.setFocusPainted(false);
        boton.setBorderPainted(false);
        boton.setVisible(true);
    }

    //actualiza todos los labels del juego
    private void actualizarLabelTurno() {
        Jugador jugadorActual = juego.getJugadores().get(turnoActualDeJugador);

        // Verificar que el jugador no está retirado
        if (jugadorActual.getRetirado()) {
            pasarAlSiguienteJugador();
            return;
        }

        labelTurnoJugador.setText("Turno de: " + jugadorActual.getNombre());
        labelCantidadFichas.setText("Cantidad de Fichas: " + jugadorActual.getFichas());
        labelRondaActual.setText("Ronda "+ ronda);
        labelApuestaActual.setText("Apuesta actual: "+juego.getApuestaActual());
        labelApuestasTotales.setText("Fichas apostadas: "+juego.getPozo());
    }

    //ayuda a cambiar el tamaño de una imagen, util para los botones
    private ImageIcon redimensionarImagen(String rutaImagen, int ancho, int alto) {
        ImageIcon original = new ImageIcon(rutaImagen);
        Image escalada = original.getImage().getScaledInstance(ancho, alto, Image.SCALE_SMOOTH);
        return new ImageIcon(escalada);
    }

    //nos devuelve al menu principal
    private void irAlMenuPrincipal() {
        // Cambia de panel o ventana, según tu arquitectura
        JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
        topFrame.setContentPane(new PanelJuegos()); // Usa tu clase de menú
        topFrame.revalidate();
    }

    //obtiene el nombre de las cartas segun su tipo y valor
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
        return "Recursos/" + valorStr + figura + ".png";
    }

}