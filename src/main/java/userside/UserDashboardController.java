package main.java.userside;

import com.jfoenix.controls.JFXButton;
import javafx.animation.*;
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
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;
import main.java.registration.User;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import java.util.ResourceBundle;

//@SuppressWarnings("All")

public class UserDashboardController implements Initializable {
    protected static Stage stage;
    protected static Scene scene;
    protected static Parent root;
    static User user;
    static FXMLLoader fxmlLoader = new FXMLLoader();
    static UserDashboardController userDashboardController;
    static MyBookingsController myBookingsController;
    static LocalDateTime dateTime;
    static Alert alert;

    private @FXML AnchorPane rootStageUser;
    private @FXML AnchorPane rootScene;

    private @FXML ScrollPane roomScrollPane;


    private  @FXML JFXButton Quit;
    private  @FXML JFXButton Minimize;
    private  @FXML JFXButton Expand;
    private  @FXML JFXButton home, myBooking, orderFood, services, invoice, settings, feedback, logout;

    private @FXML JFXButton roomA, roomB, roomC, roomD, roomE, roomF, roomG, roomH, roomI, roomJ, rookK,roomL;
    private @FXML JFXButton BOOK_NOW;
    private @FXML JFXButton roomBack;
    private @FXML JFXButton gotoBook;
    private @FXML JFXButton bookRoom;

    private @FXML Region loadBar;

    private @FXML Label userName, userStatus, hotelName;
    // Room
    private @FXML Label roomName, roomDesc, roomPrice, roomRating, roomFloor;
    // Amenity
    private @FXML Label amenity_ac, amenity_attached, amenity_wifi,amenity_landline;
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
        else if (actionEvent.getSource().equals(roomA) || actionEvent.getSource().equals(BOOK_NOW)){
            root.setDisable(true);
            animateLoading("userside/usersideRoom.fxml",actionEvent);

        }
        else if (actionEvent.getSource().equals(roomBack)|| actionEvent.getSource().equals(home)){
            if(rootStageUser.getChildren().get(0).getId().equals("homePane")){
                return;
            } else {
                root.setDisable(true);
                roomScrollPane.setVvalue(0.0);
                animateLoading("userside/userside.fxml", actionEvent);
            }
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
                    // TODO: 10/1/2021 Send Booking Data to DB
                    System.out.println("Nice");
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


    private void switchToSubScene(String fxml, ActionEvent actionEvent) throws IOException {

        fxmlLoader = new FXMLLoader(getClass().getResource("/main/resource/"+fxml));
        root = fxmlLoader.load();
        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        scene = ((Node) actionEvent.getSource()).getScene();
        if (Objects.equals(fxml, "userside/userside.fxml") || Objects.equals(fxml, "userside/usersideRoom.fxml")) {
            userDashboardController = fxmlLoader.getController();
            if (user != null) {
                userDashboardController.initUser(user);
            }
        }
        else {
            if (Objects.equals(fxml,"userside/myBookings.fxml")){
                myBookingsController = fxmlLoader.getController();
                myBookingsController.initUser(user);
            }
            else if (Objects.equals(fxml,"userside/invoice.fxml")){
                // TODO: 10/1/2021  
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
                        switchToSubScene(fxml,actionEvent);
                        Runtime.getRuntime().gc();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
        );
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
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
}


