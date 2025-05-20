import java.util.ArrayList;

public class Jugador5CardDraw extends Jugador{
    private boolean retirado = false;
    private ArrayList<Carta> mano = new ArrayList<>();
    private int fichas;
    private boolean jugadorDescartoCarta;
    private String nombre;

    public Jugador5CardDraw(String nombre,int noJugador, int fichas) {
        super(nombre,noJugador, fichas);
        this.nombre=nombre;
        this.fichas = fichas;
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

    public void restarFichas(int cantidad) {
        this.fichas -= cantidad;
    }

}