package com.example.graduation;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.sql.*;
import java.util.List;
import java.util.ResourceBundle;

import static com.example.graduation.DBUtils.*;

public class LoggedInController implements Initializable {
    private int id;
    private String roomId;
    private String roomName;
    private Date checkInDate;
    private Date checkOutDate;
    private String customerName;
    private String customerId;
    private String checked_in;
    private String checked_out;


    public LoggedInController() {
    }

    public LoggedInController(int id, String roomName, String roomId, Date checkInDate, Date checkOutDate, String customerName, String customerId, String checked_in, String checked_out) {
        this.id = id;
        this.roomName = roomName;
        this.roomId = roomId;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.customerName = customerName;
        this.customerId = customerId;
        this.checked_in = checked_in;
        this.checked_out = checked_out;
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

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public Date getCheckInDate() {
        return checkInDate;
    }

    public void setCheckInDate(Date checkInDate) {
        this.checkInDate = checkInDate;
    }

    public Date getCheckOutDate() {
        return checkOutDate;
    }

    public void setCheckOutDate(Date checkOutDate) {
        this.checkOutDate = checkOutDate;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }


    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getChecked_in() {
        return checked_in;
    }

    public void setChecked_in(String checked_in) {
        this.checked_in = checked_in;
    }

    public String getChecked_out() {
        return checked_out;
    }

    public void setChecked_out(String checked_out) {
        this.checked_out = checked_out;
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
    private Button button_newreservation;
    @FXML
    private Label label_welcome;
    @FXML
    private Button button_delete;
    @FXML
    private Button button_edit;
    @FXML
    private TableView<LoggedInController> table_logged;
    @FXML
    private TableColumn<LoggedInController, Integer> tc_id;
    @FXML
    private TableColumn<LoggedInController, String> tc_room;
    @FXML
    private TableColumn<LoggedInController, String> tc_roomId;
    @FXML
    private TableColumn<LoggedInController, Date> tc_checkındate;
    @FXML
    private TableColumn<LoggedInController, Date> tc_checkoutdate;
    @FXML
    private Button button_main;
    @FXML
    private TableColumn<LoggedInController, String> tc_customerId;
    @FXML
    private TableColumn<LoggedInController, String> tc_customer;
    @FXML
    private TableColumn<LoggedInController, String> tc_checked_in;
    @FXML
    private TableColumn<LoggedInController, String> tc_checked_out;
    @FXML
    private DatePicker dp_from;
    @FXML
    private DatePicker dp_to;
    @FXML
    private TextField tf_search;
    @FXML
    private Button button_search;

    ObservableList<LoggedInController> reservationList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        button_logout.setOnAction(event -> DBUtils.changeScene(event, "sign-in.fxml", "Log in", null));
        button_rooms.setOnAction(event -> DBUtils.changeScene(event, "room.fxml", "Room", null));
        button_features.setOnAction(event -> DBUtils.changeScene(event, "feature.fxml", "Feature", null));
        button_customer.setOnAction(event -> DBUtils.changeScene(event, "customer.fxml", "Customer", null));
        button_service.setOnAction(event -> DBUtils.changeScene(event, "services.fxml", "Services", null));
        button_reservationservices.setOnAction(event -> DBUtils.changeScene(event, "reservation-services.fxml", "Reservation Services", null));
        button_newreservation.setOnAction(event -> DBUtils.changeScene(event, "new-reservation.fxml", "Reservation", null));
        button_main.setOnAction(event -> DBUtils.changeScene(event, "logged-ın.fxml", "LoggedInController", null));
        button_delete.setOnAction(event -> DBUtils.changeScene(event, "delete-reservation.fxml", "DELETE", null));
        button_edit.setOnAction(event -> DBUtils.changeScene(event, "edit-reservation.fxml", "EDIT", null));

        button_search.setOnAction(this::handleSearchButtonClick);
        initializeTableColumns();
        loadDataFromDatabase();


    }

    private void initializeTableColumns() {
        tc_id = new TableColumn<>("ID");
        tc_room = new TableColumn<>("Room");
        tc_roomId = new TableColumn<>("Room");
        tc_checkındate = new TableColumn<>("Check-In Date");
        tc_checkoutdate = new TableColumn<>("Check-Out Date");
        tc_customerId = new TableColumn<>("Customer");
        tc_customer = new TableColumn<>("Customer");
        tc_checked_in = new TableColumn<>("Checked In");
        tc_checked_out = new TableColumn<>("Checked Out");


        tc_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        tc_room.setCellValueFactory(new PropertyValueFactory<>("roomName"));
        tc_roomId.setCellValueFactory(new PropertyValueFactory<>("roomId"));
        tc_checkındate.setCellValueFactory(new PropertyValueFactory<>("checkInDate"));
        tc_checkoutdate.setCellValueFactory(new PropertyValueFactory<>("checkOutDate"));
        tc_customerId.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        tc_customer.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        tc_checked_in.setCellValueFactory(new PropertyValueFactory<>("checked_in"));
        tc_checked_out.setCellValueFactory(new PropertyValueFactory<>("checked_out"));


        tc_customer.setCellValueFactory(cellData -> {
            String customerId = cellData.getValue().getCustomerId();
            String fullName = getCustomerNameById(customerId);
            return new SimpleStringProperty(fullName);
        });
        tc_room.setCellValueFactory(cellData -> {
            String roomId = cellData.getValue().getRoomId();
            String roomName = getRoomNameById(roomId);
            return new SimpleStringProperty(roomName);
        });

        table_logged.getColumns().clear();
        table_logged.getColumns().addAll(tc_id, tc_roomId, tc_checkındate, tc_checkoutdate, tc_checked_in, tc_checked_out, tc_customerId);


    }

    private List<LoggedInController> filterReservationsByName(String searchTerm) throws SQLException {
        List<LoggedInController> filteredReservations = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DriverManager.getConnection(DBUtils.DATABASE_CONNECTION, DBUtils.USERNAME, DBUtils.PASSWORD);
            String query = "SELECT * FROM reservation WHERE customer LIKE ?";
            stmt = conn.prepareStatement(query);
            stmt.setString(1, "%" + searchTerm + "%");

            rs = stmt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String roomName = rs.getString("room_name");
                String roomId = rs.getString("room_id");
                Date checkInDate = rs.getDate("check_in_date");
                Date checkOutDate = rs.getDate("check_out_date");
                String checkedIn = rs.getString("check_in");
                String checkedOut = rs.getString("check_out");
                String customerName = rs.getString("customer");
                String customerId = rs.getString("customer_id");

                LoggedInController reservation = new LoggedInController(id, roomId, roomName, checkInDate, checkOutDate, checkedIn, checkedOut, customerName, customerId);

                reservation.setId(id);
                reservation.setRoomName(roomName);
                reservation.setCheckInDate(checkInDate);
                reservation.setCheckOutDate(checkOutDate);
                reservation.setChecked_in(checkedIn);
                reservation.setChecked_out(checkedOut);
                reservation.setCustomerName(customerName);
                reservation.setCustomerId(customerName);

                filteredReservations.add(reservation);

            }
        } finally {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        }

        return filteredReservations;

    }

    private List<LoggedInController> filterReservationsByDate(LocalDate fromDate, LocalDate toDate) throws SQLException {
        List<LoggedInController> filteredReservations = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DriverManager.getConnection(DBUtils.DATABASE_CONNECTION, DBUtils.USERNAME, DBUtils.PASSWORD);
            String query = "SELECT * FROM reservation WHERE check_in_date BETWEEN ? AND ?";
            stmt = conn.prepareStatement(query);
            stmt.setDate(1, java.sql.Date.valueOf(fromDate));
            stmt.setDate(2, java.sql.Date.valueOf(toDate));

            rs = stmt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String roomName = rs.getString("room_name");
                String roomId = rs.getString("room_id");
                Date checkInDate = rs.getDate("check_in_date");
                Date checkOutDate = rs.getDate("check_out_date");
                String checkedIn = rs.getString("check_in");
                String checkedOut = rs.getString("check_out");
                String customerName = rs.getString("customer");
                String customerId = rs.getString("customer_id");
                LoggedInController reservation = new LoggedInController(id, roomId, roomName, checkInDate, checkOutDate, checkedIn, checkedOut, customerId, customerName);

                reservation.setId(id);
                reservation.setRoomName(roomName);
                reservation.setCheckInDate(checkInDate);
                reservation.setCheckOutDate(checkOutDate);
                reservation.setChecked_in(checkedIn);
                reservation.setChecked_out(checkedOut);
                reservation.setCustomerName(customerName);
                reservation.setCustomerId(customerName);

                filteredReservations.add(reservation);
            }
        } finally {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        }

        return filteredReservations;
    }

    private void handleSearchReservationsByNameButtonClick(ActionEvent event) {
        String searchTerm = tf_search.getText().trim();
        if (!searchTerm.isEmpty()) {
            try {
                List<LoggedInController> filteredReservations = filterReservationsByName(searchTerm);
                ObservableList<LoggedInController> filteredList = FXCollections.observableArrayList(filteredReservations);
                table_logged.setItems(filteredList);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            table_logged.setItems(reservationList);

        }
    }

    private void handleSearchReservationsByDateButtonClick(ActionEvent event) {
        LocalDate fromDate = dp_from.getValue();
        LocalDate toDate = dp_to.getValue();
        if (fromDate != null && toDate != null) {
            try {
                List<LoggedInController> filteredReservations = filterReservationsByDate(fromDate, toDate);
                ObservableList<LoggedInController> filteredList = FXCollections.observableArrayList(filteredReservations);
                table_logged.setItems(filteredList);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText(null);
            alert.setContentText("Please select both 'From' and 'To' dates.");
            alert.showAndWait();
        }
    }

    private void handleSearchButtonClick(ActionEvent event) {
        if (tf_search.getText() != null && !tf_search.getText().isEmpty()) {

            handleSearchReservationsByNameButtonClick(event);
        } else if (dp_from.getValue() != null && dp_to.getValue() != null) {

            handleSearchReservationsByDateButtonClick(event);
        } else {

            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText(null);
            alert.setContentText("Please enter a search term or select both 'From' and 'To' dates.");
            alert.showAndWait();
        }
    }

    public void updateCheckInCheckOutStatus(LoggedInController reservation) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = DriverManager.getConnection(DBUtils.DATABASE_CONNECTION, DBUtils.USERNAME, DBUtils.PASSWORD);
            String query = "UPDATE reservation SET check_in = ?, check_out = ? WHERE id = ?";
            stmt = conn.prepareStatement(query);
            stmt.setString(1, reservation.getChecked_in());
            stmt.setString(2, reservation.getChecked_out());
            stmt.setInt(3, reservation.getId());
            stmt.executeUpdate();
        } finally {
            if (stmt != null) {
                stmt.close();
            }
            if (conn != null) {
                conn.close();
            }
        }
    }

    private void loadDataFromDatabase() {
        try {
            reservationList.clear();
            reservationList.addAll(DBUtils.getReservationFromDatabase());
            for (LoggedInController reservation : reservationList) {
                String fullName = DBUtils.getCustomerNameById(reservation.getCustomerId());
                reservation.setCustomerName(fullName);

                String roomName = DBUtils.getRoomNameById(reservation.getRoomId());
                reservation.setRoomName(roomName);
            }

            table_logged.setItems(reservationList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            reservationList.clear();
            reservationList.addAll(DBUtils.getReservationFromDatabase());

            LocalDate today = LocalDate.now();

            for (LoggedInController reservation : reservationList) {
                java.sql.Date checkInSqlDate = (java.sql.Date) reservation.getCheckInDate();
                java.sql.Date checkOutSqlDate = (java.sql.Date) reservation.getCheckOutDate();

                java.util.Date checkInUtilDate = new java.util.Date(checkInSqlDate.getTime());
                java.util.Date checkOutUtilDate = new java.util.Date(checkOutSqlDate.getTime());

                LocalDate checkInLocalDate = checkInUtilDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                LocalDate checkOutLocalDate = checkOutUtilDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

                String checkedIn = (today.isAfter(checkInLocalDate) || today.equals(checkInLocalDate)) ? "YES" : "NO";

                String checkedOut = (today.isAfter(checkOutLocalDate) || today.equals(checkOutLocalDate)) ? "YES" : "NO";
                if (checkedIn == null || checkedIn.isEmpty()) {
                    checkedIn = "NO";
                }
                if (checkedOut == null || checkedOut.isEmpty()) {
                    checkedOut = "NO";
                }
                reservation.setChecked_in(checkedIn);
                reservation.setChecked_out(checkedOut);
                updateCheckInCheckOutStatus(reservation);
            }

            table_logged.setItems(reservationList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void setUserInformation(String username) {
        label_welcome.setText("Welcome" + " " + username + "!");
    }
}


