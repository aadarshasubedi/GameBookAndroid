package com.nex.gamebook.ads;

import android.content.Context;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.nex.gamebook.R;

public class AdFactory {
	public static boolean displayAds = true;
	public static InterstitialAd loadDefaultInterstitialAd(Context ctx) {
		return loadDefaultInterstitialAd(ctx, null);
	}
	public static InterstitialAd loadDefaultInterstitialAd(Context ctx, OnAdClosed listener) {
		return loadInterstitialAd(ctx, R.string.ad_interistial_story, listener);
	}
	public static InterstitialAd loadInterstitialAd(Context ctx, int id, final OnAdClosed listener) {
		final InterstitialAd mInterstitial = new InterstitialAd(ctx);
		mInterstitial.setAdUnitId(ctx.getString(id));
		mInterstitial.setAdListener(new AdListener() {
			@Override
			public void onAdLoaded() {
				mInterstitial.show();
			}
			@Override
			public void onAdClosed() {
				if(listener!=null)
					listener.closed();
			}
		});
		if(displayAds)
		mInterstitial.loadAd(new AdRequest.Builder().build());
		else if(listener!=null) listener.closed();
		return mInterstitial;
	}	
	
	
	public static interface OnAdClosed {
		void closed();
	}
	
}
