package com.invensense.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class EventObjectKey implements Serializable {
	
	@Column(name="object_id")
	private String objectId ;
	
	@Column(name="event_name")
	private String event;

	public String getObjectId() {
		return objectId;
	}

	public void setObjectId(String objectId) {
		this.objectId = objectId;
	}

	public String getEvent() {
		return event;
	}

	public void setEvent(String event) {
		this.event = event;
	}

	@Override
	public String toString() {
		return "EventObjectKey [objectId=" + objectId + ", event=" + event
				+ "]";
	}
	
}
