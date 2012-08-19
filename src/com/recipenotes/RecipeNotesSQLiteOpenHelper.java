package com.recipenotes;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class RecipeNotesSQLiteOpenHelper extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "sqlite3.db";
	private static final int DATABASE_VERSION = 1;
	private final List<String> sqlCreateStatements;

	RecipeNotesSQLiteOpenHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		
		//context.deleteDatabase(DATABASE_NAME);
		
		sqlCreateStatements = new LinkedList<String>();
		try {
			InputStream instream = context.getAssets().open("sql_create.sql");
			BufferedReader reader = new BufferedReader(new InputStreamReader(instream));
			String line;
			StringBuilder builder = new StringBuilder();
			while ((line = reader.readLine()) != null) {
				builder.append(line);
				if (line.trim().equals(";")) {
					sqlCreateStatements.add(builder.toString());
					builder = new StringBuilder();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		for (String sql : sqlCreateStatements) {
			db.execSQL(sql);
		}
		// dummy recipes
		db.execSQL("insert into main_recipe (name, display_name) values ('Steak', 'Steak');");
		db.execSQL("insert into main_recipe (name, display_name) values ('Pasta', 'Pasta with asparagus');");
		db.execSQL("insert into main_recipe (name, display_name) values ('Cake', 'Cake with Lemon marmalade');");
		
		// initial ingredients collection
		db.execSQL("insert into main_ingredient (name) values ('Avocado');");
		db.execSQL("insert into main_ingredient (name) values ('Walnuts');");
		db.execSQL("insert into main_ingredient (name) values ('Hazelnuts');");
		db.execSQL("insert into main_ingredient (name) values ('Olive oil');");
		db.execSQL("insert into main_ingredient (name) values ('Grapeseed oil');");
		db.execSQL("insert into main_ingredient (name) values ('Egg');");
		db.execSQL("insert into main_ingredient (name) values ('Lemon');");
		db.execSQL("insert into main_ingredient (name) values ('Lime');");
		db.execSQL("insert into main_ingredient (name) values ('Steak');");
		db.execSQL("insert into main_ingredient (name) values ('Rice');");
		db.execSQL("insert into main_ingredient (name) values ('Potato');");
		db.execSQL("insert into main_ingredient (name) values ('Tomato');");
		db.execSQL("insert into main_ingredient (name) values ('Zucchini');");
		db.execSQL("insert into main_ingredient (name) values ('Asparagus');");
		db.execSQL("insert into main_ingredient (name) values ('Lettuce');");
		db.execSQL("insert into main_ingredient (name) values ('Almond');");
		db.execSQL("insert into main_ingredient (name) values ('Cashew nut');");
		db.execSQL("insert into main_ingredient (name) values ('Pepper');");
		db.execSQL("insert into main_ingredient (name) values ('Salt');");
		db.execSQL("insert into main_ingredient (name) values ('Plum tomato');");
		db.execSQL("insert into main_ingredient (name) values ('Cherry tomato');");
		db.execSQL("insert into main_ingredient (name) values ('Pork');");
		db.execSQL("insert into main_ingredient (name) values ('Chicken');");
		db.execSQL("insert into main_ingredient (name) values ('Chicken breast');");
		db.execSQL("insert into main_ingredient (name) values ('Soup stock');");
		db.execSQL("insert into main_ingredient (name) values ('Spinach');");
		db.execSQL("insert into main_ingredient (name) values ('Onion');");
		db.execSQL("insert into main_ingredient (name) values ('Beet');");
		db.execSQL("insert into main_ingredient (name) values ('Red radish');");
		db.execSQL("insert into main_ingredient (name) values ('White radish');");
		db.execSQL("insert into main_ingredient (name) values ('Wasabi');");
		db.execSQL("insert into main_ingredient (name) values ('Goya');");
		db.execSQL("insert into main_ingredient (name) values ('Tofu');");
		db.execSQL("insert into main_ingredient (name) values ('Carrot');");
		db.execSQL("insert into main_ingredient (name) values ('Couscous');");
		db.execSQL("insert into main_ingredient (name) values ('Fish');");
		db.execSQL("insert into main_ingredient (name) values ('Garlic');");
		db.execSQL("insert into main_ingredient (name) values ('Basil paste');");
		db.execSQL("insert into main_ingredient (name) values ('Fresh basil');");
		db.execSQL("insert into main_ingredient (name) values ('Lime leaf');");
		db.execSQL("insert into main_ingredient (name) values ('Laurel');");
		db.execSQL("insert into main_ingredient (name) values ('Coconut milk');");
		db.execSQL("insert into main_ingredient (name) values ('Coconut');");
		db.execSQL("insert into main_ingredient (name) values ('Brown sugar');");
		db.execSQL("insert into main_ingredient (name) values ('White sugar');");
		db.execSQL("insert into main_ingredient (name) values ('Sea salt');");
		db.execSQL("insert into main_ingredient (name) values ('Milk');");
		db.execSQL("insert into main_ingredient (name) values ('Honey');");
		db.execSQL("insert into main_ingredient (name) values ('Cucumber');");
		db.execSQL("insert into main_ingredient (name) values ('Cauliflower');");
		db.execSQL("insert into main_ingredient (name) values ('Flour');");
		db.execSQL("insert into main_ingredient (name) values ('Parmesan cheese');");
		db.execSQL("insert into main_ingredient (name) values ('Gruyere cheese');");
		db.execSQL("insert into main_ingredient (name) values ('Rochefort cheese');");
		db.execSQL("insert into main_ingredient (name) values ('Reblochon cheese');");
		db.execSQL("insert into main_ingredient (name) values ('Peanuts');");
		db.execSQL("insert into main_ingredient (name) values ('Pine nuts');");
		db.execSQL("insert into main_ingredient (name) values ('Acacia honey');");
		
		// initial tags collection
		db.execSQL("insert into main_tag (name) values ('Main');");
		db.execSQL("insert into main_tag (name) values ('Side dish');");
		db.execSQL("insert into main_tag (name) values ('Salad');");
		db.execSQL("insert into main_tag (name) values ('Dessert');");
		db.execSQL("insert into main_tag (name) values ('Dip');");
		db.execSQL("insert into main_tag (name) values ('Cake');");
		db.execSQL("insert into main_tag (name) values ('Shake');");
		db.execSQL("insert into main_tag (name) values ('Appetizer');");
		db.execSQL("insert into main_tag (name) values ('Light');");
		db.execSQL("insert into main_tag (name) values ('Heavy');");
		db.execSQL("insert into main_tag (name) values ('Meat');");
		db.execSQL("insert into main_tag (name) values ('Fish');");
		db.execSQL("insert into main_tag (name) values ('Vegetarian');");
		db.execSQL("insert into main_tag (name) values ('Cheese');");
		db.execSQL("insert into main_tag (name) values ('Pasta');");
		db.execSQL("insert into main_tag (name) values ('Rice');");
		db.execSQL("insert into main_tag (name) values ('French');");
		db.execSQL("insert into main_tag (name) values ('Italian');");
		db.execSQL("insert into main_tag (name) values ('Russian');");
		db.execSQL("insert into main_tag (name) values ('Hungarian');");
		db.execSQL("insert into main_tag (name) values ('Thai');");
		db.execSQL("insert into main_tag (name) values ('Ethnic');");
		db.execSQL("insert into main_tag (name) values ('Fusion');");
		db.execSQL("insert into main_tag (name) values ('Japanese');");
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
	}
}
