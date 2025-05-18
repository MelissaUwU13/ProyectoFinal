import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class Poker7CardStud extends Poker {
    private ArrayList<Jugador7CardStud> jugadores;
    private ArrayList<Integer> apuestasRonda;
    private int apuestaActual = 5;

    public Poker7CardStud() {
        boolean error=false, ganador=false;
        Scanner sc = new Scanner(System.in);
        int cantJugadores = 0;

        while(error=false) {
            System.out.println("Ingrese la cantidad de jugadores: ");
            try {
                cantJugadores = sc.nextInt();
                if (cantJugadores < 2 || cantJugadores > 8) {
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

        error=false;

        this.jugadores = new ArrayList<>();
        this.apuestasRonda = new ArrayList<>();
        int fichasTotales=0;

        while(error=false){
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

        System.out.println("Antes de iniciar tenemos nuestra primera apuesta obligatoria!");
        System.out.println("La apuesta inicial es de 5 fichas!");
        fichasTotales=fichasTotales-5;


        for (int i = 1; i <= cantJugadores; i++) {
            jugadores.add(new Jugador7CardStud(i, fichasTotales));  // ejemplo fichas iniciales
            apuestasRonda.add(0);
        }

        //repartir las primeras cartas
        for (int i = 1; i <= cantJugadores; i++) {
            repartirCartas(1,true);
            repartirCartas(2,false);
        }


        //yo me encargo
        //iterator=???
        int m = 1;
        while (!ganador && m <= 6) {
            System.out.println("Ronda " + m);
            calles(m);
            if (m == 6) {
                ganador = true;
            }
            m++;
        }
    }

    public void turnoCalles(int ronda){
        switch (ronda){
            //Third Street
            case 1:
                //Aqui se aplicaria un for donde revise las cartas de todos los jugadores para comprobar quien empieza
                // if(//aqui iria carta mas baja){
                //solo se revisan las cartas visibles

                //aqui se analiza el brig-in y empiezan los que estan a su lado hasta que todos los jugadores hayan jugado

            //Fourth Street
            case 2:
                //Aqui se aplicaria un for donde revise las cartas de todos los jugadores para comprobar quien empieza
                //Aqui inicia el jugador con la mejor combinacion/puntaje de dos cartas mostradas, no cuentan las invisibles
                //en caso de empate de puntaje, inicia el jugador 1
                //una idea base?? no se si funcione
                int mejorPuntuacion = -1;
                Jugador ganador = null;

                for (Jugador j : jugadores) {
                    ArrayList<Carta> mano = j.getMano();
                    int puntuacion=analizarMano(mano);
                    if (puntuacion > mejorPuntuacion) {
                        mejorPuntuacion = puntuacion;
                        ganador = j;
                    }
                }


            //Fifth Street
            case 3:
                //Aqui se aplicaria un for donde revise las cartas de todos los jugadores para comprobar quien empieza
                //Aqui inicia el jugador con la mejor combinacion/puntaje de tres cartas mostradas, no cuentan las invisibles
                //en caso de empate de puntaje, inicia el jugador 1


            //Sixth Street
            case 4:
                //Aqui se aplicaria un for donde revise las cartas de todos los jugadores para comprobar quien empieza
                //Aqui inicia el jugador con la mejor combinacion/puntaje de 4 cartas mostradas, no cuentan las invisibles
                //en caso de empate de puntaje, inicia el jugador 1


            //Seventh Street
            case 5:
                //Aqui se aplicaria un for donde revise las cartas de todos los jugadores para comprobar quien empieza
                //Aqui inicia el jugador con la mejor combinacion/puntaje de 4 cartas mostradas, osea la misma que el caso 4 o Sixth Street
                //en caso de empate de puntaje, inicia el jugador 1
        }
    }

    public void calles(int ronda) {
        switch (ronda) {
            //Third Street
            case 1:
                faseApuestas(apuestaActual,1);  // apuesta mínima de 5 (bring-in)
                break;
            //Fourth Street
            case 2:
                repartirCartas(1,true);
                faseApuestas(apuestaActual,2);
                break;
            //Fifth Street
            case 3:
                repartirCartas(1,true);
                faseApuestas(apuestaActual,3);
                break;
            //Sixth Street
            case 4:
                repartirCartas(1,true);
                faseApuestas(apuestaActual,4);
                break;
            //Seventh Street
            case 5:
                //debe estar boca abajo
                repartirCartas(1,false);
                faseApuestas(apuestaActual,5);
                break;
            //The Showdown
            case 6:
                if(jugadores.size()>1){
                    //TIENE QUE ANALIZAR TODAS LAS MANOS Y RECOPILAR LOS PUNTAJES
                    determinarGanador(); //ver si funciona
                }
                else{
                    System.out.println("Gana el jugador "+jugadores.getFirst().getNoJugador());
                }
                break;
            default:
                System.out.println("Ronda inválida");
        }
    }


    public void faseApuestas(int apuestaMinima, int ronda) {
        Scanner sc = new Scanner(System.in);
        ArrayList<Jugador7CardStud> jugadoresActivos = new ArrayList<>(jugadores);

        // Reiniciar apuestasRonda para jugadores activos
        for (int i = 0; i < apuestasRonda.size(); i++) {
            apuestasRonda.set(i, 0);
        }

        int apuestaMaxima = apuestaMinima;
        int rondaActual= ronda;
        boolean apuestasPendientes = true;

        while (apuestasPendientes && jugadoresActivos.size() > 1) {
            apuestasPendientes = false;

            //FASE DE APUESTAS DEBE PEDIR EL NIVEL PARA APLICAR TURNO DE JUGADORES!!
            turnoCalles(rondaActual);

            for (Jugador7CardStud jugador : new ArrayList<>(jugadoresActivos)) {
                System.out.println("\nJugador " + jugador.getNoJugador());
                System.out.println("Apuesta máxima actual: " + apuestaMaxima);
                System.out.println("Cantidad de fichas disponibles: " + jugador.getFichas());
                System.out.println("Opciones:");
                System.out.println("1. Pasar");
                System.out.println("2. Igualar");
                System.out.println("3. Subir");
                if (apuestaMinima > 0) {
                    System.out.println("4. Completar (bring-in)");
                }

                int opcion = sc.nextInt();
                boolean errorOpcion=false;

                while(errorOpcion==false){
                    switch (opcion) {
                        case 1:
                            jugador.pasar(jugadoresActivos);
                            errorOpcion=true;
                            break;
                        case 2:
                            jugador.igualar(apuestaMaxima, apuestasRonda);
                            errorOpcion=true;
                            break;
                        case 3:
                            boolean errorApuesta = false;

                            while (errorApuesta == false) {
                                System.out.println("\nIngresa la nueva apuesta (debe ser mayor que " + apuestaMaxima + "):");
                                int nuevaApuesta = sc.nextInt();
                                if (nuevaApuesta > apuestaMaxima) {
                                    jugador.subir(nuevaApuesta, apuestasRonda);

                                    apuestaMaxima = nuevaApuesta;
                                    apuestaActual = nuevaApuesta;
                                    apuestasPendientes = true;
                                    errorApuesta = true;
                                } else {
                                    System.out.println("Debe ser mayor a la apuesta actual!");
                                    errorApuesta = false;
                                }
                            }

                            errorOpcion=true;
                            break;
                        case 4:
                            if (apuestaMinima > 0) {
                                jugador.completar(apuestaMinima);
                                apuestasRonda.set(jugador.getNoJugador() - 1, apuestaMinima);
                                if (apuestaMinima > apuestaMaxima) {
                                    apuestaMaxima = apuestaMinima;
                                    apuestasPendientes = true;
                                }
                                errorOpcion=true;
                            } else {
                                System.out.println("Opción inválida, vuelvelo a intentar");
                                errorOpcion = false;
                            }
                            break;
                        default:
                            System.out.println("Opción inválida, vuelvelo a intentar");
                            errorOpcion=false;
                    }
                }
            }
        }
        System.out.println("Fase de apuestas terminada.\n");
    }

}