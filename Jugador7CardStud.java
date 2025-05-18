import java.util.ArrayList;
import java.util.Scanner;

public class Jugador7CardStud extends Jugador {

    public Jugador7CardStud(int noJugador, int fichas) {
        super(noJugador, fichas);
    }

    public void completar(int apuestaMinima) {
        Scanner sc = new Scanner(System.in);
        int cantidad;

        do {
            System.out.println("Debes apostar al menos " + apuestaMinima + " fichas. Ingresa la cantidad a apostar:");
            cantidad = sc.nextInt();

            if (cantidad < apuestaMinima) {
                System.out.println("La apuesta es muy baja, debe ser igual o mayor a " + apuestaMinima);
            } else if (cantidad > this.getFichas()) {
                System.out.println("No tienes suficientes fichas.");
            }
        } while (cantidad < apuestaMinima || cantidad > this.getFichas());

        this.apostar(cantidad);
    }

    public void igualar(int apuestaMaxima, ArrayList<Integer> apuestasRonda) {
        int apuestaActual = apuestasRonda.get(getNombre() - 1); // índice 0-based
        int diferencia = apuestaMaxima - apuestaActual;
        if (diferencia > 0 && getFichas() >= diferencia) {
            apostar(diferencia);
            apuestasRonda.set(getNombre() - 1, apuestaMaxima);
            System.out.println("Jugador " + getNombre() + " iguala con " + diferencia + " fichas.");
        } else {
            System.out.println("Jugador " + getNombre() + " no puede igualar.");
        }
    }

    public void subir(int nuevaApuesta, ArrayList<Integer> apuestasRonda) {
        int apuestaActual = apuestasRonda.get(getNombre() - 1);
        int diferencia = nuevaApuesta - apuestaActual;
        if (diferencia > 0 && getFichas() >= diferencia) {
            apostar(diferencia);
            apuestasRonda.set(getNombre() - 1, nuevaApuesta);
            System.out.println("Jugador " + getNombre() + " sube a " + nuevaApuesta + " fichas.");
        } else {
            System.out.println("No puedes subir con esa cantidad.");
        }
    }

    public void pasar(ArrayList<Jugador7CardStud> jugadoresActivos) {
        jugadoresActivos.removeIf(j -> j.getNombre() == getNombre());
        System.out.println("Jugador " + getNombre() + " se retira de la ronda.");
    }
}
