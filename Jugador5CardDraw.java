import java.util.ArrayList;
import java.util.Collections;

public class Jugador5CardDraw extends Jugador{
    private ArrayList<Carta> mano = new ArrayList<>();

    public Jugador5CardDraw(int noJugador, int fichas) {
        super(noJugador, fichas);
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


    public void retirarse(){

    }

    public void igualar(ArrayList<Integer> ficha){

    }

    public void pasar(){

    }

    public void subir(ArrayList<Integer> ficha){

    }

}