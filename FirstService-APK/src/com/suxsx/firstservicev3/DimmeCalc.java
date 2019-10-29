package com.suxsx.firstservicev3;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DimmeCalc {	
	
	public int getDiff(String dateFuture)
	{
		SimpleDateFormat dfDate  = new SimpleDateFormat("dd/MM/yyyy");
	    java.util.Date d = null;
	    java.util.Date d1 = null;
	    Calendar cal = Calendar.getInstance();
	    try {
	            d = dfDate.parse(dateFuture);
	            d1 = dfDate.parse(dfDate.format(cal.getTime()));//Returns 15/10/2012
	        } catch (java.text.ParseException e) {
	            e.printStackTrace();
	        }

	    int diffInDays = (int) ((d.getTime() - d1.getTime())/ (1000 * 60 * 60 * 24));
	 
	    return diffInDays;
	}
}
