package ec.edu.espe.roomreservation.model;

import java.util.UUID;

public class RoomReservation {

    private String id;
    private String roomCode;
    private String reservedByEmail;
    private int hours;
    private String status;

    public RoomReservation() {
    }

    //Constructor que genera el ID automáticamente
    public RoomReservation(String roomCode, String reservedByEmail, int hours) {
        this.id = UUID.randomUUID().toString();
        this.roomCode = roomCode;
        this.reservedByEmail = reservedByEmail;
        this.hours = hours;
        this.status = "CREATED";
    }

    public RoomReservation(String id, String roomCode, String reservedByEmail, int hours, String status) {
        this.id = id;
        this.roomCode = roomCode;
        this.reservedByEmail = reservedByEmail;
        this.hours = hours;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRoomCode() {
        return roomCode;
    }

    public void setRoomCode(String roomCode) {
        this.roomCode = roomCode;
    }

    public String getReservedByEmail() {
        return reservedByEmail;
    }

    public void setReservedByEmail(String reservedByEmail) {
        this.reservedByEmail = reservedByEmail;
    }

    public int getHours() {
        return hours;
    }

    public void setHours(int hours) {
        this.hours = hours;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
