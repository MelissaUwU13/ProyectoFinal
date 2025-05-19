import java.util.ArrayList;

public abstract class EvaluarMano implements Jerarquia{

    public int analizarMano(ArrayList<Carta> mano){

        int puntuacion=0;
        if(esEscaleraReal(mano)){
            puntuacion=10;
            //System.out.println("Es una escalera real!!!!");
        }
        else if(sonDelMismoPalo(mano) && esEscalera(mano)){
            puntuacion=9;
            //System.out.println("Es una escalera corrida!!");
        }
        else if(hayNRepetidas(mano, 4)){
            puntuacion=8;
            //System.out.println("Hay 4 repetidas!!");
        }
        else if(esFullHouse(mano)){
            puntuacion=7;
            //System.out.println("Es un full house!!");
        }
        else if(sonDelMismoPalo(mano)){
            puntuacion=6;
            //System.out.println("Es un flush!!!");
        }
        else if(esEscalera(mano)){
            puntuacion=5;
            //System.out.println("Es una escalera!!");
        }
        else if(hayNRepetidas(mano, 3)){
            puntuacion=4;
            //System.out.println("Hay 3 repetidas!!");
        }
        else if(doblePar(mano)){
            puntuacion=3;
            //System.out.println("Hay dos pares!!");
        }
        else if(hayUnPar(mano)){
            puntuacion=2;
        }
        else{
            //System.out.println("Carta Alta!!");
            puntuacion=1;
        }

        return puntuacion;
    }


    public boolean esEscaleraReal(ArrayList<Carta> mano) {
        if (mano.size() < 5) return false;
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
        if (mano.size() < 5) return false;

        ordenar(mano);
        boolean hayEscalera = true;

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

        return pares == 2;
    }

    public boolean sonDelMismoPalo(ArrayList<Carta> mano){
        if (mano.size() < 5) return false;
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

    public boolean esFullHouse(ArrayList<Carta> mano) {
        int[] conteo = new int[14]; // índice 1 al 13
        for (Carta c : mano) {
            conteo[c.getValor()]++;
        }

        boolean tieneTrio = false;
        boolean tienePar = false;

        for (int i = 1; i <= 13; i++) {
            if (conteo[i] == 3) {
                tieneTrio = true;
            } else if (conteo[i] == 2) {
                tienePar = true;
            }
        }

        return tieneTrio && tienePar;
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

    public String interpretarPuntuación(int puntuacion) {
        switch (puntuacion) {
            case 1:
                return "Carta Alta";
            case 2:
                return "Par";
            case 3:
                return "Doble Par";
            case 4:
                return "Trio";
            case 5:
                return "Escalera";
            case 6:
                return "Flush";
            case 7:
                return "Full House";
            case 8:
                return "Poker";
            case 9:
                return "Escalera Corrida";
            case 10:
                return "Escalera Real";
            default:
                return "Error";
        }
    }
}