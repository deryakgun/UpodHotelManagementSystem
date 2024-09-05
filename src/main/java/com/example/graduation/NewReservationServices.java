package com.example.graduation;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class NewReservationServices implements Initializable {
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
    private SplitMenuButton smb_rid;
    @FXML
    private SplitMenuButton smb_servicename;
    @FXML
    private TextField tf_newquantity;
    @FXML
    private TextField tf_newunitprice;
    @FXML
    private Button button_reservationservicesave;

    @FXML
    private TableView<ReservationServices> table_reservationservices;

    ObservableList<ReservationServices> reservationServicesList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        table_reservationservices = new TableView<>();
        table_reservationservices = new TableView<>();
        button_logout.setOnAction(event -> DBUtils.changeScene(event, "sign-in.fxml", "Log in", null));
        button_rooms.setOnAction(event -> DBUtils.changeScene(event, "room.fxml", "Room", null));
        button_features.setOnAction(event -> DBUtils.changeScene(event, "feature.fxml", "Feature", null));
        button_customer.setOnAction(event -> DBUtils.changeScene(event, "customer.fxml", "Customer", null));
        button_service.setOnAction(event -> DBUtils.changeScene(event, "services.fxml", "Services", null));
        button_reservationservices.setOnAction(event -> DBUtils.changeScene(event, "reservation-services.fxml", "Reservation Services", null));
        button_main.setOnAction(event -> DBUtils.changeScene(event, "logged-ın.fxml", "LoggedInController", null));

        smb_servicename.getItems().clear();
        smb_rid.getItems().clear();


        List<LoggedInController> reservationList = null;
        try {
            reservationList = DBUtils.getReservationFromDatabase();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        for (LoggedInController reservation : reservationList) {
            MenuItem menuItem = new MenuItem(String.valueOf(reservation.getId()));
            menuItem.setOnAction(event -> smb_rid.setText(menuItem.getText()));
            smb_rid.getItems().add(menuItem);
        }

        List<Services> serviceList = null;
        try {
            serviceList = DBUtils.getServicesFromDatabase();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        for (Services services : serviceList) {
            MenuItem menuItem = new MenuItem(String.valueOf(services.getServicesName()));
            menuItem.setOnAction(event -> smb_servicename.setText(menuItem.getText()));
            smb_servicename.getItems().add(menuItem);
        }

        button_reservationservicesave.setOnAction(event -> {
            try {
                int rs_id = Integer.parseInt(smb_rid.getText());
                String service_name = smb_servicename.getText();
                double unitPrice = Double.parseDouble(tf_newunitprice.getText());
                int quantity = Integer.parseInt(tf_newquantity.getText());


                DBUtils.addNewReservationServices(String.valueOf(rs_id), service_name, unitPrice, quantity);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
            loadDataToTable();
            DBUtils.showSuccessAlert("Reservsation Service", "Succsess", "Reservation Service Successfully.");

        });

    }

    private void loadDataToTable() {
        try {
            reservationServicesList.clear();
            reservationServicesList.addAll(DBUtils.getReservationServicesFromDatabase());
            table_reservationservices.setItems(reservationServicesList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
