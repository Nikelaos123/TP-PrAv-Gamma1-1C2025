import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class Imagen {

    int[][] matriz;
    String path;
    int maxValor;

    public Imagen(String path) throws FileNotFoundException {
        this.path = path;
        Scanner scanner = new Scanner(new File(path));
        scanner.next();

        while (scanner.hasNext("#")) {
            scanner.nextLine();
        }

        int ancho = scanner.nextInt();
        int alto = scanner.nextInt();
        this.maxValor = scanner.nextInt();

        this.matriz = new int[alto][ancho];
        for (int i = 0; i < alto; i++) {
            for (int j = 0; j < ancho; j++) {
                if (scanner.hasNextInt()) {
                    matriz[i][j] = scanner.nextInt();
                }
            }
        }

        scanner.close();

    }

    void mostrar() {

        for (int i = 0; i < matriz.length; i++) {
            for (int j = 0; j < matriz[0].length; j++) {
                System.out.println(matriz[i][j]);
            }
            System.out.println();
        }
    }

    void guardarImg(int[][] matriz) {

        File archivo = new File(path);
        String carpeta = archivo.getAbsoluteFile().getParent();
        String nombre = archivo.getName();

        String nuevoPath = carpeta.concat("\\new_" + nombre);

        int alto = matriz.length;
        int ancho = matriz[0].length;
        try (PrintWriter out = new PrintWriter(new FileWriter(nuevoPath))) {

            // encabezado
            out.println("P2");
            out.println("# Imagen guardada");
            out.println(ancho + " " + alto);
            out.println(this.maxValor);

            for (int i = 0; i < alto; i++) {
                for (int j = 0; j < ancho; j++) {
                    out.print(matriz[i][j] + " ");
                    out.println();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    int[][] cambiarEscala(double factorEscala) {

        int altoOriginal = this.matriz.length;
        int anchoOriginal = this.matriz[0].length;

        int nuevoAlto = (int) Math.round(altoOriginal * factorEscala);
        int nuevoAncho = (int) Math.round(anchoOriginal * factorEscala);

        int[][] escalada = new int[nuevoAlto][nuevoAncho];

        for (int y = 0; y < nuevoAlto; y++) {
            for (int x = 0; x < nuevoAncho; x++) {
                // Coordenadas proporcionales en la imagen original
                float yOriginal = (float) y / (nuevoAlto - 1) * (altoOriginal - 1);
                float xOriginal = (float) x / (nuevoAncho - 1) * (anchoOriginal - 1);

                int y0 = (int) Math.floor(yOriginal);
                int x0 = (int) Math.floor(xOriginal);
                int y1 = Math.min(y0 + 1, altoOriginal - 1);
                int x1 = Math.min(x0 + 1, anchoOriginal - 1);

                float dy = yOriginal - y0;
                float dx = xOriginal - x0;

                int Q11 = this.matriz[y0][x0];
                int Q21 = this.matriz[y0][x1];
                int Q12 = this.matriz[y1][x0];
                int Q22 = this.matriz[y1][x1];

                float R1 = (1 - dx) * Q11 + dx * Q21;
                float R2 = (1 - dx) * Q12 + dx * Q22;
                float P = (1 - dy) * R1 + dy * R2;

                escalada[y][x] = Math.round(P);
            }
        }

        return escalada;

    }

}
