package com.invensense.service.impl;

import java.util.Map;

import javax.xml.ws.BindingProvider;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.invensense.util.PropFileUtil;
import com.invensense.ws.fusion.stubs.customObject.ForecastCustomC;
import com.invensense.ws.fusion.stubs.customObject.SalesCustomObjectService;
import com.invensense.ws.fusion.stubs.customObject.SalesCustomObjectService_Service;
import com.invensense.ws.fusion.stubs.customObject.ServiceException;

@Service
public class SCForecastImpl {

	private Logger log = Logger.getLogger(SCForecastImpl.class);
	private SalesCustomObjectService salesCustomObjectService ;
	public SCForecastImpl ()
	{
		SalesCustomObjectService_Service salesCustomObjectService_Service = new SalesCustomObjectService_Service();
		this.salesCustomObjectService = salesCustomObjectService_Service.getSalesCustomObjectServiceSoapHttpPort();
		BindingProvider wsbindingProvider = (BindingProvider)salesCustomObjectService;
		Map<String, Object> requestContext = wsbindingProvider.getRequestContext();
		String serviceEndPoint = PropFileUtil.getValue("sc.url");//"https://cbax.hcm.us2.oraclecloud.com:443/hcmPeopleRolesV2/UserDetailsService";

		requestContext.put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, serviceEndPoint);  
		requestContext.put(BindingProvider.USERNAME_PROPERTY, PropFileUtil.getValue("sc.username"));
		requestContext.put(BindingProvider.PASSWORD_PROPERTY, PropFileUtil.getValue("sc.password"));
	}

	public void delete(final Object obj) throws Exception {
		try
		{
			new Thread(new Runnable() {
			    public void run() {
			    	try {
						salesCustomObjectService.deleteEntity(obj, "Forecast_Custom_c");
					} catch (ServiceException e) {
						log.error("Error occurred while deleting Forecast record from Sales Cloud: " + e);
					}
					log.info("Forecast Deleted Successfully");
			    }
			}).start();
			
		}
		catch(Exception e)
		{
			log.error("Error occurred while deleting Forecast record from Sales Cloud: " + e);
		}
		
	}
	public ForecastCustomC create( ForecastCustomC forecastCustomC) throws Exception {
		ForecastCustomC forecastCustomResultC =null;
		try {
			    forecastCustomResultC = (ForecastCustomC)salesCustomObjectService.createEntity(forecastCustomC, "Forecast_Custom_c");
			} 
		catch (ServiceException e) 
		{
				log.error("Error occurred while creating Forecast record from Sales Cloud: " + e);
				e.printStackTrace();
		}
		log.info("Forecast Created Successfully");
			    
		return forecastCustomResultC;
	}
	
	
	public ForecastCustomC update( ForecastCustomC forecastCustomC) throws Exception {
		ForecastCustomC forecastCustomResultC =null;
		try {
			    forecastCustomResultC = (ForecastCustomC)salesCustomObjectService.updateEntity(forecastCustomC, "Forecast_Custom_c");
			} 
		catch (ServiceException e) 
		{
				log.error("Error occurred while updating Forecast record from Sales Cloud: " + e);
				e.printStackTrace();
		}
		log.info("Forecast Updated Successfully");
			    
		return forecastCustomResultC;
	}
	

}
