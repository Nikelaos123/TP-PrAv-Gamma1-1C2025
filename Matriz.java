
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

    private int[][] getData() {
        return data.clone();
    }
}
