package ui;

import controllers.ElectionSystemController;
import datastructures.DynamicArray;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import models.Election;

public class PoliticianElectionsController implements UsesElectionController {

    @FXML private Label lblHeader;
    @FXML private ListView<Election> listView;

    private ElectionSystemController systemController;
    private String politicianName;

    @Override
    public void setSystemController(ElectionSystemController controller) {
        this.systemController = controller;

        // If politician already known → load data
        if (politicianName != null) {
            loadElections();
        }
    }

    public void setPoliticianName(String name) {
        this.politicianName = name;

        if (systemController != null) {
            loadElections();
        }
    }

    private void loadElections() {
        lblHeader.setText("Elections for: " + politicianName);

        DynamicArray<Election> elections =
                systemController.getPoliticianElections(politicianName);

        listView.getItems().clear();
        for (int i = 0; i < elections.size(); i++) {
            listView.getItems().add(elections.get(i));
        }

        listView.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Election e, boolean empty) {
                super.updateItem(e, empty);

                if (empty || e == null) {
                    setText(null);
                } else {
                    setText(e.getElectionId() + " — " + e.getLocation());
                }
            }
        });
    }

    @FXML
    private void goBack() {
        Navigation.goTo("politician_detail.fxml", systemController, controller -> {
            ((PoliticianDetailController) controller)
                    .setPolitician(systemController.getPolitician(politicianName));
        });
    }
}
