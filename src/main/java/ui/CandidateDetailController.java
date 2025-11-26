package ui;

import controllers.ElectionSystemController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import models.Candidate;
import models.Election;

public class CandidateDetailController implements UsesElectionController {

    @FXML private Label lblHeader;
    @FXML private Label lblName;
    @FXML private Label lblParty;
    @FXML private Label lblVotes;
    @FXML private Label lblWinner;
    @FXML private Label lblStatus;

    private ElectionSystemController systemController;
    private Candidate candidate;
    private Election election;

    @Override
    public void setSystemController(ElectionSystemController controller) {
        this.systemController = controller;
    }

    public void loadCandidate(Candidate c, Election e) {
        this.candidate = c;
        this.election = e;

        lblHeader.setText("Candidate: " + c.getPolitician().getName());
        lblName.setText(c.getPolitician().getName());
        lblParty.setText(c.getPartyAtTime());
        lblVotes.setText(String.valueOf(c.getVotes()));
        lblWinner.setText(e.isWinner(c) ? "YES" : "NO");
    }

    @FXML
    private void editCandidate() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/candidate_form.fxml"));
            Node view = loader.load();

            CandidateFormController controller = loader.getController();
            controller.setSystemController(systemController);
            controller.openForEdit(election, candidate);

            UIContext.getMainLayoutController().setContent(view);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    private void deleteCandidate() {
        if (systemController == null) return;

        systemController.removeCandidate(
                candidate.getPolitician().getName(),
                election.getElectionId()
        );

        loadCandidateListScreen();
    }

    @FXML
    private void goBack() {
        loadCandidateListScreen();
    }

    private void loadCandidateListScreen() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/candidate_list.fxml"));
            Node view = loader.load();

            CandidateListController controller = loader.getController();
            controller.setSystemController(systemController);
            controller.loadElection(election);

            UIContext.getMainLayoutController().setContent(view);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
