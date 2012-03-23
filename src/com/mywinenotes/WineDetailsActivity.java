package com.mywinenotes;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Spinner;

public class WineDetailsActivity extends Activity {

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wine_details);
		
		Spinner yearView = (Spinner) findViewById(R.id.year);
		ArrayAdapter<String> yearAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, new String[]{
				"2012", "2011", "2010", "2009", "2008", "2007", "2006", "2005",
				"2004", "2003", "2002", "2001", "2000", "1999", "1998", "1997",
				});
		yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		yearView.setAdapter(yearAdapter);

		Spinner wineTypeView = (Spinner) findViewById(R.id.wine_type);
		ArrayAdapter<String> wineTypeAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, new String[]{
				"Red", "White", "Rose", "Other",
				});
		wineTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		wineTypeView.setAdapter(wineTypeAdapter);
		
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
