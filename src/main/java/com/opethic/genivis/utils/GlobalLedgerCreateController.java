package com.opethic.genivis.utils;

import com.opethic.genivis.controller.master.ledger.create.LedgerCreateController;
import com.opethic.genivis.models.master.ledger.*;

import java.util.List;

public class GlobalLedgerCreateController {
    private LedgerCreateController controller;

    private final static GlobalLedgerCreateController INSTANCE = new GlobalLedgerCreateController();

    private GlobalLedgerCreateController() {
    }

    public static GlobalLedgerCreateController getInstance() {
        return INSTANCE;
    }

    public void setController(LedgerCreateController controller) {
        this.controller = controller;
    }

    public void fcCallLedgerCreateOtherSubmit() {
        this.controller.LedgerCreateOtherSubmit();
    }

    public void fcCallLedgerCreateTaxSubmit(String taxType) {
        this.controller.LedgerCreateTaxSubmit(taxType);
    }

    public void fnCallLedgerCreateBankAccSubmit(List<PaymentMode> paymentModeList, Boolean isDefaultBank, Boolean taxable, String GstIn, String BankName, String AccNo, String IFSCCode, String BankBranch) {
        this.controller.LedgerCreateBankAccSubmit(paymentModeList, isDefaultBank, taxable, GstIn, BankName, AccNo, IFSCCode, BankBranch);
    }

    public void fnCallLedgerCreateSCSubmit(String mailingName, String address, String state, String pinCode, String mobileNo, String whatsappNo, String Email, String licenseNo, String regDate, String businessType, String businessTrade, Boolean isCredit, String creditDays, String applicableFrom, String creditNumBills, String creditBillValue, String panNo, Boolean isGst, List<GstList> lstGstDetails, Boolean isLicense, List<LicenseInfo> lstLicenseInfo, Boolean isBank, List<BankDetailList> lstBankDetails, Boolean isShipping, List<ShippingInfo> lstShippingInfo, Boolean isDepartment, List<DeptInfo> lstDeptInfo) {
        this.controller.LedgerCreateSCSubmit(mailingName, address, state, pinCode, mobileNo, whatsappNo, Email,licenseNo, regDate, businessType, businessTrade, isCredit, creditDays, applicableFrom, creditNumBills, creditBillValue, panNo, isGst, lstGstDetails, isLicense, lstLicenseInfo, isBank, lstBankDetails, isShipping, lstShippingInfo, isDepartment, lstDeptInfo);
    }

    public void fnCallLedgerCreateSDSubmit(String mailingName, String address, String state, String pinCode, String mobileNo, String whatsappNo, String Email,String licenseNo, String regDate, String businessType, String businessTrade, Boolean isCredit, String creditDays, String applicableFrom, String creditNumBills, String creditBillValue, String panNo, Boolean isGst, List<GstList> lstGstDetails, Boolean isLicense, List<LicenseInfo> lstLicenseInfo, Boolean isBank, List<BankDetailList> lstBankDetails, Boolean isShipping, List<ShippingInfo> lstShippingInfo, Boolean isDepartment, List<DeptInfo> lstDeptInfo,Boolean isSalesman,String salesManId,String areaId,String route) {
        this.controller.LedgerCreateSDSubmit(mailingName, address, state, pinCode, mobileNo, whatsappNo, Email,licenseNo, regDate, businessType, businessTrade, isCredit, creditDays, applicableFrom, creditNumBills, creditBillValue, panNo, isGst, lstGstDetails, isLicense, lstLicenseInfo, isBank, lstBankDetails, isShipping, lstShippingInfo, isDepartment, lstDeptInfo,isSalesman,salesManId,areaId,route);
    }

    public void fcCallLedgerCreateOtherCancel() {
        this.controller.LedgerCreateOtherCancel();
    }
}
