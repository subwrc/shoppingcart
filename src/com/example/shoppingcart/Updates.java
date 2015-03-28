package com.example.shoppingcart;

import android.support.v7.app.ActionBarActivity;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;


public class Updates extends ActionBarActivity {
	
	EditText textcategory;
	EditText textsupermarket;
		
	private Cursor categories;
	private Cursor products;
	private MyDatabase db;
	
	private Spinner spinner1;
	private Spinner spinner2;
    
	private String chosencategory;
	private String chosensupermarket;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_updates);
		
		textcategory = (EditText) findViewById(R.id.editText_cat);
		textsupermarket = (EditText) findViewById(R.id.editText_sup);
		
		spinner1 = (Spinner) findViewById(R.id.spinner_cat);
		spinner2 = (Spinner) findViewById(R.id.spinner_sup);
		
		db = new MyDatabase(this);			 		 
		
		setspinnercat ();	 
		setspinnersup ();	
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.updates, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_exit) {
			finish();
	           // Log.d(TAG,"exited");
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	//populate spinner of categories
	public void setspinnercat () {	
		categories = db.getCategories();
		final SimpleCursorAdapter  dataAdapter = new SimpleCursorAdapter(this, 
				android.R.layout.simple_list_item_1, 
				categories, 
				new String[] {"name"}, 
				new int[] {android.R.id.text1},0);
		// Drop down layout style - list view with radio button
		dataAdapter.setViewResource( R.layout.spinner_item);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);	 
        // attaching data adapter to spinner
        spinner1.setAdapter(dataAdapter);
        
        spinner1.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {         	
            	 
            		//Log.d("spinner1=", (String) spinner1.getSelectedItem().toString());
            		Cursor cursor = (Cursor) dataAdapter.getItem(position);
            		chosencategory = cursor.getString(cursor.getColumnIndex("name"));
            		Log.d("spinner1=", chosencategory);            	
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });
	}
	
	//populate spinner of products of chosen category
			public void setspinnersup () {
				products = db.getSupermarkets();
				final SimpleCursorAdapter  dataAdapter = new SimpleCursorAdapter(this, 
						android.R.layout.simple_list_item_1, 
						products, 
						new String[] {"name"}, 
						new int[] {android.R.id.text1},0);
				// Drop down layout style - list view with radio button
				dataAdapter.setViewResource( R.layout.spinner_item);
		        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);	 
		        // attaching data adapter to spinner
		        spinner2.setAdapter(dataAdapter);
		        
		        spinner2.setOnItemSelectedListener(new OnItemSelectedListener() {
		            @Override
		            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
		            	Cursor cursor = (Cursor) dataAdapter.getItem(position);
	            		chosensupermarket = cursor.getString(cursor.getColumnIndex("name"));	            	
		            }

		            @Override
		            public void onNothingSelected(AdapterView<?> parentView) {
		                // your code here
		            }

		        });
			}
			
			//called when rename buttons clicked
			public void rename (View view) {
				int returnedrows=0;
				switch(view.getId()) {
		        case R.id.button_rencat:
		        	String value = textcategory.getText().toString();
					if(value.length()>2) {
						try {
							returnedrows=db.updateTable("Categories", "name", chosencategory, value);
						}
						catch (android.database.sqlite.SQLiteConstraintException e) {
						    Log.e("updateTable", "SQLiteConstraintException:" + e.getMessage());
						    e.printStackTrace();						   
						}
						if (returnedrows>0) {
							Toast.makeText(this, "Category " + chosencategory +" renamed to "+value, Toast.LENGTH_LONG).show();
							setspinnercat ();  // for refreshing spinner with changed supermarket
						}
						else 
							Toast.makeText(this, "Category " +value +" already exists", Toast.LENGTH_LONG).show();	
					}
					else
						Toast.makeText(this, "Enter a name at least 3 characters long", Toast.LENGTH_LONG).show();
		          break;
		        case R.id.button_rensup:
		        	String value2 = textsupermarket.getText().toString();
					if(value2.length()>2) {
						try {
							returnedrows=db.updateTable("Supermarkets", "name", chosensupermarket, value2);							
							}
						catch (SQLiteConstraintException e) {
						    Log.e("updateTable", "SQLiteConstraintException:" + e.getMessage());
						    e.printStackTrace();
						    }
						if (returnedrows>0) {
							Toast.makeText(this, "Supermarket " + chosensupermarket +" renamed to "+value2, Toast.LENGTH_LONG).show();
							setspinnersup ();  // for refreshing spinner with changed supermarket
							}
						else
							Toast.makeText(this, "Supermarket " + value2 +" already exists", Toast.LENGTH_LONG).show();						
					}
					else
						Toast.makeText(this, "Enter a name at least 3 characters long", Toast.LENGTH_LONG).show();
		          break;
			}
		}
		
			//called when delete buttons clicked
			public void delete (View view) {
				switch(view.getId()) {
		        case R.id.button_delcat:
		        	db.deleteFromTable("Categories", "name", chosencategory);
					Toast.makeText(this, "Category " + chosencategory +" deleted", Toast.LENGTH_LONG).show();
					setspinnercat ();  // for refreshing spinner with changed supermarket
		        	break;
		        case R.id.button_delsup:
		        	db.deleteFromTable("Supermarkets", "name", chosensupermarket);
					Toast.makeText(this, "Supermarket " + chosensupermarket +" deleted", Toast.LENGTH_LONG).show();
					setspinnersup ();  // for refreshing spinner with changed supermarket
		        	break;
				}
			}
}
