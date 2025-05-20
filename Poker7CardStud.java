import java.util.*;
import javax.swing.*;
import java.awt.*;

public class Poker7CardStud extends Poker{
    private ArrayList<Integer> apuestasRonda;
    private int apuestaActual=5, pozo = 0, jugadoresQuePasaronTurno = 0;
    private int jugadoresQueHicieronCheck = 0, jugadoresQueHicieronCall = 0;
    int jugadaBring=1;
    private Map<Jugador, Integer> puntuaciones = new HashMap<>();

    public Poker7CardStud(int cantJugadores, int fichasTotales, ArrayList<String> nombresJugadores) {
        super();
        jugadores = new ArrayList<>();
        evaluador = new Evaluador7CardStud();
        this.apuestasRonda = new ArrayList<>();
        this.mazo= generarBaraja();
        fichasTotales=fichasTotales-5;

        for (int i = 0; i < cantJugadores; i++) {
            String nombre = nombresJugadores.get(i);
            jugadores.add(new Jugador7CardStud(nombre,i+1, fichasTotales));// ejemplo fichas iniciales
            apuestasRonda.add(0);
        }

        //repartir las primeras cartas
        //repartirNCartas(2,false);
        //repartirNCartas(1,true);

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
                    //determinarGanador();
                }
                else{
                    System.out.println("Gana el jugador "+jugadores.getFirst().getNombre());
                }
                break;
            default:
                System.out.println("Ronda inválida");
        }
    }

    public int obtenerNUMEROJugadorInicial(int ronda) {
        int mejor = 0;
        int mejorPuntuacion = -1;

        for (Jugador j : jugadores) {
            Jugador7CardStud jugador = (Jugador7CardStud) j; // cast aquí

            ArrayList<Carta> visibles = jugador.getCartasVisibles(ronda);
            int puntuacion = evaluarCartas(visibles); // tú debes definir esta función según reglas de póker
            if (puntuacion > mejorPuntuacion) {
                mejorPuntuacion = puntuacion;
                mejor = jugador.getNoJugador();
            } else if (puntuacion == mejorPuntuacion) {
                // desempate: gana jugador de menor número
                if (j.getNoJugador() < mejor) {
                    mejor = jugador.getNoJugador();
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




    //GRAFICOS

    // Con esto, sacamos la primera carta del mazo
    public Carta sacarCarta() {
        if (!mazo.isEmpty()) {
            return mazo.remove(0); // Saca la primera carta (el mazo debe estar barajado)
        } else {
            throw new IllegalStateException("El mazo está vacío");
        }
    }

    // Aquí guardamos la puntuación del jugador en un hashmap
    public void guardarPuntuacion(Jugador jugador) {
        int puntaje = evaluador.analizarMano(jugador.getMano());
        puntuaciones.put(jugador, puntaje);
    }

    // Aquí revisamos la puntuación del jugador sacando este valor del hashmap en donde guardamos su nombre y puntaje
    public Jugador compararPuntuaciones() {
        Jugador ganador = null;
        int mejorPuntuacion = -1;

        // Primero encontramos al ganador
        for (Map.Entry<Jugador, Integer> entry : puntuaciones.entrySet()) {
            Jugador j = entry.getKey();
            int puntaje = entry.getValue();

            if (puntaje > mejorPuntuacion && !j.getRetirado()) {
                mejorPuntuacion = puntaje;
                ganador = j;
            }
        }

        // Si hubo un ganador, le damos las fichas del pozo a el
        if (ganador != null) {
            ganador.sumarFichas(pozo);

            // Reseteamos el pozo después de distribuirlo
            reiniciarPozo();
        }

        puntuaciones.clear();
        return ganador;
    }

    // Con esto, agregamos una cantidad de fichas al pozo
    public void agregarAlPozo(int cantidad) {
        this.pozo += cantidad;
    }

    // Esto vuelve a poner la cantidad de fichas en el pozo a 0
    public void reiniciarPozo() {
        pozo = 0;
    }

    public int getPozo(){
        return pozo;
    }

    // Con esto reiniciamos el número de veces que los jugadores pasaron turno
    public void reiniciarPases() {
        jugadoresQuePasaronTurno = 0;
    }

    // Con esto incrementamos el contador de los jugadores que pasaron turno, lo cual nos sirve para pasar de la fase de apuestas al descarte
    public void incrementarPases() {
        jugadoresQuePasaronTurno++;
    }

    // Getters y setters
    public int getJugadoresQuePasaronTurno() {
        return jugadoresQuePasaronTurno;
    }

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

    public void setJugadoresQueYaJugaron(int jugador) {
        this.jugadoresQueYaJugaron = jugador;
    }
}