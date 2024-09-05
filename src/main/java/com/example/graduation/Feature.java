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

public class Feature implements Initializable {
    private int id;
    private String featureName;
    private boolean selected;

    public Feature(){}

    public Feature(int id, String featureName) {
        this.id =id;
        this.featureName = featureName;
        this.selected = false;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFeatureName() {
        return featureName;
    }
    public boolean getSelected() {
        return selected;
    }
    public void setFeatureName(String featureName) {
        this.featureName = featureName;
    }


    public void setSelected(boolean selected) {
        this.selected = selected;
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
    private Button button_delete;
    @FXML
    private Button button_edit;
    @FXML
    private Button button_newfeature;
    @FXML
    private Button button_main;
    @FXML
    private TableView<Feature> table_feature;
    @FXML
    private TableColumn<Feature, Integer> tc_featureid;
    @FXML
    private TableColumn<Feature, String> tc_feturename;
    @FXML
    private ObservableList<Feature> featureList = FXCollections.observableArrayList();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        button_rooms.setOnAction(event -> DBUtils.changeScene(event, "room.fxml", "Room", null));
        button_features.setOnAction(event -> DBUtils.changeScene(event, "feature.fxml", "Feature", null));
        button_customer.setOnAction(event -> DBUtils.changeScene(event, "customer.fxml", "Customer", null));
        button_service.setOnAction(event -> DBUtils.changeScene(event, "services.fxml", "Services", null));
        button_reservationservices.setOnAction(event -> DBUtils.changeScene(event, "reservation-services.fxml", "Reservation Services", null));
        button_logout.setOnAction(event -> DBUtils.changeScene(event, "sign-in.fxml", "Log in", null));
        button_delete.setOnAction(event -> DBUtils.changeScene(event, "delete-feature.fxml", "DELETE", null));
        button_newfeature.setOnAction(event -> DBUtils.changeScene(event, "new-feature.fxml", "Feature", null));
        button_main.setOnAction(event -> DBUtils.changeScene(event, "logged-ın.fxml", "LoggedInController", null));
        button_edit.setOnAction(event -> DBUtils.changeScene(event, "edit-feature.fxml", "EDIT", null));

        initializeTableColumns();
        loadDataFromDatabase();
    }

    private void initializeTableColumns() {
        tc_featureid = new TableColumn<>("ID");
        tc_feturename = new TableColumn<>("Feature Name");

        tc_featureid.setCellValueFactory(new PropertyValueFactory<>("id"));
        tc_feturename.setCellValueFactory(new PropertyValueFactory<>("featureName"));

        table_feature.getColumns().clear();
        table_feature.getColumns().addAll(tc_featureid, tc_feturename);


    }
    private void loadDataFromDatabase() {
        try {
            featureList.clear();
            featureList.addAll(DBUtils.getFeaturesFromDatabase());
            table_feature.setItems(featureList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
