package com.suxsx.firstservicev3;

//import android.app.Fragment;
import android.support.v4.app.Fragment;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.suxsx.firstservice.R;

public class SengeFragment extends Fragment {
	
	
	Bitmap h1, h2, h3, h4, h5;
	Resources res;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View V = inflater.inflate(R.layout.senge_layout, container, false);
        res = getResources();
        
        GoogleAdManager adMan = new GoogleAdManager(V, getActivity());
        
       
        //Load av bilder seng 1
        h1 = decodeFile(R.drawable.sengbilde_1);
        ImageView im = (ImageView) V.findViewById(R.id.iseng1);
        im.setImageBitmap(h1);
        
        
        h1 = decodeFile(R.drawable.sengbilde_2);
        im = (ImageView) V.findViewById(R.id.iseng2);
        im.setImageBitmap(h1);
        
        h1 = decodeFile(R.drawable.sengbilde_3);
        im = (ImageView) V.findViewById(R.id.iseng3);
        im.setImageBitmap(h1);
        
        h1 = decodeFile(R.drawable.sengbilde_4);
        im = (ImageView) V.findViewById(R.id.iseng4);
        im.setImageBitmap(h1);
        
        h1 = decodeFile(R.drawable.sengbilde_5);
        im = (ImageView) V.findViewById(R.id.iseng5);
        im.setImageBitmap(h1);
        
        h1 = decodeFile(R.drawable.sengbilde_6);
        im = (ImageView) V.findViewById(R.id.iseng6);
        im.setImageBitmap(h1);
        
        h1 = decodeFile(R.drawable.sengbilde_7);
        im = (ImageView) V.findViewById(R.id.iseng7);
        im.setImageBitmap(h1);
        
        h1 = decodeFile(R.drawable.sengbilde_8);
        im = (ImageView) V.findViewById(R.id.iseng8);
        im.setImageBitmap(h1);
        
        h1 = decodeFile(R.drawable.sengbilde_9);
        im = (ImageView) V.findViewById(R.id.iseng9);
        im.setImageBitmap(h1);
        
        h1 = decodeFile(R.drawable.sengbilde_10);
        im = (ImageView) V.findViewById(R.id.iseng10);
        im.setImageBitmap(h1);
        
        h1 = decodeFile(R.drawable.sengbilde_11);
        im = (ImageView) V.findViewById(R.id.iseng11);
        im.setImageBitmap(h1);
        
        h1 = decodeFile(R.drawable.sengbilde_12);
        im = (ImageView) V.findViewById(R.id.iseng12);
        im.setImageBitmap(h1);
        
        h1 = decodeFile(R.drawable.sengbilde_13);
        im = (ImageView) V.findViewById(R.id.iseng13);
        im.setImageBitmap(h1);
        
        h1 = decodeFile(R.drawable.sengbilde_14);
        im = (ImageView) V.findViewById(R.id.iseng14);
        im.setImageBitmap(h1);
        
        h1 = decodeFile(R.drawable.sengbilde_15);
        im = (ImageView) V.findViewById(R.id.iseng15);
        im.setImageBitmap(h1);
        
        h1 = decodeFile(R.drawable.sengbilde_16);
        im = (ImageView) V.findViewById(R.id.iseng16);
        im.setImageBitmap(h1);
        
        h1 = decodeFile(R.drawable.sengbilde_17);
        im = (ImageView) V.findViewById(R.id.iseng17);
        im.setImageBitmap(h1);
        
        h1 = decodeFile(R.drawable.sengbilde_18);
        im = (ImageView) V.findViewById(R.id.iseng18);
        im.setImageBitmap(h1);
        
        h1 = decodeFile(R.drawable.sengbilde_19);
        im = (ImageView) V.findViewById(R.id.iseng19);
        im.setImageBitmap(h1);

        
        return V;
        
	}
	
	private Bitmap decodeFile(int id){
	    //Decode image size
		BitmapFactory.Options o = new BitmapFactory.Options();
		o.inJustDecodeBounds = true;
		BitmapFactory.decodeResource(res, id, o);
		//BitmapFactory.decodeStream(new FileInputStream(f),null,o);

		//The new size we want to scale to
		final int REQUIRED_SIZE=70;

		//Find the correct scale value. It should be the power of 2.
		int scale=1;
		while(o.outWidth/scale/2>=REQUIRED_SIZE && o.outHeight/scale/2>=REQUIRED_SIZE)
		    scale*=2;

		//Decode with inSampleSize
		BitmapFactory.Options o2 = new BitmapFactory.Options();
		o2.inSampleSize=scale;
		return BitmapFactory.decodeResource(res, id, o2);
	}
}
