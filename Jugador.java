import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;

public class Jugador {
    private int noJugador;
    private int fichas;
    private String nombre;
    private ArrayList<Carta> mano = new ArrayList<>();

    public Jugador(int noJugador, int fichas) {
        this.noJugador = noJugador;
        this.fichas = fichas;
    }

    public Jugador(String nombre) {
        this.nombre = nombre;
        this.mano = new ArrayList<>();
        this.fichas = fichas; // Para fines de este codigo, usaremos 1000 fichas como la base
    }

    public void recibirCarta(Carta c) {
        mano.add(c);
    }

    public ArrayList<Carta> getMano() {
        return mano;
    }

    public int getNombre() {
        return noJugador;
    }

    public void tirarCarta(){

    }

    public void obtenerCarta(){

    }

    public int getFichas() {
        return fichas;
    }

    public void setFichas(int fichas) {
        this.fichas = fichas;
    }


    // Muestra solo las cartas visibles (para otras personas)
    public void mostrarCartasVisibles() {
        System.out.print(noJugador + " muestra: ");
        for (Carta c : mano) {
            if (c.isVisible()) {
                System.out.print(c + " ");
            }
        }
        System.out.println();
    }

    protected int obtenerCartaAlta(ArrayList<Carta> mano) {
        int max = 0;
        for (Carta c : mano) {
            if (c.getValor() > max) {
                max = c.getValor();
            }
        }
        return max;
    }

    public void apostar(int cantidad) {
        if (cantidad <= fichas) {
            fichas -= cantidad;
            System.out.println(noJugador + " apuesta " + cantidad + " fichas. Fichas restantes: " + fichas);
        } else {
            System.out.println(noJugador + " no tiene suficientes fichas para apostar " + cantidad);
        }
    }

    public void setMano(ArrayList<Carta> manoActual) {
        this.mano = manoActual;
    }
}