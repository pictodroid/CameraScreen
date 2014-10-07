package com.app.pictolike.Utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;


public class ConfirmDlg 
{
	private AlertDialog.Builder alertDialogBuilder;
	private String strOk, strCancel;
	private ConfirmListener listener;
	
	public ConfirmDlg(Context context, String title, String msg){
		
		alertDialogBuilder = new AlertDialog.Builder(context);
		
		// set title
		alertDialogBuilder.setTitle(title);

		// set dialog message
		alertDialogBuilder.setMessage(msg);
		alertDialogBuilder.setCancelable(false);
		
		strOk = "yes";
		strCancel = "no";
		
		listener = null;
	}
	
	public void setButtonText(String strOk, String strCancel){
		this.strOk = strOk;
		this.strCancel = strCancel;
	}
	
	public void setConfirmListener(ConfirmListener listener){
	
		this.listener = listener;
	}
	
	public void show(){
		
		alertDialogBuilder.setPositiveButton(strOk, new DialogInterface.OnClickListener() {
			
			public void onClick(DialogInterface dialog, int id) {
				// current activity
				if (listener != null)
					listener.onOkClick();
				
				dialog.cancel();
			}
		});
		
		alertDialogBuilder.setNegativeButton(strCancel, new DialogInterface.OnClickListener() {
			
			public void onClick(DialogInterface dialog, int id) {
				// current activity
				if (listener != null)
					listener.onCancelClick();
				
				dialog.cancel();
			}
		});
		
		// create alert dialog
		AlertDialog alertDialog = alertDialogBuilder.create();
		// show it
		alertDialog.show();
	}
	
	public static interface ConfirmListener{
		
		public void onOkClick();
		public void onCancelClick();		
	}
}