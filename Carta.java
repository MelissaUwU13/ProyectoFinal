import java.util.Random;

public class Carta {
    private int valor;
    private String figura;
    private String nombreArchivo;
    private boolean visible;


    public Carta(int valor, String figura, boolean visible){
        this.valor = valor;
        this.figura = figura;
        this.visible = false;
    }

    public String toString() {
        String nombre = switch (valor) { // Expresión avanzada para el switch, esto me lo dio IntelliJ para ahorrar para optimizar el código
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

}