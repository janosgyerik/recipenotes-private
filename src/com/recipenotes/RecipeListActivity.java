package com.recipenotes;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class RecipeListActivity extends ListActivity {

	// Debugging
	private static final String TAG = "RecipeListActivity";

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.recipe_list);

		SQLiteOpenHelper helper = new RecipeNotesSQLiteOpenHelper(this);
		//helper.getWritableDatabase().execSQL("DELETE from main_recipe where name = ''");
		Cursor mCursor = helper.getWritableDatabase().query(
				"main_recipe", 
				new String[]{ 
						BaseColumns._ID, "name", "summary", 
						}, 
				null, null, null, null, "name");
		startManagingCursor(mCursor);

		ListAdapter adapter = new SimpleCursorAdapter(
				this, // Context.
				R.layout.recipe_list_item,
				mCursor,
				new String[] { 
						BaseColumns._ID, "name",
						},
				new int[] { 
						R.id._ID, R.id.name,
						}
				);  // Parallel array of which template objects to bind to those columns.
		setListAdapter(adapter);
		
		getListView().setOnItemClickListener(new RecipeListItemClickListener());

		((Button)findViewById(R.id.btn_add_recipe)).setOnClickListener(new AddRecipeOnClickListener());
	}
	
	class RecipeListItemClickListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			Intent intent = new Intent(RecipeListActivity.this, RecipeDetailsActivity.class);
			intent.putExtra(BaseColumns._ID, ((TextView)view.findViewById(R.id._ID)).getText());
			startActivity(intent);
		}
		
	}
	
	class AddRecipeOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			Intent intent = new Intent(RecipeListActivity.this, RecipeDetailsActivity.class);
			intent.putExtra("dummy", "dummy");
			startActivity(intent);
		}
		
	}
}

