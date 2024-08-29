package com.opethic.genivis.controller.tranx_sales.common;

import com.opethic.genivis.utils.AutoCompleteBox;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.skin.ComboBoxListViewSkin;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
import javafx.util.StringConverter;
import javafx.util.converter.DefaultStringConverter;

public class ComboBoxTableCellForSalesInvoice<S, T> extends TableCell<S, T> {
    private final ObservableList<T> items;
    private ComboBox<T> comboBox;
    private ObjectProperty<StringConverter<T>> converter;
    private BooleanProperty comboBoxEditable;

    @SafeVarargs
    public static <S, T> Callback<TableColumn<S, T>, TableCell<S, T>> forTableColumn(T... var0) {
        return forTableColumn((StringConverter) null, (Object[]) var0);
    }

    @SafeVarargs
    public static <S, T> Callback<TableColumn<S, T>, TableCell<S, T>> forTableColumn(StringConverter<T> var0, T... var1) {
        return forTableColumn(var0, FXCollections.observableArrayList(var1));
    }

    public static <S, T> Callback<TableColumn<S, T>, TableCell<S, T>> forTableColumn(ObservableList<T> var0) {
        return forTableColumn((StringConverter) null, (ObservableList) var0);
    }

    public static <S, T> Callback<TableColumn<S, T>, TableCell<S, T>> forTableColumn(StringConverter<T> var0, ObservableList<T> var1) {
        return (var2) -> {
            return new ComboBoxTableCellForSalesInvoice(var0, var1);
        };
    }

    public ComboBoxTableCellForSalesInvoice() {
        this(FXCollections.observableArrayList());
    }

    @SafeVarargs
    public ComboBoxTableCellForSalesInvoice(T... var1) {
        this(FXCollections.observableArrayList(var1));
    }

    @SafeVarargs
    public ComboBoxTableCellForSalesInvoice(StringConverter<T> var1, T... var2) {
        this(var1, FXCollections.observableArrayList(var2));
    }

    public ComboBoxTableCellForSalesInvoice(ObservableList<T> var1) {
        this((StringConverter) null, (ObservableList) var1);
    }

    public ComboBoxTableCellForSalesInvoice(StringConverter<T> var1, ObservableList<T> var2) {
        this.converter = new SimpleObjectProperty(this, "converter");
        this.comboBoxEditable = new SimpleBooleanProperty(this, "comboBoxEditable");
//        this.getStyleClass().add("combo-box-table-cell");
        this.items = var2;
        this.setConverter(var1 != null ? var1 : (StringConverter<T>) new DefaultStringConverter());
        initCombobox();
    }

    private void initCombobox() {
//        if (this.comboBox == null) {
        this.comboBox = createComboBox(this, this.items, this.converterProperty());
//        this.comboBox.editableProperty().bind(this.comboBoxEditableProperty());
        this.comboBox.setPromptText("Select");
        comboBox.setPrefHeight(38);
        comboBox.setMaxHeight(38);
        comboBox.setMinHeight(38);
        comboBox.setPrefWidth(this.getPrefWidth());
        this.comboBox.setStyle("-fx-background-color: #f6f6f9; -fx-border-radius: 0px; -fx-border-width: 1; -fx-border-color: #f6f6f9;");
        this.comboBox.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            if (isNowFocused) {
                this.comboBox.setStyle("-fx-background-color: #fff9c4; -fx-border-radius: 0px; -fx-border-width: 1.5; -fx-border-color: #00a0f5;");
            } else {
                this.comboBox.setStyle("-fx-background-color: #f6f6f9; -fx-border-radius: 0px; -fx-border-width: 1.5; -fx-border-color: #f6f6f9;");
            }
        });

        this.comboBox.hoverProperty().addListener((obs, wasHovered, isNowHovered) -> {
            if (isNowHovered && !comboBox.isFocused()) {
                this.comboBox.setStyle("-fx-background-color: #f6f6f9; -fx-border-radius: 0px; -fx-border-width: 1; -fx-border-color: #0099ff;");
            } else if (!comboBox.isFocused()) {
                this.comboBox.setStyle("-fx-background-color: #f6f6f9; -fx-border-radius: 0px; -fx-border-width: 1; -fx-border-color: #f6f6f9;");
            }
        });
        this.comboBox.setOnKeyReleased((e) -> {
//            System.out.println("combobox Space " + e.getCode());
            if (e.getCode() == KeyCode.SPACE || e.getCode() == KeyCode.DOWN) {
                this.comboBoxEditable.set(true);
                this.comboBox.show();
            }
        });
        this.comboBox.setFocusTraversable(false);
//        AutoCompleteBox autoCompleteBox = new AutoCompleteBox(this.comboBox,-1);
    }

    public final ObjectProperty<StringConverter<T>> converterProperty() {
        return this.converter;
    }

    public final void setConverter(StringConverter<T> var1) {
        this.converterProperty().set(var1);
    }

    public final StringConverter<T> getConverter() {
        return (StringConverter) this.converterProperty().get();
    }

    public final BooleanProperty comboBoxEditableProperty() {
        return this.comboBoxEditable;
    }

    public final void setComboBoxEditable(boolean var1) {
        this.comboBoxEditableProperty().set(var1);
    }

    public final boolean isComboBoxEditable() {
        return this.comboBoxEditableProperty().get();
    }

    public ObservableList<T> getItems() {
        return this.items;
    }

    static <T> ComboBox<T> createComboBox(final Cell<T> var0, ObservableList<T> var1, ObjectProperty<StringConverter<T>> var2) {
        final ComboBox var3 = new ComboBox(var1);
        var3.converterProperty().bind(var2);

        var3.setMaxWidth(Double.MAX_VALUE);
        var3.addEventFilter(KeyEvent.KEY_RELEASED, (var2x) -> {
            System.out.println("Combobox Key Releases => " + var2x);
            if (var2x.getCode() == KeyCode.ENTER) {
                tryComboBoxCommit(var3, var0);
            } else if (var2x.getCode() == KeyCode.ESCAPE) {
                var0.cancelEdit();
            }

        });
        var3.getEditor().focusedProperty().addListener((var2x) -> {
            if (!var3.isFocused()) {
                tryComboBoxCommit(var3, var0);
            }
        });
        boolean var4 = listenToComboBoxSkin(var3, var0);
        if (!var4) {
            var3.skinProperty().addListener(new InvalidationListener() {
                public void invalidated(Observable var1) {
                    boolean var2 = listenToComboBoxSkin(var3, var0);
                    if (var2) {
                        var3.skinProperty().removeListener(this);
                    }

                }
            });
        }

        return var3;
    }

    private static <T> void tryComboBoxCommit(ComboBox<T> var0, Cell<T> var1) {
        StringConverter var2 = var0.getConverter();
        if (var0.isEditable() && var2 != null) {
            Object var3 = var2.fromString(var0.getEditor().getText());
            var1.commitEdit((T) var3);
        } else {
            var1.commitEdit(var0.getValue());
        }

    }

    private static <T> String getItemText(Cell<T> var0, StringConverter<T> var1) {
        return var1 == null ? (var0.getItem() == null ? "" : var0.getItem().toString()) : var1.toString(var0.getItem());
    }

    private static <T> boolean listenToComboBoxSkin(ComboBox<T> var0, Cell<T> var1) {
        Skin var2 = var0.getSkin();
        if (var2 != null && var2 instanceof ComboBoxListViewSkin var3) {
            Node var4 = var3.getPopupContent();
            if (var4 != null && var4 instanceof ListView) {
                var4.addEventHandler(MouseEvent.MOUSE_RELEASED, (var2x) -> {
                    var1.commitEdit(var0.getValue());
                });
                return true;
            }
        }

        return false;
    }

    static <T> void updateItem(Cell<T> var0, StringConverter<T> var1, HBox var2, Node var3, ComboBox<T> var4) {
        if (var0.isEmpty()) {
            var0.setText((String) null);
            var0.setGraphic((Node) null);
        } else if (var0.isEditing()) {
            var2.setSpacing(5);
            if (var4 != null) {
                var4.getSelectionModel().select(var0.getItem());
            }

            var0.setText((String) null);
            if (var3 != null) {
                var2.getChildren().setAll(new Node[]{var3, var4});
                var0.setGraphic(var2);
            } else {
                var0.setGraphic(var4);
            }
        } else {
            var0.setText(getItemText(var0, var1));
            var0.setGraphic(var3);
        }

    }

    public void startEdit() {
        super.startEdit();
//        if (this.isEditing()) {
        this.comboBox.getSelectionModel().select(this.getItem());
        this.setText((String) null);
        this.setGraphic(new HBox(this.comboBox));
        this.comboBox.requestFocus();
//        }
    }

    public void cancelEdit() {
        super.cancelEdit();
        this.setText(this.getConverter().toString(this.getItem()));
        this.setGraphic((Node) null);
    }

    public void updateItem(T var1, boolean var2) {
        super.updateItem(var1, var2);
        updateItem(this, this.getConverter(), (HBox) null, (Node) null, this.comboBox);
    }
}
