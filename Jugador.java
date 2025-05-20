import java.util.ArrayList;

public class Jugador {
    private int fichas, noJugador, apuestaActual;
    private String nombre;
    private boolean retirado = false, huboCambioDeCartas = false;
    private ArrayList<Carta> mano;

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

    public ArrayList<Carta> getMano() {
        return mano;
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

    public void setHuboCambioDeCartas(boolean huboCambioDeCartas) {
        this.huboCambioDeCartas = huboCambioDeCartas;
    }

    public boolean estaRetirado() {
        return retirado;
    }

    public void setRetirado(boolean retirado) {
        this.retirado = retirado;
    }

    public void retirarse() {
        this.retirado = true;
    }

    public int getFichas() {
        return fichas;
    }

    public void setFichas(int fichas) {
        this.fichas = fichas;
    }


    //Muestra solo las cartas visibles (para otras personas)
    public void mostrarCartasVisibles() {
        System.out.print(nombre + " muestra: ");
        for (Carta c : mano) {
            if (c.isVisible()) {
                System.out.print(c + " ");
            }
        }
        System.out.println();
    }

    public void setMano(ArrayList<Carta> manoActual) {
        this.mano = manoActual;
    }

    //si me sirve qwq
    public void apostar(int cantidad) {
        if (cantidad <= fichas) {
            fichas -= cantidad;
            //System.out.println("Jugador "+nombre + " apuesta " + cantidad + " fichas. Fichas restantes: " + fichas);
        }
        //else{
            //System.out.println("Jugador "+nombre + " no tiene suficientes fichas para apostar " + cantidad);
        //}
    }

    //cambios validos??
    public void subirYApostar(int cantidadParaIgualar) {
        // Permite igualar incluso si no tiene fichas suficientes (all-in)
        if (cantidadParaIgualar > 0) {
            int cantidadReal = Math.min(cantidadParaIgualar, fichas);
            fichas -= cantidadReal;
            apuestaActual += cantidadReal;
        }
    }

    public void setApuestaActual(int cantidad) {
        this.apuestaActual = cantidad;
    }

    public int getApuestaActual() {
        return this.apuestaActual;
    }

    public void sumarFichas(int cantidad) {
        if (cantidad > 0) {
            this.fichas += cantidad;
            System.out.println("Jugador " + nombre + " recibe " + cantidad + " fichas. Total ahora: " + this.fichas);
        }
    }

}