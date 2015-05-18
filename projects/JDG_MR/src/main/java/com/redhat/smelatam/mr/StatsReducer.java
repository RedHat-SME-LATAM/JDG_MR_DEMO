package com.redhat.smelatam.mr;

import java.util.Iterator;

import org.infinispan.distexec.mapreduce.Reducer;

public class StatsReducer implements Reducer<String, Double> {

	private static final long serialVersionUID = 1L;

	@Override
	public Double reduce(String chargeType, Iterator<Double> prices) {
		Double totalPrice=0.0;
		
		while(prices.hasNext()) {
			totalPrice += prices.next();
		}
		System.out.println("ChargeType: " + chargeType + " amount: " + totalPrice);
		return totalPrice;
	}
	

}
