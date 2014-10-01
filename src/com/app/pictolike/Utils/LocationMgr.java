package com.app.pictolike.Utils;

import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;


public class LocationMgr {
	
	private static LocationMgr mInstance;
	
	private Context ctx;
	private String location = "";
	
	public static void createInstance(Context ctx){
		
		mInstance = new LocationMgr(ctx);
	}
	
	public static LocationMgr getInstance(){
	
		return mInstance;
	}
	
	public LocationMgr(Context ctx){
		
		this.ctx = ctx;
		findAddress();
	}
	
	public String getLocation(){
		return location;
	}
	
	private Location getLastBestLocation() {
		
		LocationManager locManager =  (LocationManager) ctx.getSystemService(Context.LOCATION_SERVICE);
		
	    Location locationGPS = locManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
	    Location locationNet = locManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

	    long GPSLocationTime = 0;
	    if (null != locationGPS) { GPSLocationTime = locationGPS.getTime(); }

	    long NetLocationTime = 0;

	    if (null != locationNet) {
	        NetLocationTime = locationNet.getTime();
	    }

	    if ( 0 < GPSLocationTime - NetLocationTime ) {
	        return locationGPS;
	    }
	    else {
	        return locationNet;
	    }
	}

	private void findAddress(){
		
		Location loc = getLastBestLocation();
		if (loc == null)
			return;
		
		Geocoder geocoder = new Geocoder(ctx, Locale.getDefault());
        List<Address> addresses = null;
        
        double latitude = 0.0f;
        double longitude = 0.0f;
        try {
			 
			 latitude = loc.getLatitude();
			 longitude = loc.getLongitude();
			 
			 // Getting a maximum of 3 Address that matches the input text
			 addresses = geocoder.getFromLocation(latitude, longitude, 1);
			 
			 Log.e("Location2", "getFromLocation ok");

			 if (addresses != null && addresses.size() > 0){
				 
				 Address add1 = addresses.get(0);
				 location = add1.getLocality() + "-" + add1.getAdminArea() + "-" + add1.getCountryName();
				 
				 if (location.length() > 19){
					 location = location.substring(0, 16) + "..";
				 }
				 
				 Log.e("Location2", "getFromLocation location=" + location);
			 }
	        	
        } catch (Exception e) {
        	
        	location = "latt:" + latitude + "long:" + longitude;
        	Log.e("Location2", "GeocoderTask Exception=" + e.toString());
        }
	}

}
