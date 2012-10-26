package com.recipenotes;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class EditRecipeActivity extends ViewRecipeActivity {

	private static final String TAG = "EditRecipeActivity";

	private static final int RETURN_FROM_EDIT_INGREDIENTS = 1;
	private static final int RETURN_FROM_EDIT_TAGS = 2;

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
				Intent intent = new Intent(this_, EditIngredientsActivity.class);
				intent.putExtra(BaseColumns._ID, recipeId);
				startActivityForResult(intent, RETURN_FROM_EDIT_TAGS);
			}
		});

		Button save = (Button) findViewById(R.id.btn_save);
		save.setOnClickListener(new SaveRecipeOnClickListener());

		updateRecipeView();
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
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case RETURN_FROM_EDIT_INGREDIENTS:
				//				handleSmallCameraPhoto(data);
				updateRecipeView();
				break;
			}
		}
	}

	@Override  
	protected void onDestroy() {
		Log.d(TAG, "++onDestroy");
		super.onDestroy();
		helper.close();
	}
}