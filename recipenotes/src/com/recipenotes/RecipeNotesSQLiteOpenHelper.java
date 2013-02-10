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
import android.util.SparseArray;

public class RecipeNotesSQLiteOpenHelper extends SQLiteOpenHelper {

	// Debugging
	private static final String TAG = RecipeNotesSQLiteOpenHelper.class
			.getSimpleName();

	private static final String DATABASE_NAME = "sqlite3.db";
	private static final int DATABASE_VERSION = 4;

	private static final String RECIPES_TABLE_NAME = "recipes_recipe";
	private static final String INGREDIENTS_TABLE_NAME = "recipes_ingredient";
	private static final String TAGS_TABLE_NAME = "recipes_tag";
	private static final String RECIPE_INGREDIENTS_TABLE_NAME = "recipes_recipeingredient";
	private static final String RECIPE_TAGS_TABLE_NAME = "recipes_recipetag";
	private static final String RECIPE_PHOTOS_TABLE_NAME = "recipes_recipephoto";

	private List<String> sqlCreateStatements;
	private SparseArray<List<String>> sqlUpgradeStatements;

	RecipeNotesSQLiteOpenHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);

		//context.deleteDatabase(DATABASE_NAME);

		sqlCreateStatements = getSqlStatements(context, "sql_create.sql");
		sqlUpgradeStatements = new SparseArray<List<String>>();
		sqlUpgradeStatements.put(2,
				getSqlStatements(context, "sql_upgrade2.sql"));
		sqlUpgradeStatements.put(3,
				getSqlStatements(context, "sql_upgrade3.sql"));
		sqlUpgradeStatements.put(4,
				getSqlStatements(context, "sql_upgrade4.sql"));
	}

	private List<String> getSqlStatements(Context context, String assetName) {
		List<String> statements;
		try {
			statements = readSqlStatements(context, assetName);
		} catch (IOException e) {
			statements = Collections.emptyList();
			e.printStackTrace();
		}
		return statements;
	}

	static List<String> readSqlStatements(Context context, String assetName)
			throws IOException {
		List<String> statements = new ArrayList<String>();
		InputStream stream = context.getAssets().open(assetName);
		BufferedReader reader = new BufferedReader(
				new InputStreamReader(stream));
		String line;
		StringBuilder builder = new StringBuilder();
		while ((line = reader.readLine()) != null) {
			builder.append(line);
			if (line.trim().endsWith(";")) {
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
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		for (int i = oldVersion; i < newVersion; ++i) {
			upgradeToVersion(db, i + 1);
		}
	}

	private void upgradeToVersion(SQLiteDatabase db, int version) {
		Log.d(TAG, "upgrade to version " + version);
		for (String sql : sqlUpgradeStatements.get(version)) {
			db.execSQL(sql);
		}
	}

	/**
	 * Returns new recipeId on success or else null
	 * 
	 * @return
	 */
	public String newRecipe() {
		ContentValues values = new ContentValues();
		long createdDt = new Date().getTime();
		values.put("created_dt", createdDt);
		values.put("updated_dt", createdDt);
		long ret = getWritableDatabase().insert(RECIPES_TABLE_NAME, null,
				values);
		Log.d(TAG, "insert recipe ret = " + ret);
		if (ret >= 0) {
			String recipeId = String.valueOf(ret);
			return recipeId;
		} else {
			return null;
		}
	}

	public boolean saveRecipe(String recipeId, String name, String displayName,
			String memo) {
		ContentValues values = new ContentValues();
		values.put("name", name);
		values.put("memo", memo);
		long updatedDt = new Date().getTime();
		values.put("updated_dt", updatedDt);

		// display_name
		// TODO
		/*
		 * String displayName = ""; for (int i = 0; i <
		 * ingredientsListAdapter.getCount(); ++i) { displayName +=
		 * ingredientsListAdapter.getItem(i); if (i <
		 * ingredientsListAdapter.getCount() - 1) { displayName += ", "; } }
		 */
		values.put("display_name", displayName);

		// display_image
		// TODO

		int ret = getWritableDatabase().update(RECIPES_TABLE_NAME, values,
				BaseColumns._ID + " = ?", new String[] { recipeId });
		Log.d(TAG, String.format("update recipe %s -> '%s' <- %s", recipeId,
				name, ret));
		return ret == 1;
	}

	public boolean deleteRecipe(String recipeId) {
		getWritableDatabase().delete(RECIPE_INGREDIENTS_TABLE_NAME,
				"recipe_id = ?", new String[] { recipeId });
		getWritableDatabase().delete(RECIPE_TAGS_TABLE_NAME, "recipe_id = ?",
				new String[] { recipeId });
		getWritableDatabase().delete(RECIPE_PHOTOS_TABLE_NAME, "recipe_id = ?",
				new String[] { recipeId });
		getWritableDatabase().delete(RECIPES_TABLE_NAME, "_id = ?",
				new String[] { recipeId });
		Log.d(TAG, "deleted recipe " + recipeId);
		// TODO error handling
		return true;
	}

	public String getOrCreateTag(String name) {
		String tagId = getTagIdByName(name);
		if (tagId == null) {
			tagId = newTag(name);
		}
		return tagId;
	}

	/**
	 * Returns tagId or null if tag does not exist.
	 * 
	 * @param name
	 * @return
	 */
	public String getTagIdByName(String name) {
		String tagId = null;
		Cursor cursor = getReadableDatabase().query(TAGS_TABLE_NAME,
				new String[] { BaseColumns._ID }, "name = ?",
				new String[] { name }, null, null, null, "1");
		if (cursor.moveToNext()) {
			tagId = cursor.getString(0);
			Log.d(TAG, String.format("got tag: %s -> %s", tagId, name));
		}
		cursor.close();
		return tagId;
	}

	/**
	 * Returns new tagId on success or else null
	 * 
	 * @return
	 */
	private String newTag(String name) {
		ContentValues values = new ContentValues();
		values.put("name", name);
		long createdDt = new Date().getTime();
		values.put("created_dt", createdDt);
		values.put("updated_dt", createdDt);
		long ret = getWritableDatabase().insert(TAGS_TABLE_NAME, null, values);
		Log.d(TAG, String.format("insert tag: %s <- %s", name, ret));
		if (ret >= 0) {
			String tagId = String.valueOf(ret);
			return tagId;
		} else {
			return null;
		}
	}

	public boolean addRecipeTag(String recipeId, String tagId) {
		ContentValues values = new ContentValues();
		values.put("recipe_id", recipeId);
		values.put("tag_id", tagId);
		long createdDt = new Date().getTime();
		values.put("created_dt", createdDt);
		values.put("updated_dt", createdDt);
		long ret = getWritableDatabase().insert(RECIPE_TAGS_TABLE_NAME, null,
				values);
		Log.d(TAG, String.format("insert recipe tag: %s, %s <- %s", recipeId,
				tagId, ret));
		return ret >= 0;
	}

	public boolean removeRecipeTag(String recipeId, String tagId) {
		int ret = getWritableDatabase().delete(RECIPE_TAGS_TABLE_NAME,
				"recipe_id = ? AND tag_id = ?",
				new String[] { recipeId, tagId });
		Log.d(TAG, String.format("delete recipe tag: %s, %s <- %s", recipeId,
				tagId, ret));
		return ret > 0;
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
	 * 
	 * @param name
	 * @return
	 */
	public String getIngredientIdByName(String name) {
		String ingredientId = null;
		Cursor cursor = getReadableDatabase().query(INGREDIENTS_TABLE_NAME,
				new String[] { BaseColumns._ID }, "name = ?",
				new String[] { name }, null, null, null, "1");
		if (cursor.moveToNext()) {
			ingredientId = cursor.getString(0);
			Log.d(TAG, String.format("got ingredient: %s -> %s", ingredientId,
					name));
		}
		cursor.close();
		return ingredientId;
	}

	/**
	 * Returns new ingredientId on success or else null
	 * 
	 * @return
	 */
	private String newIngredient(String name) {
		ContentValues values = new ContentValues();
		values.put("name", name);
		long createdDt = new Date().getTime();
		values.put("created_dt", createdDt);
		values.put("updated_dt", createdDt);
		long ret = getWritableDatabase().insert(INGREDIENTS_TABLE_NAME, null,
				values);
		Log.d(TAG, String.format("insert ingredient: %s <- %s", name, ret));
		if (ret >= 0) {
			String ingredientId = String.valueOf(ret);
			return ingredientId;
		} else {
			return null;
		}
	}

	public boolean addRecipeIngredient(String recipeId, String ingredientId) {
		ContentValues values = new ContentValues();
		values.put("recipe_id", recipeId);
		values.put("ingredient_id", ingredientId);
		long createdDt = new Date().getTime();
		values.put("created_dt", createdDt);
		values.put("updated_dt", createdDt);
		long ret = getWritableDatabase().insert(RECIPE_INGREDIENTS_TABLE_NAME,
				null, values);
		Log.d(TAG, String.format("insert recipe ingredient: %s, %s <- %s",
				recipeId, ingredientId, ret));
		return ret >= 0;
	}

	public boolean removeRecipeIngredient(String recipeId, String ingredientId) {
		int ret = getWritableDatabase().delete(RECIPE_INGREDIENTS_TABLE_NAME,
				"recipe_id = ? AND ingredient_id = ?",
				new String[] { recipeId, ingredientId });
		Log.d(TAG, String.format("delete recipe ingredient: %s, %s <- %s",
				recipeId, ingredientId, ret));
		return ret > 0;
	}

	public boolean addRecipePhoto(String recipeId, String filename) {
		ContentValues values = new ContentValues();
		values.put("recipe_id", recipeId);
		values.put("filename", filename);
		long createdDt = new Date().getTime();
		values.put("created_dt", createdDt);
		values.put("updated_dt", createdDt);
		long ret = getWritableDatabase().insert(RECIPE_PHOTOS_TABLE_NAME, null,
				values);
		Log.d(TAG, String.format("insert recipe photo: %s, %s <- %s", recipeId,
				filename, ret));
		return ret >= 0;
	}

	public boolean removeRecipePhoto(String recipeId, String filename) {
		int ret = getWritableDatabase().delete(RECIPE_PHOTOS_TABLE_NAME,
				"recipe_id = ? AND filename = ?",
				new String[] { recipeId, filename });
		Log.d(TAG, String.format("delete recipe photo: %s, %s <- %s", recipeId,
				filename, ret));
		return ret > 0;
	}

	public Cursor getRecipeListCursor() {
		Log.d(TAG, "get all recipes");
		Cursor cursor = getReadableDatabase().rawQuery(
				"select _id, ifnull(nullif(name, ''), '(recipe)') name, summary, display_name"
						+ " from recipes_recipe"
						+ " order by updated_dt desc, name", null);
		Log.d(TAG, "get all recipes -> " + cursor.getCount());
		return cursor;
	}

	public Cursor getIngredientsListCursor() {
		Log.d(TAG, "get all ingredients");
		Cursor cursor = getReadableDatabase().query(INGREDIENTS_TABLE_NAME,
				new String[] { BaseColumns._ID, "name", }, null, null, null,
				null, "name");
		return cursor;
	}

	public Cursor getTagsListCursor() {
		Log.d(TAG, "get all tags");
		Cursor cursor = getReadableDatabase().query(TAGS_TABLE_NAME,
				new String[] { BaseColumns._ID, "name", }, null, null, null,
				null, "name");
		return cursor;
	}

	public Cursor getRecipeDetailsCursor(String recipeId) {
		Log.d(TAG, "get recipe " + recipeId);
		Cursor cursor = getReadableDatabase().query(RECIPES_TABLE_NAME,
				new String[] { "name", "memo", }, BaseColumns._ID + " = ?",
				new String[] { recipeId }, null, null, null);
		return cursor;
	}

	public Cursor getRecipeIngredientsCursor(String recipeId) {
		Log.d(TAG, "get recipe ingredients " + recipeId);
		Cursor cursor = getReadableDatabase()
				.rawQuery(
						String.format(
								"SELECT i.name FROM %s ri JOIN %s i ON ri.ingredient_id = i.%s WHERE ri.recipe_id = ? ORDER BY i.name",
								RECIPE_INGREDIENTS_TABLE_NAME,
								INGREDIENTS_TABLE_NAME, BaseColumns._ID),
						new String[] { recipeId });
		return cursor;
	}

	public Cursor getRecipeTagsCursor(String recipeId) {
		Log.d(TAG, "get recipe tags " + recipeId);
		Cursor cursor = getReadableDatabase()
				.rawQuery(
						String.format(
								"SELECT t.name FROM %s rt JOIN %s t ON rt.tag_id = t.%s WHERE rt.recipe_id = ? ORDER BY t.name",
								RECIPE_TAGS_TABLE_NAME, TAGS_TABLE_NAME,
								BaseColumns._ID), new String[] { recipeId });
		return cursor;
	}

	public Cursor getRecipePhotosCursor(String recipeId) {
		Log.d(TAG, "get recipe photos " + recipeId);
		Cursor cursor = getReadableDatabase().rawQuery(
				String.format("SELECT filename FROM %s WHERE recipe_id = ?",
						RECIPE_PHOTOS_TABLE_NAME), new String[] { recipeId });
		return cursor;
	}

	public Cursor getPhotoListCursor() {
		Log.d(TAG, "get recipe photos");
		Cursor cursor = getReadableDatabase().rawQuery(
				String.format("SELECT p.recipe_id, p.filename FROM %s p "
						+ "JOIN %s r ON p.recipe_id = r._id "
						+ "ORDER BY r.updated_dt DESC, p._id",
						RECIPE_PHOTOS_TABLE_NAME, RECIPES_TABLE_NAME),
				new String[] {});
		return cursor;
	}

	public boolean isExistingRecipeId(String recipeId) {
		Log.d(TAG, "isExistingRecipeId " + recipeId);
		boolean exists = false;
		Cursor cursor = getReadableDatabase().query(RECIPES_TABLE_NAME,
				new String[] { "name", }, BaseColumns._ID + " = ?",
				new String[] { recipeId }, null, null, null);
		if (cursor.moveToNext()) {
			exists = true;
		}
		cursor.close();
		Log.d(TAG, "isExistingRecipeId " + recipeId + " -> " + exists);
		return exists;
	}

	public boolean isEmptyRecipe(String recipeId) {
		Log.d(TAG, "isEmptyRecipe " + recipeId);
		boolean empty = true;
		String sql = String
				.format("SELECT 1 FROM %s r "
						+ "LEFT JOIN %s i ON r._id = i.recipe_id "
						+ "LEFT JOIN %s t ON r._id = t.recipe_id "
						+ "LEFT JOIN %s p ON r._id = p.recipe_id "
						+ "WHERE r._id = %s AND "
						+ "(i._id IS NOT NULL OR t._id IS NOT NULL OR p._id IS NOT NULL) "
						+ "LIMIT 1", RECIPES_TABLE_NAME,
						RECIPE_INGREDIENTS_TABLE_NAME, RECIPE_TAGS_TABLE_NAME,
						RECIPE_PHOTOS_TABLE_NAME, recipeId);
		Log.d(TAG, sql);
		Cursor cursor = getReadableDatabase().rawQuery(sql, new String[] {});
		if (cursor.moveToNext()) {
			empty = false;
		}
		cursor.close();
		Log.d(TAG, "isEmptyRecipe " + recipeId + " -> " + empty);
		return empty;
	}

}
