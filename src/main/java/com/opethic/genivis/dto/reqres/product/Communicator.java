package com.opethic.genivis.dto.reqres.product;


import com.opethic.genivis.models.tranx.sales.TranxRow;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import javafx.util.converter.LocalDateStringConverter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Communicator {
    public static Boolean isBatch;
    public static Stage stage;

    public static Stage stage2;

    public static DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    public static StringConverter<LocalDate> text_to_date = new LocalDateStringConverter(dateFormatter, dateFormatter);
    public static TableView<TranxRow> tblSaleTranx;
    public static String tranxDate;
    public static String purTranxDate;

    public static String ledgerCode;
    public static String ledgerId;
}
