package com.suxsx.firstservicev3;

import com.suxsx.firstservice.R;

//import android.app.Fragment;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;
import android.view.View.OnClickListener;

public class KulebaneFragment extends Fragment {

	public View onCreateView(LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View V = inflater.inflate(R.layout.hk_layout, container, false);
        
       
        RadioButton myOption1 =  (RadioButton) V.findViewById(R.id.radio0);
        RadioButton myOption2 =  (RadioButton) V.findViewById(R.id.radio1);

        

        myOption1.setChecked(true);
        
        myOption1.setOnClickListener(first_radio_listener);
        myOption2.setOnClickListener(secound_radio_listener);
        
        GoogleAdManager adMan = new GoogleAdManager(V, getActivity());
        
        return V;
    }
	
	OnClickListener first_radio_listener = new OnClickListener (){
		 public void onClick(View v) {
			
			 RadioButton b2 = (RadioButton) getActivity().findViewById(R.id.radio1);
			 b2.setChecked(false);
			 
			 RadioButton b1 = (RadioButton) getActivity().findViewById(R.id.radio0);
			 b1.setChecked(true);
			 
			 TextView first = (TextView) getActivity().findViewById(R.id.first_TV);
			 TextView secound = (TextView) getActivity().findViewById(R.id.topp_TV);
			 TextView third = (TextView) getActivity().findViewById(R.id.last_TV);
			 
			 first.setText(R.string.hk_7);
			 secound.setText(R.string.hk_8);
			 third.setText(R.string.hk_9);
		 }
	};
	
	OnClickListener secound_radio_listener = new OnClickListener (){
		 public void onClick(View v) {
		   
			 RadioButton b2 = (RadioButton) getActivity().findViewById(R.id.radio0);
			 b2.setChecked(false);
			 
			 RadioButton b1 = (RadioButton) getActivity().findViewById(R.id.radio1);
			 b1.setChecked(true);
			 
			 TextView first = (TextView) getActivity().findViewById(R.id.first_TV);
			 TextView secound = (TextView) getActivity().findViewById(R.id.topp_TV);
			 TextView third = (TextView) getActivity().findViewById(R.id.last_TV);
			 
			 first.setText(R.string.hk_10);
			 secound.setText(R.string.hk_11);
			 third.setText(R.string.hk_12);
		 }
	};
}
