package com.example.graduation;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.SQLException;
import java.util.Date;
import java.util.ResourceBundle;

public class ReservationServices implements Initializable {

    //id, reservationId, serviceId, serviceName, unitPrice, quantity
    private int id;
    private int reservationId;
    private String serviceName;
    private Double unitPrice;
    private int quantity;

    public ReservationServices(){}

    public ReservationServices(int id, int reservationId, String serviceName, Double unitPrice, int quantity){
        this.id = id;
        this.reservationId = reservationId;
        this.serviceName = serviceName;
        this.unitPrice = unitPrice;
        this.quantity = quantity;

    }
    public int getId() {
        return id;
    }
    public void setId(int id) {this.id = id;}
    public int getReservationId() {return reservationId;}
    public void setReservationId(int reservationId) {this.reservationId = reservationId;}
    public String getServiceName() {return serviceName;}
    public void setServiceName() {this.serviceName =serviceName;}
    public Double getUnitPrice() {return unitPrice;}
    public void setUnitPrice(Double unitPrice) {this.unitPrice = unitPrice;}
    public int getQuantity() {return quantity;}
    public void setQuantity(int quantity) {this.quantity = quantity;}

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
    private Button button_newreservationservice;
    @FXML
    private Button button_delete;
    @FXML
    private Button button_edit;
    @FXML
    private Button button_main;

    @FXML
    private TableView<ReservationServices> table_reservationservices;
    @FXML
    private TableColumn<ReservationServices, Integer> tc_rsid;
    @FXML
    private TableColumn<ReservationServices, Integer> tc_rsreservationid;
    @FXML
    private TableColumn<ReservationServices, String> tc_rsservicename;
    @FXML
    private TableColumn<ReservationServices, Double> tc_rsunitprice;
    @FXML
    private TableColumn<ReservationServices, Integer> tc_rsquantity;
    @FXML
    private ObservableList<ReservationServices> reservationServiceList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {



        button_newreservationservice.setOnAction(event -> DBUtils.changeScene(event, "new-reservationservices.fxml", "Customer", null));
        button_logout.setOnAction(event -> DBUtils.changeScene(event, "sign-in.fxml", "Log in", null));
        button_rooms.setOnAction(event -> DBUtils.changeScene(event, "room.fxml", "Room", null));
        button_features.setOnAction(event -> DBUtils.changeScene(event, "feature.fxml", "Feature", null));
        button_customer.setOnAction(event -> DBUtils.changeScene(event, "customer.fxml", "Customer", null));
        button_service.setOnAction(event -> DBUtils.changeScene(event, "services.fxml", "Services", null));
        button_reservationservices.setOnAction(event -> DBUtils.changeScene(event, "reservation-services.fxml", "Reservation Services", null));
        button_delete.setOnAction(event -> DBUtils.changeScene(event, "delete-reservationservice.fxml", "DELETE", null));
        button_main.setOnAction(event -> DBUtils.changeScene(event, "logged-ın.fxml", "LoggedInController", null));
        button_edit.setOnAction(event -> DBUtils.changeScene(event, "edit-reservationservice.fxml", "EDIT", null));

        initializeTableColumns();
        loadDataFromDatabase();

    }
    private void initializeTableColumns() {
        tc_rsid = new TableColumn<>("ID");
        tc_rsreservationid = new TableColumn<>("Reservation ID");
        tc_rsservicename = new TableColumn<>("Service Name");
        tc_rsunitprice = new TableColumn<>("Unit Price");
        tc_rsquantity = new TableColumn<>("Quantity");

        tc_rsid.setCellValueFactory(new PropertyValueFactory<>("id"));
        tc_rsreservationid.setCellValueFactory(new PropertyValueFactory<>("reservationId"));
        tc_rsservicename.setCellValueFactory(new PropertyValueFactory<>("ServiceName"));
        tc_rsunitprice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        tc_rsquantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        table_reservationservices.getColumns().clear();
        table_reservationservices.getColumns().addAll(tc_rsid, tc_rsreservationid, tc_rsservicename, tc_rsunitprice, tc_rsquantity);

    }
    private void loadDataFromDatabase() {
        try {
            reservationServiceList.clear();
            reservationServiceList.addAll(DBUtils.getReservationServicesFromDatabase());
            table_reservationservices.setItems(reservationServiceList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
