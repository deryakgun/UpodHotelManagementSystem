package com.example.graduation.controllers;

import com.example.graduation.DBUtils;
import com.example.graduation.Feature;
import com.example.graduation.Services;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SplitMenuButton;
import javafx.scene.control.TextField;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class EditService implements Initializable {
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
    private Button button_main;
    @FXML
    private Button button_editservice;
    @FXML
    private SplitMenuButton smb_service;
    @FXML
    private TextField tf_newservice;

    ObservableList<Services> serviceList = FXCollections.observableArrayList();




    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        button_logout.setOnAction(event -> DBUtils.changeScene(event, "sign-in.fxml", "Log in", null));
        button_rooms.setOnAction(event -> DBUtils.changeScene(event, "room.fxml", "Room", null));
        button_features.setOnAction(event -> DBUtils.changeScene(event, "feature.fxml", "Feature", null));
        button_customer.setOnAction(event -> DBUtils.changeScene(event, "customer.fxml", "Customer", null));
        button_service.setOnAction(event -> DBUtils.changeScene(event, "services.fxml", "Services", null));
        button_reservationservices.setOnAction(event -> DBUtils.changeScene(event, "reservation-services.fxml", "Reservation Services", null));
        button_main.setOnAction(event -> DBUtils.changeScene(event, "logged-ın.fxml", "LoggedInController", null));
        button_editservice.setOnAction(this::handleEditServiceButton);
        loadDataToTable();
        smb_service.getItems().clear();
        populateServiceComboBox();


    }
    private void handleEditServiceButton(ActionEvent event) {
        String selectedService = smb_service.getText();
        String service = tf_newservice.getText();

        editService(selectedService,service);
    }
    private void editService(String selectedService,String service) {
        try (Connection connection = DriverManager.getConnection(DBUtils.DATABASE_CONNECTION, DBUtils.USERNAME, DBUtils.PASSWORD)) {
            String sql = "UPDATE service SET service_name=? WHERE service_name=?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, service);
                preparedStatement.setString(2, selectedService);

                int rowsAffected = preparedStatement.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("Service updated successfully.");
                    DBUtils.showSuccessAlert("Success", "Service Updated", "Service has been successfully updated.");
                } else {
                    System.out.println("Failed to update service.");
                    DBUtils.showErrorAlert("Error", "Failed to Update Service", "Failed to update the service.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            DBUtils.showErrorAlert("Error", "Database Error", "An error occurred while accessing the database.");
        }
    }

    private void populateServiceComboBox() {
        try (Connection connection = DriverManager.getConnection(DBUtils.DATABASE_CONNECTION, DBUtils.USERNAME, DBUtils.PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT service_name FROM service")) {

            var resultSet = preparedStatement.executeQuery();
            ObservableList<String> service = FXCollections.observableArrayList();

            while (resultSet.next()) {
                service.add(resultSet.getString("service_name"));
            }

            smb_service.getItems().clear();
            for (String serviceName : service) {
                MenuItem item = new MenuItem(serviceName);
                item.setOnAction(event -> smb_service.setText(serviceName));
                smb_service.getItems().add(item);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            DBUtils.showErrorAlert("Error", "Database Error", "An error occurred while accessing the database.");
        }
    }


    private void loadDataToTable() {
        try {
            serviceList.clear();
            serviceList.addAll(DBUtils.getServicesFromDatabase());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
