package ku.cs.controllers.locker;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import ku.cs.components.LabelStyle;
import ku.cs.models.key.Key;
import ku.cs.models.key.KeyList;
import ku.cs.models.locker.Locker;
import ku.cs.models.locker.LockerList;
import ku.cs.models.locker.LockerType;
import ku.cs.models.request.Request;
import ku.cs.services.datasources.provider.KeyDatasourceProvider;
import ku.cs.services.datasources.provider.LockerDatasourceProvider;
import ku.cs.services.utils.QrCodeGenerator;
import ku.cs.services.ui.FXRouter;

public class LockerQrDialogController {
    private final LockerDatasourceProvider lockersProvider = new LockerDatasourceProvider();
    private final KeyDatasourceProvider keysProvider = new KeyDatasourceProvider();

    private LockerList lockers;
    private KeyList keys;
    private Locker locker;
    private Key key;

    @FXML private ImageView qrImageView;
    @FXML private Label codeLabel;

    @FXML
    public void initialize() {
        Request request = (Request) FXRouter.getData();
        if (request == null) return;
        lockers = lockersProvider.loadCollection(request.getZoneUid());
        locker = lockers.findLockerByUid(request.getLockerUid());

        keys = keysProvider.loadCollection(request.getZoneUid());
        key = keys.findKeyByUuid(request.getLockerKeyUid());

        String password = null;

        switch (locker.getLockerType()) {
            case LockerType.DIGITAL:
                password = locker.getPassword();
                break;
            case LockerType.MANUAL:
                password = key.getPasskey();
                break;
        }

        LabelStyle.LABEL_SMALL.applyTo(codeLabel);

        String qrContent = "LOCKER:" + request.getLockerUid() + ":" + password;
        qrImageView.setImage(new QrCodeGenerator().generate(qrContent, 100));

        codeLabel.setText("รหัสล็อกเกอร์: " + password);
    }
}
