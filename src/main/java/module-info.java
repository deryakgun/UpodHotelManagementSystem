module com.example.graduation {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.dlsc.formsfx;
    requires java.sql;
    requires mysql.connector.j;

    opens com.example.graduation to javafx.fxml;
    exports com.example.graduation;
    opens com.example.graduation.controllers to javafx.fxml;
    exports com.example.graduation.controllers;

}