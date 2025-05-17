import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

//QUIZAS HACERLA ABSTRACTAAA 0.0
public class Poker {
    protected ArrayList<Carta> mazo;
    protected ArrayList<Jugador> jugadores;
    protected ArrayList<Integer> apuestas;
    protected int puntuacion;

    public boolean determinarGanador(){
        return false;
    }

    //reparte N cartas
    public void repartirCartas(int N){
        for (Jugador j : jugadores) {
            for (int i = 0; i < N; i++) {
                j.recibirCarta(mazo.remove(0));
            }
        }
    }

    public List<Jugador> getJugadores() {
        return jugadores;
    }

    public void mostrarManos() {
        int num = 1;
        for (Jugador j : jugadores) {
            System.out.println("Mano del " + j.getNombre() + ":");
            for (Carta c : j.getMano()) {
                System.out.println("- " + c); // Asegúrate de que Carta tenga un buen toString()
            }
            System.out.println(); // Espacio entre jugadores
            num++;
        }
    }

    public void mostrarMano(Jugador jugador) {
        for (int i = 0; i < jugador.getMano().size(); i++) {
            System.out.println(i + ": " + jugador.getMano().get(i));
        }
    }


    public void generarBaraja(){
        mazo= new ArrayList<>();

        String[] figuras = {"corazon", "trebol", "diamante", "pica"};
        for (String figura : figuras) {
            for (int j = 1; j <= 13; j++) {
                mazo.add(new Carta(j, figura, "transparente"));
            }
        }
        Collections.shuffle(mazo);
    }


    public void tablaDePuntuaciones(ArrayList<Carta> mano){
        if(esEscaleraReal(mano)==true){
            puntuacion=10;
        }
        else if(sonDelMismoPalo(mano)==true && esEscalera(mano)==true){
                puntuacion=9;

        }
        else if(hayNRepetidas(mano,4)==true){
            puntuacion=8;
        }
        else if(hayNRepetidas(mano,3)==true && hayUnPar(mano)==true){
            puntuacion=7;
        }
        else if(sonDelMismoPalo(mano)==true){
            puntuacion=6;
        }
        else if(esEscalera(mano)==true){
            puntuacion=5;
        }
        else if(hayNRepetidas(mano,3)==true){
            puntuacion=4;
        }
        else if(doblePar(mano)==true){
            puntuacion=3;
        }
        else if(hayUnPar(mano)==true){
            puntuacion=2;
        }
        //EN CASO DE QUE NO SEA NINGUN, GANA EL JUGADOR CON LA CARTA MAS ALTA, OSEA CON A,REY, REINA, etc....
        //else if(sonDelMismoPalo()==true){
          //  puntuacion=1;
        //}
    }

    public void analizarMano(ArrayList<Carta> mano){
        if(esEscaleraReal(mano)==true){
            puntuacion=10;
            System.out.println("Es una escalera real!!!!");
        }
        else if(sonDelMismoPalo(mano)==true && esEscalera(mano)==true){
            puntuacion=9;
            System.out.println("Es una escalera corrida!!");
        }
        else if(hayNRepetidas(mano,4)==true){
            puntuacion=8;
            System.out.println("Hay 4 repetidas!!");
        }
        else if(hayNRepetidas(mano,3)==true && hayUnPar(mano)==true){
            puntuacion=7;
            System.out.println("Es un full house!!");
        }
        else if(sonDelMismoPalo(mano)==true){
            puntuacion=6;
            System.out.println("Es un flush!!!");
        }
        else if(esEscalera(mano)==true){
            puntuacion=5;
            System.out.println("Es una escalera!!");
        }
        else if(hayNRepetidas(mano,3)==true){
            puntuacion=4;
            System.out.println("Hay 3 repetidas!!");
        }
        else if(doblePar(mano)==true){
            puntuacion=3;
            System.out.println("Hay dos pares!!");
        }
        else if(hayUnPar(mano)==true){
            puntuacion=2;
            if(hayUnPar(mano)) System.out.println("Hay un par!!");
        } else System.out.println("Carta Alta!!");
    }


   public boolean esEscaleraReal(ArrayList<Carta> mano) {
       ordenar(mano);
       String palo = mano.get(0).getFigura();
       int[] valores = {10, 11, 12, 13, 1};

       for (int i = 0; i < mano.size(); i++) {
           if (mano.get(i).getValor() != valores[i]){
               return false;
           }

           if (!mano.get(i).getFigura().equals(palo)){
               return false;
           }
       }
       return true;
   }

    public boolean esEscalera(ArrayList<Carta> mano) {
        boolean hayEscalera = true;
        ordenar(mano);

        for (int i=0; i<mano.size()-1; i++) {
            Carta carta1 = mano.get(i);
            Carta carta2 = mano.get(i+1);
            if (carta1.getValor() != carta2.getValor()-1) {
                return false;
            }
        }

        return hayEscalera;
    }

    public boolean hayUnPar(ArrayList<Carta> mano) {
        boolean hayUnPar = false;

        for (int i=0; i<mano.size()-1; i++) {
            for (int j=i+1; j<mano.size(); j++) {
                Carta carta1 = mano.get(i);
                Carta carta2 = mano.get(j);
                if (carta1.getValor() == carta2.getValor()) {
                    return true;
                }
            }
        }
        return hayUnPar;
    }

    public boolean doblePar(ArrayList<Carta> mano) {
        int pares = 0;
        ArrayList<Integer> yaContados = new ArrayList<>();

        for (int i = 0; i < mano.size(); i++) {
            for (int j = i + 1; j < mano.size(); j++) {
                if (mano.get(i).getValor() == mano.get(j).getValor() &&
                        !yaContados.contains(mano.get(i).getValor())) {
                    pares++;
                    yaContados.add(mano.get(i).getValor());
                }
            }
        }

        if(pares == 2){
            return true;
        }

        return false;
    }

    public boolean sonDelMismoPalo(ArrayList<Carta> mano){
        boolean mismoPalo = true;
        for (int i=0; i<mano.size()-1; i++) {
            Carta carta1 = mano.get(i);
            Carta carta2 = mano.get(i+1);
            if (!carta1.getFigura().equals(carta2.getFigura())) {
                return false;
            }
        }

        return mismoPalo;
    }

    //sirve para 3 o 4 iguales
    public boolean hayNRepetidas(ArrayList<Carta> mano,int n) {
        for (int i = 0; i < mano.size(); i++) {
            int contador = 1;
            for (int j = 0; j < mano.size(); j++) {
                if (i != j && mano.get(i).getValor() == mano.get(j).getValor()) {
                    contador++;
                }
            }
            if (contador == n) {
                return true;
            }
        }
        return false;
    }

    public void ordenar(ArrayList<Carta> mano) { // Bubble sort
        for (int i=0; i<mano.size()-1; i++) {
            for (int j=i+1; j<mano.size(); j++) {

                Carta cartaA = mano.get(i);
                Carta cartaB = mano.get(j);

                // if (cartas.get(i).getValor() > cartas.get(j).getValor() )
                if (cartaA.getValor() > cartaB.getValor()) {
                    // intercambio
                    Carta temp;

                    temp = mano.get(i);
                    mano.set(i, mano.get(j));
                    mano.set(j, temp);
                }
            }
        }
    }


}