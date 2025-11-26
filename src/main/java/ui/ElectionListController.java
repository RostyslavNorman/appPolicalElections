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
import models.Candidate;
import models.Election;
import models.ElectionType;
import models.Politician;

public class ElectionListController implements UsesElectionController {

    @FXML
    private TextField txtSearch;

    @FXML
    private ComboBox<String> sortBox;

    @FXML
    private TreeView<Object> treeView; // replaces ListView<Election>

    @FXML
    private VBox advancedBox;
    @FXML
    private ComboBox<String> cmbYear;
    @FXML
    private ComboBox<ElectionType> cmbType;
    @FXML
    private TextField txtLocationFilter;

    private ElectionSystemController systemController;
    private DynamicArray<Election> currentList = new DynamicArray<>();

    @Override
    public void setSystemController(ElectionSystemController controller) {
        this.systemController = controller;
        loadElections();
        setupSortOptions();
        setupCellFactory();

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

    // ==========================================================
    // TREE CELL FACTORY (custom behavior)
    // ==========================================================

    private void setupCellFactory() {
        treeView.setCellFactory(tv -> {
            TreeCell<Object> cell = new TreeCell<>() {
                @Override
                protected void updateItem(Object item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                        return;
                    }

                    if (item instanceof Election e) {
                        setText(e.toString());
                    } else if (item instanceof Candidate c) {
                        setText("Candidate: "
                                + c.getPolitician().getName()
                                + " — Votes: " + c.getVotes());
                    } else if (item instanceof Politician p) {
                        setText("Politician: " + p.getName());
                    }
                }
            };

            cell.setOnMouseClicked(e -> {
                if (e.getClickCount() == 2 && !cell.isEmpty()) {
                    Object item = cell.getItem();

                    if (item instanceof Election election) {
                        openElectionDetail(election);
                    } else if (item instanceof Candidate candidate) {
                        openPoliticianDetail(candidate.getPolitician());
                    }
                }
            });

            return cell;
        });
    }

    private void openPoliticianDetail(Politician p) {
        Navigation.goTo("politician_detail.fxml", systemController, controller -> {
            ((PoliticianDetailController) controller).setPolitician(p);
        });
    }

    private void openElectionDetail(Election election) {
        Navigation.goTo("election_detail.fxml", systemController, controller -> {
            ((ElectionDetailController) controller).loadElection(election);
        });
    }

    // ==========================================================
    // BUILD TREE
    // ==========================================================
    private void buildTree(DynamicArray<Election> elections) {
        TreeItem<Object> root = new TreeItem<>();

        for (int i = 0; i < elections.size(); i++) {
            Election e = elections.get(i);

            TreeItem<Object> electionItem = new TreeItem<>(e);

            // Add candidates as children
            DynamicArray<Candidate> cList = e.getCandidates();
            for (int j = 0; j < cList.size(); j++) {
                electionItem.getChildren().add(new TreeItem<>(cList.get(j)));
            }

            root.getChildren().add(electionItem);
        }

        treeView.setRoot(root);
        treeView.setShowRoot(false);
    }

    // ==========================================================
    // LOAD & SEARCH & FILTER
    // ==========================================================
    private void loadElections() {
        currentList = systemController.getAllElections();
        buildTree(currentList);
    }

    @FXML
    private void initialize() {
        txtSearch.textProperty().addListener((obs, oldV, newV) -> applySearch(newV));
    }

    private void applySearch(String term) {
        currentList = systemController.searchElectionsSimple(term);
        buildTree(currentList);
    }

    @FXML
    private void applyAdvancedSearch() {
        ElectionType type = cmbType.getValue();
        String year = cmbYear.getValue();
        String location = txtLocationFilter.getText().trim();

        currentList = systemController.searchElections(type, year, location);
        buildTree(currentList);
    }

    @FXML
    private void clearAdvancedSearch() {
        cmbType.setValue(null);
        cmbYear.setValue(null);
        txtLocationFilter.setText("");
        loadElections();
    }

    @FXML
    private void toggleAdvancedSearch() {
        boolean show = !advancedBox.isVisible();
        advancedBox.setVisible(show);
        advancedBox.setManaged(show);
    }

    // ==========================================================
    // SORTING
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
        if (currentList == null) return;

        String selected = sortBox.getValue();
        if (selected == null) return;

        switch (selected) {
            case "Year (Ascending)" -> systemController.sortElections(currentList, "year", true);
            case "Year (Descending)" -> systemController.sortElections(currentList, "year", false);
            case "Date (Ascending)" -> systemController.sortElections(currentList, "date", true);
            case "Date (Descending)" -> systemController.sortElections(currentList, "date", false);
            case "Location (A→Z)" -> systemController.sortElections(currentList, "location", true);
            case "Location (Z→A)" -> systemController.sortElections(currentList, "location", false);
            case "Seats (Ascending)" -> systemController.sortElections(currentList, "seats", true);
            case "Seats (Descending)" -> systemController.sortElections(currentList, "seats", false);
        }

        buildTree(currentList);
    }

    // ==========================================================
    // ADD NEW
    // ==========================================================
    @FXML
    private void addElection() {
        Navigation.goTo("election_form.fxml", systemController, null);
    }
}
