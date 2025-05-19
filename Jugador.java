import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;

public class Jugador {
    private int fichas, noJugador;
    private String nombre;
    private boolean retirado = false, huboCambioDeCartas = false;
    private ArrayList<Carta> mano = new ArrayList<>();

    public Jugador(String nombre,int noJugador, int fichas) {
        this.nombre = nombre;
        this.noJugador= noJugador;
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

    public String getNombre() {
        return nombre;
    }

    public int getNoJugador() {
        return noJugador;
    }

    public boolean getHuboCambioDeCartas() {
        return huboCambioDeCartas;
    }

    public void setHuboCambioDeCartas(boolean huboCambioDeCartas) {
        this.huboCambioDeCartas = huboCambioDeCartas;
    }

    public boolean estaRetirado() {
        return retirado;
    }

    public void retirarse() {
        this.retirado = true;
    }

    public int getFichas() {
        return fichas;
    }

    public void setFichas(int fichas) {
        this.fichas = fichas;
    }

    //Muestra solo las cartas visibles (para otras personas)
    public void mostrarCartasVisibles() {
        System.out.print(nombre + " muestra: ");
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

    //si me sirve qwq
    public void apostar(int cantidad) {
        if (cantidad <= fichas) {
            fichas -= cantidad;
            System.out.println("Jugador"+nombre + " apuesta " + cantidad + " fichas. Fichas restantes: " + fichas);
        }
        else{
            System.out.println("Jugador"+nombre + " no tiene suficientes fichas para apostar " + cantidad);
        }
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

}