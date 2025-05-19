import java.util.ArrayList;
import java.util.Collections;

public class Jugador5CardDraw extends Jugador{
    private boolean retirado = false;
    private ArrayList<Carta> mano = new ArrayList<>();
    private int apuestaActual = 0;
    private boolean jugadorDescartoCarta;
    private String nombre;

    public Jugador5CardDraw(String nombre, int fichas) {
        super(nombre, fichas);
        this.nombre = nombre;
    }


    public boolean estaRetirado() {
        return retirado;
    }

    public void retirarse() {
        this.retirado = true;
    }

    public String getNombre() {
        return nombre;
    }

    public boolean getJugadorTiroCarta() {
       return jugadorDescartoCarta;
    }

    public void setApuestaActual(int apuestaActual) {
        this.apuestaActual = apuestaActual;
    }
}