package com.suxsx.firstservicev3;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.suxsx.firstservice.R;

public class GoogleAdManager {
	
	private AdView adView;
	private ImageView imageView;
	private boolean firstAdReceived = false;
	private boolean addOn = false;
	private boolean googleOn = false;
	private LoginManagerShared logMan;
	
	public GoogleAdManager(View V, Context nowCon) {
		//String addson = V.getResources().getString(R.string.freeVersion);
		
		final Context myCon = nowCon;
		
		//Setter opp logman
        logMan = new LoginManagerShared(nowCon);
        
		if(logMan.getNoAds()  == false)
		{
			//ad kode
			adView = (AdView)V.findViewById(R.id.adView);
	    	AdRequest adRequest = new AdRequest.Builder().build();
	    	
	    	imageView = (ImageView) V.findViewById(R.id.noAdImage);
	    	//Sett click funksjon til play butikk.
	    	imageView.setOnClickListener(new View.OnClickListener() {
	            	public void onClick(View v) {
	            		Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://forstegangstjeneste.com"));
	            		myCon.startActivity(intent);
	            	}
	    	});
	    	
			if(googleOn == true)
			{
		    	adView.setAdListener(new AdListener(){
		    	
		    		public void onAdLoaded() {
		    		  	firstAdReceived = true;
		    		  	// Hide the custom image and show the AdView.
		    		  	imageView.setVisibility(View.GONE);
		    		  	adView.setVisibility(View.VISIBLE);
		    		  
		    		  
		    		  
		    			}
		    	
		    		public void onAdFailedToLoad(int errorCode) {
		    		  	if (!firstAdReceived) {
		    			  	// Hide the AdView and show the custom image.
		    			  	adView.setVisibility(View.GONE);
		    		    	imageView.setVisibility(View.VISIBLE);
	
		    		    	// Turn off the click listener if there is a network error.
		    		    	if (errorCode == 2) {
		    		    	//imageView.setOnClickListener(null);
		    		    	} else { //Klikk listener maybe for custom ad. 
		    		    		}
		    		    	}
		    		  	}
		    	});
		    	adView.loadAd(adRequest);
			}
			
			else
			{		    	
		    	//Load my image. And direct them to my site
				adView.setVisibility(View.GONE);
		    	imageView.setVisibility(View.VISIBLE);
			}
		}
		
		else
		{
			V.findViewById(R.id.adViewLinearLayout).setVisibility(View.GONE);
		}
	}
	
}
