import java.util.ArrayList;

public class Poker5CardDraw extends Poker {
    private ArrayList<Integer> apuestasRonda;
    private int apuestaActual = 0;
    private int jugadoresQueHicieronCheck = 0;
    private int jugadoresQueHicieronCall = 0;

    public Poker5CardDraw(int cantJugadores) {
        super();
        evaluador = new Evaluador5CardDraw();
        jugadores = new ArrayList<>();
        for (int i = 1; i <= cantJugadores; i++) {
            jugadores.add(new Jugador5CardDraw( i, 1000));
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

    public int getJugadoresActivos() {
        int activos = 0;
        for (Jugador j : jugadores) {
            if (!((Jugador5CardDraw) j).estaRetirado()) {
                activos++;
            }
        }
        return activos;
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