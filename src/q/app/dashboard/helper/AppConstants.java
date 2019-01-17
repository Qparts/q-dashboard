package q.app.dashboard.helper;

import java.util.Date;

public class AppConstants {
    //SERVERS AND CONSTANTS
    public final static String APP_SECRET = SysProps.getValue("appSecret");
    private final static String USER_SERVICE =  SysProps.getValue("userService");
    private final static String QUOTATION_SERVICE = SysProps.getValue("quotationService");
    private final static String VEHICLE_SERVICE = SysProps.getValue("vehicleService");
    private final static String VENDOR_SERVICE = SysProps.getValue("vendorService");
    private final static String CUSTOMER_SERVICE = SysProps.getValue("customerService");
    private final static String PRODUCT_SERVICE = SysProps.getValue("productService");
    private final static String IMAGE_SERVER = SysProps.getValue("imageServer");

    //some change in the file

    //USER SERVICE CALLS//
    public final static String USER_LOGIN = USER_SERVICE + "login";
    public final static String POST_CREATE_USER = USER_SERVICE + "user";
    public final static String PUT_UPDATE_USER = USER_SERVICE + "user";
    public final static String GET_ALL_USERS = USER_SERVICE + "all-users";
    public final static String GET_ALL_ACTIVITIES = USER_SERVICE + "all-activities";
    public final static String GET_ALL_ROLES = USER_SERVICE + "all-roles";
    public final static String GET_ACTIVE_ROLES = USER_SERVICE + "active-roles";
    public final static String POST_CREATE_ROLE = USER_SERVICE + "role";
    public final static String PUT_UPDATE_ROLE = USER_SERVICE + "role";
    public final static String getCurrentQuotingScore(int userId) {
        return USER_SERVICE + "current-quoting-score/user/" + userId;
    }



    //IMAGE SERVER CALLS//
    public final static String QUOTATION_IMAGE_DIRECTORY = IMAGE_SERVER + "quotations/";
    public final static String getQuotationVinImageLink(Date date, long id){
        int[] calvals = Helper.getCalendarArray(date);
        String directory = getVINDirectoryWithDate(calvals[0], calvals[1], calvals[2], id);
        return directory + id + ".png";
    }
    public final static String getQuotationItemImageLink(Date date, long quotationId, long itemId){
        int[] calvals = Helper.getCalendarArray(date);
        String directory = getVINDirectoryWithDate(calvals[0], calvals[1], calvals[2], quotationId) + "items/";
        return directory + itemId + ".png";
    }
    public final static String getBrandImage(int id){
        return IMAGE_SERVER + "brand/" + id + ".png";
    }
    private final static String getVINDirectoryWithDate(int year, int month, int day, long id) {
        return QUOTATION_IMAGE_DIRECTORY + year + "/" + month + "/" + day + "/" + id + "/";
    }

    //VEHICLE SERVICE CALLS//
    public final static String GET_ALL_MAKES = VEHICLE_SERVICE + "all-makes";
    public final static String POST_MODEL_YEAR = VEHICLE_SERVICE + "model-year";
    public final static String POST_MAKE = VEHICLE_SERVICE + "make";
    public final static String POST_MODEL = VEHICLE_SERVICE + "model";
    public final static String PUT_MAKE = VEHICLE_SERVICE + "make";
    public final static String PUT_MODEL = VEHICLE_SERVICE + "model";
    public final static String PUT_MODEL_YEAR = VEHICLE_SERVICE + "model-year";

    //CUSTOMER SERVICE CALLS//
    public final static String POST_CUSTOMER_FROM_IDS = CUSTOMER_SERVICE + "customers-from-ids";

    public final static String getCustomer(long customerId){
        return CUSTOMER_SERVICE + "customer/" + customerId;
    }

    //PRODUCT SERVICE CALLS//
    public final static String POST_SPEC = PRODUCT_SERVICE + "spec";
    public final static String GET_SPECS = PRODUCT_SERVICE + "specs";
    public final static String GET_BRANDS = PRODUCT_SERVICE + "brands";
    public final static String POST_BRAND = PRODUCT_SERVICE + "brand";
    public final static String GET_CATEGORIES_STRUCTURED = PRODUCT_SERVICE + "categories/structured";
    public final static String GET_ALL_CATEGORIES = PRODUCT_SERVICE + "categories";
    public final static String POST_CATEGORY = PRODUCT_SERVICE + "category";
    public final static String PUT_CATEGORY = PRODUCT_SERVICE + "category";
    public final static String POST_PRODUCT = PRODUCT_SERVICE + "product";

    //VENDOR SERVICE CALLS//
    public final static String POST_VENDOR = VENDOR_SERVICE + "vendor";
    public final static String GET_ALL_VENDORS = VENDOR_SERVICE + "vendors";



    //QUOTATION SERVICE CALLS//
    public final static String POST_QUOTATION_COMMENT = QUOTATION_SERVICE + "comment";
    public final static String POST_BILL = QUOTATION_SERVICE + "bill";
    public final static String PUT_BILL_ITEM = QUOTATION_SERVICE + "bill-item";
    public final static String POST_BILL_ITEM = QUOTATION_SERVICE + "bill-item";
    public final static String POST_ASSIGN_QUOTATION_TO_USER = QUOTATION_SERVICE + "assign-to-user";
    public final static String POST_ASSIGN_QUOTATION = QUOTATION_SERVICE + "assign";
    public final static String PUT_UNASSIGN_QUOTATION = QUOTATION_SERVICE + "unassign";
    public final static String GET_PENDING_QUOTATIONS = QUOTATION_SERVICE + "quotations/pending";
    public final static String PUT_MERGE_QUOTATIONS = QUOTATION_SERVICE + "merge-quotations";
    public final static String getQuotation(long quotationId) {
        return QUOTATION_SERVICE + "quotation/" + quotationId;
    }
    public final static String deleteBillItem(long billItemId) {
        return QUOTATION_SERVICE + "bill-item/" + billItemId;
    }
    public final static String getAssignedQuotations(int userId) { return QUOTATION_SERVICE + "assigned-quotations/user/" + userId;}
    public final static String deleteBill(long billId) {
        return QUOTATION_SERVICE + "bill/" + billId;
    }
    public final static String getAssignedQuotations(int userId, long quotationId) {
        return QUOTATION_SERVICE + "assigned-quotations/user/"+userId+"/quotation/" + quotationId;
    }













}