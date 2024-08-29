package com.opethic.genivis.utils;

import com.opethic.genivis.dto.cmp_t_row.BatchWindowTableDTO;
import com.opethic.genivis.dto.pur_invoice.PurchaseInvoiceTable;
import javafx.application.Platform;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.input.KeyCode;

public class DateValidatorForBatchTable {

    private static final int MAX_LENGTH = 10;


    public static void applyDateFormat(TextField textField, int pos, TableView<BatchWindowTableDTO> tableView) {
        TextFormatter<String> textFormatter = new TextFormatter<>(change -> {
            String newText = change.getControlNewText();
            if (newText.matches("\\d{0,2}/\\d{0,2}/\\d{0,4}") && newText.length() <= MAX_LENGTH) {
                return change;
            } else {
                return null;
            }
        });

        textField.setTextFormatter(textFormatter);

        textField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                textField.setTextFormatter(null);
            } else {
                textField.setTextFormatter(textFormatter);
                validateDate(textField,pos,tableView);
            }
        });

        textField.setOnKeyReleased(event -> {
            String text = textField.getText();
            int length = text.length();

            if (event.getCode() == KeyCode.BACK_SPACE) {
                if (length == 3 || length == 6) {
                    if (text.charAt(length - 1) == '/') {
                        textField.deletePreviousChar();
                    }
                }
            } else {
                if (length == 2 || length == 5) {
                    textField.appendText("/");
                }
            }
        });
    }



    private static void validateDate(TextField textField,int pos, TableView<BatchWindowTableDTO> tableView) {

        Boolean dateValide=isValidDate(textField.getText());


        if (!dateValide) {
            // Show error alert

            AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, "Enter date in valid format DD/MM/YYYY", input -> {
                Platform.runLater(()->{
                    System.out.println("paltform");
                    tableView.edit(0, tableView.getColumns().get(pos));
                });

            });

        }
    }

    public static boolean isValidDate(String dateStr) {

        if(!dateStr.isEmpty()) {
            if(dateStr.length()==10) {
                String[] parts = dateStr.split("/");
                int day = Integer.parseInt(parts[0]);
                int month = Integer.parseInt(parts[1]);
                int year = Integer.parseInt(parts[2]);
                return isValidDate(day, month, year);
            }
            else{
                return false;
            }
        }
        else {
            return true;
        }
    }


    private static boolean isValidDate(int day, int month, int year) {

        if (year < 0 || month < 1 || month > 12 || day < 1 || day > 31)
            return false;

        if (month == 2) {

            if (year % 4 == 0 && (year % 100 != 0 || year % 400 == 0)) {
                return day <= 29;
            } else {
                return day <= 28;
            }
        } else if (month == 4 || month == 6 || month == 9 || month == 11) {
            return day <= 30;
        } else { // All other months
            return day <= 31;
        }
    }
}
