package boardGame.javafx.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

import org.tinylog.Logger;

//@Slf4j
public class LaunchController {

    @FXML
    private TextField playerA;
    @FXML
    private TextField playerB;

    @FXML
    private Label errorLabel;
    public Button startButton;

    public void startAction(ActionEvent actionEvent) throws IOException {
        if (playerA.getText().isEmpty() || playerB.getText().isEmpty()) {
            errorLabel.setText("* Username(s) is empty!");
        } else {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/ui.fxml"));
            Parent root = fxmlLoader.load();
            fxmlLoader.<BoardGameController>getController().setPlayerA(playerA.getText());
            fxmlLoader.<BoardGameController>getController().setPlayerB(playerB.getText());
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
            Logger.info("Username is set to {}, loading game scene.", playerA.getText());
        }

    }
}
