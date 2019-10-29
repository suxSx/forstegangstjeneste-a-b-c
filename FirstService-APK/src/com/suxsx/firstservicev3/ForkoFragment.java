package com.suxsx.firstservicev3;

import java.util.ArrayList;
import java.util.HashMap;

import com.suxsx.firstservice.R;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
//import android.app.DialogFragment;
import android.support.v4.app.DialogFragment;
//import android.app.Fragment;
import android.support.v4.app.Fragment;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class ForkoFragment extends Fragment {
	
	//Dialog View
	private showresultDialogFragment resultD = new showresultDialogFragment();
	private String resultof;
	
	// List view
    private ListView lv;
     
    // Listview Adapter
    ArrayAdapter<CharSequence> adapter;
     
    // Search EditText
    EditText inputSearch;
     
     
    // ArrayList for Listview
    ArrayList<HashMap<String, String>> productList;
    
    public void popUp(String Message)
	{
		Context context = getActivity().getBaseContext();
    	CharSequence text = Message;
    	int duration = Toast.LENGTH_SHORT;

    	Toast toast = Toast.makeText(context, text, duration);
    	toast.show();
	}
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState) {
        // Inflate the layout for this fragment
		View V =  inflater.inflate(R.layout.forko2_layout, container, false);
         
        lv = (ListView) V.findViewById(R.id.list_view);
        inputSearch = (EditText) V.findViewById(R.id.inputSearch);
         
        adapter = ArrayAdapter.createFromResource(getActivity().getBaseContext(), R.array.forko_array, android.R.layout.simple_spinner_item);
        lv.setAdapter(adapter);
        
        
        inputSearch.addTextChangedListener(new TextWatcher() {
            
            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // When user changed the Text
            	adapter.getFilter().filter(cs);  
            }
             
            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                    int arg3) {
                // TODO Auto-generated method stub
                 
            }
             
            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub                         
            }
        });
        
        lv.setClickable(true);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

          @Override
          public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

            Object o = lv.getItemAtPosition(position);
            resultof = o.toString();
            resultD.show(getFragmentManager(), "myDFS");
          }
        });
		
		return V;
    }
	
	@SuppressLint("ValidFragment")
	public class showresultDialogFragment extends DialogFragment {
	    @Override
	    public Dialog onCreateDialog(Bundle savedInstanceState) {
	        // Use the Builder class for convenient dialog construction
	        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	        builder.setTitle(R.string.result)
	               .setMessage(resultof)
	               .setNeutralButton(R.string.copy, new DialogInterface.OnClickListener() {
	                   public void onClick(DialogInterface dialog, int id) {
	                       // User Wish to Copy
	                	   ClipData clip = ClipData.newPlainText("Forkortelse", resultof);
	                	   ClipboardManager manager = (ClipboardManager) getActivity().getBaseContext().getSystemService(getActivity().getBaseContext().CLIPBOARD_SERVICE);
	                	   manager.setPrimaryClip(clip);
	                	   
	                	   popUp("Kopiert til utklips boken din");
	                   }
	               })
	               .setNegativeButton(R.string.ok, new DialogInterface.OnClickListener() {
	                   public void onClick(DialogInterface dialog, int id) {
	                       // User cancelled the dialog
	                   }
	               });
	        // Create the AlertDialog object and return it
	        return builder.create();
	    }
	}

}
