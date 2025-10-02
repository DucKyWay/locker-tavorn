module project681 {
    // JavaFX
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires javafx.base;

    // Java
    requires java.desktop;
    requires java.sql;
    requires java.xml.crypto;
    requires jakarta.json;
    requires jakarta.json.bind;
    requires org.eclipse.yasson;

    // 3rd Party
    requires jbcrypt;
    requires org.apache.commons.lang3;
    requires project681;

    // Controllers
    opens ku.cs.controllers to javafx.fxml;
    opens ku.cs.controllers.admin to javafx.fxml;
    opens ku.cs.controllers.components to javafx.fxml;
    opens ku.cs.controllers.locker to javafx.fxml;
    opens ku.cs.controllers.officer to javafx.fxml;
    opens ku.cs.controllers.officer.DialogPane to javafx.fxml;
    opens ku.cs.controllers.test to javafx.fxml;
    opens ku.cs.controllers.user to javafx.fxml;

    // Views
    opens ku.cs.views to javafx.fxml;
    opens ku.cs.views.components to javafx.fxml;

    // Models
    opens ku.cs.models.account to javafx.base, jakarta.json.bind, org.eclipse.yasson;
    opens ku.cs.models.key to javafx.base, jakarta.json.bind, org.eclipse.yasson;
    opens ku.cs.models.locker to javafx.base, jakarta.json.bind, org.eclipse.yasson;
    opens ku.cs.models.request to javafx.base, jakarta.json.bind, org.eclipse.yasson;
    opens ku.cs.models.zone to javafx.base, jakarta.json.bind, org.eclipse.yasson;

    // Exports
    exports ku.cs;
    exports ku.cs.project681;
    exports ku.cs.services.utils;
    exports ku.cs.models.account;
    exports ku.cs.models.key;
    exports ku.cs.models.locker;
    exports ku.cs.models.request;
    exports ku.cs.models.zone;
    exports ku.cs.services.accounts;
    exports ku.cs.services.session;
    exports ku.cs.services.context;
    exports ku.cs.services.ui;
    exports ku.cs.services.zone;
    exports ku.cs.services.datasources.provider;
    exports ku.cs.services.locker;
    exports ku.cs.services.request;
}
