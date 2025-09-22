package ku.cs.controllers.components;

import javafx.css.PseudoClass;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import ku.cs.components.Icons;
import ku.cs.components.button.ElevatedButtonWithIcon;
import ku.cs.services.FXRouter;

import java.io.IOException;

public class UserNavbarController {
    @FXML private Button lockerPageButton;
    @FXML private Button zonePageButton;
    @FXML private Button historyPageButton;
    @FXML private Button footerNavButton;

    @FXML private void initialize() {
        initUserInterfaces();
        initEvents();
    };

    private void initUserInterfaces() {
        lockerPageButton.pseudoClassStateChanged(PseudoClass.getPseudoClass("selected"), true);
        ElevatedButtonWithIcon.SMALL.mask(lockerPageButton, Icons.HOME);
        ElevatedButtonWithIcon.SMALL.mask(zonePageButton, Icons.LOCATION);
        ElevatedButtonWithIcon.SMALL.mask(historyPageButton, Icons.HISTORY);
    }

    protected void initEvents() {
        lockerPageButton.setOnAction(e -> onLockerPageButtonClick());
        zonePageButton.setOnAction(e -> onZoneButtonClick());
        footerNavButton.setOnAction(e -> openLockerReserveDialog());
    }

    public Button getFooterNavButton() {
        return footerNavButton;
    }

    protected void onLockerPageButtonClick() {
        try {
            FXRouter.goTo("user-home");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected void onZoneButtonClick() {
        try {
            FXRouter.goTo("test-zonelist");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void openLockerReserveDialog() {
        try {

            Stage dialog = FXRouter.loadDialogStage("locker-reserve");
            dialog.showAndWait();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
