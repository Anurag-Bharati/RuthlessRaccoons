package main.java.registration;

import com.jfoenix.controls.JFXButton;
import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.ScaleTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
import main.java.db.DatabaseManager;
import main.java.userside.ScreenDragable;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Objects;

@SuppressWarnings("All")
public class SceneTwoController {

    protected Stage stage;
    protected Scene scene;
    protected Parent root;
    User user;
    DatabaseManager databaseManager = new DatabaseManager();

    private String name;
    private String password;

    private String authCode;
    private String confirmPass;
    private String gender;
    private String gmail;
    private String gmailOld;
    private String phone;
    private LocalDate dob;
    private Boolean sent;

    String authCodeSys = String.valueOf(MailVerify.OTP);


    @FXML private TextField authField;
    @FXML private PasswordField passwordField, confirmPassField;
    @FXML private AnchorPane rootStage;
    @FXML private Label validLabel;
    @FXML JFXButton btn_SignUpDone, btn_SignUpBack;

    boolean authAlert = true;

    public void initUser(User user) {

        /*This method saves data from scene one*/

        this.user = user;
        this.name = user.getName();
        this.gmail = user.getGmail();
        this.phone = user.getPhone();
        this.dob = user.getDob();
        this.gender = user.getGender();
        this.password = user.getPassword();
        this.confirmPass = user.getConfirmPass();
        this.authCode = user.getAuthCode();
        this.sent = user.isSent();
        this.gmailOld = user.getGmailOld();
        if (password!=null&&password.equals(confirmPass)){
            passwordField.setText(password);
            confirmPassField.setText(confirmPass);
        }
        if (authCode!=null&&authCode.strip().length()==authCodeSys.length()){
            authField.setText(authCode);
        }
    }

    @FXML
    public void onPassword(){
        this.password = passwordField.getText();
    }

    @FXML
    public void onDone(ActionEvent actionEvent) throws SQLException {

        /* This method runs when Done button is clicked. It also checks the auth code*/

        if (checkFieldsTwo()){
            user.setPassword(passwordField.getText());
            password = passwordField.getText();
            confirmPass = confirmPassField.getText();
            if (authField.getText().strip().equals(authCodeSys)) {
//                System.out.println(name+" "+gmail+" "+phone+" "+dob+" "+gender+" "+password);
                if (NoExistence()){
                    addNewUser();
                    confirmPassField.clear();
                    confirmPassField.setDisable(true);
                    passwordField.clear();
                    passwordField.setDisable(true);
                    authField.clear();
                    authField.setDisable(true);

                    btn_SignUpDone.setDisable(true);
                    btn_SignUpBack.setDisable(true);
                    validLabel.setTextFill(Color.web("#3e8948"));

                    FadeTransition fadeTransition = new FadeTransition(Duration.seconds(.3), validLabel);
                    fadeTransition.setFromValue(1.0);
                    fadeTransition.setToValue(0.0);
                    fadeTransition.setCycleCount(6);
                    fadeTransition.setAutoReverse(true);
                    fadeTransition.setCycleCount(10);
                    fadeTransition.play();
                    validLabel.setText("User Added Successfully! Now Redirecting...");
                    fadeTransition.setOnFinished(e->{
                        try {
                            switchToLogin(actionEvent);
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    });

                }
                else validLabel.setText("User Already Exists! Please, Provide a new Gmail");
            } else validLabel.setText("Auth code does not match");
        }
    }
    @FXML
    private void switchToScene1(ActionEvent event) throws IOException {

        user = new User();
        user.setName(name);
        user.setGmail(gmail);
        user.setGmailOld(gmailOld);
        user.setPhone(phone);
        user.setDob(dob);
        user.setGender(gender);
        user.setSent(sent);
        if(passwordField.getText().equals(confirmPassField.getText())) {
            user.setPassword(passwordField.getText());
            user.setConfirmPass(confirmPassField.getText());
        }
        user.setAuthCode(authField.getText());

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../../resource/registration/Scene1.fxml"));
        root = fxmlLoader.load();

        SceneOneController sceneOneController = fxmlLoader.getController();
        sceneOneController.initUser(user);

        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT);
        stage.setScene(scene);
        ScreenDragable.stageDragable(root,stage);
        stage.show();
    }
    @FXML
    private void switchToLogin(Event event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../../resource/login/Login_Scene.fxml"));
        root = fxmlLoader.load();

//        SceneOneController sceneOneController = fxmlLoader.getController();


        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT);
        stage.setScene(scene);
        ScreenDragable.stageDragable(root,stage);
        stage.show();
    }

    @FXML
    private void onQuit(ActionEvent actionEvent){
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
    public boolean checkFieldsTwo(){

        /*This checks for the password validity made totally by anurag :) at 12AM 8/27/2021 */

        if ((Objects.requireNonNull(passwordField.getText()).length() >= 8)){
            if (passwordField.getText().equals(confirmPassField.getText())) {
                if(checkPasswordStrength(passwordField.getText())){
                    return true;
                } else validLabel.setText("Password must contain at least one letter and number");
                return false;
            } else validLabel.setText("Password does not match on both fields");
            return false;

        }else validLabel.setText("Password must be at least 8 character long");
        return false;
    }
    public boolean checkPasswordStrength(String password){

        /* This is the password strength checker*/

        boolean hasLetter = false;
        boolean hasDigit = false;
        for (int i = 0; i<password.length();i++){
            char x = password.charAt(i);
            if (Character.isLetter(x)){
                hasLetter = true;
            }
            if (Character.isDigit(x)){
                hasDigit = true;
            }
            if(hasDigit && hasLetter){
                return true;
            }
        }
        return false;
    }
    public void authAlert(){
        if (authAlert) {
            validLabel.setText("Check your Gmail for five-digit Auth code");
        }
    }
    public void authAlert1(){
        if(authAlert) {
            validLabel.setText("Make sure you have provided valid gmail");
        }
    }
    private boolean NoExistence() throws SQLException {
        Connection connection = databaseManager.connect();

        PreparedStatement checkGmail = connection.prepareStatement(
                "SELECT gmail FROM CustomerDetail WHERE gmail = ?");
        checkGmail.setString(1, gmail);

        ResultSet result = checkGmail.executeQuery();
        if(result.next()) {
            if (gmail.strip().equals(result.getString("gmail"))) {
                result.close();
                checkGmail.close();
                databaseManager.disconnect();
                return false;
            }
        }
        checkGmail.close();
        connection.close();
        databaseManager.disconnect();
        return true;

    }
    private void addNewUser() throws SQLException {
        Connection connection = databaseManager.connect();
        PreparedStatement addUser = connection.prepareStatement(
                "insert into CustomerDetail (name, gmail, phone, dob, gender, pass) values (?,?,?,?,?,?)");
        addUser.setString(1,name);
        addUser.setString(2,gmail);
        addUser.setString(3,phone);
        addUser.setString(4,dob.toString());
        addUser.setString(5,gender);
        addUser.setString(6,password);
        addUser.executeUpdate();
        addUser.close();
        connection.close();
        databaseManager.disconnect();
    }
}
