package com.example.graduation;

import java.time.LocalDate;

public class Reservation {
    private int id;
    private String room_id;
    private String room_name;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private LocalDate checkedIn;
    private LocalDate checkedOut;
    private String customerId;
    private String customer;

    public Reservation(int id, String room_id, String room_name, LocalDate checkInDate, LocalDate checkOutDate, LocalDate checkedIn, String customerId) {
        this.id = id;
        this.room_id = room_id;
        this.room_name = room_name;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.checkedIn = checkedIn;
        this.checkedOut = checkedOut;
        this.customerId = customerId;
        this.customer = customer;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRoom_id() {
        return room_id;
    }

    public void setRoom_id(String room_id) {
        this.room_id = room_id;
    }
    public String getRoom_name() {
        return room_name;
    }

    public void setRoom_name(String roomName) {
        this.room_name = roomName;
    }

    public LocalDate getCheckInDate() {
        return checkInDate;
    }

    public void setCheckInDate(LocalDate checkInDate) {
        this.checkInDate = checkInDate;
    }

    public LocalDate getCheckOutDate() {
        return checkOutDate;
    }

    public void setCheckOutDate(LocalDate checkOutDate) {
        this.checkOutDate = checkOutDate;
    }

    public LocalDate getCheckedIn() {
        return checkedIn;
    }

    public void setCheckedIn(LocalDate checkedIn) {
        this.checkedIn = checkedIn;
    }

    public LocalDate getCheckedOut() {
        return checkedOut;
    }

    public void setCheckedOut(LocalDate checkedOut) {
        this.checkedOut = checkedOut;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }
}
