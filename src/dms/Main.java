package dms;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Main application entry point.
 * Loads the login view.
 */
public class Main extends Application {

    private static final Logger logger = Logger.getLogger(Main.class.getName());

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Load LoginView.fxml and show it
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/dms/view/LoginView.fxml"));
            Parent root = loader.load();

            primaryStage.setScene(new Scene(root));
            primaryStage.setTitle("Database Login");
            primaryStage.show();

        } catch (Exception ex) {
            logger.log(Level.SEVERE, "Failed to load LoginView.fxml", ex);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
