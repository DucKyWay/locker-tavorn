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
    @FXML private Button backButton;
    @FXML private Label headerLabel;
    @FXML private Label descriptionLabel;
    @FXML private TextField searchTextField;
    @FXML private Button searchButton;
    @FXML private Button exportLockersToPdfButton;
    @FXML private TableView<Locker> lockersTableView;
    @FXML private Button addlockerButton;
    @FXML private ComboBox<LockerType> typeLockerComboBox;
    @FXML private ComboBox<LockerSizeType> sizeLockerComboBox;
    @Override
    protected void initDatasource() {
        lockers = lockersProvider.loadCollection(currentZone.getZoneUid());
    }

    @Override
    protected void initUserInterfaces() {
        headerLabel.setText("รายการล็อคเกอร์");
        descriptionLabel.setText("จุดให้บริการ " + currentZone.getZoneName() + " [" + currentZone.getZoneUid() + "]");

        ElevatedButtonWithIcon.SMALL.mask(backButton, Icons.ARROW_LEFT);
        IconButton.mask(searchButton, new Icon(Icons.MAGNIFYING_GLASS));
        FilledButtonWithIcon.MEDIUM.mask(exportLockersToPdfButton, null, Icons.EXPORT);
        FilledButton.SMALL.mask(addlockerButton);
        showTable(lockers);
    }

    @Override
    protected void initEvents() {
        requestService.updateData();
        backButton.setOnAction(e -> onBackButtonClick());
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
        Locker newLocker = new Locker(selectedType, selectedSize, currentZone.getZoneUid(), "");
        lockers.addLocker(newLocker);
        lockersProvider.saveCollection(currentZone.getZoneUid(), lockers);

        new AlertUtil().info("สำเร็จ", "เพิ่มล็อคเกอร์หมายเลข " + newLocker.getLockerUid() + " เรียบร้อยแล้ว");
        showTable(lockers);
    }
    private void showTable(LockerList lockersTable) {
        lockersTableView.getColumns().clear();
        lockersTableView.getItems().clear();

        lockersTableView.getColumns().setAll(
                tableColumnFactory.createTextColumn("ที่", "lockerId", 60),
                tableColumnFactory.createTextColumn("เลขล็อคเกอร์", "lockerUid", 105),
                tableColumnFactory.createEnumStatusColumn("ประเภทล็อคเกอร์", "lockerType",0),
                tableColumnFactory.createEnumStatusColumn("ขนาดล็อคเกอร์", "lockerSizeType", 0),
                tableColumnFactory.createStatusColumn("สถานะ", "available", "พร้อมใช้งาน", "ใช้งานอยู่"),
                tableColumnFactory.createStatusColumn("ชำรุด", "status", "ใช้งานได้", "ชำรุด"),
                createActionColumn()
        );

        lockersTableView.getItems().setAll(lockersTable.getLockers());
    }

    private TableColumn<Locker, Void> createActionColumn() {
        return tableColumnFactory.createActionColumn("จัดการ", locker -> {
            FilledButtonWithIcon infoBtn = FilledButtonWithIcon.small("ข้อมูล", Icons.EDIT);
            FilledButtonWithIcon historyBtn = FilledButtonWithIcon.small("ประวัติ", Icons.HISTORY);

            infoBtn.setOnAction(e -> infoLocker(locker));
            historyBtn.setOnAction(e -> historyLocker(locker));

            return new Button[]{infoBtn, historyBtn};
        });
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
                Locker::getLockerSizeTypeString
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
            FXRouter.goTo("officer-home", currentZone);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
