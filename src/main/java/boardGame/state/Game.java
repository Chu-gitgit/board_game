package boardGame.state;

public class Game {

    public static int steps;
    public static int[][] board = new int[8][8];
    public static int boardLength = board.length;

    public static void addStep(int x, int y, int player) {
        if (player % 2 == 0) { //Check whether there is a number on the relative position.
            board[x][y] = player;
            board[x][y + 1] = player;
        } else {
            board[x][y] = player;
            board[x + 1][y] = player;
        }
        board[x][y] = player;
        steps++;
    }

    public static void resetStep() {
        steps = 0;
        board = new int[8][8];
    }

    public static boolean clickable(int x, int y, int player) throws ArrayIndexOutOfBoundsException {
        if (player % 2 == 0) { //Check whether there is a number on the relative position.
            return board[x][y] == 0 && board[x][y + 1] == 0;
        } else {
            try {
                return board[x][y] == 0 && board[x + 1][y] == 0;
            } catch (ArrayIndexOutOfBoundsException ignore) {
            }
        }
        return false;
    }

    public static boolean isWin(int player) {
        if (player % 2 == 0) {                                      //Player A: horizontal
            for (int i = 0; i < boardLength - 1; i++) {
                for (int j = 0; j < boardLength; j++) {
                    if (board[i][j] == 0 && board[i + 1][j] == 0) return true;
                }
            }
        } else {                                              //Player B: vertical
            for (int i = 0; i < boardLength; i++) {
                for (int j = 0; j < boardLength - 1; j++) {
                    if (board[i][j] == 0 && board[i][j + 1] == 0) return true;
                }
            }
        }
        return false;
    }

}
