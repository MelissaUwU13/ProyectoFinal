import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
public class Poker5CardDraw extends Poker{
    public Poker5CardDraw(){
        super();

        Scanner sc = new Scanner(System.in);
        int cantidadDeJugadores = 0;
        boolean error=false;

        // Creamos un ciclo en el que haremos que el usuario defina la cantidad de jugadores
        while(!error) {
            System.out.println("Ingrese la cantidad de jugadores: ");
            try { // Usamos un try para evitar que el usuario ponga datos no permitidos
                cantidadDeJugadores = Integer.parseInt(sc.nextLine());
                if (cantidadDeJugadores < 2 || cantidadDeJugadores > 7) {
                    System.out.println("Cantidad erronea, este juego necesita entre 2 a 7 jugadores");
                }
                else {
                    error = true;
                }
            } catch (Exception e) {
                System.out.println("Error: Entrada no valida");
            }
        }

        // Creamos la cantidad de jugadores dada por el usuario
        jugadores = new ArrayList<>();
        for (int i = 1; i <= cantidadDeJugadores; i++) {
            jugadores.add(new Jugador5CardDraw( i, 1000));
        }

        generarBaraja();
        // Repartimos 5 cartas para cada jugador debido a las reglas del juego
        repartirCartas(5);
        mostrarManos();
    }

    public Poker5CardDraw(int cantJugadores) {
        super();
        jugadores = new ArrayList<>();
        for (int i = 1; i <= cantJugadores; i++) {
            jugadores.add(new Jugador5CardDraw(  i, 1000));
        }
        generarBaraja();
        repartirCartas(5);
    }

}