package com.opethic.genivis.controller.commons;

import com.opethic.genivis.dto.Person;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyEvent;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;



public class ScrollableTableController implements Initializable {
    @FXML
    public BorderPane apRoot;
    @FXML
    public VBox vBox;

    public TableView<Person> tableView;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        tableView=new TableView<>();
        tableView.setEditable(true);
        TableColumn<Person, String> column1 = new TableColumn<>("Name");
        TableColumn<Person, Number> column2 = new TableColumn<>("Mobile No");
        TableColumn<Person, String> column3 = new TableColumn<>("Address");
        TableColumn<Person, String> column4 = new TableColumn<>("Country");
        TableColumn<Person, Date> column5 = new TableColumn<>("Joining Date");


        Callback<TableColumn<Person, Date>, TableCell<Person, Date>> dateCellFactory
                = (TableColumn<Person, Date> param) -> new DateEditingCell();


        ArrayList<Person> personList=new ArrayList<>();
        for(int i=0;i<10;i++){
            personList.add(new Person("abc"+i,Long.parseLong("123"+i),"Addr"+i,"India"+i,new Date()));
//            personList.add(new Person(new SimpleStringProperty("abc"+i),new SimpleStringProperty("123"+i),new SimpleStringProperty("Addr"+i),new SimpleStringProperty("India"+i)));
        }

        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);

        column1.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getpName()));
        column2.setCellValueFactory(data -> new SimpleLongProperty(data.getValue().getpMobile()));
        column3.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getpAddress()));
        column4.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getpCountry()));
        column5.setCellValueFactory(data -> new SimpleObjectProperty(data.getValue().getDate()));

        column5.setCellFactory(dateCellFactory);


        column5.setOnEditCommit(
                (TableColumn.CellEditEvent<Person, Date> t) -> {
                    ((Person) t.getTableView().getItems()
                            .get(t.getTablePosition().getRow()))
                            .setDate(t.getNewValue());

                });

        tableView.getColumns().addAll(column1,column2,column3,column4,column5);
        tableView.getItems().addAll(personList);
        /*column1.setCellFactory(TextFieldTableCell.forTableColumn());
        column2.setCellFactory(TextFieldTableCell.forTableColumn());
        column3.setCellFactory(TextFieldTableCell.forTableColumn());
        column4.setCellFactory(TextFieldTableCell.forTableColumn());*/

        tableView.getSelectionModel().setCellSelectionEnabled(true);

        tableView.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                TablePosition tablePosition;
                if(keyEvent.getCode().isDigitKey() || keyEvent.getCode().isLetterKey()){
                    tablePosition=tableView.getFocusModel().getFocusedCell();
                    tableView.edit(tablePosition.getRow(),tablePosition.getTableColumn());
                    //tableView.getFocusModel().focusedItemProperty().get().setpName(new SimpleStringProperty(keyEvent.getCharacter()));

                }
            }
        });

        vBox.getChildren().add(tableView);
    }


    private class DateEditingCell extends TableCell<Person, Date> {
        DatePicker datePicker;
        private DateEditingCell(){

        }

        @Override
        public void startEdit() {
//            super.startEdit();
            if (!isEmpty()) {
                super.startEdit();
                createDatePicker();
                setText(null);
                setGraphic(datePicker);
            }
        }

        @Override
        public void cancelEdit() {
            super.cancelEdit();
            setText(getDate().toString());
            setGraphic(null);
        }

        @Override
        protected void updateItem(Date date, boolean empty) {
            super.updateItem(date, empty);
            if (empty) {
                setText(null);
                setGraphic(null);
            } else {
                if (isEditing()) {
                    if (datePicker != null) {
                        datePicker.setValue(getDate());
                    }
                    setText(null);
                    setGraphic(datePicker);
                } else {
                    setText(getDate().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)));
                    setGraphic(null);
                }
            }

        }

        private void createDatePicker() {
            datePicker = new DatePicker(getDate());
            datePicker.setMinWidth(this.getWidth() - this.getGraphicTextGap() * 2);
            datePicker.setOnAction((e) -> {
                System.out.println("Committed: " + datePicker.getValue().toString());
                commitEdit(Date.from(datePicker.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()));
            });
//            datePicker.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
//                if (!newValue) {
//                    commitEdit(Date.from(datePicker.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()));
//                }
//            });
        }

        private LocalDate getDate() {
            return getItem() == null ? LocalDate.now() : getItem().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        }
    }
}
