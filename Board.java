import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdRandom;

public class Board {
    private int[][] tiles;
    private int n;
    private int zx;
    private int zy;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        n = tiles.length;
        this.tiles = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                this.tiles[i][j] = tiles[i][j];
                if (this.tiles[i][j] == 0) {
                    zx = j;
                    zy = i;
                }
            }
        }
    }

    // string representation of this board
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(n + "\n");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                s.append(String.format("%2d ", tiles[i][j]));
            }
            s.append("\n");
        }
        return s.toString();
    }

    // board dimension n
    public int dimension() {
        return n;
    }

    // number of tiles out of place
    public int hamming() {
        int index = 1;
        int outOfPlace = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (tiles[i][j] != index && tiles[i][j] != 0)
                    outOfPlace++;
                index++;
            }
        }
        return outOfPlace;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        int index = 1;
        int outOfPlace = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (tiles[i][j] != index && tiles[i][j] != 0) {
                    int idealrow = index / n;
                    int idealcolumn = (index - 1) % n;
                    outOfPlace += (Math.abs((idealrow - i)) + Math.abs((idealcolumn - j)));
                }
                index++;
            }
        }
        return outOfPlace;
    }

    // is this board the goal board?
    public boolean isGoal() {
        boolean valid = true;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (tiles[i][j] != ((j + 1) + (i * n)) && tiles[i][j] != 0) {
                    valid = false;
                    break;
                }
            }
            if (!valid) {
                break;
            }
        }
        return valid;
    }

    // does this board equal y?

    // Are we comparing the boards or the actual "board" internal representation

    public boolean equals(Object y) {
        if (this == y)
            return true;
        if (y == null)
            return false;
        if (this.getClass() != y.getClass())
            return false;

        Board that = (Board) y;

        if (that.dimension() != this.dimension()) {
            return false;
        }

        /*
        if (that.manhattan() != this.manhattan() || that.hamming() != this.hamming()) {
            return false;
        }

        if (that.neighbors().equals(this.neighbors())) {
            return false;
        }

        if (!(that.twin().equals(this.twin()))) {
            return false;
        }
       */

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (that.tiles[i][j] != this.tiles[i][j])
                    return false;
            }
        }

        /*
        if (Arrays.deepEquals(tiles, that)) {
            return true;
        }
        */
        return true;
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {

        Stack<Board> st = new Stack<Board>();

        int row = zy;
        int col = zx;
 /*       boolean found = false;

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (tiles[i][j] == 0) {
                    row = i;
                    col = j;
                    found = true;
                    break;
                }
            }
            if (found)
                break;
        }*/

        if (row == 0) {
            exch(tiles, row, col, row + 1, col);
            Board bd = new Board(tiles);
            exch(tiles, row, col, row + 1, col);
            st.push(bd);
        }
        else if (row == (n - 1)) {
            exch(tiles, row, col, row - 1, col);
            Board bd = new Board(tiles);
            exch(tiles, row, col, row - 1, col);
            st.push(bd);
        }
        else {

            exch(tiles, row, col, row - 1, col);
            Board bd = new Board(tiles);
            exch(tiles, row, col, row - 1, col);
            st.push(bd);

            exch(tiles, row, col, row + 1, col);
            Board bd2 = new Board(tiles);
            exch(tiles, row, col, row + 1, col);
            st.push(bd2);
        }

        if (col == 0) {
            exch(tiles, row, col, row, col + 1);
            Board bd = new Board(tiles);
            exch(tiles, row, col, row, col + 1);
            st.push(bd);
        }
        else if (col == (n - 1)) {
            exch(tiles, row, col, row, col - 1);
            Board bd = new Board(tiles);
            exch(tiles, row, col, row, col - 1);
            st.push(bd);
        }
        else {
            exch(tiles, row, col, row, col + 1);
            Board bd = new Board(tiles);
            exch(tiles, row, col, row, col + 1);
            st.push(bd);

            exch(tiles, row, col, row, col - 1);
            Board bd2 = new Board(tiles);
            exch(tiles, row, col, row, col - 1);
            st.push(bd2);
        }

        return st;
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        int x1 = StdRandom.uniform(0, n);
        int y1 = StdRandom.uniform(0, n);

        while (tiles[x1][y1] == 0) {
            x1 = StdRandom.uniform(0, n);
            y1 = StdRandom.uniform(0, n);
        }

        int x2 = StdRandom.uniform(0, n);
        int y2 = StdRandom.uniform(0, n);

        while (tiles[x1][y1] == 0 || tiles[x1][y1] == tiles[x2][y2]) {
            x2 = StdRandom.uniform(0, n);
            y2 = StdRandom.uniform(0, n);
        }

        exch(tiles, x1, y1, x2, y2);
        Board bd = new Board(tiles);
        exch(tiles, x1, y1, x2, y2);

        return bd;
    }

    private static void exch(int[][] arr, int x1, int y1, int x2, int y2) {
        int swap = arr[x1][y1];
        arr[x1][y1] = arr[x2][y2];
        arr[x2][y2] = swap;
    }
    /*

    // unit testing (not graded)
    public static void main(String[] args) {

    }
    */

}
