package com.suxsx.firstservicev3;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.suxsx.firstservice.R;

public class TegnsigFragment extends Fragment {
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View V = inflater.inflate(R.layout.tegnsign_layout, container, false);
      
        GoogleAdManager adMan = new GoogleAdManager(V, getActivity());
        
        return V;
    }

}

