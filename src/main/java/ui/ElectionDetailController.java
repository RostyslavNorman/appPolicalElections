package ui;

import controllers.ElectionSystemController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import models.Election;

public class ElectionDetailController implements UsesElectionController {

    @FXML private Label lblHeader;
    @FXML private Label lblType;
    @FXML private Label lblLocation;
    @FXML private Label lblDate;
    @FXML private Label lblSeats;
    @FXML private Label lblCandidates;
    @FXML private Label lblStatus;

    private ElectionSystemController systemController;
    private Election currentElection;

    @Override
    public void setSystemController(ElectionSystemController controller) {
        this.systemController = controller;
    }

    /** Called by ElectionListController when election is selected */
    public void loadElection(Election election) {
        this.currentElection = election;

        lblHeader.setText("Election: " + election.getElectionId());
        lblType.setText(election.getElectionType().toString());
        lblLocation.setText(election.getLocation());
        lblDate.setText(election.getDate());
        lblSeats.setText(String.valueOf(election.getNumberOfSeats()));
        lblCandidates.setText(String.valueOf(election.getCandidates().size()));
    }

    @FXML
    private void openCandidates() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/fxml/candidate_list.fxml")
            );

            Node view = loader.load();

            CandidateListController controller = loader.getController();
            controller.setSystemController(systemController);
            controller.loadElection(currentElection);

            UIContext.getMainLayoutController().setContent(view);

        } catch (Exception e) {
            e.printStackTrace();
            lblStatus.setText("Error opening candidate list.");
        }
    }


    @FXML
    private void editElection() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/election_form.fxml"));
            Node view = loader.load();

            ElectionFormController controller = loader.getController();
            controller.setSystemController(systemController);
            controller.setElection(currentElection);

            UIContext.getMainLayoutController().setContent(view);

        } catch (Exception e) {
            e.printStackTrace();
            lblStatus.setText("Error opening edit screen.");
        }
    }

    @FXML
    private void deleteElection() {
        if (systemController == null || currentElection == null) {
            lblStatus.setText("Error: No controller.");
            return;
        }

        // CORRECT delete call
        systemController.deleteElection(currentElection.getElectionId());

        // Navigate back to list
        UIContext.getMainLayoutController().openElections();
    }

    @FXML
    private void goBack() {
        UIContext.getMainLayoutController().openElections();
    }
}
