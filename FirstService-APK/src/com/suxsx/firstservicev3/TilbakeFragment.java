package com.suxsx.firstservicev3;

//import android.app.Fragment;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.facebook.FacebookRequestError;
import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.RequestAsyncTask;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.android.Util;
import com.suxsx.firstservice.R;
import com.suxsx.firstservicev3.FonisktestFragment.connectDialogFragment;

public class TilbakeFragment extends Fragment {	
			
	//Tekst som skal bli publisert.
	private String outMessage;
	private String outText;
	
	//Values facebook
	private static final List<String> PERMISSIONS = Arrays.asList("publish_actions");
	private static final String PENDING_PUBLISH_KEY = "pendingPublishReauthorization";
	private boolean pendingPublishReauthorization = false;
	
	private connectDialogFragment connectD = new connectDialogFragment();
	
	private static final String TAG = "TilbakeFragment";
	private UiLifecycleHelper uiHelper;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View V = inflater.inflate(R.layout.tilbake_layout, container, false);
        
        //Ber brukeren logge inn på facebook hvis han ikke er det
        if(Login() == false)
        	connectD.show(getFragmentManager(), "myDFS");
        
        
        //Setter opp share knappen
        final Button send = (Button) V.findViewById(R.id.send);
        send.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
            	
            	if(checkInternetConnection() == true)
            	{
            	EditText editText = (EditText) V.findViewById(R.id.melding_textBox);
            	outMessage = editText.getText().toString();
            	
            	Spinner mySpinner = (Spinner) V.findViewById(R.id.spinner_hva);
            	outText = mySpinner.getSelectedItem().toString();
            	
            	if(outMessage.isEmpty() == false)
            	{
            		//Facebook ikke funke vi sende mail.
            		if(Login() == false)
            			sendMail(outMessage, outText);
            		
            		//Facebook funke vi share der
            		else
            		{
            			publishStory();
            		}   

            	}
            	
            	else
            		Toast.makeText(getActivity().getApplicationContext(), "Du må skrive et bidrag", Toast.LENGTH_LONG).show();
            	
            	}
            	
            	else
            	{
            		Toast.makeText(getActivity().getApplicationContext(), "Tjenesten krever internett, sjekk kobling.", Toast.LENGTH_LONG).show();
                	
            	}
            }
        });
         
        return V;
    }
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    uiHelper = new UiLifecycleHelper(getActivity(), callback);
	    uiHelper.onCreate(savedInstanceState);
	}
	
	
	//Dialog face
	public static class connectDialogFragment extends DialogFragment {
	    @Override
	    public Dialog onCreateDialog(Bundle savedInstanceState) {
	        // Use the Builder class for convenient dialog construction
	        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	        builder.setMessage("Du er ikke logget inn på Facebook. \n\nFor den beste opplevelsen, velg: 'Logg inn Facebook' i menyen.")
	        	   .setTitle("Hvorfor ikke logge inn?")
	               .setNegativeButton("Nei", new DialogInterface.OnClickListener() {
	                   public void onClick(DialogInterface dialog, int id) {
	                       // User cancelled the dialog
	                   }
	               })
	               .setPositiveButton("Ja", new DialogInterface.OnClickListener() {
	            	   public void onClick(DialogInterface dialog, int id) {
	            		   FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
	       					fragmentManager.beginTransaction()
	       					.replace(R.id.flContent, new  LoginFacebookFragment())
	       					.commit();
	                   }
	               });
	        // Create the AlertDialog object and return it
	        return builder.create();
	    }
	}
	
	
	//Funksjon for og sende mail
	public void sendMail(String message, String Text)
	{
    	if(message.isEmpty() == false)
    	{
    		String s="\n\n---------------------------\n\nDebug-infos:";
    		s += "\n OS Version: " + System.getProperty("os.version") + "(" + android.os.Build.VERSION.INCREMENTAL + ")";
    		s += "\n OS API Level: " + android.os.Build.VERSION.SDK;
    		s += "\n Device: " + android.os.Build.DEVICE;
    		s += "\n Model (and Product): " + android.os.Build.MODEL + " ("+ android.os.Build.PRODUCT + ")";
    		
    		message += s;
    		
    		
    			String uriText =
    			    "mailto:suxsx@forstegangstjeneste.com" + 
    			    "?subject=" + URLEncoder.encode(Text) + 
    			    "&body=" + URLEncoder.encode(message);

    			Uri uri = Uri.parse(uriText);

    			Intent sendIntent = new Intent(Intent.ACTION_SENDTO);
    			sendIntent.setData(uri);
    			startActivity(Intent.createChooser(sendIntent, "Send email")); 
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
	                    "Etter tillatelsen er gitt, trykk SEND på nytt!",
	                    Toast.LENGTH_LONG).show();
	        	
	            pendingPublishReauthorization = true;
	            Session.NewPermissionsRequest newPermissionsRequest = new Session
	                    .NewPermissionsRequest(this, PERMISSIONS);
	        session.requestNewPublishPermissions(newPermissionsRequest);
	        
	            return;
	        }
	        
	        //Wall post test
            Bundle postParams = new Bundle();
            postParams.putString("message", "Forslag gjelder: "  + outText + "\n\n" + outMessage);
            
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
	                             "Innlegget er sendt til Førstegangstjeneste A-B-C.",
	                             Toast.LENGTH_LONG).show();
	                }
	            }
	        };
	        
            
	        //"PAGE_ID/feed"
	        //Request request = new Request(session, "me/feed", postParams, 
	                              //HttpMethod.POST, callback);
	        // Request request = new Request(session, "587559311299631/feed", postParams, 
            
	        
	        Request request = new Request(session, "587559311299631/feed", postParams, HttpMethod.POST, callback);

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
}
