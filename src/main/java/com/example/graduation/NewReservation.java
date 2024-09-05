package com.example.graduation;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.sql.SQLException;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.ResourceBundle;

public class NewReservation implements Initializable {
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
    private SplitMenuButton smb_nrroom;
    @FXML
    private SplitMenuButton smb_nrcustomer;

    @FXML
    private DatePicker dp_nrcheckındate;
    @FXML
    private DatePicker dp_nrcheckoutdate;
    @FXML
    private Button button_main;
    @FXML
    private Button button_nrsave;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        button_logout.setOnAction(event -> DBUtils.changeScene(event, "sign-in.fxml", "Log in", null));
        button_rooms.setOnAction(event -> DBUtils.changeScene(event, "room.fxml", "Room", null));
        button_features.setOnAction(event -> DBUtils.changeScene(event, "feature.fxml", "Feature", null));
        button_customer.setOnAction(event -> DBUtils.changeScene(event, "customer.fxml", "Customer", null));
        button_service.setOnAction(event -> DBUtils.changeScene(event, "services.fxml", "Services", null));
        button_reservationservices.setOnAction(event -> DBUtils.changeScene(event, "reservation-services.fxml", "Reservation Services", null));
        button_main.setOnAction(event -> DBUtils.changeScene(event, "logged-ın.fxml", "LoggedInController", null));

        smb_nrcustomer.getItems().clear();
       
        smb_nrroom.getItems().clear();

        List<Room> roomList = null;
        try {
            roomList = DBUtils.getRoomsFromDatabase();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        for (Room room : roomList) {
            MenuItem menuItem = new MenuItem(String.valueOf(room.getRoomName()));
            menuItem.setOnAction(event -> smb_nrroom.setText(menuItem.getText()));
            smb_nrroom.getItems().add(menuItem);
        }


        List<Customer> customerList = null;
        try {
            customerList = DBUtils.getCustomersFromDatabase();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        for (Customer customer : customerList) {
            MenuItem menuItem = new MenuItem(String.valueOf(customer.getFullName()));
            menuItem.setOnAction(event -> smb_nrcustomer.setText(menuItem.getText()));
            smb_nrcustomer.getItems().add(menuItem);
        }

        button_nrsave.setOnAction(event -> {
            try {
                String roomName = smb_nrroom.getText();
                String checkInDate = dp_nrcheckındate.getValue().toString();
                String checkOutDate = dp_nrcheckoutdate.getValue().toString();
                //String checkedIn = dp_nrcheckedın.getValue().toString();
               // String checkedOut = dp_nrcheckedout.getValue().toString();;
                String customerName = smb_nrcustomer.getText();

                if (dp_nrcheckındate.getValue() != null && dp_nrcheckoutdate.getValue() != null) {
                    long daysBetween = ChronoUnit.DAYS.between(dp_nrcheckındate.getValue(), dp_nrcheckoutdate.getValue());

                    if (daysBetween < 1) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Invalid Reservation");
                        alert.setHeaderText("Invalid Reservation Period");
                        alert.setContentText("Reservations must be at least 1 day long.");
                        alert.showAndWait();
                        return;
                    }
                DBUtils.addNewReservation(roomName, checkInDate, checkOutDate,customerName);
                DBUtils.showSuccessAlert("Reservation Created", "Succsess", "Reservation Created Successfully.");
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Missing Date");
                    alert.setHeaderText("Incomplete Reservation Information");
                    alert.setContentText("Please select both check-in and check-out dates.");
                    alert.showAndWait();
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        });

    }
}
