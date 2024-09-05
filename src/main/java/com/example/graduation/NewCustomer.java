package com.example.graduation;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.net.DatagramPacket;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Date;
import java.util.ResourceBundle;

public class NewCustomer implements Initializable {

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
    private TextField tf_fullnamecustomer;
    @FXML
    private  TextField tf_customeridentitiynumber;
    @FXML
    private TextField tf_customerphonenumber;
    @FXML
    private DatePicker dp_customerbirthdate;
    @FXML
    private TextField tf_customerdescription;
    @FXML
    private Button button_customersave;
    @FXML
    private Button button_main;
    @FXML
    private TableView<Customer> table_customer;

    ObservableList<Customer> customerList = FXCollections.observableArrayList();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        table_customer = new TableView<>();
        button_logout.setOnAction(event -> DBUtils.changeScene(event, "sign-in.fxml", "Log in", null));
        button_rooms.setOnAction(event -> DBUtils.changeScene(event, "room.fxml", "Room", null));
        button_features.setOnAction(event -> DBUtils.changeScene(event, "feature.fxml", "Feature", null));
        button_customer.setOnAction(event -> DBUtils.changeScene(event, "customer.fxml", "Customer", null));
        button_service.setOnAction(event -> DBUtils.changeScene(event, "services.fxml", "Services", null));
        button_reservationservices.setOnAction(event -> DBUtils.changeScene(event, "reservation-services.fxml", "Reservation Services", null));
        button_main.setOnAction(event -> DBUtils.changeScene(event, "logged-ın.fxml", "LoggedInController", null));

        button_customersave.setOnAction(event -> {
            String fullName = tf_fullnamecustomer.getText();
            String identityNumber = tf_customeridentitiynumber.getText();
            String phoneNumber = tf_customerphonenumber.getText();

            LocalDate birthDate = dp_customerbirthdate.getValue();
            Date birthDateAsDate = java.sql.Date.valueOf(birthDate);

            String customerDescription = tf_customerdescription.getText();

            DBUtils.addNewCustomer(fullName, identityNumber, phoneNumber, birthDateAsDate,customerDescription);
            loadDataToTable();
            DBUtils.showSuccessAlert("Customer Added", "Succsess", "Customer Added Successfully.");

        });


    }
    private void loadDataToTable() {
        try {
            customerList.clear();
            customerList.addAll(DBUtils.getCustomersFromDatabase());
            table_customer.setItems(customerList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
