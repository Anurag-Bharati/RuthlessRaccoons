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

public class Invoice implements Initializable {
    protected Stage stage;
    protected Scene scene;
    protected Parent root;
    DatabaseManager databaseManager = new DatabaseManager();
    Connection connection;
    PreparedStatement preparedStatement;
    ResultSet resultSet;
    int Result;
    int selected;
    Alert at;

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
    private TableColumn<AdminRoom, Date> dateList;
    @FXML
    private TableColumn<AdminRoom, Float> amountList;
    @FXML
    private TableColumn<AdminRoom, String> statusList;

    private @FXML
    TableView tableView;

    private @FXML
    Region loadBar;

    private @FXML
    Label userName, userStatus, hotelName;
    private InvoiceList invoiceList;


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
    public ObservableList<InvoiceList> getInvoiceList() throws SQLException {
        ObservableList<InvoiceList> Invoices = FXCollections.observableArrayList();
        connection = databaseManager.connect();

        preparedStatement = connection.prepareStatement(
                "select cd.CID, cd.name, mb.departure ,mb.total_price, mb.status from CustomerDetail as cd , myBookings as mb where cd.CID =mb.CID and mb.status =? ");
        preparedStatement.setString(1,"CHECKED OUT");
        try {
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {

                invoiceList = new InvoiceList(
                        resultSet.getInt("CID"), resultSet.getString("name"),
                        resultSet.getDate("departure"), resultSet.getFloat("total_price"),
                        resultSet.getString("status")
                );

                Invoices.add(invoiceList);
            }
            resultSet.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return Invoices;
    }

    public void showRooms() throws SQLException {

        ObservableList<InvoiceList> invoiceList = getInvoiceList();

        IDlist.setCellValueFactory(new PropertyValueFactory<>("CID"));
        nameList.setCellValueFactory(new PropertyValueFactory<>("name"));
        dateList.setCellValueFactory(new PropertyValueFactory<>("date"));
        amountList.setCellValueFactory(new PropertyValueFactory<>("totalAmount"));
        statusList.setCellValueFactory(new PropertyValueFactory<>("status"));
        tableView.setItems(invoiceList);
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
            showRooms();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}


