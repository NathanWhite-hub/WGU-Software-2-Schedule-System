package controller;

import model.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;

import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class MainScreenController implements Initializable {
    @FXML
    private Label userNameLabel;
    @FXML
    private Label welcomeLabel;
    @FXML
    private Label customerLabel;
    @FXML
    private Label appointmentLabel;

    @FXML
    private AnchorPane MainScreen;
    @FXML
    private Button btnAppointmentReport;
    @FXML
    private Button btnContactSchedule;
    @FXML
    private Button btnStatisticsReport;

    @FXML
    private Button btnDeleteCust;
    @FXML
    private Button btnEditCust;
    @FXML
    private Button btnAddCust;

    @FXML
    private RadioButton btnRadioViewMonth;
    @FXML
    private RadioButton btnRadioViewWeek;
    @FXML
    private Button btnDeleteAppt;
    @FXML
    private Button btnEditAppt;
    @FXML
    private Button btnAddAppt;
    @FXML
    private Button btnMainExit;

    @FXML
    private TextField customerSearchField;

    @FXML
    private TableView<Customer> CustomerTableView;
    @FXML
    private TableColumn<Customer, String> customerNameCol;
    @FXML
    private TableColumn<Customer, String> customerAddressCol;
    @FXML
    private TableColumn<Customer, String> customerPostalCol;
    @FXML
    private TableColumn<Customer, String> customerPhoneCol;
    @FXML
    private TableColumn<Customer, Integer> customerDivisionCol;
    @FXML
    private TableColumn<Customer, Integer> customerCountryCol;

    @FXML
    private TableView<Appointment> AppointmentTableView;
    @FXML
    private TableColumn<Appointment, Integer> appointmentIDCol;
    @FXML
    private TableColumn<Appointment, String> appointmentTitleCol;
    @FXML
    private TableColumn<Appointment, String> appointmentDescCol;
    @FXML
    private TableColumn<Appointment, String> appointmentLocationCol;
    @FXML
    private TableColumn<Appointment, Integer> appointmentContactCol;
    @FXML
    private TableColumn<Appointment, String> appointmentTypeCol;
    @FXML
    private TableColumn<Appointment, String> appointmentStartCol;
    @FXML
    private TableColumn<Appointment, String> appointmentEndCol;
    @FXML
    private TableColumn<Appointment, Integer> appointmentCustomerIDCol;
    @FXML
    private TableColumn<Appointment, Integer> appointmentUserIDCol;

    Stage stage;
    Parent scene;

    protected static Customer selectedCustomer;
    protected static Appointment selectedAppointment;

    public static boolean queryAppointmentByMonth;
    public static boolean generateAppointmentReport = false;
    public static boolean generateContactScheduleReport = false;
    public static boolean generateStatisticsReport = false;

    public static int numOfMonthDays = 30;
    public static int numOfWeekDays = 7;

    User user;

    /**
     * The main menu is initialized and the resourcebundle is set to the local language location. The respective labels are
     * then set according to the proper variables in the respective language properties file. The boolean variable
     * queryAppointmentByMonth is set to true and each tables respective set method is called which populates the table with
     * data from the database.
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        resourceBundle = ResourceBundle.getBundle("locale/locale");
        try {
            btnAppointmentReport.setText(resourceBundle.getString("btnAppointmentReport"));
            btnContactSchedule.setText(resourceBundle.getString("btnContactSchedule"));
            btnStatisticsReport.setText(resourceBundle.getString("btnStatisticsReport"));

            welcomeLabel.setText(resourceBundle.getString("welcomeLabel"));
            userNameLabel.setText(resourceBundle.getString("userNameLabel"));
            customerLabel.setText(resourceBundle.getString("customerLabel"));
            appointmentLabel.setText(resourceBundle.getString("appointmentLabel"));
            btnAddCust.setText(resourceBundle.getString("btnAdd"));
            btnEditCust.setText(resourceBundle.getString("btnEdit"));
            btnDeleteCust.setText(resourceBundle.getString("btnDelete"));
            btnAddAppt.setText(resourceBundle.getString("btnAdd"));
            btnEditAppt.setText(resourceBundle.getString("btnEdit"));
            btnDeleteAppt.setText(resourceBundle.getString("btnDelete"));
            btnRadioViewMonth.setText(resourceBundle.getString("btnRadioViewMonth"));
            btnRadioViewWeek.setText(resourceBundle.getString("btnRadioViewWeek"));

            customerNameCol.setText(resourceBundle.getString("customerNameCol"));
            customerAddressCol.setText(resourceBundle.getString("customerAddressCol"));
            customerPostalCol.setText(resourceBundle.getString("customerPostalCol"));
            customerPhoneCol.setText(resourceBundle.getString("customerPhoneCol"));
            customerDivisionCol.setText(resourceBundle.getString("customerDivisionCol"));
            customerCountryCol.setText(resourceBundle.getString("customerCountryCol"));

            appointmentIDCol.setText(resourceBundle.getString("appointmentIDCol"));
            appointmentTitleCol.setText(resourceBundle.getString("appointmentTitleCol"));
            appointmentDescCol.setText(resourceBundle.getString("appointmentDescCol"));
            appointmentLocationCol.setText(resourceBundle.getString("appointmentLocationCol"));
            appointmentContactCol.setText(resourceBundle.getString("appointmentContactCol"));
            appointmentTypeCol.setText(resourceBundle.getString("appointmentTypeCol"));
            appointmentStartCol.setText(resourceBundle.getString("appointmentStartCol"));
            appointmentEndCol.setText(resourceBundle.getString("appointmentEndCol"));
            appointmentCustomerIDCol.setText(resourceBundle.getString("appointmentCustomerIDCol"));
            appointmentUserIDCol.setText(resourceBundle.getString("appointmentUserIDCol"));

            btnMainExit.setText(resourceBundle.getString("btnExit"));

            userNameLabel.setText(Authenticator.getUserName(LoginScreen.loggedInUser));
            queryAppointmentByMonth = true;
            setCustomerTable(CustomerTableView, customerNameCol, customerAddressCol, customerPostalCol, customerPhoneCol, customerDivisionCol, customerCountryCol);
            setAppointmentTable(AppointmentTableView, appointmentIDCol, appointmentTitleCol,
                    appointmentDescCol, appointmentLocationCol, appointmentContactCol, appointmentTypeCol,
                    appointmentStartCol, appointmentEndCol, appointmentCustomerIDCol, appointmentUserIDCol, queryAppointmentByMonth);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method sets the customer table with data returned from the Queries.getAllCustomers() method (see Queries.getAllCustomers()).
     * @param CustomerTableView
     * @param customerNameCol
     * @param customerAddressCol
     * @param customerPostalCol
     * @param customerPhoneCol
     * @param customerDivisionCol
     * @param customerCountryCol
     * @throws SQLException
     */
    public static void setCustomerTable(TableView<Customer> CustomerTableView, TableColumn<Customer, String> customerNameCol, TableColumn<Customer, String> customerAddressCol,
                                        TableColumn<Customer, String> customerPostalCol, TableColumn<Customer, String> customerPhoneCol, TableColumn<Customer, Integer> customerDivisionCol,
            TableColumn<Customer, Integer> customerCountryCol) throws SQLException {
        CustomerTableView.refresh();
        CustomerTableView.setItems(Queries.getAllCustomers());
        customerNameCol.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        customerAddressCol.setCellValueFactory(new PropertyValueFactory<>("customerAddress"));
        customerPostalCol.setCellValueFactory(new PropertyValueFactory<>("customerPostal"));
        customerPhoneCol.setCellValueFactory(new PropertyValueFactory<>("customerPhone"));
        customerDivisionCol.setCellValueFactory(new PropertyValueFactory<>("customerDivision"));
        customerCountryCol.setCellValueFactory(new PropertyValueFactory<>("customerCountry"));

    }

    /**
     * This method sets the appointment table, however, an if statement is called to check the boolean argument queryAppointmentByMonth.
     * If true, the appointment table is set with appointment data within the month. If false, it means that the user wishes
     * to see appointments by week, which it then sets with data by week.
     * @param AppointmentTableView
     * @param appointmentIDCol
     * @param appointmentTitleCol
     * @param appointmentDescCol
     * @param appointmentLocationCol
     * @param appointmentContactCol
     * @param appointmentTypeCol
     * @param appointmentStartCol
     * @param appointmentEndCol
     * @param appointmentCustomerIDCol
     * @param appointmentUserIDCol
     * @param queryAppointmentByMonth
     * @throws SQLException
     */
    public static void setAppointmentTable(TableView<Appointment> AppointmentTableView, TableColumn<Appointment, Integer> appointmentIDCol,
                                           TableColumn<Appointment, String> appointmentTitleCol,
                                           TableColumn<Appointment, String> appointmentDescCol,
                                           TableColumn<Appointment, String> appointmentLocationCol,
                                           TableColumn<Appointment, Integer> appointmentContactCol,
                                           TableColumn<Appointment, String> appointmentTypeCol,
                                           TableColumn<Appointment, String> appointmentStartCol,
                                           TableColumn<Appointment, String> appointmentEndCol,
                                           TableColumn<Appointment, Integer> appointmentCustomerIDCol,
                                           TableColumn<Appointment, Integer> appointmentUserIDCol, boolean queryAppointmentByMonth) throws SQLException {

        AppointmentTableView.refresh();
        if(queryAppointmentByMonth == true){
            AppointmentTableView.setItems(Queries.getAppointmentsByRange(numOfMonthDays));
        } else {
            AppointmentTableView.setItems(Queries.getAppointmentsByRange(numOfWeekDays));
        }
        appointmentIDCol.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));
        appointmentTitleCol.setCellValueFactory(new PropertyValueFactory<>("appointmentTitle"));
        appointmentDescCol.setCellValueFactory(new PropertyValueFactory<>("appointmentDesc"));
        appointmentLocationCol.setCellValueFactory(new PropertyValueFactory<>("appointmentLocation"));
        appointmentContactCol.setCellValueFactory(new PropertyValueFactory<>("appointmentContactID"));
        appointmentTypeCol.setCellValueFactory(new PropertyValueFactory<>("appointmentType"));
        appointmentStartCol.setCellValueFactory(new PropertyValueFactory<>("appointmentStart"));
        appointmentEndCol.setCellValueFactory(new PropertyValueFactory<>("appointmentEnd"));
        appointmentCustomerIDCol.setCellValueFactory(new PropertyValueFactory<>("appointmentCustomerID"));
        appointmentUserIDCol.setCellValueFactory(new PropertyValueFactory<>("appointmentUserID"));
    }

    /**
     * This method is called when the radio button to view appointments by month is clicked. It sets the boolean variable
     * queryAppointmentByMonth to true, sets the radio button to view by month to selected, and the radio button to view by
     * week to not selected. The setAppointmentTable method is then called.
     * @throws SQLException
     */
    public void btnRadioViewMonthHandler() throws SQLException {
        queryAppointmentByMonth = true;
        btnRadioViewMonth.setSelected(true);
        btnRadioViewWeek.setSelected(false);
        setAppointmentTable(AppointmentTableView, appointmentIDCol, appointmentTitleCol,
                appointmentDescCol, appointmentLocationCol, appointmentContactCol, appointmentTypeCol,
                appointmentStartCol, appointmentEndCol, appointmentCustomerIDCol, appointmentUserIDCol, queryAppointmentByMonth);
    }

    /**
     * This method is identical to the btnRadioViewMonthHandler() method, however, it is called when the radio button
     * view by week is clicked. It then does the opposite of the btnRadioViewMonthHandler() method and calls the setAppointmentTable method.
     * @throws SQLException
     */
    public void btnRadioViewWeekHandler() throws SQLException {
        queryAppointmentByMonth = false;
        btnRadioViewMonth.setSelected(false);
        btnRadioViewWeek.setSelected(true);
        setAppointmentTable(AppointmentTableView, appointmentIDCol, appointmentTitleCol,
                appointmentDescCol, appointmentLocationCol, appointmentContactCol, appointmentTypeCol,
                appointmentStartCol, appointmentEndCol, appointmentCustomerIDCol, appointmentUserIDCol, queryAppointmentByMonth);
    }

    /**
     * This method is called when the add button under the customer table is clicked. It loads the add customer screen.
     * @param event
     * @throws Exception
     */
    public void addCustomerHandler(MouseEvent event) throws Exception {
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/AddCustomerScreen.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /**
     * This method is called when the edit button under the customer table is clicked. It sets the variable selectedCustomer,
     * which is an instance of the class Customer to the selected item on the table. It then loads the edit customer screen.
     * @param event
     * @throws Exception
     */
    public void editCustomerHandler(MouseEvent event) throws Exception {
        selectedCustomer = CustomerTableView.getSelectionModel().getSelectedItem();
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/EditCustomerScreen.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /**
     * This method is called when the add button under the appointment table is clicked. It loads the add appointment screen.
     * @param event
     * @throws Exception
     */
    public void addAppointmentHandler(MouseEvent event) throws Exception {
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/AddAppointmentScreen.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /**
     * This method is called when the edit button under the appointment table is clicked. It sets the variable selectedAppointment,
     * which is an instance of the class Appointment to the selected item on the table. It then loads the edit appointment screen.
     * @param event
     * @throws Exception
     */
    public void editAppointmentHandler(MouseEvent event) throws Exception {
        selectedAppointment = AppointmentTableView.getSelectionModel().getSelectedItem();
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/EditAppointmentScreen.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /**
     * This method is called when the button Generate Appointment Report is clicked. It sets the boolean generateAppointmentReport
     * to true and loads the report screen.
     * @param event
     * @throws Exception
     */
    public void generateAppointmentReportHandler(MouseEvent event) throws Exception {
        generateAppointmentReport = true;
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/ReportScreen.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /**
     * This method is called when the button Generate Contact Schedule is clicked. It sets the boolean generateContactScheduleReport
     * to true and loads the report screen.
     * @param event
     * @throws Exception
     */
    public void generateContactScheduleHandler(MouseEvent event) throws Exception {
        generateContactScheduleReport = true;
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/ReportScreen.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /**
     * This method is called when the button Generate Statistics Report is clicked. It sets the boolean generateStatisticsReport
     * to true and loads the report screen.
     * @param event
     * @throws Exception
     */
    public void generateStatisticsReportHandler(MouseEvent event) throws Exception {
        generateStatisticsReport = true;
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/ReportScreen.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /**
     * This method is called when the delete button under the customer table is clicked. It is sets the selectedCustomer variable
     * to the selected item from the customer table then prompts the user to confirm if they want to delete the specified customer.
     * If the user clicks yes, the method Queries.deleteCustomer is called and the selectedCustomer.getCustomerID() argument
     * is passed in. If the customer is deleted from the database, the setCustomerTable method is called. If not, it will display
     * and alert to the user that an error occurred.
     * @param event
     * @throws Exception
     */
    public void deleteCustomerHandler(MouseEvent event) throws Exception {

        selectedCustomer = CustomerTableView.getSelectionModel().getSelectedItem();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete " + selectedCustomer.getCustomerName() + "?", ButtonType.YES, ButtonType.NO);
        alert.showAndWait();

        if (alert.getResult() == ButtonType.YES) {
            if(Queries.deleteCustomer(selectedCustomer.getCustomerID())){
                setCustomerTable(CustomerTableView, customerNameCol, customerAddressCol, customerPostalCol, customerPhoneCol, customerDivisionCol, customerCountryCol);
                alert = new Alert(Alert.AlertType.INFORMATION, "Customer successfully deleted.", ButtonType.OK);
            } else {
                alert = new Alert(Alert.AlertType.ERROR, "Customer was not deleted because an error occurred.", ButtonType.OK);
            }
            alert.showAndWait();
        }
    }

    /**
     * This method is called when the delete button under the appointment table is clicked. It is sets the selectedAppointment variable
     * to the selected item from the appointment table then prompts the user to confirm if they want to delete the specified appointment.
     * If the user clicks yes, the method Queries.deleteAppointment is called and the selectedAppointment.getAppointmentID() argument
     * is passed in. If the appointment is deleted from the database, the setAppointmentTable method is called. If not, it will display
     * and alert to the user that an error occurred.
     * @param event
     * @throws Exception
     */
    public void deleteAppointmentHandler(MouseEvent event) throws Exception {

        selectedAppointment = AppointmentTableView.getSelectionModel().getSelectedItem();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete "
                + selectedAppointment.getAppointmentTitle() + "?", ButtonType.YES, ButtonType.NO);
        alert.showAndWait();

        if (alert.getResult() == ButtonType.YES) {
            if(Queries.deleteAppointment(selectedAppointment.getAppointmentID())){
                setAppointmentTable(AppointmentTableView, appointmentIDCol, appointmentTitleCol,
                        appointmentDescCol, appointmentLocationCol, appointmentContactCol, appointmentTypeCol,
                        appointmentStartCol, appointmentEndCol, appointmentCustomerIDCol, appointmentUserIDCol, queryAppointmentByMonth);
                alert = new Alert(Alert.AlertType.INFORMATION, "Appointment successfully deleted.", ButtonType.OK);
            } else {
                alert = new Alert(Alert.AlertType.ERROR, "The appointment was not deleted because an error occurred.", ButtonType.OK);
            }
            alert.showAndWait();
        }
    }

    /**
     * This method is called when the user clicks the exit button. The program then closes.
     * @param event
     * @throws IOException
     */
    public void btnExitHandler(MouseEvent event) throws IOException {
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        stage.close();
    }


}
