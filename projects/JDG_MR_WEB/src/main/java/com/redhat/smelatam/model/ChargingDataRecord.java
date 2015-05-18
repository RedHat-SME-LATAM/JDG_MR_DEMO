package com.redhat.smelatam.model;

import java.io.Serializable;

public class ChargingDataRecord implements Serializable {
	private long lineID;
	private String chargeType;
	private double quantity;
	private long timestamp;
	

	public ChargingDataRecord(long lineID, String chargeType, double quantity,
			long timestamp) {
		super();
		this.lineID = lineID;
		this.chargeType = chargeType;
		this.quantity = quantity;
		this.timestamp = timestamp;
	}
	
	public ChargingDataRecord(String str){
		String strFields[] = str.split("\\|");
		this.lineID=Long.parseLong(strFields[0]);
		this.chargeType = strFields[1];
		this.quantity = Double.parseDouble(strFields[2]);
		this.timestamp=Long.parseLong(strFields[3]);
		
	}
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	

	public long getLineID() {
		return lineID;
	}
	public void setLineID(long lineID) {
		this.lineID = lineID;
	}
	public String getChargeType() {
		return chargeType;
	}
	public void setChargeType(String chargeType) {
		this.chargeType = chargeType;
	}
	public double getQuantity() {
		return quantity;
	}
	public void setQuantity(double quantity) {
		this.quantity = quantity;
	}
	public long getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}
	

	
}
