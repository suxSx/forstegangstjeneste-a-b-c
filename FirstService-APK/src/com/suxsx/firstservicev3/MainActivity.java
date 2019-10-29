-package com.suxsx.firstservicev3;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.suxsx.firstservice.R;
import com.suxsx.firstservicev3.util.IabHelper;
import com.suxsx.firstservicev3.util.IabResult;
import com.suxsx.firstservicev3.util.Purchase;
import com.suxsx.firstservicev3.util.Inventory;
import com.tjeannin.apprate.AppRate;

import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;
import android.support.v4.app.*;
//import android.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends FragmentActivity {
	
	protected static final String TAG = "mainAC";
	private boolean exit = false;
	
	//Test 2 funker
	private String[] drawerListViewItems;
    private DrawerLayout drawerLayout;
    private ListView drawerListView;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    public FragmentManager fragmentManager = getSupportFragmentManager();
    //private android.app.FragmentManager fragmentManager = getFragmentManager();
    
    //In app buy code
    IabHelper mHelper;
    static final String SKU_PREMIUM = "noads";
    static final int RC_REQUEST = 10001;
    
    //Help Manager
    private LoginManagerShared logMan;
  	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        //Setter opp logMan
        logMan = new LoginManagerShared(getBaseContext());
        
        //Fjerner Reklamen
        logMan.upDateNoAds(true);
        
		 //Get Key for FaceBook App
		 /*PackageInfo info;
	        try {
	            info = getPackageManager().getPackageInfo(
	            		this.getPackageName(), 
	                    PackageManager.GET_SIGNATURES);
	            for (Signature signature : info.signatures) {
	                MessageDigest md = MessageDigest.getInstance("SHA");
	                md.update(signature.toByteArray());
	                
	                String keyH = Base64.encodeToString(md.digest(), Base64.DEFAULT);
	                
	                Toast.makeText(this, keyH, Toast.LENGTH_LONG).show();
	                
	                Log.d("KeyHash:", keyH);
	                }
	        } catch (NameNotFoundException e) {

	        } catch (NoSuchAlgorithmException e) {

	        }*/
        
        //Inn app buy code
        String base64EncodedPublicKey = getResources().getString(R.string.myPublicKey);
        // compute your public key and store it in base64EncodedPublicKey
        mHelper = new IabHelper(this, base64EncodedPublicKey);
        
        mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
        	   public void onIabSetupFinished(IabResult result) {
        	      if (!result.isSuccess()) {
        	         // Oh noes, there was a problem.
        	         Log.d(TAG, "Problem setting up In-app Billing: " + result);
        	         return;
        	      }            
        	         // Hooray, IAB is fully set up!  
        	      	//Finner ut av hva vi eier
        	      mHelper.queryInventoryAsync(mGotInventoryListener);
        	   }
        	});
     
        if(logMan.getNoAds() == false)
        {
        	//Apprater
        	AlertDialog.Builder builder = new AlertDialog.Builder(this)
        	//.setCustomTitle(myCustomTitleView)
        	//.setIcon(R.drawable.my_custom_icon)
        	.setMessage("Hei, det ser ut som at du liker denne applikasjonen.\n\nHva med og bruke 2 minutter på og gi en tilbakemelding?")
        	.setPositiveButton("OK", null)
        	.setNegativeButton("Nei", null)
        	.setNeutralButton("Senere", null);

        	new AppRate(this)
        	.setCustomDialog(builder)
        	.setMinLaunchesUntilPrompt(10)
        	.init();
        }
        
        
        //Her starter fragment
        fragmentManager.beginTransaction()
        .replace(R.id.flContent, new  HomeFragment())
        .commit();
 
        // get list items from strings.xml<
        drawerListViewItems = getResources().getStringArray(R.array.items);
        // get ListView defined in activity_main.xml
        drawerListView = (ListView) findViewById(R.id.left_drawer);
 
        // Set the adapter for the list view
        drawerListView.setAdapter(new ArrayAdapter<String>(this,
                R.layout.drawer_listview_item, drawerListViewItems));
 
        // 2. App Icon
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
 
        // 2.1 create ActionBarDrawerToggle
        actionBarDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
               drawerLayout,         /* DrawerLayout object */
               R.drawable.ic_drawer,  /* nav drawer icon to replace 'Up' caret */
               R.string.drawer_open,  /* "open drawer" description */
               R.string.drawer_close  /* "close drawer" description */
               );
 
        // 2.2 Set actionBarDrawerToggle as the DrawerListener
        drawerLayout.setDrawerListener(actionBarDrawerToggle);
 
        // 2.3 enable and show "up" arrow
        getActionBar().setDisplayHomeAsUpEnabled(true);
 
        // just styling option
        drawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
 
        drawerListView.setOnItemClickListener(new DrawerItemClickListener());
    }
    
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
      Log.d(TAG,"In on Key Down");
      if (keyCode == KeyEvent.KEYCODE_BACK) {
    	  if (exit)
              this.finish();
    	  
          else {
        	  fragmentManager.beginTransaction()
              .replace(R.id.flContent, new HomeFragment())
              .commit();
        	  
        	  Toast.makeText(this, "Trykk en gang til for å avslutte programme.", Toast.LENGTH_LONG).show();
              exit = true;
              
              new Handler().postDelayed(new Runnable() {
                  @Override
                  public void run() {
                      exit = false;
                  }
              }, 4 * 1000);

          }    	  
          return false;
      }
      return super.onKeyDown(keyCode, event);

    }
    
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
         actionBarDrawerToggle.syncState();
    }
 
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        actionBarDrawerToggle.onConfigurationChanged(newConfig);
    }
 
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
 
         // call ActionBarDrawerToggle.onOptionsItemSelected(), if it returns true
        // then it has handled the app icon touch event
 
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
 
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id) {
            //Toast.makeText(MainActivity.this, ((TextView)view).getText(), Toast.LENGTH_LONG).show();
        	drawerLayout.closeDrawer(drawerListView);
        	selectItem(position, ((TextView)view).getText().toString());             
        }
    }
    
    @Override
    public void onDestroy() {
       super.onDestroy();
       if (mHelper != null) mHelper.dispose();
       mHelper = null;
    }
    
    private void selectItem(int position, String textMain) {
        
        /*switch (position) {

            case 0:*/
    	
    	if(textMain.equals("Innledning"))
    	{
                fragmentManager.beginTransaction()
                        .replace(R.id.flContent, new  InnledningFragment())
                        .commit();
    	}
    	else if(textMain.equals("Fonetiske Alfabet"))
    	{

                fragmentManager.beginTransaction()
                        .replace(R.id.flContent, new FoniskFragment())
                        .commit();
                
                drawerListViewItems = getResources().getStringArray(R.array.items_fonisk);
                
                // Set the adapter for the list view
                drawerListView.setAdapter(new ArrayAdapter<String>(this,
                R.layout.drawer_listview_item, drawerListViewItems));
               
    	}

    	else if(textMain.equals("Forkortelser"))
        {
                fragmentManager.beginTransaction()
                        .replace(R.id.flContent, new ForkoFragment())
                        .commit();
        }

    	else if(textMain.equals("HK-416 Kulebane")) {

                fragmentManager.beginTransaction()
                        .replace(R.id.flContent, new KulebaneFragment())
                        .commit();
        }
                
    	else if(textMain.equals("Kneppetelt")) {

                fragmentManager.beginTransaction()
                        .replace(R.id.flContent, new KneppeteltFragment())
                        .commit();
        }
                
    	else if(textMain.equals("Huskeregler")) {

                fragmentManager.beginTransaction()
                        .replace(R.id.flContent, new HuskeFragment())
                        .commit();
                }
            
    	else if(textMain.equals("Sanitet Nivå 2")) {

                fragmentManager.beginTransaction()
                        .replace(R.id.flContent, new SanFragment())
                        .commit();
                }
                
    	else if(textMain.equals("Avstandsbedømmelse")) {

                fragmentManager.beginTransaction()
                        .replace(R.id.flContent, new AvsFragment())
                        .commit();
                }
                
    	else if(textMain.equals("Kart og Kompass")) {

                fragmentManager.beginTransaction()
                        .replace(R.id.flContent, new KartFragment())
                        .commit();
                }
                
    	else if(textMain.equals("Refselse")) {

                fragmentManager.beginTransaction()
                        .replace(R.id.flContent, new RefFragment())
                        .commit();
                }
                
    	else if(textMain.equals("Dimmekalkulator")) {

                fragmentManager.beginTransaction()
                        .replace(R.id.flContent, new DimFragment())
                        .commit();
                }
                
    	else if(textMain.equals("Linker")) {

                fragmentManager.beginTransaction()
                        .replace(R.id.flContent, new LinkFragment())
                        .commit();
                }
                
    	 else if(textMain.equals("Send inn Forslag")) {

                fragmentManager.beginTransaction()
                        .replace(R.id.flContent, new TilbakeFragment())
                        .commit();
                }
                
    	 else if(textMain.equals("Om Appen")) {

                fragmentManager.beginTransaction()
                        .replace(R.id.flContent, new OmFragment())
                        .commit();
                }
    	
    	 else if(textMain.equals("Fonetiske Alfabet Test")) {

             fragmentManager.beginTransaction()
                     .replace(R.id.flContent, new FonisktestFragment())
                     .commit();
             }
    	
    	 else if(textMain.equals("- Tilbake")) {

    		 	drawerListViewItems = getResources().getStringArray(R.array.items);
             
    		 	// Set the adapter for the list view
    		 	drawerListView.setAdapter(new ArrayAdapter<String>(this,
    		 			R.layout.drawer_listview_item, drawerListViewItems));
    		 	drawerLayout.openDrawer(drawerListView); 
    		 	
             }
    	
    	 else if(textMain.equals("HK-416")) {

    		 fragmentManager.beginTransaction()
             .replace(R.id.flContent, new Hk416Fragment())
             .commit();
     
    		 drawerListViewItems = getResources().getStringArray(R.array.items_hk416);
     
    		 // Set the adapter for the list view
    		 drawerListView.setAdapter(new ArrayAdapter<String>(this,
    		 R.layout.drawer_listview_item, drawerListViewItems));
    		 drawerLayout.openDrawer(drawerListView); 
 		 	
          }
    	
    	 else if(textMain.equals("Tips og Triks")) {

    		 fragmentManager.beginTransaction()
             .replace(R.id.flContent, new TipsFragment())
             .commit();
     
    		 drawerListViewItems = getResources().getStringArray(R.array.items_tipstriks);
     
    		 // Set the adapter for the list view
    		 drawerListView.setAdapter(new ArrayAdapter<String>(this,
    		 R.layout.drawer_listview_item, drawerListViewItems));
    		 drawerLayout.openDrawer(drawerListView); 
 		 	
          }
    	
    	 else if(textMain.equals("HK-416 Våpenpuss")) {

             fragmentManager.beginTransaction()
                     .replace(R.id.flContent, new Hk416pussFragment())
                     .commit();
             }
    	
    	 else if(textMain.equals("Sengestrekk")) {

             fragmentManager.beginTransaction()
                     .replace(R.id.flContent, new SengeFragment())
                     .commit();
             }
             
        else if(textMain.equals("Logg inn Facebook")) {

                 fragmentManager.beginTransaction()
                         .replace(R.id.flContent, new LoginFacebookFragment())
                         .commit();
                 }
    	
    	 else if(textMain.equals("Skapbretting")) {

             fragmentManager.beginTransaction()
                     .replace(R.id.flContent, new SkapFragment())
                     .commit();
             }
    	
    	 else if(textMain.equals("HMKG Tips")) {

             fragmentManager.beginTransaction()
                     .replace(R.id.flContent, new GardeFragment())
                     .commit();
             }
    	
    	 else if(textMain.equals("Skopuss")) {

             fragmentManager.beginTransaction()
                     .replace(R.id.flContent, new SkoFragment())
                     .commit();
             }
    	
    	 else if(textMain.equals("Skosåle")) {

             fragmentManager.beginTransaction()
                     .replace(R.id.flContent, new SoleFragment())
                     .commit();
             } 
    	
    	 else if(textMain.equals("Truger")) {

             fragmentManager.beginTransaction()
                     .replace(R.id.flContent, new TrugerFragment())
                     .commit();
             }
    	
    	 else if(textMain.equals("Donation")) {

             fragmentManager.beginTransaction()
                     .replace(R.id.flContent, new UpappFragment())
                     .commit();
             }
    	
    	 else if(textMain.equals("Hjelp")) {

             fragmentManager.beginTransaction()
                     .replace(R.id.flContent, new HjelpFragment())
                     .commit();
             }
    	
    	 else if(textMain.equals("Greps Faktorer")) {

             fragmentManager.beginTransaction()
                     .replace(R.id.flContent, new GrepFragment())
                     .commit();
             }
    	
    	 else if(textMain.equals("Tips Sekundærsikte")) {

             fragmentManager.beginTransaction()
                     .replace(R.id.flContent, new TipssecFragment())
                     .commit();
             }
    	
    	 else if(textMain.equals("Aimpoint Justering")) {

             fragmentManager.beginTransaction()
                     .replace(R.id.flContent, new AimjusFragment())
                     .commit();
             }
    	
    	 else if(textMain.equals("Tøm Våpen")) {

             fragmentManager.beginTransaction()
                     .replace(R.id.flContent, new TomvapFragment())
                     .commit();
             }
    	
    	 else if(textMain.equals("Magasinbytte")) {

             fragmentManager.beginTransaction()
                     .replace(R.id.flContent, new MagbytteFragment())
                     .commit();
             }
    	
    	 else if(textMain.equals("Tegn og Signaler")) {

             fragmentManager.beginTransaction()
                     .replace(R.id.flContent, new TegnsigFragment())
                     .commit();
             }
    	
    	 else if(textMain.equals("Målangivelse")) {

             fragmentManager.beginTransaction()
                     .replace(R.id.flContent, new MalsigangFragment())
                     .commit();
             }
    	
    	 else if(textMain.equals("Primus Fyring")) {

             fragmentManager.beginTransaction()
                     .replace(R.id.flContent, new PrimusfyrFragment())
                     .commit();
             }
    	
    	 else if(textMain.equals("Fysiske Krav")) {

             fragmentManager.beginTransaction()
                     .replace(R.id.flContent, new FysiskekravFragment())
                     .commit();
             }
    	
    	 else {

             fragmentManager.beginTransaction()
                     .replace(R.id.flContent, new HomeFragment())
                     .commit();
             }
    	
    		if(logMan.getNoAds() == false)
    		{	
    			showAdd();
    		}
        }
    
    private int addCount = 0;
    private void showAdd()
    {

    }
    
    //Complainlogg
    public void complain(String message) {
        Log.e(TAG, "**** TrivialDrive Error: " + message);
        Toast.makeText(getApplicationContext(), 
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
    
    // Listener that's called when we finish querying the items and subscriptions we own
    IabHelper.QueryInventoryFinishedListener mGotInventoryListener = new IabHelper.QueryInventoryFinishedListener() {
        public void onQueryInventoryFinished(IabResult result, Inventory inventory) {
            Log.d(TAG, "Query inventory finished.");

            // Have we been disposed of in the meantime? If so, quit.
            if (mHelper == null) return;

            // Is it a failure?
            if (result.isFailure()) {
                complain("Failed to query inventory: " + result);
                return;
            }

            Log.d(TAG, "Query inventory was successful.");

            /*
             * Check for items we own. Notice that for each purchase, we check
             * the developer payload to see if it's correct! See
             * verifyDeveloperPayload().
             */

            // Do we have the premium upgrade?
            boolean mIsPremium = false;
            Purchase premiumPurchase = inventory.getPurchase(SKU_PREMIUM);
            mIsPremium = (premiumPurchase != null && verifyDeveloperPayload(premiumPurchase));
            
            if(mIsPremium == true)
            {
            	logMan.upDateNoAds(true);
            	
            	fragmentManager.beginTransaction()
                .replace(R.id.flContent, new HomeFragment())
                .commit();
            }

            Log.d(TAG, "User is " + (mIsPremium ? "PREMIUM" : "NOT PREMIUM"));
            Log.d(TAG, "Initial inventory query finished; enabling main UI.");
        }
    };
    
    public void updateFragmentUser()
    {
    	fragmentManager.beginTransaction()
        .replace(R.id.flContent, new HomeFragment())
        .commit();
    }
}
