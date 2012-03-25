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
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.Toast;

public class WineDetailsActivity extends Activity {

	// Debugging
	private static final String TAG = "WineDetailsActivity";
	
	private static final String TABLE_NAME = "main_wine";

	private SQLiteOpenHelper helper;
	private String pk;

	private EditText nameView;
	private Spinner yearView;
	private Spinner wineTypeView;
	private Spinner buyFlagView;
	private AutoCompleteTextView regionView;
	private AutoCompleteTextView grapeView;
	private RatingBar aromaRatingView;
	private RatingBar tasteRatingView;
	private RatingBar afterTasteRatingView;
	private RatingBar overallRatingView;
	private MultiAutoCompleteTextView aromaListView;
	private MultiAutoCompleteTextView tasteListView;
	private MultiAutoCompleteTextView afterTasteListView;

	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wine_details);
		
		nameView = (EditText) findViewById(R.id.name);
		
		ArrayAdapter<String> yearListAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, YEAR_CHOICES);
		yearListAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		yearView = (Spinner) findViewById(R.id.year);
		yearView.setAdapter(yearListAdapter);

		ArrayAdapter<String> wineTypeListAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, WINE_TYPE_CHOICES);
		wineTypeListAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		wineTypeView = (Spinner) findViewById(R.id.wine_type);
		wineTypeView.setAdapter(wineTypeListAdapter);
		
		ArrayAdapter<String> buyFlagListAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, BUY_FLAG_CHOICES);
		buyFlagListAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		buyFlagView = (Spinner) findViewById(R.id.buy_flag);
		buyFlagView.setAdapter(buyFlagListAdapter);
		
		ArrayAdapter<String> regionListAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_dropdown_item_1line, REGION_CHOICES);
		regionView = (AutoCompleteTextView) findViewById(R.id.region);
		regionView.setAdapter(regionListAdapter);

		ArrayAdapter<String> grapeListAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_dropdown_item_1line, GRAPE_CHOICES);
		grapeView = (AutoCompleteTextView) findViewById(R.id.grape);
		grapeView.setAdapter(grapeListAdapter);
		
		aromaRatingView = (RatingBar)findViewById(R.id.rating_aroma);
		tasteRatingView = (RatingBar)findViewById(R.id.rating_taste);
		afterTasteRatingView = (RatingBar)findViewById(R.id.rating_after_taste);
		overallRatingView = (RatingBar)findViewById(R.id.rating_overall);

		ArrayAdapter<String> aromaListAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_dropdown_item_1line, TASTE_CHOICES);
		aromaListView = (MultiAutoCompleteTextView) findViewById(R.id.aroma_list);
		aromaListView.setAdapter(aromaListAdapter);
		aromaListView.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
		
		ArrayAdapter<String> tasteListAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_dropdown_item_1line, TASTE_CHOICES);
		tasteListView = (MultiAutoCompleteTextView) findViewById(R.id.taste_list);
		tasteListView.setAdapter(tasteListAdapter);
		tasteListView.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
		
		ArrayAdapter<String> afterTasteListAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_dropdown_item_1line, TASTE_CHOICES);
		afterTasteListView = (MultiAutoCompleteTextView) findViewById(R.id.after_taste_list);
		afterTasteListView.setAdapter(afterTasteListAdapter);
		afterTasteListView.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
		
		helper = new WineNotesSQLiteOpenHelper(this);
		pk = getIntent().getExtras().getString(BaseColumns._ID);
		if (pk != null) {
			Cursor mCursor = helper.getWritableDatabase().query(
					"main_wine", 
					new String[]{ 
							BaseColumns._ID, "name", "year", "wine_type", "buy_flag", 
							"region", "grape",
							"aroma", "taste", "after_taste", "overall",
							"aroma_list", "taste_list", "after_taste_list",
							}, 
					BaseColumns._ID + " = ?", new String[]{ pk }, null, null, null);
			startManagingCursor(mCursor);

			if (mCursor.moveToFirst()) {
				String buyFlag = mCursor.getString(4);
				if (buyFlag == null) {
					buyFlag = "1";
				}
				if (buyFlag.equals("0")) {
					buyFlag = "Buy";
				}
				else if (buyFlag.equals("1")) {
					buyFlag = "Maybe";
				}
				else if (buyFlag.equals("2")) {
					buyFlag = "-";
				}
				else if (buyFlag.equals("3")) {
					buyFlag = "Never";
				}

				nameView.setText(mCursor.getString(1));
				setSpinnerValue(yearView, mCursor.getString(2), YEAR_CHOICES);
				setSpinnerValue(wineTypeView, mCursor.getString(3), WINE_TYPE_CHOICES);
				setSpinnerValue(buyFlagView, buyFlag, BUY_FLAG_CHOICES);
				regionView.setText(mCursor.getString(5));
				grapeView.setText(mCursor.getString(6));
				aromaRatingView.setRating(mCursor.getInt(7));
				tasteRatingView.setRating(mCursor.getInt(8));
				afterTasteRatingView.setRating(mCursor.getInt(9));
				overallRatingView.setRating(mCursor.getInt(10));
				aromaListView.setText(mCursor.getString(11));
				tasteListView.setText(mCursor.getString(12));
				afterTasteListView.setText(mCursor.getString(13));
			}
		}
		
		Button save = (Button) findViewById(R.id.btn_save);
		save.setOnClickListener(new SaveButtonOnClickListener());
	}
	
	void setSpinnerValue(Spinner spinner, String value, String[] choices) {
		int position = 0;
		for (String choice : choices) {
			if (choice.equals(value)) {
				spinner.setSelection(position);
				return;
			}
			++position;
		}
	}
	
	class SaveButtonOnClickListener implements OnClickListener {

		@Override
		public void onClick(View view) {
			ContentValues values = new ContentValues();
			String name = capitalize(nameView.getText().toString());
			String year = yearView.getSelectedItem().toString();
			String wineType = capitalize(wineTypeView.getSelectedItem().toString());
			String buyFlag;
			switch (buyFlagView.getSelectedItemPosition()) {
			case 0:
				buyFlag = "2"; // not this time
				break;
			case 1:
				buyFlag = "0"; // buy
				break;
			case 2:
				buyFlag = "1"; // maybe
				break;
			default:
				buyFlag = "3"; // never
			}
			String region = capitalize(regionView.getText().toString());
			String grape = capitalize(grapeView.getText().toString());
			float aromaRating = aromaRatingView.getRating();
			float tasteRating = tasteRatingView.getRating();
			float afterTasteRating = afterTasteRatingView.getRating();
			float overallRating = overallRatingView.getRating();
			String aromaList = aromaListView.getText().toString();
			String tasteList = tasteListView.getText().toString();
			String afterTasteList = afterTasteListView.getText().toString();
			
			values.put("name", name);
			values.put("year", year);
			values.put("wine_type", wineType);
			values.put("buy_flag", buyFlag);
			values.put("region", region);
			values.put("grape", grape);
			values.put("aroma", aromaRating);
			values.put("taste", tasteRating);
			values.put("after_taste", afterTasteRating);
			values.put("overall", overallRating);
			values.put("aroma_list", aromaList);
			values.put("taste_list", tasteList);
			values.put("after_taste_list", afterTasteList);

			if (pk == null) {
				long ret = helper.getWritableDatabase().insert(TABLE_NAME, null, values);
				Log.d(TAG, "insert ret = " + ret);
				if (ret >= 0) {
					pk = String.valueOf(ret);
					Toast.makeText(getApplicationContext(), "Successfully added new wine", Toast.LENGTH_SHORT).show();
				}
				else {
					Toast.makeText(getApplicationContext(), "Error adding new wine", Toast.LENGTH_SHORT).show();
				}
			}
			else {
				int ret = helper.getWritableDatabase().update(TABLE_NAME, values, 
						BaseColumns._ID + " = ?", new String[]{ pk });
				Log.d(TAG, "update ret = " + ret);
				if (ret == 1) {
					Toast.makeText(getApplicationContext(), "Successfully updated wine", Toast.LENGTH_SHORT).show();
				}
				else {
					Toast.makeText(getApplicationContext(), "Error updating wine", Toast.LENGTH_SHORT).show();
				}
			}
			finish();
		}
	}
	
	static String capitalize(String name) {
		if (name == null || name.length() < 1) return name;
		return Character.toUpperCase(name.charAt(0)) + name.substring(1);
	}
	
	
	private static final String[] TASTE_CHOICES = new String[] {
		"Acacia",
		"Acidic",
		"Acetic acid",
		"Aged",
		"Alcohol",
		"Almond",
		"Animal",
		"Apple",
		"Aromatic",
		"Fruity",
		"Berry",
		"Blackberry",
		"Blackcurrant",
		"Bland",
		"Bread",
		"Brett",
		"Butter",
		"Cedar",
		"Character",
		"Cheese",
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
		"Interesting",
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
		"Mature",
		"Medium",
		"Melon",
		"Mild",
		"Minerals",
		"Mint",
		"Mushrooms",
		"Musk",
		"Nail polish remover",
		"Nice",
		"Nutmeg",
		"Nuts",
		"Oak aging",
		"Oak",
		"Old",
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
		"Pleasant",
		"Plum",
		"Powerful",
		"Prune",
		"Pungent",
		"Red berries",
		"Rich",
		"Rose",
		"Rotten egg",
		"Round",
		"Rubber",
		"Sandalwood",
		"Sherry",
		"Smoked",
		"Soft",
		"Sour",
		"Spicy",
		"Stinging",
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
		"Uninteresting",
		"Vanilla",
		"Vegetables",
		"Vinegar",
		"Violet",
		"Volatile acidity",
		"White fruits",
		"Wholesome",
		"Wood",
		"WOW!",
		"Yeast",
		"Yoghurt",
		"Young",
	};
	
	private static final String[] REGION_CHOICES = new String[] {
		"Alsace",
		"Beaujolais",
		"Bordeaux",
		"Burgundy",
		"Champagne",
		"C™tes du Rh™ne",
		"Cotes du Rhone",
		"Corsica",
		"Jura",
		"Languedoc-Roussillon",
		"Loire Valley",
		"Provence",
		"Rh™ne",
		"Rhone",
		"Savoy",
		"South West",
		"Bergerac",
		"Dordogne",
		"Garonne ",
		"Cahors",
		"Gascony",
		"BŽarn",
		"Bearn",
		"Juranon",
		"Jurancon",
		"Basque Country",
		"IroulŽguy",
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
	
	private static final String[] BUY_FLAG_CHOICES = new String[] {
		"-", "Buy", "Maybe", "Never",
	};
	

}
