package com.opethic.genivis.models.master.ledger;

public class UnderLedger {
    public Integer principle_id;
    public String principle_name;
    public Integer ledger_form_parameter_id;
    public String ledger_form_parameter_slug;
    public Integer sub_principle_id;
    public String subprinciple_name;
    public String under_prefix;
    public Integer associates_id;
    public String associates_name;
    public String display_name;
    public String display_id;

    public UnderLedger(int principle_id, String principle_name, int ledger_form_parameter_id, String ledger_form_parameter_slug, int sub_principle_id, String subprinciple_name, String under_prefix, int associates_id, String associates_name, String display_name, String display_id) {
        this.principle_id = principle_id;
        this.principle_name = principle_name;
        this.ledger_form_parameter_id = ledger_form_parameter_id;
        this.ledger_form_parameter_slug = ledger_form_parameter_slug;
        this.sub_principle_id = sub_principle_id;
        this.subprinciple_name = subprinciple_name;
        this.under_prefix = under_prefix;
        this.associates_id = associates_id;
        this.associates_name = associates_name;
        this.display_name = display_name;
        this.display_id = display_id;
    }

    public String getDisplay_id() {
        return display_id;
    }

    public void setDisplay_id(String display_id) {
        this.display_id = display_id;
    }


    public String getDisplay_name() {
        return display_name;
    }

    public void setDisplay_name(String display_name) {
        this.display_name = display_name;
    }


    public int getPrinciple_id() {
        return principle_id;
    }

    public void setPrinciple_id(int principle_id) {
        this.principle_id = principle_id;
    }

    public String getPrinciple_name() {
        return principle_name;
    }

    public void setPrinciple_name(String principle_name) {
        this.principle_name = principle_name;
    }

    public int getLedger_form_parameter_id() {
        return ledger_form_parameter_id;
    }

    public void setLedger_form_parameter_id(int ledger_form_parameter_id) {
        this.ledger_form_parameter_id = ledger_form_parameter_id;
    }

    public String getLedger_form_parameter_slug() {
        return ledger_form_parameter_slug;
    }

    public void setLedger_form_parameter_slug(String ledger_form_parameter_slug) {
        this.ledger_form_parameter_slug = ledger_form_parameter_slug;
    }


    public String getSubprinciple_name() {
        return subprinciple_name;
    }

    public void setSubprinciple_name(String subprinciple_name) {
        this.subprinciple_name = subprinciple_name;
    }



    public String getUnder_prefix() {
        return under_prefix;
    }

    public void setUnder_prefix(String under_prefix) {
        this.under_prefix = under_prefix;
    }

    public int getSub_principle_id() {
        return sub_principle_id;
    }

    public void setSub_principle_id(int sub_principle_id) {
        this.sub_principle_id = sub_principle_id;
    }

    public int getAssociates_id() {
        return associates_id;
    }

    public void setAssociates_id(int associates_id) {
        this.associates_id = associates_id;
    }

    public String getAssociates_name() {
        return associates_name;
    }

    public void setAssociates_name(String associates_name) {
        this.associates_name = associates_name;
    }

    @Override
    public String toString() {
        return "UnderLedger{" +
                "principle_id=" + principle_id +
                ", principle_name='" + principle_name + '\'' +
                ", ledger_form_parameter_id=" + ledger_form_parameter_id +
                ", ledger_form_parameter_slug='" + ledger_form_parameter_slug + '\'' +
                ", sub_principle_id=" + sub_principle_id +
                ", subprinciple_name='" + subprinciple_name + '\'' +
                ", under_prefix='" + under_prefix + '\'' +
                ", associates_id=" + associates_id +
                ", associates_name='" + associates_name + '\'' +
                ", display_name='" + display_name + '\'' +
                ", display_id='" + display_id + '\'' +
                '}';
    }
}
