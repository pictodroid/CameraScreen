package com.app.pictolike.Utils;

import java.lang.ref.WeakReference;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.pictolike.R;


public class UploadWaitDlg extends Dialog {

	private static final String TAG = "UploadWaitDlg";
	private static final long INTERVAL = 1000;
	private static final long ANIMATION_TIME = 2000;

	private static final int MSG_CANCEL = 10;
	private static final int MSG_ANIMATION = 11;
	private static final int MSG_TEXT = 12;

	private TextView mMessageTextView;
	private ImageView mAniView;
	private View mMsgView;
	private WeakHandler mHandler;

	public UploadWaitDlg(Context context) {
		super(context);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.setContentView(R.layout.upload_waiting_dlg);

		mHandler = new WeakHandler(this);

		mAniView = (ImageView) findViewById(R.id.imgView);
		mMsgView = findViewById(R.id.msgView);
		mMessageTextView = (TextView) findViewById(R.id.txtMsg);

		findViewById(R.id.btnOk).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				cancel();
			}
		});

		showAnimation(false);
	}

	public void showAnimation(boolean uiThread) {
		if (uiThread) {
			showAnimation();
		} else {
			mHandler.sendEmptyMessage(MSG_ANIMATION);
		}
	}

	public void showMessage(String message) {

		Message msg = new Message();
		msg.what = MSG_TEXT;
		msg.obj = message;
		mHandler.sendMessageDelayed(msg, ANIMATION_TIME);
	}

	private void showAnimation() {
		mAniView.setVisibility(View.VISIBLE);
		mMsgView.setVisibility(View.INVISIBLE);
		((AnimationDrawable) mAniView.getDrawable()).start();
	}

	private void show(String message) {
		mMessageTextView.setText(message);
		mAniView.setVisibility(View.INVISIBLE);
		mMsgView.setVisibility(View.VISIBLE);
		mHandler.sendEmptyMessageDelayed(MSG_CANCEL, INTERVAL);
	}

	@Override
	public void cancel() {
		try {
			if (mHandler.hasMessages(MSG_CANCEL)) {
				mHandler.removeMessages(MSG_CANCEL);
			}
			super.cancel();
		} catch (Exception e) {
			Log.e(TAG, "error", e);
		}
	}

	private void hanldeMessage(Message msg) {
		switch (msg.what) {
		case MSG_CANCEL:
			cancel();
			break;
		case MSG_ANIMATION:
			showAnimation();
			break;
		case MSG_TEXT:
			show((String) msg.obj);
			break;
		default:
			break;
		}
	}

	private static class WeakHandler extends Handler {

		WeakReference<UploadWaitDlg> reference = null;

		WeakHandler(UploadWaitDlg dlg) {
			reference = new WeakReference<UploadWaitDlg>(dlg);
		}

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			UploadWaitDlg dlg = reference.get();
			if (dlg != null) {
				dlg.hanldeMessage(msg);
			}
		}
		
	}
}
