package com.recipenotes;

import java.io.File;
import java.io.IOException;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

public class EditRecipeActivity extends ViewRecipeActivity {

	private static final String TAG = "EditRecipeActivity";

	private static final int RETURN_FROM_EDIT_INGREDIENTS = 1;
	private static final int RETURN_FROM_EDIT_TAGS = 2;
	private static final int RETURN_FROM_ADD_PHOTO = 3;

	private EditText nameView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.editrecipe);

		helper = new RecipeNotesSQLiteOpenHelper(this);

		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			recipeId = extras.getString(BaseColumns._ID);
		}
		if (recipeId == null) {
			//			recipeId = helper.newRecipe();
			recipeId = "42"; // rich
			recipeId = "44"; // empty
		}

		nameView = (EditText) findViewById(R.id.name_edit);

		final Activity this_ = this;

		Button editIngredientsButton = (Button) findViewById(R.id.btn_edit_ingredients);
		editIngredientsButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(this_, EditIngredientsActivity.class);
				intent.putExtra(BaseColumns._ID, recipeId);
				startActivityForResult(intent, RETURN_FROM_EDIT_INGREDIENTS);
			}
		});

		Button editTagsButton = (Button) findViewById(R.id.btn_edit_tags);
		editTagsButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(this_, EditTagsActivity.class);
				intent.putExtra(BaseColumns._ID, recipeId);
				startActivityForResult(intent, RETURN_FROM_EDIT_TAGS);
			}
		});

		Button addPhotoButton = (Button) findViewById(R.id.btn_add_photo);
		addPhotoButton.setOnClickListener(new AddPhotoOnClickListener());

		Button save = (Button) findViewById(R.id.btn_save);
		save.setOnClickListener(new SaveRecipeOnClickListener());

		reloadAndRefreshRecipeDetails();
	}

	class SaveRecipeOnClickListener implements OnClickListener {
		@Override
		public void onClick(View view) {
			String name = capitalize(nameView.getText().toString());

			// TODO
			// display_name
			String displayName = "TODO:ingredients-list";
			/*
			for (int i = 0; i < ingredientsListAdapter.getCount(); ++i) {
				displayName += ingredientsListAdapter.getItem(i);
				if (i < ingredientsListAdapter.getCount() - 1) {
					displayName += ", ";
				}
			}
			 */

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

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.i(TAG, "onActivityResult");
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case RETURN_FROM_EDIT_INGREDIENTS:
				Log.i(TAG, "OK edit ingredients");
				reloadAndRefreshRecipeDetails();
				break;
			case RETURN_FROM_EDIT_TAGS:
				Log.i(TAG, "OK edit tags");
				reloadAndRefreshRecipeDetails();
				break;
			case RETURN_FROM_ADD_PHOTO:
				handleSmallCameraPhoto(data);
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
		Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
		startActivityForResult(takePictureIntent, RETURN_FROM_ADD_PHOTO);
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

	private void handleSmallCameraPhoto(Intent intent) {
		if (photoFile != null && photoFile.isFile()) {
			addPhotoToRecipe(photoFile);
			View photoView = addPhotoToLayout(photoFile);
			if (photoView != null) {
				photoView.setOnLongClickListener(new PhotoOnLongClickListener(photoFile));
			}
		}
	}

	private void removePhoto(File photoFile) {
		if (photoFile.delete()) {
			if (removePhotoFromRecipe(photoFile)) {
				removePhotoFromLayout(photoFile);
			}
		}
	}

	private void removePhotoFromLayout(File photoFile) {
		LinearLayout layout = (LinearLayout) findViewById(R.id.photos);
		layout.removeView(layout.findViewWithTag(photoFile.getName()));
	}

	private boolean removePhotoFromRecipe(File photoFile) {
		return helper.removeRecipePhoto(recipeId, photoFile.getName());
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