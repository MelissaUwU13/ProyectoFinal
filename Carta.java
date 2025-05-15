import java.util.Random;

public class Carta {
    private int valor;
    private String figura;
    private String color;

    public Carta() {
        // Crea una carta aleatoria
        Random rnd = new Random();
        valor = rnd.nextInt(14) + 1;
        makeRandomFigure();
        if (valor==14) {
            figura = "joker";
            color = "transparente";
        }
    }

    private void makeRandomFigure() {

        Random rnd = new Random();
        int numberFigure = rnd.nextInt(4);
        switch (numberFigure) {
            case 0:
                figura = "corazón";
                color = "rojo";
                break;
            case 1:
                figura = "trebol";
                color = "negro";
                break;
            case 2:
                figura = "diamante";
                color = "rojo";
                break;
            case 3:
                figura = "pica";
                color = "negro";
                break;
        }

    }

    public Carta(int valor, String figura, String color) {
        this.valor = valor;
        this.figura = figura;
        this.color = color;
    }

    public String toString() {
        String laCarta;
        switch (valor) {
            case 1:
                laCarta = figura + "A";
                break;
            case 11:
                laCarta = figura + "J";
                break;
            case 12:
                laCarta = figura + "Q";
                break;
            case 13:
                laCarta = figura + "K";
                break;
            case 14:
                laCarta = "Joker";
                break;
            default:
                laCarta = figura + valor;
        }
        return laCarta;
    }

    public int getValor() {
        return valor;
    }

    public String getFigura() {
        return figura;
    }

    public String getColor() {
        return color;
    }

    public void voltearCarta(){

    }
}
