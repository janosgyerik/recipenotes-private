package com.recipenotes;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.os.Environment;
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

		/*
		cursor = helper.getReadableDatabase().query(
				"main_recipe", 
				new String[]{ 
						BaseColumns._ID, "name", "summary", "display_name",
						}, 
				null, null, null, null, "updated_dt desc, name");
		 */
		cursor = helper.getReadableDatabase().rawQuery(
				"select _id, ifnull(nullif(name, ''), '(not yet named)') name, summary, display_name"
						+ " from main_recipe"
						+ " order by updated_dt desc, name",
						null);
		startManagingCursor(cursor);

		//		View header = (View)getLayoutInflater().inflate(R.layout.recipelist_header_row, null);
		//		getListView().addHeaderView(header);

		ListAdapter adapter = new SimpleCursorAdapter(
				this, // Context.
				R.layout.recipe_list_item,
				cursor,
				new String[] { 
						BaseColumns._ID, "name", "display_name",
				},
				new int[] { 
						R.id._ID, R.id.name, R.id.ingredients,
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
		private void deleteRecipe(long recipeId) {
			String selectedItemId = String.valueOf(recipeId);
			helper.getWritableDatabase().delete("main_recipe", "_id = ?", new String[]{ selectedItemId });
			helper.getWritableDatabase().delete("main_recipeingredient", "recipe_id = ?", new String[]{ selectedItemId });
			helper.getWritableDatabase().delete("main_recipephoto", "recipe_id = ?", new String[]{ selectedItemId });
			cursor.requery();
			String PICTURES_DIR = "RecipeNotes";

			File storageDir = new File (
					String.format("%s/%s",
							Environment.getExternalStorageDirectory(),
							PICTURES_DIR
							));
			if (storageDir.isDirectory()) {
				String[] filenames = storageDir.list();
				if (filenames != null) {
					for (String filename : filenames) {
						if (filename.startsWith(String.format("recipe_%s_", selectedItemId))) {
							Log.d(TAG, "deleting " + filename);
							new File(storageDir, filename).delete();
						}
					}
				}
			}
		}

		@Override
		public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
				int arg2, long arg3) {
			final long recipeId = arg3;
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
			backupDatabaseFile();
			//			startActivity(new Intent(this, FileUploaderActivity.class));
			return true;
		case R.id.menu_restore:
			Intent intent = new Intent(this, FileSelectorActivity.class);
			// TODO string to strings
			intent.putExtra(FileSelectorActivity.PARAM_TITLE, "Select backup file");
			intent.putExtra(FileSelectorActivity.PARAM_DIRPARAM, "RecipeNotes/backups");
			intent.putExtra(FileSelectorActivity.PARAM_PATTERN, "^sqlite3-.*\\.db$");
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

	private void backupDatabaseFile() {
		try {
			File sd = Environment.getExternalStorageDirectory();
			File data = Environment.getDataDirectory();

			if (sd.canWrite()) {
				String packageName = "com.recipenotes";
				String dbname = "sqlite3.db";
				String backupName = String.format("sqlite3-%s.db", new SimpleDateFormat("yyyyMMdd-HHmmss").format(new Date()));
				String currentDBPath = String.format("/data/%s/databases/%s", packageName, dbname);
				File backupDir = new File(sd, "RecipeNotes/backups");
				if (! backupDir.isDirectory()) {
					backupDir.mkdirs();
				}
				File currentDB = new File(data, currentDBPath);
				File backupFile = new File(backupDir, backupName);

				FileChannel src = new FileInputStream(currentDB).getChannel();
				FileChannel dst = new FileOutputStream(backupFile).getChannel();
				dst.transferFrom(src, 0, src.size());
				src.close();
				dst.close();
				Toast.makeText(getBaseContext(), R.string.msg_backup_created, Toast.LENGTH_LONG).show();
			}
		} catch (Exception e) {
			Toast.makeText(getBaseContext(), e.toString(), Toast.LENGTH_LONG).show();
			Log.e(TAG, "Exception in backupDatabaseFile", e);
		}
	}
}
