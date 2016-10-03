package com.invensense.job;

import java.util.concurrent.ThreadPoolExecutor;

import org.springframework.beans.factory.DisposableBean;

public class ShutDownThreadPool implements DisposableBean {
	
	private ThreadPoolExecutor executer;
	
	@Override
    public void destroy() {
		if (executer != null)  executer.shutdown();
    }

}
