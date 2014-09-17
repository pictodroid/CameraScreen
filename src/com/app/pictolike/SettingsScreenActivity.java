package com.app.pictolike;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


	public class SettingsScreenActivity extends Fragment{
//		@Override
//		public void onCreate(Bundle savedInstanceState) {
//	        super.onCreate(savedInstanceState);
//	        addPreferencesFromResource(R.layout.activity_settingsscreen);
//	    }
		
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.activity_settingsscreen, container,
					false);
			return rootView;
		}
	}
