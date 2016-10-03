package com.invensense.util;

import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class FinanceForecastSyncProcessorMap {
	
	private Map<String, String> financeSyncProcessorMap;
	
	public void setFinanceSyncProcessorMap(
			Map<String, String> financeSyncProcessorMap) {
		this.financeSyncProcessorMap = financeSyncProcessorMap;
	}
	
	public Map<String, String> getFinanceSyncProcessorMap() {
		return financeSyncProcessorMap;
	}

	public String getFinanceSyncProcessor(String eventKey) {
		if (financeSyncProcessorMap.containsKey(eventKey)){
			return financeSyncProcessorMap.get(eventKey);	
		} else {
			return eventKey;
		}
	}

	public String toString() {
		return financeSyncProcessorMap.toString();
	}

}
