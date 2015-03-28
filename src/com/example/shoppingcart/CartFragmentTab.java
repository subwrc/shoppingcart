package com.example.shoppingcart;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.app.Fragment;
import android.content.Intent;

public class CartFragmentTab extends Fragment {
   
	 @Override
	    public View onCreateView(LayoutInflater inflater, ViewGroup container,
	            Bundle savedInstanceState) {
	        View rootView = inflater.inflate(R.layout.cart_layout, container, false);
	
			Button button = (Button) rootView.findViewById(R.id.button_addtocart);
				   button.setOnClickListener(new OnClickListener()
				   {
				             @Override
				             public void onClick(View v)
				             {
				            	 Intent intent = new Intent(getActivity(), AddToCartActivity.class);				            	    
				            	 startActivity(intent);
				             } 
				   }); 
				   
				   Button button_showcart = (Button) rootView.findViewById(R.id.button_showcart);
				   button_showcart.setOnClickListener(new OnClickListener()
				   {
				             @Override
				             public void onClick(View v)
				             {
				            	 Intent intent = new Intent(getActivity(), Showcart.class);				            	    
				            	 startActivity(intent);
				             } 
				   }); 			
				   
				   Button button_deletecart = (Button) rootView.findViewById(R.id.button_deletecart);
				   button_deletecart.setOnClickListener(new OnClickListener()
				   {
				             @Override
				             public void onClick(View v)
				             {
				            	 Intent intent = new Intent(getActivity(), Deletecart.class);				            	    
				            	 startActivity(intent);
				             } 
				   }); 			
	        return rootView;
	    }
}
