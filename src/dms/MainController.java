package dms;

import javafx.animation.ScaleTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * JavaFX Controller for the Shipping Order Management GUI.
 * Handles UI interaction logic including CRUD operations and file loading.
 */
public class MainController {

    @FXML private TableView<ShippingOrder> orderTable;
    @FXML private TableColumn<ShippingOrder, Integer> orderIdColumn;
    @FXML private TableColumn<ShippingOrder, String> customerNameColumn;
    @FXML private TableColumn<ShippingOrder, String> shipperNameColumn;
    @FXML private TableColumn<ShippingOrder, Double> weightColumn;
    @FXML private TableColumn<ShippingOrder, Integer> distanceColumn;
    @FXML private TableColumn<ShippingOrder, Double> priceColumn;
    @FXML private Label statusLabel;
    @FXML private Label connectionStatusLabel;

    private ObservableList<ShippingOrder> orderList;
    private ShippingOrderManager shippingOrderManager;

    @FXML
    private void initialize() {
        orderIdColumn.setCellValueFactory(new PropertyValueFactory<>("orderId"));
        customerNameColumn.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        shipperNameColumn.setCellValueFactory(new PropertyValueFactory<>("shipperName"));
        weightColumn.setCellValueFactory(new PropertyValueFactory<>("weightInPounds"));
        distanceColumn.setCellValueFactory(new PropertyValueFactory<>("distanceInMiles"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("shippingCost"));

        // Format weight with "lb"
        weightColumn.setCellFactory(_ -> new TableCell<>() {
            @Override protected void updateItem(Double weight, boolean empty) {
                super.updateItem(weight, empty);
                setText(empty || weight == null ? null : String.format("%.1f lb", weight));
            }
        });

        // Format distance with "mi"
        distanceColumn.setCellFactory(_ -> new TableCell<>() {
            @Override protected void updateItem(Integer distance, boolean empty) {
                super.updateItem(distance, empty);
                setText(empty || distance == null ? null : distance + " mi");
            }
        });

        // Format price with "$"
        priceColumn.setCellFactory(_ -> new TableCell<>() {
            @Override protected void updateItem(Double price, boolean empty) {
                super.updateItem(price, empty);
                setText(empty || price == null ? null : String.format("$%.2f", price));
            }
        });

        shippingOrderManager = new ShippingOrderManager();
        orderList = FXCollections.observableArrayList(shippingOrderManager.getAllOrders());
        orderTable.setItems(orderList);
        setStatus("Orders loaded from database.");
        setConnectionStatus();
    }

    private void setConnectionStatus() {
        try {
            Connection conn = DBConnectionManager.getInstance().getConnection();
            if (conn != null && !conn.isClosed() && conn.isValid(2)) {
                connectionStatusLabel.setText("🟢 Connected");
                connectionStatusLabel.setStyle("-fx-text-fill: green;");
            } else {
                connectionStatusLabel.setText("🔴 Not connected");
                connectionStatusLabel.setStyle("-fx-text-fill: red;");
            }
        } catch (SQLException e) {
            connectionStatusLabel.setText("🔴 Connection error");
            connectionStatusLabel.setStyle("-fx-text-fill: red;");
        }
    }

    @FXML
    private void handleAddOrder() {
        String customerName = promptValidName("Enter Customer Name:");
        if (customerName == null) return;

        String shipperName = promptValidName("Enter Shipper Name:");
        if (shipperName == null) return;

        double weight = promptDouble("Enter Weight (1–150 lb):", 1, 150);
        if (weight == -1) return;

        int distance = promptInt("Enter Distance (1–3000 mi):", 1, 3000);
        if (distance == -1) return;

        boolean added = shippingOrderManager.addOrder(customerName, shipperName, weight, distance);
        if (added) {
            orderList.setAll(shippingOrderManager.getAllOrders());
            setStatus("Order added.");
        } else {
            setStatus("Failed to add order.");
        }
        setConnectionStatus();
    }

    @FXML
    private void handleUpdateOrder() {
        var selected = orderTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            setStatus("Select an order to update.");
            return;
        }

        double newWeight = promptDouble("Update Weight (1–150 lb):", 1, 150);
        if (newWeight == -1) return;

        int newDistance = promptInt("Update Distance (1–3000 mi):", 1, 3000);
        if (newDistance == -1) return;

        boolean updated = shippingOrderManager.updateOrder(selected.getOrderId(), newWeight, newDistance);
        if (updated) {
            orderList.setAll(shippingOrderManager.getAllOrders());
            setStatus("Order updated.");
        } else {
            setStatus("Update failed.");
        }
        setConnectionStatus();
    }

    @FXML
    private void handleDeleteOrder() {
        var selected = orderTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            setStatus("Select an order to delete.");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Order");
        alert.setHeaderText("Are you sure you want to delete this order?");
        alert.setContentText("Order ID: " + selected.getOrderId());

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                boolean deleted = shippingOrderManager.deleteOrder(selected.getOrderId());
                if (deleted) {
                    orderList.setAll(shippingOrderManager.getAllOrders());
                    setStatus("Order deleted.");
                } else {
                    setStatus("Delete failed.");
                }
                setConnectionStatus();
            } else {
                setStatus("Delete canceled.");
            }
        });
    }

    @FXML
    private void handleLoadFile() {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Select Order File");
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
        File file = chooser.showOpenDialog(new Stage());

        if (file != null) {
            shippingOrderManager.loadOrdersFromFile(file.getAbsolutePath());
            orderList.setAll(shippingOrderManager.getAllOrders());
            setStatus("Orders loaded from file.");
        } else {
            setStatus("No file selected.");
        }
        setConnectionStatus();
    }

    private int promptInt(String message, int min, int max) {
        while (true) {
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Input Required");
            dialog.setHeaderText(message);
            var result = dialog.showAndWait();
            if (result.isEmpty()) {
                setStatus("Canceled.");
                return -1;
            }
            try {
                int value = Integer.parseInt(result.get().trim());
                if (value < min || value > max) {
                    showValidationError("Value must be between " + min + " and " + max + ".");
                } else {
                    return value;
                }
            } catch (NumberFormatException e) {
                showValidationError("Input must be numeric.");
            }
        }
    }

    private double promptDouble(String message, double min, double max) {
        while (true) {
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Input Required");
            dialog.setHeaderText(message);
            var result = dialog.showAndWait();
            if (result.isEmpty()) {
                setStatus("Canceled.");
                return -1;
            }
            try {
                double value = Double.parseDouble(result.get().trim());
                if (value < min || value > max) {
                    showValidationError("Value must be between " + min + " and " + max + ".");
                } else {
                    return value;
                }
            } catch (NumberFormatException e) {
                showValidationError("Input must be numeric.");
            }
        }
    }

    private String promptValidName(String message) {
        while (true) {
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Input Required");
            dialog.setHeaderText(message);
            var result = dialog.showAndWait();

            if (result.isEmpty()) {
                setStatus("Canceled.");
                return null;
            }

            String name = result.get().trim();

            // Only allow letters and a single space between words
            if (name.isEmpty()) {
                showValidationError("Name cannot be blank.");
            } else if (name.length() > 30) {
                showValidationError("Name must be 30 characters or fewer.");
            } else if (!name.matches("^[A-Za-z]+( [A-Za-z]+)*$")) {
                showValidationError("Only letters and a single space allowed. No numbers or symbols.");
            } else {
                return name;
            }
        }
    }

    private void showValidationError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Invalid Input");
        alert.setHeaderText(message);
        alert.showAndWait();
    }

    @FXML
    private void handleExit() {
        try {
            Connection conn = DBConnectionManager.getInstance().getConnection();
            if (conn != null && !conn.isClosed()) {
                conn.close();
                System.out.println("Database connection closed.");
            }
        } catch (SQLException e) {
            System.err.println("Error closing connection: " + e.getMessage());
        }

        System.exit(0);
    }

    private void setStatus(String message) {
        statusLabel.setText(message);
        animateStatus();
    }

    private void animateStatus() {
        ScaleTransition anim = new ScaleTransition(Duration.millis(400), statusLabel);
        anim.setFromX(1.0);
        anim.setFromY(1.0);
        anim.setToX(2.0);
        anim.setToY(2.0);
        anim.setCycleCount(2);
        anim.setAutoReverse(true);
        anim.play();
    }
}
