package com.invensense.service;

import org.springframework.stereotype.Service;


@Service
public interface FCRMODWebService{
	
	public void setWSBindingProvider(Object publicService, String endPointAddress);

}
