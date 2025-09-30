package ku.cs.controllers.components;

import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;
import ku.cs.models.account.Account;
import ku.cs.services.AppContext;
import ku.cs.services.SessionManager;

import java.io.InputStream;
import java.net.URL;
import java.util.Objects;

public class MiniProfileCardController {

    @FXML private ImageView userImage;
    @FXML private Label fullName;
    @FXML private Label userName;

    private Image defaultAvatar;
    private Account account;

    private final SessionManager sessionManager = AppContext.getSessionManager();

    @FXML
    private void initialize() {
        account = sessionManager.getCurrentAccount();

        String imagePath = account.getImagePath();
        try {
            if (imagePath != null && !imagePath.isBlank()) {
                defaultAvatar = new Image("file:" + imagePath, 48, 48, true, true);
                if (defaultAvatar.isError()) throw new Exception("Invalid image");
            } else {
                throw new Exception("No imagePath");
            }
        } catch (Exception e) {
            defaultAvatar = new Image(
                    Objects.requireNonNull(getClass().getResource("/ku/cs/images/default_profile.png")).toExternalForm(),
                    48, 48, true, true
            );
        }

        if (defaultAvatar != null) {
            userImage.setImage(defaultAvatar);
        }

        fullName.setText(account.getFullName());
        userName.setText(account.getUsername());
        initUserInterface();
    }

    private void initUserInterface() {
        userImage.setFitWidth(48);
        userImage.setFitHeight(48);
        userImage.setPreserveRatio(true);
        userImage.setSmooth(true);

        Circle clip = new Circle();
        clip.centerXProperty().bind(userImage.fitWidthProperty().divide(2));
        clip.centerYProperty().bind(userImage.fitHeightProperty().divide(2));
        clip.radiusProperty().bind(
                Bindings.min(userImage.fitWidthProperty(), userImage.fitHeightProperty()).divide(2)
        );
        userImage.setClip(clip);
    }

    public void setProfile(String fullName, String username, Image avatar) {
        setFullName(fullName);
        setUserName(username);
        setAvatar(avatar);
    }

    public void setProfile(String fullName, String username, String imageUrlOrPath) {
        setFullName(fullName);
        setUserName(username);
        setAvatar(imageUrlOrPath);
    }

    public void setFullName(String name) {
        fullName.setText(Objects.requireNonNullElse(name, "-"));
    }

    public void setUserName(String uname) {
        userName.setText(Objects.requireNonNullElse(uname, "-"));
    }

    public void setAvatar(Image image) {
        if (image != null && !image.isError()) {
            userImage.setImage(image);
        } else if (defaultAvatar != null) {
            userImage.setImage(defaultAvatar);
        }
    }

    /** ตั้งรูปจาก URL/ไฟล์โลคัล/คลาสพาธ (พยายามเดา path ให้) */
    public void setAvatar(String urlOrPath) {
        if (urlOrPath == null || urlOrPath.isBlank()) {
            setAvatar((Image) null);
            return;
        }

        Image img = null;

        // 1) ถ้าเป็น absolute URL (http/https/file)
        try {
            URL u = new URL(urlOrPath);
            img = new Image(u.toExternalForm(), true);
        } catch (Exception ignore) { /* not an absolute URL */ }

        // 2) ถ้าเป็นไฟล์โลคัล
        if (img == null) {
            try {
                img = new Image("file:" + urlOrPath, true);
                if (img.isError()) img = null;
            } catch (Exception ignore) {}
        }

        // 3) ถ้าอยู่ใน resources (classpath)
        if (img == null) {
            Image res = loadResourceImage(urlOrPath.startsWith("/") ? urlOrPath : "/" + urlOrPath);
            if (res != null) img = res;
        }

        setAvatar(img);
    }

    /* =========================
       Utilities
       ========================= */

    private Image loadResourceImage(String classpath) {
        try (InputStream in = getClass().getResourceAsStream(classpath)) {
            if (in != null) return new Image(in);
        } catch (Exception ignore) {}
        return null;
    }

    /* =========================
       Getters (ถ้าจำเป็นต้อง bind ต่อ)
       ========================= */
    public ImageView getUserImage() { return userImage; }
    public Label getFullNameLabel() { return fullName; }
    public Label getUserNameLabel() { return userName; }
}
