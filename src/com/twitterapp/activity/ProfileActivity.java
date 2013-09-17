package com.twitterapp.activity;

import org.json.JSONObject;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.twitterapp.R;
import com.twitterapp.TwitterApp;
import com.twitterapp.R.id;
import com.twitterapp.R.layout;
import com.twitterapp.R.menu;
import com.twitterapp.fragment.ScreenNameFragment;
import com.twitterapp.fragment.UserTimelineFragment;
import com.twitterapp.model.User;

public class ProfileActivity extends FragmentActivity {

	private String screenName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile);
		setScreenName(getIntent().getStringExtra("screen_name"));
		loadUserlistFragment();
		loadScreenNameFragment();
	}

	private void loadUserlistFragment() {
		FragmentManager manager = getSupportFragmentManager();
		android.support.v4.app.FragmentTransaction transaction = manager
				.beginTransaction();
		Bundle bundle = new Bundle();
		bundle.putString("screen_name", getScreenName());
		UserTimelineFragment userTimelineFragment = new UserTimelineFragment();
		userTimelineFragment.setArguments(bundle);
		transaction.replace(R.id.flUserTimeline, userTimelineFragment);
		transaction.commit();
	}
	
	private void loadScreenNameFragment() {
		FragmentManager manager = getSupportFragmentManager();
		android.support.v4.app.FragmentTransaction transaction = manager
				.beginTransaction();
		Bundle bundle = new Bundle();
		bundle.putString("screen_name", getScreenName());
		ScreenNameFragment screenNameFragment = new ScreenNameFragment();
		screenNameFragment.setArguments(bundle);
		transaction.replace(R.id.flScreenName, screenNameFragment);
		transaction.commit();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.profile, menu);
		return true;
	}

	public String getScreenName() {
		return screenName;
	}

	public void setScreenName(String screenName) {
		this.screenName = screenName;
	}
}
