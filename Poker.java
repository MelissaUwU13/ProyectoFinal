import java.util.*;

public class Poker extends EvaluarMano{
    private int poker;
    protected ArrayList<Carta> mazo;
    protected ArrayList<Jugador> jugadores;
    protected ArrayList<Integer> apuestas;
    protected EvaluarMano evaluador;

    //checar pq es de chat
    public void determinarGanador(){
        // Lógica para decidir el ganador
        // Por ejemplo, comparar las puntuaciones y mostrar el ganador
        Jugador mejorJugador = null;
        int mejorPuntuacion = -1;
        Jugador ganador = null;

        for (Jugador j : jugadores) {
            ArrayList<Carta> mano = j.getMano();
            int puntuacion= analizarMano(mano);
            if (puntuacion > mejorPuntuacion) {
                mejorPuntuacion = puntuacion;
                ganador = j;
            }
        }

        if (ganador != null) {
            System.out.println("¡El ganador es: " + ganador.getNoJugador());
        }
    }

    public String evaluarMano(ArrayList<Carta> mano) {
        int puntuacion = evaluador.analizarMano(mano);
        return evaluador.interpretarPuntuación(puntuacion);
    }

    public void repartirCartas(int N, boolean visible) {
        for (Jugador j : jugadores) {
            for (int i = 0; i < N; i++) {
                Carta c = mazo.remove(0);
                j.recibirCarta(c, visible);
            }
        }
    }

    public List<Jugador> getJugadores() {
        return jugadores;
    }

    public void mostrarManos() {
        int num = 1;
        for (Jugador j : jugadores) {
            System.out.println("Mano del " + j.getNoJugador() + ":");
            for (Carta c : j.getMano()) {
                System.out.println("- " + c); // Asegúrate de que Carta tenga un buen toString()
            }
            System.out.println(); // Espacio entre jugadores
            num++;
        }
    }

    public void mostrarMano(Jugador jugador) {
        for (int i = 0; i < jugador.getMano().size(); i++) {
            System.out.println(i + ": " + jugador.getMano().get(i));
        }
    }

    public void generarBaraja(){
        mazo= new ArrayList<>();

        String[] figuras = {"corazon", "trebol", "diamante", "pica"};
        for (String figura : figuras) {
            for (int j = 1; j <= 13; j++) {
                mazo.add(new Carta(j, figura, true));
            }
        }
        Collections.shuffle(mazo);
    }


//QUIEN HIZO ESTO??
    public void faseDeApuestas() {
        Scanner sc = new Scanner(System.in);
        int apuestaActual = 0;
        boolean rondaActiva = true;

        // Copia para no modificar la lista original si alguien hace fold
        ArrayList<Jugador> jugadoresActivos = new ArrayList<>(jugadores);

        while (rondaActiva) {
            rondaActiva = false;

            Iterator<Jugador> it = jugadoresActivos.iterator();
            while (it.hasNext()) {
                Jugador jugador = it.next();

                System.out.println("\nJugador " + jugador.getNoJugador() + " (Fichas: " + jugador.getFichas() + ")");
                System.out.println("Mano:");
                mostrarMano(jugador);  // Esto puedes moverlo a Poker5CardDraw si quieres

                System.out.println("Apuesta actual: " + apuestaActual);

                if (apuestaActual == 0) {
                    System.out.println("1. Check");
                    System.out.println("2. Bet");
                    System.out.println("3. Fold");
                } else {
                    System.out.println("1. Call");
                    System.out.println("2. Raise");
                    System.out.println("3. Fold");
                }

                int opcion;
                while (true) {
                    try {
                        opcion = Integer.parseInt(sc.nextLine());
                        if (opcion >= 1 && opcion <= 3) break;
                        System.out.println("Opción inválida. Intenta de nuevo:");
                    } catch (Exception e) {
                        System.out.println("Entrada inválida. Ingresa un número:");
                    }
                }

                switch (opcion) {
                    case 1:
                        if (apuestaActual == 0) {
                            System.out.println(jugador.getNoJugador() + " hace check.");
                        } else {
                            int cantidadCall = apuestaActual;
                            if (cantidadCall > jugador.getFichas()) cantidadCall = jugador.getFichas();
                            jugador.apostar(cantidadCall);
                            System.out.println(jugador.getNoJugador() + " iguala la apuesta.");
                        }
                        break;
                    case 2:
                        if (apuestaActual == 0) {
                            System.out.print("¿Cuántas fichas apuestas? ");
                            int cantidadBet = Integer.parseInt(sc.nextLine());
                            if (cantidadBet > jugador.getFichas()) {
                                System.out.println("No tienes suficientes fichas.");
                                continue;
                            }
                            apuestaActual = cantidadBet;
                            jugador.apostar(cantidadBet);
                            rondaActiva = true;
                        } else {
                            System.out.print("¿Cuánto deseas subir? ");
                            int cantidadRaise = Integer.parseInt(sc.nextLine());
                            int total = apuestaActual + cantidadRaise;
                            if (total > jugador.getFichas()) {
                                System.out.println("No tienes suficientes fichas.");
                                continue;
                            }
                            apuestaActual = total;
                            jugador.apostar(total);
                            System.out.println(jugador.getNoJugador() + " sube la apuesta a " + total);
                            rondaActiva = true;
                        }
                        break;
                    case 3:
                        System.out.println(jugador.getNoJugador() + " se retira.");
                        it.remove();
                        break;
                }
            }
        }

        jugadores = jugadoresActivos;
    }
}