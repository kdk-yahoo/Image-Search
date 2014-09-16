package com.kdk.imagesearch.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.kdk.imagesearch.R;
import com.kdk.imagesearch.util.Settings;

public class SettingsActivity extends Activity {

	private Settings settings;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		settings = (Settings) getIntent().getSerializableExtra("settings");
		((EditText) findViewById(R.id.etSite)).setText(settings.site);
		setupSpinners();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.settings_menu, menu);
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
		return super.onOptionsItemSelected(item);
	}
	
	private void setupSpinners(){
		Spinner sizeSettings = (Spinner) findViewById(R.id.spImageSize);
		Spinner colorSettings = (Spinner) findViewById(R.id.spImageColor);
		Spinner typeSettings = (Spinner) findViewById(R.id.spImageType);
		
		SpinnerListener listener = new SpinnerListener();
		
		sizeSettings.setSelection(((ArrayAdapter) sizeSettings.getAdapter()).getPosition(settings.size));
		colorSettings.setSelection(((ArrayAdapter) colorSettings.getAdapter()).getPosition(settings.color));
		typeSettings.setSelection(((ArrayAdapter) typeSettings.getAdapter()).getPosition(settings.type));

		
		sizeSettings.setOnItemSelectedListener(listener);
		colorSettings.setOnItemSelectedListener(listener);
		typeSettings.setOnItemSelectedListener(listener);
	}
	
	public boolean onSaveSelected(MenuItem item){
		Intent data = new Intent();
		settings.site = ((EditText) findViewById(R.id.etSite)).getText().toString();
		data.putExtra("settings", settings);
		setResult(RESULT_OK, data);
		finish();
		return true;
	}
	
	private class SpinnerListener implements OnItemSelectedListener {

		@Override
		public void onItemSelected(AdapterView<?> parent, View view,
				int position, long id) {
			switch(parent.getId()){
				case R.id.spImageSize:
					settings.size = parent.getSelectedItem().toString();
					break;
				case R.id.spImageColor:
					settings.color = parent.getSelectedItem().toString();
					break;
				case R.id.spImageType:
					settings.type = parent.getSelectedItem().toString();
					break;
			}
			
		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {
			// TODO Auto-generated method stub
			
		}
		
	}
}
