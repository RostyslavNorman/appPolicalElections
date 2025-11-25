package ui;

import controllers.ElectionSystemController;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import models.Election;
import models.ElectionType;

public class ElectionFormController implements UsesElectionController {

    private Election editingElection = null;

    @FXML
    private ComboBox<ElectionType> cmbType;

    @FXML
    private TextField txtLocation;

    @FXML
    private TextField txtDate;

    @FXML
    private TextField txtSeats;

    @FXML
    private Label lblStatus;

    private ElectionSystemController systemController;

    @Override
    public void setSystemController(ElectionSystemController controller) {
        this.systemController = controller;
        loadElectionTypes();
    }

    private void loadElectionTypes() {
        // Static enum values — allowed for assignment
        for (ElectionType type : ElectionType.values()) {
            cmbType.getItems().add(type);
        }
    }
    @FXML
    private void saveElection() {
        lblStatus.setText("");

        if (systemController == null) {
            lblStatus.setText("Internal Error: No controller.");
            return;
        }

        // Validation
        if (cmbType.getValue() == null ||
                txtLocation.getText().isEmpty() ||
                txtDate.getText().isEmpty() ||
                txtSeats.getText().isEmpty()) {

            lblStatus.setText("All fields must be filled.");
            return;
        }

        try {
            ElectionType type = cmbType.getValue();
            String location = txtLocation.getText().trim();
            String date = txtDate.getText().trim();
            int seats = Integer.parseInt(txtSeats.getText().trim());

            // ======================================================
            // 🔥 EDIT MODE — update existing election
            // ======================================================
            if (editingElection != null) {

                systemController.updateElection(
                        editingElection.getElectionId(),
                        type,
                        location,
                        date,
                        seats
                );

                lblStatus.setText("Election updated!");

                // go back to list
                UIContext.getMainLayoutController().openElections();
                return;
            }

            // ======================================================
            // 🔥 CREATE MODE — make new election
            // ======================================================
            try {
                // validate date first
                if (!date.matches("\\d{4}-\\d{2}-\\d{2}")) {
                    lblStatus.setText("Date must be in format YYYY-MM-DD");
                    return;
                }

                Election newElection = new Election(type, location, date, seats);
                systemController.addElection(newElection);

                lblStatus.setText("Election saved!");
                UIContext.getMainLayoutController().openElections();

            } catch (IllegalArgumentException ex) {
                lblStatus.setText(ex.getMessage()); // show meaningful message
            }


            lblStatus.setText("Election saved!");

            UIContext.getMainLayoutController().openElections();

        } catch (NumberFormatException ex) {
            lblStatus.setText("Seats must be a number.");
        } catch (Exception ex) {
            lblStatus.setText("Error saving election.");
            ex.printStackTrace();
        }
    }


    @FXML
    private void cancel() {
        UIContext.getMainLayoutController().openElections();
    }

    public void setElection(Election election) {
        this.editingElection = election;

        cmbType.setValue(election.getElectionType());
        txtLocation.setText(election.getLocation());
        txtDate.setText(election.getDate());
        txtSeats.setText(String.valueOf(election.getNumberOfSeats()));
    }


}
