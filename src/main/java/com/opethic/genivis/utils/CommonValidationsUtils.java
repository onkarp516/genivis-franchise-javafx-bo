package com.opethic.genivis.utils;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.util.ArrayList;
import java.util.List;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;

public class CommonValidationsUtils {
    private static Node[] focusableNodes;
    private static int currentIndex = 0;

    public static void restrictToNumbers(TextField textField) {
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                textField.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
    }

    public static void restrictToDecimalNumbers(TextField textField) {
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*(\\.\\d*)?")) {
                textField.setText(oldValue);
            }
        });
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
        comboBox.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
//                comboBox.show(); // This opens the ComboBox when focused
            }
        });
    }

    // Restrict input to numeric values only
    public static void onlyEnterNumbers(TextField textField) {
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("-?\\d*(\\.\\d*)?")) {
                textField.setText(newValue.replaceAll("[^-?\\d*(\\.\\d*)?]", ""));
            }
        });
    }



    public static void onlyEnterNumbersLimit(TextField textField, Integer num) {
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            // Allow only numbers and limit the length
            if (!newValue.matches("\\d*") || newValue.length() > num) {
                if (newValue.length() > num) {
                    textField.setText(oldValue);
                } else {
                    textField.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
    }
    public static void onlyEnterNumbersLimit1(TextField textField, Integer num) {      //function for accept float numbers(numbers and point)
        textField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                // Allow only numbers and one decimal point, and limit the length
                if (newValue.length() > num || !newValue.matches("\\d*(\\.\\d*)?")) {
                    if (newValue.length() > num) {
                        textField.setText(oldValue);
                    } else {
                        String filteredValue = newValue.replaceAll("[^\\d.]", "");
                        int decimalCount = filteredValue.length() - filteredValue.replace(".", "").length();
                        if (decimalCount > 1) {
                            textField.setText(oldValue);
                        } else {
                            textField.setText(filteredValue);
                        }
                    }
                }
            }
        });
    }

    public static void onlyEnterCharactersLimit(TextField textField, Integer num) {
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            // Allow only letters, '-' and '+', and limit the length
            if (!newValue.matches("[a-zA-Z\\-\\+]*") || newValue.length() > num) {
                if (newValue.length() > num) {
                    textField.setText(oldValue);
                } else {
                    textField.setText(newValue.replaceAll("[^a-zA-Z\\-\\+]", ""));
                }
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


    public static void changeStarColour(VBox vbox) {
        List<Label> labels = findAllLabels(vbox);

        for (Label label : labels) {
            if (label.getText().contains("*")) {
                TextFlow styledTextFlow = styleAsteriskInText(label.getText());
                label.setGraphic(styledTextFlow);
                label.setText(""); // Clear the plain text to avoid duplication
            }
        }
    }
    public static void changeStarColour2(HBox vbox) {
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
                    AlertUtility.AlertWarningTimeout3(AlertUtility.alertTypeError, "Enter 10 Digit Number", in -> {
                        textField.requestFocus();
                    });
                }
            }
        });
    }
    /**
     * @implNote Method to restrict email TextField
     * @auther vinitc@opethic.com
     * @JavaFx migration
     * @Date 02/03/2024
     **/
    public static void restrictEmail(TextField textField) {
        // Create a pattern for email validation
        Pattern pattern = Pattern.compile("^\\S+@\\S+\\.\\S+$");

        // Add listener to display popup if invalid email format
        textField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) { // If field loses focus
                String newText = textField.getText();
                if (!pattern.matcher(newText).matches() && !newText.isEmpty()) {
                    AlertUtility.AlertWarningTimeout3(AlertUtility.alertTypeError, "Enter Valid Email Address", in -> {
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
            focusableNodes[i].setOnKeyPressed(CommonValidationsUtils::handleKeyPressed);
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
            if (focusableNodes[currentIndex] instanceof javafx.scene.control.Button) {
                javafx.scene.control.Button button = (javafx.scene.control.Button) focusableNodes[currentIndex];
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
        String message = null;
        for (Node node : nodes) {
            if (node instanceof TextField) {
                TextField textField = (TextField) node;
                if (textField.getText().isEmpty()) {

                    if (firstEmptyField == null) {
                        firstEmptyField = textField;
                        message = "Enter "+textField.getPromptText();
                    }
                    isValid = false;
                } else {
                    removeHighlight(textField);
                }
            } else if (node instanceof ComboBox) {
                ComboBox comboBox = (ComboBox) node;
                if (comboBox.getValue() == null) {
                    if (firstEmptyField == null) {
                        firstEmptyField = comboBox;
                        message = comboBox.getPromptText();
                    }
                    isValid = false;
                } else {
                    removeHighlight(comboBox);
                }
            } else if (node instanceof RadioButton) {
                RadioButton radioButton = (RadioButton) node;
                if (!radioButton.isSelected()) {
                    System.out.println("prompt Text From Validations >> : "+ radioButton.getText());
                    if (firstEmptyField == null) {
                        firstEmptyField = radioButton;
                        message = radioButton.getText();
                    }
//                    highlightField(radioButton);
                    isValid = false;
                } else {
                    removeHighlight(radioButton);
                }
            }
//            else if (node instanceof ToggleButton) {
//                ToggleButton radioButton = (ToggleButton) node;
//                if (!radioButton.isSelected()) {
//                    System.out.println("prompt Text From Validations >> : "+ radioButton.getText());
//                    if (firstEmptyField == null) {
//                        firstEmptyField = radioButton;
//                        message = radioButton.getText();
//                    }
////                    highlightField(radioButton);
//                    isValid = false;
//                } else {
//                    removeHighlight(radioButton);
//                }
//            }
        }
        if (!isValid) {
            firstEmptyField.requestFocus();
            //            AlertUtility.AlertError(AlertUtility.alertTypeError, message, in -> {
//            });
            AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, message, in -> {
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

    public static void configureTextFieldToTitleCase(TextField textField) {
        // Set the TextFormatter to allow only letters and spaces
        textField.setTextFormatter(createLetterTextFormatter());

        // Add a listener to convert the input to title case
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isEmpty()) {
                textField.setText(toTitleCase(newValue));
            }
        });
    }

    private static TextFormatter<String> createLetterTextFormatter() {
        UnaryOperator<TextFormatter.Change> filter = change -> {
            String text = change.getText();
            if (text.matches("[a-zA-Z ]*")) {
                return change;
            }
            return null;
        };
        return new TextFormatter<>(filter);
    }

    private static String toTitleCase(String input) {
        StringBuilder titleCase = new StringBuilder();
        boolean nextTitleCase = true;

        for (char c : input.toCharArray()) {
            if (Character.isWhitespace(c)) {
                nextTitleCase = true;
                titleCase.append(c);
            } else if (nextTitleCase) {
                titleCase.append(Character.toTitleCase(c));
                nextTitleCase = false;
            } else {
                titleCase.append(Character.toLowerCase(c));
            }
        }

        return titleCase.toString();
    }

}
