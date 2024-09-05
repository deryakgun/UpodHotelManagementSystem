package com.example.graduation.controllers;
import com.example.graduation.DBUtils;
import com.example.graduation.ReservationServices;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SplitMenuButton;

import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class DeletedReservationService implements Initializable {
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
    private Button button_deletedrservice;
    @FXML
    private SplitMenuButton smb_deleterservice;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        button_logout.setOnAction(event -> DBUtils.changeScene(event, "sign-in.fxml", "Log in", null));
        button_rooms.setOnAction(event -> DBUtils.changeScene(event, "room.fxml", "Room", null));
        button_features.setOnAction(event -> DBUtils.changeScene(event, "feature.fxml", "Feature", null));
        button_customer.setOnAction(event -> DBUtils.changeScene(event, "customer.fxml", "Customer", null));
        button_service.setOnAction(event -> DBUtils.changeScene(event, "services.fxml", "Services", null));
        button_reservationservices.setOnAction(event -> DBUtils.changeScene(event, "reservation-services.fxml", "Reservation Services", null));

        smb_deleterservice.getItems().clear();
        List<ReservationServices> reservationServicesList = null;
        try {
            reservationServicesList = DBUtils.getReservationServicesFromDatabase();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        for (ReservationServices reservationServices : reservationServicesList) {
            MenuItem menuItem = new MenuItem(String.valueOf(reservationServices.getReservationId()));
            menuItem.setOnAction(event -> smb_deleterservice.setText(menuItem.getText()));
            smb_deleterservice.getItems().add(menuItem);
        }
        button_deletedrservice.setOnAction(event -> {
            String selectedReservationService = smb_deleterservice.getText();
            deleteReservationService(selectedReservationService);
        });

    }
    private boolean deleteReservationService(String selectedReservationService) {
        try {
            int rowsAffected = DBUtils.deleteReservationRService(selectedReservationService);
            if (rowsAffected >= 0) {
                rowsAffected = DBUtils.deleteReservationService(selectedReservationService);
                if (rowsAffected > 0) {
                    System.out.println("Reservation Service deleted successfully.");
                    DBUtils.showSuccessAlert("Success", "Reservation Service  Deleted", "Reservation Service  has been successfully deleted.");
                    return true;
                } else {
                    System.out.println("Failed to delete Reservation Service .");
                    DBUtils.showErrorAlert("Error", "Failed to Delete Reservation Service ", "Failed to delete the Reservation Service .");
                }
            } else {
                System.out.println("Failed to delete reservations.");
                DBUtils.showErrorAlert("Error", "Failed to Delete Reservations", "Failed to delete reservations of the Reservation Service .");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            DBUtils.showErrorAlert("Error", "Database Error", "An error occurred while accessing the database.");
        }
        return false;

    }







}