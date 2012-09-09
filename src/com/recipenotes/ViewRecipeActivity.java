package com.recipenotes;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ViewRecipeActivity extends Activity {

	private static final String TAG = "ViewRecipeActivity";

	protected static final int RETURN_FROM_EDIT = 1;

	private RecipeNotesSQLiteOpenHelper helper;
	private String recipeId;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.viewrecipe);

		helper = new RecipeNotesSQLiteOpenHelper(this);

		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			recipeId = extras.getString(BaseColumns._ID);
		}
		
		updateRecipeView();
	}

	private void addPhotoToLayout(File photoFile) {
		if (photoFile.isFile()) {
			String path = photoFile.getAbsolutePath();
			Bitmap bitmap = BitmapFactory.decodeFile(path);
			ImageView photoView = new ImageView(this);
			photoView.setImageBitmap(bitmap);
			photoView.setPadding(10, 10, 10, 10);
			photoView.setTag(photoFile.getName());

			// dirty hack for motorola
			int targetHeight = getWindowManager().getDefaultDisplay().getWidth() * bitmap.getHeight() / bitmap.getWidth();
			Log.d(TAG, "targetHeight = " + targetHeight);
			LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			photoView.setLayoutParams(params);
			photoView.getLayoutParams().height = targetHeight;

			LinearLayout layout = (LinearLayout) findViewById(R.id.photos);
			layout.addView(photoView);
		}
	}

	private void updateRecipeView() {
		if (recipeId != null) {
			Cursor recipeCursor = helper.getRecipeDetailsCursor(recipeId);
			startManagingCursor(recipeCursor);

			if (recipeCursor.moveToNext()) {
				TextView nameView = (TextView) findViewById(R.id.name);
				String name = recipeCursor.getString(0);
				if (name != null && name.length() > 0) {
					nameView.setText(name);
				}
				else {
					nameView.setText(R.string.title_noname_recipe);
				}

				Cursor ingredientsCursor = helper.getRecipeIngredientsCursor(recipeId);
				startManagingCursor(ingredientsCursor);
				StringBuffer ingredientsBuffer = new StringBuffer();
				while (ingredientsCursor.moveToNext()) {
					String ingredient = ingredientsCursor.getString(0);
					ingredientsBuffer.append(ingredient);
					ingredientsBuffer.append(", ");
				}
				TextView ingredientsView = (TextView) findViewById(R.id.ingredients);
				if (ingredientsBuffer.length() > 2) {
					ingredientsView.setText(ingredientsBuffer.substring(0, ingredientsBuffer.length() - 2));
				}
				else {
					ingredientsView.setText(R.string.label_none);
				}

				Cursor tagsCursor = helper.getRecipeTagsCursor(recipeId);
				startManagingCursor(tagsCursor);
				StringBuffer tagsBuffer = new StringBuffer();
				while (tagsCursor.moveToNext()) {
					String tag = tagsCursor.getString(0);
					tagsBuffer.append(tag);
					tagsBuffer.append(", ");
				}
				TextView tagsView = (TextView) findViewById(R.id.tags);
				if (tagsBuffer.length() > 2) {
					tagsView.setText(tagsBuffer.substring(0, tagsBuffer.length() - 2));
				}
				else {
					tagsView.setText(R.string.label_none);
				}

				Cursor photosCursor = helper.getRecipePhotosCursor(recipeId);
				startManagingCursor(photosCursor);
				while (photosCursor.moveToNext()) {
					String filename = photosCursor.getString(0);
					addPhotoToLayout(RecipeFileManager.getPhotoFile(filename));
				}

				Button editButton = (Button) findViewById(R.id.btn_edit);
				editButton.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent intent = new Intent(ViewRecipeActivity.this, EditRecipeActivity.class);
						intent.putExtra(BaseColumns._ID, recipeId);
						startActivityForResult(intent, RETURN_FROM_EDIT);
					}
				});
			}
			else {
				// TODO
			}
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
			switch (requestCode) {
			case RETURN_FROM_EDIT:
				updateRecipeView();
				break;
		}
	}

	@Override  
	protected void onDestroy() {
		Log.d(TAG, "++onDestroy");
		super.onDestroy();
		helper.close();
	}
}