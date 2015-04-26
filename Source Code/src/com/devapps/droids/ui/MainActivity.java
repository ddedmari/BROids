package com.devapps.droids.ui;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

import com.devapps.droids.R;
import com.devapps.droids.adapter.HouseListingArrayAdapter;
import com.devapps.droids.utils.HouseDataSouce;

/**
 * Created by mahendraliya on 26/04/15.
 */
public class MainActivity extends Activity {
	
	private ListView lvHouseListing;
	private HouseListingArrayAdapter mAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		setupLayout();
	}
	
	private void setupLayout() {
		lvHouseListing = (ListView) findViewById(R.id.lvHouseListing);
		mAdapter = new HouseListingArrayAdapter(MainActivity.this, HouseDataSouce.getHouseDataSource());
		lvHouseListing.setAdapter(mAdapter);
	}
	
}
