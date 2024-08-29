package com.opethic.genivis.network;

public class EndPoints {

    //!Master Ledger Related End Points
    public static final String ledgerList = "get_all_ledgers_pagination";
    public static final String underLedgerList = "get_under_list";
    public static final String getBalancingMethods = "get_balancing_methods";
    public static final String createLedgerMaster = "create_ledger_master";
    public static final String editLedgerMaster = "edit_ledger_master";

    public static final String getPaymentMode = "get_payment_mode";
    public static final String getGstType = "get_gst_type";
    public static final String getIndianState = "getIndianState";
    public static final String getLedgersById = "get_ledgers_by_id";
    public static final String tranxLedgerList = "transaction_ledger_list";
    //!Master Ledger Related End Points
    //Common for all
    public static final String CHANGE_PASSWORD_ENDPOINT = "bo/changePassword";
    public static final String GET_ALL_DISTRICT_HEAD_ENDPOINT = "get_all_district_head";
    public static final String GET_COUNTRY_DATA_ENDPOINT = "getIndiaCountry";
    public static final String GET_PINCODE_ENDPOINT = "get_pincode";
    public static final String VALIDATE_PINCODE_ENDPOINT = "validate_pincode";
    public static final String GET_PARENT_HEAD_BY_DH_ENDPOINT = "get_parent_head_by_role";
    //    public static final String GET_TRANX_PRODUCT_LIST = "transaction_product_list_new";
    public static final String GET_TRANX_PRODUCT_LIST = "transaction_product_list_multiunit";
    public static final String GET_TRANX_PRODUCT_LIST_BY_BARCODE = "get_product_list_by_barcode";
    public static final String GET_TRANX_PRODUCT_LIST_BARCODE = "transaction_product_list_by_barcode";
//    public static final String GET_TRANX_PRODUCT_BATCH_LIST = "get_Product_batch";
    public static final String GET_TRANX_PRODUCT_BATCH_LIST = "get_product_whole_stock";
    public static final String GET_TRANX_LEDGER_LIST = "transaction_ledger_list";
    public static final String TRANSACTION_LEDGER_DETAILS = "transaction_ledger_details";
    public static final String TRANSACTION_PRODUCT_DETAILS = "transaction_product_details";
    public static final String PRODUCT_BATCH_DETAILS = "product_batch_details";
    public static final String GET_SUPPLIERLIST_BY_PRODUCTID_ENDPOINT = "get_supplierlist_by_productid";
    public static final String GET_SALES_SUPPLIERLIST_BY_PRODUCTID_ENDPOINT = "get_sales_invoice_supplierlist_by_productid";
    public static final String GET_BATCH_DETAILS_ENDPOINT = "transaction_batch_details";
    public static final String CREATE_BRAND = "create_brand";
    public static final String UPDATE_BRAND = "update_brand";
    public static final String UPDATE_GROUP = "update_group";
    public static final String UPDATE_FORMATION = "update_sub_group";
    public static final String UPDATE_CATEGORY = "update_category";
    public static final String UPDATE_SUB_CATEGORY = "update_sub_category";
    public static final String UPDATE_PACKAGE = "update_packing";
    public static final String UPDATE_UNIT = "update_unit";
    public static final String DELETE_BRANDS = "remove-multiple-brand";
    public static final String DELETE_GROUPS = "remove-multiple-groups";
    public static final String DELETE_SUBGROUPS = "remove-multiple-subgroups";
    public static final String DELETE_CATEGORIES = "remove-multiple-categories";
    public static final String DELETE_SUBCATEGORIES = "remove-multiple-subcategories";
    public static final String DELETE_PACKAGE = "remove-multiple-packages";
    public static final String DELETE_UNITS = "remove-multiple-units";


    //Common for all

    // Masters
    public static final String AUTHENTICATE_ENDPOINT = "authenticate";
    public static final String GET_ROLE_PERMISSION_LIST_ENDPOINT = "get_role_permission_list";
    public static final String GET_MASTER_ACTIONS = "get_master_actions";
    public static final String GET_SYSTEM_ALL_PERMISSIONS_ENDPOINT = "get_systems_all_permissions";
    public static final String GET_ROLE_BY_ID = "get_role_by_id";
    public static final String IMPORT_PRODUCT_ENDPOINT = "import_product";
    public static final String TRANX_SALES_INVOICE_RTN_LAST_RECORD = "get_last_record_creditnote";
    public static final String TRANX_SALES_INVOICE_RTN_LIST_CLIENT_WISE = "list_sales_invoice_clients_wise";
    public static final String IMPORT_STOCK_ENDPOINT = "import_product_stock";
    public static final String GET_PARTNER_COMMISSION_PAYMENT = "get_partner_commission_payment";
    public static final String DELETE_LEDGER = "delete_ledger";
    public static String createUserRole = "create_user_role";
    public static String updateUserRole = "update_role";
    public static String getOutletAreaMaster = "get_outlet_area_master";
    public static String createAreaMaster = "create_area_master";
    public static String updateAreaMaster = "update_area_master";
    public static String deleteAreaHead = "delete_area_head";
    public static final String AREA_MASTER_DELETE_ENDPOINT = "remove_area_master";
    public static String updateSalesmanMaster = "update_salesman_master";
    public static String createSalesmanMaster = "create_salesman_master";
    public static String getOutletSalesmanMaster = "get_outlet_salesman_master";

    public static String removesalesmanmaster = "remove_salesman_master";

    public static String CREATE_COMPANY_MULTIPART = "create_company";
    public static String UPDATE_COMPANY_MULTIPART = "update_company";
    public static String createAreaHead = "create_area_head";
    public static String updateAreaHead = "update_area_head";
    public static String GET_COMPANY_USER_LIST = "get_b_users";
    public static String getCompanyName = "get_companies_super_admin";
    public static String userRoleAccessData = "get_master_actions";
    public static String getUserRolePermissions = "get_role_permissions_by_id";
    public static String getUserRolePermList = "get_role_permission_list";
    public static String createCompanyUser = "register_user";
    public static String updateCompanyUser = "updateUser";
    public static String getEditDataOfCompanyUser = "get_user_by_id";
    public static String getPurchaseInvoiceList = "list_purchase_invoice";

    public static final String GET_PUR_RETURN_LIST = "get_pur_returns_by_outlet";
    //Product
    public static final String PRODUCT_LIST_ENDPOINT = "get_all_product_new";
    public static final String PRODUCT_CREATE_ENDPOINT = "create_new_product";
    public static final String PRODUCT_GET_PACKING_ENDPOINT = "get_packings";
    public static final String PRODUCT_GET_BRAND_ENDPOINT = "get_outlet_brands";
    public static final String PRODUCT_GET_TAX_ENDPOINT = "get_tax_master_by_outlet";
    public static final String PRODUCT_GET_HSN_ENDPOINT = "get_hsn_by_outlet";
    public static final String PRODUCT_CREATE_PKG_ENDPOINT = "create_packing";
    public static final String PRODUCT_CREATE_BRAND_ENDPOINT = "create_brand";
    public static final String PRODUCT_CREATE_HSN_ENDPOINT = "create_hsn";
    public static final String PRODUCT_VALIDATE_HSN_ENDPOINT = "validate_HSN";
    public static final String PRODUCT_VALIDATE_HSN_UPDATE_ENDPOINT = "validate_HSN_update";
    public static final String PRODUCT_GET_DRUGTYPE_ENDPOINT = "get_all_drug_types";
    public static final String PRODUCT_CREATE_DRUGTYPE_ENDPOINT = "create_drug_Type";
    public static final String PRODUCT_GET_CONTENTS_MASTER_ENDPOINT = "get_all_content_master";
    public static final String PRODUCT_GET_CONTENT_PACKINGS_ENDPOINT = "get_all_content_package_master";
    public static final String PRODUCT_GET_CONTENT_TYPE_ENDPOINT = "get_all_content_master_dose";
    public static final String PRODUCT_CREATE_CONTENTS_MASTER_ENDPOINT = "create_content_master";
    public static final String PRODUCT_CREATE_CONTENTS_TYPE_ENDPOINT = "create_content_master_dose";
    public static final String CREATE_NEW_PRODUCT_ENDPOINT = "create_new_product";
    public static final String UPDATE_NEW_PRODUCT_ENDPOINT = "update_product_new";

    public static final String DELETE_PRODUCT_ENDPOINT = "delete_product";

    public static final String GET_PRODUCT_BY_ID_ENDPOINT = "get_product_by_id_new";

    //Company Admin Start
    public static final String GET_COMPANIES_SUPER_ADMIN = "get_companies_super_admin";
    public static final String GET_C_ADMINS = "get_c_admins";
    public static final String REGISTER_USER = "register_user";
    public static final String UPDATE_USER = "updateUser";
    public static final String GET_USER_BY_ID = "get_user_by_id";
    public static final String DISABLE_USER = "disable_user";
    //Company Admin End

    //Franchise EndPoints
    public static final String FRANCHISE_DUPLICATE_ENDPOINT = "validate_franchise_code";
    public static final String FRANCHISE_DUPLICATE_UPDATE_ENDPOINT = "validate_franchise_update";
    public static final String FRANCHISE_CREATE_ENDPOINT = "create_franchise";
    public static final String FRANCHISE_UPDATE_ENDPOINT = "update_franchise";
    public static final String FRANCHISE_LIST_ENDPOINT = "get_all_franchise";
    public static final String FRANCHISE_GET_DATA_BY_ID_ENDPOINT = "get_franchise_by_id";
    //Franchise EndPoints


    //Company List Create Endpoints
    public static final String COMPANY_LIST_ENDPOINT = "get_companies_super_admin";

    // Company Create Endpoints
    public static final String GET_GST_TYPE_ENDPOINT = "get_gst_type";
    public static final String GET_ALL_MASTER_SYSTEM_CONFIG = "get_all_master_system_config";
    public static final String GET_COMPANY_BY_ID = "get_company_by_id";
    public static final String VALIDATE_COMPANY = "validate_company";
    public static final String VALIDATE_COMPANY_UPDATE = "validate_company_update";
    public static final String VALIDATE_COMPANY_USER = "validate_user";
    public static final String VALIDATE_COMPANY_USER_UPDATE = "validate_cadmin_update";


    //Patient Master
    public static final String PATIENT_CREATE_ENDPOINT = "create_patient_master";
    public static final String PATIENT_UPDATE_ENDPOINT = "update_patient_master";
    public static final String PATIENT_LIST_ENDPOINT = "get_all_patient_master";
    public static final String PATIENT_DELETE_ENDPOINT = "remove_patient_master";


    //Doctor Master
    public static final String DOCTOR_CREATE_ENDPOINT = "create_doctor_master";

    public static final String GET_ALL_DOCTOR_MASTER = "get_all_doctor_master";
    public static final String DOCTOR_DELETE_ENDPOINT = "remove_doctor_master";


    //Payment Master

    public static final String PAYMENT_MODE_CREATE_ENDPOINT = "create_payment_mode";
    public static final String PAYMENT_MODE_UPDATE_ENDPOINT = "update_payment_mode";
    public static final String PAYMENT_MODE_DELETE_ENDPOINT = "remove_payment_master";

    public static final String PAYMENT_MODE_LIST_ENDPOINT = "get_payment_mode";

    //TODO : PURCHASE
    public static final String PUR_INV_LEDGER_LIST = "transaction_ledger_list";
    public static final String PUR_ACCOUNTS_LIST = "get_purchase_accounts";
    public static final String VALIDATE_PUR_INV_NUMBER = "validate_purchase_invoices";
    public static final String VALIDATE_PUR_INV_DATE = "checkInvoiceDateIsBetweenFY";


    public static final String VALIDATE_PUR_INV_NUMBER_EDIT = "validate_purchase_invoices_update";

    public static final String CHECK_INV_DATE_IS_BETWEEN_FY = "checkInvoiceDateIsBetweenFY";
    public static final String PUR_INV_GET_LAST_INVOICE = "get_last_invoice_record";
    public static final String GET_PAYMENT_INVOICE_LAST_RECORDS = "get_payment_invoice_last_records";
    public static final String GET_PUR_INVOICE_BY_ID = "get_purchase_invoice_by_id_new";
    public static final String GET_CR_PENDING_BILLS = "get_creditors_pending_bills";
    public static final String CREATE_PUR_INVOICE_END = "create_purchase_invoices";
    public static final String EDIT_PUR_INVOICE_END = "edit_purchase_invoices";
    public static final String GET_LAST_PUR_INV_RECORD = "get_last_invoice_record";




    // Payment AccountEntry
    public static final String CREATE_PAYMENT_ENDPOINT = "create_payments";
    public static final String UPDATE_PAYMENT_ENDPOINT = "update_payments";
    public static final String GET_ALL_PAYMENT_LIST = "get_payment_list_by_outlet";
    public static final String DELETE_PAYMENT = "delete_payment";
    public static final String GET_PAYMENT_BY_ID = "get_payments_by_id";


    // Receipt AccountEntry
    public static final String GET_RECEIPT_INVOICE_LAST_RECORDS = "get_receipt_invoice_last_records";
    public static final String GET_SUNDRY_DEBTORS_INDIRECT_INCOMES = "get_sundry_debtors_indirect_incomes";
    public static final String GET_DEBTORS_PENDING_BILLS = "get_debtors_pending_bills";
    public static final String GET_PAYMENT_MODE = "get_payment_mode";
    public static final String GET_GV_BANK_NAMES = "get_gv_bank_details";

    public static final String createReceipt = "create_receipt";
    public static final String updateReceipt = "update_receipt";
    public static final String getAllReceiptList = "get_receipt_list_by_outlet";
    public static final String deleteReceipt = "delete_receipt";
    public static final String getReceiptById = "get_receipt_by_id";


    //todo: Purchase order Endpoints
    public static final String GET_LAST_PO_INVOICE_RECORD = "get_last_po_invoice_record";
    public static final String GET_PURCHASE_ACCOUNTS = "get_purchase_accounts";
    public static final String PURCHASR_ORDER_CREATE = "create_po_invoices";
    public static final String PURCHASR_ORDER_UPDATE = "edit_pur_order";
    public static final String PURCHASE_ORDER_GET_BY_ID = "get_purchase_order_by_id_new";
    public static final String LIST_OF_ALL_PO_INVOICE_LIST = "list_po_invoice";
    public static final String PURCHASE_ORDER_LIST_DELETE = "delete_purchase_order";
    public static final String VALIDATE_PURCHASE_ORDER = "validate_purchase_order";
    public static final String VALIDATE_PURCHASE_ORDER_UPDATE = "validate_purchase_order_update";


    //?  Sales Quotation
    public static final String SALESQUOTATION_GET_LAST_SALES_QUOTATION_RECORD = "get_last_sales_quotation_record";
    public static final String SALESQUOTATION_GET_SALES_QUOTATION_ACCOUNTS = "get_sales_accounts";
    public static final String SALESQUOTATION_CREATE_SALES_QUOTATION = "create_sales_quotation";
    public static final String SALESQUOTATION_UPDATE_SALES_QUOTATION = "update_sales_quotation";
    public static final String SALESQUOTATION_SALES_QUOTATION_LIST = "list_sales_quotations";

    public static final String ALL_SALES_QUOTATION_LIST = "all_list_sales_quotations";
    public static final String SALESQUOTATION_VALIDATE_SALES_QUOTATION_NO = "validate_sales_quotation";
    public static final String SALESQUOTATION_VALIDATE_SALES_QUOTATIONEDIT_NO = "validate_sales_quotation_update";
    public static final String SALESQUOTATION_VALIDATE_SALES_ORDER_NO = "validate_sales_order";
    public static final String GET_SALES_QUOTATION_BY_ID_NEW = "get_sales_quotation_by_id_new";
    public static final String GET_SALES_QUOTATION_BY_ID = "get_sale_quotation_with_ids";
    public static final String SALES_QUOTATION_DELETE_ENDPOINT = "delete_sales_quotation";
    public static final String GET_SALES_QUOTATION_WITH_IDS_QUOTTOORDER = "get_sale_quotation_with_ids";
    public static final String CREATE_SALES_QUOTATION_TO_ORDER = "create_sales_order_invoice";

    //Sales
    //Sales Order
    public static final String SALESORDER_GET_SALES_ACCOUNTS = "get_sales_accounts";
    public static final String SALESORDER_GET_LAST_SALES_ORDER_RECORD = "get_last_sales_order_record";
    public static final String SALES_ORDER_CREATE_ENDPOINT = "create_sales_order_invoice";
    public static final String SALES_ORDER_LIST_ENDPOINT = "list_sale_orders";
    public static final String SALES_CHALLAN_LIST_ENDPOINT = "list_sale_challan";

    public static final String SALES_ORDER_LIST_ALL_ENDPOINT = "all_list_sale_orders";
    public static final String SALES_ORDER_GET_DATA_BY_ID_ENDPOINT = "get_sales_order_by_id_new";
    public static final String SALES_CHALLAN_GET_DATA_BY_ID_ENDPOINT = "get_sales_challan_by_id_new";

    public static final String SALES_ORDER_EDIT_ENDPOINT = "update_sales_order";
    public static final String SALES_ORDER_DELETE_ENDPOINT = "delete_sales_order";
    public static final String SALES_ORDER_PRINT_ENDPOINT = "get_order_bill_print";
    public static final String SALES_ORDER_NO_DUPLICATE_ENDPOINT = "validate_sales_order";
    public static final String SALES_ORDER_NO_DUPLICATE_UPDATE_ENDPOINT = "validate_sales_order_update";

    //Sales Order

    //Sales Challan
    public static final String SALES_CHALLAN_CREATE_ENDPOINT = "create_sales_challan";
    public static final String SALES_CHALLAN_EDIT_ENDPOINT = "edit_sales_challan";
    public static final String SALES_CHALLAN_DELETE_ENDPOINT = "delete_sales_challan";
    public static final String VALIDATE_SALES_CHALLAN_NUMBER = "validate_sales_challan";

    public static final String TRANX_SALES_CHALLAN_DATA_WITH_IDS = "get_sale_challan_with_ids";
    public static final String VALIDATE_SALES_INV_NUMBER = "validate_sales_invoices";
    public static final String SALES_CHALLAN_TO_INVOICE_CREATE_SLUG = "sales-challan-to-invoice-create";


    //Counter Sale
    public static final String COUNTER_SALE_CREATE_ENDPOINT = "create_counter_sales_invoice";
    public static final String COUNTER_SALE_UPDATE_ENDPOINT = "update_counter_sales_invoices";
    public static final String COUNTER_SALE_GET_ENDPOINT = "get_counter_sales_data";
    public static final String COUNTER_SALE_GET_By_Id_ENDPOINT = "findCounterSalesById";
    public static final String COUNTER_SALE_TO_SALE_INVOICE_GET_By_Id_ENDPOINT = "get_counter_sales_by_no";
    //Consumer Sale
    public static final String CONSUMER_SALE_GET_ENDPOINT = "listOfConsumerSales";
    public static final String CONSUMER_SALE_CREATE_ENDPOINT = "create_sales_comp_invoices";
    public static final String CONSUMER_SALE_UPDATE_ENDPOINT = "update_sales_comp_invoices";
    public static final String CONSUMER_SALE_GET_By_Id_ENDPOINT = "findConsumerSalesById";
    public static final String GET_BANK_PAYMENT_MODE_LIST = "get_bank_payment_mode_list";

    //Account Entry


    //Reports
    /* Trial Balance */
    public static final String TRIAL_BALANCE_LIST = "get_all_ledgers";

    //    Day Book start
    public static final String ACCOUNTS_DAY_BOOK_LIST = "get_all_ledger_tranx_details";
    public static final String ACCOUNTS_DAY_BOOK_EXCEL_EXPORT = "exportToExcelDaybook";
    //    Day Book end

    // Ledger start
    public static final String ACCOUNTS_LEDGER_REPORT1_LIST = "get_all_ledgers";
    public static final String ACCOUNTS_LEDGER_REPORT2_LIST = "get_tranx_detail_of_month";
    public static final String ACCOUNTS_LEDGER_REPORT3_LIST = "get_monthwise_tranx_details";
    public static final String MASTER_LEDGER_TRANX_REPORT_LIST = "get_ledger_tranx_details_report";
    // Ledger end

    // Stocks start
    public static final String STOCKS_STOCK_REPORT1_LIST = "get_allstock_Report";
    public static final String STOCKS_STOCK_REPORT2_LIST = "get_monthwise_whole_stock_prdtranx_details";
    public static final String STOCKS_STOCK_REPORT3_LIST = "get_monthwise_whole_stock_details";

    public static final String STOCKS_STOCK_VALUATION1_LIST = "get_allstockValuation_Report";
    public static final String STOCKS_STOCK_VALUATION2_LIST = "get_monthwise_whole_stock_prdtranx_details";
    // Stocks end

    // Credit Note start
    public static final String TRANSACTION_CREDIT_NOTE1_LIST = "get_creditnote_details";
    public static final String TRANSACTION_CREDIT_NOTE2_LIST = "get_monthwise_creditnote_details";

    // Credit Note end


    // Debit Note start
    public static final String TRANSACTION_DEBIT_NOTE1_LIST = "get_debitnote_details";
    public static final String TRANSACTION_DEBIT_NOTE2_LIST = "get_monthwise_debitnote_details";

    // Debit Note end

    //GSTR
    //GSTR-1 start
    public static final String GSTR1_CREDIT_DEBIT_REGISTERED_9B = "get_GSTR1_CRNoteReg_data";
    public static final String GSTR1_CREDIT_DEBIT_UNREGISTERED_9B = "get_GSTR2_CRNoteUnreg_data";
    //GSTR-3 end

    //GSTR-2 start
    public static final String GSTR2_CREDIT_DEBIT_UNREGISTER = "get_GSTR2_DebNoteUnreg_data";
    public static final String GSTR2_AMMEND_CREDIT_DEBIT_UNREGISTER = "get_GSTR2_DebNoteReg_data";
    //GSTR-2 end

    //GSTR-3B start
    public static final String GSTR3B_OutwardTaxableSupplies_LIST = "get_GSTR3B_outward_tax_suplier_data";
    public static final String GSTR3B_AllOtherITC_LIST = "get_GSTR3B_all_other_itc_data";
    //GSTR-3B end

    //Utility
    public static final String GET_BARCODE_PATH = "get_barcode_home_path";
    public static final String UPDATE_BARCODE_PATH = "update_barcode_home_path";
    //Dispatch Management start
    public static final String DISPATCH_MANAGEMENT_ALL_LIST = "all_disp_manage_list";
    public static final String DISPATCH_MANAGEMENT_PickupSalesOrders_LIST = "get_all_sales_orders";
    public static final String DISPATCH_MANAGEMENT_PickupSalesOrders_DETAILS = "get_sales_order_by_id_new";
    public static final String DISPATCH_MANAGEMENT_PackingSalesChallan_LIST = "all_list_sale_challan";
    public static final String DISPATCH_MANAGEMENT_PackingSalesChallan_DETAILS = "get_sales_challan_by_id_new";
    public static final String DISPATCH_MANAGEMENT_ReadyFDSalesInvoice_LIST = "all_transaction_sale_list";
    public static final String DISPATCH_MANAGEMENT_ReadyFDSalesInvoice_DETAILS = "get_sales_invoice_by_id_new";
    public static final String DISPATCH_MANAGEMENT_ReadyFDSales_STATUS_UPDATE = "sales_status_update_by_id";
    public static final String DISPATCH_MANAGEMENT_DeliveredSalesInvoice_LIST = "list_sale_invoice_delivered";
    public static final String DISPATCH_MANAGEMENT_DeliveredSalesInvoice_DETAILS = "get_sales_invoice_by_id_new";
    public static final String DISPATCH_MANAGEMENT_ReturnedSalesInvoice_LIST = "list_sale_invoice_cancelled";
    public static final String DISPATCH_MANAGEMENT_ReturnedSalesInvoice_DETAILS = "get_sales_invoice_by_id_new";

    //Dispatch Management end

    public static String getAllContentPackageMaster = "get_all_content_package_master";
    public static final String CONTENT_MASTER_DELETE_ENDPOINT = "remove_content_master";

    public static String getAllContentMasterDose = "get_all_content_master_dose";
    public static String ProductListByContentDetails = "productListByContentDetails";
    public static String GetAllCommissionMaster = "get_all_commission_master";
    public static String validateCommissionMaster = "validate_commission_master";
    public static String CreateCommissionMaster = "create_commission_master";

    public static String UpdateCommissionMaster = "update_commission_master";

    public static String getAllContentMaster = "get_all_content_master";
    public static String validateContentMaster = "validate_content_name";
    public static String updateContentMaster = "update_content_master";
    public static String createContentMaster = "create_content_master";
    //Purchse Return
    public static String GET_LEDGER_BILL_LIST = "list_pur_invoice_supplier_wise";
    public static String GET_PUR_RTN_LAST_RECORD = "get_last_pur_returns_record";
    public static String CREATE_PUR_RETURN_ENDPOINT = "create_purchase_returns_invoices";
    public static String EDIT_PUR_RETURN_ENDPOINT = "edit_purchase_returns_invoices";
    public static String GET_BY_ID_PUR_RTN_ENDPOINT = "get_purchase_return_by_id_new";


    //Purchase Challan

    public static final String DELETE_PRODUCT_BATCH = "delete_product_batch";
    public static String getPurChallPurchaseAccounts = "get_purchase_accounts";
    public static String CREATE_PURCHASE_CHALLAN = "create_po_challan_invoices";
    public static String EDIT_PURCHASE_CHALLAN = "edit_purchase_challan";
    public static String getCurrentPurChallSrNo = "get_last_po_challan_record";
    public static String DELETE_PURCHASE_CHALLAN = "delete_purchase_challan";
    public static String VALIDATE_PURCHASE_CHALLAN = "validate_purchase_challan";
    public static String VALIDATE_PURCHASE_CHALLAN_UPDATE = "validate_purchase_challan_update";

    //PurchaseOrderToChallanConversion
    public static String GET_PUR_ORD_TO_CHALL_BY_ID = "get_po_invoices_with_ids";


    // CreditNote Acc. Entry
    public static String getAllCreditNoteList = "list_credit_notes";
    public static String deleteCreditNoteEntry = "delete_credit_note";
    public static String getCurrentCredNtVoucherNo = "get_last_record_creditnote";
    public static String createCreditNote = "create_credit";
    public static String getCreditNoteById = "get_credit_note_by_id";
    public static String updateCreditNote = "update_credit_note";

    // DebitNote Acc. Entry
    public static String getCurrentDebitNtVoucherNo = "get_last_record_debitnote";
    public static String createDebitNote = "create_debit";
    public static String getDebitNoteById = "get_debit_note_by_id";
    public static String updateDebitNote = "update_debit_note";
//    public static String getAllCreditNoteList = "list_credit_notes";
//    public static String deleteCreditNoteEntry = "delete_credit_note";


    //! Sales Invoice
    public static final String TRANX_SALES_INVOICE_LAST_RECORD = "get_sales_last_invoice_record";
    public static final String TRANX_SALES_GET_UNITS_FROM_PRODUCT = "get_all_product_units_packings_flavour";
    public static final String TRANX_SALES_INVOICE_CREATE = "create_sales_invoices";
    public static final String SALES_INVOICE_DELETE_ENDPOINT = "delete_sales_invoice";
    public static final String SALES_INVOICE_PRINT_ENDPOINT = "get_invoice_bill_print";
    public static final String TRANX_SALES_RETURN_CREATE = "create_sales_returns_invoices";
    public static final String TRANX_SALES_INVOICE_EDIT_BY_ID = "get_sales_invoice_by_id_new";
    public static final String TRANX_SALES_INVOICE_RTN_EDIT_BY_ID = "get_sales_return_by_id_new";
    public static final String TRANX_SALES_INVOICE_EDIT = "edit_sales_invoices";
    public static final String TRANX_SALES_INVOICE_RTN_EDIT = "edit_sales_returns_invoices";
    public static final String TRANX_SALES_INVOICE_LIST = "list_sale_invoice";
    public static final String TRANX_SALES_INVOICE_RTN_LIST = "get_sales_returns_by_outlet";
    //Gstr1

    public static final String GET_GSTR1_B2B1_REPORT_API = "get_GSTR1_B2B1_data";
    public static final String GET_GSTR1_B2B2_REPORT_API = "GSTR1_sale_invoice_details";
    public static final String GET_GSTR1_B2CL1_DATA_API = "get_GSTR1_B2CL_data";
    public static final String GET_GSTR1_B2CL2_DATA_API = "GSTR1_B2CL_sale_invoice_details";
    public static final String GET_GSTR1_B2CS1_DATA_API = "get_GSTR1_B2CS_data";
    public static final String GET_GSTR1_B2CS2_DATA_API = "GSTR1_B2CS_sale_invoice_details";
    public static final String GET_GSTR1_NIL_RATED_ENDPOINT = "get_GSTR_1NilRate_data";
    public static final String GET_GSTR1_CredDebUnreg_RATED_ENDPOINT = "get_GSTR2_CRNoteUnreg_data";


    //Controls
    public static String getOutletAppConfig = "get_outlet_appConfig";
    public static String updateAppConfig = "update_app_config";
    //For Transaction Reports
    public static final String GET_PAYABLE_REPORTS = "get_payable_report";
    public static final String GET_RECEIVABLE_REPORTS = "get_receivable_report";
    public static final String GET_RECEIPT_REPORT_DETAILS = "get_receipt_details";
    public static final String GET_PAYMENT_REPORT_DETAILS = "get_payment_details";
    public static final String GET_PAYMENT_REPORT2_DETAILS = "get_monthwise_payment_details";
    public static final String GET_JOURNAL_REPORT1_DETAILS = "get_journal_details";
    public static final String GET_JOURNAL_REPORT2_DETAILS = "get_monthwise_journal_details";
    public static final String GET_RECEIPT_REPORT2_DETAILS = "get_monthwise_receipt_details";


    //Account Entry

    public static final String GET_LAST_CONTRA_RECORD_ENDPOINT = "get_last_record_contra";
    public static final String CONTRA_LIST_ENDPOINT = "get_contra_list_by_outlet";
    public static final String CONTRA_DELETE_ENDPOINT = "delete_contra";

    public static String CONTRA_CREATE_ENDPOINT = "create_contra";
    public static String CONTRA_GET_DATA_BY_ID_ENDPOINT = "get_contra_by_id";
    public static String CONTRA_UPDATE_ENDPOINT = "update_contra";

    public static final String GET_LAST_JOURNAL_RECORD_ENDPOINT = "get_last_record_journal";
    public static final String JOURNAL_LIST_ENDPOINT = "get_journal_list_by_outlet";
    public static final String JOURNAL_DELETE_ENDPOINT = "delete_journal";
    public static String JOURNAL_CREATE_ENDPOINT = "create_journal";
    public static String JOURNAL_GET_DATA_BY_ID_ENDPOINT = "get_journal_by_id";
    public static String JOURNAL_UPDATE_ENDPOINT = "update_journal";

    // Debit note

    public static final String DEBIT_NOTE_LIST_ENDPOINT = "list_debit_notes";

    public static final String DEBIT_NOTE_DELETE_ENDPOINT = "delete_debit_note";

    //Purchase register report
    public static final String getPurchaseRegisterData = "get_pur_register_details";
    public static final String getPurchaseRegisterYearlyData = "get_monthwise_pur_register_details";


    public static final String TRANX_SALES_QUOTATION_PENDING_LIST = "sq_pending_list";
    public static final String TRANX_SALES_ORDER_PENDING_LIST = "saleOrdersPendingList";
    public static final String TRANX_SALES_CHALLAN_PENDING_LIST = "saleChallan_pending_list";
    public static final String TRANX_SALES_CHALLAN_PENDING_WITH_IDS = "get_sale_challan_with_ids";
    public static final String TRANX_SALES_ORDER_PENDING_WITH_IDS = "get_sale_orders_with_ids";
    public static final String TRANX_SALES_QUOTATION_PENDING_WITH_IDS = "get_sale_quotation_with_ids";

    //Dashboard
    public static final String GET_DASHBOARD_PRODUCT_AND_STOCK_DATA = "get_all_product_new";
    public static final String GET_DASHBOARD_DATA = "get_dashboard_data1";

    //Franchise Dashboard
    public static final String GET_SALE_INVOICE_LIST = "dashboard/list_sale_invoice";
    public static final String GET_PURCHASE_INVOICE_LIST = "dashboard/list_purchase_invoice";
    public static final String GET_TOP_SELLING_LIST = "dashboard/top_selling";
    public static final String GET_LEAST_SELLING_LIST = "dashboard/least_selling";
    public static final String GET_SUMMARY_LIST = "get_Dashboard_Cash_summary";
    public static final String CREATE_ZONE = "create_zone";
    public static final String CREATE_REGION = "create_region";
    public static final String CREATE_DISTRICT = "create_district";

    public static final String GET_ALL_ZONES = "get_all_zones";
    public static final String GET_ALL_REGIONS = "get_all_regions";
    public static final String GET_ALL_DISTRICTS = "get_all_districts";

    public static final String GET_RETURN_BILL_LIST = "list_tranx_debites_notes";


    // GSTR1 Dashboard Api's
    public static final String GET_GSTR1_B2B_DATA = "get_GSTR1_mainScreenb2b";
    public static final String GET_GSTR1_B2C_LARGE_DATA = "get_GSTR1_mainScreenb2cLarge";
    public static final String GET_GSTR1_B2C_SMALL_DATA = "get_GSTR1_mainScreenb2cSmall";
    public static final String GET_GSTR1_CRNOTE_REG_DATA = "get_GSTR1_mainScreenCRNote_reg";
    public static final String GET_GSTR1_CRNOTE_UNREG_DATA = "get_GSTR1_mainScreenCRNote_Unreg";
    public static final String GET_GSTR1_HSN_DATA = "get_GSTR1_mainScreenHSN";

//    public static final String GET_GSTR1_NIL_DATA = "list_tranx_debites_notes";


}
