package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import main.Main;
import model.Authenticator;
import model.Customer;
import model.Queries;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;


public class EditCustomerScreen implements Initializable {

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
    private Label editCustomerLabel;
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
     * This method is identical to the AddAppointmentScreen counterpart. See AddAppointmentScreen.
     * @param selectedCustomer
     * @throws SQLException
     */
    public void setData(Customer selectedCustomer) throws SQLException{
        customerIDField.setText(String.valueOf(selectedCustomer.getCustomerID()));
        customerNameField.setText(selectedCustomer.getCustomerName());
        customerAddressField.setText(selectedCustomer.getCustomerAddress());
        customerPostalField.setText(selectedCustomer.getCustomerPostal());
        customerPhoneField.setText(selectedCustomer.getCustomerPhone());
        customerCountryCombo.getSelectionModel().select(selectedCustomer.getCustomerCountry());
        customerCityCombo.getSelectionModel().select(selectedCustomer.getCustomerDivision());
    }

    /**
     * This method is identical to the AddCustomerScreen counterpart, however, the method setData is called with
     * MainScreenController.selectedCustomer being passed as an argument. This is the customer instance that was selected
     * on the main screen.
     * @param url
     * @param rb
     */
    public void initialize(URL url, ResourceBundle rb) {
        rb = ResourceBundle.getBundle("locale/locale");
        editCustomerLabel.setText(rb.getString("editCustomerLabel"));
        nameLabel.setText(rb.getString("customerNameCol"));
        addressLabel.setText(rb.getString("customerAddressCol"));
        postalCodeLabel.setText(rb.getString("customerPostalCol"));
        phoneNumberLabel.setText(rb.getString("customerPhoneCol"));
        countryLabel.setText(rb.getString("customerCountryCol"));
        cityLabel.setText(rb.getString("cityLabel"));
        btnSave.setText(rb.getString("btnSave"));
        btnCancel.setText(rb.getString("btnCancel"));

        try {
            setData(MainScreenController.selectedCustomer);
            customerCountryCombo.setItems(Queries.getCountries());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        /**
         * This lambda expression is identical to the AddCustomerScreen counterpart. See AddCustomerScreen.
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
     * This method is identical to the AddCustomerScreen counterpart. See AddCustomerScreen.
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

            if(Queries.updateCustomer(name, address, postal, phoneNum, creatingUser, country, city, MainScreenController.selectedCustomer.getCustomerID())){
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
     * This method is identical to the AddCustomerScreen counterpart. See AddCustomerScreen.
     * @param event
     * @throws IOException
     */
    public void btnCancelHandler(MouseEvent event) throws IOException{
        stage = (Stage)((Button) event.getSource()).getScene().getWindow();
        loadMainMenu(stage);
    }

    /**
     * This method is identical to the AddCustomerScreen counterpart. See AddCustomerScreen.
     * @param stage
     * @throws IOException
     */
    public void loadMainMenu(Stage stage) throws IOException{
        scene = FXMLLoader.load(getClass().getResource("/view/MainMenuScreen.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }
}
