package com.example.graduation.controllers;
import com.example.graduation.Customer;
import com.example.graduation.DBUtils;
import com.example.graduation.Services;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SplitMenuButton;

import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class DeletedService implements Initializable {
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
    private Button button_deletedservice;
    @FXML
    private SplitMenuButton smb_deleteservice;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        button_logout.setOnAction(event -> DBUtils.changeScene(event, "sign-in.fxml", "Log in", null));
        button_rooms.setOnAction(event -> DBUtils.changeScene(event, "room.fxml", "Room", null));
        button_features.setOnAction(event -> DBUtils.changeScene(event, "feature.fxml", "Feature", null));
        button_customer.setOnAction(event -> DBUtils.changeScene(event, "customer.fxml", "Customer", null));
        button_service.setOnAction(event -> DBUtils.changeScene(event, "services.fxml", "Services", null));
        button_reservationservices.setOnAction(event -> DBUtils.changeScene(event, "reservation-services.fxml", "Reservation Services", null));

        smb_deleteservice.getItems().clear();
        List<Services> servicesList = null;
        try {
            servicesList = DBUtils.getServicesFromDatabase();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        for (Services services : servicesList) {
            MenuItem menuItem = new MenuItem(String.valueOf(services.getServicesName()));
            menuItem.setOnAction(event -> smb_deleteservice.setText(menuItem.getText()));
            smb_deleteservice.getItems().add(menuItem);
        }
        button_deletedservice.setOnAction(event -> {
            String selectedService = smb_deleteservice.getText();
            deleteService(selectedService);
        });

    }
    private boolean deleteService(String selectedService) {
        try {
            int rowsAffected = DBUtils.deleteReservationService(selectedService);
            if (rowsAffected >= 0) {
                rowsAffected = DBUtils.deleteService(selectedService);
                if (rowsAffected > 0) {
                    System.out.println("Service deleted successfully.");
                    DBUtils.showSuccessAlert("Success", "Service Deleted", "Service has been successfully deleted.");
                    return true;
                } else {
                    System.out.println("Failed to delete service.");
                    DBUtils.showErrorAlert("Error", "Failed to Delete Service", "Failed to delete the service.");
                }
            } else {
                System.out.println("Failed to delete reservations.");
                DBUtils.showErrorAlert("Error", "Failed to Delete Reservations", "Failed to delete reservations of the service.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            DBUtils.showErrorAlert("Error", "Database Error", "An error occurred while accessing the database.");
        }
        return false;

    }







}