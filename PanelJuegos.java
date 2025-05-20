import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;

public class PanelJuegos extends JPanel {
    private Image fondoPantalla;
    private Clip musica;
    ArrayList<String> nombresJugadores = new ArrayList<>();

    public PanelJuegos() {
        // Creamos la ventana
        setPreferredSize(new Dimension(1000, 600));
        setLayout(null);
        setBackground(Color.WHITE);
        setFocusable(true);
        // Reproducimos la música, en este caso lo volví una clase porque si volvía al menu principal, la canción vuelve a sonar por encima de la primera vez que se reprodujo
        ReproductorMusica reproductorDeMusica = new ReproductorMusica();
        reproductorDeMusica.reproducir("cartas/musica.wav");

        // Asignamos el fondo de pantalla
        fondoPantalla = new ImageIcon("cartas/Portada.png").getImage();

        // Creamos las imágenes para los botones, junto con estos
        ImageIcon imagenBoton5CardPoker = redimensionarImagen("cartas/boton5CardPoker.png", 256, 128);
        ImageIcon imagenBoton7CardStud = redimensionarImagen("cartas/boton7CardStud.png", 256, 128);
        JButton botonPoker5Hands = new JButton(imagenBoton5CardPoker);
        JButton botonPoker7CardStud = new JButton(imagenBoton7CardStud);

        // Añadimos las acciones que va a hacer cada uno
        botonPoker5Hands.addActionListener(e -> {
            int cantidadDeJugadores = 0;
            int cantidadFichas = 0;
            boolean entradaValida = false;
            // Le pedimos al usuario la cantidad y nombre de los jugadores
            while (!entradaValida) {
                String entrada = JOptionPane.showInputDialog(this, "Introduzca la cantidad de jugadores (2-7)", "Número de Jugadores", JOptionPane.QUESTION_MESSAGE);
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

            for (int i = 1; i <= cantidadDeJugadores; i++) {
                String nombre;
                do {
                    nombre = JOptionPane.showInputDialog(this, "Nombre del jugador " + i + ":", "Nombre de Jugador", JOptionPane.QUESTION_MESSAGE);
                    if (nombre == null || nombre.trim().isEmpty()) {
                        JOptionPane.showMessageDialog(this, "Nombre inválido. Intenta de nuevo.");
                    }
                } while (nombre == null || nombre.trim().isEmpty());
                nombresJugadores.add(nombre.trim());
            }

            entradaValida = false;
            // Luego le pedimos la cantidad de fichas que va a tener cada jugador
            while (!entradaValida) {
                String entrada = JOptionPane.showInputDialog(this, "Introduzca la cantidad de fichas por jugador (debe ser una cantidad entre 500 y 1000):", "Número de fichas", JOptionPane.QUESTION_MESSAGE);
                if (entrada == null) return;

                try {
                    cantidadFichas = Integer.parseInt(entrada);
                    if (cantidadFichas >= 500 && cantidadFichas <= 1000) {
                        entradaValida = true;
                        fondoPantalla = new ImageIcon("cartas/fondoPantallaPoker.jpg").getImage();
                    } else {
                        JOptionPane.showMessageDialog(
                                this,
                                "Cantidad inválida. Debe ser entre 500 y 1000.",
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

            // Ocultamos botones que ya no se usarán mientras se juega
            botonPoker5Hands.setVisible(false);
            botonPoker7CardStud.setVisible(false);

            // Quitamos los componentes actuales (botones, etc.) para poner el panel del póker
            removeAll();
            revalidate();
            repaint();

            // Agregamos un panel dedicado para jugar 5 Card Draw
            PanelPoker5CardDraw panelPoker5 = new PanelPoker5CardDraw(cantidadDeJugadores, cantidadFichas, nombresJugadores, this);
            panelPoker5.setBounds(0, 0, 1000, 600);
            add(panelPoker5);

            revalidate();
            repaint();
        });





        botonPoker7CardStud.addActionListener(e -> {
            int cantidadDeJugadores = 0;
            int cantidadFichas = 0;
            boolean entradaValida = false;
            // Igual que en el anterior botón, le pedimos al usuario una cantidad y los nombres de los jugadores
            while (!entradaValida) {
                String entrada = JOptionPane.showInputDialog(this, "Introduzca la cantidad de jugadores (2-8)", "Número de Jugadores", JOptionPane.QUESTION_MESSAGE);
                if (entrada == null) return;

                try {
                    cantidadDeJugadores = Integer.parseInt(entrada);
                    if (cantidadDeJugadores >= 2 && cantidadDeJugadores <= 8) {
                        entradaValida = true;
                        fondoPantalla = new ImageIcon("cartas/fondoPantallaPoker.jpg").getImage();
                    } else {
                        JOptionPane.showMessageDialog(
                                this,
                                "Cantidad inválida. Debe ser entre 2 y 8.",
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

            for (int i = 1; i <= cantidadDeJugadores; i++) {
                String nombre;
                do {
                    nombre = JOptionPane.showInputDialog(this, "Nombre del jugador " + i + ":", "Nombre de Jugador", JOptionPane.QUESTION_MESSAGE);
                    if (nombre == null || nombre.trim().isEmpty()) {
                        JOptionPane.showMessageDialog(this, "Nombre inválido. Intenta de nuevo.");
                    }
                } while (nombre == null || nombre.trim().isEmpty());
                nombresJugadores.add(nombre.trim());
            }

            entradaValida = false;
            // Ahora le pedimos una cantidad de fichas que van a tener todos los jugadores al usuario
            while (!entradaValida) {
                String entrada = JOptionPane.showInputDialog(this, "Introduzca la cantidad de fichas por jugador (debe ser una cantidad entre 500 y 1000):", "Número de fichas", JOptionPane.QUESTION_MESSAGE);
                if (entrada == null) return;

                try {
                    cantidadFichas = Integer.parseInt(entrada);
                    if (cantidadFichas >= 500 && cantidadFichas <= 1000) {
                        entradaValida = true;
                        fondoPantalla = new ImageIcon("cartas/fondoPantallaPoker.jpg").getImage();
                    } else {
                        JOptionPane.showMessageDialog(
                                this,
                                "Cantidad inválida. Debe ser entre 500 y 1000.",
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

            // Ocultamos botones que ya no se usarán mientras se juega
            botonPoker5Hands.setVisible(false);
            botonPoker7CardStud.setVisible(false);

            // Quitamos los componentes actuales (botones, etc.) para poner el panel del póker
            removeAll();
            revalidate();
            repaint();

            // Agregamos ahora el panel dedicado a jugar nuestro Poker de 7 Card Stud
            PanelPoker7CardStud panelPoker7 = new PanelPoker7CardStud(cantidadDeJugadores, cantidadFichas, nombresJugadores,this);
            panelPoker7.setBounds(0, 0, 1000, 600);
            add(panelPoker7);

            revalidate();
            repaint();
        });


        // Adaptamos las imágenes a los botones, removiendo sus esquinas, recuadros que se iluminen al seleccionarlos y volviendolos visibles
        inicializarBotonConImagen(botonPoker5Hands);
        botonPoker5Hands.setBounds(200, 400, 256, 128);

        inicializarBotonConImagen(botonPoker7CardStud);
        botonPoker7CardStud.setBounds(500, 400, 256, 128);

        add(botonPoker5Hands);
        add(botonPoker7CardStud);
    }

    // Esto nos sirve para adaptar el tamaño de la imagen al botón, la cual puede resultar muy grande o pequeña
    private ImageIcon redimensionarImagen(String rutaImagen, int ancho, int alto) {
        ImageIcon original = new ImageIcon(rutaImagen);
        // Usamos este comando para escalar la imagen al ancho y alto seleccionado, procurando que no se vea rara o estirada
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

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(fondoPantalla, 0, 0, getWidth(), getHeight(), this);
    }

}