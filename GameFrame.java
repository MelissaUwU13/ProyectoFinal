import javax.swing.JFrame;
public class GameFrame extends JFrame {
    public GameFrame() {
        setTitle("Poker");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setContentPane(new PanelJuegos());
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
    public static void main(String[] args) {
        new GameFrame();
//        Poker5CardDraw juego = new Poker5CardDraw();
//        juego.jugar();
    }
}