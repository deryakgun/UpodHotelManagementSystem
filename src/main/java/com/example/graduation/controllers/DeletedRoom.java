package com.example.graduation.controllers;
import com.example.graduation.DBUtils;
import com.example.graduation.Room;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SplitMenuButton;

import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class DeletedRoom implements Initializable {
    @FXML
    private Button button_rooms;
    @FXML
    private Button button_features;
    @FXML
    private Button button_customer;
    @FXML
    private Button button_service;
    @FXML
    private Button button_reservationservices;
    @FXML
    private Button button_logout;
    @FXML
    private  Button button_main;
    @FXML
    private Button button_deletedroom;
    @FXML
    private SplitMenuButton smb_deleteroom;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        button_logout.setOnAction(event -> DBUtils.changeScene(event, "sign-in.fxml", "Log in", null));
        button_rooms.setOnAction(event -> DBUtils.changeScene(event, "room.fxml", "Room", null));
        button_features.setOnAction(event -> DBUtils.changeScene(event, "feature.fxml", "Feature", null));
        button_customer.setOnAction(event -> DBUtils.changeScene(event, "customer.fxml", "Customer", null));
        button_service.setOnAction(event -> DBUtils.changeScene(event, "services.fxml", "Services", null));
        button_reservationservices.setOnAction(event -> DBUtils.changeScene(event, "reservation-services.fxml", "Reservation Services", null));
        button_main.setOnAction(event -> DBUtils.changeScene(event, "logged-ın.fxml", "LoggedInController", null));

        smb_deleteroom.getItems().clear();
        List<Room> roomList = null;
        try {
            roomList = DBUtils.getRoomsFromDatabase();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        for (Room room : roomList) {
            MenuItem menuItem = new MenuItem(String.valueOf(room.getRoomName()));
            menuItem.setOnAction(event -> smb_deleteroom.setText(menuItem.getText()));
            smb_deleteroom.getItems().add(menuItem);
        }
        button_deletedroom.setOnAction(event -> {
            String selectedRoom = smb_deleteroom.getText();
            deleteRoom(selectedRoom);
        });

    }
    private boolean deleteRoom(String selectedRoom) {
        try {
            int rowsAffected = DBUtils.deleteReservationRoom(selectedRoom);
            if (rowsAffected >= 0) {
                rowsAffected = DBUtils.deleteRoom(selectedRoom);
                if (rowsAffected > 0) {
                    System.out.println("Room deleted successfully.");
                    DBUtils.showSuccessAlert("Success", "Room Deleted", "Room has been successfully deleted.");
                    return true;
                } else {
                    System.out.println("Failed to delete room.");
                    DBUtils.showErrorAlert("Error", "Failed to Delete Room", "Failed to delete the room.");
                }
            } else {
                System.out.println("Failed to delete reservations.");
                DBUtils.showErrorAlert("Error", "Failed to Delete Reservations", "Failed to delete reservations of the room.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            DBUtils.showErrorAlert("Error", "Database Error", "An error occurred while accessing the database.");
        }
        return false;

    }







}
