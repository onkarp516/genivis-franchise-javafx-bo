package com.opethic.genivis.utils;

import com.opethic.genivis.GenivisApplication;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class CommonFunctionalUtils {
    private static Node[] focusableNodes;
    private static int currentIndex = 0;


    public static void cursorMomentForTextField(TextField textField) {
        textField.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) {
                String text = textField.getText().trim();

                if (text.isEmpty()) {
                    textField.requestFocus();
                }
            }
        });
    }

    public static void onlyEnterCharacters(TextField textField) {
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("[a-zA-Z]*")) {
                textField.setText(newValue.replaceAll("[^a-zA-Z]", ""));
            }
        });
    }


    public static void cursorMomentForRowTextField(TextField textField) {
        textField.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) {
                String text = textField.getText().trim();

                if (text.isEmpty() || text.equals("0") || text.equals(0)) {
                    textField.requestFocus();
                }
            }
        });
    }


    public static void cursorMomentForTextFieldWithParticularLenght(TextField textField, int lengthVal) {
        textField.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) {
                String text = textField.getText().trim();
//                if (textField.equals(tfAreaHeadCreateAdhaarText)) {
//                    btnAreaHeadCreateImgAdhaar.requestFocus();
//                }
                if (text.length()!=lengthVal) {
                    textField.requestFocus();
                }
            }
        });
    }


    public static void cursorMomentForComboBox(ComboBox<?> comboBox) {
        comboBox.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) {
                if (comboBox.getSelectionModel().getSelectedIndex() == -1) {
                    // No item selected in the ComboBox
                    comboBox.requestFocus();
                }
            }
        });
    }

    public static void commonInit(TextField passText, TextField plainText, Button eyeButton) {
        passText.managedProperty().bind(passText.visibleProperty());
        plainText.managedProperty().bind(plainText.visibleProperty());

        ImageView imageView;
        imageView = new ImageView(new javafx.scene.image.Image(GenivisApplication.class.getResourceAsStream("/com/opethic/genivis/ui/assets/eye_hidden.png")));
        imageView.setFitWidth(18);
        imageView.setFitHeight(18);
        eyeButton.setGraphic(imageView);

    }

    public static void passwordField(TextField passText, TextField plainText, Button eyeButton) {
        boolean visibility = plainText.isVisible();
        String passVal;
        ImageView imageView;

        if (visibility) {
            passVal = plainText.getText();
            passText.setText(passVal);
            imageView = new ImageView(new javafx.scene.image.Image(GenivisApplication.class.getResourceAsStream("/com/opethic/genivis/ui/assets/eye_hidden.png")));
        } else {
            imageView = new ImageView(new javafx.scene.image.Image(GenivisApplication.class.getResourceAsStream("/com/opethic/genivis/ui/assets/eye.png")));
        }

        imageView.setFitWidth(18);
        imageView.setFitHeight(18);
        eyeButton.setGraphic(imageView);
        passText.setVisible(visibility);
        passVal = passText.getText();
        plainText.setText(passVal);
        plainText.setVisible(!visibility);

    }

    private static void findLabelsRecursively(Parent parent, List<Label> labelList) {
        for (var node : parent.getChildrenUnmodifiable()) {
            if (node instanceof Label) {
                labelList.add((Label) node);
            } else if (node instanceof Parent) {
                findLabelsRecursively((Parent) node, labelList);
            }
        }
    }

    // Method to style the asterisk in a label's text
    private static TextFlow styleAsteriskInText(String text) {
        TextFlow textFlow = new TextFlow();

        if (text.contains("*")) {
            String[] parts = text.split("\\*", -1); // Split the text by asterisk
            for (int i = 0; i < parts.length; i++) {
                textFlow.getChildren().add(new Text(parts[i])); // Add the regular text
                if (i < parts.length - 1) {
                    Text redAsterisk = new Text("*");
                    redAsterisk.setStyle("-fx-fill: red;"); // Style the asterisk in red
                    textFlow.getChildren().add(redAsterisk);
                }
            }
        } else {
            textFlow.getChildren().add(new Text(text)); // If no asterisk, add the text as is
        }

        return textFlow;
    }

    /**
     * @implNote Method to restrict mobile number TextField to 10 digits
     * @auther vinitc@opethic.com
     * @JavaFx migration
     * @Date 02/03/2024
     **/

    //******************************************Validation START******************************************************
    //TEXTFIELD
    public static void restrictTextFieldToDigitsAndDateFormat(TextField textField) {
        Pattern pattern = Pattern.compile("\\d*");
        textField.setTextFormatter(new TextFormatter<>(change -> {
            String newText = change.getControlNewText().replaceAll("\\D", "");
            if (newText.length() > 8 || !pattern.matcher(newText).matches()) {
                return null;
            }
            return change;
        }));

        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            String digitsOnly = newValue.replaceAll("\\D", "");
            StringBuilder formattedText = new StringBuilder();

            for (int i = 0; i < digitsOnly.length(); i++) {
                if ((i == 2 || i == 4) && i < digitsOnly.length()) {
                    formattedText.append('/');
                }
                formattedText.append(digitsOnly.charAt(i));
            }
            if (formattedText.length() > 10) {
                formattedText.setLength(10);
            }
            if (!newValue.equals(formattedText.toString())) {
                textField.setText(formattedText.toString());
            }
        });
    }

    public static void restrictTextFieldToDigitsAndBirthDateFormat(TextField textField) {
        Pattern pattern = Pattern.compile("\\d*");
        textField.setTextFormatter(new TextFormatter<>(change -> {
            String newText = change.getControlNewText().replaceAll("\\D", "");
            if (newText.length() > 8 || !pattern.matcher(newText).matches()) {
                return null;
            }
            return change;
        }));

        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            String digitsOnly = newValue.replaceAll("\\D", "");
            StringBuilder formattedText = new StringBuilder();

            for (int i = 0; i < digitsOnly.length(); i++) {
                if ((i == 2 || i == 4) && i < digitsOnly.length()) {
                    formattedText.append('/');
                }
                formattedText.append(digitsOnly.charAt(i));
            }
            if (formattedText.length() > 10) {
                formattedText.setLength(10);
            }
            if (!newValue.equals(formattedText.toString())) {
                textField.setText(formattedText.toString());
            }
        });

        textField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) { // when focus is lost
                validateDate(textField);
            }
        });
    }

    private static void validateDate(TextField textField) {
        String text = textField.getText();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        try {
            LocalDate enteredDate = LocalDate.parse(text, dateFormatter);
            LocalDate currentDate = LocalDate.now();

            if (enteredDate.isAfter(currentDate)) {
                showError("Date cannot be in the future.");
                AlertUtility.AlertError(AlertUtility.alertTypeError, "Birth Date should not be grather than current date", in -> {
                    textField.requestFocus();
                });
            } else {
                clearError();
            }
        } catch (DateTimeParseException e) {
            showError("Invalid date format.");
        }
    }

    public static void tranxDateGreaterThanCurrentDate(TextField textField) {
        Pattern pattern = Pattern.compile("\\d*");
        textField.setTextFormatter(new TextFormatter<>(change -> {
            String newText = change.getControlNewText().replaceAll("\\D", "");
            if (newText.length() > 8 || !pattern.matcher(newText).matches()) {
                return null;
            }
            return change;
        }));

        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            String digitsOnly = newValue.replaceAll("\\D", "");
            StringBuilder formattedText = new StringBuilder();

            for (int i = 0; i < digitsOnly.length(); i++) {
                if ((i == 2 || i == 4) && i < digitsOnly.length()) {
                    formattedText.append('/');
                }
                formattedText.append(digitsOnly.charAt(i));
            }
            if (formattedText.length() > 10) {
                formattedText.setLength(10);
            }
            if (!newValue.equals(formattedText.toString())) {
                textField.setText(formattedText.toString());
            }
        });

        textField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) { // when focus is lost
                validateTranxDate(textField);
            }
        });
    }

    private static void validateTranxDate(TextField textField) {
        String text = textField.getText();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        try {
            LocalDate enteredDate = LocalDate.parse(text, dateFormatter);
            LocalDate currentDate = LocalDate.now();

            if (enteredDate.isBefore(currentDate)) {
                AlertUtility.CustomCallback callback = number -> {
                    if (number == 1) {
//                        textField.requestFocus();
                    } else {
                        textField.requestFocus();
                    }
                };
                AlertUtility.AlertConfirmation(AlertUtility.alertTypeConfirmation, "Entered Date is Smaller than Current Date.", callback);
            } else {
                // clearError(); // Uncomment this line if you have a method to clear error messages
            }
        } catch (DateTimeParseException e) {
            // showError("Invalid date format."); // Uncomment this line if you have a method to show error messages
        }
    }
    public static void tranxDateGreaterThanCurrentDate1(TextField textField, ComboBox textField1) {
        Pattern pattern = Pattern.compile("\\d*");
        textField.setTextFormatter(new TextFormatter<>(change -> {
            String newText = change.getControlNewText().replaceAll("\\D", "");
            if (newText.length() > 8 || !pattern.matcher(newText).matches()) {
                return null;
            }
            return change;
        }));

        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            String digitsOnly = newValue.replaceAll("\\D", "");
            StringBuilder formattedText = new StringBuilder();

            for (int i = 0; i < digitsOnly.length(); i++) {
                if ((i == 2 || i == 4) && i < digitsOnly.length()) {
                    formattedText.append('/');
                }
                formattedText.append(digitsOnly.charAt(i));
            }
            if (formattedText.length() > 10) {
                formattedText.setLength(10);
            }
            if (!newValue.equals(formattedText.toString())) {
                textField.setText(formattedText.toString());
            }
        });

        textField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) { // when focus is lost
                validateTranxDate1(textField, textField1);
            }
        });
    }

    private static void validateTranxDate1(TextField textField, ComboBox textField1) {
        String text = textField.getText();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        try {
            LocalDate enteredDate = LocalDate.parse(text, dateFormatter);
            LocalDate currentDate = LocalDate.now();

            if (enteredDate.isBefore(currentDate)) {
                AlertUtility.CustomCallback callback = number -> {
                    if (number == 1) {
                        textField1.requestFocus();
                    } else {
                        textField.requestFocus();
                    }
                };
                AlertUtility.AlertConfirmation(AlertUtility.alertTypeConfirmation, "Entered Date is Smaller than Current Date.", callback);
            } else {
                // clearError(); // Uncomment this line if you have a method to clear error messages
            }
        } catch (DateTimeParseException e) {
            // showError("Invalid date format."); // Uncomment this line if you have a method to show error messages
        }
    }

    private static void showError(String message) {
        // Implementation for showing the error, e.g., using a Label or Alert dialog.
        System.err.println(message); // Placeholder for actual error display.
    }

    private static void clearError() {
        // Implementation for clearing the error message.
        System.out.println("No error."); // Placeholder for actual error clearing.
    }

    public static void restrictTextFieldToDigits(TextField textField, int maxTotalLength) {
        Pattern pattern = Pattern.compile("\\d*");

        textField.setTextFormatter(new TextFormatter<>(change -> {
            String newText = change.getControlNewText();

            // Check if the new text contains only digits
            String digitsOnly = newText.replaceAll("\\D", "");

            // Check if the total length exceeds the specified limit
            if (newText.length() > maxTotalLength || !pattern.matcher(digitsOnly).matches()) {
                return null;
            }

            return change;
        }));
    }


    public static void restrictTextField(TextField textField) {
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                textField.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
    }

    public static void panCardValidation(TextField textField) {
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            // Convert to uppercase and remove non-alphanumeric characters
            String cleanedText = newValue.toUpperCase().replaceAll("[^A-Z0-9]", "");

            // Limit to 10 characters
            if (cleanedText.length() > 10) {
                cleanedText = cleanedText.substring(0, 10);
            }

            // Check if the cleaned text matches the PAN pattern: 5 letters, 4 digits, 1 letter
            if (cleanedText.matches("^[A-Z]{0,5}$") ||
                    cleanedText.matches("^[A-Z]{5}[0-9]{0,4}$") ||
                    cleanedText.matches("^[A-Z]{5}[0-9]{4}[A-Z]?$")) {
                textField.setText(cleanedText);
            } else {
                // Revert to old value if the cleanedText doesn't match the format
                textField.setText(oldValue);
            }

            System.out.println(oldValue);
            cursorMomentForTextFieldWithParticularLenght(textField,10);

        });
    }

    public static void textToUpperCase(TextField textField) {
        textField.setTextFormatter(new TextFormatter<>(change -> {
            if (change.isContentChange()) {
                change.setText(change.getText().toUpperCase());
            }
            return change;
        }));


    }

    public static void checkFieldAndHighlight(TextField textField) {
        if (textField.getText().isBlank()) {
            highlightTextField(textField);
        } else {
            removeHighlight(textField);
        }
    }

    public static void highlightTextField(TextField textField) {
//        showAlert("Please Fill all the Fields ");

        // Change the border color or background color to indicate the field needs attention
        textField.setStyle("-fx-border-color: red;");
    }

    // Method to remove highlight from the field
    public static void removeHighlight(TextField textField) {
        // Reset the style to remove the highlight
        textField.setStyle("");
    }

    //COMBOBOX
    public static void checkComboFieldAndHighlight(ComboBox comboBox) {
        Object selectedObject = comboBox.getSelectionModel().getSelectedItem();
        if (selectedObject == null) {
            comboBox.setStyle("-fx-border-color: red;");
        } else {
            comboBox.setStyle("");  // Clears all styles, reverting to default
        }
    }

    //    to show all the contents from the combo box
    public static void comboBoxDataShow(ComboBox comboBox) {
        comboBox.setOnKeyPressed(event -> {
            System.out.println(event.getCode());
            if (event.getCode() == KeyCode.DOWN || event.getCode() == KeyCode.SPACE) {
                comboBox.show(); // This opens the ComboBox
            }
        });
    }


    // Method to find all labels in a parent node
    public static List<Label> findAllLabels(Parent root) {
        List<Label> labelList = new ArrayList<>();
        findLabelsRecursively(root, labelList);
        return labelList;
    }

    // Helper method to recursively find labels


    public static void highLightStarAsRedColour(VBox vbox) {
        List<Label> labels = findAllLabels(vbox);

        for (Label label : labels) {
            if (label.getText().contains("*")) {
                TextFlow styledTextFlow = styleAsteriskInText(label.getText());
                label.setGraphic(styledTextFlow);
                label.setText(""); // Clear the plain text to avoid duplication
            }
        }
    }

    //DATEPICKER
    public static void dateCheckVal(DatePicker date) {
        String selectedObject = String.valueOf(date.getValue());
        if (selectedObject == "null") {
//            String franchiseDistrict = (String) selectedObject;
            date.setStyle("-fx-border-color: red;");

        } else {
            date.setStyle("");  // Clears all styles, reverting to default
        }
    }

    //RADIOBUTTON
    public static void radioToggleValidation(String isGender, HBox valBox) {
        if (isGender == null) {
//            String franchiseDistrict = (String) selectedObject;
            valBox.setStyle("-fx-border-color: red;");

        } else {
            valBox.setStyle("");  // Clears all styles, reverting to default
        }
    }
    //Below Code for One Line Common Validation Purpose
//    public static void textCheckVal(TextField inputText){
//        textCheckVal(tfFranchiseCreateFranchiseCode);
//
//        System.out.println(inputText.getText());
//        Boolean selectedObject= inputText.getText().isBlank();
//        System.out.println("Selected value during else: " + selectedObject);
//
//        if (selectedObject) {
//            String franchiseDistrict = (String) selectedObject;
//            inputText.setStyle("-fx-border-color: red;");
//
//        } else {
//            inputText.setStyle("-fx-border-color: #CED4DA;");
//        }
//    }
    //******************************************Validation END******************************************************


    public static void restrictMobileNumber(TextField textField) {
        // Create a pattern to allow only digits
        Pattern pattern = Pattern.compile("\\d*");

        // Add listener to restrict to 10 digits and show alert
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!pattern.matcher(newValue).matches()) {
                textField.setText(oldValue);
            }
            if (newValue.length() > 10) {
                textField.setText(oldValue);
            }
        });

        // Add listener to display popup if less than 10 digits are entered
        textField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) { // If field loses focus
                String newText = textField.getText();
                if (newText.length() < 10 && newText.length() > 0) {
                    AlertUtility.AlertError(AlertUtility.alertTypeError, "Please enter a 10-digit number.", in -> {
                        textField.requestFocus();
                    });
                }
            }
        });
    }

    public static void restrictEmail(TextField textField) {
        // Create a pattern for email validation
        Pattern pattern = Pattern.compile("^\\S+@\\S+\\.\\S+$");

        // Add listener to display popup if invalid email format
        textField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) { // If field loses focus
                String newText = textField.getText();
                if (!pattern.matcher(newText).matches() && !newText.isEmpty()) {
                    AlertUtility.AlertError(AlertUtility.alertTypeError, "Please enter a valid email address.", in -> {
                        textField.requestFocus();
                    });
                }
            }
        });
    }

    /**
     * @implNote Method to Focus Next Element and also Focus on Previous Elements
     * @auther vinitc@opethic.com
     * @JavaFx migration
     * @Date 03/03/2024
     **/
    public static void setupFocusNavigation(Node[] nodes) {
        focusableNodes = nodes;
        currentIndex = 0;

        for (int i = 0; i < focusableNodes.length; i++) {
            int finalIndex = i;
            focusableNodes[i].setOnMouseClicked(event -> {
                currentIndex = finalIndex;
            });
            focusableNodes[i].setOnKeyPressed(CommonFunctionalUtils::handleKeyPressed);
        }
    }

    /**
     * @implNote Method to Focus Next Element and also Focus on Previous Elements
     * @auther vinitc@opethic.com
     * @JavaFx migration
     * @Date 03/03/2024
     **/
    private static void handleKeyPressed(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            if (focusableNodes[currentIndex] instanceof Button) {
                Button button = (Button) focusableNodes[currentIndex];
                button.fire();
            } else {
                focusNextNode();
            }
        } else if (event.getCode() == KeyCode.TAB) {
            if (event.isShiftDown()) {
                focusPreviousNode();
            } else {
                focusNextNode();
            }
            event.consume();
        }
    }

    /**
     * @implNote Method to Focus Next Element and also Focus on Previous Elements
     * @auther vinitc@opethic.com
     * @JavaFx migration
     * @Date 03/03/2024
     **/
    private static void focusNextNode() {
        int nextIndex = (currentIndex + 1) % focusableNodes.length;
        while (!isValidNode(nextIndex)) {
            nextIndex = (nextIndex + 1) % focusableNodes.length;
            if (nextIndex == currentIndex) {
                // Avoid infinite loop if all nodes are disabled
                return;
            }
        }
        currentIndex = nextIndex;
        focusableNodes[currentIndex].requestFocus();
    }

    /**
     * @implNote Method to Focus Next Element and also Focus on Previous Elements
     * @auther vinitc@opethic.com
     * @JavaFx migration
     * @Date 03/03/2024
     **/
    private static void focusPreviousNode() {
        int previousIndex = (currentIndex - 1 + focusableNodes.length) % focusableNodes.length;
        while (!isValidNode(previousIndex)) {
            previousIndex = (previousIndex - 1 + focusableNodes.length) % focusableNodes.length;
            if (previousIndex == currentIndex) {
                // Avoid infinite loop if all nodes are disabled
                return;
            }
        }
        currentIndex = previousIndex;
        focusableNodes[currentIndex].requestFocus();
    }

    /**
     * @implNote Method to Focus Next Element and also Focus on Previous Elements
     * @auther vinitc@opethic.com
     * @JavaFx migration
     * @Date 03/03/2024
     **/
    private static boolean isValidNode(int index) {
        return focusableNodes[index].isManaged() &&
                focusableNodes[index].isVisible() &&
                !focusableNodes[index].isDisabled();
    }

    /**
     * @implNote Method to Focus the Compulsory Fields in Form While Submitting the Form
     * @auther vinitc@opethic.com
     * @JavaFx migration
     * @Date 03/04/2024
     **/
    public static boolean validateForm(Node... nodes) {
        boolean isValid = true;
        Node firstEmptyField = null;
        for (Node node : nodes) {
            if (node instanceof TextField) {
                TextField textField = (TextField) node;
                if (textField.getText().isEmpty()) {
                    if (firstEmptyField == null) {
                        firstEmptyField = textField;
                    }
//                    highlightField(textField);
                    isValid = false;
                } else {
                    removeHighlight(textField);
                }
            } else if (node instanceof ComboBox) {
                ComboBox comboBox = (ComboBox) node;
                if (comboBox.getValue() == null) {
                    if (firstEmptyField == null) {
                        firstEmptyField = comboBox;
                    }
//                    highlightField(comboBox);
                    isValid = false;
                } else {
                    removeHighlight(comboBox);
                }
            } else if (node instanceof RadioButton) {
                RadioButton radioButton = (RadioButton) node;
                if (!radioButton.isSelected()) {
                    if (firstEmptyField == null) {
                        firstEmptyField = radioButton;
                    }
//                    highlightField(radioButton);
                    isValid = false;
                } else {
                    removeHighlight(radioButton);
                }
            }
        }
        if (!isValid) {
            firstEmptyField.requestFocus();
//            showFieldMissAlert("Error", "Please fill in all required fields!");
            AlertUtility.AlertError(AlertUtility.alertTypeError, "Please Fill in All Mandatory Fields!", in -> {
            });
        }
        return isValid;
    }

    /**
     * @implNote Method to Highlight with red border the Compulsory Fields in Form While Submitting the Form
     * @auther vinitc@opethic.com
     * @JavaFx migration
     * @Date 03/04/2024
     **/
    private static void highlightField(Node node) {
        node.setStyle("-fx-border-color: black;");
    }

    private static void removeHighlight(Node node) {
        node.setStyle("");
    }

    private static void showFieldMissAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void restrictHsnNumber(TextField textField) {
        // Create a pattern to allow only digits
        Pattern pattern = Pattern.compile("\\d*");

        // Add listener to restrict to 10 digits and show alert
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!pattern.matcher(newValue).matches()) {
                textField.setText(oldValue);
            }
            if (newValue.length() > 8) {
                textField.setText(oldValue);
            }
        });

        // Add listener to display popup if less than 10 digits are entered
        textField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) { // If field loses focus
                String newText = textField.getText();
                if (newText.length() > 8) {
                    AlertUtility.AlertError(AlertUtility.alertTypeError, "Only allow 8 numeric values.", in -> {
                        textField.requestFocus();
                    });
                }
            }
        });
    }


    public static void restrictDateFormat(TextField textField) {
        Pattern pattern = Pattern.compile("\\d*");
        textField.setTextFormatter(new TextFormatter<>(change -> {
            String newText = change.getControlNewText().replaceAll("\\D", "");
            if (newText.length() > 8 || !pattern.matcher(newText).matches()) {
                return null;
            }
            return change;
        }));

        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            String digitsOnly = newValue.replaceAll("\\D", "");
            StringBuilder formattedText = new StringBuilder();

            for (int i = 0; i < digitsOnly.length(); i++) {
                if ((i == 2 || i == 4) && i < digitsOnly.length()) {
                    formattedText.append('/');
                }
                formattedText.append(digitsOnly.charAt(i));
            }
            if (formattedText.length() > 10) {
                formattedText.setLength(10);
            }
            if (!newValue.equals(formattedText.toString())) {
                textField.setText(formattedText.toString());
            }
        });
    }
}
