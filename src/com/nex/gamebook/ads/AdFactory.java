package com.nex.gamebook.ads;

import android.content.Context;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.nex.gamebook.R;

public class AdFactory {
	public static boolean displayAds = true;
	public static InterstitialAd loadDefaultInterstitialAd(Context ctx) {
		return loadInterstitialAd(ctx, R.string.ad_interistial_story);
	}
	public static InterstitialAd loadInterstitialAd(Context ctx, int id) {
//		Toast.makeText(ctx, "DisplayingAd", Toast.LENGTH_LONG).show();
		final InterstitialAd mInterstitial = new InterstitialAd(ctx);
		mInterstitial.setAdUnitId(ctx.getString(id));
		mInterstitial.setAdListener(new AdListener() {
			@Override
			public void onAdLoaded() {
				mInterstitial.show();
			}
		});
		if(displayAds)
		mInterstitial.loadAd(new AdRequest.Builder().build());
		return mInterstitial;
	}	
}
