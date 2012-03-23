package com.mywinenotes;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.MultiAutoCompleteTextView;

public class WineDetailsActivity extends Activity {

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wine_details);

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_dropdown_item_1line, AROMAS);
		MultiAutoCompleteTextView textView = (MultiAutoCompleteTextView) findViewById(R.id.aromas);
		textView.setAdapter(adapter);
		textView.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
	}

	private static final String[] AROMAS = new String[] {
		"Wow",
		"Fruity",
		"Woody",
		"Mineral",
		"Mushroomy",
		"Acidic",
		"Intense",
		"Strong",
		"Light",
		"Heavy",
		"Cherry",
		"Vanilla",
		"Sweet",
		"Sour",
	};
}
