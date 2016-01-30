/**
 * Created by serge on 13.01.2016.
 */

public class Game {

    final int SIZEFIELD = 4;
    int field[][] = new int[SIZEFIELD][SIZEFIELD];
    int randomRow, randomColumn;
    static int highscores;
    static int scores;

    public void initializeField() {
        for (int i = 0; i < SIZEFIELD; i++)
            for (int j = 0; j < SIZEFIELD; j++)
                field[i][j] = 0;

        randomRow = (int) (Math.random() * SIZEFIELD);
        randomColumn = (int) (Math.random() * SIZEFIELD);
        field[randomRow][randomColumn] = 2;

        int tempRow, tempColumn;
        do {
            tempRow = (int) (Math.random() * SIZEFIELD);
            tempColumn = (int) (Math.random() * SIZEFIELD);
        }
        while ((tempRow == randomRow) || (tempColumn == randomColumn));
        field[tempRow][tempColumn] = 2;
    }

    public void addNewCell() {
        do {
            randomRow = (int) (Math.random() * SIZEFIELD);
            randomColumn = (int) (Math.random() * SIZEFIELD);
        }
        while (field[randomRow][randomColumn] != 0);

        int rand = (int) (Math.random() * 10 + 1);
        if (rand == 10)
            field[randomRow][randomColumn] = 4;
        else
            field[randomRow][randomColumn] = 2;
    }

    public int getNumberfree() {
        int count = 0;
        for (int i = 0; i < SIZEFIELD; i++)
            for (int j = 0; j < SIZEFIELD; j++)
                if (field[i][j] == 0)
                    count++;
        return count;
    }

    public void printField() {
        for (int i = 0; i < SIZEFIELD; i++) {
            for (int j = 0; j < SIZEFIELD; j++) {
                if (field[i][j] == 0)
                    System.out.print("- ");
                else
                    System.out.print(field[i][j] + " ");
            }
            System.out.println();
        }
    }

    public boolean canMoveUp() {
        for (int i = 0; i < SIZEFIELD; i++) {
            if (canMoveColumnUp(i))
                return true;
            if (canSumColumn(i))
                return true;
        }
        return false;
    }

    public void moveUp() {
        for (int i = 0; i < SIZEFIELD; i++)
            columnUp(i);
    }

    public boolean canMoveDown() {
        for (int i = 0; i < SIZEFIELD; i++) {
            if (canMoveColumnDown(i))
                return true;
            if (canSumColumn(i))
                return true;
        }
        return false;
    }

    public void moveDown() {
        for (int i = 0; i < SIZEFIELD; i++)
            columnDown(i);
    }

    public boolean canMoveLeft() {
        for (int i = 0; i < SIZEFIELD; i++) {
            if (canMoveRowLeft(i))
                return true;
            if (canSumRow(i))
                return true;
        }
        return false;
    }

    public void moveLeft() {
        for (int i = 0; i < SIZEFIELD; i++)
            rowLeft(i);
    }

    public boolean canMoveRight() {
        for (int i = 0; i < SIZEFIELD; i++) {
            if (canMoveRowRight(i))
                return true;
            if (canSumRow(i))
                return true;
        }
        return false;
    }

    public void moveRight() {
        for (int i = 0; i < SIZEFIELD; i++)
            rowRight(i);
    }

    public void rowLeft(int numberRow) {
        int i, j;
        int temp = 0;
        for (int k = 0; k < SIZEFIELD; k++) {
            // поиск первого ненулевого значения, начиная с левого края
            for (i = k; i < SIZEFIELD; i++) {
                if (field[numberRow][i] != 0) {
                    temp = i;
                    break;
                }
            }
            // первый сдвиг
            for (j = 0; j < temp; j++) {
                if (field[numberRow][j] == 0) {
                    field[numberRow][j] = field[numberRow][temp];
                    field[numberRow][temp] = 0;
                }
            }
        }
        // сумма, если одинаковые
        for (j = 0; j < SIZEFIELD - 1; j++) {
            if (field[numberRow][j] == field[numberRow][j + 1]) {
                field[numberRow][j] *= 2;
                field[numberRow][j + 1] = 0;
                scores += field[numberRow][j];
            }
        }
        for (int k = 0; k < SIZEFIELD; k++) {
            for (i = k; i < SIZEFIELD; i++) {
                if (field[numberRow][i] != 0) {
                    temp = i;
                    break;
                }
            }
            // второй сдвиг
            for (j = 0; j < temp; j++) {
                if (field[numberRow][j] == 0) {
                    field[numberRow][j] = field[numberRow][temp];
                    field[numberRow][temp] = 0;
                }
            }
        }
    }

    public void rowRight(int numberRow) {
        int i, j;
        int temp = 0;
        for (int k = SIZEFIELD - 1; k >= 0; k--) {
            // поиск ненулевого значения, начиная с правого края
            for (i = k; i >= 0; i--) {
                if (field[numberRow][i] != 0) {
                    temp = i;
                    break;
                }
            }
            // первый сдвиг
            for (j = SIZEFIELD - 1; j > temp; j--) {
                if (field[numberRow][j] == 0) {
                    field[numberRow][j] = field[numberRow][temp];
                    field[numberRow][temp] = 0;
                }
            }
        }
        // сумма, если одинаковые
        for (j = SIZEFIELD - 1; j > 0; j--) {
            if (field[numberRow][j] == field[numberRow][j - 1]) {
                field[numberRow][j] *= 2;
                field[numberRow][j - 1] = 0;
                scores += field[numberRow][j];
            }
        }
        for (int k = SIZEFIELD - 1; k >= 0; k--) {
            for (i = k; i >= 0; i--) {
                if (field[numberRow][i] != 0) {
                    temp = i;
                    break;
                }
            }
            // сдвиг до края
            for (j = SIZEFIELD - 1; j > temp; j--) {
                if (field[numberRow][j] == 0) {
                    field[numberRow][j] = field[numberRow][temp];
                    field[numberRow][temp] = 0;
                }
            }
        }
    }

    public void columnUp(int numberColumn) {
        int i, j;
        int temp = 0;
        for (int k = 0; k < SIZEFIELD; k++) {
            // поиск ненулевого значения, начиная с правого края
            for (i = k; i >= 0; i--) {
                if (field[i][numberColumn] != 0) {
                    temp = i;
                    break;
                }
            }
            // первый сдвиг
            for (j = 0; j < temp; j++) {
                if (field[j][numberColumn] == 0) {
                    field[j][numberColumn] = field[temp][numberColumn];
                    field[temp][numberColumn] = 0;
                }
            }
        }
        // сумма, если одинаковые
        for (j = 0; j < SIZEFIELD - 1; j++) {
            if (field[j + 1][numberColumn] == field[j][numberColumn]) {
                field[j][numberColumn] *= 2;
                field[j + 1][numberColumn] = 0;
                scores += field[j][numberColumn];
            }
        }
        for (int k = 0; k < SIZEFIELD; k++) {
            for (i = k; i < SIZEFIELD; i++) {
                if (field[i][numberColumn] != 0) {
                    temp = i;
                    break;
                }
            }
            // сдвиг до края
            for (j = 0; j < temp; j++) {
                if (field[j][numberColumn] == 0) {
                    field[j][numberColumn] = field[temp][numberColumn];
                    field[temp][numberColumn] = 0;
                }
            }
        }
    }

    public void columnDown(int numberColumn) {
        int i, j;
        int temp = 0;
        for (int k = SIZEFIELD - 1; k >= 0; k--) {
            // поиск ненулевого значения, начиная с правого края
            for (i = k; i >= 0; i--) {
                if (field[i][numberColumn] != 0) {
                    temp = i;
                    break;
                }
            }
            // первый сдвиг
            for (j = SIZEFIELD - 1; j > temp; j--) {
                if (field[j][numberColumn] == 0) {
                    field[j][numberColumn] = field[temp][numberColumn];
                    field[temp][numberColumn] = 0;
                }
            }
        }
        // сумма, если одинаковые
        for (j = SIZEFIELD - 1; j > 0; j--) {
            if (field[j][numberColumn] == field[j - 1][numberColumn]) {
                field[j][numberColumn] *= 2;
                field[j - 1][numberColumn] = 0;
                scores += field[j][numberColumn];
            }
        }
        for (int k = SIZEFIELD - 1; k >= 0; k--) {
            for (i = k; i >= 0; i--) {
                if (field[i][numberColumn] != 0) {
                    temp = i;
                    break;
                }
            }
            // сдвиг до края
            for (j = SIZEFIELD - 1; j > temp; j--) {
                if (field[j][numberColumn] == 0) {
                    field[j][numberColumn] = field[temp][numberColumn];
                    field[temp][numberColumn] = 0;
                }
            }
        }
    }

    public boolean canMoveRowLeft(int number) {
        for (int i = SIZEFIELD - 1; i >= 0; i--)
            if (field[number][i] != 0)
                for (int j = i; j >= 0; j--)
                    if (field[number][j] == 0)
                        return true;
        return false;
    }

    public boolean canMoveRowRight(int number) {
        for (int i = 0; i < SIZEFIELD; i++) {
            if (field[number][i] != 0)
                for (int j = i; j < SIZEFIELD; j++)
                    if (field[number][j] == 0)
                        return true;
        }
        return false;
    }

    public boolean canMoveColumnUp(int number) {
        for (int i = SIZEFIELD - 1; i >= 0; i--)
            if (field[i][number] != 0)
                for (int j = i; j >= 0; j--)
                    if (field[j][number] == 0)
                        return true;
        return false;
    }

    public boolean canMoveColumnDown(int number) {
        for (int i = 0; i < SIZEFIELD; i++)
            if (field[i][number] != 0)
                for (int j = i; j < SIZEFIELD; j++)
                    if (field[j][number] == 0)
                        return true;
        return false;
    }

    public boolean canSumRow(int number) {
        for (int i = 0; i < SIZEFIELD - 1; i++)
            if (field[number][i] == field[number][i + 1] && field[number][i] != 0)
                return true;
        return false;
    }

    public boolean canSumColumn(int number) {
        for (int i = 0; i < SIZEFIELD - 1; i++)
            if (field[i][number] == field[i + 1][number] && field[i][number] != 0)
                return true;
        return false;
    }

    public boolean checkGameOver() {
        for (int i = 0; i < SIZEFIELD; i++)
            if (canSumRow(i) || canSumColumn(i))
                return false;
        for (int i = 0; i < SIZEFIELD; i++)
            for (int j = 0; j < SIZEFIELD; j++)
                if (field[i][j] == 0)
                    return false;
        return true;
    }
}
