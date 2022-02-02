package model;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;

/**
 * This class is used to create Appointment objects, specifically when populating the appointment table on the main screen.
 * This is also used to set which appointment is selected on the main screen when editing an appointment.
 */
public class Appointment {
    private int appointmentID, appointmentCustomerID, appointmentUserID, appointmentContactID;
    private String appointmentTitle, appointmentDesc, appointmentLocation, appointmentType, appointmentStart, appointmentEnd;

    /**
     * This is a constructor for the Appointment class.
     * @param appointmentID
     * @param appointmentTitle
     * @param appointmentDesc
     * @param appointmentLocation
     * @param appointmentContactID
     * @param appointmentType
     * @param appointmentStart
     * @param appointmentEnd
     * @param appointmentCustomerID
     * @param appointmentUserID
     */
    public Appointment(int appointmentID, String appointmentTitle, String appointmentDesc,
                       String appointmentLocation, int appointmentContactID, String appointmentType, String appointmentStart,
                       String appointmentEnd, int appointmentCustomerID, int appointmentUserID){
        this.appointmentID = appointmentID;
        this.appointmentTitle = appointmentTitle;
        this.appointmentDesc = appointmentDesc;
        this.appointmentLocation = appointmentLocation;
        this.appointmentContactID = appointmentContactID;
        this.appointmentType = appointmentType;
        this.appointmentStart = appointmentStart;
        this.appointmentEnd = appointmentEnd;
        this.appointmentCustomerID = appointmentCustomerID;
        this.appointmentUserID = appointmentUserID;
    }

    /**
     * @return the appointmentID
     */
    public int getAppointmentID() {
        return appointmentID;
    }

    /**
     * @param appointmentID the id to set
     */
    public void setAppointmentID(int appointmentID) {
        this.appointmentID = appointmentID;
    }

    /**
     * @return the appointmentTitle
     */
    public String getAppointmentTitle() {
        return appointmentTitle;
    }

    /**
     * @param appointmentTitle the id to set
     */
    public void setAppointmentTitle(String appointmentTitle) {
        this.appointmentTitle = appointmentTitle;
    }

    /**
     * @return the appointmentDesc
     */
    public String getAppointmentDesc() {
        return appointmentDesc;
    }

    /**
     * @param appointmentDesc the id to set
     */
    public void setAppointmentDesc(String appointmentDesc) {
        this.appointmentDesc = appointmentDesc;
    }

    /**
     * @return the appointmentLocation
     */
    public String getAppointmentLocation() {
        return appointmentLocation;
    }

    /**
     * @param appointmentLocation the id to set
     */
    public void setAppointmentLocation(String appointmentLocation) {
        this.appointmentLocation = appointmentLocation;
    }

    /**
     * @return the appointmentContactID
     */
    public int getAppointmentContactID() {
        return appointmentContactID;
    }

    /**
     * @param appointmentContactID the id to set
     */
    public void setAppointmentContactID(int appointmentContactID) {
        this.appointmentContactID = appointmentContactID;
    }

    /**
     * @return the appointmentType
     */
    public String getAppointmentType() {
        return appointmentType;
    }

    /**
     * @param appointmentType the id to set
     */
    public void setAppointmentType(String appointmentType) {
        this.appointmentType = appointmentType;
    }

    /**
     * @return the appointmentStart
     */
    public String getAppointmentStart() {
        return appointmentStart;
    }

    /**
     * @param appointmentStart the id to set
     */
    public void setAppointmentStart(String appointmentStart) {
        this.appointmentStart = appointmentStart;
    }

    /**
     * @return the appointmentEnd
     */
    public String getAppointmentEnd() {
        return appointmentEnd;
    }

    /**
     * @param appointmentEnd the id to set
     */
    public void setAppointmentEnd(String appointmentEnd) {
        this.appointmentEnd = appointmentEnd;
    }

    /**
     * @return the appointmentCustomerID
     */
    public int getAppointmentCustomerID() {
        return appointmentCustomerID;
    }

    /**
     * @param appointmentCustomerID the id to set
     */
    public void setAppointmentCustomerID(int appointmentCustomerID) {
        this.appointmentCustomerID = appointmentCustomerID;
    }

    /**
     * @return the appointmentUserID
     */
    public int getAppointmentUserID() {
        return appointmentUserID;
    }

    /**
     * @param appointmentUserID the id to set
     */
    public void setAppointmentUserID(int appointmentUserID) {
        this.appointmentUserID = appointmentUserID;
    }

}
