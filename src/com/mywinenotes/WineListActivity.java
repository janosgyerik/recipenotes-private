package com.mywinenotes;

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

public class WineListActivity extends ListActivity {

	// Debugging
	private static final String TAG = "WineListActivity";

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wine_list);

		SQLiteOpenHelper helper = new WineNotesSQLiteOpenHelper(this);
		Cursor mCursor = helper.getWritableDatabase().query(
				"main_wine", 
				new String[]{ 
						BaseColumns._ID, "name", "year", "wine_type", "region", "grape",
						"aroma", "taste", "after_taste", "overall", "to_buy", 
						}, 
				null, null, null, null, null);
		startManagingCursor(mCursor);

		ListAdapter adapter = new SimpleCursorAdapter(
				this, // Context.
				R.layout.wine_list_item,
				mCursor,
				new String[] { 
						BaseColumns._ID, "name",
						},
				new int[] { 
						R.id._ID, R.id.name, 
						}
				);  // Parallel array of which template objects to bind to those columns.
		setListAdapter(adapter);
		
		getListView().setOnItemClickListener(new WineListItemClickListener());

		((Button)findViewById(R.id.btn_add_wine)).setOnClickListener(new AddWineOnClickListener());
	}
	
	class WineListItemClickListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			Intent intent = new Intent(WineListActivity.this, WineDetailsActivity.class);
			intent.putExtra(BaseColumns._ID, ((TextView)view.findViewById(R.id._ID)).getText());
			startActivity(intent);
		}
		
	}
	
	class AddWineOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			Intent intent = new Intent(WineListActivity.this, WineDetailsActivity.class);
			intent.putExtra("dummy", "dummy");
			startActivity(intent);
		}
		
	}
}

