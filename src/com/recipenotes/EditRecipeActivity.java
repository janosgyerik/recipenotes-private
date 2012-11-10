package com.recipenotes;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class EditRecipeActivity extends AbstractRecipeActivity {

	private static final String TAG = "EditRecipeActivity";

	private static final int RETURN_FROM_EDIT_INGREDIENTS = 1;
	private static final int RETURN_FROM_EDIT_TAGS = 2;
	private static final int RETURN_FROM_ADD_PHOTO = 3;

	private static final String PHOTO_INFO_FILE = "photoInfo.bin";

	private EditText nameView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.editrecipe);

		// for debugging:
		//		recipeId = "42"; // rich
		//		recipeId = "9999"; // non-existent
		//		 recipeId = "44"; // lean

		if (recipeId == null) {
			PhotoInfo info = loadPhotoInfo();
			if (info != null) {
				recipeId = info.recipeId;
				photoFile = info.photoFile;
			}
		}
		
		if (recipeId == null) {
			recipeId = helper.newRecipe();
		}

		nameView = (EditText) findViewById(R.id.name_edit);

		final Activity this_ = this;

		View editIngredientsButton = (View) findViewById(R.id.btn_edit_ingredients);
		editIngredientsButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(this_, EditIngredientsActivity.class);
				intent.putExtra(BaseColumns._ID, recipeId);
				startActivityForResult(intent, RETURN_FROM_EDIT_INGREDIENTS);
			}
		});

		View editTagsButton = (View) findViewById(R.id.btn_edit_tags);
		editTagsButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(this_, EditTagsActivity.class);
				intent.putExtra(BaseColumns._ID, recipeId);
				startActivityForResult(intent, RETURN_FROM_EDIT_TAGS);
			}
		});

		View addPhotoButton = (View) findViewById(R.id.btn_add_photo);
		addPhotoButton.setOnClickListener(new AddPhotoOnClickListener());

		Button save = (Button) findViewById(R.id.btn_save);
		save.setOnClickListener(new SaveRecipeOnClickListener());

		reloadAndRefreshRecipeDetails(true);
	}

	class SaveRecipeOnClickListener implements OnClickListener {
		@Override
		public void onClick(View view) {
			String name = capitalize(nameView.getText().toString());

			// display_name
			String displayName = "";
			Cursor ingredientsCursor = helper.getRecipeIngredientsCursor(recipeId);
			if (ingredientsCursor.moveToNext()) {
				String ingredient = ingredientsCursor.getString(0);
				displayName = ingredient;
				while (ingredientsCursor.moveToNext()) {
					displayName += ", ";
					ingredient = ingredientsCursor.getString(0);
					displayName += ingredient;
				}
			}
			ingredientsCursor.close();

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

	private void handleReturnFromEditIngredients(Intent data) {
		Bundle extras = data.getExtras();
		if (extras != null) {
			boolean isChanged = extras.getBoolean(AbstractEditRecipeItemsActivity.OUT_CHANGED);
			if (isChanged) {
				Log.i(TAG, "ingredients have changed -> reloading details");
				reloadAndRefreshRecipeDetails(true);
				return;
			}
		}
		Log.i(TAG, "ingredients have NOT changed -> NOT reloading details");
	}

	private void handleReturnFromEditTags(Intent data) {
		Bundle extras = data.getExtras();
		if (extras != null) {
			boolean isChanged = extras.getBoolean(AbstractEditRecipeItemsActivity.OUT_CHANGED);
			if (isChanged) {
				Log.i(TAG, "tags have changed -> reloading details");
				reloadAndRefreshRecipeDetails(true);
				return;
			}
		}
		Log.i(TAG, "tags have NOT changed -> NOT reloading details");
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.i(TAG, "onActivityResult");
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case RETURN_FROM_EDIT_INGREDIENTS:
				Log.i(TAG, "OK edit ingredients");
				handleReturnFromEditIngredients(data);
				break;
			case RETURN_FROM_EDIT_TAGS:
				Log.i(TAG, "OK edit tags");
				handleReturnFromEditTags(data);
				break;
			case RETURN_FROM_ADD_PHOTO:
				Log.i(TAG, "OK take photo");
				handleSmallCameraPhoto();
				break;
			default:
				Log.i(TAG, "OK ???");
			}
		}
		else {
			switch (requestCode) {
			case RETURN_FROM_EDIT_INGREDIENTS:
				Log.i(TAG, "CANCEL edit ingredients");
				break;
			case RETURN_FROM_EDIT_TAGS:
				Log.i(TAG, "CANCEL edit tags");
				break;
			case RETURN_FROM_ADD_PHOTO:
				Log.i(TAG, "CANCEL add photo");
				if (photoFile != null && photoFile.isFile()) {
					photoFile.delete();
				}
				break;
			default:
				Log.i(TAG, "CANCEL ???");
			}
		}
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
		if (photoFile != null) {
			savePhotoInfo();
			Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
			startActivityForResult(takePictureIntent, RETURN_FROM_ADD_PHOTO);
		}
		else {
			new AlertDialog.Builder(this)
			.setTitle(R.string.title_unexpected_error)
			.setMessage(R.string.error_allocating_photo_file)
			.setCancelable(true)
			.setPositiveButton(R.string.btn_ok, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					dialog.cancel();
				}
			})
			.show();
		}
	}

	public static boolean isIntentAvailable(Context context, String action) {
		final PackageManager packageManager = context.getPackageManager();
		final Intent intent = new Intent(action);
		List<ResolveInfo> list =
				packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
		return list.size() > 0;
	}

	private void addPhotoToRecipe(File photoFile) {
		helper.addRecipePhoto(recipeId, photoFile.getName());
	}

	private void handleSmallCameraPhoto() {
		if (photoFile != null && photoFile.isFile()) {
			deletePhotoInfo();
			Log.d(TAG, "adding photo: " + photoFile);
			addPhotoToRecipe(photoFile);
			addPhotoToLayout(photoFile, true);
		}
		else {
			Log.e(TAG, "something's wrong with the photo file: " + photoFile);
		}
	}

	private void savePhotoInfo() {
		try {
			FileOutputStream fos = openFileOutput(PHOTO_INFO_FILE, Context.MODE_PRIVATE);
			PhotoInfo info = new PhotoInfo();
			info.recipeId = recipeId;
			info.photoFile = photoFile;
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			oos.writeObject(info);
			fos.write(baos.toByteArray());
			fos.close();
		}
		catch (Exception e) {
			Log.e(TAG, "Could not create photo info file!");
			e.printStackTrace();
		}
	}

	private void deletePhotoInfo() {
		deleteFile(PHOTO_INFO_FILE);
	}

	private PhotoInfo loadPhotoInfo() {
		try {
			FileInputStream fis = openFileInput(PHOTO_INFO_FILE);
			Log.w(TAG, "Loading photo info file, the app must have crashed earlier...");
			ObjectInputStream ois = new ObjectInputStream(fis);
			PhotoInfo info = (PhotoInfo)ois.readObject();
			Log.i(TAG, "read recipeId = " + info.recipeId);
			Log.i(TAG, "read photoFile = " + info.photoFile);
			return info;
		}
		catch (FileNotFoundException e) {
			// this is normal, normally there should be no photo info file
		}
		catch (Exception e) {
			Log.e(TAG, "Could not read photo file!");
			e.printStackTrace();
		}
		return null;
	}
}