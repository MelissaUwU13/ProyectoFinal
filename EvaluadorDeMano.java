import java.util.ArrayList;

public abstract class EvaluadorDeMano implements Jerarquia{

    public int analizarMano(ArrayList<Carta> mano){
        // inicializamos la puntuación de la mano como 0
        int puntuacion;
        if(esEscaleraReal(mano)){
            // Escalera real
            puntuacion=10;
        }
        else if(sonDelMismoPalo(mano) && esEscalera(mano)){
            // Escalera de color
            puntuacion=9;
        }
        else if(hayNRepetidas(mano, 4)){
            // Póker
            puntuacion=8;
        }
        else if(esFullHouse(mano)){
            // Full house
            puntuacion=7;
        }
        else if(sonDelMismoPalo(mano)){
            // Color
            puntuacion=6;
        }
        else if(esEscalera(mano)){
            // Escalera normal
            puntuacion=5;
        }
        else if(hayNRepetidas(mano, 3)){
            // Tercia o trio
            puntuacion=4;
        }
        else if(doblePar(mano)){
            // Doble par
            puntuacion=3;
        }
        else if(hayUnPar(mano)){
            // Par
            puntuacion=2;
        }
        else{
            // Carta Alta
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

    // Estos métodos sirven para identificar el tipo de jugada en la mano de cartas dada

    public boolean hayUnPar(ArrayList<Carta> mano) {
        boolean hayUnPar = false;
        // Aquí identificamos si hay un par en la mano
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
        // Aquí identificamos si la mano contiene dos pares
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
        // Aquí identificamos si todas las cartas de la mano contienen la misma figura
        for (int i=0; i<mano.size()-1; i++) {
            Carta carta1 = mano.get(i);
            Carta carta2 = mano.get(i+1);
            if (!carta1.getFigura().equals(carta2.getFigura())) {
                return false;
            }
        }

        return mismoPalo;
    }

    public boolean hayNRepetidas(ArrayList<Carta> mano,int n) {
        // Aquí identificamos si todas las cartas son iguales un cierto número de veces. La usamos para la tercia y el Poker
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
        // Como lo dice el nombre, aquí revisamos si hay un full house en la mano dada
        int[] conteo = new int[14];
        for (Carta carta : mano) {
            conteo[carta.getValor()]++;
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
        // Con esto ordenamos los valores de las cartas, para las escaleras
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

    public String interpretarPuntuacion(int puntuacion) {
        // Aquí volvemos a interpretar las puntuaciones de las jugadas, muy util para decirle al jugador que tipo de jugada realizó
        switch (puntuacion) {
            case 1:
                return "Carta Alta";
            case 2:
                return "Par";
            case 3:
                return "Doble Par";
            case 4:
                return "Tercia";
            case 5:
                return "Escalera";
            case 6:
                return "Color";
            case 7:
                return "Full House";
            case 8:
                return "Poker";
            case 9:
                return "Escalera de Color";
            case 10:
                return "Escalera Real";
            default:
                return "Error";
        }
    }
}