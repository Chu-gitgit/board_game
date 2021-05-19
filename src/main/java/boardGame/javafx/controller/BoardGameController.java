package boardGame.javafx.controller;

import boardGame.state.Game;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import org.tinylog.Logger;

public class BoardGameController {

    private String playerA;
    private String playerB;

    @FXML
    private GridPane board;
//    private Label usernameLabel;

    @FXML
    private void initialize() {
        Game.resetStep();
        for (var i = 0; i < board.getRowCount(); i++) {
            for (var j = 0; j < board.getColumnCount(); j++) {
                var square = createSquare();
                board.add(square, j, i);

            }
        }
    }

    public void setPlayerA(String playerA) {
        this.playerA = playerA;
    }

    public void setPlayerB(String playerB) {
        this.playerB = playerB;
    }

    private StackPane createSquare() {
        var square = new StackPane();
        var coin = new Circle(20);
        coin.setFill(Color.TRANSPARENT);
        square.getChildren().add(coin);
        square.getStyleClass().add("square");
        square.setOnMouseClicked(this::handleMouseClick);  //handle mouse click
        return square;
    }

    private void nextSquare(int col, int row, int step) {
        var square = new StackPane();
        var coin = new Circle(20);
        if (step % 2 == 0) {
            coin.setFill(Color.RED);
        } else {
            coin.setFill(Color.BLUE);
        }
        square.getChildren().add(coin);
        if (step % 2 == 0) {
            board.add(square, col, row + 1);
        } else {
            board.add(square, col + 1, row);
        }

    }


    @FXML
    private void handleMouseClick(MouseEvent event) {
        var square = (StackPane) event.getSource();
        var row = GridPane.getRowIndex(square);
        var col = GridPane.getColumnIndex(square);
        Logger.info("Mouse click on square ({},{})", row, col);
        Circle coin = (Circle) square.getChildren().get(0);
        coin.getFill();
//       coin.setFill(nextColor(currentColor));
        Game.addStep();
        if (Game.steps % 2 == 0) {
            coin.setFill(Color.RED);
        } else {
            coin.setFill(Color.BLUE);
        }
        nextSquare(col, row, Game.steps);
    }

}
