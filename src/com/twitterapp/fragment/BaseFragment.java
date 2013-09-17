package com.twitterapp.fragment;

import java.util.ArrayList;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.widget.ListView;

import com.twitterapp.R;
import com.twitterapp.TweetsAdapter;
import com.twitterapp.model.Tweet;

public abstract class BaseFragment extends Fragment {
	private TweetsAdapter adapter;

	public abstract void loadFromApi();

	public abstract void loadFromDb();

	public abstract void doStuff();
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		setupAdapter();
		if (isNetworkAvailable()) {
			loadFromApi();
		} else {
			loadFromDb();
		}
	}

	private void setupAdapter() {
		ArrayList<Tweet> tweets = new ArrayList<Tweet>();
		ListView lvTweets = (ListView) getActivity()
				.findViewById(R.id.lvTweets);
		adapter = new TweetsAdapter(getActivity(), tweets);
		lvTweets.setAdapter(adapter);
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
