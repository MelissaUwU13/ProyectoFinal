import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Poker7CardStud extends Poker {
    private ArrayList<Integer> apuestasRonda;
    private int apuestaActual=5, pozo = 0;
    private int jugadoresQueHicieronCheck = 0, jugadoresQueDescartaron = 0, jugadoresQueHicieronCall = 0;
    int jugadaBring=1;

    public Poker7CardStud(int cantJugadores, int fichasTotales, ArrayList<String> nombresJugadores) {
        super();
        jugadores = new ArrayList<>();
        this.apuestasRonda = new ArrayList<>();
        this.mazo=generarBaraja();
        fichasTotales=fichasTotales-5;

        for (int i = 0; i < cantJugadores; i++) {
            String nombre = nombresJugadores.get(i);
            jugadores.add(new Jugador7CardStud(nombre,i+1, fichasTotales));// ejemplo fichas iniciales
            apuestasRonda.add(0);
        }

        //repartir las primeras cartas
        repartirNCartas(2,false);
        repartirNCartas(1,true);

        //IniciarJuego();
    }

    public void IniciarJuego() {
        boolean ganador = false;

        int m = 1;
        while (!ganador && m <= 6) {
            System.out.println("\nRonda " + m);
            calles(m);
            if (m == 6) {
                ganador = true;
            }
            m++;
        }
    }

    public void repartirNCartas(int N, boolean visible) {
        int totalCartas = N * jugadores.size();
        if (mazo.size() < totalCartas) {
            System.out.println("No hay suficientes cartas en el mazo para repartir.");
            return;
        }

        for (Jugador j : jugadores) {
            if (j instanceof Jugador7CardStud) {
                Jugador7CardStud js = (Jugador7CardStud) j;
                for (int i = 0; i < N; i++) {
                    Carta c = mazo.remove(0);
                    js.recibirCarta(c, visible);
                }
            }
        }
    }

    public void turnoCalles(int ronda){
        switch (ronda){
            //Third Street
            case 1:
                Jugador7CardStud jugadorBringIn = obtenerJugadorConCartaVisibleMasBaja(); // ronda 3
                reorganizarOrdenDesde(jugadorBringIn);
                break;
            //Fourth Street
            case 2:
                Jugador7CardStud inicial2 = obtenerJugadorInicial(2);
                reorganizarOrdenDesde(inicial2);
                break;

            //Fifth Street
            case 3:
                Jugador7CardStud inicial3 = obtenerJugadorInicial(3);
                reorganizarOrdenDesde(inicial3);
                break;

            //Sixth Street
            case 4:
                Jugador7CardStud inicial4 = obtenerJugadorInicial(4);
                reorganizarOrdenDesde(inicial4);
                break;
            //Seventh Street
            case 5:
                Jugador7CardStud inicial5 = obtenerJugadorInicial(5);
                reorganizarOrdenDesde(inicial5);
                break;
        }
    }

    public void faseApuestas(int apuestaMinima, int ronda) {
        Scanner sc = new Scanner(System.in);
        ArrayList<Jugador7CardStud> jugadoresActivos = new ArrayList<>();
        apuestaActual = apuestaMinima;
        boolean apuestasPendientes = true;

        for (Jugador j : jugadores) {
            Jugador7CardStud jug = (Jugador7CardStud) j;
            if (jug.esActivo()) {
                jugadoresActivos.add(jug);
            }
        }

        // Reiniciar apuestas por ronda
        for (int i = 0; i < apuestasRonda.size(); i++) {
            apuestasRonda.set(i, 0);
        }

        HashMap<Jugador7CardStud, Boolean> yaIgualo = new HashMap<>();
        for (Jugador7CardStud j : jugadoresActivos) {
            yaIgualo.put(j, false);
        }

        while (apuestasPendientes) {
            // Limpiar jugadores inactivos
            jugadoresActivos.removeIf(j -> !j.esActivo());

            if (jugadoresActivos.size() <= 1) {
                apuestasPendientes = false;
                break;
            }

            apuestasPendientes = false;

            for (Jugador7CardStud jugador : jugadoresActivos) {
                Boolean haIgualado = yaIgualo.get(jugador);
                if (haIgualado != null && haIgualado) {
                    // No usar continue, simplemente saltamos a siguiente iteración con control manual
                } else {
                    System.out.println("\nJugador " + jugador.getNombre());
                    System.out.println("Apuesta máxima actual: " + apuestaActual);
                    System.out.println("Cantidad de fichas disponibles: " + jugador.getFichas());
                    System.out.println("Opciones:");
                    System.out.println("1. Pasar");
                    System.out.println("2. Igualar");
                    System.out.println("3. Subir");

                    boolean puedeCompletar = jugador.equals(obtenerJugadorConCartaVisibleMasBaja()) && ronda == 1;
                    if (puedeCompletar) {
                        System.out.println("4. Completar - BRING IN");
                    }

                    boolean accionValida = false;
                    while (!accionValida) {
                        int opcion = sc.nextInt();

                        switch (opcion) {
                            case 1: // Pasar
                                jugador.pasar();
                                yaIgualo.put(jugador, true);
                                accionValida = true;
                                break;

                            case 2: // Igualar
                                jugador.igualar(apuestaActual, apuestasRonda);
                                int cantidadIgualada = apuestasRonda.get(jugador.getNoJugador() - 1);
                                pozo += cantidadIgualada;
                                yaIgualo.put(jugador, true);
                                accionValida = true;
                                break;

                            case 3: // Subir
                                System.out.println("Ingresa la nueva apuesta (debe ser mayor que " + apuestaActual + "):");
                                int nuevaApuesta = sc.nextInt();
                                int actualJugador = apuestasRonda.get(jugador.getNoJugador() - 1);
                                int diferencia = nuevaApuesta - actualJugador;

                                if (nuevaApuesta > apuestaActual && jugador.getFichas() >= diferencia) {
                                    jugador.subir(nuevaApuesta, apuestasRonda); // <-- este metodo no debe modificar el mapa
                                    apuestaActual = nuevaApuesta;
                                    pozo += diferencia;

                                    // Reiniciar el HashMap
                                    for (Jugador7CardStud j : jugadoresActivos) {
                                        if (j.equals(jugador)) {
                                            yaIgualo.put(j, true); // él ya igualó porque subió
                                        } else {
                                            yaIgualo.put(j, false); // todos los demás deben igualar
                                        }
                                    }

                                    apuestasPendientes = true;
                                    accionValida = true;
                                } else {
                                    System.out.println("Apuesta inválida. Debe ser mayor y tener suficientes fichas.");
                                }
                                break;

                            case 4:
                                if (puedeCompletar) {
                                    jugador.completar(apuestaMinima);
                                    apuestasRonda.set(jugador.getNoJugador() - 1, apuestaMinima);
                                    pozo += apuestaMinima;
                                    yaIgualo.put(jugador, true);
                                    accionValida = true;
                                } else {
                                    System.out.println("No puedes usar esa opción.");
                                }
                                break;

                            default:
                                System.out.println("Opción inválida.");
                                break;
                        }
                    }
                }
            }

            // Revisar si todos ya igualaron
            boolean todosIgualaron = true;
            for (Jugador7CardStud j : jugadoresActivos) {
                Boolean estado = yaIgualo.get(j);
                if (estado == null || !estado) {
                    todosIgualaron = false;
                    break;
                }
            }

            if (!todosIgualaron) {
                apuestasPendientes = true;
            }
        }

        System.out.println("Fase de apuestas terminada.");
    }

    public void calles(int ronda) {
        switch (ronda) {
            //Third Street
            case 1:
                turnoCalles(1);
                apuestaActual=5;
                faseApuestas(apuestaActual,1);// apuesta mínima de 5 (bring-in)

                System.out.println("\nEstado tras la calle 1:");
                for (Jugador j : jugadores) {
                    Jugador7CardStud j7 = (Jugador7CardStud) j;
                    System.out.println("Jugador " + j7.getNombre() + " tiene " + j7.getFichas() + " fichas.");
                    System.out.println("Cartas visibles: ");
                    for (Carta c : j7.getCartasVisibles(1)) {
                        System.out.println("- " + c);
                    }
                }
                break;
            //Fourth Street
            case 2:
                repartirNCartas(1,true);
                turnoCalles(2);
                faseApuestas(apuestaActual,2);

                System.out.println("\nEstado tras la calle 2 :");
                for (Jugador j : jugadores) {
                    Jugador7CardStud j7 = (Jugador7CardStud) j;
                    System.out.println("Jugador " + j7.getNombre() + " tiene " + j7.getFichas() + " fichas.");
                    System.out.println("Cartas visibles: ");
                    for (Carta c : j7.getCartasVisibles(2)) {
                        System.out.println("- " + c);
                    }
                }
                break;
            //Fifth Street
            case 3:
                repartirNCartas(1,true);
                turnoCalles(3);
                faseApuestas(apuestaActual,3);

                System.out.println("\nEstado tras la calle 3 :");
                for (Jugador j : jugadores) {
                    Jugador7CardStud j7 = (Jugador7CardStud) j;
                    System.out.println("Jugador " + j7.getNombre() + " tiene " + j7.getFichas() + " fichas.");
                    System.out.println("Cartas visibles: ");
                    for (Carta c : j7.getCartasVisibles(3)) {
                        System.out.println("- " + c);
                    }
                }
                break;
            //Sixth Street
            case 4:
                repartirNCartas(1,true);
                turnoCalles(4);
                faseApuestas(apuestaActual,4);

                System.out.println("\nEstado tras la calle 4 :");
                for (Jugador j : jugadores) {
                    Jugador7CardStud j7 = (Jugador7CardStud) j;
                    System.out.println("Jugador " + j7.getNombre() + " tiene " + j7.getFichas() + " fichas.");
                    System.out.println("Cartas visibles: ");
                    for (Carta c : j7.getCartasVisibles(4)) {
                        System.out.println("- " + c);
                    }
                }
                break;
            //Seventh Street
            case 5:
                //debe estar boca abajo
                repartirNCartas(1,false);
                turnoCalles(5);
                faseApuestas(apuestaActual,5);

                System.out.println("\nEstado tras la calle 5 :");
                for (Jugador j : jugadores) {
                    Jugador7CardStud j7 = (Jugador7CardStud) j;
                    System.out.println("Jugador " + j7.getNombre() + " tiene " + j7.getFichas() + " fichas.");
                    System.out.println("Cartas visibles: ");
                    for (Carta c : j7.getCartasVisibles(5)) {
                        System.out.println("- " + c);
                    }
                }
                break;
            //The Showdown
            case 6:
                if(jugadores.size()>1){
                    determinarGanador();
                }
                else{
                    System.out.println("Gana el jugador "+jugadores.getFirst().getNombre());
                }
                break;
            default:
                System.out.println("Ronda inválida");
        }
    }

    public Jugador7CardStud obtenerJugadorInicial(int ronda) {
        Jugador7CardStud mejor = null;
        int mejorPuntuacion = -1;

        for (Jugador j : jugadores) {
            Jugador7CardStud jugador = (Jugador7CardStud) j; // cast aquí

            ArrayList<Carta> visibles = jugador.getCartasVisibles(ronda);
            int puntuacion = evaluarCartas(visibles); // tú debes definir esta función según reglas de póker
            if (puntuacion > mejorPuntuacion) {
                mejorPuntuacion = puntuacion;
                mejor = jugador;
            } else if (puntuacion == mejorPuntuacion) {
                // desempate: gana jugador de menor número
                if (j.getNoJugador() < mejor.getNoJugador()) {
                    mejor = jugador;
                }
            }
        }

        return mejor;
    }

    //cambiar name
    public Jugador7CardStud obtenerJugadorConCartaVisibleMasBaja(){
        Jugador7CardStud peor = null;
        Carta cartaPeor = null;

        for (Jugador j : jugadores) {
            Jugador7CardStud jugador = (Jugador7CardStud) j;

            ArrayList<Carta> mano = j.getMano();

            if (mano.size() >= 3) {
                Carta visible = mano.get(2); // tercera carta

                if (visible.isVisible()) {
                    if (cartaPeor == null) {
                        cartaPeor = visible;
                        peor = jugador;
                    } else {
                        int valorVisible = visible.getValor();
                        int valorPeor = cartaPeor.getValor();

                        if (valorVisible < valorPeor ||
                                (valorVisible == valorPeor && j.getNoJugador() > peor.getNoJugador())) {
                            cartaPeor = visible;
                            peor = jugador;
                        }
                    }
                }
            }
        }

        return peor;
    }

    public void reorganizarOrdenDesde(Jugador7CardStud primero) {
        int index = jugadores.indexOf(primero);
        if (index > 0) {
            ArrayList<Jugador7CardStud> nuevoOrden = new ArrayList<>();

            for (Jugador j : jugadores.subList(index, jugadores.size())) {
                nuevoOrden.add((Jugador7CardStud) j); // cast necesario
            }
            for (Jugador j : jugadores.subList(0, index)) {
                nuevoOrden.add((Jugador7CardStud) j); // cast necesario
            }

            jugadores = new ArrayList<>(nuevoOrden); // esto es seguro porque los elementos casteados lo permiten
        }
    }

    public void determinarGanador() {
        Scanner sc = new Scanner(System.in);

        // Filtrar jugadores activos
        ArrayList<Jugador7CardStud> jugadoresActivos = new ArrayList<>();
        for (Jugador j : jugadores) {
            Jugador7CardStud jugador = (Jugador7CardStud) j;
            if (jugador.esActivo()) {
                jugadoresActivos.add(jugador);
            }
        }

        if (jugadoresActivos.size() == 0) {
            System.out.println("\nNingún jugador llegó al final... ");
            return;
        } else if (jugadoresActivos.size() == 1) {
            Jugador7CardStud ganadorUnico = jugadoresActivos.get(0);
            System.out.println("\n¡El único jugador activo es el Jugador " + ganadorUnico.getNombre() + ", gana automáticamente!");
            return;
        }
        else {

            // Comparación entre jugadores activos
            int mejorPuntaje = -1;
            Jugador7CardStud ganador = null;

            for (Jugador7CardStud jugador : jugadoresActivos) {
                ArrayList<Carta> mano = jugador.getMano();
                System.out.println("\nJugador " + jugador.getNombre() + ", estas son tus 7 cartas:");

                for (int i = 0; i < mano.size(); i++) {
                    System.out.println((i + 1) + ". " + mano.get(i));
                }

                ArrayList<Carta> seleccionadas = new ArrayList<>();
                while (seleccionadas.size() < 5) {
                    System.out.println("Elige la carta #" + (seleccionadas.size() + 1) + " (1-7):");
                    int eleccion = sc.nextInt();

                    if (eleccion >= 1 && eleccion <= 7) {
                        Carta carta = mano.get(eleccion - 1);
                        if (!seleccionadas.contains(carta)) {
                            seleccionadas.add(carta);
                        } else {
                            System.out.println("Ya seleccionaste esa carta, elige otra.");
                        }
                    } else {
                        System.out.println("Índice inválido, intenta de nuevo.");
                    }
                }

                int puntaje = evaluarCartas(seleccionadas);
                System.out.println("Jugador " + jugador.getNombre() + " tiene una mano con puntaje: " + puntaje);

                if (puntaje > mejorPuntaje) {
                    mejorPuntaje = puntaje;
                    ganador = jugador;
                } else if (puntaje == mejorPuntaje && jugador.getNoJugador() < ganador.getNoJugador()) {
                    ganador = jugador;
                }
            }

            System.out.println("\n¡El ganador es el Jugador " + ganador.getNombre());
        }
    }



    //GRAFICOS

    public void incrementarChecks() {
        jugadoresQueHicieronCheck++;
    }

    public int getJugadoresQueHicieronCheck() {
        return jugadoresQueHicieronCheck;
    }

    public void reiniciarChecks() {
        jugadoresQueHicieronCheck = 0;
    }

    //VER SI SIRVE

    public Jugador getJugadorActivoRestante() {
        for (Jugador j : jugadores) {
            if (!j.getRetirado()) return j;
        }
        return null;
    }

    public int getApuestaActual() {
        return apuestaActual;
    }

    public void setApuestaActual(int apuesta) {
        this.apuestaActual = apuesta;
    }


    //Metodo para poker 5
    public Carta sacarCarta() {
        if (!mazo.isEmpty()) {
            return mazo.remove(0); // Saca la primera carta (el mazo debe estar barajado)
        } else {
            throw new IllegalStateException("El mazo está vacío");
        }
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

}