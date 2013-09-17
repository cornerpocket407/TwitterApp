package com.twitterapp.fragment;

import java.util.ArrayList;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.twitterapp.R;
import com.twitterapp.TweetsAdapter;
import com.twitterapp.model.Tweet;

public abstract class BaseFragment extends Fragment {
	private TweetsAdapter adapter;
	private ListView lvTweets;
	protected ArrayList<Tweet> tweets;

	public abstract void loadFromApi();

	public abstract void loadFromDb();
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragments_tweets_list, container,
				false);
		lvTweets = (ListView) v.findViewById(R.id.lvTweets);
		lvTweets.setAdapter(adapter);
		return v;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setupAdapter();
	}
	
	@Override
	public void onResume() {
		super.onResume();
		if (isNetworkAvailable()) {
			loadFromApi();
		} else {
			loadFromDb();
		}
	}

	private void setupAdapter() {
		tweets = new ArrayList<Tweet>();
		adapter = new TweetsAdapter(getActivity(), tweets);
	}
	
	public TweetsAdapter getAdapter() {
		return adapter;
	}
	
	private boolean isNetworkAvailable() {
		ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if (netInfo != null && netInfo.isConnectedOrConnecting()) {
			return true;
		}
		return false;
	}

}
