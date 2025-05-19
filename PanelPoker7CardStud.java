import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class PanelPoker7CardStud extends JPanel{
    private PanelJuegos panelPrincipal;
    private Poker7CardStud juego;
    private ArrayList<JButton> botonesCartas;
    private ArrayList<Boolean> cartasSeleccionadas;
    private Image fondoPantalla;
    private int turnoActualDeJugador = 1, faseDeApuestaActual;
    JButton botonPasar, botonApostar, botonIgualar, botonSubir, botonCompletar, botonAnalizarCartas, botonTerminarTurno, botonJugar;
    private JLabel labelTurnoJugador, labelCantidadFichas, labelRondaActual, labelApuestaActual;
    private boolean esFaseDeApuesta;


    public PanelPoker7CardStud(int cantidadDeJugadores, int cantidadFichas, ArrayList<String> nombresJugadores, PanelJuegos panelPrincipal) {
        this.panelPrincipal = panelPrincipal;
        esFaseDeApuesta=true;

        setLayout(null);
        setPreferredSize(new Dimension(1000, 600));
        fondoPantalla = new ImageIcon("cartas/fondoPantallaPoker.jpg").getImage();

        //ERROR DESDE ACA
        //FALTA AGREGAR NOMBRES
        juego = new Poker7CardStud(cantidadDeJugadores, cantidadFichas, nombresJugadores);


        labelTurnoJugador = new JLabel();
        labelTurnoJugador.setFont(new Font("Arial", Font.BOLD, 24));
        labelTurnoJugador.setForeground(Color.WHITE);
        labelTurnoJugador.setBounds(450, 20, 500, 30);

        labelCantidadFichas = new JLabel();
        labelCantidadFichas.setFont(new Font("Arial", Font.BOLD, 24));
        labelCantidadFichas.setForeground(Color.WHITE);
        labelCantidadFichas.setBounds(20, 50, 500, 30);

        labelRondaActual = new JLabel();
        labelRondaActual.setFont(new Font("Arial", Font.BOLD, 24));
        labelRondaActual.setForeground(Color.WHITE);
        labelRondaActual.setBounds(450, 30, 500, 30);

        labelApuestaActual = new JLabel();
        labelApuestaActual.setFont(new Font("Arial", Font.BOLD, 24));
        labelApuestaActual.setForeground(Color.WHITE);
        labelApuestaActual.setBounds(450, 40, 500, 30);

        actualizarLabelTurno(); // Esto pone el nombre del primer jugador

        add(labelTurnoJugador);
        add(labelCantidadFichas);
        add(labelRondaActual);
        add(labelApuestaActual);
        botonesCartas = new ArrayList<>();
        cartasSeleccionadas = new ArrayList<>();

        ImageIcon imagenBotonTerminarTurno = redimensionarImagen("cartas/botonFinalizarTurno.png", 128, 64);
        botonTerminarTurno = new JButton(imagenBotonTerminarTurno);
        botonTerminarTurno.setBounds(800, 20, 128, 64);
        inicializarBotonConImagen(botonTerminarTurno);



        botonTerminarTurno.addActionListener(e -> {
            //AQUI FASE DE APUESTAS!
            pasarAlSiguienteJugador();
        });



        //cambiar name de la imagen
        ImageIcon imagenBotonPasar = redimensionarImagen("cartas/botonPasar.png", 128, 64);
        botonPasar = new JButton(imagenBotonPasar);
        botonPasar.setBounds(200, 300, 128, 64);
        inicializarBotonConImagen(botonPasar);

        //ESTE BOTON ES DIFERENCIA AL MIO - FUNCION DIFERENTE - BOTON PASAR
        //COPIE LA FUNCION DE RETIRARSE AQUI - COMPROBAR
        botonPasar.addActionListener(e -> {
            System.out.println("Jugador se retiró");
            JOptionPane.showMessageDialog(this, "Jugador se retira del juego!");
            /* El parentesis con el que iniciamos abajo es parte de un concepto llamado "casting", en este caso, le
            indicamos a Java que queremos obtener los metodos y atributos de Jugador5CardDraw, y esto para hacer que el jugador se retire
            */
            juego.getJugadores().get(turnoActualDeJugador).retirarse();
            if (juego.getJugadoresActivos() == 1) {
                // Declarar ganador
                Jugador ganador = juego.getJugadorActivoRestante();
                JOptionPane.showMessageDialog(this, "¡" + ganador.getNoJugador() + " gana la ronda por abandono!");
                // Avanza a la siguiente ronda o termina el juego aquí
            }
            pasarAlSiguienteJugador();
        });


        //cambiar name foto
        ImageIcon imagenBotonApostar = redimensionarImagen("cartas/botonApostar.png", 128, 64);
        botonApostar = new JButton(imagenBotonApostar);
        botonApostar.setBounds(350, 300, 128, 64);
        inicializarBotonConImagen(botonApostar);

        //SI FUNCIONA BIEN, PONERSELO AL CODIGO DE DEREK
        botonApostar.addActionListener(e -> {
            boolean apuestaRealizada = false;

            while (!apuestaRealizada) {
                String input = JOptionPane.showInputDialog(this, "¿Cuánto quieres apostar?", "Apuesta", JOptionPane.QUESTION_MESSAGE);

                if (input == null || input.trim().isEmpty()) {
                    int opcion = JOptionPane.showConfirmDialog(this, "¿Quieres intentar apostar de nuevo?", "Cancelar apuesta", JOptionPane.YES_NO_OPTION);
                    if (opcion == JOptionPane.NO_OPTION) {
                        apuestaRealizada = true; // Salir del ciclo sin hacer nada
                    }
                } else {
                    try {
                        int cantidad = Integer.parseInt(input);
                        Jugador7CardStud jugador = (Jugador7CardStud) juego.getJugadores().get(turnoActualDeJugador);

                        if (cantidad > jugador.getFichas()) {
                            JOptionPane.showMessageDialog(this, "No tienes suficientes fichas.", "Error", JOptionPane.ERROR_MESSAGE);
                        } else {
                            jugador.apostar(cantidad);
                            juego.setApuestaActual(cantidad);
                            pasarAlSiguienteJugador();
                            apuestaRealizada = true; // Salir del ciclo después de apostar
                        }

                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(this, "Cantidad inválida", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }

        });


        //cambiar name foto
        ImageIcon imagenBotonIgualar = redimensionarImagen("cartas/botonIgualar.png", 128, 64);
        botonIgualar = new JButton(imagenBotonIgualar);
        botonIgualar.setBounds(500, 300, 128, 64);
        inicializarBotonConImagen(botonIgualar);

        botonIgualar.addActionListener(e -> {
            boolean puedeIgualar = true;

            //No es tan necesario en poker 7 pero va
            if (juego.getApuestaActual() == 0) {
                JOptionPane.showMessageDialog(this, "No hay una apuesta activa. Usa 'Apostar' o 'Pasar'.", "Aviso", JOptionPane.WARNING_MESSAGE);
                puedeIgualar = false;
            }

            Jugador7CardStud jugador = (Jugador7CardStud) juego.getJugadores().get(turnoActualDeJugador);
            int apuestaActual = juego.getApuestaActual();
            int fichasJugador = jugador.getFichas();
            int cantidadParaIgualar = apuestaActual; // si guardas cuánto apostó cada jugador, usa la diferencia aquí

            if (puedeIgualar && cantidadParaIgualar > fichasJugador) {
                JOptionPane.showMessageDialog(this, "No tienes fichas suficientes para igualar.", "Error", JOptionPane.ERROR_MESSAGE);
                puedeIgualar = false;
            }

            if (puedeIgualar) {
                jugador.subirYApostar(apuestaActual, juego.getApuestaActual());
                System.out.println("Jugador igualó con " + cantidadParaIgualar + " fichas");
                juego.incrementarCalls();

                if (juego.getJugadoresQueHicieronCall() >= juego.getJugadoresActivos() - 1) {
                    System.out.println("¡Todos igualaron la apuesta!");
                    esFaseDeApuesta = !esFaseDeApuesta; // ← AQUÍ

                    //NECESARIO??
                    juego.reiniciarChecks();
                    mostrarBotonesDeDiferentesFases(esFaseDeApuesta);
                    faseDeApuestaActual++;
                }

                pasarAlSiguienteJugador();
            }
        });


        //cambiar foto name
        ImageIcon imagenBotonSubir = redimensionarImagen("cartas/botonSubir.png", 128, 64);
        botonSubir = new JButton(imagenBotonSubir);
        botonSubir.setBounds(650, 300, 128, 64);
        inicializarBotonConImagen(botonSubir);

        botonSubir.addActionListener(e -> {
            Jugador7CardStud jugador = (Jugador7CardStud) juego.getJugadores().get(turnoActualDeJugador);
            int apuestaActual = juego.getApuestaActual();
            boolean subidaRealizada = false;

            while (!subidaRealizada) {
                String input = JOptionPane.showInputDialog(this, "¿Cuánto quieres subir? (La apuesta actual es de " + apuestaActual + ")", "Subir apuesta", JOptionPane.QUESTION_MESSAGE);

                if (input == null || input.trim().isEmpty()) {
                    int opcion = JOptionPane.showConfirmDialog(this, "¿Cancelar subida?", "Subir apuesta", JOptionPane.YES_NO_OPTION);
                    if (opcion == JOptionPane.YES_OPTION) {
                        subidaRealizada = true; // salir sin subir
                    }
                } else {
                    try {
                        int cantidad = Integer.parseInt(input);

                        if (cantidad <= apuestaActual) {
                            JOptionPane.showMessageDialog(this, "La nueva apuesta debe ser mayor a la apuesta actual.", "Error", JOptionPane.ERROR_MESSAGE);
                        } else if (cantidad > jugador.getFichas()) {
                            JOptionPane.showMessageDialog(this, "No tienes suficientes fichas para subir esa cantidad.", "Error", JOptionPane.ERROR_MESSAGE);
                        } else {
                            jugador.apostar(cantidad);
                            juego.setApuestaActual(cantidad);
                            juego.reiniciarChecks(); // se resetean los checks porque hubo subida
                            System.out.println("Jugador subió la apuesta a " + cantidad);
                            pasarAlSiguienteJugador();
                            subidaRealizada = true; // salida exitosa
                        }

                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(this, "Cantidad inválida", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        //cambiar name foto
        ImageIcon imagenBotonCompletar = redimensionarImagen("cartas/botonCompletar.png", 128, 64);
        botonCompletar = new JButton(imagenBotonCompletar);
        inicializarBotonConImagen(botonCompletar);
        botonCompletar.setBounds(800, 20, 128, 64); // Mismo tamaño que "Analizar Cartas"

        //METER LA LOGICA PARA QUE FUNCIONE Y NOMAS SE LE MUESTRA AL PRIMER JUGADOR
        botonCompletar.addActionListener(e -> {

            pasarAlSiguienteJugador();
        });


        //Boton innecesario, es temporal
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


        //ESTE BOTON PUEDE SERVIR PARA LA ULTIMA RONDA
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
            System.out.println(cartasSeleccionadasParaAnalizar);
            JOptionPane.showMessageDialog(this, resultado);
            mostrarBotonesDeDiferentesFases(esFaseDeApuesta);
        });

        inicializarBotonesCartas(juego.getJugadores().get(turnoActualDeJugador).getMano());
        add(botonPasar);
        add(botonApostar);
        add(botonIgualar);
        add(botonIgualar);
        add(botonCompletar);
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
        botonCompletar.setVisible(esFaseDeApuesta);
        botonCompletar.setEnabled(esFaseDeApuesta);

        botonPasar.setVisible(esFaseDeApuesta);
        botonPasar.setEnabled(esFaseDeApuesta);

        botonApostar.setVisible(esFaseDeApuesta);
        botonApostar.setEnabled(esFaseDeApuesta);

        botonIgualar.setVisible(esFaseDeApuesta);
        botonIgualar.setEnabled(esFaseDeApuesta);

        botonIgualar.setVisible(esFaseDeApuesta);
        botonIgualar.setEnabled(esFaseDeApuesta);

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

    //ME SIRVE???
    private void reiniciarJuego() {
        // Lógica para reiniciar el juego (barajar, repartir, resetear variables, etc.)
        //juego.reiniciarPartida(); // si tienes un m3todo así
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
