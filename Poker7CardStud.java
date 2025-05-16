import java.util.ArrayList;

public class Poker7CardStud extends Poker{
    private Jugador7CardStud jugadores;

    public Poker7CardStud() {
        super();
        boolean error=false, ganador=false;
        while(error=false) {
            System.out.println("Ingrese la cantidad de jugadores: ");
            int cantJugadores = 0;
            if (cantJugadores < 2 || cantJugadores > 8) {
                System.out.println("Cantidad erronea, vuelvelo a intentar ");
                error = false;
            }
            else {
                error = true;
            }
        }

        generarBaraja();

        //repartir cartas 2 abajo y 1 arriba LLAMAR METODO REPARTIR 3 y metodo VOLTEAR CARTA,
        // o ver si voltear toda la baraja y luego irlas mostrando


        //iterator=???
        int m=0;

        while(ganador==false) {
            calles(1); //aqui dentro el iterator
                 //el itetador++

            //Una vez lleguemos a la ronda 6, ya deberiamps tener ganador, m es el iterator
            if(m==6){
                ganador=true;
            }
        }

    }

    public void faseApuestas(int fichas){
        //aqui una serie de botones que salga lo que le jugador puede ser al apostar y aja
        //entre cada ronda de apuestas, se iran jugadores y hay que eliminarlos del juego
    }


    //ver como meter la cantidad de fichas de cada jugador o cuantas se repaten
    int fichas=0;

    public void calles(int ronda){
        switch (ronda){
            case 1:
                turnoCalles(1);
                faseApuestas(fichas);
            case 2:
                //debe estar volteada
                repartirCartas(1);
                turnoCalles(2);
                faseApuestas(fichas);
            case 3:
                repartirCartas(1);
                turnoCalles(3);
                faseApuestas(fichas);

            case 4:
                repartirCartas(1);
                turnoCalles(4);
                faseApuestas(fichas);
            case 5:
                //debe estar volteada
                repartirCartas(1);
                turnoCalles(5);
                faseApuestas(fichas);

            case 6:
                turnoCalles(6);
                formarMejorMano();
                //volter las 2 cartas ocultas
                determinarGanador();
        }
    }

    public void turnoCalles(int ronda){
        switch (ronda){
            //Third Street
            case 1:
               //Aqui se aplicaria un for donde revise las cartas de todos los jugadores para comprobar quien empieza
                // if(//aqui iria carta mas baja){

                //}
            //Fourth Street
            case 2:
                //Aqui se aplicaria un for donde revise las cartas de todos los jugadores para comprobar quien empieza


                //Fifth Street
            case 3:

            //Sixth Street
            case 4:
                //Aqui se aplicaria un for donde revise las cartas de todos los jugadores para comprobar quien empieza

            //Seventh Street
            case 5:
                //Aqui se aplicaria un for donde revise las cartas de todos los jugadores para comprobar quien empieza

            //Fase final
            case 6:
                //Aqui se aplicaria un for donde revise las cartas de todos los jugadores para comprobar quien empieza

        }
    }

    //Este metodo va hasta el final, que de las 7 cartas, el jugador elije 5 y voltea 2
    //metodo voltear y un arraylist de mejor mano
    public void formarMejorMano(){

    }
}
