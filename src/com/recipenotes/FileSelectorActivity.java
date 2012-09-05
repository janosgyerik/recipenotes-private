package com.recipenotes;

import java.io.File;
import java.io.FileFilter;

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
	
	public static final String PARAM_TITLE = "TITLE";
	public static final String PARAM_DIRPARAM = "DIRPARAM";
	public static final String PARAM_PATTERN = "PATTERN";
	
	protected static final String RESULT_FILENAME = "filename";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (D) Log.d(TAG, "+++ ON CREATE +++");
		
		setContentView(R.layout.fileselector);
		
		Bundle extras = getIntent().getExtras();
		
		final String title = extras.getString(PARAM_TITLE);
		final String dirparam = extras.getString(PARAM_DIRPARAM);
		final String pattern = extras.getString(PARAM_PATTERN);
		
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
		}

		setListAdapter(adapter);
		getListView().setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
                String filename = adapter.getItem(arg2);
                
                Intent intent = new Intent();
                intent.putExtra(RESULT_FILENAME, filename);

                // Set result and finish this Activity
                setResult(Activity.RESULT_OK, intent);
                finish();
			}
		});
	}
	
	public static boolean isExternalStorageWritable() {
		return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
	}

}
