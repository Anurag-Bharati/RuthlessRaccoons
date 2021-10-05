package main.java.adminside;

import com.jfoenix.controls.JFXButton;
import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.ScaleTransition;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import javafx.util.Duration;
import main.java.db.DatabaseManager;

import java.net.URL;
import java.sql.*;
import java.util.Locale;
import java.util.ResourceBundle;

public class Reserved implements Initializable {
    protected Stage stage;
    protected Scene scene;
    protected Parent root;
    DatabaseManager databaseManager = new DatabaseManager();
    Connection connection;
    PreparedStatement preparedStatement;
    ResultSet resultSet;
    ReservedRooms reservedRooms;

    private @FXML
    AnchorPane rootStageUser;
    private @FXML
    JFXButton Quit;
    private @FXML
    JFXButton Minimize;
    private @FXML
    JFXButton Expand;
    private @FXML
    JFXButton home, myBooking, orderFood, services, invoice, settings, feedback, logout;

    @FXML
    private TableColumn<AdminRoom, Integer> IDlist;
    @FXML
    private TableColumn<AdminRoom, String> nameList;
    @FXML
    private TableColumn<AdminRoom, String> phoneList;
    @FXML
    private TableColumn<AdminRoom, Date> arrivalList;
    @FXML
    private TableColumn<AdminRoom, Date> depList;
    @FXML
    private TableColumn<AdminRoom, Float> priceList;

    private @FXML
    TableView tableView;

    @FXML
    private Button dltBtn;

    private @FXML
    Region loadBar;

    private @FXML
    Label userName, userStatus, hotelName;
    private GuestList guestList;


    @FXML
    private void onAction(ActionEvent actionEvent) throws SQLException {
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
        if (actionEvent.getSource().equals(Minimize)) {
            stage.setIconified(!stage.isIconified());
        }
        if (actionEvent.getSource().equals(Expand)) {
            String max = System.getProperty("os.name", "unknown").toLowerCase(Locale.ROOT);
            if (max.contains("win")) {
                stage.setMaximized(!stage.isMaximized());
            }
        }

    }

    @FXML
    public ObservableList<ReservedRooms> getReservedRooms() throws SQLException {
        ObservableList<ReservedRooms> rooms = FXCollections.observableArrayList();
        connection = databaseManager.connect();

        preparedStatement = connection.prepareStatement(
                "select mb.BID, cd.name, cd.phone ,mb.arrival, mb.departure, mb.total_price from CustomerDetail as cd , myBookings as mb where cd.CID =mb.CID and mb.status = ?");
        preparedStatement.setString(1,"BOOKED");
        try {
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {

                reservedRooms = new ReservedRooms(
                        resultSet.getInt("BID"), resultSet.getString("name"),
                        resultSet.getString("phone"), resultSet.getDate("arrival"),
                        resultSet.getDate("departure"),resultSet.getFloat("total_price"));

                rooms.add(reservedRooms);
            }
            resultSet.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return rooms;
    }

    public void showGuests() throws SQLException {

        ObservableList<ReservedRooms> GuestTable = getReservedRooms();

        IDlist.setCellValueFactory(new PropertyValueFactory<>("RID"));
        nameList.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        phoneList.setCellValueFactory(new PropertyValueFactory<>("phoneNo"));
        arrivalList.setCellValueFactory(new PropertyValueFactory<>("arrival"));
        depList.setCellValueFactory(new PropertyValueFactory<>("departure"));
        priceList.setCellValueFactory(new PropertyValueFactory<>("price"));

        tableView.setItems(GuestTable);
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

    public void init() throws SQLException {
        // showRooms();
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            showGuests();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}

