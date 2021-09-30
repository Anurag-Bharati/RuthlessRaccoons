package main.java.registration;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.Objects;

public class Test1 extends Application {

    static double  xOffset, yOffset;
    public static Scene scene;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        try{

            Parent root = FXMLLoader.load((Objects.requireNonNull(getClass().getResource("/main/resource/registration" +
                    "/Scene1.fxml"))));
            scene = new Scene(root);
            scene.setFill(Color.TRANSPARENT);
            stage.initStyle(StageStyle.TRANSPARENT);
            stageDragable(root,stage);
            stage.setScene(scene);
            stage.show();


        }

        catch (Exception e){
            e.printStackTrace();
            Platform.exit();
            System.exit(0);
        }

    }
    public static void stageDragable(Parent root, Stage stage){
        root.setOnMousePressed(mouseEvent -> {
            xOffset = mouseEvent.getSceneX();
            yOffset = mouseEvent.getSceneY();
        });

        root.setOnMouseDragged(mouseEvent -> {
            stage.setX(mouseEvent.getScreenX()-xOffset);
            stage.setY(mouseEvent.getScreenY()-yOffset);
        });
    }

}
