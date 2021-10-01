package main.java.userside;

import com.jfoenix.controls.JFXButton;
import javafx.animation.*;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;
import main.java.db.DatabaseManager;
import main.java.registration.User;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;


public class MyBookingsController implements Initializable {

    protected static Stage stage;
    protected static Scene scene;
    protected static Parent root;
    static User user;

    UserDashboardController userDashboardController;

    static FXMLLoader fxmlLoader = new FXMLLoader();
    static LocalDateTime dateTime;
    static Alert alert;

    private  @FXML JFXButton Quit;
    private  @FXML JFXButton Minimize;
    private  @FXML JFXButton Expand;
    private  @FXML JFXButton home, myBookings_btn, orderFood, services, invoice, settings, feedback, logout;

    private @FXML
    Circle onlineIndicator;


    static ScaleTransition scaleTransitionLoadBar;
    static TranslateTransition translateTransitionRootScene;
    static ScaleTransition scaleTransitionApplyUser;
    static FadeTransition fadeTransitionIndicator;


    private @FXML
    Region loadBar;

    private @FXML Label userName, userStatus, hotelName;

    private @FXML Label selected_roomName, selected_room_dates;

    private @FXML JFXButton myBookingDelete, myBookingUpdate;

    DatabaseManager databaseManager = new DatabaseManager();
    PreparedStatement preparedStatement;
    ResultSet resultSet;
    MyBooking myBooking;

    private @FXML AnchorPane rootStageUser;
    private @FXML AnchorPane rootScene;

    @FXML
    public TableView<MyBooking> tableView;


    @FXML
    private TableColumn<MyBooking, Integer> booking_id;

    @FXML
    private TableColumn<MyBooking, String> arrival;
    @FXML
    private TableColumn<MyBooking, String> departure;
    @FXML
    private TableColumn<MyBooking, Float> room_price;
    @FXML
    private TableColumn<MyBooking, Float> total_price;
    @FXML
    private TableColumn<MyBooking, String> room_name;
    @FXML
    private TableColumn<MyBooking, Timestamp> timestamp;
    @FXML
    private TableColumn<MyBooking, String> status;

    @FXML
    private void onAction(ActionEvent actionEvent){
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
        }
        else if (actionEvent.getSource().equals(Minimize)){
            stage.setIconified(!stage.isIconified());
        }
        else if (actionEvent.getSource().equals(Expand)){
            stage.setMaximized(!stage.isMaximized());
        }
        else if (actionEvent.getSource().equals(home)){
            if(rootStageUser.getChildren().get(0).getId().equals("homePane")){
                return;
            } else {
                root.setDisable(true);
                animateLoading("userside/userside.fxml", actionEvent);
            }
        }
        else if (actionEvent.getSource().equals(myBookings_btn)){
            if(rootStageUser.getChildren().get(0).getId().equals("myBookingsPane")) {
                return;
            }
        }
        else if (actionEvent.getSource().equals(myBookingUpdate)){
            alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setHeaderText("BOOKING UPDATE CONFIRMATION");
            alert.setContentText("Are you sure to update " +selected_roomName.getText()+ "?");
            alert.showAndWait();
        }
        else if (actionEvent.getSource().equals(myBookingDelete)){
            alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setHeaderText("BOOKING DELETION CONFIRMATION");
            alert.setContentText("Are you sure to delete "+ selected_roomName.getText()+"?");
            alert.showAndWait();
        }

    }

    @FXML
    public ObservableList<MyBooking> getMyBookings() throws SQLException {
        ObservableList<MyBooking> myBookings = FXCollections.observableArrayList();
        Connection connection =databaseManager.connect();
        preparedStatement = connection.prepareStatement("select * from myBookings where CID = ?");
        preparedStatement.setInt(1,1);
        try {
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {

                myBooking = new MyBooking(
                        resultSet.getInt("BID"), resultSet.getString("arrival"),
                        resultSet.getString("departure"), resultSet.getFloat("room_price"),
                        resultSet.getFloat("total_price"), resultSet.getString("room_name"),
                        resultSet.getTimestamp("booked_date"), resultSet.getString("status"));
                myBookings.add(myBooking);
            }
            resultSet.close();
            connection.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return myBookings;
    }
    public void showMyBookings() throws SQLException {

        ObservableList<MyBooking> list = getMyBookings();

        booking_id.setCellValueFactory(new PropertyValueFactory<MyBooking, Integer>("booking_id"));
        arrival.setCellValueFactory(new PropertyValueFactory<MyBooking, String>("arrival"));
        departure.setCellValueFactory(new PropertyValueFactory<MyBooking, String>("departure"));
        room_price.setCellValueFactory(new PropertyValueFactory<MyBooking, Float>("room_price"));
        total_price.setCellValueFactory(new PropertyValueFactory<MyBooking, Float>("total_price"));
        room_name.setCellValueFactory(new PropertyValueFactory<MyBooking, String>("room_name"));
        timestamp.setCellValueFactory(new PropertyValueFactory<MyBooking, Timestamp>("timestamp"));
        status.setCellValueFactory(new PropertyValueFactory<MyBooking, String>("status"));
        tableView.setItems(list);
    }
    @FXML
    private void selectBooking() {
        if (!myBookingDelete.isDisable()){
            myBookingDelete.setDisable(true);
        }
        if (!myBookingUpdate.isDisable()){
            myBookingUpdate.setDisable(true);
        }


        int index = tableView.getSelectionModel().getSelectedIndex();
        if (index <= -1) {
            selected_roomName.setText("NOT SELECTED");
            selected_room_dates.setText("N/A");
        }
        else if (status.getCellData(index).equals("CHECKED IN")){
            if (!myBookingDelete.isDisable()){
                myBookingDelete.setDisable(true);
            }
            if (!myBookingUpdate.isDisable()){
                myBookingUpdate.setDisable(true);
            }
            selected_roomName.setText(room_name.getCellData(index).toUpperCase(Locale.ROOT));
            selected_room_dates.setText("ROOM IS CHECKED IN");
        }
        else {

            String arrival_departure = arrival.getCellData(index) +" / "+ departure.getCellData(index);
            if (myBookingDelete.isDisable()){
                myBookingDelete.setDisable(false);
            }
            if (myBookingUpdate.isDisable()){
                myBookingUpdate.setDisable(false);
            }

            selected_roomName.setText(room_name.getCellData(index).toUpperCase(Locale.ROOT));
            selected_room_dates.setText(arrival_departure.toUpperCase(Locale.ROOT));

        }
    }

    private void animateLoading(String fxml, ActionEvent actionEvent){
        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        scene = ((Node) actionEvent.getSource()).getScene();

        scaleTransitionLoadBar = new ScaleTransition(Duration.seconds(1), loadBar);
        scaleTransitionLoadBar.setByX(stage.getWidth()*0.20);
        scaleTransitionLoadBar.setInterpolator(Interpolator.EASE_BOTH);

        translateTransitionRootScene = new TranslateTransition(Duration.seconds(.3), rootScene);
        translateTransitionRootScene.setInterpolator(Interpolator.EASE_IN);
        translateTransitionRootScene.setToY(stage.getHeight());

        scaleTransitionLoadBar.play();
        translateTransitionRootScene.play();

        scaleTransitionLoadBar.setOnFinished(e->{
                    try {
                        scaleTransitionLoadBar.stop();
                        translateTransitionRootScene.stop();
                        switchToScene(fxml,actionEvent);
                        Runtime.getRuntime().gc();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
        );
    }

    private void switchToScene(String fxml, ActionEvent actionEvent) throws IOException {

        fxmlLoader = new FXMLLoader(getClass().getResource("/main/resource/"+fxml));
        root = fxmlLoader.load();
        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        scene = ((Node) actionEvent.getSource()).getScene();
        if (Objects.equals(fxml,"userside/userside.fxml")) {
            userDashboardController = fxmlLoader.getController();
            if (user != null) {
                userDashboardController.initUser(user);
            }
        }

        scene.setRoot(root);
        root.setDisable(false);
    }

    @FXML
    private void setOnHover(){
        if (Quit.isHover()) {
            Quit.setStyle("-fx-background-color: #dc473c");
        }
        if (Minimize.isHover()){
            Minimize.setStyle("-fx-background-color: #F0C04C");
        }
        if (Expand.isHover()){
            Expand.setStyle("-fx-background-color: #ec6a45");
        }

    }
    @FXML
    private void setDefault(){
        if (!Quit.isHover()) {
            Quit.setStyle("-fx-background-color: Transparent");
        }
        if (!Minimize.isHover()){
            Minimize.setStyle("-fx-background-color: Transparent");
        }
        if (!Expand.isHover()){
            Expand.setStyle("-fx-background-color: Transparent");
        }

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            showMyBookings();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void initUser(User user) {
        /*This method is used to pass object between scenes.*/
        MyBookingsController.user = new User();
        MyBookingsController.user.setName(user.getName());
        MyBookingsController.user.setGmail(user.getGmail());
        MyBookingsController.user.setPhone(user.getPhone());
        MyBookingsController.user.setDob(user.getDob());
        MyBookingsController.user.setGender(user.getGender());
        MyBookingsController.user.setPassword(user.getPassword());
        applyUser();

    }

    @FXML
    private void applyUser(){
        if (user!=null) {
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

}
