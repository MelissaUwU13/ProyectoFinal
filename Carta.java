import java.util.Random;

public class Carta {
    private int valor;
    private String figura;
    private String color;
    private String nombreArchivo;

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

    public String toString() {
        String nombre = switch (valor) {
            case 1 -> "As";
            case 11 -> "J";
            case 12 -> "Q";
            case 13 -> "K";
            default -> String.valueOf(valor);
        };
        return nombre + figura;
    }

    public int getValor() {
        return valor;
    }

    public String getFigura() {
        return figura;
    }

    public void voltearCarta(){

    }

    public String obtenerImagenCarta(String nombreArchivo) {
        // Idea: Vamos a obtener los valores de las cartas, y dependiendo de que carta es, se va a obtener una imagen de la carta correspondiente para el gamePanel
        return "hi";
    }
}