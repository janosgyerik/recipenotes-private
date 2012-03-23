package com.mywinenotes;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.SimpleCursorAdapter;

public class WineListActivity extends ListActivity {

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wine_list);

		SQLiteOpenHelper helper = new WineNotesSQLiteOpenHelper(this);
		Cursor mCursor = helper.getWritableDatabase().query(
				"main_wine", 
				new String[]{"_id", "name"}, 
				null, null, null, null, null);
		startManagingCursor(mCursor);

		ListAdapter adapter = new SimpleCursorAdapter(
				this, // Context.
				android.R.layout.simple_list_item_1,
				mCursor,                                              // Pass in the cursor to bind to.
				new String[] {"name",},           // Array of cursor columns to bind to.
				new int[] {android.R.id.text1}
				);  // Parallel array of which template objects to bind to those columns.

		// Bind to our new adapter.
		setListAdapter(adapter);
		
		getListView().setOnItemClickListener(new WineListItemClickListener());
	}
	
	class WineListItemClickListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int position,
				long arg3) {
			Intent intent = new Intent(WineListActivity.this, WineDetailsActivity.class);
			intent.putExtra("pos", position);
			startActivity(intent);
		}
		
	}
}

