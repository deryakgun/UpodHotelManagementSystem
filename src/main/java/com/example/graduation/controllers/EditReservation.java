package com.example.graduation.controllers;

import com.example.graduation.*;
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
import java.util.List;
import java.util.ResourceBundle;

public class EditReservation implements Initializable {

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
    private Button button_editreservation;

    @FXML
    private SplitMenuButton smb_editroom;
    @FXML
    private TextField tf_editcustomer;
    @FXML
    private SplitMenuButton smb_reservationid;
    @FXML
    private DatePicker dp_editcheckındate;
    @FXML
    private DatePicker dp_editcheckoutdate;
    @FXML
    private Button button_newreservation;
    @FXML
    private Button button_main;
    @FXML
    private TableView<LoggedInController> table_reservation;

    ObservableList<LoggedInController> reservationList = FXCollections.observableArrayList();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        table_reservation = new TableView<>();
        button_logout.setOnAction(event -> DBUtils.changeScene(event, "sign-in.fxml", "Log in", null));
        button_rooms.setOnAction(event -> DBUtils.changeScene(event, "room.fxml", "Room", null));
        button_features.setOnAction(event -> DBUtils.changeScene(event, "feature.fxml", "Feature", null));
        button_customer.setOnAction(event -> DBUtils.changeScene(event, "customer.fxml", "Customer", null));
        button_service.setOnAction(event -> DBUtils.changeScene(event, "services.fxml", "Services", null));
        button_reservationservices.setOnAction(event -> DBUtils.changeScene(event, "reservation-services.fxml", "Reservation Services", null));
        button_main.setOnAction(event -> DBUtils.changeScene(event, "logged-ın.fxml", "LoggedInController", null));
        button_editreservation.setOnAction(this::handleEditCustomerButton);
        loadDataToTable();
        smb_editroom.getItems().clear();
        populateReservationComboBox();

        List<Room> roomList = null;
        try {
            roomList = DBUtils.getRoomsFromDatabase();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        for (Room room : roomList) {
            MenuItem menuItem = new MenuItem(String.valueOf(room.getRoomName()));
            menuItem.setOnAction(event -> smb_editroom.setText(menuItem.getText()));
            smb_editroom.getItems().add(menuItem);
        }

    }

    private void handleEditCustomerButton(ActionEvent event) {
        String selectedReservationid = smb_reservationid.getText();
        String customer = tf_editcustomer.getText();
        String room = smb_editroom.getText();
        //String checkInDate = dp_editcheckındate.getText();
        String checkInDate = (dp_editcheckındate.getValue() != null) ? dp_editcheckındate.getValue().toString() : null;
        String checkOutDate = (dp_editcheckoutdate.getValue() != null) ? dp_editcheckoutdate.getValue().toString() : null;

        editReservation(selectedReservationid, customer , room, checkInDate, checkOutDate);
    }

    private void editReservation(String selectedReservationId, String customer, String room, String checkInDate, String checkOutDate) {
        try (Connection connection = DriverManager.getConnection(DBUtils.DATABASE_CONNECTION, DBUtils.USERNAME, DBUtils.PASSWORD)) {
            String sql = "UPDATE reservation SET customer=?, room_name=?, check_in_date=?, check_out_date=? WHERE id=?";

            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, customer);
                preparedStatement.setString(2, room);
                preparedStatement.setString(3, checkInDate);
                preparedStatement.setString(4, checkOutDate);
                preparedStatement.setString(5, selectedReservationId);

                int rowsAffected = preparedStatement.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("Reservation updated successfully.");
                    DBUtils.showSuccessAlert("Success", "Reservation Updated", "Reservation has been successfully updated.");
                } else {
                    System.out.println("Failed to update reservation.");
                    DBUtils.showErrorAlert("Error", "Failed to Update Reservation", "Failed to update the reservation.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            DBUtils.showErrorAlert("Error", "Database Error", "An error occurred while accessing the database.");
        }
    }

    private void populateReservationFields(String selectedReservationid) {
        try (Connection connection = DriverManager.getConnection(DBUtils.DATABASE_CONNECTION, DBUtils.USERNAME, DBUtils.PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM reservation WHERE id=?")) {

            preparedStatement.setString(1, selectedReservationid);
            var resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                tf_editcustomer.setText(resultSet.getString("customer"));
                smb_editroom.setText(resultSet.getString("room_name"));
                dp_editcheckındate.setValue(LocalDate.parse(resultSet.getString("check_in_date")));
                dp_editcheckoutdate.setValue(LocalDate.parse(resultSet.getString("check_out_date")));

            }
        } catch (SQLException e) {
            e.printStackTrace();
            DBUtils.showErrorAlert("Error", "Database Error", "An error occurred while accessing the database.");
        }
    }

    private void populateReservationComboBox() {
        try (Connection connection = DriverManager.getConnection(DBUtils.DATABASE_CONNECTION, DBUtils.USERNAME, DBUtils.PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT id FROM reservation")) {

            var resultSet = preparedStatement.executeQuery();
            ObservableList<String> reservation_id = FXCollections.observableArrayList();

            while (resultSet.next()) {
                reservation_id.add(resultSet.getString("id"));
            }

            smb_reservationid.getItems().clear();
            for (String reservationid : reservation_id) {
                MenuItem item = new MenuItem(reservationid);
                item.setOnAction(event ->{
                    smb_reservationid.setText(reservationid);
                    populateReservationFields(reservationid);
                });
                smb_reservationid.getItems().add(item);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            DBUtils.showErrorAlert("Error", "Database Error", "An error occurred while accessing the database.");
        }
    }

    private void loadDataToTable() {
        try {
            reservationList.clear();
            reservationList.addAll(DBUtils.getReservationFromDatabase());
            table_reservation.setItems(reservationList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
