package main.java.adminside;

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
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import main.java.db.DatabaseManager;
import main.java.userside.ScreenDragable;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;

@SuppressWarnings("All")
public class Home implements Initializable {
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

    FXMLLoader fxmlLoader;

    private @FXML
    Circle onlineIndicator;
    static ScaleTransition scaleTransitionLoadBar;
    static TranslateTransition translateTransitionRootScene;
    static ScaleTransition scaleTransitionApplyUser;
    static FadeTransition fadeTransitionIndicator;
    private @FXML AnchorPane rootScene;

    private @FXML
    AnchorPane rootStageUser;
    private @FXML
    JFXButton Quit;
    private @FXML
    JFXButton Minimize;
    private @FXML
    JFXButton Expand;
    private @FXML
    JFXButton reservedRooms, ourGuests, services, invoices, settings, feedback, logout;
    private @FXML
    Button UpdateBtn, resetBtn, deleteBtn;
    private @FXML
    TextField TypeField, ClassField, AmenityField, PriceField, StatusField;
    @FXML
    private TableColumn<AdminRoom, String> RoomIDlist;
    @FXML
    private TableColumn<AdminRoom, String> Typelist;
    @FXML
    private TableColumn<AdminRoom, String> classlist;
    @FXML
    private TableColumn<AdminRoom, String> Amenitylist;
    @FXML
    private TableColumn<AdminRoom, Float> Pricelist;
    @FXML
    private TableColumn<AdminRoom, Integer> Floorlist;
    @FXML
    private TableColumn<AdminRoom, String> Statuslist;
    private @FXML
    TableView tableView;

    private @FXML
    Region loadBar;

    private @FXML
    Label userName, userStatus, hotelName;
    private AdminRoom adminRoom;


    @FXML
    private void onAction(ActionEvent actionEvent) throws SQLException, IOException {
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
        if (actionEvent.getSource().equals(Minimize)) {
            stage.setIconified(!stage.isIconified());
        }
        if (actionEvent.getSource().equals(Expand)) {
            String max = System.getProperty("os.name", "unknown").toLowerCase(Locale.ROOT);
            if (max.contains("win")) {
                stage.setMaximized(!stage.isMaximized());
            }
        }
        if (actionEvent.getSource().equals(deleteBtn)) {

            at = new Alert(Alert.AlertType.CONFIRMATION);
            at.setHeaderText("CONFIRM");
            at.setContentText("Are you sure you want to delete " + RoomIDlist.getCellData(selected));
            at.showAndWait();
            if (at.getResult().equals(ButtonType.OK)) {
                if (delete()) {
                    at = new Alert(Alert.AlertType.INFORMATION);
                    at.setHeaderText("Success");
                    at.setContentText("Succesfully deleted" + RoomIDlist.getCellData(selected));
                    showRooms();
                    clear();

                } else {
                    at = new Alert(Alert.AlertType.ERROR);
                    at.setHeaderText("Failure");
                    at.setContentText("Something went wrong while deleting" + RoomIDlist.getCellData(selected));
                }
                at.showAndWait();
            }
        }
        if (actionEvent.getSource().equals(UpdateBtn)) {

            at = new Alert(Alert.AlertType.CONFIRMATION);
            at.setHeaderText("CONFIRM");
            at.setContentText("Are you sure you want to update " + RoomIDlist.getCellData(selected));
            at.showAndWait();
            if (at.getResult().equals(ButtonType.OK)) {
                if (update()) {
                    at = new Alert(Alert.AlertType.INFORMATION);
                    at.setHeaderText("Success");
                    at.setContentText("Succesfully updated" + RoomIDlist.getCellData(selected));
                    showRooms();
                    clear();

                } else {
                    at = new Alert(Alert.AlertType.ERROR);
                    at.setHeaderText("Failure");
                    at.setContentText("Something went wrong while updating" + RoomIDlist.getCellData(selected));
                }
                at.showAndWait();

            }

        }
        if (actionEvent.getSource().equals(reservedRooms)){
            root.setDisable(true);
            animateLoading("adminside/Reservedrooms.fxml",actionEvent);
        }
        if (actionEvent.getSource().equals(ourGuests)){
            root.setDisable(true);
            animateLoading("adminside/OurGuests.fxml",actionEvent);
        }
        if (actionEvent.getSource().equals(invoices)){
            root.setDisable(true);
            animateLoading("adminside/Invoices.fxml",actionEvent);
        }

        if (actionEvent.getSource().equals(logout)){
            purgeConnection();
            logOut();
        }

    }

    // To select from Table View

    @FXML
    private void select() {
        int index = tableView.getSelectionModel().getSelectedIndex();
        if (index <= -1) {
            return;

        }
        TypeField.setText(Typelist.getCellData(index));
        ClassField.setText(classlist.getCellData(index));
        AmenityField.setText(Amenitylist.getCellData(index));
        PriceField.setText(Pricelist.getCellData(index).toString());
        StatusField.setText(Statuslist.getCellData(index));
    }

    // To reset
    @FXML
    private void clear() throws SQLException {
        TypeField.setText(" ");
        ClassField.setText(" ");
        AmenityField.setText(" ");
        PriceField.setText(" ");
        StatusField.setText(" ");
    }

    //To delete
    private boolean delete() throws SQLException {

        selected = tableView.getSelectionModel().getSelectedIndex();
        connection = databaseManager.connect();
        preparedStatement = connection.prepareStatement(
                "DELETE FROM Room WHERE RID = ?");
                preparedStatement.setString(1, RoomIDlist.getCellData(selected));

        Result = preparedStatement.executeUpdate();
        return Result > 0;
    }

    private boolean update() throws SQLException {

        selected = tableView.getSelectionModel().getSelectedIndex();
        connection = databaseManager.connect();
        preparedStatement = connection.prepareStatement(
                "UPDATE Room SET type = ?, class = ? , amenity = ? , price =?,isBooked=? " +
                        "WHERE RID = ?");
        preparedStatement.setString(1,TypeField.getText());
        preparedStatement.setString(2,ClassField.getText());
        preparedStatement.setString(3,AmenityField.getText());
        preparedStatement.setFloat(4, Float.parseFloat(PriceField.getText()));
        preparedStatement.setString(5,StatusField.getText());
        preparedStatement.setString(6, RoomIDlist.getCellData(selected));
        Result = preparedStatement.executeUpdate();
        return Result > 0;
    }

    @FXML
    public ObservableList<AdminRoom> getAdminRoom() throws SQLException {
        ObservableList<AdminRoom> adminRooms = FXCollections.observableArrayList();
        connection = databaseManager.connect();

        preparedStatement = connection.prepareStatement(
                "select * from Room");
        try {
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {

                adminRoom = new AdminRoom(
                        resultSet.getString("RID"), resultSet.getString("type"),
                        resultSet.getString("class"), resultSet.getString("amenity"),
                        resultSet.getFloat("price"), resultSet.getInt("floor_level"),
                        resultSet.getString("isBooked")
                );

                adminRooms.add(adminRoom);
            }
            resultSet.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return adminRooms;
    }

    public void showRooms() throws SQLException {

        ObservableList<AdminRoom> bookingList = getAdminRoom();

        RoomIDlist.setCellValueFactory(new PropertyValueFactory<>("RID"));
        Typelist.setCellValueFactory(new PropertyValueFactory<>("Type"));
        classlist.setCellValueFactory(new PropertyValueFactory<>("Classlist"));
        Amenitylist.setCellValueFactory(new PropertyValueFactory<>("Amenity"));
        Pricelist.setCellValueFactory(new PropertyValueFactory<>("Price"));
        Floorlist.setCellValueFactory(new PropertyValueFactory<>("Floor"));
        Statuslist.setCellValueFactory(new PropertyValueFactory<>("Status"));
        tableView.setItems(bookingList);
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

    private void switchToScene(String fxml, ActionEvent actionEvent) throws IOException{

        fxmlLoader = new FXMLLoader(getClass().getResource("/main/resource/"+fxml));
        root = fxmlLoader.load();
        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        scene = ((Node) actionEvent.getSource()).getScene();

        scene.setRoot(root);
        root.setDisable(false);
    }

    public void applyUser() {

        userName.setText("ADMIN");
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            showRooms();
            applyUser();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}


