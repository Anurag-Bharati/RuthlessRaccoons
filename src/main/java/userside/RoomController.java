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
import javafx.scene.control.*;
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
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Locale;
import java.util.Objects;


@SuppressWarnings("All")
public class RoomController{
    protected static Stage stage;
    protected static Scene scene;
    protected static Parent root;
    static User user;


    static DatabaseManager databaseManager = new DatabaseManager();
    Connection connection;
    PreparedStatement preparedStatement;
    ResultSet resultSet;
    int CID;

    static FXMLLoader fxmlLoader = new FXMLLoader();
    static UserDashboardController userDashboardController;
    static MyBookingsController myBookingsController;
    static InvoiceController invoiceController;

    static Alert alert;

    private @FXML AnchorPane rootStageUser;
    private @FXML AnchorPane rootScene;

    private @FXML ScrollPane roomScrollPane;


    private  @FXML JFXButton Quit;
    private  @FXML JFXButton Minimize;
    private  @FXML JFXButton Expand;
    private  @FXML JFXButton home, myBooking, orderFood, services, invoices, settings, feedback, logout;

    private @FXML JFXButton roomBack;
    private @FXML JFXButton gotoBook;
    private @FXML JFXButton bookRoom;

    private @FXML Region loadBar;

    private @FXML Label userName, userStatus, hotelName;
    // Room
    private @FXML Label roomName, roomDesc, roomPrice, roomRating, roomFloor;
    // Amenity
    private @FXML Label amenity_ac, amenity_attached, amenity_wifi,amenity_landline;
    private @FXML Label AC, FT, WF,AT,RD,LL;
    // BookRoom
    private @FXML DatePicker checkIn, checkOut;
    // Details
    private @FXML Label arrival, departure, day, night;
    // Invoice
    private @FXML Label roomPriceInvoice, tax, off, total;
    Float totalPrice;


    private @FXML Circle onlineIndicator;

    static ScaleTransition scaleTransitionLoadBar;
    static TranslateTransition translateTransitionRootScene;
    static ScaleTransition scaleTransitionApplyUser;
    static FadeTransition fadeTransitionIndicator;

    String db_RID, db_type, db_class, db_amenity,db_short_desc;
    float db_room_price, db_room_rating;
    int db_floor_level;


    public void initUser(User user) {

        /*This method saves data from scene one*/
        RoomController.user = new User();
        RoomController.user.setName(user.getName());
        RoomController.user.setGmail(user.getGmail());
        RoomController.user.setPhone(user.getPhone());
        RoomController.user.setDob(user.getDob());
        RoomController.user.setGender(user.getGender());
        RoomController.user.setPassword(user.getPassword());
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
        }

        else if (actionEvent.getSource().equals(Minimize)){
            stage.setIconified(!stage.isIconified());
        }
        else if (actionEvent.getSource().equals(Expand)){
            stage.setMaximized(!stage.isMaximized());
        }
        else if (actionEvent.getSource().equals(roomBack)|| actionEvent.getSource().equals(home)){
            root.setDisable(true);
            roomScrollPane.setVvalue(0.0);
            animateLoading("userside/userside.fxml", actionEvent);
        }
        else if (actionEvent.getSource().equals(gotoBook)){
            roomScrollPane.setVvalue(0.7);
        }

        else if (actionEvent.getSource().equals(bookRoom)){
            if (checkBooking()){
                alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setHeaderText("Booking Confirmation");
                alert.setContentText("Are you sure to book this room?\nYour Total: $"+totalPrice);
                alert.showAndWait();
                if(alert.getResult().equals(ButtonType.OK)){
                    if (checkIfNotBooked(db_RID)) {
                        if (fetchUserID()) {
                            if (addBooking()) {
                                if (updateRoomStatus(db_RID,"YES")){
                                    System.out.println("[DATABASE] ROOM STATUS HAS BEEN UPDATED");
                                }
                                alert = new Alert(Alert.AlertType.CONFIRMATION);
                                alert.setHeaderText("SUCCESS");
                                alert.setContentText("You have successfully booked this room");
                            } else {
                                alert = new Alert(Alert.AlertType.ERROR);
                                alert.setHeaderText("ERROR");
                                alert.setContentText("Something went wrong! Please try again later.");
                            }

                        } else {
                            alert = new Alert(Alert.AlertType.ERROR);
                            alert.setHeaderText("USER NOT FOUND");
                            alert.setContentText("User ID not found in the database");
                        }
                    } else {
                        alert = new Alert(Alert.AlertType.ERROR);
                        alert.setHeaderText("ROOM NOT AVAILABLE");
                        alert.setContentText("This room is already booked");
                    }
                    alert.showAndWait();

                }
                else if(alert.getResult().equals(ButtonType.CANCEL)){
                    System.out.println("Booking Process Purged!");
                }
            }
        }
        else if (actionEvent.getSource().equals(myBooking)){
            root.setDisable(true);
            animateLoading("userside/myBookings.fxml", actionEvent);
        }
        else if (actionEvent.getSource().equals(invoices)){
            root.setDisable(true);
            animateLoading("userside/invoice.fxml", actionEvent);
        }
        else if (actionEvent.getSource().equals(logout)) {
            purgeConnection();
            logOut();
        }
    }
    @FXML
    private void calculate(){
        total.setText("N/A");
        day.setText("N/A");
        night.setText("N/A");
        arrival.setText("N/A");
        departure.setText("N/A");
        totalPrice = 0.0f;
        if(checkBooking()){
            arrival.setText(checkIn.getValue().toString());
            departure.setText(checkOut.getValue().toString());
            int int_day = Integer.parseInt(String.valueOf(checkIn.getValue().until(checkOut.getValue(), ChronoUnit.DAYS)));
            day.setText(String.valueOf(int_day+1));
            night.setText(String.valueOf(int_day));
            roomPriceInvoice.setText("$"+roomPrice.getText());
            totalPrice = int_day*Float.parseFloat(String.valueOf(roomPrice.getText()));
            totalPrice = (float) Math.round(totalPrice * 100)/100;
            total.setText("$"+totalPrice);
            bookRoom.setDisable(false);
        }
    }

    private void switchToScene(String fxml, ActionEvent actionEvent) throws IOException, SQLException {

        fxmlLoader = new FXMLLoader(getClass().getResource("/main/resource/"+fxml));
        root = fxmlLoader.load();
        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        scene = ((Node) actionEvent.getSource()).getScene();

        if (Objects.equals(fxml, "userside/userside.fxml")){
            userDashboardController = fxmlLoader.getController();
            if (user != null) {
                userDashboardController.initUser(user);
            }
        }
        if (Objects.equals(fxml, "userside/myBookings.fxml")){
            myBookingsController = fxmlLoader.getController();
            if (user != null) {
                myBookingsController.initUser(user);
            }
        }
        if (Objects.equals(fxml, "userside/invoice.fxml")){
             invoiceController = fxmlLoader.getController();
            if (user != null) {
                invoiceController.initUser(user);
            }
        }

        scene.setRoot(root);
        root.setDisable(false);
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
                    } catch (IOException | SQLException ex) {
                        ex.printStackTrace();
                    }
                }
        );
    }

    @FXML
    private void enableCheckOut(){
        if (checkIn.getValue()==null) {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Invalid CheckIn Date");
            alert.setContentText("Please provide a checkIn date");
            alert.showAndWait();

        }
        else if (checkIn.getValue().isBefore(LocalDate.now())) {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Invalid CheckIn Date");
            alert.setContentText("Can not book for past date");
            alert.showAndWait();

        }
        else if (checkIn.getValue().isEqual(LocalDate.now())){
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Invalid CheckIn Date");
            alert.setContentText("Booking MUST be done one day prior");
            alert.showAndWait();
            checkIn.setValue(null);

        } else {
            bookRoom.setDisable(true);
            checkOut.setValue(null);
            checkOut.setPromptText("Departure>");
            checkOut.setDisable(false);
        }
    }

    private boolean checkBooking(){

        if (checkIn.getValue()==null){
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Invalid CheckIn Date");
            alert.setContentText("Please provide a checkIn date");

            alert.showAndWait();
            return false;
        }
        else if (checkOut.getValue()==null){
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Invalid CheckOut Date");
            alert.setContentText("Please provide a checkOut date");
            alert.showAndWait();
            return false;
        }
        LocalDate checkInDate = checkIn.getValue();
        LocalDate checkOutDate = checkOut.getValue();
        LocalDate currentDate = LocalDate.now();

        if (checkInDate.isBefore(currentDate)||checkInDate.isEqual(currentDate)){
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Invalid CheckIn Date");
            alert.setContentText("Booking Must be done one day prior");
            alert.showAndWait();
            System.out.println("Booking Must be done one day prior");
            checkIn.setValue(null);

            return false;
        }

        else if (checkOutDate.isBefore(checkInDate)){
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Invalid CheckOut Date");
            alert.setContentText("Can not book past the check in date");
            alert.showAndWait();
            System.out.println("Invalid CheckOut Date");
            checkOut.setValue(null);
            return false;
        }

        else if (checkOutDate.isEqual(checkInDate)){
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Invalid CheckOut Date");
            alert.setContentText("Can not book on the check in date");
            alert.showAndWait();
            System.out.println("Invalid CheckOut Date");
            checkOut.setValue(null);
            return false;
        }

        return true;
    }
    private boolean fetchUserID() throws SQLException {
        connection = databaseManager.connect();
        preparedStatement = connection.prepareStatement("Select CID from CustomerDetail where gmail = ?");
        preparedStatement.setString(1,user.getGmail());
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
    private boolean addBooking() throws SQLException {
        connection = databaseManager.connect();
        preparedStatement = connection.prepareStatement(
                "INSERT INTO myBookings(CID, RID, arrival, departure, total_price, status)" +
                        "VALUES (?,?,?,?,?,?)");
        preparedStatement.setInt(1,CID);
        preparedStatement.setString(2,db_RID);
        preparedStatement.setDate(3, Date.valueOf(checkIn.getValue()));
        preparedStatement.setDate(4, Date.valueOf(checkOut.getValue()));
        preparedStatement.setFloat(5, totalPrice);
        preparedStatement.setString(6, "BOOKED");
        int queryResult = preparedStatement.executeUpdate();
        return queryResult != 0;

    }
    private boolean checkIfNotBooked(String RoomID) throws SQLException {
        connection = databaseManager.connect();
        preparedStatement = connection.prepareStatement("SELECT isBooked FROM Room WHERE RID =?");
        preparedStatement.setString(1,RoomID);
        resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            if (Objects.equals(resultSet.getString("isBooked"), "NO")){
                resultSet.close();
                preparedStatement.close();
                return true;
            }
        }
        resultSet.close();
        preparedStatement.close();
        return false;
    }

    private boolean fetchRoomDetails(String RoomID) throws SQLException {
        connection = databaseManager.connect();
        preparedStatement = connection.prepareStatement("SELECT * FROM Room WHERE RID =?");
        preparedStatement.setString(1,RoomID);
        resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            db_RID = resultSet.getString("RID");
            db_type = resultSet.getString("type");
            db_class = resultSet.getString("class");
            db_amenity = resultSet.getString("amenity");
            db_room_price = resultSet.getFloat("price");
            db_floor_level = resultSet.getInt("floor_level");
            db_room_rating = resultSet.getFloat("rating");
            db_short_desc = resultSet.getString("short_desc");
            resultSet.close();
            preparedStatement.close();
            return true;
        }
        resultSet.close();
        preparedStatement.close();
        return false;
    }
    private void applyRoomDetails(){
        roomName.setText((db_type+" "+db_class +" - "+db_RID).toUpperCase(Locale.ROOT));
        roomPrice.setText(String.valueOf(db_room_price));
        roomDesc.setText(db_short_desc);
        roomFloor.setText("F"+db_floor_level);
        roomRating.setText("\uD83C\uDF1F"+db_room_rating);

    }

    private void updateAmenity(){
        String[] amenities = db_amenity.split("/");
        for(int i = amenities.length - 1; i >= 0; i--) {
            if (amenities[i].equals("WF")) {
                amenity_wifi.setStyle("-fx-background-color:#229443; -fx-border-color: white");
                WF.setStyle("-fx-background-color: #6FBA3B");
            }
            if (amenities[i].equals("LL")) {
                amenity_landline.setStyle("-fx-background-color:#229443; -fx-border-color: white");
                LL.setStyle("-fx-background-color: #FF51CF");
            }
            if (amenities[i].equals("RD")) {
                RD.setStyle("-fx-background-color: #9F20C0");
            }
            if (amenities[i].equals("FT")) {
                FT.setStyle("-fx-background-color:  #F0C04C");
            }
            if (amenities[i].equals("AT")) {
                amenity_attached.setStyle("-fx-background-color:#229443; -fx-border-color: white");
                AT.setStyle("-fx-background-color:  #2FB6C3");
            }
            if (amenities[i].equals("AC")) {
                amenity_ac.setStyle("-fx-background-color:#229443; -fx-border-color: white");
                AC.setStyle("-fx-background-color:  #dc473c");
            }

        }
    }
    private boolean updateRoomStatus(String RoomID, String status) throws SQLException {
        connection = databaseManager.connect();
        preparedStatement = connection.prepareStatement("UPDATE Room SET isBooked = ? WHERE RID=? ");
        preparedStatement.setString(1,status);
        preparedStatement.setString(2,RoomID);
        int result  = preparedStatement.executeUpdate();
        resultSet.close();
        preparedStatement.close();
        return result >0;
    }

    public void getRoomDetails(String RoomID) throws SQLException {
        if(fetchRoomDetails(RoomID)){
            applyRoomDetails();
            updateAmenity();
        }
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


