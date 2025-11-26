package ui;

import controllers.ElectionSystemController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;

/**
 * Utility class for loading screens into the MainLayout content area.
 * Used by all controllers to navigate between screens.
 */
public class Navigation {

    /**
     * Load a new FXML screen into the main content area.
     *
     * @param fxmlFile   The FXML file name inside /resources/fxml/ (e.g., "politician_list.fxml")
     * @param system     The shared ElectionSystemController instance
     * @param callback   Optional: allows caller to configure the loaded controller
     *                   Example: (controller) -> ((PoliticianDetailController) controller).setPolitician(p)
     */
    public static void goTo(String fxmlFile,
                            ElectionSystemController system,
                            ControllerCallback callback) {

        try {
            FXMLLoader loader = new FXMLLoader(
                    Navigation.class.getResource("/fxml/" + fxmlFile)
            );

            Node root = loader.load();
            Object controller = loader.getController();

            // Inject shared backend controller if supported
            if (controller instanceof UsesElectionController) {
                ((UsesElectionController) controller).setSystemController(system);
            }

            // Apply optional callback
            if (callback != null) {
                callback.call(controller);
            }

            // Replace center content in MainLayout
            MainLayoutController main = UIContext.getMainLayoutController();
            main.setContent(root);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // ==========================================================
    // Callback interface
    // ==========================================================
    public interface ControllerCallback {
        void call(Object controller);
    }
}
