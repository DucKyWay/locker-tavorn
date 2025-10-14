package ku.cs.controllers.components;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import ku.cs.components.Icon;
import ku.cs.components.Icons;
import ku.cs.components.button.IconButton;
import ku.cs.models.account.Account;
import ku.cs.services.ui.FXRouter;
import ku.cs.services.ui.ThemeProvider;
import ku.cs.services.ui.FontFamily;
import ku.cs.services.ui.FontScale;
import ku.cs.services.utils.AlertUtil;
import ku.cs.services.session.SessionManager;

public class SettingDropdownController {
    private final SessionManager sessionManager = (SessionManager) FXRouter.getService("session");
    private final AlertUtil alertUtil = new AlertUtil();

    @FXML private ComboBox<String> settingComboBox;
    @FXML private Button settingIconButton;

    private final ThemeProvider themeProvider = ThemeProvider.getInstance();
    Account current;

    @FXML
    public void initialize() {
        current = sessionManager.getCurrentAccount();

        IconButton.mask(settingIconButton,new Icon(Icons.GEAR));
        refreshMenuItems();

        settingComboBox.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setText((empty || item == null) ? "ตั้งค่าโปรแกรม" : item);
            }
        });

    }

    private void refreshMenuItems() {
        String fontFamilyLabel = (themeProvider.getFontFamily() == FontFamily.BAI_JAMJUREE) ? "Bai Jamjuree" : "Sarabun";
        String scaleLabel = (themeProvider.getFontScale() == FontScale.REGULAR) ? "ปกติ" : "ใหญ่";

        settingComboBox.getItems().setAll(
                "เปลี่ยนโปรไฟล์",
                "เปลี่ยนรหัสผ่าน",
                "เปลี่ยนฟอนต์: " + fontFamilyLabel,
                "ขนาดตัวอักษร: " + scaleLabel,
                "ออกจากระบบ"
        );
    }

    @FXML
    protected void onSelectSettingDropdown(ActionEvent e) {
        String selected = settingComboBox.getValue();
        if (selected == null) return;

        switch (selected) {
            case "เปลี่ยนโปรไฟล์" -> onChangeProfileButtonClick();
            case "เปลี่ยนรหัสผ่าน" -> onChangePasswordButtonClick();
            case "ออกจากระบบ"      -> onLogoutButtonClick();
            default -> {
                if (selected.startsWith("เปลี่ยนฟอนต์")) {
                    onToggleFontFamily();
                } else if (selected.startsWith("ขนาดตัวอักษร")) {
                    onToggleFontScale();
                }
            }
        }
    }

    private void onToggleFontFamily() {
        themeProvider.toggleFontFamily();
        Platform.runLater(() -> {
            refreshMenuItems();
            resetSettingComboBox();
        });
    }

    private void onToggleFontScale() {
        themeProvider.toggleFontScale();
        Platform.runLater(() -> {
            refreshMenuItems();
            resetSettingComboBox();
        });
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
        alertUtil.confirm("ยืนยันการออกจากระบบ", "คุณต้องการออกจากระบบหรือไม่?")
                .ifPresent(btn -> {
                    if (btn == ButtonType.OK) {
                        sessionManager.logout();
                    }
                    resetSettingComboBox();
                });
    }

    protected void resetSettingComboBox() {
        Platform.runLater(() -> settingComboBox.setValue(null));
    }
}
