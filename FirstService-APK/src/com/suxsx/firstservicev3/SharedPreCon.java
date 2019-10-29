package com.suxsx.firstservicev3;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SharedPreCon {
	
	final private SharedPreferences prefs;
	
	public SharedPreCon (Context nowCon)
		{ prefs = PreferenceManager.getDefaultSharedPreferences(nowCon.getApplicationContext()); }
	
	public String get(String Name, String Default)
	{
		return prefs.getString(Name, Default);
	}
	
	public void set(String Name, String Value)
	{	
		prefs.edit().putString(Name, Value).commit();
	}
}
