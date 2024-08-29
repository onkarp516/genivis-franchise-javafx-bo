package com.opethic.genivis.utils;


import com.opethic.genivis.controller.commons.OverlaysEffect;
import com.opethic.genivis.dto.reqres.product.Communicator;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

import static com.opethic.genivis.dto.reqres.product.Communicator.stage;

public class DateValidator
{
    private static final int MAX_LENGTH = 10;


    public static void applyDateFormat(TextField textField) {

        TextFormatter<String> textFormatter = new TextFormatter<>(change -> {
            String newText = change.getControlNewText();
            if (newText.isEmpty() || newText.matches("(0?[1-9]|[12][0-9]|3[01])/(0?[1-9]|1[0-2])/((19|20)\\d\\d)")) {
//                System.out.println("hello called" + change);
                return change;
            } else {
//                System.out.println("hello called ASDF" );
                return null;
            }
        });
        textField.setTextFormatter(textFormatter);
        textField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(final ObservableValue<? extends String> ov, final String oldValue, final String newValue) {
                if (textField.getText().length() > 10) {
                    String s = textField.getText().substring(0, 10);
                    textField.setText(s);
                }
            }
        });


        textField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                textField.setTextFormatter(null);
            } else {
                textField.setTextFormatter(textFormatter);
                validateDate(textField);
            }
        });

        textField.setOnKeyReleased(event -> {
            String text = textField.getText();
            int length = text.length();

            if (length > 10) {
                textField.deletePreviousChar();
            }
            else {

                if (event.getCode() == KeyCode.BACK_SPACE) {
                    if ((length == 3 || length == 6) && text.endsWith("/")) {
                        textField.deletePreviousChar();
                    }
                } else {
                    if (length == 2 || length == 5) {
                        String[] parts = text.split("/");
                        if (parts.length > 0 && parts[0].matches("\\d{1,2}")) {
                            int day = Integer.parseInt(parts[0]);
                            if (day > 31) {
                                showAlert("Enter a valid day");
                                textField.deletePreviousChar();
                            } else if (length == 2) {
                                textField.appendText("/");
                            }
                        }
                        if (parts.length > 1 && parts[1].matches("\\d{1,2}")) {
                            int month = Integer.parseInt(parts[1]);
                            if (month > 12) {
                                showAlert("Enter a valid month");
                                textField.deletePreviousChar();
                            } else if (length == 5) {
                                textField.appendText("/");
                            }
                        }
                    }
                }
            }
        });


//        textField.setOnKeyTyped(event -> {
//            String character = event.getCharacter();
//            if (!textField.getText().isEmpty() && !character.matches("\\d|/|\b")) {
//                event.consume();
//                showAlert("Please enter only numeric values");
//                textField.clear();
//            }
//        });
    }

    private static void showAlert(String message) {
        AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, message, input -> {
        });
    }


//    public static void applyDateFormat(TextField textField) {
//
//        textField.setTextFormatter(new TextFormatter<>(change -> {
//            if (change.isDeleted() || change.isContentChange()) {
//                if (change.getControlNewText().length() <= MAX_LENGTH) {
//                    return change;
//                }
//            }
//            return null;
//        }));
//
//        textField.setOnKeyReleased(event -> {
//            String text = textField.getText();
//            int length = text.length();
//
//            if (event.getCode() == KeyCode.BACK_SPACE) {
//                if (length == 3 || length == 6) {
//                    if (text.charAt(length - 1) == '/') {
//                        textField.deletePreviousChar();
//                    }
//                }
//            } else {
//                if (length == 2 || length == 5) {
//                    textField.appendText("/");
//                }
//            }
//        });
//
//
//        textField.focusedProperty().addListener((observable, oldValue, newValue) -> {
//            if (!newValue) {
//                validateDate(textField);
//            }
//        });
//    }


    private static void validateDate(TextField textField) {

        Boolean dateValide=isValidDate(textField.getText());


        if (!dateValide) {
            // Show error alert

            AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, "Enter date in valid format DD/MM/YYYY", input -> {
                textField.requestFocus();
            });

//            AlertUtility.CustomCallback callback = (number) -> {
//                if(number==0) {
//                    textField.clear();
//                    textField.requestFocus();
//                }
//            };
//            AlertUtility.AlertError(Communicator.stage,AlertUtility.alertTypeError, "Enter date in valid format DD/MM/YYYY", callback);
//            Alert alert = new Alert(Alert.AlertType.ERROR);
//            alert.setTitle("Invalid Date");
//            alert.setHeaderText(null);
//            alert.setContentText("Please enter a valid date in the format dd/MM/yyyy.");
//            alert.showAndWait();

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

