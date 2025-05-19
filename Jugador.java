import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;

public class Jugador {
    private int noJugador;
    private int fichas;
    private boolean retirado = false;
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

    public boolean estaRetirado() {
        return retirado;
    }

    public void retirarse() {
        this.retirado = true;
    }

    public void tirarCarta(){

    }

    public void obtenerCarta(){

    }

    // No se si ocupas esto, pero es para restar las fichas del jugador
    public void restarFichas(int cantidad) {
        if (cantidad <= fichas) {
            fichas -= cantidad;
        } else {
            // Puedes decidir si lo dejas en 0 o lanzas un error
            System.out.println("No tienes suficientes fichas. Se restan todas las que tienes.");
            fichas = 0;
        }
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


    //NO NECESITO ESTO, DEREK TU SI??
    //modificar este apostar
    public void apostar(int cantidad) {
        if (cantidad <= fichas) {
            fichas -= cantidad;
            System.out.println(noJugador + " apuesta " + cantidad + " fichas. Fichas restantes: " + fichas);
        } else {
            System.out.println(noJugador + " no tiene suficientes fichas para apostar " + cantidad);
        }
    }

}