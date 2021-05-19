package boardGame.state;

public class Game {

    public static int steps;

    public static void addStep(){
        steps++;
    }

    public static void resetStep(){
        steps = 0;
    }

}
