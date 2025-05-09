import java.io.IOException;

public class App {
    private static final double FACTOR_ESCALA = 0.5;

    public static void main(String[] args) {
        System.out.println("\n\n+---------- Interpolacion bilineal ----------+\n");
        
        try {
            String[] files = {"./im/feep_p2.pgm", "./im/feep_p5.pgm"};
            for (String file : files) {
                System.out.println("+ Procesando imagen: " + file);
                Imagen im1 = new Imagen(file);
                int[][] matrizEscalada = im1.cambiarEscala(FACTOR_ESCALA);
                im1.guardarImg(matrizEscalada);
                System.out.println("\t+ Imagen reescalada y guardada.\n");
            }
        } catch (IOException e) {
            System.err.println("+ Error: " + e.getMessage());
        }
    }
}
