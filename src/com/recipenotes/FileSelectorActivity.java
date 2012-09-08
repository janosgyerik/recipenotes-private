package com.recipenotes;

import java.io.File;
import java.io.FileFilter;
import java.util.Comparator;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;

public class FileSelectorActivity extends ListActivity {

	// Debugging
	private static final String TAG = "FileSelectorActivity";
	private static final boolean D = false;

	public static final String IN_TITLE = "TITLE";
	public static final String IN_DIRPARAM = "DIRPARAM";
	public static final String IN_PATTERN = "PATTERN";
	public static final String IN_ORDER = "ORDER";
	
	public static final String ORDER_ABC = "ABC";
	public static final String ORDER_ZYX = "ZYX";

	protected static final String OUT_FILENAME = "filename";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (D) Log.d(TAG, "+++ ON CREATE +++");

		setContentView(R.layout.fileselector);

		Bundle extras = getIntent().getExtras();

		final String title = extras.getString(IN_TITLE);
		final String dirparam = extras.getString(IN_DIRPARAM);
		final String pattern = extras.getString(IN_PATTERN);
		final String order = extras.getString(IN_ORDER);
		
		Comparator<String> comparator;
		if (order != null && order.equals(ORDER_ZYX)) {
			comparator = new ReverseAlphabeticComparator();
		}
		else {
			comparator = new AlphabeticComparator();
		}

		setTitle(title);

		final ArrayAdapter<String> adapter =
				new ArrayAdapter<String>(this, R.layout.filename);

		File externalDir =
				new File(Environment.getExternalStorageDirectory(), dirparam);

		FileFilter fileFilter = new FileFilter() {
			@Override
			public boolean accept(File file) {
				return file.getName().matches(pattern);
			}
		};

		if (externalDir.isDirectory()) {
			for (File file : externalDir.listFiles(fileFilter)) {
				adapter.add(file.getName());
			}
			adapter.sort(comparator);
		}

		setListAdapter(adapter);
		getListView().setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				String filename = adapter.getItem(arg2);

				Intent intent = new Intent();
				intent.putExtra(OUT_FILENAME, filename);

				// Set result and finish this Activity
				setResult(Activity.RESULT_OK, intent);
				finish();
			}
		});
	}

	public static boolean isExternalStorageWritable() {
		return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
	}

	class AlphabeticComparator implements Comparator<String> {
		@Override
		public int compare(String lhs, String rhs) {
			return lhs.compareTo(rhs);
		}
	}

	class ReverseAlphabeticComparator implements Comparator<String> {
		@Override
		public int compare(String lhs, String rhs) {
			return rhs.compareTo(lhs);
		}
	}
}
