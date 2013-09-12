package com.twitterapp;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.query.Select;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.twitterapp.models.Tweet;
import com.twitterapp.models.User;

public class TimelineActivity extends Activity {

	private int REQUEST_CODE = 1;
	private ArrayList<Tweet> tweets;
	private User user;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_timeline);
		getUser();
		loadTweets();
	}

	private void getUser() {
		if (isNetworkAvailable()) {
			TwitterApp.getRestClient().getUserInfo(
					new JsonHttpResponseHandler() {
						@Override
						public void onSuccess(JSONObject jsonObject) {
							user = User.fromJson(jsonObject);
							setTitle("@" + user.getName());
						}
					});
		} else {
			User dbUser = new Select().from(User.class).executeSingle();
			if (dbUser != null) {
				user = dbUser;
				setTitle("@" + user.getName());
			}
		}
	}

	private boolean isNetworkAvailable() {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if (netInfo != null && netInfo.isConnectedOrConnecting()) {
			return true;
		}
		return false;
	}

	private void loadTweets() {
		boolean networkAvailable = isNetworkAvailable();
		if (networkAvailable) {
			TwitterApp.getRestClient().getHomeTimeline(
					new JsonHttpResponseHandler() {
						@Override
						public void onSuccess(JSONArray jsonTweets) {
							tweets = Tweet.fromJson(jsonTweets);
							ActiveAndroid.beginTransaction();
							try {
								User dbUser = new Select()
										.from(User.class)
										.where("twitter_user_id = ?",
												user.getTwitterUserId())
										.executeSingle();
								if (dbUser == null) {
									dbUser = user;
									dbUser.save();
								}
								for (Tweet tweet : tweets) {
									Tweet dbTweet = new Select()
											.from(Tweet.class)
											.where("twitter_id = ?",
													tweet.getTwitterId())
											.executeSingle();
									if (dbTweet == null) {
										tweet.setUser(dbUser);
										tweet.save();
									}
								}
								ActiveAndroid.setTransactionSuccessful();
							} finally {
								ActiveAndroid.endTransaction();
							}
							populateTweetsAdapter();
							super.onSuccess(jsonTweets);
						}
					});
		} else {
			tweets = new Select().from(Tweet.class).orderBy("id").limit("25")
					.execute();
			populateTweetsAdapter();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
			loadTweets();
		}
	}

	private void populateTweetsAdapter() {
		ListView lvTweets = (ListView) findViewById(R.id.lvTweets);
		TweetsAdapter adapter = new TweetsAdapter(getBaseContext(), tweets);
		lvTweets.setAdapter(adapter);
	}

	// Inflate the menu; this adds items to the action bar if it is present.
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.timeline, menu);
		return true;
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
}
