package ku.cs.controllers.components;

import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import ku.cs.models.zone.ZoneList;
import ku.cs.services.context.AppContext;
import ku.cs.services.ui.FXRouter;
import ku.cs.services.session.SessionManager;
import ku.cs.services.datasources.Datasource;
import ku.cs.services.datasources.ZoneListFileDatasource;
import ku.cs.services.utils.AlertUtil;

import java.io.IOException;

public class AddNewZonePopup {
    public void run() {
        final SessionManager sessionManager = AppContext.getSessionManager();
        final AlertUtil alertUtil = new AlertUtil();

        sessionManager.requireAdminLogin();

        Datasource<ZoneList> datasource = new ZoneListFileDatasource("data", "test-zone-data.json");;
        ZoneList zones = datasource.readData();

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Add New Zone");
        dialog.setHeaderText("Please enter the new zone name");

        ButtonType addNewZoneButtonType = new ButtonType("Add new zone", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addNewZoneButtonType, ButtonType.CANCEL);

        VBox vBox = new VBox();
        vBox.setSpacing(10);

        Label zoneNameLabel = new Label("Zone Name:");
        TextField zoneNameTextField = new TextField();
        zoneNameTextField.setPromptText("Zone name");

        vBox.getChildren().addAll(zoneNameLabel, zoneNameTextField);

        dialog.getDialogPane().setContent(vBox);

        // Not Empty
        Node addButton = dialog.getDialogPane().lookupButton(addNewZoneButtonType);
        addButton.setDisable(true);

        zoneNameTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            addButton.setDisable(newValue.trim().isEmpty());
        });

        // Wait Result on dialog
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addNewZoneButtonType) {
                return addNewZoneButtonType;
            }
            return null;
        });

        dialog.showAndWait().ifPresent(result -> {
            if (result == addNewZoneButtonType) {
                String zoneName = zoneNameTextField.getText().trim();

                if (zoneName.isEmpty()) {
                    alertUtil.error("Error", "Zone name cannot be empty.");
                    return;
                }

                // Add New Zone
                zones.addZone(zoneName);
                datasource.writeData(zones);

                alertUtil.info("Successfully", "New zone \"" + zoneName + "\" has been added successfully!");
                try {
                    FXRouter.goTo("admin-manage-zones");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
}
