package com.recipenotes;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class PhotoListAdapter extends BaseAdapter {

	private static final String TAG = "BaseAdapter";

	// hardcoded in photolist.xml too, cannot be dynamic
	private static final int NUM_COLUMNS = 3;
	private static final int ORIGINAL_SIZE_THRESHOLD = 200;

	private final Context mContext;
	private final int mAppWidth;
	private final boolean mUseOriginalSize;

	class RecipePhoto {
		final String recipeId;
		final File photoFile;

		public RecipePhoto(String recipeId, File photoFile) {
			this.recipeId = recipeId;
			this.photoFile = photoFile;
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
			String photoFilename = photoListCursor.getString(1);
			RecipePhoto item = new RecipePhoto(recipeId, RecipeFileManager.getPhotoFile(photoFilename));
			items.add(item);
		}
		photoListCursor.close();
		helper.close();

		mUseOriginalSize = appWidth / 3 >= ORIGINAL_SIZE_THRESHOLD;
		Log.d(TAG, "mUseOriginalSize = " + mUseOriginalSize);
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
		File photoFile = items.get(position).photoFile;
		Bitmap bitmap;
		if (mUseOriginalSize) {
			bitmap = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
		}
		else {
			bitmap = BitmapTools.createScaledBitmap(photoFile, mAppWidth / NUM_COLUMNS);
		}

		ImageView imageView;
		if (convertView == null) {  // if it's not recycled, initialize some attributes
			imageView = new ImageView(mContext);
			int targetWidth = mAppWidth / NUM_COLUMNS - 2;
			int targetHeight = targetWidth * bitmap.getHeight() / bitmap.getWidth();
			imageView.setLayoutParams(new GridView.LayoutParams(targetWidth, targetHeight));
			imageView.setPadding(0, 0, 4, 0);
		} else {
			imageView = (ImageView) convertView;
		}
		imageView.setImageBitmap(bitmap);

		return imageView;
	}
}