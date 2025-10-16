package ku.cs.controllers.officer;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import ku.cs.components.Icon;
import ku.cs.components.Icons;
import ku.cs.components.button.*;
import ku.cs.models.locker.Locker;
import ku.cs.models.locker.LockerList;
import ku.cs.models.locker.LockerSizeType;
import ku.cs.models.locker.LockerType;
import ku.cs.services.datasources.provider.LockerDatasourceProvider;
import ku.cs.services.locker.LockerService;
import ku.cs.services.request.RequestService;
import ku.cs.services.ui.FXRouter;
import ku.cs.services.utils.*;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

public class OfficerManageLockersController extends BaseOfficerController{
    private final RequestService requestService = new RequestService();
    private final LockerDatasourceProvider lockersProvider = new LockerDatasourceProvider();
    private final SearchService<Locker> searchService = new SearchService<>();
    private final TableColumnFactory tableColumnFactory = new TableColumnFactory();

    private LockerList lockers;

    @FXML private TableView<Locker> lockersTableView;

    @FXML private TextField searchTextField;
    @FXML private Button searchButton;

    @FXML private Button exportLockersToPdfButton;

    @FXML private Button addlockerButton;
    @FXML private ComboBox<LockerType> typeLockerComboBox;
    @FXML private ComboBox<LockerSizeType> sizeLockerComboBox;

    @FXML private Button officerZoneRouteLabelButton;
    @FXML private Button officerManageLockerRouteLabelButton;


    @Override
    protected void initDatasource() {
        lockers = lockersProvider.loadCollection(currentZone.getZoneUid());
    }

    @Override
    protected void initUserInterfaces() {
        OutlinedButtonWithIcon.SMALL.mask(exportLockersToPdfButton, null, Icons.EXPORT);
        FilledButtonWithIcon.SMALL.mask(addlockerButton, null, Icons.PLUS);

        ElevatedButtonWithIcon.LABEL.mask(officerZoneRouteLabelButton, Icons.LOCATION);
        ElevatedButtonWithIcon.LABEL.mask(officerManageLockerRouteLabelButton, Icons.TAG);
        IconButton.mask(searchButton, new Icon(Icons.MAGNIFYING_GLASS));

        officerZoneRouteLabelButton.setText(currentZone.getZoneName());

        showTable(lockers);
    }

    @Override
    protected void initEvents() {
        requestService.updateData();
        officerManageLockerRouteLabelButton.setOnAction(e -> onBackButtonClick());
        searchTextField.textProperty().addListener((obs, oldValue, newValue) -> {
            onSearch();
        });
        searchButton.setOnAction(e -> onSearch());

        exportLockersToPdfButton.setOnAction(e -> onExportQrPdfToSelectFolder());

        lockersTableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Locker>() {
            @Override
            public void changed(ObservableValue<? extends Locker> observableValue, Locker oldLocker, Locker newLocker) {
                if(newLocker !=null){
                    infoLocker(newLocker);
                }
            }
        });

        typeLockerComboBox.setItems(FXCollections.observableArrayList(LockerType.values()));
        sizeLockerComboBox.setItems(FXCollections.observableArrayList(LockerSizeType.values()));
        typeLockerComboBox.setCellFactory(param -> new ListCell<LockerType>() {
            @Override
            protected void updateItem(LockerType item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getDescription());
                }
            }
        });
        typeLockerComboBox.setButtonCell(new ListCell<LockerType>() {
            @Override
            protected void updateItem(LockerType item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getDescription());
                }
            }
        });
        sizeLockerComboBox.setCellFactory(param -> new ListCell<LockerSizeType>() {
            @Override
            protected void updateItem(LockerSizeType item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getDescription());
                }
            }
        });

        sizeLockerComboBox.setButtonCell(new ListCell<LockerSizeType>() {
            @Override
            protected void updateItem(LockerSizeType item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getDescription());
                }
            }
        });
        addlockerButton.setOnAction(e -> onAddLockerButton());

    }
    private void onAddLockerButton() {
        LockerType selectedType = typeLockerComboBox.getValue();
        LockerSizeType selectedSize = sizeLockerComboBox.getValue();
        if (selectedType == null || selectedSize == null) {
            new AlertUtil().error("ข้อมูลไม่ครบถ้วน", "กรุณาเลือกประเภทและขนาดของล็อคเกอร์ก่อนเพิ่ม");
            return;
        }
        LockerService lockerService = new LockerService();
        Locker newLocker = lockerService.createLocker(selectedType, selectedSize, currentZone.getZoneUid());
        lockers.addLocker(newLocker);
        lockersProvider.saveCollection(currentZone.getZoneUid(), lockers);

        new AlertUtil().info("สำเร็จ", "เพิ่มล็อคเกอร์หมายเลข " + newLocker.getLockerUid() + " เรียบร้อยแล้ว");
        showTable(lockers);
    }
    private void showTable(LockerList lockersTable) {
        lockersTableView.getColumns().clear();
        lockersTableView.getItems().clear();

        lockersTableView.getColumns().setAll(
                createLockerIdColumn(),
                tableColumnFactory.createTextColumn("เลขล็อคเกอร์", "lockerUid", 105),
                tableColumnFactory.createEnumStatusColumn("ประเภทล็อคเกอร์", "lockerType",-1),
                tableColumnFactory.createEnumStatusColumn("ขนาดล็อคเกอร์", "lockerSizeType", -1),
                tableColumnFactory.createStatusColumn("สภาพ", "status", "ใช้งานได้", "ชำรุด"),
                tableColumnFactory.createStatusColumn("สถานะ", "available", "ว่าง", "ไม่ว่าง"),
                tableColumnFactory.createActionColumn("", 84, locker -> {
                    IconButton infoBtn = new IconButton(new Icon( Icons.EDIT));
                    IconButton historyBtn = new IconButton(new Icon( Icons.HISTORY));
                    infoBtn.setOnAction(e -> infoLocker(locker));
                    historyBtn.setOnAction(e -> historyLocker(locker));

                    return new Button[]{infoBtn, historyBtn};
                })
        );

        lockersTableView.getItems().setAll(lockersTable.getLockers());
        lockersTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);

    }

    TableColumn<Locker, String> createLockerIdColumn() {
        TableColumn<Locker, String> col = new TableColumn<>("");
        col.setMinWidth(36);
        col.setPrefWidth(36);
        col.setMaxWidth(36);
        col.setStyle("-fx-padding: 0 8; -fx-alignment: CENTER;");
        col.setCellFactory(c -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || getTableRow() == null || getTableRow().getItem() == null) {
                    setText(null);
                    return;
                }
                Locker locker = getTableRow().getItem();
                setText(String.valueOf(locker.getLockerId()));
            }
        });
        return col;
    }

    private void infoLocker(Locker locker){
        try {
            FXRouter.loadDialogStage("officer-locker-dialog", locker);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        showTable(lockers);
    }

    private void historyLocker(Locker locker){
        try {
            FXRouter.loadDialogStage("officer-display-locker-history", locker);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void onSearch() {
        String keyword = searchTextField.getText();

        if(keyword.isEmpty()) {
            showTable(lockers);
        }

        List<Locker> filtered = searchService.search(
                lockers.getLockers(),
                keyword,
                Locker::getLockerUid,
                l -> String.valueOf(l.getLockerId()),
                l -> l.getLockerType().getDescription(), // lambda ไม่ต้องทำ getLockerTypeString
                l -> l.getLockerSizeType().getDescription()
        );

        LockerList filteredList = new LockerList();
        filtered.forEach(filteredList::addLocker);

        showTable(filteredList);
    }

    // Save on chosen folder
    private void onExportQrPdfToSelectFolder() {
        try {
            String zoneName = currentZone.getZoneName().replaceAll("\\s+", "_");
            String timestamp = new TimeFormatUtil().formatNumeric(LocalDateTime.now());
            String defaultFileName = "QR-" + zoneName + "_" + timestamp + ".pdf";

            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("บันทึกไฟล์รหัส QR ตู้ล็อคเกอร์"); // file chooser header
            fileChooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("PDF Files (*.pdf)", "*.pdf") // save as type
            );
            fileChooser.setInitialFileName(defaultFileName);

            File chosenFile = fileChooser.showSaveDialog(exportLockersToPdfButton.getScene().getWindow());
            if (chosenFile == null) {
                // cancel download
                new AlertUtil().error("การบันทึกถูกยกเลิก", "คุณได้ยกเลิกกการบันทึก " + defaultFileName);
                return;
            }

            // แปลงให้เป็น *.pdf
            if (!chosenFile.getName().toLowerCase().endsWith(".pdf")) {
                chosenFile = new File(chosenFile.getAbsolutePath() + ".pdf");
            }

            // สร้าง pdf ตาม format PdfExportUtil
            new PdfExportUtil().exportLockerQrGrid(currentZone, lockers, chosenFile, current);

            new AlertUtil().info(
                    "สร้าง PDF สำเร็จ",
                    "ไฟล์ถูกบันทึกไว้ที่:\n" + chosenFile.getAbsolutePath()
            );

            // ถ้าเปิดไฟล์ได้ให้เปิดเลย
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().open(chosenFile);
            } else {
                new AlertUtil().info("เปิดไฟล์ด้วยตนเอง", "ระบบไม่รองรับการเปิดอัตโนมัติ\nกรุณาเปิดไฟล์จากโฟลเดอร์ด้วยตนเอง");
            }

        } catch (Exception e) {
            new AlertUtil().error("เกิดข้อผิดพลาด", e.getMessage());
        }
    }


    private void onBackButtonClick(){
        try {
            FXRouter.goTo("officer-select-zone", currentZone);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
