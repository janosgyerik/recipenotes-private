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
		
		ArrayAdapter<String> yearAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, new String[]{
				"2012", "2011", "2010", "2009", "2008", "2007", "2006", "2005",
				"2004", "2003", "2002", "2001", "2000", "1999", "1998", "1997",
				});
		yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		Spinner yearView = (Spinner) findViewById(R.id.year);
		yearView.setAdapter(yearAdapter);

		ArrayAdapter<String> wineTypeAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, new String[]{
				"Red", "White", "Rose", "Other",
				});
		wineTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		Spinner wineTypeView = (Spinner) findViewById(R.id.wine_type);
		wineTypeView.setAdapter(wineTypeAdapter);
		
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
}
