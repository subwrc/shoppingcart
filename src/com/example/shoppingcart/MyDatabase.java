package com.example.shoppingcart;

import java.util.StringTokenizer;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.util.Log;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

public class MyDatabase extends SQLiteAssetHelper {

	private static final String DATABASE_NAME = "shopping_cart.db";
	private static final int DATABASE_VERSION = 1;
	
	public MyDatabase(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		
		// you can use an alternate constructor to specify a database location 
		// (such as a folder on the sd card)
		// you must ensure that this folder is available and you have permission
		// to write to it
		//super(context, DATABASE_NAME, context.getExternalFilesDir(null).getAbsolutePath(), null, DATABASE_VERSION);
		
	}
	
// ******************************************************READING FUNCTIONS**********************************************************

	// returns all categories for spinner1 in AddToCartActivity and Updates activities
	public Cursor getCategories() {

		SQLiteDatabase db = getReadableDatabase();
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		db.execSQL("PRAGMA foreign_keys=ON");
		
		String [] sqlSelect = {"0 _id", "name"}; 
		String sqlTables = "Categories";

		qb.setTables(sqlTables);
		Cursor c = qb.query(db, sqlSelect, null, null,
				null, null, null);

		c.moveToFirst();
		return c;

	}
	
	// returns products per chosen category for spinner2 AddToCartActivity
	public Cursor getCategoryProducts(String category) {

		SQLiteDatabase db = getReadableDatabase();
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		db.execSQL("PRAGMA foreign_keys=ON");
		
		String [] sqlSelect = {"0 _id","productname"}; 
		String sqlTables = "Products";

		qb.setTables(sqlTables);
		String where="category_name=" +"'"+category+"'";
		Cursor c = qb.query(db, sqlSelect, where, null,
				null, null, null);

		c.moveToFirst();
		return c;

	}

	// returns all categories for spinner1 in AddToCartActivity and Updates activities
		public Cursor getSupermarkets() {

			SQLiteDatabase db = getReadableDatabase();
			SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
			db.execSQL("PRAGMA foreign_keys=ON");
			
			String [] sqlSelect = {"0 _id", "name"}; 
			String sqlTables = "Supermarkets";

			qb.setTables(sqlTables);
			Cursor c = qb.query(db, sqlSelect, null, null,
					null, null, null);

			c.moveToFirst();
			return c;

		}
		
	// returns all products of Cart
	public Cursor getCart() {

		SQLiteDatabase db = getReadableDatabase();
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		db.execSQL("PRAGMA foreign_keys=ON");
		
		String [] sqlSelect = {"0 _id","name","supermarket_name", "product_name", "price"}; 
		String sqlTables = "Cart";
	
		qb.setTables(sqlTables);
					
		Cursor c = qb.query(db, sqlSelect, null, null,
							null, null, null);
	
		c.moveToFirst();
		return c;

	}
	// returns chosen products of Cart ordered by ascending price
		public Cursor getCartProducts(String name) {

			SQLiteDatabase db = getReadableDatabase();
			SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
			db.execSQL("PRAGMA foreign_keys=ON");
			
			String [] sqlSelect = {"0 _id","name","supermarket_name", "product_name", "price"}; 
			String sqlTables = "Cart";

			qb.setTables(sqlTables);
			String where="name=" +"'"+name+"'";
			Cursor c = qb.query(db, sqlSelect, where, null,
					null, null,  "price" + " ASC");

			c.moveToFirst();
			return c;

		}
		
		// returns  products of Cart of chosen category ordered by ascending price
		public Cursor getCartCategories(String name) {

			SQLiteDatabase db = getReadableDatabase();
			//SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
			db.execSQL("PRAGMA foreign_keys=ON");
			 // INNER JOIN of cart and Products to find the category of the product
			String MY_QUERY = "SELECT 0 _id, name, supermarket_name, product_name, price FROM Cart INNER JOIN Products"
					+ " ON Cart.product_name = Products.productname WHERE Products.category_name=? ORDER BY price ASC";
			Cursor c = db.rawQuery(MY_QUERY, new String[]{String.valueOf(name)});
			
			c.moveToFirst();
			return c;
		}
		
// ******************************************************INSERTING FUNCTIONS******************************************************************
		
	public boolean 	insertcategory (String othercategory) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL("PRAGMA foreign_keys=ON");
	    ContentValues contentValues = new ContentValues();
	    long rowid;
		contentValues.put("name", othercategory);
	   
	    Log.d("insertcategory","before inserting");
	    rowid=db.insert("Categories", null, contentValues);
	    Log.d("insertcategory","returned rowid= "+rowid);
	    if (rowid>0) 
			return true;
		else
			return false;			
	}
	
	public boolean 	insertproduct(String otherproduct, String othercategory) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL("PRAGMA foreign_keys=ON");
	    ContentValues contentValues = new ContentValues();
	    long rowid;
		contentValues.put("productname", otherproduct);
		contentValues.put("category_name", othercategory);
		
	    Log.d("insertproduct","before inserting");
	    rowid=db.insert("Products", null, contentValues);
	    Log.d("insertproduct","returned rowid= "+rowid);
	    if (rowid>0) 
			return true;
		else
			return false;		
	}
	
	public boolean inserttocart(String nametext, String supermarket,String product,String price) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL("PRAGMA foreign_keys=ON");
	    ContentValues contentValues = new ContentValues();
	    long rowid;
	    double dprice=Double.parseDouble(price); // .replaceAll(",", ".") or use String.format(Locale.ENGLISH,"%.2f", ...) before entering price!!
		contentValues.put("name", nametext);
		contentValues.put("supermarket_name", supermarket);
		contentValues.put("product_name", product);
		contentValues.put("price", dprice);
		
	    Log.d("inserttocart","before inserting");
	    rowid=db.insert("Cart", null, contentValues);
	    Log.d("inserttocart","returned rowid= "+rowid);
	    if (rowid>0) 
			return true;
		else
			return false;		
	}
	
	public boolean insertsupermarket(String name, String location) {
		
		StringTokenizer st = new StringTokenizer(location, "-");
		//Log.d("insertsupermarket","string latitude="+st.nextToken());
		//Log.d("insertsupermarket","string longitude="+st.nextToken());
		double latitude=Double.parseDouble(st.nextToken().replaceAll(",", ".")); // this will contain Latitude
		Log.d("insertsupermarket","latitude="+latitude);
		double longitude=Double.parseDouble(st.nextToken().replaceAll(",", "."));// this will contain Longitude
		Log.d("insertsupermarket","longitude="+longitude);
		
		 SQLiteDatabase db = this.getWritableDatabase();
		 db.execSQL("PRAGMA foreign_keys=ON");
	      ContentValues contentValues = new ContentValues();
	      long rowid;
	     
	      contentValues.put("name", name);
	      contentValues.put("lat", latitude);
	      contentValues.put("long", longitude);
	      Log.d("insertsupermarket","before inserting");
	      rowid=db.insert("Supermarkets", null, contentValues);
	      Log.d("insertsupermarket","returned rowid= "+rowid);
		if (rowid>0) 
			return true;
		else
			return false;		
	}

//******************************************************DELETING FUNCTIONS*************************************************************/

	public int 	deletecart () {
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL("PRAGMA foreign_keys=ON");   
		int rowid;
			rowid= db.delete("Cart", "1", null);    
		return rowid;			
		}
	
	public int 	deleteFromTable (String table, String column, String value) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL("PRAGMA foreign_keys=ON"); 
		String where=column+"=" +"'"+value+"'";
		int rowid;
			rowid= db.delete(table, where, null);    
		return rowid;			
		}
	
	//******************************************************UPDATE FUNCTION*************************************************************/

	public  int	updateTable (String table, String column, String oldname, String newname) throws SQLiteConstraintException {
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL("PRAGMA foreign_keys=ON");
		ContentValues newValues = new ContentValues();
		newValues.put(column, newname);		
		String where=column+"="+"'"+oldname+"'";
		Log.d("updateTable","where="+where);
		int rowid=0;
		if ((rowid= db.update(table, newValues, where,null))<1)
			throw new SQLiteConstraintException("Failed to insert row into " + table);
		Log.d("updateTable","rowid="+rowid);
		return	rowid;
		
/*		try {
			rowid= db.update(table, newValues, where,null);
			Log.d("updateTable","rowid="+rowid);
			return	rowid;
			}
		catch (android.database.sqlite.SQLiteConstraintException e) {
		    Log.e("updateTable", "SQLiteConstraintException:" + e.getMessage());
		    e.printStackTrace();
		    return	rowid;
		}
		catch (android.database.sqlite.SQLiteException e) {
		    Log.e("updateTable", "SQLiteException:" + e.getMessage());
		} 
		catch (Exception e) {
		    Log.e("updateTable", "Exception:" + e.getMessage());
		}
				*/		
	}
}


