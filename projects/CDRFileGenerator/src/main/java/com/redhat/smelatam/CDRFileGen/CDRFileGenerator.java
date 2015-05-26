package com.redhat.smelatam.CDRFileGen;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class CDRFileGenerator {

	public static final int LINE_MIN = 1111111;
	public static final int LINE_MAX = 9999999;
	public static final String[] ChargeTypes = {"SMS","CALL","DATA"};
	public static final int AMOUNT_MAX=9999;
	public static final int TIMESTAMP_MAX = 2678400;
	

	public static void main(String[] args) {
			
		int recordCount = Integer.valueOf(args[0]);

			FileWriter cdrFile=null;
			try {
				cdrFile = new FileWriter("CDRFile" + recordCount + ".txt");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			String line ="";			
			 
			for(int i=0;i<recordCount;i++){
				line = getRandom(LINE_MIN,LINE_MAX) + "|" + ChargeTypes[getRandom(0,(ChargeTypes.length-1))] + "|" + getRandom(1,AMOUNT_MAX) + "|" + (System.currentTimeMillis() + getRandom(0,TIMESTAMP_MAX)) + "*";
				try {
					cdrFile.write(line);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			try {
				cdrFile.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
	}
	public static int getRandom(int min, int max){
		Random rn = new Random();
		int range = (max - min)  + 1;
		return (rn.nextInt(range) + min);
	}

}
