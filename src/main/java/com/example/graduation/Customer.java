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

public class Customer implements Initializable {
    private int customerId;
    private String fullName;
    private String identityNumber;
    private String phoneNumber;
    private Date birthDate;
    private String description;

    public Customer(){}

    public Customer(int customerId, String fullName, String identityNumber, String phoneNumber, Date birthDate, String description){
        this.customerId = customerId;
        this.fullName = fullName;
        this.identityNumber = identityNumber;
        this.phoneNumber = phoneNumber;
        this.birthDate = birthDate;
        this.description = description;

    }
    public Customer(int customerId, String fullName){
        this.customerId = customerId;
        this.fullName = fullName;

    }


    public int getCustomerId() {
        return customerId;
    }
    public void setCustomerId(int customerId) {this.customerId = customerId;}
    public String getFullName() {
        return fullName;
    }
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
    public String getIdentityNumber() {return identityNumber;}
    public void setIdentityNumber(String identityNumber) {this.identityNumber = identityNumber;}
    public String getPhoneNumber() {
        return phoneNumber;
    }
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    public Date getBirthDate() {
        return birthDate;
    }
    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

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
    private Button button_deletecustomer;
    @FXML
    private Button button_edit;

    @FXML
    private Button button_newcustomer;
    @FXML
    private TableView<Customer> table_customer;
    @FXML
    private TableColumn<Customer, Integer> tc_id;
    @FXML
    private TableColumn<Customer, String> tc_fullname;
    @FXML
    private TableColumn<Customer, String> tc_identitynumber;
    @FXML
    private TableColumn<Customer, String> tc_phonenumber;
    @FXML
    private TableColumn<Customer, Date> tc_birthdate;
    @FXML
    private TableColumn<Customer, String> tc_description;
    @FXML
    private Button button_main;
    @FXML
    private ObservableList<Customer> customerList = FXCollections.observableArrayList();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        button_rooms.setOnAction(event -> DBUtils.changeScene(event, "room.fxml", "Room", null));
        button_features.setOnAction(event -> DBUtils.changeScene(event, "feature.fxml", "Feature", null));
        button_customer.setOnAction(event -> DBUtils.changeScene(event, "customer.fxml", "Customer", null));
        button_service.setOnAction(event -> DBUtils.changeScene(event, "services.fxml", "Services", null));
        button_reservationservices.setOnAction(event -> DBUtils.changeScene(event, "reservation-services.fxml", "Reservation Services", null));
        button_logout.setOnAction(event -> DBUtils.changeScene(event, "sign-in.fxml", "Log in", null));
        button_deletecustomer.setOnAction(event -> DBUtils.changeScene(event, "delete-customer.fxml", "DELETE", null));
        button_main.setOnAction(event -> DBUtils.changeScene(event, "logged-ın.fxml", "LoggedInController", null));
        button_newcustomer.setOnAction(event -> DBUtils.changeScene(event, "new-customer.fxml", "Customer", null));
        button_edit.setOnAction(event -> DBUtils.changeScene(event, "edit-customer.fxml", "EDIT", null));
        initializeTableColumns();
        loadDataFromDatabase();
    }
    private void initializeTableColumns() {
        tc_id = new TableColumn<>("ID");
        tc_fullname = new TableColumn<>("Full Name");
        tc_identitynumber = new TableColumn<>("Identity Number");
        tc_phonenumber = new TableColumn<>("Phone Number");
        tc_birthdate = new TableColumn<>("Birth Date");
        tc_description = new TableColumn<>("Description");

        tc_id.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        tc_fullname.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        tc_identitynumber.setCellValueFactory(new PropertyValueFactory<>("identityNumber"));
        tc_phonenumber.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        tc_birthdate.setCellValueFactory(new PropertyValueFactory<>("birthDate"));
        tc_description.setCellValueFactory(new PropertyValueFactory<>("description"));

        table_customer.getColumns().clear();
        table_customer.getColumns().addAll(tc_id, tc_fullname, tc_identitynumber, tc_phonenumber, tc_birthdate, tc_description);


    }
    private void loadDataFromDatabase() {
        try {
            customerList.clear();
            customerList.addAll(DBUtils.getCustomersFromDatabase());
            table_customer.setItems(customerList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
