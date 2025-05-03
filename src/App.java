import java.io.IOException;

public class App {

    public static void main(String[] args) throws IOException {
        Imagen im1 = new Imagen(".\\im\\1.pgm");

        im1.guardarImg(im1.cambiarEscala(3));


        /*
        import java.io.FileNotFoundException;
        import java.io.IOException;

        public class App {

        public static void main(String[] args) throws IOException {
            Imagen im1 = new Imagen(".\\im\\1.pgm");

        im1.guardarImg(im1.cambiarEscala(3));

        */
    }

}