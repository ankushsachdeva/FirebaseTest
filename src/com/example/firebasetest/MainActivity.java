package com.example.firebasetest;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity extends Activity {
	ListView lv;
	ArrayAdapter<String> mAdapter;
	Firebase rootRef;
	Boolean stop = false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Firebase.setAndroidContext(getApplicationContext());
		rootRef = new Firebase("https://torid-heat-4014.firebaseio.com/");
		setContentView(R.layout.activity_main);
		lv = (ListView) findViewById(R.id.lv);
		mAdapter = new ArrayAdapter<String>(this, R.layout.textview);
		lv.setAdapter(mAdapter);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		
	}
	
	private void init(){
		new android.os.Handler().postDelayed(
			    new Runnable() {
			        public void run() {
			        	if(stop)
			        		return;
			        	 double rand = Math.random();
				            if(rand < 0.25){
				            	goOnline();
				            }
				            else if(rand < 0.5){
				            	goOffline();
				            }
				            else if(rand < 0.75){
				            	randomRead();
				            }
				            else{
				            	randomWrite();
				            }
				            init();
			        }
			    }, 
			1000);
	}
	
	private void goOnline(){
		Firebase.goOnline();
		mAdapter.add("goOnline");
	}
	
	private void goOffline(){
		Firebase.goOffline();
		mAdapter.add("goOffline");
	}
	
	private void randomRead(){
		rootRef.child("random").addListenerForSingleValueEvent(new ValueEventListener() {
			
			@Override
			public void onDataChange(DataSnapshot snapshot) {
				mAdapter.add("Read data");
				
			}
			
			@Override
			public void onCancelled(FirebaseError arg0) {
				mAdapter.add("Read data->Cancelled");
			}
		});
	}
	
	private void randomWrite(){
		mAdapter.add("Write data");
		rootRef.child("random").push().setValue("a");
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		stop = true;
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		stop = false;
		init();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
}
