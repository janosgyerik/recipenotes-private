package com.recipenotes;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.BaseColumns;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

public class RecipeDetailsActivity extends Activity {

	// Debugging
	private static final String TAG = "RecipeDetailsActivity";

	private static final String RECIPES_TABLE_NAME = "main_recipe";
	private static final String INGREDIENTS_TABLE_NAME = "main_ingredient";
	private static final String RECIPE_INGREDIENTS_TABLE_NAME = "main_recipeingredient";

	private static final int PICTURE_TAKEN = 1;

	private String recipeId;
	private SQLiteOpenHelper helper;

	private EditText nameView;

	private AutoCompleteTextView ingredientView;

	private ArrayAdapter<String> ingredientsListAdapter;
	private ListView ingredientsListView;

	private String mCurrentPhotoPath; 

	private static String PICTURES_DIR = "RecipeNotes";

	private static File storageDir = new File (
			String.format("%s/%s",
					Environment.getExternalStorageDirectory(),
					PICTURES_DIR
					));
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
		Log.d(TAG, "ENV: " + storageDir);

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
		ingredientView = (AutoCompleteTextView) findViewById(R.id.ingredient);
		ingredientView.setAdapter(ingredientsAutoCompleteAdapter);

		//TODO: load from database
		ArrayList<String> ingredientsList = new ArrayList<String>();
		ingredientsListAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, ingredientsList);
		ingredientsListView = (ListView) findViewById(R.id.ingredients);
		ingredientsListView.setAdapter(ingredientsListAdapter);
		ingredientsListView.setOnItemLongClickListener(new IngredientListItemLongClickListener());

		Button addIngredientButton = (Button) findViewById(R.id.btn_add_ingredient);
		addIngredientButton.setOnClickListener(new AddIngredientOnClickListener());

		Button addPhotoButton = (Button) findViewById(R.id.btn_add_photo);
		addPhotoButton.setOnClickListener(new AddPhotoOnClickListener());

		recipeId = getIntent().getExtras().getString(BaseColumns._ID);
		if (recipeId != null) {
			Cursor recipeCursor = helper.getReadableDatabase().query(
					RECIPES_TABLE_NAME, new String[]{ "name", }, 
					BaseColumns._ID + " = ?", new String[]{ recipeId },
					null, null, null);
			startManagingCursor(recipeCursor);

			if (recipeCursor.moveToNext()) {
				nameView.setText(recipeCursor.getString(0));

				Cursor ingredientsCursor = helper.getReadableDatabase().rawQuery(
						String.format(
								"SELECT i.name FROM %s ri JOIN %s i ON ri.ingredient_id = i.%s WHERE ri.recipe_id = ?",
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
			}
			else {
				// TODO should exit with error
			}
		}

		Button save = (Button) findViewById(R.id.btn_save);
		save.setOnClickListener(new SaveRecipeOnClickListener());
	}

	void setSpinnerValue(Spinner spinner, String value, String[] choices) {
		int position = 0;
		for (String choice : choices) {
			if (choice.equals(value)) {
				spinner.setSelection(position);
				return;
			}
			++position;
		}
	}

	class SaveRecipeOnClickListener implements OnClickListener {
		@Override
		public void onClick(View view) {
			ContentValues values = new ContentValues();

			String name = capitalize(nameView.getText().toString());
			values.put("name", name);

			// display_name
			String displayName;
			if (name.length() > 0) {
				displayName = name + " with ";
			}
			else {
				displayName = "";
			}
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
				long ret = helper.getWritableDatabase().insert(RECIPES_TABLE_NAME, null, values);
				Log.d(TAG, "insert recipe ret = " + ret);
				if (ret >= 0) {
					recipeId = String.valueOf(ret);
					saveIngredients();
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
					helper.getWritableDatabase().delete(RECIPE_INGREDIENTS_TABLE_NAME, "recipe_id = ?", new String[]{ recipeId });
					saveIngredients();
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

	class AddIngredientOnClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			String ingredient = capitalize(ingredientView.getText().toString());
			if (ingredient.length() > 0) {
				ingredientsListAdapter.add(ingredient);
				ingredientView.setText("");
				setListViewHeightBasedOnChildren(ingredientsListView);
				Toast.makeText(getApplicationContext(), "Added " + ingredient, Toast.LENGTH_SHORT).show();
			}
		}
	}

	private void saveIngredients() {
		for (int i = 0; i < ingredientsListAdapter.getCount(); ++i) {
			String ingredient = ingredientsListAdapter.getItem(i);
			Cursor ingredientIdCursor =
					helper.getReadableDatabase().query(INGREDIENTS_TABLE_NAME,
							new String[]{ BaseColumns._ID, },
							"name = ?",
							new String[]{ ingredient, },
							null, null, null, "1");
			startManagingCursor(ingredientIdCursor);

			String ingredientId;
			if (ingredientIdCursor.moveToNext()) {
				ingredientId = ingredientIdCursor.getString(0);
				Log.d(TAG, "got ingredientId = " + ingredientId);
			}
			else {
				ContentValues values = new ContentValues();
				values.put("name", ingredient);
				long ret = helper.getWritableDatabase().insert(INGREDIENTS_TABLE_NAME, null, values);
				Log.d(TAG, "insert ingredient ret = " + ret);
				ingredientId = String.valueOf(ret);
			}
			ContentValues values = new ContentValues();
			values.put("recipe_id", recipeId);
			values.put("ingredient_id", ingredientId);
			long ret = helper.getWritableDatabase().insert(RECIPE_INGREDIENTS_TABLE_NAME, null, values);
			Log.d(TAG, "insert recipe ingredient ret = " + ret);
		}
	}

	class IngredientListItemLongClickListener implements OnItemLongClickListener {
		@Override
		public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
				int arg2, long arg3) {
			ingredientsListAdapter.remove(ingredientsListAdapter.getItem(arg2));
			setListViewHeightBasedOnChildren(ingredientsListView);
			return true;
		}
	}

	private void dispatchTakePictureIntent() {
		/*
		File f;
		try {
			f = createImageFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
		*/
		Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		//takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
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
		Bundle extras = intent.getExtras();
		Bitmap bitmap = (Bitmap) extras.get("data");
		ImageView photo = new ImageView(this);
		photo.setImageBitmap(bitmap);
		LinearLayout layout = (LinearLayout) findViewById(R.id.photos);
		layout.addView(photo);
	}

	private File createImageFile() throws IOException {
		// Create an image file name
		String timeStamp = 
				new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
		String JPEG_FILE_PREFIX = "photo_";
		String imageFileName = JPEG_FILE_PREFIX + timeStamp + "_";
		String JPEG_FILE_SUFFIX = ".jpg";
		File image = File.createTempFile(
				imageFileName, 
				JPEG_FILE_SUFFIX 
				);
		mCurrentPhotoPath = image.getAbsolutePath();
		return image;
	}

	class AddPhotoOnClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			dispatchTakePictureIntent();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case PICTURE_TAKEN:
			handleSmallCameraPhoto(data);
			break;
		}
	}

	
}
