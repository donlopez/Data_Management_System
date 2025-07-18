package dms;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Main application entry point for the Data Management System (DMS).
 * This class extends {@link javafx.application.Application} and initializes
 * the JavaFX stage by loading the login view.
 *
 * <p>Responsible for:
 * <ul>
 *     <li>Loading the LoginView.fxml interface</li>
 *     <li>Displaying the primary stage</li>
 *     <li>Handling UI startup exceptions via logging</li>
 * </ul>
 *
 * Author: Julio Lopez
 * Version: 1.0
 */
public class Main extends Application {

    /** Logger for capturing application startup errors */
    private static final Logger logger = Logger.getLogger(Main.class.getName());

    /**
     * Default constructor for Main class.
     * Required by JavaFX for application launch.
     */
    public Main() {
        // No initialization logic needed
    }

    /**
     * Initializes and displays the JavaFX primary stage.
     * Loads the login interface from FXML and sets it as the root scene.
     *
     * @param primaryStage the main window (stage) of the application
     */
    @Override
    public void start(Stage primaryStage) {
        try {
            // Load the login view from FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/dms/view/LoginView.fxml"));
            Parent root = loader.load();

            // Set scene and window title
            primaryStage.setScene(new Scene(root));
            primaryStage.setTitle("Database Login");
            primaryStage.show();

        } catch (Exception ex) {
            // Log failure to load the FXML
            logger.log(Level.SEVERE, "Failed to load LoginView.fxml", ex);
        }
    }

    /**
     * Launches the JavaFX application.
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        launch(args);
    }
}
