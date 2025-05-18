import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Poker extends EvaluarMano{
    private int poker;
    protected ArrayList<Carta> mazo;
    protected ArrayList<Jugador> jugadores;
    protected ArrayList<Integer> apuestas;
    protected int puntuacion;
    protected EvaluarMano evaluador;

    //checar pq es de chat
    public boolean determinarGanador(){
        // Lógica para decidir el ganador
        // Por ejemplo, comparar las puntuaciones y mostrar el ganador
        int mejorPuntuacion = -1;
        Jugador ganador = null;

        for (Jugador j : jugadores) {
            ArrayList<Carta> mano = j.getMano();
            analizarMano(mano);
            if (puntuacion > mejorPuntuacion) {
                mejorPuntuacion = puntuacion;
                ganador = j;
            }
        }

        if (ganador != null) {
            System.out.println("¡El ganador es: " + ganador.getNombre() + " con puntuación: " + mejorPuntuacion + "!");
            return true;
        }

        return false;
    }

    public String evaluarMano(ArrayList<Carta> mano) {
        int puntuacion = evaluador.analizarMano(mano);
        return evaluador.interpretarPuntuación(puntuacion);
    }

    //reparte N cartas
    public void repartirCartas(int N){
        for (Jugador j : jugadores) {
            for (int i = 0; i < N; i++) {
                j.recibirCarta(mazo.remove(0));
            }
        }
    }

    public List<Jugador> getJugadores() {
        return jugadores;
    }

    public void mostrarManos() {
        int num = 1;
        for (Jugador j : jugadores) {
            System.out.println("Mano del " + j.getNombre() + ":");
            for (Carta c : j.getMano()) {
                System.out.println("- " + c); // Asegúrate de que Carta tenga un buen toString()
            }
            System.out.println(); // Espacio entre jugadores
            num++;
        }
    }

    public void mostrarMano(Jugador jugador) {
        for (int i = 0; i < jugador.getMano().size(); i++) {
            System.out.println(i + ": " + jugador.getMano().get(i));
        }
    }

    public void generarBaraja(){
        mazo= new ArrayList<>();

        String[] figuras = {"corazon", "trebol", "diamante", "pica"};
        for (String figura : figuras) {
            for (int j = 1; j <= 13; j++) {
                mazo.add(new Carta(j, figura, "transparente"));
            }
        }
        Collections.shuffle(mazo);
    }

}