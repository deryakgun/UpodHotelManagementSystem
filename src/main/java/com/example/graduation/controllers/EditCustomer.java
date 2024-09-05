package com.example.graduation.controllers;

import com.example.graduation.Customer;
import com.example.graduation.DBUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Date;
import java.util.ResourceBundle;

public class EditCustomer implements Initializable {

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
    private SplitMenuButton smb_customer;
    @FXML
    private TextField tf_customeridentitiynumber;
    @FXML
    private TextField tf_customerphonenumber;
    @FXML
    private DatePicker dp_customerbirthdate;
    @FXML
    private TextField tf_customerdescription;
    @FXML
    private TextField tf_customername;
    @FXML
    private Button button_editcustomer;
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

        button_editcustomer.setOnAction(this::handleEditCustomerButton);
        loadDataToTable();
        smb_customer.getItems().clear();
        populateCustomerComboBox();

    }

    private void handleEditCustomerButton(ActionEvent event) {
        String selectedCustomer = smb_customer.getText();
        String fullName = tf_customername.getText();
        String identityNumber = tf_customeridentitiynumber.getText();
        String phoneNumber = tf_customerphonenumber.getText();
        String birthDate = (dp_customerbirthdate.getValue() != null) ? dp_customerbirthdate.getValue().toString() : null;
        String description = tf_customerdescription.getText();

        editCustomer(selectedCustomer, fullName, identityNumber, phoneNumber, birthDate, description);
    }

    private void editCustomer(String selectedCustomer, String fullName, String identityNumber, String phoneNumber, String birthDate, String description) {
        try (Connection connection = DriverManager.getConnection(DBUtils.DATABASE_CONNECTION, DBUtils.USERNAME, DBUtils.PASSWORD)) {
            String sql = "UPDATE customer SET full_name=?, identity_number=?, phone_number=?, birth_date=?, decription=? WHERE full_name=?";

            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, fullName);
                preparedStatement.setString(2, identityNumber);
                preparedStatement.setString(3, phoneNumber);
                preparedStatement.setString(4, birthDate);
                preparedStatement.setString(5, description);
                preparedStatement.setString(6, selectedCustomer);

                int rowsAffected = preparedStatement.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("Customer updated successfully.");
                    DBUtils.showSuccessAlert("Success", "Customer Updated", "Customer has been successfully updated.");
                } else {
                    System.out.println("Failed to update customer.");
                    DBUtils.showErrorAlert("Error", "Failed to Update Customer", "Failed to update the customer.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            DBUtils.showErrorAlert("Error", "Database Error", "An error occurred while accessing the database.");
        }
    }

    private void populateCustomerFields(String selectedCustomer) {
        try (Connection connection = DriverManager.getConnection(DBUtils.DATABASE_CONNECTION, DBUtils.USERNAME, DBUtils.PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM customer WHERE full_name=?")) {

            preparedStatement.setString(1, selectedCustomer);
            var resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                tf_customername.setText(resultSet.getString("full_name"));
                tf_customeridentitiynumber.setText(resultSet.getString("identity_number"));
                tf_customerphonenumber.setText(resultSet.getString("phone_number"));
                dp_customerbirthdate.setValue(LocalDate.parse(resultSet.getString("birth_date")));
                tf_customerdescription.setText(resultSet.getString("decription"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            DBUtils.showErrorAlert("Error", "Database Error", "An error occurred while accessing the database.");
        }
    }

    private void populateCustomerComboBox() {
        try (Connection connection = DriverManager.getConnection(DBUtils.DATABASE_CONNECTION, DBUtils.USERNAME, DBUtils.PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT full_name FROM customer")) {

            var resultSet = preparedStatement.executeQuery();
            ObservableList<String> customerNames = FXCollections.observableArrayList();

            while (resultSet.next()) {
                customerNames.add(resultSet.getString("full_name"));
            }

            // smb_customer.setText(customerNames.toString());
            smb_customer.getItems().clear();
            for (String customerName : customerNames) {
                MenuItem item = new MenuItem(customerName);
                item.setOnAction(event ->{
                        smb_customer.setText(customerName);
                        populateCustomerFields(customerName);
                });
                smb_customer.getItems().add(item);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            DBUtils.showErrorAlert("Error", "Database Error", "An error occurred while accessing the database.");
        }
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
