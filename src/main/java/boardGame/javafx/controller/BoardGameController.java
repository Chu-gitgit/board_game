package boardGame.javafx.controller;

import boardGame.results.GameResult;
import boardGame.results.GameResultDao;
import boardGame.state.Game;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import org.tinylog.Logger;

import javax.inject.Inject;
import java.io.IOException;

public class BoardGameController {

    @FXML
    public Label stepsLabel;
    private String playerA;
    private String playerB;

    @Inject
    private FXMLLoader fxmlLoader;

    @Inject
    public GameResultDao gameResultDao;

    @FXML
    private GridPane board;

    @FXML
    public Button resetButton;

    @FXML
    public Button giveupFinishButton;

    public static IntegerProperty step = new SimpleIntegerProperty();


    @FXML
    private void initialize() {
        Game.resetStep();
        for (var i = 0; i < board.getRowCount(); i++) {
            for (var j = 0; j < board.getColumnCount(); j++) {
                var square = createSquare();
                board.add(square, j, i);
            }
        }
        step.set(Game.steps);
        stepsLabel.textProperty().bind(step.asString());
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
    private void handleMouseClick(MouseEvent event) throws RuntimeException {
        var square = (StackPane) event.getSource();
        var row = GridPane.getRowIndex(square);
        var col = GridPane.getColumnIndex(square);
        Logger.info("Mouse click on square ({},{})", row, col);
        Circle coin = (Circle) square.getChildren().get(0);
        coin.getFill();
//       coin.setFill(nextColor(currentColor));
        if (Game.clickable(col, row, Game.steps % 2 == 0 ? 1 : 2)) {
            Game.addStep(col, row, Game.steps % 2 == 0 ? 1 : 2);
            Logger.info("Steps: {}", Game.steps);
            if (Game.steps % 2 == 0) {
                coin.setFill(Color.RED);
            } else {
                coin.setFill(Color.BLUE);
            }
            nextSquare(col, row, Game.steps);
            if (Game.isWin(Game.steps)) {
                Logger.info("Continue.");
            } else {
                Logger.info("Player {} is win!", Game.steps % 2 == 1 ? "A" : "B");
            }
            step.set(Game.steps);
            try {
                stepsLabel.textProperty().set(step.toString());
            } catch (RuntimeException ignored) {
            }
        }
    }

    public void handleResetButton(ActionEvent actionEvent) throws IOException {
        Logger.debug("{} is pressed", ((Button) actionEvent.getSource()).getText());
        Logger.info("Resetting game");
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/ui.fxml"));
        Parent root = fxmlLoader.load();
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    public void handleGiveUpFinishButton(ActionEvent actionEvent) throws IOException {
        var buttonText = ((Button) actionEvent.getSource()).getText();
        Logger.debug("{} is pressed", buttonText);
//        if (buttonText.equals("Give Up")) {
//            Logger.info("The game has been given up");
//        }
        Logger.debug("Saving result");
        gameResultDao.persist(createGameResult());
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/highscores.fxml"));
        Parent root = fxmlLoader.load();
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    private GameResult createGameResult() {
        return GameResult.builder()
                .player(Game.steps % 2 == 1 ? playerA : playerB)
                .solved(Game.isWin(Game.steps))
                .steps(Game.steps)
                .build();
    }

}
