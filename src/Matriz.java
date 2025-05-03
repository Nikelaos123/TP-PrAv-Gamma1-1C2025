public class Matriz {

    private int data[][];

    public Matriz() {

    }

    public Matriz(int[][] copia) {
        data = copia.clone();
    }

    public Matriz(Matriz otra) {
        this.data = otra.getData();
    }

    public int[][] getData() {
        return data.clone();
    }

    public void mostrarMatriz() {

        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data[i].length; j++) {
                System.out.print("[" + data[i][j] + "]");
            }
            System.out.println("");
        }

    }

    public int obtenerValorEscalado(int x, int y) {
        float a = (x-data[y][x-1])/(data[y][x+1] - data[y][x-1] );
        float b = (y-data[y-1][x])/(data[y+1][x] - data[y-1][x] );

        return (int) (  (1-a)*(1-b)*data[y][x] + 
                        (a)*(1-b)*data[y][x+1] + 
                        (1-a)*(b)*data[y+1][x] + 
                        (a)*(b)*data[y+1][x+1] );
    }

    public int[][] retornarEscalada(int valorEscalado){

        int xEsc = valorEscalado * data[0].length;
        int yEsc = valorEscalado * data.length;
        int nueva[][] = new int[yEsc][xEsc];

        for(int i = 0; i < yEsc; i++) {
            for (int j = 0; j < xEsc; j++) {
                //nueva[i][j] = data[i/valorEscalado][j/valorEscalado];

                nueva[i][j] = this.obtenerValorEscalado(i, j);
            }
        }


        return nueva;
    }
}
