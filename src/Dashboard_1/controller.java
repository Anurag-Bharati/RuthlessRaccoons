package Dashboard_1;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;

public class controller {
    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private AnchorPane rootStage;

    @FXML
    public void switchtoScene(ActionEvent event) throws IOException{
        root= FXMLLoader.load(getClass().getResource("main.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        scene.setFill(Color.WHITE);
        stage.setScene(scene);
        stage.show();

    }

    @FXML
    public void onExit(ActionEvent actionEvent) throws IOException {
        stage.close();
        System.exit(0);
        Platform.exit();



    }





}
