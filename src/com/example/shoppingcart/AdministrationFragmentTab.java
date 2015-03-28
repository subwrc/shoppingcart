package com.example.shoppingcart;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class AdministrationFragmentTab extends Fragment {

	public static final String MyPREFERENCES = "MyPrefs" ;
	//initially username and pass
	public static final String firstname = "admin";
	public static final String firstpass = "AdminAdmin";
	SharedPreferences sharedpreferences;
	   
	 EditText textUsername;
	 EditText textPassword;
	 Button loginbutton;
	 Button logoutbutton;
	 Button changepassbutton;
	 Button updatesbutton;
	 
	 String logged ="loggedin";
	 String isfirstuse="firstuse";
	 String username="username";
	 String password="password";
	 LinearLayout adminpanel;
	 LinearLayout userform;
     
	 @Override
	    public View onCreateView(LayoutInflater inflater, ViewGroup container,
	            Bundle savedInstanceState) {
	        View rootView = inflater.inflate(R.layout.administration_layout, container, false);
	       
	        textUsername = (EditText) rootView.findViewById(R.id.txtUsername);
	        textPassword = (EditText) rootView.findViewById(R.id.txtPassword);
	        loginbutton = (Button) rootView.findViewById(R.id.btnLogin);
	        logoutbutton = (Button) rootView.findViewById(R.id.btnLogout);
	        adminpanel = (LinearLayout) rootView.findViewById(R.id.adminpanel);
	        userform = (LinearLayout) rootView.findViewById(R.id.userform);
	        
	        sharedpreferences=getActivity().getSharedPreferences(MyPREFERENCES,Context.MODE_PRIVATE);
	        
	        if (!sharedpreferences.contains(isfirstuse))
	        	{ // then it's the first run of the app, so declare the initially username and pass
	        	Log.d("sharedpreferences", "firsttime");
	        	Editor editor = sharedpreferences.edit();
	        	editor.putString(username, firstname);
	        	editor.putString(password, firstpass);
	        	editor.putBoolean(isfirstuse, false);
	        	editor.commit();	        	    	 
	        	}
	        if (sharedpreferences.getBoolean(logged, false)) {
	        	showpanel(true);	        	
	        }
	        
	        loginbutton.setOnClickListener(new OnClickListener()
			  {
			    @Override
			    public void onClick(View v)
			    {
			    	login();
			    } 
			}); 	
	        
	        logoutbutton.setOnClickListener(new OnClickListener()
			  {
			    @Override
			    public void onClick(View v)
			    {
			    	logout();
			    } 
			}); 	        
	   	 Button changepassbutton = (Button) rootView.findViewById(R.id.button_changepass);
	   	 changepassbutton.setOnClickListener(new OnClickListener()
		   {
		             @Override
		             public void onClick(View v)
		             {	
		            	// get prompts.xml view
		 				LayoutInflater li = LayoutInflater.from(getActivity());
		 				View promptsView = li.inflate(R.layout.promptpass, null);
		            	AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
		            	// set prompts.xml to alertdialog builder
		            	alert.setView(promptsView);
		            	final EditText editusername = (EditText) promptsView.findViewById(R.id.editTextDialogUsername);
			 			final EditText editpassword = (EditText) promptsView.findViewById(R.id.editTextDialogPassword);		 				
		 				
		            	 alert.setTitle("Change Credentials");
		            //	 alert.setMessage("Are you sure?");
		            	 
		            	 alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
		            	 public void onClick(DialogInterface dialog, int whichButton) {
		            		 if ((editusername.getText().toString().length())>3&&(editpassword.getText().toString().length())>3) {
								Editor editor = sharedpreferences.edit();
					        	editor.putString(username, editusername.getText().toString());
					        	editor.putString(password, editpassword.getText().toString());					        	
					        	editor.commit();	        	    
					        	Toast.makeText(getActivity(), "Username and Password changed!", Toast.LENGTH_LONG).show();
		            		 }
		            		 else
		            			 Toast.makeText(getActivity(), "Username and Password must be at least 4 characters!", Toast.LENGTH_LONG).show(); 
		            	   }
		            	 });
		            	 alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
		            	   public void onClick(DialogInterface dialog, int whichButton) {
		            	     // Canceled.
		            		   dialog.cancel();
		            	   }
		            	 });
		            	 alert.show();	 
		             } 
		   });   	      
	   	 
	   	Button updatesbutton = (Button) rootView.findViewById(R.id.button_updates);
	   		updatesbutton.setOnClickListener(new OnClickListener()
	   		{
		             @Override
		             public void onClick(View v)
		             {
		            	 Intent intent = new Intent(getActivity(), Updates.class);				            	    
		            	 startActivity(intent);
		             } 
		   }); 	
		   
	        return rootView;
	    }
	 
	 public void login(){		
	      String usertext = textUsername.getText().toString();
	      String passtext = textPassword.getText().toString();
	      
	      String saveduser =  sharedpreferences.getString(username, "");
	      String savedpass =  sharedpreferences.getString(password, "");
	      //Log.d("login", "saveduser "+saveduser+" savedpass="+savedpass);
	      if (usertext.equals(saveduser))  {
	    	  if(passtext.equals(savedpass))  {
	    		  Editor editor = sharedpreferences.edit();
	    		  editor.putBoolean(logged, true);
	    		  editor.commit();
	    		  showpanel(true);
	    		  Toast.makeText(getActivity(), "Logged in succesfully!", Toast.LENGTH_LONG).show(); 
	    	  }
	    	  else
		    	  Toast.makeText(getActivity(), "Login failed!", Toast.LENGTH_LONG).show();
	      }
	      else
	    	  Toast.makeText(getActivity(), "Login failed!", Toast.LENGTH_LONG).show();     
	   }
	
	 
	 public void logout(){
		Editor editor = sharedpreferences.edit();
		editor.putBoolean(logged, false);
		editor.commit();
		showpanel(false); 
	   
	   }
	
	 public void showpanel(Boolean show){
		 if (show){
			 userform.setVisibility(View.GONE);
			 loginbutton.setVisibility(View.GONE);
			 logoutbutton.setVisibility(View.VISIBLE);
			 adminpanel.setVisibility(View.VISIBLE);			 
		 	}
		 else {
			userform.setVisibility(View.VISIBLE);
			logoutbutton.setVisibility(View.GONE);
			adminpanel.setVisibility(View.INVISIBLE);
			loginbutton.setVisibility(View.VISIBLE); 
		 }			 
	 }
	 
}
		

