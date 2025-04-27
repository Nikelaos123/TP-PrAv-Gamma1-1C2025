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
                System.out.println("[" + data[i][j] + "]");
            }
        }

    }
}
