package com.opethic.genivis.dto.pur_invoice;

import java.util.List;

public interface TableCellCallbackForUnit<UnitForPurInvoice> {
    void call(List<UnitForPurInvoice> item);
}
