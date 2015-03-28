package com.example.shoppingcart;

import android.support.v7.app.ActionBarActivity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Toast;
import android.widget.SimpleCursorAdapter;

public class Showcart extends ActionBarActivity {
	
	private Cursor products;
	private MyDatabase db;
	EditText searchname;
	RadioButton radio_showall;
	RadioButton radio_products;
	RadioButton radio_category;
	ListView lv;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_showcart);
		
		searchname = (EditText) findViewById(R.id.editsearch);
		radio_showall = (RadioButton) findViewById(R.id.radioButton1);
		radio_products = (RadioButton) findViewById(R.id.radioButton2);
		radio_category = (RadioButton) findViewById(R.id.radioButton3);
		
		db = new MyDatabase(this);	
		
		lv = (ListView) findViewById(android.R.id.list);
        //code to add header to listview
        LayoutInflater inflater = getLayoutInflater();
        ViewGroup header = (ViewGroup) inflater.inflate(R.layout.header, lv,false);						        
        lv.addHeaderView(header, null, false);
	
		Button button = (Button) findViewById(R.id.searchbutton);
			   button.setOnClickListener(new OnClickListener()
			   {			           
						@Override
						public void onClick(View v) {
							if (radio_showall.isChecked())  {
								products = db.getCart();
								if (products.getCount()>0){
									ListAdapter adapter = new SimpleCursorAdapter(Showcart.this, 
										R.layout.row_cart_4column, 
										products, 
										new String[] {"name","supermarket_name", "product_name", "price"}, 
										new int[] {R.id.textname,R.id.textsupermarket,R.id.textproduct,R.id.textprice},0);								
									lv.setAdapter(adapter); 
							//	getListView().setAdapter(adapter);
								}
								else
									Toast.makeText(Showcart.this, "Cart is empty!", Toast.LENGTH_LONG).show();
							}
							else if (radio_products.isChecked())  {
								if(searchname.getText().toString().length()>0) {
									products = db.getCartProducts(searchname.getText().toString());
									if (products.getCount()>0){
									ListAdapter adapter = new SimpleCursorAdapter(Showcart.this, 
											R.layout.row_cart_4column, 
											products, 
											new String[] {"name","supermarket_name", "product_name", "price"}, 
											new int[] {R.id.textname,R.id.textsupermarket,R.id.textproduct,R.id.textprice},0);									
									lv.setAdapter(adapter); 
									}
									else
										Toast.makeText(Showcart.this, "No products found with name:"+searchname.getText().toString(), Toast.LENGTH_LONG).show();
								}
								else
									Toast.makeText(Showcart.this, "Please enter a product name", Toast.LENGTH_LONG).show();
							}
							else if  (radio_category.isChecked())  {
								if(searchname.getText().toString().length()>0) {
									products = db.getCartCategories(searchname.getText().toString());
									if (products.getCount()>0){
									ListAdapter adapter = new SimpleCursorAdapter(Showcart.this, 
											R.layout.row_cart_4column, 
											products, 
											new String[] {"name","supermarket_name", "product_name", "price"}, 
											new int[] {R.id.textname,R.id.textsupermarket,R.id.textproduct,R.id.textprice},0);									
									lv.setAdapter(adapter); 
									}
									else
										Toast.makeText(Showcart.this, "No products found in category:"+searchname.getText().toString(), Toast.LENGTH_LONG).show();
								}
								else
									Toast.makeText(Showcart.this, "Please enter a category name", Toast.LENGTH_LONG).show();
							}
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
				if (id == R.id.action_settings) {
					return true;
				}
				if (id ==R.id.action_exit) {
		            finish();
		           // Log.d(TAG,"exited");
		            return true;
				}
				return super.onOptionsItemSelected(item);
	}
}
