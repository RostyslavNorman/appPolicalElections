package ui;

import controllers.ElectionSystemController;
import datastructures.DynamicArray;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import models.Politician;

public class PoliticianListController implements UsesElectionController {

    private ElectionSystemController systemController;

    @FXML
    private TextField txtSearch;
    @FXML
    private ComboBox<String> sortBox;
    @FXML
    private ListView<Politician> listView;
    @FXML
    private VBox advancedBox;
    @FXML
    private ComboBox<String> cmbParty;
    @FXML
    private ComboBox<String> cmbCounty;


    // ==========================================================
    // Receive backend controller from MainLayoutController
    // ==========================================================
    @Override
    public void setSystemController(ElectionSystemController controller) {
        this.systemController = controller;
        initializeData();
    }


    // ==========================================================
    // Initialize screen (called after systemController is set)
    // ==========================================================
    private void initializeData() {

        // Populate sort options
        sortBox.getItems().addAll("name", "party", "county", "age");
        sortBox.setValue("name");

        // Load initial data
        refreshList(systemController.getAllPoliticians());

        // Search updates
        txtSearch.textProperty().addListener((obs, oldVal, newVal) -> updateFiltered());

        // Sorting updates
        sortBox.valueProperty().addListener((obs, oldVal, newVal) -> updateFiltered());

        // Drill-down on item click
        listView.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2) {
                Politician p = listView.getSelectionModel().getSelectedItem();
                if (p != null) {
                    Navigation.goTo("politician_detail.fxml", systemController, controller -> {
                        ((PoliticianDetailController) controller).setPolitician(p);
                    });
                }
            }
        });

        // fill party list
        DynamicArray<String> parties = systemController.getAllParties();
        for (int i = 0; i < parties.size(); i++) {
            cmbParty.getItems().add(parties.get(i));
        }

        // fill county list
        DynamicArray<String> counties = systemController.getAllCounties();
        for (int i = 0; i < counties.size(); i++) {
            cmbCounty.getItems().add(counties.get(i));
        }

    }


    // ==========================================================
    // Filtering + sorting logic
    // ==========================================================
    private void updateFiltered() {

        DynamicArray<Politician> results =
                systemController.searchPoliticiansByName(txtSearch.getText());

        systemController.sortPoliticians(
                results,
                sortBox.getValue(),
                true  // ascending
        );

        refreshList(results);
    }


    // ==========================================================
    // Refresh ListView content
    // ==========================================================
    private void refreshList(DynamicArray<Politician> array) {
        listView.getItems().clear();
        for (int i = 0; i < array.size(); i++) {
            listView.getItems().add(array.get(i));
        }
    }


    // ==========================================================
    // Add button → go to form screen
    // ==========================================================
    @FXML
    private void addPolitician() {
        Navigation.goTo("politician_form.fxml", systemController, null);
    }

    @FXML
    private void toggleAdvancedSearch() {
        boolean show = !advancedBox.isVisible();
        advancedBox.setVisible(show);
        advancedBox.setManaged(show);
    }

    @FXML
    private void applyAdvancedSearch() {
        String party = cmbParty.getValue();
        String county = cmbCounty.getValue();

        DynamicArray<Politician> results =
                systemController.searchPoliticians(null, party, county);

        refreshList(results);
    }

    @FXML
    private void clearAdvancedSearch() {
        cmbParty.setValue(null);
        cmbCounty.setValue(null);
        refreshList(systemController.getAllPoliticians());
    }


}
