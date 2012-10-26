package com.recipenotes;

import java.util.ArrayList;

import android.app.ListActivity;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Toast;

public class EditTagsActivity extends ListActivity {

	// Debugging
	private static final String TAG = "EditTagsActivity";
	private static final boolean D = false;
	
	private RecipeNotesSQLiteOpenHelper helper;
	private String recipeId;

	private MultiAutoCompleteTextView tagView;
	private ArrayAdapter<String> tagsListAdapter;
	private ListView tagsListView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edittags);
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
		
		// add tag
		// TODO store id too
		ArrayList<String> tagsAutoCompleteList = new ArrayList<String>();
		{
			Cursor tagsCursor = helper.getTagsListCursor();
			startManagingCursor(tagsCursor);

			int i = tagsCursor.getColumnIndex("name");
			while (tagsCursor.moveToNext()) {
				tagsAutoCompleteList.add(tagsCursor.getString(i));
			}
		}

		ArrayAdapter<String> tagsAutoCompleteAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_dropdown_item_1line, tagsAutoCompleteList);
		tagView = (MultiAutoCompleteTextView) findViewById(R.id.tag);
		tagView.setAdapter(tagsAutoCompleteAdapter);
		tagView.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());

		tagsListAdapter =
				new ArrayAdapter<String>(this, R.layout.tagslist_item);
		tagsListView = (ListView) findViewById(android.R.id.list);
		tagsListView.setAdapter(tagsListAdapter);
		tagsListView.setOnItemLongClickListener(new TagListOnItemLongClickListener());

		Button addTagButton = (Button) findViewById(R.id.btn_add_tag);
		addTagButton.setOnClickListener(new AddTagOnClickListener());

		Cursor tagsCursor = helper.getRecipeTagsCursor(recipeId);
		startManagingCursor(tagsCursor);
		while (tagsCursor.moveToNext()) {
			String tag = tagsCursor.getString(0);
			tagsListAdapter.add(tag);
		}
		
		setListAdapter(tagsListAdapter);
	}
	
	/*
	private void returnResult(String filename) {
		Intent intent = new Intent();
		intent.putExtra(OUT_CHANGED, filename);
		setResult(Activity.RESULT_OK, intent);
		finish();
	}
	*/

	private void addTags() {
		String tags = tagView.getText().toString().trim();
		if (tags.length() > 0) {
			StringBuffer tagsListMsgBuffer = new StringBuffer();
			for (String tag : tags.split(",")) {
				tag = ClutteredEditRecipeActivity.capitalize(tag);
				tagsListMsgBuffer.append(tag);
				tagsListMsgBuffer.append(", ");
				String tagId = helper.getOrCreateTag(tag);
				if (tagId != null
						&& helper.addRecipeTag(recipeId, tagId)) {
					tagsListAdapter.insert(tag, 0);
				}
			}
			tagView.setText("");
			String tagsListMsg = tagsListMsgBuffer.substring(0, tagsListMsgBuffer.lastIndexOf(","));
			Toast.makeText(getApplicationContext(), "Added " + tagsListMsg, Toast.LENGTH_SHORT).show();
		}
	}

	private void removeTag(String tagName) {
		String tagId = helper.getTagIdByName(tagName);
		if (tagId != null
				&& helper.removeRecipeTag(recipeId, tagId)) {
			tagsListAdapter.remove(tagName);
		}
	}

	class AddTagOnClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			addTags();
		}
	}

	class TagListOnItemLongClickListener implements OnItemLongClickListener {
		@Override
		public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
				int arg2, long arg3) {
			String tagName = tagsListAdapter.getItem(arg2);
			removeTag(tagName);
			return true;
		}
	}

}
