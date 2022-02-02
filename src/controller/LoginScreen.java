package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.Authenticator;
import model.User;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class LoginScreen implements Initializable {
    public static int loggedInUser;
    @FXML
    private Text welcomeLabel;
    @FXML
    private Text welcomeSubtitleLabel;
    @FXML
    private Text loggingInSubtitleLabel;
    @FXML
    private Text locationLabel;
    @FXML
    private Text userNameLabel;
    @FXML
    private Text passwordLabel;
    @FXML
    private Button btnLogin;
    @FXML
    private TextField userNameField;
    @FXML
    private TextField passwordField;

    Stage stage;
    Parent scene;
    public static ZoneId timeZone;
    public static BufferedWriter logger;

    /**
     * The screen is initialized and the program has started. The resource bundle is set to the locales location, followed by
     * the timezone being set to the system default, and all labels being set to the locale of the user (English or French).
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        resourceBundle = ResourceBundle.getBundle("locale/locale");
        timeZone = ZoneId.systemDefault();
        welcomeLabel.setText(resourceBundle.getString("welcomeLabel"));
        welcomeSubtitleLabel.setText(resourceBundle.getString("welcomeSubtitleLabel"));
        loggingInSubtitleLabel.setText(resourceBundle.getString("loggingInSubtitleLabel"));
        locationLabel.setText(timeZone.toString());
        userNameLabel.setText(resourceBundle.getString("userNameLabel"));
        passwordLabel.setText(resourceBundle.getString("passwordLabel"));
        btnLogin.setText(resourceBundle.getString("btnLogin"));
    }

    /**
     * This is called when the login button is clicked. The username and password variables are set to the text of their
     * respective fields, followed by a log of the login attempt to the text file. An if statement is called to the
     * Authenticator.isValidUser() method with the userName and password variables passed as arguments. If true, this is
     * counted as successful login which authenticates the user, logging the success, and loads the main menu.
     *
     * If not true, the failure is logged and an alert is displayed to the user.
     * @param event
     * @throws Exception
     */
    public void loginHandler(MouseEvent event) throws Exception {
        File file = new File("login_activity.txt");

        LocalDateTime currentTime = LocalDateTime.parse(ZonedDateTime.now(
                        ZoneOffset.UTC).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));

        if (!file.exists()) {
            file.createNewFile();
        }

        FileWriter fw = new FileWriter(file);
        String userName = userNameField.getText();
        String password = passwordField.getText();
        logger = new BufferedWriter(fw);
        logger.write("LOGIN ATTEMPT [" + currentTime + "]: " + timeZone + " USER - " + userName + "\n");

        if(Authenticator.isValidUser(userName, password)){
            User user = new User(Authenticator.getUserID(userName), userName);
            logger.write("LOGIN SUCCESS [" + currentTime + "]: For user " + userName + "\n\n");
            logger.close();
            loggedInUser = Authenticator.getUserID(userName);
            stage = (Stage)((Button)event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getClassLoader().getResource("view/MainMenuScreen.fxml"));
            stage.setScene(new Scene(scene));
            stage.show();
        } else {
            logger.write("LOGIN FAILURE [" + currentTime + "]: For user " + userName + "\n\n");
            logger.close();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Invalid Credentials.", ButtonType.OK);
            alert.showAndWait();
        }

    }
}
