package main;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import main.java.userside.ResizeHelper;

import java.util.Objects;

public class Main extends Application {
    static double  xOffset, yOffset;
    public static Scene scene;
    protected Stage stage;
    final String LOGIN = "/main/resource/login/Login_Scene.fxml";
    final String USERSIDE = "/main/resource/adminside/OurGuests.fxml";

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        try{
            Parent root = FXMLLoader.load((Objects.requireNonNull(
                    getClass().getResource(USERSIDE))));
            scene = new Scene(root);
            scene.setFill(Color.TRANSPARENT);
            stage.initStyle(StageStyle.TRANSPARENT);
            if (root.getId().equals("rootStage")) {
                stageDragable(root, stage);
            }

            stage.setScene(scene);
            if (root.getId().equals("rootStageUser")){
                ResizeHelper.addResizeListener(stage);
            }
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
