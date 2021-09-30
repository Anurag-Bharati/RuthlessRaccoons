package main.java.login;

import com.jfoenix.controls.JFXButton;
import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.ScaleTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
import main.java.db.DatabaseManager;
import main.java.registration.User;
import main.java.userside.ResizeHelper;
import main.java.userside.UserDashboardController;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Locale;
import java.util.ResourceBundle;

import static main.java.registration.Test1.stageDragable;

@SuppressWarnings("All")
public class Controller implements Initializable {
    private Stage stage;
    private Scene scene;
    public Parent root;
    DatabaseManager databaseManager = new DatabaseManager();


    @FXML private AnchorPane rootStage;

    @FXML private TextField gmailField;
    @FXML private PasswordField passwordField;
    @FXML private Label actionOutput;
    @FXML private JFXButton LoginButton;
    @FXML private Button SignUpHere;

    @FXML private ComboBox comboBox;
    @FXML private Region loadbar;


    @FXML
    public void switchToSignUp(ActionEvent event) throws IOException {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../../resource/registration/Scene1.fxml"));
            root = fxmlLoader.load();
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stageDragable(root,stage);
            scene.setFill(Color.TRANSPARENT);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e){
            e.printStackTrace();
        }
    }
    @FXML
    public Boolean onDone() throws IOException {
        if (gmailField.getLength() != 0) {
            return true;
        } else
            actionOutput.setText("Please, Provide your Gmail");
            return false;
    }

    @FXML
    public void onClick(ActionEvent e) throws SQLException, IOException {
        if (comboBox.getValue().equals("Admin")){
            if (gmailField.getLength()!=0){
                if (gmailField.getText().equals("admin")&&passwordField.getText().equals("admin")){
                    openAdminSide(e);
                } else actionOutput.setText("Invalid Admin Credential");
            }
            else actionOutput.setText("Please, Provide your Username");

        }
        if(comboBox.getValue().equals("User")) {
            if (onDone()) {
                if (checkGmail(gmailField.getText().toLowerCase(Locale.ROOT))) {
                    if (checkPass()) {
                        SignUpHere.setDisable(true);
                        LoginButton.setDisable(true);
                        actionOutput.setTextFill(Color.web("#3e8948"));
                        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(.3), actionOutput);
                        fadeTransition.setFromValue(1.0);
                        fadeTransition.setToValue(0.0);
                        fadeTransition.setCycleCount(6);
                        fadeTransition.setAutoReverse(true);
                        ;
                        fadeTransition.setCycleCount(10);
                        fadeTransition.play();
                        actionOutput.setText("Access Granted!");

                        stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
                        openUserSide(fetchUser());

                        System.out.println("NICE");

                    } else actionOutput.setText("Incorrect login credential. Please, try again");
                } else actionOutput.setText("Please, Provide a valid Gmail");
            }
        }

    }

    @FXML
    public void onQuit(ActionEvent actionEvent) {

        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(.4), rootStage);
        ScaleTransition scaleTransition = new ScaleTransition(Duration.seconds(.4), rootStage);

        scaleTransition.setInterpolator(Interpolator.EASE_IN);

        scaleTransition.setByX(.05);

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
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        comboBox.getItems().addAll("Admin", "User");
        comboBox.getSelectionModel().select("User");
    }
    private void openAdminSide(ActionEvent e) throws IOException{
        stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        stage.close();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/main/resource/adminside/adminside.fxml"));
        root = fxmlLoader.load();
        scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT);
        if (root.getId().equals("rootStage")) {
            stageDragable(root, stage);
        }
        stage.setScene(scene);
        ResizeHelper.addResizeListener(stage);
        stage.show();
    }

    private void openUserSide(User user) throws IOException {
        stage.close();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/main/resource/userside/userside.fxml"));
        root = fxmlLoader.load();
        scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT);
        scene.setUserData(user);
        if (root.getId().equals("rootStage")) {
            stageDragable(root, stage);
        }
        stage.setScene(scene);
        ResizeHelper.addResizeListener(stage);
        UserDashboardController userDashboardController =  fxmlLoader.getController();
        userDashboardController.initUser(user);
        stage.show();
    }
    private User fetchUser() throws SQLException {
        User user = new User();
        Connection connection = databaseManager.connect();
        PreparedStatement checkGmail = connection.prepareStatement(
                "SELECT * FROM CustomerDetail WHERE gmail = ?");
        checkGmail.setString(1, gmailField.getText().toLowerCase(Locale.ROOT).strip());
        ResultSet resultSet = checkGmail.executeQuery();
        if (resultSet.next()) {
            user.setName(resultSet.getString("name"));
            user.setGmail(resultSet.getString("gmail"));
            user.setPhone(resultSet.getString("phone"));
            user.setDob(LocalDate.parse(resultSet.getString("dob")));
            user.setGender(resultSet.getString("gender"));
            resultSet.close();
            checkGmail.close();
            connection.close();
            databaseManager.disconnect();
            return user;
        }
        resultSet.close();
        checkGmail.close();
        connection.close();
        databaseManager.disconnect();
        return null;

    }
    public boolean checkGmail(String gMail) {

        /*This function takes gmail as string and checks if the domain is gmail or not.
        If not it returns false and true if it is.*/

        StringBuilder checkDomain = new StringBuilder();

        for (int i = 0; i < gMail.length(); i++) {
            char letter = gMail.charAt(i);
            if (letter == '@') {
                for (int j = i; j < gMail.length(); j++) {
                    if (gMail.charAt(j) != ' ') {
                        checkDomain.append(gMail.charAt(j));
                    } else j++;
                    if (checkDomain.toString().equals("@gmail.com")) {
                        return true;
                    }
                }

            }
        }
        return false;
    }private boolean checkPass() throws SQLException {
        Connection connection = databaseManager.connect();

        PreparedStatement checkGmail = connection.prepareStatement(
                "SELECT gmail FROM CustomerDetail WHERE gmail = ?");
        checkGmail.setString(1, gmailField.getText().toLowerCase(Locale.ROOT).strip());
        ResultSet result = checkGmail.executeQuery();
        if (result.next()){
            if(gmailField.getText().strip().toLowerCase(Locale.ROOT).equals(
                    result.getString("gmail").strip())){
                PreparedStatement checkPass = connection.prepareStatement("SELECT pass FROM CustomerDetail " +
                        "WHERE gmail = ?");
                checkPass.setString(1, gmailField.getText().toLowerCase(Locale.ROOT).strip());
                ResultSet result0 = checkPass.executeQuery();
                if(result0.next()){
                    if(passwordField.getText().equals(result0.getString("pass"))){
                        result0.close();
                        result.close();
                        checkPass.close();
                        checkGmail.close();
                        connection.close();
                        databaseManager.disconnect();
                        return true;
                    }
                    else {
                        result0.close();
                        result.close();
                        checkPass.close();
                        checkGmail.close();
                        connection.close();
                        databaseManager.disconnect();
                        return false;
                    }

                }
                checkGmail.close();
                checkPass.close();
                result0.close();
                result.close();
                connection.close();
                databaseManager.disconnect();
                return false;
            }

        }
        checkGmail.close();
        result.close();
        connection.close();
        databaseManager.disconnect();
        return false;
    }
}


