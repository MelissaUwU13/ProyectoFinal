public class Jugador5CardDraw extends Jugador{
    private boolean retirado = false;
    private int fichas;
    private String nombre;

    //constructor de clase jugador7 que hereda de jugador
    public Jugador5CardDraw(String nombre,int noJugador, int fichas) {
        super(nombre,noJugador, fichas);
        this.nombre=nombre;
        this.fichas = fichas;
    }

    // Algunos getters y setters
    public boolean getRetirado() {
        return retirado;
    }

    //cambiamos el booleano de jugador para saber si sigue en el juego
    public void retirarse() {
        this.retirado = true;
    }

    //devuelve el nombre de jugador
    public String getNombre() {
        return nombre;
    }

}