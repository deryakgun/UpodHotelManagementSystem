package com.example.graduation;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class NewFeature implements Initializable {
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
    private TextField tf_newfeaturename;
    @FXML
    private Button button_featuresave;
    @FXML
    private Button button_main;
    @FXML
    private TableView<Feature> table_features;

    ObservableList<Feature> featureList = FXCollections.observableArrayList();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        table_features = new TableView<>();
        button_logout.setOnAction(event -> DBUtils.changeScene(event, "sign-in.fxml", "Log in", null));
        button_rooms.setOnAction(event -> DBUtils.changeScene(event, "room.fxml", "Room", null));
        button_features.setOnAction(event -> DBUtils.changeScene(event, "feature.fxml", "Feature", null));
        button_customer.setOnAction(event -> DBUtils.changeScene(event, "customer.fxml", "Customer", null));
        button_service.setOnAction(event -> DBUtils.changeScene(event, "services.fxml", "Services", null));
        button_reservationservices.setOnAction(event -> DBUtils.changeScene(event, "reservation-services.fxml", "Reservation Services", null));
        button_main.setOnAction(event -> DBUtils.changeScene(event, "logged-ın.fxml", "LoggedInController", null));

        button_featuresave.setOnAction(event -> {
            String featureName = tf_newfeaturename.getText();
            DBUtils.addNewFeature(featureName);
            loadDataToTable();

            DBUtils.showSuccessAlert("Feature Added", "Succsess", "Feature Added Successfully.");

        });

    }
    private void loadDataToTable() {
        try {
            featureList.clear();
            featureList.addAll(DBUtils.getFeaturesFromDatabase());
            table_features.setItems(featureList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
