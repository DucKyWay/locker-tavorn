module ku.cs.project681 {
    requires javafx.controls;
    requires javafx.fxml;


    opens ku.cs.project681 to javafx.fxml;
    exports ku.cs.project681;
    exports ku.cs.controllers;
    opens ku.cs.controllers to javafx.fxml;
}