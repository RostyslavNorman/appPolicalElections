package ui;

import controllers.ElectionSystemController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import models.Candidate;
import models.Election;
import models.Politician;
import utils.JFXUtils;

public class CandidateFormController implements UsesElectionController {

    @FXML private Label lblHeader;
    @FXML private ComboBox<Politician> cmbPolitician;
    @FXML private TextField txtParty;
    @FXML private TextField txtVotes;
    @FXML private Label lblStatus;

    private ElectionSystemController systemController;
    private Election election;
    private Candidate editing;

    @Override
    public void setSystemController(ElectionSystemController controller) {
        this.systemController = controller;
    }

    /** ================================
     *  OPEN FOR NEW CANDIDATE
     *  ================================ */
    public void openForElection(Election e) {
        this.election = e;
        this.editing = null;

        lblHeader.setText("Add Candidate");

        cmbPolitician.setItems(
                JFXUtils.toObservableList(systemController.getAllPoliticians())
        );
    }

    /** ================================
     *  OPEN FOR EDIT CANDIDATE
     *  ================================ */
    public void openForEdit(Election e, Candidate c) {
        this.election = e;
        this.editing = c;

        lblHeader.setText("Edit Candidate");

        // Politician cannot be changed during edit
        cmbPolitician.setDisable(true);
        cmbPolitician.getItems().add(c.getPolitician());
        cmbPolitician.getSelectionModel().select(c.getPolitician());

        txtParty.setText(c.getPartyAtTime());
        txtVotes.setText(String.valueOf(c.getVotes()));
    }

    /** ================================
     *  SAVE CANDIDATE
     *  ================================ */
    @FXML
    private void saveCandidate() {
        lblStatus.setText("");

        if (election == null) {
            lblStatus.setText("Error: No election.");
            return;
        }

        try {
            Politician p = cmbPolitician.getValue();
            if (p == null) {
                lblStatus.setText("Select politician.");
                return;
            }

            String party = txtParty.getText().trim();
            int votes = Integer.parseInt(txtVotes.getText().trim());

            if (editing == null) {
                // Create new candidate
                systemController.addCandidate(
                        p.getName(),
                        election.getElectionId(),
                        votes,
                        party
                );
            } else {
                // Update existing
                systemController.updateCandidateVotes(
                        p.getName(),
                        election.getElectionId(),
                        votes
                );
                systemController.updateCandidateParty(
                        p.getName(),
                        election.getElectionId(),
                        party
                );
            }

            lblStatus.setText("Saved!");

            returnToCandidateList();

        } catch (NumberFormatException ex) {
            lblStatus.setText("Votes must be a number.");
        } catch (Exception ex) {
            lblStatus.setText("Error saving candidate.");
            ex.printStackTrace();
        }
    }

    /** ================================
     *  NAVIGATION BACK TO LIST
     *  ================================ */
    private void returnToCandidateList() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/fxml/candidate_list.fxml")
            );

            Node view = loader.load();

            CandidateListController controller = loader.getController();
            controller.setSystemController(systemController);
            controller.loadElection(election);

            UIContext.getMainLayoutController().setContent(view);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void cancel() {
        returnToCandidateList();
    }
}
