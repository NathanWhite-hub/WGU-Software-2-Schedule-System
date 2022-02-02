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
import model.Appointment;
import model.Authenticator;
import model.Queries;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;


public class EditAppointmentScreen implements Initializable {

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
    private Label editAppointmentLabel;
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
     * This method is called to set the data of the proper textfields and comboboxes with the data of the selectedAppointment from
     * the main menu screen. The startData, startTime, and endTime variables are set to the converted values of the data of the selectedAppointment variable.
     * @param selectedAppointment
     * @throws SQLException
     */
    public void setData(Appointment selectedAppointment) throws SQLException{
        LocalDateTime startDate = LocalDateTime.parse(selectedAppointment.getAppointmentStart(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        LocalTime startTime = LocalDateTime.parse(selectedAppointment.getAppointmentStart(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")).toLocalTime();
        LocalTime endTime = LocalDateTime.parse(selectedAppointment.getAppointmentEnd(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")).toLocalTime();

        appointmentIDField.setText(String.valueOf(selectedAppointment.getAppointmentID()));
        appointmentTitleField.setText(selectedAppointment.getAppointmentTitle());
        appointmentDescField.setText(selectedAppointment.getAppointmentDesc());
        appointmentLocationField.setText(selectedAppointment.getAppointmentLocation());
        appointmentTypeField.setText(selectedAppointment.getAppointmentType());
        appointmentContactCombo.getSelectionModel().select(Queries.getContactName(selectedAppointment.getAppointmentContactID()));
        appointmentCustomerCombo.getSelectionModel().select(Queries.getCustomerName(selectedAppointment.getAppointmentCustomerID()));
        appointmentStartCombo.getSelectionModel().select(startTime);
        appointmentEndCombo.getSelectionModel().select(endTime);
        appointmentDatePicker.setValue(startDate.toLocalDate());
    }

    /**
     * This method is identical to the AddAppointmentScreen counterpart, however the setData method is called with the
     * selectedAppointment instance, which is stored in the MainScreenController class, being passed as an argument. See the setData method.
     * @param url
     * @param rb
     */
    public void initialize(URL url, ResourceBundle rb) {
        rb = ResourceBundle.getBundle("locale/locale");
        editAppointmentLabel.setText(rb.getString("editAppointmentLabel"));
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

            setData(MainScreenController.selectedAppointment);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        /**
         * This lambda expression is identical to the AddAppointmentScreen counterpart. See AddAppointmentScreen.
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
     * This method is identical to the AddAppointmentScreen counterpart. See AddAppointmentScreen.
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
     * This method is identical to the AddAppointmentScreen counterpart. See AddAppointmentScreen.
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
     * This method is identical to the AddAppointmentScreen counterpart, however, instead of Queries.saveNewAppointment being called,
     * the method Queries.updateAppointment is called as the appointment is being updated.
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

            if(Queries.updateAppointment(title, desc, location, type, contact, customer, convertedStartTime,
                    convertedEndTime, creatingUser, appointmentDate, zonedStart, zonedEnd, MainScreenController.selectedAppointment.getAppointmentID())){
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
     * This method is identical to the AddAppointmentScreen counterpart. See AddAppointmentScreen.
     * @param event
     * @throws IOException
     */
    public void btnCancelHandler(MouseEvent event) throws IOException{
        stage = (Stage)((Button) event.getSource()).getScene().getWindow();
        loadMainMenu(stage);
    }

    /**
     * This method is identical to the AddAppointmentScreen counterpart. See AddAppointmentScreen.
     * @param stage
     * @throws IOException
     */
    public void loadMainMenu(Stage stage) throws IOException{
        scene = FXMLLoader.load(getClass().getResource("/view/MainMenuScreen.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }
}
