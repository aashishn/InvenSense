package com.invensense.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.ws.BindingProvider;
import javax.xml.ws.handler.MessageContext;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.invensense.util.Constants;
import com.invensense.util.PropFileUtil;
import com.invensense.ws.fusion.stubs.userdetails.FindSelfUserDetailsResponse;
import com.invensense.ws.fusion.stubs.userdetails.ObjectFactory;
import com.invensense.ws.fusion.stubs.userdetails.UserDetails;
import com.invensense.ws.fusion.stubs.userdetails.UserDetailsResult;
import com.invensense.ws.fusion.stubs.userdetails.UserDetailsService;
import com.invensense.ws.fusion.stubs.userdetails.UserDetailsService_Service;

@Service
public class UserDetailsHcmService {
	private static final Logger log = LoggerFactory.getLogger(UserDetailsHcmService.class);
	protected static UserDetailsService_Service userDetailsService_Service;
	protected UserDetailsService userDetailsService;
	protected ObjectFactory objectFactory;
	
	private static List<String> skipSSOTokenValidationActionList = new ArrayList<String>();

	public UserDetails findSelfUserDetailsInOsc(String jwtUserToken) throws Exception{
		log.info("findSelfUserDetailsInOsc >>");
		FindSelfUserDetailsResponse findSelfUserDetailsResponse = null;
		UserDetailsResult userDetailsResult = null;
		objectFactory = new ObjectFactory();

//		FindSelfUserDetails findUserDetails = objectFactory.createFindSelfUserDetails();
		final String authZParameterValue = "Bearer " + jwtUserToken;
		String serviceEndPoint = PropFileUtil.getValue(Constants.USER_DETAILS_SERVICE_END_POINT);//"https://cbax.hcm.us2.oraclecloud.com:443/hcmPeopleRolesV2/UserDetailsService";

		try{
//			System.setProperty("javax.xml.transform.TransformerFactory","com.sun.org.apache.xalan.internal.xsltc.trax.TransformerFactoryImpl");
			userDetailsService_Service = new UserDetailsService_Service();
//
//			SecurityPolicyFeature[] securityFeature =
//					new SecurityPolicyFeature[] { new SecurityPolicyFeature("") };
			
			userDetailsService = userDetailsService_Service.getUserDetailsServiceSoapHttpPort();

			BindingProvider wsbindingProvider = (BindingProvider)userDetailsService;
			Map<String, Object> requestContext = wsbindingProvider.getRequestContext();
			
			requestContext.put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, serviceEndPoint);         
//			requestContext.put(BindingProvider.SOAPACTION_URI_PROPERTY,serviceEndPoint);
			Map<String, List<String>> authMap = new HashMap<String, List<String>>();
			List<String> authZList = new ArrayList<String>();
			authZList.add(authZParameterValue);

			authMap.put("Authorization", authZList);

			requestContext.put(MessageContext.HTTP_REQUEST_HEADERS, authMap);

			userDetailsResult = userDetailsService.findSelfUserDetails();

		}catch(Exception e){
			log.error("Error validating jwt="+jwtUserToken+" in findSelfUserDetails using static proxy",e);
			throw e;
		}

		if(userDetailsResult==null || userDetailsResult.getValue().isEmpty())
			return null;

		log.info("findSelfUserDetailsInOsc <<");               
		return userDetailsResult.getValue().get(0);             
	}

	public  boolean validateJwtToken(String jwt, String action) throws Exception{		
		boolean valid = false;
    	if(skipValidationBasedOnAction(action)) {
    		return true;
    	}
		UserDetails userDetails = findSelfUserDetailsInOsc(jwt);
		if(userDetails!=null && userDetails.getUsername()!=null){				
			if(userDetails.getUsername().getValue()!=null && !userDetails.getUsername().getValue().equals("")){
				valid = true;
				log.debug("JWT Valid!! User info found"+userDetails.getUsername().getValue());
			}
		}else{
			log.debug("JWT Invalid!! User info not found");
		}
		return valid;
	}
	
	public static void main(String[] args) throws Exception{		
		String jwt = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCIsIng1dCI6ImVzQmFQNDhjSlljbTVEakdKOGoyMzQ0cE5GMCJ9.eyJleHAiOjE0NzU1MDY4NTMsImlzcyI6Ind3dy5vcmFjbGUuY29tIiwicHJuIjoiSVVTRVIiLCJpYXQiOjE0NzU0OTI0NTN9.Uawqu_XqmngrxVr2-NfOcvi9TdUE32jB-r4TRi4cAFZY2ywbfTABpYZEDM3QsSwO-h4YNQIUA_5DRxmtDqi3ZGbLBx7RsyS5MKt8_X1eV1uF9mzKBW2rWbEh9v31A__AqsyRJHh_LMpETALK4axhaHuDpnWNq39JoPFwdeqSmAkClrRY77v9mS47NAzNdk4yozR7b5-NCUzNR7kMn0PjU9RFHs6LvkwdwbAeeLEIf9NWUiR75vOwe54plYkweCfCKE1Zd5npyn1ZHYMRVIEhk0K3D-K4zGIbd-cyHlveiOmfxW-WVOEFYq8v6OPHZTmqfFkUOxCeybWS-Zxt9GfIYQ";
		boolean valid = false;
		
		UserDetailsHcmService client = new UserDetailsHcmService();
		UserDetails userDetails = client.findSelfUserDetailsInOsc(jwt);
		if(userDetails!=null && userDetails.getUsername()!=null){				
			if(userDetails.getUsername().getValue()!=null && !userDetails.getUsername().getValue().equals("")){
				valid = true;
				System.out.println("User info found"+userDetails.getUsername().getValue());
			}
		}else{
			System.out.println("User info not found");
		}
	}
	
	private static boolean skipValidationBasedOnAction(String action) {
		if(skipSSOTokenValidationActionList.isEmpty()) {
			loadSkipSSOTokenValidationList();
		}
		return StringUtils.isNotBlank(action) && skipSSOTokenValidationActionList.contains(action);
	}
	
	private static void loadSkipSSOTokenValidationList() {
		skipSSOTokenValidationActionList.add("CreateFinanceForecast");
		skipSSOTokenValidationActionList.add("ViewFinanceForecast");
		skipSSOTokenValidationActionList.add("BackToSalesForecast");
	}
}
