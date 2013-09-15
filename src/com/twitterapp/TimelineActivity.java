package com.twitterapp;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.query.Select;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.twitterapp.models.Tweet;
import com.twitterapp.models.User;

public class TimelineActivity extends FragmentActivity implements TabListener {

	private int REQUEST_CODE = 1;

	private User user;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_timeline);
		// loadTweets();
		setupNavigation();
	}

	private void setupNavigation() {
		ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		actionBar.setDisplayShowTitleEnabled(true);
		Tab tabHome = actionBar.newTab().setText("Home")
				.setTag("HomeTimelineFragment").setIcon(R.drawable.ic_home)
				.setTabListener(this);
		Tab tabMentions = actionBar.newTab().setText("Mentions")
				.setTag("MentionsTimelineFragment")
				.setIcon(R.drawable.ic_action_mentions).setTabListener(this);
		actionBar.addTab(tabHome);
		actionBar.addTab(tabMentions);
		actionBar.selectTab(tabHome);
		// TODO Auto-generated method stub

	}

	private void saveUserToSharedPreferences(User user) {
		if (user != null) {
			SharedPreferences pref = PreferenceManager
					.getDefaultSharedPreferences(this);
			Editor edit = pref.edit();
			edit.putString("screenName", user.getScreenName());
			edit.putString("profileImage", user.getProfileImageUrl());
			edit.commit();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
			// loadTweets();
		}
	}

	// Inflate the menu; this adds items to the action bar if it is present.
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.timeline, menu);
		return true;
	}

	private boolean isNetworkAvailable() {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if (netInfo != null && netInfo.isConnectedOrConnecting()) {
			return true;
		}
		return false;
	}

	public void onComposeTweet(MenuItem item) {
		if (isNetworkAvailable()) {
			Intent intent = new Intent(this, ComposeActivity.class);
			startActivityForResult(intent, REQUEST_CODE);
		} else {
			Toast.makeText(getBaseContext(),
					"Please connect to a network connection before composing",
					Toast.LENGTH_LONG).show();
		}
	}

	public void onProfileView(MenuItem mi) {
		Intent intent = new Intent(this, ProfileActivity.class);
		startActivity(intent);
	}

	@Override
	public void onTabSelected(Tab tag, FragmentTransaction ft) {
		FragmentManager manager = getSupportFragmentManager();
		android.support.v4.app.FragmentTransaction transaction = manager
				.beginTransaction();
		if (tag.getTag().equals("HomeTimelineFragment")) {
			transaction.replace(R.id.frame_container,
					new HomeTimelineFragment());
		} else {
			transaction.replace(R.id.frame_container, new MentionsFragment());
		}
		transaction.commit();
	}

	@Override
	public void onTabUnselected(Tab arg0, FragmentTransaction arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTabReselected(Tab arg0, FragmentTransaction arg1) {
		// TODO Auto-generated method stub

	}
}
