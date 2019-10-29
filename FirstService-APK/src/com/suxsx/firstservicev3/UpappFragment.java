package com.suxsx.firstservicev3;

//import android.app.Fragment;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.suxsx.firstservice.R;
import com.suxsx.firstservicev3.util.IabHelper;
import com.suxsx.firstservicev3.util.IabResult;
import com.suxsx.firstservicev3.util.Purchase;
import com.suxsx.firstservicev3.util.Inventory;

public class UpappFragment extends Fragment {
	
    protected static final String TAG = "uppgradeFragment";
	//In app buy code
    IabHelper mHelper;
    static final String SKU_PREMIUM = "noads";
    static final String SKU_DON10 = "donation_10";
    static final String SKU_DON50 = "donation_50";
    static final String SKU_DON100 = "donation_100";
    static final int RC_REQUEST = 10001;
    private LoginManagerShared logMan;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View V = inflater.inflate(R.layout.updateapp_layout, container, false);
        
        //Setter opp logman
        logMan = new LoginManagerShared(getActivity());
        
        
        //Gammel kode for og bytte bilde ved kjøp av preminum
        /*if(logMan.getNoAds() == true)
        	V.findViewById(R.id.UpdateLayout).setVisibility(View.GONE);
        
        else
        	V.findViewById(R.id.UpdateDoneLayout).setVisibility(View.GONE);*/
        
        //Inn app buy code
        String base64EncodedPublicKey = getResources().getString(R.string.myPublicKey);
        // compute your public key and store it in base64EncodedPublicKey
        mHelper = new IabHelper(V.getContext(), base64EncodedPublicKey);
        
        mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
        	   public void onIabSetupFinished(IabResult result) {
        	      if (!result.isSuccess()) {
        	         // Oh noes, there was a problem.
        	         Log.d(TAG, "Problem setting up In-app Billing: " + result);
        	      }            
        	         // Hooray, IAB is fully set up!  
        	   }
        	});
        
        
        
        //Setter opp update knappen Gammel KNapp for preminum
        /*final Button send = (Button) V.findViewById(R.id.upappButton);
        send.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
            	Log.d(TAG, "Upgrade button clicked; launching purchase flow for upgrade.");
                String payload = "";

                mHelper.launchPurchaseFlow(getActivity(), SKU_PREMIUM, RC_REQUEST,
                        mPurchaseFinishedListener, payload);
            }
        });*/
        
        //Setter opp 10 DON Knappe
        final ImageView sendDON10 = (ImageView) V.findViewById(R.id.Don10Img);
        sendDON10.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
            	Log.d(TAG, "Upgrade button clicked; launching purchase flow for upgrade.");

                /* TODO: for security, generate your payload here for verification. See the comments on
                 *        verifyDeveloperPayload() for more info. Since this is a SAMPLE, we just use
                 *        an empty string, but on a production app you should carefully generate this. */
                String payload = "";

                mHelper.launchPurchaseFlow(getActivity(), SKU_DON10, RC_REQUEST,
                        mPurchaseFinishedListener, payload);
            }
        });
        
        
        //Setter opp 50 DON Knappen
        final ImageView sendDON50 = (ImageView) V.findViewById(R.id.Don50Img);
        sendDON50.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
            	Log.d(TAG, "Upgrade button clicked; launching purchase flow for upgrade.");

                /* TODO: for security, generate your payload here for verification. See the comments on
                 *        verifyDeveloperPayload() for more info. Since this is a SAMPLE, we just use
                 *        an empty string, but on a production app you should carefully generate this. */
                String payload = "";

                mHelper.launchPurchaseFlow(getActivity(), SKU_DON50, RC_REQUEST,
                        mPurchaseFinishedListener, payload);
            }
        });
        
        
        //Setter opp 100 DON Knappen
        final ImageView sendDON100 = (ImageView) V.findViewById(R.id.Don100Img);
        sendDON100.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
            	Log.d(TAG, "Upgrade button clicked; launching purchase flow for upgrade.");

                /* TODO: for security, generate your payload here for verification. See the comments on
                 *        verifyDeveloperPayload() for more info. Since this is a SAMPLE, we just use
                 *        an empty string, but on a production app you should carefully generate this. */
                String payload = "";

                mHelper.launchPurchaseFlow(getActivity(), SKU_DON100, RC_REQUEST,
                        mPurchaseFinishedListener, payload);
            }
        });
        
        return V;     
	}
	
	// Callback for when a purchase is finished
    IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener = new IabHelper.OnIabPurchaseFinishedListener() {
        public void onIabPurchaseFinished(IabResult result, Purchase purchase) {
            Log.d(TAG, "Purchase finished: " + result + ", purchase: " + purchase);

            // if we were disposed of in the meantime, quit.
            if (mHelper == null) return;

            if (result.isFailure()) {
                complain("Error purchasing: " + result);
                return;
            }
            if (!verifyDeveloperPayload(purchase)) {
                complain("Error purchasing. Authenticity verification failed.");
                return;
            }

            Log.d(TAG, "Purchase successful.");
            
            
            //Gammel kode for preminum
            /*
            if (purchase.getSku().equals(SKU_PREMIUM)) {
                // bought the premium upgrade!
                Log.d(TAG, "Purchase is premium upgrade. Congratulating user.");
                
                Toast.makeText(getActivity()
                        .getApplicationContext(), 
                        "Tusen takk for ditt kjøp av Førstegangstjeneste A-B-C, vennligst lukk appen og åpne den på nytt.",
                        Toast.LENGTH_LONG).show();
                
                //Setter no ad false
                //mIsPremium = true;
                logMan.upDateNoAds(true);               
            }*/
        }
    };
    
    //Complainlogg
    void complain(String message) {
        Log.e(TAG, "**** TrivialDrive Error: " + message);
        Toast.makeText(getActivity()
                .getApplicationContext(), 
                "Error: " + message,
                Toast.LENGTH_LONG).show();
    }
	
    public boolean verifyDeveloperPayload(Purchase p) {
        String payload = p.getDeveloperPayload();

        /*
         * TODO: verify that the developer payload of the purchase is correct. It will be
         * the same one that you sent when initiating the purchase.
         *
         * WARNING: Locally generating a random string when starting a purchase and
         * verifying it here might seem like a good approach, but this will fail in the
         * case where the user purchases an item on one device and then uses your app on
         * a different device, because on the other device you will not have access to the
         * random string you originally generated.
         *
         * So a good developer payload has these characteristics:
         *
         * 1. If two different users purchase an item, the payload is different between them,
         *    so that one user's purchase can't be replayed to another user.
         *
         * 2. The payload must be such that you can verify it even when the app wasn't the
         *    one who initiated the purchase flow (so that items purchased by the user on
         *    one device work on other devices owned by the user).
         *
         * Using your own server to store and verify developer payloads across app
         * installations is recommended.
         */

        return true;
    }
}