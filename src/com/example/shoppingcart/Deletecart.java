package com.example.shoppingcart;

import android.support.v7.app.ActionBarActivity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Deletecart extends ActionBarActivity {
	
	private MyDatabase db;
	EditText deletetext;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_deletecart);
		
		 db = new MyDatabase(this);
		 deletetext = (EditText) findViewById(R.id.editText_product);
		 
		 Button button_emptycart = (Button) findViewById(R.id.button_emptycart);
		 button_emptycart.setOnClickListener(new OnClickListener()
		   {
		             @Override
		             public void onClick(View v)
		             {
		            	 AlertDialog.Builder alert = new AlertDialog.Builder(Deletecart.this);
		            	 alert.setTitle("Delete Cart");
		            	 alert.setMessage("Are you sure?");
		            	 alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
		            	 public void onClick(DialogInterface dialog, int whichButton) {
		            		 int deleted;
		            		 deleted=db.deletecart();
		            		Toast.makeText(Deletecart.this, "Deleted " +deleted + " records from Cart table.", Toast.LENGTH_LONG).show();
		            	   }
		            	 });
		            	 alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
		            	   public void onClick(DialogInterface dialog, int whichButton) {
		            	     // Canceled.
		            	   }
		            	 });
		            	 alert.show();	 
		             } 
		   }); 	
		 
		 Button button_deleteproduct = (Button) findViewById(R.id.button_deleteproduct);
		 button_deleteproduct.setOnClickListener(new OnClickListener()
		 	{
		             @Override
		             public void onClick(View v)
		             {
		            	 int deleted=0;
		            	 String value =deletetext.getText().toString();
		            	 if (value.length()>0) {
		            		deleted=db.deleteFromTable("Cart", "name", value);
		            		if (deleted>0)
		            			Toast.makeText(Deletecart.this, "Deleted " +deleted + " records from Cart table.", Toast.LENGTH_LONG).show();
		            		else
		            			Toast.makeText(Deletecart.this, "Product not found in Cart table.", Toast.LENGTH_LONG).show();
		            	 	}
		            	 else
		            		 Toast.makeText(Deletecart.this, "Please enter a product", Toast.LENGTH_LONG).show(); 
		             } 
		 	}); 	
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id ==R.id.action_exit) {
            finish();
           // Log.d(TAG,"exited");
            return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
