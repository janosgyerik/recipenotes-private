package com.recipenotes;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

public class BitmapTools {
	
	private static final String TAG = "BitmapTools";
	
	public static Bitmap createScaledBitmap(File file, int appWidth) {
		String path = file.getAbsolutePath();
		Bitmap bitmap = BitmapFactory.decodeFile(path);
		Log.d(TAG, String.format("dstWidth=%d srcWidth=%d", appWidth, bitmap.getWidth()));
		int dstHeight = appWidth * bitmap.getHeight() / bitmap.getWidth();
		Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, appWidth, dstHeight, false);
		if (!scaledBitmap.equals(bitmap)) {
			bitmap.recycle();
			bitmap = null;
		}
		return scaledBitmap;
	}
	
	public static void createResized(File inFile, File outFile, int targetWidth) {
		String path = inFile.getAbsolutePath();
		Bitmap bitmap = BitmapFactory.decodeFile(path);
		Log.d(TAG, String.format("dstWidth=%d srcWidth=%d", targetWidth, bitmap.getWidth()));
		int dstHeight = targetWidth * bitmap.getHeight() / bitmap.getWidth();
		Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, targetWidth, dstHeight, false);
		if (!scaledBitmap.equals(bitmap)) {
			bitmap.recycle();
			bitmap = null;
		}
		FileOutputStream out;
		try {
			out = new FileOutputStream(outFile);
			scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
		} catch (FileNotFoundException e) {
			Log.e(TAG, "could not save resized photo: " + outFile);
			e.printStackTrace();
		}
	}
}
