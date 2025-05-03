import java.io.IOException;

public class App {

    public static void main(String[] args) throws IOException {
        Imagen im1 = new Imagen(".\\im\\1.pgm");

        im1.guardarImg(im1.cambiarEscala(3));


        /*
        System.out.println("Matriz Original: ");
        int[][] original = {
            {1, 5},
            {5, 1}
        };
        Matriz m1 = new Matriz(original);

        m1.mostrarMatriz();

        int valorEscalado = 3;
        System.out.println();

        System.out.println("Matriz Escalada * "+ valorEscalado +": ");
        

        Matriz m2 = new Matriz(m1.retornarEscalada(valorEscalado));
        m2.mostrarMatriz();

        */
    }

}