package com.example.techcampteam01;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphObject;
import com.facebook.model.OpenGraphAction;
import com.facebook.model.OpenGraphObject;
import com.facebook.widget.FacebookDialog;

/**
 * Display result screen of game.
 * 
 * @author ティエプ
 * 
 */

public class ResultScreen extends FragmentActivity {
	private UiLifecycleHelper uiHelper;
	private static final String TAG = "Your App";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		setContentView(R.layout.result_layout);
		super.onCreate(savedInstanceState);
		uiHelper = new UiLifecycleHelper(this, callback);
		uiHelper.onCreate(savedInstanceState);

		Button btnShare = (Button) findViewById(R.id.btn_share);
		btnShare.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				pushstory();

			}
		});
	}

	private void pushstory() {
		OpenGraphObject meal = OpenGraphObject.Factory
				.createForPost("cooking-app:meal");
		meal.setProperty("title", "Buffalo Tacos");
		meal.setProperty("image",
				"http://example.com/cooking-app/images/buffalo-tacos.png");
		meal.setProperty("url",
				"https://example.com/cooking-app/meal/Buffalo-Tacos.html");
		meal.setProperty("description", "Leaner than beef and great flavor.");

		OpenGraphAction action = GraphObject.Factory
				.create(OpenGraphAction.class);
		action.setProperty("meal", meal);

		FacebookDialog shareDialog = new FacebookDialog.OpenGraphActionDialogBuilder(
				this, action, "cooking-app:cook", "meal").build();
		// FacebookDialog shareDialog = new
		// FacebookDialog.OpenGraphActionDialogBuilder(
		// this, action, "meal").build();
		uiHelper.trackPendingDialogCall(shareDialog.present());
	}

	private Session.StatusCallback callback = new Session.StatusCallback() {
		@Override
		public void call(Session session, SessionState state,
				Exception exception) {
			onSessionStateChange(session, state, exception);
		}
	};

	@Override
	public void onResume() {
		super.onResume();
		Session session = Session.getActiveSession();
		if (session != null && (session.isOpened() || session.isClosed())) {
			onSessionStateChange(session, session.getState(), null);
		}
		uiHelper.onResume();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		uiHelper.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onPause() {
		super.onPause();
		uiHelper.onPause();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		uiHelper.onDestroy();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		uiHelper.onSaveInstanceState(outState);
	}

	private void onSessionStateChange(Session session, SessionState state,
			Exception exception) {
		if (state.isOpened()) {
			Log.i(TAG, "Logged in...");
		} else if (state.isClosed()) {
			Log.i(TAG, "Logged out...");
		}
	}
}
