import java.util.ArrayList;

public class Poker5CardDraw extends Poker {
    private ArrayList<Integer> apuestasRonda;
    private int apuestaActual = 0;
    private int jugadoresQueHicieronCheck = 0, jugadoresQueDescartaron = 0, jugadoresQueHicieronCall = 0;

    public Poker5CardDraw(int cantidadDeJugadores, ArrayList<String> nombresJugadores) {
        super();
        evaluador = new Evaluador5CardDraw();
        jugadores = new ArrayList<>();
        for (int i = 0; i < cantidadDeJugadores; i++) {
            String nombre = nombresJugadores.get(i);
            jugadores.add(new Jugador5CardDraw(nombre,i, 1000)); // Aquí le pasas el nombre
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
    public void incrementarCalls() {
        jugadoresQueHicieronCall++;
    }

    public int getJugadoresQueHicieronCall() {
        return jugadoresQueHicieronCall;
    }

    public void reiniciarCalls() {
        jugadoresQueHicieronCall = 0;
    }

    public void incrementarDescartes() {
        jugadoresQueDescartaron++;
    }

    public int getJugadoresQueDescartaron() {
        return jugadoresQueDescartaron;
    }

    public void reiniciarDescartes() {
        jugadoresQueDescartaron = 0;
    }

    public int getJugadoresActivos() {
        int count = 0;
        for (Jugador jugador : jugadores) {
            if (!jugador.estaRetirado()) {
                count++;
            }
        }
        return count;
    }

    public Jugador getJugadorActivoRestante() {
        for (Jugador j : jugadores) {
            if (!j.estaRetirado()) return j;
        }
        return null;
    }


    public int getApuestaActual() {
        return apuestaActual;
    }

    public void setApuestaActual(int apuesta) {
        this.apuestaActual = apuesta;
    }

    public void jugar() {
        generarBaraja();
        repartirCartas(5,true);
    }
}