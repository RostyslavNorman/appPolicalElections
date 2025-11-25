package ui;

import controllers.ElectionSystemController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;

public class MainLayoutController {

    @FXML
    private StackPane contentPane;

    // Shared backend controller instance
    private ElectionSystemController systemController = new ElectionSystemController();


    // ==========================================================
    // Helper method: loads an FXML screen into the content area
    // ==========================================================
    private void loadContent(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/fxml/" + fxmlFile)
            );

            Node root = loader.load();

            // Pass backend controller to child controller, if it supports it
            Object childController = loader.getController();
            if (childController instanceof UsesElectionController) {
                ((UsesElectionController) childController)
                        .setSystemController(systemController);
            }

            contentPane.getChildren().setAll(root);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // ==========================================================
    // Navigation actions (called from buttons in MainLayout.fxml)
    // ==========================================================

    @FXML
    public  void openPoliticians() {
        loadContent("politician_list.fxml");
    }

    @FXML
    public  void openElections() {
        loadContent("election_list.fxml");
    }


    // ==========================================================
    // Save / Load functionality (implemented later)
    // ==========================================================

    @FXML
    private void saveData() {
        try {
            persistence.DataManager.save(systemController);
            System.out.println("Data saved to XML.");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("SAVE FAILED");
        }
    }

    @FXML
    private void loadData() {
        try {
            if (persistence.DataManager.hasDataFile()) {
                ElectionSystemController loaded =
                        persistence.DataManager.load();

                // Replace shared controller
                UIContext.getMainLayoutController()
                        .setSystemController(loaded);

                System.out.println("Data loaded from XML.");

                openElections(); // Refresh UI
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("LOAD FAILED");
        }
    }

    public void setSystemController(ElectionSystemController controller) {
        this.systemController = controller;
    }


    @FXML
    private void initialize() {
        UIContext.setMainLayoutController(this);
    }

    public void setContent(Node node) {
        contentPane.getChildren().setAll(node);
    }

}
