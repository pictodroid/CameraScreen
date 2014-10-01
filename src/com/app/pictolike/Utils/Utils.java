package com.app.pictolike.Utils;

import android.content.Context;
import android.content.SharedPreferences;

public class Utils {

	public static final String PREFS_NAME = "inDistance";
	
	public static void setSharedPreferencesString(Context context,String key, String value)
	{ 
		
		SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putString(key, value).commit();
	}
	
	public static void setSharedPreferencesBoolean(Context context,String key, Boolean value)
	{ 
		
		SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putBoolean(key, value).commit();
	}
	
	public static Boolean getSharedPreferencesBoolean(Context context,String key)
	{ 

		SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, 0);
		return settings.getBoolean(key, false); 
	}
	
	public static String getSharedPreferencesString(Context context,String key)
	{ 

		SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, 0);
		return settings.getString(key, ""); 
	}
	
}
