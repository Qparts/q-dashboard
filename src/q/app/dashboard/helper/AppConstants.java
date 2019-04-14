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
    private final static String INVOICE_SERVICE = SysProps.getValue("invoiceService");

    private final static String CATALOG_SERVICE = PRODUCT_SERVICE + "catalog/";
    private final static String CART_SERVICE = SysProps.getValue("cartService");
    private final static String LOCATION_SERVICE = SysProps.getValue("locationService");

    private final static String IMAGE_SERVER = SysProps.getValue("imageServer");
    private static final String AMAZON_S3_PATH = SysProps.getValue("amazonS3Path");
    private static final String PRODUCT_BUCKET_NAME =SysProps.getValue("productBucketName");
    private static final String BRAND_BUCKET_NAME = SysProps.getValue("brandBucketName");
    public  static final String QUOTATION_ITEM_BUCKET_NAME = SysProps.getValue("quotationItemBucketName");
    public  static final String CUSTOMER_VEHICLE_BUCKET_NAME = SysProps.getValue("quotationBucketName");


    //AWS CALLS//
    public static final String AWS_REGION = "eu-central-1";

    public static final String getProductImage(long id){
        return AMAZON_S3_PATH + PRODUCT_BUCKET_NAME + "/" + id + ".png";
    }

    public static final String getBrandImage(long id){
        return AMAZON_S3_PATH + BRAND_BUCKET_NAME + "/" +  id + ".png";
    }

    public static final String getQuotationItemImage(long id){
        return  "/app/quotation-item/" + id + ".png";
    }

    public static final String getCustomerVehicleImage(long id){
        return  "/app/customer-vehicle/" + id + ".png";
    }

    //LOCATION SERVICE CALLS//
    public final static String GET_COUNTRIES = LOCATION_SERVICE + "countries";
    public final static String POST_COUNTRY = LOCATION_SERVICE + "country";
    public final static String GET_REGIONS = LOCATION_SERVICE + "regions";
    public final static String POST_REGION = LOCATION_SERVICE + "region";
    public final static String GET_CITIES = LOCATION_SERVICE + "cities";
    public final static String POST_CITY = LOCATION_SERVICE + "city";

    //CART SERVICE CALLS//
    public final static String GET_AWAITING_CARTS = CART_SERVICE + "carts/awaiting";
    public final static String GET_INITIATED_CARTS = CART_SERVICE + "carts/initiated";
    public final static String GET_AWAITING_WIRE_TRANSFERS = CART_SERVICE + "wire-transfers/awaiting";
    public final static String POST_CART_COMMENT = CART_SERVICE + "comment";
    public final static String GET_BANKS = CART_SERVICE + "banks";
    public final static String POST_PUT_DELETE_BANK = CART_SERVICE + "bank";
    public final static String POST_FUND_WALLET = CART_SERVICE + "fund-wallet/wire-transfer";
    public final static String PUT_CANCEL_TRANSFER = CART_SERVICE + "cancel-transfer";
    public final static String GET_NOTIFICATION_CARTS = CART_SERVICE + "carts-notification";
    public final static String getAwaitingWireTransfer(long id){
      return CART_SERVICE + "wire-transfer/" +  id + "/awaiting";
    }
    public final static String getAwaitingCart(long id){
        return CART_SERVICE + "cart/" +  id + "/awaiting";
    }
    public final static String getLiveWallet(long customerId){
        return CART_SERVICE + "wallet-live/" + customerId;
    }


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
    public final static String GET_NEWEST_CUSTOMERS = CUSTOMER_SERVICE + "newest";
    public final static String GET_INCOMPLETE_CUSTOMER_VEHICLES = CUSTOMER_SERVICE + "customer-vehicles/no-vin";
    public final static String PUT_CUSTOMER_VEHICLE_VIN = CUSTOMER_SERVICE + "customer-vehicle/vin";
    public final static String getCustomer(long customerId){
        return CUSTOMER_SERVICE + "customer/" + customerId;
    }
    public final static String getSearchCustomer(String query){
        return PRODUCT_SERVICE + "search/" + query;
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
    public final static String PUT_PRODUCT_PRICE = PRODUCT_SERVICE + "product-price";
    public final static String GET_NEWEST_PRODUCTS = PRODUCT_SERVICE + "products/newest";
    public final static String SEARCH_PRODUCT_BY_NUMBER = PRODUCT_SERVICE +  "search-product-by-number";
    public final static String getProduct(long id){
        return PRODUCT_SERVICE + "product/" + id;
    }
    public final static String getSearchProduct(String query){
        return PRODUCT_SERVICE + "products/search/" + query;
    }

    public final static String getMakeCategories(long makeId){
        return PRODUCT_SERVICE + "categories/make/" + makeId;
    }


    //CATALOG SERVICE CALLS//
    public final static String getSearchCatalogCarsByVin(int makeId, String vin){
        return CATALOG_SERVICE + "cars/make/"+makeId+"/vin/" + vin;
    }

    public final static String getCatalogGroups(int makeId, String carId, String groupId){
        String link = CATALOG_SERVICE + "groups/make/"+makeId+"/car/" + carId;
        if(groupId != null){
            link += "?groupid=" + groupId;
        }
        return link;
    }

    public final static String getCatalogParts(int makeId, String carId, String groupId){
        return CATALOG_SERVICE + "parts/make/"+makeId+"/car/" + carId +"/group/" + groupId;
    }




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
    public final static String POST_BILL_ITEM_RESPONSE = QUOTATION_SERVICE + "bill-item-response";
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

    //INVOICE SERVICE CALLS//
    public final static String POST_PURCHASE_ORDER = INVOICE_SERVICE + "purchase";
    public final static String getPurchase(long purchaseId){
        return INVOICE_SERVICE + "purchase/" + purchaseId;
    }
    public final static String getProductPurchaseS(long productId){
        return INVOICE_SERVICE + "purchases/product/" + productId;
    }












}
