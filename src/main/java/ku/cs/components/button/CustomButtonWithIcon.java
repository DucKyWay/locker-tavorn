package ku.cs.components.button;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.text.TextAlignment;

import ku.cs.components.Icon;
import ku.cs.components.Icons;

public class CustomButtonWithIcon extends CustomButton{
    private final HBox content = new HBox();
    private final Region spaceLeft  = new Region();
    private final Region spaceRight = new Region();
    private Label textLabel = new Label();
    private final Icon iconLeft  = new Icon();
    private final Icon iconRight = new Icon();

    public CustomButtonWithIcon(String label) {
        this(label, null, null);
    }

    public CustomButtonWithIcon(String label, Icons iconLeft) {
        this(label, iconLeft, null);
    }

    public CustomButtonWithIcon(String label, Icons iconLeft, Icons iconRight) {
        super("");
        this.textLabel.setText(label);
        this.iconLeft.setIcon(iconLeft);
        this.iconRight.setIcon(iconRight);
        initializeStyle();
        initializeStructure();
        refreshContent();
    }

    @Override
    protected void initializeStyle() {
        ensureStyleClassPresent(this,  "filled-button-with-icon");
    }

    private void initializeStructure() {
        content.setAlignment(Pos.CENTER);

        if (iconRight != null && iconLeft != null)
            textLabel.setAlignment(Pos.CENTER);
        else if (iconLeft != null)
            textLabel.setAlignment(Pos.CENTER_LEFT);
        else if (iconRight != null)
            textLabel.setAlignment(Pos.CENTER_RIGHT);
        textLabel.setTextAlignment(TextAlignment.CENTER);
        textLabel.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(textLabel, Priority.ALWAYS);

        spaceLeft.setPrefWidth(12);
        spaceRight.setPrefWidth(12);

        setGraphic(content);
    }

    private void refreshContent() {
        content.getChildren().clear();

        boolean hasLeft  = iconLeft.getIcon()  != null;
        boolean hasRight = iconRight.getIcon() != null;
        boolean hasText  = textLabel.getText() != null && !textLabel.getText().isEmpty();

        if (hasLeft)  content.getChildren().addAll(iconLeft, spaceLeft);
        if (hasText)  content.getChildren().add(textLabel);
        if (hasRight) content.getChildren().addAll(spaceRight, iconRight);

        HBox.setHgrow(textLabel, hasText ? Priority.ALWAYS : Priority.NEVER);

        requestLayout();
    }

    public Label getTextLabel() {
        return textLabel;
    }

    public void setTextLabel(Label newLabel) {
        if (newLabel == null) return;
        newLabel.setMaxWidth(Double.MAX_VALUE);
        newLabel.setAlignment(Pos.CENTER);
        newLabel.setTextAlignment(TextAlignment.CENTER);
        this.textLabel = newLabel;
        refreshContent();
    }

    public void setLabelText(String text) {
        this.textLabel.setText(text);
        refreshContent();
    }

    public String getLabelText() {
        return this.textLabel.getText();
    }

    public Icon getLeftIconNode() {
        return iconLeft;
    }
    public Icon getRightIconNode() {
        return iconRight;
    }

    public void setIconLeft(Icons icon) {
        iconLeft.setIcon(icon);
        refreshContent();
    }

    public void setIconRight(Icons icon) {
        iconRight.setIcon(icon);
        refreshContent();
    }

    public void setIconSize(double size) {
        iconLeft.setIconSize(size);
        iconRight.setIconSize(size);
        requestLayout();
    }

    public void setLeftIconSize(double size) {
        iconLeft.setIconSize(size);
        requestLayout();
    }

    public void setRightIconSize(double size) {
        iconRight.setIconSize(size);
        requestLayout();
    }

    public void setButtonSize(double prefWidth, double prefHeight) {
        setPrefSize(prefWidth, prefHeight);
    }

    public void setButtonWidth(double prefWidth) {
        setPrefWidth(prefWidth);
    }

    public void setButtonHeight(double prefHeight) {
        setPrefHeight(prefHeight);
    }

    public double getButtonPrefWidth()  {
        return getPrefWidth();
    }
    public double getButtonPrefHeight() {
        return getPrefHeight();
    }

    public void setSpaceLeft(double width) {
        spaceLeft.setPrefWidth(width);
        requestLayout();
    }

    public void setSpaceRight(double width) {
        spaceRight.setPrefWidth(width);
        requestLayout();
    }

    public double getSpaceLeft()  {
        return spaceLeft.getPrefWidth();
    }
    public double getSpaceRight() {
        return spaceRight.getPrefWidth();
    }
}
