package com.suxsx.firstservicev3;

//import android.app.Fragment;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.FacebookRequestError;
import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.RequestAsyncTask;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.suxsx.firstservice.R;

public class DimFragment extends Fragment {
	
	//Values facebook
		private static final List<String> PERMISSIONS = Arrays.asList("publish_actions");
		private static final String PENDING_PUBLISH_KEY = "pendingPublishReauthorization";
		private boolean pendingPublishReauthorization = false;
		private showDialogClass showdialog = new showDialogClass();
		private LoginManagerShared logMan;
			
		private static final String TAG = "DimFragment";
		private UiLifecycleHelper uiHelper;
		String outMessage;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState) {
        // Inflate the layout for this fragment
		final View V = inflater.inflate(R.layout.dim_layout, container, false);
		
		GoogleAdManager adMan = new GoogleAdManager(V, getActivity());
		
		//Setter opp logman
        logMan = new LoginManagerShared(getActivity());
        
        DimmeCalc exCalcu = new DimmeCalc();
		DatePicker datePicker = (DatePicker) V.findViewById(R.id.datePicker1);
		datePicker.setMinDate(System.currentTimeMillis() - 1000);
		
		String DIM = PreferenceManager.getDefaultSharedPreferences(getActivity().getBaseContext()).getString("dimmedag", "nodim");
		int diffInDaysD = 0;
		
		if(DIM == "nodim")
		{
			V.findViewById(R.id.layerDIM).setVisibility(View.GONE);
		}
		
		else
		{
			V.findViewById(R.id.layerDIM).setVisibility(View.VISIBLE);
			diffInDaysD = exCalcu.getDiff(DIM);
		}
		
		
		final Button send = (Button) V.findViewById(R.id.save_dim);
        send.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
            	
            	 DatePicker datePicker = (DatePicker) V.findViewById(R.id.datePicker1);
            	 int day = datePicker.getDayOfMonth();
            	 int month = datePicker.getMonth() + 1;
            	 int year = datePicker.getYear();
            	 
            	 String dateFuture = day + "/" + month + "/" + year;
            	 
            	 DimmeCalc exCalcu = new DimmeCalc();
            	 int diffInDays = exCalcu.getDiff(dateFuture);
            	 
            	 PreferenceManager.getDefaultSharedPreferences(getActivity().getBaseContext()).edit().putString("dimmedag", dateFuture).commit();
            	 
            	 TextView first = (TextView) getActivity().findViewById(R.id.days_to__dim);
            	 first.setText(String.valueOf(diffInDays));
            	 getActivity().findViewById(R.id.layerDIM).setVisibility(View.VISIBLE);
            	 
            	 Intent intent = new Intent(getActivity(), DimmeWidgetProvider.class);
            	 	intent.setAction("android.appwidget.action.APPWIDGET_UPDATE");
            	 	// Use an array and EXTRA_APPWIDGET_IDS instead of AppWidgetManager.EXTRA_APPWIDGET_ID,
            	 	// since it seems the onUpdate() is only fired on that:
            		int[] ids = AppWidgetManager.getInstance(getActivity()).getAppWidgetIds(new ComponentName(getActivity(), DimmeWidgetProvider.class));
            		intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS,ids);
            		
            		getActivity().sendBroadcast(intent);
            	 
            	//Publiserer til Facebook
         		if(checkInternetConnection() == true)
             	{
         			if(Login() == true)
         			{
         				//Publisere på face?
         				outMessage = "Det er nå kun: " + diffInDays + " dager igjen til dim. Prøv dimmekalkulatoren du å! Du finner den på Førstegangstjeneste A-B-C.";
         				showdialog.show(getFragmentManager(), "myDFS");
         			}
             	}
            	 
            }
        });
        
       
		TextView firstD = (TextView) V.findViewById(R.id.days_to__dim);
   	 	firstD.setText(String.valueOf(diffInDaysD));
   	 	
   	 	
   	 	Intent intent = new Intent(getActivity(), DimmeWidgetProvider.class);
   	 	intent.setAction("android.appwidget.action.APPWIDGET_UPDATE");
   	 	// Use an array and EXTRA_APPWIDGET_IDS instead of AppWidgetManager.EXTRA_APPWIDGET_ID,
   	 	// since it seems the onUpdate() is only fired on that:
   		int[] ids = AppWidgetManager.getInstance(getActivity()).getAppWidgetIds(new ComponentName(getActivity(), DimmeWidgetProvider.class));
   		intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS,ids);
   		
   		getActivity().sendBroadcast(intent);
   	 	
        return V;
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
	        saveLogin(true);
	       
	    } else if (state.isClosed()) {
	        Log.i(TAG, "Logged out...");
	        saveLogin(false);
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
	                    "Etter tillatelsen er gitt, trykk kalkuler på nytt!",
	                    Toast.LENGTH_LONG).show();
	        	
	            pendingPublishReauthorization = true;
	            Session.NewPermissionsRequest newPermissionsRequest = new Session
	                    .NewPermissionsRequest(this, PERMISSIONS);
	        session.requestNewPublishPermissions(newPermissionsRequest);
	        
	            return;
	        }

	        Bundle postParams = new Bundle();
	        postParams.putString("name", "Førstegangstjeneste A-B-C");
	        postParams.putString("caption", "Ikke lenge igjen til dim nå!");
	        postParams.putString("description", outMessage);
	        postParams.putString("link", "https://play.google.com/store/apps/details?id=com.suxsx.firstservice");
	        postParams.putString("picture", "http://imageshack.com/a/img28/5334/j7z9.png");

            
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
	                             "Dimme resultatet er nå publisert!",
	                             Toast.LENGTH_LONG).show();
	                }
	            }
	        };
	        
	        Request request = new Request(session, "me/feed", postParams, HttpMethod.POST, callback);

	        RequestAsyncTask task = new RequestAsyncTask(request);
	        task.execute();
	    }
	}
	
	public void saveLogin(boolean is)
	{	
		if(is == true)
			PreferenceManager.getDefaultSharedPreferences(getActivity().getBaseContext()).edit().putString("facebookloggedin", "y").commit();
		
		if(is == false)
			PreferenceManager.getDefaultSharedPreferences(getActivity().getBaseContext()).edit().putString("facebookloggedin", "n").commit();
	}
	
	public boolean Login()
	{
		String logg = PreferenceManager.getDefaultSharedPreferences(getActivity().getBaseContext()).getString("facebookloggedin", "n");
		
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
	
	@SuppressLint("ValidFragment")
	private class showDialogClass extends DialogFragment {
	    @Override
	    public Dialog onCreateDialog(Bundle savedInstanceState) {
	        // Use the Builder class for convenient dialog construction
	        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	        builder.setTitle("Publisere til Facebook")
	               .setMessage("Skal vi publisere dimmedagene på Facebook?")
	               .setNegativeButton("Nei", new DialogInterface.OnClickListener() {
	                   public void onClick(DialogInterface dialog, int id) {
	                       // User cancelled the dialog
	                   }
	               })
	               .setNeutralButton("Ja", new DialogInterface.OnClickListener() {
	                   public void onClick(DialogInterface dialog, int id) {
	                	   
	                	   publishStory();
	                	   
	                   }
	               });
	        // Create the AlertDialog object and return it
	        return builder.create();
	    }
	}
}