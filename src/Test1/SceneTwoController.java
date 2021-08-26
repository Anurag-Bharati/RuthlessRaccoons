package Test1;

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
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Objects;

public class SceneTwoController {

    protected Stage stage;
    protected Scene scene;
    protected Parent root;
    User user;

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


    @FXML
    private TextField authField;

    String authCodeSys = String.valueOf(MailVerify.OTP);

    @FXML
    private PasswordField passwordField, confirmPassField;

    @FXML
    private AnchorPane rootStage;

    @FXML
    private Label validLabel;


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
        if (authCode!=null&&authCode.length()==authCodeSys.length()){
            authField.setText(authCode);
        }
    }

    @FXML
    public void onPassword(){
        this.password = passwordField.getText();
    }

    @FXML
    public void onDone() {
        /* This method runs when Done button is clicked. It also checks the auth code*/
        if (checkFieldsTwo()){
            user.setPassword(passwordField.getText());
            password = passwordField.getText();
            confirmPass = confirmPassField.getText();
            if (authField.getText().strip().equals(authCodeSys)) {
                System.out.println(name);
                System.out.println(gmail);
                System.out.println(phone);
                System.out.println(dob);
                System.out.println(gender);
                System.out.println(password);
            } else validLabel.setText("Auth code does not match");
        }
    }

    public void switchToScene1(ActionEvent event) throws IOException {
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


        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Scene1.fxml"));
        root = fxmlLoader.load();

        SceneOneController sceneOneController = fxmlLoader.getController();
        sceneOneController.initUser(user);

        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT);
        stage.setScene(scene);
        Test1.stageDragable(root,stage);
        stage.show();
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
        validLabel.setText("Check your Gmail for five-digit Auth code");
    }
    public void authAlert1(){
        validLabel.setText("Make sure you have provided valid gmail");
    }

}
