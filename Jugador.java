import java.util.ArrayList;

public class Jugador {
    private int fichas, noJugador, apuestaActual;
    private String nombre;
    private boolean retirado = false, huboCambioDeCartas = false;
    private ArrayList<Carta> mano;
    private String jugadaFinal; // ej. "Full House", "Color", etc.

    public Jugador(String nombre,int noJugador, int fichas) {
        this.nombre = nombre;
        this.noJugador= noJugador;
        this.mano = new ArrayList<>();
        this.fichas = fichas;
    }

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

    // Getters y setters
    public ArrayList<Carta> getMano() {
        return mano;
    }

    public void setMano(ArrayList<Carta> mano) {
        this.mano = mano;
    }

    public String getNombre() {
        return nombre;
    }

    public int getNoJugador() {
        return noJugador;
    }

    public boolean getHuboCambioDeCartas() {
        return huboCambioDeCartas;
    }

    public int getApuestaActual() {
        return this.apuestaActual;
    }

    public boolean getRetirado() {
        return retirado;
    }

    public void setHuboCambioDeCartas(boolean huboCambioDeCartas) {
        this.huboCambioDeCartas = huboCambioDeCartas;
    }

    public int getFichas() {
        return fichas;
    }

    public void setRetirado(boolean retirado) {
        this.retirado = retirado;
    }

    public void retirarse() {
        this.retirado = true;
    }

    public void setFichas(int fichas) {
        this.fichas = fichas;
    }

    public void setApuestaActual(int cantidad) {
        this.apuestaActual = cantidad;
    }

    public boolean sinFichas() {
        return fichas <= 0;
    }

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

    public String getJugadaFinal() {
        return jugadaFinal;
    }

}