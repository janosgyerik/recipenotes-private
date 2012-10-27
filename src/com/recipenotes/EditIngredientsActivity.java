package com.recipenotes;

import java.util.ArrayList;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Toast;

public class EditIngredientsActivity extends ListActivity {

	// Debugging
	private static final String TAG = "EditIngredientsActivity";
	private static final boolean D = false;

	public static final String OUT_CHANGED = "CHANGED";

	private RecipeNotesSQLiteOpenHelper helper;
	private String recipeId;

	private MultiAutoCompleteTextView ingredientView;
	private ArrayAdapter<String> ingredientsListAdapter;
	private ListView ingredientsListView;

	private boolean isChanged;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.editingredients);
		if (D) Log.d(TAG, "++onCreate");

		helper = new RecipeNotesSQLiteOpenHelper(this);

		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			recipeId = extras.getString(BaseColumns._ID);
		}
		else {
			// TODO just for debugging!
			recipeId = "44";
		}

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

		ingredientsListAdapter =
				new ArrayAdapter<String>(this, R.layout.ingredientslist_item);
		ingredientsListView = (ListView) findViewById(android.R.id.list);
		ingredientsListView.setAdapter(ingredientsListAdapter);
		ingredientsListView.setOnItemLongClickListener(new IngredientListOnItemLongClickListener());

		Button addIngredientButton = (Button) findViewById(R.id.btn_add_ingredient);
		addIngredientButton.setOnClickListener(new AddIngredientOnClickListener());

		Cursor ingredientsCursor = helper.getRecipeIngredientsCursor(recipeId);
		startManagingCursor(ingredientsCursor);
		while (ingredientsCursor.moveToNext()) {
			String ingredient = ingredientsCursor.getString(0);
			ingredientsListAdapter.add(ingredient);
		}

		setListAdapter(ingredientsListAdapter);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)  {
		if (D) Log.d(TAG, "onKeyDown");
		if (Integer.parseInt(android.os.Build.VERSION.SDK) < 5
				&& keyCode == KeyEvent.KEYCODE_BACK
				&& event.getRepeatCount() == 0) {
			returnResult();
		}

		return super.onKeyDown(keyCode, event);
	}

	private void returnResult() {
		Intent intent = new Intent();
		intent.putExtra(OUT_CHANGED, isChanged);
		setResult(Activity.RESULT_OK, intent);
		finish();
	}

	private void addIngredients() {
		String ingredients = ingredientView.getText().toString().trim();
		if (ingredients.length() > 0) {
			StringBuffer ingredientsListMsgBuffer = new StringBuffer();
			for (String ingredient : ingredients.split(",")) {
				ingredient = ClutteredEditRecipeActivity.capitalize(ingredient);
				ingredientsListMsgBuffer.append(ingredient);
				ingredientsListMsgBuffer.append(", ");
				String ingredientId = helper.getOrCreateIngredient(ingredient);
				if (ingredientId != null
						&& helper.addRecipeIngredient(recipeId, ingredientId)) {
					ingredientsListAdapter.insert(ingredient, 0);
					isChanged = true;
				}
			}
			ingredientView.setText("");
			String ingredientsListMsg = ingredientsListMsgBuffer.substring(0, ingredientsListMsgBuffer.lastIndexOf(","));
			Toast.makeText(getApplicationContext(), "Added " + ingredientsListMsg, Toast.LENGTH_SHORT).show();
		}
	}

	private void removeIngredient(String ingredientName) {
		String ingredientId = helper.getIngredientIdByName(ingredientName);
		if (ingredientId != null
				&& helper.removeRecipeIngredient(recipeId, ingredientId)) {
			ingredientsListAdapter.remove(ingredientName);
			isChanged = true;
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

	@Override  
	protected void onDestroy() {
		Log.d(TAG, "++onDestroy");
		super.onDestroy();
		helper.close();
	}
}
