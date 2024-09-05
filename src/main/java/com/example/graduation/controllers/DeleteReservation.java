package com.example.graduation.controllers;

import com.example.graduation.DBUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SplitMenuButton;

import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class DeleteReservation implements Initializable {
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
        private Button button_deletedreservation;
        @FXML
        private SplitMenuButton smb_deletereservation;


        @Override
        public void initialize(URL url, ResourceBundle resourceBundle) {
                button_logout.setOnAction(event -> DBUtils.changeScene(event, "sign-in.fxml", "Log in", null));
                button_rooms.setOnAction(event -> DBUtils.changeScene(event, "room.fxml", "Room", null));
                button_features.setOnAction(event -> DBUtils.changeScene(event, "feature.fxml", "Feature", null));
                button_customer.setOnAction(event -> DBUtils.changeScene(event, "customer.fxml", "Customer", null));
                button_service.setOnAction(event -> DBUtils.changeScene(event, "services.fxml", "Services", null));
                button_reservationservices.setOnAction(event -> DBUtils.changeScene(event, "reservation-services.fxml", "Reservation Services", null));
                button_main.setOnAction(event -> DBUtils.changeScene(event, "logged-ın.fxml", "LoggedInController", null));

                smb_deletereservation.getItems().clear();
                List<String> reservationIDs = null;
                try {
                        reservationIDs = DBUtils.getReservationID();
                } catch (SQLException e) {
                        throw new RuntimeException(e);
                }
                for (String reservationID : reservationIDs) {
                        MenuItem menuItem = new MenuItem(reservationID);
                        menuItem.setOnAction(event -> smb_deletereservation.setText(menuItem.getText()));
                        smb_deletereservation.getItems().add(menuItem);
                }

                button_deletedreservation.setOnAction(event -> {
                        String selectedReservationID = smb_deletereservation.getText();
                        deleteReservation(selectedReservationID);
                });
        }
        private void deleteReservation(String selectedReservationID) {
                try {
                        int rowsAffected = DBUtils.deleteReservation(selectedReservationID);
                        if (rowsAffected > 0) {
                                System.out.println("Reservation deleted successfully.");
                                DBUtils.showSuccessAlert("Success", "Reservation Deleted", "Reservation has been successfully deleted.");
                        } else {
                                System.out.println("Failed to delete reservation.");
                                DBUtils.showErrorAlert("Error", "Failed to Delete Reservation", "Failed to delete the reservation.");
                        }
                } catch (SQLException e) {
                        e.printStackTrace();
                        DBUtils.showErrorAlert("Error", "Database Error", "An error occurred while accessing the database.");
                }
        }

}
