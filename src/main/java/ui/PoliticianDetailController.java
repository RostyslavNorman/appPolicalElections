package ui;

import controllers.ElectionSystemController;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import models.Politician;

public class PoliticianDetailController implements UsesElectionController {

    @FXML private Label lblName;
    @FXML private Label lblDOB;
    @FXML private Label lblAge;
    @FXML private Label lblParty;
    @FXML private Label lblCounty;
    @FXML private Label lblImage;

    @FXML private Label lblStatus;

    private Politician selectedPolitician;
    private ElectionSystemController systemController;


    // ==========================================================
    // Receive backend controller
    // ==========================================================
    @Override
    public void setSystemController(ElectionSystemController controller) {
        this.systemController = controller;

        // If politician was set BEFORE backend injection → load now
        if (selectedPolitician != null) {
            loadData();
        }
    }


    // ==========================================================
    // Called by PoliticianListController to set the selected politician
    // ==========================================================
    public void setPolitician(Politician politician) {
        this.selectedPolitician = politician;

        if (systemController != null) { // controller already injected
            loadData();
        }
    }


    // ==========================================================
    // Fill screen labels with politician data
    // ==========================================================
    private void loadData() {
        lblName.setText(selectedPolitician.getName());
        lblDOB.setText(selectedPolitician.getDateOfBirth());
        lblAge.setText(String.valueOf(selectedPolitician.getAge()));
        lblParty.setText(selectedPolitician.getPoliticalParty());
        lblCounty.setText(selectedPolitician.getHomeCounty());
        lblImage.setText(selectedPolitician.getImageUrl());
    }


    // ==========================================================
    // Edit button
    // ==========================================================
    @FXML
    private void editPolitician() {
        Navigation.goTo("politician_form.fxml", systemController, controller -> {
            ((PoliticianFormController) controller).setEditingPolitician(selectedPolitician);
        });
    }


    // ==========================================================
    // Delete button
    // ==========================================================
    @FXML
    private void deletePolitician() {
        try {
            boolean removed = systemController.deletePolitician(selectedPolitician.getName());

            if (!removed) {
                lblStatus.setText("Error: Could not delete politician.");
                return;
            }

            // After successful deletion → go back to list
            Navigation.goTo("politician_list.fxml", systemController, null);

        } catch (Exception e) {
            lblStatus.setText("Error: " + e.getMessage());
        }
    }


    // ==========================================================
    // View Elections Participated button
    // (We will implement this screen later)
    // ==========================================================



    // ==========================================================
    // Back button
    // ==========================================================
    @FXML
    private void back() {
        Navigation.goTo("politician_list.fxml", systemController, null);
    }
}

