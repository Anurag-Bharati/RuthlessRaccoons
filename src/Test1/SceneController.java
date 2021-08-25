package Test1;

import com.jfoenix.controls.JFXButton;
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
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Objects;


public class SceneController {


    private Stage stage;
    Scene scene , scene1;

    @FXML
    private JFXButton btn_SignupQuit, btn_SignUpDone;

    @FXML
    private AnchorPane rootStage;

    @FXML
    private TextField nameField, gmailField, authField, phoneField;

    @FXML
    private PasswordField passwordField, confirmPassField;

    @FXML
    private DatePicker dobPicker;

    @FXML
    private ChoiceBox<String> genderSelect;

    private String name;
    private String password;
    private String confirmPass;
    private String gender;
    private String gmail;
    private String phone;
    private LocalDate dob;

    private final String[] genderType = {"Male", "Female", "Other"};

    User user;


    @FXML
    public void switchToScene1(ActionEvent event) throws IOException {

        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Scene1.fxml")));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT);
        stage.setScene(scene);
        Test1.stageDragable(root,stage);
        stage.show();
    }

    @FXML
    public void switchToScene2(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Scene2.fxml")));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene1 = new Scene(root);
        scene1.setFill(Color.TRANSPARENT);
        stage.setScene(scene1);
        Test1.stageDragable(root,stage);
        stage.show();
    }

    @FXML
    public void onDone() {
        if (passwordField.getLength() != 0) {
            System.out.println(name);
            System.out.println(gmail);
            System.out.println(phone);
            System.out.println(dob);
            System.out.println(gender);
        }
    }

    @FXML
    public void onNext(ActionEvent e) throws IOException {


        System.out.println(user.getName() + user.getGmail() + user.getPhone() + user.getDob() + user.getGender());
        switchToScene2(e);
    }

    @FXML
    public void fetchUser(){
        stage.setUserData(user);
        nameField.setText(user.getName());
        gmailField.setText(user.getGmail());
        phoneField.setText(user.getPhone());
        dobPicker.setValue(user.getDob());
        genderSelect.setValue(user.getGender());
    }

    @FXML
    public void onQuit(ActionEvent actionEvent) throws IOException {

        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(.4), rootStage);
        ScaleTransition scaleTransition = new ScaleTransition(Duration.seconds(.4), rootStage);

        scaleTransition.setInterpolator(Interpolator.EASE_IN);

        scaleTransition.setByX(.05);
//        scaleTransition.setByY(.05);


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
}