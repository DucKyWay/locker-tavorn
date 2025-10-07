package ku.cs.controllers.officer.DialogPane;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import ku.cs.components.button.FilledButton;
import ku.cs.models.key.Key;
import ku.cs.models.key.KeyList;
import ku.cs.models.key.KeyType;
import ku.cs.models.locker.Locker;
import ku.cs.models.locker.LockerList;
import ku.cs.models.locker.LockerType;
import ku.cs.models.request.Request;
import ku.cs.models.request.RequestList;
import ku.cs.models.request.RequestType;
import ku.cs.models.zone.Zone;
import ku.cs.models.zone.ZoneList;
import ku.cs.services.datasources.provider.KeyDatasourceProvider;
import ku.cs.services.datasources.provider.LockerDatasourceProvider;
import ku.cs.services.datasources.provider.RequestDatasourceProvider;
import ku.cs.services.datasources.provider.ZoneDatasourceProvider;
import ku.cs.services.ui.FXRouter;
import ku.cs.services.utils.ImageUploadUtil;

public class OfficerRequestInfoDialogController {
    private final ZoneDatasourceProvider zonesProvider = new ZoneDatasourceProvider();
    private final LockerDatasourceProvider lockersProvider = new LockerDatasourceProvider();
    private final KeyDatasourceProvider keysProvider = new KeyDatasourceProvider();
    private final RequestDatasourceProvider requestsProvider = new RequestDatasourceProvider();
    private final ImageUploadUtil imageUploadUtil = new ImageUploadUtil();
    @FXML
    private AnchorPane lockerDialogPane;
    @FXML private ImageView itemImage;

    @FXML private Label lockerNumberLabel;

    @FXML private Label statusLabel;
    @FXML private Label priceLabel;

    @FXML private Label lockerIdLabel;
    @FXML private Label lockerZoneLabel;
    @FXML private Label lockerTypeLabel;
    @FXML private Label lockerKeyTypeLabel;

    @FXML private Label startDateLabel;
    @FXML private Label endDateLabel;

    @FXML private HBox containerHBox;
    @FXML private Button closeLockerButton;
    RequestList requestList;
    Request request;
    LockerList lockerList;
    Locker locker;

    KeyList keyList;
    Key key;

    ZoneList zoneList;
    Zone zone;

    @FXML
    private void initialize() {
        request = (Request) FXRouter.getData();
        initializeDatasource();
        initUserInterface();
        initEvents();
        refreshContainerUI();
    }

    private void initializeDatasource() {
        zoneList = zonesProvider.loadCollection();
        zone = zoneList.findZoneByName(request.getZoneName());

        lockerList = lockersProvider.loadCollection(zone.getZoneUid());
        locker = lockerList.findLockerByUid(request.getLockerUid());

        requestList =  requestsProvider.loadCollection(zone.getZoneUid());
        request = requestList.findRequestByUid(request.getRequestUid());


        System.out.println("locker: " + request.getLockerUid() );
        System.out.println("data/lockers" + "/zone-"+zone.getZoneUid()+ ".json");

        lockerNumberLabel.setText(request.getLockerUid());
        statusLabel.setText(request.getRequestType().toString());
        lockerIdLabel.setText(request.getLockerUid());
        lockerZoneLabel.setText(request.getZoneName());
        lockerTypeLabel.setText(locker.getLockerType().toString());
        startDateLabel.setText(request.getStartDate().toString());
        endDateLabel.setText(request.getEndDate().toString());
    }

    private void initUserInterface() {
        if(!request.getImagePath().isBlank() && request.getImagePath()!=null) {
            Image image = new Image("file:" + request.getImagePath(), 230, 230, true, true);
            itemImage.setImage(image);
        }
        FilledButton.MEDIUM.mask(closeLockerButton);
    }

    private void initEvents() {
        if (closeLockerButton != null) {
            closeLockerButton.setOnAction(e -> onCloseButtonClick());
        }
    }

    private void refreshContainerUI() {
        containerHBox.getChildren().clear();

        RequestType status = request.getRequestType();

        switch (status) {
            case RequestType.APPROVE:
                switch (locker.getLockerType()) {
                    case LockerType.DIGITAL:
                        renderApproveDigitalOrChain();
                        break;
                    case LockerType.MANUAL:
                        keyList = keysProvider.loadCollection(zone.getZoneUid());
                        key = keyList.findKeyByUuid(request.getLockerKeyUid());
                        KeyType keyType = key.getKeyType();
                        switch (keyType) {
                            case KeyType.MANUAL:
                                lockerKeyTypeLabel.setText(keyType.toString());
                                renderApproveManual();
                                break;
                            case KeyType.CHAIN:
                                lockerKeyTypeLabel.setText(keyType.toString());
                                renderApproveDigitalOrChain();
                                break;
                        }
                        break;
                    default:
                        containerHBox.getChildren().add(new Text("Unknown key type"));
                }
                break;
            default:
                containerHBox.getChildren().add(new Text("Unknown status"));
        }
    }

    private void renderApproveDigitalOrChain() {
        VBox box = new VBox(6);
        box.setFillWidth(true);
        Label passkey = new Label("");
        Label title = new Label("Set digital code");
        if(locker.getLockerType().equals(LockerType.DIGITAL)) {
            title = new Label("Digital code: ");
            passkey.setText(locker.getPassword());

        }else{
            title = new Label("Chain code: ");
            passkey.setText(key.getPasskey());
        }
        box.getChildren().addAll(title,passkey);
        containerHBox.getChildren().add(box);
    }

    private void renderApproveManual() {
        VBox box = new VBox(4);
        HBox r1 = new HBox(6);
        HBox r2 = new HBox(6);
        r1.setAlignment(Pos.CENTER_LEFT);
        r2.setAlignment(Pos.CENTER_LEFT);

        r1.getChildren().addAll(new Label("Key Code:"), new Label(key.getPasskey()));
        r2.getChildren().addAll(new Label("Key UUID:"), new Label(key.getKeyUid()));

        box.getChildren().addAll(r1, r2);
        containerHBox.getChildren().add(box);
    }
    private void onCloseButtonClick() {
        if (lockerDialogPane != null && lockerDialogPane.getScene() != null && lockerDialogPane.getScene().getWindow() != null) {
            lockerDialogPane.getScene().getWindow().hide();
        }

    }
}
