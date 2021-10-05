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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
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
import java.sql.*;
import java.util.Objects;

@SuppressWarnings("All")
public class InvoiceController{

    protected static Stage stage;
    protected static Scene scene;
    protected static Parent root;
    static User user;
    int CID;

    Connection connection;

    UserDashboardController userDashboardController;
    MyBookingsController myBookingsController;

    static FXMLLoader fxmlLoader = new FXMLLoader();
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
    private TableColumn<MyBooking, Float> price;
    @FXML
    private TableColumn<MyBooking, Float> total_price;
    @FXML
    private TableColumn<MyBooking, String> room_id;
    @FXML
    private TableColumn<MyBooking, Timestamp> timestamp;
    @FXML
    private TableColumn<MyBooking, String> status;

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

            fadeTransition.setOnFinished(e -> {
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
            root.setDisable(true);
            animateLoading("userside/userside.fxml", actionEvent);
        }
        else if (actionEvent.getSource().equals(myBookings_btn)){
            root.setDisable(true);
            animateLoading("userside/myBookings.fxml", actionEvent);
        }
        else if (actionEvent.getSource().equals(logout)) {
            purgeConnection();
            logOut();
        }


    }

    @FXML
    public ObservableList<MyBooking> getMyBookings() throws SQLException {
        ObservableList<MyBooking> myBookings = FXCollections.observableArrayList();
        connection =databaseManager.connect();

        preparedStatement = connection.prepareStatement(
                "select BID, arrival, departure, total_price, booked_date, status, myBookings.RID, price " +
                        "from myBookings, Room where myBookings.RID = Room.RID and CID = ? and not status = ?");
        preparedStatement.setInt(1,CID);
        preparedStatement.setString(2,"BOOKED");
        try {
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {

                myBooking = new MyBooking(
                        resultSet.getInt("BID"), resultSet.getString("arrival"),
                        resultSet.getString("departure"), resultSet.getFloat("price"),
                        resultSet.getFloat("total_price"), resultSet.getString("RID"),
                        resultSet.getTimestamp("booked_date"), resultSet.getString("status"));
                myBookings.add(myBooking);
            }
            resultSet.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return myBookings;
    }
    public void showMyBookings() throws SQLException {

        ObservableList<MyBooking> bookingList = getMyBookings();

        booking_id.setCellValueFactory(new PropertyValueFactory<>("booking_id"));
        arrival.setCellValueFactory(new PropertyValueFactory<>("arrival"));
        departure.setCellValueFactory(new PropertyValueFactory<>("departure"));
        price.setCellValueFactory(new PropertyValueFactory<>("price"));
        total_price.setCellValueFactory(new PropertyValueFactory<>("total_price"));
        room_id.setCellValueFactory(new PropertyValueFactory<>("room_id"));
        timestamp.setCellValueFactory(new PropertyValueFactory<>("timestamp"));
        status.setCellValueFactory(new PropertyValueFactory<>("status"));

        tableView.setItems(bookingList);
    }

    private void animateLoading(String fxml, ActionEvent actionEvent) throws SQLException {
        purgeConnection();

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

    private void switchToScene(String fxml, ActionEvent actionEvent) throws IOException, SQLException {

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
        if (Objects.equals(fxml,"userside/myBookings.fxml")) {
            myBookingsController = fxmlLoader.getController();
            if (user != null) {
                myBookingsController.initUser(user);
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
    private boolean fetchUserID() throws SQLException {
        connection = databaseManager.connect();
        preparedStatement = connection.prepareStatement("Select CID from CustomerDetail where gmail = ?");
        preparedStatement.setString(1,InvoiceController.user.getGmail());
        resultSet = preparedStatement.executeQuery();
        if (resultSet.next()){
            CID = resultSet.getInt("CID");
            resultSet.close();
            preparedStatement.close();
            return true;
        } else {
            resultSet.close();
            preparedStatement.close();
            return false;
        }
    }

    public void initUser(User user) throws SQLException {
        /*This method is used to pass object between scenes.*/
        InvoiceController.user = new User();
        InvoiceController.user.setName(user.getName());
        InvoiceController.user.setGmail(user.getGmail());
        InvoiceController.user.setPhone(user.getPhone());
        InvoiceController.user.setDob(user.getDob());
        InvoiceController.user.setGender(user.getGender());
        InvoiceController.user.setPassword(user.getPassword());
        applyUser();
        if (fetchUserID()){
            showMyBookings();
        }
        else{
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("USER NOT FOUND");
            alert.setContentText("User ID not found in the database");
        }
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
    private void purgeConnection() throws SQLException {
        if (!(connection ==null)){
            connection.close();

        }
        connection = null;
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
}
