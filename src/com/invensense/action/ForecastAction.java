package com.invensense.action;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.persistence.Query;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.invensense.model.Account;
import com.invensense.model.FinanceForecastExist;
import com.invensense.model.ForeCast;
import com.invensense.model.ForecastAttribute;
import com.invensense.model.ForecastData;
import com.invensense.model.PicklistLookup;
import com.invensense.model.Product;
import com.invensense.model.SalesForecastLockInfo;
import com.invensense.model.User;
import com.invensense.service.EntityService;
import com.invensense.service.impl.ForecastService;
import com.invensense.service.impl.SCForecastImpl;
import com.invensense.service.impl.UserDetailsHcmService;
import com.invensense.util.CommonUtil;
import com.invensense.util.Constants;
import com.invensense.ws.fusion.stubs.customObject.ForecastCustomC;

import net.sf.json.JSONFunction;
import net.sf.json.JSONObject;
@Controller
public class ForecastAction extends BaseAction {

	public static final String DELETE_SALES_FORECAST_QUERY_STRING = " delete from forecast where end_customer_id = ? and market = ? and sub_market = ? " +
			                                                   " and program_name = ? and business_unit = ? and product_id = ? and year = ? ";

	private static final String DELETE_FINANCE_FORECAST_QUERY_STRING = " delete from finance_forecast where end_customer_id = ? and market = ? and sub_market = ? " +
            														   " and program_name = ? and business_unit = ? and product_id = ? and month = 'NA'";

	private Logger log = Logger.getLogger(ForecastAction.class);

	@Resource
	private ForecastService forecastService ;

	@Resource
	private EntityService entityService ;

	@Resource
	private SCForecastImpl scForecastImpl ;
	
	private int rowId;

	public EntityService getEntityService() {
		return entityService;
	}

	public void setEntityService(EntityService entityService) {
		this.entityService = entityService;
	}

	public int getRowId() {
		return rowId;
	}

	public void setRowId(int rowId) {
		this.rowId = rowId;
	}

	@RequestMapping(value="/viewForecast")	
	public String viewForecast(String ssoToken, String salesRepId, String parentSalesRepName,String sny, String action, Model model) throws Exception {
		
		// Validate SSO Token for Forecast UI.
		if((salesRepId!=null && !salesRepId.equals(""))) {
			UserDetailsHcmService userDetailsHcmService = new UserDetailsHcmService();
			
			//if (!CommonUtil.validateSSOToken(CRMODSessionManager.getCRMODSessionManager().getUrl(), ssoToken, salesRepId, action)) {
			if(!userDetailsHcmService.validateJwtToken(ssoToken)) {
	  			log.error("Error validating SSO Token");	  			
	  			return "authenticationFailure";
	  		} else {
	  			log.debug("SSO Token was validated successfully....");
	  			
				log.info("salesRepId = "+ salesRepId);
				log.info("parentSalesRepName = "+ parentSalesRepName);
				log.info("sny = "+ sny);
				
				log.info("Parameters from URL : " + salesRepId + " , " + parentSalesRepName + " , " + sny);
		
				Calendar calendar = Calendar.getInstance();
				SimpleDateFormat df = new SimpleDateFormat("MMM-yy");
				String currentMonth = df.format(calendar.getTime());
				int currentMth=calendar.get(Calendar.MONTH);
		
		
				model.addAttribute("currentMonth",currentMonth);
				model.addAttribute("currentMth",currentMth);
				model.addAttribute("parentSalesRepName",parentSalesRepName);
				
				User user = null ;
				if (parentSalesRepName == null || parentSalesRepName.equalsIgnoreCase("")) {
					parentSalesRepName = salesRepId ;
					model.addAttribute("parentSalesRepName",parentSalesRepName);
					
					List<User> users=null;
					Object[] values = new Object[1];
					values[0]=salesRepId;
						
					log.info("Trying to get User with parameters 1 : " + values[0]);
					users = entityService.findByNameQuery(Constants.GET_USER_BY_USER_SIGN_IN_ID, values);
					if(users!=null && !users.isEmpty()){
						user=users.get(0);
					}		
				} else {
					List<User> users=null;
					Object[] values = new Object[1];
					values[0]=parentSalesRepName;
					
					log.info("Trying to get User with parameters 2 : " + values[0]);
					users = entityService.findByNameQuery(Constants.GET_USER_BY_USER_SIGN_IN_ID, values);
					if(users!=null && !users.isEmpty()){
						user=users.get(0);
					}
				}
		
				List<User> salesRepList = new LinkedList<User>();
				user.setBreadcrumName(user.getFirstName() + " " + user.getLastName());
				salesRepList.add(user);
				user.setHierarchyLevel(1);
				
				// check if user have lock permission
				if(StringUtils.isBlank(user.getCanLockSalesForecast())){
					model.addAttribute("canLockSalesForecast","");
				}
				else{
					model.addAttribute("canLockSalesForecast",user.getCanLockSalesForecast());
				}
				
				// Set SalesRep Hierarchy
				forecastService.setChild(user,salesRepList,1,1,user);		
		
				Set<User> uniqueUser = new LinkedHashSet<User>(salesRepList);
				log.debug(" Total sales rep " + uniqueUser.size());
				model.addAttribute("salesRepList",uniqueUser);
				
//				List<Account> accounts = entityService.findByNameQuery(Constants.GET_PARENT_ACCOUNTS_BY_OWNER, new Object[]{user.getId()});
//				model.addAttribute("accountList",accounts);
				
				List<Account> accounts = forecastService.getForecastParentAccountsByUser(user.getId());
				Set<Account> uniqueAccountList = new LinkedHashSet<Account>(accounts);
				model.addAttribute("accountList",uniqueAccountList);
				
				List<Product> products = entityService.findByNameQuery(Constants.GET_PRODUCTS);
				Collections.sort(products);
				model.addAttribute("productList",products);
				
				List<PicklistLookup> marketPicklistLookup = entityService.findByNameQuery(Constants.GET_MARKET_SEGMENT_PICKLIST);
				model.addAttribute("marketList",marketPicklistLookup);
				
				List<PicklistLookup> subMarketPicklistLookup = entityService.findByNameQuery(Constants.GET_SUB_SEGMENT_PICKLIST);
				Set<PicklistLookup> uniquesubMarketPicklistLookup = new LinkedHashSet<PicklistLookup>(subMarketPicklistLookup);
				model.addAttribute("subMarketList",uniquesubMarketPicklistLookup);
				
				List<PicklistLookup> buPicklistLookup = entityService.findByNameQuery(Constants.GET_BUSINESS_UNIT_PICKLIST);
				Collections.sort(buPicklistLookup);
				model.addAttribute("buList",buPicklistLookup);
				
				List<SalesForecastLockInfo> lockedForecastLockingInfoList = entityService.findByNameQuery(Constants.GET_LOCKED_FORECAST_LOCKING_INFO_BY_YEAR,
						new Object[] {getCurrentFiscalYear().toString(),Constants.FORECAST_TYPE_SALES_FORECAST});
				model.addAttribute("lockedForecastLockingInfo",lockedForecastLockingInfoList);
				
				Integer previousFiscalYear = getCurrentFiscalYear()-1;
				List<SalesForecastLockInfo> PreviousLockedForecastLockingInfoList = entityService.findByNameQuery(Constants.GET_LOCKED_FORECAST_LOCKING_INFO_BY_YEAR,
						new Object[] {previousFiscalYear.toString(),Constants.FORECAST_TYPE_SALES_FORECAST});
				model.addAttribute("PreviousLockedForecastLockingInfoList",PreviousLockedForecastLockingInfoList);
				showHideCreateViewFinance(model);
				
				// 10-01-2013 Fix for forecast not showing correctly in January
//				if (sny != null && sny.equals("Y")) {					
//					calendar.set(Calendar.YEAR,calendar.get(Calendar.YEAR)+2);
//				} else {
//					sny = "N" ;
//				}
				
				//model.addAttribute("forecestYear",(calendar.get(Calendar.YEAR)+1));
				sny = "N";
				model.addAttribute("forecestYear", getCurrentFiscalYear());
				model.addAttribute("sny",sny);
		
				ForecastAttribute forecast = new ForecastAttribute();
				LinkedList columns = new LinkedList();
		
				// set static columns
				columns.add("OpportunityId");
				columns.add("Customer");		
				columns.add("Market");
				columns.add("Sub Market");
				columns.add("Program");
				columns.add("BU");
				columns.add("Part #");
				columns.add("Data");
				columns.add("Seq");
		
				LinkedList columnDefs = new LinkedList();
		
				// set static column def
		
				LinkedHashMap columnDefOpportunityId= new LinkedHashMap();
				columnDefOpportunityId.put("name","RevenueId");
				columnDefOpportunityId.put("hidden",true);
				columnDefOpportunityId.put("search",false);
				columnDefs.add(columnDefOpportunityId);
		
				LinkedHashMap columnDefCust= new LinkedHashMap();
				columnDefCust.put("name","Customer");
				columnDefCust.put("width",85);
				columnDefCust.put("sortable",true);
				columnDefCust.put("search",true);
				columnDefCust.put("classes","no_border");
				columnDefs.add(columnDefCust);
		
				// set static column def
				LinkedHashMap columnDefSeg= new LinkedHashMap();
				columnDefSeg.put("name","Market");
				columnDefSeg.put("width",68);
				columnDefSeg.put("sortable",true);
				columnDefSeg.put("search",true);
				columnDefSeg.put("classes","no_border");
		//		columnDefSeg.put("align","right");
				columnDefs.add(columnDefSeg);
		
				// set static column def
				LinkedHashMap columnDefSubSeg= new LinkedHashMap();
				columnDefSubSeg.put("name","SubMarket");
				columnDefSubSeg.put("width",96);
				columnDefSubSeg.put("sortable",true);
				columnDefSubSeg.put("search",true);
				columnDefSubSeg.put("classes","no_border");
		//		columnDefSubSeg.put("align","right");
				columnDefs.add(columnDefSubSeg);
		
				// set static column def
				LinkedHashMap columnDefCCT= new LinkedHashMap();
				columnDefCCT.put("name","Program");
				columnDefCCT.put("width",80);
				columnDefCCT.put("search",true);
				columnDefCCT.put("classes","no_border");
				columnDefCCT.put("sortable",true);
		//		columnDefCCT.put("align","right");
				columnDefs.add(columnDefCCT);
		
				// set static column def
				LinkedHashMap columnDefCRI= new LinkedHashMap();
				columnDefCRI.put("name","BU");
				columnDefCRI.put("width",75);
				columnDefCRI.put("sortable",true);
				columnDefCRI.put("search",true);
				columnDefCRI.put("classes","no_border");
		//		columnDefCRI.put("align","right");
				columnDefs.add(columnDefCRI);
		
				// set static column def
				LinkedHashMap columnDefPart= new LinkedHashMap();
				columnDefPart.put("name","BasePart");
				columnDefPart.put("index","BasePart");
				columnDefPart.put("width",70);
				columnDefPart.put("sortable",true);
				columnDefPart.put("search",true);
				columnDefPart.put("classes","no_border");
				columnDefs.add(columnDefPart);
				
				// set static column def
				LinkedHashMap columnDefData= new LinkedHashMap();
				columnDefData.put("name","Data");
				columnDefData.put("width",70);
				columnDefData.put("sortable",false);
				columnDefData.put("search",false);
				columnDefs.add(columnDefData);
		
				LinkedHashMap columnDefSeq= new LinkedHashMap();
				columnDefSeq.put("name","seq");
				columnDefSeq.put("hidden",true);
				columnDefSeq.put("search",false);
				columnDefs.add(columnDefSeq);
		
				calendar.set(Calendar.MONTH, 3);
				df = new SimpleDateFormat("MMM");
				for (int i = 0 ; i < 12; i ++ ) {			
					calendar.set(Calendar.DAY_OF_MONTH,1);
					log.debug(calendar.get(Calendar.MONTH));
					
					String dataAsStr = df.format(calendar.getTime());
					String dataAsStrName=dataAsStr;
					log.debug ("dataAsStr"+dataAsStr);
		
					columns.add(dataAsStrName);
		
					HashMap columnDef = new HashMap();
					columnDef.put("index",dataAsStr);
					columnDef.put("name",dataAsStr);
					columnDef.put("editable",true);
					columnDef.put("editoptions","{dataInit: function (el) {validateNumeric(el);}}");
					columnDef.put("width",70);
					columnDef.put("sortable",false);
					columnDef.put("search",false);
					columnDef.put("align","right");
					columnDef.put("formatter",new JSONFunction(new String[] {"a","b","c"},"return formatCells(a,b,c);"));
		//			columnDef.put("formatter","number");
					columnDefs.add(columnDef);
					
					int month=calendar.get(Calendar.MONTH);
		
					
					LinkedHashMap columnDefQt= new LinkedHashMap();
					columnDefQt.put("editable",false);
					columnDefQt.put("classes","quarter_column");		
					columnDefQt.put("width",70);
					columnDefQt.put("sortable",false);
					columnDefQt.put("search",false);
					columnDefQt.put("align","right");
					columnDefQt.put("formatter",new JSONFunction(new String[] {"a","b","c"},"return computeQuarterTotal(a,b,c);"));
					
						if(month ==2){
							columns.add("Q4");
							columnDefQt.put("index","Q4");
							columnDefQt.put("name","Q4");
							columnDefs.add(columnDefQt);
						}else if(month == 5){
							columns.add("Q1");
							columnDefQt.put("index","Q1");
							columnDefQt.put("name","Q1");
							columnDefs.add(columnDefQt);
						}else if(month == 8){
							columns.add("Q2");
							columnDefQt.put("index","Q2");
							columnDefQt.put("name","Q2");
							columnDefs.add(columnDefQt);
						}else if(month == 11){
							columns.add("Q3");
							columnDefQt.put("index","Q3");
							columnDefQt.put("name","Q3");
							columnDefs.add(columnDefQt);
						}
		
					if(calendar.get(Calendar.MONTH)==12){
						calendar.set(Calendar.MONTH, 0);
					}else{
						calendar.add(Calendar.MONTH,1);
					}
				}
				
				columns.add("Total");
				
				LinkedHashMap columnDefTotal= new LinkedHashMap();
				columnDefTotal.put("name","Total");
				columnDefTotal.put("width",70);
				columnDefTotal.put("sortable",false);
				columnDefTotal.put("search",false);
				columnDefTotal.put("align","right");
				columnDefTotal.put("formatter",new JSONFunction(new String[] {"a","b","c"},"return computeTotal(a,b,c);"));
				columnDefs.add(columnDefTotal);
				
				columns.add("MarketH");
				columns.add("SubMarketH");
				columns.add("ProgramH");
				columns.add("BUH");
				
				LinkedHashMap columnDefMarketHidden= new LinkedHashMap();
				columnDefMarketHidden.put("name","MarketH");
				columnDefMarketHidden.put("hidden",true);
				columnDefMarketHidden.put("search",false);
				columnDefs.add(columnDefMarketHidden);
				
				LinkedHashMap columnDefSubMarketHidden= new LinkedHashMap();
				columnDefSubMarketHidden.put("name","SubMarketH");
				columnDefSubMarketHidden.put("hidden",true);
				columnDefSubMarketHidden.put("search",false);
				columnDefs.add(columnDefSubMarketHidden);
				
				LinkedHashMap columnDefProgramHidden= new LinkedHashMap();
				columnDefProgramHidden.put("name","ProgramH");
				columnDefProgramHidden.put("hidden",true);
				columnDefProgramHidden.put("search",false);
				columnDefs.add(columnDefProgramHidden);
				
				LinkedHashMap columnDefBUHidden= new LinkedHashMap();
				columnDefBUHidden.put("name","BUH");
				columnDefBUHidden.put("hidden",true);
				columnDefBUHidden.put("search",false);
				columnDefs.add(columnDefBUHidden);
				
				
				forecast.setColumns(columns);
				forecast.setColumnsDef(columnDefs);
				JSONObject jsonObject = JSONObject.fromObject(forecast);
				String str =  jsonObject.toString();
				model.addAttribute("forecast", jsonObject);
				return "forecast";
	  		}
		} else {
			log.error("SSOToken or userId is either NULL or Empty.");  			
  			return "authenticationFailure";
		} 			
	}
	@RequestMapping(value="/getForecastData")	
	public String getForecastData(String request_locale,String salesRepId,String parentSalesRepName,String sny,
			String Customer, String BasePart,String page,String rows,String nd,
			String _search,String sidx,String sord,String Market,String SubMarket,String Program,String BU,String sortByDate,
			ModelMap model ) throws Exception {
		String returnString = "";
		try {
			log.info("In getForecastData");
			log.info("salesRepId = "+ salesRepId);
			log.info("parentSalesRepName = "+ parentSalesRepName);
			log.info("sny = "+ sny);
			log.info("Customer = "+ Customer);
			log.info("BasePart = "+ BasePart);
			log.info("page = "+ page);
			log.info("rows = "+ rows);
			log.info("nd = "+ nd);
			log.info("_search = "+ _search);
			log.info("sidx = "+ sidx);
			log.info("sord = "+ sord);
			log.info("Market = "+ Market);
			log.info("SubMarket = "+ SubMarket);
			log.info("Program = "+ Program);
			log.info("BU = "+ BU);
			log.info("sortByDate = "+ sortByDate);
			
			rowId = 1 ;
			
			Calendar calendar = Calendar.getInstance();
			if (sny == null && sny.equals("")) {
				sny = "N" ;
			}		
			// 10-01-2013 Fix for forecast not showing correctly in January
//			int year = Calendar.getInstance().get(Calendar.YEAR)+1;
//			if (sny != null && sny.equals("Y")) {
//				year = Calendar.getInstance().get(Calendar.YEAR)+2;
//			} else if(sny != null && sny.equals("P")){
//				year = Calendar.getInstance().get(Calendar.YEAR);
//			}			
			int year = getCurrentFiscalYear();
			if(sny != null && sny.equals("Y")){
				year = year+1;
			}else if(sny != null && sny.equals("P")){
				year = year-1;
			}

			if (salesRepId == null) {
				salesRepId = "";
			}
			if (StringUtils.isBlank(Customer)) {
				Customer = "";
			}
			if (StringUtils.isBlank(Market)) {
				Market = "";
			}
			if (StringUtils.isBlank(SubMarket)) {
				SubMarket = "";
			}
			if (StringUtils.isBlank(Program)) {
				Program = "";
			}
			if (StringUtils.isBlank(BU)) {
				BU = "";
			}			
			if (StringUtils.isBlank(BasePart)) {
				BasePart = "";
			}
			if(StringUtils.isBlank(sord)){
				sord = "asc";
			}
			if(!StringUtils.isBlank(sortByDate) && "Y".equalsIgnoreCase(sortByDate)){
				sidx = "";
				sord = "asc";
			}
//			if(StringUtils.isBlank(sidx)){
//				sidx = "Customer";
//			}
			Customer = "%" +Customer +"%";
			BasePart = "%" +BasePart +"%";
			Market = "%" +Market +"%";
			SubMarket = "%" +SubMarket +"%";
			Program = "%" +Program +"%";
			BU = "%" +BU +"%";
					
			log.debug("get Forecast Data 1");
			LinkedHashMap data = new LinkedHashMap();
			List list = new LinkedList();
			List forecasts = new LinkedList();

			// Get user id
			Object[] vals = new Object[1];
			vals[0] = salesRepId;
			List<User> users = null;		
			User user = null;
			users = entityService.findByNameQuery(Constants.GET_USER_BY_USER_NAME, vals);
			if(users!=null){
				user = users.get(0);
			}
			String userID =user.getId();
			
			List<User> userHeirarchyList = forecastService.getUserHeirarchy(userID);
			addAllUsersForAdministrator(userHeirarchyList, userID);
			Set<User> uniqueUser = new LinkedHashSet<User>(userHeirarchyList);
	
			for (User salesUser : uniqueUser) {
				// Get Forecast Data from db
				if (salesUser.getId() !=  null){
					log.info("Calling get Forecast Data");
					StringBuilder sqlQuery = new StringBuilder(Constants.GET_DATA_FOR_FORECAST);
					sqlQuery.append(getOrderByClause(sidx,sord));
					Query q=entityService.getEntityManager().createNativeQuery(sqlQuery.toString());				
					log.info("year="+year);
					q.setParameter(1, year);
					q.setParameter(2, salesUser.getId());
					q.setParameter(3, Customer);
					q.setParameter(4, BasePart);
					q.setParameter(5, Market);
					q.setParameter(6, SubMarket);
					q.setParameter(7, Program);
					q.setParameter(8, BU);
					q.setParameter(9, year);
					q.setParameter(10, salesUser.getId());
					q.setParameter(11, Customer);
					q.setParameter(12, BasePart);
					q.setParameter(13, Market);
					q.setParameter(14, SubMarket);
					q.setParameter(15, Program);
					q.setParameter(16, BU);
					q.setParameter(17, year);
					q.setParameter(18, salesUser.getId());
					q.setParameter(19, Customer);
					q.setParameter(20, BasePart);
					q.setParameter(21, Market);
					q.setParameter(22, SubMarket);
					q.setParameter(23, Program);
					q.setParameter(24, BU);
					
					List forecastPerUser = q.getResultList();
					
					if(forecastPerUser!= null && !forecastPerUser.isEmpty()) {
						forecasts.addAll(forecastPerUser);
					}
					
//					if(forecasts!=null && !forecasts.isEmpty())
//						log.info("forecast data found");
//					else
//						log.info("forecast data not found");
				}
			}

			// create forecastData json object begin
			int rowPerPage = Integer.parseInt(rows);
			int pageAsInt = Integer.parseInt(page);
			int startRow = ((pageAsInt-1)*(rowPerPage));
			if (pageAsInt == 1)  {
				startRow = 0 ;
			}

			int endRow = startRow+((rowPerPage));
			int custRow=0;

		    String prevAccName=null;
			String fName = null;
			int currentMthFcst=calendar.get(Calendar.MONTH)+1;
			if (currentMthFcst > 12)
			  currentMthFcst=0;


		    for (int i =  startRow; (i < endRow && i < forecasts.size()) ; i++) {
				  Object[] row = (Object[]) forecasts.get(i);
				  if (row == null) {
					  continue ;
				  }

				  ForecastData  forecastData = new ForecastData();
  			  	  LinkedList cellRowData = new LinkedList();
  			  	  
				  String aName = "" ;
				  if (row[2] != null)	aName = row[2].toString();

			      if (row[8] != null)	fName = row[8].toString();
				  if (fName ==null)
					  fName="";
				  String pName = "" ;
				 if (row[7] != null) pName = row[7].toString();
				 cellRowData.add(row[0]);
				  if (i%3 == 0) {					 
					  cellRowData.add(row[2]);
					  cellRowData.add(row[3]);
					  cellRowData.add(row[4]);
					  cellRowData.add(row[5]);
					  cellRowData.add(row[6]);
					  cellRowData.add(row[7]);
				  } else {
					  cellRowData.add("");
					  cellRowData.add("");
					  cellRowData.add("");
					  cellRowData.add("");
					  cellRowData.add("");
					  cellRowData.add("");
				  }
				  cellRowData.add(row[8]);
				  cellRowData.add(row[9]);
				  int startCol = 10;
				  forecastData.setId((rowId++)+ "");
			      cellRowData=populateForecastData(currentMthFcst,row,"FORECAST",0,startCol,null,cellRowData);
			      
			      forecastData.setCell(cellRowData);
			      list.add(forecastData);
			      
		   }

		   // Set page number 
		   data.put("page",page);

		   int totalRecords = forecasts.size();   

		   if((totalRecords%rowPerPage) == 0){
			   data.put("total",Math.abs(totalRecords/rowPerPage));
		   }else{
			   data.put("total",Math.abs(totalRecords/rowPerPage)+1);
		   }	
		   log.debug(" Total rows " + totalRecords);
		   
		   // Set Total number of records
		   data.put("records",totalRecords);
		   
		   data.put("rows",list);
		   log.debug(" Total rows " + list.size());

		   // get total row
		   String userData = forecastService.getRowTotalAsJson(year,user.getId());
		   log.debug("userData="+userData);
		   log.info("userData="+userData);
		   JSONObject jsonObject = JSONObject.fromObject(userData);
		   log.debug(" userdata Data" + jsonObject);
		   data.put("userdata",jsonObject);
		   
			List<SalesForecastLockInfo> lockedForecastLockingInfoList = entityService.findByNameQuery(Constants.GET_LOCKED_FORECAST_LOCKING_INFO_BY_YEAR,
					new Object[] {getCurrentFiscalYear().toString(),Constants.FORECAST_TYPE_SALES_FORECAST});
 			List<String> lockedMonths = new ArrayList<String>();
 			for(SalesForecastLockInfo salesForecastLockInfo: lockedForecastLockingInfoList){
 				lockedMonths.add(salesForecastLockInfo.getMonth());
 			}
 			data.put("lockedMonths",lockedMonths);
 			
 			Integer previousFiscalYear = getCurrentFiscalYear()-1;
 			List<SalesForecastLockInfo> prevLockedForecastLockingInfoList = entityService.findByNameQuery(Constants.GET_LOCKED_FORECAST_LOCKING_INFO_BY_YEAR,
					new Object[] {previousFiscalYear.toString(),Constants.FORECAST_TYPE_SALES_FORECAST});
 			List<String> prevLockedMonths = new ArrayList<String>();
 			for(SalesForecastLockInfo salesForecastLockInfo: prevLockedForecastLockingInfoList){
 				prevLockedMonths.add(salesForecastLockInfo.getMonth());
 			}
 			data.put("prevLockedMonths",prevLockedMonths);

		   JSONObject forecastData = JSONObject.fromObject(data);
		   log.debug(" forecast Data" + forecastData);
		   model.addAttribute("forecastData", forecastData);
		   
		   Integer currentFiscalYear = getCurrentFiscalYear();
		   
		   if(currentFiscalYear == year) {
			  forecastService.createForecastIfRequired(salesRepId); 
		   }
		   
		}catch (Exception ex) {
			log.error(" Error while getting forecast data", ex);
			throw new Exception(ex);
		}
		return "forecastData";
	}

	private void addAllUsersForAdministrator(List<User> userHeirarchyList, String userID) {
		List<User> userList = entityService.findByNameQuery(Constants.GET_USER_BY_ROW_ID, new Object[] {userID});
		if(!userList.isEmpty()) {
			User user = userList.get(0);
			if(Constants.CRM_ROLE_ADMINISTRATOR.equals(user.getCrmodRole())) {
				List<User> activeUserList = entityService.findByNameQuery(Constants.GET_ALL_ACTIVE_USERS);
				userHeirarchyList.addAll(activeUserList);
			}
		}
	}

	private String getOrderByClause(String sidx, String sord) {
		if("Market".equals(sidx)){
			return " order by"+ " market "+sord+" ,"+" parent_end_customer_name "+sord+" ,"+" sub_market "+sord+" ,"
		+" program_name "+sord+" ,"+ " business_unit "+sord+" ,"+" product_name "+sord+" ,"+ " seq asc";
		}else if("SubMarket".equals(sidx)){
			return " order by"+" sub_market "+sord+" ,"+" parent_end_customer_name "+sord+" ,"+ " market "+sord+" ,"
		+" program_name "+sord+" ,"+ " business_unit "+sord+" ,"+" product_name "+sord+" ,"+ " seq asc";
		}else if("Program".equals(sidx)){
			return " order by"+" program_name "+sord+" ,"+" parent_end_customer_name "+sord+" ,"+ " market "+sord+" ,"
		+" sub_market "+sord+" ,"+ " business_unit "+sord+" ,"+" product_name "+sord+" ,"+ " seq asc";
		}else if("BU".equals(sidx)){
			return " order by"+ " business_unit "+sord+" ,"+" parent_end_customer_name "+sord+" ,"+ " market "+sord+" ,"
		+" sub_market "+sord+" ,"+" program_name "+sord+" ,"+" product_name "+sord+" ,"+ " seq asc";
		}else if("BasePart".equals(sidx)){
			return " order by"+" product_name "+sord+" ,"+" parent_end_customer_name "+sord+" ,"+ " market "+sord+" ,"
		+" sub_market "+sord+" ,"+" program_name "+sord+" ,"+ " business_unit "+sord+" ,"+ " seq asc";
		}else if("Customer".equals(sidx)){
			return " order by"+" parent_end_customer_name "+sord+" ,"+ " market "+sord+" ,"+" sub_market "+sord+" ,"
		+" program_name "+sord+" ,"+ " business_unit "+sord+" ,"+" product_name "+sord+" ,"+ " seq asc";
		}else{
			return " order by"+" created_date desc,"+" parent_end_customer_name "+sord+" ,"+ " market "+sord+" ,"+" sub_market "+sord+" ,"
		+" program_name "+sord+" ,"+ " business_unit "+sord+" ,"+" product_name "+sord+" ,"+ " seq asc";
		}
	}

	private LinkedList populateForecastData(int currentMthFcst,Object[] row){

          return populateForecastData(currentMthFcst,row,null,0,0,null,null);

	}

	private LinkedList populateForecastData(int currentMthFcst,Object[] row,String calledFrom,int k,int startCol,DecimalFormat df,LinkedList cellRowData){

        String prevAccName=null;
		String fName = null;
		if (calledFrom == null )
		  cellRowData = new LinkedList();
		String aName = "" ;
		int noCols =21;
		if (row[2] != null)	aName = row[2].toString();
	    boolean flag=true;
		int colSize;

		for (int col = startCol; col < row.length-1; col++) {
			//log.debug("row value "+col + "row value is "+ row[col]);
						if (col == 1){
							continue ;
						}
						if (col >= 2 && col < 8) {
							cellRowData.add("");
							continue ;
						}
				    	if (col == 9) {
				    		cellRowData.add("");
				    	} else{
			    			cellRowData.add(row[col]);			    								    			
				    	}
		 }
		
		 cellRowData.add("0"); // Total
	     cellRowData.add(row[3]); // Market Hidden
	     cellRowData.add(row[4]); // Sub Market Hidden
	     cellRowData.add(row[5]); // Program Hidden
	     cellRowData.add(row[6]); // BU Hidden
	      
		 return cellRowData;
	}

	private void populatePriorData(ForecastData data,int value) {
		for (int i = 0 ; i <= Calendar.getInstance().get(Calendar.MONTH); i ++ ) {
			data.getCell().add(value);
		}
	}

	private void setCustomerTotal(int year,List forecastData,Object[] forecastRow,String salesRepId,int currentMthFcst) {
		Object[] row = new Object[forecastRow.length] ;
		String[] data = new String[] {Constants.FORECAST_CUSTOMER_TOTAL_LABEL};
		String parentAccountId="";
		parentAccountId=forecastRow[1].toString();

	   log.debug ("header Id "+parentAccountId  + "year "+ year + "salesRepId "+salesRepId);

		String sqlQuery=Constants.CUSTOMER_FORECAST_TOTAL_FOR_FORECAST;
		Query q=entityService.getEntityManager().createNativeQuery(sqlQuery);
		int nxtYr=year+1;
		q.setParameter(1, year);
		q.setParameter(2, salesRepId);
		q.setParameter(3, parentAccountId);

		List customerTotalRowList = q.getResultList();
		log.debug(" Size Customer Total "  + customerTotalRowList.size());
		DecimalFormat df = new DecimalFormat("###0.000");
		for (int k = 0; k < 1 ; k++) {
			ForecastData  customerTotalData= new ForecastData();
			customerTotalData.setId((rowId++)+ "");

			LinkedList cellRowData = new LinkedList();
			cellRowData.add(forecastRow[0]);

			cellRowData.add("<span style='display:none'>"+forecastRow[2]+"</span>");
			cellRowData.add("<span style='display:none'>"+forecastRow[3]+"</span>");
			cellRowData.add("<span style='display:none'>"+forecastRow[4]+"</span>");
			cellRowData.add("<span style='display:none'>"+forecastRow[5]+"</span>");
			cellRowData.add("<span style='display:none'>"+forecastRow[6]+"</span>");
			cellRowData.add("<span style='display:none'>"+forecastRow[7]+"</span>");
			cellRowData.add(data[k]);
			cellRowData.add(forecastRow[9]);
			if (customerTotalRowList.size() > 0 && k < customerTotalRowList.size()) {
				row = (Object[]) customerTotalRowList.get(k);
				log.debug(" total cell " + row.length);
				int startCol=10;
				cellRowData=populateForecastData(currentMthFcst,row,"BACKLOG",k,startCol,df,cellRowData);

			} else {
				for (int col = 4; col < 17; col++) {
					cellRowData.add("");
				}
			}

			cellRowData.add(""); // total  column
			
			customerTotalData.setCell(cellRowData);
			forecastData.add(customerTotalData);
		}
	}

	@RequestMapping(value="/saveForecast")
	@ResponseBody
	public void saveForecast(String RevenueId ,String Customer ,String BasePart ,String Data ,
			 String Jan , String Feb , String Mar , String Apr , String May , String Jun , String Jul , String Aug ,String Sep , String Oct , String Nov , String Dec ,
			 String Program, String BU, String Market, String SubMarket, String sny
			 ) throws Exception {
		 String[] idArray=RevenueId.split(":");
		 String productId=idArray[0];
		 String year=idArray[1];
		 String customerId=idArray[2];
		 log.info("Saving Forecast-"+" customerId="+customerId+" productId="+productId+" year="+year+" Market="+Market+" SubMarket="+SubMarket
				 +" Program="+Program+" BU="+BU);
	
		 List<ForeCast> foreCastList = entityService.findByNameQuery(Constants.GET_FORECAST_BY_COMPOSITE_KEY, new Object[] {customerId, productId, year, Market, SubMarket, Program, BU});		 
		 if(foreCastList!=null && !foreCastList.isEmpty() && foreCastList.get(0)!=null) {
			 log.info("Forecast record present in DB");
			 ForeCast foreCast = foreCastList.get(0);
			 if(Data.equalsIgnoreCase(Constants.FORECAST_QUANTITY_LABEL)) {				 
				 foreCast.setQuantity1(Integer.valueOf(Apr.replace(",", "")));
				 foreCast.setQuantity2(Integer.valueOf(May.replace(",","")));
				 foreCast.setQuantity3(Integer.valueOf(Jun.replace(",","")));
				 foreCast.setQuantity4(Integer.valueOf(Jul.replace(",","")));
				 foreCast.setQuantity5(Integer.valueOf(Aug.replace(",","")));
				 foreCast.setQuantity6(Integer.valueOf(Sep.replace(",","")));
				 foreCast.setQuantity7(Integer.valueOf(Oct.replace(",","")));
				 foreCast.setQuantity8(Integer.valueOf(Nov.replace(",","")));
				 foreCast.setQuantity9(Integer.valueOf(Dec.replace(",","")));
				 foreCast.setQuantity10(Integer.valueOf(Jan.replace(",","")));
				 foreCast.setQuantity11(Integer.valueOf(Feb.replace(",","")));
				 foreCast.setQuantity12(Integer.valueOf(Mar.replace(",",""))); 
				 foreCast.setQuantity(BigDecimal.valueOf(foreCast.getQuantity1()+foreCast.getQuantity1()+foreCast.getQuantity2()+foreCast.getQuantity3()+foreCast.getQuantity4()+foreCast.getQuantity5()+foreCast.getQuantity6()+foreCast.getQuantity7()+foreCast.getQuantity8()+foreCast.getQuantity9()+foreCast.getQuantity10()));
			 } else if(Data.equalsIgnoreCase(Constants.FORECAST_ASP_LABEL)) {
				 BigDecimal sum = BigDecimal.ZERO;   
				 foreCast.setAsp1(BigDecimal.valueOf(Double.valueOf(Apr.replace(",",""))));
				 sum = sum.add(foreCast.getAsp1());
				 foreCast.setAsp2(BigDecimal.valueOf(Double.valueOf(May.replace(",",""))));
				 sum = sum.add(foreCast.getAsp2());
				 foreCast.setAsp3(BigDecimal.valueOf(Double.valueOf(Jun.replace(",",""))));
				 sum = sum.add(foreCast.getAsp3());
				 foreCast.setAsp4(BigDecimal.valueOf(Double.valueOf(Jul.replace(",",""))));
				 sum = sum.add(foreCast.getAsp4());
				 foreCast.setAsp5(BigDecimal.valueOf(Double.valueOf(Aug.replace(",",""))));
				 sum = sum.add(foreCast.getAsp5());
				 foreCast.setAsp6(BigDecimal.valueOf(Double.valueOf(Sep.replace(",",""))));
				 sum = sum.add(foreCast.getAsp6());
				 foreCast.setAsp7(BigDecimal.valueOf(Double.valueOf(Oct.replace(",",""))));
				 sum = sum.add(foreCast.getAsp7());
				 foreCast.setAsp8(BigDecimal.valueOf(Double.valueOf(Nov.replace(",",""))));
				 sum = sum.add(foreCast.getAsp8());
				 foreCast.setAsp9(BigDecimal.valueOf(Double.valueOf(Dec.replace(",",""))));
				 sum = sum.add(foreCast.getAsp9());
				 foreCast.setAsp10(BigDecimal.valueOf(Double.valueOf(Jan.replace(",",""))));
				 sum = sum.add(foreCast.getAsp10());
				 foreCast.setAsp11(BigDecimal.valueOf(Double.valueOf(Feb.replace(",",""))));
				 sum = sum.add(foreCast.getAsp11());
				 foreCast.setAsp12(BigDecimal.valueOf(Double.valueOf(Mar.replace(",",""))));
				 sum = sum.add(foreCast.getAsp12());
				 foreCast.setAsp(sum);

			 }
			 foreCast.setSyncStep(null);
			 foreCast.setSyncStatus(null);
			 foreCast.setModifiedBy(Constants.INTEGRATION_USER);
			 foreCast.setModifiedDate(new Date());
			 entityService.save(foreCast);
			 forecastService.syncFinanceForecastFromSalesForecast(foreCast);
		 }
	}
	
	
	@RequestMapping("/createForecast")
	@ResponseBody
	public String createForecast(String loginSalesRepId, String customerId, String marketSegment, String subMarket, String program, String bu, String productId) throws Exception {
		log.info("Request parameters from URL to create Forecast are : CustomerId " + customerId + " , Product Id " + productId + " , Login Sales Rep Id : " + loginSalesRepId);
		Integer currentFiscalYear = getCurrentFiscalYear();
		List<ForeCast> forecastList = entityService.findByNameQuery(Constants.GET_FORECAST_BY_COMPOSITE_KEY, 
									  new Object[] {customerId, productId, String.valueOf(currentFiscalYear), marketSegment, subMarket, program, bu});
		if(!forecastList.isEmpty()) {
			return "error";
		} else {
			loginSalesRepId = getSalesRepRowId(loginSalesRepId);
			ForeCast currentFiscalYearForeCast = forecastService.getForeCastRecord(customerId, productId, currentFiscalYear, marketSegment, subMarket, program, bu, loginSalesRepId);
			ForeCast nextFiscalYearForeCast = forecastService.getForeCastRecord(customerId, productId, currentFiscalYear + 1, marketSegment, subMarket, program, bu, loginSalesRepId);
			entityService.save(currentFiscalYearForeCast);
			entityService.save(nextFiscalYearForeCast);
			//foreCastSyncJob.executeJob();
			forecastService.syncFinanceForecastFromSalesForecast(currentFiscalYearForeCast);
			forecastService.syncFinanceForecastFromSalesForecast(nextFiscalYearForeCast);
		}
		return "success";
	}

	private String getSalesRepRowId(String loginSalesRepId) {
		List<User> userList = entityService.findByNameQuery(Constants.GET_USER_BY_USER_SIGN_IN_ID, new Object[] {loginSalesRepId});
		if(!userList.isEmpty()) {
			User user = userList.get(0);
			return user.getId();
		}
		return null;
	}

	private Integer getCurrentFiscalYear() {
	    int currentMonth = Calendar.getInstance().get(Calendar.MONTH);
		return currentMonth < 3 ? Calendar.getInstance().get(Calendar.YEAR) : Calendar.getInstance().get(Calendar.YEAR) + 1;
	}
	
	@RequestMapping(value="/deleteForecast")
	@ResponseBody
	public List<String> deleteForecast(@RequestBody List<String> deletedRecordKeys) {
		log.info("Forecast delete key is : " + deletedRecordKeys.toString());
		List<String> result = new ArrayList<String>();
		if(!deletedRecordKeys.isEmpty()) {
			for (String deleteKey : deletedRecordKeys) {
				try {
					List<ForeCast> deleteForecastList = getDeletedForecastList(deleteKey);
					for (ForeCast deleteForecast : deleteForecastList) {
						List<ForeCast> forecastList = entityService.findByNameQuery(Constants.GET_FORECAST_BY_KEY_TO_DELETE, new Object[] 
								  					 { deleteForecast.getEndCustomerId(), deleteForecast.getMarket(), deleteForecast.getSubMarket(),
								  					   deleteForecast.getProgramName(), deleteForecast.getBusinessUnit(), deleteForecast.getProductId(), 
								  					   deleteForecast.getYear() });
						if(!forecastList.isEmpty()) {
							deleteForcastFromCRM(forecastList);
							deleteForecastFromStagingDatabase(deleteForecast);
						}
					}
					markPreviousYearForecastClosed(deleteForecastList.get(0));
				} catch(Exception ex) {
					log.error("Error occurred while deleting Forecast record : " + ex);
					result.add("error");
				}
			}
		   result.add("success");
		}
		log.info("Return result : " + result.toString());
		return result;
	 }
	
	private void markPreviousYearForecastClosed(ForeCast deleteForecast) {
		List<ForeCast> forecastList = entityService.findByNameQuery(Constants.GET_FORECAST_BY_COMPOSITE_KEY ,
									  new Object[] { deleteForecast.getEndCustomerId(), deleteForecast.getProductId(), String.valueOf(Integer.parseInt(deleteForecast.getYear()) - 1), 
													 deleteForecast.getMarket(), deleteForecast.getSubMarket(), deleteForecast.getProgramName(), deleteForecast.getBusinessUnit()});
		if(!forecastList.isEmpty()) {
			ForeCast forecast = forecastList.get(0);
			forecast.setClosed(Boolean.TRUE);
			entityService.save(forecast);
		}
	}

	private void deleteForcastFromCRM(List<ForeCast> forecastList) throws Exception {
		try {
			for (ForeCast foreCast : forecastList) {
				//if(StringUtils.isNotBlank(foreCast.getRowId())) {
					ForeCast deleteForecast = new ForeCast();
					deleteForecast.setRowId(foreCast.getRowId());
					if(null!=foreCast.getRowId())
					{
						ForecastCustomC forecast_Custom_c = new ForecastCustomC();
						forecast_Custom_c.setId(new BigDecimal(foreCast.getRowId()));
						scForecastImpl.delete(forecast_Custom_c);
					}
					
					//crmodService.delete(deleteForecast);
				//}
			}
			log.info("Forecast record delete successfully from CRM-OD");
		} catch(Exception ex) {
			log.error("Error occurred while deleting record from CRM-OD", ex);
			throw new Exception(ex);
		}
	}

	private void deleteForecastFromStagingDatabase(ForeCast deleteForecast) throws Exception {
		try {
			deleteSalesForecast(deleteForecast);
			deleteLatestFinanceForecast(deleteForecast);
		} catch(Exception ex) {
			log.error("Error occurred while deleting record from staging database", ex);
			throw new Exception(ex);
		}
	}
	
	private void deleteSalesForecast(ForeCast deleteForecast) throws Exception {
		Connection connection = null;
		PreparedStatement statement = null;
		try {
			connection = entityService.getConnection();
			statement = connection.prepareStatement(DELETE_SALES_FORECAST_QUERY_STRING);
			statement.setString(1, deleteForecast.getEndCustomerId());
			statement.setString(2, deleteForecast.getMarket());
			statement.setString(3, deleteForecast.getSubMarket());
			statement.setString(4, deleteForecast.getProgramName());
			statement.setString(5, deleteForecast.getBusinessUnit());
			statement.setString(6, deleteForecast.getProductId());
			statement.setString(7, deleteForecast.getYear());
			
			int salesForecastDeletedRows = statement.executeUpdate();
	
			log.info("Number of sales forecast rows deleted are : " + salesForecastDeletedRows);
		} catch(Exception ex) {
			log.error("Error occurred while deleting sales forecast record from staging database", ex);
			throw new Exception(ex);
		} finally {
			if(statement != null) {
				try {
					statement.close();
				} catch(Exception ex) {
				}
			}
			if(connection != null) {
				try {
					connection.close();
				} catch(Exception ex) {
				}
			}
		}
	}

	private void deleteLatestFinanceForecast(ForeCast deleteForecast) throws Exception {
		Connection connection = null;
		PreparedStatement statement = null;
		try {
			connection = entityService.getConnection();
			statement = connection.prepareStatement(DELETE_FINANCE_FORECAST_QUERY_STRING);
			statement.setString(1, deleteForecast.getEndCustomerId());
			statement.setString(2, deleteForecast.getMarket());
			statement.setString(3, deleteForecast.getSubMarket());
			statement.setString(4, deleteForecast.getProgramName());
			statement.setString(5, deleteForecast.getBusinessUnit());
			statement.setString(6, deleteForecast.getProductId());
			
			int financeForecastDeletedRows = statement.executeUpdate();
	
			log.info("Number of finance forecast rows deleted are : " + financeForecastDeletedRows);
		} catch(Exception ex) {
			log.error("Error occurred while deleting finance forecast record from staging database", ex);
			throw new Exception(ex);
		} finally {
			if(statement != null) {
				try {
					statement.close();
				} catch(Exception ex) {
				}
			}
			if(connection != null) {
				try {
					connection.close();
				} catch(Exception ex) {
				}
			}
		}
	}

	private List<ForeCast> getDeletedForecastList(String deleteKey) {
		List<ForeCast> deleteForecastList = new ArrayList<ForeCast>();
		ForeCast foreCast1 = getForecastFromKey(deleteKey);
		ForeCast foreCast2 = getNextYearForecast(foreCast1);
		deleteForecastList.add(foreCast1);
		deleteForecastList.add(foreCast2);
		return deleteForecastList;
	}

	private ForeCast getNextYearForecast(ForeCast foreCast1) {
		ForeCast foreCast2 = new ForeCast();
		foreCast2.setEndCustomerId(foreCast1.getEndCustomerId());
		foreCast2.setMarket(foreCast1.getMarket());
		foreCast2.setSubMarket(foreCast1.getSubMarket());
		foreCast2.setProgramName(foreCast1.getProgramName());
		foreCast2.setBusinessUnit(foreCast1.getBusinessUnit());
		foreCast2.setProductId(foreCast1.getProductId());
		foreCast2.setYear(String.valueOf(Integer.parseInt(foreCast1.getYear()) + 1));
		return foreCast2;
	}

	private ForeCast getForecastFromKey(String deleteKey) {
		String[] deleteKeys = deleteKey.split("\\|");
		ForeCast foreCast = new ForeCast();
		foreCast.setEndCustomerId(deleteKeys[0]);
		foreCast.setMarket(deleteKeys[1]);
		foreCast.setSubMarket(deleteKeys[2]);
		foreCast.setProgramName(deleteKeys[3]);
		foreCast.setBusinessUnit(deleteKeys[4]);
		foreCast.setProductId(deleteKeys[5]);
		foreCast.setYear(deleteKeys[6]);
		return foreCast;
	}

	@RequestMapping("/getSubSegmentList")
	@ResponseBody
	public String getSubSegmentList(String marketSegment) throws Exception {
		try {
			List<PicklistLookup> subSegmentList = entityService.findByNameQuery(Constants.GET_SUB_SEGMENT_PICKLIST_BY_MARKET, new Object[] {marketSegment});
			HashMap  mapData  = new HashMap();
			mapData.put("subSegmentList", subSegmentList);
			ObjectMapper mapper = new ObjectMapper();
			String data = mapper.writeValueAsString(mapData);
			return data;
		} catch(Exception ex) {
			log.error("Unable to fetch Sub-Segment List : " , ex);
			return "error";
		}
	}
	
	@RequestMapping("/insertForecastLockInfo")
	@ResponseBody
	public String insertForecastLockInfo(String salesRepId, String selectedMonth) throws Exception {
		if(StringUtils.isBlank(salesRepId) || StringUtils.isBlank(selectedMonth)){
			log.debug("sales rep Id or selected month or year cannot be blank");
			return "error";
		}
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy"); 
		selectedMonth = selectedMonth.replaceFirst(",", "");
		System.out.println("selectedMonth: "+selectedMonth);
		System.out.println("salesRepId: "+salesRepId);
		
		Object[] vals = new Object[1];
		vals[0] = salesRepId;
		List<User> users = null;	
		User user = null;
		users = entityService.findByNameQuery(Constants.GET_USER_BY_USER_NAME, vals);
		if(users!=null){
			user = users.get(0);
		}
		
		String[] getSelectedMonthandYear = selectedMonth.split(",");
		boolean insertForecast = false;
		//String dupeMonth = "";
		
		List<String> monthList = new ArrayList<String>();
		
		Map<String, List<String>> lockedYearMonthMap = new HashMap<String, List<String>>();
		
		for(int mnth=0;mnth<getSelectedMonthandYear.length;mnth++){
		//	System.out.println(getSelectedMonthandYear[mnth]);
			String[] getSelectedMonth = getSelectedMonthandYear[mnth].split("--");
			//System.out.println(getSelectedMonth[0]);
			//System.out.println(getSelectedMonth[1]);
			
			List<SalesForecastLockInfo> lstSalesForecastLockInfo = null;
			try{
				lstSalesForecastLockInfo = entityService.findByNameQuery(Constants.GET_SALES_FORECAST_LOCK_BY_COMPOSITE_KEY, new Object[] {getSelectedMonth[1], getSelectedMonth[0],Constants.FORECAST_TYPE_SALES_FORECAST});
				/*
				if(lstSalesForecastLockInfo.size()>0){
					insertForecast = false;
					dupeMonth = dupeMonth + ", " + getSelectedMonth[mnth];
				}*/
				
				if(lstSalesForecastLockInfo.size()==0) {
					insertForecast = true;
					SalesForecastLockInfo lockInfo = new SalesForecastLockInfo();
					lockInfo.setLocked(true);
					lockInfo.setLockedBy(user.getUserSignInId());
					lockInfo.setMonth(getSelectedMonth[1]);
					lockInfo.setMonthSequence(CommonUtil.getFiscalYearMonthSequence(getSelectedMonth[1]));
					lockInfo.setYear(getSelectedMonth[0]);
					lockInfo.setLockedDate(sdf.parse(sdf.format(new Date())));
					lockInfo.setForecastType(Constants.FORECAST_TYPE_SALES_FORECAST);
					entityService.save(lockInfo);
					
					
					List<String> lockedMonthsList = lockedYearMonthMap.get(getSelectedMonth[0]);
					 if(lockedMonthsList == null) {
						 List<String> list = new ArrayList<String>();
						 list.add(getSelectedMonth[1]);
						 lockedYearMonthMap.put(getSelectedMonth[0], list);
					 } else {
						 lockedMonthsList.add(getSelectedMonth[1]);
						 lockedYearMonthMap.put(getSelectedMonth[0], lockedMonthsList);
					 }
				}
			}
			catch(Exception e){
				log.error("Error while saving forecast lock info", e);
				return "error";
			}
		}
		
		if(!lockedYearMonthMap.isEmpty()) {
			forecastService.takeSalesForecastSnapshot(lockedYearMonthMap, salesRepId);
		}
		
		forecastService.updateFinanceForecastWithLockedMonth();
		
		if(!insertForecast){
			return "alreadyexists";
		}
		return "success";
	}
	
	@RequestMapping("/getForcastYearsandLockInfo")
	@ResponseBody
	public String getForcastYearsandLockInfo(String currentYear, String prevYear) throws Exception {
		
		try {
			//prevYear=String.valueOf(Integer.parseInt(prevYear)+2);
			List<ForeCast> lstForecastInfo = entityService.findByNameQuery(Constants.GET_FORECAST_OF_CURRENT_AND_NEXT_FISCAL_YEAR, new Object[] {currentYear,prevYear});
			List<SalesForecastLockInfo> lockedForecastLockingInfo = null;
			List<SalesForecastLockInfo> PreviousLockedForecastLockingInfoList = null;
			String lockForecastData = "";
			boolean foundRecords = false;
			boolean foundCurrentRecords = false;
			boolean foundPrevRecords = false;
			int noofmonthstolock = 11;
			Calendar calendar = Calendar.getInstance();
			SimpleDateFormat df = new SimpleDateFormat("MMM-yy");
			String strCurrentMonth = df.format(calendar.getTime());
			
			int currentMonth=calendar.get(Calendar.MONTH);
			int lockyear = getCurrentFiscalYear();
			String lockmonth = "";
			String displaylockmonth = "";
			String displaylockmonth1 = "";
			String lockmonth1 = "";
			String[] month = {"January",
			"February",
			"March",
			"April",
			"May",
			"June",
			"July",
			"August",
			"September",
			"October",
			"November",
			"December"};
			
			for(ForeCast foreCast: lstForecastInfo){
				if(foundRecords)
					break;
				else{
					if(!foundCurrentRecords){
						if(foreCast.getYear().equalsIgnoreCase(currentYear)){
							foundCurrentRecords = true;
						}
					}
					if(!foundPrevRecords){
						if(foreCast.getYear().equalsIgnoreCase(prevYear)){
							foundPrevRecords = true;
						}
					}
					if(foundCurrentRecords && foundPrevRecords) {
						foundRecords = true;
						break;
					}
				}
			}
			
			
			if(foundCurrentRecords){
				lockedForecastLockingInfo = entityService.findByNameQuery(Constants.GET_LOCKED_FORECAST_LOCKING_INFO_BY_YEAR,
						new Object[] {getCurrentFiscalYear().toString(),Constants.FORECAST_TYPE_SALES_FORECAST});
				
				String lockedMonths = "";
				for(SalesForecastLockInfo salesForecastLockInfo: lockedForecastLockingInfo){
					lockedMonths = lockedMonths + "," + salesForecastLockInfo.getYear() + "--" +salesForecastLockInfo.getMonth();
				}
				
				for(int j=1;j<=noofmonthstolock;j++){
					int lock = currentMonth-j;
					if(lock<0){
						currentMonth = 12;
						lock = currentMonth-j;
					}
					boolean checkedbox = false;
					if(lockedMonths!= "" && lockedMonths.indexOf(lockyear+"--"+month[lock])!=-1){
						checkedbox = true;
					}
					
					if(lock>=0){
						if(lock==2){ 
							break;
						}
		
						if(lock==0){ currentMonth=12+j;}
						if(!checkedbox){
							//lockmonth = "<input type='checkbox' id='lockmonth' name='lockmonth' value='"+month[lock]+"'>&nbsp;"+month[lock]+"&nbsp;&nbsp;"+lockmonth;
							displaylockmonth = month[lock] + ", " + displaylockmonth;
							lockmonth = lockmonth + "," +lockyear +"--" +month[lock];
						}
					}
				}
				if(StringUtils.isNotBlank(displaylockmonth))
					displaylockmonth = "<br>FY: "+lockyear + " - " +displaylockmonth;
			}

			if(foundPrevRecords) {
				Integer previousFiscalYear = getCurrentFiscalYear()-1;
				PreviousLockedForecastLockingInfoList = entityService.findByNameQuery(Constants.GET_LOCKED_FORECAST_LOCKING_INFO_BY_YEAR,
						new Object[] {previousFiscalYear.toString(),Constants.FORECAST_TYPE_SALES_FORECAST});
				
				String lockedMonths = "";
				for(SalesForecastLockInfo salesForecastLockInfo: PreviousLockedForecastLockingInfoList){
					lockedMonths = lockedMonths + "," + salesForecastLockInfo.getYear() + "--" +salesForecastLockInfo.getMonth();
				}
				
				int startMonth = 2;
				lockyear = getCurrentFiscalYear()-1;
				for(int j=0;j<=noofmonthstolock;j++){
					int lock = startMonth-j;
					boolean checkedbox = false;
					if(lockedMonths!= "" && lockedMonths.indexOf(lockyear+"--"+month[lock])!=-1){
						checkedbox = true;
					}
					
					if(lock>=0){
						if(lock==2){ 
							//break;
						}
		
						if(lock==0){ startMonth=12+j;}
						if(!checkedbox){
							//lockmonth = "<input type='checkbox' id='lockmonth' name='lockmonth' value='"+month[lock]+"'>&nbsp;"+month[lock]+"&nbsp;&nbsp;"+lockmonth;
							displaylockmonth1 = month[lock] + ", " + displaylockmonth1;
							lockmonth1 = lockmonth1 + "," +lockyear +"--" +month[lock];
						}
					}
				}
				
				if(StringUtils.isNotBlank(displaylockmonth1))
					displaylockmonth1 = "<br>FY: "+lockyear + " - " +displaylockmonth1;
			}
			if(!foundCurrentRecords && !foundPrevRecords){
				lockForecastData = "ERROR::NoForecastData";
				return lockForecastData;
			}
			
			lockmonth = lockmonth + lockmonth1;
			if(StringUtils.isBlank(lockmonth)){
				if(foundCurrentRecords || foundPrevRecords)
					lockForecastData = "ERROR::NoForecastData";
				else
					lockForecastData = "ERROR::PreviousMonthLocked";
			}
			else{
				if(StringUtils.isBlank(displaylockmonth))
					displaylockmonth = displaylockmonth1;
				else if(StringUtils.isBlank(displaylockmonth1)){}
				else{
					StringBuilder b = new StringBuilder(displaylockmonth);
					b.replace(displaylockmonth.lastIndexOf(","), displaylockmonth.lastIndexOf(",") + 1, "" );
					displaylockmonth = b.toString();
					displaylockmonth = displaylockmonth + displaylockmonth1;
				}
				
				lockForecastData = "INFO::"+lockmonth+"**"+displaylockmonth;
			}
			
			
			return lockForecastData;
			
		} catch(Exception ex) {
			log.error("Exception occurred in getting year and lock info : ", ex);
			return "";
		}
	}
	
	private void showHideCreateViewFinance(Model model) {
		
		try {
		
		String showButton = "";
		Calendar calendar = Calendar.getInstance();
		String currentYear = getCurrentFiscalYear().toString();
		int currentMonth=calendar.get(Calendar.MONTH);
		int previousMonth=currentMonth-1;
		if(previousMonth<0)
			previousMonth = 11;
		
		String[] month = {"January",
		"February",
		"March",
		"April",
		"May",
		"June",
		"July",
		"August",
		"September",
		"October",
		"November",
		"December"};
		
		List<SalesForecastLockInfo> lockedForecastLockingInfo = null;
		lockedForecastLockingInfo = entityService.findByNameQuery(Constants.GET_LOCKED_FORECAST_LOCKING_INFO_BY_YEAR,
				new Object[] {getCurrentFiscalYear().toString(),Constants.FORECAST_TYPE_SALES_FORECAST});
		
		if(lockedForecastLockingInfo.size()==0){
			// dont show create and view finance button
			showButton = "NO";
		}
		else{
			for(SalesForecastLockInfo lockingInfo: lockedForecastLockingInfo){
				log.info("Locked Month : " + lockingInfo.getMonth() + " , Locking Year : " + lockingInfo.getYear());
				if(month[previousMonth].equalsIgnoreCase(lockingInfo.getMonth()) && currentYear.equalsIgnoreCase(lockingInfo.getYear()))  {
				/*if(lockingInfo.getMonth().equalsIgnoreCase(month[previousMonth]) && lockingInfo.getYear().equalsIgnoreCase(currentYear)) */ 
					showButton = "CREATE";
					break;
				}
			}
		}
		
		List<SalesForecastLockInfo> lockedFinanceForecastLockingInfo = null;
		lockedFinanceForecastLockingInfo = entityService.findByNameQuery(Constants.GET_LOCKED_FORECAST_LOCKING_INFO_BY_YEAR,
				new Object[] {getCurrentFiscalYear().toString(),Constants.FORECAST_TYPE_FINANCE_FORECAST});
		
		if(lockedFinanceForecastLockingInfo.size()!=0){
			for(SalesForecastLockInfo financelockingInfo: lockedFinanceForecastLockingInfo){
				if(financelockingInfo.getMonth().equalsIgnoreCase(month[previousMonth]) && financelockingInfo.getYear().equalsIgnoreCase(currentYear)){
					showButton = "VIEW";
					break;
				}
			}
		}
		
		if(showButton.equalsIgnoreCase("CREATE")){
			List<FinanceForecastExist> financeForecastExists = null;
			financeForecastExists = entityService.findByNameQuery(Constants.GET_FINANCE_FORECAST_EXIST_BY_COMPOSITE_KEY,
					new Object[] {getCurrentFiscalYear().toString(),month[currentMonth]});
			
			if(financeForecastExists.size()!=0){
				showButton = "VIEW";
			}
		}
		
		
		Integer salesForecastLockedMonth = forecastService.getLatestSalesForecastLockedMonth();
		Integer financeForecastLockedMonth = forecastService.getLatestFinanceForecastLockedMonth();
		
		if(salesForecastLockedMonth == financeForecastLockedMonth) {
			showButton = "NO";
		} else if(salesForecastLockedMonth > financeForecastLockedMonth) {
			List<FinanceForecastExist> financeForecastExistList = entityService.findByNameQuery(Constants.GET_FINANCE_FORECAST_EXIST_BY_MONTH_SEQUENCE, new Object[] {salesForecastLockedMonth});
			 if(financeForecastExistList.isEmpty()) {
				 showButton = "CREATE"; 
			 } else {
				 showButton = "VIEW";
			 }
		} 
		
		model.addAttribute("showButton",showButton);
		} catch(Exception ex) {
			log.error("Exception occurred in show hide create view finance ", ex);
		}
	}
	
}