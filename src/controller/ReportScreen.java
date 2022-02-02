package controller;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;

import javafx.scene.input.MouseEvent;
import model.Appointment;
import model.Queries;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ReportScreen implements Initializable {

    @FXML
    private TextArea textAreaReport;
    @FXML
    private Button btnExit;

    Stage stage;
    Parent scene;

    /**
     * This report screen is initialized and the exit button is set to the proper locale value. A series of if statements
     * are called to determine which report to generate. The boolean values are set to false or true from the MainScreenController
     * prior to loading the report screen.
     * @param url
     * @param resourceBundle
     */
    public void initialize(URL url, ResourceBundle resourceBundle) {
        resourceBundle = ResourceBundle.getBundle("locale/locale");
        textAreaReport.setText("");
        btnExit.setText(resourceBundle.getString("btnExit"));

        /**
         * This section of the code executes when a user is generating an appointment report. An observablelist called
         * appointmentList is initialized and then set to the returned value of Queries.getAppointmentsByTypeAndMonth().
         * The appointmentList is then looped through for each value and the text area then has each value appended.
         */
        if (MainScreenController.generateAppointmentReport == true) {
            ObservableList<String> appointmentList = null;
            try {
                appointmentList = Queries.getAppointmentsByTypeAndMonth();
                appointmentList.forEach((appointment -> {
                    textAreaReport.appendText(appointment);
                }));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        /**
         * This section of the code executes when a user is generating a contact schedule. An observablelist called
         * contactList is initialized and then set to the returned value of Queries.getAllAppointments().
         * The contactList is then looped through for each "contact" in the list. An integer contactID is set to the result
         * of the current contact in the list that checked against the Queries.getContactID() method. If the resulting
         * contactID is equal to the appointment contact ID, then the appointment ID, title, type, description, start date,
         * end date, and attached customer ID are appended to the text area.
         */
        } else if (MainScreenController.generateContactScheduleReport == true) {
            ObservableList<String> contactList = null;
            try {
                contactList = Queries.getContactNames();
                ObservableList<Appointment> appointmentList = Queries.getAllAppointments();

                contactList.forEach((contact -> {
                    textAreaReport.appendText("Contact: " + contact.toString() + "\n\n");
                    try {
                        int contactID = Queries.getContactID(contact);
                        appointmentList.forEach((appointment -> {
                            if(contactID == appointment.getAppointmentContactID()) {
                                textAreaReport.appendText(
                                        "Appointment ID: " + appointment.getAppointmentID() + "\n" +
                                                "Appointment Title: " + appointment.getAppointmentTitle() + "\n" +
                                                "Appointment Type: " + appointment.getAppointmentType() + "\n" +
                                                "Appointment Desc: " + appointment.getAppointmentDesc() + "\n" +
                                                "Appointment Start: " + appointment.getAppointmentStart() + "\n" +
                                                "Appointment End: " + appointment.getAppointmentEnd() + "\n" +
                                                "Customer ID: " + appointment.getAppointmentCustomerID() + "\n\n");
                            }
                        }));
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }));
            } catch (SQLException e) {
                e.printStackTrace();
            }
            /**
             * This section of the code executes when a user is generating a statistical report. The text area is appended
             * with the size of the returned observablelists for the methods Queries.getAllAppointments(), Queries.getAllCustomers(),
             * and Queries.getContactNames().
             */
        } else if (MainScreenController.generateStatisticsReport == true) {
            try {
                textAreaReport.setText(
                        "Total Number of Overall Appointments: " + Queries.getAllAppointments().size() + "\n" +
                        "Total Number of Customers: " + Queries.getAllCustomers().size() + "\n" +
                        "Total Number of Contacts: " + Queries.getContactNames().size() + "\n");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * This method is called when the exit button is selected. It will return the user to the main menu.
     * @param event
     * @throws IOException
     */
    public void btnExitHandler(MouseEvent event) throws IOException{
        stage = (Stage)((Button) event.getSource()).getScene().getWindow();
        loadMainMenu(stage);
    }

    /**
     * This method is called to load the main menu screen.
     * @param stage
     * @throws IOException
     */
    public void loadMainMenu(Stage stage) throws IOException{
        scene = FXMLLoader.load(getClass().getResource("/view/MainMenuScreen.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }
}
