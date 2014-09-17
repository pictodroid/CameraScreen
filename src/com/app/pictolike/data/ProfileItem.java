package com.app.pictolike.data;

public class ProfileItem {
	private String mUrl;
	private int mLikeCount;
	private int mSeenCount;
	private int mSavedCount;
	
	public String getmUrl() {
		return mUrl;
	}
	public void setUrl(String mUrl) {
		this.mUrl = mUrl;
	}
	public int getLikeCount() {
		return mLikeCount;
	}
	public void setLikeCount(int mLikeCount) {
		this.mLikeCount = mLikeCount;
	}
	public int getSeenCount() {
		return mSeenCount;
	}
	public void setSeenCount(int mSeenCount) {
		this.mSeenCount = mSeenCount;
	}
	public int getSavedCount() {
		return mSavedCount;
	}
	public int setSavedCount(int mSavedCount) {
		return mSavedCount;
	}
	
}