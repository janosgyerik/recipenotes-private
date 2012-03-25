package com.mywinenotes;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Spinner;

public class WineDetailsActivity extends Activity {

	// Debugging
	private static final String TAG = "WineDetailsActivity";
	
	private static final String TABLE_NAME = "main_wine";
	
	private SQLiteOpenHelper helper;
	private String pk;

	private EditText nameView;
	private AutoCompleteTextView regionView;

	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wine_details);
		
		nameView = (EditText) findViewById(R.id.name);
		
		ArrayAdapter<String> yearListAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, YEAR_CHOICES);
		yearListAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		Spinner yearView = (Spinner) findViewById(R.id.year);
		yearView.setAdapter(yearListAdapter);

		ArrayAdapter<String> wineTypeListAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, WINE_TYPE_CHOICES);
		wineTypeListAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		Spinner wineTypeView = (Spinner) findViewById(R.id.wine_type);
		wineTypeView.setAdapter(wineTypeListAdapter);
		
		ArrayAdapter<String> regionListAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_dropdown_item_1line, REGION_CHOICES);
		regionView = (AutoCompleteTextView) findViewById(R.id.region);
		regionView.setAdapter(regionListAdapter);

		ArrayAdapter<String> grapeListAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_dropdown_item_1line, GRAPE_CHOICES);
		AutoCompleteTextView grapeView = (AutoCompleteTextView) findViewById(R.id.grape);
		grapeView.setAdapter(grapeListAdapter);

		ArrayAdapter<String> aromaListAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_dropdown_item_1line, TASTE_CHOICES);
		MultiAutoCompleteTextView aromaListView = (MultiAutoCompleteTextView) findViewById(R.id.aroma_list);
		aromaListView.setAdapter(aromaListAdapter);
		aromaListView.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
		
		ArrayAdapter<String> tasteListAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_dropdown_item_1line, TASTE_CHOICES);
		MultiAutoCompleteTextView tasteListView = (MultiAutoCompleteTextView) findViewById(R.id.taste_list);
		tasteListView.setAdapter(tasteListAdapter);
		tasteListView.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
		
		ArrayAdapter<String> afterTasteListAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_dropdown_item_1line, TASTE_CHOICES);
		MultiAutoCompleteTextView afterTasteListView = (MultiAutoCompleteTextView) findViewById(R.id.after_taste_list);
		afterTasteListView.setAdapter(afterTasteListAdapter);
		afterTasteListView.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
		
		helper = new WineNotesSQLiteOpenHelper(this);
		pk = getIntent().getExtras().getString(BaseColumns._ID);
		if (pk != null) {
			Cursor mCursor = helper.getWritableDatabase().query(
					"main_wine", 
					new String[]{ BaseColumns._ID, "name" }, 
					BaseColumns._ID + " = ?", new String[]{ pk }, null, null, null);
			startManagingCursor(mCursor);

			if (mCursor.moveToFirst()) {
				nameView.setText(mCursor.getString(1));
			}
		}
		
		Button save = (Button) findViewById(R.id.btn_save);
		save.setOnClickListener(new SaveButtonOnClickListener());

	}
	
	class SaveButtonOnClickListener implements OnClickListener {

		@Override
		public void onClick(View view) {
			ContentValues values = new ContentValues();
			values.put("name", nameView.getText().toString());
			values.put("region", regionView.getText().toString());

			if (pk == null) {
				long ret = helper.getWritableDatabase().insert(TABLE_NAME, null, values);
				Log.d(TAG, "insert ret = " + ret);
			}
			else {
				int ret = helper.getWritableDatabase().update(TABLE_NAME, values, 
						BaseColumns._ID + " = ?", new String[]{ pk });
				Log.d(TAG, "update ret = " + ret);
			}
		}
		
	}

	
	private static final String[] TASTE_CHOICES = new String[] {
		"Acacia",
		"Acetic acid",
		"Acidic",
		"Aged",
		"Almond",
		"Animal",
		"Apple",
		"Aromatic",
		"Berry Fruits",
		"Blackberry",
		"Blackcurrant",
		"Bread",
		"Brett",
		"Butter",
		"Cedar",
		"Cherry",
		"Chocolate",
		"Cinnamon",
		"Citrus",
		"Clove",
		"Coconut",
		"Coffee",
		"Complex",
		"Corked",
		"Cut grass",
		"Developed",
		"Dill",
		"Dried Herbs",
		"Dry apricot",
		"Earthy",
		"Ethyl acetate",
		"Ethyl-phenol",
		"Eucalyptus",
		"Flint",
		"Floral",
		"Fresh Herbs",
		"Fruity",
		"Grapefruit",
		"Green apple",
		"Green pepper",
		"Guava",
		"Hawthorn",
		"Hay",
		"Hazelnut",
		"Heat",
		"Heavy",
		"Herbaceous",
		"Honey",
		"Intense",
		"Jasmine",
		"Kerosene",
		"Late harvest/Botrytis",
		"Lavender",
		"Leather",
		"Leaves",
		"Lemon",
		"Light",
		"Lime",
		"Linden",
		"Lychee",
		"Madeira",
		"Malolactic fermentation",
		"Mandarine",
		"Melon",
		"Minerals",
		"Mint",
		"Mushrooms",
		"Musk",
		"Nail polish remover",
		"Nutmeg",
		"Nuts",
		"Oak aging",
		"Oak",
		"Old band-aid",
		"Onion",
		"Orange blossom",
		"Orange peel",
		"Oxygen",
		"Passion fruit",
		"Peach",
		"Pear",
		"Pepper",
		"Pine",
		"Pineapple",
		"Plum",
		"Prune",
		"Red berries",
		"Rose",
		"Rotten egg",
		"Rubber",
		"Sandalwood",
		"Sherry",
		"Smoked",
		"Sour",
		"Spicy",
		"Strawberry",
		"Strong",
		"Sulfides",
		"Sweet corn",
		"Sweet",
		"Tar",
		"Thyme",
		"Toast",
		"Tobacco",
		"Tomato",
		"Tree fruits",
		"Tree moss",
		"Trichloroanisole",
		"Tropical fruits",
		"Truffle",
		"Undergrowth",
		"Vanilla",
		"Vegetables",
		"Vinegar",
		"Violet",
		"Volatile acidity",
		"White fruits",
		"Wood",
		"WOW!",
		"Yeast",
		"Yoghurt",
	};
	
	private static final String[] REGION_CHOICES = new String[] {
		"Alsace",
		"Beaujolais",
		"Bordeaux",
		"Burgundy",
		"Champagne",
		"Côtes du Rhône",
		"Cotes du Rhone",
		"Corsica",
		"Jura",
		"Languedoc-Roussillon",
		"Loire Valley",
		"Provence",
		"Rhône",
		"Rhone",
		"Savoy",
		"South West",
		"Bergerac",
		"Dordogne",
		"Garonne ",
		"Cahors",
		"Gascony",
		"Béarn",
		"Bearn",
		"Jurançon",
		"Jurancon",
		"Basque Country",
		"Irouléguy",
		"Irouleguy",
	};
	
	private static final String[] GRAPE_CHOICES = new String[] {
		"Pinot Noir",
		"Cabernet Sauvignon",
		"Shiraz",
		"Zinfandel",
		"Cabernet Franc",
		"Gamay",
		"Merlot",
		"Grenache",
		"Tempranillo",
		"Sangiovese",
		"Nebbiolo",
		"Chardonnay",
		"Sauvignon Blanc",
		"Riesling",
		"Semillon",
		"Gewurztraminer",
		"Muscat",
		"Viognier",
		"Verdelho",
		"Chenin Blanc",
		"Pinot Gris",
		"Colombard",
	};
	
	private static final String[] YEAR_CHOICES = new String[] {
		"2012", "2011", "2010", "2009", "2008", "2007", "2006", "2005",
		"2004", "2003", "2002", "2001", "2000", "1999", "1998", "1997",
	};

	private static final String[] WINE_TYPE_CHOICES = new String[] {
		"red", "white", "rose", "orange",
		"gray", "yellow", "tawny", "other",
	};
	

}
