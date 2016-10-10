package com.invensense.service.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.persistence.Query;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.invensense.model.Account;
import com.invensense.model.AccountTeamWS1;
import com.invensense.model.FinanceForeCast;
import com.invensense.model.FinanceForeCastHistory;
import com.invensense.model.ForeCast;
import com.invensense.model.ForeCastHistory;
import com.invensense.model.InvensenseProperty;
import com.invensense.model.Product;
import com.invensense.model.SalesForecastLockInfo;
import com.invensense.model.User;
import com.invensense.service.EntityService;
import com.invensense.sync.SyncFinanceForecast;
import com.invensense.sync.impl.SyncFinanceForecastDefault;
import com.invensense.util.CommonUtil;
import com.invensense.util.Constants;
import com.invensense.util.FinanceForecastSyncProcessorMap;

@Service
public class ForecastService {
	
	private static Logger log = Logger.getLogger(ForecastService.class);
	
	private static final String[] months = {"January", "February",
		      								"March", "April", "May", "June", "July",
		      								"August", "September", "October", "November",
		      								"December"};
	
	private static final String COPY_SALES_FORECAST_TO_FINANCE_FORCAST_QUERY_STRING = 
			" insert into finance_forecast " +
			" (end_customer_id, business_unit, market, sub_market, product_id, program_name, year, " +
			" name, sales_forecast_row_id, asp, quantity, revenue, forecast_type, product_name, part_number, " +
			" product_category, parent_end_customer, parent_end_customer_name, sales_rep_id," +
			" owner, role, asp1, asp2, asp3, asp4, asp5, asp6, asp7, asp8, asp9,asp10, asp11,asp12," +
			" quantity1, quantity2, quantity3, quantity4, quantity5, quantity6, quantity7," +
			" quantity8, quantity9, quantity10, quantity11, quantity12, created_by, created_date," +
			" modified_by, modified_date) " +
			" select end_customer_id, business_unit, market, sub_market, product_id, program_name, year, " +
			" name, row_id, asp, quantity, revenue, 'Finance Forecast', product_name, part_number, " +
			" product_category, parent_end_customer, parent_end_customer_name, sales_rep_id, " +
			" owner, role, asp1, asp2, asp3, asp4, asp5, asp6, asp7, asp8, asp9,asp10, asp11,asp12," +
			" quantity1, quantity2, quantity3, quantity4, quantity5, quantity6, quantity7," +
			" quantity8, quantity9, quantity10, quantity11, quantity12, 'Integration', now()," +
			" 'Integration', now() from forecast" +
			" where year = ? or year = ?" +
			" on duplicate key " +
			" update asp1 = values (asp1), asp2 = values (asp2), asp3 = values (asp3), " +
			" asp4 = values (asp4), asp5 = values (asp5), asp6 = values (asp6)," +
			" asp7 = values (asp7), asp8 = values (asp8), asp9 = values (asp9)," +
			" asp10 = values (asp10), asp11 = values (asp11), asp12 = values (asp12)," +
			" quantity1 = values (quantity1), quantity2 = values (quantity2), " +
			" quantity3 = values (quantity3), quantity4 = values (quantity4), " +
			" quantity5 = values (quantity5), quantity6 = values (quantity6), " +
			" quantity7 = values (quantity7), quantity8 = values (quantity8), " +
			" quantity9 = values (quantity9), quantity10 = values (quantity10), " +
			" quantity11 = values (quantity11), quantity12 = values (quantity12), " +
			" modified_by = 'Integration', modified_date = now();";

	private static final String GET_LAST_LOCKED_SALES_FORECAST_MONTH = 
			" select month from sales_forecast_lock_info" +
			" where year = ? and forecast_type = 'Sales Forecast' and locked_date in (select max(locked_date) from sales_forecast_lock_info);";

	private static final String GET_LAST_COPIED_SALES_FORECAST_TIMESTAMP = 
			" select last_copied_timestamp from last_copied_timestamp where forecast_type = 'Sales Forecast';";

	private static final String UPDATE_FINANCE_FORECAST_WITH_LOCKED_MONTH_QUERY_STRING = "update finance_forecast set month = ? where month = 'NA'";

	private static final String DELETE_FINANCE_FORECAST_SNAPSHOT_RECORDS_QUERY_STRING = " delete from finance_forecast where month != 'NA'";

	private static final String GET_LATEST_SALES_FORECAST_LOCKED_MONTH_QUERY_STRING = 
			"select max(month_sequence) from sales_forecast_lock_info " + 
			" where year = ? and forecast_type = 'Sales Forecast'";
	
	private static final String GET_LATEST_FINANCE_FORECAST_LOCKED_MONTH_QUERY_STRING = 
			"select max(month_sequence) from sales_forecast_lock_info " + 
			" where year = ? and forecast_type = 'Finance Forecast'";
	
	@Resource
	private EntityService entityService ;  
	
//	@Resource
//	private ForeCastSyncJob foreCastSyncJob;
//	
//	@Resource
//	private CrmodService crmodService;
//	
	@Resource
	private FinanceForecastSyncProcessorMap financeForecastSyncProcessorMap;
	
	@Autowired
	private ApplicationContext applicationContext;
	
	@Resource
	private SyncFinanceForecastDefault syncFinanceForecastDefault;

	public EntityService getEntityService() {
		return entityService;
	}

	public void setEntityService(EntityService entityService) {
		this.entityService = entityService;
	}

	public boolean inList(List<User> allChildrens,String id) {
		boolean retValue = false ;
		for (User user : allChildrens) {
			if (user.getId().equals(id)) {
				return true ;
			}
		}
		return retValue ;
	}
	
	public List<User>  setChild(User user,List<User> allChildrens,int count,int level,User parent){
//		List<User>  users = baseService.getObjectsByNamedQuery("GetSalesRepForManager", user.getCrmodRowId());
		String[] params= new String[1];
		params[0]= user.getId();
		List<User>  users = (List<User>)entityService.findByNameQuery("GetSalesRepForManager", params);
		count = count+ 1;
		if (users == null || users.isEmpty()) {
			//level = 1 ;
		}
		for(User childUser : users) {
			if (inList(allChildrens,childUser.getId())) {
				continue ;
			}
			if (childUser.getReportsTo() != null && !childUser.getReportsTo().equals("")) {
				for (int i = 1 ; i <= parent.getHierarchyLevel() ; i++) {
					childUser.setBreadcrumName("&nbsp;&nbsp;&nbsp;"+ childUser.getBreadcrumName());	
				}
				childUser.setBreadcrumName(childUser.getBreadcrumName()+ childUser.getFirstName() + " " + childUser.getLastName());
				level =level+1;
				childUser.setHierarchyLevel(parent.getHierarchyLevel()+1);
			} 
			allChildrens.add(childUser);
			if (!childUser.getId().equals(childUser.getReportsTo())) {
				if (count <= 1000) {
					setChild(childUser,allChildrens,count,level,childUser);	
				}
			}
		}
		return allChildrens ;
	}
	
	public List<User> getUserListByHierarchy(User user,List<User> allChildrens,int count,int level,User parent) {
		String[] params= new String[1];
		params[0]= user.getId();
		List<User>  users = (List<User>)entityService.findByNameQuery("GetSalesRepForManager", params);
		count = count+ 1;
		if (users == null || users.isEmpty()) {
			//level = 1 ;
		}
		for(User childUser : users) {
			if (inList(allChildrens,childUser.getId())) {
				continue ;
			}
			if (childUser.getReportsTo() != null && !childUser.getReportsTo().equals("")) {
//				for (int i = 1 ; i <= parent.getHierarchyLevel() ; i++) {
//					childUser.setBreadcrumName("&nbsp;&nbsp;&nbsp;"+ childUser.getBreadcrumName());	
//				}
//				childUser.setBreadcrumName(childUser.getBreadcrumName()+ childUser.getFirstName() + " " + childUser.getLastName());
				level =level+1;
				childUser.setHierarchyLevel(parent.getHierarchyLevel()+1);
			} 
			allChildrens.add(childUser);
			if (!childUser.getId().equals(childUser.getReportsTo())) {
				if (count <= 1000) {
					getUserListByHierarchy(childUser,allChildrens,count,level,childUser);	
				}
			}
		}
		return allChildrens ;
	}
	
	public List getForecastTotal(int year,String salesRepRowId){
		//return baseService.getObjectsByNamedQuery(Constants.GET_FORECAST_TOTAL, salesRepRowId,salesRepRowId);
//		return baseService.getObjectsByNamedQuery(Constants.GET_FORECAST_TOTAL,year, salesRepRowId);
		String getForecastTotalQuery = getForecastTotalQueryByUserRollUp(salesRepRowId);
		//String getForecastTotalQuery = Constants.GET_FORECAST_TOTAL_FOR_FORECAST;
		Query q=entityService.getEntityManager().createNativeQuery(getForecastTotalQuery);
		int nxtYr=year+1;
		q.setParameter(1, year);
//		q.setParameter(2, nxtYr);
		//q.setParameter(2, salesRepRowId);
		return q.getResultList();
	}
		
	public String getForecastTotalQueryByUserRollUp(String salesRepRowId) {
		String salesRepConditionQueryString = getForecadtTotalSalesRepConditionString(salesRepRowId);
		String queryString = CommonUtil.getForecastTotalQuery(salesRepConditionQueryString);
		return queryString;
	}

	private String getForecadtTotalSalesRepConditionString(String salesRepRowId) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("(");
		List<User> userList = getUserHeirarchy(salesRepRowId);
		for (int i = 0; i < userList.size() ; i++) {
			User user = userList.get(i);
			 if(i == (userList.size() - 1)) {
				 buffer.append("f.sales_rep_id= '" + user.getId() + "'");
			 } else {
				 buffer.append("f.sales_rep_id= '" + user.getId() + "' OR ");
			 }
		}
		buffer.append(")");
		return buffer.toString();
	}

	public String getRowTotalAsJson(int year,String salesRepRowId){
		StringBuffer grandTotalRow= new StringBuffer("{"); 
		
//		List<Object[]> objs = getForecastTotalByUserHierarchy(salesRepRowId, year);
//		System.out.println("Object length : " + objs.size());
		List<Object[]> objs = getForecastTotal(year,salesRepRowId);
		Double total = null;
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat df = new SimpleDateFormat("MMM");
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		//DecimalFormat f = new DecimalFormat("###0");
		Double qTotal = new Double(0);
		grandTotalRow.append("\"Customer\":\"\""+",");
		grandTotalRow.append("\"BasePart\":\"\""+",");
		grandTotalRow.append("\"Segment\":\"\""+",");
		grandTotalRow.append("\"SubSegment\":\"\""+",");
		grandTotalRow.append("\"CCT\":\"\""+",");
		grandTotalRow.append("\"CRI\":\"\""+",");
		
		if (objs != null && !objs.isEmpty()) {
			for (Object[] o : objs) {
			//Object[] o =  objs.get(0);
//			log.info("Currently processing ");
//			for (Object object : o) {
//				log.info("Object : " + object);
//			}
			log.debug(o);
			for (int i = 0 ; i < 12; i ++) {
				if (o[i] != null) {
					if (total == null) {
						total = new Double(0) ;
					}
					try {
						total = total + Double.parseDouble(o[i].toString()) ;
						qTotal = qTotal + Double.parseDouble(o[i].toString()) ;
					} catch (NumberFormatException e) {
						log.error(" Error parsing " + o[i].toString(),e);
					}
				}
				calendar.set(Calendar.MONTH,i);
				String dataAsStr = df.format(calendar.getTime());
				//grandTotalRow.append(dataAsStr + ":"+ o[i] + ",");
				if (o[i] != null) {
					try {
						grandTotalRow.append("\"" + dataAsStr + "\":\"" + o[i].toString() + "\",");
					} catch (NumberFormatException e) {
						log.warn(" Error parsing " + o[i].toString());
						try {
							grandTotalRow.append("\"" + dataAsStr + "\":\"" + o[i].toString() + "\",");
						} catch (Exception e2) {
							log.error(" Error while converting to parsing " + o[i].toString(),e);
						}
					}	
				} else {
					grandTotalRow.append("\"" + dataAsStr + "\":" + null + ",");
				}
				if(i==2){
					grandTotalRow.append("\"Q4\":\"" + BigDecimal.valueOf(qTotal).setScale(1,BigDecimal.ROUND_HALF_UP) + "\",");
					qTotal = 0.0;
				}else if(i==5){
					grandTotalRow.append("\"Q1\":\"" + BigDecimal.valueOf(qTotal).setScale(1,BigDecimal.ROUND_HALF_UP) + "\",");
					qTotal = 0.0;
				}else if(i==8){
					grandTotalRow.append("\"Q2\":\"" + BigDecimal.valueOf(qTotal).setScale(1,BigDecimal.ROUND_HALF_UP) + "\",");
					qTotal = 0.0;
				}else if(i==11){
					grandTotalRow.append("\"Q3\":\"" + BigDecimal.valueOf(qTotal).setScale(1,BigDecimal.ROUND_HALF_UP) + "\",");
					qTotal = 0.0;
				}			
			}
		}
	}
		if (total != null) {
			grandTotalRow.append("\"Total\":\"" + BigDecimal.valueOf(total).setScale(1,BigDecimal.ROUND_HALF_UP) + "\",");	
		} else {
			grandTotalRow.append("\"Total\":" +null +",");
		}
//		grandTotalRow.append("\"Q1\":" + f.format(0) +",");
//		grandTotalRow.append("\"Q2\":" + f.format(0) +",");
//		grandTotalRow.append("\"Q3\":" + f.format(0) +",");
//		grandTotalRow.append("\"Q4\":" + f.format(0) +",");
		grandTotalRow.append("\"Data\":\"Grand Total:\"");
		System.out.println(grandTotalRow.toString()+"}");
		return grandTotalRow.toString() +"}";
	}
	
	private List<Object[]> getForecastTotalByUserHierarchy(String salesRepRowId, int year) {
		List<Object[]> objs = new ArrayList<Object[]>();
		List<User> usrList = getUserHeirarchy(salesRepRowId);
		for (User user : usrList) {
			List<Object[]> forecastTotal = getForecastTotal(year, user.getId());
			 if(forecastTotal != null && !forecastTotal.isEmpty()) {
				 objs.addAll(forecastTotal); 
			 }
		}
		return objs;
	}

	public void takeSalesForecastSnapshot(Map<String, List<String>> lockedYearMonthMap, String salesRepId) throws Exception {
		//Sync Pending Forecast records before taking snapshots 
//		foreCastSyncJob.executeJob();
		log.info("Locked Year Month Map is : " + lockedYearMonthMap.toString());
		for (String year : lockedYearMonthMap.keySet()) {
			log.info("Currently processing FY Year : " + year + " for Locking");
			List<ForeCast> foreCastList = entityService.findByNameQuery(Constants.GET_FORECAST_OF_CURRENT_AND_NEXT_FISCAL_YEAR, 
										  new Object[] { year, String.valueOf(Integer.parseInt(year) + 1)});
			log.info("Got Number of forecast are : " + foreCastList.size());
			List<String> monthList = lockedYearMonthMap.get(year);
			log.info("Month(s) for FY Year : " + year + " for Locking are : " + monthList.toString());
			for (ForeCast foreCast : foreCastList) {
				for (String month : monthList) {
					List<ForeCastHistory> foreCastHistoryList = entityService.findByNameQuery(Constants.GET_FORECAST_HISTORY_BY_COMPOSITE_KEY, 
																new Object[] {foreCast.getRowId(), month, foreCast.getYear()});
						if(foreCastHistoryList.isEmpty()) {
							persistForecastHistory(foreCast, month, salesRepId);
						}
					}
				}	
			}
		
//		Integer currentFiscalYear = getCurrentFiscalYear();
//		List<ForeCast> foreCastList = entityService.findByNameQuery(Constants.GET_FORECAST_OF_CURRENT_AND_NEXT_FISCAL_YEAR, 
//													new Object[] {String.valueOf(currentFiscalYear), String.valueOf(currentFiscalYear + 1)});
//		for (ForeCast foreCast : foreCastList) {
//			for (String month : lockedYearMonthMap) {
//				List<ForeCastHistory> foreCastHistoryList = entityService.findByNameQuery(Constants.GET_FORECAST_HISTORY_BY_COMPOSITE_KEY, 
//															new Object[] {foreCast.getRowId(), month, foreCast.getYear()});
//				if(foreCastHistoryList.isEmpty()) {
//					persistForecastHistory(foreCast, month, salesRepId);
//				}
//			}
//		}
	}
	
	private void persistForecastHistory(ForeCast foreCast, String month, String salesRepId) {
		ForeCastHistory foreCastHistory = getForeCastHistory(foreCast, month, salesRepId);
		entityService.save(foreCastHistory);
	}

	private ForeCastHistory getForeCastHistory(ForeCast foreCast, String month, String salesRepId) {
		ForeCastHistory foreCastHistory = new ForeCastHistory();
		//foreCastHistory.setName(foreCast.getRowId() + "-" + month + "-" + foreCast.getYear());
		foreCastHistory.setForecastId(foreCast.getRowId());
		foreCastHistory.setYear(foreCast.getYear());
		foreCastHistory.setRevenue(foreCast.getRevenue());
		foreCastHistory.setAsp(foreCast.getAsp());
		foreCastHistory.setQuantity(foreCast.getQuantity());
		foreCastHistory.setLockedMonth(month);
		foreCastHistory.setLockedYear(String.valueOf(getCurrentFiscalYear()));
		foreCastHistory.setLockedById(salesRepId);
		foreCastHistory.setLockedBy(getSalesRepUserName(salesRepId));
		foreCastHistory.setQuarter(CommonUtil.getForecastQuarter(month));
		foreCastHistory.setForecastType(Constants.FORECAST_TYPE_SALES_FORECAST);
		foreCastHistory.setEndCustomerId(foreCast.getEndCustomerId());
		foreCastHistory.setMarket(foreCast.getMarket());
		foreCastHistory.setSubMarket(foreCast.getSubMarket());
		foreCastHistory.setBusinessUnit(foreCast.getBusinessUnit());
		foreCastHistory.setProgramName(foreCast.getProgramName());
		foreCastHistory.setRole(foreCast.getRole());
		foreCastHistory.setParentEndCustomer(foreCast.getParentEndCustomer());
		foreCastHistory.setParentEndCustomerName(foreCast.getParentEndCustomerName());
		foreCastHistory.setPartNumber(foreCast.getPartNumber());
		foreCastHistory.setProductId(foreCast.getProductId());
		foreCastHistory.setProductName(foreCast.getProductName());
		foreCastHistory.setProductCategory(foreCast.getProductCategory());
		foreCastHistory.setSalesRepId(foreCast.getSalesRepId());
		foreCastHistory.setOwner(foreCast.getOwner());
		foreCastHistory.setAsp1(foreCast.getAsp1());
		foreCastHistory.setAsp2(foreCast.getAsp2());
		foreCastHistory.setAsp3(foreCast.getAsp3());
		foreCastHistory.setAsp4(foreCast.getAsp4());
		foreCastHistory.setAsp5(foreCast.getAsp5());
		foreCastHistory.setAsp6(foreCast.getAsp6());
		foreCastHistory.setAsp7(foreCast.getAsp7());
		foreCastHistory.setAsp8(foreCast.getAsp8());
		foreCastHistory.setAsp9(foreCast.getAsp9());
		foreCastHistory.setAsp10(foreCast.getAsp10());
		foreCastHistory.setAsp11(foreCast.getAsp11());
		foreCastHistory.setAsp12(foreCast.getAsp12());
		foreCastHistory.setQuantity1(foreCast.getQuantity1());
		foreCastHistory.setQuantity2(foreCast.getQuantity2());
		foreCastHistory.setQuantity3(foreCast.getQuantity3());
		foreCastHistory.setQuantity4(foreCast.getQuantity4());
		foreCastHistory.setQuantity5(foreCast.getQuantity5());
		foreCastHistory.setQuantity6(foreCast.getQuantity6());
		foreCastHistory.setQuantity7(foreCast.getQuantity7());
		foreCastHistory.setQuantity8(foreCast.getQuantity8());
		foreCastHistory.setQuantity9(foreCast.getQuantity9());
		foreCastHistory.setQuantity10(foreCast.getQuantity10());
		foreCastHistory.setQuantity11(foreCast.getQuantity11());
		foreCastHistory.setQuantity12(foreCast.getQuantity12());
		foreCastHistory.setCreatedDate(new Date());
		foreCastHistory.setModifiedDate(new Date());
		return foreCastHistory;
	}

	private String getSalesRepUserName(String salesRepId) {
		List<User> userList = entityService.findByNameQuery(Constants.GET_USER_BY_ROW_ID, new Object[] {salesRepId});
		return userList.isEmpty() ? null : userList.get(0).getUsername();
	}

	private Integer getCurrentFiscalYear() {
	    int currentMonth = Calendar.getInstance().get(Calendar.MONTH);
		return currentMonth < 3 ? Calendar.getInstance().get(Calendar.YEAR) : Calendar.getInstance().get(Calendar.YEAR) + 1;
	}
	
	public void createForecastIfRequired(String salesRepId) throws Exception {
		Integer currentFiscalYear = getCurrentFiscalYear();
		List<ForeCast> currentYearForecastList = entityService.findByNameQuery(Constants.GET_FORECAST_OF_OWNER_BY_FISCAL_YEAR, 
									  new Object[] { salesRepId, String.valueOf(currentFiscalYear)});
		log.info("Number of Forecasts for FY Year : " + currentFiscalYear + " are : " + currentYearForecastList.size());
		for (ForeCast foreCast : currentYearForecastList) {
			List<ForeCast> nextYearForecastList = entityService.findByNameQuery(Constants.GET_FORECAST_BY_COMPOSITE_KEY, 
												  new Object[] {foreCast.getEndCustomerId(), foreCast.getProductId(), String.valueOf(currentFiscalYear + 1), 
												  foreCast.getMarket(), foreCast.getSubMarket(), foreCast.getProgramName(), foreCast.getBusinessUnit()});
			if(nextYearForecastList.isEmpty()) {
//				ForeCast forecast = getForeCastRecord(foreCast.getEndCustomerId(), foreCast.getProductId(), currentFiscalYear + 1, foreCast.getMarket(), foreCast.getSubMarket(), foreCast.getProgramName(), foreCast.getBusinessUnit(), salesRepId);
//				entityService.save(forecast);
				log.info("Created Forecast record for FY Year : " + (currentFiscalYear + 1));
			}
		}
	}
	
	public ForeCast getForeCastRecord(Account account, Product product,String customerId, String productId, Integer fiscalYear, String marketSegment, String subMarket, String program, String bu, String loginSalesRepId) {
		ForeCast foreCast = new ForeCast();
		
		
		//foreCast.setName(customerId + "-" + product.getProductName() + "-" + fiscalYear);
		foreCast.setForecastType(Constants.FORECAST_TYPE_SALES_FORECAST);
		foreCast.setParentEndCustomer(customerId);
		foreCast.setParentEndCustomerName(account.getName());
		foreCast.setEndCustomerId(customerId);
		foreCast.setProductId(productId);
		foreCast.setPartNumber(product.getPartNumber());
		foreCast.setProductName(product.getProductName());
		foreCast.setProductCategory(product.getProductCategory());
		foreCast.setYear(String.valueOf(fiscalYear));
		foreCast.setSalesRepId(loginSalesRepId);
		foreCast.setOwner(getOwnerBySalesRepId(loginSalesRepId));
		foreCast.setMarket(marketSegment);
		foreCast.setSubMarket(subMarket);
		foreCast.setProgramName(program);
		foreCast.setBusinessUnit(bu);
		foreCast.setCreatedBy(Constants.INTEGRATION_USER);
		foreCast.setModifiedBy(Constants.INTEGRATION_USER);
		foreCast.setCreatedDate(new Date());
		foreCast.setModifiedDate(new Date());
		return foreCast;
	}
	
	private String getOwnerBySalesRepId(String loginSalesRepId) {
		List<User> userList = entityService.findByNameQuery(Constants.GET_USER_BY_USER_NAME, new Object[] {loginSalesRepId});
		if(!userList.isEmpty()) {
			User user = userList.get(0);
			return user.getUserSignInId();
		}
		return null;
	}

	private String getSalesRepIdByLoginUser(String loginSalesRepId) {
		List<User> userList = entityService.findByNameQuery(Constants.GET_USER_BY_USER_SIGN_IN_ID, new Object[] {loginSalesRepId});
		if(!userList.isEmpty()) {
			User user = userList.get(0);
			return user.getId();
		}
		return null;
	}

	public void takeFinanceForecastSnapshot(Map<String, List<String>> lockedYearMonthMap, String salesRepId) throws Exception {
		log.info("Locked Year Month Map is : " + lockedYearMonthMap.toString());
		for (String year : lockedYearMonthMap.keySet()) {
			log.info("Currently processing FY Year : " + year + " for Locking");
			List<FinanceForeCast> foreCastList = entityService.findByNameQuery(Constants.GET_FINANCE_FORECAST_OF_CURRENT_AND_NEXT_FISCAL_YEAR, 
										  new Object[] { year, String.valueOf(Integer.parseInt(year) + 1)});
			log.info("Got Number of forecast are : " + foreCastList.size());
			List<String> monthList = lockedYearMonthMap.get(year);
			log.info("Month(s) for FY Year : " + year + " for Locking are : " + monthList.toString());
			for (FinanceForeCast foreCast : foreCastList) {
				for (String month : monthList) {
					List<FinanceForeCastHistory> foreCastHistoryList = entityService.findByNameQuery(Constants.GET_FINANCE_FORECAST_HISTORY_BY_COMPOSITE_KEY, 
																new Object[] {foreCast.getSalesForecastRowId(), month, foreCast.getYear()});
						if(foreCastHistoryList.isEmpty()) {
							persistFinanceForecastHistory(foreCast, month, salesRepId);
					}
				}
			}	
		}
	}

	private void persistFinanceForecastHistory(FinanceForeCast financeforeCast, String month, String salesRepId) {
		FinanceForeCastHistory financeforeCastHistory = getFinanceForeCastHistory(financeforeCast, month, salesRepId);
		entityService.save(financeforeCastHistory);
	}

	private FinanceForeCastHistory getFinanceForeCastHistory(FinanceForeCast financeforeCast, String month, String salesRepId) {
		FinanceForeCastHistory financeforeCastHistory = new FinanceForeCastHistory();
		financeforeCastHistory.setName(financeforeCast.getSalesForecastRowId() + "-" + month + "-" + financeforeCast.getYear());
		financeforeCastHistory.setForecastId(financeforeCast.getSalesForecastRowId());
		financeforeCastHistory.setYear(financeforeCast.getYear());
		financeforeCastHistory.setRevenue(financeforeCast.getRevenue());
		financeforeCastHistory.setAsp(financeforeCast.getAsp());
		financeforeCastHistory.setQuantity(financeforeCast.getQuantity());
		financeforeCastHistory.setLockedMonth(month);
		financeforeCastHistory.setLockedYear(String.valueOf(getCurrentFiscalYear()));
		financeforeCastHistory.setLockedById(salesRepId);
		financeforeCastHistory.setLockedBy(getSalesRepUserName(salesRepId));
		financeforeCastHistory.setQuarter(CommonUtil.getForecastQuarter(month));
		financeforeCastHistory.setForecastType(Constants.FORECAST_TYPE_FINANCE_FORECAST);
		financeforeCastHistory.setEndCustomerId(financeforeCast.getEndCustomerId());
		financeforeCastHistory.setMarket(financeforeCast.getMarket());
		financeforeCastHistory.setSubMarket(financeforeCast.getSubMarket());
		financeforeCastHistory.setBusinessUnit(financeforeCast.getBusinessUnit());
		financeforeCastHistory.setProgramName(financeforeCast.getProgramName());
		financeforeCastHistory.setRole(financeforeCast.getRole());
		financeforeCastHistory.setParentEndCustomer(financeforeCast.getParentEndCustomer());
		financeforeCastHistory.setParentEndCustomerName(financeforeCast.getParentEndCustomerName());
		financeforeCastHistory.setPartNumber(financeforeCast.getPartNumber());
		financeforeCastHistory.setProductId(financeforeCast.getProductId());
		financeforeCastHistory.setProductName(financeforeCast.getProductName());
		financeforeCastHistory.setProductCategory(financeforeCast.getProductCategory());
		financeforeCastHistory.setSalesRepId(financeforeCast.getSalesRepId());
		financeforeCastHistory.setOwner(salesRepId);
		financeforeCastHistory.setAsp1(financeforeCast.getAsp1());
		financeforeCastHistory.setAsp2(financeforeCast.getAsp2());
		financeforeCastHistory.setAsp3(financeforeCast.getAsp3());
		financeforeCastHistory.setAsp4(financeforeCast.getAsp4());
		financeforeCastHistory.setAsp5(financeforeCast.getAsp5());
		financeforeCastHistory.setAsp6(financeforeCast.getAsp6());
		financeforeCastHistory.setAsp7(financeforeCast.getAsp7());
		financeforeCastHistory.setAsp8(financeforeCast.getAsp8());
		financeforeCastHistory.setAsp9(financeforeCast.getAsp9());
		financeforeCastHistory.setAsp10(financeforeCast.getAsp10());
		financeforeCastHistory.setAsp11(financeforeCast.getAsp11());
		financeforeCastHistory.setAsp12(financeforeCast.getAsp12());
		financeforeCastHistory.setQuantity1(financeforeCast.getQuantity1());
		financeforeCastHistory.setQuantity2(financeforeCast.getQuantity2());
		financeforeCastHistory.setQuantity3(financeforeCast.getQuantity3());
		financeforeCastHistory.setQuantity4(financeforeCast.getQuantity4());
		financeforeCastHistory.setQuantity5(financeforeCast.getQuantity5());
		financeforeCastHistory.setQuantity6(financeforeCast.getQuantity6());
		financeforeCastHistory.setQuantity7(financeforeCast.getQuantity7());
		financeforeCastHistory.setQuantity8(financeforeCast.getQuantity8());
		financeforeCastHistory.setQuantity9(financeforeCast.getQuantity9());
		financeforeCastHistory.setQuantity10(financeforeCast.getQuantity10());
		financeforeCastHistory.setQuantity11(financeforeCast.getQuantity11());
		financeforeCastHistory.setQuantity12(financeforeCast.getQuantity12());
		financeforeCastHistory.setCreatedDate(new Date());
		financeforeCastHistory.setModifiedDate(new Date());
		return financeforeCastHistory;
	}
	
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void copySalesForecastToFinanceForecast() {
		Integer currentFiscalYear = getCurrentFiscalYear();
		Query query =  entityService.getEntityManager().createNativeQuery(COPY_SALES_FORECAST_TO_FINANCE_FORCAST_QUERY_STRING);
		query.setParameter(1, String.valueOf(currentFiscalYear));
		query.setParameter(2, String.valueOf(currentFiscalYear + 1));
		int result = query.executeUpdate();
		log.info("Data copied successfully : " + result);
	}
	 
	public void syncFinanceForecastFromSalesForecast(ForeCast forecast) throws Exception {
		String lastLockedMonth = getLastLockedSalesForecastMonth(forecast.getYear());
		SyncFinanceForecast syncFinanceForecast = getFinanceForecastSyncProcessor(lastLockedMonth);
		syncFinanceForecast.syncFinanceForecast(forecast, false);  
	}

	private String getLastLockedSalesForecastMonth(String year) {
		try {
			Integer lastLockedMonthSequence = getLatestSalesForecastLockedMonth();
			if(lastLockedMonthSequence > -1 && lastLockedMonthSequence < 12) {
				List<SalesForecastLockInfo> salesForecastLockInfoList = entityService.findByNameQuery(Constants.GET_FORECAST_LOCK_BY_MONTH_SEQUENCE, 
																		new Object[] {lastLockedMonthSequence, year, Constants.FORECAST_TYPE_SALES_FORECAST});
				if(!salesForecastLockInfoList.isEmpty()) {
					SalesForecastLockInfo salesForecastLockInfo = salesForecastLockInfoList.get(0);
					return salesForecastLockInfo.getMonth();
				} else {
					return null;
				}
			}
			return null;
		} catch(Exception ex) {
			log.error("Exception occurred while getting Last Locked Sales Forecast Month : ", ex);
			return null;
		}
	}

	public FinanceForeCast createFinanceForecastFromSalesForecast(ForeCast salesForecast) {
		FinanceForeCast financeForeCast = new FinanceForeCast();
		salesForecast = getSyncedSalesForecastFromStaging(salesForecast);
		financeForeCast.setName(salesForecast.getRowId() + "-" + Constants.DEFAULT_FORECAST_MONTH + "-" + salesForecast.getYear());
		financeForeCast.setEndCustomerId(salesForecast.getEndCustomerId());
		financeForeCast.setProductId(salesForecast.getProductId());
		financeForeCast.setYear(salesForecast.getYear());
		financeForeCast.setMarket(salesForecast.getMarket());
		financeForeCast.setSubMarket(salesForecast.getSubMarket());
		financeForeCast.setBusinessUnit(salesForecast.getBusinessUnit());
		financeForeCast.setProgramName(salesForecast.getProgramName());
		financeForeCast.setMonth(Constants.DEFAULT_FORECAST_MONTH);
		financeForeCast.setForecastType(Constants.FORECAST_TYPE_FINANCE_FORECAST);
		financeForeCast.setParentEndCustomer(salesForecast.getParentEndCustomer());
		financeForeCast.setParentEndCustomerName(salesForecast.getParentEndCustomerName());
		financeForeCast.setOwner(salesForecast.getOwner());
		financeForeCast.setPartNumber(salesForecast.getPartNumber());
		financeForeCast.setProductCategory(salesForecast.getProductCategory());
		financeForeCast.setProductName(salesForecast.getProductName());
		financeForeCast.setSalesForecastRowId(salesForecast.getRowId());
		financeForeCast.setSalesRepId(salesForecast.getSalesRepId());
		financeForeCast.setRole(salesForecast.getRole());
		financeForeCast.setAsp1(salesForecast.getAsp1());
		financeForeCast.setAsp2(salesForecast.getAsp2());
		financeForeCast.setAsp3(salesForecast.getAsp3());
		financeForeCast.setAsp4(salesForecast.getAsp4());
		financeForeCast.setAsp5(salesForecast.getAsp5());
		financeForeCast.setAsp6(salesForecast.getAsp6());
		financeForeCast.setAsp7(salesForecast.getAsp7());
		financeForeCast.setAsp8(salesForecast.getAsp8());
		financeForeCast.setAsp9(salesForecast.getAsp9());
		financeForeCast.setAsp10(salesForecast.getAsp10());
		financeForeCast.setAsp11(salesForecast.getAsp11());
		financeForeCast.setAsp12(salesForecast.getAsp12());
		financeForeCast.setQuantity1(salesForecast.getQuantity1());
		financeForeCast.setQuantity2(salesForecast.getQuantity2());
		financeForeCast.setQuantity3(salesForecast.getQuantity3());
		financeForeCast.setQuantity4(salesForecast.getQuantity4());
		financeForeCast.setQuantity5(salesForecast.getQuantity5());
		financeForeCast.setQuantity6(salesForecast.getQuantity6());
		financeForeCast.setQuantity7(salesForecast.getQuantity7());
		financeForeCast.setQuantity8(salesForecast.getQuantity8());
		financeForeCast.setQuantity9(salesForecast.getQuantity9());
		financeForeCast.setQuantity10(salesForecast.getQuantity10());
		financeForeCast.setQuantity11(salesForecast.getQuantity11());
		financeForeCast.setQuantity12(salesForecast.getQuantity12());
		financeForeCast.setCreatedBy(Constants.INTEGRATION_USER);
		financeForeCast.setModifiedBy(Constants.INTEGRATION_USER);
		financeForeCast.setCreatedDate(new Date());
		financeForeCast.setModifiedDate(new Date());
		return financeForeCast;
	}
	
	private ForeCast getSyncedSalesForecastFromStaging(ForeCast salesForecast) {
		List<ForeCast> foreCastList = entityService.findByNameQuery(Constants.GET_FORECAST_BY_COMPOSITE_KEY, 
									  new Object[] {salesForecast.getEndCustomerId(), salesForecast.getProductId(), salesForecast.getYear(), salesForecast.getMarket(),
													salesForecast.getSubMarket(), salesForecast.getProgramName(), salesForecast.getBusinessUnit()});
		return foreCastList.get(0);
	}

	public void syncFinanceForecaseBasedOnLastCopiedTimestamp() throws Exception {
		List<ForeCast> foreCastList = getSalesForecaseByLastModifiedDate();
		log.info("Forecast List size by Modified date : " + foreCastList.size());
		String currentFiscalYear = String.valueOf(getCurrentFiscalYear());
		String lastLockedMonth = getLastLockedSalesForecastMonth(currentFiscalYear);
		log.info("Latest locked month is : " + lastLockedMonth);
		SyncFinanceForecast syncFinanceForecast = getFinanceForecastSyncProcessor(lastLockedMonth);
		for (ForeCast foreCast : foreCastList) {
			syncFinanceForecast.syncFinanceForecast(foreCast, true);
		}
	}

	private SyncFinanceForecast getFinanceForecastSyncProcessor(String lastLockedMonth) throws Exception {
		String processorClassName = financeForecastSyncProcessorMap.getFinanceSyncProcessor(lastLockedMonth);
		log.info("Sync processor for latest locked month " + lastLockedMonth + " is : " + processorClassName);
		 if(StringUtils.isNotBlank(processorClassName)) {
			 return (SyncFinanceForecast)applicationContext.getBean(Class.forName(processorClassName));
		 } else {
			 return syncFinanceForecastDefault;
		 }
	}

	private List<ForeCast> getSalesForecaseByLastModifiedDate() {
		List<ForeCast> foreCastList = null;
		Object result = entityService.getEntityManager().createNativeQuery(GET_LAST_COPIED_SALES_FORECAST_TIMESTAMP).getSingleResult();
		log.info("Last copied modified date : " + result);
		 if(result != null) {
			 foreCastList = entityService.findByNameQuery(Constants.GET_SALES_FORECAST_BY_LAST_MODIFIED_DATE, new Object[] {result}); 
		 } else {
			 foreCastList = entityService.findByNameQuery(Constants.GET_ALL_SALES_FORECAST); 
		 }
		return foreCastList;
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void updateFinanceForecastWithLockedMonth() {
		int previousMonth = Calendar.getInstance().get(Calendar.MONTH) - 1;
		previousMonth = previousMonth < 0 ? 11 : previousMonth;
		String latestLockedMonth = months[previousMonth];
		Query query = entityService.getEntityManager().createNativeQuery(UPDATE_FINANCE_FORECAST_WITH_LOCKED_MONTH_QUERY_STRING);
		query.setParameter(1, latestLockedMonth);
		int updatedRecords = query.executeUpdate();
		log.info("Number of records updated are : " + updatedRecords);
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void deleteFianaceForecastSnapshotRecords() {
		int deletedRecords = entityService.getEntityManager().createNativeQuery(DELETE_FINANCE_FORECAST_SNAPSHOT_RECORDS_QUERY_STRING).executeUpdate();
		log.info("Number of records deleted are : " + deletedRecords);
	}

	public void syncFinanceForecastFromHistory(ForeCast salesForecast) throws Exception {
		String lastLockedMonth = getLastLockedFinanceForecastMonth();
		 if(StringUtils.isBlank(lastLockedMonth)) {
			FinanceForeCast financeForeCast = createFinanceForecastFromSalesForecast(salesForecast);
			entityService.save(financeForeCast);
		 } else {
			List<FinanceForeCastHistory> financeForeCastHistoryList = entityService.findByNameQuery(Constants.GET_FINANCE_FORECAST_HISTORY_BY_COMPOSITE_KEY,
																	  new Object[] {salesForecast.getRowId(), lastLockedMonth, salesForecast.getYear()} );
			if(!financeForeCastHistoryList.isEmpty()) {
				FinanceForeCastHistory financeForeCastHistory = financeForeCastHistoryList.get(0);
				SyncFinanceForecast syncFinanceForecast = getFinanceForecastSyncProcessor(lastLockedMonth);
				syncFinanceForecast.syncFinanceForecastFromHistory(salesForecast, financeForeCastHistory);
			} else {
				FinanceForeCast financeForeCast = createFinanceForecastFromSalesForecast(salesForecast);
				entityService.save(financeForeCast);
			}
		}
	}

	private String getLastLockedFinanceForecastMonth() {
		try {
			Integer lastLockedMonthSequence = getLatestFinanceForecastLockedMonth();
			if(lastLockedMonthSequence > -1 && lastLockedMonthSequence < 12) {
				List<SalesForecastLockInfo> salesForecastLockInfoList = entityService.findByNameQuery(Constants.GET_FORECAST_LOCK_BY_MONTH_SEQUENCE, 
																		new Object[] {lastLockedMonthSequence, String.valueOf(getCurrentFiscalYear()), Constants.FORECAST_TYPE_FINANCE_FORECAST});
				if(!salesForecastLockInfoList.isEmpty()) {
					SalesForecastLockInfo salesForecastLockInfo = salesForecastLockInfoList.get(0);
					return salesForecastLockInfo.getMonth();
				} else {
					return null;
				}
			}
			return null;
		} catch(Exception ex) {
			log.error("Exception occurred while getting Last Locked Finance Forecast Month : ", ex);
			return null;
		}
	}

	public Integer getLatestSalesForecastLockedMonth() {
		try {
			Query query = entityService.getEntityManager().createNativeQuery(GET_LATEST_SALES_FORECAST_LOCKED_MONTH_QUERY_STRING);
			query.setParameter(1, String.valueOf(getCurrentFiscalYear()));
			Object result = query.getSingleResult();
			return result == null ? -1 : Integer.valueOf(result.toString());
		} catch(Exception ex) {
			log.error("Exception occurred while getting latest locked Sales Forecast month", ex);
			return -1;
		}
	}

	public Integer getLatestFinanceForecastLockedMonth() {
		try {
			Query query = entityService.getEntityManager().createNativeQuery(GET_LATEST_FINANCE_FORECAST_LOCKED_MONTH_QUERY_STRING);
			query.setParameter(1, String.valueOf(getCurrentFiscalYear()));
			Object result = query.getSingleResult();
			return result == null ? -1 : Integer.valueOf(result.toString());
		} catch(Exception ex) {
			log.error("Exception occurred while getting latest locked Finance Forecast month", ex);
			return -1;
		}
	}
	
	public void persistSSOToken(String ssoToken) {
		InvensenseProperty invensenseProperty = new InvensenseProperty();
		invensenseProperty.setName(Constants.FORECAST_SSO_TOKEN);
		invensenseProperty.setValue(ssoToken);
		invensenseProperty.setStatus(Constants.ACTIVE);
		entityService.save(invensenseProperty);
	}

	public void syncFinanceForecastAfterSnapshot() throws Exception {
//		foreCastSyncJob.executeJob();
		String year = String.valueOf(getCurrentFiscalYear());
		List<ForeCast> foreCastList = entityService.findByNameQuery(Constants.GET_FORECAST_OF_CURRENT_AND_NEXT_FISCAL_YEAR, 
				  					  new Object[] { year, String.valueOf(Integer.parseInt(year) + 1)});
		log.info("Got Number of forecast are : " + foreCastList.size());
		for (ForeCast foreCast : foreCastList) {
			List<FinanceForeCast> financeForeCastList = entityService.findByNameQuery(Constants.GET_INVISIBLE_FINANCE_FORECAST_BY_COMPOSITE_KEY, 
														new Object[] { foreCast.getEndCustomerId(), foreCast.getProductId(), foreCast.getYear(),
														foreCast.getMarket(), foreCast.getSubMarket(), foreCast.getProgramName(), foreCast.getBusinessUnit()});
				if(financeForeCastList.isEmpty()) {
					syncFinanceForecastFromHistory(foreCast);
			}
		}		
	}
	
	public List<User> getUserHeirarchy(String salesRepId) {
		List<User> salesRepList = new LinkedList<User>();
		List<User> userList = entityService.findByNameQuery(Constants.GET_USER_BY_ROW_ID, new Object[] {salesRepId});
		User user = userList.get(0);
		salesRepList.add(user);
		return  getUserListByHierarchy(user, salesRepList, 1, 1, user);
	}

	public List<Account> getForecastParentAccountsByUser(String userId) {
		List<Account> accountList = new ArrayList<Account>();
		List<User> userList = getUserHeirarchy(userId);
		for (User user : userList) {
			List<AccountTeamWS1> accountTeamList = entityService.findByNameQuery(Constants.GET_ACCOUNT_TEAM_BY_USER_ID, new Object[] {user.getId()});
			for (AccountTeamWS1 accountTeamWS1 : accountTeamList) {
				String accountId = accountTeamWS1.getAccountId();
				if(StringUtils.isNotBlank(accountId)) {
					List<Account> stagingAccountList = entityService.findByNameQuery(Constants.CHECK_ACCOUNT_IS_PARENT, 
														new Object[] {accountId, Constants.ACCOUNT_TYPE_PARENT_CUSTOMER});
					if(!stagingAccountList.isEmpty()) {
						accountList.addAll(stagingAccountList);
					}
				}
			}
		}
		return accountList;
	}
 
}
