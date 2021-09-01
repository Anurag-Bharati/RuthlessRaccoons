package Dashboard_1;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.Objects;

public class main extends Application {
    double xOffset;
    double yOffset;
    Parent root;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        try {
            root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/Dashboard_1/main.fxml")));
            Scene scene = new Scene(root);
            scene.setFill(Color.WHITE);
            stage.setScene(scene);
//            stage.setHeight(400);
//            stage.setWidth(600);
            stage.setTitle("Hello Siri");
            stage.initStyle(StageStyle.TRANSPARENT);

            root.setOnMousePressed(mouseEvent -> {
                xOffset = mouseEvent.getSceneX();
                yOffset = mouseEvent.getSceneY();
            });

            root.setOnMouseDragged(mouseEvent -> {
                stage.setX(mouseEvent.getScreenX() - xOffset);
                stage.setY(mouseEvent.getScreenY() - yOffset);
            });


            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
