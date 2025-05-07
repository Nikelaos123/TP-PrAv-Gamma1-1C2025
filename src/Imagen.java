import java.io.*;
import java.util.Scanner;

public class Imagen {

    int[][] matriz;
    String path;
    int maxValor;
    String magic; // P2 o P5

    // Constructor para crear imagen a partir de matriz
    public Imagen(int[][] mat, String path) {
        this.matriz = mat;
        this.path = path;
        this.maxValor = 255; // Standard para 8-bit PGM --> Va de 0 a 255
        this.magic = "P2"; // Default es P2
    }

    // Constructor para leer un archivo PGM
    public Imagen(String path) throws IOException {
        this.path = path;
        File file = new File(path);
        if (!file.exists()) {
            throw new FileNotFoundException("+ Archivo no encontrado: " + path);
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            // Leer magic number
            this.magic = br.readLine().trim();
            if (!magic.equals("P2") && !magic.equals("P5")) {
                throw new IOException("+ Formato PGM no soportado: " + magic);
            }

            // Leer dimensiones, saltando comentarios
            String line;
            do {
                line = br.readLine();
                if (line == null) {
                    throw new IOException("+ Encabezado PGM incompleto");
                }
                line = line.trim();
            } while (line.startsWith("#") || line.isEmpty());

            String[] dimensions = line.split("\\s+");
            int ancho = Integer.parseInt(dimensions[0]);
            int alto = Integer.parseInt(dimensions[1]);

            // Leer max value
            do {
                line = br.readLine();
                if (line == null) {
                    throw new IOException("+ Encabezado PGM incompleto");
                }
                line = line.trim();
            } while (line.startsWith("#") || line.isEmpty());

            this.maxValor = Integer.parseInt(line);
            if (this.maxValor < 0 || this.maxValor > 255) {
                throw new IOException("+ Maximo valor soportado: " + this.maxValor);
            }

            // Inicializar matriz
            this.matriz = new int[alto][ancho];

            if (magic.equals("P2")) {
                // Leer pixeles en formato ASCII
                Scanner scanner = new Scanner(br);
                try {
                    for (int i = 0; i < alto; i++) {
                        for (int j = 0; j < ancho; j++) {
                            if (scanner.hasNextInt()) {
                                matriz[i][j] = scanner.nextInt();
                                if (matriz[i][j] < 0 || matriz[i][j] > maxValor) {
                                    throw new IOException("+ Pixel invalido: " + matriz[i][j]);
                                }
                            } else {
                                throw new IOException("+ Datos insuficientes en archivo P2.");
                            }
                        }
                    }
                } finally {
                    scanner.close();
                }
            } else {
                // Calcular la posicion del inicio de los datos binarios
                StringBuilder header = new StringBuilder();
                header.append(this.magic).append("\n");
                try (BufferedReader br2 = new BufferedReader(new FileReader(file))) {
                    String headerLine;
                    int linesRead = 0;
                    while ((headerLine = br2.readLine()) != null) {
                        headerLine = headerLine.trim();
                        if (headerLine.isEmpty()) {
                            continue;
                        }
                        header.append(headerLine).append("\n");
                        linesRead++;
                        if (linesRead == 3 && !headerLine.startsWith("#")) {
                            break; // 3 lineas para leer --> magic number, dimensiones y maxval
                        }
                    }
                }

                // Abrir archivo en modo binario y saltar el header
                try (FileInputStream fis = new FileInputStream(file)) {
                    fis.skip(header.toString().getBytes().length);

                    // Leer pixeles en formato binario
                    for (int i = 0; i < alto; i++) {
                        for (int j = 0; j < ancho; j++) {
                            int pixel = fis.read();
                            if (pixel == -1) {
                                throw new IOException("+ Datos insuficientes en archivo P5.");
                            }
                            matriz[i][j] = pixel;
                            if (matriz[i][j] < 0 || matriz[i][j] > maxValor) {
                                throw new IOException("+ Pixel invalido: " + matriz[i][j]);
                            }
                        }
                    }
                }
            }
        }
    }

    void guardarImg(int[][] matriz) {
        File archivo = new File(path);
        String carpeta = archivo.getAbsoluteFile().getParent();
        String nombre = archivo.getName();
        String nuevoPath = carpeta + File.separator + "new_" + nombre;

        int alto = matriz.length;
        int ancho = matriz[0].length;

        try (FileOutputStream fos = new FileOutputStream(nuevoPath);
             PrintWriter out = new PrintWriter(fos)) {
            // Escribir header
            out.println(this.magic);
            out.println("# Imagen guardada");
            out.println(ancho + " " + alto);
            out.println(this.maxValor);
            out.flush(); // Asegurarse que el header esta escrito antes que los datos binarios

            if (this.magic.equals("P2")) {
                // Escribir pixeles en formato ASCII
                for (int i = 0; i < alto; i++) {
                    for (int j = 0; j < ancho; j++) {
                        if (matriz[i][j] < 0 || matriz[i][j] > maxValor) {
                            throw new IOException("+ Pixel invalido al guardar: " + matriz[i][j]);
                        }
                        out.print(matriz[i][j] + " ");
                    }
                    out.println();
                }
            } else {
                // Escribir pixeles en formato binario
                for (int i = 0; i < alto; i++) {
                    for (int j = 0; j < ancho; j++) {
                        if (matriz[i][j] < 0 || matriz[i][j] > maxValor) {
                            throw new IOException("+ Pixel invalido al guardar: " + matriz[i][j]);
                        }
                        fos.write(matriz[i][j]);
                    }
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

        // Asegurarse que las nuevas dimensiones son por lo menos 1
        nuevoAlto = Math.max(1, nuevoAlto);
        nuevoAncho = Math.max(1, nuevoAncho);

        int[][] escalada = new int[nuevoAlto][nuevoAncho];

        for (int y = 0; y < nuevoAlto; y++) {
            for (int x = 0; x < nuevoAncho; x++) {
                // Mapeo de coordenadas
                float xOriginal = ((float) x / (nuevoAncho - 1)) * (anchoOriginal - 1);
                float yOriginal = ((float) y / (nuevoAlto - 1)) * (altoOriginal - 1);

                // Coordenadas de pixeles de alrededor
                int x0 = (int) Math.floor(xOriginal);
                int x1 = Math.min(x0 + 1, anchoOriginal - 1);
                int y0 = (int) Math.floor(yOriginal);
                int y1 = Math.min(y0 + 1, altoOriginal - 1);

                // Fracciones de interpolacion
                float dx = xOriginal - x0;
                float dy = yOriginal - y0;

                // Valores de pixeles vecinos
                int Q11 = this.matriz[y0][x0];
                int Q21 = this.matriz[y0][x1];
                int Q12 = this.matriz[y1][x0];
                int Q22 = this.matriz[y1][x1];

                // Interpolacion bilineal
                float R1 = (1 - dx) * Q11 + dx * Q21;
                float R2 = (1 - dx) * Q12 + dx * Q22;
                float P = (1 - dy) * R1 + dy * R2;

                escalada[y][x] = Math.round(P);
            }
        }

        return escalada;
    }
}
