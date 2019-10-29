package com.suxsx.firstservicev3;

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

import android.app.AlertDialog;
import android.app.Dialog;
//import android.app.DialogFragment;
//import android.app.Fragment;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class FonisktestFragment extends Fragment {
	
	static String popUpText = "";
	static String popUpTitle = "";
	String outMessage;
	final FoniskTestManager testManager = new FoniskTestManager();
	private connectDialogFragment connectD = new connectDialogFragment();
	private facebookLoginYesDialogFragment faceYesDia = new facebookLoginYesDialogFragment();
	
	//Values facebook
	private static final List<String> PERMISSIONS = Arrays.asList("publish_actions");
	private static final String PENDING_PUBLISH_KEY = "pendingPublishReauthorization";
	private boolean pendingPublishReauthorization = false;
		
	private static final String TAG = "FontestFragment";
	private UiLifecycleHelper uiHelper;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View V = inflater.inflate(R.layout.fonisk_test, container, false);
        
        GoogleAdManager adMan = new GoogleAdManager(V, getActivity());
        
        //Sjekker om du er logget inn face
        if(Login() == false)
        {
        	faceYesDia.show(getFragmentManager(), "myDFS");
        }
        
        final EditText svarText = (EditText) V.findViewById(R.id.svarForkoText);
        final TextView infoText = (TextView) V.findViewById(R.id.txtViewtest);
        
        if(testManager.isStarted() == false)
        { V.findViewById(R.id.svarForkoText).setVisibility(View.GONE); }
        
        final Button send = (Button) V.findViewById(R.id.knappForkoTest);
        send.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
            	
            	//Nå er den false for den er ikke started. Vi tar og starter den.
            	if(testManager.isStarted() == false)
                { 
            		testManager.nullStill();
            		//Vi må sette teksten for første spørsmål.
            		testManager.getNext();
            		svarText.setHint("Hva er: " + testManager.getCurrent(0) + "?");
            		
            		//Må forandre tekst på knappen også.
            		send.setText("Sjekk Svar");
            		
            		//Vi må gjøre ting skjult og ikke skjult.
            		V.findViewById(R.id.svarForkoText).setVisibility(View.VISIBLE);
            		V.findViewById(R.id.txtViewtest).setVisibility(View.GONE);
            		testManager.Started();
            	}
            	
            	//Nå har vi started den så vi må ha funksjonen for hver gang de gir et svar
            	else
            	{
            		//Current ID = 29 er vi da ferdig med testen.
            		
            		//Først må vi sjekke svaret fra istad.
            		String svar = svarText.getText().toString().toLowerCase().trim();
            		String rightSvar = testManager.getCurrent(1).toLowerCase().trim();
            		
            		//Må sjekke om svar ikke er tomt.
            		
            		if(svar.equals(""))
            		{
            			//ber de skrive inn et svar
            			//Setter tekst for popUp
            			popUpText = "Du må skrive inn et svar alternativ. Den kan ikke være tom.";
            			
            			//Setter tittel
            			popUpTitle = "Skriv et svar.";
            			
            			//Viser popUP
            			connectD.show(getFragmentManager(), "myDFS");
            		}
            		
            		else
            		{
            		
            		if(svar.equals(rightSvar))
            		{
            			//Svaret var rett
            			
            			//Setter tekst for popUp
            			popUpText = "Du hadde helt rett! Vi var ute etter: " + rightSvar; 
            			
            			//Setter tittel
            			popUpTitle = "Rett Svar";
            			
            			//Setter en riktig
            			testManager.right++;
            			
            			//Viser popUP
            			connectD.show(getFragmentManager(), "myDFS");
            		}
            		else
            		{
            			//Det var galt svar
            			
            			//Setter en feil.
            			testManager.enFeil();
            			
            			//Legger til navnet i wrong feil
            			testManager.setWrongName(rightSvar);
            			
            			//Setter tekst for popUp
            			popUpText = "Du hadde feil svar, vi var ute etter: " + rightSvar + ".\n\nIkke: " + svar;
            			
            			//Setter tittel
            			popUpTitle = "Feil Svar";
            			
            			//Viser popUP
            			connectD.show(getFragmentManager(), "myDFS");
            		}
            		
            		//Gjør klar for neste
            		
            		//Sjekker først om vi er ferdig
            		if(testManager.currentID() == 29)
            		{
            			//Vi er ferdig

            			//Viser Resultatet.            			
            			if(testManager.isFeil() == true)
            				infoText.setText("Gratulerer! Du klarte alle svara uten en eneste feil. Du må gjerne prøve deg en gang til.");
            			
            			else
            			{
            				//Det var feil her vi må derfor vis dem.
            				infoText.setText("Du klarte deg ganske bra, men du hadde følgene feil:\n\n" + testManager.getWrongName() + "\nmen du klarer det nok bedre neste gang.\n\nPrøv engang til så skal du se det går bedre.");
            			}
            			
                		//Publiserer til Facebook
                		if(checkInternetConnection() == true)
                    	{
                			if(Login() == true)
                			{
                				//Kode for publisering
                				outMessage = "Jeg fikk " + testManager.right + " rette. Av totalt " + testManager.total + ". Klarer du og slå meg? Ta testen selv i Førstegangstjeneste A-B-C sin app. Der vil du også finne mye bra og nyttig informasjon om det norske Forsvaret.";
                				publishStory();
                			}
                    	}
            			
            			//Nullstiller testManager
            			testManager.nullStill();
            			svarText.setText(null);
            			
            			//Setter ny Tekst
            			send.setText("Start Testen på Nytt");
            			
            			//Viser fram infoen og skjuler inn menyen.
            			V.findViewById(R.id.svarForkoText).setVisibility(View.GONE);
                		V.findViewById(R.id.txtViewtest).setVisibility(View.VISIBLE);
            		}
            		
            		else
            		{
            			//Gjør klar neste spørsmål.
            			testManager.getNext();
                		svarText.setHint("Hva er: " + testManager.getCurrent(0) + "?");
                		svarText.setText(null);
            		}
            		
            		}
            	}
            	 
            }
        });
        
        
        
        
        
        return V;
    }
	
	public static class connectDialogFragment extends DialogFragment {
	    @Override
	    public Dialog onCreateDialog(Bundle savedInstanceState) {
	        // Use the Builder class for convenient dialog construction
	        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	        builder.setMessage(popUpText)
	        	   .setTitle(popUpTitle)
	               .setNegativeButton(R.string.ok, new DialogInterface.OnClickListener() {
	                   public void onClick(DialogInterface dialog, int id) {
	                       // User cancelled the dialog
	                   }
	               });
	        // Create the AlertDialog object and return it
	        return builder.create();
	    }
	}
	
	public static class facebookLoginYesDialogFragment extends DialogFragment {
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
		                    "Etter tillatelsen er gitt, trykk SEND på nytt!",
		                    Toast.LENGTH_LONG).show();
		        	
		            pendingPublishReauthorization = true;
		            Session.NewPermissionsRequest newPermissionsRequest = new Session
		                    .NewPermissionsRequest(this, PERMISSIONS);
		        session.requestNewPublishPermissions(newPermissionsRequest);
		        
		            return;
		        }

		        Bundle postParams = new Bundle();
		        postParams.putString("name", "Førstegangstjeneste A-B-C");
		        postParams.putString("caption", "Hva kan du om det Fonetiske Alfabetet?");
		        postParams.putString("description", outMessage);
		        postParams.putString("link", "https://play.google.com/store/apps/details?id=com.suxsx.firstservice");
		        postParams.putString("picture", "http://imageshack.com/a/img545/7535/anjf.png");

	            
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
		                             "Publisert Resultatet til Facebook.",
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
}