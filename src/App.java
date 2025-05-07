import java.io.IOException;

public class App {
    public static void main(String[] args) {
        try {
            String[] files = {"./im/feep_p2.pgm", "./im/feep_p5.pgm"};
            for (String file : files) {
                System.out.println("+ Procesando: " + file);
                Imagen im1 = new Imagen(file);
                int[][] matrizEscalada = im1.cambiarEscala(0.5);
                im1.guardarImg(matrizEscalada);
                System.out.println("+ Imagen escalada guardada para: " + file + "\n");
            }
        } catch (IOException e) {
            System.err.println("+ Error: " + e.getMessage());
        }
    }
}
