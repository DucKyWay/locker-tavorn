package ku.cs.controllers.officer;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
import ku.cs.models.request.Request;
import ku.cs.models.request.RequestList;
import ku.cs.services.datasources.provider.LockerDatasourceProvider;
import ku.cs.services.datasources.provider.RequestDatasourceProvider;
import ku.cs.services.request.RequestService;
import ku.cs.services.ui.FXRouter;
import ku.cs.services.utils.*;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

public class OfficerManageLockersController extends BaseOfficerController{
    private RequestService requestService = new RequestService();
    private final RequestDatasourceProvider requestsProvider = new RequestDatasourceProvider();
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
    @FXML private Button addlockerManualButton;
    @FXML private Button addlockerDigitalButton;

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
        FilledButton.SMALL.mask(addlockerManualButton);
        FilledButton.SMALL.mask(addlockerDigitalButton);
        showTable(lockers);
    }

    @Override
    protected void initEvents() {
        requestService.updateData();
        backButton.setOnAction(e -> onBackButtonClick());
        addlockerManualButton.setOnAction(e -> onAddlockerManualButtonClick());
        addlockerDigitalButton.setOnAction(e -> onAddlockerDigitalButtonClick());
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
    private void onAddlockerManualButtonClick(){
        Locker newLocker = new Locker(LockerType.MANUAL, LockerSizeType.MEDIUM, currentZone.getZoneUid());
        lockers.addLocker(newLocker);
        lockersProvider.saveCollection(currentZone.getZoneUid(), lockers);
        showTable(lockers);

    }
    private void onAddlockerDigitalButtonClick(){
        Locker newLocker = new Locker(LockerType.DIGITAL, LockerSizeType.MEDIUM,currentZone.getZoneUid());
        lockers.addLocker(newLocker);
        lockersProvider.saveCollection(currentZone.getZoneUid(), lockers);
        showTable(lockers);
        
    }
    private TableColumn<Locker, Void> createActionColumn() {
        return tableColumnFactory.createActionColumn("จัดการ", locker -> {
            FilledButtonWithIcon infoBtn = FilledButtonWithIcon.small("ข้อมูล", Icons.EDIT);
            FilledButtonWithIcon historyBtn = FilledButtonWithIcon.small("ประวัติ", Icons.HISTORY);

            infoBtn.setOnAction(e -> infoLocker(locker));
            historyBtn.setOnAction(e -> deleteLocker(locker));

            return new Button[]{infoBtn, historyBtn};
        });
    }

    private void infoLocker(Locker locker){
        RequestList requests = requestsProvider.loadCollection(currentZone.getZoneUid());
        Request request = requests.findRequestByLockerUid(locker.getLockerUid());
        try {
            FXRouter.loadDialogStage("officer-locker-dialog", locker);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        showTable(lockers);
    }

    private void deleteLocker(Locker locker){
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
            fileChooser.setTitle("บันทึกไฟล์รหัส QR ตู้ล็อกเกอร์"); // file chooser header
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
            e.printStackTrace();
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
