import edu.princeton.cs.algs4.Queue;

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

        // instead of use running index
        // i'll directly compute it using:
        // (j + 1) + (i * n)
        int outOfPlace = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if ((tiles[i][j] != (j + 1) + (i * n)) && tiles[i][j] != 0)
                    outOfPlace++;
            }
        }
        return outOfPlace;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        int outOfPlace = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                // storing the index so i don't have to recompute it
                int index = (j + 1) + (i * n); // the ideal number there
                if (tiles[i][j] != index && tiles[i][j] != 0) {
                    int idealrow = (tiles[i][j] - 1) / n;
                    int idealcolumn = (tiles[i][j] - 1) % n;
                    outOfPlace += (Math.abs((idealrow - i)) + Math.abs((idealcolumn - j)));
                }
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

        Queue<Board> q = new Queue<>();

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
            q.enqueue(bd);
        }
        else if (row == (n - 1)) {
            exch(tiles, row, col, row - 1, col);
            Board bd = new Board(tiles);
            exch(tiles, row, col, row - 1, col);
            q.enqueue(bd);
        }
        else {

            exch(tiles, row, col, row - 1, col);
            Board bd = new Board(tiles);
            exch(tiles, row, col, row - 1, col);
            q.enqueue(bd);

            exch(tiles, row, col, row + 1, col);
            bd = new Board(tiles);
            exch(tiles, row, col, row + 1, col);
            q.enqueue(bd);
        }

        if (col == 0) {
            exch(tiles, row, col, row, col + 1);
            Board bd = new Board(tiles);
            exch(tiles, row, col, row, col + 1);
            q.enqueue(bd);
        }
        else if (col == (n - 1)) {
            exch(tiles, row, col, row, col - 1);
            Board bd = new Board(tiles);
            exch(tiles, row, col, row, col - 1);
            q.enqueue(bd);
        }
        else {
            exch(tiles, row, col, row, col + 1);
            Board bd = new Board(tiles);
            exch(tiles, row, col, row, col + 1);
            q.enqueue(bd);

            exch(tiles, row, col, row, col - 1);
            bd = new Board(tiles);
            exch(tiles, row, col, row, col - 1);
            q.enqueue(bd);
        }

        return q;
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {

        int x1 = 0;
        int y1 = 0;
        if (tiles[x1][y1] == 0) {
            x1++;
            y1++;
        }
        int x2;
        int y2 = y1;
        if (x1 == 0) {
            x2 = x1 + 1;
        }
        else {
            x2 = x1 - 1;
        }
/*      // (x1 == n - 1) //// else if condition
        else { // The else if and if can be combined because it ain't matter
            x2 = x1 + 1;
        }*/
        if (tiles[x2][y2] == 0) {
            x2 = x1;
            if (y1 == 0) {
                y2 = y1 + 1;
            }
            else if (y1 == n - 1) {
                y2 = y1 - 1;
            }
        }

        // The twin method should return the same regardless of its calling order

        /*
        int x1 = StdRandom.uniform(0, n);
        int y1 = StdRandom.uniform(0, n);

        while (tiles[x1][y1] == 0) {
            x1 = StdRandom.uniform(0, n);
            y1 = StdRandom.uniform(0, n);
        }


        int x2 = x1;
        int y2 = y1;
        do {
            // USE RNJESUS FOR PICKING TWINS
            // BWAHAHA

            int randpicker = StdRandom.uniform(0, 2);
            if (randpicker == 0) {
                if (x1 == 0) {
                    x2 = x1 + 1;
                }
                else if (x1 == n - 1) {
                    x2 = x1 - 1;
                }
                else {
                    int rand = StdRandom.uniform(0, 2);
                    if (rand == 0) {
                        x2 = x1 + 1;
                    }
                    else {
                        x2 = x1 - 1;
                    }
                }
            }
            else {
                if (y1 == 0) {
                    y2 = y1 + 1;
                }
                else if (y1 == n - 1) {
                    y2 = y1 - 1;
                }
                else {
                    int rand = StdRandom.uniform(0, 2);
                    if (rand == 0) {
                        y2 = y1 + 1;
                    }
                    else {
                        y2 = y1 - 1;
                    }
                }
            }

            // the blank tiles is not a tile
        } while (tiles[x2][y2] == 0);
        */

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
