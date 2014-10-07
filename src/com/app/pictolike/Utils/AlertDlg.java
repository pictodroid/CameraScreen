package com.app.pictolike.Utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;


public class AlertDlg
{
	private AlertDialog.Builder alertDialogBuilder;
	private String strOk;
	
	public AlertDlg(Context context, String title, String msg){
		
		alertDialogBuilder = new AlertDialog.Builder(context);

		// set title
		alertDialogBuilder.setTitle(title);

		// set dialog message
		alertDialogBuilder.setMessage(msg);
		alertDialogBuilder.setCancelable(false);
		
		strOk = "yes";
	}
	
	public void setButtonText(String strOk){
		this.strOk = strOk;
	}
	
	public void show(){
		
		alertDialogBuilder.setPositiveButton(strOk, new DialogInterface.OnClickListener() {
			
			public void onClick(DialogInterface dialog, int id) {
				// current activity
				dialog.cancel();
			}
		});
		
		// create alert dialog
		AlertDialog alertDialog = alertDialogBuilder.create();
		// show it
		alertDialog.show();
	}
}