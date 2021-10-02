package main.java.userside;

import com.jfoenix.controls.JFXButton;
import javafx.animation.*;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;
import main.java.registration.User;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Objects;

//@SuppressWarnings("All")

public class UserDashboardController {
    protected static Stage stage;
    protected static Scene scene;
    protected static Parent root;
    static User user;


    static FXMLLoader fxmlLoader = new FXMLLoader();
    static RoomController roomController;
    static MyBookingsController myBookingsController;
    static LocalDateTime dateTime;
    static Alert alert;

    private @FXML
    AnchorPane rootStageUser;
    private @FXML
    AnchorPane rootScene;


    private @FXML
    JFXButton Quit;
    private @FXML
    JFXButton Minimize;
    private @FXML
    JFXButton Expand;
    private @FXML
    JFXButton home, myBooking, orderFood, services, invoice, settings, feedback, logout;

    private @FXML
    JFXButton roomA, roomB, roomC, roomD, roomE, roomF, roomG, roomH, roomI, roomJ, rookK, roomL;
    private @FXML
    JFXButton BOOK_NOW;

    private @FXML
    Region loadBar;

    private @FXML
    Label userName, userStatus, hotelName;

    private @FXML
    Circle onlineIndicator;

    static ScaleTransition scaleTransitionLoadBar;
    static TranslateTransition translateTransitionRootScene;
    static ScaleTransition scaleTransitionApplyUser;
    static FadeTransition fadeTransitionIndicator;


    public void initUser(User user) {

        /*This method saves data from scene one*/
        UserDashboardController.user = new User();
        UserDashboardController.user.setName(user.getName());
        UserDashboardController.user.setGmail(user.getGmail());
        UserDashboardController.user.setPhone(user.getPhone());
        UserDashboardController.user.setDob(user.getDob());
        UserDashboardController.user.setGender(user.getGender());
        UserDashboardController.user.setPassword(user.getPassword());
        applyUser();
    }

    @FXML
    private void onAction(ActionEvent actionEvent) throws SQLException {
        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        root = stage.getScene().getRoot();
        if (actionEvent.getSource().equals(Quit)) {
            FadeTransition fadeTransition = new FadeTransition(Duration.seconds(.4), rootStageUser);
            ScaleTransition scaleTransition = new ScaleTransition(Duration.seconds(.4), rootStageUser);

            scaleTransition.setInterpolator(Interpolator.EASE_IN);
            scaleTransition.setByX(-.05);
            scaleTransition.setByY(-.9);

            fadeTransition.setInterpolator(Interpolator.EASE_IN);
            fadeTransition.setFromValue(1.0);
            fadeTransition.setToValue(0);

            scaleTransition.play();
            fadeTransition.play();

            fadeTransition.setOnFinished(actionEvent1 -> {
                scaleTransition.stop();
                fadeTransition.stop();
                actionEvent.consume();
                stage.close();
                Platform.exit();
                System.exit(0);
            });
        } else if (actionEvent.getSource().equals(Minimize)) {
            stage.setIconified(!stage.isIconified());
        } else if (actionEvent.getSource().equals(Expand)) {
            stage.setMaximized(!stage.isMaximized());
        } else if (actionEvent.getSource().equals(roomA) || actionEvent.getSource().equals(BOOK_NOW)) {
            root.setDisable(true);
            animateLoading("userside/usersideRoom.fxml", actionEvent);
        } else if (actionEvent.getSource().equals(home)) {
            return;

        } else if (actionEvent.getSource().equals(myBooking)) {
            root.setDisable(true);
            animateLoading("userside/myBookings.fxml", actionEvent);
        }
    }

    private void switchToScene(String fxml, ActionEvent actionEvent) throws IOException, SQLException {

        fxmlLoader = new FXMLLoader(getClass().getResource("/main/resource/" + fxml));
        root = fxmlLoader.load();

        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        scene = ((Node) actionEvent.getSource()).getScene();

        if (Objects.equals(fxml, "userside/usersideRoom.fxml")) {
            if (user != null) {
                roomController = fxmlLoader.getController();
                roomController.initUser(user);
                roomController.getRoomDetails("ROOM101");
            }
        } else {
            if (Objects.equals(fxml, "userside/myBookings.fxml")) {
                myBookingsController = fxmlLoader.getController();
                myBookingsController.initUser(user);
            } else if (Objects.equals(fxml, "userside/invoice.fxml")) {
                // TODO: 10/1/2021  
            }
        }
        scene.setRoot(root);
        root.setDisable(false);
    }

    @FXML
    private void applyUser() {
        if (user != null) {
            userName.setText(user.getName().strip().toUpperCase());
            userStatus.setText("Online");
            onlineIndicator.setFill(Color.web("#74BE3D"));

            scaleTransitionApplyUser = new ScaleTransition(Duration.seconds(.5), onlineIndicator);
            scaleTransitionApplyUser.setInterpolator(Interpolator.EASE_BOTH);
            scaleTransitionApplyUser.setByX(1);
            scaleTransitionApplyUser.setByY(1);
            scaleTransitionApplyUser.setCycleCount(-1);
            scaleTransitionApplyUser.setAutoReverse(false);
            scaleTransitionApplyUser.play();

            fadeTransitionIndicator = new FadeTransition(Duration.seconds(0.5), onlineIndicator);
            fadeTransitionIndicator.setFromValue(1.0);
            fadeTransitionIndicator.setToValue(0.0);
            fadeTransitionIndicator.setCycleCount(Animation.INDEFINITE);
            fadeTransitionIndicator.setInterpolator(Interpolator.EASE_BOTH);
            fadeTransitionIndicator.play();

        } else this.userName.setText("ANURAG");
    }


    @FXML
    private void setOnHover() {
        if (Quit.isHover()) {
            Quit.setStyle("-fx-background-color: #dc473c");
        }
        if (Minimize.isHover()) {
            Minimize.setStyle("-fx-background-color: #F0C04C");
        }
        if (Expand.isHover()) {
            Expand.setStyle("-fx-background-color: #ec6a45");
        }

    }

    @FXML
    private void setDefault() {
        if (!Quit.isHover()) {
            Quit.setStyle("-fx-background-color: Transparent");
        }
        if (!Minimize.isHover()) {
            Minimize.setStyle("-fx-background-color: Transparent");
        }
        if (!Expand.isHover()) {
            Expand.setStyle("-fx-background-color: Transparent");
        }

    }

    private void animateLoading(String fxml, ActionEvent actionEvent) {
        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        scene = ((Node) actionEvent.getSource()).getScene();

        scaleTransitionLoadBar = new ScaleTransition(Duration.seconds(1), loadBar);
        scaleTransitionLoadBar.setByX(stage.getWidth() * 0.20);
        scaleTransitionLoadBar.setInterpolator(Interpolator.EASE_BOTH);

        translateTransitionRootScene = new TranslateTransition(Duration.seconds(.3), rootScene);
        translateTransitionRootScene.setInterpolator(Interpolator.EASE_IN);
        translateTransitionRootScene.setToY(stage.getHeight());

        scaleTransitionLoadBar.play();
        translateTransitionRootScene.play();

        scaleTransitionLoadBar.setOnFinished(e -> {
                    try {
                        scaleTransitionLoadBar.stop();
                        translateTransitionRootScene.stop();
                        switchToScene(fxml, actionEvent);

                        Runtime.getRuntime().gc();
                    } catch (IOException | SQLException ex) {
                        ex.printStackTrace();
                    }
                }
        );
    }
}

