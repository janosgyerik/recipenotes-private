package com.recipenotes;

import android.content.Intent;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.view.View;
import android.view.View.OnClickListener;

public class ViewRecipeActivity extends AbstractRecipeActivity {

//	private static final String TAG = "ViewRecipeActivity";

	private static final int RETURN_FROM_EDIT = 1;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.viewrecipe);

		View editButton = (View) findViewById(R.id.btn_edit);
		editButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ViewRecipeActivity.this, EditRecipeActivity.class);
				intent.putExtra(BaseColumns._ID, recipeId);
				startActivityForResult(intent, RETURN_FROM_EDIT);
			}
		});

		reloadAndRefreshRecipeDetails(false);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case RETURN_FROM_EDIT:
			if (data != null) {
				Bundle extras = data.getExtras();
				if (extras != null) {
					if (extras.get(EditRecipeActivity.OUT_DELETED) != null) {
						finish();
					}
				}
			}
			reloadAndRefreshRecipeDetails(false);
			break;
		}
	}

}