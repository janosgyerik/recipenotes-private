package com.recipenotes;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.os.Environment;
import android.util.Log;

public class RecipeFileManager {

	// Debugging
	private static final String TAG = "RecipeFileManager";

	public static final String BACKUPS_DIRPARAM = "RecipeNotes/backups";
	public static final String PHOTOS_DIRPARAM = "RecipeNotes/photos";
	public static final File PHOTOS_DIR = new File(Environment.getExternalStorageDirectory(), PHOTOS_DIRPARAM);

	public static final String BACKUPFILE_FORMAT = "";
	public static final String BACKUPFILES_PATTERN = "^sqlite3-.*\\.db$";

	public static final String RECIPE_PHOTOFILE_FORMAT = "recipe_%s_%d.jpg";
	public static final String RECIPE_PHOTOFILES_PATTERN = "^recipe_%s_.*";

	public static boolean deleteRecipe(String recipeId) {
		File storageDir = new File(Environment.getExternalStorageDirectory(), PHOTOS_DIRPARAM);
		if (storageDir.isDirectory()) {
			String pattern = String.format(RECIPE_PHOTOFILES_PATTERN, recipeId);
			FileFilter fileFilter = new PatternFileFilter(pattern);
			for (File file : storageDir.listFiles(fileFilter)) {
				Log.d(TAG, "deleting photo: " + file);
				file.delete();
			}
		}
		return true;
	}

	public static boolean backupDatabaseFile() throws IOException {
		File sd = Environment.getExternalStorageDirectory();
		File data = Environment.getDataDirectory();

		if (sd.canWrite()) {
			String packageName = "com.recipenotes";
			String dbname = "sqlite3.db";
			String backupName = String.format("sqlite3-%s.db", new SimpleDateFormat("yyyyMMdd-HHmmss").format(new Date()));
			String currentDBPath = String.format("/data/%s/databases/%s", packageName, dbname);
			File backupDir = new File(sd, BACKUPS_DIRPARAM);
			if (! backupDir.isDirectory()) {
				backupDir.mkdirs();
			}
			File currentDB = new File(data, currentDBPath);
			File backupFile = new File(backupDir, backupName);

			FileChannel src = new FileInputStream(currentDB).getChannel();
			FileChannel dst = new FileOutputStream(backupFile).getChannel();
			dst.transferFrom(src, 0, src.size());
			src.close();
			dst.close();
			return true;
		}
		return false;
	}

	public static File getPhotoFile(String filename) {
		return new File(PHOTOS_DIR, filename);
	}

	public static File newPhotoFile(String recipeId) throws IOException {
		return File.createTempFile(String.format("recipe_%s_", recipeId),
				".jpg", PHOTOS_DIR);
	}

}
