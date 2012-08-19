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
import android.widget.MultiAutoCompleteTextView;
import android.widget.RatingBar;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.Toast;

public class RecipeDetailsActivity extends Activity {

	// Debugging
	private static final String TAG = "RecipeDetailsActivity";

	private static final String RECIPES_TABLE_NAME = "main_recipe";
	private static final String INGREDIENTS_TABLE_NAME = "main_ingredient";
	private static final String RECIPE_INGREDIENTS_TABLE_NAME = "main_recipeingredient";

	private SQLiteOpenHelper helper;
	private String pk;

	private EditText nameView;
	private Spinner yearView;
	private Spinner recipeTypeView;
	private Spinner buyFlagView;
	private AutoCompleteTextView regionView;
	private MultiAutoCompleteTextView grapeView;
	private RatingBar aromaRatingView;
	private RatingBar tasteRatingView;
	private RatingBar afterTasteRatingView;
	private RatingBar overallRatingView;
	private MultiAutoCompleteTextView aromaListView;
	private MultiAutoCompleteTextView tasteListView;
	private MultiAutoCompleteTextView afterTasteListView;

	private AutoCompleteTextView ingredientView;
	
	private ArrayAdapter<String> ingredientsListAdapter;
	private ListView ingredientsListView; 

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.recipe_details);

		nameView = (EditText) findViewById(R.id.name);
		
		SQLiteOpenHelper helper = new RecipeNotesSQLiteOpenHelper(this);
		Cursor mCursor = helper.getWritableDatabase().query(
				INGREDIENTS_TABLE_NAME, 
				new String[]{ 
						BaseColumns._ID, "name", 
						}, 
				null, null, null, null, "name");
		startManagingCursor(mCursor);
		ArrayList<String> ingredientsAutoCompleteList = new ArrayList<String>();
		int i = mCursor.getColumnIndex("name");
		while (mCursor.moveToNext()) {
			ingredientsAutoCompleteList.add(mCursor.getString(i));
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

		helper = new RecipeNotesSQLiteOpenHelper(this);
		pk = getIntent().getExtras().getString(BaseColumns._ID);
		/*
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
				regionView.setText(mCursor.getString(5));
				grapeView.setText(mCursor.getString(6));
				aromaRatingView.setRating(mCursor.getInt(7));
				tasteRatingView.setRating(mCursor.getInt(8));
				afterTasteRatingView.setRating(mCursor.getInt(9));
				overallRatingView.setRating(mCursor.getInt(10));
				aromaListView.setText(mCursor.getString(11));
				tasteListView.setText(mCursor.getString(12));
				afterTasteListView.setText(mCursor.getString(13));
			}
		}
		*/

		Button save = (Button) findViewById(R.id.btn_save);
		save.setOnClickListener(new SaveButtonOnClickListener());
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

	class SaveButtonOnClickListener implements OnClickListener {

		@Override
		public void onClick(View view) {
			ContentValues values = new ContentValues();
			String name = capitalize(nameView.getText().toString());
			String year = yearView.getSelectedItem().toString();
			String recipeType = capitalize(recipeTypeView.getSelectedItem().toString());
			String buyFlag;
			switch (buyFlagView.getSelectedItemPosition()) {
			case 0:
				buyFlag = "2"; // not this time
				break;
			case 1:
				buyFlag = "0"; // buy
				break;
			case 2:
				buyFlag = "1"; // maybe
				break;
			default:
				buyFlag = "3"; // never
			}
			String region = capitalize(regionView.getText().toString());
			String grape = capitalize(grapeView.getText().toString());
			float aromaRating = aromaRatingView.getRating();
			float tasteRating = tasteRatingView.getRating();
			float afterTasteRating = afterTasteRatingView.getRating();
			float overallRating = overallRatingView.getRating();
			String aromaList = aromaListView.getText().toString();
			String tasteList = tasteListView.getText().toString();
			String afterTasteList = afterTasteListView.getText().toString();

			values.put("name", name);
			values.put("year", year);
			values.put("recipe_type", recipeType);
			values.put("buy_flag", buyFlag);
			values.put("region", region);
			values.put("grape", grape);
			values.put("aroma", aromaRating);
			values.put("taste", tasteRating);
			values.put("after_taste", afterTasteRating);
			values.put("overall", overallRating);
			values.put("aroma_list", aromaList);
			values.put("taste_list", tasteList);
			values.put("after_taste_list", afterTasteList);

			if (pk == null) {
				long ret = helper.getWritableDatabase().insert(RECIPES_TABLE_NAME, null, values);
				Log.d(TAG, "insert ret = " + ret);
				if (ret >= 0) {
					pk = String.valueOf(ret);
					Toast.makeText(getApplicationContext(), "Successfully added new recipe", Toast.LENGTH_SHORT).show();
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
