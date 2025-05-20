import java.util.*;

//clase poker 7 que hereda de poker
public class Poker7CardStud extends Poker{
    private ArrayList<Integer> apuestasRonda;
    private int apuestaActual=5, pozo = 0, jugadoresQuePasaronTurno = 0;
    private Map<Jugador, Integer> puntuaciones = new HashMap<>();

    //Constructor de clase Poker7 que revise los jugadores, nombres y cantidad de fichas
    public Poker7CardStud(int cantJugadores, int fichasTotales, ArrayList<String> nombresJugadores) {
        super();
        jugadores = new ArrayList<>();
        evaluador = new Evaluador7CardStud();
        this.apuestasRonda = new ArrayList<>();
        this.mazo= generarBaraja();
        fichasTotales=fichasTotales-5;

        for (int i = 0; i < cantJugadores; i++) {
            String nombre = nombresJugadores.get(i);
            jugadores.add(new Jugador7CardStud(nombre,i+1, fichasTotales));// ejemplo fichas iniciales
            apuestasRonda.add(0);
        }
    }

    //Reparte n cantidad de cartas a los jugadores, tambien revisa si son cartas suficientes
    public void repartirNCartas(int N, boolean visible) {
        int totalCartas = N * jugadores.size();
        if (mazo.size() < totalCartas) {
            System.out.println("No hay suficientes cartas en el mazo para repartir.");
        }
        else {
            for (Jugador j : jugadores) {
                if (j instanceof Jugador7CardStud) {
                    Jugador7CardStud js = (Jugador7CardStud) j;
                    for (int i = 0; i < N; i++) {
                        Carta c = mazo.remove(0);
                        js.recibirCarta(c, visible);
                    }
                }
            }
        }
    }

    //obtener el mejor de cada ronda de todas las cartas segun sus cartas visibles
    public int obtenerNUMEROJugadorInicial(int ronda) {
        int mejor = 0;
        int mejorPuntuacion = -1;

        for (Jugador j : jugadores) {
            Jugador7CardStud jugador = (Jugador7CardStud) j; // cast aquí

            ArrayList<Carta> visibles = jugador.getCartasVisibles(ronda);
            int puntuacion = evaluarCartas(visibles); // tú debes definir esta función según reglas de póker
            if (puntuacion > mejorPuntuacion) {
                mejorPuntuacion = puntuacion;
                mejor = jugador.getNoJugador();
            }
            //en caso de empate....
            else if (puntuacion == mejorPuntuacion) {
                //gana jugador de menor número
                if (j.getNoJugador() < mejor) {
                    mejor = jugador.getNoJugador();
                }
            }
        }

        return mejor;
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