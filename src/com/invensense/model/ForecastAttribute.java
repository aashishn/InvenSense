package com.invensense.model;

import java.util.LinkedList;
import java.util.Map;

public class ForecastAttribute {

	private LinkedList columns ; 
	
	private LinkedList<Map> columnsDef ;
	
	public LinkedList<Map> getColumnsDef() {
		return columnsDef;
	}
	public void setColumnsDef(LinkedList<Map> columnsDef) {
		this.columnsDef = columnsDef;
	}
	public LinkedList getColumns() {
		return columns;
	}
	public void setColumns(LinkedList columns) {
		this.columns = columns;
	}
}
