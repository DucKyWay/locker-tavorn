package ku.cs.services.utils;

import javafx.css.Size;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;
import javafx.scene.shape.StrokeType;
import javafx.util.Callback;

import java.util.function.Function;

public class TableColumnFactory {
    private static final String DEFAULT_AVATAR = "/ku/cs/images/default_profile.png";

    public TableColumnFactory() {}

    public <S, T> TableColumn<S, T> createTextColumn(String title, String property) {
        return createTextColumn(title, property, Region.USE_COMPUTED_SIZE, "-fx-alignment: CENTER_LEFT; -fx-padding: 0 16");
    }

    public <S, T> TableColumn<S, T> createTextColumn(String title, String property, String style) {
        return createTextColumn(title, property, Region.USE_COMPUTED_SIZE, style);
    }

    public <S, T> TableColumn<S, T> createTextColumn(String title, String property, double width) {
        return createTextColumn(title, property, width, "-fx-alignment: CENTER_LEFT; -fx-padding: 0 16");
    }

    public <S, T> TableColumn<S, T> createTextColumn(String title, String property, double width, String style) {
        TableColumn<S, T> col = new TableColumn<>(title);
        col.setCellValueFactory(new PropertyValueFactory<>(property));
        if (width > 0) col.setMinWidth(width);
        if (width > 0) col.setPrefWidth(width);
        if (width > 0) col.setMaxWidth(width);
        if (style != null) col.setStyle(style);
        return col;
    }

    public <S> TableColumn<S, Boolean> createStatusColumn(
            String title, String property) {

        TableColumn<S, Boolean> col = new TableColumn<>(title);
        col.setCellValueFactory(new PropertyValueFactory<>(property));
        col.setCellFactory(tc -> new TableCell<>() {
            @Override
            protected void updateItem(Boolean value, boolean empty) {
                super.updateItem(value, empty);
                if (empty || value == null) {
                    setText(null);
                } else {
                    setText(value.toString());
                }
            }
        });

        col.setStyle("-fx-alignment: CENTER;");
        return col;
    }

    public <S> TableColumn<S, Boolean> createStatusColumn(
            String title,  String property, String trueText, String falseText) {
     return createStatusColumn(title, property, Region.USE_COMPUTED_SIZE,  trueText, falseText);
    }
    public <S> TableColumn<S, Boolean> createStatusColumn(
            String title, String property, double width, String trueText, String falseText) {

        TableColumn<S, Boolean> col = new TableColumn<>(title);
        if (width > 0) col.setMinWidth(width);
        if (width > 0) col.setPrefWidth(width);
        if (width > 0) col.setMaxWidth(width);
        col.setCellValueFactory(new PropertyValueFactory<>(property));
        col.setCellFactory(tc -> new TableCell<>() {
            @Override
            protected void updateItem(Boolean value, boolean empty) {
                super.updateItem(value, empty);
                if (empty || value == null) {
                    setText(null);
                } else {
                    setText(value ? trueText : falseText);
                }
            }
        });

        col.setStyle("-fx-alignment: CENTER_LEFT; -fx-padding: 10 16;");
        return col;
    }

    public <S, E extends Enum<E>> TableColumn<S, E> createEnumStatusColumn(
            String title, String property, int minWidth) {

        TableColumn<S, E> col = new TableColumn<>(title);
        col.setCellValueFactory(new PropertyValueFactory<>(property));
        col.setCellFactory(tc -> new TableCell<>() {
            @Override
            protected void updateItem(E value, boolean empty) {
                super.updateItem(value, empty);
                if (empty || value == null) {
                    setText(null);
                } else {
                    try {
                        var method = value.getClass().getMethod("getDescription");
                        Object result = method.invoke(value);
                        setText(result != null ? result.toString() : value.name());
                    } catch (Exception ex) {
                        setText(value.name());
                    }
                }
            }
        });
        col.setMinWidth(minWidth);
        col.setStyle("-fx-alignment: CENTER;");
        return col;
    }


    public <S> TableColumn<S, Void> createNumberColumn() {
        TableColumn<S, Void> col = new TableColumn<>("ที่");
        col.setCellFactory(tc -> new TableCell<>() {
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : String.valueOf(getIndex() + 1));
            }
        });
        col.setStyle("-fx-alignment: CENTER;");
        col.setMaxWidth(30);
        col.setSortable(false);
        return col;
    }

    public <S> TableColumn<S, Void> createActionColumn(
            String title,
            Function<S, Button[]> buttonFactory
    ) { return  createActionColumn(title, buttonFactory, Region.USE_COMPUTED_SIZE);
    }

    public <S> TableColumn<S, Void> createActionColumn(
            String title,
            Function<S, Button[]> buttonFactory,
            double width
    ) {
        TableColumn<S, Void> col = new TableColumn<>(title);

        col.setCellFactory(tc -> new TableCell<>() {
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || getTableRow() == null || getTableRow().getItem() == null) {
                    setGraphic(null);
                    return;
                }

                S rowItem = getTableRow().getItem();
                Button[] buttons = buttonFactory.apply(rowItem); // สร้างปุ่ม
                HBox box = new HBox(5, buttons);
                box.setAlignment(Pos.CENTER_RIGHT);
                setGraphic(box);
            }
        });

        col.setMinWidth(width);
        col.setPrefWidth(width);
        col.setMaxWidth(width);
        col.setStyle("-fx-alignment: CENTER; -fx-padding: 0 0 0 16");
        return col;
    }

    public <S> TableColumn<S, String> createProfileColumn() {

        TableColumn<S, String> profileColumn = new TableColumn<>();
        profileColumn.setMinWidth(36);
        profileColumn.setMaxWidth(36);
        profileColumn.setPrefWidth(36);
        profileColumn.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("imagePath"));

        profileColumn.setCellFactory(new Callback<>() {
            @Override
            public TableCell<S, String> call(TableColumn<S, String> param) {
                return new TableCell<>() {
                    private final ImageView imageView = new ImageView();

                    @Override
                    protected void updateItem(String imagePath, boolean empty) {
                        super.updateItem(imagePath, empty);

                        if (empty) {
                            setGraphic(null);
                            return;
                        }

                        Image image;
                        HBox pane = new HBox();

                        try {
                            if (imagePath != null && !imagePath.isBlank()) {
                                image = new Image("file:" + imagePath, 34, 34, true, true);
                                if (image.isError()) throw new Exception("Invalid image");
                            } else {
                                throw new Exception("No imagePath");
                            }
                        } catch (Exception e) {
                            image = new Image(
                                    getClass().getResource(DEFAULT_AVATAR).toExternalForm(),
                                    34, 34, true, true
                            );
                        }

                        imageView.setImage(image);
                        imageView.setFitWidth(34);
                        imageView.setFitHeight(34);
                        imageView.setPickOnBounds(true);
                        imageView.setClip(new Circle(17, 17, 17));

                        pane.setMinSize(36, 36);
                        pane.setMaxSize(36, 36);
                        pane.setPrefSize(36, 36);
                        pane.setShape(new Circle(18, 18, 18));
                        pane.setAlignment(Pos.TOP_CENTER);
                        pane.setStyle("-fx-border-width: 1; -fx-padding: 1");
                        pane.getStyleClass().addAll("bg-disabled", "border-on-background");
                        pane.getChildren().add(imageView);

                        setGraphic(pane);
                    }
                };
            }
        });

        profileColumn.setStyle("-fx-alignment: CENTER;");
        return profileColumn;
    }
}
