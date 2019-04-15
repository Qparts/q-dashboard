package q.app.dashboard.helper;

public class WebsocketLinks {

	private static final String BASE = SysProps.getValue("dashboardWSBase");
	//private static final String BASE = "wss://www.qetaa.com/";
	private static final String QUOTATION_SERVICE_WS = BASE + "service-q-quotation/ws/";
	private static final String CUSTOMER_SERVICE_WS = BASE + "service-q-customer/ws/";

	public static final String getQuotingLink(Integer username, String token) {
		return QUOTATION_SERVICE_WS + "quoting/user/"+username+"/token/" + token;
	}
	
	public static final String getQuotationsLink(Integer username, String token) {
		return QUOTATION_SERVICE_WS + "quotations/user/"+username+"/token/" + token;
	}
	
	public static final String getQuotationNotificationsLink(Integer username, String token) {
		return QUOTATION_SERVICE_WS + "notifications/user/"+username+"/token/" + token;
	}

	public static final String getCustomerNotificationsLink(Integer username, String token) {
		return CUSTOMER_SERVICE_WS + "notifications/user/"+username+"/token/" + token;
	}



}
