package com.example.graduation;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class NewRoom implements Initializable {

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
    private Button button_roomsave;
    @FXML
    private Button button_main;
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
    @FXML
    private  ListView<String> lv_features;
    @FXML
    private TableView<Room> table_room;

    ObservableList<Room> roomList = FXCollections.observableArrayList();
    private ObservableList<Feature> featureList = FXCollections.observableArrayList();
    private List<String> selectedFeatures = new ArrayList<String>();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        table_room = new TableView<>();
        button_logout.setOnAction(event -> DBUtils.changeScene(event, "sign-in.fxml", "Log in", null));
        button_rooms.setOnAction(event -> DBUtils.changeScene(event, "room.fxml", "Room", null));
        button_features.setOnAction(event -> DBUtils.changeScene(event, "feature.fxml", "Feature", null));
        button_customer.setOnAction(event -> DBUtils.changeScene(event, "customer.fxml", "Customer", null));
        button_service.setOnAction(event -> DBUtils.changeScene(event, "services.fxml", "Services", null));
        button_reservationservices.setOnAction(event -> DBUtils.changeScene(event, "reservation-services.fxml", "Reservation Services", null));
        button_main.setOnAction(event -> DBUtils.changeScene(event, "logged-ın.fxml", "LoggedInController", null));


        button_roomsave.setOnAction(event -> {
            String roomName = tf_newroomname.getText();
            int capacity = Integer.parseInt(tf_newroomcapacity.getText());
            double price = Double.parseDouble(tf_newroomprice.getText());
            String room_feature = tf_feature.getText();
            int total = Integer.parseInt(tf_total.getText());

            DBUtils.addNewRoom(roomName, room_feature, capacity,total, price);
            DBUtils.showSuccessAlert("Room Added", "Succsess", "Room Added Successfully.");


            loadDataToTable();
        });


    }
    private void loadDataToTable() {
        try {
            roomList.clear();
            roomList.addAll(DBUtils.getRoomsFromDatabase());
            table_room.setItems(roomList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
