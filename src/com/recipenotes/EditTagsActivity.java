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

public class EditTagsActivity extends ListActivity {

	// Debugging
	private static final String TAG = "EditTagsActivity";
	private static final boolean D = false;

	public static final String OUT_CHANGED = "CHANGED";

	private RecipeNotesSQLiteOpenHelper helper;
	private String recipeId;

	private MultiAutoCompleteTextView tagView;
	private ArrayAdapter<String> tagsListAdapter;
	private ListView tagsListView;

	private boolean isChanged;

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
//			recipeId = "44";
		}

		// add tag
		// TODO store id too
		ArrayList<String> tagsAutoCompleteList = new ArrayList<String>();
		{
			Cursor tagsCursor = helper.getTagsListCursor();
			int i = tagsCursor.getColumnIndex("name");
			while (tagsCursor.moveToNext()) {
				tagsAutoCompleteList.add(tagsCursor.getString(i));
			}
			tagsCursor.close();
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
		while (tagsCursor.moveToNext()) {
			String tag = tagsCursor.getString(0);
			tagsListAdapter.add(tag);
		}
		tagsCursor.close();

		setListAdapter(tagsListAdapter);
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

	private void addTags() {
		String tags = tagView.getText().toString().trim();
		if (tags.length() > 0) {
			StringBuffer tagsListMsgBuffer = new StringBuffer();
			for (String tag : tags.split(",")) {
				tag = EditRecipeActivity.capitalize(tag);
				tagsListMsgBuffer.append(tag);
				tagsListMsgBuffer.append(", ");
				String tagId = helper.getOrCreateTag(tag);
				if (tagId != null
						&& helper.addRecipeTag(recipeId, tagId)) {
					tagsListAdapter.insert(tag, 0);
					isChanged = true;
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
			isChanged = true;
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

	@Override  
	protected void onDestroy() {
		Log.d(TAG, "++onDestroy");
		super.onDestroy();
		helper.close();
	}
}
