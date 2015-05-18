package com.redhat.smelatam.model;

import java.io.Serializable;
import java.util.List;

public class Customer implements Serializable {
	
	public Customer(String name, String lastName, long customerID) {
		super();
		this.name = name;
		this.lastName = lastName;
		this.customerID = customerID;
	}


	private String name;
	private String lastName;
	private long customerID;
	private List<Line> lines;
	private long planID;
	
	public Customer(String name, String lastName, long customerID,
			List<Line> lines, long planID) {
		super();
		this.name = name;
		this.lastName = lastName;
		this.customerID = customerID;
		this.lines = lines;
		this.planID = planID;
	}
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public long getCustomerID() {
		return customerID;
	}
	public void setCustomerID(long customerID) {
		this.customerID = customerID;
	}

	public long getPlanID() {
		return planID;
	}
	public void setPlanID(long planID) {
		this.planID = planID;
	}
	

	public List<Line> getLines() {
		return lines;
	}
	public void setLines(List<Line> lines) {
		this.lines = lines;
	}
	public void addLine(Line line){
		this.lines.add(line);
	}
	public boolean hasLineID(long lineID){
			
		for(Line line:this.lines){
			if(line.getLineid()==lineID)
				return true;
		}
		return false;
	}
	

}
