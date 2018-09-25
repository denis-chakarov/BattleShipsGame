package battleships.game.logic;
import java.util.*;

public class GameTable {

    private char[][] table;


    public GameTable() {
        table = new char[10][10];

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                table[i][j] = '.';
            }
        }
        initializer();
        //generateTable();
    }

    private void initializer() {
        table[3][0] = '#';
        table[4][0] = '#';
    }
    private boolean isSecured(int lowerBound, int upperBound, int index, boolean rowOrCol) { //true - row false - col
        if(rowOrCol) {
            for (int i = lowerBound; i < upperBound; i++) {
                if(table[index][i] == '#') {
                    return false;
                }
            }
            return true;
        }
        else {
            for (int i = lowerBound; i < upperBound; i++) {
                if(table[i][index] == '#') {
                    return false;
                }
            }
            return true;
        }
    }
    public void generateTable() {
       Random rnd = new Random();
       int counter = 3;
       int row, col;
       col = rnd.nextInt(6);
       row = rnd.nextInt(2);
        for (int i = col; i < col + 5; i++) { //0,1 ред 5 клетки
            table[row][i] = '#';
        }
        while (counter > 0) {
            col = rnd.nextInt(10); // 2 до 4 ред 3x3 клетки
            int i;
            if(isSecured(2, 5, col, false)) {
                for (i = 2; i < 5; i++) {
                    table[i][col] = '#';
                }
                counter--;
            }
        }
        counter = 2;

        while(counter > 0) {  //2x4 kletki horizontalno
            row = rnd.nextInt(3) + 5;
            col = rnd.nextInt(7);
            int i;
            if(isSecured(col, col + 4, row, true)) {
                for (i = col; i < col + 4; i++) {
                    table[row][i] = '#';
                }
                counter--;
            }
        }
        counter = 4;
        while(counter > 0) {
            col = rnd.nextInt(10);
            int i;
            if(isSecured(8, 10, col,false)) {
                for (i = 8; i < 10; i++) {
                    table[i][col] = '#';
                }
                counter--;
            }
        }
    }

    public String printTable() {
        String result = "   1 2 3 4 5 6 7 8 9 10\\n" +
                "   _ _ _ _ _ _ _ _ _ _\\n";

        for(int i = 0; i < 10; i++) {
            result += (char)(i + 65) + " |";
            for (int j = 0; j < 10; j++) {
                if(table[i][j] == '.') {
                    result += "_|";
                }
                else {
                    result += table[i][j] + "|";
                }
            }
            result += "\\n";
        }
        return result;
    }

    public void attacked(String target) {
        int index1 = target.charAt(0) - 65;
        int index2 = Character.getNumericValue(target.charAt(1)) - 1;
        if(table[index1][index2] == '#') {
            table[index1][index2] = 'X';
        }
        else if(table[index1][index2] == '.') {
            table[index1][index2] = 'O';
        }
    }
    public boolean lost() {
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                if(table[i][j] == '#') {
                    return false;
                }
            }
        }
        return true;
    }

    public char[][] getTable() {
        return table;
    }

    public void setTable(char[][] table) {
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                this.table[i][j] = table[i][j];
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GameTable gameTable = (GameTable) o;

        return Arrays.deepEquals(table, gameTable.table);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(table);
    }

    public static void main(String[] args) {
        List<Integer> mylist = new ArrayList<>();
        Map<String, List<Integer>> mymap = new HashMap<>();

    }
}

