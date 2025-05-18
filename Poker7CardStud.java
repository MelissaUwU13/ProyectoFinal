import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class Poker7CardStud extends Poker {
    private int cantJugadores;
    private ArrayList<Jugador7CardStud> jugadores;
    private ArrayList<Integer> apuestasRonda;

    public Poker7CardStud(int cantJugadores) {
        this.cantJugadores = cantJugadores;
        this.jugadores = new ArrayList<>();
        this.apuestasRonda = new ArrayList<>();
        for (int i = 1; i <= cantJugadores; i++) {
            jugadores.add(new Jugador7CardStud(i, 100));  // ejemplo fichas iniciales
            apuestasRonda.add(0);
        }
    }

    public void turnoCalles(int ronda){
        switch (ronda){
            //Third Street
            case 1:
                //Aqui se aplicaria un for donde revise las cartas de todos los jugadores para comprobar quien empieza
                // if(//aqui iria carta mas baja){
                //solo se revisan las cartas visibles

                //}
                //Fourth Street
            case 2:
                //Aqui se aplicaria un for donde revise las cartas de todos los jugadores para comprobar quien empieza


                //Fifth Street
            case 3:

                //Sixth Street
            case 4:
                //Aqui se aplicaria un for donde revise las cartas de todos los jugadores para comprobar quien empieza

                //Seventh Street
            case 5:
                //Aqui se aplicaria un for donde revise las cartas de todos los jugadores para comprobar quien empieza

                //Fase final
            case 6:
                //Aqui se aplicaria un for donde revise las cartas de todos los jugadores para comprobar quien empieza

        }
    }

    public void calles(int ronda) {
        switch (ronda) {
            case 1:
                repartirCartas(2); // por ejemplo 2 cartas iniciales
                turnoCalles(1);
                faseApuestas(5);  // apuesta mínima (bring-in)
                break;
            case 2:
                repartirCartas(1);
                turnoCalles(2);
                faseApuestas(0);
                break;
            case 3:
                repartirCartas(1);
                turnoCalles(3);
                faseApuestas(0);
                break;
            case 4:
                repartirCartas(1);
                turnoCalles(4);
                faseApuestas(0);
                break;
            case 5:
                repartirCartas(1);
                turnoCalles(5);
                faseApuestas(0);
                break;
            default:
                System.out.println("Ronda inválida");
        }
    }

    public void faseApuestas(int apuestaMinima) {
        Scanner sc = new Scanner(System.in);
        ArrayList<Jugador7CardStud> jugadoresActivos = new ArrayList<>(jugadores);

        // Reiniciar apuestasRonda para jugadores activos
        for (int i = 0; i < apuestasRonda.size(); i++) {
            apuestasRonda.set(i, 0);
        }

        int apuestaMaxima = apuestaMinima;
        boolean apuestasPendientes = true;

        while (apuestasPendientes && jugadoresActivos.size() > 1) {
            apuestasPendientes = false;

            for (Jugador7CardStud jugador : new ArrayList<>(jugadoresActivos)) {
                System.out.println("\nJugador " + jugador.getNombre() + ", tu turno.");
                System.out.println("Apuesta máxima actual: " + apuestaMaxima);
                System.out.println("Tus fichas: " + jugador.getFichas());
                System.out.println("Opciones:");
                System.out.println("1. Pasar");
                System.out.println("2. Igualar");
                System.out.println("3. Subir");
                if (apuestaMinima > 0) {
                    System.out.println("4. Completar (bring-in)");
                }

                int opcion = sc.nextInt();

                switch (opcion) {
                    case 1:
                        jugador.pasar(jugadoresActivos);
                        break;
                    case 2:
                        jugador.igualar(apuestaMaxima, apuestasRonda);
                        break;
                    case 3:
                        System.out.println("Ingresa la nueva apuesta (debe ser mayor que " + apuestaMaxima + "):");
                        int nuevaApuesta = sc.nextInt();
                        jugador.subir(nuevaApuesta, apuestasRonda);
                        if (nuevaApuesta > apuestaMaxima) {
                            apuestaMaxima = nuevaApuesta;
                            apuestasPendientes = true;
                        }
                        break;
                    case 4:
                        if (apuestaMinima > 0) {
                            jugador.completar(apuestaMinima);
                            apuestasRonda.set(jugador.getNombre() - 1, apuestaMinima);
                            if (apuestaMinima > apuestaMaxima) {
                                apuestaMaxima = apuestaMinima;
                                apuestasPendientes = true;
                            }
                        } else {
                            System.out.println("Opción inválida.");
                        }
                        break;
                    default:
                        System.out.println("Opción inválida.");
                }

                if (jugadoresActivos.size() == 1) {
                    System.out.println("Solo queda un jugador, ronda terminada.");
                    return;
                }
            }
        }
        System.out.println("Fase de apuestas terminada.\n");
    }
}