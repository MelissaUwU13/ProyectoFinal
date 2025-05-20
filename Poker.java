import java.util.*;

public class Poker extends EvaluadorDeMano {
    protected ArrayList<Carta> mazo;
    protected ArrayList<Jugador> jugadores;
    protected ArrayList<Integer> apuestas;
    protected EvaluadorDeMano evaluador;
    protected int jugadoresQueDescartaron = 0, jugadoresQueIgualaron = 0, jugadoresQueYaJugaron = 0;
    protected int apuestaActual = 0, pozo = 0;
    protected int jugadoresQuePasaronTurno = 0;
    protected Map<Jugador, Integer> puntuaciones = new HashMap<>();

    // Le repartimos cartas a los jugadores con esto
    public void repartirCartas(int N, boolean visible) {
        for (Jugador j : jugadores) {
            for (int i = 0; i < N; i++) {
                Carta c =  mazo.remove(0);
                j.recibirCarta(c, visible);
            }
        }
    }

    // Con esto, generamos el mazo y lo barajeamos
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

    // Con esto, llamamos a la clase que evalúa las manos y lo usamos para revisar las puntuaciones
    public String evaluarMano(ArrayList<Carta> mano) {
        int puntuacion = evaluador.analizarMano(mano);
        return evaluador.interpretarPuntuacion(puntuacion);
    }

    // Con esto, simplemente evaluamos la mano dada, pero no interpretamos su puntuación, solo obtenemos está en enteros
    public int evaluarCartas(ArrayList<Carta> cartas) {
        return analizarMano(cartas);
    }
    // Nos sirve para incrementar el número de jugadores que igualaron la apuesta
    public void incrementarIgualadas() {
        jugadoresQueIgualaron++;
    }
    //Lo usamos para incrementar el número de jugadores que ya jugaron su mano
    public void incrementarJugadas() {
        jugadoresQueYaJugaron++;
    }
    // Esto también lo usamos para incrementar el número de jugadores que hicieron descarte, todas importantes para el panel del 5 Card Draw
    public void incrementarDescartes() {
        jugadoresQueDescartaron++;
    }
    // Esto se usa para cuando se acaba la fase de descarte, para evitar problemas al pasar la fase del descarte
    public void reiniciarDescartes() {
        jugadoresQueDescartaron = 0;
    }

    //con esto reiniciamos los jugadores que le dieron a igualar
    public void reiniciarIgualadas() {
        jugadoresQueIgualaron = 0;
    }

    //obtenemos el valor de los jugadores que le dieron a igualar
    public int getJugadoresQueIgualaron() {
        return jugadoresQueIgualaron;
    }

    //obtenemos los jugadores
    public List<Jugador> getJugadores() {
        return jugadores;
    }

    //obtenemos los jugadores que ya jugaron su turno
    public int getJugadoresQueYaJugaron() {
        return jugadoresQueYaJugaron;
    }

    //obtenemos los jugadores que descataron
    public int getJugadoresQueDescartaron() {
        return jugadoresQueDescartaron;
    }

    //obtenemos cuantos jugadores aun siguen jugando
    public int getJugadoresActivos() {
        int count = 0;
        for (Jugador jugador : jugadores) {
            if (!jugador.getRetirado()) {
                count++;
            }
        }
        return count;
    }

    // Con esto, agregamos una cantidad de fichas al pozo
    public void agregarAlPozo(int cantidad) {
        this.pozo += cantidad;
    }

    //nos devuelve el valor del pozo
    public int getPozo(){
        return pozo;
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

    // nos devuelve los jugadores que pasaron turno
    public int getJugadoresQuePasaronTurno() {
        return jugadoresQuePasaronTurno;
    }

    //nos devuelve el jugador activo que queda
    public Jugador getJugadorActivoRestante() {
        for (Jugador j : jugadores) {
            if (!j.getRetirado()) return j;
        }
        return null;
    }
}