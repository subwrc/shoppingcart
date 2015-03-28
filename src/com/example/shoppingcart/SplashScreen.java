package com.example.shoppingcart;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
//import android.app.Activity;
import android.content.Intent;

public class SplashScreen extends Activity {
	int splashWaitingTime = 3 * 1000;// 3 seconds
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash_screen);
		
		Animation anim = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        anim.reset();
        ImageView iv = (ImageView) findViewById(R.id.splashscreen);
        iv.clearAnimation();
        iv.startAnimation(anim);
        
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {

				Intent intent = new Intent(SplashScreen.this,MainActivity.class);
				finish();
				startActivity(intent);

				overridePendingTransition(R.anim.fade_in, R.anim.zoom_out);

			}
		}, splashWaitingTime);

		new Handler().postDelayed(new Runnable() 
		{
			@Override
			public void run() 
			{
				//Do nothing
			} 
		}, splashWaitingTime);
		
	}
	@Override
	public void onBackPressed() {
		finish();
		super.onBackPressed();
	}
	
}
