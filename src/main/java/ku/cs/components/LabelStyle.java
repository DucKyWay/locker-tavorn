package ku.cs.components;

import javafx.scene.control.Label;

public enum LabelStyle {
    LABEL_SMALL("label-small"),
    LABEL_MEDIUM("label-medium"),
    LABEL_LARGE("label-large"),
    BODY_SMALL("body-small"),
    BODY_MEDIUM("body-medium"),
    BODY_LARGE("body-large"),
    TITLE_SMALL("title-small"),
    TITLE_MEDIUM("title-medium"),
    TITLE_LARGE("title-large"),
    HEADLINE_SMALL("headline-small"),
    HEADLINE_MEDIUM("headline-medium"),
    HEADLINE_LARGE("headline-large"),
    DISPLAY_SMALL("display-small"),
    DISPLAY_MEDIUM("display-medium"),
    DISPLAY_LARGE("display-large");

    private final String styleClass;

    LabelStyle(String styleClass) {
        this.styleClass = styleClass;
    }

    public String getStyleClass() {
        return styleClass;
    }

    public void applyTo(Label labeled) {
        labeled.getStyleClass().add(styleClass);
    }

    @Override
    public String toString() {
        return styleClass;
    }
}
