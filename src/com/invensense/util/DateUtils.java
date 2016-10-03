package com.invensense.util;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class DateUtils {
	
	private static Logger log = Logger.getLogger(DateUtils.class);
	private static String DB_FORMAT = "MM/dd/yyyy";
	
	// private static String FULL_CAL_FORMAT = "EEE, d MMM yyyy HH:mm:ss z";
	private static final String FULL_CAL_FORMAT = "MM/dd/yyyy HH:mm:ss";
	private static final DateFormat dateFormat = new SimpleDateFormat(FULL_CAL_FORMAT);
	private static final DateFormat defaultDateFormat = new SimpleDateFormat(DB_FORMAT);
	
	public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormat.forPattern("MM/dd/yyyy HH:mm:ss");
	
	public static final DateTimeFormatter CRMOD_DATE_TIME_FORMATTER = DateTimeFormat.forPattern("MM/dd/yyyy HH:mm:ss");
	
	public static final DateTimeFormatter DATE_TIMEZONE_FORMATTER = DateTimeFormat.forPattern("M/dd/yyyy hh:mm aaa z");
	
	private static final String SFDC_FORMAT = "yyyy-MM-dd";
	private static final SimpleDateFormat dateformat2 = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
	
	
	public static java.util.Date sfdcTime(String dt) throws ParseException{
	
		java.util.Date oDate= dateformat2.parse(dt);
		
		
		return oDate; 
		
		
	}
	public static DateTime substractOneMinute(DateTime dt){
		return dt.minusMinutes(1);
	}
	
	/**
	 * Convert date from source time zone to destination time zone
	 * 
	 * @param date
	 * @param sourceTimeZone
	 * @param destinationTimeZone
	 * @return
	 */
	public static DateTime convertJodaTimezone(DateTime date, String sourceTimeZone, String destinationTimeZone) {
		  DateTime srcDateTime = date.toDateTime(DateTimeZone.forID(sourceTimeZone));
		  DateTime dstDateTime = srcDateTime.withZone(DateTimeZone.forID(destinationTimeZone));
		  return dstDateTime;
	}
	
	/**
	 * Convert the date stored in GMT to the time zone
	 * 
	 * @param date
	 * @param timezone
	 * @return
	 */
	public static String getDateInTimeZone(Date date, String timezone){
		DateTime dt = new DateTime(date.getTime());
		dt = convertJodaTimezone(dt, DateTimeZone.UTC.getID(), timezone);
		return DATE_TIMEZONE_FORMATTER.print(dt);
	}
	
	
	/**
	 * Return the current time in GMT
	 * 
	 * @return
	 */
	public static Timestamp getCurrentTimestampInGMT(){
		DateTime dt = convertJodaTimezone(new DateTime(), DateTimeZone.getDefault().getID(), DateTimeZone.UTC.getID());
	    
	    return new Timestamp(dt.getMillis());
	}
	
	public static Timestamp toTimeStamp(String pattern, String dateStr) {
		DateFormat df = null;
		if (StringUtils.isBlank(pattern)) {
			df = defaultDateFormat;
		} else {
			df = new SimpleDateFormat(pattern);	
		}
		
		Date date = null;
		try {
			date = df.parse(dateStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return new Timestamp(date.getTime());
	}
	
	public static String toFullCalString(Date date) {
		return dateFormat.format(date);
	}
	
	public static String toFullCalString(DateTime date) {
		return DATE_TIME_FORMATTER.print(date);
	}

	public static String currentDateAsStr(){
		return format(new Date(), null);
	}
	
	public static String format(Date date, String pattern) {
		String formattedDateAsStr = null;
		
		if(pattern == null || pattern.trim().equals(""))
		{
			formattedDateAsStr = defaultDateFormat.format(date);
		}
		else
		{
			try {
				DateFormat defDateFormat = new SimpleDateFormat(pattern);
				formattedDateAsStr = defDateFormat.format(date);
			} catch (Exception e) {
				formattedDateAsStr = defaultDateFormat.format(date);
			}
		}
		
		return formattedDateAsStr;
	}
	
	public static Date convertEpochToDate(String timeInLongFormat) {
		return new Date(new Long(timeInLongFormat));
	}
	
	public static Date addMonths(Date input, int months){
		Calendar cal = Calendar.getInstance();
		cal.setTime(input);
		cal.add(Calendar.MONTH, months);
		Date output = cal.getTime();
		return output;
	}
	
	public static void main(String args){
		addMonths(new Date(), 12);
	}
	

	public static String convertDateTimeToGMT(String dateStr) {
		String dateTimeInGMT = dateStr ;
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat(Constants.CRMOD_QUERY_DATE_FORMAT);
			Date date = dateFormat.parse(dateStr); 
			SimpleDateFormat dateFormatGMT = new SimpleDateFormat(Constants.CRMOD_QUERY_DATE_FORMAT);
			dateFormatGMT.setTimeZone(TimeZone.getTimeZone("GMT"));
			dateTimeInGMT = dateFormatGMT.format(date);
		} catch (Exception ex){
			log.error(" Error while converting date tin GMT " + dateStr,ex );
		}
		return dateTimeInGMT ;
	}
	
	public static String convertTimeToZone(String dateStr,String fromTimeZone,String toTimeZone) {
		String dateTimeTo = dateStr ;
		if (dateStr == null || dateStr.equals("")) {
			return dateTimeTo ;
		}
		if (fromTimeZone.equals(toTimeZone)) {
			return dateTimeTo ;
		}
		dateStr = dateStr.trim() ;
		try {
			Calendar cal = Calendar.getInstance(TimeZone.getTimeZone(fromTimeZone));
			SimpleDateFormat sf = new SimpleDateFormat(Constants.CRMOD_QUERY_DATE_FORMAT);
			sf.setCalendar(cal);
			Date d = sf.parse(dateStr);
			
			cal.setTime(d);
			TimeZone toZone = TimeZone.getTimeZone(toTimeZone);
			if (toZone.inDaylightTime(cal.getTime())) {
				cal.add(Calendar.MILLISECOND,  toZone.getDSTSavings()* -1);
			}
			DateTime time2 = DateUtils.convertJodaTimezone(new DateTime(cal.getTime()), fromTimeZone, toTimeZone);
			dateTimeTo = time2.toString(DateUtils.CRMOD_DATE_TIME_FORMATTER);
		} catch (Exception ex){
			log.error(" Error while converting date " + dateStr,ex );
		}
		return dateTimeTo ;
	}	

	public static DateTime convertDateToTimezone(Date date, String sourceTimeZone, String destinationTimeZone) {
		  DateTime jodaTime = new DateTime(date.getTime());
		  DateTime srcDate = jodaTime.toDateTime(DateTimeZone.forID(sourceTimeZone));
		  DateTime dstDate = srcDate.withZone(DateTimeZone.forID(destinationTimeZone));
		  return dstDate;
	}
	
	public static Timestamp getTimestampFromUtilDate(java.util.Date date){
		Timestamp ts = null;
		if (date!=null){
			ts = new Timestamp(date.getTime());
		}
		return ts;
	}
}
