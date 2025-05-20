import java.util.ArrayList;

public class Jugador5CardDraw extends Jugador{
    private boolean retirado = false;
    private int fichas;
    private String nombre;

    public Jugador5CardDraw(String nombre,int noJugador, int fichas) {
        super(nombre,noJugador, fichas);
        this.nombre=nombre;
        this.fichas = fichas;
    }

    // Algunos getters y setters
    public boolean getRetirado() {
        return retirado;
    }

    public void retirarse() {
        this.retirado = true;
    }

    public String getNombre() {
        return nombre;
    }

}