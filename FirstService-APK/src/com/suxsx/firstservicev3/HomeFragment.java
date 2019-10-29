package com.suxsx.firstservicev3;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.facebook.FacebookRequestError;
import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.RequestAsyncTask;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.suxsx.firstservice.R;







//import android.app.Fragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class HomeFragment extends Fragment {
	
	//Values facebook
	private static final List<String> PERMISSIONS = Arrays.asList("publish_actions");
	private static final String PENDING_PUBLISH_KEY = "pendingPublishReauthorization";
	private boolean pendingPublishReauthorization = false;
	private LoginManagerShared logMan;
			
	private static final String TAG = "HomeFragment";
	private UiLifecycleHelper uiHelper;
	
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View V = inflater.inflate(R.layout.home, container, false);
        
        //Setter opp logman
        logMan = new LoginManagerShared(getActivity());
        
        //Skjuler facefra og kjop fra starten skal kun vises ved gratis og nett
        V.findViewById(R.id.layerDelFace).setVisibility(View.GONE);
        V.findViewById(R.id.layerKjopPro).setVisibility(View.GONE);
        V.findViewById(R.id.layerDelFaceLogin).setVisibility(View.GONE);
        
        //String freeVersion = V.getResources().getString(R.string.freeVersion);
        String freeVersion = "y";
        
        if(logMan.getNoAds() == true)
        	freeVersion = "n";
        
        if(freeVersion.equals("y"))
		{
        	//Show FaceShare
        	if(Login() == true)
        	{
        		if(checkInternetConnection() == true)
        		{
        			V.findViewById(R.id.layerDelFace).setVisibility(View.VISIBLE);
        		}
        	}
        	
        	else
        	{
        		V.findViewById(R.id.layerDelFaceLogin).setVisibility(View.VISIBLE);
        	}
		}
        	
    	V.findViewById(R.id.layerKjopPro).setVisibility(View.VISIBLE);
		

        
        DimmeCalc exCalcu = new DimmeCalc();		
		String DIM = PreferenceManager.getDefaultSharedPreferences(getActivity().getBaseContext()).getString("dimmedag", "nodim");
        int diffInDaysD = 0;
		
		if(DIM == "nodim")
		{
			V.findViewById(R.id.layerDIMHome).setVisibility(View.GONE);
		}
		
		else
		{
			V.findViewById(R.id.layerDIMHome).setVisibility(View.VISIBLE);
			diffInDaysD = exCalcu.getDiff(DIM);
		}
		
		TextView firstD = (TextView) V.findViewById(R.id.days_to__dim);
   	 	firstD.setText(String.valueOf(diffInDaysD));

   	 	//Setup Share knapp
   	 	ImageView img = (ImageView) V.findViewById(R.id.shareFaceImg);
   	 	img.setOnClickListener(new View.OnClickListener() {
   	 		public void onClick(View v) {
   	 		 Toast.makeText(getActivity()
                     .getApplicationContext(), 
                     "Poster til Facebook, vennligst vent...",
                     Toast.LENGTH_LONG).show();
   	 			publishStory();
   	 		}
   	 	});
   	 	
   	 	//Setup Add knapp
   	 	ImageView imgAd = (ImageView) V.findViewById(R.id.KjopProImg);
   	 	imgAd.setOnClickListener(new View.OnClickListener() {
   	 		public void onClick(View v) {
   	 			FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
				fragmentManager.beginTransaction()
				.replace(R.id.flContent, new  UpappFragment())
				.commit();
   	 		}
   	 	});
   	 	
   	 	//Setup Login Facebook
   	 	ImageView imgAdFace = (ImageView) V.findViewById(R.id.shareFaceImgLogin);
   	 	imgAdFace.setOnClickListener(new View.OnClickListener() {
   	 		public void onClick(View v) {
   	 			FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
				fragmentManager.beginTransaction()
				.replace(R.id.flContent, new  LoginFacebookFragment())
				.commit();
   	 		}
   	 	});
   	 	
   	 	//Setup Dimme Knapp
   	 	ImageView imgChangeDim = (ImageView) V.findViewById(R.id.newDimImg);
   	 	imgChangeDim.setOnClickListener(new View.OnClickListener() {
   	 		public void onClick(View v) {
   	 			FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
				fragmentManager.beginTransaction()
				.replace(R.id.flContent, new  DimFragment())
				.commit();
   	 		}
   	 	});
   	 	
   	 	//Setter opp lik knappen
        ImageView img3 = (ImageView) V.findViewById(R.id.LikOssImg);
        img3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/forstegangstjeneste"));
            startActivity(intent);
            }
        });
        
        //Setter Opp Sanknappen
        ImageView img4 = (ImageView) V.findViewById(R.id.ABCDrillImg);
        img4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
 	
            	CopyReadAssets("saninfo.pdf");   	
            }
        });
        
        //Setter opp Grader KNappen
        ImageView img5 = (ImageView) V.findViewById(R.id.GraderForImg);
        img5.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
 	
            	CopyReadAssets("graderfor.pdf");  	           	
            }
        });
   	 	
        return V;
      }

	public boolean Login()
	{
		String logg = PreferenceManager.getDefaultSharedPreferences(getActivity().getBaseContext()).getString("facebookloggedin", "n");
		
		if(logg.equals("n"))
			return false;
		
		else
			return true;
	}
	
	public boolean getNoAds()
    {
		String logg = PreferenceManager.getDefaultSharedPreferences(getActivity().getBaseContext()).getString("noads", "n");
		
		if(logg.equals("n"))
			return false;
		
		else 
			return true;
    }
	
	public boolean checkInternetConnection() {
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isAvailable() && cm.getActiveNetworkInfo().isConnected()) {
            return true;

        } else {
            return false;
        }
	}
	
	//Facebook Publish Code
	//Sjekk for autoriasjon
		private boolean isSubsetOf(Collection<String> subset, Collection<String> superset) {
		    for (String string : subset) {
		        if (!superset.contains(string)) {
		            return false;
		        }
		    }
		    return true;
		}
		
		@Override
		public void onCreate(Bundle savedInstanceState) {
		    super.onCreate(savedInstanceState);
		    uiHelper = new UiLifecycleHelper(getActivity(), callback);
		    uiHelper.onCreate(savedInstanceState);
		}
		
		//Callback Funksjon til Facebook
		private Session.StatusCallback callback = new Session.StatusCallback() {
			    @Override
			    public void call(Session session, SessionState state, Exception exception) {
			        onSessionStateChange(session, state, exception);
			    }
		};
	
	//Sjekk om bruker er identifisert om vi kan poste to facebook eller ikke.
	private void onSessionStateChange(Session session, SessionState state, Exception exception) {
	    if (state.isOpened()) {
	        Log.i(TAG, "Logged in...");
	        logMan.saveLoginFaceBook(true);
	       
	    } else if (state.isClosed()) {
	        Log.i(TAG, "Logged out...");
	        logMan.saveLoginFaceBook(false);
	    }
	}
	
	@Override
	public void onResume() {
	    super.onResume();
	    uiHelper.onResume();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
	    super.onActivityResult(requestCode, resultCode, data);
	    uiHelper.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onPause() {
	    super.onPause();
	    uiHelper.onPause();
	}

	@Override
	public void onDestroy() {
	    super.onDestroy();
	    uiHelper.onDestroy();
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
	    super.onSaveInstanceState(outState);
	    uiHelper.onSaveInstanceState(outState);
	}

	//Publish code
	private void publishStory() {
	    Session session = Session.getActiveSession();

	    if (session != null){

	        // Check for publish permissions    
	        List<String> permissions = session.getPermissions();
	        if (!isSubsetOf(PERMISSIONS, permissions)) {
	        	
	        	Toast.makeText(getActivity()
	                    .getApplicationContext(), 
	                    "Etter tillatelsen er gitt, trykk DEL på nytt!",
	                    Toast.LENGTH_LONG).show();
	        	
	            pendingPublishReauthorization = true;
	            Session.NewPermissionsRequest newPermissionsRequest = new Session
	                    .NewPermissionsRequest(this, PERMISSIONS);
	        session.requestNewPublishPermissions(newPermissionsRequest);
	        
	            return;
	        }

	        Bundle postParams = new Bundle();
	        postParams.putString("name", "Førstegangstjeneste A-B-C");
	        postParams.putString("caption", "Skal du eller er du i Førstegangstjeneste?");
	        postParams.putString("description", "Jeg bruker Førstegangstjeneste A-B-C, der finner jeg svaret på det jeg trenger og vite for at min Førstegangstjeneste i Forsvaret skal bli bedre. Prøv du også!");
	        postParams.putString("link", "https://play.google.com/store/apps/details?id=com.suxsx.firstservice");
	        postParams.putString("picture", "http://imageshack.com/a/img809/106/r99f.png");

            
            Request.Callback callback= new Request.Callback() {
	            public void onCompleted(Response response) {
	                JSONObject graphResponse = response.getGraphObject().getInnerJSONObject();
	                String postId = null;
	                try {
	                    postId = graphResponse.getString("id");
	                } catch (JSONException e) {
	                    Log.i(TAG,
	                        "JSON error "+ e.getMessage());
	                }
	                FacebookRequestError error = response.getError();
	                if (error != null) {
	                    Toast.makeText(getActivity()
	                         .getApplicationContext(),
	                         error.getErrorMessage(),
	                         Toast.LENGTH_SHORT).show();
	                    } else {
	                        Toast.makeText(getActivity()
	                             .getApplicationContext(), 
	                             "Publisert til Facebook!\nTusen takk for at du hjelper oss!",
	                             Toast.LENGTH_LONG).show();
	                }
	            }
	        };
	        
	        Request request = new Request(session, "me/feed", postParams, HttpMethod.POST, callback);

	        RequestAsyncTask task = new RequestAsyncTask(request);
	        task.execute();
	    }
	}
	
	private void CopyReadAssets(String pdfFile)
    {
        AssetManager assetManager = getActivity().getAssets();

        InputStream in = null;
        OutputStream out = null;
        File file = new File(getActivity().getFilesDir(), pdfFile);
        try
        {
            in = assetManager.open(pdfFile);
            out = getActivity().openFileOutput(file.getName(), Context.MODE_WORLD_READABLE);

            copyFile(in, out);
            in.close();
            in = null;
            out.flush();
            out.close();
            out = null;
        } catch (Exception e)
        {
            Log.e("tag", e.getMessage());
        }

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(
                Uri.parse("file://" + getActivity().getFilesDir() + "/" + pdfFile),
                "application/pdf");

        startActivity(intent);
    }

    private void copyFile(InputStream in, OutputStream out) throws IOException
    {
        byte[] buffer = new byte[1024];
        int read;
        while ((read = in.read(buffer)) != -1)
        {
            out.write(buffer, 0, read);
        }
    }
}
