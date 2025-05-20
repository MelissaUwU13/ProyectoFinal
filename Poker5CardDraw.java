import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Poker5CardDraw extends Poker {
    private ArrayList<Integer> apuestasRonda;
    private int apuestaActual = 0, pozo = 0;
    private int jugadoresQuePasaronTurno = 0;
    private Map<Jugador, Integer> puntuaciones = new HashMap<>();

    public Poker5CardDraw(int cantidadDeJugadores, ArrayList<String> nombresJugadores, int fichas) {
        super();
        evaluador = new Evaluador5CardDraw();
        jugadores = new ArrayList<>();
        for (int i = 0; i < cantidadDeJugadores; i++) {
            String nombre = nombresJugadores.get(i);
            jugadores.add(new Jugador5CardDraw(nombre,i, fichas)); // Aquí le pasas el nombre
        }
        jugar();
    }

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

    // Con esto, agregamos una cantidad de fichas al pozo
    public void agregarAlPozo(int cantidad) {
        this.pozo += cantidad;
    }

    // Esto vuelve a poner la cantidad de fichas en el pozo a 0
    public void reiniciarPozo() {
        pozo = 0;
    }
    // Con esto reiniciamos el número de veces que los jugadores pasaron turno
    public void reiniciarPases() {
        jugadoresQuePasaronTurno = 0;
    }

    // Con esto incrementamos el contador de los jugadores que pasaron turno, lo cual nos sirve para pasar de la fase de apuestas al descarte
    public void incrementarPases() {
        jugadoresQuePasaronTurno++;
    }

    // Getters y setters
    public int getJugadoresQuePasaronTurno() {
        return jugadoresQuePasaronTurno;
    }

    public Jugador getJugadorActivoRestante() {
        for (Jugador j : jugadores) {
            if (!j.getRetirado()) return j;
        }
        return null;
    }

    public int getApuestaActual() {
        return apuestaActual;
    }

    public void setApuestaActual(int apuesta) {
        this.apuestaActual = apuesta;
    }

    public void setJugadoresQueYaJugaron(int jugador) {
        this.jugadoresQueYaJugaron = jugador;
    }

}