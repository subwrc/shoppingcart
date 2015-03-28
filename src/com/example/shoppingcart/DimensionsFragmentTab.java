package com.example.shoppingcart;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Fragment;

public class DimensionsFragmentTab extends Fragment {
	 @Override
	    public View onCreateView(LayoutInflater inflater, ViewGroup container,
	            Bundle savedInstanceState) {
	        View rootView = inflater.inflate(R.layout.dimensions_layout, container, false);
	      
	        final TextView textWidth = (TextView) rootView.findViewById(R.id.textWidth);
	        final TextView textHeight = (TextView) rootView.findViewById(R.id.textHeight);
	        final TextView textorientation = (TextView) rootView.findViewById(R.id.textOrientation);
	      
	        final int orientation = getResources().getConfiguration().orientation;	               
	        
	        Button button_screensize = (Button) rootView.findViewById(R.id.button_screensize);
	        button_screensize.setOnClickListener(new OnClickListener()
			   {
			             @Override
			             public void onClick(View v)
			             {			            	  
			     	        textWidth.setText("Width="+String.valueOf(getResources().getDisplayMetrics().widthPixels)+ " pixels");
			     	        textHeight.setText("Height="+String.valueOf(getResources().getDisplayMetrics().heightPixels)+ " pixels");
			     	        
			            	 switch (orientation) {	           
			 	            case 1: 
			 	            	textorientation.setText("Orientation="+String.valueOf(orientation) + " portrait");	
			 	                Toast.makeText(getActivity(), "portrait", Toast.LENGTH_SHORT).show();
			 	                break;
			 	            case 2:
			 	            	textorientation.setText("Orientation="+String.valueOf(orientation) + " landscape");	
			 	                Toast.makeText(getActivity(), "landscape", Toast.LENGTH_SHORT).show();
			 	                break;	            
			 	            default:
			 	                Toast.makeText(getActivity(), "Oops, rotation code:" + orientation, Toast.LENGTH_SHORT).show();
			 	                break;
			            	 }	        
			             } 
			   });
	        return rootView;
	    }
	}
