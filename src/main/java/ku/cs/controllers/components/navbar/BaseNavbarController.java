package ku.cs.controllers.components.navbar;

import javafx.scene.control.Button;
import ku.cs.components.button.ElevatedButtonWithIcon;
import ku.cs.components.button.FilledButtonWithIcon;
import ku.cs.components.Icons;
import ku.cs.services.ui.FXRouter;
import ku.cs.services.utils.AlertUtil;
import ku.cs.services.session.SessionManager;

import javafx.css.PseudoClass;
import javafx.scene.control.ButtonType;
import java.io.IOException;
import java.util.Map;

public abstract class BaseNavbarController {

    private final SessionManager sessionManager = (SessionManager) FXRouter.getService("session");
    protected final AlertUtil alertUtil = new AlertUtil();

    // bind icon to button
    protected void applyIcon(Button button, Icons icon, boolean filled) {
        if (filled) {
            FilledButtonWithIcon.SMALL.mask(button, icon);
        } else {
            ElevatedButtonWithIcon.SMALL.mask(button, icon);
        }
    }

    // attach action that routes
    protected void routeOnClick(Button button, String route) {
        button.setOnAction(e -> {
            try {
                FXRouter.goTo(route);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
    }

    // attach action that routes with data
    protected void routeOnClick(Button button, String route, Object data) {
        button.setOnAction(e -> {
            try {
                FXRouter.goTo(route, data);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
    }

    // highlight current route
    protected void highlightCurrentRoute(Map<String, Button> routeToButton) {
        String current = FXRouter.getCurrentRouteLabel();
        if (routeToButton.containsKey(current)) {
            routeToButton.get(current)
                    .pseudoClassStateChanged(PseudoClass.getPseudoClass("selected"), true);
        }
    }

    // logout
    protected void bindLogout(Button logoutButton) {
        applyIcon(logoutButton, Icons.SIGN_OUT, true);
        logoutButton.setOnAction(e ->
                alertUtil.confirm("ยืนยันการออกจากระบบ", "คุณต้องการออกจากระบบหรือไม่?")
                        .ifPresent(btn -> {
                            if (btn == ButtonType.OK) sessionManager.logout();
                        })
        );
    }

    public abstract Button getFooterNavButton();
}
