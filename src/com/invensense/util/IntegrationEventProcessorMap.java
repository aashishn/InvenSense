package com.invensense.util;

import java.util.Map;

import org.springframework.stereotype.Component;

@Component
public class IntegrationEventProcessorMap {
	
	private Map<String, String> eventProcessorMap;
	
	private Map<String, String> eventErrorProcessorMap;

	public void setEventProcessorMap(Map<String, String> eventProcessorMap) {
		this.eventProcessorMap = eventProcessorMap;
	}
	
	public String getEventProcessor(String eventKey) {
		if (eventProcessorMap.containsKey(eventKey)){
			return eventProcessorMap.get(eventKey);	
		} else {
			return eventKey;
		}
	}
	
	public void setEventErrorProcessorMap(Map<String, String> eventErrorProcessorMap) {
		this.eventErrorProcessorMap = eventErrorProcessorMap;
	}

	public String getEventErrorProcessor(String eventKey) {
		return eventErrorProcessorMap.get(eventKey);
	}

	public String toString() {
		return eventProcessorMap.toString() + " : " + eventErrorProcessorMap.toString();
	}

}
