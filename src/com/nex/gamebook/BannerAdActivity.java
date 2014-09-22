package com.nex.gamebook;

import android.app.Activity;
import android.os.Bundle;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.nex.gamebook.ads.AdFactory;

public abstract class BannerAdActivity extends Activity {
	private AdView mAdView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		onPreCreate(savedInstanceState);
		mAdView = (AdView) findViewById(R.id.banner_ad);
		if(AdFactory.displayAds && mAdView!=null)
		mAdView.loadAd(new AdRequest.Builder().build());
	}

	protected abstract void onPreCreate(Bundle savedInstanceState);
	
	@Override
	protected void onPause() {
		mAdView.pause();
		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
		mAdView.resume();
	}

	@Override
	protected void onDestroy() {
		mAdView.destroy();
		super.onDestroy();
	}
}
