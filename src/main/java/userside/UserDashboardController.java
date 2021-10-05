package main.java.userside;

import com.jfoenix.controls.JFXButton;
import javafx.animation.*;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import javafx.stage.StageStyle;
import javafx.util.Duration;
import main.java.db.DatabaseManager;
import main.java.registration.User;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Objects;


@SuppressWarnings("All")
public class UserDashboardController {
    protected static Stage stage;
    protected static Scene scene;
    protected static Parent root;
    static User user;

    DatabaseManager databaseManager = new DatabaseManager();
    Connection connection;
    PreparedStatement preparedStatement;
    ResultSet resultSet;
    Room room;

    static FXMLLoader fxmlLoader = new FXMLLoader();
    static RoomController roomController;
    static MyBookingsController myBookingsController;
    static InvoiceController invoiceController;
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
    JFXButton home, myBooking, orderFood, services, invoices, settings, feedback, logout;

    private @FXML
    JFXButton ROOM101, ROOM102, ROOM103, ROOM104,
            ROOM105, ROOM106, ROOM107, ROOM108,
            ROOM109, ROOM110, ROOM111, ROOM112;

    private @FXML
    Label   ROOM101_name, ROOM102_name, ROOM103_name, ROOM104_name,
            ROOM105_name, ROOM106_name, ROOM107_name, ROOM108_name,
            ROOM109_name, ROOM110_name, ROOM111_name, ROOM112_name;

    private @FXML
    Label   ROOM101_price, ROOM102_price, ROOM103_price, ROOM104_price,
            ROOM105_price, ROOM106_price, ROOM107_price, ROOM108_price,
            ROOM109_price, ROOM110_price, ROOM111_price, ROOM112_price;

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


    public void initUser(User user) throws SQLException {

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
    private void onAction(ActionEvent actionEvent) throws SQLException, IOException {
        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        root = stage.getScene().getRoot();
        if (actionEvent.getSource().equals(Quit)) {
            purgeConnection();
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
        } else if (actionEvent.getSource().equals(ROOM101) ||
                actionEvent.getSource().equals(ROOM102) ||
                actionEvent.getSource().equals(ROOM103) ||
                actionEvent.getSource().equals(ROOM104) ||
                actionEvent.getSource().equals(ROOM105) ||
                actionEvent.getSource().equals(ROOM106) ||
                actionEvent.getSource().equals(ROOM107) ||
                actionEvent.getSource().equals(ROOM108) ||
                actionEvent.getSource().equals(ROOM109) ||
                actionEvent.getSource().equals(ROOM110) ||
                actionEvent.getSource().equals(ROOM111) ||
                actionEvent.getSource().equals(ROOM112) ||
                actionEvent.getSource().equals(BOOK_NOW)
        ) {
            root.setDisable(true);
            animateLoading("userside/usersideRoom.fxml", actionEvent);
        } else if (actionEvent.getSource().equals(myBooking)) {
            root.setDisable(true);
            animateLoading("userside/myBookings.fxml", actionEvent);
        }
        else if (actionEvent.getSource().equals(invoices)) {
            root.setDisable(true);
            animateLoading("userside/invoice.fxml", actionEvent);
        }
        else if (actionEvent.getSource().equals(logout)) {
            purgeConnection();
            logOut();
        }

    }

    private void logOut() throws IOException {
        stage.close();
        Parent root = FXMLLoader.load((Objects.requireNonNull(
                getClass().getResource("/main/resource/login/Login_Scene.fxml"))));
        scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT);
        Stage stage = new Stage();
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setScene(scene);
        ScreenDragable.stageDragable(root, stage);
        stage.show();
    }

    private void purgeConnection() throws SQLException {
        if (!(connection ==null)){
            connection.close();

        }
        connection = null;
    }

    private void switchToScene(String fxml, ActionEvent actionEvent) throws IOException, SQLException {
        purgeConnection();
        fxmlLoader = new FXMLLoader(getClass().getResource("/main/resource/" + fxml));
        root = fxmlLoader.load();
        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        scene = ((Node) actionEvent.getSource()).getScene();

        if (Objects.equals(fxml, "userside/usersideRoom.fxml")) {
            if (user != null) {
                roomController = fxmlLoader.getController();
                roomController.initUser(user);
                roomController.getRoomDetails(checkRoomID(actionEvent));
            }
        }
        else {
            if (Objects.equals(fxml, "userside/myBookings.fxml")) {
                myBookingsController = fxmlLoader.getController();
                myBookingsController.initUser(user);
            } else if (Objects.equals(fxml, "userside/invoice.fxml")) {
                invoiceController = fxmlLoader.getController();
                invoiceController.initUser(user);
            }
        }

        scene.setRoot(root);
        root.setDisable(false);
    }

    private String checkRoomID(ActionEvent event){
        Object source = event.getSource();
        if (ROOM101.equals(source)) { return "ROOM101";
        } else if (ROOM102.equals(source)) { return "ROOM102";
        } else if (ROOM103.equals(source)) { return "ROOM103";
        } else if (ROOM104.equals(source)) { return "ROOM104";
        } else if (ROOM105.equals(source)) { return "ROOM105";
        } else if (ROOM106.equals(source)) { return "ROOM106";
        } else if (ROOM107.equals(source)) { return "ROOM107";
        } else if (ROOM108.equals(source)) { return "ROOM108";
        } else if (ROOM109.equals(source)) { return "ROOM109";
        } else if (ROOM110.equals(source)) { return "ROOM110";
        } else if (ROOM111.equals(source)) { return "ROOM111";
        } else if (ROOM112.equals(source)) { return "ROOM112";
        } else return "ROOM101";
    }

    @FXML
    private void applyUser() throws SQLException {
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
            applyRoomDetails();

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
    private ObservableList<Room> fetchRoomDetails() throws SQLException {
        ObservableList<Room> Rooms = FXCollections.observableArrayList();
        connection = databaseManager.connect();
        preparedStatement = connection.prepareStatement("SELECT * FROM Room");
        resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            room = new Room(resultSet.getString("RID"),resultSet.getFloat("price"));
            Rooms.add(room);
        }
        resultSet.close();
        preparedStatement.close();
        return Rooms;
    }
    private void applyRoomDetails() throws SQLException {
        ObservableList<Room> Rooms = fetchRoomDetails();
        int roomCount = 0;
        for(Room room : Rooms){
            roomCount = roomCount+1;
        }
        if (roomCount==12) {
            ROOM101_name.setText(Rooms.get(0).getRID());
            ROOM101_price.setText("$"+Rooms.get(0).getPrice());

            ROOM102_name.setText(Rooms.get(1).getRID());
            ROOM102_price.setText("$"+Rooms.get(1).getPrice());

            ROOM103_name.setText(Rooms.get(2).getRID());
            ROOM103_price.setText("$"+Rooms.get(2).getPrice());

            ROOM104_name.setText(Rooms.get(3).getRID());
            ROOM104_price.setText("$"+Rooms.get(3).getPrice());

            ROOM105_name.setText(Rooms.get(4).getRID());
            ROOM105_price.setText("$"+Rooms.get(4).getPrice());

            ROOM106_name.setText(Rooms.get(5).getRID());
            ROOM106_price.setText("$"+Rooms.get(5).getPrice());

            ROOM107_name.setText(Rooms.get(6).getRID());
            ROOM107_price.setText("$"+Rooms.get(6).getPrice());

            ROOM108_name.setText(Rooms.get(7).getRID());
            ROOM108_price.setText("$"+Rooms.get(7).getPrice());

            ROOM109_name.setText(Rooms.get(8).getRID());
            ROOM109_price.setText("$"+Rooms.get(8).getPrice());

            ROOM110_name.setText(Rooms.get(9).getRID());
            ROOM110_price.setText("$"+Rooms.get(9).getPrice());

            ROOM111_name.setText(Rooms.get(10).getRID());
            ROOM111_price.setText("$"+Rooms.get(10).getPrice());

            ROOM112_name.setText(Rooms.get(11).getRID());
            ROOM112_price.setText("$"+Rooms.get(11).getPrice());
        }
    }
}

