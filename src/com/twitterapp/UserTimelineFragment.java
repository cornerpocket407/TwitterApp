package com.twitterapp;

import java.util.Collection;

import org.json.JSONArray;

import com.activeandroid.query.Select;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.twitterapp.fragments.BaseFragment;
import com.twitterapp.models.Tweet;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class UserTimelineFragment extends BaseFragment {
	private Collection<? extends Tweet> tweets;
	private String screenName;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		this.screenName = getArguments().getString("screen_name");
		return inflater.inflate(R.layout.fragments_tweets_list, container,
				false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void loadFromApi() {
		TwitterApp.getRestClient().getUserTimeline(screenName,
				new JsonHttpResponseHandler() {
					@Override
					public void onSuccess(JSONArray jsonTweets) {
						super.onSuccess(jsonTweets);
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

	@Override
	public void doStuff() {
		getAdapter().addAll(tweets);
	}
}
