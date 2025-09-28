package ku.cs.controllers.components;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import ku.cs.components.Icon;
import ku.cs.components.Icons;
import ku.cs.models.account.Account;
import ku.cs.services.AppContext;
import ku.cs.services.utils.AlertUtil;
import ku.cs.services.SessionManager;

public class SettingDropdownController {
    private final SessionManager sessionManager = AppContext.getSessionManager();

    @FXML private ComboBox<String> settingComboBox;
    @FXML private Label settingIconLabel;

    Account current = sessionManager.getCurrentAccount();

    @FXML
    public void initialize() {
        settingIconLabel.setGraphic(new Icon(Icons.GEAR, 24));
        settingComboBox.getItems().setAll(
                "เปลี่ยนโปรไฟล์",
                "เปลี่ยนรหัสผ่าน",
                "ออกจากระบบ"
        );

        settingComboBox.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText("Settings"); // fallback
                } else {
                    setText(item);
                }
            }
        });
    }

    @FXML
    protected void onSelectSettingDropdown(ActionEvent e) {
        String selected = settingComboBox.getValue();
        if (selected == null) return;

        switch (selected) {
            case "เปลี่ยนโปรไฟล์" -> onChangeProfileButtonClick();
            case "เปลี่ยนรหัสผ่าน" -> onChangePasswordButtonClick();
            case "ออกจากระบบ"  -> onLogoutButtonClick();
        }
    }

    protected void onChangeProfileButtonClick() {
        new UploadProfilePopup().run(current);
        resetSettingComboBox();
    }

    protected void onChangePasswordButtonClick() {
        new ChangePasswordPopup().run(current);
        resetSettingComboBox();
    }


    protected void onLogoutButtonClick() {
        AlertUtil.confirm("Confirm Logout", "คุณต้องการออกจากระบบหรือไม่?")
            .ifPresent(btn -> {
                if (btn == ButtonType.OK) {
                    sessionManager.logout();
                } resetSettingComboBox();
            });
    }

    protected void resetSettingComboBox() {
        Platform.runLater(() -> { // async on fxml
            settingComboBox.setValue(null);
        });
    }

}
