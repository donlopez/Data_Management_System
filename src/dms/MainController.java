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

/**
 * Controls the main GUI and handles user interactions for
 * adding, updating, deleting, and loading shipping orders.
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

    private ObservableList<ShippingOrder> orderList;
    private ShippingOrderManager shippingOrderManager;

    /**
     * Initializes the controller after its root element has been processed.
     */
    @FXML
    private void initialize() {
        orderIdColumn.setCellValueFactory(new PropertyValueFactory<>("orderID"));
        customerNameColumn.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        shipperNameColumn.setCellValueFactory(new PropertyValueFactory<>("shipperName"));
        weightColumn.setCellValueFactory(new PropertyValueFactory<>("weightInPounds"));
        distanceColumn.setCellValueFactory(new PropertyValueFactory<>("distanceInMiles"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("shippingCost"));

        // custom formatters for units
        weightColumn.setCellFactory(_ -> new TableCell<>() {
            @Override
            protected void updateItem(Double weight, boolean empty) {
                super.updateItem(weight, empty);
                if (empty || weight == null) {
                    setText(null);
                } else {
                    setText(String.format("%.1f lb", weight));
                }
            }
        });

        distanceColumn.setCellFactory(_ -> new TableCell<>() {
            @Override
            protected void updateItem(Integer distance, boolean empty) {
                super.updateItem(distance, empty);
                if (empty || distance == null) {
                    setText(null);
                } else {
                    setText(distance + " mi");
                }
            }
        });

        priceColumn.setCellFactory(_ -> new TableCell<>() {
            @Override
            protected void updateItem(Double price, boolean empty) {
                super.updateItem(price, empty);
                if (empty || price == null) {
                    setText(null);
                } else {
                    setText(String.format("$%.2f", price));
                }
            }
        });

        shippingOrderManager = new ShippingOrderManager();
        orderList = FXCollections.observableArrayList(shippingOrderManager.getOrders());
        orderTable.setItems(orderList);

        setStatus("Welcome to Shipping Order Manager");

        orderTable.getSelectionModel().clearSelection();
        orderTable.getFocusModel().focus(-1);
    }

    /**
     * Handles adding a new order, either manually or by loading a file.
     */
    @FXML
    private void handleAddOrder() {
        Alert choiceAlert = new Alert(Alert.AlertType.CONFIRMATION);
        choiceAlert.setTitle("Add Order");
        choiceAlert.setHeaderText("Choose how to add an order:");
        choiceAlert.setContentText("Would you like to manually add a new order, or load from a file?");

        ButtonType manualButton = new ButtonType("Manual Entry");
        ButtonType fileButton = new ButtonType("Load from File");
        ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

        choiceAlert.getButtonTypes().setAll(manualButton, fileButton, cancelButton);

        choiceAlert.showAndWait().ifPresent(response -> {
            if (response == manualButton) {
                showAddOrderDialog();
            } else if (response == fileButton) {
                handleLoadFile();
            } else {
                setStatus("Add order canceled.");
            }
        });
    }

    /**
     * Shows dialogs to manually add a new order.
     */
    private void showAddOrderDialog() {
        String customerName;
        String shipperName;
        double weight;
        int distance;

        // customer name
        while (true) {
            var dialog = new TextInputDialog();
            dialog.setTitle("Add Order");
            dialog.setHeaderText("Enter customer name (letters/spaces/hyphens only, max 30 chars):");
            dialog.setContentText("Customer name:");
            var result = dialog.showAndWait();
            if (result.isEmpty()) {
                setStatus("Add order canceled.");
                return;
            }
            customerName = result.get().trim();
            if (customerName.isBlank()) {
                showValidationError("Customer name cannot be empty.");
            } else if (!customerName.matches("[a-zA-Z\\s\\-]+")) {
                showValidationError("Customer name must contain only letters, spaces, or hyphens.");
            } else if (customerName.length() > 30) {
                showValidationError("Customer name must be 30 characters or fewer.");
            } else break;
        }

        // shipper name
        while (true) {
            var dialog = new TextInputDialog();
            dialog.setTitle("Add Order");
            dialog.setHeaderText("Enter shipper name (letters/spaces/hyphens only, max 30 chars):");
            dialog.setContentText("Shipper name:");
            var result = dialog.showAndWait();
            if (result.isEmpty()) {
                setStatus("Add order canceled.");
                return;
            }
            shipperName = result.get().trim();
            if (shipperName.isBlank()) {
                showValidationError("Shipper name cannot be empty.");
            } else if (!shipperName.matches("[a-zA-Z\\s\\-]+")) {
                showValidationError("Shipper name must contain only letters, spaces, or hyphens.");
            } else if (shipperName.length() > 30) {
                showValidationError("Shipper name must be 30 characters or fewer.");
            } else break;
        }

        // weight
        while (true) {
            var dialog = new TextInputDialog();
            dialog.setTitle("Add Order");
            dialog.setHeaderText("Enter weight (1–150 pounds):");
            dialog.setContentText("Weight in pounds:");
            var result = dialog.showAndWait();
            if (result.isEmpty()) {
                setStatus("Add order canceled.");
                return;
            }
            try {
                weight = Double.parseDouble(result.get().trim());
                if (weight <= 0 || weight > 150) {
                    showValidationError("Weight must be between 1 and 150 pounds.");
                } else break;
            } catch (NumberFormatException e) {
                showValidationError("Weight must be numeric.");
            }
        }

        // distance
        while (true) {
            var dialog = new TextInputDialog();
            dialog.setTitle("Add Order");
            dialog.setHeaderText("Enter distance (1–3000 miles):");
            dialog.setContentText("Distance in miles:");
            var result = dialog.showAndWait();
            if (result.isEmpty()) {
                setStatus("Add order canceled.");
                return;
            }
            try {
                distance = Integer.parseInt(result.get().trim());
                if (distance < 1 || distance > 3000) {
                    showValidationError("Distance must be between 1 and 3000 miles.");
                } else break;
            } catch (NumberFormatException e) {
                showValidationError("Distance must be numeric.");
            }
        }

        shippingOrderManager.addOrder(customerName, shipperName, weight, distance);
        orderList.setAll(shippingOrderManager.getOrders());
        setStatus("Order added successfully.");
    }

    /**
     * Handles updating the selected order.
     */
    @FXML
    private void handleUpdateOrder() {
        var selected = orderTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            setStatus("Please select an order to update.");
            return;
        }

        double newWeight = selected.getWeightInPounds();
        int newDistance = selected.getDistanceInMiles();

        // weight
        while (true) {
            var dialog = new TextInputDialog(String.valueOf(newWeight));
            dialog.setTitle("Update Order");
            dialog.setHeaderText("Update weight (1–150 pounds):");
            dialog.setContentText("New weight:");
            var result = dialog.showAndWait();
            if (result.isEmpty()) {
                setStatus("Update canceled.");
                return;
            }
            try {
                newWeight = Double.parseDouble(result.get().trim());
                if (newWeight <= 0 || newWeight > 150) {
                    showValidationError("Weight must be between 1 and 150 pounds.");
                } else break;
            } catch (NumberFormatException e) {
                showValidationError("Weight must be numeric.");
            }
        }

        // distance
        while (true) {
            var dialog = new TextInputDialog(String.valueOf(newDistance));
            dialog.setTitle("Update Order");
            dialog.setHeaderText("Update distance (1–3000 miles):");
            dialog.setContentText("New distance:");
            var result = dialog.showAndWait();
            if (result.isEmpty()) {
                setStatus("Update canceled.");
                return;
            }
            try {
                newDistance = Integer.parseInt(result.get().trim());
                if (newDistance < 1 || newDistance > 3000) {
                    showValidationError("Distance must be between 1 and 3000 miles.");
                } else break;
            } catch (NumberFormatException e) {
                showValidationError("Distance must be numeric.");
            }
        }

        selected.setWeightInPounds(newWeight);
        selected.setDistanceInMiles(newDistance);
        orderList.setAll(shippingOrderManager.getOrders());
        setStatus("Order ID " + selected.getOrderID() + " updated successfully.");
    }

    /**
     * Displays a reusable validation alert.
     */
    private void showValidationError(String message) {
        var alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Invalid Input");
        alert.setHeaderText(message);
        alert.showAndWait();
    }

    /**
     * Handles loading orders from a text file.
     */
    @FXML
    private void handleLoadFile() {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Select Orders File");
        File file = chooser.showOpenDialog(new Stage());

        if (file != null) {
            shippingOrderManager.loadOrdersFromFile(file.getAbsolutePath());
            orderList.setAll(shippingOrderManager.getOrders());
            setStatus("Orders loaded from file: " + file.getName());
        } else {
            setStatus("No file selected.");
        }
    }

    /**
     * Handles deleting a selected order.
     */
    @FXML
    private void handleDeleteOrder() {
        var selected = orderTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            var confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setTitle("Confirm Delete");
            confirm.setHeaderText("Are you sure you want to delete this order?");
            confirm.setContentText(selected.toString());
            confirm.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    shippingOrderManager.deleteOrder(selected.getOrderID());
                    orderList.setAll(shippingOrderManager.getOrders());
                    setStatus("Order ID " + selected.getOrderID() + " deleted.");
                } else {
                    setStatus("Delete canceled.");
                }
            });
        } else {
            setStatus("Please select an order to delete.");
        }
    }

    /**
     * Handles exiting the app.
     */
    @FXML
    private void handleExit() {
        System.exit(0);
    }

    /**
     * Sets the status label with a growth/shrink animation.
     */
    private void setStatus(String message) {
        statusLabel.setText(message);
        animateStatus();
    }

    /**
     * Makes the status label grow/shrink to catch the eye.
     */
    private void animateStatus() {
        var scale = new ScaleTransition(Duration.millis(400), statusLabel);
        scale.setFromX(1.0);
        scale.setFromY(1.0);
        scale.setToX(2.0);
        scale.setToY(2.0);
        scale.setCycleCount(2);
        scale.setAutoReverse(true);
        scale.play();
    }
}
