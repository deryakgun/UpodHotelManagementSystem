package com.example.graduation.controllers;

import com.example.graduation.DBUtils;
import com.example.graduation.ReservationServices;
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
import java.util.List;
import java.util.ResourceBundle;

public class EditReservationService implements Initializable {
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
    private Button button_editrs;
    @FXML
    private SplitMenuButton smb_rsıd;
    @FXML
    private SplitMenuButton smb_newreservationıd;
    @FXML
    private SplitMenuButton smb_newservice;
    @FXML
    private TextField tf_newunitprice;
    @FXML
    private TextField tf_newquantity;

    ObservableList<ReservationServices> reservationServiceList = FXCollections.observableArrayList();




    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        button_logout.setOnAction(event -> DBUtils.changeScene(event, "sign-in.fxml", "Log in", null));
        button_rooms.setOnAction(event -> DBUtils.changeScene(event, "room.fxml", "Room", null));
        button_features.setOnAction(event -> DBUtils.changeScene(event, "feature.fxml", "Feature", null));
        button_customer.setOnAction(event -> DBUtils.changeScene(event, "customer.fxml", "Customer", null));
        button_service.setOnAction(event -> DBUtils.changeScene(event, "services.fxml", "Services", null));
        button_reservationservices.setOnAction(event -> DBUtils.changeScene(event, "reservation-services.fxml", "Reservation Services", null));
        button_main.setOnAction(event -> DBUtils.changeScene(event, "logged-ın.fxml", "LoggedInController", null));
        button_editrs.setOnAction(this::handleEditReservationServiceButton);

        loadDataToTable();
        smb_newreservationıd.getItems().clear();
        smb_rsıd.getItems().clear();
        smb_newservice.getItems().clear();

        populateReservationServiceComboBox();

        List<ReservationServices> reservationServicesList = null;
        try {
            reservationServicesList = DBUtils.getReservationServicesFromDatabase();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        for (ReservationServices reservationServices : reservationServicesList) {
            MenuItem menuItem = new MenuItem(String.valueOf(reservationServices.getReservationId()));
            menuItem.setOnAction(event -> smb_newreservationıd.setText(menuItem.getText()));
            smb_newreservationıd.getItems().add(menuItem);
        }
        List<Services> servicesList = null;
        try {
            servicesList = DBUtils.getServicesFromDatabase();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        for (Services services : servicesList) {
            MenuItem menuItem = new MenuItem(String.valueOf(services.getServicesName()));
            menuItem.setOnAction(event -> smb_newservice.setText(menuItem.getText()));
            smb_newservice.getItems().add(menuItem);
        }


    }
    private void handleEditReservationServiceButton(ActionEvent event) {
        String selectedrsid = smb_rsıd.getText();
        String reservationid = smb_newreservationıd.getText();
        String service = smb_newservice.getText();
        int unitprice = Integer.parseInt(tf_newunitprice.getText());
        int quantity = Integer.parseInt(tf_newquantity.getText());

        editReservationService(selectedrsid, reservationid, service, unitprice, quantity);
    }

    private void editReservationService(String selectedrsid, String reservationid, String service, int unitprice, int quantity) {
        try (Connection connection = DriverManager.getConnection(DBUtils.DATABASE_CONNECTION, DBUtils.USERNAME, DBUtils.PASSWORD)) {
            String sql = "UPDATE reservation_service SET reservation_id=?, service_name=?, unit_price=?, quantity=? WHERE id=? ";

            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, reservationid);
                preparedStatement.setString(2, service);
                preparedStatement.setInt(3, unitprice);
                preparedStatement.setInt(4, quantity);
                preparedStatement.setString(5, selectedrsid);

                int rowsAffected = preparedStatement.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("Reservation Service updated successfully.");
                    DBUtils.showSuccessAlert("Success", "Reservation Service Updated", "Reservation Service has been successfully updated.");
                } else {
                    System.out.println("Failed to update Reservation Service.");
                    DBUtils.showErrorAlert("Error", "Failed to Update Reservation Service", "Failed to update the Reservation Service.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            DBUtils.showErrorAlert("Error", "Database Error", "An error occurred while accessing the database.");
        }
    }
    private void populateReservationServiceComboBox() {
        try (Connection connection = DriverManager.getConnection(DBUtils.DATABASE_CONNECTION, DBUtils.USERNAME, DBUtils.PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT id FROM reservation_service")) {

            var resultSet = preparedStatement.executeQuery();
            ObservableList<String> reservationserviceid = FXCollections.observableArrayList();

            while (resultSet.next()) {
                reservationserviceid.add(resultSet.getString("id"));
            }

            smb_rsıd.getItems().clear();
            for (String rsid : reservationserviceid) {
                MenuItem item = new MenuItem(rsid);
                item.setOnAction(event ->{
                    smb_rsıd.setText(rsid);
                    loadReservationServiceDetails(rsid);
                });
                smb_rsıd.getItems().add(item);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            DBUtils.showErrorAlert("Error", "Database Error", "An error occurred while accessing the database.");
        }
    }
    private void loadReservationServiceDetails(String rsid) {
        try (Connection connection = DriverManager.getConnection(DBUtils.DATABASE_CONNECTION, DBUtils.USERNAME, DBUtils.PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM reservation_service WHERE id = ?")) {

            preparedStatement.setString(1, rsid);
            var resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                //smb_newreservationıd.setText(resultSet.getString("reservation_id"));
                smb_newservice.setText(resultSet.getString("service_name"));
                tf_newunitprice.setText(String.valueOf(resultSet.getInt("unit_price")));
                tf_newquantity.setText(String.valueOf(resultSet.getInt("quantity")));
            }

        } catch (SQLException e) {
            e.printStackTrace();
            DBUtils.showErrorAlert("Error", "Database Error", "An error occurred while accessing the database.");
        }
    }





    private void loadDataToTable() {
        try {
            reservationServiceList.clear();
            reservationServiceList.addAll(DBUtils.getReservationServicesFromDatabase());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
