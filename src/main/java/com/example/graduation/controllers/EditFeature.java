package com.example.graduation.controllers;

import com.example.graduation.DBUtils;
import com.example.graduation.Feature;
import com.example.graduation.Room;
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

public class EditFeature implements Initializable {
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
    private Button button_editfeature;
    @FXML
    private SplitMenuButton sbm_feature;
    @FXML
    private TextField tf_newfeature;

    ObservableList<Feature> featureList = FXCollections.observableArrayList();




    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        button_logout.setOnAction(event -> DBUtils.changeScene(event, "sign-in.fxml", "Log in", null));
        button_rooms.setOnAction(event -> DBUtils.changeScene(event, "room.fxml", "Room", null));
        button_features.setOnAction(event -> DBUtils.changeScene(event, "feature.fxml", "Feature", null));
        button_customer.setOnAction(event -> DBUtils.changeScene(event, "customer.fxml", "Customer", null));
        button_service.setOnAction(event -> DBUtils.changeScene(event, "services.fxml", "Services", null));
        button_reservationservices.setOnAction(event -> DBUtils.changeScene(event, "reservation-services.fxml", "Reservation Services", null));
        button_main.setOnAction(event -> DBUtils.changeScene(event, "logged-ın.fxml", "LoggedInController", null));
        button_editfeature.setOnAction(this::handleEditFeatureButton);
        loadDataToTable();
        sbm_feature.getItems().clear();
        populateFeatureComboBox();


    }
    private void handleEditFeatureButton(ActionEvent event) {
        String selectedFeature = sbm_feature.getText();
        String feature = tf_newfeature.getText();

        editFeature(selectedFeature,feature);
    }
    private void editFeature(String selectedFeature,String feature) {
        try (Connection connection = DriverManager.getConnection(DBUtils.DATABASE_CONNECTION, DBUtils.USERNAME, DBUtils.PASSWORD)) {
            String sql = "UPDATE feature SET feature_name=? WHERE feature_name=?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, feature);
                preparedStatement.setString(2, selectedFeature);

                int rowsAffected = preparedStatement.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("Feature updated successfully.");
                    DBUtils.showSuccessAlert("Success", "Feature Updated", "Feature has been successfully updated.");
                } else {
                    System.out.println("Failed to update feature.");
                    DBUtils.showErrorAlert("Error", "Failed to Update Feature", "Failed to update the feature.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            DBUtils.showErrorAlert("Error", "Database Error", "An error occurred while accessing the database.");
        }
    }

    private void populateFeatureComboBox() {
        try (Connection connection = DriverManager.getConnection(DBUtils.DATABASE_CONNECTION, DBUtils.USERNAME, DBUtils.PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT feature_name FROM feature")) {

            var resultSet = preparedStatement.executeQuery();
            ObservableList<String> feature = FXCollections.observableArrayList();

            while (resultSet.next()) {
                feature.add(resultSet.getString("feature_name"));
            }

            sbm_feature.getItems().clear();
            for (String featureName : feature) {
                MenuItem item = new MenuItem(featureName);
                item.setOnAction(event -> sbm_feature.setText(featureName));
                sbm_feature.getItems().add(item);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            DBUtils.showErrorAlert("Error", "Database Error", "An error occurred while accessing the database.");
        }
    }


    private void loadDataToTable() {
        try {
            featureList.clear();
            featureList.addAll(DBUtils.getFeaturesFromDatabase());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



}
