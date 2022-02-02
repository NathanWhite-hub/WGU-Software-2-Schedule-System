package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import model.Authenticator;
import model.Queries;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;


public class AddAppointmentScreen implements Initializable {

    @FXML
    private Button btnSave;
    @FXML
    private Button btnCancel;
    @FXML
    private TextField appointmentIDField;
    @FXML
    private TextField appointmentTitleField;
    @FXML
    private TextField appointmentDescField;
    @FXML
    private TextField appointmentLocationField;
    @FXML
    private TextField appointmentTypeField;
    @FXML
    private ComboBox appointmentContactCombo;
    @FXML
    private ComboBox appointmentCustomerCombo;
    @FXML
    private DatePicker appointmentDatePicker;
    @FXML
    private ComboBox appointmentStartCombo;
    @FXML
    private ComboBox appointmentEndCombo;

    @FXML
    private Label addAppointmentLabel;
    @FXML
    private Label customerLabel;
    @FXML
    private Label titleLabel;
    @FXML
    private Label descriptionLabel;
    @FXML
    private Label locationLabel;
    @FXML
    private Label typeLabel;
    @FXML
    private Label dateLabel;
    @FXML
    private Label startLabel;
    @FXML
    private Label endLabel;

    private final DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("HH:mm");
    private final ObservableList<LocalTime> appointmentTimes = FXCollections.observableArrayList();

    Stage stage;
    Parent scene;


    /**
     * The resourcebundle is set to the locale languages location and the respective labels are set to the translation
     * provided in the proper .properties file. The variable time, which is an instance of the LocalTime class is set to
     * the time 08:00:00. Based on this time, a while loop is called which checks if time is equal to the time 17:30:00.
     * If it's not, it will add the value of time to the appointmentTimes observable list, then add thirty minutes to the variable
     * time. Once the while loop has finished, it will set customer, contact, start times, and end times comboboxes with
     * their respective values.
     * @param url
     * @param rb
     */
    public void initialize(URL url, ResourceBundle rb) {
        rb = ResourceBundle.getBundle("locale/locale");
        addAppointmentLabel.setText(rb.getString("addAppointmentLabel"));
        customerLabel.setText(rb.getString("customerLabel"));
        titleLabel.setText(rb.getString("appointmentTitleCol"));
        descriptionLabel.setText(rb.getString("appointmentDescCol"));
        locationLabel.setText(rb.getString("appointmentLocationCol"));
        typeLabel.setText(rb.getString("appointmentTypeCol"));
        dateLabel.setText(rb.getString("dateLabel"));
        startLabel.setText(rb.getString("appointmentStartCol"));
        endLabel.setText(rb.getString("appointmentEndCol"));
        btnSave.setText(rb.getString("btnSave"));
        btnCancel.setText(rb.getString("btnCancel"));

        appointmentIDField.setEditable(false);
        try {
            LocalTime time = LocalTime.of(8, 0, 0);
            while(!time.equals(LocalTime.of(17,30, 0))){
                appointmentTimes.add(time);
                time = time.plusMinutes(30);
            }
            appointmentCustomerCombo.setItems(Queries.getCustomerNames());
            appointmentContactCombo.setItems(Queries.getContactNames());
            appointmentStartCombo.setItems(appointmentTimes);
            appointmentEndCombo.setItems(appointmentTimes);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        /**
         * This lambda expression is used to set saturday and sunday as disabled in the appointmentDatePicker.
         */
        appointmentDatePicker.setDayCellFactory(datePicker -> new DateCell() {
            @Override
            public void updateItem(LocalDate datePicker, boolean empty) {
                super.updateItem(datePicker, empty);
                setDisable(empty || datePicker.getDayOfWeek() == DayOfWeek.SATURDAY || datePicker.getDayOfWeek() == DayOfWeek.SUNDAY );
            }
        });
    }

    /**
     * This method is called to check if the user selected start time is after the end time. If it's not, it will return true,
     * essentially passing the check. If it is, it will return false.
     * @param startTime
     * @param endTime
     * @return
     * @throws IOException
     */
    public boolean timeValidator(LocalDateTime startTime, LocalDateTime endTime) throws IOException{
        try{
            if(startTime.isAfter(endTime)){
                return true;
            }
        } catch (Exception e){
            System.out.print(e);
        }
        return false;
    }

    /**
     * This method is called to validate that the user selected times are within the specified business hours in EST. If they are,
     * it will return true, otherwise it will "pass" and return false.
     * @param zonedStartTime
     * @param zonedEndTime
     * @param appointmentDate
     * @return
     * @throws IOException
     */
    public boolean businessHoursValidator(ZonedDateTime zonedStartTime, ZonedDateTime zonedEndTime, LocalDate appointmentDate) throws IOException{
        try{
            LocalDateTime startTime = zonedStartTime.toLocalDateTime();
            LocalDateTime endTime = zonedEndTime.toLocalDateTime();

            LocalDateTime businessStartTime = ZonedDateTime.of(appointmentDate, LocalTime.of(8,0),
                    ZoneId.of("America/New_York")).toLocalDateTime();
            LocalDateTime businessEndTime = ZonedDateTime.of(appointmentDate, LocalTime.of(22, 0),
                    ZoneId.of("America/New_York")).toLocalDateTime();
            if(startTime.isBefore(businessStartTime) || startTime.isAfter(businessEndTime)){
                return true;
            } else if(endTime.isBefore(businessStartTime) || endTime.isAfter(businessEndTime)) {
                return true;
            }
            return false;
        } catch (Exception e){
            System.out.print(e);
        }
        return false;
    }

    /**
     * This method is called when the save button is clicked. Variables are set to their respective JavaFX counterpart's
     * values. Both the timeValidator and businessHoursValidator are called to check the user inputed times. If either returns
     * true, an error will be displayed to the user and the saving process will be stopped.
     *
     * If both return false, the user inputed times are then converted to a String and the Queries.saveNewAppointment
     * method is called. If it returned true, it means the appointment saved properly, and it will return the user to the main menu.
     * If it returned false, it means the appointment didn't save and an error occurred. It will display and alert to the user
     * to make them aware of this.
     * @param event
     * @throws IOException
     */
    public void saveAppointmentHandler(MouseEvent event) throws IOException{
        checkFields: try {
            DateTimeFormatter dateTimeFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

            String title = appointmentTitleField.getText();
            String desc = appointmentDescField.getText();
            String location = appointmentLocationField.getText();
            String type = appointmentTypeField.getText();
            String contact = (String) appointmentContactCombo.getValue();
            String customer = (String) appointmentCustomerCombo.getValue();
            LocalDate appointmentDate = appointmentDatePicker.getValue();

            LocalDateTime startTime = LocalDateTime.of(appointmentDate,
                    LocalTime.parse(appointmentStartCombo.getValue().toString(), timeFormat));

            LocalDateTime endTime = LocalDateTime.of(appointmentDate,
                    LocalTime.parse(appointmentEndCombo.getValue().toString(), timeFormat));

            String creatingUser = Authenticator.getUserName(LoginScreen.loggedInUser);

            if(timeValidator(startTime, endTime)){
                Alert errorAlert = new Alert(Alert.AlertType.ERROR, "The appointment start time must be before appointment" +
                        " end time.", ButtonType.OK);
                errorAlert.showAndWait();
                break checkFields;
            }

            ZonedDateTime zonedStart = ZonedDateTime.of(startTime, LoginScreen.timeZone);
            ZonedDateTime zonedEnd = ZonedDateTime.of(endTime, LoginScreen.timeZone);

            if(businessHoursValidator(zonedStart, zonedEnd, appointmentDate)){
                Alert errorAlert = new Alert(Alert.AlertType.ERROR, "The appointment times are outside of " +
                        " business hours (8AM to 10PM EST).", ButtonType.OK);
                errorAlert.showAndWait();
                break checkFields;
            }

            zonedStart = zonedStart.withZoneSameInstant(ZoneOffset.UTC);
            zonedEnd = zonedEnd.withZoneSameInstant(ZoneOffset.UTC);

            String convertedStartTime = zonedStart.format(dateTimeFormat);
            String convertedEndTime = zonedEnd.format(dateTimeFormat);

            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();

            if(Queries.saveNewAppointment(title, desc, location, type, contact, customer, convertedStartTime,
                    convertedEndTime, creatingUser, appointmentDate, zonedStart, zonedEnd)){
                loadMainMenu(stage);
            }
        }
        catch (Exception e){
            System.out.print(e);
            Alert errorAlert = new Alert(Alert.AlertType.ERROR, "Invalid Information in Fields.", ButtonType.OK);
            errorAlert.showAndWait();
        }
    }

    /**
     * This method is called when the cancel button is selected. It will return the user to the main menu.
     * @param event
     * @throws IOException
     */
    public void btnCancelHandler(MouseEvent event) throws IOException{
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
