package com.twitterapp.fragment;

import java.util.ArrayList;

import org.json.JSONArray;

import com.activeandroid.query.Select;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.twitterapp.R;
import com.twitterapp.TwitterApp;
import com.twitterapp.R.layout;
import com.twitterapp.model.Tweet;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class MentionsFragment extends BaseFragment {
	protected ArrayList<Tweet> tweets = new ArrayList<Tweet>();


	@Override
	public void loadFromApi() {
		TwitterApp.getRestClient().getMentions(
				new JsonHttpResponseHandler() {
					@Override
					public void onSuccess(JSONArray jsonTweets) {
						super.onSuccess(jsonTweets);
						getAdapter().clear();
						getAdapter().addAll(Tweet.fromJson(jsonTweets));
					}
				});
	}

	@Override
	public void loadFromDb() {
		tweets = new Select().from(Tweet.class).orderBy("id").limit("25")
				.execute();
	}
}
