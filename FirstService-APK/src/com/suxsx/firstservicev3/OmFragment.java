package com.suxsx.firstservicev3;

import com.suxsx.firstservice.R;


//import android.app.Fragment;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class OmFragment extends Fragment {
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState) {
        // Inflate the layout for this fragment
		final View V = inflater.inflate(R.layout.om_layout, container, false);
		
        return V;
    }

}

