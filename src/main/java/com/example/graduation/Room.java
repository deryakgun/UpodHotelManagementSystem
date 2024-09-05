package com.example.graduation;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.sql.*;
import java.util.List;
import java.util.ResourceBundle;

public class Room implements Initializable {

    private int id;
    private String roomName;
    private int capacity;
    private double price;
    private int total;
    private String roomFeature;

    public Room() {
    }

    public Room(int id, String roomName, int capacity, double price, int total, String roomFeature) {
        this.id = id;
        this.roomName = roomName;
        this.capacity = capacity;
        this.price = price;
        this.total = total;
        this.roomFeature =roomFeature;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getRoomFeature() {
        return roomFeature;
    }

    public void setRoomFeature(String roomFeature) {
        this.roomFeature = roomFeature;
    }

    public int calculateAvailableRooms(Connection connection) {
        int reservedCount = getReservedCount(connection);
        return total - reservedCount;
    }

    public int getReservedCount(Connection connection) {
        try {
            int reservedCount = 0;
            String query = "SELECT COUNT(*) AS reserved_count FROM reservation WHERE room_id = ?";
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setInt(1, id);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        reservedCount = rs.getInt("reserved_count");
                    }
                }
            }
            return reservedCount;
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }
    @FXML
    private Button button_logout;
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
    private Button button_newroom;
    @FXML
    private Button button_delete;
    @FXML
    private Button button_edit;
    @FXML
    private Button button_main;
    @FXML
    private TableView<Room> table_room;
    @FXML
    private TableColumn<Room, Integer> tc_id;
    @FXML
    private TableColumn<Room, String> tc_roomname;
    @FXML
    private TableColumn<Room, Integer> tc_capacity;
    @FXML
    private TableColumn<Room, Integer> tc_total;
    @FXML
    private TableColumn<Room, Integer> tc_available;
    @FXML
    private TableColumn<Room, String> tc_roomfeature;
    @FXML
    private TableColumn<Room, Double> tc_price;
    @FXML
    private ObservableList<Room> roomList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        button_rooms.setOnAction(event -> DBUtils.changeScene(event, "room.fxml", "Room", null));
        button_features.setOnAction(event -> DBUtils.changeScene(event, "feature.fxml", "Feature", null));
        button_customer.setOnAction(event -> DBUtils.changeScene(event, "customer.fxml", "Customer", null));
        button_service.setOnAction(event -> DBUtils.changeScene(event, "services.fxml", "Services", null));
        button_reservationservices.setOnAction(event -> DBUtils.changeScene(event, "reservation-services.fxml", "Reservation Services", null));
        button_logout.setOnAction(event -> DBUtils.changeScene(event, "sign-in.fxml", "Log in", null));
        button_newroom.setOnAction(event -> DBUtils.changeScene(event, "new-rooms.fxml", "New Room", null));
        button_delete.setOnAction(event -> DBUtils.changeScene(event, "delete-room.fxml", "DELETE", null));
        button_main.setOnAction(event -> DBUtils.changeScene(event, "logged-ın.fxml", "LoggedInController", null));
        button_edit.setOnAction(event -> DBUtils.changeScene(event, "edit-room.fxml", "EDIT", null));

        initializeTableColumns();
        loadDataFromDatabase();

    }
    private void initializeTableColumns() {
        tc_id = new TableColumn<>("ID");
        tc_roomname = new TableColumn<>("Room Name");
        tc_capacity = new TableColumn<>("Capacity");
        tc_total = new TableColumn<>("Total");
        tc_available = new TableColumn<>("Available");
        tc_price = new TableColumn<>("Price");
        tc_roomfeature = new TableColumn<>("Feature");

        tc_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        tc_roomname.setCellValueFactory(new PropertyValueFactory<>("roomName"));
        tc_capacity.setCellValueFactory(new PropertyValueFactory<>("capacity"));
        tc_total.setCellValueFactory(new PropertyValueFactory<>("total"));
        tc_available.setCellValueFactory(new PropertyValueFactory<>("available"));
        tc_price.setCellValueFactory(new PropertyValueFactory<>("price"));
        tc_roomfeature.setCellValueFactory(new PropertyValueFactory<>("roomFeature"));


        table_room.getColumns().clear();
        table_room.getColumns().addAll(tc_id, tc_roomname, tc_capacity,tc_total, tc_price, tc_roomfeature);
    }
    private void loadDataFromDatabase() {
        try {
            roomList.clear();
            List<Room> rooms = DBUtils.getRoomsFromDatabase();
            for (Room room : rooms) {
                try (Connection connection = DriverManager.getConnection(DBUtils.DATABASE_CONNECTION, DBUtils.USERNAME, DBUtils.PASSWORD)) {
                    int availableRooms = room.calculateAvailableRooms(connection);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            roomList.addAll(rooms);
            table_room.setItems(roomList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}




