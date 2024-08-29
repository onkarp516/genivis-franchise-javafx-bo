package com.opethic.genivis.utils;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Font;
import javafx.stage.Popup;

import java.util.ArrayList;
import java.util.List;


public class SearchField<T> extends TextField {
    private ListView<T> listView;
    private Popup popup;
    private ObservableList<T> items;
    private T selectedItem;

    public SearchField() {
        this.items = FXCollections.observableArrayList();
        this.selectedItem = null;


        this.textProperty().addListener((obs, oldText, newText) -> {
            filterItems(newText);
        });



    }

    public void setItems(ObservableList<T> items) {
        this.items = items;
        listView = new ListView<>(items);
        listView.setMaxHeight(147);
        Platform.runLater(() -> listView.setPrefWidth(this.getWidth()-1.5));
        listView.setItems(items);
        popup = new Popup();
        popup.getContent().add(listView);
        popup.setAutoHide(true);
        listView.setOnMouseClicked(event -> {
            selectedItem = listView.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                this.setText(selectedItem.toString());
                this.requestFocus();
                this.end();
                popup.hide();
            }
        });

        // Handle selection via keyboard (Enter key)
        listView.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                selectedItem = listView.getSelectionModel().getSelectedItem();
                if (selectedItem != null) {
                    this.setText(selectedItem.toString());
                    this.requestFocus();
                    this.end();
                    popup.hide();
                }
            }
        });

        listView.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (!event.isShiftDown() && event.getCode() == KeyCode.TAB || event.getCode() == KeyCode.ENTER) {
                popup.hide();
                Node nextNode = getNextFocusableNode();
                if (nextNode != null) {
                    nextNode.requestFocus();
                }
            } else if (event.isShiftDown() && event.getCode() == KeyCode.TAB) {
                popup.hide();
                Node previousNode = getPreviousFocusableNode();
                if (previousNode != null) {
                    previousNode.requestFocus();
                }
            }
        });

        listView.setCellFactory(list -> new ListCell<T>() {
            @Override
            protected void updateItem(T item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    setText(item.toString());
                    setFont(Font.font(15));
                }
            }
        });

    }

    private Node getNextFocusableNode() {
        List<Node> nodes = new ArrayList<>();
        addDescendants(this.getScene().getRoot(), nodes);
        boolean found = false;
        for (Node node : nodes) {
            if (found && node.isFocusTraversable() && node.isVisible() && node.isManaged()) {
                return node;
            }
            if (node.equals(this)) {
                found = true;
            }
        }
        return null;
    }

    private Node getPreviousFocusableNode() {
        List<Node> nodes = new ArrayList<>();
        addDescendants(this.getScene().getRoot(), nodes);
        Node previousNode = null;
        for (Node node : nodes) {
            if (node.equals(this)) {
                break; // Stop searching once the current node is found
            }
            if (node.isFocusTraversable() && node.isVisible() && node.isManaged()) {
                previousNode = node;
            }
        }
        return previousNode;
    }

    private void addDescendants(Node node, List<Node> nodes) {
        if (node != null) {
            if (node instanceof Parent) {
                ((Parent) node).getChildrenUnmodifiable().forEach(child -> addDescendants(child, nodes));
            }
            nodes.add(node);
        }
    }

    // Method to filter items based on user input
    private void filterItems(String filter) {
        if (!filter.isEmpty()) {
            ObservableList<T> filteredItems = FXCollections.observableArrayList();
            for (T item : items) {
                if (item.toString().toLowerCase().contains(filter.toLowerCase())) {
                    filteredItems.add(item);
                }
            }
            listView.setItems(filteredItems);
            if (!filteredItems.isEmpty()) {
                showPopup();
                listView.getSelectionModel().clearSelection();
                listView.getFocusModel().focus(-1);
            } else {
                popup.hide();
            }
        } else {
            listView.setItems(items);
            showPopup();
            listView.getSelectionModel().clearSelection();
            listView.getFocusModel().focus(-1);
        }
    }

    // Method to show the popup below the TextField
    public void showPopup() {
        Bounds bounds = this.localToScreen(this.getBoundsInLocal());
        if (bounds != null) {
            popup.show(this, bounds.getMinX(), bounds.getMaxY());
        }
        listView.getFocusModel().focus(-1);
        filterAndFocusItem(getText());
    }

    public T getSelectedItem() {
        return selectedItem;
    }
    public ObservableList<T> getItems() {
        return listView.getItems();
    }

    public void filterAndFocusItem(String filter) {
        if (!filter.isEmpty()) {
            for (T item : items) {
                if (item.toString().toLowerCase().contains(filter.toLowerCase())) {
                    listView.getSelectionModel().select(item);
                    listView.scrollTo(item);

                    return;
                }
            }
        }
        listView.getSelectionModel().clearSelection();
    }

}
