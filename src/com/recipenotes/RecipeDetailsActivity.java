package com.recipenotes;

import java.util.ArrayList;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
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

	private String pk;
	private SQLiteOpenHelper helper;

	private EditText nameView;

	private AutoCompleteTextView ingredientView;

	private ArrayAdapter<String> ingredientsListAdapter;
	private ListView ingredientsListView; 

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.recipe_details);

		helper = new RecipeNotesSQLiteOpenHelper(this);

		nameView = (EditText) findViewById(R.id.name);

		Cursor cursor = helper.getWritableDatabase().query(
				INGREDIENTS_TABLE_NAME, 
				new String[]{ 
						BaseColumns._ID, "name", 
				}, 
				null, null, null, null, "name");
		startManagingCursor(cursor);

		ArrayList<String> ingredientsAutoCompleteList = new ArrayList<String>();
		int i = cursor.getColumnIndex("name");
		while (cursor.moveToNext()) {
			ingredientsAutoCompleteList.add(cursor.getString(i));
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
		ingredientsListAdapter.add("Avocado");
		setListViewHeightBasedOnChildren(ingredientsListView);

		Button addIngredientButton = (Button) findViewById(R.id.btn_add_ingredient);
		addIngredientButton.setOnClickListener(new AddIngredientOnClickListener());

		pk = getIntent().getExtras().getString(BaseColumns._ID);
		if (pk != null) {
			Cursor mCursor = helper.getWritableDatabase().query(
					"main_recipe", 
					new String[]{ 
							BaseColumns._ID, "name", "year", "recipe_type", "buy_flag", 
							"region", "grape",
							"aroma", "taste", "after_taste", "overall",
							"aroma_list", "taste_list", "after_taste_list",
					}, 
					BaseColumns._ID + " = ?", new String[]{ pk }, null, null, null);
			startManagingCursor(mCursor);

			if (mCursor.moveToFirst()) {
				String buyFlag = mCursor.getString(4);
				if (buyFlag == null) {
					buyFlag = "1";
				}
				if (buyFlag.equals("0")) {
					buyFlag = "Buy";
				}
				else if (buyFlag.equals("1")) {
					buyFlag = "Maybe";
				}
				else if (buyFlag.equals("2")) {
					buyFlag = "-";
				}
				else if (buyFlag.equals("3")) {
					buyFlag = "Never";
				}

				nameView.setText(mCursor.getString(1));
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

			if (pk == null) {
				long ret = helper.getWritableDatabase().insert(RECIPES_TABLE_NAME, null, values);
				Log.d(TAG, "insert recipe ret = " + ret);
				if (ret >= 0) {
					String recipeId = String.valueOf(ret);
					Toast.makeText(getApplicationContext(), "Successfully added new recipe", Toast.LENGTH_SHORT).show();
					for (int i = 0; i < ingredientsListAdapter.getCount(); ++i) {
						String ingredient = ingredientsListAdapter.getItem(i);
						Cursor ingredientIdCursor =
								helper.getReadableDatabase().query(INGREDIENTS_TABLE_NAME,
										new String[]{ BaseColumns._ID, },
										"name = ?",
										new String[]{ ingredient, },
										null, null, null, "1");
						String ingredientId;
						if (ingredientIdCursor.moveToNext()) {
							ingredientId = ingredientIdCursor.getString(0);
							Log.d(TAG, "got ingredientId = " + ingredientId);
						}
						else {
							ret = helper.getWritableDatabase().insert(RECIPES_TABLE_NAME, null, values);
							Log.d(TAG, "insert ingredient ret = " + ret);
							ingredientId = String.valueOf(ret);
						}
						values = new ContentValues();
						values.put("recipe_id", recipeId);
						values.put("ingredient_id", ingredientId);
						ret = helper.getWritableDatabase().insert(RECIPE_INGREDIENTS_TABLE_NAME, null, values);
						Log.d(TAG, "insert recipe ingredient ret = " + ret);
					}
				}
				else {
					Toast.makeText(getApplicationContext(), "Error adding new recipe", Toast.LENGTH_SHORT).show();
				}
			}
			else {
				int ret = helper.getWritableDatabase().update(RECIPES_TABLE_NAME, values, 
						BaseColumns._ID + " = ?", new String[]{ pk });
				Log.d(TAG, "update ret = " + ret);
				if (ret == 1) {
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
}
