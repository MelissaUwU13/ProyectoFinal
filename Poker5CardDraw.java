import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Poker5CardDraw extends Poker {
    private ArrayList<Integer> apuestasRonda;
    private int apuestaActual = 0, pozo = 0;
    private int jugadoresQueHicieronCheck = 0;
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

    public void imprimirFichasJugadores() {
        for (int i = 0; i < jugadores.size(); i++) {
            /* El parentesis es para algo llamado "casting", en este caso, le indicamos a Java que queremos obtener
               los metodos y atributos de Jugador5CardDraw, y esto para verificar que todos los jugadores tengan la cantidad de fichas correctas
             */
            Jugador5CardDraw jugador =  (Jugador5CardDraw) jugadores.get(i);
            System.out.println("Jugador " + (i + 1) + " tiene " + jugador.getFichas() + " fichas.");
        }
    }

    public Carta sacarCarta() {
        if (!mazo.isEmpty()) {
            return mazo.remove(0); // Saca la primera carta (el mazo debe estar barajado)
        } else {
            throw new IllegalStateException("El mazo está vacío");
        }
    }
    public ArrayList<Carta> getMazo() {
        return mazo;
    }

    public void incrementarChecks() {
        jugadoresQueHicieronCheck++;
    }

    public int getJugadoresQueHicieronCheck() {
        return jugadoresQueHicieronCheck;
    }

    public void reiniciarChecks() {
        jugadoresQueHicieronCheck = 0;
    }

    public void reiniciarPartida() {
        // Limpiar y reiniciar variables del juego
        apuestaActual = 0;
        pozo = 0;
        jugadoresQueHicieronCheck = 0;
        jugadoresQueDescartaron = 0;
        jugadoresQueHicieronCall = 0;
        jugadoresQueYaJugaron = 0;
        puntuaciones.clear();

        // Reiniciar estado de cada jugador
        for (Jugador jugador : jugadores) {
            jugador.setRetirado(false);
            jugador.getMano().clear();  // limpia la mano actual
            System.out.println("Reiniciando estado de " + jugador.getNombre());
            System.out.println("¿Está retirado?: " + jugador.estaRetirado());
        }
        System.out.println("Mano del jugador b (desde lista): " + jugadores.get(1).getMano());
        System.out.println("¿Retirado b desde lista?: " + jugadores.get(1).estaRetirado());

        // Volver a barajar y repartir nuevas cartas
        generarBaraja();
        repartirCartas(5, true);
    }

    public void reiniciarCalls() {
        jugadoresQueHicieronCall = 0;
    }

    public void guardarPuntuacion(Jugador jugador) {
        int puntaje = evaluador.analizarMano(jugador.getMano());
        puntuaciones.put(jugador, puntaje);
    }

    public Jugador compararPuntuaciones() {
        Jugador ganador = null;
        int mejorPuntuacion = -1;

        // Primero encontrar al ganador
        for (Map.Entry<Jugador, Integer> entry : puntuaciones.entrySet()) {
            Jugador j = entry.getKey();
            int puntaje = entry.getValue();

            if (puntaje > mejorPuntuacion && !j.estaRetirado()) {
                mejorPuntuacion = puntaje;
                ganador = j;
            }
        }

        if (ganador != null) {
            // Sumar el pozo al ganador
            ganador.sumarFichas(pozo);
            System.out.println(ganador.getNombre() + " gana " + pozo + " fichas del pozo");

            // Resetear el pozo después de distribuirlo
            reiniciarPozo();
        }

        puntuaciones.clear();
        return ganador;
    }


    public Jugador getJugadorActivoRestante() {
        for (Jugador j : jugadores) {
            if (!j.estaRetirado()) return j;
        }
        return null;
    }

    public void agregarAlPozo(int cantidad) {
        this.pozo += cantidad;
    }

    public void reiniciarPozo() {
        pozo = 0;
    }

    public int getPozo() {
        return pozo;
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
    public void jugar() {
        generarBaraja();
        repartirCartas(5,true);
    }
}