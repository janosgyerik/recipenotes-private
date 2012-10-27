package com.recipenotes;

import android.database.Cursor;

public class EditIngredientsActivity extends AbstractEditRecipeItemsActivity {

	@Override
	int getContentViewId() {
		return R.layout.editingredients;
	}

	@Override
	Cursor getAutoCompleteListCursor() {
		return helper.getIngredientsListCursor();
	}

	@Override
	Cursor getItemListCursor() {
		return helper.getRecipeIngredientsCursor(recipeId);
	}

	@Override
	String getOrCreateItem(String name) {
		return helper.getOrCreateIngredient(name);
	}

	@Override
	boolean addRecipeItem(String itemId) {
		return helper.addRecipeIngredient(recipeId, itemId);
	}

	@Override
	String getItemIdByName(String name) {
		return helper.getIngredientIdByName(name);
	}

	@Override
	boolean removeRecipeItem(String itemId) {
		return helper.removeRecipeIngredient(recipeId, itemId);
	}
}
