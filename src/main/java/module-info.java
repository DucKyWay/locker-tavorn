module ku.cs.project681 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires jakarta.json.bind;
    requires java.sql;
    requires java.xml.crypto;
    requires javafx.graphics;
    requires jakarta.json;
    requires org.eclipse.yasson;
    requires javafx.base;

    opens ku.cs.project681 to javafx.fxml;
    opens ku.cs.models.account to javafx.base, jakarta.json.bind, org.eclipse.yasson;
    opens ku.cs.models.locker to javafx.base, jakarta.json.bind, org.eclipse.yasson;
    opens ku.cs.models.zone to javafx.base, jakarta.json.bind, org.eclipse.yasson;
    opens ku.cs.models.key to javafx.base, jakarta.json.bind, org.eclipse.yasson;
    opens ku.cs.controllers to javafx.fxml;
    opens ku.cs.controllers.components to javafx.fxml;
    opens ku.cs.controllers.test to javafx.fxml;
    opens ku.cs.views to javafx.fxml;
    opens ku.cs.views.components to javafx.fxml;
    opens ku.cs.controllers.admin to javafx.fxml;
    opens ku.cs.controllers.officer to javafx.fxml;
    opens ku.cs.controllers.user to javafx.fxml;
    opens ku.cs.controllers.locker to javafx.fxml;

    exports ku.cs.project681;
    exports ku.cs.controllers;
    exports ku.cs.controllers.components;
    exports ku.cs.controllers.test;
    exports ku.cs.controllers.admin;
    exports ku.cs.controllers.officer;
    exports ku.cs.controllers.user;
    exports ku.cs.models.locker;
    exports ku.cs.models.zone;
    exports ku.cs.models.account;
}