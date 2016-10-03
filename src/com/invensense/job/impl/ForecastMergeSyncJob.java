//package com.invensense.job.impl;
//
//import java.io.FileWriter;
//import java.io.IOException;
//import java.math.BigDecimal;
//import java.util.List;
//import javax.annotation.Resource;
//import org.apache.commons.lang3.StringUtils;
//import org.apache.log4j.Logger;
//import org.springframework.stereotype.Service;
//import com.csvreader.CsvReader;
//import com.csvreader.CsvWriter;
//import com.invensense.job.AbstractSpringJob;
//import com.invensense.model.ForeCast;
//import com.invensense.service.EntityService;
//
//@Service
//public class ForecastMergeSyncJob extends AbstractSpringJob {
//	
//	private Logger log= Logger.getLogger(ForecastMergeSyncJob.class);
//	
//	@Resource
//	private EntityService entityService;
//
//	@Override
//	protected void executeJob() throws Exception {
//		
//		CsvWriter identicalRecordCsvWriter = new CsvWriter(new FileWriter("/home/oracrm/CRM/Serene/Forecast/Output/Identical_Forecast_Output.csv"), ',');
//	    writeHeader(identicalRecordCsvWriter);
//	    
//	    CsvWriter mismatchRecordCsvWriter = new CsvWriter(new FileWriter("/home/oracrm/CRM/Serene/Forecast/Output/Mismatch_Forecast_Output.csv"), ',');
//	    writeHeader(mismatchRecordCsvWriter);
//		
//	    CsvWriter notExistsRecordCsvWriter = new CsvWriter(new FileWriter("/home/oracrm/CRM/Serene/Forecast/Output/Not_Exists_Forecast_Output.csv"), ',');
//	    writeHeader(notExistsRecordCsvWriter);
//		
//		CsvReader csvReader = new CsvReader("/home/oracrm/CRM/Serene/Forecast/Client_Provided_Forecast_Output_final.csv");
//		csvReader.setDelimiter(',');
//		csvReader.readHeaders();
//		while(csvReader.readRecord()) {
//			ForeCast forecastFromCSV = getForecastFromCSV(csvReader);
//			List<ForeCast> forecastList = entityService.findByNameQuery("GET_FORECAST_BY_CLIENT_DATA", 
//										  new Object[] {forecastFromCSV.getParentEndCustomerName(), forecastFromCSV.getPartNumber(), forecastFromCSV.getMarket(), 
//														forecastFromCSV.getSubMarket(), forecastFromCSV.getBusinessUnit(), forecastFromCSV.getProgramName(), 
//														forecastFromCSV.getYear()});
//			if(!forecastList.isEmpty()) {
//				log.info("Forecast exists in staging database with count : " + forecastList.size());
//				ForeCast stagingForecast = forecastList.get(0);
//				if(!ifNotEqual(stagingForecast, forecastFromCSV)) {
//					log.info("Forecast records does not match in staging and CSV.");
//					writeToCSV(mismatchRecordCsvWriter, stagingForecast, "STAGING");
//					writeToCSV(mismatchRecordCsvWriter, forecastFromCSV, "CSV");
//					
//					overwriteForecastRecord(stagingForecast, forecastFromCSV);
//					log.info("Overwritten the forecast record");
//					
//				} else {
//					log.info("Forecast recors matched.");
//					writeToCSV(identicalRecordCsvWriter, stagingForecast, "STAGING");
//					writeToCSV(identicalRecordCsvWriter, forecastFromCSV, "CSV");
//				}
//			} else {
//				log.info("Forecast does not exists in Staging database.");
//				writeToCSV(notExistsRecordCsvWriter, forecastFromCSV, "CSV");
//			}
//		}
//		
//		identicalRecordCsvWriter.close();
//		mismatchRecordCsvWriter.close();
//		notExistsRecordCsvWriter.close();
//		
//		log.info("Process completed successfully");
//	}
//
//	private void overwriteForecastRecord(ForeCast stagingForecast, ForeCast forecastFromCSV) {
//		forecastFromCSV.setEndCustomerId(stagingForecast.getEndCustomerId());
//		forecastFromCSV.setProductId(stagingForecast.getProductId());
//		forecastFromCSV.setName(stagingForecast.getName());
//		forecastFromCSV.setRowId(stagingForecast.getRowId());
//		forecastFromCSV.setForecastType(stagingForecast.getForecastType());
//		forecastFromCSV.setProductName(stagingForecast.getProductName());
//		forecastFromCSV.setProductCategory(stagingForecast.getProductCategory());
//		forecastFromCSV.setParentEndCustomer(stagingForecast.getParentEndCustomer());
//		forecastFromCSV.setSalesRepId(stagingForecast.getSalesRepId());
//		forecastFromCSV.setOwner(stagingForecast.getOwner());
//		forecastFromCSV.setRole(stagingForecast.getRole());
//		forecastFromCSV.setCreatedBy(stagingForecast.getCreatedBy());
//		forecastFromCSV.setCreatedDate(stagingForecast.getCreatedDate());
//		forecastFromCSV.setModifiedBy(stagingForecast.getModifiedBy());
//		forecastFromCSV.setModifiedDate(stagingForecast.getModifiedDate());
//		forecastFromCSV.setSyncStep(stagingForecast.getSyncStep());
//		forecastFromCSV.setSyncStatus(stagingForecast.getSyncStatus());
//		entityService.save(forecastFromCSV);
//	}
//
//	private void writeToCSV(CsvWriter csvWriter, ForeCast foreCast, String source) throws IOException {
//		csvWriter.writeRecord(new String[] {source, foreCast.getParentEndCustomerName(), foreCast.getPartNumber(), foreCast.getMarket(), foreCast.getSubMarket(), 
//				  foreCast.getBusinessUnit(), foreCast.getProgramName(), foreCast.getYear(), 
//				  String.valueOf(foreCast.getQuantity1()), String.valueOf(foreCast.getAsp1()), 
//				  String.valueOf(foreCast.getQuantity2()), String.valueOf(foreCast.getAsp2()), 
//				  String.valueOf(foreCast.getQuantity3()), String.valueOf(foreCast.getAsp3()), 
//				  String.valueOf(foreCast.getQuantity4()), String.valueOf(foreCast.getAsp4()), 
//				  String.valueOf(foreCast.getQuantity5()), String.valueOf(foreCast.getAsp5()), 
//				  String.valueOf(foreCast.getQuantity6()), String.valueOf(foreCast.getAsp6()), 
//				  String.valueOf(foreCast.getQuantity7()), String.valueOf(foreCast.getAsp7()), 
//				  String.valueOf(foreCast.getQuantity8()), String.valueOf(foreCast.getAsp8()), 
//				  String.valueOf(foreCast.getQuantity9()), String.valueOf(foreCast.getAsp9()), 
//				  String.valueOf(foreCast.getQuantity10()), String.valueOf(foreCast.getAsp10()), 
//				  String.valueOf(foreCast.getQuantity11()), String.valueOf(foreCast.getAsp11()), 
//				  String.valueOf(foreCast.getQuantity12()), String.valueOf(foreCast.getAsp12()) });
//		
//	}
//
//	private void writeHeader(CsvWriter csvWriter) throws IOException {
//		csvWriter.writeRecord(new String[] {"Source", "Customer-Name", "Part-Number", "Market", "Sub-Market", "BU", "Program-Name", "Year",  
//				 "quantity1", "asp1", "quantity2", "asp2", "quantity3", "asp3", "quantity4", "asp4", 
//				 "quantity5", "asp5", "quantity6", "asp6", "quantity7", "asp7", "quantity8", "asp8", 
//				 "quantity9", "asp9", "quantity10", "asp10", "quantity11", "asp11", "quantity12", "asp12"});
//	}
//	
//	private boolean ifNotEqual(ForeCast stagingForecast, ForeCast forecastFromCSV) {
//		Integer stagingQuantity1 = stagingForecast.getQuantity1() == null ? Integer.valueOf(0) : stagingForecast.getQuantity1();
//		Integer csvQuantity1 = forecastFromCSV.getQuantity1() == null ? Integer.valueOf(0) : forecastFromCSV.getQuantity1();
//		if(!stagingQuantity1.equals(csvQuantity1)) {
//			log.info("Quantity1 mismatch. Staging : " + stagingQuantity1 + " , CSV : " + csvQuantity1 + " of forecast : " + stagingForecast.toString());
//			return false;
//		}
//		
//		Integer stagingQuantity2 = stagingForecast.getQuantity2() == null ? Integer.valueOf(0) : stagingForecast.getQuantity2();
//		Integer csvQuantity2 = forecastFromCSV.getQuantity2() == null ? Integer.valueOf(0) : forecastFromCSV.getQuantity2();
//		if(!stagingQuantity2.equals(csvQuantity2)) {
//			log.info("Quantity2 mismatch. Staging : " + stagingQuantity2 + " , CSV : " + csvQuantity2 + " of forecast : " + stagingForecast.toString());
//			return false;
//		}
//		
//		Integer stagingQuantity3 = stagingForecast.getQuantity3() == null ? Integer.valueOf(0) : stagingForecast.getQuantity3();
//		Integer csvQuantity3 = forecastFromCSV.getQuantity3() == null ? Integer.valueOf(0) : forecastFromCSV.getQuantity3();
//		if(!stagingQuantity3.equals(csvQuantity3)) {
//			log.info("Quantity3 mismatch. Staging : " + stagingQuantity3 + " , CSV : " + csvQuantity3 + " of forecast : " + stagingForecast.toString());
//			return false;
//		}
//		
//		Integer stagingQuantity4 = stagingForecast.getQuantity4() == null ? Integer.valueOf(0) : stagingForecast.getQuantity4();
//		Integer csvQuantity4 = forecastFromCSV.getQuantity4() == null ? Integer.valueOf(0) : forecastFromCSV.getQuantity4();
//		if(!stagingQuantity4.equals(csvQuantity4)) {
//			log.info("Quantity4 mismatch. Staging : " + stagingQuantity4 + " , CSV : " + csvQuantity4 + " of forecast : " + stagingForecast.toString());
//			return false;
//		}
//		
//		Integer stagingQuantity5 = stagingForecast.getQuantity5() == null ? Integer.valueOf(0) : stagingForecast.getQuantity5();
//		Integer csvQuantity5 = forecastFromCSV.getQuantity5() == null ? Integer.valueOf(0) : forecastFromCSV.getQuantity5();
//		if(!stagingQuantity5.equals(csvQuantity5)) {
//			log.info("Quantity5 mismatch. Staging : " + stagingQuantity5 + " , CSV : " + csvQuantity5 + " of forecast : " + stagingForecast.toString());
//			return false;
//		}
//		
//		Integer stagingQuantity6 = stagingForecast.getQuantity6() == null ? Integer.valueOf(0) : stagingForecast.getQuantity6();
//		Integer csvQuantity6 = forecastFromCSV.getQuantity6() == null ? Integer.valueOf(0) : forecastFromCSV.getQuantity6();
//		if(!stagingQuantity6.equals(csvQuantity6)) {
//			log.info("Quantity6 mismatch. Staging : " + stagingQuantity6 + " , CSV : " + csvQuantity6 + " of forecast : " + stagingForecast.toString());
//			return false;
//		}
//		
//		Integer stagingQuantity7 = stagingForecast.getQuantity7() == null ? Integer.valueOf(0) : stagingForecast.getQuantity7();
//		Integer csvQuantity7 = forecastFromCSV.getQuantity7() == null ? Integer.valueOf(0) : forecastFromCSV.getQuantity7();
//		if(!stagingQuantity7.equals(csvQuantity7)) {
//			log.info("Quantity7 mismatch. Staging : " + stagingQuantity7 + " , CSV : " + csvQuantity7 + " of forecast : " + stagingForecast.toString());
//			return false;
//		}
//		
//		Integer stagingQuantity8 = stagingForecast.getQuantity8() == null ? Integer.valueOf(0) : stagingForecast.getQuantity8();
//		Integer csvQuantity8 = forecastFromCSV.getQuantity8() == null ? Integer.valueOf(0) : forecastFromCSV.getQuantity8();
//		if(!stagingQuantity8.equals(csvQuantity8)) {
//			log.info("Quantity8 mismatch. Staging : " + stagingQuantity8 + " , CSV : " + csvQuantity8 + " of forecast : " + stagingForecast.toString());
//			return false;
//		}
//		
//		Integer stagingQuantity9 = stagingForecast.getQuantity9() == null ? Integer.valueOf(0) : stagingForecast.getQuantity9();
//		Integer csvQuantity9 = forecastFromCSV.getQuantity9() == null ? Integer.valueOf(0) : forecastFromCSV.getQuantity9();
//		if(!stagingQuantity9.equals(csvQuantity9)) {
//			log.info("Quantity9 mismatch. Staging : " + stagingQuantity9 + " , CSV : " + csvQuantity9 + " of forecast : " + stagingForecast.toString());
//			return false;
//		}
//		
//		Integer stagingQuantity10 = stagingForecast.getQuantity10() == null ? Integer.valueOf(0) : stagingForecast.getQuantity10();
//		Integer csvQuantity10 = forecastFromCSV.getQuantity10() == null ? Integer.valueOf(0) : forecastFromCSV.getQuantity10();
//		if(!stagingQuantity10.equals(csvQuantity10)) {
//			log.info("Quantity10 mismatch. Staging : " + stagingQuantity10 + " , CSV : " + csvQuantity10 + " of forecast : " + stagingForecast.toString());
//			return false;
//		}
//		
//		Integer stagingQuantity11 = stagingForecast.getQuantity11() == null ? Integer.valueOf(0) : stagingForecast.getQuantity11();
//		Integer csvQuantity11 = forecastFromCSV.getQuantity11() == null ? Integer.valueOf(0) : forecastFromCSV.getQuantity11();
//		if(!stagingQuantity11.equals(csvQuantity11)) {
//			log.info("Quantity11 mismatch. Staging : " + stagingQuantity11 + " , CSV : " + csvQuantity11 + " of forecast : " + stagingForecast.toString());
//			return false;
//		}
//		
//		Integer stagingQuantity12 = stagingForecast.getQuantity12() == null ? Integer.valueOf(0) : stagingForecast.getQuantity12();
//		Integer csvQuantity12 = forecastFromCSV.getQuantity12() == null ? Integer.valueOf(0) : forecastFromCSV.getQuantity12();
//		if(!stagingQuantity12.equals(csvQuantity12)) {
//			log.info("Quantity12 mismatch. Staging : " + stagingQuantity12 + " , CSV : " + csvQuantity12 + " of forecast : " + stagingForecast.toString());
//			return false;
//		}
//		
//		BigDecimal stagingAsp1 = stagingForecast.getAsp1() == null ? BigDecimal.valueOf(0) : stagingForecast.getAsp1();
//		BigDecimal csvAsp1 = forecastFromCSV.getAsp1() == null ? BigDecimal.valueOf(0) : forecastFromCSV.getAsp1();
//		
//		if(stagingAsp1.compareTo(csvAsp1) != 0) {
//			log.info("ASP1 mismatch. Staging : " + stagingAsp1 + " , CSV : " + csvAsp1 + " of forecast : " + stagingForecast.toString());
//			return false;
//		}
//		
//		BigDecimal stagingAsp2 = stagingForecast.getAsp2() == null ? BigDecimal.valueOf(0) : stagingForecast.getAsp2();
//		BigDecimal csvAsp2 = forecastFromCSV.getAsp2() == null ? BigDecimal.valueOf(0) : forecastFromCSV.getAsp2();
//		if(stagingAsp2.compareTo(csvAsp2) != 0) {
//			log.info("ASP2 mismatch. Staging : " + stagingAsp2 + " , CSV : " + csvAsp2 + " of forecast : " + stagingForecast.toString());
//			return false;
//		}
//		
//		BigDecimal stagingAsp3 = stagingForecast.getAsp3() == null ? BigDecimal.valueOf(0) : stagingForecast.getAsp3();
//		BigDecimal csvAsp3 = forecastFromCSV.getAsp3() == null ? BigDecimal.valueOf(0) : forecastFromCSV.getAsp3();
//		if(stagingAsp3.compareTo(csvAsp3) != 0) {
//			log.info("ASP3 mismatch. Staging : " + stagingAsp3 + " , CSV : " + csvAsp3 + " of forecast : " + stagingForecast.toString());
//			return false;
//		}
//		
//		BigDecimal stagingAsp4 = stagingForecast.getAsp4() == null ? BigDecimal.valueOf(0) : stagingForecast.getAsp4();
//		BigDecimal csvAsp4 = forecastFromCSV.getAsp4() == null ? BigDecimal.valueOf(0) : forecastFromCSV.getAsp4();
//		if(stagingAsp4.compareTo(csvAsp4) != 0) {
//			log.info("ASP4 mismatch. Staging : " + stagingAsp4 + " , CSV : " + csvAsp4 + " of forecast : " + stagingForecast.toString());
//			return false;
//		}
//		
//		BigDecimal stagingAsp5 = stagingForecast.getAsp5() == null ? BigDecimal.valueOf(0) : stagingForecast.getAsp5();
//		BigDecimal csvAsp5 = forecastFromCSV.getAsp5() == null ? BigDecimal.valueOf(0) : forecastFromCSV.getAsp5();
//		if(stagingAsp5.compareTo(csvAsp5) != 0) {
//			log.info("ASP5 mismatch. Staging : " + stagingAsp5 + " , CSV : " + csvAsp5 + " of forecast : " + stagingForecast.toString());
//			return false;
//		}
//		
//		BigDecimal stagingAsp6 = stagingForecast.getAsp6() == null ? BigDecimal.valueOf(0) : stagingForecast.getAsp6();
//		BigDecimal csvAsp6 = forecastFromCSV.getAsp6() == null ? BigDecimal.valueOf(0) : forecastFromCSV.getAsp6();
//		if(stagingAsp6.compareTo(csvAsp6) != 0) {
//			log.info("ASP6 mismatch. Staging : " + stagingAsp6 + " , CSV : " + csvAsp6 + " of forecast : " + stagingForecast.toString());
//			return false;
//		}
//		
//		BigDecimal stagingAsp7 = stagingForecast.getAsp7() == null ? BigDecimal.valueOf(0) : stagingForecast.getAsp7();
//		BigDecimal csvAsp7 = forecastFromCSV.getAsp7() == null ? BigDecimal.valueOf(0) : forecastFromCSV.getAsp7();
//		if(stagingAsp7.compareTo(csvAsp7) != 0) {
//			log.info("ASP7 mismatch. Staging : " + stagingAsp7 + " , CSV : " + csvAsp7 + " of forecast : " + stagingForecast.toString());
//			return false;
//		}
//		
//		BigDecimal stagingAsp8 = stagingForecast.getAsp8() == null ? BigDecimal.valueOf(0) : stagingForecast.getAsp8();
//		BigDecimal csvAsp8 = forecastFromCSV.getAsp8() == null ? BigDecimal.valueOf(0) : forecastFromCSV.getAsp8();
//		if(stagingAsp8.compareTo(csvAsp8) != 0) {
//			log.info("ASP8 mismatch. Staging : " + stagingAsp8 + " , CSV : " + csvAsp8 + " of forecast : " + stagingForecast.toString());
//			return false;
//		}
//		
//		BigDecimal stagingAsp9 = stagingForecast.getAsp9() == null ? BigDecimal.valueOf(0) : stagingForecast.getAsp9();
//		BigDecimal csvAsp9 = forecastFromCSV.getAsp9() == null ? BigDecimal.valueOf(0) : forecastFromCSV.getAsp9();
//		if(stagingAsp9.compareTo(csvAsp9) != 0) {
//			log.info("ASP9 mismatch. Staging : " + stagingAsp9 + " , CSV : " + csvAsp9 + " of forecast : " + stagingForecast.toString());
//			return false;
//		}
//		
//		BigDecimal stagingAsp10 = stagingForecast.getAsp10() == null ? BigDecimal.valueOf(0) : stagingForecast.getAsp10();
//		BigDecimal csvAsp10 = forecastFromCSV.getAsp10() == null ? BigDecimal.valueOf(0) : forecastFromCSV.getAsp10();
//		if(stagingAsp10.compareTo(csvAsp10) != 0) {
//			log.info("ASP10 mismatch. Staging : " + stagingAsp10 + " , CSV : " + csvAsp10 + " of forecast : " + stagingForecast.toString());
//			return false;
//		}
//		
//		BigDecimal stagingAsp11 = stagingForecast.getAsp11() == null ? BigDecimal.valueOf(0) : stagingForecast.getAsp11();
//		BigDecimal csvAsp11 = forecastFromCSV.getAsp11() == null ? BigDecimal.valueOf(0) : forecastFromCSV.getAsp11();
//		if(stagingAsp11.compareTo(csvAsp11) != 0) {
//			log.info("ASP11 mismatch. Staging : " + stagingAsp11 + " , CSV : " + csvAsp11 + " of forecast : " + stagingForecast.toString());
//			return false;
//		}
//		
//		BigDecimal stagingAsp12 = stagingForecast.getAsp12() == null ? BigDecimal.valueOf(0) : stagingForecast.getAsp12();
//		BigDecimal csvAsp12 = forecastFromCSV.getAsp12() == null ? BigDecimal.valueOf(0) : forecastFromCSV.getAsp12();
//		if(stagingAsp12.compareTo(csvAsp12) != 0) {
//			log.info("ASP12 mismatch. Staging : " + stagingAsp12 + " , CSV : " + csvAsp12 + " of forecast : " + stagingForecast.toString());
//			return false;
//		}
//		
//		return true;
//	}
//	
//	private ForeCast getForecastFromCSV(CsvReader csvReader) throws Exception {
//		ForeCast forecast = new ForeCast();
//		
//		forecast.setParentEndCustomerName(csvReader.get("Customer-Name"));
//		forecast.setPartNumber(csvReader.get("Part-Number"));
//		forecast.setMarket(csvReader.get("Market"));
//		forecast.setSubMarket(csvReader.get("Sub-Market"));
//		forecast.setBusinessUnit(csvReader.get("BU"));
//		forecast.setProgramName(csvReader.get("Program-Name"));
//		forecast.setYear(csvReader.get("Year"));
//		
//		forecast.setQuantity1(getQuantityValue(csvReader.get("quantity1")));
//		forecast.setQuantity2(getQuantityValue(csvReader.get("quantity2")));
//		forecast.setQuantity3(getQuantityValue(csvReader.get("quantity3")));
//		forecast.setQuantity4(getQuantityValue(csvReader.get("quantity4")));
//		forecast.setQuantity5(getQuantityValue(csvReader.get("quantity5")));
//		forecast.setQuantity6(getQuantityValue(csvReader.get("quantity6")));
//		forecast.setQuantity7(getQuantityValue(csvReader.get("quantity7")));
//		forecast.setQuantity8(getQuantityValue(csvReader.get("quantity8")));
//		forecast.setQuantity9(getQuantityValue(csvReader.get("quantity9")));
//		forecast.setQuantity10(getQuantityValue(csvReader.get("quantity10")));
//		forecast.setQuantity11(getQuantityValue(csvReader.get("quantity11")));
//		forecast.setQuantity12(getQuantityValue(csvReader.get("quantity12")));
//		
//		forecast.setAsp1(getAspValue(csvReader.get("asp1")));
//		forecast.setAsp2(getAspValue(csvReader.get("asp2")));
//		forecast.setAsp3(getAspValue(csvReader.get("asp3")));
//		forecast.setAsp4(getAspValue(csvReader.get("asp4")));
//		forecast.setAsp5(getAspValue(csvReader.get("asp5")));
//		forecast.setAsp6(getAspValue(csvReader.get("asp6")));
//		forecast.setAsp7(getAspValue(csvReader.get("asp7")));
//		forecast.setAsp8(getAspValue(csvReader.get("asp8")));
//		forecast.setAsp9(getAspValue(csvReader.get("asp9")));
//		forecast.setAsp10(getAspValue(csvReader.get("asp10")));
//		forecast.setAsp11(getAspValue(csvReader.get("asp11")));
//		forecast.setAsp12(getAspValue(csvReader.get("asp12")));
//		
//		return forecast;
//	}
//	
//	private Integer getQuantityValue(String quantity) {
//		try {
//			if(StringUtils.isNotBlank(quantity)) {
//				return Integer.valueOf(quantity); 
//			}
//			return Integer.valueOf(0);
//		} catch(Exception ex) {
//			log.error("Error in getting Quantity value ", ex);
//			return Integer.valueOf(0);
//		}
//	}
//	
//	private BigDecimal getAspValue(String asp) {
//		try {
//			if(StringUtils.isNotBlank(asp)) {
//				BigDecimal aspValue = BigDecimal.valueOf(Double.valueOf(asp));
//				return aspValue;
//			}
//			return BigDecimal.valueOf(0);
//		} catch(Exception ex) {
//			log.error("Error in getting ASP value ", ex);
//			return BigDecimal.valueOf(0);
//		}
//	}
//	
//	public void testJob() throws Exception {
//		executeJob();
//	}
//
//}
