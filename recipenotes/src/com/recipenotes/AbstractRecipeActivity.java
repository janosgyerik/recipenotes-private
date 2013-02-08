package com.recipenotes;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.util.Log;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public abstract class AbstractRecipeActivity extends Activity {

	private static final String TAG = "AbstractRecipeActivity";

	protected RecipeNotesSQLiteOpenHelper helper;
	protected String recipeId;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		helper = new RecipeNotesSQLiteOpenHelper(this);

		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			recipeId = extras.getString(BaseColumns._ID);
		}
	}

	private void clearPhotosFromLayout() {
		LinearLayout layout = (LinearLayout) findViewById(R.id.photos);
		layout.removeAllViews();
	}

	protected void addPhotoToLayout(String photoFilename, boolean editable) {
		int appWidth = getWindowManager().getDefaultDisplay().getWidth();
		Bitmap bitmap = RecipeFileManager.getMediumPhotoBitmap(photoFilename, appWidth);
		if (bitmap != null) {
			ImageView photoView = new ImageView(this);
			photoView.setImageBitmap(bitmap);
			photoView.setPadding(2, 2, 2, 2);
			photoView.setTag(photoFilename);
			if (editable) {
				photoView.setOnLongClickListener(new PhotoOnLongClickListener(photoFilename));
			}

			// dirty hack for motorola
			int targetHeight = appWidth * bitmap.getHeight() / bitmap.getWidth();
			LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			photoView.setLayoutParams(params);
			photoView.getLayoutParams().height = targetHeight;

			LinearLayout layout = (LinearLayout) findViewById(R.id.photos);
			layout.addView(photoView);
		}
	}

	protected void reloadAndRefreshRecipeDetails(boolean editable) {
		if (recipeId != null) {
			Cursor recipeCursor = helper.getRecipeDetailsCursor(recipeId);
			if (recipeCursor.moveToNext()) {
				TextView nameView = (TextView) findViewById(R.id.name);
				String name = recipeCursor.getString(0);
				if (name != null && name.length() > 0) {
					nameView.setText(name);
					EditText nameEditView = (EditText) findViewById(R.id.name_edit);
					if (nameEditView != null) {
						nameEditView.setText(name);
					}
				}
				else {
					nameView.setText(R.string.title_noname_recipe);
				}
				
				String memo = recipeCursor.getString(1);
				TextView memoView = (TextView) findViewById(R.id.memo);
				memoView.setText(memo);
				View memoLabel = findViewById(R.id.memo_label);
				if (memo != null && memo.length() > 0) {
					memoLabel.setVisibility(View.VISIBLE);
					memoView.setVisibility(View.VISIBLE);
				}
				else if (editable) {
					memoLabel.setVisibility(View.VISIBLE);
					memoView.setVisibility(View.VISIBLE);
				}
				else {
					memoLabel.setVisibility(View.GONE);
					memoView.setVisibility(View.GONE);
				}
				
				Cursor ingredientsCursor = helper.getRecipeIngredientsCursor(recipeId);
				StringBuffer ingredientsBuffer = new StringBuffer();
				while (ingredientsCursor.moveToNext()) {
					String ingredient = ingredientsCursor.getString(0);
					ingredientsBuffer.append(ingredient);
					ingredientsBuffer.append(", ");
				}
				ingredientsCursor.close();

				View ingredientsLabel = findViewById(R.id.ingredients_label);
				TextView ingredientsView = (TextView) findViewById(R.id.ingredients);
				if (ingredientsBuffer.length() > 2) {
					ingredientsView.setText(ingredientsBuffer.substring(0, ingredientsBuffer.length() - 2));
					ingredientsView.setVisibility(View.VISIBLE);
					ingredientsLabel.setVisibility(View.VISIBLE);
				}
				else if (editable) {
					ingredientsView.setText(R.string.label_none);
					ingredientsView.setVisibility(View.VISIBLE);
					ingredientsLabel.setVisibility(View.VISIBLE);
				}
				else {
					ingredientsView.setVisibility(View.GONE);
					ingredientsLabel.setVisibility(View.GONE);
				}

				Cursor tagsCursor = helper.getRecipeTagsCursor(recipeId);
				StringBuffer tagsBuffer = new StringBuffer();
				while (tagsCursor.moveToNext()) {
					String tag = tagsCursor.getString(0);
					tagsBuffer.append(tag);
					tagsBuffer.append(", ");
				}
				tagsCursor.close();

				View tagsLabel = findViewById(R.id.tags_label);
				TextView tagsView = (TextView) findViewById(R.id.tags);
				if (tagsBuffer.length() > 2) {
					tagsView.setText(tagsBuffer.substring(0, tagsBuffer.length() - 2));
					tagsView.setVisibility(View.VISIBLE);
					tagsLabel.setVisibility(View.VISIBLE);
				}
				else if (editable) {
					tagsView.setText(R.string.label_none);
					tagsView.setVisibility(View.VISIBLE);
					tagsLabel.setVisibility(View.VISIBLE);
				}
				else {
					tagsView.setVisibility(View.GONE);
					tagsLabel.setVisibility(View.GONE);
				}

				Cursor photosCursor = helper.getRecipePhotosCursor(recipeId);
				clearPhotosFromLayout();
				while (photosCursor.moveToNext()) {
					String filename = photosCursor.getString(0);
					addPhotoToLayout(filename, editable);
				}
				photosCursor.close();
			}
			else {
				// TODO
			}
			recipeCursor.close();
		}
	}

	private void removePhoto(String photoFilename) {
		if (removePhotoFromRecipe(photoFilename)) {
			RecipeFileManager.deletePhotos(photoFilename);
			removePhotoFromLayout(photoFilename);
		}
	}

	private void removePhotoFromLayout(String photoFilename) {
		LinearLayout layout = (LinearLayout) findViewById(R.id.photos);
		layout.removeView(layout.findViewWithTag(photoFilename));
	}

	private boolean removePhotoFromRecipe(String photoFilename) {
		return helper.removeRecipePhoto(recipeId, photoFilename);
	}

	class PhotoOnLongClickListener implements OnLongClickListener {
		private final String photoFilename;

		public PhotoOnLongClickListener(String photoFilename) {
			this.photoFilename = photoFilename;
		}

		@Override
		public boolean onLongClick(View arg0) {
			new AlertDialog.Builder(AbstractRecipeActivity.this)
			.setMessage(R.string.confirm_are_you_sure)
			.setCancelable(true)
			.setTitle(R.string.title_delete_photo)
			.setPositiveButton(R.string.btn_yes, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					removePhoto(photoFilename);
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