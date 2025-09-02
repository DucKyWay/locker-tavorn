package ku.cs.components;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.text.TextAlignment;

public class FilledButtonWithIcon extends FilledButton implements ReplaceableInParent<FilledButton> {
    private final HBox content = new HBox();
    private final Region spaceLeft  = new Region();
    private final Region spaceRight = new Region();
    private Label textLabel = new Label();
    private final Icon iconLeft  = new Icon();
    private final Icon iconRight = new Icon();

    public static final StyleMasker DEFAULT = new StyleMasker("filled-button-with-icon");
    public static final StyleMasker MEDIUM = new StyleMasker("filled-button-with-icon", "medium");
    public static final StyleMasker SMALL  = new StyleMasker("filled-button-with-icon", "small");

    public FilledButtonWithIcon(String label) {
        this(label, null, null);
    }

    public FilledButtonWithIcon(String label, Icons iconLeft) {
        this(label, iconLeft, null);
    }

    public FilledButtonWithIcon(String label, Icons iconLeft, Icons iconRight) {
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
        getStyleClass().add("filled-button-with-icon");
    }

    private void initializeStructure() {
        content.setAlignment(Pos.CENTER);

        textLabel.setAlignment(Pos.CENTER);
        textLabel.setTextAlignment(TextAlignment.CENTER);
        textLabel.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(textLabel, Priority.ALWAYS);

        // spacer เริ่มต้น
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

    public static FilledButtonWithIcon small(String label) {
        return small(label, null, null);
    }

    public static FilledButtonWithIcon small(String label, Icons icon) {
        return small(label, icon, null);
    }

    public static FilledButtonWithIcon small(String label, Icons iconLeft, Icons iconRight) {
        FilledButtonWithIcon b = new FilledButtonWithIcon(label, iconLeft, iconRight);
        b.getStyleClass().add("small");
        return b;
    }

    public static FilledButtonWithIcon medium(String label) {
        return small(label, null, null);
    }

    public static FilledButtonWithIcon medium(String label, Icons icon) {
        return small(label, icon, null);
    }

    public static FilledButtonWithIcon medium(String label, Icons iconLeft, Icons iconRight) {
        FilledButtonWithIcon b = new FilledButtonWithIcon(label, iconLeft, iconRight);
        b.getStyleClass().add("medium");
        return b;
    }

    public static void mask(Button button, Icons iconLeft, Icons iconRight) {
        HBox content = new HBox();
        content.setAlignment(Pos.CENTER);

        if (iconLeft != null) {
            Icon left = new Icon(iconLeft);
            Region spacerL = new Region(); spacerL.setPrefWidth(12);
            content.getChildren().addAll(left, spacerL);
        }

        if (button.getText() != null && !button.getText().isEmpty()) {
            Label lbl = new Label(button.getText());
            button.setText(null);
            lbl.setMaxWidth(Double.MAX_VALUE);
            lbl.setAlignment(Pos.CENTER);
            lbl.setTextAlignment(TextAlignment.CENTER);
            HBox.setHgrow(lbl, Priority.ALWAYS);
            content.getChildren().add(lbl);
        }

        if (iconRight != null) {
            Region spacerR = new Region(); spacerR.setPrefWidth(12);
            Icon right = new Icon(iconRight);
            content.getChildren().addAll(spacerR, right);
        }

        DEFAULT.mask(button);
        button.setGraphic(content);
    }
}
