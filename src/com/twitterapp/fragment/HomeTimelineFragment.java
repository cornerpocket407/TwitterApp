package com.twitterapp.fragment;

import org.json.JSONArray;

import android.os.Bundle;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.query.Select;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.twitterapp.TwitterApp;
import com.twitterapp.model.Tweet;
import com.twitterapp.model.User;

public class HomeTimelineFragment extends BaseFragment {

	@Override
	public void loadFromApi() {
		TwitterApp.getRestClient().getHomeTimeline(
				new JsonHttpResponseHandler() {
					@Override
					public void onSuccess(JSONArray jsonTweets) {
						super.onSuccess(jsonTweets);
						getAdapter().clear();
						getAdapter().addAll(Tweet.fromJson(jsonTweets));
						// doStuff();
						saveToDb();
					}

					@Override
					public void onFailure(Throwable arg0, JSONArray arg1) {
						// TODO Auto-generated method stub
						super.onFailure(arg0, arg1);
						loadFromDb();
					}
				});

	}

	protected void saveToDb() {
		ActiveAndroid.beginTransaction();
		try {
			for (Tweet tweet : tweets) {
				Tweet dbTweet = new Select().from(Tweet.class)
						.where("twitter_id = ?", tweet.getTwitterId())
						.executeSingle();
				
				User user = tweet.getUser();
				User dbUser = new Select().from(User.class)
						.where("twitter_user_id = ?", user.getTwitterUserId())
						.executeSingle();
				if (dbUser == null) {
					user.save();
				}
				if (dbTweet == null) {
					tweet.setUser(dbUser);
					tweet.save();
				}
			}
			ActiveAndroid.setTransactionSuccessful();
		} finally {
			ActiveAndroid.endTransaction();
		}

	}

	@Override
	public void loadFromDb() {
		tweets = new Select().from(Tweet.class).orderBy("id").limit("25")
				.execute();
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}
}
