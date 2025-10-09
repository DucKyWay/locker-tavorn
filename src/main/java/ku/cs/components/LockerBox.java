package ku.cs.components;

import javafx.css.PseudoClass;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import ku.cs.models.locker.Locker;
import ku.cs.models.locker.LockerSizeType;
import ku.cs.models.locker.LockerType;

import java.util.Objects;

public class LockerBox extends Button {
    private final Locker locker;

    public LockerBox(Locker locker) {
        this.locker = Objects.requireNonNull(locker, "locker cannot be null");
        initialize();
    }

    private void initialize() {
        getStyleClass().add("locker-box");
        setPadding(Insets.EMPTY);

        final AnchorPane root = buildRoot();
        setGraphic(root);

        setOpacity(locker.isStatus() && locker.isAvailable() ? 1.0 : 0.3);
    }

    private AnchorPane buildRoot() {
        AnchorPane root = new AnchorPane();

        Rectangle clip = new Rectangle();
        clip.setArcWidth(10.0);
        clip.setArcHeight(10.0);
        clip.widthProperty().bind(root.widthProperty());
        clip.heightProperty().bind(root.heightProperty());
        root.setClip(clip);

        Label uidLabel = new Label(locker.getLockerUid());
        uidLabel.getStyleClass().addAll("lockerUid", "body-small");
        AnchorPane.setLeftAnchor(uidLabel, 6.0);
        AnchorPane.setTopAnchor(uidLabel, 6.0);

        VBox metaBox = new VBox(4.0);
        metaBox.getStyleClass().add("lockerStatus");
        AnchorPane.setRightAnchor(metaBox, 6.0);
        AnchorPane.setTopAnchor(metaBox, 6.0);
        metaBox.getChildren().addAll(
                buildStatusCell(),
                buildKeyCell(locker.getLockerType()),
                buildSizeCell(locker.getLockerSizeType())
        );

        Label bigNum = new Label(Integer.toString(locker.getLockerId()));
        bigNum.getStyleClass().add("number");
        AnchorPane.setRightAnchor(bigNum, -9.0);
        AnchorPane.setBottomAnchor(bigNum, -40.0);


        root.getChildren().addAll(uidLabel, metaBox, bigNum);

        if (!locker.isAvailable() || !locker.isStatus()) {
            HBox banner = new HBox();
            banner.getStyleClass().add("banner");
            banner.setAlignment(Pos.CENTER);
            banner.setRotate(22.0);
            banner.setMinSize(207, 69);
            banner.setPrefSize(283, 69);
            banner.setMaxSize(300, 100);

            AnchorPane.setTopAnchor(banner, 78.0);
            AnchorPane.setBottomAnchor(banner, 78.0);
            AnchorPane.setLeftAnchor(banner, -71.0);
            AnchorPane.setRightAnchor(banner, -72.0);

            Label label = new Label();
            label.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);

            if (locker.isAvailable()) {
                label.setText("ชำรุด");
            }else if (locker.isStatus()) {
                label.setText("ไม่ว่าง");
            }else {
                label.setText("พัง");
            }

            banner.getChildren().add(label);
            root.getChildren().add(banner);
        }

        return root;
    }

    private HBox buildStatusCell() {
        HBox box = squareCellBase();
        Circle dot = new Circle(4.5);

        if (locker.isStatus() && locker.isAvailable()) {
            dot.getStyleClass().add("fill-success");
        } else if (locker.isAvailable()) {
            dot.getStyleClass().add("fill-on-disabled");
        } else if (locker.isStatus()) {
            dot.getStyleClass().add("fill-error");
        } else {
            dot.getStyleClass().add("fill-transparent");
        }

        box.getChildren().add(dot);
        return box;
    }

    private HBox buildKeyCell(LockerType type) {
        HBox box = new HBox();
        box.getStyleClass().add("keyHBox");

        Icon key = new Icon(16);
        if (type == LockerType.DIGITAL) {
            key.setIcon(Icons.PASSWORD);
            key.getStyleClass().add("text-info-on-container");
            box.getStyleClass().add("bg-info-container");
        } else if (type == LockerType.MANUAL) {
            key.setIcon(Icons.KEY);
            key.getStyleClass().add("text-warning-on-container");
            box.getStyleClass().add("bg-warning-container");
        } else {
            key.setIcon(Icons.NULL);
        }

        box.getChildren().add(key);
        return box;
    }

    private HBox buildSizeCell(LockerSizeType sizeType) {
        HBox box = new HBox();
        box.getStyleClass().add("sizeHBox");
        Label size = new Label(sizeType.getDescription());
        box.getChildren().add(size);
        return box;
    }

    private HBox squareCellBase() {
        HBox box = new HBox();
        box.setAlignment(Pos.CENTER);
        box.setSpacing(8);
        box.setPadding(new Insets(13));
        box.setMinSize(24, 24);
        box.setPrefSize(24, 24);
        box.setMaxSize(24, 24);
        box.setStyle("-fx-background-radius: 8; -fx-border-radius: 8;");
        box.getStyleClass().add("bg-elevated");
        return box;
    }
}
