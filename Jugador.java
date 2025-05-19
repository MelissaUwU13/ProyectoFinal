import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;

public class Jugador {
    private int noJugador;
    private int fichas;
    private boolean retirado = false, huboCambioDeCartas = false;
    private ArrayList<Carta> mano = new ArrayList<>();
    String nombre;

    public Jugador(int noJugador, int fichas) {
        this.noJugador = noJugador;
        this.mano = new ArrayList<>();
        this.fichas = fichas;
    }

    public Jugador(String nombre, int fichas) {
       this.nombre = nombre;
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
    public String getNombre() {
        return nombre;
    }
    public boolean getHuboCambioDeCartas() {
        return huboCambioDeCartas;
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

    public void setHuboCambioDeCartas(boolean huboCambioDeCartas) {
        this.huboCambioDeCartas = huboCambioDeCartas;
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


    //NO NECESITO ESTO, DEREK TU SI?? ahora chi uwu
    // Modificar este apostar
    public void apostar(int cantidad) {
        if (cantidad <= fichas) {
            fichas -= cantidad;
            System.out.println("Fichas restantes: " + fichas);
        } else {
            System.out.println(noJugador + " no tiene suficientes fichas para apostar " + cantidad);
            fichas = 0;
        }
    }

}