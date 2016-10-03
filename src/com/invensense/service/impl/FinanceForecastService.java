package com.invensense.service.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import javax.annotation.Resource;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.invensense.service.EntityService;
import com.invensense.util.Constants;

@Service
public class FinanceForecastService {
	private static Logger log = Logger.getLogger(FinanceForecastService.class);
	
	@Resource
	private EntityService entityService ;  
	
//	@Resource
//	private ForeCastSyncJob foreCastSyncJob;
	
//	@Resource
//	private CrmodService crmodService;

	public EntityService getEntityService() {
		return entityService;
	}

	public void setEntityService(EntityService entityService) {
		this.entityService = entityService;
	}
	
	public List getForecastTotal(int year,String salesRepRowId){
		//return baseService.getObjectsByNamedQuery(Constants.GET_FORECAST_TOTAL, salesRepRowId,salesRepRowId);
//		return baseService.getObjectsByNamedQuery(Constants.GET_FORECAST_TOTAL,year, salesRepRowId);
		String getForecastTotalQuery = Constants.GET_FORECAST_TOTAL_FOR_FINANCE_FORECAST;
		Query q=entityService.getEntityManager().createNativeQuery(getForecastTotalQuery);
		int nxtYr=year+1;
		q.setParameter(1, year);
//		q.setParameter(2, nxtYr);
		//q.setParameter(2, salesRepRowId);
		return q.getResultList();
	}
		
	public String getRowTotalAsJson(int year,String salesRepRowId){
		StringBuffer grandTotalRow= new StringBuffer("{"); 
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
			Object[] o =  objs.get(0);
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
}
