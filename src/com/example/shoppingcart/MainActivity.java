package com.example.shoppingcart;

import android.support.v7.app.ActionBarActivity;
import android.app.ActionBar;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.app.Fragment;

public class MainActivity extends ActionBarActivity {

	// Declaring our tabs and the corresponding fragments.
		ActionBar.Tab CartTab, AdministrationTab, DimensionsTab;
		Fragment cartFragmentTab = new CartFragmentTab();
		Fragment administrationFragmentTab = new AdministrationFragmentTab();
		Fragment dimensionsFragmentTab = new DimensionsFragmentTab();
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		// Asking for the default ActionBar element that our platform supports.
				ActionBar actionBar = getActionBar();
				 
		        // Screen handling while hiding ActionBar icon.
		        actionBar.setDisplayShowHomeEnabled(true);
		 
		        // Screen handling while hiding Actionbar title.
		        actionBar.setDisplayShowTitleEnabled(true);
		 
		        // Creating ActionBar tabs.
		        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		 
		        // Setting custom tab icons.
		        CartTab = actionBar.newTab().setIcon(R.drawable.ic_action_shop_cart_icon);
		        AdministrationTab = actionBar.newTab().setIcon(R.drawable.ic_action_data_admin);
		        DimensionsTab = actionBar.newTab().setIcon(R.drawable.ic_action_dimensions);
		        
		        // Setting tab listeners.
		        CartTab.setTabListener(new TabListener(cartFragmentTab));
		        AdministrationTab.setTabListener(new TabListener(administrationFragmentTab));
		        DimensionsTab.setTabListener(new TabListener(dimensionsFragmentTab));
		       
		        // Adding tabs to the ActionBar.
		        actionBar.addTab(CartTab);
		        actionBar.addTab(AdministrationTab);
		        actionBar.addTab(DimensionsTab);
		
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
