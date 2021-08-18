package login;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.Objects;

public class Login_run extends Application {
    double xOffset;
    double yOffset;


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Login_Scene.fxml")));
            Scene scene = new Scene(root);
            scene.setFill(Color.TRANSPARENT);
            primaryStage.setScene(scene);
//            primaryStage.setHeight(400);
//            primaryStage.setWidth(600);
            //  primaryStage.setTitle("Hello Siri");
            primaryStage.initStyle(StageStyle.TRANSPARENT);

            root.setOnMousePressed(mouseEvent -> {
                xOffset = mouseEvent.getSceneX();
                yOffset = mouseEvent.getSceneY();
            });

            root.setOnMouseDragged(mouseEvent -> {
                primaryStage.setX(mouseEvent.getScreenX()-xOffset);
                primaryStage.setY(mouseEvent.getScreenY()-yOffset);
            });



            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}