import java.util.ArrayList;

public class Jugador {
    private String nombre;
    private int fichas;
    private ArrayList<Carta> mano = new ArrayList<>();

    public Jugador(String nombre, int fichas) {
        this.nombre = nombre;
        this.fichas = fichas;
    }

    public void recibirCarta(Carta c) {
        mano.add(c);
    }

    public ArrayList<Carta> getMano() {
        return mano;
    }

    public String getNombre() {
        return nombre;
    }

    public void tirarCarta(){

    }

    public void obtenerCarta(){

    }

    public void apostar(){

    }

}
