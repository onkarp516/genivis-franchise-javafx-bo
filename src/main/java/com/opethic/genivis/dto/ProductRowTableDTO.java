package com.opethic.genivis.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
public class ProductRowTableDTO {

    private String unit;
    private String conv;
    private String mrp;

    private String pur_rate;

    private String rate1;

    private String rate2;

    private String rate3;

    private String opn_stock;

    private String negative;
}
