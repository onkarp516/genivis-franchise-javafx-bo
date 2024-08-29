package com.opethic.genivis.controller.dialogs;

import com.google.gson.Gson;
import com.opethic.genivis.GenivisApplication;
import com.opethic.genivis.controller.commons.OverlaysEffect;
import com.opethic.genivis.dto.CommonDTO;
import com.opethic.genivis.dto.GstDetailsDTO;
import com.opethic.genivis.dto.ProductContentsMasterDTO;
import com.opethic.genivis.dto.ProductRowDTO;
import com.opethic.genivis.dto.add_charges.AddChargesDTO;
import com.opethic.genivis.dto.add_charges.CustomAddChargesDTO;
import com.opethic.genivis.dto.reqres.catalog.CUnitListDTO;
import com.opethic.genivis.dto.reqres.catalog.CUnitResDTO;
import com.opethic.genivis.dto.reqres.product.Communicator;
import com.opethic.genivis.dto.reqres.product.LevelListDTO;
import com.opethic.genivis.dto.reqres.product.LevelResDTO;
import com.opethic.genivis.dto.reqres.product.TableCellCallback;
import com.opethic.genivis.network.APIClient;
import com.opethic.genivis.utils.AutoCompleteBox;
import com.opethic.genivis.utils.CommonValidationsUtils;
import com.opethic.genivis.utils.Globals;
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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.sound.midi.Soundbank;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class AdditionalCharges {

    public static  <T> void additionalCharges(Stage stage,List<CustomAddChargesDTO> listEdit,Double amount, BiConsumer<String[],List> callback) {
        OverlaysEffect.setOverlaysEffect(stage);
        Stage primaryStage = new Stage();

        primaryStage.initOwner(stage);
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        primaryStage.initModality(Modality.APPLICATION_MODAL);

        //Main Layout................................................................................................................................
        BorderPane borderPane = new BorderPane();
        borderPane.getStylesheets().add(GenivisApplication.class.getResource("/com/opethic/genivis/ui/css/additional_charges.css").toExternalForm());
        borderPane.setStyle("-fx-background-radius: 5; -fx-background-color: white; -fx-border-color: #bfbfc0; -fx-border-radius: 5; -fx-border-width: 0.8;");
        Platform.runLater(() -> borderPane.requestFocus());
        //BorderPan under Top Layout....................................................................................................................
        HBox hbox_top = new HBox();
        hbox_top.setMinWidth(998);
        hbox_top.setMaxWidth(998);
        hbox_top.setPrefWidth(998);
        hbox_top.setMaxHeight(50);
        hbox_top.setMinHeight(50);
        hbox_top.setPrefHeight(50);

        Label popup_title = new Label("Additional Charges");
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
        hbox_top.setStyle("-fx-background-radius: 5 5 0 0; -fx-background-color: #EDF7FF; -fx-border-color: #C7C7CD; -fx-border-width: 0 0 1 0;");


        HBox hbox_bottom2 = new HBox();
        hbox_bottom2.setMinWidth(998);
        hbox_bottom2.setMaxWidth(998);
        hbox_bottom2.setPrefWidth(998);
        hbox_bottom2.setMaxHeight(50);
        hbox_bottom2.setMinHeight(50);
        hbox_bottom2.setPrefHeight(50);
        hbox_bottom2.setSpacing(10);
        hbox_bottom2.setStyle("-fx-background-radius: 0 0 5 5; -fx-background-color: #FFFFFF;");
        hbox_bottom2.setAlignment(Pos.CENTER_RIGHT);

        Button subButton = new Button("Submit");
        subButton.setId("submit-btn");
        hbox_bottom2.setMargin(subButton, new Insets(10, 10, 10, 10));
        hbox_bottom2.getChildren().addAll(subButton);

        TableView<AddChargesDTO> tableView = new TableView();
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tableView.setPrefHeight(359);
        tableView.setMaxHeight(359);
        tableView.setMinHeight(359);
        tableView.setMaxWidth(998);
        tableView.setMinWidth(998);
        tableView.setPrefWidth(998);


        TableColumn<AddChargesDTO, String> cmParticular= new TableColumn<>("Particular");
        // cmParticular.setMaxWidth(550);
        cmParticular.setMinWidth(550);
        cmParticular.setPrefWidth(550);

        TableColumn<AddChargesDTO, String> cmAmount = new TableColumn<>("Amount");
        cmAmount.setMaxWidth(120);
        cmAmount.setMinWidth(120);
        cmAmount.setPrefWidth(120);


        TableColumn<AddChargesDTO, String> cmPercentage = new TableColumn<>("%");
        cmPercentage.setMaxWidth(120);
        cmPercentage.setMinWidth(120);
        cmPercentage.setPrefWidth(120);

        TableColumn<AddChargesDTO, String> cmActions = new TableColumn<>("Actions");
        cmActions.setMaxWidth(120);
        cmActions.setMinWidth(120);
        cmActions.setPrefWidth(120);

        tableView.getColumns().addAll(cmParticular, cmAmount, cmPercentage, cmActions);

        System.out.println(amount);


        if(listEdit!=null && !listEdit.isEmpty()){
            for(CustomAddChargesDTO customAddChargesDTO: listEdit){

                String par = LedgersById.ledgersById(Long.parseLong(customAddChargesDTO.getLedgerId()));

                tableView.getItems().addAll(new AddChargesDTO("0",customAddChargesDTO.getAdditional_charges_details_id(),customAddChargesDTO.getLedgerId(),par,customAddChargesDTO.getAmt(),customAddChargesDTO.getPercent(),""));
            }
        }
        else{
            tableView.getItems().addAll(new AddChargesDTO("0","","","","",""));
        }


        JSONArray acDelDetailsIds = new JSONArray();
        TableCellCallback<JSONObject> callback_for_deleted_rows_ids = item -> {
            acDelDetailsIds.put(item);
        };




        cmParticular.setCellValueFactory(cellData -> cellData.getValue().particularProperty());
        cmParticular.setCellFactory(column -> new  TextFieldTableCellForAddChr("cmParticular",amount,primaryStage));

        cmAmount.setCellValueFactory(cellData -> cellData.getValue().amtProperty());
        cmAmount.setCellFactory(column -> new  TextFieldTableCellForAddChr("cmAmount",amount,primaryStage));

        cmPercentage.setCellValueFactory(cellData -> cellData.getValue().percentProperty());
        cmPercentage.setCellFactory(column -> new TextFieldTableCellForAddChr("cmPercentage",amount,primaryStage));

        cmActions.setCellValueFactory(cellData -> cellData.getValue().actionsProperty());
        cmActions.setCellFactory(column -> new ButtonsTableCell(callback_for_deleted_rows_ids));


        borderPane.setTop(hbox_top);
        borderPane.setCenter(tableView);
        borderPane.setBottom(hbox_bottom2);

        Scene scene = new Scene(borderPane, 1000, 460);

        primaryStage.setScene(scene);
        primaryStage.setTitle("Additional Charges");
        primaryStage.setResizable(false);

        scene.setFill(Color.TRANSPARENT);

        primaryStage.centerOnScreen();

        primaryStage.show();

        subButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {


                List<AddChargesDTO> list = tableView.getItems();

                List<CustomAddChargesDTO> customList = new ArrayList<>();

                for (AddChargesDTO dto : list) {
                    CustomAddChargesDTO customDto = new CustomAddChargesDTO();
                    customDto.setAdditional_charges_details_id(dto.getAdditional_charges_details_id());
                    customDto.setLedgerId(dto.getLedgerId());
                    customDto.setAmt(dto.getAmt());
                    customDto.setPercent(dto.getPercent());
                    customList.add(customDto);
                }

                double add_charges = 0.0;
                for (AddChargesDTO dto : list) {
                    add_charges = add_charges + (dto.getAmt() != null && dto.getAmt() != "" ? Double.parseDouble(dto.getAmt()) : 0.0);
                }

                String[] s = new String[2];
                s[0] = acDelDetailsIds.toString();
                s[1] = String.valueOf(add_charges);
                callback.accept(s,customList);

                primaryStage.close();
                OverlaysEffect.removeOverlaysEffect(stage);
            }
        });

        Platform.runLater(() -> {
            scene.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
                if (event.getCode() == KeyCode.ESCAPE) {
                    primaryStage.close();
                    OverlaysEffect.removeOverlaysEffect(Communicator.stage);
                    event.consume();
                }
            });
        });

        borderPane.addEventFilter(KeyEvent.KEY_PRESSED, (KeyEvent eve) -> {
            if (eve.getCode() == KeyCode.ENTER) {
                if (eve.getTarget() instanceof Button targetButton && targetButton.getText().equalsIgnoreCase("submit")) {
                    System.out.println(targetButton.getText());
                } else {
                    KeyEvent newEvent = new KeyEvent(
                            null,
                            null,
                            KeyEvent.KEY_PRESSED,
                            "",
                            "\t",
                            KeyCode.TAB,
                            eve.isShiftDown(),
                            eve.isControlDown(),
                            eve.isAltDown(),
                            eve.isMetaDown()
                    );
                    Event.fireEvent(eve.getTarget(), newEvent);
                    eve.consume();
                }
            }
        });

    }

}

class TextFieldTableCellForAddChr extends TableCell<AddChargesDTO, String> {
    private TextField textField;
    private String columnName;

    private Double amount;
    private Stage stage;

    public TextFieldTableCellForAddChr(String columnName,Double amount,Stage stage) {
        this.columnName = columnName;
        this.textField = new TextField();
        this.stage = stage;
        this.amount = amount;



        if (columnName.equals("cmAmount")) {
            textField.setOnKeyTyped(event -> {

                String addAmtText = textField.getText();
                if(!textField.getText().isEmpty()) {

                    double disc_amt = Double.parseDouble(addAmtText);
                    double percentage = (disc_amt/amount) * 100;
                    String addChgPer =  String.format("%.2f", percentage);
                    getTableRow().getItem().setPercent(String.valueOf(addChgPer));
                }
                else {
                    getTableRow().getItem().setPercent("");
                }
            });

        }

        if (columnName.equals("cmPercentage")) {



            textField.setOnKeyTyped(event -> {
                String per = textField.getText();
                if(!per.isEmpty()) {
                    double disc_per = Double.parseDouble(per);
                    Double disc_amount  = ( amount* disc_per) / 100;
                    getTableRow().getItem().setAmt(String.valueOf(disc_amount));
                }else {
                    getTableRow().getItem().setAmt("");
                }
            });
        }


        this.textField.setOnAction(event -> commitEdit(textField.getText()));

        this.textField.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) {
                commitEdit(textField.getText());
            }
        });

        this.textField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.TAB) {
                commitEdit(textField.getText());
            }
        });

        if (columnName.equals("cmParticular")) {
            Platform.runLater(() -> {
                getTableView().edit(0, getTableView().getColumns().get(0));
            });
            textField.setPromptText("Particular");
            textField.setEditable(false);

            textField.setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.SPACE) {
                    int current_index = getTableRow().getIndex();
                    SingleInputDialogs.openLedgerPopUp(stage, "Filter", input -> {
                        String ledgerName =  LedgersById.ledgersById(Long.parseLong((String) input[1]));
                        commitEdit(ledgerName);
                        AddChargesDTO addChargesDTO = getTableView().getItems().get(current_index);
                        addChargesDTO.setLedgerId((String) input[1]);
                    },in->{
                        System.out.println("In >> Called >. "+ in);
                        if(in == true){
//                            isLedgerRed = true;
//                            setPurChallDataToProduct();
                        }
                    });
                }
            });

            textField.setOnMouseClicked(event -> {
                SingleInputDialogs.openLedgerPopUp(stage, "Filter", input -> {
                    int current_index = getTableRow().getIndex();
                    String ledgerName =  LedgersById.ledgersById(Long.parseLong((String) input[1]));
                    commitEdit(ledgerName);
                    AddChargesDTO addChargesDTO = getTableView().getItems().get(current_index);
                    addChargesDTO.setLedgerId((String) input[1]);
                },in->{
                    System.out.println("In >> Called >. "+ in);
                    if(in == true){
//                            isLedgerRed = true;
//                            setPurChallDataToProduct();
                    }
                });
            });

        } else if (columnName.equals("cmAmount")) {
            textField.setPromptText("Amount");
        } else if (columnName.equals("cmPercentage")) {
            textField.setPromptText("Percentage");
        }

        textField.setStyle("-fx-background-color: #f6f6f9; -fx-border-radius: 0px; -fx-border-width: 1; -fx-border-color: #f6f6f9;");

        textField.setPrefHeight(38);
        textField.setMaxHeight(38);
        textField.setMinHeight(38);


        textField.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            if (isNowFocused) {
                textField.setStyle("-fx-background-color: #fff9c4; -fx-border-radius: 0px; -fx-border-width: 2; -fx-border-color: #00a0f5;");
            } else {
                textField.setStyle("-fx-background-color: #f6f6f9; -fx-border-radius: 0px; -fx-border-width: 2; -fx-border-color: #f6f6f9;");
            }
        });
    }

    @Override
    public void startEdit() {
        super.startEdit();
        setText(null);
        setGraphic(new VBox(textField));
        textField.requestFocus();
    }

    @Override
    public void cancelEdit() {
        super.cancelEdit();
        setText((String) getItem());
        setGraphic(null);
    }


    @Override
    protected void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);
        if (empty) {
            setGraphic(null);
        } else {
            VBox vbox = new VBox(textField);
            vbox.setAlignment(Pos.CENTER);

            textField.setText(item.toString());
            setGraphic(vbox);
        }
    }

    @Override
    public void commitEdit(String newValue) {
        super.commitEdit(newValue);
        if (columnName.equals("cmParticular")) {
            ( getTableRow().getItem()).setParticular(newValue.isEmpty()?"":newValue);
        }
        else if (columnName.equals("cmAmount")) {
            ( getTableRow().getItem()).setAmt(newValue.isEmpty()?"":newValue);
        }
        else if (columnName.equals("cmPercentage")) {
            ( getTableRow().getItem()).setPercent(newValue.isEmpty()?"":newValue);
        }
    }
}

class ButtonsTableCell extends TableCell<AddChargesDTO, String> {

    private final TableCellCallback<JSONObject> callback2;

    private final Button add;
    private final Button delete;

    public ButtonsTableCell(TableCellCallback<JSONObject> callback2) {

        this.callback2 = callback2;


        this.add = new Button();

        this.delete = new Button();

        ImageView imageView1 = new ImageView(new Image(getClass().getResourceAsStream("/com/opethic/genivis/ui/assets/add.png")));
        imageView1.setFitWidth(22);
        imageView1.setFitHeight(22);
        add.setGraphic(imageView1);
        add.setStyle("-fx-background-color: transparent; -fx-background-radius: 50; -fx-border-radius:50;" +
                "-fx-min-width: 22px; " +
                "-fx-min-height: 22px; " +
                "-fx-max-width: 22px; " +
                "-fx-max-height: 22px;");

        ImageView imageView2 = new ImageView(new Image(getClass().getResourceAsStream("/com/opethic/genivis/ui/assets/sub.png")));
        imageView2.setFitWidth(22);
        imageView2.setFitHeight(22);
        delete.setGraphic(imageView2);
        delete.setStyle("-fx-background-color: transparent; -fx-background-radius: 50; -fx-border-radius:50;" +
                "-fx-min-width: 22px; " +
                "-fx-min-height: 22px; " +
                "-fx-max-width: 22px; " +
                "-fx-max-height: 22px;");

        add.setOnAction(event -> {
            //   ProductRowDTO item = getTableView().getItems().get(getIndex());
            int currentIndex = getTableRow().getIndex();
            getTableView().getItems().add(new AddChargesDTO(String.valueOf(currentIndex+1), "","","","",""));
            System.out.println("Row index: " + currentIndex);
        });




        delete.setOnAction(event -> {

            int indexToRemove = getTableRow().getIndex();
            if(indexToRemove!=0) {
                AddChargesDTO addChargesDTO = getTableView().getItems().get(indexToRemove);
                getTableView().getItems().remove(indexToRemove);
                System.out.println("Row index: " + indexToRemove + " Id: " + addChargesDTO.getAdditional_charges_details_id());


                if(Long.parseLong(addChargesDTO.getAdditional_charges_details_id())>0) {

                    JSONObject obj1 = new JSONObject();
                    obj1.put("del_id", addChargesDTO.getAdditional_charges_details_id());
                    if (callback2 != null) {
                        callback2.call(obj1);
                    }

                }



            }
        });

    }


    @Override
    public void startEdit() {
        super.startEdit();
        setText(null);
        setGraphic(new HBox(add, delete));
    }

    @Override
    public void cancelEdit() {
        super.cancelEdit();
        setText((String) getItem());
        setGraphic(null);
    }

    @Override
    protected void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);
        if (empty) {
            setGraphic(null);
        } else {


            HBox hbox = new HBox();
            hbox.getChildren().addAll(add, delete);
            hbox.setAlignment(Pos.CENTER);
            hbox.setSpacing(5);

            setGraphic(hbox);
        }
    }

    @Override
    public void commitEdit(String newValue) {
        super.commitEdit(newValue);
    }
}

class LedgersById{
    public static String ledgersById(Long id){

        Map<String,String> map = new HashMap<>();
        map.put("id", String.valueOf(id));

        String formData =  Globals.mapToStringforFormData(map);
        HttpResponse<String> response = APIClient.postFormDataRequest(formData,"get_ledgers_by_id");

        String json = response.body();
        JSONObject jsonObject = new JSONObject(json);
        JSONObject responseObj = jsonObject.getJSONObject("response");
        String ledgerName = responseObj.getString("ledger_name");

        return ledgerName;
    }
}
