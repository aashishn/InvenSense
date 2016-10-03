package com.invensense.model;

import java.util.List;

public class ForecastData {

	private String id ;
	
	private List cell ;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List getCell() {
		return cell;
	}

	public void setCell(List cell) {
		this.cell = cell;
	}
}
