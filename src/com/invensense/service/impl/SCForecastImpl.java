package com.invensense.service.impl;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.invensense.util.PropFileUtil;
import com.invensense.ws.fusion.stubs.customObject.SalesCustomObjectService;
import com.invensense.ws.fusion.stubs.customObject.SalesCustomObjectService_Service;

@Service
public class SCForecastImpl extends AbstractFCRMODWebService {

	private Logger log = Logger.getLogger(SCForecastImpl.class);
	private SalesCustomObjectService salesCustomObjectService ;
	public SCForecastImpl ()
	{
		SalesCustomObjectService_Service salesCustomObjectService_Service = new SalesCustomObjectService_Service();
		this.salesCustomObjectService = salesCustomObjectService_Service.getSalesCustomObjectServiceSoapHttpPort();
		setWSBindingProvider(salesCustomObjectService,PropFileUtil.getValue("sc.url"));
	}

	public void delete(Object obj) throws Exception {
		try
		{
			salesCustomObjectService.deleteEntity(obj, "Forecast_Custom_c");
			log.info("Forecast Deleted Successfully");
		}
		catch(Exception e)
		{
			log.error("Error occurred while deleting Forecast record from Sales Cloud: " + e);
			e.printStackTrace();
		}
		
	}

	

}
