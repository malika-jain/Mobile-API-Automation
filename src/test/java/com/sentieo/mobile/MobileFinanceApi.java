package com.sentieo.mobile;

import static com.sentieo.constants.Constants.*;
import static org.testng.Assert.assertEquals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.response.Response;
import com.jayway.restassured.specification.RequestSpecification;
import com.sentieo.data.TickerData;
import com.sentieo.dataprovider.DataProviderClass;
import com.sentieo.rest.base.RestAPISpecs;
import com.sentieo.rest.base.RestUtils;

public class MobileFinanceApi extends RestAPISpecs {

	TickerData obj = new TickerData();
	List<String[]> tickers = obj.readTickerCSV();
	
    //@Parameters({"EMAIL","PASSWORD"})
	@BeforeClass
	public void setup() throws Exception {
		try {
			
			String URI = USER_APP_URL + LOGIN_URL;
			HashMap<String, String> loginData = new HashMap<String, String>();
//			String email = System.getProperty("EMAIL");
//			String password = System.getProperty("PASSWORD");
			
			System.out.println(EMAIL);
			System.out.println(PASSWORD);
	
			loginData.put("email", EMAIL);
			loginData.put("password", PASSWORD);

			RequestSpecification spec = mobileLoginSpec(loginData);
			Response resp = RestUtils.login(URI, null, spec, loginData);

			System.out.println("Response Body is :" + resp.getBody().asString());

			apid = resp.getCookie("apid");
			usid = resp.getCookie("usid");

			RestAssured.baseURI = APP_URL;
			
			
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	@Test(enabled = false, description = "fetch_graph_data - Price chart", dataProvider = "tickerdataprovider", dataProviderClass = DataProviderClass.class)
	public void fetchGraphDataPriceChart(String ticker) throws Exception {
		String URI = APP_URL + FETCH_GRAPH_DATA;
		
		HashMap<String, Object> graphData = new HashMap<String, Object>();

		List<String> graphType = new ArrayList<String>();
		graphType.add("stock");
		graphType.add("newratio");
		graphType.add("shorts");

		List<String> seriesId = new ArrayList<String>();
		seriesId.add("ev_ebitda");
		seriesId.add("p_sales");
		seriesId.add("p_eps");

		graphData.put("counter", 1);
		

		for (String graph : graphType) {
			if (graph.equals("stock")) {
				graphData.put("ticker", ticker);
				graphData.put("graphtype", graph);
				graphData.put("pagetype", "realtool");
				graphData.put("term", ticker);

				RequestSpecification spec = mobileFormParamsSpec(graphData);

				Response resp = RestUtils.post(URI, null, spec, graphData);
				System.out.println("Response Body is :" + resp.getBody().asString());



			} else if (graph.equals("newratio")) {
				for (String seriesType : seriesId) {
					graphData.put("ticker", ticker);
					graphData.put("graphtype", graph);
					graphData.put("seriesid", seriesType);
					graphData.put("ratio", seriesType);
					graphData.put("ptype", "rolling");
					graphData.put("shift", "backward");
					graphData.put("ratio_name", "Next+4+Quarters");

					RequestSpecification spec = mobileFormParamsSpec(graphData);

					Response resp = RestUtils.post(URI, null, spec, graphData);
					System.out.println("Response Body is :" + resp.getBody().asString());

				}
			} else {
				graphData.put("ticker", ticker);
				graphData.put("subtype_shorts", "shorts");
				graphData.put("graphtype", graph);
				graphData.put("seriesid", "shorts");
				graphData.put("call_key", "get_graph");

				RequestSpecification spec = mobileFormParamsSpec(graphData);

				Response resp = RestUtils.post(URI, null, spec, graphData);
				System.out.println("Response Body is :" + resp.getBody().asString());

			}
		}

	}

	@Test(enabled = false ,description = "Fin Matrices", dataProvider = "matrixtypeprovider", dataProviderClass = DataProviderClass.class)
	public void fetchValueTable(String matrixType) throws Exception {
		String URI = APP_URL + FETCH_VALUE_DATA;
		HashMap<String, Object> tableData = new HashMap<String, Object>();

		for (String[] row : tickers) {
			for (String cell : row) {

				tableData.put("ticker", cell);
				tableData.put("type", matrixType);

				RequestSpecification spec = mobileFormParamsSpec(tableData);

				Response resp = RestUtils.post(URI, null, spec, tableData);

				System.out.println("Response Body is :" + resp.getBody().asString());
				assertEquals(resp.getStatusCode(), 200);
				

			}
		}
	
	}
	
	@Test(enabled = false,description = "Comparables", dataProvider = "tickerdataprovider", dataProviderClass = DataProviderClass.class)
    public void comparableSearch(String ticker) throws Exception
    {
		String URI =  APP_URL + COMPARABLE_SEARCH;
		
		HashMap<String, Object> compData = new HashMap<String, Object>();
		
		compData.put("tickers", ticker);
		compData.put("pagetype", "riskreward");
		compData.put("appnew_version", "7.2");
		compData.put("apikey", apid);
		compData.put("dtable", 1);
		compData.put("model_id", "default");
		compData.put("init", 1);
		compData.put("peer_limit", 10);
		compData.put("rival", 1);

	
		RequestSpecification spec = mobileFormParamsSpec(compData);

		Response resp = RestUtils.post(URI, null, spec, compData);
		
		System.out.println("Response Body is :" + resp.getBody().asString());
		assertEquals(resp.getStatusCode(), 200);

		
    }
	
	@Test(enabled=false,description = "Mobile Stock Data", dataProvider = "tickerdataprovider", dataProviderClass = DataProviderClass.class)
    public void mobileStockData(String ticker) throws Exception
    {
		String URI =  APP_URL + MOBILE_STOCK_DATA;
		HashMap<String, Object> stockData = new HashMap<String, Object>();
		
		stockData.put("ticker", ticker);
		
		RequestSpecification spec = mobileFormParamsSpec(stockData);

		Response resp = RestUtils.post(URI, null, spec, stockData);
		
		System.out.println("Response Body is :" + resp.getBody().asString());
		assertEquals(resp.getStatusCode(), 200);

		
    }
	

	@Test(enabled = true,description = "Fetch Past Intra", dataProvider = "tickerdataprovider", dataProviderClass = DataProviderClass.class)
    public void fetchPastIntra(String ticker) throws Exception
    {
		String URI =  APP_URL + FETCH_PAST_INTRA;
		HashMap<String, Object> intraData = new HashMap<String, Object>();
		
	      System.out.println("app url is: "+ APP_URL);
	      System.out.println(EMAIL);
		  System.out.println(PASSWORD);
		  System.out.println(ENV);
		  System.out.println(APP_URL + FETCH_PAST_INTRA);
		intraData.put("ticker", ticker);
		//intraData.put("frequency", "300s");
		//intraData.put("counter", 1);
		//intraData.put("apikey", apid);
		//intraData.put("_", "1424333159713");
		
		RequestSpecification spec = mobileFormParamsSpec(intraData);

		Response resp = RestUtils.post(URI, null, spec, intraData);
		
		System.out.println("Response Body is :" + resp.getBody().asString());
		assertEquals(resp.getStatusCode(), 200);

		
    }	

	@Test(enabled=false, description = "Fetch Graph Data - Estimate Trends", dataProvider = "estimatestypeprovider", dataProviderClass = DataProviderClass.class)
    public void fetchGraphDataEstimateTrends(String estimateType) throws Exception
    {
		String URI =  APP_URL + FETCH_GRAPH_DATA;
		HashMap<String, Object> graphData = new HashMap<String, Object>();
		
		for (String[] row : tickers) {
			for (String cell : row) {

				graphData.put("ticker", cell);
				graphData.put("subtype", estimateType);
				graphData.put("graphtype", "yearlyEstimate");
				graphData.put("ptype", "q5");
				graphData.put("next4", "true");
				graphData.put("startyear", "2010");
				graphData.put("endyear", "2017");
                graphData.put("getstock", "true");
                graphData.put("mob", 1);

				RequestSpecification spec = mobileFormParamsSpec(graphData);
				Response resp = RestUtils.post(URI, null, spec, graphData);

				System.out.println("Response Body is :" + resp.getBody().asString());
				assertEquals(resp.getStatusCode(), 200);
				

			}
		}

		
    }	
	
	@Test(enabled=false,description = "Fetch Graph Data - Fin Matrices", dataProvider = "matrixtypeprovider", dataProviderClass = DataProviderClass.class)
    public void fetchGraphDataFinMatrices(String matrixType) throws Exception
    {
		String URI =  APP_URL + FETCH_GRAPH_DATA;
		HashMap<String, Object> graphData = new HashMap<String, Object>();
		
		for (String[] row : tickers) {
			for (String cell : row) {

				graphData.put("ticker", cell);
				graphData.put("subtype", matrixType);
				graphData.put("graphtype", "financialData");
                graphData.put("getestimates", "true");
                graphData.put("yoy", "true");
                graphData.put("ttmind", "true");


				RequestSpecification spec = mobileFormParamsSpec(graphData);
				Response resp = RestUtils.post(URI, null, spec, graphData);

				System.out.println("Response Body is :" + resp.getBody().asString());
				assertEquals(resp.getStatusCode(), 200);
				

			}
		}

		
    }	
	
	@Test(enabled =false,description = "Fetch Graph Data - Yearly Price Chart", dataProvider = "tickerdataprovider", dataProviderClass = DataProviderClass.class)
    public void fetchGraphDataYearlyChart(String ticker) throws Exception
    {
		String URI =  APP_URL + FETCH_GRAPH_DATA;
		HashMap<String, Object> graphData = new HashMap<String, Object>();
		
		graphData.put("ticker", ticker);
		graphData.put("term", ticker);
		graphData.put("graphtype", "stock");

		
		RequestSpecification spec = mobileFormParamsSpec(graphData);
		Response resp = RestUtils.post(URI, null, spec, graphData);
		
		System.out.println("Response Body is :" + resp.getBody().asString());
		assertEquals(resp.getStatusCode(), 200);

		
    }	

	@Test(enabled=false,description = "Fetch Live Price", dataProvider = "tickerdataprovider", dataProviderClass = DataProviderClass.class)
    public void fetchLivePrice(String ticker) throws Exception
    {
		String URI =  APP_URL + FETCH_LIVE_PRICE;
		HashMap<String, Object> priceData = new HashMap<String, Object>();
		
		priceData.put("ticker", ticker);
		
		
		
		RequestSpecification spec = mobileFormParamsSpec(priceData);
		Response resp = RestUtils.post(URI, null, spec, priceData);
		System.out.println(System.getProperty("EMAIL"));
		
		System.out.println("Response Body is :" + resp.getBody().asString());
		
		assertEquals(resp.getStatusCode(), 100);
		
    }	
	
	@Test(enabled=false,description = "Financial Model", dataProvider = "tickerdataprovider", dataProviderClass = DataProviderClass.class)
    public void financialModel(String ticker) throws Exception
    {
		String URI =  APP_URL + FIN_MODEL_YEARLY_NEW;
		HashMap<String, Object> finModelData = new HashMap<String, Object>();
		
		finModelData.put("ticker", ticker);
		
		RequestSpecification spec = mobileFormParamsSpec(finModelData);
		Response resp = RestUtils.post(URI, null, spec, finModelData);
		
		System.out.println("Response Body is :" + resp.getBody().asString());
		
		assertEquals(resp.getStatusCode(), 200);
		
    }	
	
	@Test(enabled= false,description = "Financial Statements1", dataProvider = "tickerdataprovider", dataProviderClass = DataProviderClass.class)
    public void financialStatement(String ticker) throws Exception
    {
		String URI =  APP_URL + FETCH_ALL_XBRL_TABLES;
		HashMap<String, Object> finStatementsData = new HashMap<String, Object>();
		
		finStatementsData.put("ticker", ticker);
		
		RequestSpecification spec = mobileFormParamsSpec(finStatementsData);
		Response resp = RestUtils.post(URI, null, spec, finStatementsData);
		
		System.out.println("Response Body is :" + resp.getBody().asString());
		assertEquals(resp.getStatusCode(), 200);
		
		
    }	
	
	@Test(enabled = false, description = "Financial Statements2", dataProvider = "statementtypeprovider", dataProviderClass = DataProviderClass.class)
    public void financialStatement1(String statementType) throws Exception
    {
		String URI =  APP_URL + XBRL_TABLE_WITH_CHANGE;
		HashMap<String, Object> finStatementsData = new HashMap<String, Object>();
		
		int tickerListSize = tickers.size();
		System.out.println("ticker list size is:" + tickerListSize);
		
		for(String[] row:tickers)

		{	
				finStatementsData.put("key", statementType);
				finStatementsData.put("url", "http://www.sec.gov/Archives/edgar/data/320193/000032019319000010/R2.htm");
				
				RequestSpecification spec = mobileFormParamsSpec(finStatementsData);
				Response resp = RestUtils.post(URI, null, spec, finStatementsData);
				
				System.out.println("Response Body is :" + resp.getBody().asString());
				assertEquals(resp.getStatusCode(), 200);

			}
		
    }

}

