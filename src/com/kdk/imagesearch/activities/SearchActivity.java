package com.kdk.imagesearch.activities;

import java.util.ArrayList;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.kdk.imagesearch.R;
import com.kdk.imagesearch.adapters.ImageResultsAdapter;
import com.kdk.imagesearch.models.ImageResult;
import com.kdk.imagesearch.util.EndlessScrollListener;
import com.kdk.imagesearch.util.Settings;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

public class SearchActivity extends Activity {
	private EditText etSearch;
	private GridView gvResults;
	private ArrayList<ImageResult> imageResults;
	private ImageResultsAdapter aImageResults;
	private Settings settings;
	private final int REQUEST_CODE = 1776;
	private String search;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		setupViews();
		imageResults = new ArrayList<ImageResult>();
		aImageResults = new ImageResultsAdapter(this, imageResults);
		gvResults.setAdapter(aImageResults);
		settings = new Settings();
		}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu){
		getMenuInflater().inflate(R.menu.search_menu, menu);
		return true;
	}
	
	private void setupViews(){
		etSearch= (EditText) findViewById(R.id.etSearch);
		gvResults = (GridView) findViewById(R.id.gvResults);
		gvResults.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				//Launch the image display activity
				//Create the intent
				Intent i = new Intent(SearchActivity.this, ImageDisplayActivity.class);
				//Get the image restult to dispaly
				ImageResult result = imageResults.get(position);
				//Pass Image result into the intent
				i.putExtra("result", result);
				//Launch the new activity
				startActivity(i);
			}
			
		});
		
		gvResults.setOnScrollListener(new EndlessScrollListener(){

			@Override
			public void onLoadMore(int page, int totalItemsCount) {
				if (totalItemsCount < 64){
					searchImages(search, page);
				}
			}
			
		});
		
	}
	
	public void onImageSearch(View v){
		search = etSearch.getText().toString();
		searchImages(search, 0);
		//searchImages(search,1);
		
	}
	
	
	public void searchImages(String query, final int page){
		String searchUrl = buildURL(query, page*8);
		Toast.makeText(this, searchUrl, Toast.LENGTH_SHORT).show();
		AsyncHttpClient client = new AsyncHttpClient();
        client.get(searchUrl, new JsonHttpResponseHandler(){
        	@Override
        	public void onSuccess(int statusCode, Header[] headers, JSONObject response){
        		try {
					JSONArray imageResultsJSON = response.getJSONObject("responseData").getJSONArray("results");
					if(page==0){
						imageResults.clear(); //Only on new search
					}
					aImageResults.addAll(ImageResult.fromJSONArray(imageResultsJSON));
				} catch (JSONException e) {
					e.printStackTrace();
				}
            	Log.i("INFO", imageResults.toString());
        	}
        });
	}
	public void onSettingsSelected(MenuItem item){
		Log.d("GridImage", "Settings Menu");
		Intent settingsIntent = new Intent(SearchActivity.this, SettingsActivity.class);
		settingsIntent.putExtra("settings", settings);
		startActivityForResult(settingsIntent, REQUEST_CODE);
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data){
		if(resultCode==RESULT_OK && requestCode == REQUEST_CODE){
			settings = (Settings) data.getSerializableExtra("settings");
			Toast.makeText(this, settings.toString(), Toast.LENGTH_SHORT).show();
		}
	}
	
	private String buildURL(String query, int start){
		//String searchUrl = "http://ajax.googleapis.com/ajax/services/search/images?v=1.0&q=" + search;
		Uri.Builder builder = new Uri.Builder();
		builder.scheme("http");
		builder.authority("ajax.googleapis.com");
		builder.appendPath("ajax");
		builder.appendPath("services");
		builder.appendPath("search");
		builder.appendPath("images");
		builder.appendQueryParameter("v", "1.0");
		builder.appendQueryParameter("q", query);
		builder.appendQueryParameter("rsz", "8");
		builder.appendQueryParameter("start", Integer.toString(start));
		
		if(! settings.size.equals("")){
			builder.appendQueryParameter("imgsz", settings.size);
		}
		if(! settings.color.equals("")){
			builder.appendQueryParameter("imgcolor", settings.color);
		}
		if(! settings.type.equals("")){
			builder.appendQueryParameter("imgtype", settings.type);
		}
		if(! settings.site.equals("")){
			builder.appendQueryParameter("as_sitesearch", settings.site);
		}
		return builder.build().toString();
	}
	
}
