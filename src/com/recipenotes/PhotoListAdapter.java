package com.recipenotes;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class PhotoListAdapter extends BaseAdapter {

	// hardcoded in photolist.xml too, cannot be dynamic
	private static final int NUM_COLUMNS = 3;

	private final Context mContext;
	private final int mAppWidth;

	class RecipePhoto {
		final String recipeId;
		final String filename;

		public RecipePhoto(String recipeId, String filename) {
			this.recipeId = recipeId;
			this.filename = filename;
		}
	}
	List<RecipePhoto> items = new ArrayList<RecipePhoto>();

	public PhotoListAdapter(Context context, int appWidth) {
		mContext = context;
		mAppWidth = appWidth;

		RecipeNotesSQLiteOpenHelper helper =
				new RecipeNotesSQLiteOpenHelper(mContext);

		Cursor photoListCursor = helper.getPhotoListCursor();
		while (photoListCursor.moveToNext()) {
			String recipeId = photoListCursor.getString(0);
			String filename = photoListCursor.getString(1);
			RecipePhoto item = new RecipePhoto(recipeId, filename);
			items.add(item);
		}
		photoListCursor.close();
		helper.close();
	}

	@Override
	public int getCount() {
		return items.size();
	}

	@Override
	public Object getItem(int arg0) {
		return items.get(arg0);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	// create a new ImageView for each item referenced by the Adapter
	public View getView(int position, View convertView, ViewGroup parent) {
		String filename = items.get(position).filename;
		Bitmap bitmap = RecipeFileManager.getSmallPhotoBitmap(filename, mAppWidth);

		ImageView imageView;
		if (convertView == null) {  // if it's not recycled, initialize some attributes
			imageView = new ImageView(mContext);
			int targetWidth = mAppWidth / NUM_COLUMNS - 2;
			int targetHeight = targetWidth * bitmap.getHeight() / bitmap.getWidth();
			imageView.setLayoutParams(new GridView.LayoutParams(targetWidth, targetHeight));
			//imageView.setScaleType(ScaleType.CENTER);
			imageView.setPadding(0, 0, 4, 0);
		} else {
			imageView = (ImageView) convertView;
		}
		imageView.setImageBitmap(bitmap);

		return imageView;
	}
}