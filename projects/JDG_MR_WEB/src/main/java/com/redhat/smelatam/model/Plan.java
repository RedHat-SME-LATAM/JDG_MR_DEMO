package com.redhat.smelatam.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Plan implements Serializable {

	public Plan(long planID, String planDescription) {
		super();
		this.planID = planID;
		this.planDescription = planDescription;
	}
	public Plan(long planID, String planDescription, Map<String, Double> prices) {
		super();
		this.planID = planID;
		this.planDescription = planDescription;
		this.prices = prices;
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private long planID;
	private String planDescription;
	private Map<String,Double> prices=new HashMap<String,Double>();
	
	
	public long getPlanID() {
		return planID;
	}
	public void setPlanID(long planID) {
		this.planID = planID;
	}
	public String getPlanDescription() {
		return planDescription;
	}
	public void setPlanDescription(String planDescription) {
		this.planDescription = planDescription;
	}
	public Map<String, Double> getPrices() {
		return prices;
	}
	public void setPrices(Map<String, Double> prices) {
		this.prices = prices;
	}
	public void addPrice(String chargeType, Double price){
		this.prices.put(chargeType, price);
	}
	public Double getPrice(String chargeType){
		return this.prices.get(chargeType);
	}
	

}
