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

    public Imagen(int[][] mat) {
        this.path = "\".\\\\im\\\\prueba1.pgm\"";
        this.matriz = mat;
        this.maxValor = 256;

    }

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
                System.out.print(matriz[i][j]);
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
                // Coordenadas proporcionales en la imagen original, en que posicion se
                // encuentra
                float yOriginal = (float) y / (nuevoAlto - 1) * (altoOriginal - 1);
                float xOriginal = (float) x / (nuevoAncho - 1) * (anchoOriginal - 1);

                /// se redondea para abajo para obtener el punto superior izquierdo en la
                /// imagen(matriz) original
                int y0 = (int) Math.floor(yOriginal);
                int x0 = (int) Math.floor(xOriginal);
                /// se aumentan en 1 las coordenadas obtenidas para obtener el punto inferior
                /// derecho
                /// se usa min() para evitar salirse del rango de la matriz original,
                /// si ya me encuentro en la ultima columna o fila de la matriz, yo+1 me envia
                /// una columna
                /// que no existe en la matriz original
                int y1 = Math.min(y0 + 1, altoOriginal - 1);
                int x1 = Math.min(x0 + 1, anchoOriginal - 1);

                // Diferencias de distancia entre las nuevas sub coordenadas de mi nuevo pixel
                // y los valores enteros de las coordenadas de mi punto superior izquierdo
                float dy = yOriginal - y0;
                float dx = xOriginal - x0;

                // obtengo los 4 puntos vecinos
                int Q11 = this.matriz[y0][x0];
                int Q21 = this.matriz[y0][x1];
                int Q12 = this.matriz[y1][x0];
                int Q22 = this.matriz[y1][x1];

                /// aplico interpolacion lineal en sentido horizontal tanto arriba como abajo de
                /// mi nuevo pixel
                float R1 = Q11 + ((dx) / (x1 - x0)) * (Q21 - Q11);
                float R2 = Q12 + ((dx) / (x1 - x0)) * (Q22 - Q12);
                /// con los valores obtenidos en sentido horizontal ahora aplico interp lineal
                /// en sentido vertical
                float P = R1 + ((dy) / (y1 - y0)) * (R2 - R1);

                /*
                 * ///version optima para menos cuentas
                 * float R1 = (1 - dx) * Q11 + dx * Q21;
                 * float R2 = (1 - dx) * Q12 + dx * Q22;
                 * float P = (1 - dy) * R1 + dy * R2;
                 */
                // Formula: (1 - dy) * (1 - dx) * Q11 + (1 - dy) * dx * Q21 +
                // dy * (1 - dx) * Q12 + dy * dx * Q22

                escalada[y][x] = Math.round(P);
            }
        }

        return escalada;

    }

}
