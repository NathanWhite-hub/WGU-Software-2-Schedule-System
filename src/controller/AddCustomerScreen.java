package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;
import model.Authenticator;
import model.Queries;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;


public class AddCustomerScreen implements Initializable {

    @FXML
    private Button btnSave;
    @FXML
    private Button btnCancel;
    @FXML
    private TextField customerIDField;
    @FXML
    private TextField customerNameField;
    @FXML
    private TextField customerAddressField;
    @FXML
    private TextField customerPostalField;
    @FXML
    private TextField customerPhoneField;
    @FXML
    private ComboBox customerCountryCombo;
    @FXML
    private ComboBox customerCityCombo;

    @FXML
    private Label addCustomerLabel;
    @FXML
    private Label nameLabel;
    @FXML
    private Label addressLabel;
    @FXML
    private Label postalCodeLabel;
    @FXML
    private Label phoneNumberLabel;
    @FXML
    private Label countryLabel;
    @FXML
    private Label cityLabel;

    Stage stage;
    Parent scene;

    /**
     * The resourcebundle is set to the locale languages location and the respective labels are set to the translation
     * provided in the proper .properties file. The country combobox is then set with the countries returned from the method
     * Queries.getCountries().
     * @param url
     * @param rb
     */
    public void initialize(URL url, ResourceBundle rb) {
        rb = ResourceBundle.getBundle("locale/locale");
        addCustomerLabel.setText(rb.getString("addCustomerLabel"));
        nameLabel.setText(rb.getString("customerNameCol"));
        addressLabel.setText(rb.getString("customerAddressCol"));
        postalCodeLabel.setText(rb.getString("customerPostalCol"));
        phoneNumberLabel.setText(rb.getString("customerPhoneCol"));
        countryLabel.setText(rb.getString("customerCountryCol"));
        cityLabel.setText(rb.getString("cityLabel"));
        btnSave.setText(rb.getString("btnSave"));
        btnCancel.setText(rb.getString("btnCancel"));

        customerIDField.setEditable(false);
        try {
            customerCountryCombo.setItems(Queries.getCountries());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        /**
         * This lambda expression is used to check if the customerCountryCombo value changes. If the value is null, it will clear the items
         * from the customerCityCombo and disable it. If not, it will enable the customerCityCombo and set the selectable items
         * to the returned data of the method Queries.getDivisions.
         */
        customerCountryCombo.valueProperty().addListener(((observableValue, oldValue, newValue) -> {
            if(newValue == null){
                customerCityCombo.getItems().clear();
                customerCityCombo.setDisable(true);
            } else {
                customerCityCombo.setDisable(false);
                try {
                    customerCityCombo.setItems(Queries.getDivisions(customerCountryCombo.getValue().toString()));
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }));
    }

    /**
     * This method is called when the save button is clicked. Variables are set to their respective JavaFX counterpart's
     * values. The creatingUser variable is set to the logged-in user that was stored in the LoginScreen class. The
     * Queries.saveNewCustomer method is then called with the above-mentioned variables passed as arguments. If it returns true,
     * it means the customer saved successfully and then loads the main menu screen. If false, it means the customer didn't
     * save successfully and an error occurred. It displays an alert to the user that this is the case.
     * @param event
     * @throws IOException
     */
    public void saveCustomerHandler(MouseEvent event) throws IOException{
        checkFields: try {
            String name = customerNameField.getText();
            String address = customerAddressField.getText();
            String postal = customerPostalField.getText();
            String phoneNum = customerPhoneField.getText();
            String country = (String) customerCountryCombo.getValue();
            String city = (String) customerCityCombo.getValue();
            String creatingUser = Authenticator.getUserName(LoginScreen.loggedInUser);

            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();

            if(Queries.saveNewCustomer(name, address, postal, phoneNum, creatingUser, country, city)){
                loadMainMenu(stage);
            } else {
                Alert errorAlert = new Alert(Alert.AlertType.ERROR, "An error occurred.", ButtonType.OK);
                errorAlert.showAndWait();
            }
        }
        catch (Exception e){
            Alert errorAlert = new Alert(Alert.AlertType.ERROR, "Invalid Information in Fields.", ButtonType.OK);
            errorAlert.showAndWait();
        }
    }

    /**
     * This method is called when the cancel button is clicked. It then loads the main menu.
     * @param event
     * @throws IOException
     */
    public void btnCancelHandler(MouseEvent event) throws IOException{
        stage = (Stage)((Button) event.getSource()).getScene().getWindow();
        loadMainMenu(stage);
    }

    /**
     * This method loads the main menu screen.
     * @param stage
     * @throws IOException
     */
    public void loadMainMenu(Stage stage) throws IOException{
        scene = FXMLLoader.load(getClass().getResource("/view/MainMenuScreen.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }
}
