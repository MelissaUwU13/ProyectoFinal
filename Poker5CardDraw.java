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
            jugadores.add(new Jugador5CardDraw("Jugador " + i, 1000));
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
            jugadores.add(new Jugador5CardDraw("Jugador " + i, 1000));
        }
        generarBaraja();
        repartirCartas(5);
    }

    public void jugarTurnoConsola(int jugadorIndex) {
        Scanner sc = new Scanner(System.in);
        Jugador5CardDraw jugador = (Jugador5CardDraw) jugadores.get(jugadorIndex);

        System.out.println("Mano actual del " + jugador.getNombre() + ":");
        mostrarMano(jugador);

        // Evaluar la mano
        tablaDePuntuaciones(jugador.getMano());
        analizarMano(jugador.getMano());
        System.out.println("Puntuación: " + puntuacion);
    }


    public void jugarCartas(int jugadorIndex, ArrayList<Integer> indicesATirar) {
        Jugador5CardDraw jugador = (Jugador5CardDraw) jugadores.get(jugadorIndex);

        // El jugador tira las cartas seleccionadas
        jugador.tirarCartas(indicesATirar);

        // Repartir cartas nuevas para reponer hasta 5 cartas
        int cartasFaltantes = 5 - jugador.getMano().size();
        for (int i = 0; i < cartasFaltantes; i++) {
            if (!mazo.isEmpty()) {
                jugador.recibirCarta(mazo.remove(0));
            }
        }

        // Evaluar la mano actualizada usando la tabla de puntuaciones
        tablaDePuntuaciones(jugador.getMano());
        System.out.println("Puntuación del jugador " + jugador.getNombre() + ": " + puntuacion);
    }
}
