package com.opethic.genivis.dto.pur_invoice;

import java.util.List;

public interface TableCellCallbackForPurInvoice<LevelAForPurInvoice> {
    void call(List<LevelAForPurInvoice> item);
}
