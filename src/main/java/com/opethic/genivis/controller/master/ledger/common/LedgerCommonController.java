package com.opethic.genivis.controller.master.ledger.common;

import com.opethic.genivis.GenivisApplication;
import com.opethic.genivis.controller.commons.OverlaysEffect;
import com.opethic.genivis.controller.tranx_sales.common.TranxCommonPopUps;
import com.opethic.genivis.dto.master.ledger.OBListDTO;
import com.opethic.genivis.models.master.ledger.*;
import com.opethic.genivis.utils.*;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.*;
import javafx.util.StringConverter;

import java.awt.*;
import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.function.Consumer;
import java.util.regex.Pattern;

public class LedgerCommonController {
    public static <T> ComboBox<T> createComboBox(String promptText, EventHandler<ActionEvent> actionHandler) {
        ComboBox<T> comboBox = new ComboBox<>();

        double height = Screen.getPrimary().getBounds().getHeight();
        double width = Screen.getPrimary().getBounds().getWidth();
        System.out.println("width" + width);
        if (width >= 992 && width <= 1024) {
            comboBox.setStyle("-fx-font-size:11px");
            comboBox.setPrefWidth(100);
            comboBox.setMaxWidth(100);
            comboBox.setMinWidth(100);
            comboBox.setMaxHeight(24);
            comboBox.setMinHeight(24);
            comboBox.setPrefHeight(24);
        } else if (width >= 1025 && width <= 1280) {
            comboBox.setStyle("-fx-font-size:12px");
            comboBox.setPrefWidth(120);
            comboBox.setMaxWidth(120);
            comboBox.setMinWidth(120);
            comboBox.setMaxHeight(25);
            comboBox.setMinHeight(25);
            comboBox.setPrefHeight(25);
        } else if (width >= 1281 && width <= 1368) {
            comboBox.setStyle("-fx-font-size:12px");
            comboBox.setPrefWidth(150);
            comboBox.setMaxWidth(150);
            comboBox.setMinWidth(150);
            comboBox.setMaxHeight(28);
            comboBox.setMinHeight(28);
            comboBox.setPrefHeight(28);
        } else if (width >= 1369 && width <= 1400) {
            comboBox.setStyle("-fx-font-size:13px");
            comboBox.setPrefWidth(150);
            comboBox.setMaxWidth(150);
            comboBox.setMinWidth(150);
            comboBox.setMaxHeight(32);
            comboBox.setMinHeight(32);
            comboBox.setPrefHeight(32);
        } else if (width >= 1401 && width <= 1440) {
            comboBox.setStyle("-fx-font-size:13px");
            comboBox.setPrefWidth(160);
            comboBox.setMaxWidth(160);
            comboBox.setMinWidth(160);
            comboBox.setMaxHeight(32);
            comboBox.setMinHeight(32);
            comboBox.setPrefHeight(32);
        } else if (width >= 1441 && width <= 1680) {
            comboBox.setStyle("-fx-font-size:13px");
            comboBox.setPrefWidth(160);
            comboBox.setMaxWidth(160);
            comboBox.setMinWidth(160);
            comboBox.setMaxHeight(32);
            comboBox.setMinHeight(32);
            comboBox.setPrefHeight(32);
        } else if (width >= 1681 && width <= 1920) {
            comboBox.setStyle("-fx-font-size:14px");
            comboBox.setPrefWidth(180);
            comboBox.setMaxWidth(180);
            comboBox.setMinWidth(180);
            comboBox.setMaxHeight(34);
            comboBox.setMinHeight(34);
            comboBox.setPrefHeight(34);
        }

        comboBox.setPromptText(promptText);
        comboBox.setOnAction(actionHandler);

        return comboBox;
    }

    //TODO: Function to validate GSTIN
    private static boolean isValidGSTIN(String gstin) {
        String regex = "^\\d{2}[A-Z]{5}\\d{4}[A-Z]{1}\\d[Z]{1}[A-Z\\d]{1}$";
        return Pattern.matches(regex, gstin);
    }

    // Method to handle key pressed event for any Node
    private void setKeyPressedHandler(Node node) {
        node.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                Node nextNode = CommonTraversa.getNextFocusableNode(node.getScene());
                if (nextNode != null) {
                    nextNode.requestFocus();
                }
            }
        });
    }

    // phone no. validation
    private boolean isValidMobileNumber(String mobileNumber) {
        // Define your mobile number pattern here. This example assumes a 10-digit number.
        String regex = "\\d{10}";
        return mobileNumber.matches(regex);
    }

    // aadhaar no. validation
    private boolean isValidAadhaarNumber(String mobileNumber) {
        // Define your mobile number pattern here. This example assumes a 10-digit number.
        String regex = "\\d{12}";
        return mobileNumber.matches(regex);
    }

    //function for email validation
    private boolean validEmail(String email) {
        String emailReg = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return email.matches(emailReg);
    }

    public static <T> TextField createTextField(String promptText) {
        TextField tf = new TextField();
        tf.setPromptText(promptText);

        double height = Screen.getPrimary().getBounds().getHeight();
        double width = Screen.getPrimary().getBounds().getWidth();
        System.out.println("width" + width);
        if (width >= 992 && width <= 1024) {
            tf.setStyle("-fx-font-size:11px");
            tf.setPrefWidth(100);
            tf.setMaxWidth(100);
            tf.setMinWidth(100);
            tf.setMaxHeight(24);
            tf.setMinHeight(24);
            tf.setPrefHeight(24);
        } else if (width >= 1025 && width <= 1280) {
            tf.setStyle("-fx-font-size:12px");
            tf.setPrefWidth(120);
            tf.setMaxWidth(120);
            tf.setMinWidth(120);
            tf.setMaxHeight(25);
            tf.setMinHeight(25);
            tf.setPrefHeight(25);
        } else if (width >= 1281 && width <= 1368) {
            tf.setStyle("-fx-font-size:12px");
            tf.setPrefWidth(150);
            tf.setMaxWidth(150);
            tf.setMinWidth(150);
            tf.setMaxHeight(28);
            tf.setMinHeight(28);
            tf.setPrefHeight(28);
        } else if (width >= 1369 && width <= 1400) {
            tf.setStyle("-fx-font-size:13px");
            tf.setPrefWidth(150);
            tf.setMaxWidth(150);
            tf.setMinWidth(150);
            tf.setMaxHeight(32);
            tf.setMinHeight(32);
            tf.setPrefHeight(32);
        } else if (width >= 1401 && width <= 1440) {
            tf.setStyle("-fx-font-size:13px");
            tf.setPrefWidth(160);
            tf.setMaxWidth(160);
            tf.setMinWidth(160);
            tf.setMaxHeight(32);
            tf.setMinHeight(32);
            tf.setPrefHeight(32);
        } else if (width >= 1441 && width <= 1680) {
            tf.setStyle("-fx-font-size:13px");
            tf.setPrefWidth(160);
            tf.setMaxWidth(160);
            tf.setMinWidth(160);
            tf.setMaxHeight(32);
            tf.setMinHeight(32);
            tf.setPrefHeight(32);
        } else if (width >= 1681 && width <= 1920) {
            tf.setStyle("-fx-font-size:14px");
            tf.setPrefWidth(180);
            tf.setMaxWidth(180);
            tf.setMinWidth(180);
            tf.setMaxHeight(34);
            tf.setMinHeight(34);
            tf.setPrefHeight(34);
        }

        return tf;
    }

    public static <T> Label createLabel(String lblString) {
        Label lb = new Label();
        lb.setText(lblString);

        double height = Screen.getPrimary().getBounds().getHeight();
        double width = Screen.getPrimary().getBounds().getWidth();
        System.out.println("width" + width);
        if (width >= 992 && width <= 1024) {
            lb.setStyle("-fx-font-size: 11px;");
        } else if (width >= 1025 && width <= 1280) {
            lb.setStyle("-fx-font-size: 12px;");
        } else if (width >= 1281 && width <= 1368) {
            lb.setStyle("-fx-font-size: 12px;");
        } else if (width >= 1369 && width <= 1400) {
            lb.setStyle("-fx-font-size: 13px;");
        } else if (width >= 1401 && width <= 1440) {
            lb.setStyle("-fx-font-size: 13px;");
        } else if (width >= 1441 && width <= 1680) {
            lb.setStyle("-fx-font-size: 13px;");
        } else if (width >= 1681 && width <= 1920) {
            lb.setStyle("-fx-font-size: 14px;");
        }


        return lb;
    }

    public static <T> Label createLabelWithRequired() {
//        <Label minWidth="15.0" style="-fx-text-fill: red; " text=" * "/>
        Label lblReq = new Label();
        lblReq.setMinWidth(5.0);
        lblReq.setStyle("-fx-text-fill: red; ");
        lblReq.setText(" * ");
        return lblReq;
    }

    public static <T> DatePicker createDatePicker(String promptText) {
        DatePicker dp = new DatePicker();
        dp.setPromptText(promptText);
        return dp;
    }

    public <T> void openGstPopUp(Stage stage, String title, GstList inGstList, Consumer<GstList> callback) {
        OverlaysEffect.setOverlaysEffect(stage);
        Stage primaryStage = new Stage();
        primaryStage.initOwner(stage);
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        primaryStage.initModality(Modality.APPLICATION_MODAL);

        //Main Layout................................................................................................................................
        BorderPane borderPane = new BorderPane();
        borderPane.getStylesheets().add(GenivisApplication.class.getResource("/com/opethic/genivis/ui/css/popup_for_catalog.css").toExternalForm());
        borderPane.setStyle("-fx-background-radius: 5; -fx-background-color: white; -fx-border-color: #bfbfc0; -fx-border-radius: 5; -fx-border-width: 0.8;");

        Platform.runLater(() -> {
            borderPane.requestFocus();
        });
        double popup_width = TranxCommonPopUps.calculatePopUpWidth(60.0);
        double width = TranxCommonPopUps.getScreenWidth();
        System.out.println("width" + width);
        if (width >= 992 && width <= 1024) {
            popup_width = TranxCommonPopUps.calculatePopUpWidth(67.0);
        } else if (width >= 1025 && width <= 1280) {
            popup_width = TranxCommonPopUps.calculatePopUpWidth(67.0);
        } else if (width >= 1281 && width <= 1368) {
            popup_width = TranxCommonPopUps.calculatePopUpWidth(67.0);
        } else if (width >= 1369 && width <= 1400) {
            popup_width = TranxCommonPopUps.calculatePopUpWidth(67.0);
        } else if (width >= 1401 && width <= 1440) {
            popup_width = TranxCommonPopUps.calculatePopUpWidth(67.0);
        } else if (width >= 1441 && width <= 1680) {
            popup_width = TranxCommonPopUps.calculatePopUpWidth(56.0);
        } else if (width >= 1681 && width <= 1920) {
            popup_width = TranxCommonPopUps.calculatePopUpWidth(55.0);
        }
        //BorderPan under Top Layout....................................................................................................................
        HBox hbox_top = new HBox();
        hbox_top.setMinWidth(popup_width);
        hbox_top.setMaxWidth(popup_width);
        hbox_top.setPrefWidth(popup_width);
//        hbox_top.setMaxHeight(50);
//        hbox_top.setMinHeight(50);
//        hbox_top.setPrefHeight(50);

        Label popup_title = new Label(title);
        popup_title.setStyle("-fx-font-size: 16; -fx-text-fill: #404040; -fx-font-weight: bold;");
//        popup_title.setPadding(new Insets(0, 10, 0, 0));

        Image image = new Image(GenivisApplication.class.getResource("/com/opethic/genivis/ui/assets/close.png").toExternalForm());
        ImageView imageView = new ImageView(image);
        imageView.setStyle("-fx-cursor: hand;");
        imageView.setOnMouseClicked(event -> {
            primaryStage.close();
            OverlaysEffect.removeOverlaysEffect(stage);
        });
        HBox.setMargin(imageView, new Insets(0, 10, 0, 0));
        imageView.setFitWidth(30);
        imageView.setFitHeight(30);

        hbox_top.setAlignment(Pos.CENTER_LEFT);
        hbox_top.getChildren().add(popup_title);
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
//        HBox.setMargin(popup_title, new Insets(0, 0, 0, 10));
        hbox_top.getChildren().add(spacer);
        hbox_top.getChildren().add(imageView);
        hbox_top.setStyle("-fx-background-radius: 5 5 0 0; -fx-background-color: #d9f0fb; -fx-border-color: #ffffff; -fx-border-width: 0 0 3 0;");
        hbox_top.setPadding(new Insets(8));

        //BorderPane Under Center Layout.....................................................................................................
        HBox hbox_center = new HBox();
        hbox_center.setMinWidth(popup_width);
        hbox_center.setMaxWidth(popup_width);
        hbox_center.setPrefWidth(popup_width);
        hbox_center.setAlignment(Pos.CENTER_LEFT);
        hbox_center.setStyle("-fx-background-color: #e6f2f8;");

        Integer initialIndex = -1;
        ObservableList<GSTType> lstGstTypes = FXCollections.observableArrayList(Globals.getGstTypeList());
        HBox gstLabel = new HBox();
        Label lbGstType = createLabel("Type");
        Label lbGstTypeReq = createLabelWithRequired();
        gstLabel.getChildren().addAll(lbGstType, lbGstTypeReq);
        gstLabel.setAlignment(Pos.CENTER_LEFT);
        EventHandler<ActionEvent> handleGstType = event -> {
            ComboBox<T> comboBox = (ComboBox<T>) event.getSource();
            lstGstTypes.stream().filter(s -> s.getLabel().equalsIgnoreCase(String.valueOf(comboBox.getValue()))).findAny().ifPresent(ob -> {
                inGstList.setGstTypeid(ob.getValue());
                inGstList.setGstTypeName(ob.getLabel());
            });
        };
        ComboBox<GSTType> gstTypeComboBox = createComboBox("Type", handleGstType);
        gstTypeComboBox.setItems(lstGstTypes);
        AutoCompleteBox autoCompleteBox = new AutoCompleteBox(gstTypeComboBox, initialIndex);
        if (inGstList.getGstTypeid() > 0) {
            lstGstTypes.stream().filter(s -> s.getLabel().equalsIgnoreCase(String.valueOf(inGstList.getGstTypeName()))).findAny().ifPresent(ob -> {
                gstTypeComboBox.setValue(ob);
            });
        } else {
            gstTypeComboBox.getSelectionModel().selectFirst();
        }
        gstTypeComboBox.getStyleClass().add("isRequired");

        HBox typeCombine = new HBox();
        typeCombine.getChildren().addAll(gstLabel, gstTypeComboBox);


        HBox regDateLabel = new HBox();
        Label lbdp = createLabel("Reg Date");
        Label lbdpReq = createLabelWithRequired();
        regDateLabel.getChildren().addAll(lbdp, lbdpReq);
        regDateLabel.setAlignment(Pos.CENTER_LEFT);
//        DatePicker dpReg = createDatePicker("Reg Date");
        TextField dpReg = createTextField("DD/MM/YYYY");
        DateValidator.applyDateFormat(dpReg);
        CommonFunctionalUtils.restrictDateFormat(dpReg);

        if (inGstList.getGstRegDate() != null) {
//            dpReg.setText(inGstList.getGstRegDate());
            dpReg.setText(inGstList.getGstRegDate());
        }
        dpReg.getStyleClass().add("isRequired");

        HBox regDateCombine = new HBox();
        regDateCombine.getChildren().addAll(regDateLabel, dpReg);


//        dpReg.focusedProperty().addListener((obs, old, nw) -> {
//            System.out.println("nw" + nw);
//            if (!nw) {
//                if (dpReg.getText().isEmpty()) {
//                    dpReg.requestFocus();
//                }
//                String regDateStr = dpReg.getText();
//                if (!regDateStr.isEmpty()) {
//                    LocalDate currentDate = LocalDate.now();
//                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
////                String formattedCurrentDate = currentDate.format(formatter);
//                    LocalDate regDate = LocalDate.parse(regDateStr, formatter);
////                System.out.println("register date --> " + regDateStr);
////                System.out.println("currentDate --> " + formattedCurrentDate);
//                    if (regDate.isAfter(currentDate)) {
//                        AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, "Register date cannot be greater than the current date. ", input -> {
//                            dpReg.requestFocus();
//                        });
//                    }
//                }
//            }
//        });


        HBox gstInLabel = new HBox();
        Label lbGSTIN = createLabel("GST IN");
        Label lbtfGstInReq = createLabelWithRequired();
        gstInLabel.getChildren().addAll(lbGSTIN, lbtfGstInReq);
        gstInLabel.setAlignment(Pos.CENTER_LEFT);
        TextField tfGstIn = createTextField("GST IN");
        if (!inGstList.getGstNo().isEmpty()) {
            tfGstIn.setText(inGstList.getGstNo());
        }
        //?Gst no on enter it will make upper case and maxlength is less or equal to 15
        tfGstIn.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.length() <= 15) {
                tfGstIn.setText(newValue.toUpperCase());
            } else {
                tfGstIn.setText(oldValue);
            }
        });

        HBox gstInCombine = new HBox();
        gstInCombine.getChildren().addAll(gstInLabel, tfGstIn);

        HBox panLabel = new HBox();
        Label lbPAN = createLabel("PAN");
        Label lbtfPANReq = createLabelWithRequired();
        panLabel.getChildren().addAll(lbPAN, lbtfPANReq);
        panLabel.setAlignment(Pos.CENTER_LEFT);
        TextField tfPAN = createTextField("PAN");
        tfPAN.getStyleClass().add("isRequired");
        if (!inGstList.getPanNo().isEmpty()) tfPAN.setText(inGstList.getPanNo());
        tfPAN.setTextFormatter(new TextFormatter<>(change -> {
            String newText = change.getControlNewText();
            if (newText.matches("[A-Z]{0,5}[0-9]{0,4}[A-Z]{0,1}")) {
                return change;
            }
            return null;
        }));

        tfGstIn.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) {
                if (tfGstIn.getText().isEmpty()) {
//                    tfGstIn.requestFocus();
                } else {
                    String gstin = tfGstIn.getText();
                    if (gstin.length() > 15) {
                        gstin = gstin.substring(0, 15);
                    }
                    tfGstIn.setText(gstin);
                    if (!isValidGSTIN(gstin)) {
//                        AlertUtility.AlertError("WARNING", "Entered GST Is Not Valid", input -> {
//                        });
//                        AlertUtility.AlertTimer("WARNING", "ENTERED GST IS NOT VALID", 2);
//                        tfGstIn.requestFocus();
                        AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, "ENTERED GST IS NOT VALID", newEvent -> {
                            tfGstIn.requestFocus();
                        });
                    } else {
                        String panNo = gstin.substring(2, 12);
                        tfPAN.setText(panNo);
                    }
                }
            }
        });

        dpReg.focusedProperty().addListener((obs, old, nw) -> {
            System.out.println("nw" + nw);
            if (!nw) {
                String regDateStr = dpReg.getText();
                if (!regDateStr.isEmpty()) {
                    LocalDate currentDate = LocalDate.now();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
//                String formattedCurrentDate = currentDate.format(formatter);
                    LocalDate regDate = LocalDate.parse(regDateStr, formatter);
                    if (regDate.isAfter(currentDate)) {
                        AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, "Register date cannot be greater than current date", input -> {
//                                dpReg.requestFocus();
                            GlobalTranx.requestFocusOrDieTrying(dpReg, 3);
                        });
                    } else {
                        GlobalTranx.requestFocusOrDieTrying(tfGstIn, 3);
                    }
                }
            }
        });

        HBox panCombine = new HBox();
        panCombine.getChildren().addAll(panLabel, tfPAN);

//        Insets Margin = new Insets(0, 10, 0, 1);
//        HBox.setMargin(tfGstIn, Margin);
//        HBox.setMargin(tfPAN, Margin);
//        HBox.setMargin(gstTypeComboBox, Margin);
//        HBox.setMargin(dpReg, Margin);

        typeCombine.setSpacing(10);
        regDateCombine.setSpacing(10);
        gstInCombine.setSpacing(10);
        panCombine.setSpacing(10);
        typeCombine.setAlignment(Pos.CENTER_LEFT);
        regDateCombine.setAlignment(Pos.CENTER_LEFT);
        gstInCombine.setAlignment(Pos.CENTER_LEFT);
        panCombine.setAlignment(Pos.CENTER_LEFT);

        hbox_center.getChildren().addAll(typeCombine, regDateCombine, gstInCombine, panCombine);
        hbox_center.setPadding(new Insets(8));
        hbox_center.setSpacing(10);

        HBox hbox_bottom = new HBox();
        hbox_bottom.setMinWidth(popup_width);
        hbox_bottom.setMaxWidth(popup_width);
        hbox_bottom.setPrefWidth(popup_width);
//        hbox_bottom.setMaxHeight(55);
//        hbox_bottom.setMinHeight(55);
//        hbox_bottom.setPrefHeight(55);
        hbox_bottom.setSpacing(10);
        hbox_bottom.setPadding(new Insets(8));
        hbox_bottom.setStyle("-fx-background-radius: 0 0 5 5; -fx-background-color: white;");
        hbox_bottom.setAlignment(Pos.CENTER_RIGHT);

        Button clButton = new Button("Clear");
//        HBox.setMargin(clButton, new Insets(0, 10, 0, 0));
        Button suButton = new Button("Submit");
        clButton.setId("cancel-btn");
        suButton.setId("submit-btn");


        hbox_bottom.getChildren().addAll(suButton, clButton);

        borderPane.setTop(hbox_top);
        borderPane.setCenter(hbox_center);
        borderPane.setBottom(hbox_bottom);

//        Node[] nodes = new Node[]{gstTypeComboBox, dpReg, tfGstIn, tfPAN, suButton,clButton};
//        CommonValidationsUtils.setupFocusNavigation(nodes);

        Scene scene = new Scene(borderPane, popup_width, 170);

        primaryStage.setScene(scene);
        primaryStage.setTitle(title);
        primaryStage.setResizable(false);

        scene.setFill(Color.TRANSPARENT);

        primaryStage.centerOnScreen();
        primaryStage.setOnShown((e) -> {
            Platform.runLater(gstTypeComboBox::requestFocus);
        });

        suButton.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.LEFT) {
                Platform.runLater(() -> {
                    clButton.requestFocus();
                });
            }
            event.consume();
        });
        clButton.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.RIGHT) {
                Platform.runLater(() -> {
                    suButton.requestFocus();
                });
            }
            event.consume();
        });

        scene.setOnKeyReleased((e) -> {
            if (e.getCode() == KeyCode.ESCAPE) {
                OverlaysEffect.removeOverlaysEffect(stage);
                primaryStage.close();
            }
        });
        scene.setOnKeyPressed((e) -> {
            if (e.getCode() == KeyCode.ENTER) {
                if (gstTypeComboBox.isFocused()) {
                    Platform.runLater(dpReg::requestFocus);
                } else if (dpReg.isFocused() && !dpReg.getText().isEmpty()) {
                    Platform.runLater(tfGstIn::requestFocus);
                } else if (tfGstIn.isFocused() && !tfGstIn.getText().isEmpty()) {
                    Platform.runLater(tfPAN::requestFocus);
                } else if (tfPAN.isFocused() && !tfPAN.getText().isEmpty()) {
                    Platform.runLater(suButton::requestFocus);
                }
            }
            e.consume();
        });
//        borderPane.addEventFilter(KeyEvent.KEY_PRESSED, (KeyEvent event) -> {
//            KeyEvent newEvent = new KeyEvent(
//                    null,
//                    null,
//                    KeyEvent.KEY_PRESSED,
//                    "",
//                    "\t",
//                    KeyCode.TAB,
//                    event.isShiftDown(),
//                    event.isControlDown(),
//                    event.isAltDown(),
//                    event.isMetaDown()
//            );
//            Event.fireEvent(event.getTarget(), newEvent);
//            event.consume();
//        });
        primaryStage.show();

        suButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if (!tfGstIn.getText().isEmpty() && !tfPAN.getText().isEmpty() && !dpReg.getText().isEmpty()) {
                    System.out.println("tfGstIn.getText()--------> " + tfGstIn.getText());
                    inGstList.setGstNo(tfGstIn.getText());
                    inGstList.setPanNo(tfPAN.getText());
                    inGstList.setGstRegDate(dpReg.getText());
                    System.out.println("inGstList --------> " + inGstList);
                    callback.accept(inGstList);
                    primaryStage.close();
                    OverlaysEffect.removeOverlaysEffect(stage);
                } else {
                    String msg = "Please Fill Data In All Fields !";
                    AlertUtility.CustomCallback callback = number -> {
                        gstTypeComboBox.requestFocus();
                    };
                    AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, msg, callback);
                }
            }
        });

        clButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
//                primaryStage.close();
//                OverlaysEffect.removeOverlaysEffect(stage);
                gstTypeComboBox.setValue(null);
                tfGstIn.setText("");
                dpReg.setText("");
                tfPAN.setText("");
                Platform.runLater(gstTypeComboBox::requestFocus);
            }
        });

    }


    public <T> void openBankPopUp(Stage stage, String title, BankDetailList inBankDetails, Consumer<BankDetailList> callback) {
        OverlaysEffect.setOverlaysEffect(stage);
        Stage primaryStage = new Stage();

        primaryStage.initOwner(stage);
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        primaryStage.initModality(Modality.APPLICATION_MODAL);

        //Main Layout................................................................................................................................
        BorderPane borderPane = new BorderPane();
        borderPane.getStylesheets().add(GenivisApplication.class.getResource("/com/opethic/genivis/ui/css/popup_for_catalog.css").toExternalForm());
        borderPane.setStyle("-fx-background-radius: 5; -fx-background-color: white; -fx-border-color: #bfbfc0; -fx-border-radius: 5; -fx-border-width: 0.8;");

        Platform.runLater(() -> {
            borderPane.requestFocus();
        });
        double popup_width = TranxCommonPopUps.calculatePopUpWidth(60.0);
        double width = TranxCommonPopUps.getScreenWidth();
        System.out.println("width" + width);
        if (width >= 992 && width <= 1024) {
            popup_width = TranxCommonPopUps.calculatePopUpWidth(67.0);
        } else if (width >= 1025 && width <= 1280) {
            popup_width = TranxCommonPopUps.calculatePopUpWidth(67.0);
        } else if (width >= 1281 && width <= 1368) {
            popup_width = TranxCommonPopUps.calculatePopUpWidth(67.0);
        } else if (width >= 1369 && width <= 1400) {
            popup_width = TranxCommonPopUps.calculatePopUpWidth(67.0);
        } else if (width >= 1401 && width <= 1440) {
            popup_width = TranxCommonPopUps.calculatePopUpWidth(67.0);
        } else if (width >= 1441 && width <= 1680) {
            popup_width = TranxCommonPopUps.calculatePopUpWidth(56.0);
        } else if (width >= 1681 && width <= 1920) {
            popup_width = TranxCommonPopUps.calculatePopUpWidth(55.0);
        }
        //BorderPan under Top Layout....................................................................................................................
        HBox hbox_top = new HBox();
        hbox_top.setMinWidth(popup_width);
        hbox_top.setMaxWidth(popup_width);
        hbox_top.setPrefWidth(popup_width);
//        hbox_top.setMaxHeight(50);
//        hbox_top.setMinHeight(50);
//        hbox_top.setPrefHeight(50);

        Label popup_title = new Label(title);
        popup_title.setStyle("-fx-font-size: 16; -fx-text-fill: #404040; -fx-font-weight: bold;");
//        popup_title.setPadding(new Insets(0, 10, 0, 0));

        Image image = new Image(GenivisApplication.class.getResource("/com/opethic/genivis/ui/assets/close.png").toExternalForm());
        ImageView imageView = new ImageView(image);
        imageView.setStyle("-fx-cursor: hand;");
        imageView.setOnMouseClicked(event -> {
            primaryStage.close();
            OverlaysEffect.removeOverlaysEffect(stage);
        });
//        HBox.setMargin(imageView, new Insets(0, 10, 0, 0));
        imageView.setFitWidth(30);
        imageView.setFitHeight(30);

        hbox_top.setAlignment(Pos.CENTER_LEFT);
        hbox_top.getChildren().add(popup_title);
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
//        HBox.setMargin(popup_title, new Insets(0, 0, 0, 10));
        hbox_top.getChildren().add(spacer);
        hbox_top.getChildren().add(imageView);
        hbox_top.setStyle("-fx-background-radius: 5 5 0 0; -fx-background-color: #d9f0fb; -fx-border-color: #ffffff; -fx-border-width: 0 0 3 0;");
        hbox_top.setPadding(new Insets(8));

        //BorderPane Under Center Layout.....................................................................................................
        HBox hbox_center = new HBox();
        hbox_center.setMinWidth(popup_width);
        hbox_center.setMaxWidth(popup_width);
        hbox_center.setPrefWidth(popup_width);
        hbox_center.setAlignment(Pos.CENTER_LEFT);
        hbox_center.setStyle("-fx-background-color: #e6f2f8;");


        Integer initialIndex = -1;
        Label lbBank = createLabel("Bank");
        Label lbBankReq = createLabelWithRequired();
        HBox bankLabel = new HBox();
        bankLabel.getChildren().addAll(lbBank, lbBankReq);
        bankLabel.setAlignment(Pos.CENTER_LEFT);
        TextField tfBank = createTextField("Bank");
        if (!inBankDetails.getBankName().isEmpty()) tfBank.setText(inBankDetails.getBankName());
        HBox bankCombine = new HBox();
        bankCombine.getChildren().addAll(bankLabel, tfBank);
        tfBank.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) {
                if (tfBank.getText().isEmpty()) {
                    tfBank.requestFocus();
                }
            }
        });

        Label lbAccNo = createLabel("A/C Number");
        Label lbAccReq = createLabelWithRequired();
        HBox accNoLabel = new HBox();
        accNoLabel.getChildren().addAll(lbAccNo, lbAccReq);
        accNoLabel.setAlignment(Pos.CENTER_LEFT);
        TextField tfAccNo = createTextField("Acc No");
        if (!inBankDetails.getBankAccNo().isEmpty()) tfAccNo.setText(inBankDetails.getBankAccNo());
        HBox accNumCombine = new HBox();
        accNumCombine.getChildren().addAll(accNoLabel, tfAccNo);
        tfAccNo.setTextFormatter(new TextFormatter<>(change -> {
            String text = change.getText();
            if (text.matches("\\d*")) { // Allow only numeric characters
                return change;
            }
            return null;
        }));
        tfAccNo.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) {
                if (tfAccNo.getText().isEmpty()) {
                    tfAccNo.requestFocus();
                }
            }
        });

        Label lbifsc = createLabel("IFSC");
        Label lbifscReq = createLabelWithRequired();
        HBox ifscLabel = new HBox();
        ifscLabel.getChildren().addAll(lbifsc, lbifscReq);
        ifscLabel.setAlignment(Pos.CENTER_LEFT);
        TextField tfIfsc = createTextField("IFSC");
        if (!inBankDetails.getBankIFSCCode().isEmpty()) tfIfsc.setText(inBankDetails.getBankIFSCCode());
        HBox ifscCombine = new HBox();
        ifscCombine.getChildren().addAll(ifscLabel, tfIfsc);
        tfIfsc.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) {
                if (tfIfsc.getText().isEmpty()) {
                    tfIfsc.requestFocus();
                }
            }
        });

        Label lbbranch = createLabel("Branch");
        Label lbBranchReq = createLabelWithRequired();
        HBox branchLabel = new HBox();
        branchLabel.getChildren().addAll(lbbranch, lbBranchReq);
        branchLabel.setAlignment(Pos.CENTER_LEFT);
        TextField tfBranch = createTextField("Branch");
        if (!inBankDetails.getBankBranch().isEmpty()) tfBranch.setText(inBankDetails.getBankBranch());
        tfBranch.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) {
                if (tfBranch.getText().isEmpty()) {
                    tfBranch.requestFocus();
                }
            }
        });

        tfIfsc.textProperty().addListener((observable, oldValue, newValue) -> {
            tfIfsc.setText(newValue.toUpperCase());
        });
        HBox branchCombine = new HBox();
        branchCombine.getChildren().addAll(branchLabel, tfBranch);

//        Insets Margin = new Insets(0, 10, 0, 1);
//        HBox.setMargin(tfBank, Margin);
//        HBox.setMargin(tfAccNo, Margin);
//        HBox.setMargin(tfIfsc, Margin);
//        HBox.setMargin(tfBranch, Margin);
        bankCombine.setSpacing(10);
        accNumCombine.setSpacing(10);
        ifscCombine.setSpacing(10);
        branchCombine.setSpacing(10);
        bankCombine.setAlignment(Pos.CENTER_LEFT);
        accNumCombine.setAlignment(Pos.CENTER_LEFT);
        ifscCombine.setAlignment(Pos.CENTER_LEFT);
        branchCombine.setAlignment(Pos.CENTER_LEFT);

        hbox_center.getChildren().addAll(bankCombine, accNumCombine, ifscCombine, branchCombine);
        hbox_center.setSpacing(10);
        hbox_center.setPadding(new Insets(8));

        HBox hbox_bottom = new HBox();
        hbox_bottom.setMinWidth(popup_width);
        hbox_bottom.setMaxWidth(popup_width);
        hbox_bottom.setPrefWidth(popup_width);
//        hbox_bottom.setMaxHeight(55);
//        hbox_bottom.setMinHeight(55);
//        hbox_bottom.setPrefHeight(55);
        hbox_bottom.setSpacing(10);
        hbox_bottom.setStyle("-fx-background-radius: 0 0 5 5; -fx-background-color: white;");
        hbox_bottom.setAlignment(Pos.CENTER_RIGHT);

        Button clButton = new Button("Clear");
//        HBox.setMargin(clButton, new Insets(0, 10, 0, 0));
        Button suButton = new Button("Submit");
        clButton.setId("cancel-btn");
        suButton.setId("submit-btn");

        hbox_bottom.getChildren().addAll(suButton, clButton);
        hbox_bottom.setPadding(new Insets(8));

        borderPane.setTop(hbox_top);
        borderPane.setCenter(hbox_center);
        borderPane.setBottom(hbox_bottom);

//        Node[] nodes = new Node[]{tfBank, tfAccNo, tfIfsc, tfBranch, suButton,clButton};
//        CommonValidationsUtils.setupFocusNavigation(nodes);

        Scene scene = new Scene(borderPane, popup_width, 170);

        primaryStage.setScene(scene);
        primaryStage.setTitle(title);
        primaryStage.setResizable(false);

        scene.setFill(Color.TRANSPARENT);

        primaryStage.centerOnScreen();
        primaryStage.setOnShown((e) -> {
            Platform.runLater(tfBank::requestFocus);
        });

        scene.setOnKeyPressed((e) -> {
            if (e.getCode() == KeyCode.ENTER) {
                if (tfBank.isFocused()) {
                    Platform.runLater(tfAccNo::requestFocus);
                } else if (tfAccNo.isFocused()) {
                    Platform.runLater(tfIfsc::requestFocus);
                } else if (tfIfsc.isFocused()) {
                    Platform.runLater(tfBranch::requestFocus);
                } else if (tfBranch.isFocused()) {
                    Platform.runLater(suButton::requestFocus);
                }
            }
            e.consume();
        });
        suButton.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.LEFT) {
                Platform.runLater(() -> {
                    clButton.requestFocus();
                });
            }
            event.consume();
        });
        clButton.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.RIGHT) {
                Platform.runLater(() -> {
                    suButton.requestFocus();
                });
            }
            event.consume();
        });

        scene.setOnKeyReleased((e) -> {
            if (e.getCode() == KeyCode.ESCAPE) {
                OverlaysEffect.removeOverlaysEffect(stage);
                primaryStage.close();
            }
            e.consume();
        });
        primaryStage.show();

        suButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if (!tfBank.getText().isEmpty() && !tfAccNo.getText().isEmpty() && !tfIfsc.getText().isEmpty() && !tfBranch.getText().isEmpty()) {
                    inBankDetails.setBankAccNo(tfAccNo.getText());
                    inBankDetails.setBankName(tfBank.getText());
                    inBankDetails.setBankIFSCCode(tfIfsc.getText());
                    inBankDetails.setBankBranch(tfBranch.getText());
                    callback.accept(inBankDetails);
                    primaryStage.close();
                    OverlaysEffect.removeOverlaysEffect(stage);
                } else {
                    String msg = "Please Fill Data In All Fields !";
                    AlertUtility.CustomCallback callback = number -> {
                        tfBank.requestFocus();
                    };
                    AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, msg, callback);
                }
            }
        });

        clButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
//                primaryStage.close();
//                OverlaysEffect.removeOverlaysEffect(stage);
                tfBank.setText("");
                tfAccNo.setText("");
                tfIfsc.setText("");
                tfBranch.setText("");
                Platform.runLater(tfBank::requestFocus);
            }
        });

    }

    public <T> void openDepartmentPopUp(Stage stage, String title, DeptInfo inDeptInfo, Consumer<DeptInfo> callback) {
        OverlaysEffect.setOverlaysEffect(stage);
        Stage primaryStage = new Stage();

        primaryStage.initOwner(stage);
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        primaryStage.initModality(Modality.APPLICATION_MODAL);

        //Main Layout................................................................................................................................
        BorderPane borderPane = new BorderPane();
        borderPane.getStylesheets().add(GenivisApplication.class.getResource("/com/opethic/genivis/ui/css/popup_for_catalog.css").toExternalForm());
        borderPane.setStyle("-fx-background-radius: 5; -fx-background-color: white; -fx-border-color: #bfbfc0; -fx-border-radius: 5; -fx-border-width: 0.8;");

        Platform.runLater(() -> {
            borderPane.requestFocus();
        });
        double popup_width = TranxCommonPopUps.calculatePopUpWidth(60.0);
        double width = TranxCommonPopUps.getScreenWidth();
        System.out.println("width" + width);
        if (width >= 992 && width <= 1024) {
            popup_width = TranxCommonPopUps.calculatePopUpWidth(67.0);
        } else if (width >= 1025 && width <= 1280) {
            popup_width = TranxCommonPopUps.calculatePopUpWidth(67.0);
        } else if (width >= 1281 && width <= 1368) {
            popup_width = TranxCommonPopUps.calculatePopUpWidth(67.0);
        } else if (width >= 1369 && width <= 1400) {
            popup_width = TranxCommonPopUps.calculatePopUpWidth(67.0);
        } else if (width >= 1401 && width <= 1440) {
            popup_width = TranxCommonPopUps.calculatePopUpWidth(67.0);
        } else if (width >= 1441 && width <= 1680) {
            popup_width = TranxCommonPopUps.calculatePopUpWidth(56.0);
        } else if (width >= 1681 && width <= 1920) {
            popup_width = TranxCommonPopUps.calculatePopUpWidth(55.0);
        }

        //BorderPan under Top Layout....................................................................................................................
        HBox hbox_top = new HBox();
        hbox_top.setMinWidth(popup_width);
        hbox_top.setMaxWidth(popup_width);
        hbox_top.setPrefWidth(popup_width);
//        hbox_top.setMaxHeight(50);
//        hbox_top.setMinHeight(50);
//        hbox_top.setPrefHeight(50);

        Label popup_title = new Label(title);
        popup_title.setStyle("-fx-font-size: 16; -fx-text-fill: #404040; -fx-font-weight: bold;");
//        popup_title.setPadding(new Insets(0, 10, 0, 0));

        Image image = new Image(GenivisApplication.class.getResource("/com/opethic/genivis/ui/assets/close.png").toExternalForm());
        ImageView imageView = new ImageView(image);
        imageView.setStyle("-fx-cursor: hand;");
        imageView.setOnMouseClicked(event -> {
            primaryStage.close();
            OverlaysEffect.removeOverlaysEffect(stage);
        });
//        HBox.setMargin(imageView, new Insets(0, 10, 0, 0));
        imageView.setFitWidth(30);
        imageView.setFitHeight(30);

        hbox_top.setAlignment(Pos.CENTER_LEFT);
        hbox_top.getChildren().add(popup_title);
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
//        HBox.setMargin(popup_title, new Insets(0, 0, 0, 10));
        hbox_top.getChildren().add(spacer);
        hbox_top.getChildren().add(imageView);
        hbox_top.setStyle("-fx-background-radius: 5 5 0 0; -fx-background-color: #d9f0fb; -fx-border-color: #ffffff; -fx-border-width: 0 0 3 0;");
        hbox_top.setPadding(new Insets(8));

        //BorderPane Under Center Layout.....................................................................................................
        HBox hbox_center = new HBox();
        hbox_center.setMinWidth(popup_width);
        hbox_center.setMaxWidth(popup_width);
        hbox_center.setPrefWidth(popup_width);
        hbox_center.setAlignment(Pos.CENTER_LEFT);
        hbox_center.setStyle("-fx-background-color: #e6f2f8;");

        Label lbDept = createLabel("Dept Name");
        Label lbDeptReq = createLabelWithRequired();
        HBox deptnNameLabel = new HBox();
        deptnNameLabel.getChildren().addAll(lbDept, lbDeptReq);
        deptnNameLabel.setAlignment(Pos.CENTER_LEFT);
        TextField tfDept = createTextField("Dept Name");
        if (!inDeptInfo.getPersonDeptName().isEmpty()) tfDept.setText(inDeptInfo.getPersonDeptName());
        HBox deptNameCombine = new HBox();
        deptNameCombine.getChildren().addAll(deptnNameLabel, tfDept);
        tfDept.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) {
                if (tfDept.getText().isEmpty()) {
                    tfDept.requestFocus();
                }
            }
        });

        Label lbNameNo = createLabel("Name");
        Label lbNameReq = createLabelWithRequired();
        HBox nameLabel = new HBox();
        nameLabel.getChildren().addAll(lbNameNo, lbNameReq);
        nameLabel.setAlignment(Pos.CENTER_LEFT);
        TextField tfName = createTextField("Name");
        if (!inDeptInfo.getPersonName().isEmpty()) tfName.setText(inDeptInfo.getPersonName());
        HBox nameCombine = new HBox();
        nameCombine.getChildren().addAll(nameLabel, tfName);
        tfName.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) {
                if (tfName.getText().isEmpty()) {
                    tfName.requestFocus();
                }
            }
        });

        Label lbEmail = createLabel("Email");
//        Label lbEmailReq = createLabelWithRequired();
        TextField tfEmail = createTextField("Email");
        if (!inDeptInfo.getPersonEmail().isEmpty()) tfEmail.setText(inDeptInfo.getPersonEmail());
        HBox emailCombine = new HBox();
        emailCombine.getChildren().addAll(lbEmail, tfEmail);

        Label lbPhone = createLabel("Phone");
//        Label lbPhoneReq = createLabelWithRequired();
        TextField tfPhone = createTextField("Phone");
        if (!inDeptInfo.getPersonPhone().isEmpty()) tfPhone.setText(inDeptInfo.getPersonPhone());
        HBox phoneCombine = new HBox();
        phoneCombine.getChildren().addAll(lbPhone, tfPhone);
        tfPhone.setTextFormatter(new TextFormatter<>(change -> {
            String text = change.getText();
            if (text.matches("\\d*") && change.getControlNewText().length() <= 10) { // Allow only numeric characters
                return change;
            }
            return null;
        }));
        // phone no. validation
        tfPhone.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.TAB && event.isShiftDown()) {
                event.consume();
                if (!isValidMobileNumber(tfPhone.getText()) && !tfPhone.getText().equalsIgnoreCase("")) {
                    // Clear the text if the entered Phone number is not valid
                    AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, "Please Enter Valid Phone Number", newEvent -> {
                        tfPhone.requestFocus();
                    });
                    event.consume();
                }
            } else if (event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.TAB) {
                if (!isValidMobileNumber(tfPhone.getText()) && !tfPhone.getText().equalsIgnoreCase("")) {
                    // Clear the text if the entered Phone number is not valid
                    AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, "Please Enter Valid Phone Number", newEvent -> {
                        tfPhone.requestFocus();
                    });
                    event.consume();
                }
            }
        });
        //code for email validation
        tfEmail.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.TAB && event.isShiftDown()) {
                event.consume();
                if (!validEmail(tfEmail.getText()) && !tfEmail.getText().equalsIgnoreCase("")) {
                    // Clear the text if the entered email is not valid
                    AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, "Please Enter Valid Email Id", newEvent -> {
                        tfEmail.requestFocus();
                    });
                    event.consume();
                }
            } else if (event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.TAB) {

                if (!validEmail(tfEmail.getText()) && !tfEmail.getText().equalsIgnoreCase("")) {
                    // Clear the text if the entered email is not valid
                    AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, "Please Enter Valid Email Id", newEvent -> {
                        tfEmail.requestFocus();
                    });
                    event.consume();
                }
            }
        });

//        Insets Margin = new Insets(0, 10, 0, 1);
//        HBox.setMargin(tfDept, Margin);
//        HBox.setMargin(tfName, Margin);
//        HBox.setMargin(tfEmail, Margin);
//        HBox.setMargin(tfPhone, Margin);
        deptNameCombine.setSpacing(10);
        nameCombine.setSpacing(10);
        emailCombine.setSpacing(10);
        phoneCombine.setSpacing(10);
        deptNameCombine.setAlignment(Pos.CENTER_LEFT);
        nameCombine.setAlignment(Pos.CENTER_LEFT);
        emailCombine.setAlignment(Pos.CENTER_LEFT);
        phoneCombine.setAlignment(Pos.CENTER_LEFT);

        hbox_center.getChildren().addAll(deptNameCombine, nameCombine, emailCombine, phoneCombine);
        hbox_center.setSpacing(10);
        hbox_center.setPadding(new Insets(8));

        HBox hbox_bottom = new HBox();
        hbox_bottom.setMinWidth(popup_width);
        hbox_bottom.setMaxWidth(popup_width);
        hbox_bottom.setPrefWidth(popup_width);
//        hbox_bottom.setMaxHeight(55);
//        hbox_bottom.setMinHeight(55);
//        hbox_bottom.setPrefHeight(55);
        hbox_bottom.setSpacing(10);
        hbox_bottom.setStyle("-fx-background-radius: 0 0 5 5; -fx-background-color: white;");
        hbox_bottom.setAlignment(Pos.CENTER_RIGHT);

        Button clButton = new Button("Clear");
//        HBox.setMargin(clButton, new Insets(0, 10, 0, 0));
        Button suButton = new Button("Submit");
        clButton.setId("cancel-btn");
        suButton.setId("submit-btn");

        hbox_bottom.getChildren().addAll(suButton, clButton);
        hbox_bottom.setPadding(new Insets(8));

        borderPane.setTop(hbox_top);
        borderPane.setCenter(hbox_center);
        borderPane.setBottom(hbox_bottom);

//        Node[] nodes = new Node[]{tfDept, tfName, tfEmail, tfPhone, suButton,clButton};
//        CommonValidationsUtils.setupFocusNavigation(nodes);

        Scene scene = new Scene(borderPane, popup_width, 170);

        primaryStage.setScene(scene);
        primaryStage.setTitle(title);
        primaryStage.setResizable(false);

        scene.setFill(Color.TRANSPARENT);

        primaryStage.centerOnScreen();
        primaryStage.setOnShown((e) -> {
            Platform.runLater(tfDept::requestFocus);
        });

        scene.setOnKeyPressed((e) -> {
            if (e.getCode() == KeyCode.ENTER) {
                if (tfDept.isFocused()) {
                    Platform.runLater(tfName::requestFocus);
                } else if (tfName.isFocused()) {
                    Platform.runLater(tfEmail::requestFocus);
                } else if (tfEmail.isFocused()) {
                    Platform.runLater(tfPhone::requestFocus);
                } else if (tfPhone.isFocused()) {
                    Platform.runLater(suButton::requestFocus);
                }
            }
            e.consume();
        });
        suButton.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.LEFT) {
                Platform.runLater(() -> {
                    clButton.requestFocus();
                });
            }
            event.consume();
        });
        clButton.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.RIGHT) {
                Platform.runLater(() -> {
                    suButton.requestFocus();
                });
            }
            event.consume();
        });


        scene.setOnKeyReleased((e) -> {
            if (e.getCode() == KeyCode.ESCAPE) {
                OverlaysEffect.removeOverlaysEffect(stage);
                primaryStage.close();
            }
            e.consume();
        });
        primaryStage.show();

        suButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if (!tfDept.getText().isEmpty() && !tfName.getText().isEmpty()) {
                    inDeptInfo.setPersonDeptName(tfDept.getText());
                    inDeptInfo.setPersonName(tfName.getText());
                    inDeptInfo.setPersonEmail(tfEmail.getText());
                    inDeptInfo.setPersonPhone(tfPhone.getText());
                    callback.accept(inDeptInfo);
                    primaryStage.close();
                    OverlaysEffect.removeOverlaysEffect(stage);
                } else {
                    String msg = "Please Fill Data In All Fields !";
                    AlertUtility.CustomCallback callback = number -> {
                        tfDept.requestFocus();
                    };
                    AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, msg, callback);
                }
            }
        });

        clButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
//                primaryStage.close();
//                OverlaysEffect.removeOverlaysEffect(stage);
                tfDept.setText("");
                tfName.setText("");
                tfEmail.setText("");
                tfPhone.setText("");
                Platform.runLater(tfDept::requestFocus);
            }
        });

    }

    public <T> void openLicensePopUp(Stage stage, String title, LicenseInfo inlicenseInfo, Consumer<LicenseInfo> callback) {
        OverlaysEffect.setOverlaysEffect(stage);
        Stage primaryStage = new Stage();

        primaryStage.initOwner(stage);
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        primaryStage.initModality(Modality.APPLICATION_MODAL);

        BorderPane borderPane = new BorderPane();
        borderPane.getStylesheets().add(GenivisApplication.class.getResource("/com/opethic/genivis/ui/css/popup_for_catalog.css").toExternalForm());
        borderPane.setStyle("-fx-background-radius: 5; -fx-background-color: white; -fx-border-color: #bfbfc0; -fx-border-radius: 5; -fx-border-width: 0.8;");

        Platform.runLater(() -> {
            borderPane.requestFocus();
        });

        HBox hbox_top = new HBox();
        hbox_top.setMinWidth(998);
        hbox_top.setMaxWidth(998);
        hbox_top.setPrefWidth(998);
//        hbox_top.setMaxHeight(50);
//        hbox_top.setMinHeight(50);
//        hbox_top.setPrefHeight(50);

        Label popup_title = new Label(title);
        popup_title.setStyle("-fx-font-size: 16; -fx-text-fill: #404040; -fx-font-weight: bold;");
//        popup_title.setPadding(new Insets(0, 10, 0, 0));

        Image image = new Image(GenivisApplication.class.getResource("/com/opethic/genivis/ui/assets/close.png").toExternalForm());
        ImageView imageView = new ImageView(image);
        imageView.setStyle("-fx-cursor: hand;");
        imageView.setOnMouseClicked(event -> {
            primaryStage.close();
            OverlaysEffect.removeOverlaysEffect(stage);
        });
//        HBox.setMargin(imageView, new Insets(0, 10, 0, 0));
        imageView.setFitWidth(30);
        imageView.setFitHeight(30);

        hbox_top.setAlignment(Pos.CENTER_LEFT);
        hbox_top.getChildren().add(popup_title);
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
//        HBox.setMargin(popup_title, new Insets(0, 0, 0, 10));
        hbox_top.getChildren().add(spacer);
        hbox_top.getChildren().add(imageView);
        hbox_top.setStyle("-fx-background-radius: 5 5 0 0; -fx-background-color: #d9f0fb; -fx-border-color: #ffffff; -fx-border-width: 0 0 3 0;");
        hbox_top.setPadding(new Insets(8));

        HBox hbox_center = new HBox();
        hbox_center.setMinWidth(998);
        hbox_center.setMaxWidth(998);
        hbox_center.setPrefWidth(998);
        hbox_center.setAlignment(Pos.CENTER_LEFT);
        hbox_center.setStyle("-fx-background-color: #e6f2f8;");

        Integer initialIndex = -1;
        ObservableList<LicenseType> lstLicenseType = FXCollections.observableArrayList(Globals.getLicenseTypeList());

        HBox liceTypeLbl = new HBox();
        Label lbLicType = createLabel("Type");
        Label lbLicTypeReq = createLabelWithRequired();
        liceTypeLbl.getChildren().addAll(lbLicType, lbLicTypeReq);
        liceTypeLbl.setAlignment(Pos.CENTER_LEFT);

        HBox numLabel = new HBox();
        Label lbNum = createLabel("Number");
        Label lbNumReq = createLabelWithRequired();
        numLabel.getChildren().addAll(lbNum, lbNumReq);
        numLabel.setAlignment(Pos.CENTER_LEFT);
        TextField tfNum = createTextField("Number");
        HBox numberCombine = new HBox();
        numberCombine.getChildren().addAll(numLabel, tfNum);

// // Add a listener to tfNum to enforce 12 digits
//        tfNum.textProperty().addListener((observable, oldValue, newValue) -> {
//            // If the new value is not a digit or the length exceeds 12, revert to old value
//            if (!newValue.matches("\\d*") || newValue.length() > 12) {
//                tfNum.setText(oldValue);
//            }
//        });

        if (!inlicenseInfo.getLicenseNo().isEmpty()) tfNum.setText(inlicenseInfo.getLicenseNo());

        EventHandler<ActionEvent> handleLicenseType = event -> {
            ComboBox<LicenseType> comboBox = (ComboBox<LicenseType>) event.getSource();
            LicenseType selectedLicenseType = comboBox.getValue();
            if (selectedLicenseType != null) {
                inlicenseInfo.setLicenseTypeId(selectedLicenseType.getValue());
                inlicenseInfo.setLicenseTypeName(selectedLicenseType.getLabel());
                System.out.println("Selected License Type ID: " + selectedLicenseType.getValue());
                System.out.println("Selected License Type Name: " + selectedLicenseType.getLabel());
//                if (selectedLicenseType.getValue().equals(1)) {
//                    tfNum.focusedProperty().addListener((obs, oldVal, newVal) -> {
//                        if (!newVal) {
//                            String text = tfNum.getText().trim();
//                            if (text.length() != 12) {
//                                tfNum.requestFocus();
//                            }
//                        }
//                    });
//                }else  if (selectedLicenseType.getValue().equals(1)) {
//                    tfNum.focusedProperty().addListener((obs, oldVal, newVal) -> {
//                        if (!newVal) {
//                            String text = tfNum.getText().trim();
//                            if (text.length() != 12) {
//                                tfNum.requestFocus();
//                            }
//                        }
//                    });
//                }

            }
        };

        ComboBox<LicenseType> licenseTypeComboBox = createComboBox("Type", handleLicenseType);
        licenseTypeComboBox.setConverter(new StringConverter<LicenseType>() {
            @Override
            public String toString(LicenseType licenseType) {
                return licenseType != null ? licenseType.getLabel() : "";
            }

            @Override
            public LicenseType fromString(String string) {
                return lstLicenseType.stream().filter((v) -> v.getLabel().equalsIgnoreCase(string)).findAny().orElse(null);
            }
        });

        licenseTypeComboBox.setItems(lstLicenseType);
        AutoCompleteBox autoCompleteBox = new AutoCompleteBox(licenseTypeComboBox, initialIndex);
        if (inlicenseInfo.getLicenseTypeId() > 0) {
            lstLicenseType.stream().filter(s -> s.getLabel().equalsIgnoreCase(String.valueOf(inlicenseInfo.getLicenseTypeName()))).findAny().ifPresent(ob -> {
                licenseTypeComboBox.setValue(ob);
            });
        }

        HBox licTypeCombine = new HBox();
        licTypeCombine.getChildren().addAll(liceTypeLbl, licenseTypeComboBox);


        Label lbexp = createLabel("Expiry Date");
        TextField expDate = createTextField("DD/MM/YYYY");
        DateValidator.applyDateFormat(expDate);
        CommonFunctionalUtils.restrictDateFormat(expDate);

        expDate.focusedProperty().addListener((obs, old, nw) -> {
            System.out.println("nw" + nw);
            if (!nw) {
                String regDateStr = expDate.getText();
                if (!regDateStr.isEmpty()) {
                    LocalDate currentDate = LocalDate.now();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
//                String formattedCurrentDate = currentDate.format(formatter);
                    LocalDate regDate = LocalDate.parse(regDateStr, formatter);
//                System.out.println("register date --> " + regDateStr);
//                System.out.println("currentDate --> " + formattedCurrentDate);
                    if (regDate.isBefore(currentDate)) {
                        AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, "Expiry date cannot be lower than the current date. ", input -> {
                            expDate.requestFocus();
                        });
                    }
                }
            }
        });

        /*
        // upload start
//        Label lbdoc = createLabel("Upload Doc");
        Button uploadDoc = new Button("Upload Doc");
        uploadDoc.setId("upload-doc");
        // Set button action
        uploadDoc.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();

            // Optionally, set multiple extension filters
            FileChooser.ExtensionFilter allFilesFilter = new FileChooser.ExtensionFilter("All Files", "*.*");
            FileChooser.ExtensionFilter imageFilesFilter = new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.jpeg", "*.png", "*.gif", "*.bmp");
            FileChooser.ExtensionFilter docFilesFilter = new FileChooser.ExtensionFilter("Document Files", "*.pdf", "*.doc", "*.docx", "*.txt");
            fileChooser.getExtensionFilters().addAll(allFilesFilter, imageFilesFilter, docFilesFilter);

            // Show open file dialog
            File file = fileChooser.showOpenDialog(primaryStage);

            if (file != null) {
                System.out.println("File selected: " + file.getAbsolutePath());
            }
        });
        // upload end
//         */

        if (inlicenseInfo.getLicenseExp() != null) {
            expDate.setText(inlicenseInfo.getLicenseExp());
        }

        HBox expDateCombine = new HBox();
        expDateCombine.getChildren().addAll(lbexp, expDate);

//        Insets Margin = new Insets(0, 10, 0, 1);
//        HBox.setMargin(licenseTypeComboBox, Margin);
//        HBox.setMargin(tfNum, Margin);
//        HBox.setMargin(expDate, Margin);
        licTypeCombine.setSpacing(10);
        numberCombine.setSpacing(10);
        expDateCombine.setSpacing(10);
        licTypeCombine.setAlignment(Pos.CENTER_LEFT);
        numberCombine.setAlignment(Pos.CENTER_LEFT);
        expDateCombine.setAlignment(Pos.CENTER_LEFT);

        hbox_center.getChildren().addAll(licTypeCombine, numberCombine, expDateCombine);
        hbox_center.setSpacing(10);
        hbox_center.setPadding(new Insets(8));
        hbox_center.getChildren().addAll(lbLicType, lbLicTypeReq, licenseTypeComboBox, lbNum, lbNumReq, tfNum, lbexp, expDate);
        licenseTypeComboBox.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            System.out.println("lstLicenseType->>>> " + lstLicenseType);
            System.out.println("KK NEW------> " + newValue);
            if (newValue != null) {
                if (newValue.getLabel().equalsIgnoreCase("Aadhar No")) {

                    CommonValidationsUtils.onlyEnterNumbersLimit(tfNum, 12);
                    lbexp.setVisible(false);
                    expDate.setVisible(false);
                } else if (newValue.getLabel().equalsIgnoreCase("PAN No")) {
                    CommonFunctionalUtils.panCardValidation(tfNum);
                    lbexp.setVisible(false);
                    expDate.setVisible(false);
                }
            } else {
                lbexp.setVisible(true);
                expDate.setVisible(true);
            }
        });


//        licenseTypeComboBox.focusedProperty().addListener((obs, oldVal, newVal) -> {
//            if (!newVal) {
//                if (licenseTypeComboBox.getValue().equals("")) {
//                    licenseTypeComboBox.requestFocus();
//                }
//            }
//        });
        tfNum.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) {
                if (tfNum.getText().isEmpty()) {
                    tfNum.requestFocus();
                }
            }
        });

        HBox hbox_bottom = new HBox();
        hbox_bottom.setMinWidth(998);
        hbox_bottom.setMaxWidth(998);
        hbox_bottom.setPrefWidth(998);
//        hbox_bottom.setMaxHeight(55);
//        hbox_bottom.setMinHeight(55);
//        hbox_bottom.setPrefHeight(55);
        hbox_bottom.setSpacing(10);
        hbox_bottom.setPadding(new Insets(10));
        hbox_bottom.setStyle("-fx-background-radius: 0 0 5 5; -fx-background-color: white;");
        hbox_bottom.setAlignment(Pos.CENTER_RIGHT);

        Button clButton = new Button("Clear");
//        HBox.setMargin(clButton, new Insets(0, 10, 0, 0));
        Button suButton = new Button("Submit");
        clButton.setId("cancel-btn");
        suButton.setId("submit-btn");

        hbox_bottom.getChildren().addAll(suButton, clButton);

        borderPane.setTop(hbox_top);
        borderPane.setCenter(hbox_center);
        borderPane.setBottom(hbox_bottom);

        Scene scene = new Scene(borderPane, 1000, 170);

        primaryStage.setScene(scene);
        primaryStage.setTitle(title);
        primaryStage.setResizable(false);

        scene.setFill(Color.TRANSPARENT);

        primaryStage.centerOnScreen();
        primaryStage.setOnShown((e) -> {
            Platform.runLater(licenseTypeComboBox::requestFocus);
        });
        scene.setOnKeyPressed((e) -> {
            if (e.getCode() == KeyCode.ENTER) {
                if (licenseTypeComboBox.isFocused()) {
                    System.out.println(tfNum.getText());
                    Platform.runLater(tfNum::requestFocus);
                } else if (tfNum.isFocused()) {
                    Platform.runLater(expDate::requestFocus);
                } else if (expDate.isFocused()) {
                    Platform.runLater(suButton::requestFocus);
                }
            }
            e.consume();
        });
        suButton.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.LEFT) {
                Platform.runLater(() -> {
                    clButton.requestFocus();
                });
            }
            event.consume();
        });
        clButton.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.RIGHT) {
                Platform.runLater(() -> {
                    suButton.requestFocus();
                });
            }
            event.consume();
        });
        scene.setOnKeyReleased((e) -> {
            if (e.getCode() == KeyCode.ESCAPE) {
                OverlaysEffect.removeOverlaysEffect(stage);
                primaryStage.close();
            }
            e.consume();
        });
        primaryStage.show();

        suButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if (!tfNum.getText().isEmpty()) {
                    inlicenseInfo.setLicenseSlugName(licenseTypeComboBox.getValue().getSlugName());
                    inlicenseInfo.setLicenseTypeName(licenseTypeComboBox.getValue().getLabel());
                    inlicenseInfo.setLicenseTypeId(licenseTypeComboBox.getValue().getValue());
                    inlicenseInfo.setLicenseNo(tfNum.getText());
                    inlicenseInfo.setLicenseExp(expDate.getText());
                    callback.accept(inlicenseInfo);
                    primaryStage.close();
                    OverlaysEffect.removeOverlaysEffect(stage);
                } else {
                    String msg = "Please Fill Data In All Fields !";
                    AlertUtility.CustomCallback callback = number -> {
                        licenseTypeComboBox.requestFocus();
                    };
                    AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, msg, callback);
                }
            }
        });

        clButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                licenseTypeComboBox.setValue(null);
                tfNum.setText("");
                expDate.setText("");
                Platform.runLater(licenseTypeComboBox::requestFocus);
            }
        });
    }


    public <T> void openShippingPopUp(Stage stage, String title, ShippingInfo inshippingInfo, Consumer<ShippingInfo> callback) {
        OverlaysEffect.setOverlaysEffect(stage);
        Stage primaryStage = new Stage();

        primaryStage.initOwner(stage);
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        primaryStage.initModality(Modality.APPLICATION_MODAL);

        //Main Layout................................................................................................................................
        BorderPane borderPane = new BorderPane();
        borderPane.getStylesheets().add(GenivisApplication.class.getResource("/com/opethic/genivis/ui/css/popup_for_catalog.css").toExternalForm());
        borderPane.setStyle("-fx-background-radius: 5; -fx-background-color: white; -fx-border-color: #bfbfc0; -fx-border-radius: 5; -fx-border-width: 0.8;");

        Platform.runLater(() -> {
            borderPane.requestFocus();
        });

        //BorderPan under Top Layout....................................................................................................................
        HBox hbox_top = new HBox();
        hbox_top.setMinWidth(998);
        hbox_top.setMaxWidth(998);
        hbox_top.setPrefWidth(998);
//        hbox_top.setMaxHeight(50);
//        hbox_top.setMinHeight(50);
//        hbox_top.setPrefHeight(50);

        Label popup_title = new Label(title);
        popup_title.setStyle("-fx-font-size: 16; -fx-text-fill: #404040; -fx-font-weight: bold;");
//        popup_title.setPadding(new Insets(0, 10, 0, 0));

        Image image = new Image(GenivisApplication.class.getResource("/com/opethic/genivis/ui/assets/close.png").toExternalForm());
        ImageView imageView = new ImageView(image);
        imageView.setStyle("-fx-cursor: hand;");
        imageView.setOnMouseClicked(event -> {
            primaryStage.close();
            OverlaysEffect.removeOverlaysEffect(stage);
        });
//        HBox.setMargin(imageView, new Insets(0, 10, 0, 0));
        imageView.setFitWidth(30);
        imageView.setFitHeight(30);

        hbox_top.setAlignment(Pos.CENTER_LEFT);
        hbox_top.getChildren().add(popup_title);
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
//        HBox.setMargin(popup_title, new Insets(0, 0, 0, 10));
        hbox_top.getChildren().add(spacer);
        hbox_top.getChildren().add(imageView);
        hbox_top.setStyle("-fx-background-radius: 5 5 0 0; -fx-background-color: #d9f0fb; -fx-border-color: #ffffff; -fx-border-width: 0 0 3 0;");
        hbox_top.setPadding(new Insets(8));

        //BorderPane Under Center Layout.....................................................................................................
        HBox hbox_center = new HBox();
        hbox_center.setMinWidth(998);
        hbox_center.setMaxWidth(998);
        hbox_center.setPrefWidth(998);
        hbox_center.setAlignment(Pos.CENTER_LEFT);
        hbox_center.setStyle("-fx-background-color: #e6f2f8;");

        Label lbAdd = createLabel("Address");
        Label lbAddReq = createLabelWithRequired();
        HBox addLabel = new HBox();
        addLabel.getChildren().addAll(lbAdd, lbAddReq);
        addLabel.setAlignment(Pos.CENTER_LEFT);
        TextField tfAdd = createTextField("Address");
        HBox addCombine = new HBox();
        addCombine.getChildren().addAll(addLabel, tfAdd);
        if (!inshippingInfo.getShippingName().isEmpty()) tfAdd.setText(inshippingInfo.getShippingName());
        tfAdd.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) {
                if (tfAdd.getText().isEmpty()) {
                    tfAdd.requestFocus();
                }
            }
        });
        Integer initialIndex = -1;
        ObservableList<StateOption> lstShippingType = FXCollections.observableArrayList(Globals.getIndiaStateList());
        Label lbStateType = createLabel("State");
        Label lbStateReq = createLabelWithRequired();
        HBox stateLabel = new HBox();
        stateLabel.getChildren().addAll(lbStateType, lbStateReq);
        stateLabel.setAlignment(Pos.CENTER_LEFT);
        EventHandler<ActionEvent> handleLicenseType = event -> {
            ComboBox<T> comboBox = (ComboBox<T>) event.getSource();
            lstShippingType.stream().filter(s -> s.getStateName().equalsIgnoreCase(String.valueOf(comboBox.getValue()))).findAny().ifPresent(ob -> {
                inshippingInfo.setShippingStateId(ob.getId());
                inshippingInfo.setShippingStateCode(ob.getStateCode());
                inshippingInfo.setShippingStateName(ob.getStateName());
            });
        };

        // Create the ComboBox
        ComboBox<StateOption> stateComboBox = createComboBox("Select", handleLicenseType);

//        stateComboBox.focusedProperty().addListener((obs, oldVal, newVal) -> {
//            if (!newVal) {
//                if (stateComboBox.getValue().equals("")) {
//                    stateComboBox.requestFocus();
//                }
//            }
//        });

//        stateComboBox.setConverter(new StringConverter<StateOption>() {
//            @Override
//            public String toString(StateOption stateOption) {
//                return stateOption != null ? stateOption.getStateName() : "";
//            }
//
//            @Override
//            public StateOption fromString(String string) {
//                return null;
//            }
//        });

        // Set the items
        stateComboBox.setItems(lstShippingType);

        // Set a custom cell factory to display only the stateName in the dropdown list
        stateComboBox.setCellFactory(lv -> new ListCell<StateOption>() {
            @Override
            protected void updateItem(StateOption item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getStateName());
                }
            }
        });

        // Set the button cell to display the selected stateName in the ComboBox
        stateComboBox.setButtonCell(new ListCell<StateOption>() {
            @Override
            protected void updateItem(StateOption item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getStateName());
                }
            }
        });

        // Set a StringConverter to control the text representation of the selected item
        stateComboBox.setConverter(new StringConverter<StateOption>() {
            @Override
            public String toString(StateOption item) {
                if (item == null) {
                    return null;
                } else {
                    return item.getStateName();
                }
            }

            @Override
            public StateOption fromString(String string) {
                return lstShippingType.stream()
                        .filter(state -> state.getStateName().equals(string))
                        .findFirst()
                        .orElse(null);
            }
        });


        AutoCompleteBox autoCompleteBox = new AutoCompleteBox(stateComboBox, initialIndex);

        if (inshippingInfo.getShippingStateId() > 0) {
            lstShippingType.stream().filter(s -> s.getStateName().equalsIgnoreCase(String.valueOf(inshippingInfo.getShippingStateName()))).findAny().ifPresent(ob -> {
                stateComboBox.setValue(ob);
                inshippingInfo.setShippingStateId(ob.getId());
                inshippingInfo.setShippingStateCode(ob.getStateCode());
                inshippingInfo.setShippingStateName(ob.getStateName());
            });
        }

        HBox stateCombine = new HBox();
        stateCombine.getChildren().addAll(stateLabel, stateComboBox);

        stateComboBox.getStyleClass().add("isRequired");
//        Insets Margin = new Insets(0, 10, 0, 1);
//        HBox.setMargin(tfAdd, Margin);
//        HBox.setMargin(stateComboBox, Margin);

        addCombine.setSpacing(10);
        stateCombine.setSpacing(10);
        addCombine.setAlignment(Pos.CENTER_LEFT);
        stateCombine.setAlignment(Pos.CENTER_LEFT);

        hbox_center.getChildren().addAll(addCombine, stateCombine);
        hbox_center.setPadding(new Insets(8));
        hbox_center.setSpacing(10);

        HBox hbox_bottom = new HBox();
        hbox_bottom.setMinWidth(998);
        hbox_bottom.setMaxWidth(998);
        hbox_bottom.setPrefWidth(998);
//        hbox_bottom.setMaxHeight(55);
//        hbox_bottom.setMinHeight(55);
//        hbox_bottom.setPrefHeight(55);
        hbox_bottom.setSpacing(10);
        hbox_bottom.setStyle("-fx-background-radius: 0 0 5 5; -fx-background-color: white;");
        hbox_bottom.setAlignment(Pos.CENTER_RIGHT);

        Button clButton = new Button("Clear");
//        HBox.setMargin(clButton, new Insets(0, 10, 0, 0));
        Button suButton = new Button("Submit");
        clButton.setId("cancel-btn");
        suButton.setId("submit-btn");


        hbox_bottom.getChildren().addAll(suButton, clButton);
        hbox_bottom.setPadding(new Insets(8));

        borderPane.setTop(hbox_top);
        borderPane.setCenter(hbox_center);
        borderPane.setBottom(hbox_bottom);

//        Node[] nodes = new Node[]{tfAdd,stateComboBox,suButton,clButton};
//        CommonValidationsUtils.setupFocusNavigation(nodes);

        Scene scene = new Scene(borderPane, 1000, 170);

        primaryStage.setScene(scene);
        primaryStage.setTitle(title);
        primaryStage.setResizable(false);

        scene.setFill(Color.TRANSPARENT);

        primaryStage.centerOnScreen();
        primaryStage.setOnShown((e) -> {
            Platform.runLater(tfAdd::requestFocus);
        });
        scene.setOnKeyPressed((e) -> {
            if (e.getCode() == KeyCode.ENTER) {
                if (tfAdd.isFocused()) {
                    Platform.runLater(stateComboBox::requestFocus);
                } else if (stateComboBox.isFocused()) {
                    Platform.runLater(suButton::requestFocus);
                }
            }
            e.consume();
        });


        suButton.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.LEFT) {
                Platform.runLater(() -> {
                    clButton.requestFocus();
                });
            }
            event.consume();
        });
        clButton.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.RIGHT) {
                Platform.runLater(() -> {
                    suButton.requestFocus();
                });
            }
            event.consume();
        });

        scene.setOnKeyReleased((e) -> {
            if (e.getCode() == KeyCode.ESCAPE) {
                OverlaysEffect.removeOverlaysEffect(stage);
                primaryStage.close();
            }
            e.consume();
        });
        primaryStage.show();

        suButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if (!tfAdd.getText().isEmpty()) {
                    inshippingInfo.setShippingName(tfAdd.getText());
                    inshippingInfo.setShippingStateName(stateComboBox.getValue().getStateName());
                    inshippingInfo.setShippingStateId(stateComboBox.getValue().getId());
                    inshippingInfo.setShippingStateCode(stateComboBox.getValue().getStateCode());
                    callback.accept(inshippingInfo);
                    primaryStage.close();
                    OverlaysEffect.removeOverlaysEffect(stage);
                } else {
                    String msg = "Please Fill Data In All Fields !";
                    AlertUtility.CustomCallback callback = number -> {
                        tfAdd.requestFocus();
                    };
                    AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, msg, callback);
                }
            }
        });

        clButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
//                primaryStage.close();
//                OverlaysEffect.removeOverlaysEffect(stage);
                tfAdd.setText("");
                stateComboBox.setValue(null);
                Platform.runLater(tfAdd::requestFocus);
            }
        });

    }

 /*   public <T> void openOpeningBalancePopUp(Stage stage, GstList inGstList, Consumer<GstList> callback) {
        OverlaysEffect.setOverlaysEffect(stage);
        Stage primaryStage = new Stage();
        primaryStage.initOwner(stage);
        ObservableList<OBListDTO> tbllstOB = FXCollections.observableArrayList();
        int edtIdx = -1, edtId = 0, bankEdtIdx = -1, bankEdtId = 0;
        int id = 0;
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        primaryStage.initModality(Modality.APPLICATION_MODAL);

        //Main Layout................................................................................................................................
        BorderPane borderPane = new BorderPane();
        borderPane.getStylesheets().add(GenivisApplication.class.getResource("/com/opethic/genivis/ui/css/popup_for_catalog.css").toExternalForm());
        borderPane.setStyle("-fx-background-radius: 5; -fx-background-color: white; -fx-border-color: #bfbfc0; -fx-border-radius: 5; -fx-border-width: 0.8;");

        Platform.runLater(() -> {
            borderPane.requestFocus();
        });

        VBox vbox_top = new VBox();
        //BorderPan under Top Layout....................................................................................................................
        HBox hbox_top = new HBox();
        hbox_top.setMinWidth(1000);
        hbox_top.setMaxWidth(1000);
        hbox_top.setPrefWidth(1000);
        hbox_top.setMaxHeight(50);
        hbox_top.setMinHeight(50);
        hbox_top.setPrefHeight(50);

        Label popup_title = new Label("Opening Balance");
        popup_title.setStyle("-fx-font-size: 16; -fx-text-fill: #404040; -fx-font-weight: bold;");
        popup_title.setPadding(new Insets(0, 10, 0, 0));

        Image image = new Image(GenivisApplication.class.getResource("/com/opethic/genivis/ui/assets/close.png").toExternalForm());
        ImageView imageView = new ImageView(image);
        imageView.setStyle("-fx-cursor: hand;");
        imageView.setOnMouseClicked(event -> {
            primaryStage.close();
            OverlaysEffect.removeOverlaysEffect(stage);
        });
        HBox.setMargin(imageView, new Insets(0, 10, 0, 0));
        imageView.setFitWidth(30);
        imageView.setFitHeight(30);

        hbox_top.setAlignment(Pos.CENTER_LEFT);
        hbox_top.getChildren().add(popup_title);
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        HBox.setMargin(popup_title, new Insets(0, 0, 0, 10));
        hbox_top.getChildren().add(spacer);
        hbox_top.getChildren().add(imageView);
        hbox_top.setStyle("-fx-background-radius: 5 5 0 0; -fx-background-color: #d9f0fb; -fx-border-color: #ffffff; -fx-border-width: 0 0 3 0;");

        //BorderPane Under Center Layout.....................................................................................................
        HBox hbox_1 = new HBox();
        hbox_1.setMinWidth(1000);
        hbox_1.setMaxWidth(1000);
        hbox_1.setPrefWidth(1000);
        hbox_1.setAlignment(Pos.CENTER);
        hbox_1.setStyle("-fx-background-color: #e6f2f8;");

        HBox hbox_2 = new HBox();
        hbox_2.setMinWidth(1000);
        hbox_2.setMaxWidth(1000);
        hbox_2.setPrefWidth(1000);
        hbox_2.setAlignment(Pos.CENTER);
        hbox_2.setStyle("-fx-background-color: #e6f2f8;");

        HBox hbox_3 = new HBox();
        hbox_3.setMinWidth(1000);
        hbox_3.setMaxWidth(1000);
        hbox_3.setPrefWidth(1000);
        hbox_3.setAlignment(Pos.CENTER);
        hbox_3.setStyle("-fx-background-color: #e6f2f8;");

        Integer initialIndex = -1;

        Label lbOpeningBanking = createLabel("Opening Banking");
        TextField tfOpeningBanking = createTextField("Opening Banking");

        Label lbSumOfBanlance = createLabel("Sum Of Balance");
        TextField tfSumOfBanlance = createTextField("Sum Of Balance");

        Label lbRemaningAmount = createLabel("Remaning Amount");
        TextField tfRemaningAmount = createTextField("Remaning Amount");

        Label lbInvoiceNo = createLabel("Invoice No");
        TextField tfInvoiceNo = createTextField("Invoice No");

        Label lbInvoiceDate = createLabel("Invoice Date");
        DatePicker dpInvoiceDate = createDatePicker("DD/MM/YYYY");

        Label lbDueDate = createLabel("Due Date");
        DatePicker dpDueDate = createDatePicker("DD/MM/YYYY");


        Label lbDueDays = createLabel("Due Days");
        TextField tfDueDays = createTextField("Due Days");


        Label lbBillAmount = createLabel("Bill Amount");
        TextField tfBillAmount = createTextField("Bill Amount");

        Label lbPaidAmount = createLabel("Paid Amount");
        TextField tfPaidAmount = createTextField("Paid Amount");

        Label lbBalanceAmount = createLabel("Balance Amount");
        TextField tfBalanceAmount = createTextField("Balance Amount");

        ObservableList<BalanceType> lstOpeningBalanceTypes = FXCollections.observableArrayList(Globals.getAllBalanceType());
        Label lbOBType = createLabel("Type");
        EventHandler<ActionEvent> handleGstType = event -> {
            ComboBox<T> comboBox = (ComboBox<T>) event.getSource();
            lstOpeningBalanceTypes.stream().filter(s -> s.getType().equalsIgnoreCase(String.valueOf(comboBox.getValue()))).findAny().ifPresent(ob -> {
                inGstList.setGstTypeid(ob.getId());
                inGstList.setGstTypeName(ob.getType());
            });
        };
        ComboBox<BalanceType> OpeningBalanceTypeComboBox = createComboBox("Type", handleGstType);
        OpeningBalanceTypeComboBox.setItems(lstOpeningBalanceTypes);
        AutoCompleteBox autoCompleteBox = new AutoCompleteBox(OpeningBalanceTypeComboBox, initialIndex);
        if (inGstList.getGstTypeid() > 0) {
            lstOpeningBalanceTypes.stream().filter(s -> s.getType().equalsIgnoreCase(String.valueOf(inGstList.getGstTypeName()))).findAny().ifPresent(ob -> {
                OpeningBalanceTypeComboBox.setValue(ob);
            });
        }

        VBox vBox = new VBox();

        TableView<OBListDTO> tableView = new TableView();
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tableView.setPrefHeight(500);
        tableView.setMaxHeight(500);
        tableView.setMinHeight(500);

        TableColumn<OBListDTO, String> tbc1InvoiceNo = new TableColumn<>("Invoice No");
        TableColumn<OBListDTO, String> tbc2InvoiceAmt = new TableColumn<>("Invoice Amount");
        TableColumn<OBListDTO, Void> tbc3Type = new TableColumn<>("Type");
        TableColumn<OBListDTO, String> tbc4BillDate = new TableColumn<>("Bill Date");
        TableColumn<OBListDTO, String> tbc5PaidAmount = new TableColumn<>("Paid Amount");
        TableColumn<OBListDTO, String> tbc6Balance = new TableColumn<>("Balance");
        TableColumn<OBListDTO, String> tbc7DueDate = new TableColumn<>("Due Date");
        TableColumn<OBListDTO, String> tbc8DueDays = new TableColumn<>("Due Days");
        TableColumn<OBListDTO, Void> tbc9Action = new TableColumn<>("Action");

        tableView.getColumns().addAll(tbc1InvoiceNo, tbc2InvoiceAmt, tbc3Type, tbc4BillDate, tbc5PaidAmount, tbc6Balance, tbc7DueDate, tbc8DueDays, tbc9Action);

        tbc1InvoiceNo.setCellValueFactory(new PropertyValueFactory<>("invoiceNo"));
        tbc2InvoiceAmt.setCellValueFactory(new PropertyValueFactory<>("billAmount"));
        tbc3Type.setCellValueFactory(new PropertyValueFactory<>("type"));
        tbc4BillDate.setCellValueFactory(new PropertyValueFactory<>("invoiceDate"));
        tbc5PaidAmount.setCellValueFactory(new PropertyValueFactory<>("paidAmount"));
        tbc6Balance.setCellValueFactory(new PropertyValueFactory<>("balanceAmount"));
        tbc7DueDate.setCellValueFactory(new PropertyValueFactory<>("dueDate"));
        tbc8DueDays.setCellValueFactory(new PropertyValueFactory<>("dueDays"));
        tbc9Action.setCellFactory(param -> {
            final TableCell<OBListDTO, Void> cell = new TableCell<>() {
                private ImageView delImg = Globals.getDelImage();
                private ImageView edtImg = Globals.getEdtImage();

                {
                    delImg.setFitHeight(20.0);
                    delImg.setFitWidth(20.0);
                    edtImg.setFitHeight(20.0);
                    edtImg.setFitWidth(20.0);
                }

                private final Button actionButton = new Button("", delImg);
                private final Button edtButton = new Button("", edtImg);

                {
                    actionButton.setOnAction(event -> {
                        tbllstOB.remove(getIndex());
                        tfInvoiceNo.requestFocus();
                    });

                    edtButton.setOnAction(event -> {

//                      edtIdx = getIndex();
                        OBListDTO nv = tbllstOB.get(edtId);

                        if (nv != null) {
                            tfInvoiceNo.setText(nv.getInvoiceNo() + "");
                            dpInvoiceDate.setValue(DateConvertUtil.convertStringToLocalDate(nv.getInvoiceDate()));
                            dpDueDate.setValue(DateConvertUtil.convertStringToLocalDate(nv.getDueDate()));
                            tfDueDays.setText(nv.getDueDays() + "");
                            tfBillAmount.setText(nv.getBillAmount() + "");
                            tfPaidAmount.setText(nv.getPaidAmount() + "");
                            tfBalanceAmount.setText(nv.getBalanceAmount() + "");
//                            OpeningBalanceTypeComboBox.setText(nv.getBalanceAmount() + "");
                            tfInvoiceNo.requestFocus();
                        }
                    });
                }

                HBox hbActions = new HBox();

                {
                    hbActions.getChildren().add(edtButton);
                    hbActions.getChildren().add(actionButton);
                    hbActions.setSpacing(10.0);
                }

                @Override
                protected void updateItem(Void item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                    } else {
                        setGraphic(hbActions);
                    }
                }
            };
            return cell;
        });
        tableView.setOnMouseClicked(mouseEvent -> {
            if (mouseEvent.getClickCount() == 2) {
                OBListDTO SelectedValue = tableView.getSelectionModel().getSelectedItem();

                if (SelectedValue != null) {
//                    id = SelectedValue.getId();
                    tfInvoiceNo.setText("" + SelectedValue.getInvoiceNo());
                    dpInvoiceDate.setValue(DateConvertUtil.convertStringToLocalDate(SelectedValue.getInvoiceDate()));
                    dpDueDate.setValue(DateConvertUtil.convertStringToLocalDate(SelectedValue.getDueDate()));
                    tfDueDays.setText("" + SelectedValue.getDueDays());
                    tfBillAmount.setText("" + SelectedValue.getBillAmount());
                    tfPaidAmount.setText("" + SelectedValue.getPaidAmount());
                    tfBalanceAmount.setText("" + SelectedValue.getBalanceAmount());
//                    OpeningBalanceTypeComboBox.setValue("" + SelectedValue.getBalanceAmount());
                }
            }
        });

        tableView.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                OBListDTO SelectedValue = tableView.getSelectionModel().getSelectedItem();
                if (SelectedValue != null) {
//                    id = SelectedValue.getId();
                    tfInvoiceNo.setText("" + SelectedValue.getInvoiceNo());
                    dpInvoiceDate.setValue(DateConvertUtil.convertStringToLocalDate(SelectedValue.getInvoiceDate()));
                    dpDueDate.setValue(DateConvertUtil.convertStringToLocalDate(SelectedValue.getDueDate()));
                    tfDueDays.setText("" + SelectedValue.getDueDays());
                    tfBillAmount.setText("" + SelectedValue.getBillAmount());
                    tfPaidAmount.setText("" + SelectedValue.getPaidAmount());
                    tfBalanceAmount.setText("" + SelectedValue.getBalanceAmount());
//                    OpeningBalanceTypeComboBox.setValue("" + SelectedValue.getBalanceAmount());
                }
            }
        });
        tableView.setItems(tbllstOB);

        Insets Margin = new Insets(0, 10, 0, 1);
        HBox.setMargin(tfOpeningBanking, Margin);
        HBox.setMargin(tfSumOfBanlance, Margin);
        HBox.setMargin(tfRemaningAmount, Margin);

        HBox.setMargin(tfInvoiceNo, Margin);
        HBox.setMargin(dpInvoiceDate, Margin);
        HBox.setMargin(dpDueDate, Margin);
        HBox.setMargin(tfDueDays, Margin);

        HBox.setMargin(tfBillAmount, Margin);
        HBox.setMargin(tfPaidAmount, Margin);
        HBox.setMargin(tfBalanceAmount, Margin);
        HBox.setMargin(OpeningBalanceTypeComboBox, Margin);


        hbox_1.getChildren().addAll(lbOpeningBanking, tfOpeningBanking, lbSumOfBanlance, tfSumOfBanlance, lbRemaningAmount, tfRemaningAmount);
        hbox_2.getChildren().addAll(lbInvoiceNo, tfInvoiceNo, lbInvoiceDate, dpInvoiceDate, lbDueDate, dpDueDate, lbDueDays, tfDueDays);
        hbox_3.getChildren().addAll(lbBillAmount, tfBillAmount, lbPaidAmount, tfPaidAmount, lbBalanceAmount, tfBalanceAmount, lbOBType, OpeningBalanceTypeComboBox);

        HBox hbox_bottom = new HBox();
        hbox_bottom.setMinWidth(1000);
        hbox_bottom.setMaxWidth(1000);
        hbox_bottom.setPrefWidth(1000);
        hbox_bottom.setMaxHeight(55);
        hbox_bottom.setMinHeight(55);
        hbox_bottom.setPrefHeight(55);
        hbox_bottom.setSpacing(10);
        hbox_bottom.setStyle("-fx-background-radius: 0 0 5 5; -fx-background-color: white;");
        hbox_bottom.setAlignment(Pos.CENTER_RIGHT);

        Button suButton = new Button("Submit");
        HBox.setMargin(suButton, new Insets(0, 10, 0, 0));
        Button clButton = new Button("Cancel");
        suButton.setId("submit-btn");
        clButton.setId("cancel-btn");

        HBox hbox_middle = new HBox();
        hbox_middle.setMinWidth(1000);
        hbox_middle.setMaxWidth(1000);
        hbox_middle.setPrefWidth(1000);
        hbox_middle.setMaxHeight(55);
        hbox_middle.setMinHeight(55);
        hbox_middle.setPrefHeight(55);
        hbox_middle.setSpacing(10);
        hbox_middle.setStyle("-fx-background-radius: 0 0 5 5; -fx-background-color: white;");
        hbox_middle.setAlignment(Pos.CENTER_RIGHT);

        Button addButton = new Button("Add");
        HBox.setMargin(addButton, new Insets(0, 10, 0, 0));
        Button clearButton = new Button("Cancel");
        addButton.setId("add-btn");
        clearButton.setId("clear-btn");
        hbox_bottom.getChildren().addAll(addButton, clearButton);

        borderPane.setTop(hbox_top);
        borderPane.setCenter(hbox_1);
        borderPane.setCenter(hbox_2);
        borderPane.setCenter(hbox_3);
        borderPane.setCenter(hbox_middle);
        borderPane.setBottom(hbox_bottom);

        vbox_top.getChildren().addAll(hbox_top, hbox_1, hbox_2, hbox_3, hbox_middle);
        borderPane.setTop(vbox_top);
        borderPane.setCenter(vBox);

        Scene scene = new Scene(borderPane, 1010, 210);

        primaryStage.setScene(scene);
        primaryStage.setResizable(false);

        scene.setFill(Color.TRANSPARENT);

        primaryStage.centerOnScreen();
        primaryStage.setOnShown((e) -> {
            Platform.runLater(tfInvoiceNo::requestFocus);
        });

        clearButton.setOnKeyPressed(event -> {
            tfInvoiceNo.setText("");
            dpInvoiceDate.setValue(null);
            dpDueDate.setValue(null);
            tfDueDays.setText("");
            tfBillAmount.setText("");
            tfPaidAmount.setText("");
            tfBalanceAmount.setText("");
            OpeningBalanceTypeComboBox.setValue(null);
            tfInvoiceNo.requestFocus();
        });

        addButton.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.TAB) {
                Platform.runLater(() -> {
                    clearButton.requestFocus();
                });
            }
            event.consume();
        });

        addButton.setOnAction(actionEvent -> {
            if (tfInvoiceNo.getText().isEmpty()) {

            } else {
                LocalDate date = dpInvoiceDate.getValue();
                LocalDate date1 = dpDueDate.getValue();
                OBListDTO OBListDTO = new OBListDTO(0, tfInvoiceNo.getText(), DateConvertUtil.convertLocalDatetoString(date), DateConvertUtil.convertLocalDatetoString(date1), Integer.parseInt(tfDueDays.getText()), Double.parseDouble(tfBillAmount.getText()), Double.parseDouble(tfPaidAmount.getText()), Double.parseDouble(tfBalanceAmount.getText()));
                tbllstOB.add(OBListDTO);
                dpInvoiceDate.setValue(null);
                dpDueDate.setValue(null);
                tfInvoiceNo.setText("");
                tfDueDays.setText("");
                tfBillAmount.setText("");
                tfBalanceAmount.setText("");
                tfPaidAmount.setText("");
                tfInvoiceNo.requestFocus();
            }
        });

        scene.setOnKeyReleased((e) -> {
            if (e.getCode() == KeyCode.ESCAPE) {
                primaryStage.close();
            }
        });
        scene.setOnKeyPressed((e) -> {
            if (e.getCode() == KeyCode.ENTER) {
                if (tfInvoiceNo.isFocused()) {
                    Platform.runLater(dpInvoiceDate::requestFocus);
                } else if (dpInvoiceDate.isFocused()) {
                    Platform.runLater(dpDueDate::requestFocus);
                } else if (dpDueDate.isFocused()) {
                    Platform.runLater(tfDueDays::requestFocus);
                } else if (tfDueDays.isFocused()) {
                    Platform.runLater(tfBillAmount::requestFocus);
                } else if (tfBillAmount.isFocused()) {
                    Platform.runLater(tfPaidAmount::requestFocus);
                } else if (tfPaidAmount.isFocused()) {
                    Platform.runLater(tfBalanceAmount::requestFocus);
                } else if (tfBalanceAmount.isFocused()) {
                    Platform.runLater(OpeningBalanceTypeComboBox::requestFocus);
                } else if (OpeningBalanceTypeComboBox.isFocused()) {
                    Platform.runLater(addButton::requestFocus);
                }
            }
            e.consume();
        });
        primaryStage.show();

        suButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if (!tfInvoiceNo.getText().isEmpty() && !tfDueDays.getText().isEmpty() && dpInvoiceDate.getValue() != null && dpDueDate.getValue() != null && !tfBillAmount.getText().isEmpty() && !tfPaidAmount.getText().isEmpty() && !tfBalanceAmount.getText().isEmpty()) {
                    inGstList.setGstNo(tfInvoiceNo.getText());
                    inGstList.setPanNo(tfDueDays.getText());
                    inGstList.setGstRegDate(dpInvoiceDate.getValue());
                    inGstList.setGstRegDate(dpDueDate.getValue());
                    inGstList.setPanNo(tfBillAmount.getText());
                    inGstList.setPanNo(tfPaidAmount.getText());
                    inGstList.setPanNo(tfBalanceAmount.getText());
                    callback.accept(inGstList);
                    primaryStage.close();
                    OverlaysEffect.removeOverlaysEffect(stage);
                } else {
                    String msg = "Please Fill Data In All Fields !";
                    AlertUtility.CustomCallback callback = number -> {
                        tfInvoiceNo.requestFocus();
                    };
                    AlertUtility.AlertError(AlertUtility.alertTypeError, msg, callback);
                }
            }
        });

        clButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                primaryStage.close();
                OverlaysEffect.removeOverlaysEffect(stage);
            }
        });
    }*/


    public <T> void openOpeningBalancePopUpWithParam(Stage stage, Double openBal, ObservableList<OBListDTO> tbllstOB, Consumer<ObservableList<OBListDTO>> callback) {
//        ObservableList<OBListDTO> tbllstOB = FXCollections.observableArrayList();
//
//        tbllstOB = inTblLstOb;
        OverlaysEffect.setOverlaysEffect(stage);
        Stage primaryStage = new Stage();
        primaryStage.initOwner(stage);
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        primaryStage.initModality(Modality.APPLICATION_MODAL);
//        AtomicReference<Integer> edtInx = new AtomicReference<>(-1);
//        AtomicReference<Integer> edtId = new AtomicReference<>(0);
        final Integer[] edtInx = {-1};
        final Integer[] edtId = {0};

        Double height = ReCalculateHeight();
        Double width = ReCalculateWidth();

        BorderPane borderPane = new BorderPane();
        borderPane.getStylesheets().add(GenivisApplication.class.getResource("/com/opethic/genivis/ui/css/popup_for_catalog.css").toExternalForm());
        borderPane.setStyle("-fx-background-radius: 5; -fx-background-color: white; -fx-border-color: #bfbfc0; -fx-border-radius: 5; -fx-border-width: 0.8;");

        Platform.runLater(() -> {
            borderPane.requestFocus();
        });

        VBox vbox_top = new VBox();
        //BorderPan under Top Layout....................................................................................................................
        HBox hbox_top = new HBox();
        hbox_top.setMinWidth(height);
        hbox_top.setMaxWidth(width);
        hbox_top.setPrefWidth(width);
//        hbox_top.setMaxHeight(50);
//        hbox_top.setMinHeight(50);
//        hbox_top.setPrefHeight(50);

        Label popup_title = new Label("Opening Balance");
        popup_title.setStyle("-fx-font-size: 16; -fx-text-fill: #404040; -fx-font-weight: bold; -fx-font-family: \"Arial\";");
//        popup_title.setPadding(new Insets(0, 10, 0, 0));

        Image image = new Image(GenivisApplication.class.getResource("/com/opethic/genivis/ui/assets/close.png").toExternalForm());
        ImageView imageView = new ImageView(image);
        imageView.setStyle("-fx-cursor: hand;");
        imageView.setOnMouseClicked(event -> {
            primaryStage.close();
            OverlaysEffect.removeOverlaysEffect(stage);
        });
//        HBox.setMargin(imageView, new Insets(0, 10, 0, 0));
        imageView.setFitWidth(30);
        imageView.setFitHeight(30);

        hbox_top.setAlignment(Pos.CENTER_LEFT);
        hbox_top.getChildren().add(popup_title);
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
//        HBox.setMargin(popup_title, new Insets(0, 0, 0, 10));
        hbox_top.getChildren().add(spacer);
        hbox_top.getChildren().add(imageView);
        hbox_top.setStyle("-fx-background-radius: 5 5 0 0; -fx-background-color: #d9f0fb; -fx-border-color: #ffffff; -fx-border-width: 0 0 3 0;");
        hbox_top.setPadding(new Insets(8));

        //BorderPane Under Center Layout.....................................................................................................
        HBox hbox_1 = new HBox();
        hbox_1.setMinWidth(width);
        hbox_1.setMaxWidth(width);
        hbox_1.setPrefWidth(width);
        hbox_1.setAlignment(Pos.CENTER_LEFT);


        HBox hbox_2 = new HBox();
        hbox_2.setMinWidth(width);
        hbox_2.setMaxWidth(width);
        hbox_2.setPrefWidth(width);
        hbox_2.setAlignment(Pos.CENTER_LEFT);

        HBox hbox_3 = new HBox();
        hbox_3.setMinWidth(width);
        hbox_3.setMaxWidth(width);
        hbox_3.setPrefWidth(width);
        hbox_3.setAlignment(Pos.CENTER_LEFT);

        Integer initialIndex = -1;

        Label lbOpeningBalance = createLabel("Opening Balance");
        lbOpeningBalance.getStyleClass().add("cust-lbl-b");
        Label lbOpeningBalanceVal = createLabel("0.0");
        lbOpeningBalanceVal.getStyleClass().add("cust-lbl");
        lbOpeningBalanceVal.setText(openBal + "");

        Label lbSumOfBanlance = createLabel("Sum Of Balance");
        lbSumOfBanlance.getStyleClass().add("cust-lbl-b");
        Label lbSumOfBanlanceVal = createLabel("0.0");
        lbSumOfBanlanceVal.getStyleClass().add("cust-lbl");

        Label lbRemaningAmount = createLabel("Remaining Amount");
        lbRemaningAmount.getStyleClass().add("cust-lbl-b");
        Label lbRemaningAmountVal = createLabel("0.0");
        lbRemaningAmountVal.getStyleClass().add("cust-lbl");

        Label lbInvoiceNo = createLabel("Invoice No");
//        lbInvoiceNo.getStyleClass().add("cust-lbl");
        TextField tfInvoiceNo = createTextField("Invoice No");
        Label rqInvoiceNo = createLabelWithRequired();
        tfInvoiceNo.getStyleClass().add("isRequired");
        tfInvoiceNo.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) {
                if (tfInvoiceNo.getText().isEmpty()) {
                    tfInvoiceNo.requestFocus();
                }
            }
        });

        HBox invoiceLabel = new HBox();
        invoiceLabel.getChildren().addAll(lbInvoiceNo, rqInvoiceNo);
        invoiceLabel.setAlignment(Pos.CENTER_LEFT);
        HBox invoiceCombine = new HBox();
        invoiceCombine.getChildren().addAll(invoiceLabel, tfInvoiceNo);

        Label lbInvoiceDate = createLabel("Invoice Date");
        Label rqInvoiceDate = createLabelWithRequired();
//        lbInvoiceDate.getStyleClass().add("cust-lbl");
        TextField dpInvoiceDate = createTextField("DD/MM/YYYY");
        dpInvoiceDate.getStyleClass().add("isRequired");
        DateValidator.applyDateFormat(dpInvoiceDate);
        CommonFunctionalUtils.restrictDateFormat(dpInvoiceDate);

        HBox invoiceDateCombine = new HBox();
        invoiceDateCombine.getChildren().addAll(lbInvoiceDate, rqInvoiceDate, dpInvoiceDate);


//        dpInvoiceDate.focusedProperty().addListener((obs, old, nw) -> {
//            System.out.println("nw Getting >> " + nw);
//            if (!nw) {
//                if (dpInvoiceDate.getText().isEmpty()) {
//                    dpInvoiceDate.requestFocus();
//                }
//                String regDateStr = dpInvoiceDate.getText();
//                if (!regDateStr.isEmpty()) {
//                    LocalDate currentDate = LocalDate.now();
//                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
////                String formattedCurrentDate = currentDate.format(formatter);
//                    LocalDate regDate = LocalDate.parse(regDateStr, formatter);
////                System.out.println("register date --> " + regDateStr);
////                System.out.println("currentDate --> " + formattedCurrentDate);
//                    if (regDate.isAfter(currentDate)) {
//                        AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, "Invoice date should not be greater than today's date", input -> {
//                            dpInvoiceDate.requestFocus();
//                        });
//                    }
//                }
//                if(dpInvoiceDate.getText().isEmpty()){
//                    dpInvoiceDate.requestFocus();
//                }else{
//                    String regDateStr = dpInvoiceDate.getText();
//                    LocalDate currentDate = LocalDate.now();
//                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
//                    LocalDate regDate = LocalDate.parse(regDateStr, formatter);
//                    if (regDate.isAfter(currentDate)) {
//                        AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, "Invoice date should not be greater than today's date", input -> {
//                            dpInvoiceDate.requestFocus();
//                        });
//                    }
//                }
//
//
//
//            }
//        });

//        dpInvoiceDate.addEventFilter(KeyEvent.KEY_PRESSED, e->{
////            System.out.println("Hellllo I am Here ");
//            if(e.getCode() == KeyCode.ENTER){
//                if(dpInvoiceDate.getText().isEmpty()){
//                    dpInvoiceDate.requestFocus();
//                }else{
//                    String regDateStr = dpInvoiceDate.getText();
//                    LocalDate currentDate = LocalDate.now();
//                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
//                    LocalDate regDate = LocalDate.parse(regDateStr, formatter);
//                    if (regDate.isAfter(currentDate)) {
//                        AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, "Invoice date should not be greater than today's date", input -> {
//                            Platform.runLater(()->{
//                                dpInvoiceDate.requestFocus();
//                            });
//                        });
//                    }
//                }
//            }
//                e.consume();
//        });


        Label lbDueDate = createLabel("Due Date");
//        lbDueDate.getStyleClass().add("cust-lbl");
        CommonFunctionalUtils.restrictTextFieldToDigitsAndDateFormat(dpInvoiceDate);
        TextField dpDueDate = createTextField("DD/MM/YYYY");

        HBox dueDateCombine = new HBox();
        dueDateCombine.getChildren().addAll(lbDueDate, dpDueDate);

        DateValidator.applyDateFormat(dpDueDate);
        CommonFunctionalUtils.restrictDateFormat(dpDueDate);

//        dpDueDate.focusedProperty().addListener((obs, old, nw) -> {
//            System.out.println("nw" + nw);
//            if (!nw) {
////                if (dpDueDate.getText().isEmpty()) {
////                    dpDueDate.requestFocus();
////                }
//                String regDateStr = dpDueDate.getText();
//                String dpInvoice = dpInvoiceDate.getText();
//                if (!regDateStr.isEmpty() && !dpInvoice.isEmpty()) {
//                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
////                String formattedCurrentDate = currentDate.format(formatter);
//                    LocalDate regDate = LocalDate.parse(regDateStr, formatter);
//                    LocalDate currentDate = LocalDate.parse(dpInvoice, formatter);
////                System.out.println("register date --> " + regDateStr);
////                System.out.println("currentDate --> " + formattedCurrentDate);
//                    if (regDate.isBefore(currentDate)) {
//                        AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, "Expiry date should be greater Invoice Date", input -> {
//                            dpDueDate.requestFocus();
//                        });
//                    }
//                }
//            }
//        });

//        dpDueDate.addEventFilter(KeyEvent.KEY_PRESSED, e->{
////            System.out.println("Hellllo I am Here ");
//            if(e.getCode() == KeyCode.ENTER){
//                if(dpDueDate.getText().isEmpty()){
//                    dpDueDate.requestFocus();
//                }else{
//                    String regDateStr = dpDueDate.getText();
//                    String dpInvoice = dpInvoiceDate.getText();
//                    if (!regDateStr.isEmpty() && !dpInvoice.isEmpty()) {
//                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
//                        LocalDate regDate = LocalDate.parse(regDateStr, formatter);
//                        LocalDate currentDate = LocalDate.parse(dpInvoice, formatter);
//                        if (regDate.isBefore(currentDate)) {
//                            AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, "Invoice date should not be greater than today's date", input -> {
//                                Platform.runLater(()->{
//                                    dpDueDate.requestFocus();
//                                });
//                            });
//                        }
//
//                    }
//                }
//            }
//                e.consume();
//        });

        Label lbDueDays = createLabel("Due Days");
//        lbDueDays.getStyleClass().add("cust-lbl");
        TextField tfDueDays = createTextField("Due Days");
        tfDueDays.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                tfDueDays.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });

        CommonFunctionalUtils.restrictTextFieldToDigitsAndDateFormat(dpDueDate);

        HBox dueDaysCombine = new HBox();
        dueDaysCombine.getChildren().addAll(lbDueDays, tfDueDays);
//        tfInvoiceNo.setOnKeyPressed(event -> {
//                    if (event.isShiftDown() && event.getCode() == KeyCode.TAB) {
//                        Platform.runLater(() -> {
//                            tfDueDays.requestFocus();
//                        });
//                    }
//        });


        Label lbBillAmount = createLabel("Bill Amount");
        Label rqBillAmount = createLabelWithRequired();
//        lbBillAmount.getStyleClass().add("cust-lbl");
        TextField tfBillAmount = createTextField("Bill Amount");
        tfBillAmount.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) {
                if (tfBillAmount.getText().isEmpty()) {
                    tfBillAmount.requestFocus();
                }
            }
        });

        HBox billAmtCombine = new HBox();
        billAmtCombine.getChildren().addAll(lbBillAmount, rqBillAmount, tfBillAmount);

        Label lbPaidAmount = createLabel("Paid Amount");
//        lbPaidAmount.getStyleClass().add("cust-lbl");
        TextField tfPaidAmount = createTextField("Paid Amount");
        tfPaidAmount.setText("0");

        HBox openingPaidAmt = new HBox();
        openingPaidAmt.getChildren().addAll(lbPaidAmount, tfPaidAmount);

        Label lbBalanceAmount = createLabel("Balance Amount");
//        lbBalanceAmount.getStyleClass().add("cust-lbl");
        TextField tfBalanceAmount = createTextField("Balance Amount");

        HBox balanceAmt = new HBox();
        balanceAmt.getChildren().addAll(lbBalanceAmount, tfBalanceAmount);

        ObservableList<BalanceType> lstOpeningBalanceTypes = FXCollections.observableArrayList(Globals.getAllBalanceType());
        Label lbOBType = createLabel("Type");
        EventHandler<ActionEvent> handleGstType = event -> {
            ComboBox<T> comboBox = (ComboBox<T>) event.getSource();
        };
        ComboBox<BalanceType> OpeningBalanceTypeComboBox = createComboBox("Type", handleGstType);

        OpeningBalanceTypeComboBox.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.SPACE && !OpeningBalanceTypeComboBox.isShowing()) {
                OpeningBalanceTypeComboBox.show();
                event.consume(); // Consume the event to prevent other actions
            }
        });
        OpeningBalanceTypeComboBox.setConverter(new StringConverter<BalanceType>() {
            @Override
            public String toString(BalanceType balanceType) {
                return balanceType != null ? balanceType.getType() : "";
            }

            @Override
            public BalanceType fromString(String s) {
                return null;
            }
        });
        OpeningBalanceTypeComboBox.setItems(lstOpeningBalanceTypes);
        OpeningBalanceTypeComboBox.setValue(OpeningBalanceTypeComboBox.getItems().get(0));
        HBox openTypeCombine = new HBox();
        openTypeCombine.getChildren().addAll(lbOBType, OpeningBalanceTypeComboBox);

//        AutoCompleteBox autoCompleteBox = new AutoCompleteBox(OpeningBalanceTypeComboBox, initialIndex);
        TableView<OBListDTO> tableView = new TableView();
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tableView.setPrefHeight(GlobalTranx.TranxCalculatePerReverse(height, 80.0));
        tableView.setMaxHeight(GlobalTranx.TranxCalculatePerReverse(height, 80.0));
        tableView.setMinHeight(GlobalTranx.TranxCalculatePerReverse(height, 80.0));
        tableView.getStyleClass().add("cust-tbl");
        TableColumn<OBListDTO, String> tbc1InvoiceNo = new TableColumn<>("Invoice No");
        TableColumn<OBListDTO, String> tbc2InvoiceAmt = new TableColumn<>("Invoice Amount");
        TableColumn<OBListDTO, Void> tbc3Type = new TableColumn<>("Type");
        TableColumn<OBListDTO, String> tbc4BillDate = new TableColumn<>("Bill Date");
        TableColumn<OBListDTO, String> tbc5PaidAmount = new TableColumn<>("Paid Amount");
        TableColumn<OBListDTO, String> tbc6Balance = new TableColumn<>("Balance");
        TableColumn<OBListDTO, String> tbc7DueDate = new TableColumn<>("Due Date");
        TableColumn<OBListDTO, String> tbc8DueDays = new TableColumn<>("Due Days");
        TableColumn<OBListDTO, Void> tbc9Action = new TableColumn<>("Action");

        tableView.getColumns().addAll(tbc1InvoiceNo, tbc2InvoiceAmt, tbc3Type, tbc4BillDate, tbc5PaidAmount, tbc6Balance, tbc7DueDate, tbc8DueDays, tbc9Action);

        tbc1InvoiceNo.setCellValueFactory(new PropertyValueFactory<>("invoiceNo"));
        tbc2InvoiceAmt.setCellValueFactory(new PropertyValueFactory<>("billAmt"));
        tbc3Type.setCellValueFactory(new PropertyValueFactory<>("type"));
        tbc4BillDate.setCellValueFactory(new PropertyValueFactory<>("invoiceDate"));
        tbc5PaidAmount.setCellValueFactory(new PropertyValueFactory<>("invoicePaidAmt"));
        tbc6Balance.setCellValueFactory(new PropertyValueFactory<>("invoiceBalAmt"));
        tbc7DueDate.setCellValueFactory(new PropertyValueFactory<>("dueDate"));
        tbc8DueDays.setCellValueFactory(new PropertyValueFactory<>("dueDays"));
        tbc9Action.setCellFactory(param -> {
            final TableCell<OBListDTO, Void> cell = new TableCell<>() {
                private ImageView delImg = Globals.getDelImage();
                private ImageView edtImg = Globals.getEdtImage();

                {
                    delImg.setFitHeight(20.0);
                    delImg.setFitWidth(20.0);
                    edtImg.setFitHeight(20.0);
                    edtImg.setFitWidth(20.0);
                }

                private final Button actionButton = new Button("", delImg);
                private final Button edtButton = new Button("", edtImg);

                {
                    actionButton.setOnAction(event -> {
                        tbllstOB.remove(getIndex());
                        tfInvoiceNo.requestFocus();
                    });

                    edtButton.setOnAction(event -> {
                        OBListDTO nv = tbllstOB.get(getIndex());
                        if (nv != null) {
                            edtInx[0] = getIndex();
                            tfInvoiceNo.setText(nv.getInvoiceNo() + "");
                            dpInvoiceDate.setText(nv.getInvoiceDate());
                            dpDueDate.setText(nv.getDueDate());
                            tfDueDays.setText(nv.getDueDays() + "");
                            tfBillAmount.setText(nv.getBillAmt() + "");
                            tfPaidAmount.setText(nv.getInvoicePaidAmt() + "0");
                            tfBalanceAmount.setText(nv.getInvoiceBalAmt() + "");
                            lstOpeningBalanceTypes.stream().filter(v -> v.getType().equalsIgnoreCase(nv.getType())).findAny().ifPresent((p) -> {
                                OpeningBalanceTypeComboBox.setValue(p);
                            });
//                            OpeningBalanceTypeComboBox.setValue();
                            tfInvoiceNo.requestFocus();
                            tbllstOB.remove(getIndex());
                        }
                    });
                }

                HBox hbActions = new HBox();

                {
                    hbActions.getChildren().add(edtButton);
                    hbActions.getChildren().add(actionButton);
                    hbActions.setSpacing(10.0);
                }

                @Override
                protected void updateItem(Void item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                    } else {
                        setGraphic(hbActions);
                    }
                }
            };
            return cell;
        });
        tableView.setOnMouseClicked(mouseEvent -> {
            if (mouseEvent.getClickCount() == 2) {
                OBListDTO SelectedValue = tableView.getSelectionModel().getSelectedItem();
                if (SelectedValue != null) {
                    Integer idx = tableView.getSelectionModel().getSelectedIndex();
                    tfInvoiceNo.setText("" + SelectedValue.getInvoiceNo());
                    dpInvoiceDate.setText(SelectedValue.getInvoiceDate());
                    dpDueDate.setText(SelectedValue.getDueDate());
                    tfDueDays.setText("" + SelectedValue.getDueDays());
                    tfBillAmount.setText("" + SelectedValue.getBillAmt());
                    tfPaidAmount.setText("" + SelectedValue.getInvoicePaidAmt());
                    tfBalanceAmount.setText("" + SelectedValue.getInvoiceBalAmt());
                    lstOpeningBalanceTypes.stream().filter(v -> v.getType().equalsIgnoreCase(SelectedValue.getType())).findAny().ifPresent((p) -> {
                        OpeningBalanceTypeComboBox.setValue(p);
                    });
//                            OpeningBalanceTypeComboBox.setValue();
                    tfInvoiceNo.requestFocus();
                    tbllstOB.remove((int) idx);
                }
            }
        });

        tableView.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                OBListDTO SelectedValue = tableView.getSelectionModel().getSelectedItem();
                if (SelectedValue != null) {
                    Integer idx = tableView.getSelectionModel().getSelectedIndex();

                    tfInvoiceNo.setText("" + SelectedValue.getInvoiceNo());
                    dpInvoiceDate.setText(SelectedValue.getInvoiceDate());
                    dpDueDate.setText(SelectedValue.getDueDate());
                    tfDueDays.setText("" + SelectedValue.getDueDays());
                    tfBillAmount.setText("" + SelectedValue.getBillAmt());
                    tfPaidAmount.setText("" + SelectedValue.getInvoicePaidAmt());
                    tfBalanceAmount.setText("" + SelectedValue.getInvoiceBalAmt());
                    lstOpeningBalanceTypes.stream().filter(v -> v.getType().equalsIgnoreCase(SelectedValue.getType())).findAny().ifPresent((p) -> {
                        OpeningBalanceTypeComboBox.setValue(p);
                    });
                    tbllstOB.remove(idx);
                }
            }
        });
        tableView.setItems(tbllstOB);


        invoiceCombine.setAlignment(Pos.CENTER_LEFT);
        invoiceDateCombine.setAlignment(Pos.CENTER_LEFT);
        dueDateCombine.setAlignment(Pos.CENTER_LEFT);
        dueDaysCombine.setAlignment(Pos.CENTER_LEFT);


        billAmtCombine.setAlignment(Pos.CENTER_LEFT);
        openingPaidAmt.setAlignment(Pos.CENTER_LEFT);
        balanceAmt.setAlignment(Pos.CENTER_LEFT);
        openTypeCombine.setAlignment(Pos.CENTER_LEFT);

        hbox_1.getChildren().addAll(lbOpeningBalance, lbOpeningBalanceVal, lbSumOfBanlance, lbSumOfBanlanceVal, lbRemaningAmount, lbRemaningAmountVal);
        hbox_1.getStyleClass().add("opening-hbox-style-upper");
        hbox_2.getChildren().addAll(invoiceCombine, invoiceDateCombine, dueDateCombine, dueDaysCombine);
        hbox_2.setPadding(new Insets(8, 8, 4, 8));
        hbox_2.getStyleClass().add("cust-back-blue");
        hbox_3.getChildren().addAll(billAmtCombine, openingPaidAmt, balanceAmt, openTypeCombine);
        hbox_3.setPadding(new Insets(4, 8, 8, 8));
        hbox_3.getStyleClass().add("cust-back-blue");

        //for spacing
        double height1 = Screen.getPrimary().getBounds().getHeight();
        double width1 = Screen.getPrimary().getBounds().getWidth();
        if (width1 >= 992 && width1 <= 1024) {
            invoiceCombine.setSpacing(6);
            invoiceDateCombine.setSpacing(6);
            dueDateCombine.setSpacing(6);
            dueDaysCombine.setSpacing(6);

            billAmtCombine.setSpacing(6);
            openingPaidAmt.setSpacing(6);
            balanceAmt.setSpacing(6);
            openTypeCombine.setSpacing(6);
            hbox_1.setSpacing(10);
            hbox_2.setSpacing(10);
            hbox_3.setSpacing(10);
        } else if (width1 >= 1025 && width1 <= 1280) {
            invoiceCombine.setSpacing(6);
            invoiceDateCombine.setSpacing(6);
            dueDateCombine.setSpacing(6);
            dueDaysCombine.setSpacing(6);

            billAmtCombine.setSpacing(6);
            openingPaidAmt.setSpacing(6);
            balanceAmt.setSpacing(6);
            openTypeCombine.setSpacing(6);
            hbox_1.setSpacing(12);
            hbox_2.setSpacing(12);
            hbox_3.setSpacing(12);
        } else if (width1 >= 1281 && width1 <= 1368) {
            invoiceCombine.setSpacing(6);
            invoiceDateCombine.setSpacing(6);
            dueDateCombine.setSpacing(6);
            dueDaysCombine.setSpacing(6);

            billAmtCombine.setSpacing(6);
            openingPaidAmt.setSpacing(6);
            balanceAmt.setSpacing(6);
            openTypeCombine.setSpacing(6);
            hbox_1.setSpacing(12);
            hbox_2.setSpacing(12);
            hbox_3.setSpacing(12);
        } else if (width1 >= 1369 && width1 <= 1400) {
            invoiceCombine.setSpacing(8);
            invoiceDateCombine.setSpacing(8);
            dueDateCombine.setSpacing(8);
            dueDaysCombine.setSpacing(8);

            billAmtCombine.setSpacing(8);
            openingPaidAmt.setSpacing(8);
            balanceAmt.setSpacing(8);
            openTypeCombine.setSpacing(8);
            hbox_1.setSpacing(15.0);
            hbox_2.setSpacing(15.0);
            hbox_3.setSpacing(15.0);
        } else if (width1 >= 1401 && width1 <= 1440) {
            invoiceCombine.setSpacing(8);
            invoiceDateCombine.setSpacing(8);
            dueDateCombine.setSpacing(8);
            dueDaysCombine.setSpacing(8);

            billAmtCombine.setSpacing(8);
            openingPaidAmt.setSpacing(8);
            balanceAmt.setSpacing(8);
            openTypeCombine.setSpacing(8);
            hbox_1.setSpacing(15.0);
            hbox_2.setSpacing(15.0);
            hbox_3.setSpacing(15.0);
        } else if (width1 >= 1441 && width1 <= 1680) {
            invoiceCombine.setSpacing(10);
            invoiceDateCombine.setSpacing(10);
            dueDateCombine.setSpacing(10);
            dueDaysCombine.setSpacing(10);

            billAmtCombine.setSpacing(10);
            openingPaidAmt.setSpacing(10);
            balanceAmt.setSpacing(10);
            openTypeCombine.setSpacing(10);
            hbox_1.setSpacing(20.0);
            hbox_2.setSpacing(20.0);
            hbox_3.setSpacing(20.0);
        } else if (width1 >= 1681 && width1 <= 1920) {
            invoiceCombine.setSpacing(10);
            invoiceDateCombine.setSpacing(10);
            dueDateCombine.setSpacing(10);
            dueDaysCombine.setSpacing(10);

            billAmtCombine.setSpacing(10);
            openingPaidAmt.setSpacing(10);
            balanceAmt.setSpacing(10);
            openTypeCombine.setSpacing(10);

            hbox_1.setSpacing(20.0);
            hbox_2.setSpacing(20.0);
            hbox_3.setSpacing(20.0);
        }


        //?Calculate the remaining amount
//        Double sumBillAmt = tableView.getItems().stream().mapToDouble(v -> v.getBillAmt()).sum();
        {
            double sumPaidAmt = tableView.getItems().stream().mapToDouble(v -> v.getInvoicePaidAmt()).sum();
            double sumRemaingAmt = tableView.getItems().stream().mapToDouble(v -> v.getInvoiceBalAmt()).sum();
            lbSumOfBanlanceVal.setText(sumPaidAmt + "");
            lbRemaningAmountVal.setText(sumRemaingAmt + "");
        }
        tfPaidAmount.focusedProperty().addListener((obs, old, nw) -> {
            if (!nw) {
                if (!tfBillAmount.getText().isEmpty() && !tfPaidAmount.getText().isEmpty()) {
                    double billAmt = Double.parseDouble(tfBillAmount.getText());
                    double paidAmt = Double.parseDouble(tfPaidAmount.getText());
                    double remAmt = billAmt - paidAmt;
                    tfBalanceAmount.setText(remAmt + "");
                }
            }
        });
        HBox hbMi = new HBox();
        VBox vmi = new VBox();
        vmi.getStyleClass().add("cust-back-blue");
        VBox vBox = new VBox();
        HBox hbox_add_clear = new HBox();
        Button btnAdd = new Button("Add");
        btnAdd.setFocusTraversable(true);
        Button btnClear = new Button("Clear");
        btnClear.setFocusTraversable(true);
        btnAdd.setId("submit-btn");
        btnClear.setId("cancel-btn");
        btnAdd.setMaxWidth(80);
        btnClear.setMaxWidth(80);
//        btnAdd.setOnAction((e) -> {
//            if (edtInx[0] > -1) {
//                tbllstOB.add(new OBListDTO(0, tfInvoiceNo.getText(), dpInvoiceDate.getText(), dpDueDate.getText(),
//                        Integer.parseInt(tfDueDays.getText()), Double.parseDouble(tfBillAmount.getText()),
//                        Double.parseDouble(tfPaidAmount.getText()), Double.parseDouble(tfBalanceAmount.getText()),
//                        OpeningBalanceTypeComboBox.getSelectionModel().getSelectedItem().getType()));
//                edtInx[0] = -1;
//                edtId[0] = 0;
//                Double sumPaidAmt = tableView.getItems().stream().mapToDouble(v -> v.getInvoicePaidAmt()).sum();
//                double sumRemaingAmt = tableView.getItems().stream().mapToDouble(v -> v.getInvoiceBalAmt()).sum();
//                lbSumOfBanlanceVal.setText(sumPaidAmt + "");
//                lbRemaningAmountVal.setText(sumRemaingAmt + "");
//            } else {
//                tbllstOB.add(new OBListDTO(0, tfInvoiceNo.getText(), dpInvoiceDate.getText(), dpDueDate.getText(),
//                        tfDueDays.getText().isEmpty() ? 0 : Integer.parseInt(tfDueDays.getText()), Double.parseDouble(tfBillAmount.getText()),
//                        tfPaidAmount.getText().isEmpty() ? 0 : Double.parseDouble(tfPaidAmount.getText()), tfBalanceAmount.getText().isEmpty() ? 0 : Double.parseDouble(tfBalanceAmount.getText()),
//                        OpeningBalanceTypeComboBox.getSelectionModel().getSelectedItem().getType()));
//                Double sumPaidAmt = tableView.getItems().stream().mapToDouble(v -> v.getInvoicePaidAmt()).sum();
//                double sumRemaingAmt = tableView.getItems().stream().mapToDouble(v -> v.getInvoiceBalAmt()).sum();
//                lbSumOfBanlanceVal.setText(sumPaidAmt + "");
//                lbRemaningAmountVal.setText(sumRemaingAmt + "");
//            }
//            //! Clear the fields
//            tfInvoiceNo.setText("");
//            dpInvoiceDate.clear();
//            dpDueDate.clear();
//            tfDueDays.setText("");
//            tfBillAmount.setText("");
//            tfPaidAmount.setText("0");
//            tfBalanceAmount.setText("");
//            OpeningBalanceTypeComboBox.setValue(null);
//        });
//        btnClear.setOnAction((e) -> {
//            //! Clear the fields
//            tfInvoiceNo.setText("");
//            dpInvoiceDate.clear();
//            dpDueDate.clear();
//            tfDueDays.setText("");
//            tfBillAmount.setText("");
//            tfPaidAmount.setText("");
//            tfBalanceAmount.setText("");
//            OpeningBalanceTypeComboBox.setValue(null);
//            Double sumPaidAmt = tableView.getItems().stream().mapToDouble(v -> v.getInvoicePaidAmt()).sum();
//            double sumRemaingAmt = tableView.getItems().stream().mapToDouble(v -> v.getInvoicePaidAmt()).sum();
//            lbSumOfBanlanceVal.setText(sumPaidAmt + "");
//            lbRemaningAmountVal.setText(sumRemaingAmt + "");
//        });

        hbox_add_clear.getChildren().addAll(btnAdd, btnClear);
        hbox_add_clear.setAlignment(Pos.CENTER_RIGHT);
        hbox_add_clear.setSpacing(10.0);
        hbox_add_clear.setPadding(new Insets(8));
        vmi.getChildren().addAll(hbox_2, hbox_3, hbox_add_clear);
        vmi.setSpacing(10.0);
//        VBox.setMargin(vmi, new Insets(20.0));
        hbMi.getChildren().add(vmi);
        vBox.getChildren().addAll(hbox_1, hbMi, tableView);
//        vBox.setSpacing(20.0);
        vbox_top.getChildren().addAll(hbox_top);
//        Double calH = GlobalTranx.TranxCalculatePerReverse(height, 30.0);
//        Double calW = GlobalTranx.TranxCalculatePerReverse(width, 5.0);
        Button btnSubmit = new Button("Submit");
        btnSubmit.setFocusTraversable(true);
        Button btnCancel = new Button("Cancel");
        btnCancel.setFocusTraversable(true);
        btnSubmit.setId("submit-btn");
        btnCancel.setId("cancel-btn");
        btnSubmit.setMaxWidth(80);
        btnCancel.setMaxWidth(80);
        btnSubmit.setOnAction((e) -> {
            double sumRemaingAmt = tableView.getItems().stream().mapToDouble(v -> v.getInvoiceBalAmt()).sum();
//            System.out.println("cal =>" + sumRemaingAmt == openBal);
            System.out.println("OpenBal" + openBal);
            System.out.println("remainngAmt " + sumRemaingAmt);
            if (sumRemaingAmt == openBal) {
                callback.accept(tbllstOB);
                OverlaysEffect.removeOverlaysEffect(stage);
                primaryStage.close();
            } else {
                AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, "Please check the opening bal entry doesn't matches", input -> {
                    GlobalTranx.requestFocusOrDieTrying(tfInvoiceNo, 2);
                });
            }
        });
        btnCancel.setOnAction((e) -> {
//            tbllstOB.clear();
            double sumRemaingAmt = tableView.getItems().stream().mapToDouble(v -> v.getInvoiceBalAmt()).sum();
            if (sumRemaingAmt == openBal) {
                callback.accept(tbllstOB);
                OverlaysEffect.removeOverlaysEffect(stage);
                primaryStage.close();
            } else {
                AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, "Please check the opening bal entry doesn't matches", input -> {
                    GlobalTranx.requestFocusOrDieTrying(tfInvoiceNo, 2);
                });
            }
        });


        btnAdd.setOnAction((e) -> {
            if (edtInx[0] > -1) {
                tbllstOB.add(new OBListDTO(0, tfInvoiceNo.getText(), dpInvoiceDate.getText(), dpDueDate.getText(),
                        Integer.parseInt(tfDueDays.getText()), Double.parseDouble(tfBillAmount.getText()),
                        Double.parseDouble(tfPaidAmount.getText()), Double.parseDouble(tfBalanceAmount.getText()),
                        OpeningBalanceTypeComboBox.getSelectionModel().getSelectedItem().getType()));
                edtInx[0] = -1;
                edtId[0] = 0;
                Double sumPaidAmt = tableView.getItems().stream().mapToDouble(v -> v.getInvoicePaidAmt()).sum();
                double sumRemaingAmt = tableView.getItems().stream().mapToDouble(v -> v.getInvoiceBalAmt()).sum();
                lbSumOfBanlanceVal.setText(sumPaidAmt + "");
                lbRemaningAmountVal.setText(sumRemaingAmt + "");
            } else {
                tbllstOB.add(new OBListDTO(0, tfInvoiceNo.getText(), dpInvoiceDate.getText(), dpDueDate.getText(),
                        tfDueDays.getText().isEmpty() ? 0 : Integer.parseInt(tfDueDays.getText()), Double.parseDouble(tfBillAmount.getText()),
                        tfPaidAmount.getText().isEmpty() ? 0 : Double.parseDouble(tfPaidAmount.getText()), tfBalanceAmount.getText().isEmpty() ? 0 : Double.parseDouble(tfBalanceAmount.getText()),
                        OpeningBalanceTypeComboBox.getSelectionModel().getSelectedItem().getType()));
                Double sumPaidAmt = tableView.getItems().stream().mapToDouble(v -> v.getInvoicePaidAmt()).sum();
                double sumRemaingAmt = tableView.getItems().stream().mapToDouble(v -> v.getInvoiceBalAmt()).sum();
                double sumInvBillAmt = tableView.getItems().stream().mapToDouble(v -> v.getBillAmt()).sum();
//                System.out.println("Amounts >> "+ sumPaidAmt);
//                System.out.println("Amounts 22  >> "+ sumRemaingAmt);
//                System.out.println("Amounts 22  33 >> " + tableView.getItems().toString());
//                System.out.println("Amounts 22 55 >> "+ sumInvBillAmt);
//                System.out.println("Double.parseDouble(lbOpeningBalanceVal.getText())--> " + Double.parseDouble(lbOpeningBalanceVal.getText()));
                lbSumOfBanlanceVal.setText(sumPaidAmt + "");
                lbRemaningAmountVal.setText(sumRemaingAmt + "");
                if (sumInvBillAmt != Double.parseDouble(lbOpeningBalanceVal.getText())) {
//                    System.out.println("Hello here >. ");
                    tfInvoiceNo.requestFocus();
                } else {
                    btnSubmit.requestFocus();
                }
            }

//            borderPane.addEventFilter(KeyEvent.KEY_PRESSED, (KeyEvent event) -> {
//                if (event.getCode() == KeyCode.ENTER) {
//                    if (event.getTarget() instanceof Button btnFocus) {
//                        if (btnFocus.getText().equalsIgnoreCase("Add")) {
//                            btnSubmit.requestFocus();
//                        }
//                    }
//                }
//            });

            //! Clear the fields
            tfInvoiceNo.setText("");
            dpInvoiceDate.clear();
            dpDueDate.clear();
            tfDueDays.setText("");
            tfBillAmount.setText("");
            tfPaidAmount.setText("0");
            tfBalanceAmount.setText("");
//            OpeningBalanceTypeComboBox.setValue(null);
        });
        btnClear.setOnAction((e) -> {
            //! Clear the fields
            tfInvoiceNo.setText("");
            dpInvoiceDate.clear();
            dpDueDate.clear();
            tfDueDays.setText("");
            tfBillAmount.setText("");
            tfPaidAmount.setText("");
            tfBalanceAmount.setText("");
            OpeningBalanceTypeComboBox.setValue(null);
            Double sumPaidAmt = tableView.getItems().stream().mapToDouble(v -> v.getInvoicePaidAmt()).sum();
            double sumRemaingAmt = tableView.getItems().stream().mapToDouble(v -> v.getInvoicePaidAmt()).sum();
            lbSumOfBanlanceVal.setText(sumPaidAmt + "");
            lbRemaningAmountVal.setText(sumRemaingAmt + "");

            borderPane.addEventFilter(KeyEvent.KEY_PRESSED, (KeyEvent event) -> {
                if (event.getCode() == KeyCode.ENTER) {
                    if (event.getTarget() instanceof Button btnFocus) {
                        if (btnFocus.getText().equalsIgnoreCase("Clear")) {
                            btnSubmit.requestFocus();
                        }
                    }
                }
            });
        });


        HBox hboxBottom = new HBox();
        hboxBottom.getChildren().addAll(btnSubmit, btnCancel);
        hboxBottom.setAlignment(Pos.CENTER_RIGHT);
        hboxBottom.setSpacing(10.0);
        hboxBottom.setPadding(new Insets(8));
//        hboxBottom.setMaxHeight(50);
//        hboxBottom.setMinHeight(50);
//        hboxBottom.setPrefHeight(50);
        borderPane.setTop(vbox_top);
        borderPane.setCenter(vBox);
        borderPane.setBottom(hboxBottom);

        // Setting the handler for each node
        setKeyPressedHandler(tfInvoiceNo);
        setKeyPressedHandler(dpInvoiceDate);
        setKeyPressedHandler(dpDueDate);
        setKeyPressedHandler(tfDueDays);
        setKeyPressedHandler(tfBillAmount);
        setKeyPressedHandler(tfPaidAmount);
        setKeyPressedHandler(tfBalanceAmount);
        setKeyPressedHandler(OpeningBalanceTypeComboBox);

        OpeningBalanceTypeComboBox.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.TAB && event.isShiftDown()) {
                tfBalanceAmount.requestFocus();
            } else if (event.getCode() == KeyCode.ENTER) {
                Node nextNode = CommonTraversa.getNextFocusableNode(OpeningBalanceTypeComboBox.getScene());
                nextNode.requestFocus();
            }
        });

//        btnAdd.addEventFilter(KeyEvent.KEY_PRESSED, (KeyEvent event) -> {
//            if (event.getCode() == KeyCode.DOWN){
//                System.out.println("dowunnnnnnnnn");
////                btnSubmit.requestFocus();
//            }
//        });

//        borderPane.addEventFilter(KeyEvent.KEY_PRESSED, (KeyEvent event) -> {
//            if (event.getCode() == KeyCode.DOWN) {
//                if (event.getTarget() instanceof Button btnFocus) {
//                    if(btnFocus.getText().equalsIgnoreCase("Add")){
//                        btnSubmit.requestFocus();
//                    }
//                }
//            }
//        });

//        tblPurRegList.requestFocus();

//        borderPane.addEventFilter(KeyEvent.KEY_PRESSED, (KeyEvent event) -> {
//            if (event.getCode() == KeyCode.TAB) {
//                System.out.println("onTab =>" + event.getTarget());
//                if (event.getTarget() instanceof Button btnFocus) {
//
//                    switch (btnFocus.getText()) {
//                        case "Add":
//                            GlobalTranx.requestFocusOrDieTrying(btnClear, 2);
//                            break;
//                        case "Clear":
//                            GlobalTranx.requestFocusOrDieTrying(tableView, 2);
//                            break;
//                        case "Submit":
//                            GlobalTranx.requestFocusOrDieTrying(btnCancel, 2);
//                            break;
//                        case "Cancel":
//                            GlobalTranx.requestFocusOrDieTrying(tfInvoiceNo, 2);
//                            break;
//                    }
//
//                }
//            }
//            if (event.getCode() == KeyCode.ENTER) {
//                KeyEvent newEvent = new KeyEvent(null, null, KeyEvent.KEY_PRESSED, "", "\t", KeyCode.TAB, event.isShiftDown(), event.isControlDown(), event.isAltDown(), event.isMetaDown());
//                if (event.getTarget() instanceof ComboBox<?>) {
//                    GlobalTranx.requestFocusOrDieTrying(btnAdd, 2);
//                }
//
//                Event.fireEvent(event.getTarget(), newEvent);
//                event.consume();
//            }
//        });
        Scene scene = new Scene(borderPane, width, height);
        scene.setOnKeyPressed((e) -> {
            if (e.getCode() == KeyCode.ESCAPE) {
                double sumRemaingAmt = tableView.getItems().stream().mapToDouble(v -> v.getInvoiceBalAmt()).sum();
                if (sumRemaingAmt == openBal) {
                    callback.accept(tbllstOB);
                    OverlaysEffect.removeOverlaysEffect(stage);
                    primaryStage.close();
                } else {
                    AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, "Please check the opening bal entry doesn't matches", input -> {
                        GlobalTranx.requestFocusOrDieTrying(tfInvoiceNo, 2);
                    });
                }
            }
            e.consume();

        });
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.setAlwaysOnTop(true);
        primaryStage.centerOnScreen();
        primaryStage.setOnShown((v) -> {
            GlobalTranx.requestFocusOrDieTrying(tfInvoiceNo, 2);
        });
//        primaryStage.setY(calH);
//        primaryStage.setX(calW);
        primaryStage.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            if (!isNowFocused) {
                double sumRemaingAmt = tableView.getItems().stream().mapToDouble(v -> v.getInvoiceBalAmt()).sum();
                if (sumRemaingAmt == openBal) {
                    callback.accept(tbllstOB);
                    OverlaysEffect.removeOverlaysEffect(stage);
                    primaryStage.close();
                } else {
                    AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, "Please check the opening bal entry doesn't matches", input -> {
                        GlobalTranx.requestFocusOrDieTrying(tfInvoiceNo, 2);
                    });
                }
            }
        });

//        btnAdd.setOnKeyPressed(actionEvent -> {
//            if (actionEvent.getCode() == KeyCode.LEFT || actionEvent.getCode() == KeyCode.RIGHT) {
//                btnSubmit.requestFocus();
//            }
//            if (actionEvent.getCode() == KeyCode.TAB && actionEvent.isShiftDown()) {
//                OpeningBalanceTypeComboBox.requestFocus();
//            }
//        });
//
//        btnSubmit.setOnKeyPressed(actionEvent -> {
//            if (actionEvent.getCode() == KeyCode.LEFT || actionEvent.getCode() == KeyCode.RIGHT) {
//                btnSubmit.requestFocus();
//            }
//            if (actionEvent.getCode() == KeyCode.TAB && actionEvent.isShiftDown()) {
//                btnClear.requestFocus();
//            }
//        });


//        btnClear.setOnKeyPressed(actionEvent -> {
//            if (actionEvent.getCode() == KeyCode.LEFT || actionEvent.getCode() == KeyCode.RIGHT) {
//                Platform.runLater(() -> {
//                    btnAdd.requestFocus();
//                });
//            }
//            if(actionEvent.getCode() == KeyCode.TAB && actionEvent.isShiftDown()){
//                OpeningBalanceTypeComboBox.requestFocus();
//            }
//        });

        primaryStage.show();

    }

    private Double ReCalculateHeight() {
//        System.out.println("Screen.getPrimary().getVisualBounds().getHeight()" + Screen.getPrimary().getVisualBounds().getHeight());
        return Screen.getPrimary().getVisualBounds().getHeight() - 440;
    }

    private Double ReCalculateWidth() {
//        System.out.println("Screen.getPrimary().getVisualBounds().getWidth()" + Screen.getPrimary().getVisualBounds().getWidth());
        return Screen.getPrimary().getVisualBounds().getWidth() - 400;
    }

}
