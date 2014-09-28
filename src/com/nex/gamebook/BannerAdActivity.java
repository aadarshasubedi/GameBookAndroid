package com.nex.gamebook;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.nex.gamebook.ads.AdFactory;

public abstract class BannerAdActivity extends Activity {
	private AdView mAdView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		onPreCreate(savedInstanceState);
		loadAd();
	}

	protected abstract void onPreCreate(Bundle savedInstanceState);
	
	
	
	protected void loadAd() {
		mAdView = (AdView) findViewById(R.id.banner_ad);
		if(AdFactory.displayAds && mAdView!=null) {
			showAd();
			mAdView.loadAd(new AdRequest.Builder().build());
		} else {
			hideAd();
		}
	}
	protected void showAd() {
		mAdView = (AdView) findViewById(R.id.banner_ad);
		if(mAdView!=null)
			mAdView.setVisibility(View.VISIBLE);
	}
	protected void hideAd() {
		mAdView = (AdView) findViewById(R.id.banner_ad);
		if(mAdView!=null)
			mAdView.setVisibility(View.GONE);
	}
	
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
