import java.util.ArrayList;

public class Jugador {
    private int fichas, noJugador, apuestaActual;
    private String nombre;
    private boolean retirado = false, huboCambioDeCartas = false;
    private ArrayList<Carta> mano;

    //Constructor de clase jugador
    public Jugador(String nombre,int noJugador, int fichas) {
        this.nombre = nombre;
        this.noJugador= noJugador;
        this.mano = new ArrayList<>();
        this.fichas = fichas;
    }

    //recibir carta a nuestra mano
    public void recibirCarta(Carta c, boolean visible) {
        c.setVisible(visible);
        mano.add(c);
    }

    // Con esto podemos poner las fichas en el pozo de fichas
    public void apostar(int cantidad) {
        if (cantidad <= fichas) {
            fichas -= cantidad;
        }
    }

    // Esto es para igualar o subir la cantidad de fichas apostadas en la parte gráfica de mi juego
    public void subirYApostar(int cantidadParaIgualar) {
        // Nos permite igualar incluso si ya no tenemos fichas
        if (cantidadParaIgualar > 0) {
            int cantidadReal = Math.min(cantidadParaIgualar, fichas);
            fichas -= cantidadReal;
            apuestaActual += cantidadReal;
        }
    }

    // Esto lo usamos para que se le transfieran las fichas al ganador del juego
    public void sumarFichas(int cantidad) {
        if (cantidad > 0) {
            this.fichas += cantidad;
            System.out.println("Jugador " + nombre + " recibe " + cantidad + " fichas. Total ahora: " + this.fichas);
        }
    }

    //devuelve la mano del jugador
    public ArrayList<Carta> getMano() {
        return mano;
    }

    //cambiamos la mano del jugador por otra
    public void setMano(ArrayList<Carta> mano) {
        this.mano = mano;
    }

    //devuelve el nombre del jugador
    public String getNombre() {
        return nombre;
    }

    //devuelve el numero del jugador
    public int getNoJugador() {
        return noJugador;
    }

    //devuelve un booleano si es que hubo o no cambio de cartas  - POKER5
    public boolean getHuboCambioDeCartas() {
        return huboCambioDeCartas;
    }

    //devuelve el valor de la apuesta actual
    public int getApuestaActual() {
        return this.apuestaActual;
    }

    //devuelve el valor si esta el jugador retirado o ne
    public boolean getRetirado() {
        return retirado;
    }

    //cambiamos el booleano de cambio en cartas - POKER 5
    public void setHuboCambioDeCartas(boolean huboCambioDeCartas) {
        this.huboCambioDeCartas = huboCambioDeCartas;
    }

    //nos devuelve el valor de las fichas
    public int getFichas() {
        return fichas;
    }

    //cambiamos el estado de retirado
    public void setRetirado(boolean retirado) {
        this.retirado = retirado;
    }

    //comprueba si un jugador se sale de la partida
    public void retirarse() {
        this.retirado = true;
    }

    //cambiamos el valor de las fichas
    public void setFichas(int fichas) {
        this.fichas = fichas;
    }

    //cambiamos el valor de la apuesta actual
    public void setApuestaActual(int cantidad) {
        this.apuestaActual = cantidad;
    }

    //revisa si nos hemos quedado sin fichas
    public boolean sinFichas() {
        return fichas <= 0;
    }

    //devuele las cartas que son visibles para el jugador - POKER7
    public ArrayList<Carta> getCartasVisibles(int ronda) {
        ArrayList<Carta> visibles = new ArrayList<>();
        ArrayList<Carta> mano = getMano();
        for (int i = 0; i < mano.size() && i < ronda; i++) {
            Carta c = mano.get(i);
            if (c.isVisible()) {
                visibles.add(c);
            }
        }
        return visibles;
    }

}