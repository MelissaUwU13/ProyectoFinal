import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.io.File;

public class PanelJuegos extends JPanel {
    private Image fondoPantalla;
    private Clip musica;

    public PanelJuegos() {
        setPreferredSize(new Dimension(1000, 600));
        setLayout(null);
        setBackground(Color.WHITE);
        setFocusable(true);

        ImageIcon imagenBoton5CardPoker = redimensionarImagen("cartas/boton5CardPoker.png", 256, 128);
        ImageIcon imagenBoton7CardStud = redimensionarImagen("cartas/boton7CardStud.png", 256, 128);

        reproducirMusica("musica.wav");

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

            // Ocultamos botones que ya no se usarán mientras se juega
            botonPoker5Hands.setVisible(false);
            botonPoker7CardStud.setVisible(false);

            // Quitamos los componentes actuales (botones, etc) para poner el panel del póker
            removeAll();
            revalidate();
            repaint();

            // Agregar el panel especializado para jugar 5 Card Draw (el que tú creaste)
            PanelPoker5CardDraw panelPoker5 = new PanelPoker5CardDraw(cantidadDeJugadores);
            panelPoker5.setBounds(0, 0, 1000, 600);
            add(panelPoker5);

            revalidate();
            repaint();
        });

        botonPoker7CardStud.addActionListener(e -> {
            // Aquí puedes mantener o añadir lógica para 7 Card Stud sin afectar el resto
            System.out.println("7 Card Stud no implementado aún");
        });

        inicializarBotonConImagen(botonPoker5Hands);
        botonPoker5Hands.setBounds(200, 400, 256, 128);

        inicializarBotonConImagen(botonPoker7CardStud);
        botonPoker7CardStud.setBounds(500, 400, 256, 128);

        add(botonPoker5Hands);
        add(botonPoker7CardStud);
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
            AudioInputStream audio = AudioSystem.getAudioInputStream(new File(rutaArchivo));
            musica = AudioSystem.getClip();
            musica.stop();
            musica.open(audio);
            musica.loop(Clip.LOOP_CONTINUOUSLY);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}