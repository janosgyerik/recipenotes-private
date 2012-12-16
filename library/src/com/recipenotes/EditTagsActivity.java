package com.recipenotes;

import android.database.Cursor;

public class EditTagsActivity extends AbstractEditRecipeItemsActivity {

	@Override
	int getContentViewId() {
		return R.layout.edittags;
	}

	@Override
	Cursor getAutoCompleteListCursor() {
		return helper.getTagsListCursor();
	}

	@Override
	Cursor getItemListCursor() {
		return helper.getRecipeTagsCursor(recipeId);
	}

	@Override
	String getOrCreateItem(String name) {
		return helper.getOrCreateTag(name);
	}

	@Override
	boolean addRecipeItem(String itemId) {
		return helper.addRecipeTag(recipeId, itemId);
	}

	@Override
	String getItemIdByName(String name) {
		return helper.getTagIdByName(name);
	}

	@Override
	boolean removeRecipeItem(String itemId) {
		return helper.removeRecipeTag(recipeId, itemId);
	}
}
