package ku.cs.services.utils;

import javafx.css.Size;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.Label;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.shape.Circle;
import javafx.util.Callback;

import java.util.Objects;
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
        applyFixedWidth(col, width);
        if (style != null) col.setStyle(style);
        return col;
    }

    public <S> TableColumn<S, Boolean> createStatusColumn(String title, String property) {
        TableColumn<S, Boolean> col = new TableColumn<>(title);
        col.setCellValueFactory(new PropertyValueFactory<>(property));
        col.setCellFactory(tc -> new TableCell<>() {
            @Override
            protected void updateItem(Boolean value, boolean empty) {
                super.updateItem(value, empty);
                if (empty || value == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    setText(value.toString());
                    setGraphic(null);
                }
            }
        });

        col.setStyle("-fx-alignment: CENTER;");
        return col;
    }

    public <S> TableColumn<S, Boolean> createStatusColumn(String title, String property, String trueText, String falseText) {
        return createStatusColumn(title, property, Region.USE_COMPUTED_SIZE, trueText, falseText);
    }

    public <S> TableColumn<S, Boolean> createStatusColumn(String title, String property, double width, String trueText, String falseText) {
        TableColumn<S, Boolean> col = new TableColumn<>(title);
        col.setCellValueFactory(new PropertyValueFactory<>(property));
        applyFixedWidth(col, width);
        col.setCellFactory(tc -> new TableCell<>() {
            @Override
            protected void updateItem(Boolean value, boolean empty) {
                super.updateItem(value, empty);

                if (empty || value == null) {
                    setText(null);
                    setGraphic(null);
                    return;
                }

                Circle dot = new Circle(4.5);
                if (value) {
                    dot.getStyleClass().add("fill-success");
                } else {
                    dot.getStyleClass().add("fill-on-disabled");
                }

                Label label = new Label(value ? trueText : falseText);
                label.getStyleClass().addAll("body-small", "text-on-background");

                HBox h = new HBox();
                h.getChildren().addAll(dot, label);
                h.setSpacing(8);
                h.setPadding(new Insets(4, 8, 4, 8));
                h.setMinSize(24, 24);
                h.setMaxHeight(24);
                h.setAlignment(Pos.CENTER_LEFT);
                h.setStyle("-fx-background-radius: 12; -fx-border-radius: 12;");
                h.getStyleClass().add("bg-elevated");

                HBox cellBox = new HBox(h);
                cellBox.setAlignment(Pos.CENTER_LEFT);

                setText(null);
                setGraphic(cellBox);
            }
        });

        col.setStyle("-fx-alignment: CENTER;" + " -fx-padding: 0 16;");
        return col;
    }

    public <S, E extends Enum<E>> TableColumn<S, E> createEnumStatusColumn(String title, String property, int width) {
        TableColumn<S, E> col = new TableColumn<>(title);
        col.setCellValueFactory(new PropertyValueFactory<>(property));
        applyFixedWidth(col, width);
        col.setCellFactory(tc -> new TableCell<>() {
            @Override
            protected void updateItem(E value, boolean empty) {
                super.updateItem(value, empty);

                if (empty || value == null) {
                    setText(null);
                    setGraphic(null);
                    return;
                }

                Circle dot = new Circle(4.5);
                int type = resolveEnumValue(value);
                if (type < 0) {
                    dot.setStyle("-fx-fill-color: transparent;");
                }
                else if (type == 0) {
                    dot.getStyleClass().add("fill-on-disabled");
                }
                else if (type == 1) {
                    dot.getStyleClass().add("fill-success");
                }
                else if (type == 2) {
                    dot.getStyleClass().add("fill-warning");
                }
                else if (type == 3) {
                    dot.getStyleClass().add("fill-error");
                }
                else if (type == 4) {
                    dot.getStyleClass().add("fill-info");
                }

                Label label = new Label(resolveEnumDescription(value));
                label.getStyleClass().addAll("body-small", "text-on-background");

                HBox h = new HBox();
                h.getChildren().addAll(dot, label);
                h.setSpacing(8);
                h.setPadding(new Insets(4, 8, 4, 8));
                h.setMinSize(24, 24);
                h.setMaxHeight(24);
                h.setAlignment(Pos.CENTER_LEFT);
                h.setStyle("-fx-background-radius: 12; -fx-border-radius: 12;");
                h.getStyleClass().add("bg-elevated");

                HBox cellBox = new HBox(h);
                cellBox.setAlignment(Pos.CENTER_LEFT);

                setText(null);
                setGraphic(cellBox);
            }
        });
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

    public <S> TableColumn<S, Void> createActionColumn(String title, Function<S, Button[]> buttonFactory) {
        return createActionColumn(title, Region.USE_COMPUTED_SIZE, buttonFactory);
    }

    public <S> TableColumn<S, Void> createActionColumn(String title, double width, Function<S, Button[]> buttonFactory) {
        Objects.requireNonNull(buttonFactory, "buttonFactory must not be null");

        TableColumn<S, Void> col = new TableColumn<>(title);

        col.setCellFactory(tc -> new TableCell<>() {
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || getTableRow() == null || getTableRow().getItem() == null) {
                    setGraphic(null);
                    setText(null);
                    return;
                }

                S rowItem = (S) getTableRow().getItem();
                Button[] buttons = buttonFactory.apply(rowItem);

                HBox box = new HBox();
                box.setSpacing(8);
                box.setAlignment(Pos.CENTER_RIGHT);
                if (buttons != null) box.getChildren().addAll(buttons);

                setText(null);
                setGraphic(box);
            }
        });

        applyFixedWidth(col, width);
        col.setStyle("-fx-alignment: CENTER;" + " -fx-padding: 0 0 0 12;");
        return col;
    }

    public <S> TableColumn<S, String> createProfileColumn() {
        TableColumn<S, String> col = new TableColumn<>();
        applyFixedWidth(col, 36);

        col.setCellValueFactory(new PropertyValueFactory<>("imagePath"));
        col.setCellFactory(new Callback<>() {
            @Override
            public TableCell<S, String> call(TableColumn<S, String> param) {
                return new TableCell<>() {
                    private final ImageView imageView = new ImageView();

                    @Override
                    protected void updateItem(String imagePath, boolean empty) {
                        super.updateItem(imagePath, empty);

                        if (empty) {
                            setText(null);
                            setGraphic(null);
                            return;
                        }

                        Image image = loadImageOrDefault(imagePath, 34, 34);

                        imageView.setImage(image);
                        imageView.setFitWidth(34);
                        imageView.setFitHeight(34);
                        imageView.setPickOnBounds(true);
                        imageView.setClip(new Circle(17, 17, 17));

                        HBox pane = new HBox(imageView);
                        pane.setMinSize(36, 36);
                        pane.setPrefSize(36, 36);
                        pane.setMaxSize(36, 36);
                        pane.setShape(new Circle(18, 18, 18));
                        pane.setAlignment(Pos.TOP_CENTER);
                        pane.setStyle("-fx-border-width: 1; -fx-padding: 1;");
                        pane.getStyleClass().addAll("bg-disabled", "border-on-background");

                        setText(null);
                        setGraphic(pane);
                    }
                };
            }
        });

        col.setStyle("-fx-alignment: CENTER;");
        return col;
    }

    private static <S, T> void applyFixedWidth(TableColumn<S, T> col, double width) {
        if (width > 0) {
            col.setMinWidth(width);
            col.setPrefWidth(width);
            col.setMaxWidth(width);
        }
    }

    private static String resolveEnumDescription(Enum<?> e) {
        try {
            var method = e.getClass().getMethod("getDescription");
            Object result = method.invoke(e);
            return result != null ? result.toString() : e.name();
        } catch (Exception ignore) {
            return e.name();
        }
    }

    private static int resolveEnumValue(Enum<?> e) {
        try {
            var method = e.getClass().getMethod("getValue");
            Object result = method.invoke(e);
            return result != null ? (int)result : -1;
        } catch (Exception ignore) {
            return -1;
        }
    }

    private Image loadImageOrDefault(String filePath, double w, double h) {
        try {
            if (filePath != null && !filePath.isBlank()) {
                Image img = new Image("file:" + filePath, w, h, true, true);
                if (!img.isError()) return img;
            }
        } catch (Exception ignored) { /* fall through */ }

        var url = getClass().getResource(DEFAULT_AVATAR);
        return new Image(Objects.requireNonNull(url, "Default avatar not found").toExternalForm(), w, h, true, true);
    }
}
