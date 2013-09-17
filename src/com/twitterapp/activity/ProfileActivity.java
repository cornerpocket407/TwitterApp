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
		getUser();
	}

	private void loadUserlistFragment() {
		FragmentManager manager = getSupportFragmentManager();
		android.support.v4.app.FragmentTransaction transaction = manager
				.beginTransaction();
		Bundle bundle = new Bundle();
		bundle.putString("screen_name", getScreenName());
		UserTimelineFragment userTimelineFragment = new UserTimelineFragment();
		userTimelineFragment.setArguments(bundle);
		transaction.replace(R.id.frame_container, userTimelineFragment);
		transaction.commit();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.profile, menu);
		return true;
	}

	private void getUser() {
		TwitterApp.getRestClient().getUser(getScreenName(),
				new JsonHttpResponseHandler() {
					@Override
					public void onSuccess(int arg0, JSONObject arg1) {
						super.onSuccess(arg0, arg1);
						User user = User.fromJson(arg1);
						getActionBar().setTitle("@" + user.getName());
//						populateProfileHeader(user);
					}
				});
		// saveUserToSharedPreferences(user);
	}

//	protected void populateProfileHeader(User user) {
//		TextView tvName = (TextView) findViewById(R.id.tvName);
//		tvName.setText(user.getName());
//		TextView tvTagline = (TextView) findViewById(R.id.tvTagline);
//		tvTagline.setText(user.getTagline());
//		TextView tvFollowers = (TextView) findViewById(R.id.tvFollowers);
//		tvFollowers.setText(user.getFollowersCount() + "Followers");
//		TextView tvFollowing = (TextView) findViewById(R.id.tvFollowing);
//		tvFollowing.setText(user.getFollowersCount() + "Following");
//		ImageLoader.getInstance().displayImage(user.getProfileImageUrl(),
//				(ImageView) findViewById(R.id.ivProfileImage));
//	}

	public String getScreenName() {
		return screenName;
	}

	public void setScreenName(String screenName) {
		this.screenName = screenName;
	}
}
