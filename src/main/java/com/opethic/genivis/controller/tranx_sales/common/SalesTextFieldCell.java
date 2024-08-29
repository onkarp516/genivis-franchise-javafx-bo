package com.opethic.genivis.controller.tranx_sales.common;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Cell;
import javafx.scene.control.TableCell;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;
import javafx.util.converter.DefaultStringConverter;

public class SalesTextFieldCell<S, T> extends TableCell<S, T> {
    private TextField textField;
    private ObjectProperty<StringConverter<T>> converter;

    public SalesTextFieldCell() {
        this.textField = new TextField();
        this.converter = new SimpleObjectProperty(this, "converter");
        this.setConverter(new DefaultStringConverter());
        this.textField.setOnAction(event -> commitEdit((T) textField.getText()));
        // Commit when focus is lost
        this.textField.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) {
                commitEdit((T) textField.getText());
            }
        });

        // Commit when Tab key is pressed
        this.textField.setOnKeyPressed(event -> {
            System.out.println("TextField On Key Preesed CMPTROW ->" + event);
            if (event.getCode() == KeyCode.TAB) {
                commitEdit((T) textField.getText());
            }
            if (event.getCode() == KeyCode.ENTER) {
                commitEdit((T) textField.getText());
            }
        });
        textfieldStyle();

    }

    public SalesTextFieldCell(StringConverter<T> var0) {
        this.textField = new TextField();
        this.converter = new SimpleObjectProperty(this, "converter");
        this.setConverter(var0);
        this.textField.setOnAction(event -> commitEdit(var0.fromString(textField.getText())));
        // Commit when focus is lost
        this.textField.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) {
                commitEdit(var0.fromString(textField.getText()));
            }
        });

        // Commit when Tab key is pressed
        this.textField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.TAB) {
                commitEdit(var0.fromString(textField.getText()));
            }
            if (event.getCode() == KeyCode.ENTER) {
                commitEdit(var0.fromString(textField.getText()));
            }
        });
        textfieldStyle();
    }

    private static <T> String getItemText(Cell<T> var0, StringConverter<T> var1) {
        return var1 == null ? (var0.getItem() == null ? "" : var0.getItem().toString()) : var1.toString(var0.getItem());
    }

    public void textfieldStyle() {
        textField.setPrefHeight(38);
        textField.setMaxHeight(38);
        textField.setMinHeight(38);

        textField.setStyle("-fx-background-color: #f6f6f9; -fx-alignment: CENTER-RIGHT; -fx-border-radius: 0px; -fx-border-width: 1; -fx-border-color: #f6f6f9;");
        textField.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            if (isNowFocused) {
                textField.setStyle("-fx-background-color: #fff9c4; -fx-alignment: CENTER-RIGHT; -fx-border-radius: 0px; -fx-border-width: 1.5; -fx-border-color: #00a0f5;");
            } else {
                textField.setStyle("-fx-background-color: #f6f6f9; -fx-alignment: CENTER-RIGHT; -fx-border-radius: 0px; -fx-border-width: 1.5; -fx-border-color: #f6f6f9;");
            }
        });
        textField.setOnMouseEntered(e -> {
            textField.setStyle("-fx-background-color: #f6f6f9; -fx-alignment: CENTER-RIGHT; -fx-border-radius: 0px; -fx-border-width: 1; -fx-border-color:   #0099ff;");
        });

        textField.setOnMouseExited(e -> {
            if (!textField.isFocused()) {
                textField.setStyle("-fx-background-color: #f6f6f9; -fx-alignment: CENTER-RIGHT; -fx-border-radius: 0px; -fx-border-width: 1; -fx-border-color: #f6f6f9;");
            }
        });
    }

    static <T> void startEdit(Cell<T> var0, StringConverter<T> var1, VBox var2, TextField var4) {
        if (var4 != null) {
            var4.setText(getItemText(var0, var1));
        }

        var0.setText((String) null);
        var0.setGraphic(new VBox(var4));

        var4.selectAll();
        var4.requestFocus();
    }


    static <T> void cancelEdit(Cell<T> var0, StringConverter<T> var1, Node var2) {
        var0.setText(getItemText(var0, var1));
        var0.setGraphic(var2);
    }

    static <T> void updateItem(Cell<T> var0, StringConverter<T> var1, VBox var2, TextField var4) {
        if (var0.isEmpty()) {
            var0.setText((String) null);
            var0.setGraphic((Node) null);
        } else {
            VBox vbox = new VBox(var4);
            vbox.setPrefWidth(var4.getPrefWidth());
            vbox.setAlignment(Pos.CENTER);
            if (var4 != null) {
                var4.setText(getItemText(var0, var1));
            }
            var0.setGraphic(vbox);
        }

    }

    @Override
    public void startEdit() {
        super.startEdit();

        startEdit(this, this.getConverter(), (VBox) null, this.textField);
    }

    @Override
    public void cancelEdit() {
        super.cancelEdit();
//        setText((String) getItem());
//        setGraphic(null);
        cancelEdit(this, this.getConverter(), (Node) null);
    }


    @Override
    protected void updateItem(T item, boolean empty) {
//        super.updateItem((T) item, empty);
//        if (empty) {
//            setGraphic(null);
//        } else {
//            VBox vbox = new VBox(textField);
//            vbox.setAlignment(Pos.CENTER);
//            textField.setText(item.toString());
//            setGraphic(vbox);
//        }
        super.updateItem(item, empty);
        updateItem(this, this.getConverter(), (VBox) null, this.textField);
    }

    @Override
    public void commitEdit(T newValue) {
        super.commitEdit((T) newValue);

    }

    public StringConverter getConverter() {
        return converter.get();
    }

    public ObjectProperty<StringConverter<T>> converterProperty() {
        return converter;
    }

    public void setConverter(StringConverter converter) {
        this.converter.set(converter);
    }
}
