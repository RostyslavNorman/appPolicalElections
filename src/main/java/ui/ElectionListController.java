package ui;

import controllers.ElectionSystemController;
import datastructures.DynamicArray;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import models.Election;
import models.ElectionType;


public class ElectionListController implements UsesElectionController {

    @FXML
    private TextField txtSearch;

    @FXML
    private ComboBox<String> sortBox;

    @FXML
    private ListView<Election> listView;

    @FXML
    private VBox advancedBox;
    @FXML
    private ComboBox<String> cmbYear;
    @FXML
    private ComboBox<ElectionType> cmbType;
    @FXML
    private TextField txtLocationFilter;


    private ElectionSystemController systemController;

    @Override
    public void setSystemController(ElectionSystemController controller) {
        this.systemController = controller;
        loadElections();
        setupSortOptions();
        setupClickHandler();

        // populate types
        for (ElectionType t : ElectionType.values()) {
            cmbType.getItems().add(t);
        }

        // populate years
        DynamicArray<String> years = systemController.getAllElectionYears();
        for (int i = 0; i < years.size(); i++) {
            cmbYear.getItems().add(years.get(i));
        }

    }

    private void setupClickHandler() {
        listView.setOnMouseClicked(event -> {
            Election selected = listView.getSelectionModel().getSelectedItem();

            if (selected != null) {
                openElectionDetail(selected);
            }
        });
    }

    private void openElectionDetail(Election election) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/election_detail.fxml"));
        try {
            Node view = loader.load();

            ElectionDetailController controller = loader.getController();
            controller.setSystemController(systemController);
            controller.loadElection(election);

            UIContext.getMainLayoutController().setContent(view);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // ==========================================================
    // LOAD / REFRESH LIST
    // ==========================================================

    private void loadElections() {
        if (systemController == null) return;

        DynamicArray<Election> data = systemController.getAllElections();
        listView.setItems(utils.JFXUtils.toObservableList(data));

        listView.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Election election, boolean empty) {
                super.updateItem(election, empty);
                if (empty || election == null) {
                    setText(null);
                } else {
                    setText(election.toString());
                }
            }
        });

    }

    // ==========================================================
    // SORT SETUP
    // ==========================================================

    private void setupSortOptions() {
        sortBox.getItems().addAll(
                "Year (Ascending)",
                "Year (Descending)",
                "Date (Ascending)",
                "Date (Descending)",
                "Location (A→Z)",
                "Location (Z→A)",
                "Seats (Ascending)",
                "Seats (Descending)"
        );

        sortBox.setOnAction(e -> applySorting());
    }

    private void applySorting() {
        if (systemController == null) return;

        DynamicArray<Election> elections = systemController.getAllElections();

        String selected = sortBox.getValue();
        if (selected == null) return;

        switch (selected) {
            case "Year (Ascending)" -> systemController.sortElections(elections, "year", true);
            case "Year (Descending)" -> systemController.sortElections(elections, "year", false);
            case "Date (Ascending)" -> systemController.sortElections(elections, "date", true);
            case "Date (Descending)" -> systemController.sortElections(elections, "date", false);
            case "Location (A→Z)" -> systemController.sortElections(elections, "location", true);
            case "Location (Z→A)" -> systemController.sortElections(elections, "location", false);
            case "Seats (Ascending)" -> systemController.sortElections(elections, "seats", true);
            case "Seats (Descending)" -> systemController.sortElections(elections, "seats", false);
        }

        loadElections();
    }

    // ==========================================================
    // ADD NEW ELECTION
    // ==========================================================

    @FXML
    private void addElection() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/fxml/election_form.fxml")
            );

            Node form = loader.load();

            Object controller = loader.getController();
            if (controller instanceof UsesElectionController) {
                ((UsesElectionController) controller).setSystemController(systemController);
            }

            UIContext.getMainLayoutController().setContent(form);

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("ERROR: Could not load election_form.fxml");
        }
    }


    // ==========================================================
    // SEARCH (simple: location or year)
    // ==========================================================

    @FXML
    private void initialize() {
        txtSearch.textProperty().addListener((obs, oldV, newV) -> applySearch(newV));
    }

    private void applySearch(String term) {
        if (systemController == null) return;

        term = term.toLowerCase().trim();
        DynamicArray<Election> all = systemController.getAllElections();

        DynamicArray<Election> filtered = new DynamicArray<>();

        for (int i = 0; i < all.size(); i++) {
            Election e = all.get(i);

            if (e.getLocation().toLowerCase().contains(term) ||
                    e.getYear().toLowerCase().contains(term)) {
                filtered.add(e);
            }
        }

        ObservableList<Election> obs = FXCollections.observableArrayList();

        for (int i = 0; i < filtered.size(); i++) {
            obs.add(filtered.get(i));
        }

        listView.setItems(obs);
    }

    @FXML
    private void toggleAdvancedSearch() {
        boolean show = !advancedBox.isVisible();
        advancedBox.setVisible(show);
        advancedBox.setManaged(show);
    }


    @FXML
    private void applyAdvancedSearch() {

        ElectionType type = cmbType.getValue();
        String year = cmbYear.getValue();
        String location = txtLocationFilter.getText().trim();

        DynamicArray<Election> results =
                systemController.searchElections(type, year, location);

        ObservableList<Election> obs = FXCollections.observableArrayList();

        for (int i = 0; i < results.size(); i++) {
            obs.add(results.get(i));
        }

        listView.setItems(obs);


    }

    @FXML
    private void clearAdvancedSearch() {
        cmbType.setValue(null);
        cmbYear.setValue(null);
        txtLocationFilter.setText("");

        loadElections();
    }


}
