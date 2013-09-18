package com.twitterapp.activity;

import org.json.JSONObject;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.twitterapp.R;
import com.twitterapp.TwitterApp;
import com.twitterapp.fragment.HomeTimelineFragment;
import com.twitterapp.fragment.MentionsFragment;
import com.twitterapp.model.TwitterAppConsts;
import com.twitterapp.model.User;

public class TimelineActivity extends FragmentActivity implements TabListener {

	private int REQUEST_CODE = 1;
	private User user;
	private HomeTimelineFragment homelineFragment;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_timeline);
		getUser();
		setupNavigation();
	}

	private void getUser() {
		TwitterApp.getRestClient().getCurrentUser(new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int arg0, JSONObject arg1) {
				super.onSuccess(arg0, arg1);
				user = User.fromJson(arg1);
				saveToSharedPreferences();
			}
		});
	}

	protected void saveToSharedPreferences() {
		SharedPreferences pref = getSharedPreferences(TwitterAppConsts.SHARED_PREFERENCE_NAME, MODE_PRIVATE);
		Editor edit = pref.edit();
		edit.putString("screen_name", user.getScreenName());
		edit.commit();
		
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
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
			homelineFragment.loadTweets();
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
		intent.putExtra("screen_name", user.getScreenName());
		startActivity(intent);
	}

	@Override
	public void onTabSelected(Tab tag, FragmentTransaction ft) {
		FragmentManager manager = getSupportFragmentManager();
		android.support.v4.app.FragmentTransaction transaction = manager
				.beginTransaction();
		if (tag.getTag().equals("HomeTimelineFragment")) {
			homelineFragment = new HomeTimelineFragment();
			transaction.replace(R.id.frame_container,
					homelineFragment);
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
