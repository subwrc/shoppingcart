package com.example.shoppingcart;

import java.util.Locale;

import com.example.shoppingcart.AlertDialogManager;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;

import android.support.v7.app.ActionBarActivity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Point;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class AddToCartActivity extends ActionBarActivity implements OnClickListener,ConnectionCallbacks,OnConnectionFailedListener,LocationListener {

	TextView location1 ;
	TextView location2 ;
	TextView location3 ;
	EditText othercategory;
	EditText otherproduct;
	EditText nametext;
	EditText name1;
	EditText price1;
	EditText name2;
	EditText price2;
	EditText name3;
	EditText price3;
	
	private Cursor categories;
	private Cursor products;
	private MyDatabase db;
	
    Spinner spinner1;
    Spinner spinner2;
    String chosencategory;
    String chosenproduct;
    
    Boolean initialDisplay1 = false; //used for spinner1 click listener
   // Boolean initialDisplay2 = true; //used for spinner2 click listener
    Boolean mRequestingLocationUpdates=true;
    int width ;
    int height;
    
    PopupWindow pw;
	GoogleMap map;
	//LocationClient mLocationClient;
	LayoutInflater inflater;
	View layout;
	 // Kalamaria Thessalonikis coordinates
    double latitude = 40.5764478;
    double longitude = 22.9552206;
    MarkerOptions markerOptions;
    Marker marker;
    float maxprice=100f; //maximum price
    
   protected GoogleApiClient mGoogleApiClient;

   protected Location mLastLocation;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_to_cart);	
		
		// For the popup map window
		//We need to get the instance of the LayoutInflater, use the context of this activity
	     inflater = (LayoutInflater) AddToCartActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    //Inflate the view from a predefined XML layout
	     layout = inflater.inflate(R.layout.popupspage,(ViewGroup) findViewById(R.id.popuplayout));
	     // create a 3/4 of screen width and 3/4 of screen height PopupWindow
	     Display display = getWindowManager().getDefaultDisplay();
	    
	     Point size = new Point();
	     display.getSize(size); // save display resolution in size
	     width = (int) (0.75f*size.x);
	     height = (int) (0.75f*size.y);	    
    
		location1 = (TextView) findViewById(R.id.location1);
		location2 = (TextView) findViewById(R.id.location2);
		location3 = (TextView) findViewById(R.id.location3);
		othercategory = (EditText) findViewById(R.id.other_category);
		otherproduct = (EditText) findViewById(R.id.other_product);
		nametext = (EditText) findViewById(R.id.name_text);
		name1 = (EditText) findViewById(R.id.name1);
		price1 = (EditText) findViewById(R.id.price1);
		name2 = (EditText) findViewById(R.id.name2);
		price2 = (EditText) findViewById(R.id.price2);
		name3 = (EditText) findViewById(R.id.name3);
		price3 = (EditText) findViewById(R.id.price3);
		
		location1.setOnClickListener(this);
		location2.setOnClickListener(AddToCartActivity.this);		
		location3.setOnClickListener(this);
		
		spinner1 = (Spinner) findViewById(R.id.spinner1);
		spinner2 = (Spinner) findViewById(R.id.spinner2);
		
		Button buttonclear = (Button) findViewById(R.id.buttonclear);
		buttonclear.setOnClickListener(new OnClickListener()
		   {
		             @Override
		             public void onClick(View v)
		             {
		            	 location1.setText("");
		            	 location2.setText("");
		            	 location3.setText("");
		             } 
		   }); 
		 
		db = new MyDatabase(this);			 		 
		
		setspinner1 ();	 
		buildGoogleApiClient();
	}
	
	//populate spinner of categories
	public void setspinner1 () {	
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
            	
            	if (initialDisplay1==true)
            		initialDisplay1=false;
            	else { 
            		//Log.d("spinner1=", (String) spinner1.getSelectedItem().toString());
            		Cursor cursor = (Cursor) dataAdapter.getItem(position);
            		chosencategory = cursor.getString(cursor.getColumnIndex("name"));
            		Log.d("spinner1=", chosencategory);
            		if (chosencategory.equals("’λλο")) { 
            			Log.d("spinner1=", "’λλο");
            			othercategory.setVisibility(TextView.VISIBLE);
            			otherproduct.setVisibility(TextView.VISIBLE);
            			spinner2.setVisibility(View.GONE);
            		}
            		else {
            			othercategory.setVisibility(TextView.GONE);
            			otherproduct.setVisibility(TextView.GONE);
            			spinner2.setVisibility(View.VISIBLE);
            			
            			setspinner2(chosencategory);
            		}
            	}
            	
            	if (spinner2.getVisibility() == View.VISIBLE) {
            	    // Its visible
            	} else {
            	    // Either gone or invisible
            	}
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });
	}

	//populate spinner of products of chosen category
		public void setspinner2 (String category) {
			products = db.getCategoryProducts(category);
			final SimpleCursorAdapter  dataAdapter = new SimpleCursorAdapter(this, 
					android.R.layout.simple_list_item_1, 
					products, 
					new String[] {"productname"}, 
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
            		chosenproduct = cursor.getString(cursor.getColumnIndex("productname"));	            	
	            }

	            @Override
	            public void onNothingSelected(AdapterView<?> parentView) {
	                // your code here
	            }

	        });
		}
	
			//called when save button clicked
	public void savetocart (View view) {
			if (!isEmpty(nametext))  { 				
				if ((spinner2.getVisibility() == View.VISIBLE)|| ((othercategory.getVisibility() == View.VISIBLE)&&!isEmpty(othercategory)&&!isEmpty(otherproduct)) ) {
					if(!isEmpty(name1)&&(isValidPrice(price1))&& (location1.getText().toString().length()>0) ) { // check supermarket1						
						if ((spinner2.getVisibility() == View.VISIBLE))	{
							// ignoring if insertsupermarket fails because even if supermarket already exists we want to add product to cart
								db.insertsupermarket(name1.getText().toString(),location1.getText().toString()); 
									if(db.inserttocart(nametext.getText().toString(),name1.getText().toString(),chosenproduct,price1.getText().toString()))
										Toast.makeText(this, "Product's supermarket 1 added to cart!", Toast.LENGTH_SHORT).show();
									else Toast.makeText(this, "Supermarket 1 did NOT saved,record already exists!", Toast.LENGTH_LONG).show();
						}  
						else { // we have chosen to insert new category
							db.insertsupermarket(name1.getText().toString(),location1.getText().toString());
							if((db.insertcategory(othercategory.getText().toString()))&&
							(db.insertproduct(otherproduct.getText().toString(),othercategory.getText().toString()))&&
							(db.inserttocart(nametext.getText().toString(),name1.getText().toString(),otherproduct.getText().toString(),price1.getText().toString())))
								{Toast.makeText(this, "Product's supermarket 1 added to cart!", Toast.LENGTH_SHORT).show();
								setspinner1 (); //to refresh spinner with new category entered
								}
							else {
								Toast.makeText(this, "Supermarket 1 did NOT saved,record already exists!", Toast.LENGTH_LONG).show();
								setspinner1 (); //to refresh spinner with new category entered
								}
						}
					
						if(!isEmpty(name2)&&(isValidPrice(price2))&& (location2.getText().toString().length()>0) ) {  // check supermarket2
							db.insertsupermarket(name2.getText().toString(),location2.getText().toString());
							if((db.inserttocart(nametext.getText().toString(),name2.getText().toString(),chosenproduct,price2.getText().toString())) )
								Toast.makeText(this, "Product's supermarket 2 added to cart!", Toast.LENGTH_SHORT).show();
							else Toast.makeText(this, "Supermarket 2 did NOT saved,record already exists!", Toast.LENGTH_LONG).show();
						}
						
						if(!isEmpty(name3)&&(isValidPrice(price3))&&(location3.getText().toString().length()>0) ) {  // check supermarket3	
							db.insertsupermarket(name3.getText().toString(),location3.getText().toString());
							if((db.inserttocart(nametext.getText().toString(),name3.getText().toString(),chosenproduct,price3.getText().toString())) )
								Toast.makeText(this, "Product's supermarket 3 added to cart!", Toast.LENGTH_SHORT).show();	
							else Toast.makeText(this, "Supermarket 3 did NOT saved,record already exists!", Toast.LENGTH_LONG).show();
						}
						
					}
					else
						Toast.makeText(this, "Please fill all fields", Toast.LENGTH_LONG).show();
	        	}
				else 
					Toast.makeText(this, "Please choose or create a category", Toast.LENGTH_LONG).show();
			}				
			else 
				Toast.makeText(this, "Please enter a product name", Toast.LENGTH_LONG).show();			
		}
	
	// validating price
	private boolean isValidPrice(EditText myeditText) {
		double transprice;	
		String price= myeditText.getText().toString();
		if (price.length()>0){
			transprice=Double.parseDouble(price)-0.005f; // -.005 to avoid rounding by String.format
			// must use locale or else "." will be changed to "," and after setText inserttocart will fail!
			String formatted = String.format(Locale.ENGLISH,"%.2f", transprice); 
			myeditText.setText(formatted);				
			if (transprice<=maxprice)
				return true;
			else {
				Toast.makeText(this, "Please enter a price lower than "+maxprice, Toast.LENGTH_LONG).show();
			return false;
			}
		}
			else
				return false;
	}
		
	private boolean isEmpty(EditText myeditText) {
            return myeditText.getText().toString().trim().length() == 0;
        }
		
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_to_cart, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_help) {
			// Alert dialog manager
			AlertDialogManager alert = new AlertDialogManager();
			alert.showAlertDialog(AddToCartActivity.this, "Help", "To save a supermarket you have to have filled first it's previous ones!");
			return true;
		}
		if (id ==R.id.action_exit) {
            finish();
           // Log.d(TAG,"exited");
            return true;		
		}
		return super.onOptionsItemSelected(item);
	}
	
	private void initiatePopupWindow(final String supermarket) 
    {  
        try 
        {        	
        	 // create a 3/4 of screen width and 3/4 of screen height PopupWindow
            pw = new PopupWindow(layout, width, height, true);

            // display the popup in the center
            pw.showAtLocation(layout, Gravity.CENTER, 0, 0);
            
            setUpMapIfNeeded();
            setUpLocationClientIfNeeded();
            mGoogleApiClient.connect();
           
            // Creating a LatLng object for the Kalamaria location
            LatLng latLng = new LatLng(latitude, longitude);            
            // Showing the current location in Google Map
            map.moveCamera(CameraUpdateFactory.newLatLng(latLng));     
            // Zoom in the Google Map
            map.animateCamera(CameraUpdateFactory.zoomTo(14));
         // Clears the previously touched position
            map.clear();     
            
            // Setting a click event handler for the map
            map.setOnMapClickListener(new OnMapClickListener() {     
                @Override
                public void onMapClick(LatLng latLng) {     
                    // Creating a marker
                    markerOptions = new MarkerOptions();     
                    // Setting the position for the marker
                    markerOptions.position(latLng);     
                    // Setting the title for the marker.
                    // This will be displayed on taping the marker
                    markerOptions.title(latLng.latitude + " : " + latLng.longitude);
                    markerOptions.draggable(true);     
                    // Clears the previously touched position
                    map.clear();  
                    // Animating to the touched position
                    map.animateCamera(CameraUpdateFactory.newLatLng(latLng));     
                    // Placing a marker on the touched position
                    marker=map.addMarker(markerOptions); 
                }
            });
            
            Button buttoncancel=(Button)layout.findViewById(R.id.buttoncancel);
            buttoncancel.setOnClickListener(new OnClickListener() 
            {   
                @Override
                public void onClick(View v) 
                {                    
                	 pw.dismiss();
                }

            });            

            Button okbutton=(Button)layout.findViewById(R.id.okbutton);
            okbutton.setOnClickListener(new OnClickListener() 
            	{   
                @Override
                public void onClick(View v) 
                	{
                	if (mLastLocation!=null)
                		{    // Getting longitude of the current location
                		 latitude = mLastLocation.getLatitude();               	 
                		 longitude = mLastLocation.getLongitude();  
                		}
                		// Setting latitude and longitude in the TextView
                		if (supermarket.equals("supermarket1"))  {
                			if (marker==null)
                				location1.setText(latitude  + "-"+ longitude );
                			else  { 
                				LatLng latLng =  markerOptions.getPosition();                				
                				location1.setText(String.format("%.7f", latLng.latitude) + "-"+String.format("%.7f", latLng.longitude));
                				}                				
                		}
                		else if (supermarket.equals("supermarket2")) {
                			if (marker==null)
                				location2.setText(latitude  + "-"+ longitude );
                			else  { 
                				LatLng latLng =  markerOptions.getPosition();                				
                				location2.setText(String.format( "%.7f", latLng.latitude) + "-"+ String.format("%.7f", latLng.longitude));
                				}                				
                		}
                		else if (supermarket.equals("supermarket3")) {
                			if (marker==null)
                				location3.setText(latitude  + "-"+ longitude );
                			else  { 
                				LatLng latLng =  markerOptions.getPosition();                				
                				location3.setText(String.format("%.7f", latLng.latitude) + "-"+String.format("%.7f", latLng.longitude));
                				}                				
                			}
                		
                	 marker=null;
                     pw.dismiss();
                	
                	}

            	});      

        }
        catch (Exception e) 
        {
            e.printStackTrace();
        }
     }
	
	  private void setUpMapIfNeeded() {
	        // Do a null check to confirm that we have not already instantiated the map.
	        if (map == null) {
	            // Try to obtain the map from the SupportMapFragment.
	        	map = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.popupmapview))
	                    .getMap();
	            // Check if we were successful in obtaining the map.
	            if (map != null) {
	            	map.setMyLocationEnabled(true);	            	
	            }
	        }
	    }
	  /**
	     * Builds a GoogleApiClient. Uses the addApi() method to request the LocationServices API.
	     */
	    protected synchronized void buildGoogleApiClient() {
	        mGoogleApiClient = new GoogleApiClient.Builder(this)
	                .addConnectionCallbacks(this)
	                .addOnConnectionFailedListener(this)
	                .addApi(LocationServices.API)
	                .build();
	    }  
	    
	  private void setUpLocationClientIfNeeded() {
		  Log.d("setUpLocationClientIfNeeded","entered");
	    }
	  
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()) {
        case R.id.location1:
        	Log.d("location1", "location1");
        	initiatePopupWindow("supermarket1") ;
          // it was the first button
          break;
        case R.id.location2:
          // it was the second button
        	initiatePopupWindow("supermarket2"); 
        	Log.d("location2", "location2");
          break;	
        case R.id.location3:
            // it was the THIRD button
        	initiatePopupWindow("supermarket3"); 
        	Log.d("location3", "location3");
            break;		
		}	
	}

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		mLastLocation=location;
	}

	@Override
	public void onConnectionFailed(ConnectionResult result) {
		// TODO Auto-generated method stub
		 Log.d("onConnectionFailed", "Connection failed: ConnectionResult.getErrorCode() = " + result.getErrorCode());
	}

	 /**
     * Runs when a GoogleApiClient object successfully connects.
     */
    @Override
    public void onConnected(Bundle connectionHint) {
        // Provides a simple way of getting a device's location and is well suited for
        // applications that do not require a fine-grained location and that do not need location
        // updates. Gets the best and most recent location currently available, which may be null
        // in rare cases when a location is not available.
    	 if (mRequestingLocationUpdates) {
    	        startLocationUpdates();
    	    }   	
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null) {
        	Toast.makeText(this, "Your location is :"+ mLastLocation.getLatitude()+","+ mLastLocation.getLongitude(), Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "No location detected. Please enable GPS.", Toast.LENGTH_LONG).show();
        }
    }
    
    protected void startLocationUpdates() {
    	 LocationRequest mLocationRequest = new LocationRequest();
 	    mLocationRequest.setInterval(10000);
 	    mLocationRequest.setFastestInterval(5000);
 	    mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
 	    
        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);
    }	
    @Override
    protected void onPause() {
        super.onPause();
        if(mGoogleApiClient.isConnected())
        	stopLocationUpdates();
    }

    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(
                mGoogleApiClient, this);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mGoogleApiClient.isConnected() && !mRequestingLocationUpdates) {
            startLocationUpdates();
        }
    }
    
	@Override
	public void onConnectionSuspended(int cause) {
		// The connection to Google Play services was lost for some reason. We call connect() to
        // attempt to re-establish the connection.
        Log.i("onConnectionSuspended", "Connection suspended");
        mGoogleApiClient.connect();
	}
}