package com.opethic.genivis.controller.tranx_sales.common;

import com.opethic.genivis.dto.reqres.product.Communicator;
import com.opethic.genivis.models.tranx.sales.*;
import com.opethic.genivis.utils.GlobalTranx;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Cell;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.StringConverter;
import javafx.util.converter.DefaultStringConverter;

import java.util.List;

public class TextFieldTableCellForSalesInvoiceWithConverter<S, T> extends TableCell<S, T> {
    private TextField textField;
    private ObjectProperty<StringConverter<T>> converter;

    public static <S> Callback<TableColumn<S, String>, TableCell<S, String>> forTableColumn() {
        return forTableColumn(new DefaultStringConverter());
    }

    public static <S, T> Callback<TableColumn<S, T>, TableCell<S, T>> forTableColumn(StringConverter<T> var0) {
        return (var1) -> {
            return new TextFieldTableCellForSalesInvoiceWithConverter(var0);
        };
    }

    public TextFieldTableCellForSalesInvoiceWithConverter() {
        this((StringConverter) null);
    }

    public TextFieldTableCellForSalesInvoiceWithConverter(StringConverter<T> var1) {
        this.converter = new SimpleObjectProperty(this, "converter");
        this.getStyleClass().add("text-field-table-cell");
        this.setConverter(var1);
        initTextField();
    }

    private static <T> String getItemText(Cell<T> var0, StringConverter<T> var1) {
        return var1 == null ? (var0.getItem() == null ? "" : var0.getItem().toString()) : var1.toString(var0.getItem());
    }

    private <T> TextField createTextField(Cell<T> var0, StringConverter<T> var1) {
        TextField var2 = new TextField(getItemText(var0, var1));
        var2.setOnAction((var3) -> {
            if (var1 == null) {
                throw new IllegalStateException("Attempting to convert text input into Object, but provided StringConverter is null. Be sure to set a StringConverter in your cell factory.");
            } else {
                var0.commitEdit(var1.fromString(var2.getText()));
                var3.consume();
            }
        });
        var2.setOnKeyPressed((var1x) -> {
            if (var1x.getCode() == KeyCode.ENTER) {
                var0.commitEdit(var1.fromString(var2.getText()));
                var1x.consume();
            }
            if (var1x.getCode() == KeyCode.TAB) {
                var0.commitEdit(var1.fromString(var2.getText()));
                var1x.consume();
            }
        });
        var2.setOnKeyReleased((var1x) -> {
            if (var1x.getCode() == KeyCode.ESCAPE) {
                var0.cancelEdit();
                var1x.consume();
            }
        });
        var2.setOnMouseClicked((var1x) -> {
            var0.startEdit();
            var1x.consume();
        });
        var2.focusedProperty().addListener((obs, old, nw) -> {
            if (nw) {
                var0.startEdit();
            }/* else {
                var0.commitEdit(var1.fromString(var2.getText()));
            }*/
            //! Commit when focus is lost
            /*if(!nw){
                var0.commitEdit(var1.fromString(var2.getText()));
            }*/
        });
        return var2;
    }

    private void initTextField() {
        this.textField = createTextField(this, this.getConverter());
        this.textField.setPrefHeight(38);
        this.textField.setMaxHeight(38);
        this.textField.setMinHeight(38);
        this.textField.setPrefWidth(this.getPrefWidth());
        this.textField.setStyle("-fx-background-color: #f6f6f9; -fx-alignment: CENTER-RIGHT; " +
//                "-fx-border-radius: 0px; -fx-border-width: 1; -fx-border-color: #f6f6f9;" +
                "");
        this.textField.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            if (isNowFocused) {
                this.textField.setStyle("-fx-background-color: #fff9c4; -fx-alignment: CENTER-RIGHT;" +
//                        " -fx-border-radius: 0px; -fx-border-width: 1.5; -fx-border-color: #00a0f5;" +
                        "");
            } else {
                this.textField.setStyle("-fx-background-color: #f6f6f9; -fx-alignment: CENTER-RIGHT;" +
//                        " -fx-border-radius: 0px; -fx-border-width: 1.5; -fx-border-color: #f6f6f9;" +
                        "");
            }
        });
        this.textField.setOnMouseEntered(e -> {
            this.textField.setStyle("-fx-background-color: #f6f6f9; -fx-alignment: CENTER-RIGHT;" +
//                    " -fx-border-radius: 0px; -fx-border-width: 1; -fx-border-color:   #0099ff;" +
                    "");
        });

        this.textField.setOnMouseExited(e -> {
            if (!this.textField.isFocused()) {
                this.textField.setStyle("-fx-background-color: #f6f6f9; -fx-alignment: CENTER-RIGHT; " +
//                        "-fx-border-radius: 0px; -fx-border-width: 1; -fx-border-color: #f6f6f9;" +
                        "");
            }
        });


        startEdit(this, this.getConverter(), (VBox) null, this.textField);
    }

    public final ObjectProperty<StringConverter<T>> converterProperty() {
        return this.converter;
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

    public final void setConverter(StringConverter<T> var1) {
        this.converterProperty().set(var1);
    }

    public final StringConverter<T> getConverter() {
        return (StringConverter) this.converterProperty().get();
    }

    @Override
    public void startEdit() {
        super.startEdit();
        startEdit(this, this.getConverter(), (VBox) null, this.textField);
    }

    @Override
    public void cancelEdit() {
        super.cancelEdit();
        cancelEdit(this, this.getConverter(), (Node) null);
    }

    @Override
    public void updateItem(T var1, boolean var2) {
        super.updateItem(var1, var2);
        updateItem(this, this.getConverter(), (VBox) null, this.textField);
    }

    public void openBatchWindow() {
        TranxRow selectedRow = Communicator.tblSaleTranx.getSelectionModel().getSelectedItem();
        if (selectedRow.isBatch() == true) {
            TranxCommonPopUps.openBatchPopUp(Communicator.stage, selectedRow, Communicator.tranxDate.toString(), "Batch", input -> {
                Integer selectedIndex = Communicator.tblSaleTranx.getSelectionModel().getSelectedIndex();
                if (input != null) {
                    selectedRow.setSelectedBatch(input);
                    selectedRow.setbDetailId(input.getId());
                    selectedRow.setbNo(input.getBatchNo());
//                    selectedRow.setRate(input.minRateA);
                }
                Communicator.tblSaleTranx.getItems().set(selectedIndex, selectedRow);
//                TableColumn<TranxRow, ?> focuscolumn = visibleColumns.get(column + 1);
//                Communicator.tblSaleTranx.edit(row, focuscolumn);
            });
        } else {
            selectedRow.setbNo("#");
        }
    }
}
