package com.invensense.action;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.invensense.model.FinanceForeCast;
import com.invensense.model.FinanceForecastExist;
import com.invensense.model.ForecastAttribute;
import com.invensense.model.ForecastData;
import com.invensense.model.LastCopiedTimestamp;
import com.invensense.model.SalesForecastLockInfo;
import com.invensense.model.User;
import com.invensense.service.EntityService;
import com.invensense.service.impl.FinanceForecastService;
import com.invensense.service.impl.ForecastService;
import com.invensense.service.impl.UserDetailsHcmService;
import com.invensense.util.CommonUtil;
import com.invensense.util.Constants;

import net.sf.json.JSONFunction;
import net.sf.json.JSONObject;

@Controller
public class FinanceForecastAction extends BaseAction{
	
	private Logger log = Logger.getLogger(FinanceForecastAction.class);

	@Resource
	private ForecastService forecastService ;
	
	@Resource
	private FinanceForecastService financeForecastService ;
	
	@Resource
	private EntityService entityService ;
	
//	@Resource
//	private CrmodService crmodService;

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

	@Resource
	private UserDetailsHcmService userDetailsHcmService;
	
	@RequestMapping(value="/createFinanceForecast")	
	public String createFinanceForecast(String ssoToken, String salesRepId, String parentSalesRepName,String sny, String action, Model model) throws Exception {
		//forecastService.copySalesForecastToFinanceForecast();
		model.addAttribute("createFinanceForecast","Y");

		Calendar calendar = Calendar.getInstance();
		String currentYear = getCurrentFiscalYear().toString();
		int currentMonth=calendar.get(Calendar.MONTH);
		int prevMonth = currentMonth-1;
		if(prevMonth<0)
			prevMonth = 11;
		
		FinanceForecastExist financeForecastExist = new FinanceForecastExist();
		financeForecastExist.setMonth(month[prevMonth]);
		financeForecastExist.setMonthSequence(CommonUtil.getFiscalYearMonthSequence(month[prevMonth]));
		financeForecastExist.setYear(currentYear);
		financeForecastExist.setRecordExist(Boolean.TRUE);
		financeForecastExist.setLocked(Boolean.FALSE);
		entityService.save(financeForecastExist);
		
		return viewFinanceForecast(ssoToken,salesRepId,parentSalesRepName,sny,action,model);
	}
	
	@RequestMapping(value="/viewFinanceForecast")	
	public String viewFinanceForecast(String ssoToken, String salesRepId, String parentSalesRepName,String sny, String action, Model model) throws Exception {
		log.debug("**** SSO token validation - Forecast UI **** ");		
		forecastService.persistSSOToken(ssoToken);
		// Validate SSO Token for Forecast UI.
//		if((ssoToken!=null && !ssoToken.equals("")) && (salesRepId!=null && !salesRepId.equals(""))) {
//			if (!CommonUtil.validateSSOToken(CRMODSessionManager.getCRMODSessionManager().getUrl(), ssoToken, salesRepId, action)) {
//	  			log.error("Error validating SSO Token");	  			
//	  			return "authenticationFailure";
//	  		}else{
			if(userDetailsHcmService.validateJwtToken(ssoToken, action)) {
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
				//forecastService.setChild(user,salesRepList,1,1,user);		
		
				Set<User> uniqueUser = new LinkedHashSet<User>(salesRepList);
				log.debug(" Total sales rep " + uniqueUser.size());
				model.addAttribute("salesRepList",uniqueUser);
				
				List<SalesForecastLockInfo> lockedForecastLockingInfoList = entityService.findByNameQuery(Constants.GET_LOCKED_FORECAST_LOCKING_INFO_BY_YEAR,
						new Object[] {getCurrentFiscalYear().toString(),Constants.FORECAST_TYPE_FINANCE_FORECAST});
				model.addAttribute("lockedForecastLockingInfo",lockedForecastLockingInfoList);
				
				Integer previousFiscalYear = getCurrentFiscalYear()-1;
				List<SalesForecastLockInfo> PreviousLockedForecastLockingInfoList = entityService.findByNameQuery(Constants.GET_LOCKED_FORECAST_LOCKING_INFO_BY_YEAR,
						new Object[] {previousFiscalYear.toString(),Constants.FORECAST_TYPE_FINANCE_FORECAST});
				model.addAttribute("PreviousLockedForecastLockingInfoList",PreviousLockedForecastLockingInfoList);
				
				List<SalesForecastLockInfo> financeForecastLockingInfo = null;
				financeForecastLockingInfo = entityService.findByNameQuery(Constants.GET_LOCKED_FORECAST_LOCKING_INFO_BY_YEAR,
						new Object[] {getCurrentFiscalYear().toString(),Constants.FORECAST_TYPE_FINANCE_FORECAST});
				
				model.addAttribute("disableCell","N");
				if(financeForecastLockingInfo.size()!=0){
					for(SalesForecastLockInfo financelockingInfo: financeForecastLockingInfo){
						int intCurrentMonth=calendar.get(Calendar.MONTH);
						int previousMonth = intCurrentMonth-1;
						if(previousMonth<0)
							previousMonth = 11;
						if(financelockingInfo.getMonth().equalsIgnoreCase(month[previousMonth]) && financelockingInfo.getYear().equalsIgnoreCase(getCurrentFiscalYear().toString())){
							model.addAttribute("disableCell","Y");
							break;
						}
					}
				}
				else{
					model.addAttribute("disableCell","N");
				}
				
				// 10-01-2013 Fix for forecast not showing correctly in January
//				if (sny != null && sny.equals("Y")) {
//					calendar.set(Calendar.YEAR,calendar.get(Calendar.YEAR)+2);
//				} else {
//					sny = "N" ;
//				}
//		
//				model.addAttribute("forecestYear",(calendar.get(Calendar.YEAR)+1));
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
				return "financeForecast";
//	  		}
		} else {
			log.error("SSOToken or userId is either NULL or Empty.");  			
  			return "authenticationFailure";
		}			
	}

	@RequestMapping(value="/getFinanceForecastData")	
	public String getFinanceForecastData(String request_locale,String salesRepId,String parentSalesRepName,String sny,
			String Customer, String BasePart,String page,String rows,String nd,
			String _search,String sidx,String sord,String Market,String SubMarket,String Program,String BU,String sortByDate,
			ModelMap model ) throws Exception {
		String returnString = "";
		try {
			log.info("In getFinanceForecastData");
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
			users = entityService.findByNameQuery(Constants.GET_USER_BY_USER_SIGN_IN_ID, vals);
			if(users!=null){
				user = users.get(0);
			}
			String userID =user.getId();

			// Get Forecast Data from db
			if (userID !=  null){
				log.info("Calling get Finance Forecast Data");
				StringBuilder sqlQuery = new StringBuilder(Constants.GET_DATA_FOR_FINANCE_FORECAST);
				sqlQuery.append(getOrderByClause(sidx,sord));
				Query q=entityService.getEntityManager().createNativeQuery(sqlQuery.toString());				
				log.info("year="+year);
				q.setParameter(1, year);
				//q.setParameter(2, user.getId());
				q.setParameter(2, Customer);
				q.setParameter(3, BasePart);
				q.setParameter(4, Market);
				q.setParameter(5, SubMarket);
				q.setParameter(6, Program);
				q.setParameter(7, BU);
				q.setParameter(8, year);
				//q.setParameter(10, user.getId());
				q.setParameter(9, Customer);
				q.setParameter(10, BasePart);
				q.setParameter(11, Market);
				q.setParameter(12, SubMarket);
				q.setParameter(13, Program);
				q.setParameter(14, BU);
				q.setParameter(15, year);
				//q.setParameter(18, user.getId());
				q.setParameter(16, Customer);
				q.setParameter(17, BasePart);
				q.setParameter(18, Market);
				q.setParameter(19, SubMarket);
				q.setParameter(20, Program);
				q.setParameter(21, BU);
				
				forecasts = q.getResultList();
				if(forecasts!=null && !forecasts.isEmpty()) {
					log.info("finance forecast data found");
				}   else {
					log.info("finance forecast data not found");
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
			      cellRowData=populateForecastData(currentMthFcst,row,"FINANCEFORECAST",0,startCol,null,cellRowData);
			      
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
		   String userData = financeForecastService.getRowTotalAsJson(year,user.getId());
		   log.debug("userData="+userData);
		   log.info("userData="+userData);
		   JSONObject jsonObject = JSONObject.fromObject(userData);
		   log.debug(" userdata Data" + jsonObject);
		   data.put("userdata",jsonObject);
		   
			List<SalesForecastLockInfo> lockedForecastLockingInfoList = entityService.findByNameQuery(Constants.GET_LOCKED_FORECAST_LOCKING_INFO_BY_YEAR,
					new Object[] {getCurrentFiscalYear().toString(),Constants.FORECAST_TYPE_FINANCE_FORECAST});
 			List<String> lockedMonths = new ArrayList<String>();
 			for(SalesForecastLockInfo salesForecastLockInfo: lockedForecastLockingInfoList){
 				lockedMonths.add(salesForecastLockInfo.getMonth());
 			}
 			data.put("lockedMonths",lockedMonths);
 			
 			Integer previousFiscalYear = getCurrentFiscalYear()-1;
 			List<SalesForecastLockInfo> prevLockedForecastLockingInfoList = entityService.findByNameQuery(Constants.GET_LOCKED_FORECAST_LOCKING_INFO_BY_YEAR,
					new Object[] {previousFiscalYear.toString(),Constants.FORECAST_TYPE_FINANCE_FORECAST});
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
			  //forecastService.createForecastIfRequired(salesRepId); 
		   }
		   
		}catch (Exception ex) {
			log.error(" Error while getting finance forecast data", ex);
			throw new Exception(ex);
		}
		return "financeForecastData";
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
	
	private Integer getCurrentFiscalYear() {
	    int currentMonth = Calendar.getInstance().get(Calendar.MONTH);
		return currentMonth < 3 ? Calendar.getInstance().get(Calendar.YEAR) : Calendar.getInstance().get(Calendar.YEAR) + 1;
	}
	
	@RequestMapping(value="/saveFinanceForecast")
	@ResponseBody
	public void saveFinanceForecast(String RevenueId ,String Customer ,String BasePart ,String Data ,
			 String Jan , String Feb , String Mar , String Apr , String May , String Jun , String Jul , String Aug ,String Sep , String Oct , String Nov , String Dec ,
			 String Program, String BU, String Market, String SubMarket, String sny
			 ) throws Exception {
		 String[] idArray=RevenueId.split(":");
		 String productId=idArray[0];
		 String year=idArray[1];
		 String customerId=idArray[2];
		 log.info("Saving Finance Forecast-"+" customerId="+customerId+" productId="+productId+" year="+year+" Market="+Market+" SubMarket="+SubMarket
				 +" Program="+Program+" BU="+BU);
	
		 List<FinanceForeCast> foreCastList = entityService.findByNameQuery(Constants.GET_FINANCE_FORECAST_BY_COMPOSITE_KEY, new Object[] {customerId, productId, year, Market, SubMarket, Program, BU});		 
		 if(foreCastList!=null && !foreCastList.isEmpty() && foreCastList.get(0)!=null) {
			 log.info("Finance Forecast record present in DB");
			 FinanceForeCast financeForeCast = foreCastList.get(0);
			 if(Data.equalsIgnoreCase(Constants.FINANCE_FORECAST_QUANTITY_LABEL)) {				 
				 financeForeCast.setQuantity1(Integer.valueOf(Apr.replace(",", "")));
				 financeForeCast.setQuantity2(Integer.valueOf(May.replace(",","")));
				 financeForeCast.setQuantity3(Integer.valueOf(Jun.replace(",","")));
				 financeForeCast.setQuantity4(Integer.valueOf(Jul.replace(",","")));
				 financeForeCast.setQuantity5(Integer.valueOf(Aug.replace(",","")));
				 financeForeCast.setQuantity6(Integer.valueOf(Sep.replace(",","")));
				 financeForeCast.setQuantity7(Integer.valueOf(Oct.replace(",","")));
				 financeForeCast.setQuantity8(Integer.valueOf(Nov.replace(",","")));
				 financeForeCast.setQuantity9(Integer.valueOf(Dec.replace(",","")));
				 financeForeCast.setQuantity10(Integer.valueOf(Jan.replace(",","")));
				 financeForeCast.setQuantity11(Integer.valueOf(Feb.replace(",","")));
				 financeForeCast.setQuantity12(Integer.valueOf(Mar.replace(",",""))); 
			 } else if(Data.equalsIgnoreCase(Constants.FINANCE_FORECAST_ASP_LABEL)) {
				 financeForeCast.setAsp1(BigDecimal.valueOf(Double.valueOf(Apr.replace(",",""))));
				 financeForeCast.setAsp2(BigDecimal.valueOf(Double.valueOf(May.replace(",",""))));
				 financeForeCast.setAsp3(BigDecimal.valueOf(Double.valueOf(Jun.replace(",",""))));
				 financeForeCast.setAsp4(BigDecimal.valueOf(Double.valueOf(Jul.replace(",",""))));
				 financeForeCast.setAsp5(BigDecimal.valueOf(Double.valueOf(Aug.replace(",",""))));
				 financeForeCast.setAsp6(BigDecimal.valueOf(Double.valueOf(Sep.replace(",",""))));
				 financeForeCast.setAsp7(BigDecimal.valueOf(Double.valueOf(Oct.replace(",",""))));
				 financeForeCast.setAsp8(BigDecimal.valueOf(Double.valueOf(Nov.replace(",",""))));
				 financeForeCast.setAsp9(BigDecimal.valueOf(Double.valueOf(Dec.replace(",",""))));
				 financeForeCast.setAsp10(BigDecimal.valueOf(Double.valueOf(Jan.replace(",",""))));
				 financeForeCast.setAsp11(BigDecimal.valueOf(Double.valueOf(Feb.replace(",",""))));
				 financeForeCast.setAsp12(BigDecimal.valueOf(Double.valueOf(Mar.replace(",",""))));
			 }
			 //financeForeCast.setSyncStep(null);
			 //financeForeCast.setSyncStatus(null);
			 entityService.save(financeForeCast);
		 }
	}
	
	@RequestMapping("/insertFinanceForecastLockInfo")
	@ResponseBody
	public String insertFinanceForecastLockInfo(String salesRepId, String selectedMonth) throws Exception {
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
				lstSalesForecastLockInfo = entityService.findByNameQuery(Constants.GET_SALES_FORECAST_LOCK_BY_COMPOSITE_KEY, new Object[] {getSelectedMonth[1], getSelectedMonth[0],Constants.FORECAST_TYPE_FINANCE_FORECAST});
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
					lockInfo.setForecastType(Constants.FORECAST_TYPE_FINANCE_FORECAST);
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
		
		updateFinanceForecastTransactionInfo();
		
		if(!lockedYearMonthMap.isEmpty()) {
			forecastService.takeFinanceForecastSnapshot(lockedYearMonthMap, salesRepId);
		}
		
		forecastService.deleteFianaceForecastSnapshotRecords();
		
		forecastService.syncFinanceForecastAfterSnapshot();
		
		if(!insertForecast){
			return "alreadyexists";
		}
		return "success";
	}
	
	public void updateFinanceForecastTransactionInfo() {
		Integer latestLockedMonth = forecastService.getLatestFinanceForecastLockedMonth();
		log.info("Latest lock sales forecast month is : " + latestLockedMonth);
		List<FinanceForecastExist> financeForecastExistList = entityService.findByNameQuery(Constants.GET_FINANCE_FORECAST_EXIST_BY_MONTH_SEQUENCE, new Object[] {latestLockedMonth});
		log.info("Finance forecast exist for latest locked sales forecast : " + financeForecastExistList.size());
		if(!financeForecastExistList.isEmpty()) {
			FinanceForecastExist financeForecastExist = financeForecastExistList.get(0);
			financeForecastExist.setLocked(Boolean.TRUE);
			entityService.save(financeForecastExist);
		}
	}

	@RequestMapping("/getFinanceForcastYearsandLockInfo")
	@ResponseBody
	public String getFinanceForcastYearsandLockInfo(String currentYear, String prevYear) throws Exception {
		
		List<FinanceForeCast> lstForecastInfo = entityService.findByNameQuery(Constants.GET_FINANCE_FORECAST_OF_CURRENT_AND_NEXT_FISCAL_YEAR, new Object[] {currentYear,prevYear});
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
		
		for(FinanceForeCast foreCast: lstForecastInfo){
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
					new Object[] {getCurrentFiscalYear().toString(),Constants.FORECAST_TYPE_FINANCE_FORECAST});
			
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
					new Object[] {previousFiscalYear.toString(),Constants.FORECAST_TYPE_FINANCE_FORECAST});
			
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
	}
	
	@RequestMapping("/takeFutureMonthUpdatesFromSalesForecast")
	@ResponseBody
	public String syncSalesForecastToFinanceForecast() throws Exception {
		log.info("Sync Sales Forecast to Finance Forecast");
		//String lastCopiedSalesForecastDate = getLastCopiedSalesForecastDate();
		forecastService.syncFinanceForecaseBasedOnLastCopiedTimestamp();
		return "success";
	}

	private String getLastCopiedSalesForecastDate() {
		List<LastCopiedTimestamp> lastCopiedTimestampList = entityService.findByNameQuery(Constants.GET_LAST_COPIED_TIMESTAMP, 
															new Object[] {Constants.FORECAST_TYPE_SALES_FORECAST});
		if(!lastCopiedTimestampList.isEmpty()) {
			LastCopiedTimestamp lastCopiedTimestamp = lastCopiedTimestampList.get(0);
			return lastCopiedTimestamp.getLastCopiedTimestampString();
		}
		return null;
	}
}
