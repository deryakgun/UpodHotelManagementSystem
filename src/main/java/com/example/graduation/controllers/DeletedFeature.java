package com.example.graduation.controllers;
import com.example.graduation.Customer;
import com.example.graduation.DBUtils;
import com.example.graduation.Feature;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SplitMenuButton;

import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class DeletedFeature implements Initializable {
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
    private Button button_deletedfeature;
    @FXML
    private SplitMenuButton smb_deletefeature;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        button_logout.setOnAction(event -> DBUtils.changeScene(event, "sign-in.fxml", "Log in", null));
        button_rooms.setOnAction(event -> DBUtils.changeScene(event, "room.fxml", "Room", null));
        button_features.setOnAction(event -> DBUtils.changeScene(event, "feature.fxml", "Feature", null));
        button_customer.setOnAction(event -> DBUtils.changeScene(event, "customer.fxml", "Customer", null));
        button_service.setOnAction(event -> DBUtils.changeScene(event, "services.fxml", "Services", null));
        button_reservationservices.setOnAction(event -> DBUtils.changeScene(event, "reservation-services.fxml", "Reservation Services", null));

        smb_deletefeature.getItems().clear();
        List<Feature> featureList = null;
        try {
            featureList = DBUtils.getFeaturesFromDatabase();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        for (Feature feature : featureList) {
            MenuItem menuItem = new MenuItem(String.valueOf(feature.getFeatureName()));
            menuItem.setOnAction(event -> smb_deletefeature.setText(menuItem.getText()));
            smb_deletefeature.getItems().add(menuItem);
        }
        button_deletedfeature.setOnAction(event -> {
            String selectedCustomer = smb_deletefeature.getText();
            deleteFeature(selectedCustomer);
        });

    }
    private boolean deleteFeature(String selectedFeature) {
        try {
            int rowsAffected = DBUtils.deleteFeature(selectedFeature);
            if (rowsAffected > 0) {
                System.out.println("Feature deleted successfully.");
                DBUtils.showSuccessAlert("Success", "Feature Deleted", "Feature has been successfully deleted.");
                return true;
            } else {
                System.out.println("Failed to delete Feature.");
                DBUtils.showErrorAlert("Error", "Failed to Delete Feature", "Failed to delete the Feature.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            DBUtils.showErrorAlert("Error", "Database Error", "An error occurred while accessing the database.");
        }
        return false;
    }







}