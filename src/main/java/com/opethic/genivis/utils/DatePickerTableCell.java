package com.opethic.genivis.utils;

import com.opethic.genivis.dto.TranxBatchWindowDTO;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableCell;
import javafx.util.StringConverter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DatePickerTableCell extends TableCell<TranxBatchWindowDTO, LocalDate> {
    private final DatePicker datePicker;

    public DatePickerTableCell() {
        this.datePicker = new DatePicker();
        this.datePicker.setConverter(new StringConverter<LocalDate>() {
            final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            @Override
            public String toString(LocalDate object) {
                if (object != null) {
                    return dateFormatter.format(object);
                } else {
                    return "";
                }
            }

            @Override
            public LocalDate fromString(String string) {
                if (string != null && !string.isEmpty()) {
                    return LocalDate.parse(string, dateFormatter);
                } else {
                    return null;
                }
            }
        });

        // When DatePicker value changes, commit edit
        this.datePicker.setOnAction(event -> {
            commitEdit(this.datePicker.getValue());
        });
    }

    @Override
    public void updateItem(LocalDate item, boolean empty) {
        super.updateItem(item, empty);
        if (empty) {
            setText(null);
            setGraphic(null);
        } else {
            this.datePicker.setValue(item);
            setGraphic(this.datePicker);
        }
    }
}
