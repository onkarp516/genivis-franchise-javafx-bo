package com.opethic.genivis.dto.utilities;

import javafx.beans.property.SimpleStringProperty;

public class DispatchMngtReadyFDSalesInvoiceDetailsDTO {
    private SimpleStringProperty id;
    private SimpleStringProperty description;
    private SimpleStringProperty batch;
    private SimpleStringProperty qty;
    private SimpleStringProperty price;

    public DispatchMngtReadyFDSalesInvoiceDetailsDTO(
            String id,
            String description,
            String batch,
            String qty,
            String price){
        this.id = new SimpleStringProperty(id);
        this.description = new SimpleStringProperty(description);
        this.batch = new SimpleStringProperty(batch);
        this.qty = new SimpleStringProperty(qty);
        this.price = new SimpleStringProperty(price);
    }

    public String getId() {return id.get();}
    public SimpleStringProperty idProperty() {return id;}
    public void setId(String id) { this.id = new SimpleStringProperty(id);}

    public String getDescription() {return description.get();}
    public SimpleStringProperty descriptionProperty() {return description;}
    public void setDescription(String description) { this.description = new SimpleStringProperty(description);}

    public String getBatch() {return batch.get();}
    public SimpleStringProperty batchProperty() {return batch;}
    public void setBatch(String batch) { this.batch = new SimpleStringProperty(batch);}

    public String getQty() {return qty.get();}
    public SimpleStringProperty qtyProperty() {return qty;}
    public void setQty(String qty) { this.qty = new SimpleStringProperty(qty);}

    public String getPrice() {return price.get();}
    public SimpleStringProperty priceProperty() {return price;}
    public void setPrice(String price) { this.price = new SimpleStringProperty(price);}
}
