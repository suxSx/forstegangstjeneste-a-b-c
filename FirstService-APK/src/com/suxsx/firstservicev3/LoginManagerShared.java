package com.suxsx.firstservicev3;

import android.content.Context;
import android.preference.PreferenceManager;

public class LoginManagerShared {
	
	final SharedPreCon precon;
	
	public LoginManagerShared(Context myCons)
	{
		precon = new SharedPreCon(myCons);
	}
	
	
	public void saveLoginFaceBook(boolean is)
	{	
		
		if(is == true)
			precon.set("facebookloggedin", "y");
		
		if(is == false)
			precon.set("facebookloggedin", "n");
	}
	
	public boolean LoginFaceBook()
	{
		String logg = precon.get("facebookloggedin", "n");
		
		if(logg.equals("n"))
			return false;
		
		else
			return true;
	}
	
	public boolean getNoAds()
    {
		String logg = precon.get("noads", "n");
		
		if(logg.equals("n"))
			return false;
		
		else 
			return true;
    }
    
    
    public void upDateNoAds(boolean on)
    {
    	if(on == true)
    	{
    		precon.set("noads", "y");
    	}
    	
    	else
    	{
    		precon.set("noads", "n");
    	}
    }
}
