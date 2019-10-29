package com.suxsx.firstservicev3;
import com.suxsx.firstservice.R;


//import android.app.Fragment;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class KneppeteltFragment extends Fragment {
	
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View V = inflater.inflate(R.layout.kneppe_layout, container, false);
        
        GoogleAdManager adMan = new GoogleAdManager(V, getActivity());
        
        return V;
    }
	
	private void unbindDrawables(View view) {
        if (view.getBackground() != null) {
        view.getBackground().setCallback(null);
        }
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
            unbindDrawables(((ViewGroup) view).getChildAt(i));
            }
        ((ViewGroup) view).removeAllViews();
        }
    }



//call this method from onDestroy()
    public void onDestroy() {
    super.onDestroy();
        unbindDrawables(getActivity().findViewById(R.id.layerContent));
        System.gc();
      }

}
