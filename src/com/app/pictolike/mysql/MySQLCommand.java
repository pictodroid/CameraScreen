package com.app.pictolike.mysql;

import android.os.Handler;

abstract public class MySQLCommand implements Runnable {
	private int m_nErrCode;
	private OnCompleteListener m_oCompleteListener;
	private Handler m_oHandler;
	private Object m_oResult;
	
	MySQLCommand() {
		m_nErrCode = MySQLConnect.ERR_NONE;
		m_oCompleteListener = null;
		m_oResult = null;
		m_oHandler = new Handler();
	}

	@Override
	public void run() {
		command();
		m_oHandler.postDelayed(new Runnable() {			
			@Override
			public void run() {
				if (m_oCompleteListener != null)
					m_oCompleteListener.OnComplete(m_oResult);
			}
		}, 0);
	}
	
	abstract void command();
	
	public void setOnCompleteListener(OnCompleteListener listener) {
		m_oCompleteListener = listener;
	}
	
	void setErrorCode(int code) {
		m_nErrCode = code;
	}
	
	void setResult(Object result) {
		m_oResult = result;
	}
	
	Object getResult() {
		return m_oResult;
	}
	
	int getErrorCode() {
		return m_nErrCode;
	}
	
	public interface OnCompleteListener {
		public void OnComplete(Object result);
	}
}
