package com.invensense.service.impl;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import com.invensense.util.PropFileUtil;
import com.invensense.ws.fusion.stubs.customObject.ForecastCustomC;
import com.invensense.ws.fusion.stubs.customObject.SalesCustomObjectService;
import com.invensense.ws.fusion.stubs.customObject.SalesCustomObjectService_Service;

@Service
public class SCForecastImpl //extends AbstractFCRMODWebService {
{
	
	public static void main(String args[]) throws Exception
	{
		ForecastCustomC forecast_Custom_c = new ForecastCustomC();
		forecast_Custom_c.setId(new BigDecimal("300000001672487"));
		SCForecastImpl scForecastImpl = new SCForecastImpl();
		scForecastImpl.delete(forecast_Custom_c);
	}
	

	public void delete(Object obj) throws Exception {
		SalesCustomObjectService_Service salesCustomObjectService_Service = new SalesCustomObjectService_Service();
		SalesCustomObjectService salesCustomObjectService = salesCustomObjectService_Service.getSalesCustomObjectServiceSoapHttpPort();
		//setWSBindingProvider(salesCustomObjectService,PropFileUtil.getValue("sc.url"));
		salesCustomObjectService.deleteEntity(obj, "Forecast_Custom_c");
		
	}

	

}
