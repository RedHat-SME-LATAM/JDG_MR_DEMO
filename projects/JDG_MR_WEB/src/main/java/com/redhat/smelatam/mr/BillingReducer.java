package com.redhat.smelatam.mr;

import java.util.Iterator;

import org.infinispan.distexec.mapreduce.Reducer;

public class BillingReducer implements Reducer<Long, Double> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public Double reduce(Long line, Iterator<Double> prices) {
		Double totalPrice=0.0;
		
		while(prices.hasNext()) {
			totalPrice += prices.next();
		}
		System.out.println("Line: " + line + " amount: " + totalPrice);
		return totalPrice;
	}
	

}
