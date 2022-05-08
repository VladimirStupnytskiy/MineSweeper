import java.util.ArrayList;

public class BattleField {
    int n = 1;
    int m = 1;
    int bombNumber = 0;
    private Cell[][] cells;
    private ArrayList <Cell> bombs = new ArrayList<Cell>();

    public ArrayList<Cell> getBombs() {
        return bombs;
    }

    public Cell[][] getCells() {
        return cells;
    }

    public void setN(int n) {
        this.n = n;
    }

    public void setBombNumber(int bombNumber) {
        this.bombNumber = bombNumber;
    }

    public void setM(int m) {
        this.m = m;
    }

    public BattleField() {
    }

    public BattleField(int n, int m) {
        this.n = n;
        this.m = m;
        cells = new Cell[n][m];

        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells[i].length; j++) {
                cells[i][j] = new Cell(i, j);

            }
        }
    }

    public void setBombToCell() {
        int k = bombNumber;
        while (k > 0) {
            int i = (int) (Math.random() * n); // *3 - це до 2 включно
            int j = (int) (Math.random() * m); // *3 - це до 2 включно
            if (!cells[i][j].isBomb()) {
                cells[i][j].setBomb(true);
                k--;
                bombs.add(cells[i][j]);

            }
        }
    }

    public void printToConsole() {
        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells[i].length; j++) {
                if (cells[i][j].isBomb()) {
                    System.out.print(" x ");
                } else System.out.print(" " + cells[i][j].radiusBomb + " ");
            }
            System.out.println("");
        }
    }

    public void setR() {
        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells[i].length; j++) {
                if (cells[i][j].isBomb()) {
                    for (int k = i - 1; k <= i + 1; k++) {
                        for (int l = j - 1; l <= j + 1; l++) {
                            try {
                                cells[k][l].incBomb();
                            } catch (ArrayIndexOutOfBoundsException e) {
                            }
                        }
                    }
                }
            }
        }
    }
}
