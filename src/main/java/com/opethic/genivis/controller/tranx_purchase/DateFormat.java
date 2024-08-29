package com.opethic.genivis.controller.tranx_purchase;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateFormat extends javafx.util.StringConverter<LocalDate> {    //class for date format in dd/mm/yyyy
    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @Override
    public String toString(LocalDate localDate) {
        if (localDate == null) {
            return "";
        }
        return dateFormatter.format(localDate);
    }

    @Override
    public LocalDate fromString(String dateString) {
        if (dateString == null || dateString.trim().isEmpty()) {
            return null;
        }
        return LocalDate.parse(dateString, dateFormatter);
    }
}
