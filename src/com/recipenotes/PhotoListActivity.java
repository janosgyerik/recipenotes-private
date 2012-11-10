package com.recipenotes;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.recipenotes.PhotoListAdapter.RecipePhoto;

public class PhotoListActivity extends Activity {

	private static final String TAG = "PhotoListActivity";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(TAG, "++onCreate");
		setContentView(R.layout.photolist);

		int appWidth = getWindowManager().getDefaultDisplay().getWidth();
		
	    GridView gridview = (GridView) findViewById(R.id.gridview);
	    final PhotoListAdapter adapter = new PhotoListAdapter(this, appWidth);
	    gridview.setAdapter(adapter);
	    
	    gridview.setOnItemClickListener(new OnItemClickListener() {
	        public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
	        	RecipePhoto item = (RecipePhoto)adapter.getItem(position);
				Intent intent = new Intent(PhotoListActivity.this, EditRecipeActivity.class);
				intent.putExtra(BaseColumns._ID, item.recipeId);
				startActivity(intent);
	        }
	    });		

		findViewById(R.id.btn_add_recipe).setOnClickListener(new AddRecipeOnClickListener());
		
		findViewById(R.id.btn_recipelist).setOnClickListener(new RecipeListOnClickListener());
	}
	
	@Override
	public void onResume() {
		super.onResume();
		Log.d(TAG, "++onResume");
	}

	class AddRecipeOnClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			Intent intent = new Intent(PhotoListActivity.this, EditRecipeActivity.class);
			startActivity(intent);
		}
	}

	class RecipeListOnClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			PhotoListActivity.this.finish();
		}
	}
}
