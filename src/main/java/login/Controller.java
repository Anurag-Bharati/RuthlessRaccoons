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
import main.java.userside.OS;
import main.java.userside.ResizeHelper;
import main.java.userside.ScreenDragable;
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


/**
 * <H1>LoginController</H1>
 * <p>Used to communicate with GUI</p>
 */
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

    /**
     * <h2>Signup Scene Switch</h2>
     * <p>This method is responsible for switching scenes</p>
     * @param event is the mouse button press event
     * @throws IOException if fxml file is missing
     */
    @FXML
    public void switchToSignUp(ActionEvent event) throws IOException {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(
                    "../../resource/registration/Scene1.fxml"));
            root = fxmlLoader.load();
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            ScreenDragable.stageDragable(root,stage);
            scene.setFill(Color.TRANSPARENT);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     * <h2>Gmail Check</h2>
     * <p>This method checks for gmail</p>
     * @return true if check passes, false if not
     * @throws IOException
     */
    @FXML
    public Boolean onDone() throws IOException {
        if (gmailField.getLength() != 0) {
            return true;
        } else
            actionOutput.setText("Please, Provide your Gmail");
            return false;
    }

    /**
     * <h2>Main Event Handler</h2>
     * <p>This method handles all the events for login scene</p>
     * @param e is the Button Event
     * @throws SQLException if sth wrong w/ either sql query or w/ db
     * @throws IOException if fxml file not found
     */
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

                    } else actionOutput.setText("Incorrect login credential. Please, try again");
                } else actionOutput.setText("Please, Provide a valid Gmail");
            }
        }

    }

    /**
     * <H2>Quit Event Handler</H2>
     * <P>Responsible for animating exit</P>
     * @param actionEvent is quit Button event
     */
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

    /**
     * <h2>Init</h2>
     * <p>This method runs after all the fxml var done injecting</p>
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        comboBox.getItems().addAll("Admin", "User");
        comboBox.getSelectionModel().select("User");
    }

    /**
     * <h2>Responsible for switching stage to Admin Side</h2>
     * @param e Button event
     * @throws IOException if fxml not found
     */
    private void openAdminSide(ActionEvent e) throws IOException{
        stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        stage.close();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(
                "/main/resource/adminside/adminside.fxml"));
        root = fxmlLoader.load();
        scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT);

        stage.setScene(scene);
        if ((OS.isUnix()||OS.isMac())){ScreenDragable.stageDragable(root, stage);}
        if (OS.isWindows()){ResizeHelper.addResizeListener(stage);}
        stage.show();
    }

    /**
     * <h2>Responsible for switching stage to Admin Side</h2>
     * @param user object that is not null
     * @throws IOException if fxml not found
     * @throws SQLException if sth w/ with b/e
     */
    private void openUserSide(User user) throws IOException, SQLException {
        stage.close();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(
                "/main/resource/userside/userside.fxml"));
        root = fxmlLoader.load();
        scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT);
        scene.setUserData(user);
        stage.setScene(scene);
        if ((root.getId().equals("rootStage")) ||OS.isMac()){ScreenDragable.stageDragable(root, stage);}
        if (OS.isWindows()||OS.isUnix()){ResizeHelper.addResizeListener(stage);}
        UserDashboardController userDashboardController =  fxmlLoader.getController();
        userDashboardController.initUser(user);
        stage.show();
    }

    /**
     * <h2>Fetches data from database once login credential is valid</h2>
     *
     * @return user object
     * @throws SQLException if sth wrong w/ b/e
     */
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

    /**
     * <h2>Gmail Domain Check</h2>
     * <p>This function takes gmail as string and checks if the domain is gmail or not.<br>
     *         If not it returns false and true if it is.</p>
     * @param gMail
     * @return
     */
    public boolean checkGmail(String gMail) {


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

    /**
     * <h2>Login validation</h2>
     * <p> This method checks for login credential match</p>
     * @return true if the credential matches and false if not or user not found
     * @throws SQLException if sth wrong w/ b/e
     */
    private boolean checkPass() throws SQLException {
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


