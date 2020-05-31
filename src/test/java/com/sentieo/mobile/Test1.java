//package com.sentieo.mobile;
//
//import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
//import static org.testng.Assert.assertEquals;
//
//import java.io.File;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import org.testng.annotations.Test;
//
//import com.sentieo.utils.JSONUtils;
//
//import io.restassured.RestAssured;
//import io.restassured.http.Method;
//import io.restassured.path.json.JsonPath;
//import io.restassured.response.Response;
//import io.restassured.specification.RequestSpecification;
//
//public class Test1 {
//
//	 String csrftoken = "";
//	 String apid = "";
//	 String usid = "";
//	 String fileId = "";
//	 
//	 JSONUtils lb = new JSONUtils();
//		 
//	 
//	 
//	@Test(priority = 1)
//	public void setCsrfToken() {
//
//		 
//		  RestAssured.baseURI = "https://app.sentieo.com/api";
//		 
//	
//		  
//		  //Request Object
//		 RequestSpecification httprequest = RestAssured.given();
//		 
//		 //Response Object
//		  Response response = httprequest.request(Method.GET,"/set_csrf_cookie");
//		 
//		  
//	
//		  System.out.println("Response Body is :" + response.getBody().asString());
//		  System.out.println("Status Code :" + response.getStatusCode());
//		//  System.out.println("Does Reponse contains 'employee_salary'? :" + response.asString().contains("employee_salary"));
//		 
//		  csrftoken = response.getCookie("csrftoken");
//		  System.out.println("csrftoken is:" + csrftoken);
//		  
//		  response.then().assertThat().body(matchesJsonSchemaInClasspath("csrf_schema.json"));    
//		  assertEquals(response.getStatusCode(),200);
//		 }
//		
//	@Test(priority = 2)
//	public void loginWithEmail()
//	{
//		
//		RestAssured.baseURI = "https://user-app.sentieo.com/api";
//		
//
//		  //Request Object
//		 RequestSpecification httprequest = RestAssured.given();
//		 
//		 
//		//Request Payload for POST request
//		 
//		 Map<String,String> map=new HashMap<String,String>();  
//		 
//		 map.put("email", "malika.jain@sentieo.com");
//		 map.put("password", "Sentieo1234!");
//		 map.put("csrfmiddlewaretoken", csrftoken);
//		 map.put("loc", "ios");
//		 
//		 httprequest.formParams(map);
//		 
//		   
//		   httprequest.header("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
//		   httprequest.cookie("csrftoken", csrftoken); 
//		   httprequest.cookie("app_version", "7.2");
//		   httprequest.cookie("device_name","iphone");
//		   httprequest.cookie("ios_version","13.2.2");
// 		   
//		 
//		 //Response Object
//		  Response response = httprequest.request(Method.POST,"/login_1/");
//		  
//		 
//		  		   
//		   System.out.println("Response Body is :" + response.getBody().asString());
//		   System.out.println("Status Code :" + response.getStatusCode());
//		  
//		  // To get all headers in response
////		   Headers allHeaders = response.headers();
////		   
////		   for(Header header:allHeaders)
////		   {
////			   System.out.println(header.getName()+ "   " + header.getValue()) ;
////		   }
//		   
//		   
//		   // To extract values from response
//		   JsonPath jsonPath = response.jsonPath();
//		  
//		   
//		   Map<String, String> response1 = jsonPath.getMap("response");
//		    apid = response1.get("apid");
//		    usid = response1.get("usid");
//		   
//		   System.out.println("Apid is"+ apid);
//		   System.out.println("Usid is"+ usid);
//
//		   //Assertions
//		   assertEquals(response.getStatusCode(),200);
//		   assertEquals(response.getContentType(),"application/json");
//		   //assertTrue(response.getTime()<=5000,"Response time is more than 5000 ms");
//		   //response.then().assertThat().body(matchesJsonSchemaInClasspath("login_schema.json"));   
//		   
//		   
//	}
//
//	@Test(priority = 3, dataProvider = "tickerdataprovider", dataProviderClass = JSONUtils.class)
//	
//	public void fetchGraphDataForPriceChart(String ticker)
//	{
//		RestAssured.baseURI = "https://app.sentieo.com/api";
//		
//		RequestSpecification httpRequest = RestAssured.given();
//		
//		httpRequest.header("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
//
//		httpRequest.cookie("csrftoken", csrftoken); 
//		httpRequest.cookie("app_version", "7.2");
//		httpRequest.cookie("device_name","iphone");
//		httpRequest.cookie("ios_version","13.2.2");
//		//httpRequest.cookie("fileDownload", true);
//		
//		List<String> graphType = new ArrayList<String>();
//		graphType.add("stock");
//		graphType.add("newratio");
//		graphType.add("shorts");
//		
//		List<String> seriesId = new ArrayList<String>();
//		seriesId.add("ev_ebitda");
//		seriesId.add("p_sales");
//		seriesId.add("p_eps");
//					
//		
//		Map<String,Object> map = new HashMap<String,Object>();
//				
//		map.put("apikey", apid);
//		map.put("csrfmiddlewaretoken", csrftoken);
//		map.put("counter", 1);
//		map.put("loc", "ios");
//		
//		
//		for(String graph:graphType)
//		{
//			if(graph.equals("stock"))
//			{
//				map.put("ticker", ticker);
//				map.put("graphtype", graph);
//				map.put("pagetype", "realtool");
//				map.put("term",ticker);
//				httpRequest.formParams(map);
//		
//				Response response = httpRequest.when().post("/fetch_graph_data/");
//				assertEquals(response.getStatusCode(),200); 
//				System.out.println("Response Body is :" + response.getBody().asString());
//			
//			}
//			else if(graph.equals("newratio"))
//			{
//				for(String seriesType:seriesId)
//				{
//					map.put("ticker", ticker);
//					map.put("graphtype", graph);
//					map.put("seriesid", seriesType);
//					map.put("ratio", seriesType);
//					map.put("ptype", "rolling");
//					map.put("shift", "backward");
//					map.put("ratio_name", "Next+4+Quarters");
//					httpRequest.formParams(map);
//					httpRequest.log().body();
//
//					Response response = httpRequest.when().post("/fetch_graph_data/");
//					assertEquals(response.getStatusCode(),200); 
//					System.out.println("Response Body is :" + response.getBody().asString());
//				
//				}
//			}
//			else
//			{
//				map.put("ticker", ticker);
//				map.put("subtype_shorts", "shorts");
//				map.put("graphtype", graph);
//				map.put("seriesid", "shorts");
//				map.put("call_key", "get_graph");
//				httpRequest.formParams(map);
//				httpRequest.log().body();
//				
//
//				Response response = httpRequest.when().post("/fetch_graph_data/");
//				assertEquals(response.getStatusCode(),200); 
//				System.out.println("Response Body is :" + response.getBody().asString());
//			
//			}
//		}
//				
//		
//	}
//	
//	@Test(priority = 3)
//	public void uploadFile() 
//	{
//		File testFile = new File("/Users/malikajain/Downloads/AN_DS1_WTO-Dispute-Settlement-Issues-to-consider-in-DSU-negotiations_EN (1).pdf");
//		
//	    RestAssured.baseURI = "https://user-app.sentieo.com";
//	    
//	    RequestSpecification httpRequest = RestAssured.given();
//	    
//	    
//	    httpRequest.header("Content-Type", "multipart/form-data");
//	    
//	    httpRequest.cookie("csrftoken", csrftoken);
//	    httpRequest.cookie("usid", usid);
//	    httpRequest.cookie("fileDownload", "true");
//	    
//	    Map<String, Object> map = new HashMap<String, Object>();
//	    
//	    map.put("qqfile", "file1.pdf");
//	    map.put("csrfmiddlewaretoken", csrftoken);
//	    	    
//        httpRequest.multiPart("attachments[]", testFile);        
//        httpRequest.formParams(map);
//        
//        
//        Response response = httpRequest.request(Method.POST,"/upload/");
//        
//        assertEquals(response.getStatusCode(),200); 
//		System.out.println("Response Body is :" + response.getBody().asString());
//				
//		JsonPath jsonPath = response.jsonPath();
//		fileId = jsonPath.get("id");
//		   
//	    System.out.println(fileId);
//		 
//	}
//	
//	@Test(priority = 4)
//	public void createNoteForAttachments()
//	{
//		RestAssured.baseURI = "https://user-app.sentieo.com/api";
//		
//		RequestSpecification httpRequest = RestAssured.given();
//		
//		//httpRequest.header("Content-type", "application/x-www-form-urlencoded; charset=UTF-8");
//		
//	    httpRequest.cookie("csrftoken", csrftoken);
//	    httpRequest.cookie("usid", usid);
//	    httpRequest.cookie("fileDownload", "true");	    
//	    
//	    Map<String,Object> dataDictMap = new HashMap<String, Object>();
//	    dataDictMap.put("file_id",fileId);
//	    dataDictMap.put("content_type","application/pdf");
//	    dataDictMap.put("filename","AN_DS1_WTO-Dispute-Settlement-Issues-to-consider-in-DSU-negotiations_EN (1).pdf");
//	    
//	    
//	   
//	    List<Map<String,Object>> dataDictList= new ArrayList<Map<String,Object>>();
//	    dataDictList.add(dataDictMap);
//	    	
//	    String json = lb.toJson(dataDictList);
//	    httpRequest.formParams("dataDict", json);
//	    httpRequest.formParams("csrfmiddlewaretoken",csrftoken);
//	       
//	    
//	    Response response = httpRequest.when().post(("/create_note_for_attachments/"));
//	    
//	    assertEquals(response.getStatusCode(),200); 
//		System.out.println("Response Body is :" + response.getBody().asString());
//	    
//	   
//	}
//	
//}
