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
    @FXML private Button lockerReserveButton;

    @FXML private void initialize() {
        initUserInterfaces();
        initEvents();
    };

    private void initUserInterfaces() {
        System.out.println("current Route: " + FXRouter.getCurrentRouteLabel());
        switch (FXRouter.getCurrentRouteLabel()){
            case "user-home":
                lockerPageButton.pseudoClassStateChanged(PseudoClass.getPseudoClass("selected"), true);
                break;
            case "user-zone":
                zonePageButton.pseudoClassStateChanged(PseudoClass.getPseudoClass("selected"), true);
                break;
            case "user-history":
                historyPageButton.pseudoClassStateChanged(PseudoClass.getPseudoClass("selected"), true);
                break;
            default:
                break;
        }
        ElevatedButtonWithIcon.SMALL.mask(lockerPageButton, Icons.HOME);
        ElevatedButtonWithIcon.SMALL.mask(zonePageButton, Icons.LOCATION);
        ElevatedButtonWithIcon.SMALL.mask(historyPageButton, Icons.HISTORY);
    }

    protected void initEvents() {
        lockerPageButton.setOnAction(e-> onLockerButtonClick());
        zonePageButton.setOnAction(e -> onZoneButtonClick());
        historyPageButton.setOnAction(e -> onHistoryButtonClick());
//        lockerReserveButton.setOnAction(e -> openLockerReserveDialog());
    }

    public Button getFooterNavButton() {
        return lockerReserveButton;
    }

    protected void onLockerButtonClick() {
        try {
            FXRouter.goTo("user-home");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
            FXRouter.goTo("user-zone");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected void onHistoryButtonClick() {
        try {
            FXRouter.goTo("user-history");
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
