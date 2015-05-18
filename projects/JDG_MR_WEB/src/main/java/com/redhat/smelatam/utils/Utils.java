package com.redhat.smelatam.utils;

import com.redhat.smelatam.model.ChargingDataRecord;

public class Utils {
	
	public static Double rate(ChargingDataRecord cdr) {
		Double price=0.0;
		switch (cdr.getChargeType()) {
		case "CALL":
				price = cdr.getQuantity()* 0.05;
			break;
			
		case "DATA":
			price = cdr.getQuantity()* 0.01;
			break;
			
		case "SMS":
			price = cdr.getQuantity()*0.5 ;
			break;

		default:
			break;
		}
		
		return price;
	}

}
