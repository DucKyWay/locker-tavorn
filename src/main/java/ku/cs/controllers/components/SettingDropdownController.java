package ku.cs.controllers.components;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import ku.cs.models.account.Account;
import ku.cs.services.utils.AlertUtil;
import ku.cs.services.SessionManager;

public class SettingDropdownController {

    @FXML private ComboBox<String> settingComboBox;

    Account current = SessionManager.getCurrentAccount();

    @FXML
    public void initialize() {
        settingComboBox.getItems().setAll(
                "เปลี่ยนโปรไฟล์",
                "เปลี่ยนรหัสผ่าน",
                "ออกจากระบบ"
        );
        settingComboBox.setPromptText("Settings");
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
        settingComboBox.setPromptText("Settings");
    }

    protected void onChangePasswordButtonClick() {
        new ChangePasswordPopup().run(current);
        settingComboBox.setPromptText("Settings");
    }


    protected void onLogoutButtonClick() {
        AlertUtil.confirm("Confirm Logout", "คุณต้องการออกจากระบบหรือไม่?")
            .ifPresent(btn -> {
                if (btn == ButtonType.OK) {
                    SessionManager.logout();
                }
            });
    }
}
