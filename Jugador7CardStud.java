public class Jugador7CardStud extends Jugador {
boolean activo;

//constructor de clase jugador7 que hereda de jugador
public Jugador7CardStud(String nombre,int noJugador, int fichas) {
    super(nombre,noJugador, fichas);
    this.activo = true;  // empieza activo
}

//revisa si el jugador sigue jugando
public boolean esActivo() {
    return activo;
}

//modifica si el jugador sigue jugando
public void setActivo(boolean activo) {
    this.activo = activo;
}

}
