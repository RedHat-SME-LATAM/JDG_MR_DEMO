package com.redhat.smelatam.mr;

import org.infinispan.distexec.mapreduce.Collector;
import org.infinispan.distexec.mapreduce.Mapper;

import com.redhat.smelatam.model.ChargingDataRecord;
import com.redhat.smelatam.utils.Utils;

public class StatsMapper implements Mapper<String, String, String, Double> {

	private static final long serialVersionUID = 1L;
	
	public void map(String recordID, String cdrStr, Collector<String,Double> collector) {
		ChargingDataRecord cdr = new ChargingDataRecord(cdrStr);
		Double price = Utils.rate(cdr);
		collector.emit(cdr.getChargeType(), price);
		System.out.println("Record ID: " + recordID + " - ChargeType: " + cdr.getChargeType() + " - Amount: " + price);
	};

}
