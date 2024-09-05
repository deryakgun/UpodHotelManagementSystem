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
import java.util.ResourceBundle;

public class Services implements Initializable {
    private  int id;
    private String  servicesName;

    public Services(){}

    public Services(int id, String servicesName) {
        this.id = id;
        this.servicesName = servicesName;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    public String getServicesName() {
        return servicesName;
    }
    public void setServicesName(String servicesName) {
        this.servicesName = servicesName;
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
    private Button button_newservice;
    @FXML
    private Button button_delete;
    @FXML
    private Button button_edit;
    @FXML
    private Button button_main;
    @FXML
    private TableView<Services> table_services;
    @FXML
    private TableColumn<Services, Integer> tc_servicesid;
    @FXML
    private TableColumn<Services, String> tc_servicesname;

    @FXML
    private ObservableList<Services> serviceList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        button_newservice.setOnAction(event -> DBUtils.changeScene(event, "new-service.fxml", "Service", null));
        button_logout.setOnAction(event -> DBUtils.changeScene(event, "sign-in.fxml", "Log in", null));
        button_rooms.setOnAction(event -> DBUtils.changeScene(event, "room.fxml", "Room", null));
        button_features.setOnAction(event -> DBUtils.changeScene(event, "feature.fxml", "Feature", null));
        button_customer.setOnAction(event -> DBUtils.changeScene(event, "customer.fxml", "Customer", null));
        button_service.setOnAction(event -> DBUtils.changeScene(event, "services.fxml", "Services", null));
        button_reservationservices.setOnAction(event -> DBUtils.changeScene(event, "reservation-services.fxml", "Reservation Services", null));
        button_delete.setOnAction(event -> DBUtils.changeScene(event, "delete-service.fxml", "DELETE", null));
        button_main.setOnAction(event -> DBUtils.changeScene(event, "logged-ın.fxml", "LoggedInController", null));
        button_edit.setOnAction(event -> DBUtils.changeScene(event, "edit-service.fxml", "EDIT", null));

        initializeTableColumns();
        loadDataFromDatabase();
    }
    private void initializeTableColumns() {
        tc_servicesid = new TableColumn<>("ID");
        tc_servicesname = new TableColumn<>("Service Name");

        tc_servicesid.setCellValueFactory(new PropertyValueFactory<>("id"));
        tc_servicesname.setCellValueFactory(new PropertyValueFactory<>("servicesName"));


        table_services.getColumns().clear();
        table_services.getColumns().addAll(tc_servicesid, tc_servicesname);


    }
    private void loadDataFromDatabase() {
        try {
            serviceList.clear();
            serviceList.addAll(DBUtils.getServicesFromDatabase());
            table_services.setItems(serviceList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
