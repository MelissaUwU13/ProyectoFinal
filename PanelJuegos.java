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
        setPreferredSize(new Dimension(1000, 600));
        setLayout(null);
        setBackground(Color.WHITE);
        setFocusable(true);

        ImageIcon imagenBoton5CardPoker = redimensionarImagen("cartas/boton5CardPoker.png", 256, 128);
        ImageIcon imagenBoton7CardStud = redimensionarImagen("cartas/boton7CardStud.png", 256, 128);
        reproducirMusica("cartas/musica.wav");

        // Fondo de la pantalla
        fondoPantalla = new ImageIcon("cartas/Portada.png").getImage();

        JButton botonPoker5Hands = new JButton(imagenBoton5CardPoker);
        JButton botonPoker7CardStud = new JButton(imagenBoton7CardStud);
        botonPoker5Hands.addActionListener(e -> {
            int cantidadDeJugadores = 0;
            boolean entradaValida = false;

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

            // Ocultamos botones que ya no se usarán mientras se juega
            botonPoker5Hands.setVisible(false);
            botonPoker7CardStud.setVisible(false);

            // Quitamos los componentes actuales (botones, etc) para poner el panel del póker
            removeAll();
            revalidate();
            repaint();

            // Agregar el panel especializado para jugar 5 Card Draw
            PanelPoker5CardDraw panelPoker5 = new PanelPoker5CardDraw(cantidadDeJugadores, nombresJugadores, this);
            panelPoker5.setBounds(0, 0, 1000, 600);
            add(panelPoker5);

            revalidate();
            repaint();
        });





        botonPoker7CardStud.addActionListener(e -> {
            // Aquí puedes mantener o añadir lógica para 7 Card Stud sin afectar el resto
            System.out.println("7 Card Stud no implementado aún");
            int cantidadDeJugadores = 0;
            int cantidadFichas = 0;
            boolean entradaValida = false;

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

            while (!entradaValida) {
                String entrada = JOptionPane.showInputDialog(this, "Introduzca la cantidad de fichas (500-1000)", "Número de fichas", JOptionPane.QUESTION_MESSAGE);
                if (entrada == null) return;

                try {
                    cantidadDeJugadores = Integer.parseInt(entrada);
                    if (cantidadDeJugadores >= 500 && cantidadDeJugadores <= 1000) {
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

            // Quitamos los componentes actuales (botones, etc) para poner el panel del póker
            removeAll();
            revalidate();
            repaint();

            // Agregar el panel especializado para jugar 5 Card Draw
            PanelPoker7CardStud panelPoker7 = new PanelPoker7CardStud(cantidadDeJugadores, nombresJugadores, cantidadFichas);
            panelPoker7.setBounds(0, 0, 1000, 600);
            add(panelPoker7);

            revalidate();
            repaint();
        });






        inicializarBotonConImagen(botonPoker5Hands);
        botonPoker5Hands.setBounds(200, 400, 256, 128);

        inicializarBotonConImagen(botonPoker7CardStud);
        botonPoker7CardStud.setBounds(500, 400, 256, 128);

        add(botonPoker5Hands);
        add(botonPoker7CardStud);
    }

    public void regresarAlMenuPrincipal() {
        removeAll(); // Quitar panel de juego actual

        // Restaurar fondo
        fondoPantalla = new ImageIcon("cartas/Portada.png").getImage();

        // Restaurar botones
        ImageIcon imagenBoton5CardPoker = redimensionarImagen("cartas/boton5CardPoker.png", 256, 128);
        ImageIcon imagenBoton7CardStud = redimensionarImagen("cartas/boton7CardStud.png", 256, 128);

        JButton botonPoker5Hands = new JButton(imagenBoton5CardPoker);
        JButton botonPoker7CardStud = new JButton(imagenBoton7CardStud);

        botonPoker5Hands.setBounds(200, 400, 256, 128);
        botonPoker7CardStud.setBounds(500, 400, 256, 128);

        inicializarBotonConImagen(botonPoker5Hands);
        inicializarBotonConImagen(botonPoker7CardStud);

        // Reagregar listeners si es necesario, o puedes mover los botones originales a atributos de clase si prefieres.
        add(botonPoker5Hands);
        add(botonPoker7CardStud);

        revalidate();
        repaint();
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

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(fondoPantalla, 0, 0, getWidth(), getHeight(), this);
    }


    private void reproducirMusica(String rutaArchivo) {
        try {
            if (musica != null) {
                if (musica.isRunning()) {
                    musica.stop();  // Para la música si está sonando
                }
                musica.close();  // Libera recursos del clip anterior
            }
            AudioInputStream audio = AudioSystem.getAudioInputStream(new File(rutaArchivo));
            musica = AudioSystem.getClip();
            musica.open(audio);
            musica.loop(Clip.LOOP_CONTINUOUSLY);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}