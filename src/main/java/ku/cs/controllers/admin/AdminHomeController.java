package ku.cs.controllers.admin;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import ku.cs.components.LabelStyle;

public class AdminHomeController extends BaseAdminController{

    @FXML private Label titleHome;
    @FXML private Label descriptionHome;

    @Override
    protected void initDatasource() {

    }

    @Override
    protected void initUserInterfaces() {
        titleHome.setText("Welcome Admin!");
        descriptionHome.setText("Have a nice day! " + current.getUsername() + ".");

        LabelStyle.TITLE_LARGE.applyTo(titleHome);
        LabelStyle.TITLE_MEDIUM.applyTo(descriptionHome);
    }

    @Override
    protected void initEvents() {

    }
}
