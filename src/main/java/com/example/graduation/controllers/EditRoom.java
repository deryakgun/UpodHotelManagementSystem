package com.example.graduation.controllers;

import com.example.graduation.DBUtils;
import com.example.graduation.Feature;
import com.example.graduation.Room;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class EditRoom implements Initializable {

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
    private Button button_editroom;

    @FXML
    private Button button_main;
    @FXML
    private SplitMenuButton smb_room;
    @FXML
    private TextField tf_newroomname;
    @FXML
    private TextField tf_newroomcapacity;
    @FXML
    private TextField tf_newroomprice;
    @FXML
    private TextField tf_total;
    @FXML
    private TextField tf_feature;

    ObservableList<Room> roomList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //table_room = new TableView<>();
        button_logout.setOnAction(event -> DBUtils.changeScene(event, "sign-in.fxml", "Log in", null));
        button_rooms.setOnAction(event -> DBUtils.changeScene(event, "room.fxml", "Room", null));
        button_features.setOnAction(event -> DBUtils.changeScene(event, "feature.fxml", "Feature", null));
        button_customer.setOnAction(event -> DBUtils.changeScene(event, "customer.fxml", "Customer", null));
        button_service.setOnAction(event -> DBUtils.changeScene(event, "services.fxml", "Services", null));
        button_reservationservices.setOnAction(event -> DBUtils.changeScene(event, "reservation-services.fxml", "Reservation Services", null));
        button_main.setOnAction(event -> DBUtils.changeScene(event, "logged-ın.fxml", "LoggedInController", null));
        button_editroom.setOnAction(this::handleEditRoomButton);
        loadDataToTable();
        smb_room.getItems().clear();
        populateRoomComboBox();


    }
    private void handleEditRoomButton(ActionEvent event) {
        String selectedRoom = smb_room.getText();
        String roomName = tf_newroomname.getText();
        String capacity = tf_newroomcapacity.getText();
        String price = tf_newroomprice.getText();
        String total = tf_total.getText();
        String feature = tf_feature.getText();

        editRoom(selectedRoom, roomName, capacity, price, total, feature);
    }

    private void editRoom(String selectedRoom, String roomName, String capacity, String price, String total, String feature) {
        try (Connection connection = DriverManager.getConnection(DBUtils.DATABASE_CONNECTION, DBUtils.USERNAME, DBUtils.PASSWORD)) {
            String sql = "UPDATE room SET room_name=?, room_feature=?, capacity=?, total=?, price=? WHERE room_name=?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, roomName);
                preparedStatement.setString(2, feature);
                preparedStatement.setString(3, capacity);
                preparedStatement.setString(4, total);
                preparedStatement.setString(5, price);
                preparedStatement.setString(6, selectedRoom);

                int rowsAffected = preparedStatement.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("Room updated successfully.");
                    DBUtils.showSuccessAlert("Success", "Room Updated", "Room has been successfully updated.");
                } else {
                    System.out.println("Failed to update room.");
                    DBUtils.showErrorAlert("Error", "Failed to Update Room", "Failed to update the room.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            DBUtils.showErrorAlert("Error", "Database Error", "An error occurred while accessing the database.");
        }
    }
    private void populateRoomFields(String selectedRoom) {
        try (Connection connection = DriverManager.getConnection(DBUtils.DATABASE_CONNECTION, DBUtils.USERNAME, DBUtils.PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM room WHERE room_name=?")) {

            preparedStatement.setString(1, selectedRoom);
            var resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                tf_newroomname.setText(resultSet.getString("room_name"));
                tf_newroomcapacity.setText(resultSet.getString("capacity"));
                tf_newroomprice.setText(resultSet.getString("price"));
                tf_total.setText(resultSet.getString("total"));
                tf_feature.setText(resultSet.getString("room_feature"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            DBUtils.showErrorAlert("Error", "Database Error", "An error occurred while accessing the database.");
        }
    }

    private void populateRoomComboBox() {
        try (Connection connection = DriverManager.getConnection(DBUtils.DATABASE_CONNECTION, DBUtils.USERNAME, DBUtils.PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT room_name FROM room")) {

            var resultSet = preparedStatement.executeQuery();
            ObservableList<String> roomNames = FXCollections.observableArrayList();

            while (resultSet.next()) {
                roomNames.add(resultSet.getString("room_name"));
            }

            smb_room.getItems().clear();
            for (String roomName : roomNames) {
                MenuItem item = new MenuItem(roomName);
                item.setOnAction(event -> {
                    smb_room.setText(roomName);
                    populateRoomFields(roomName);
                });
                smb_room.getItems().add(item);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            DBUtils.showErrorAlert("Error", "Database Error", "An error occurred while accessing the database.");
        }
    }

    private void loadDataToTable() {
        try {
            roomList.clear();
            roomList.addAll(DBUtils.getRoomsFromDatabase());
            //table_room.setItems(roomList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }





}
