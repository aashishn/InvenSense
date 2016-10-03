package com.invensense.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.jibx.runtime.JiBXException;
import org.jibx.runtime.Utility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.invensense.model.InvensenseProperty;
import com.invensense.model.SystemProperty;
import com.invensense.service.EntityService;


@Component
public class CommonUtil {
	
	private static EntityService entityService ;
	
	private static Logger log = Logger.getLogger(CommonUtil.class);
	
	private static Map<String, String> forecastQuarterMap = new HashMap<String, String>();
	
	private static List<String> fiscalYearMonthList = new ArrayList<String>();
	
	private static List<String> skipSSOTokenValidationActionList = new ArrayList<String>();

	@Autowired(required = true)
	public void setEntityService(EntityService entityService) {
		CommonUtil.entityService = entityService;
	}
	
	public static SystemProperty getSystemProperty(String name) {
		return entityService.get(SystemProperty.class, name);
	}
	
	public static String getExceptionMessageWithCauses(Exception ex) {
		try {
			StringWriter sw = new StringWriter();
			ex.printStackTrace(new PrintWriter(sw));
			return sw.toString();
		} catch (Exception e) {
			return null;
		}
	}
	
	public static String getSystemPropertyValue(String name) {
		try {
			SystemProperty obj = getSystemProperty(name);
			if (obj == null) {
				return null ;
			}
			return obj.getKeyValue() ;
		}
		catch(Exception ex) {
			log.error(" Error while system property ",ex);
		}
		return null ;
	}
	
	public static String getSystemPropertyValue(String name,String defaultValue) {
		try {
			SystemProperty obj = getSystemProperty(name);
			if (obj == null) {
				return defaultValue ;
			}
			return obj.getKeyValue() ;
		}
		catch(Exception ex) {
			log.error(" Error while system property ",ex);
		}
		return defaultValue ;
	}
	/*
	public static Date deserializeDateTime(String date) {
		try {
			return Utility.deserializeDateTime(date);
		} catch (JiBXException e) {
			return null ;
		}
	}
	*/
	public static Date deserializeDateTime(String date) {
		try {
			return Utility.deserializeDateTime(date);
		} catch (JiBXException e) {
			return null ;
		}
	}
	
	public static String serializeDateTime(Date date) {
		String dateAsStr = "";
		if (date == null) {
			return null ;
		}
		try {
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String time = df.format(date);
			log.debug(" before date " + time);
			time = time.replaceAll(" ", "T");
			time = time + "Z";
			
			Calendar cal = Calendar.getInstance(TimeZone.getTimeZone(PropFileUtil.getValue("crmod.user.timezone")));
			SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
			sf.setCalendar(cal);
			
			Date d = sf.parse(time);
			cal.setTime(d);
			
			sf.setTimeZone(TimeZone.getTimeZone("GMT"));
			dateAsStr = sf.format(d);
			
			log.debug(" after date " + dateAsStr);
		} catch (Exception e) {
			log.error(" Error while formating date",e);
		}
		return dateAsStr ;
	}
	
	public static String serializerCrmodWS1Date(Timestamp date) {
		SimpleDateFormat sf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
		sf.setTimeZone(TimeZone.getTimeZone(PropFileUtil.getValue("crmod.user.timezone")));
		return sf.format(date.getTime());
	}	
	
	public static Timestamp deSerializerCrmodWS1Date(String date) {
		SimpleDateFormat sf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
		try {
			Date d = sf.parse(date);
			return new Timestamp(d.getTime());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null ;
	}
	
	public static String serializerBoolean(boolean value) {
	      return value ? "Y" : "N";
	}	
	
	public static boolean deSerializerBoolean(String value) {
		if (value == null || value.equals("")) {
			return false ;	
		}
		return value.equals("Y") ? true : false;
	}
	
	
	/**
	 * Validate SSO Token
	 * 
	 * The following steps are used to validate an SSO token:
		1 The third-party application can pass the SSO token as an URL parameter called "odSsoToken" of
			the following URL through a POST or GET request:
			https://server/Services/SSOTokenValidate?odSsoToken = [SSOTOKEN}
		2 If successful, the server returns the username in the HTTP body.
			The server returns errors if the SSO token has expired or is missing from the request.
	 * 
	 * @param url
	 * @param ssoToken
	 * @param userId
	 * @param action 
	 * @return
	 * @throws Exception
	 */
	public static boolean validateSSOToken(String url,String ssoToken,String userId, String action) throws Exception {
        HttpURLConnection wsConnection =null;
        StringBuffer sb = new StringBuffer("");
        try {
        	if(skipValidationBasedOnAction(action)) {
        		return true;
        	}
        	ssoToken = ssoToken.replaceAll(" ", "+");        
        	StringBuffer odURL = new StringBuffer(url.substring(0, url.lastIndexOf("/")));
        	String ssoTokenValidateURL = odURL.substring(0, odURL.lastIndexOf("/")+1) + "SSOTokenValidate?odSsoToken="+URLEncoder.encode(ssoToken, "UTF-8");
	
	        URL wsURL = new URL(ssoTokenValidateURL);
	        // create an HTTPS connection to the OnDemand webservices
	        wsConnection = (HttpURLConnection)wsURL.openConnection();
	        // we don't want any caching to occur
	        wsConnection.setUseCaches(false);
	        // set some http headers to indicate the username and password we are using to logon
	        wsConnection.setRequestMethod("GET");
	        
	        wsConnection.connect();
	        if (wsConnection.getResponseCode() != HttpURLConnection.HTTP_OK) {
	              return false;
	        }else{
	              BufferedReader br = new BufferedReader(new InputStreamReader(wsConnection.getInputStream()));
	              String data = "";
	              while (true) {
	                    data = br.readLine();
	                    if (data == null || "".equals(data)) {
	                          break;
	                    }
	                    sb.append(data);
	              }
	        }
	    } catch (UnsupportedEncodingException e) {
	        log.error("Failed encoding ssotoken",e);
	        throw new Exception("Failed encoding ssotoken",e);
	    } catch (MalformedURLException e) {
	        log.error("Error while logging off from OD Session",e);
	        throw new Exception(e.getMessage(),e);
	    } catch (IOException e) {
              log.error("IOException while logging off from OD Session",e);
              throw new Exception("IOException while logging off from OD Session",e);
        }finally {
	        try {
	            if (wsConnection!=null){
	                wsConnection.disconnect();
	                wsConnection = null;
	            }
	        } catch (Exception e){
	              log.error("IOException while logging off from OD Session",e);
	              throw new Exception("Exception while disconnecting from Oracle OnDemand",e);
	        }
        }
        log.debug("data from OD :"+sb.toString().length());
        return sb.toString().trim().equalsIgnoreCase(userId.trim());
	}
	
	private static boolean skipValidationBasedOnAction(String action) {
		if(skipSSOTokenValidationActionList.isEmpty()) {
			loadSkipSSOTokenValidationList();
		}
		return StringUtils.isNotBlank(action) && skipSSOTokenValidationActionList.contains(action) && hasValidSSOToken();
	}

	private static boolean hasValidSSOToken() {
		List<InvensenseProperty> invensensePropertyList = entityService.findByNameQuery(Constants.GET_INVENSENSE_PROPERTY_BY_NAME, 
														  new Object[] {Constants.FORECAST_SSO_TOKEN});
		if(!invensensePropertyList.isEmpty()) {
			InvensenseProperty invensenseProperty = invensensePropertyList.get(0);
			return Constants.ACTIVE.equals(invensenseProperty.getStatus());
		}
		return false;
	}

	private static void loadSkipSSOTokenValidationList() {
		skipSSOTokenValidationActionList.add("CreateFinanceForecast");
		skipSSOTokenValidationActionList.add("ViewFinanceForecast");
		skipSSOTokenValidationActionList.add("BackToSalesForecast");
	}

	public static String getForecastQuarter(String month) {
		if(forecastQuarterMap.isEmpty()) {
			loadForecastQuarterMap();
		}
		return forecastQuarterMap.get(month);
	}

	private static void loadForecastQuarterMap() {
		forecastQuarterMap.put(Constants.APRIL, Constants.QUARTER1);
		forecastQuarterMap.put(Constants.MAY, Constants.QUARTER1);
		forecastQuarterMap.put(Constants.JUNE, Constants.QUARTER1);
		forecastQuarterMap.put(Constants.JULY, Constants.QUARTER2);
		forecastQuarterMap.put(Constants.AUGUST, Constants.QUARTER2);
		forecastQuarterMap.put(Constants.SEPTEMBER, Constants.QUARTER2);
		forecastQuarterMap.put(Constants.OCTOBER, Constants.QUARTER3);
		forecastQuarterMap.put(Constants.NOVEMBER, Constants.QUARTER3);
		forecastQuarterMap.put(Constants.DECEMBER, Constants.QUARTER3);
		forecastQuarterMap.put(Constants.JANUARY, Constants.QUARTER4); 
		forecastQuarterMap.put(Constants.FEBRUARY, Constants.QUARTER4);
		forecastQuarterMap.put(Constants.MARCH, Constants.QUARTER4);
	}

	public static Integer getFiscalYearMonthSequence(String month) {
		if(fiscalYearMonthList.isEmpty()) {
			loadFiscalYearMonthList();
		}
		return fiscalYearMonthList.indexOf(month);
	}

	private static void loadFiscalYearMonthList() {
		fiscalYearMonthList.add(Constants.APRIL);
		fiscalYearMonthList.add(Constants.MAY);
		fiscalYearMonthList.add(Constants.JUNE);
		fiscalYearMonthList.add(Constants.JULY);
		fiscalYearMonthList.add(Constants.AUGUST);
		fiscalYearMonthList.add(Constants.SEPTEMBER);
		fiscalYearMonthList.add(Constants.OCTOBER);
		fiscalYearMonthList.add(Constants.NOVEMBER);
		fiscalYearMonthList.add(Constants.DECEMBER);
		fiscalYearMonthList.add(Constants.JANUARY);
		fiscalYearMonthList.add(Constants.FEBRUARY);
		fiscalYearMonthList.add(Constants.MARCH);
	}

	public static String getForecastTotalQuery(String salesRepConditionQueryString) {
		String query = " SELECT sum(Jan),sum(Feb),sum(Mar),sum(Apr),sum(May),sum(Jun),sum(Jul),sum(Aug),sum(Sep),\r\n" + 
			"				sum(Oct),sum(Nov),sum(Dec1) \r\n" + 
			"FROM( SELECT\r\n" + 
			"							ROUND((ifnull(quantity1,0)*ifnull(asp1,0)) ,1) AS 'Apr',\r\n" + 
			"							ROUND((ifnull(quantity2,0)*ifnull(asp2,0)) ,1) AS 'May',\r\n" + 
			"							ROUND((ifnull(quantity3,0)*ifnull(asp3,0)) ,1) AS 'Jun',\r\n" + 
			"							ROUND((ifnull(quantity4,0)*ifnull(asp4,0)) ,1) AS 'Jul',\r\n" + 
			"							ROUND((ifnull(quantity5,0)*ifnull(asp5,0)) ,1) AS 'Aug',\r\n" + 
			"							ROUND((ifnull(quantity6,0)*ifnull(asp6,0)) ,1) AS 'Sep',\r\n" + 
			"							ROUND((ifnull(quantity7,0)*ifnull(asp7,0)) ,1) AS 'Oct',\r\n" + 
			"							ROUND((ifnull(quantity8,0)*ifnull(asp8,0)) ,1) AS 'Nov',\r\n" + 
			"							ROUND((ifnull(quantity9,0)*ifnull(asp9,0)) ,1) AS 'Dec1',\r\n" + 
			"							ROUND((ifnull(quantity10,0)*ifnull(asp10,0)) ,1) AS 'Jan',\r\n" + 
			"							ROUND((ifnull(quantity11,0)*ifnull(asp11,0)) ,1) AS 'Feb',\r\n" + 
			"							ROUND((ifnull(quantity12,0)*ifnull(asp12,0)) ,1) AS 'Mar'\r\n" + 
			"		FROM forecast f\r\n" + 
			"		WHERE\r\n" + 
			"		f.year=?\r\n" + 
			"		AND " + salesRepConditionQueryString + "	\r\n" + 
			") S1		";
		return query;
	}
	
}
