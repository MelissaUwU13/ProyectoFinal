import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
public class Poker5CardDraw extends Poker {
    private ArrayList<Integer> apuestasRonda;

    public Poker5CardDraw(){
        super();
        evaluador = new Evaluador5CardDraw();
        Scanner sc = new Scanner(System.in);
        int cantidadDeJugadores = 0;
        boolean error=false, ganador=false;

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
            }
            catch (Exception e) {
                System.out.println("Error: Entrada no valida");
            }
        }

        error=false;
        int fichasTotales=0;

        while(error=false) {
            System.out.println("Con cuantas fichas quieres iniciar para cada jugador? MAX 50");
            try {
                fichasTotales = sc.nextInt();
                if (fichasTotales < 0 || fichasTotales > 50) {
                    System.out.println("Cantidad erronea, vuelvelo a intentar ");
                    error = false;
                } else {
                    error = true;
                }
            }
            catch (Exception e) {
            System.out.println("Error: Entrada no valida");
        }

        }

        // Creamos la cantidad de jugadores dada por el usuario
        this.jugadores = new ArrayList<>();
        this.apuestasRonda = new ArrayList<>();
        for (int i = 1; i <= cantidadDeJugadores; i++) {
            super.jugadores.add(new Jugador5CardDraw(i, fichasTotales));
        }

        generarBaraja();
        // Repartimos 5 cartas para cada jugador debido a las reglas del juego
        repartirCartas(5,true);
    }

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


    public void jugar() {
        generarBaraja();
        repartirCartas(5,true);
    }
}