import java.util.Random;

public class Carta {
    private int valor;
    private String figura;
    private String nombreArchivo;
    private boolean visible;

    private void figuraRandom() {
        Random rnd = new Random();
        int numberFigure = rnd.nextInt(4);
        switch (numberFigure) {
            case 0:
                figura = "corazón";
                break;
            case 1:
                figura = "trebol";
                break;
            case 2:
                figura = "diamante";
                break;
            case 3:
                figura = "pica";
                break;
        }

    }

    public Carta(int valor, String figura, boolean visible){
        this.valor = valor;
        this.figura = figura;
        this.visible = false;
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

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public void voltearCarta(){
        this.visible = !this.visible;
    }

}