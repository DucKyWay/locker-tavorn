package ku.cs.controllers.officer;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import ku.cs.components.DefaultButton;
import ku.cs.components.DefaultLabel;
import ku.cs.models.account.Account;
import ku.cs.models.account.Officer;
import ku.cs.models.account.User;
import ku.cs.models.account.UserList;
import ku.cs.models.key.KeyList;
import ku.cs.models.key.KeyLocker;
import ku.cs.models.locker.KeyType;
import ku.cs.services.FXRouter;
import ku.cs.services.SessionManager;
import ku.cs.services.datasources.Datasource;
import ku.cs.services.datasources.KeyListFileDatasource;

import java.io.IOException;

public class OfficerKeyLockerController {
    @FXML private HBox headerLabelContainer;
    @FXML private HBox backButtonContainer;
    @FXML private TableView<KeyLocker> keylockerTableView;

    private DefaultButton backButton;
    private DefaultLabel headerLabel;

    private Officer officer;
    private Datasource<KeyList> keyListDatasource;
    private KeyList keyList;
    private Account current;
    @FXML
    public void initialize() {
        SessionManager.requireOfficerLogin();
        current = SessionManager.getCurrentAccount();
        Object data = FXRouter.getData();
        if (data instanceof Officer) {
            officer = (Officer) data;
        } else {
            System.out.println("Error: Data is not an Officer");
        }
        initialKeyListDatasource();
        initUserInterface();
        initEvents();
        showTable(keyList);

    }
    public void initialKeyListDatasource() {
        keyListDatasource = new KeyListFileDatasource(
                "data/keys",
                "zone-"+ officer.getIdZone()+".json"
        );
        keyList = keyListDatasource.readData();
    }
    private void initUserInterface() {
        headerLabel = DefaultLabel.h2("Key List Zone : "+officer.getServiceZone());
        backButton = DefaultButton.primary("Back");
        backButtonContainer.getChildren().add(backButton);
        headerLabelContainer.getChildren().add(headerLabel);
    }
    private void initEvents() {
        backButton.setOnAction(e -> onBackButtonClick());
    }
    private void onBackButtonClick() {
        try {
            FXRouter.goTo("officer-home");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private void showTable(KeyList keyList) {
        keylockerTableView.getColumns().clear();

        TableColumn<KeyLocker, KeyType> keyTypeColumn = new TableColumn<>("ประเภทกุญแจ");
        keyTypeColumn.setCellValueFactory(new PropertyValueFactory<>("keyType"));

        TableColumn<KeyLocker, String> uuidColumn = new TableColumn<>("เลขประจำกุญแจ");
        uuidColumn.setCellValueFactory(new PropertyValueFactory<>("uuid"));

        TableColumn<KeyLocker, String> passKeyColumn = new TableColumn<>("รหัสกุญแจ");
        passKeyColumn.setCellValueFactory(new PropertyValueFactory<>("passkey"));

        TableColumn<KeyLocker, Boolean> availableColumn = new TableColumn<>("สถานะกุญแจ");
        availableColumn.setCellValueFactory(new PropertyValueFactory<>("available"));

        TableColumn<KeyLocker, String> uuidLockerColumn = new TableColumn<>("uuidLocker");
        uuidLockerColumn.setCellValueFactory(new PropertyValueFactory<>("uuidLocker"));
        keylockerTableView.getColumns().addAll(keyTypeColumn, uuidColumn, passKeyColumn, availableColumn, uuidLockerColumn);

        keylockerTableView.getItems().clear();
        keylockerTableView.getItems().addAll(keyList.getKeys());
    }

    @FXML
    protected void onAddKeyChain(){
        KeyLocker keyLocker = new KeyLocker(KeyType.CHAIN,officer.getServiceZone());
        keyList.addKey(keyLocker);
        keyListDatasource.writeData(keyList);
        showTable(keyList);
    }
    @FXML
    protected void onAddKeyManual(){
        KeyLocker keyLocker = new KeyLocker(KeyType.MANUAL,officer.getServiceZone());
        keyList.addKey(keyLocker);
        keyListDatasource.writeData(keyList);
        showTable(keyList);
    }
    @FXML
    protected void onResetKeyList(){
        keyList.resetKeyList();
        keyListDatasource.writeData(keyList);
        showTable(keyList);
    }
}
