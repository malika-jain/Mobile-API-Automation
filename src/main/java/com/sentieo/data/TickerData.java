package com.sentieo.data;

//import static com.sentieo.utils.FileUtil.RESOURCE_PATH;

import java.io.File;
import java.io.FileReader;
import java.util.List;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

public class TickerData {
FileReader filereader;
public static final String RESOURCE_PATH = System.getProperty("user.dir") + File.separator + "src" + File.separator
+ "test" + File.separator + "resources";
	
	public List<String[]> readTickerCSV() {
		FileReader filereader;
		
		try {
		    filereader = new FileReader(RESOURCE_PATH + File.separator + "mobileFin" + File.separator + "tickers.csv");
			
			CSVReader csvReader = new CSVReaderBuilder(filereader).withSkipLines(1).build();
			List<String[]> allData = csvReader.readAll();
			return allData;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}
}
