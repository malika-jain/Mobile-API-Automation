package com.sentieo.constants;



public class Constants {

	public static String EMAIL = "";
	public static String PASSWORD = "";
	public static String ENV = "";
	public static  String APP_URL;
	public static String USER_APP_URL;
	static
	{
		EMAIL = System.getProperty("EMAIL");
		PASSWORD = System.getProperty("PASSWORD");
		ENV = System.getProperty("env");
		APP_URL = "https://" + ENV+ ".sentieo.com";
		USER_APP_URL = "https://user-" + ENV + ".sentieo.com";
	}
	
	// base URL
	 

	
	// finance
	public static final String LOGIN_URL = "/api/login_1/";
	public static final String FETCH_GRAPH_DATA = "/api/fetch_graph_data/";
	public static final String FETCH_VALUE_DATA = "/api/fetch_value_table/";
	public static final String COMPARABLE_SEARCH = "/api/comparable_search/";
	public static final String MOBILE_STOCK_DATA = "/api/mobile_stock_data/";
	public static final String FETCH_PAST_INTRA = "/api/fetch_past_intra/";
	public static final String FETCH_LIVE_PRICE = "/api/fetch_mobile_live_price/";
	public static final String FIN_MODEL_YEARLY_NEW = "/api/mobile_fin_model_yearly_new/";
	public static final String FETCH_ALL_XBRL_TABLES = "/api/fetch_all_xbrl_tables/";
	public static final String XBRL_TABLE_WITH_CHANGE = "/api/xbrl_table_with_change/";
	

	// docsearch
}
