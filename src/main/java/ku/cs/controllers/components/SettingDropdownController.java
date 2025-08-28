package ku.cs.controllers.components;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import ku.cs.models.Account;
import ku.cs.services.AlertUtil;
import ku.cs.services.SessionManager;

public class SettingDropdownController {

    @FXML private ComboBox<String> settingComboBox;

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
        AlertUtil.info("ยังไม่มีระบบจ้า", "เดี๋ยวอนาคตมีนะจั๊บ");
    }

    protected void onChangePasswordButtonClick() {
        Account current = SessionManager.getCurrentAccount();
        new ChangePasswordAlert().run(current);
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
