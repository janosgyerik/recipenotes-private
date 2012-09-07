package com.recipenotes;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

public class RecipeNotesSQLiteOpenHelper extends SQLiteOpenHelper {

	// Debugging
	private static final String TAG = "RecipeNotesSQLiteOpenHelper";

	private static final String DATABASE_NAME = "sqlite3.db";
	private static final int DATABASE_VERSION = 1;

	private static final String RECIPES_TABLE_NAME = "main_recipe";
	private static final String INGREDIENTS_TABLE_NAME = "main_ingredient";
	private static final String RECIPE_INGREDIENTS_TABLE_NAME = "main_recipeingredient";
	private static final String RECIPE_PHOTOS_TABLE_NAME = "main_recipephoto";

	private List<String> sqlCreateStatements;

	RecipeNotesSQLiteOpenHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);

		//context.deleteDatabase(DATABASE_NAME);

		try {
			sqlCreateStatements = readSqlStatements(context, "sql_create.sql");
		} catch (IOException e) {
			sqlCreateStatements = Collections.emptyList();
			e.printStackTrace();
		}
	}

	static List<String> readSqlStatements(Context context, String assetName) throws IOException {
		List<String> statements = new ArrayList<String>();
		InputStream stream = context.getAssets().open(assetName);
		BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
		String line;
		StringBuilder builder = new StringBuilder();
		while ((line = reader.readLine()) != null) {
			builder.append(line);
			if (line.trim().equals(";")) {
				statements.add(builder.toString());
				builder = new StringBuilder();
			}
		}
		return statements;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		for (String sql : sqlCreateStatements) {
			db.execSQL(sql);
			// TODO check success
		}
		// dummy recipes
		db.execSQL("insert into main_recipe (name, display_name) values ('Steak', 'Steak');");
		db.execSQL("insert into main_recipe (name, display_name) values ('Pasta', 'Pasta with asparagus');");
		db.execSQL("insert into main_recipe (name, display_name) values ('Cake', 'Cake with Lemon marmalade');");

		// initial ingredients collection
		db.execSQL("insert into main_ingredient (name) values ('Avocado');");
		db.execSQL("insert into main_ingredient (name) values ('Walnuts');");
		db.execSQL("insert into main_ingredient (name) values ('Hazelnuts');");
		db.execSQL("insert into main_ingredient (name) values ('Olive oil');");
		db.execSQL("insert into main_ingredient (name) values ('Grapeseed oil');");
		db.execSQL("insert into main_ingredient (name) values ('Egg');");
		db.execSQL("insert into main_ingredient (name) values ('Lemon');");
		db.execSQL("insert into main_ingredient (name) values ('Lime');");
		db.execSQL("insert into main_ingredient (name) values ('Steak');");
		db.execSQL("insert into main_ingredient (name) values ('Rice');");
		db.execSQL("insert into main_ingredient (name) values ('Potato');");
		db.execSQL("insert into main_ingredient (name) values ('Tomato');");
		db.execSQL("insert into main_ingredient (name) values ('Zucchini');");
		db.execSQL("insert into main_ingredient (name) values ('Asparagus');");
		db.execSQL("insert into main_ingredient (name) values ('Lettuce');");
		db.execSQL("insert into main_ingredient (name) values ('Almond');");
		db.execSQL("insert into main_ingredient (name) values ('Cashew nut');");
		db.execSQL("insert into main_ingredient (name) values ('Pepper');");
		db.execSQL("insert into main_ingredient (name) values ('Salt');");
		db.execSQL("insert into main_ingredient (name) values ('Plum tomato');");
		db.execSQL("insert into main_ingredient (name) values ('Cherry tomato');");
		db.execSQL("insert into main_ingredient (name) values ('Pork');");
		db.execSQL("insert into main_ingredient (name) values ('Chicken');");
		db.execSQL("insert into main_ingredient (name) values ('Chicken breast');");
		db.execSQL("insert into main_ingredient (name) values ('Soup stock');");
		db.execSQL("insert into main_ingredient (name) values ('Spinach');");
		db.execSQL("insert into main_ingredient (name) values ('Onion');");
		db.execSQL("insert into main_ingredient (name) values ('Beet');");
		db.execSQL("insert into main_ingredient (name) values ('Red radish');");
		db.execSQL("insert into main_ingredient (name) values ('White radish');");
		db.execSQL("insert into main_ingredient (name) values ('Wasabi');");
		db.execSQL("insert into main_ingredient (name) values ('Goya');");
		db.execSQL("insert into main_ingredient (name) values ('Tofu');");
		db.execSQL("insert into main_ingredient (name) values ('Carrot');");
		db.execSQL("insert into main_ingredient (name) values ('Couscous');");
		db.execSQL("insert into main_ingredient (name) values ('Fish');");
		db.execSQL("insert into main_ingredient (name) values ('Garlic');");
		db.execSQL("insert into main_ingredient (name) values ('Basil paste');");
		db.execSQL("insert into main_ingredient (name) values ('Fresh basil');");
		db.execSQL("insert into main_ingredient (name) values ('Lime leaf');");
		db.execSQL("insert into main_ingredient (name) values ('Laurel');");
		db.execSQL("insert into main_ingredient (name) values ('Coconut milk');");
		db.execSQL("insert into main_ingredient (name) values ('Coconut');");
		db.execSQL("insert into main_ingredient (name) values ('Brown sugar');");
		db.execSQL("insert into main_ingredient (name) values ('White sugar');");
		db.execSQL("insert into main_ingredient (name) values ('Sea salt');");
		db.execSQL("insert into main_ingredient (name) values ('Milk');");
		db.execSQL("insert into main_ingredient (name) values ('Honey');");
		db.execSQL("insert into main_ingredient (name) values ('Cucumber');");
		db.execSQL("insert into main_ingredient (name) values ('Cauliflower');");
		db.execSQL("insert into main_ingredient (name) values ('Flour');");
		db.execSQL("insert into main_ingredient (name) values ('Parmesan cheese');");
		db.execSQL("insert into main_ingredient (name) values ('Gruyere cheese');");
		db.execSQL("insert into main_ingredient (name) values ('Rochefort cheese');");
		db.execSQL("insert into main_ingredient (name) values ('Reblochon cheese');");
		db.execSQL("insert into main_ingredient (name) values ('Peanuts');");
		db.execSQL("insert into main_ingredient (name) values ('Pine nuts');");
		db.execSQL("insert into main_ingredient (name) values ('Acacia honey');");

		// initial tags collection
		db.execSQL("insert into main_tag (name) values ('Main');");
		db.execSQL("insert into main_tag (name) values ('Side dish');");
		db.execSQL("insert into main_tag (name) values ('Salad');");
		db.execSQL("insert into main_tag (name) values ('Dessert');");
		db.execSQL("insert into main_tag (name) values ('Dip');");
		db.execSQL("insert into main_tag (name) values ('Cake');");
		db.execSQL("insert into main_tag (name) values ('Shake');");
		db.execSQL("insert into main_tag (name) values ('Appetizer');");
		db.execSQL("insert into main_tag (name) values ('Light');");
		db.execSQL("insert into main_tag (name) values ('Heavy');");
		db.execSQL("insert into main_tag (name) values ('Meat');");
		db.execSQL("insert into main_tag (name) values ('Fish');");
		db.execSQL("insert into main_tag (name) values ('Vegetarian');");
		db.execSQL("insert into main_tag (name) values ('Cheese');");
		db.execSQL("insert into main_tag (name) values ('Pasta');");
		db.execSQL("insert into main_tag (name) values ('Rice');");
		db.execSQL("insert into main_tag (name) values ('French');");
		db.execSQL("insert into main_tag (name) values ('Italian');");
		db.execSQL("insert into main_tag (name) values ('Russian');");
		db.execSQL("insert into main_tag (name) values ('Hungarian');");
		db.execSQL("insert into main_tag (name) values ('Thai');");
		db.execSQL("insert into main_tag (name) values ('Ethnic');");
		db.execSQL("insert into main_tag (name) values ('Fusion');");
		db.execSQL("insert into main_tag (name) values ('Japanese');");
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
	}

	/**
	 * Returns new recipeId on success or else null
	 * @return
	 */
	public String newRecipe() {
		ContentValues values = new ContentValues();
		long createdDt = new Date().getTime();
		values.put("created_dt", createdDt);
		values.put("updated_dt", createdDt);
		long ret = getWritableDatabase().insert(RECIPES_TABLE_NAME, null, values);
		Log.d(TAG, "insert recipe ret = " + ret);
		if (ret >= 0) {
			String recipeId = String.valueOf(ret);
			return recipeId;
		}
		else {
			return null;
		}
	}

	public boolean saveRecipe(String recipeId, String name) {
		ContentValues values = new ContentValues();
		values.put("name", name);
		long updatedDt = new Date().getTime();
		values.put("updated_dt", updatedDt);

		// display_name
		// TODO
		/*
		String displayName = "";
		for (int i = 0; i < ingredientsListAdapter.getCount(); ++i) {
			displayName += ingredientsListAdapter.getItem(i);
			if (i < ingredientsListAdapter.getCount() - 1) {
				displayName += ", ";
			}
		}
		values.put("display_name", displayName);
		 */

		int ret = getWritableDatabase().update(RECIPES_TABLE_NAME, values, 
				BaseColumns._ID + " = ?", new String[]{ recipeId });
		Log.d(TAG, "update ret = " + ret);
		return ret == 1;
	}

	public boolean deleteRecipe(String recipeId) {
		getWritableDatabase().delete(RECIPE_INGREDIENTS_TABLE_NAME, "recipe_id = ?", new String[]{ recipeId });
		getWritableDatabase().delete(RECIPE_PHOTOS_TABLE_NAME, "recipe_id = ?", new String[]{ recipeId });
		getWritableDatabase().delete(RECIPES_TABLE_NAME, "recipe_id = ?", new String[]{ recipeId });
		// TODO error handling
		// TODO delete photos in the filesystem
		return true;
	}

	public String getOrCreateIngredient(String name) {
		String ingredientId = getIngredientIdByName(name);
		if (ingredientId == null) {
			ingredientId = newIngredient(name);
		}
		return ingredientId;
	}

	/**
	 * Returns ingredientId or null if ingredient does not exist.
	 * @param name
	 * @return
	 */
	public String getIngredientIdByName(String name) {
		String ingredientId = null;
		Cursor cursor = getReadableDatabase().query(
				INGREDIENTS_TABLE_NAME, 
				new String[] { BaseColumns._ID }, 
				"name = ?", 
				new String[] { name }, 
				null, null, null, "1");
		if (cursor.moveToNext()) {
			ingredientId = cursor.getString(0);
			Log.d(TAG, "got ingredientId = " + ingredientId);
		}
		cursor.close();
		return ingredientId;
	}

	/**
	 * Returns new ingredientId on success or else null
	 * @return
	 */
	private String newIngredient(String name) {
		ContentValues values = new ContentValues();
		long createdDt = new Date().getTime();
		values.put("created_dt", createdDt);
		values.put("updated_dt", createdDt);
		long ret = getWritableDatabase().insert(INGREDIENTS_TABLE_NAME, null, values);
		Log.d(TAG, "insert ingredient ret = " + ret);
		if (ret >= 0) {
			String ingredientId = String.valueOf(ret);
			return ingredientId;
		}
		else {
			return null;
		}
	}

	public boolean addRecipeIngredient(String recipeId, String ingredientId) {
		ContentValues values = new ContentValues();
		values.put("recipe_id", recipeId);
		values.put("ingredient_id", ingredientId);
		long ret = getWritableDatabase().insert(RECIPE_INGREDIENTS_TABLE_NAME, null, values);
		Log.d(TAG, String.format("insert recipe ingredient %s %s ret = %s",
				recipeId, ingredientId, ret));
		return ret >= 0;
	}
	
	public boolean removeRecipeIngredient(String recipeId, String ingredientId) {
		int ret = getWritableDatabase().delete(RECIPE_INGREDIENTS_TABLE_NAME,
				"recipe_id = ? AND ingredient_id = ?",
				new String[]{ recipeId, ingredientId });
		Log.d(TAG, String.format("delete recipe ingredient %s %s ret = %s",
				recipeId, ingredientId, ret));
		return ret > 0;
	}

}
