import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class ReproductorMusica {
    private static Clip musicaActual;

    public static void reproducir(String ruta) {
        try {
            if (musicaActual != null && musicaActual.isRunning()) {
                musicaActual.stop();
                musicaActual.close();
            }
            AudioInputStream audio = AudioSystem.getAudioInputStream(new File(ruta));
            musicaActual = AudioSystem.getClip();
            musicaActual.open(audio);
            musicaActual.loop(Clip.LOOP_CONTINUOUSLY);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}