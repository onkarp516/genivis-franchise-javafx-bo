package com.opethic.genivis.controller.tranx_sales.invoice;


import com.opethic.genivis.models.tranx.sales.TranxRow;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.ComboBoxTableCell;

public class DependentComboBoxTableCell<S, T> extends ComboBoxTableCell<S, T> {

    private ObservableList<T> items;

    public DependentComboBoxTableCell(ObservableList<T> items) {
        super(items);
        this.items = items;
    }

    @Override
    public void startEdit() {
        super.startEdit();

        // Access the selected item in another column (e.g., Product) and update ComboBox items
        S rowData = getTableView().getItems().get(getIndex());
        // Assuming Product is a property in your row class

        // Update ComboBox items based on the selected Product

    }

}