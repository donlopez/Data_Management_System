package dms;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Controller class for the login view.
 * Handles user input for database connection credentials and connects to the database.
 */
public class LoginView {

    @FXML private TextField hostField;         // Input for database host (e.g., "localhost")
    @FXML private TextField portField;         // Input for port number (e.g., "3306")
    @FXML private TextField dbNameField;       // Input for database name (e.g., "ShippingDMS")
    @FXML private TextField usernameField;     // Input for username
    @FXML private PasswordField passwordField; // Input for password
    @FXML private Label statusLabel;           // Label to display connection status

    /**
     * Triggered when the "Connect" button is clicked.
     * Attempts to connect using the provided credentials and transitions to the main view if successful.
     */
    @FXML
    private void handleConnect() {
        String host = hostField.getText().trim();
        String port = portField.getText().trim();
        String dbName = dbNameField.getText().trim();
        String user = usernameField.getText().trim();
        String pass = passwordField.getText();

        if (host.isEmpty() || port.isEmpty() || dbName.isEmpty() || user.isEmpty()) {
            statusLabel.setText("All fields are required.");
            return;
        }

        try {
            // Connect and keep the connection open
            DBConnectionManager dbManager = DBConnectionManager.getInstance();
            Connection conn = dbManager.connect(host, port, dbName, user, pass);

            if (conn != null && !conn.isClosed()) {
                statusLabel.setText("🟢 Connected successfully.");

                // Load MainView.fxml and switch scene
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/dms/view/MainView.fxml"));
                Parent mainRoot = loader.load();
                Stage currentStage = (Stage) hostField.getScene().getWindow();
                currentStage.setScene(new Scene(mainRoot));
                currentStage.setTitle("Shipping Order Manager");
                currentStage.show();

            } else {
                statusLabel.setText("🔴 Connection failed.");
            }

        } catch (SQLException e) {
            statusLabel.setText("🔴 Connection error: " + e.getMessage());
        } catch (IOException e) {
            statusLabel.setText("🔴 Failed to load main view: " + e.getMessage());
        }
    }
}
