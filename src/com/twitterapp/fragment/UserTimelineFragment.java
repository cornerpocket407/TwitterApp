package com.twitterapp.fragment;

import java.util.Collection;

import org.json.JSONArray;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.activeandroid.query.Select;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.twitterapp.R;
import com.twitterapp.TwitterApp;
import com.twitterapp.model.Tweet;
import com.twitterapp.model.User;

public class UserTimelineFragment extends BaseFragment {
	private Collection<? extends Tweet> tweets;
	private String screenName;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.screenName = getArguments().getString("screen_name");
	}

	@Override
	public void loadFromApi() {
		TwitterApp.getRestClient().getUserTimeline(screenName,
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
		// TODO: CHANGE QUERY
		tweets = new Select().from(Tweet.class).orderBy("id").limit("25")
				.execute();

	}

}
