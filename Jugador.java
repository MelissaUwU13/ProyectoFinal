import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;

public class Jugador {
    private int noJugador;
    private int fichas;
    private ArrayList<Carta> mano = new ArrayList<>();

    public Jugador(int noJugador, int fichas) {
        this.noJugador = noJugador;
        this.mano = new ArrayList<>();
        this.fichas = fichas;
    }

    public void recibirCarta(Carta c, boolean visible) {
        c.setVisible(visible);
        mano.add(c);
    }

    public ArrayList<Carta> getMano() {
        return mano;
    }

    public int getNoJugador() {
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

    public void setMano(ArrayList<Carta> manoActual) {
        this.mano = manoActual;
    }
}