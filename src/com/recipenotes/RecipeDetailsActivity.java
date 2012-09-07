package com.recipenotes;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.BaseColumns;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Toast;

public class RecipeDetailsActivity extends Activity {

	// Debugging
	private static final String TAG = "RecipeDetailsActivity";

	private static final String RECIPES_TABLE_NAME = "main_recipe";
	private static final String INGREDIENTS_TABLE_NAME = "main_ingredient";
	private static final String RECIPE_INGREDIENTS_TABLE_NAME = "main_recipeingredient";
	private static final String RECIPE_PHOTOS_TABLE_NAME = "main_recipephoto";

	private static final int PICTURE_TAKEN = 1;

	private String recipeId;
	private RecipeNotesSQLiteOpenHelper helper;

	private EditText nameView;

	private MultiAutoCompleteTextView ingredientView;

	private ArrayAdapter<String> ingredientsListAdapter;
	private ListView ingredientsListView;

	private List<String> photoFilenames;
	private List<String> photosToDelete = new ArrayList<String>();

	private static String PICTURES_DIR = "RecipeNotes/photos";

	private static File storageDir = new File(Environment.getExternalStorageDirectory(), PICTURES_DIR);
	
	static {
		if (!storageDir.isDirectory()) {
			storageDir.mkdirs();
		}
	}

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.recipe_details);

		helper = new RecipeNotesSQLiteOpenHelper(this);

		nameView = (EditText) findViewById(R.id.name);
		
		ArrayList<String> ingredientsAutoCompleteList = new ArrayList<String>();
		{
			Cursor ingredientsCursor = helper.getReadableDatabase().query(
					INGREDIENTS_TABLE_NAME, 
					new String[]{ BaseColumns._ID, "name", }, 
					null, null, null, null, "name");
			startManagingCursor(ingredientsCursor);

			int i = ingredientsCursor.getColumnIndex("name");
			while (ingredientsCursor.moveToNext()) {
				ingredientsAutoCompleteList.add(ingredientsCursor.getString(i));
			}
		}

		ArrayAdapter<String> ingredientsAutoCompleteAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_dropdown_item_1line, ingredientsAutoCompleteList);
		ingredientView = (MultiAutoCompleteTextView) findViewById(R.id.ingredient);
		ingredientView.setAdapter(ingredientsAutoCompleteAdapter);
		ingredientView.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());

		ArrayList<String> ingredientsList = new ArrayList<String>();
		ingredientsListAdapter = new ArrayAdapter<String>(this,
				R.layout.ingredientslist_item, ingredientsList);
		ingredientsListView = (ListView) findViewById(R.id.ingredients);
		ingredientsListView.setAdapter(ingredientsListAdapter);
		ingredientsListView.setOnItemLongClickListener(new IngredientListItemLongClickListener());

		photoFilenames = new ArrayList<String>();

		Button addIngredientButton = (Button) findViewById(R.id.btn_add_ingredient);
		addIngredientButton.setOnClickListener(new AddIngredientOnClickListener());

		// load recipe data
		recipeId = getIntent().getExtras().getString(BaseColumns._ID);
		if (recipeId == null) {
			recipeId = helper.newRecipe();
		}
		else {
			Cursor recipeCursor = helper.getReadableDatabase().query(
					RECIPES_TABLE_NAME, new String[]{ "name", }, 
					BaseColumns._ID + " = ?", new String[]{ recipeId },
					null, null, null);
			startManagingCursor(recipeCursor);

			if (recipeCursor.moveToNext()) {
				nameView.setText(recipeCursor.getString(0));

				Cursor ingredientsCursor = helper.getReadableDatabase().rawQuery(
						String.format(
								"SELECT i.name FROM %s ri JOIN %s i ON ri.ingredient_id = i.%s WHERE ri.recipe_id = ? ORDER BY i.name",
								RECIPE_INGREDIENTS_TABLE_NAME, INGREDIENTS_TABLE_NAME, BaseColumns._ID
								),
								new String[]{ recipeId }
						);
				startManagingCursor(ingredientsCursor);
				while (ingredientsCursor.moveToNext()) {
					String ingredient = ingredientsCursor.getString(0);
					ingredientsListAdapter.add(ingredient);
				}
				setListViewHeightBasedOnChildren(ingredientsListView);
				
				Cursor photosCursor = helper.getReadableDatabase().rawQuery(
						String.format(
								"SELECT filename FROM %s WHERE recipe_id = ?",
								RECIPE_PHOTOS_TABLE_NAME
								),
								new String[]{ recipeId }
						);
				startManagingCursor(photosCursor);
				while (photosCursor.moveToNext()) {
					String filename = photosCursor.getString(0);
					addPhoto(new File(getPhotoPath(filename)));
				}
			}
			else {
				// TODO should exit with error
			}
		}
		
		Button addPhotoButton = (Button) findViewById(R.id.btn_add_photo);
		addPhotoButton.setOnClickListener(new AddPhotoOnClickListener());

		Button save = (Button) findViewById(R.id.btn_save);
		save.setOnClickListener(new SaveRecipeOnClickListener());
	}

	class SaveRecipeOnClickListener implements OnClickListener {
		@Override
		public void onClick(View view) {
			addIngredients();
			
			ContentValues values = new ContentValues();

			String name = capitalize(nameView.getText().toString());
			values.put("name", name);
			long updatedDt = new Date().getTime();
			values.put("updated_dt", updatedDt);

			// display_name
			String displayName = "";
			for (int i = 0; i < ingredientsListAdapter.getCount(); ++i) {
				displayName += ingredientsListAdapter.getItem(i);
				if (i < ingredientsListAdapter.getCount() - 1) {
					displayName += ", ";
				}
			}
			values.put("display_name", displayName);

			// display_image
			//TODO

			if (recipeId == null) {
				values.put("created_dt", updatedDt);
				long ret = helper.getWritableDatabase().insert(RECIPES_TABLE_NAME, null, values);
				Log.d(TAG, "insert recipe ret = " + ret);
				if (ret >= 0) {
					recipeId = String.valueOf(ret);
					savePhotos();
					Toast.makeText(getApplicationContext(), "Successfully added new recipe", Toast.LENGTH_SHORT).show();
				}
				else {
					Toast.makeText(getApplicationContext(), "Error adding new recipe", Toast.LENGTH_SHORT).show();
				}
			}
			else {
				int ret = helper.getWritableDatabase().update(RECIPES_TABLE_NAME, values, 
						BaseColumns._ID + " = ?", new String[]{ recipeId });
				Log.d(TAG, "update ret = " + ret);
				if (ret == 1) {
					helper.getWritableDatabase().delete(RECIPE_PHOTOS_TABLE_NAME, "recipe_id = ?", new String[]{ recipeId });
					savePhotos();
					Toast.makeText(getApplicationContext(), "Successfully updated recipe", Toast.LENGTH_SHORT).show();
				}
				else {
					Toast.makeText(getApplicationContext(), "Error updating recipe", Toast.LENGTH_SHORT).show();
				}
			}
			finish();
		}
	}

	static String capitalize(String name) {
		if (name == null || name.trim().length() < 1) return name;
		name = name.trim();
		return Character.toUpperCase(name.charAt(0)) + name.substring(1);
	}

	public static void setListViewHeightBasedOnChildren(ListView listView) {
		ListAdapter listAdapter = listView.getAdapter(); 
		if (listAdapter == null) {
			// pre-condition
			return;
		}

		int totalHeight = 0;
		for (int i = 0; i < listAdapter.getCount(); i++) {
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		listView.setLayoutParams(params);
	}
	
	private void addIngredients() {
		String ingredients = ingredientView.getText().toString().trim();
		if (ingredients.length() > 0) {
			for (String ingredient : ingredients.split(",")) {
				ingredient = capitalize(ingredient);
				String ingredientId = helper.getIngredientIdByName(ingredient);
				if (ingredientId != null
						&& helper.addRecipeIngredient(recipeId, ingredientId)) {
					ingredientsListAdapter.insert(ingredient, 0);
				}
			}
			ingredientView.setText("");
			setListViewHeightBasedOnChildren(ingredientsListView);
			Toast.makeText(getApplicationContext(), "Added " + ingredients, Toast.LENGTH_SHORT).show();
		}
	}

	class AddIngredientOnClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			addIngredients();
		}
	}

	private void savePhotos() {
		for (String filename : photoFilenames) {
			ContentValues values = new ContentValues();
			values.put("recipe_id", recipeId);
			values.put("filename", filename);
			long ret = helper.getWritableDatabase().insert(RECIPE_PHOTOS_TABLE_NAME, null, values);
			Log.d(TAG, "insert recipe photo ret = " + ret);
		}
		for (String path : photosToDelete) {
			File photoFile = new File(path);
			if (photoFile.isFile()) {
				photoFile.delete();
			}
		}
	}

	class IngredientListItemLongClickListener implements OnItemLongClickListener {
		@Override
		public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
				int arg2, long arg3) {
			String name = ingredientsListAdapter.getItem(arg2);
			String ingredientId = helper.getIngredientIdByName(name);
			if (ingredientId != null
					&& helper.removeRecipeIngredient(recipeId, ingredientId)) {
				ingredientsListAdapter.remove(name);
				setListViewHeightBasedOnChildren(ingredientsListView);
			}
			return true;
		}
	}

	private File photoFile;
	
	private String getPhotoPath(String filename) {
		return String.format("%s/%s", storageDir, filename);
	}

	private void dispatchTakePictureIntent() {
		try {
			photoFile = File.createTempFile(String.format(
					"recipe_%s_%d_", recipeId, photoFilenames.size()+1),
					".jpg", storageDir);
		} catch (IOException e) {
			e.printStackTrace();
			photoFile = null; // TODO better way to handle it?
		}
		Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
		startActivityForResult(takePictureIntent, PICTURE_TAKEN);
	}

	public static boolean isIntentAvailable(Context context, String action) {
		final PackageManager packageManager = context.getPackageManager();
		final Intent intent = new Intent(action);
		List<ResolveInfo> list =
				packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
		return list.size() > 0;
	}

	private void handleSmallCameraPhoto(Intent intent) {
		if (photoFile != null) {
			addPhoto(photoFile);
		}
	}

	private void addPhoto(File file) {
		String path = file.getAbsolutePath();
		Bitmap bitmap = BitmapFactory.decodeFile(path);
		ImageView photoView = new ImageView(this);
		photoView.setImageBitmap(bitmap);
		photoView.setPadding(10, 10, 10, 10);
		photoView.setTag(file.getName());
		photoView.setOnLongClickListener(new PhotoLongClickListener(file));
		
		// dirty hack for motorola
		int targetHeight = getWindowManager().getDefaultDisplay().getWidth() * bitmap.getHeight() / bitmap.getWidth();
		Log.d(TAG, "targetHeight = " + targetHeight);
		LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		photoView.setLayoutParams(params);
		photoView.getLayoutParams().height = targetHeight;
		
		LinearLayout layout = (LinearLayout) findViewById(R.id.photos);
		layout.addView(photoView);
		photoFilenames.add(file.getName());
	}

	class AddPhotoOnClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			dispatchTakePictureIntent();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case PICTURE_TAKEN:
				handleSmallCameraPhoto(data);
				break;
			}
		}
	}
	
	class PhotoLongClickListener implements OnLongClickListener {
		
		private final String path;
		private final String filename;

		public PhotoLongClickListener(File file) {
			this.path = file.getAbsolutePath();
			this.filename = file.getName();
		}
		
		private void deletePhoto() {
			photoFilenames.remove(filename);
			LinearLayout layout = (LinearLayout) findViewById(R.id.photos);
			layout.removeView(layout.findViewWithTag(filename));
			photosToDelete.add(path);
		}

		@Override
		public boolean onLongClick(View arg0) {
			new AlertDialog.Builder(RecipeDetailsActivity.this)
			.setMessage(R.string.confirm_are_you_sure)
			.setCancelable(true)
			.setTitle(R.string.title_delete_photo)
			.setPositiveButton(R.string.btn_yes, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					deletePhoto();
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
	protected void onDestroy() {
		Log.d(TAG, "++onDestroy");
		super.onDestroy();
		helper.close();
	}
}