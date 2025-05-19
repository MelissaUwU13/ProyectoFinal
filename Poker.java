import java.util.*;

public class Poker extends EvaluarMano{
    private int poker;
    protected ArrayList<Carta> mazo;
    protected ArrayList<Jugador> jugadores;
    protected ArrayList<Integer> apuestas;
    protected EvaluarMano evaluador;


    public String evaluarMano(ArrayList<Carta> mano) {
        int puntuacion = evaluador.analizarMano(mano);
        return evaluador.interpretarPuntuación(puntuacion);
    }

    public void repartirCartas(int N, boolean visible) {
        for (Jugador j : jugadores) {
            for (int i = 0; i < N; i++) {
                Carta c = mazo.remove(0);
                j.recibirCarta(c, visible);
            }
        }
    }

    public List<Jugador> getJugadores() {
        return jugadores;
    }

    public ArrayList<Carta> generarBaraja(){
        mazo= new ArrayList<>();

        String[] figuras = {"corazon", "trebol", "diamante", "pica"};
        for (String figura : figuras) {
            for (int j = 1; j <= 13; j++) {
                mazo.add(new Carta(j, figura, true));
            }
        }
        Collections.shuffle(mazo);

        return mazo;
    }

    public int evaluarCartas(ArrayList<Carta> cartas) {
        return analizarMano(cartas); // Usa su propio metodo interno
    }

    public void mostrarManos() {
        int num = 1;
        for (Jugador j : jugadores) {
            System.out.println("Mano del " + j.getNoJugador() + ":");
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
}