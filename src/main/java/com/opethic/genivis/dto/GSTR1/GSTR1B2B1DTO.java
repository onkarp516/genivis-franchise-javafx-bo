package com.opethic.genivis.dto.GSTR1;
import javafx.beans.property.SimpleStringProperty;
public class GSTR1B2B1DTO {
    private SimpleStringProperty sr_no;
    private SimpleStringProperty ledger_id;
    private SimpleStringProperty ledger_name;
    private SimpleStringProperty gst_number;
    //    private SimpleStringProperty invoice_date;
    private SimpleStringProperty total_invoices;
    private SimpleStringProperty taxable_amt;
    private SimpleStringProperty total_igst;
    private SimpleStringProperty total_sgst;
    private SimpleStringProperty total_cgst;
    private SimpleStringProperty total_tax;
    private SimpleStringProperty total_amt;

    public GSTR1B2B1DTO(String sr_no, String ledger_id, String ledger_name, String gst_number, String total_invoices, String taxable_amt,
                             String total_igst, String total_sgst,String total_cgst, String total_tax, String total_amt) {

        this.sr_no = new SimpleStringProperty(sr_no);
        this.ledger_id = new SimpleStringProperty(ledger_id);
        this.ledger_name = new SimpleStringProperty(ledger_name);
        this.gst_number = new SimpleStringProperty(gst_number);
        this.total_invoices = new SimpleStringProperty(total_invoices);
        this.taxable_amt = new SimpleStringProperty(taxable_amt);
        this.total_igst = new SimpleStringProperty(total_igst);
        this.total_cgst = new SimpleStringProperty(total_cgst);
        this.total_sgst = new SimpleStringProperty(total_sgst);
        this.total_tax = new SimpleStringProperty(total_tax);
        this.total_amt = new SimpleStringProperty(total_amt);
    }

    public String getSr_no() {
        return sr_no.get();
    }

    public SimpleStringProperty sr_noProperty() {
        return sr_no;
    }

    public void setSr_no(String sr_no) {
        this.sr_no.set(sr_no);
    }

    public String getLedgerId() {return ledger_id.get();}
    public SimpleStringProperty ledger_idProperty() {
        return ledger_id;
    }
    public void setLedgerId(String ledger_id) { this.ledger_id=new SimpleStringProperty(ledger_id);}

        public String getLedgerName() {return ledger_name.get();}

        public SimpleStringProperty ledger_nameProperty() {
            return ledger_name;
        }
        public void setLedgerName(String ledger_name) { this.ledger_name=new SimpleStringProperty(ledger_name);}

        public String getGst_number() {return gst_number.get();}

        public SimpleStringProperty gst_numberProperty() {
           return gst_number;
        }
        public void setGst_number(String gst_number) { this.gst_number=new SimpleStringProperty(gst_number);}

        public String getTotal_invoices() {
            return total_invoices.get();
        }
        public SimpleStringProperty total_invoicesProperty() {
                  return total_invoices;
        }
        public void setTotal_invoices(String total_invoices) {
                this.total_invoices=new SimpleStringProperty(total_invoices);
        }
        public String getTotal_igst() {
            return total_igst.get();
        }
        public SimpleStringProperty total_igstProperty() {
        return total_igst;
        }
        public void setTotal_igst(String total_igst) {
        this.total_igst=new SimpleStringProperty(total_igst);
        }
         public String getTotal_cgst() {
        return total_cgst.get();
         }
         public SimpleStringProperty total_cgstProperty() {
        return total_cgst;
          }

         public void setTotal_cgst(String total_cgst) {
        this.total_cgst=new SimpleStringProperty(total_cgst);
         }

         public String getTotal_sgst() {
        return total_sgst.get();
         }
         public SimpleStringProperty total_sgstProperty() {
        return total_sgst;
          }
          public void setTotal_sgst(String total_sgst) {
        this.total_sgst=new SimpleStringProperty(total_sgst);
          }

        public String getTotal_tax() {
            return total_tax.get();
        }
         public SimpleStringProperty total_taxProperty() {
        return total_tax;
           }
         public void setTotal_tax(String total_tax) {
           this.total_tax=new SimpleStringProperty(total_tax);
         }
        public String getTotal_amt() {
            return total_amt.get();
        }
         public SimpleStringProperty total_amtProperty() {
        return total_amt;
        }

        public void setTotal_amt(String total_amt) {
        this.total_amt=new SimpleStringProperty(total_amt);
        }
        public String getTaxable_amt() {
            return taxable_amt.get();
        }
        public SimpleStringProperty taxable_amtProperty() {
            return taxable_amt;
        }
        public void setTaxable_amt(String taxable_amt) {
            this.taxable_amt=new SimpleStringProperty(taxable_amt);
        }






}
