package com.opethic.genivis.utils;

import com.opethic.genivis.dto.cmp_t_row.BatchWindowTableDTO;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class TableTraversal {

    // final int row = getIndex();
    //final int column = tableView.getColumns().indexOf(getTableColumn());
    public static <T> void next(int row, int column,int position, TableView<T> tableView) {
        TableColumn<T,?> colName = tableView.getColumns().get(column+position);
        tableView.edit(row, colName);
    }

    public static <T> void previous(int row, int column,int position, TableView<T> tableView) {
        TableColumn<T,?> colName = tableView.getColumns().get(Math.abs(column-position));
        tableView.edit(row, colName);
    }


}
