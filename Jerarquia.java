import java.util.ArrayList;

public interface Jerarquia {
    int analizarMano(ArrayList<Carta> mano);
    boolean esEscaleraReal(ArrayList<Carta> mano);
    boolean esEscalera(ArrayList<Carta> mano);
    boolean hayUnPar(ArrayList<Carta> mano);
    boolean doblePar(ArrayList<Carta> mano);
    boolean sonDelMismoPalo(ArrayList<Carta> mano);
    boolean hayNRepetidas(ArrayList<Carta> mano, int n);
    void ordenar(ArrayList<Carta> mano);
}
