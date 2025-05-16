import java.util.Random;

public class Carta {
    private int valor;
    private String figura;
    private String color;
    private String nombreArchivo;

    public Carta() {
        // Crea una carta aleatoria
        Random rnd = new Random();
        valor = rnd.nextInt(13) + 1;
        figuraRandom();
    }

    private void figuraRandom() {
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

    public Carta(int valor, String figura, String color){
        this.valor = valor;
        this.figura = figura;
        this.color = color;
    }

    public String toString(int valor, String figura){
        switch (valor) {
            case 11:
                return "J" + figura;
            case 12:
                return "Q" + figura;
            case 13:
                return "K"  + figura;
            case 14:
                return "As" + figura;
        }
        return valor + figura;
    }

    public String obtenerImagenCarta(String nombreArchivo) {
        // Idea: Vamos a obtener los valores de las cartas, y dependiendo de que carta es, se va a obtener una imagen de la carta correspondiente para el gamePanel
        return "hi";
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