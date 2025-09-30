package ku.cs.controllers.user;

import javafx.fxml.FXML;

import javafx.scene.control.Label;
import ku.cs.components.LabelStyle;

public class UserHistoryController extends BaseUserController {

    @FXML
    private Label titleLabel;
    @FXML
    private Label descriptionLabel;

//    @FXML private TableView<> historyListTable;

    @Override
    protected void initDatasource() {

    }

    @Override
    protected void initUserInterfaces() {
        LabelStyle.BODY_LARGE.applyTo(titleLabel);
        LabelStyle.LABEL_MEDIUM.applyTo(descriptionLabel);
    }

    @Override
    protected void initEvents() {

    }
}
