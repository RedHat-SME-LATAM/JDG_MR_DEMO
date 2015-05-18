package com.redhat.smelatam.model;

import java.io.Serializable;
import java.util.List;

public class Line implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private long lineid;
	private long customerid;
	private List<ChargingDataRecord> charges;
	
	
	public List<ChargingDataRecord> getCharges() {
		return charges;
	}
	public void setCharges(List<ChargingDataRecord> charges) {
		this.charges = charges;
	}
	public long getLineid() {
		return lineid;
	}
	public void setLineid(long lineid) {
		this.lineid = lineid;
	}
	public long getCustomerid() {
		return customerid;
	}
	public void setCustomerid(long customerid) {
		this.customerid = customerid;
	}

	
}
