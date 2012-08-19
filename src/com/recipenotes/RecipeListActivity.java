package com.recipenotes;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class RecipeListActivity extends ListActivity {

	// Debugging
	private static final String TAG = "RecipeListActivity";
	
	private SQLiteOpenHelper helper;
	private Cursor cursor;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.recipe_list);

		helper = new RecipeNotesSQLiteOpenHelper(this);
		//helper.getWritableDatabase().execSQL("DELETE from main_recipe where name = ''");
		
		cursor = helper.getReadableDatabase().query(
				"main_recipe", 
				new String[]{ 
						BaseColumns._ID, "name", "summary", "display_name",
						}, 
				null, null, null, null, "name");
		startManagingCursor(cursor);

		ListAdapter adapter = new SimpleCursorAdapter(
				this, // Context.
				R.layout.recipe_list_item,
				cursor,
				new String[] { 
						BaseColumns._ID, "display_name",
						},
				new int[] { 
						R.id._ID, R.id.name,
						}
				);  // Parallel array of which template objects to bind to those columns.
		setListAdapter(adapter);
		
		getListView().setOnItemClickListener(new RecipeListItemClickListener());
		getListView().setOnItemLongClickListener(new RecipeListItemLongClickListener());

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
	
	class RecipeListItemLongClickListener implements OnItemLongClickListener {
		@Override
		public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
				int arg2, long arg3) {
			/*
			String selectedItemId = String.valueOf(arg3);
			helper.getWritableDatabase().delete("main_recipe", "_id = ?", new String[]{ selectedItemId });
			helper.getWritableDatabase().delete("main_recipeingredient", "recipe_id = ?", new String[]{ selectedItemId });
			cursor.requery();
			*/
			return true;
		}
	}
}
