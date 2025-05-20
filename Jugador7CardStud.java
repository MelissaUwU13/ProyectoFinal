import java.util.ArrayList;
import java.util.Scanner;

public class Jugador7CardStud extends Jugador {
boolean activo;

public Jugador7CardStud(String nombre,int noJugador, int fichas) {
    super(nombre,noJugador, fichas);
    this.activo = true;  // empieza activo
}

public boolean esActivo() {
    return activo;
}

public void setActivo(boolean activo) {
    this.activo = activo;
}

// Modifica el metodo pasar para cambiar el estado
public void pasar() {
    this.activo = false;
    System.out.println("Jugador " + getNombre() + " se retira del juego.");
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
    int apuestaActual = apuestasRonda.get(getNoJugador() - 1); // índice 0-based
    int diferencia = apuestaMaxima - apuestaActual;
    if (diferencia > 0 && getFichas() >= diferencia) {
        apostar(diferencia);
        apuestasRonda.set(getNoJugador() - 1, apuestaMaxima);
        System.out.println("Jugador " + getNombre() + " iguala con " + diferencia + " fichas.");
    }
    else {
        System.out.println("Jugador " + getNombre() + " no puede igualar.");
    }
}


//BYE BYE?!
public void subir(int nuevaApuesta, ArrayList<Integer> apuestasRonda) {
    int actual = apuestasRonda.get(getNoJugador() - 1);
    int diferencia = nuevaApuesta - actual;

    if (diferencia > 0 && getFichas() >= diferencia) {
        apostar(diferencia);
        apuestasRonda.set(getNoJugador() - 1, nuevaApuesta);
        System.out.println("Jugador " + getNombre() + " sube la apuesta a " + nuevaApuesta + " fichas.");
    } else {
        System.out.println("No puedes subir, ficha insuficiente o apuesta inválida.");
    }
}




}
