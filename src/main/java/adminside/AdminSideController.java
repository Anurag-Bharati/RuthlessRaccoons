package main.java.adminside;

import com.jfoenix.controls.JFXButton;
import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.ScaleTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

public class AdminSideController implements Initializable {
    protected Stage stage;
    protected Scene scene;
    protected Parent root;

    private @FXML AnchorPane rootStageUser;
    private  @FXML JFXButton Quit;
    private  @FXML JFXButton Minimize;
    private  @FXML JFXButton Expand;
    private  @FXML JFXButton home, myBooking, orderFood, services, invoice, settings, feedback, logout;

    private @FXML JFXButton roomA, roomB, roomC, roomD, roomE, roomF, roomG, roomH, roomI, roomJ, rookK,roomL;
    private @FXML JFXButton roomBack;
    private @FXML JFXButton gotoBook;
    private @FXML JFXButton bookRoom;

    private @FXML
    Region loadBar;

    private @FXML Label userName, userStatus, hotelName;
    // Room
    private @FXML Label roomName, roomDesc, roomPrice, roomRating, roomFloor;
    // Amenity
    private @FXML Label amenity_ac, amenity_attached, amenity_wifi,amenity_landline;
    // BookRoom
    private @FXML
    DatePicker checkIn, checkOut;
    // Details
    private @FXML Label arrival, departure, day, night;
    // Invoice
    private @FXML Label roomPriceInvoice, tax, off, total;

    @FXML
    private void onAction(ActionEvent actionEvent){
        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
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
        if (actionEvent.getSource().equals(Minimize)){
            stage.setIconified(!stage.isIconified());
        }
        if (actionEvent.getSource().equals(Expand)){
            stage.setMaximized(!stage.isMaximized());
        }
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

    }
}


