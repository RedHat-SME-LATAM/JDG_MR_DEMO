package com.redhat.smelatam.model;

import java.io.Serializable;
import java.util.HashMap;

public class Bill implements Serializable {

	public Bill(long customerID, HashMap<String, Double> billLines) {
		super();
		this.customerID = customerID;
		this.billLines = billLines;
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private long customerID;
	private HashMap<String,Double> billLines;
	
	public long getCustomerID() {
		return customerID;
	}
	public void setCustomerID(long customerID) {
		this.customerID = customerID;
	}
	public HashMap<String, Double> getBillLines() {
		return billLines;
	}
	public void setBillLines(HashMap<String, Double> billLines) {
		this.billLines = billLines;
	}
	
	
}
