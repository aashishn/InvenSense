package com.invensense.util;

public class Constants {
	public static final String OD_NEW_OBJECT	= "New";
	public static final String UNPROCESSED_LEAD = "UNPROCESSED_LEAD";
	public static final String UNPROCESSED_EVENTS = "UNPROCESSED_EVENTS";
	public static final String SYNC_JOB_QUERY = "SYNC_JOB_QUERY";
	public static final String DATA_SYNC_STAGE_QUERY = "DATA_SYNC_STAGE_QUERY";
	public static final String ERROR_OBJECTS = "ERROR_OBJECTS";
	public static final String GET_SFDC_PICKLIST_VALUE = "GET_SFDC_PICKLIST_VALUE";	
	public static final String GET_CRMOD_PICKLIST_VALUE = "GET_CRMOD_PICKLIST_VALUE";
	public static enum  JOBS  {ACCOUNT_JOB};
	
	
	public static enum  JOBS_STATUS  {COMPLETED,IN_PROGRESS,FAILED};
	public static enum  SYNC_STEP {UNPROCESSED,CRMOD_TO_DB,DB_CRMOD,PROCESSED,STAGING_TO_CRM};
	public static enum  SYNC_STATUS {COMPLETED,FAILED};
	
	//page sizes for Query page
	public static final int STARTING_RECORD = 1;
	public static final int PAGE_SIZE = 100;
	
	//Interface Names
	public static enum INTERFACE_NAME {ServiceRequest,Opportunity};
	
	public static enum SYNC_STATE {ERROR,PROCESSED};

	public static final String APP_PROPERTY_FILE_NAME = "ApplicationResource.properties";
	
	public static final String SC_PROPERTY_FILE_NAME = "crmod.properties";
	
	//Interface Constants
	public static final String OPPORTUNITY = "Opportunity";
	
	public static final String Case = "Case";
	
	//Opportunity Query
	public static final String SFDC_OPP_SELECT_PARAM = "SFDC_OPP_SELECT_PARAM";
	public static final String OPP_PROBABILITY_CRITERIA = "OPP_PROBABILITY_CRITERIA";
	public static final String LAST_MODIFIED_DATE_CRITERIA = "LAST_MODIFIED_DATE_CRITERIA";
	public static final String RECORD_LIMIT = "RECORD_LIMIT";
	public static final String ORDER_BY = "ORDER_BY";
	public static final String AND = " AND";
	public static final String OR = " OR";
	public static final String WHERE = " WHERE";
	public static final String OPP_SYNC_START_DATE = "OPP_SYNC_START_DATE";
	public static final String SALES_STAGE = "SalesStage";
	
	// Forecast Query

	public static final String  GET_USER_BY_USER_NAME="GET_USER_BY_USER_NAME";
	
	
	
	//PickList Values 
	public static final String SOURCE = "Source";
	public static final String STATUS = "Status";
	public static final String ORIGIN = "Origin";
	public static final String PRIORITY = "Priority";
	
	public static final String WAITING_FOR_APPROVAL = "Waiting For Approval";
	public static final String QUOTE_LINE_ITEM_APPROVED = "Approved";
	public static final String REJECTED = "Rejected";
	
	public static final String PENDING_FOR_APPROVAL = "Pending For Approval";
	public static final String PARTIALLY_APPROVED = "Partially Approved";
	public static final String QUOTE_APPROVED = "Approved";
	
	public static final String QUOTE_LINE_ITEM_REJECTED = "Rejected";
	
	public static final String QUOTE_PENDING = "Pending";
	public static final String QUOTE_REJECTED = "Rejected";
	public static final String QUOTE_CANCELLED = "Cancelled";
	public static final String QUOTE_PARTIALLY_APPROVED = "Partially Approved";
	public static final String OPPORTUNITY_REVENUE_TYPE_LOST = "Lost";
	public static final String FORECAST_TYPE_SALES_FORECAST = "Sales Forecast";	
	public static final String GET_DATA_FOR_FORECAST ="  SELECT  concat(revenue_id,':',year,':',end_customer_id),end_customer_id,parent_end_customer_name,market, sub_market, program_name, business_unit,product_name,data,seq,sum(Apr),sum(May),sum(Jun),0 q1,sum(Jul),\r\n" + 
			"					sum(Aug),sum(Sep),0 q2,sum(Oct),sum(Nov),sum(Dec1),0 q3,sum(Jan),sum(Feb),sum(Mar),0 q4,created_date \r\n" + 
			"FROM( \r\n" + 
			"		SELECT f.product_id revenue_id, f.end_customer_id, f.parent_end_customer_name, f.year,\r\n" + 
			"		f.market,f.sub_market,f.program_name,f.business_unit,f.product_name,'Quantity (U)' AS data,1 AS 'seq', f.created_date,\r\n" + 
			"							CASE WHEN quantity1 IS NULL THEN 0 ELSE (quantity1) END AS 'Apr',\r\n" + 
			"							CASE WHEN quantity2 IS NULL THEN 0 ELSE (quantity2)  END AS 'May',\r\n" + 
			"							CASE WHEN quantity3 IS NULL THEN 0 ELSE (quantity3)  END AS 'Jun',\r\n" + 
			"							CASE WHEN quantity4 IS NULL THEN 0 ELSE (quantity4)  END AS 'Jul',\r\n" + 
			"							CASE WHEN quantity5 IS NULL THEN 0 ELSE (quantity5)  END AS 'Aug',\r\n" + 
			"							CASE WHEN quantity6 IS NULL THEN 0 ELSE (quantity6)  END AS 'Sep',\r\n" + 
			"							CASE WHEN quantity7 IS NULL THEN 0 ELSE (quantity7)  END AS 'Oct',\r\n" + 
			"							CASE WHEN quantity8 IS NULL THEN 0 ELSE (quantity8)  END AS 'Nov',\r\n" + 
			"							CASE WHEN quantity9 IS NULL THEN 0 ELSE (quantity9)  END AS 'Dec1',\r\n" + 
			"							CASE WHEN quantity10 IS NULL THEN 0 ELSE (quantity10)  END AS 'Jan',\r\n" + 
			"							CASE WHEN quantity11 IS NULL THEN 0 ELSE (quantity11)  END AS 'Feb',\r\n" + 
			"							CASE WHEN quantity12 IS NULL THEN 0 ELSE (quantity12)  END AS 'Mar'\r\n" + 
			"		FROM forecast f\r\n" + 
			"		WHERE\r\n" + 
			"		f.year= ?\r\n" + 
			"		AND f.sales_rep_id= ?\r\n" + 
			"		AND UPPER(f.parent_end_customer_name) LIKE UPPER(?) AND UPPER(f.product_name) LIKE UPPER(?)			\r\n" + 
			"		AND UPPER(f.market) LIKE UPPER(?) AND UPPER(f.sub_market) LIKE UPPER(?)			\r\n" + 
			"		AND UPPER(f.program_name) LIKE UPPER(?) AND UPPER(f.business_unit) LIKE UPPER(?)			\r\n" + 
			") s1 GROUP BY revenue_id,end_customer_id,parent_end_customer_name,year,product_name,market, sub_market, program_name, business_unit,data,seq		\r\n" + 
			"UNION\r\n" + 
			"SELECT  concat(revenue_id,':',year,':',end_customer_id),end_customer_id,parent_end_customer_name,market, sub_market, program_name, business_unit,product_name,data,seq,sum(Apr),sum(May),sum(Jun),0 q1,sum(Jul),\r\n" + 
			"					sum(Aug),sum(Sep),0 q2,sum(Oct),sum(Nov),sum(Dec1),0 q3,sum(Jan),sum(Feb),sum(Mar),0 q4,created_date \r\n" + 
			"FROM( \r\n" + 
			"		SELECT f.product_id revenue_id, f.end_customer_id, f.parent_end_customer_name, f.year,\r\n" + 
			"		f.market,f.sub_market,f.program_name,f.business_unit,f.product_name,'ASP ($/U)' AS data,2 AS 'seq', f.created_date,\r\n" + 
			"							CASE WHEN asp1 IS NULL THEN 0 ELSE asp1 END AS 'Apr',\r\n" + 
			"							CASE WHEN asp2 IS NULL THEN 0 ELSE asp2 END AS 'May',\r\n" + 
			"							CASE WHEN asp3 IS NULL THEN 0 ELSE asp3 END AS 'Jun',\r\n" + 
			"							CASE WHEN asp4 IS NULL THEN 0 ELSE asp4 END AS 'Jul',\r\n" + 
			"							CASE WHEN asp5 IS NULL THEN 0 ELSE asp5 END AS 'Aug',\r\n" + 
			"							CASE WHEN asp6 IS NULL THEN 0 ELSE asp6 END AS 'Sep',\r\n" + 
			"							CASE WHEN asp7 IS NULL THEN 0 ELSE asp7 END AS 'Oct',\r\n" + 
			"							CASE WHEN asp8 IS NULL THEN 0 ELSE asp8 END AS 'Nov',\r\n" + 
			"							CASE WHEN asp9 IS NULL THEN 0 ELSE asp9 END AS 'Dec1',\r\n" + 
			"							CASE WHEN asp10 IS NULL THEN 0 ELSE asp10 END AS 'Jan',\r\n" + 
			"							CASE WHEN asp11 IS NULL THEN 0 ELSE asp11 END AS 'Feb',\r\n" + 
			"							CASE WHEN asp12 IS NULL THEN 0 ELSE asp12 END AS 'Mar'\r\n" + 
			"		FROM forecast f\r\n" + 
			"		WHERE\r\n" + 
			"		f.year= ?\r\n" + 
			"		AND f.sales_rep_id= ?\r\n" + 
			"		AND UPPER(f.parent_end_customer_name) LIKE UPPER(?) AND UPPER(f.product_name) LIKE UPPER(?)			\r\n" + 
			"		AND UPPER(f.market) LIKE UPPER(?) AND UPPER(f.sub_market) LIKE UPPER(?)			\r\n" + 
			"		AND UPPER(f.program_name) LIKE UPPER(?) AND UPPER(f.business_unit) LIKE UPPER(?)			\r\n" + 
			") s1 GROUP BY revenue_id,end_customer_id,parent_end_customer_name,year,product_name,market, sub_market, program_name, business_unit,data,seq\r\n" + 
			"UNION\r\n" + 
			"SELECT  concat(revenue_id,':',year,':',end_customer_id),end_customer_id,parent_end_customer_name,market, sub_market, program_name, business_unit,product_name,data,seq,sum(Apr),sum(May),sum(Jun),0 q1,sum(Jul),\r\n" + 
			"					sum(Aug),sum(Sep),0 q2,sum(Oct),sum(Nov),sum(Dec1),0 q3,sum(Jan),sum(Feb),sum(Mar),0 q4,created_date \r\n" + 
			"FROM( \r\n" + 
			"		SELECT f.product_id revenue_id, f.end_customer_id, f.parent_end_customer_name, f.year,\r\n" + 
			"		f.market,f.sub_market,f.program_name,f.business_unit,f.product_name,'Forecast ($)' AS data,3 AS 'seq', f.created_date,\r\n" + 
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
			"		f.year= ?\r\n" + 
			"		AND f.sales_rep_id= ?\r\n" + 
			"		AND UPPER(f.parent_end_customer_name) LIKE UPPER(?) AND UPPER(f.product_name) LIKE UPPER(?)			\r\n" + 
			"		AND UPPER(f.market) LIKE UPPER(?) AND UPPER(f.sub_market) LIKE UPPER(?)			\r\n" + 
			"		AND UPPER(f.program_name) LIKE UPPER(?) AND UPPER(f.business_unit) LIKE UPPER(?)			\r\n" + 
			") s1 GROUP BY revenue_id,end_customer_id,parent_end_customer_name,year,product_name,market, sub_market, program_name, business_unit,data,seq\r\n" + 
//			" order by parent_end_customer,market, sub_market, program_name, business_unit,product_name,seq asc\r\n" + 
			"";				
	
	public static final String GET_FORECAST_BY_COMPOSITE_KEY = "GET_FORECAST_BY_COMPOSITE_KEY";
	public static final String UNPROCESSED_STAGING_FORECAST = "UNPROCESSED_STAGING_FORECAST";
	public static final String CUSTOMER_FORECAST_TOTAL_FOR_FORECAST ="  SELECT  concat(revenue_id,':',year,':',end_customer_id),end_customer_id,parent_end_customer_name,market, sub_market, program_name, business_unit,product_name,data,seq,sum(Apr),sum(May),sum(Jun),0 q1,sum(Jul),\r\n" + 
			"					sum(Aug),sum(Sep),0 q2,sum(Oct),sum(Nov),sum(Dec1),0 q3,sum(Jan),sum(Feb),sum(Mar),0 q4 \r\n" + 
			"FROM( \r\n" + 
			"		SELECT f.product_id revenue_id, f.end_customer_id, f.parent_end_customer_name, f.year,\r\n" + 
			"		f.market,f.sub_market,f.program_name,f.business_unit,f.product_name,'Customer Total ($)' AS data,4 AS 'seq',\r\n" + 
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
			"		f.year= ?\r\n" + 
			"		AND f.sales_rep_id= ?\r\n" + 
			"		AND f.end_customer_id= ?\r\n" + 
			")	s1	";
	public static final String GET_FORECAST_TOTAL_FOR_FORECAST = "SELECT sum(Jan),sum(Feb),sum(Mar),sum(Apr),sum(May),sum(Jun),sum(Jul),sum(Aug),sum(Sep),\r\n" + 
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
			"		AND f.sales_rep_id= ?	\r\n" + 
			") S1		";
	
	public static final String GET_USER_BY_USER_SIGN_IN_ID = "GET_USER_BY_USER_SIGN_IN_ID";
	
	public static final String GET_ACCOUNT_NAME_BY_OWNER = "GET_ACCOUNT_NAME_BY_OWNER";
	public static final String GET_PRODUCTS = "GET_PRODUCTS";
	public static final String GET_MARKET_SEGMENT_PICKLIST = "GET_MARKET_SEGMENT_PICKLIST";
	public static final String GET_SUB_SEGMENT_PICKLIST = "GET_SUB_SEGMENT_PICKLIST";
	public static final String GET_BUSINESS_UNIT_PICKLIST = "GET_BUSINESS_UNIT_PICKLIST";
	public static final String GET_SUB_SEGMENT_PICKLIST_BY_MARKET = "GET_SUB_SEGMENT_PICKLIST_BY_MARKET";
	public static final String GET_TOTAL_CUSTOMERS_FOR_FORECAST = "select count(DISTINCT parent_end_customer_name) from forecast where year=? AND sales_rep_id=? AND parent_end_customer_name LIKE ? AND market LIKE ? \r\n" + 
			"				AND sub_market LIKE ? AND program_name LIKE ? AND business_unit LIKE ? AND product_name LIKE ?";
	public static final String GET_LOCKED_FORECAST_LOCKING_INFO_BY_YEAR = "GET_LOCKED_FORECAST_LOCKING_INFO_BY_YEAR";
	
	public static final String GET_SALES_FORECAST_LOCK_BY_COMPOSITE_KEY = "GET_SALES_FORECAST_LOCK_BY_COMPOSITE_KEY";
	
	public static final String GET_FORECAST_OF_CURRENT_AND_NEXT_FISCAL_YEAR = "GET_FORECAST_OF_CURRENT_AND_NEXT_FISCAL_YEAR";
	public static final String GET_FORECAST_HISTORY_BY_COMPOSITE_KEY = "GET_FORECAST_HISTORY_BY_COMPOSITE_KEY";
	public static final String GET_USER_BY_ROW_ID = "GET_USER_BY_ROW_ID";
	public static final String UNPROCESSED_STAGING_FORECAST_HISTORY = "UNPROCESSED_STAGING_FORECAST_HISTORY";
	public static final String FORECAST_QUANTITY_LABEL = "Quantity (U)";
	public static final String FORECAST_ASP_LABEL = "ASP ($/U)";
	public static final String FORECAST_REVENUE_LABEL = "Forecast ($)";
	public static final String FORECAST_CUSTOMER_TOTAL_LABEL = "Customer Total ($)";	
	public static final String GET_PARENT_ACCOUNTS_BY_OWNER = "GET_PARENT_ACCOUNTS_BY_OWNER";
	public static final String INTEGRATION_USER = "Integration";
	public static final String GET_FORECAST_OF_OWNER_BY_FISCAL_YEAR = "GET_FORECAST_OF_OWNER_BY_FISCAL_YEAR";
	public static final String READ_ONLY_ACCESS = "ReadOnly";
	public static final String EDIT_ACCESS = "Edit";
	public static final String EDIT_ACCESS_ROW_ID = "EDIT_ACCESS_ROW_ID";
	public static final String UNPROCESSED_STAGING_FINANCE_FORECAST_HISTORY = "UNPROCESSED_STAGING_FINANCE_FORECAST_HISTORY";
	public static final String GET_FINANCE_FORECAST_OF_CURRENT_AND_NEXT_FISCAL_YEAR = "GET_FINANCE_FORECAST_OF_CURRENT_AND_NEXT_FISCAL_YEAR";
	public static final String GET_FINANCE_FORECAST_HISTORY_BY_COMPOSITE_KEY = "GET_FINANCE_FORECAST_HISTORY_BY_COMPOSITE_KEY";
	public static final String FORECAST_TYPE_FINANCE_FORECAST = "Finance Forecast";
	public static final String GET_DATA_FOR_FINANCE_FORECAST ="  SELECT  concat(revenue_id,':',year,':',end_customer_id),end_customer_id,parent_end_customer_name,market, sub_market, program_name, business_unit,product_name,data,seq,sum(Apr),sum(May),sum(Jun),0 q1,sum(Jul),\r\n" + 
			"					sum(Aug),sum(Sep),0 q2,sum(Oct),sum(Nov),sum(Dec1),0 q3,sum(Jan),sum(Feb),sum(Mar),0 q4,created_date \r\n" + 
			"FROM( \r\n" + 
			"		SELECT f.product_id revenue_id, f.end_customer_id, f.parent_end_customer_name, f.year,\r\n" + 
			"		f.market,f.sub_market,f.program_name,f.business_unit,f.product_name,'Quantity (U)' AS data,1 AS 'seq', f.created_date,\r\n" + 
			"							CASE WHEN quantity1 IS NULL THEN 0 ELSE (quantity1) END AS 'Apr',\r\n" + 
			"							CASE WHEN quantity2 IS NULL THEN 0 ELSE (quantity2)  END AS 'May',\r\n" + 
			"							CASE WHEN quantity3 IS NULL THEN 0 ELSE (quantity3)  END AS 'Jun',\r\n" + 
			"							CASE WHEN quantity4 IS NULL THEN 0 ELSE (quantity4)  END AS 'Jul',\r\n" + 
			"							CASE WHEN quantity5 IS NULL THEN 0 ELSE (quantity5)  END AS 'Aug',\r\n" + 
			"							CASE WHEN quantity6 IS NULL THEN 0 ELSE (quantity6)  END AS 'Sep',\r\n" + 
			"							CASE WHEN quantity7 IS NULL THEN 0 ELSE (quantity7)  END AS 'Oct',\r\n" + 
			"							CASE WHEN quantity8 IS NULL THEN 0 ELSE (quantity8)  END AS 'Nov',\r\n" + 
			"							CASE WHEN quantity9 IS NULL THEN 0 ELSE (quantity9)  END AS 'Dec1',\r\n" + 
			"							CASE WHEN quantity10 IS NULL THEN 0 ELSE (quantity10)  END AS 'Jan',\r\n" + 
			"							CASE WHEN quantity11 IS NULL THEN 0 ELSE (quantity11)  END AS 'Feb',\r\n" + 
			"							CASE WHEN quantity12 IS NULL THEN 0 ELSE (quantity12)  END AS 'Mar'\r\n" + 
			"		FROM finance_forecast f\r\n" + 
			"		WHERE\r\n" + 
			"		f.year= ?\r\n" + 
			//"		AND f.sales_rep_id= ?\r\n" + 
			"		AND f.month <> 'NA'		\r\n" + 
			"		AND UPPER(f.parent_end_customer_name) LIKE UPPER(?) AND UPPER(f.product_name) LIKE UPPER(?)			\r\n" + 
			"		AND UPPER(f.market) LIKE UPPER(?) AND UPPER(f.sub_market) LIKE UPPER(?)			\r\n" + 
			"		AND UPPER(f.program_name) LIKE UPPER(?) AND UPPER(f.business_unit) LIKE UPPER(?)			\r\n" + 
			") s1 GROUP BY revenue_id,end_customer_id,parent_end_customer_name,year,product_name,market, sub_market, program_name, business_unit,data,created_date,seq		\r\n" + 
			"UNION\r\n" + 
			"SELECT  concat(revenue_id,':',year,':',end_customer_id),end_customer_id,parent_end_customer_name,market, sub_market, program_name, business_unit,product_name,data,seq,sum(Apr),sum(May),sum(Jun),0 q1,sum(Jul),\r\n" + 
			"					sum(Aug),sum(Sep),0 q2,sum(Oct),sum(Nov),sum(Dec1),0 q3,sum(Jan),sum(Feb),sum(Mar),0 q4,created_date \r\n" + 
			"FROM( \r\n" + 
			"		SELECT f.product_id revenue_id, f.end_customer_id, f.parent_end_customer_name, f.year,\r\n" + 
			"		f.market,f.sub_market,f.program_name,f.business_unit,f.product_name,'ASP ($/U)' AS data,2 AS 'seq', f.created_date,\r\n" + 
			"							CASE WHEN asp1 IS NULL THEN 0 ELSE asp1 END AS 'Apr',\r\n" + 
			"							CASE WHEN asp2 IS NULL THEN 0 ELSE asp2 END AS 'May',\r\n" + 
			"							CASE WHEN asp3 IS NULL THEN 0 ELSE asp3 END AS 'Jun',\r\n" + 
			"							CASE WHEN asp4 IS NULL THEN 0 ELSE asp4 END AS 'Jul',\r\n" + 
			"							CASE WHEN asp5 IS NULL THEN 0 ELSE asp5 END AS 'Aug',\r\n" + 
			"							CASE WHEN asp6 IS NULL THEN 0 ELSE asp6 END AS 'Sep',\r\n" + 
			"							CASE WHEN asp7 IS NULL THEN 0 ELSE asp7 END AS 'Oct',\r\n" + 
			"							CASE WHEN asp8 IS NULL THEN 0 ELSE asp8 END AS 'Nov',\r\n" + 
			"							CASE WHEN asp9 IS NULL THEN 0 ELSE asp9 END AS 'Dec1',\r\n" + 
			"							CASE WHEN asp10 IS NULL THEN 0 ELSE asp10 END AS 'Jan',\r\n" + 
			"							CASE WHEN asp11 IS NULL THEN 0 ELSE asp11 END AS 'Feb',\r\n" + 
			"							CASE WHEN asp12 IS NULL THEN 0 ELSE asp12 END AS 'Mar'\r\n" + 
			"		FROM finance_forecast f\r\n" + 
			"		WHERE\r\n" + 
			"		f.year= ?\r\n" + 
			//"		AND f.sales_rep_id= ?\r\n" + 
			"		AND f.month <> 'NA'		\r\n" + 
			"		AND UPPER(f.parent_end_customer_name) LIKE UPPER(?) AND UPPER(f.product_name) LIKE UPPER(?)			\r\n" + 
			"		AND UPPER(f.market) LIKE UPPER(?) AND UPPER(f.sub_market) LIKE UPPER(?)			\r\n" + 
			"		AND UPPER(f.program_name) LIKE UPPER(?) AND UPPER(f.business_unit) LIKE UPPER(?)			\r\n" + 
			") s1 GROUP BY revenue_id,end_customer_id,parent_end_customer_name,year,product_name,market, sub_market, program_name, business_unit,data,created_date,seq\r\n" + 
			"UNION\r\n" + 
			"SELECT  concat(revenue_id,':',year,':',end_customer_id),end_customer_id,parent_end_customer_name,market, sub_market, program_name, business_unit,product_name,data,seq,sum(Apr),sum(May),sum(Jun),0 q1,sum(Jul),\r\n" + 
			"					sum(Aug),sum(Sep),0 q2,sum(Oct),sum(Nov),sum(Dec1),0 q3,sum(Jan),sum(Feb),sum(Mar),0 q4,created_date \r\n" + 
			"FROM( \r\n" + 
			"		SELECT f.product_id revenue_id, f.end_customer_id, f.parent_end_customer_name, f.year,\r\n" + 
			"		f.market,f.sub_market,f.program_name,f.business_unit,f.product_name,'Forecast ($)' AS data,3 AS 'seq', f.created_date,\r\n" + 
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
			"		FROM finance_forecast f\r\n" + 
			"		WHERE\r\n" + 
			"		f.year= ?\r\n" + 
			//"		AND f.sales_rep_id= ?\r\n" + 
			"		AND UPPER(f.parent_end_customer_name) LIKE UPPER(?) AND UPPER(f.product_name) LIKE UPPER(?)			\r\n" + 
			"		AND UPPER(f.market) LIKE UPPER(?) AND UPPER(f.sub_market) LIKE UPPER(?)			\r\n" + 
			"		AND UPPER(f.program_name) LIKE UPPER(?) AND UPPER(f.business_unit) LIKE UPPER(?)			\r\n" + 
			"		AND f.month <> 'NA'		\r\n" + 
			") s1 GROUP BY revenue_id,end_customer_id,parent_end_customer_name,year,product_name,market, sub_market, program_name, business_unit,data,created_date,seq\r\n" + 
//			" order by parent_end_customer,market, sub_market, program_name, business_unit,product_name,seq asc\r\n" + 
			"";				
	public static final String GET_FORECAST_TOTAL_FOR_FINANCE_FORECAST = "SELECT sum(Jan),sum(Feb),sum(Mar),sum(Apr),sum(May),sum(Jun),sum(Jul),sum(Aug),sum(Sep),\r\n" + 
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
			"		FROM finance_forecast f\r\n" + 
			"		WHERE\r\n" + 
			"		f.year=?\r\n" + 
			"		AND f.month != 'NA'	\r\n" + 
			") S1		";
	public static final String GET_FINANCE_FORECAST_BY_COMPOSITE_KEY = "GET_FINANCE_FORECAST_BY_COMPOSITE_KEY";
	public static final String FINANCE_FORECAST_QUANTITY_LABEL = "Quantity (U)";
	public static final String FINANCE_FORECAST_ASP_LABEL = "ASP ($/U)";
	public static final String GET_FINANCE_FORECAST_EXIST_BY_COMPOSITE_KEY = "GET_FINANCE_FORECAST_EXIST_BY_COMPOSITE_KEY";
	public static final String APRIL = "April" ;
	public static final String MAY = "May";
	public static final String JUNE = "June";
	public static final String JULY = "July";
	public static final String AUGUST = "August";
	public static final String SEPTEMBER = "September";
	public static final String OCTOBER = "October";
	public static final String NOVEMBER = "November";
	public static final String DECEMBER = "December";
	public static final String JANUARY = "January";
	public static final String FEBRUARY = "February";
	public static final String MARCH = "March";
	public static final String QUARTER1 = "Q1";
	public static final String QUARTER2 = "Q2";
	public static final String QUARTER3 = "Q3";
	public static final String QUARTER4 = "Q4";
	public static final String GET_LAST_COPIED_TIMESTAMP = "GET_LAST_COPIED_TIMESTAMP";
	public static final String GET_INVISIBLE_FINANCE_FORECAST_BY_COMPOSITE_KEY = "GET_INVISIBLE_FINANCE_FORECAST_BY_COMPOSITE_KEY";
	public static final String DEFAULT_FORECAST_MONTH = "NA";
	public static final String GET_SALES_FORECAST_BY_LAST_MODIFIED_DATE = "GET_SALES_FORECAST_BY_LAST_MODIFIED_DATE";
	public static final String GET_ALL_SALES_FORECAST = "GET_ALL_SALES_FORECAST";
	public static final String GET_FINANCE_FORECAST_EXIST_NOT_LOCKED = "GET_FINANCE_FORECAST_EXIST_NOT_LOCKED";
	public static final String GET_FINANCE_FORECAST_EXIST_BY_MONTH_SEQUENCE = "GET_FINANCE_FORECAST_EXIST_BY_MONTH_SEQUENCE";
	public static final String FORECAST_SSO_TOKEN = "FORECAST_SSO_TOKEN";
	public static final String ACTIVE = "Active";
	public static final String GET_INVENSENSE_PROPERTY_BY_NAME = "GET_INVENSENSE_PROPERTY_BY_NAME";
	public static final String GET_FORECAST_LOCK_BY_MONTH_SEQUENCE = "GET_FORECAST_LOCK_BY_MONTH_SEQUENCE";
	public static final String GET_IMMEDIATE_REPORTEE_USERS = "GET_IMMEDIATE_REPORTEE_USERS";
	public static final String GET_ACCOUNT_TEAM_BY_USER_ID = "GET_ACCOUNT_TEAM_BY_USER_ID";
	public static final String CHECK_ACCOUNT_IS_PARENT = "CHECK_ACCOUNT_IS_PARENT";
	public static final String ACCOUNT_TYPE_PARENT_CUSTOMER = "Parent Customer";
	public static final String SUBMIT_FOR_APPROVAL = "Submit For Approval";
	public static final String GET_FORECAST_BY_KEY_TO_DELETE = "GET_FORECAST_BY_KEY_TO_DELETE";
	public static final String CRM_ROLE_ADMINISTRATOR = "Administrator";
	public static final String GET_ALL_ACTIVE_USERS = "GET_ALL_ACTIVE_USERS";
	
	// OJDM - Opportunity team addition base on Market/Submarket
	public static final String TEAM_ROLE_PRODUCT_BU_MANAGER = "Product BU Manager";
	public static final String USER_ROLE_PRODUCT_MARKETING_MANAGER = "Product Marketing Manager";
	public static final String TEAM_OPPORTUNITY_ACCESS_OWNER = "Owner";
	public static final String USER_DETAILS_SERVICE_END_POINT = "userdetails.endpoint";
	public static final String CRMOD_QUERY_DATE_FORMAT = null;
}
