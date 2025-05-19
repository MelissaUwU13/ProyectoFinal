import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        boolean error = false;
        Scanner sc = new Scanner(System.in);
        int cantJugadores = 0;

        while (!error) {
            System.out.println("Ingrese la cantidad de jugadores(2-8):  ");
            try {
                cantJugadores = sc.nextInt();
                if (cantJugadores < 2 || cantJugadores > 8) {
                    System.out.println("Cantidad erronea, vuelvelo a intentar ");
                    error = false;
                } else {
                    error = true;
                }
            } catch (Exception e) {
                System.out.println("Error: Entrada no valida");
            }
        }

        error = false;
        int fichasTotales = 0;

        while (!error) {
            System.out.println("Con cuantas fichas quieres iniciar para cada jugador? MIN 500 - MAX 1000");
            try {
                fichasTotales = sc.nextInt();
                if (fichasTotales < 500 || fichasTotales > 1000) {
                    System.out.println("Cantidad erronea, vuelvelo a intentar ");
                    error = false;
                } else {
                    error = true;
                }
            } catch (Exception e) {
                System.out.println("Error: Entrada no valida");
            }
        }

        System.out.println("Antes de iniciar tenemos nuestra primera apuesta obligatoria!");
        System.out.println("La apuesta inicial es de 5 fichas!");
        fichasTotales=fichasTotales-5;

        //Poker7CardStud juego = new Poker7CardStud(cantJugadores, fichasTotales, nombreJu);
    }
}