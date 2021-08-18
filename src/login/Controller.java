package login;

import com.jfoenix.controls.JFXToggleNode;
import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.ScaleTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.io.IOException;

public class Controller {
    private Stage stage;
    private Scene scene;
    private Parent root;
    private Alert alert;

    @FXML
    private JFXToggleNode toggleButton ;

//    @FXML
//    private JFXButton btn_SignupQuit, btn_SignUpDone;

    @FXML
    private AnchorPane rootStage;

    @FXML
    private TextField nameField;
    @FXML
    private PasswordField passwordField;

    @FXML
    public void switchtoScene(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("Login_Scene1.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        scene.setFill(Color.WHITE);
        stage.setScene(scene);
        stage.show();

    }


    @FXML
    public Boolean onDone() {
        if (nameField.getLength() != 0) {
            System.out.println(nameField.getText());
            System.out.println(passwordField.getText());
            return true;
        }
        else{
            // Error Message
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Field Empty");
            alert.setHeaderText("Empty Field");
            alert.initStyle(StageStyle.UNDECORATED);
            alert.setContentText("Please, fill both username and password to login successfully");
            alert.showAndWait();
            return false;
        }
    }

    @FXML
    public void onClick(ActionEvent click) throws IOException{
        if (onDone()) {
            switchtoScene(click);
        }

    }


    @FXML
    public void onQuit(ActionEvent actionEvent) throws IOException {

        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(.4), rootStage);
        ScaleTransition scaleTransition = new ScaleTransition(Duration.seconds(.4), rootStage);

        scaleTransition.setInterpolator(Interpolator.EASE_IN);

        scaleTransition.setByX(.05);
        scaleTransition.setByY(.05);

        fadeTransition.setInterpolator(Interpolator.EASE_IN);
        fadeTransition.setFromValue(1.0);
        fadeTransition.setToValue(0);

        scaleTransition.play();
        fadeTransition.play();

        fadeTransition.setOnFinished(actionEvent1 -> {
            scaleTransition.stop();
            fadeTransition.stop();
            stage.close();
            Platform.exit();
            System.exit(0);
        });


    }
    @FXML
    public void onToggle(ActionEvent e){

        toggleButton.setOnAction(event ->{
            toggleButton.setText("User");
//                if(!toggleButton.getText().equals("User")){
//                toggleButton.setText("User"); }
//                else{
//                    toggleButton.setText("Admin");




        });






    }

}
