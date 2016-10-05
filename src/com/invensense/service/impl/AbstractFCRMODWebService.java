package com.invensense.service.impl;



import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.xml.ws.BindingProvider;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.invensense.service.FCRMODWebService;
import com.invensense.util.PropFileUtil;
import com.invensense.util.SOAPLoggingHandler;
import com.invensense.ws.fusion.stubs.customObject.SalesCustomObjectService;
import com.invensense.ws.fusion.stubs.customObject.SalesCustomObjectService_Service;
import com.sun.xml.internal.ws.client.BindingProviderProperties;


@Service
public abstract class AbstractFCRMODWebService implements FCRMODWebService {
	
	private static final Logger log = Logger.getLogger(AbstractFCRMODWebService.class);

	@Override
	public void setWSBindingProvider(Object publicService, String endPointAddress) {
		 
		BindingProvider wsbp = (BindingProvider)publicService;
	     Map<String, Object> reqContext = ((BindingProvider) wsbp).getRequestContext();
	     log.info("Got the end point address : " + endPointAddress);
	     reqContext.put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, endPointAddress);
	     
	     System.out.println("UserName: "+ PropFileUtil.getValue("sc.username") + "Password: "+PropFileUtil.getValue("sc.password"));
	     
	     reqContext.put(BindingProvider.USERNAME_PROPERTY, PropFileUtil.getValue("sc.username"));
	     reqContext.put(BindingProvider.PASSWORD_PROPERTY, PropFileUtil.getValue("sc.password"));
	      
	     reqContext.put(BindingProviderProperties.REQUEST_TIMEOUT, 60000);
	     
	     reqContext.put(BindingProviderProperties.CONNECT_TIMEOUT, 60000);
	   
	     List handlerList = wsbp.getBinding().getHandlerChain();
		 if (handlerList == null)
			handlerList = new ArrayList();
			SOAPLoggingHandler loggingHandler = new SOAPLoggingHandler();
			handlerList.add(loggingHandler);
			wsbp.getBinding().setHandlerChain(handlerList);
	     
	     
	}
	
	//Get Sales Custom Object Service
		public SalesCustomObjectService getSalesCustomObjectService(){
			SalesCustomObjectService_Service customObjectService_Service = new SalesCustomObjectService_Service();
			SalesCustomObjectService customObjectService = customObjectService_Service.getSalesCustomObjectServiceSoapHttpPort();;
			setWSBindingProvider(customObjectService,PropFileUtil.getValue("sc.url"));
			return customObjectService;
		}

		
}
