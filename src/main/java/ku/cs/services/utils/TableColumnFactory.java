package ku.cs.services.utils;

import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Circle;
import javafx.util.Callback;

import java.util.function.Function;

public class TableColumnFactory {
    private static final String DEFAULT_AVATAR = "/ku/cs/images/default_profile.png";

    public TableColumnFactory() {}

    public <S, T> TableColumn<S, T> createTextColumn(String title, String property) {
        return createTextColumn(title, property, 0, "-fx-alignment: CENTER_LEFT");
    }

    public <S, T> TableColumn<S, T> createTextColumn(String title, String property, String style) {
        return createTextColumn(title, property, 0, style);
    }

    public <S, T> TableColumn<S, T> createTextColumn(String title, String property, double minWidth) {
        return createTextColumn(title, property, minWidth, "-fx-alignment: CENTER_LEFT");
    }

    public <S, T> TableColumn<S, T> createTextColumn(String title, String property, double minWidth, String style) {
        TableColumn<S, T> col = new TableColumn<>(title);
        col.setCellValueFactory(new PropertyValueFactory<>(property));
        if (minWidth > 0) col.setMinWidth(minWidth);
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
            String title, String property, String trueText, String falseText) {

        TableColumn<S, Boolean> col = new TableColumn<>(title);
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

        col.setStyle("-fx-alignment: CENTER;");
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
                setGraphic(box);
            }
        });

        col.setStyle("-fx-alignment: CENTER;");
        return col;
    }

    public <S> TableColumn<S, String> createProfileColumn(
            int size) {

        TableColumn<S, String> profileColumn = new TableColumn<>();
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
                        try {
                            if (imagePath != null && !imagePath.isBlank()) {
                                image = new Image("file:" + imagePath, size, size, true, true);
                                if (image.isError()) throw new Exception("Invalid image");
                            } else {
                                throw new Exception("No imagePath");
                            }
                        } catch (Exception e) {
                            image = new Image(
                                    getClass().getResource(DEFAULT_AVATAR).toExternalForm(),
                                    size, size, true, true
                            );
                        }

                        imageView.setImage(image);
                        imageView.setFitWidth(size);
                        imageView.setFitHeight(size);
                        imageView.setClip(new Circle(size / 2.0, size / 2.0, size / 2.0));

                        setGraphic(imageView);
                    }
                };
            }
        });

        profileColumn.setPrefWidth(size + 20);
        profileColumn.setStyle("-fx-alignment: CENTER;");
        return profileColumn;
    }
}
