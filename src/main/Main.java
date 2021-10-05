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
import main.java.userside.ScreenDragable;

import java.util.Objects;

/**
 * <h1>MAIN CLASS</h1>
 */
@SuppressWarnings("All")
public class Main extends Application {
    public static Scene scene;
    protected Stage stage;
    final String LOGIN = "/main/resource/login/Login_Scene.fxml";

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        try{
            Parent root = FXMLLoader.load((Objects.requireNonNull(
                    getClass().getResource(LOGIN))));
            scene = new Scene(root);
            scene.setFill(Color.TRANSPARENT);
            stage.initStyle(StageStyle.TRANSPARENT);
            stage.setScene(scene);
            if (root.getId().equals("rootStage")) {
                ScreenDragable.stageDragable(root, stage);
            }
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

}
