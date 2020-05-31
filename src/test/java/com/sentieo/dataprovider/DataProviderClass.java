package com.sentieo.dataprovider;

import java.io.File;

import org.testng.annotations.DataProvider;

import com.sentieo.utils.CSVReaderUtil;


public class DataProviderClass {

	@DataProvider(name = "tickerdataprovider")
	Object [][] getTickerName()
	{
		
		String[][] groupArray = null;
		groupArray = CSVReaderUtil.readAllDataAtOnce("mobileFin" + File.separator + "tickers.csv");
				
		return groupArray;
		
	}
	
	@DataProvider(name = "matrixtypeprovider")
	Object [][] getMatrixType()
	{
		String[][] groupArray = null;
		groupArray = CSVReaderUtil.readAllDataAtOnce("mobileFin" + File.separator + "matrix_subtype.csv");
		
		return groupArray;
	}
	
	@DataProvider(name = "estimatestypeprovider")
	Object [][] getEstimateType()
	{
		String[][] groupArray = null;
		groupArray = CSVReaderUtil.readAllDataAtOnce("mobileFin" + File.separator + "matrix_subtype.csv");
		
		return groupArray;
	}
	
	@DataProvider(name = "statementtypeprovider")
	Object [][] getStatementType()
	{
		String[][] groupArray = null;
		groupArray = CSVReaderUtil.readAllDataAtOnce("mobileFin" + File.separator + "matrix_subtype.csv");
		
		return groupArray;
	}
}

