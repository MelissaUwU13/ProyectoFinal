public class Poker5CardDraw extends Poker{
    public Poker5CardDraw(){
        super();
        boolean error=false;
        while(error=false) {
            System.out.println("Ingrese la cantidad de jugadores: ");
            int cantJugadores = 0;
            if (cantJugadores < 2 || cantJugadores > 7) {
                System.out.println("Cantidad erronea, vuelvelo a intentar ");
                error = false;
            }
            else {
                error = true;
            }
        }

        generarBaraja();
    }
}
