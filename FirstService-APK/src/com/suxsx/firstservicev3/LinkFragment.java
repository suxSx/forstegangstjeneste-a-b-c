package com.suxsx.firstservicev3;


import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
//import android.app.Fragment;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.suxsx.firstservice.R;

public class LinkFragment extends Fragment {
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View V = inflater.inflate(R.layout.link_layout, container, false);
        
        GoogleAdManager adMan = new GoogleAdManager(V, getActivity());
        
        
        //Linkene
        ImageView img = (ImageView) V.findViewById(R.id.tmo_link_knapp);
        img.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.soldaten.no"));
            startActivity(intent);
        }
    });
        
        ImageView img2 = (ImageView) V.findViewById(R.id.forsvaret_link_knapp);
        img2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.forsvaret.no"));
            startActivity(intent);
        }
    });
        
        ImageView img3 = (ImageView) V.findViewById(R.id.disp_link_knapp);
        img3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://lovdata.no/dokument/NL/lov/1988-05-20-32"));
            startActivity(intent);
        }
    });
        
        ImageView img4 = (ImageView) V.findViewById(R.id.sanhefte_link_knapp);
        img4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
 	
            	CopyReadAssets("saninfo.pdf");
            	
            	
        }
    });
        
        ImageView img5 = (ImageView) V.findViewById(R.id.grader_link_knapp);
        img5.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
 	
            	CopyReadAssets("graderfor.pdf");
            	
            	
        }
    });
        
        ImageView img6 = (ImageView) V.findViewById(R.id.face_link_knapp);
        img6.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/forstegangstjeneste"));
            startActivity(intent);
        }
    });
       
        
        
        return V;
    }
	
	private void CopyReadAssets(String pdfFile)
    {
        AssetManager assetManager = getActivity().getAssets();

        InputStream in = null;
        OutputStream out = null;
        File file = new File(getActivity().getFilesDir(), pdfFile);
        try
        {
            in = assetManager.open(pdfFile);
            out = getActivity().openFileOutput(file.getName(), Context.MODE_WORLD_READABLE);

            copyFile(in, out);
            in.close();
            in = null;
            out.flush();
            out.close();
            out = null;
        } catch (Exception e)
        {
            Log.e("tag", e.getMessage());
        }

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(
                Uri.parse("file://" + getActivity().getFilesDir() + "/" + pdfFile),
                "application/pdf");

        startActivity(intent);
    }

    private void copyFile(InputStream in, OutputStream out) throws IOException
    {
        byte[] buffer = new byte[1024];
        int read;
        while ((read = in.read(buffer)) != -1)
        {
            out.write(buffer, 0, read);
        }
    }

}