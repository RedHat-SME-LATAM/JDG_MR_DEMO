package com.redhat.smelatam.mr;

import org.infinispan.distexec.mapreduce.Collector;
import org.infinispan.distexec.mapreduce.Mapper;

import com.redhat.smelatam.model.ChargingDataRecord;
import com.redhat.smelatam.utils.Utils;
public class BillingMapper implements Mapper<String,String,Long,Double>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void map(String recordID, String cdrStr, Collector<Long, Double> collector) {
		ChargingDataRecord cdr = new ChargingDataRecord(cdrStr);
		Double price = Utils.rate(cdr);
		collector.emit(cdr.getLineID(), price);		
		System.out.println("recordID: " + recordID + " - lineID: " + cdr.getLineID());
		
	}

	
	
}
