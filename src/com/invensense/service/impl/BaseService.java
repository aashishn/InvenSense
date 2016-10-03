package com.invensense.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.invensense.service.EntityService;

@Service
public class BaseService {

	@Resource
	private EntityService entityService ;
	
	public void getUnProccessedData(Class clazz){
		
	}
}
