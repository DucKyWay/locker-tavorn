module ku.cs.project681 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires jakarta.json.bind;
    requires java.sql;
    requires java.xml.crypto;

    opens ku.cs.project681 to javafx.fxml;
    opens ku.cs.models to javafx.base;
    opens ku.cs.controllers to javafx.fxml;
    opens ku.cs.controllers.components to javafx.fxml;
    opens ku.cs.controllers.test to javafx.fxml;
    opens ku.cs.views to javafx.fxml;
    opens ku.cs.views.components to javafx.fxml;

    exports ku.cs.project681;
    exports ku.cs.controllers;
    exports ku.cs.controllers.components;
    exports ku.cs.models;
    exports ku.cs.controllers.test;
}