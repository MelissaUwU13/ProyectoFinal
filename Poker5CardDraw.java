import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

//clase poker5 que hereda de poker
public class Poker5CardDraw extends Poker {
    private ArrayList<Integer> apuestasRonda;
    private Map<Jugador, Integer> puntuaciones = new HashMap<>();

    //Constructor de clase Poker5 que revise los jugadores, nombres y cantidad de fichas
    public Poker5CardDraw(int cantidadDeJugadores, ArrayList<String> nombresJugadores, int fichas) {
        super();
        jugadores = new ArrayList<>();
        evaluador = new Evaluador5CardDraw();
        for (int i = 0; i < cantidadDeJugadores; i++) {
            String nombre = nombresJugadores.get(i);
            jugadores.add(new Jugador5CardDraw(nombre,i, fichas)); // Aquí le pasas el nombre
        }
        jugar();
    }

    //repartir cartas y barajear
    public void jugar() {
        generarBaraja();
        repartirCartas(5,true);
    }

    // Con esto, sacamos la primera carta del mazo
    public Carta sacarCarta() {
        if (!mazo.isEmpty()) {
            return mazo.remove(0); // Saca la primera carta (el mazo debe estar barajado)
        } else {
            throw new IllegalStateException("El mazo está vacío");
        }
    }

    // Aquí guardamos la puntuación del jugador en un hashmap
    public void guardarPuntuacion(Jugador jugador) {
        int puntaje = evaluador.analizarMano(jugador.getMano());
        puntuaciones.put(jugador, puntaje);
    }

    // Aquí revisamos la puntuación del jugador sacando este valor del hashmap en donde guardamos su nombre y puntaje
    public Jugador compararPuntuaciones() {
        Jugador ganador = null;
        int mejorPuntuacion = -1;

        // Primero encontramos al ganador
        for (Map.Entry<Jugador, Integer> entry : puntuaciones.entrySet()) {
            Jugador j = entry.getKey();
            int puntaje = entry.getValue();

            if (puntaje > mejorPuntuacion && !j.getRetirado()) {
                mejorPuntuacion = puntaje;
                ganador = j;
            }
        }

        // Si hubo un ganador, le damos las fichas del pozo a el
        if (ganador != null) {
            ganador.sumarFichas(pozo);

            // Reseteamos el pozo después de distribuirlo
            reiniciarPozo();
        }

        puntuaciones.clear();
        return ganador;
    }

    //nos devuelve la apuesta actual
    public int getApuestaActual() {
        return apuestaActual;
    }

    //cambiamos la apuesta actual
    public void setApuestaActual(int apuesta) {
        this.apuestaActual = apuesta;
    }

    //nos devuelve los jugadores que ya jugaron
    public void setJugadoresQueYaJugaron(int jugador) {
        this.jugadoresQueYaJugaron = jugador;
    }
}