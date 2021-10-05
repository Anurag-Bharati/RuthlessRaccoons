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

/**
 * <h2>GUEST CONTROLLER</h2>
 */

@SuppressWarnings("All")
public class Guest implements Initializable {
    protected Stage stage;
    protected Scene scene;
    protected Parent root;
    DatabaseManager databaseManager = new DatabaseManager();
    Connection connection;
    PreparedStatement preparedStatement;
    ResultSet resultSet;

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
    JFXButton dashboard, reservedRooms, services, invoices, settings, feedback, logout;


    @FXML
    private TableColumn<AdminRoom, Integer> IDlist;
    @FXML
    private TableColumn<AdminRoom, String> nameList;
    @FXML
    private TableColumn<AdminRoom, String> genderList;
    @FXML
    private TableColumn<AdminRoom, String> phonenoList;
    @FXML
    private TableColumn<AdminRoom, String> gmailList;
    private @FXML
    TableView tableView;

    private @FXML
    Region loadBar;

    private @FXML
    Label userName, userStatus, hotelName;
    private GuestList guestList;


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
        if (actionEvent.getSource().equals(dashboard)){
            root.setDisable(true);
            animateLoading("adminside/adminside.fxml",actionEvent);
        }
        if (actionEvent.getSource().equals(reservedRooms)){
            root.setDisable(true);
            animateLoading("adminside/Reservedrooms.fxml",actionEvent);
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

    @FXML
    public ObservableList<GuestList> getGuestList() throws SQLException {
        ObservableList<GuestList> guests = FXCollections.observableArrayList();
        connection = databaseManager.connect();

        preparedStatement = connection.prepareStatement(
                "select * from CustomerDetail");
        try {
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {

                guestList = new GuestList(
                        resultSet.getInt("CID"), resultSet.getString("name"),
                        resultSet.getString("gender"), resultSet.getString("phone"),
                        resultSet.getString("gmail"));

                guests.add(guestList);
            }
            resultSet.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return guests;
    }

    public void showGuests() throws SQLException {

        ObservableList<GuestList> GuestTable = getGuestList();

        IDlist.setCellValueFactory(new PropertyValueFactory<>("guestID"));
        nameList.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        genderList.setCellValueFactory(new PropertyValueFactory<>("gender"));
        phonenoList.setCellValueFactory(new PropertyValueFactory<>("phoneNo"));
        gmailList.setCellValueFactory(new PropertyValueFactory<>("gmail"));
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
            showGuests();
            applyUser();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}

