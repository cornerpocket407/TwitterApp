package com.twitterapp;

import java.util.ArrayList;

import org.json.JSONArray;

import com.activeandroid.query.Select;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.twitterapp.fragments.BaseFragment;
import com.twitterapp.models.Tweet;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class MentionsFragment extends BaseFragment {
	protected ArrayList<Tweet> tweets = new ArrayList<Tweet>();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragments_tweets_list, container,
				false);
	}

	@Override
	public void loadFromApi() {
		TwitterApp.getRestClient().getMentions(
				new JsonHttpResponseHandler() {
					@Override
					public void onSuccess(JSONArray jsonTweets) {
						super.onSuccess(jsonTweets);
						tweets = Tweet.fromJson(jsonTweets);
						doStuff();
					}
				});
		
	}

	@Override
	public void loadFromDb() {
		//TODO: Change this to pull mentions from db
		tweets = new Select().from(Tweet.class).orderBy("id").limit("25")
				.execute();
		
	}

	@Override
	public void doStuff() {
		getAdapter().addAll(tweets);
	}
}
