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

public class NewService implements Initializable {

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
    private Button button_servicesave;
    @FXML
    private Button button_main;
    @FXML
    private TextField tf_newservice;
    @FXML
    private TableView<Services> table_services;

    ObservableList<Services> serviceList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        table_services= new TableView<>();
        button_rooms.setOnAction(event -> DBUtils.changeScene(event, "room.fxml", "Room", null));
        button_features.setOnAction(event -> DBUtils.changeScene(event, "feature.fxml", "Feature", null));
        button_customer.setOnAction(event -> DBUtils.changeScene(event, "customer.fxml", "Customer", null));
        button_service.setOnAction(event -> DBUtils.changeScene(event, "services.fxml", "Services", null));
        button_reservationservices.setOnAction(event -> DBUtils.changeScene(event, "reservation-services.fxml", "Reservation Services", null));
        button_logout.setOnAction(event -> DBUtils.changeScene(event, "sign-in.fxml", "Log in", null));
        button_main.setOnAction(event -> DBUtils.changeScene(event, "logged-ın.fxml", "LoggedInController", null));

        button_servicesave.setOnAction(event -> {
            String servicesName = tf_newservice.getText();
            DBUtils.addNewServices(servicesName);
            loadDataToTable();

            DBUtils.showSuccessAlert("Service Added", "Succsess", "Service Added Successfully.");

        });

    }
    private void loadDataToTable() {
        try {
            serviceList.clear();
            serviceList.addAll(DBUtils.getServicesFromDatabase());
            table_services.setItems(serviceList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
