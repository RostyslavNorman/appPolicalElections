package ui;

import controllers.ElectionSystemController;
import datastructures.DynamicArray;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import models.Candidate;
import models.Election;
import utils.JFXUtils;

public class CandidateListController implements UsesElectionController {

    @FXML
    private Label lblHeader;
    @FXML
    private TextField txtSearch;
    @FXML
    private ComboBox<String> sortBox;
    @FXML
    private ListView<Candidate> listView;

    private ElectionSystemController systemController;
    private Election election;

    @Override
    public void setSystemController(ElectionSystemController controller) {
        this.systemController = controller;
    }

    /**
     * ==========================================
     * LOAD ELECTION
     * Called by ElectionDetailController
     * ==========================================
     */
    public void loadElection(Election e) {
        this.election = e;
        lblHeader.setText("Candidates for: " + e.getElectionId());

        setupSortOptions();
        loadCandidates();
    }

    /**
     * ==========================================
     * LOAD CANDIDATE LIST
     * ==========================================
     */
    private void loadCandidates() {
        if (election == null) return;

        listView.setItems(
                JFXUtils.toObservableList(election.getCandidates())
        );

        listView.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Candidate c, boolean empty) {
                super.updateItem(c, empty);

                if (empty || c == null) {
                    setText(null);
                } else {
                    setText(c.getPolitician().getName()
                            + " — Votes: " + c.getVotes()
                            + " — Party: " + c.getPartyAtTime()
                    );
                }
            }
        });

        listView.setOnMouseClicked(e -> {
            Candidate selected = listView.getSelectionModel().getSelectedItem();
            if (selected != null) openCandidateDetail(selected);
        });
    }

    /**
     * ==========================================
     * SORT OPTIONS
     * ==========================================
     */
    private void setupSortOptions() {
        sortBox.getItems().addAll(
                "Votes ↑", "Votes ↓",
                "Name A→Z", "Name Z→A",
                "Party A→Z", "Party Z→A"
        );

        sortBox.setOnAction(e -> applySort());
    }

    private void applySort() {
        if (election == null) return;

        DynamicArray<Candidate> arr = election.getCandidates();
        String s = sortBox.getValue();
        if (s == null) return;

        switch (s) {
            case "Votes ↑" -> systemController.sortCandidates(arr, "votes", true);
            case "Votes ↓" -> systemController.sortCandidates(arr, "votes", false);
            case "Name A→Z" -> systemController.sortCandidates(arr, "name", true);
            case "Name Z→A" -> systemController.sortCandidates(arr, "name", false);
            case "Party A→Z" -> systemController.sortCandidates(arr, "party", true);
            case "Party Z→A" -> systemController.sortCandidates(arr, "party", false);
        }

        loadCandidates();
    }

    /**
     * ==========================================
     * ADD NEW CANDIDATE
     * ==========================================
     */
    @FXML
    private void addCandidate() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/candidate_form.fxml"));
            Node view = loader.load();

            CandidateFormController controller = loader.getController();
            controller.setSystemController(systemController);
            controller.openForElection(election);

            UIContext.getMainLayoutController().setContent(view);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * ==========================================
     * OPEN CANDIDATE DETAIL
     * ==========================================
     */
    private void openCandidateDetail(Candidate c) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/candidate_detail.fxml"));
            Node view = loader.load();

            CandidateDetailController controller = loader.getController();
            controller.setSystemController(systemController);
            controller.loadCandidate(c, election);

            UIContext.getMainLayoutController().setContent(view);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * ==========================================
     * GO BACK TO ELECTION DETAIL
     * ==========================================
     */
    @FXML
    private void goBack() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/election_detail.fxml"));
            Node view = loader.load();

            ElectionDetailController controller = loader.getController();
            controller.setSystemController(systemController);
            controller.loadElection(election);

            UIContext.getMainLayoutController().setContent(view);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
