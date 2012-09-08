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
	public static final File BACKUPS_DIR = new File(Environment.getExternalStorageDirectory(), BACKUPS_DIRPARAM);
	public static final String PHOTOS_DIRPARAM = "RecipeNotes/photos";
	public static final File PHOTOS_DIR = new File(Environment.getExternalStorageDirectory(), PHOTOS_DIRPARAM);

	public static final String BACKUPFILE_FORMAT = "";
	public static final String BACKUPFILES_PATTERN = "^sqlite3-.*\\.db$";
	public static final String DAILY_BACKUPFILE = "sqlite3-daily.db";

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

	private static String getDatabasePath() {
		String packageName = "com.recipenotes";
		String dbname = "sqlite3.db";
		return String.format("/data/%s/databases/%s", packageName, dbname);
	}

	public static boolean backupDatabaseFile() throws IOException {
		String filename = String.format("sqlite3-%s.db", new SimpleDateFormat("yyyyMMdd-HHmmss").format(new Date()));
		return backupDatabaseFile(filename);
	}

	private static boolean backupDatabaseFile(String filename) throws IOException {
		File sd = Environment.getExternalStorageDirectory();
		File data = Environment.getDataDirectory();

		if (sd.canWrite()) {
			File backupDir = new File(sd, BACKUPS_DIRPARAM);
			if (! backupDir.isDirectory()) {
				backupDir.mkdirs();
			}
			File currentDB = new File(data, getDatabasePath());
			File backupFile = new File(backupDir, filename);

			FileChannel src = new FileInputStream(currentDB).getChannel();
			FileChannel dst = new FileOutputStream(backupFile).getChannel();
			dst.transferFrom(src, 0, src.size());
			src.close();
			dst.close();
			return true;
		}
		return false;
	}

	public static boolean restoreDatabaseFile(String filename) throws IOException {
		File sd = Environment.getExternalStorageDirectory();
		File data = Environment.getDataDirectory();

		if (sd.canWrite()) {
			File currentFile = new File(data, getDatabasePath());
			File backupFile = new File(BACKUPS_DIR, filename);

			FileChannel src = new FileInputStream(backupFile).getChannel();
			FileChannel dst = new FileOutputStream(currentFile).getChannel();
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

	private static Date lastDailyBackup;

	/**
	 * Update daily database backup, only once a day.
	 */
	public static void updateDailyBackup() {
		Log.d(TAG, "lastDailyBackup " + lastDailyBackup);

		Date now = new Date();
		if (lastDailyBackup == null || lastDailyBackup.getDay() < now.getDay()) {
			try {
				backupDatabaseFile(DAILY_BACKUPFILE);
				lastDailyBackup = now;
				Log.d(TAG, "Updated daily backup");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static boolean isExternalStorageWritable() {
		return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
	}

}
