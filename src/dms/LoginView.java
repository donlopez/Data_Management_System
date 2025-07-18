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
 * Controller class for the login view (LoginView.fxml).
 * Handles user input for MySQL connection settings and establishes a connection
 * through the {@link DBConnectionManager}. If successful, the app transitions
 * to the main dashboard view.
 *
 * Author: Julio Lopez
 * Version: 1.0
 */
public class LoginView {

    /** Default constructor for LoginView. Required by JavaFX FXML loader. */
    public LoginView() {
        // No initialization logic needed here
    }

    /** TextField for entering the database host (e.g., "localhost") */
    @FXML private TextField hostField;

    /** TextField for entering the port number (e.g., "3306") */
    @FXML private TextField portField;

    /** TextField for entering the name of the database (e.g., "ShippingDMS") */
    @FXML private TextField dbNameField;

    /** TextField for entering the database username */
    @FXML private TextField usernameField;

    /** PasswordField for entering the database password */
    @FXML private PasswordField passwordField;

    /** Label for displaying connection status messages to the user */
    @FXML private Label statusLabel;

    /**
     * Triggered when the user clicks the "Connect" button.
     * Attempts to connect to the database using the provided credentials.
     * If successful, transitions the application to the MainView interface.
     */
    @FXML
    private void handleConnect() {
        // Retrieve and trim input values
        String host = hostField.getText().trim();
        String port = portField.getText().trim();
        String dbName = dbNameField.getText().trim();
        String user = usernameField.getText().trim();
        String pass = passwordField.getText();

        // Basic input validation to ensure all fields are filled
        if (host.isEmpty() || port.isEmpty() || dbName.isEmpty() || user.isEmpty()) {
            statusLabel.setText("All fields are required.");
            return;
        }

        try {
            // Attempt to establish a connection using DBConnectionManager
            DBConnectionManager dbManager = DBConnectionManager.getInstance();
            Connection conn = dbManager.connect(host, port, dbName, user, pass);

            // Check if connection is valid and active
            if (conn != null && !conn.isClosed()) {
                statusLabel.setText("🟢 Connected successfully.");

                // Load the main dashboard view
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/dms/view/MainView.fxml"));
                Parent mainRoot = loader.load();

                // Switch scenes
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
