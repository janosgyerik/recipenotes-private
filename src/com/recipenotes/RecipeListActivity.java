package com.recipenotes;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class RecipeListActivity extends ListActivity {

	private static final String TAG = "RecipeListActivity";

	private RecipeNotesSQLiteOpenHelper helper;
	private Cursor cursor;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.recipelist);

		helper = new RecipeNotesSQLiteOpenHelper(this);

		cursor = helper.getRecipeListCursor();
		startManagingCursor(cursor);

		ListAdapter adapter = new SimpleCursorAdapter(
				this, // Context.
				R.layout.recipelist_item,
				cursor,
				new String[] { 
						BaseColumns._ID, "name", "display_name",
				}, // column names in the query ...
				new int[] { 
						R.id._ID, R.id.name, R.id.ingredients,
				}
				); // ... view ids to bind to
		setListAdapter(adapter);

		getListView().setOnItemClickListener(new RecipeListOnItemClickListener());
		getListView().setOnItemLongClickListener(new RecipeListOnItemLongClickListener());

		((Button)findViewById(R.id.btn_add_recipe)).setOnClickListener(new AddRecipeOnClickListener());
	}

	class AddRecipeOnClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			Intent intent = new Intent(RecipeListActivity.this, RecipeDetailsActivity.class);
			startActivity(intent);
		}
	}

	class RecipeListOnItemClickListener implements OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			Intent intent = new Intent(RecipeListActivity.this, RecipeDetailsActivity.class);
			intent.putExtra(BaseColumns._ID, ((TextView)view.findViewById(R.id._ID)).getText());
			startActivity(intent);
		}
	}

	class RecipeListOnItemLongClickListener implements OnItemLongClickListener {
		private void deleteRecipe(String recipeId) {
			if (helper.deleteRecipe(recipeId)) {
				RecipeFileManager.deleteRecipe(recipeId);
				cursor.requery();
				Toast.makeText(getBaseContext(), R.string.msg_recipe_deleted, Toast.LENGTH_LONG).show();
			}
		}

		@Override
		public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
				int arg2, long arg3) {
			final String recipeId = String.valueOf(arg3);
			new AlertDialog.Builder(RecipeListActivity.this)
			.setMessage(R.string.confirm_are_you_sure)
			.setCancelable(true)
			.setTitle(R.string.title_delete_recipe)
			.setPositiveButton(R.string.btn_yes, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					deleteRecipe(recipeId);
				}
			})
			.setNegativeButton(R.string.btn_cancel, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					dialog.cancel();
				}
			})
			.show();
			return true;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_backup:
			try {
				boolean success = RecipeFileManager.backupDatabaseFile();
				if (success) {
					Toast.makeText(getBaseContext(), R.string.msg_backup_created, Toast.LENGTH_LONG).show();
				}
				else {
					Toast.makeText(getBaseContext(), R.string.error_backup_failed, Toast.LENGTH_LONG).show();
				}
			} catch (Exception e) {
				Toast.makeText(getBaseContext(), R.string.error_backup_exception, Toast.LENGTH_LONG).show();
				Log.e(TAG, "Exception in backupDatabaseFile", e);
			}
			return true;

		case R.id.menu_restore:
			Intent intent = new Intent(this, FileSelectorActivity.class);
			intent.putExtra(FileSelectorActivity.PARAM_TITLE, getString(R.string.title_select_backupfile));
			intent.putExtra(FileSelectorActivity.PARAM_DIRPARAM, RecipeFileManager.BACKUPS_DIRPARAM);
			intent.putExtra(FileSelectorActivity.PARAM_PATTERN, RecipeFileManager.BACKUPFILES_PATTERN);
			startActivity(intent);
			return true;

		case R.id.menu_quit:
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage(R.string.msg_quit)
			.setCancelable(true)
			.setPositiveButton(R.string.btn_yes, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					finish();
				}
			})
			.setNegativeButton(R.string.btn_cancel, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					dialog.cancel();
				}
			}).show();
			return true;
		}
		return false;
	}

	@Override  
	protected void onDestroy() {
		Log.d(TAG, "++onDestroy");
		super.onDestroy();
		helper.close();
	}
}
