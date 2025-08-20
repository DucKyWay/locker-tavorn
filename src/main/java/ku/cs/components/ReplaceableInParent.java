package ku.cs.components;

import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.Pane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public interface ReplaceableInParent<T extends Node> {
    T self();

    default void copyGeometryFrom(Node oldNode) {
        T node = self();

        node.setLayoutX(oldNode.getLayoutX());
        node.setLayoutY(oldNode.getLayoutY());
        node.setTranslateX(oldNode.getTranslateX());
        node.setTranslateY(oldNode.getTranslateY());
        node.setRotate(oldNode.getRotate());
        node.setScaleX(oldNode.getScaleX());
        node.setScaleY(oldNode.getScaleY());
        node.setOpacity(oldNode.getOpacity());
        node.setManaged(oldNode.isManaged());
        node.setVisible(oldNode.isVisible());

        node.resizeRelocate(
                oldNode.getLayoutX(), oldNode.getLayoutY(),
                oldNode.prefWidth(-1), oldNode.prefHeight(-1)
        );

        AnchorPane.setTopAnchor(node,    AnchorPane.getTopAnchor(oldNode));
        AnchorPane.setRightAnchor(node,  AnchorPane.getRightAnchor(oldNode));
        AnchorPane.setBottomAnchor(node, AnchorPane.getBottomAnchor(oldNode));
        AnchorPane.setLeftAnchor(node,   AnchorPane.getLeftAnchor(oldNode));

        GridPane.setRowIndex(node,    GridPane.getRowIndex(oldNode));
        GridPane.setColumnIndex(node, GridPane.getColumnIndex(oldNode));
        GridPane.setRowSpan(node,     GridPane.getRowSpan(oldNode));
        GridPane.setColumnSpan(node,  GridPane.getColumnSpan(oldNode));

        Parent parent = oldNode.getParent();
        if (parent instanceof HBox) {
            Insets margin = HBox.getMargin(oldNode);
            if (margin != null) HBox.setMargin(node, margin);
        } else if (parent instanceof VBox) {
            Insets margin = VBox.getMargin(oldNode);
            if (margin != null) VBox.setMargin(node, margin);
        }
    }

    default boolean replaceInParentOf(Node oldNode) {
        T node = self();
        Parent parent = oldNode.getParent();

        if (parent == null) return false;

        copyGeometryFrom(oldNode);

        if (parent instanceof Pane pane) {
            ObservableList<Node> children = pane.getChildren();
            int idx = children.indexOf(oldNode);
            if (idx >= 0) { children.set(idx, node); return true; }
        }

        if (parent instanceof Group group) {
            ObservableList<Node> children = group.getChildren();
            int idx = children.indexOf(oldNode);
            if (idx >= 0) { children.set(idx, node); return true; }
        }

        if (parent instanceof ToolBar tb) {
            ObservableList<Node> items = tb.getItems();
            int idx = items.indexOf(oldNode);
            if (idx >= 0) { items.set(idx, node); return true; }
        }

        System.err.println("replaceInParentOf: unsupported parent " + parent.getClass());
        return false;
    }

    static boolean replace(Node oldNode, Node newNode) {
        Parent parent = oldNode.getParent();
        if (parent == null) return false;

        if (newNode instanceof ReplaceableInParent<?> rp) {
            rp.copyGeometryFrom(oldNode);
        }

        if (parent instanceof Pane pane) {
            int idx = pane.getChildren().indexOf(oldNode);
            if (idx >= 0) { pane.getChildren().set(idx, newNode); return true; }
        }

        if (parent instanceof Group group) {
            int idx = group.getChildren().indexOf(oldNode);
            if (idx >= 0) { group.getChildren().set(idx, newNode); return true; }
        }

        if (parent instanceof ToolBar tb) {
            int idx = tb.getItems().indexOf(oldNode);
            if (idx >= 0) { tb.getItems().set(idx, newNode); return true; }
        }

        System.err.println("ReplaceableInParent.replace: unsupported parent " + parent.getClass());
        return false;
    }

    default void adoptButtonFrom(Button srcButton) {
        Button button = (Button)self();

        button.setGraphic(srcButton.getGraphic());
        button.setDisable(srcButton.isDisable());
        button.setWrapText(srcButton.isWrapText());
        button.setMnemonicParsing(srcButton.isMnemonicParsing());
        button.setContentDisplay(srcButton.getContentDisplay());
        if (srcButton.getTooltip() != null) button.setTooltip(srcButton.getTooltip());
        if (srcButton.getOnAction() != null) button.setOnAction(srcButton.getOnAction());

        button.setPrefSize(srcButton.getPrefWidth(), srcButton.getPrefHeight());
        button.setMinSize(srcButton.getMinWidth(),   srcButton.getMinHeight());
        button.setMaxSize(srcButton.getMaxWidth(),   srcButton.getMaxHeight());

        srcButton.getStyleClass().stream().filter(sc -> !"button".equals(sc))
                .forEach(sc -> button.getStyleClass().add(sc));

        String inline = srcButton.getStyle();
        if (inline != null && !inline.isBlank()) button.setStyle(inline);
    }
}
