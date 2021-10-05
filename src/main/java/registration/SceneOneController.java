package main.java.registration;

import com.jfoenix.controls.JFXButton;
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
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
import main.java.userside.ScreenDragable;

import java.net.UnknownHostException;
import java.time.LocalDate;
import java.util.Locale;
import java.util.Objects;

@SuppressWarnings("All")
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
    private String gmailOld;
    private String phone;
    private LocalDate dob;
    private Boolean sent = false;

    @FXML
    private AnchorPane rootStage;

    @FXML
    private TextField nameField, gmailField, phoneField;

    @FXML
    private  DatePicker dobPicker;

    @FXML
    private ChoiceBox<String> genderSelect;

    @FXML
    private Label actionOutput;

    @FXML
    private JFXButton btn_SignUpNext;


    @FXML
    public void switchToScene2(ActionEvent event) throws Exception {
        actionOutput.setTextFill(Color.web("#f77622"));
        if (checkGmail(gmailField.getText().toLowerCase(Locale.ROOT))) {
            if (checkFields()) {
                user = new User();
                user.setName(nameField.getText());
                user.setGmail(gmailField.getText().strip().toLowerCase(Locale.ROOT));
                user.setGmailOld(gmailField.getText().strip().toLowerCase(Locale.ROOT));
                user.setPhone(phoneField.getText().strip());
                user.setDob(dobPicker.getValue());
                user.setGender(genderSelect.getValue());
                user.setPassword(password);
                user.setConfirmPass(confirmPass);
                user.setAuthCode(authCode);
                user.setSent(sent);

                if (sendIt(user.getName(), user.getGmail().strip().toLowerCase(Locale.ROOT))) {

                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../../resource/registration/Scene2.fxml"));
                    root = fxmlLoader.load();

                    SceneTwoController sceneTwoController = fxmlLoader.getController();
                    sceneTwoController.initUser(user);

                    stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    scene = new Scene(root);
                    scene.setFill(Color.TRANSPARENT);
                    stage.setScene(scene);
                    ScreenDragable.stageDragable(root, stage);
                    stage.show();
                }
                else actionOutput.setText("Please connect to a network and then proceed");
                actionOutput.setTextFill(Color.web("#be4a2f"));
            }
        }
        else{
            actionOutput.setTextFill(Color.web("#f77622"));
            actionOutput.setText("Please provide a gmail address");
        }
    }

    @FXML
    public void onQuit(ActionEvent actionEvent){

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

    public void initUser(User user) {

        /*This method is used to pass object between scenes.*/

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

        nameField.setText(name);
        gmailField.setText(gmail);
        phoneField.setText(phone);
        dobPicker.setValue(dob);
        genderSelect.setValue(gender);
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
    }

    public boolean checkFields() {

        /*This method check for data validity made totally by anurag :) at 12AM 8/27/2021 */

        if (Objects.requireNonNull(nameField.getText()).length() < 3) {
            actionOutput.setText("Name must be at least 3 characters long");
            return false;
        }else if (Objects.requireNonNull(nameField.getText()).length() > 30) {
            actionOutput.setText("Name must be of 30 characters max");
            return false;
        }else if (Objects.requireNonNull(gmailField.getText()).length() <= 10) {
            actionOutput.setText("Please, provide a valid gmail address");
            return false;
        } else if (Objects.requireNonNull(phoneField.getText()).length() < 1) {
            actionOutput.setText("Please, provide your phone number");
            return false;
        } else if (Objects.requireNonNull(phoneField.getText().strip()).length() != 10) {
            actionOutput.setText("Please, provide a valid phone number");
            return false;
        } else if (dobPicker.getValue() == null) {
            actionOutput.setText("Please, provide your birth date");
            return false;
        } else if (genderSelect.getValue() == null) {
            actionOutput.setText("Please, select your gender");
            return false;
        } else if (!actionOutput.getText().equals("All the requirements have been satisfied. Press Confirm to proceed" +
                ".")){
            actionOutput.setText("All the requirements have been satisfied. Press Confirm to proceed.");
            actionOutput.setTextFill(Color.web("#3e8948"));
            btn_SignUpNext.setText("CONFIRM");
            return false;
        } else return true;
    }

    public boolean sendIt(String name,String mail) throws Exception {

        /*This method sends gmail to the provided address only if the user is connected to an internet */

        if (!Objects.equals(gmailField.getText().strip(), gmailOld)){
            user.setSent(false);
            user.setGmailOld(gmailField.getText().strip());
        }

        if (!user.isSent()) {

            try {
                user.setSent(true);
                MailVerify.sendMail(name, mail);
                return true;

            } catch (UnknownHostException | MailConnectException e) {
                System.out.println("Please connect to a network and then proceed");
                return false;
            }
        }
        return true;
    }


}

