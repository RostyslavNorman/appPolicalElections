package ui;

import controllers.ElectionSystemController;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import models.Politician;

public class PoliticianFormController implements UsesElectionController {

    @FXML private TextField txtName;
    @FXML private TextField txtDOB;
    @FXML private TextField txtParty;
    @FXML private TextField txtCounty;
    @FXML private TextField txtImage;
    @FXML private Label lblStatus;

    private ElectionSystemController systemController;

    // If editing an existing politician
    private Politician editingPolitician = null;


    // ==========================================================
    // Receive backend controller from MainLayoutController
    // ==========================================================
    @Override
    public void setSystemController(ElectionSystemController controller) {
        this.systemController = controller;

        // If editing, preload data
        if (editingPolitician != null) {
            loadPoliticianData();
        }
    }


    // ==========================================================
    // Used by List/Detail screens to set edit mode
    // ==========================================================
    public void setEditingPolitician(Politician politician) {
        this.editingPolitician = politician;

        if (systemController != null) {  // already injected
            loadPoliticianData();
        }
    }

    private void loadPoliticianData() {
        txtName.setText(editingPolitician.getName());
        txtDOB.setText(editingPolitician.getDateOfBirth());
        txtParty.setText(editingPolitician.getPoliticalParty());
        txtCounty.setText(editingPolitician.getHomeCounty());
        txtImage.setText(editingPolitician.getImageUrl());
    }


    // ==========================================================
    // Save button handler
    // ==========================================================
    @FXML
    private void savePolitician() {
        lblStatus.setText("");

        try {
            String name = txtName.getText().trim();
            String dob = txtDOB.getText().trim();
            String party = txtParty.getText().trim();
            String county = txtCounty.getText().trim();
            String image = txtImage.getText().trim();

            if (name.isEmpty() || dob.isEmpty() || county.isEmpty()) {
                lblStatus.setText("Name, DOB, and County are required.");
                return;
            }

            boolean success;

            if (editingPolitician == null) {
                success = systemController.addPolitician(name, dob, party, county, image);

                if (!success) {
                    lblStatus.setText("Politician already exists.");
                    return;
                }
            } else {
                success = systemController.updatePolitician(
                        editingPolitician.getName(),
                        name,
                        dob,
                        party,
                        county,
                        image
                );
                if (!success) {
                    lblStatus.setText("Update failed.");
                    return;
                }
            }

            Navigation.goTo("politician_list.fxml", systemController, null);

        } catch (Exception e) {
            lblStatus.setText("Error: " + e.getMessage());
        }
    }


    // ==========================================================
    // Cancel button handler
    // ==========================================================
    @FXML
    private void cancel() {
        Navigation.goTo("politician_list.fxml", systemController, null);
    }
}
