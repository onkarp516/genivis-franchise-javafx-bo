package com.opethic.genivis.utils;



import com.opethic.genivis.controller.master.ledger.edit.LedgerEditController;
import com.opethic.genivis.models.master.ledger.*;

import java.util.List;

public class GlobalLedgerEditController {
    private LedgerEditController controller;

    private final static GlobalLedgerEditController INSTANCE = new GlobalLedgerEditController();

    private GlobalLedgerEditController() {
    }

    public static GlobalLedgerEditController getInstance() {
        return INSTANCE;
    }

    public void setController(LedgerEditController controller) {
        this.controller = controller;
    }

    public void fcCallLedgerEditOtherSubmit() {
        this.controller.LedgerEditOtherSubmit();
    }

    public void fcCallLedgerEditTaxSubmit(String taxType) {
        this.controller.LedgerEditTaxSubmit(taxType);
    }

    public void fnCallLedgerEditBankAccSubmit(List<PaymentMode> paymentModeList, Boolean isDefaultBank, Boolean taxable, String GstIn, String BankName, String AccNo, String IFSCCode, String BankBranch) {
        this.controller.LedgerEditBankAccSubmit(paymentModeList, isDefaultBank, taxable, GstIn, BankName, AccNo, IFSCCode, BankBranch);
    }

    public void fnCallLedgerEditSCSubmit(String mailingName, String address, String state, String pinCode, String mobileNo, String whatsappNo, String Email,String licenseNo, String regDate, String businessType, String businessTrade, Boolean isCredit, String creditDays, String applicableFrom, String creditNumBills, String creditBillValue, String panNo, Boolean isGst, List<GstList> lstGstDetails, Boolean isLicense, List<LicenseInfo> lstLicenseInfo, Boolean isBank, List<BankDetailList> lstBankDetails, Boolean isShipping, List<ShippingInfo> lstShippingInfo, Boolean isDepartment, List<DeptInfo> lstDeptInfo,List<Integer> lstRemoveGst,List<Integer> lstRemoveLicense,List<Integer> lstRemoveDept,List<Integer> lstRemoveBank,List<Integer> lstRemoveShipping) {
        this.controller.LedgerEditSCSubmit(mailingName, address, state, pinCode, mobileNo, whatsappNo, Email,licenseNo, regDate, businessType, businessTrade, isCredit, creditDays, applicableFrom, creditNumBills, creditBillValue, panNo, isGst, lstGstDetails, isLicense, lstLicenseInfo, isBank, lstBankDetails, isShipping, lstShippingInfo, isDepartment, lstDeptInfo,lstRemoveGst, lstRemoveLicense,lstRemoveDept, lstRemoveBank,lstRemoveShipping);
    }

    public void fnCallLedgerEditSDSubmit(String mailingName, String address, String state, String pinCode, String mobileNo, String whatsappNo, String Email,String licenseNo, String regDate, String businessType, String businessTrade, Boolean isCredit, String creditDays, String applicableFrom, String creditNumBills, String creditBillValue, String panNo, Boolean isGst, List<GstList> lstGstDetails, Boolean isLicense, List<LicenseInfo> lstLicenseInfo, Boolean isBank, List<BankDetailList> lstBankDetails, Boolean isShipping, List<ShippingInfo> lstShippingInfo, Boolean isDepartment, List<DeptInfo> lstDeptInfo,Boolean isSalesman,String salesManId,String areaId,String route,List<Integer> lstRemoveGst,List<Integer> lstRemoveLicense,List<Integer> lstRemoveDept,List<Integer> lstRemoveBank,List<Integer> lstRemoveShipping) {
        this.controller.LedgerEditSDSubmit(mailingName, address, state, pinCode, mobileNo, whatsappNo, Email,licenseNo, regDate, businessType, businessTrade, isCredit, creditDays, applicableFrom, creditNumBills, creditBillValue, panNo, isGst, lstGstDetails, isLicense, lstLicenseInfo, isBank, lstBankDetails, isShipping, lstShippingInfo, isDepartment, lstDeptInfo,isSalesman,salesManId,areaId, route,lstRemoveGst, lstRemoveLicense,lstRemoveDept, lstRemoveBank,lstRemoveShipping);
    }

    public void fcCallLedgerEditOtherCancel() {
        this.controller.LedgerCreateOtherCancel();
    }
}
