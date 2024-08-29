package com.opethic.genivis.utils;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;

public class AutoCompleteBox2<T> implements EventHandler<KeyEvent> {
    private ComboBox<T> comboBox;
    private final ObservableList<T> data;
    private Integer sid;

    public AutoCompleteBox2(final ComboBox<T> comboBox) {
        this.comboBox = comboBox;
        this.data = comboBox.getItems();

        this.doAutoCompleteBox();
    }

    public AutoCompleteBox2(final ComboBox<T> comboBox, Integer sid) {
        this.comboBox = comboBox;
        this.data = comboBox.getItems();
        this.sid = sid;

        this.doAutoCompleteBox();
    }

    private void doAutoCompleteBox() {
        this.comboBox.setEditable(true);
        this.comboBox.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                this.comboBox.hide();
            }
        });

        this.comboBox.getEditor().focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                this.comboBox.hide();
            }
        });

        this.comboBox.getEditor().setOnMouseClicked(event -> {
            if (event.getButton().equals(MouseButton.PRIMARY)) {
                if (event.getClickCount() == 2) {
                    return;
                }
            }
            //this.comboBox.show();
        });

        this.comboBox.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
            moveCaret(this.comboBox.getEditor().getText().length());
        });

        this.comboBox.setOnKeyPressed(t -> comboBox.hide());

        this.comboBox.setOnKeyReleased(this);
        this.comboBox.setOnShowing(event -> {
            Node node = this.comboBox.lookup(".list-view");
            if (node instanceof ListView<?>) {
                ((ListView<?>) node).setMaxHeight(200);
                ((ListView<?>) node).setMinHeight(200);
                ((ListView<?>) node).setPrefHeight(200);
                ((ListView<?>) node).getFocusModel().focus(-1);
            }
        });


        if (this.sid != null)
            this.comboBox.getSelectionModel().select(this.sid);
    }

    @Override
    public void handle(KeyEvent event) {
        if (event.getCode() == KeyCode.UP || event.getCode() == KeyCode.DOWN
                || event.getCode() == KeyCode.RIGHT || event.getCode() == KeyCode.LEFT
                || event.getCode() == KeyCode.HOME || event.getCode() == KeyCode.ENTER
                || event.getCode() == KeyCode.END || event.getCode() == KeyCode.TAB || event.getCode() == KeyCode.SPACE
        ) {
            this.comboBox.hide();
            comboBox.getEditor().setText("");
            return;
        }

        if (event.getCode() == KeyCode.BACK_SPACE) {
            String str = this.comboBox.getEditor().getText();
            /*if (str != null && str.length() > 0) {
                System.out.println("Before -->"+str);
                str = str.substring(0, str.length());
                System.out.println("After -->"+str);
            }*/
            if (str != null) {
                this.comboBox.getEditor().setText(str);
                moveCaret(str.length());
            }
            this.comboBox.getSelectionModel().clearSelection();
        }


        if (event.getCode() == KeyCode.ENTER && comboBox.getSelectionModel().getSelectedIndex() > -1)
            return;

        setItems();
        if (event.getCode() == KeyCode.DOWN) {
            ObservableList<T> items = comboBox.getItems();
            String typedText = comboBox.getEditor().getText().toLowerCase();
            int selectedIndex = comboBox.getSelectionModel().getSelectedIndex();
            for (int i = selectedIndex + 1; i < items.size(); i++) {
                if (items.get(i).toString().toLowerCase().contains(typedText)) {
                    comboBox.getSelectionModel().select(i);
                    return;
                }
            }
        }
    }

    private void setItems() {
        ObservableList<T> list = FXCollections.observableArrayList();


        for (T datum : this.data) {
            String s = this.comboBox.getEditor().getText().toLowerCase();
            if (datum.toString().toLowerCase().contains(s.toLowerCase())) {
                list.add(datum);
            }
        }

        if (list.isEmpty()) {
            this.comboBox.hide();
        } else {
            this.comboBox.setItems(list);
            this.comboBox.show();
        }
    }

    private void moveCaret(int textLength) {
        System.out.println("textLenght--->" + textLength);
        this.comboBox.getEditor().positionCaret(textLength);
    }
}

