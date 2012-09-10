package com.recipenotes;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
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

public class EditRecipeActivity extends Activity {

	private static final String TAG = "EditRecipeActivity";

	private static final int PICTURE_TAKEN = 1;

	private String recipeId;
	private RecipeNotesSQLiteOpenHelper helper;

	private EditText nameView;

	private MultiAutoCompleteTextView ingredientView;
	private ArrayAdapter<String> ingredientsListAdapter;
	private ListView ingredientsListView;

	private MultiAutoCompleteTextView tagView;
	private ArrayAdapter<String> tagsListAdapter;
	private ListView tagsListView;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.editrecipe);

		helper = new RecipeNotesSQLiteOpenHelper(this);

		nameView = (EditText) findViewById(R.id.name);


		// add ingredient
		// TODO store id too
		ArrayList<String> ingredientsAutoCompleteList = new ArrayList<String>();
		{
			Cursor ingredientsCursor = helper.getIngredientsListCursor();
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
		ingredientsListView.setOnItemLongClickListener(new IngredientListOnItemLongClickListener());

		Button addIngredientButton = (Button) findViewById(R.id.btn_add_ingredient);
		addIngredientButton.setOnClickListener(new AddIngredientOnClickListener());


		// add tag

		ArrayList<String> tagsAutoCompleteList = new ArrayList<String>();
		{
			Cursor tagsCursor = helper.getTagsListCursor();
			startManagingCursor(tagsCursor);

			int i = tagsCursor.getColumnIndex("name");
			while (tagsCursor.moveToNext()) {
				tagsAutoCompleteList.add(tagsCursor.getString(i));
			}
		}

		ArrayAdapter<String> tagsAutoCompleteAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_dropdown_item_1line, tagsAutoCompleteList);
		tagView = (MultiAutoCompleteTextView) findViewById(R.id.tag);
		tagView.setAdapter(tagsAutoCompleteAdapter);
		tagView.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());

		ArrayList<String> tagsList = new ArrayList<String>();
		tagsListAdapter = new ArrayAdapter<String>(this,
				R.layout.tagslist_item, tagsList);
		tagsListView = (ListView) findViewById(R.id.tags);
		tagsListView.setAdapter(tagsListAdapter);
		tagsListView.setOnItemLongClickListener(new TagListOnItemLongClickListener());

		Button addTagButton = (Button) findViewById(R.id.btn_add_tag);
		addTagButton.setOnClickListener(new AddTagOnClickListener());


		// load recipe data
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			recipeId = extras.getString(BaseColumns._ID);
		}
		if (recipeId == null) {
			recipeId = helper.newRecipe();
		}
		else {
			Cursor recipeCursor = helper.getRecipeDetailsCursor(recipeId);
			startManagingCursor(recipeCursor);

			if (recipeCursor.moveToNext()) {
				nameView.setText(recipeCursor.getString(0));

				Cursor ingredientsCursor = helper.getRecipeIngredientsCursor(recipeId);
				startManagingCursor(ingredientsCursor);
				while (ingredientsCursor.moveToNext()) {
					String ingredient = ingredientsCursor.getString(0);
					ingredientsListAdapter.add(ingredient);
				}
				setListViewHeightBasedOnChildren(ingredientsListView);

				Cursor tagsCursor = helper.getRecipeTagsCursor(recipeId);
				startManagingCursor(tagsCursor);
				while (tagsCursor.moveToNext()) {
					String tag = tagsCursor.getString(0);
					tagsListAdapter.add(tag);
				}
				setListViewHeightBasedOnChildren(tagsListView);

				Cursor photosCursor = helper.getRecipePhotosCursor(recipeId);
				startManagingCursor(photosCursor);
				while (photosCursor.moveToNext()) {
					String filename = photosCursor.getString(0);
					addPhotoToLayout(RecipeFileManager.getPhotoFile(filename));
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

			String name = capitalize(nameView.getText().toString());

			// TODO
			// display_name
			String displayName = "";
			for (int i = 0; i < ingredientsListAdapter.getCount(); ++i) {
				displayName += ingredientsListAdapter.getItem(i);
				if (i < ingredientsListAdapter.getCount() - 1) {
					displayName += ", ";
				}
			}

			if (helper.saveRecipe(recipeId, name, displayName)) {
				Toast.makeText(getApplicationContext(), R.string.msg_updated_recipe, Toast.LENGTH_SHORT).show();
				finish();
			}
			else {
				Toast.makeText(getApplicationContext(), R.string.error_update_recipe, Toast.LENGTH_SHORT).show();
			}
		}
	}

	static String capitalize(String name) {
		if (name == null || name.trim().length() < 1) return name;
		name = name.trim();
		return Character.toUpperCase(name.charAt(0)) + name.substring(1);
	}

	public static void setListViewHeightBasedOnChildren(ListView listView) {
		ListAdapter listAdapter = listView.getAdapter();
		/*
		if (listAdapter == null) {
			// pre-condition
			return;
		}
		 */

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
			StringBuffer ingredientsListMsgBuffer = new StringBuffer();
			for (String ingredient : ingredients.split(",")) {
				ingredient = capitalize(ingredient);
				ingredientsListMsgBuffer.append(ingredient);
				ingredientsListMsgBuffer.append(", ");
				String ingredientId = helper.getOrCreateIngredient(ingredient);
				if (ingredientId != null
						&& helper.addRecipeIngredient(recipeId, ingredientId)) {
					ingredientsListAdapter.insert(ingredient, 0);
				}
			}
			ingredientView.setText("");
			setListViewHeightBasedOnChildren(ingredientsListView);
			String ingredientsListMsg = ingredientsListMsgBuffer.substring(0, ingredientsListMsgBuffer.lastIndexOf(","));
			Toast.makeText(getApplicationContext(), "Added " + ingredientsListMsg, Toast.LENGTH_SHORT).show();
		}
	}

	private void removeIngredient(String ingredientName) {
		String ingredientId = helper.getIngredientIdByName(ingredientName);
		if (ingredientId != null
				&& helper.removeRecipeIngredient(recipeId, ingredientId)) {
			ingredientsListAdapter.remove(ingredientName);
			setListViewHeightBasedOnChildren(ingredientsListView);
		}
	}

	class AddIngredientOnClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			addIngredients();
		}
	}

	class IngredientListOnItemLongClickListener implements OnItemLongClickListener {
		@Override
		public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
				int arg2, long arg3) {
			String ingredientName = ingredientsListAdapter.getItem(arg2);
			removeIngredient(ingredientName);
			return true;
		}
	}

	private void addTags() {
		String tags = tagView.getText().toString().trim();
		if (tags.length() > 0) {
			StringBuffer tagsListMsgBuffer = new StringBuffer();
			for (String tag : tags.split(",")) {
				tag = capitalize(tag);
				tagsListMsgBuffer.append(tag);
				tagsListMsgBuffer.append(", ");
				String tagId = helper.getOrCreateTag(tag);
				if (tagId != null
						&& helper.addRecipeTag(recipeId, tagId)) {
					tagsListAdapter.insert(tag, 0);
				}
			}
			tagView.setText("");
			setListViewHeightBasedOnChildren(tagsListView);
			String tagsListMsg = tagsListMsgBuffer.substring(0, tagsListMsgBuffer.lastIndexOf(","));
			Toast.makeText(getApplicationContext(), "Added " + tagsListMsg, Toast.LENGTH_SHORT).show();
		}
	}

	private void removeTag(String tagName) {
		String tagId = helper.getTagIdByName(tagName);
		if (tagId != null
				&& helper.removeRecipeTag(recipeId, tagId)) {
			tagsListAdapter.remove(tagName);
			setListViewHeightBasedOnChildren(tagsListView);
		}
	}

	class AddTagOnClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			addTags();
		}
	}

	class TagListOnItemLongClickListener implements OnItemLongClickListener {
		@Override
		public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
				int arg2, long arg3) {
			String tagName = tagsListAdapter.getItem(arg2);
			removeTag(tagName);
			return true;
		}
	}

	private void addPhotoToRecipe(File photoFile) {
		helper.addRecipePhoto(recipeId, photoFile.getName());
	}

	private void addPhotoToLayout(File photoFile) {
		if (photoFile.isFile()) {
			int appWidth = getWindowManager().getDefaultDisplay().getWidth();
			Bitmap bitmap = BitmapTools.createScaledBitmap(photoFile, appWidth);
			ImageView photoView = new ImageView(this);
			photoView.setImageBitmap(bitmap);
			photoView.setPadding(10, 10, 10, 10);
			photoView.setTag(photoFile.getName());

			// dirty hack for motorola
			int targetHeight = appWidth * bitmap.getHeight() / bitmap.getWidth();
			LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			photoView.setLayoutParams(params);
			photoView.getLayoutParams().height = targetHeight;

			LinearLayout layout = (LinearLayout) findViewById(R.id.photos);
			layout.addView(photoView);
		}
	}

	private void removePhoto(File photoFile) {
		if (photoFile.delete()) {
			if (removePhotoFromRecipe(photoFile)) {
				removePhotoFromLayout(photoFile);
			}
		}
	}

	private boolean removePhotoFromRecipe(File photoFile) {
		return helper.removeRecipePhoto(recipeId, photoFile.getName());
	}

	private void removePhotoFromLayout(File photoFile) {
		LinearLayout layout = (LinearLayout) findViewById(R.id.photos);
		layout.removeView(layout.findViewWithTag(photoFile.getName()));
	}

	private File photoFile;

	class AddPhotoOnClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			dispatchTakePictureIntent();
		}
	}

	private void dispatchTakePictureIntent() {
		try {
			photoFile = RecipeFileManager.newPhotoFile(recipeId);
		} catch (IOException e) {
			e.printStackTrace();
			photoFile = null;
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
		if (photoFile != null && photoFile.isFile()) {
			addPhotoToRecipe(photoFile);
			addPhotoToLayout(photoFile);
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
		else if (photoFile != null && photoFile.isFile()) {
			photoFile.delete();
		}
	}

	class PhotoOnLongClickListener implements OnLongClickListener {

		private final File photoFile;

		public PhotoOnLongClickListener(File photoFile) {
			this.photoFile = photoFile;
		}

		@Override
		public boolean onLongClick(View arg0) {
			new AlertDialog.Builder(EditRecipeActivity.this)
			.setMessage(R.string.confirm_are_you_sure)
			.setCancelable(true)
			.setTitle(R.string.title_delete_photo)
			.setPositiveButton(R.string.btn_yes, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					removePhoto(photoFile);
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