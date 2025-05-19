import java.util.ArrayList;
import java.util.Collections;

public class Jugador5CardDraw extends Jugador{
    private boolean retirado = false;
    private ArrayList<Carta> mano = new ArrayList<>();
    private int apuestaActual = 0;
    private int fichas;

    public Jugador5CardDraw(int noJugador, int fichas) {
        super(noJugador, fichas);
    }


    public boolean estaRetirado() {
        return retirado;
    }

    public void retirarse() {
        this.retirado = true;
    }

    public void tirarCartas(ArrayList<Integer> indices) {
        // Ordenar índices descendente para eliminar sin romper orden
        indices.sort(Collections.reverseOrder());
        for (int i : indices) {
            if (i >= 0 && i < mano.size()) {
                mano.remove(i);
            }
        }
    }

    public void setApuestaActual(int apuestaActual) {
        this.apuestaActual = apuestaActual;
    }
}