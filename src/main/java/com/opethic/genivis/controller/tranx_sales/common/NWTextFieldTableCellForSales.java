package com.opethic.genivis.controller.tranx_sales.common;

import com.opethic.genivis.models.tranx.sales.TranxRow;
import com.opethic.genivis.utils.GlobalTranx;
import com.opethic.genivis.utils.Globals;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.Event;
import javafx.geometry.Pos;
import javafx.scene.control.TableCell;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;
import javafx.util.converter.DefaultStringConverter;
import javafx.util.converter.DoubleStringConverter;

import java.util.function.Consumer;

public class NWTextFieldTableCellForSales<S, T> extends TableCell<TranxRow, T> {
    private TextField tf;
    private String clmn;
    private ObjectProperty<StringConverter<T>> converter;
    private Consumer<TranxConsumer> consumer;
    private Boolean isEditable;


    public NWTextFieldTableCellForSales(String clmn, Boolean isEditable, Consumer<TranxConsumer> callback) {
        this.clmn = clmn;
        this.converter = new SimpleObjectProperty<>(this, "converter");
        this.setConverter(new DefaultStringConverter());
        this.isEditable = isEditable;
        consumer = callback;
        setUpTextField();
    }

    public NWTextFieldTableCellForSales(String clmn, Boolean isEditable, StringConverter<T> inConverter, Consumer<TranxConsumer> callback) {
        this.clmn = clmn;
        this.converter = new SimpleObjectProperty<>(this, "converter");
        this.setConverter(inConverter);
        this.isEditable = isEditable;
        consumer = callback;
        setUpTextField();
    }

    private void setUpTextField() {
        this.tf = new TextField();
        this.tf.setFocusTraversable(true);
        this.tf.setOnAction(e -> commitEdit((T) tf.getText()));
        this.tf.setEditable(this.isEditable != null ? this.isEditable : true);
//        this.tf.setEditable(true);
        this.tf.getStyleClass().add("tbl-editable-cell");

        this.tf.setOnKeyReleased(event -> {
            if (event.getCode() == KeyCode.TAB) {
                if (event.getTarget() instanceof TextField reqTF) {
                    if (this.isEditable != null && this.isEditable == true) {
                        if (reqTF.getStyleClass().contains("isRequired") && !reqTF.getText().isEmpty()) {
                            Platform.runLater(() -> {
                                GlobalTranx.requestFocusOrDieTrying(reqTF, 2);
                            });
                        }
                    }
                }
                commitEdit((T) tf.getText());
            }

            //! if textField is not editable then call to callback function
            if (this.isEditable != null && this.isEditable == false) {
                if (event.getCode() == KeyCode.SPACE) {
                    if (consumer != null) consumer.accept(new TranxConsumer(this.clmn, getIndex()));
                    event.consume();
                }
            }

            //! Move next Field
            if (event.getCode() == KeyCode.ENTER) {
                KeyEvent newEvent = new KeyEvent(
                        null,
                        null,
                        KeyEvent.KEY_PRESSED,
                        "",
                        "\t",
                        KeyCode.TAB,
                        event.isShiftDown(),
                        event.isControlDown(),
                        event.isAltDown(),
                        event.isMetaDown()
                );

                Event.fireEvent(event.getTarget(), newEvent);
                event.consume();

            }
        });
    }

    public TextField getTf() {
        return tf;
    }

    public void setTf(TextField tf) {
        this.tf = tf;
    }

    public String getClmn() {
        return clmn;
    }

    public void setClmn(String clmn) {
        this.clmn = clmn;
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

    public Consumer<TranxConsumer> getConsumer() {
        return consumer;
    }

    public void setConsumer(Consumer<TranxConsumer> consumer) {
        this.consumer = consumer;
    }

    public Boolean getEditable() {
        return isEditable;
    }

    public void setEditable(Boolean editable) {
        isEditable = editable;
    }


    @Override
    public void startEdit() {
        super.startEdit();
        setText(null);
        setGraphic(new VBox(tf));
    }

    @Override
    public void cancelEdit() {
        super.cancelEdit();
        setText((String) getItem());
        setGraphic(null);
    }

    @Override
    protected void updateItem(T item, boolean empty) {
        super.updateItem(item, empty);
        if (empty) {
            setGraphic(null);
        } else {
            VBox vBox = new VBox(tf);
            vBox.setAlignment(Pos.CENTER);
            tf.setText(item != null ? item.toString() : "");
            setGraphic(vBox);
        }
    }

    @Override
    public void commitEdit(T nwValue) {
        super.commitEdit(nwValue);
        TranxRow tranxRow = getTableRow().getItem();
        if (tranxRow != null) {
            switch (this.clmn) {
                case "tcTranxQty":
                    (getTableRow().getItem()).setQty(Integer.valueOf((String) nwValue));
                    break;
                case "tcTranxFreeQty":
                    (getTableRow().getItem()).setFreeqty(Integer.valueOf((String) nwValue));
                    break;
                case "tcTranxRate":
                    (getTableRow().getItem()).setRate(Double.valueOf((String) nwValue));
                    break;
                case "tcTranx1DisPer":
                    (getTableRow().getItem()).setDisPer(Double.valueOf((String) nwValue));
                    break;
                case "tcTranx2DisPer":
                    (getTableRow().getItem()).setDisPer2(Double.valueOf((String) nwValue));
                    break;
                case "tcTranxDisAmt":
                    (getTableRow().getItem()).setDisAmt(Double.valueOf((String) nwValue));
                    break;
            }
        }

        if (consumer != null && this.isEditable != null && this.isEditable != false) {
            consumer.accept(new TranxConsumer(this.clmn, getIndex()));
        }
    }
}
