package ku.cs.controllers.admin;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.util.Callback;
import ku.cs.components.Icon;
import ku.cs.components.Icons;
import ku.cs.components.LabelStyle;
import ku.cs.components.button.FilledButton;
import ku.cs.components.button.FilledButtonWithIcon;
import ku.cs.components.button.IconButton;
import ku.cs.controllers.components.AdminNavbarController;
import ku.cs.models.account.*;

import ku.cs.services.FXRouter;
import ku.cs.services.SessionManager;
import ku.cs.services.datasources.Datasource;
import ku.cs.services.datasources.OfficerListFileDatasource;
import ku.cs.services.utils.AlertUtil;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;

public class AdminManageOfficersController {

    private static final int PROFILE_SIZE = 40;
    private static final String DEFAULT_AVATAR = "/ku/cs/images/default_profile.png";

    @FXML private TableView<Officer> officersTableView;

    @FXML private HBox parentHBoxFilled;
    private Label headerLabel;
    private Label descriptionLabel;
    private Button addNewOfficerFilledButton;

    @FXML private AdminNavbarController adminNavbarController;
    private Button footerNavBarButton;

    OfficerList officers;
    Datasource<OfficerList> datasource;

    Account current;

    @FXML public void initialize() throws FileNotFoundException {

        SessionManager.requireAdminLogin();
        current = SessionManager.getCurrentAccount();

        footerNavBarButton = adminNavbarController.getFooterNavButton();

        initDatasources();
        initUserInterfaces();
        initEvents();

        showTable(officers);
    }

    private void initDatasources() {
        datasource = new OfficerListFileDatasource("data", "test-officer-data.json");
        officers = datasource.readData();
    }

    private void initUserInterfaces() {
        Region region = new Region();
        VBox vBox = new VBox();

        parentHBoxFilled.setSpacing(4);
        region.setPrefSize(620, 50);

        footerNavBarButton.setText("ย้อนกลับ");

        headerLabel = new Label("จัดการพนักงาน");
        descriptionLabel = new Label("ด้วย " + current.getUsername());
        addNewOfficerFilledButton = new FilledButton("เพิ่มพนักงานใหม่");

        LabelStyle.TITLE_LARGE.applyTo(headerLabel);
        LabelStyle.TITLE_SMALL.applyTo(descriptionLabel);

        vBox.getChildren().addAll(headerLabel, descriptionLabel);

        parentHBoxFilled.getChildren().addAll(vBox, region, addNewOfficerFilledButton);
    }

    private void initEvents() {
        footerNavBarButton.setOnAction(e -> onBackButtonClick());
        addNewOfficerFilledButton.setOnAction(e -> onAddNewOfficerButtonClick());
    }

    private void showTable(OfficerList officers) {
        officersTableView.getColumns().setAll(
                createProfileColumn(),
                createTextColumn("ชื่อผู้ใช้", "username"),
                createTextColumn("ชื่อ", "fullName"),
                createTextColumn("อีเมล", "email", 180),
                createTextColumn("เบอร์มือถือ", "phone", 0, "-fx-alignment: CENTER;"),
                createTextColumn("ตำแหน่ง", "role", 0, "-fx-alignment: CENTER;"),
                createActionColumn()
        );

        officersTableView.getItems().setAll(officers.getOfficers());
        officersTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    private <T> TableColumn<Officer, T> createTextColumn(String title, String property) {
        TableColumn<Officer, T> col = new TableColumn<>(title);
        col.setCellValueFactory(new PropertyValueFactory<>(property));
        return col;
    }

    private <T> TableColumn<Officer, T> createTextColumn(String title, String property, double minWidth) {
        TableColumn<Officer, T> col = createTextColumn(title, property);
        if (minWidth > 0) col.setMinWidth(minWidth);
        return col;
    }

    private <T> TableColumn<Officer, T> createTextColumn(String title, String property, double minWidth, String style) {
        TableColumn<Officer, T> col = createTextColumn(title, property);
        if (minWidth > 0) col.setMinWidth(minWidth);
        if (style != null) col.setStyle(style);
        return col;
    }

    private TableColumn<Officer, String> createProfileColumn() {
        TableColumn<Officer, String> profileColumn = new TableColumn<>();
        profileColumn.setCellValueFactory(new PropertyValueFactory<>("imagePath"));

        profileColumn.setCellFactory(col -> new TableCell<>() {
            private final ImageView imageView = new ImageView();

            @Override
            protected void updateItem(String imagePath, boolean empty) {
                super.updateItem(imagePath, empty);

                if (empty) {
                    setGraphic(null);
                    return;
                }

                Image image;
                try {
                    if (imagePath != null && !imagePath.isBlank()) {
                        image = new Image("file:" + imagePath, PROFILE_SIZE, PROFILE_SIZE, true, true);
                        if (image.isError()) throw new Exception("Invalid image");
                    } else {
                        throw new Exception("No imagePath");
                    }
                } catch (Exception e) {
                    // Default
                    image = new Image(
                            getClass().getResource(DEFAULT_AVATAR).toExternalForm(),
                            PROFILE_SIZE, PROFILE_SIZE, true, true
                    );
                }

                imageView.setImage(image);
                imageView.setFitWidth(PROFILE_SIZE);
                imageView.setFitHeight(PROFILE_SIZE);

                // clip เป็นวงกลม
                Circle clip = new Circle(PROFILE_SIZE / 2.0, PROFILE_SIZE / 2.0, PROFILE_SIZE / 2.0);
                imageView.setClip(clip);

                setGraphic(imageView);
            }
        });

        profileColumn.setPrefWidth(60);
        profileColumn.setStyle("-fx-alignment: CENTER;");
        return profileColumn;
    }


    private TableColumn<Officer, Void> createActionColumn() {
        TableColumn<Officer, Void> actionColumn = new TableColumn<>("จัดการ");
        actionColumn.setCellFactory(col -> new TableCell<>() {
            private final IconButton passBtn = new IconButton(new Icon(Icons.KEY, 24, "#EF4444"));
            private final FilledButtonWithIcon editBtn = FilledButtonWithIcon.small("แก้ไข", Icons.EDIT);
            private final FilledButtonWithIcon deleteBtn = FilledButtonWithIcon.small("ลบ", Icons.DELETE);

            {
                passBtn.setOnAction(e -> showDefaultPassword(getTableView().getItems().get(getIndex())));
                editBtn.setOnAction(e -> editOfficer(getTableView().getItems().get(getIndex())));
                deleteBtn.setOnAction(e -> deleteOfficer(getTableView().getItems().get(getIndex())));
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : new HBox(5, passBtn, editBtn, deleteBtn));
            }
        });
        actionColumn.setPrefWidth(180);
        actionColumn.setStyle("-fx-alignment: CENTER;");
        return actionColumn;
    }

    private void showDefaultPassword(Officer officer) {
        if(!officer.isStatus()) {
            AlertUtil.info("Default Password",
                    officer.getUsername() + " hasn't change password." + "\n" +
                            "Default Password is "+ officer.getDefaultPassword());
        } else {
            AlertUtil.info("Default Password", officer.getUsername() + " password has changed.");
        }
    }

    // ======== Manage Button Column ========
    private void editOfficer(Officer officer) {
        try {
            FXRouter.goTo("admin-manage-officer-details", officer.getUsername());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void deleteOfficer(Officer officer) {
        AlertUtil.confirm(
                "Warning",
                "Do you want to remove " + officer.getUsername() + "?"
        ).ifPresent(response -> {
            if (response == ButtonType.OK) {
                officers.removeOfficer(officer);
                datasource.writeData(officers);
                showTable(officers);
            }
        });
    }

    protected void onBackButtonClick() {
        try {
            FXRouter.goTo("admin-home", current);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    protected void onAddNewOfficerButtonClick() {
        try {
            FXRouter.goTo("admin-manage-new-officer", current);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
