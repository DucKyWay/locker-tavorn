module ku.cs.project681 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens ku.cs.project681 to javafx.fxml;
    exports ku.cs.project681;
    exports ku.cs.controllers;
    opens ku.cs.controllers to javafx.fxml;
    exports ku.cs.models;
    opens  ku.cs.models to javafx.base;
}