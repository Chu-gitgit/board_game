package boardGame.javafx;


import boardGame.results.GameResultDao;
import com.gluonhq.ignite.guice.GuiceContext;
import com.google.inject.AbstractModule;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import util.guice.PersistenceModule;

import javax.inject.Inject;
import java.util.List;

public class BoardGameApplication extends Application {

    private GuiceContext context = new GuiceContext(this, () -> List.of(
            new AbstractModule() {
                @Override
                protected void configure() {
                    install(new PersistenceModule("boardGame"));
                    bind(GameResultDao.class);
                }
            }
    ));

    @Inject
    private FXMLLoader fxmlLoader;

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/launch.fxml"));
        stage.setScene(new Scene(root));
        stage.setTitle("JavaFX Board Game");
        stage.show();
    }
}
