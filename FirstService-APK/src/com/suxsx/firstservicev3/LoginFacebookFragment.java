package com.suxsx.firstservicev3;

import java.util.Arrays;
import java.util.List;

import android.support.v4.app.Fragment;
import android.content.ContextWrapper;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.widget.LoginButton;
import com.suxsx.firstservice.R;

public class LoginFacebookFragment extends Fragment {
	
	private static final String TAG = "LoginFragment";
	private UiLifecycleHelper uiHelper;
	private static final List<String> PERMISSIONS = Arrays.asList("publish_actions", "user_likes", "user_status");

	public View onCreateView(LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View V = inflater.inflate(R.layout.login_facebook, container, false);
   
        //Setter opp session hjelper til facebook
        super.onCreate(savedInstanceState);
        uiHelper = new UiLifecycleHelper(getActivity(), callback);
        uiHelper.onCreate(savedInstanceState);
        
        //Setter opp knappen til Facebook Loggin
        LoginButton authButton = (LoginButton) V.findViewById(R.id.authButtonLogin);
        authButton.setFragment(this);
        
        
        return V;
    }
	
	//Facebook Code for status på innlogging
	private void onSessionStateChange(Session session, SessionState state, Exception exception) {
	    if (state.isOpened()) {
	        Log.i(TAG, "Logged in...");
	        saveLogin(true);
	    } else if (state.isClosed()) {
	        Log.i(TAG, "Logged out...");
	        saveLogin(false);
	    }
	}
	
	//Callback Funksjon til Facebook
	private Session.StatusCallback callback = new Session.StatusCallback() {
	    @Override
	    public void call(Session session, SessionState state, Exception exception) {
	        onSessionStateChange(session, state, exception);
	    }
	};
	
	//Events ved logging Facebook
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
}
