package Test1;

import com.sun.mail.util.MailConnectException;
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
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.UnknownHostException;
import java.time.LocalDate;


public class SceneOneController {
    protected Stage stage;
    protected Scene scene;
    protected Parent root;
    User user;

    private String name;
    private String password;
    private String confirmPass;
    private String authCode;
    private String gender;
    private String gmail;
    private String phone;
    private LocalDate dob;
    private Boolean sent = false;

    @FXML
    private AnchorPane rootStage;

    @FXML
    private TextField nameField, gmailField, authField, phoneField;

    @FXML
    private PasswordField passwordField, confirmPassField;

    @FXML
    private  DatePicker dobPicker;

    @FXML
    private ChoiceBox<String> genderSelect;


    @FXML
    public void switchToScene2(ActionEvent event) throws Exception {
        if (checkGmail(gmailField.getText())) {

            user = new User();
            user.setName(nameField.getText());
            user.setGmail(gmailField.getText());
            user.setPhone(phoneField.getText());
            user.setDob(dobPicker.getValue());
            user.setGender(genderSelect.getValue());
            user.setPassword(password);
            user.setConfirmPass(confirmPass);
            user.setAuthCode(authCode);
            user.setSent(sent);

            sendIt(user.getName(), user.getGmail());

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Scene2.fxml"));
            root = fxmlLoader.load();

            SceneTwoController sceneTwoController = fxmlLoader.getController();
            sceneTwoController.initUser(user);

            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            scene.setFill(Color.TRANSPARENT);
            stage.setScene(scene);
            Test1.stageDragable(root, stage);
            stage.show();
        }
        else{
            Alert alert = new Alert(Alert.AlertType.ERROR,"",ButtonType.OK);

            alert.setTitle("Invalid Gmail Format");
            alert.setHeaderText(null);
            alert.setContentText("Please enter a valid gmail address!");
            alert.showAndWait();
        }
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

    public void initUser(User user) {
        this.name = user.getName();
        this.gmail = user.getGmail();
        this.phone = user.getPhone();
        this.dob = user.getDob();
        this.gender = user.getGender();
        this.password = user.getPassword();
        this.confirmPass = user.getConfirmPass();
        this.authCode = user.getAuthCode();
        this.sent = user.isSent();
        nameField.setText(name);
        gmailField.setText(gmail);
        phoneField.setText(phone);
        dobPicker.setValue(dob);
        genderSelect.setValue(gender);
    }
    public boolean checkGmail(String gMail){
        StringBuilder checkDomain= new StringBuilder();

        for(int i = 0; i<gMail.length();i++) {
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
    }

    public void sendIt(String name,String mail) throws Exception {
        if (!user.isSent()) {
            try {
                user.setSent(true);
                MailVerify.sendMail(name, mail);
            } catch (UnknownHostException | MailConnectException e) {
                System.out.println("Please connect to a network and then proceed");
            }
        }

    }
}

