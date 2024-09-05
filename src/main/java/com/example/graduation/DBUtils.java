package com.example.graduation;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class DBUtils {

    public final static String DATABASE_CONNECTION = "jdbc:mysql://localhost:3306/upod_hotell";
    public final static String USERNAME = "root";
    public final static String PASSWORD = "Derya440java";

    public static void changeScene(ActionEvent event, String fxmlFile, String title, String username) {

        Parent root = null;
        try {
            if (username != null) {

                FXMLLoader loader = new FXMLLoader(DBUtils.class.getResource(fxmlFile));
                root = loader.load();

                LoggedInController loggedInController = loader.getController();
                loggedInController.setUserInformation(username);

            } else {
                root = FXMLLoader.load(DBUtils.class.getResource(fxmlFile));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setTitle(title);
        stage.setScene(new Scene(root, 695, 400));
        stage.show();


    }

    public static void signUpUser(ActionEvent event, String username, String password) {
        Connection connection = null;
        PreparedStatement psInsert = null;
        PreparedStatement psCheckUserExist = null;
        ResultSet resultSet = null;

        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/login_demo", "root", "Derya440java");
            psCheckUserExist = connection.prepareStatement("SELECT * FROM users WHERE username = ?");
            psCheckUserExist.setString(1, username);
            resultSet = psCheckUserExist.executeQuery();

            if (resultSet.isBeforeFirst()) {
                System.out.println("User already exist!");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("You can not use this username");
                alert.show();
            } else {
                psInsert = connection.prepareStatement("INSERT INTO users(username, password) VALUES (?, ?)");
                psInsert.setString(1, username);
                psInsert.setString(2, password);
                psInsert.executeUpdate();

                changeScene(event, "logged-ın.fxml", "Welcome-" + username, username);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (resultSet != null) ;
                {
                    resultSet.close();
                }
                if (psCheckUserExist != null) {
                    psCheckUserExist.close();
                }
                if (psInsert != null) {
                    psInsert.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }

    public static void logInUser(ActionEvent event, String username, String password) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/login_demo", "root", "Derya440java");
            preparedStatement = connection.prepareStatement("SELECT password FROM users WHERE username = ?");
            preparedStatement.setString(1, username);
            resultSet = preparedStatement.executeQuery();


            if (!resultSet.isBeforeFirst()) {
                System.out.println("User not found in the database");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Provided credentials are incorrect");
                alert.show();
            } else {
                while (resultSet.next()) {
                    String retrievedPassword = resultSet.getString("password");
                    if (retrievedPassword.equals(password)) {
                        changeScene(event, "logged-ın.fxml", "Welcome" + username, "  " + username);
                    } else {
                        System.out.println("Password did not match");
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setContentText("The information entered is incorrect");
                        alert.show();
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void showSuccessAlert(String title, String headerText, String contentText) {
        showAlert(Alert.AlertType.INFORMATION, title, headerText, contentText);
    }

    public static void showErrorAlert(String title, String headerText, String contentText) {
        showAlert(Alert.AlertType.ERROR, title, headerText, contentText);
    }

    private static void showAlert(Alert.AlertType alertType, String title, String headerText, String contentText) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.showAndWait();
    }


    //------------------------
    public static void addNewRoom(String roomName, String roomFeature, int capacity, int total, double price) {
        Connection connection = null;
        PreparedStatement psInsert = null;

        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/upod_hotell", "root", "Derya440java");
            psInsert = connection.prepareStatement("INSERT INTO room(room_name, room_feature, capacity, total, price) VALUES (?, ?, ?,?,?)");
            psInsert.setString(1, roomName);
            psInsert.setString(2, roomFeature);
            psInsert.setInt(3, capacity);
            psInsert.setInt(4, total);
            psInsert.setDouble(5, price);
            psInsert.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (psInsert != null) {
                    psInsert.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static List<Room> getRoomsFromDatabase() throws SQLException {
        List<Room> rooms = new ArrayList<>();
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/upod_hotell", "root", "Derya440java");
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM room");


            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String roomName = resultSet.getString("room_name");
                int capacity = resultSet.getInt("capacity");
                double price = resultSet.getDouble("price");
                int total = resultSet.getInt("total");
                String roomFeature = resultSet.getString("room_feature");

                Room room = new Room(id, roomName, capacity, price, total, roomFeature);
                rooms.add(room);
            }
        } finally {
            if (resultSet != null) resultSet.close();
            if (statement != null) statement.close();
            if (connection != null) connection.close();
        }

        return rooms;
    }

    public static int getReservedRoomCount(Connection connection, int roomId) throws SQLException {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        int reservedCount = 0;

        try {
            preparedStatement = connection.prepareStatement("SELECT COUNT(*) FROM reservation WHERE room_id = ?");
            preparedStatement.setInt(1, roomId);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                reservedCount = resultSet.getInt(1);
            }
        } finally {
            if (resultSet != null) resultSet.close();
            if (preparedStatement != null) preparedStatement.close();
        }

        return reservedCount;
    }

    public static String getRoomNameById(String roomId) {
        String roomName = null;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/upod_hotell", "root", "Derya440java");
            String database = "SELECT room_name FROM room WHERE id = ?";
            preparedStatement = connection.prepareStatement(database);
            preparedStatement.setString(1, roomId);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                roomName = resultSet.getString("room_name");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return roomName;
    }

    public static int deleteRoom(String roomName) throws SQLException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/upod_hotell", "root", "Derya440java");
            preparedStatement = connection.prepareStatement("DELETE FROM room WHERE room_name = ?");
            preparedStatement.setString(1, roomName);
            return preparedStatement.executeUpdate();
        } finally {
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
    }

    //------------------------
    public static void addNewFeature(String feature_name) {
        Connection connection = null;
        PreparedStatement psInsert = null;

        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/upod_hotell", "root", "Derya440java");
            psInsert = connection.prepareStatement("INSERT INTO feature(feature_name) VALUES (?)");
            psInsert.setString(1, feature_name);
            psInsert.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (psInsert != null) {
                    psInsert.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }

    public static List<Feature> getFeaturesFromDatabase() throws SQLException {
        List<Feature> features = new ArrayList<>();
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/upod_hotell", "root", "Derya440java");
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM feature");

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String featureName = resultSet.getString("feature_name");

                Feature feature = new Feature(id, featureName);
                features.add(feature);
            }
        } finally {
            if (resultSet != null) resultSet.close();
            if (statement != null) statement.close();
            if (connection != null) connection.close();
        }
        return features;
    }

    public static int deleteFeature(String featureName) throws SQLException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/upod_hotell", "root", "Derya440java");
            preparedStatement = connection.prepareStatement("DELETE FROM feature WHERE feature_name = ?");
            preparedStatement.setString(1, featureName);
            return preparedStatement.executeUpdate();
        } finally {
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
    }

    //--------------
    public static void addNewReservation(String roomName, String checkInDate, String checkOutDate,String customerName) {
        Connection connection = null;
        PreparedStatement psInsert = null;

        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/upod_hotell", "root", "Derya440java");
            psInsert = connection.prepareStatement("INSERT INTO reservation(room_name, check_in_date, check_out_date,customer) VALUES (?,?,?,?)");
            psInsert.setString(1, roomName);
            psInsert.setString(2, checkInDate);
            psInsert.setString(3, checkOutDate);
            //psInsert.setString(4, checkedIn);
           // psInsert.setString(5, checkedOut);
            psInsert.setString(4, customerName);
            psInsert.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (psInsert != null) {
                    psInsert.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }


    }
    //????????????????

    public static List<LoggedInController> getReservationFromDatabase() throws SQLException {
        List<LoggedInController> reservations = new ArrayList<>();
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/upod_hotell", "root", "Derya440java");
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM reservation");

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String room_id = resultSet.getString("room_id");
                String room = resultSet.getString("room_name");
                Date checkInDate = resultSet.getDate("check_in_date");
                Date checkOutDate = resultSet.getDate("check_out_date");
                //Date checkedIn = resultSet.getDate("checked_in");
                //Date checkedOut = resultSet.getDate("checked_out");
                String customer_id = resultSet.getString("customer_id");
                String customer = resultSet.getString("customer");
                String checkIn = resultSet.getString("check_in");
                String checkOut = resultSet.getString("check_out");

                LoggedInController reservation = new LoggedInController(id, room_id, room, checkInDate, checkOutDate, customer_id, customer, checkIn, checkOut);
                reservations.add(reservation);
            }
        } finally {
            if (resultSet != null) resultSet.close();
            if (statement != null) statement.close();
            if (connection != null) connection.close();
        }

        return reservations;
    }
    public static List<String> getReservationID() throws SQLException {
        List<String> reservationIDs = new ArrayList<>();
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            connection = DriverManager.getConnection(DATABASE_CONNECTION, USERNAME, PASSWORD);
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT id FROM reservation");

            while (resultSet.next()) {
                String id = resultSet.getString("id");
                reservationIDs.add(id);
            }
        } finally {
            if (resultSet != null) resultSet.close();
            if (statement != null) statement.close();
            if (connection != null) connection.close();
        }

        return reservationIDs;
    }

    public static int deleteReservation(String reservationID) throws SQLException {
        String deleteReservationQuery = "DELETE FROM reservation WHERE id = ?";
        try (Connection connection = DriverManager.getConnection(DATABASE_CONNECTION, USERNAME, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(deleteReservationQuery)) {
            preparedStatement.setString(1, reservationID);
            return preparedStatement.executeUpdate();
        }
    }


    public static int deleteReservationByCustomerId(int customerId) throws SQLException {
        String deleteReservationQuery = "DELETE FROM reservation WHERE customer_id = ?";
        try (Connection connection = DriverManager.getConnection(DATABASE_CONNECTION, USERNAME, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(deleteReservationQuery)) {
            preparedStatement.setInt(1, customerId);
            return preparedStatement.executeUpdate();
        }
    }

    public static int deleteReservationRoom(String roomName) throws SQLException {
        String deleteReservationQuery2 = "DELETE FROM reservation WHERE room_id IN (SELECT id FROM room WHERE room_name = ?)";
        try (Connection connection = DriverManager.getConnection(DATABASE_CONNECTION, USERNAME, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(deleteReservationQuery2)) {
            preparedStatement.setString(1, roomName);
            return preparedStatement.executeUpdate();
        }
    }

    public static int deleteReservationService(String serviceName) throws SQLException {
        String deleteReservationQuery3 = "DELETE FROM reservation_service WHERE service_id IN (SELECT id FROM service WHERE service_name = ?)";
        try (Connection connection = DriverManager.getConnection(DATABASE_CONNECTION, USERNAME, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(deleteReservationQuery3)) {
            preparedStatement.setString(1, serviceName);
            return preparedStatement.executeUpdate();
        }
    }

    public static int deleteReservationRService(String rServiceId) throws SQLException {
        String deleteReservationQuery4 = "DELETE FROM service WHERE service_name IN (SELECT id FROM reservation_service WHERE id = ?)";
        try (Connection connection = DriverManager.getConnection(DATABASE_CONNECTION, USERNAME, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(deleteReservationQuery4)) {
            preparedStatement.setString(1, rServiceId);
            return preparedStatement.executeUpdate();
        }
    }


    //----------------------
    public static void addNewCustomer(String fullName, String identityNumber, String phoneNumber, Date birthDate, String description) {
        Connection connection = null;
        PreparedStatement psInsert = null;

        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/upod_hotell", "root", "Derya440java");
            psInsert = connection.prepareStatement("INSERT INTO customer(full_name, identity_number, phone_number, birth_date, decription) VALUES (?, ?, ?, ?, ?)");
            psInsert.setString(1, fullName);
            psInsert.setString(2, identityNumber);
            psInsert.setString(3, phoneNumber);
            psInsert.setDate(4, new java.sql.Date(birthDate.getTime()));
            psInsert.setString(5, description);

            psInsert.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (psInsert != null) {
                    psInsert.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static List<Customer> getCustomersFromDatabase() throws SQLException {
        List<Customer> customers = new ArrayList<>();
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/upod_hotell", "root", "Derya440java");
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM customer");

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String fullName = resultSet.getString("full_name");
                String identityNumber = resultSet.getString("identity_number");
                String phoneNumber = resultSet.getString("phone_number");
                Date birhDate = resultSet.getDate("birth_date");
                String description = resultSet.getString("decription");


                Customer customer = new Customer(id, fullName, identityNumber, phoneNumber, birhDate, description);
                customers.add(customer);
            }
        } finally {
            if (resultSet != null) resultSet.close();
            if (statement != null) statement.close();
            if (connection != null) connection.close();
        }

        return customers;
    }

    public static String getCustomerNameById(String customerId) {
        String customerName = null;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/upod_hotell", "root", "Derya440java");
            String database = "SELECT full_name FROM customer WHERE id = ?";
            preparedStatement = connection.prepareStatement(database);
            preparedStatement.setString(1, customerId);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                customerName = resultSet.getString("full_name");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return customerName;
    }

    public static int deleteCustomer(String fullName) throws SQLException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/upod_hotell", "root", "Derya440java");
            preparedStatement = connection.prepareStatement("DELETE FROM customer WHERE full_name = ?");
            preparedStatement.setString(1, fullName);
            return preparedStatement.executeUpdate();
        } finally {
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
    }


    //-------------------------
    public static void addNewServices(String service_name) {
        Connection connection = null;
        PreparedStatement psInsert = null;

        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/upod_hotell", "root", "Derya440java");
            psInsert = connection.prepareStatement("INSERT INTO service(service_name) VALUES (?)");
            psInsert.setString(1, service_name);
            psInsert.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (psInsert != null) {
                    psInsert.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }

    public static List<Services> getServicesFromDatabase() throws SQLException {
        List<Services> services = new ArrayList<>();
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/upod_hotell", "root", "Derya440java");
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM service");

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String serviceName = resultSet.getString("service_name");

                Services service = new Services(id, serviceName);
                services.add(service);
            }
        } finally {
            if (resultSet != null) resultSet.close();
            if (statement != null) statement.close();
            if (connection != null) connection.close();
        }
        return services;
    }

    public static int deleteService(String serviceName) throws SQLException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/upod_hotell", "root", "Derya440java");
            preparedStatement = connection.prepareStatement("DELETE FROM service WHERE service_name = ?");
            preparedStatement.setString(1, serviceName);
            return preparedStatement.executeUpdate();
        } finally {
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
    }

    //---------------------------------------------------------------
    public static void addNewReservationServices(String rs_id, String service_name, double unitPrice, int quantity) {
        Connection connection = null;
        PreparedStatement psInsert = null;


        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/upod_hotell", "root", "Derya440java");

            psInsert = connection.prepareStatement("INSERT INTO reservation_service(reservation_id,service_name, unit_price, quantity) VALUES (?, ?, ?, ?)");
            psInsert.setString(1, rs_id);
            psInsert.setString(2, service_name);
            psInsert.setDouble(3, unitPrice);
            psInsert.setInt(4, quantity);
            psInsert.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (psInsert != null) {
                    psInsert.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static List<ReservationServices> getReservationServicesFromDatabase() throws SQLException {
        List<ReservationServices> reservationServicess = new ArrayList<>();
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/upod_hotell", "root", "Derya440java");
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM reservation_service");

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                int reservationId = resultSet.getInt("reservation_id");

                String serviceName = resultSet.getString("service_name");
                double unitPrice = resultSet.getDouble("unit_price");
                int quantity = resultSet.getInt("quantity");

                ReservationServices reservationServices = new ReservationServices(id, reservationId, serviceName, unitPrice, quantity);
                reservationServicess.add(reservationServices);
            }
        } finally {
            if (resultSet != null) resultSet.close();
            if (statement != null) statement.close();
            if (connection != null) connection.close();
        }

        return reservationServicess;
    }

    public static ObservableList<String> getReservationIDs() throws SQLException {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        ObservableList<String> reservationIDs = FXCollections.observableArrayList();

        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/upod_hotell", "root", "Derya440java");
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT DISTINCT reservation_id FROM reservation_service");

            while (resultSet.next()) {
                reservationIDs.add(resultSet.getString("reservation_id"));
            }
        } finally {
            if (resultSet != null) resultSet.close();
            if (statement != null) statement.close();
            if (connection != null) connection.close();
        }

        return reservationIDs;
    }

    public static ObservableList<String> getServiceNames() throws SQLException {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        ObservableList<String> serviceNames = FXCollections.observableArrayList();

        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/upod_hotell", "root", "Derya440java");
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT DISTINCT service_name FROM reservation_service");

            while (resultSet.next()) {
                serviceNames.add(resultSet.getString("service_name"));
            }
        } finally {
            if (resultSet != null) resultSet.close();
            if (statement != null) statement.close();
            if (connection != null) connection.close();
        }

        return serviceNames;
    }


}



