package com.recipenotes;

import android.app.Application;

public class RecipeNotesApplication extends Application {
        public boolean isLiteVersion() {
                return getPackageName().toLowerCase().endsWith(".lite"); 
        }
}
