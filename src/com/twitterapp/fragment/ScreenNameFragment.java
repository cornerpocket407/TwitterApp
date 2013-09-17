package com.twitterapp.fragment;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.activeandroid.Model;
import com.activeandroid.query.Select;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.twitterapp.R;
import com.twitterapp.TwitterApp;
import com.twitterapp.activity.ProfileActivity;
import com.twitterapp.model.TwitterAppConsts;
import com.twitterapp.model.User;

public class ScreenNameFragment extends Fragment {

	private String screenName;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		this.screenName = getArguments().getString("screen_name");
		return inflater
				.inflate(R.layout.fragment_screen_name, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		fetchUser();
	}

	private void populateFields(User user) {
		TextView tvName = (TextView) getActivity().findViewById(R.id.tvName);
		tvName.setText(user.getScreenName());
		ImageView imageView = (ImageView) getActivity().findViewById(
				R.id.ivProfileImg);
		ImageLoader.getInstance().displayImage(
				user.getProfileImageUrl(), imageView);

		TextView tvFollowers = (TextView) getActivity().findViewById(
				R.id.tvFollowers);
		tvFollowers.setText(user.getFollowersCount() + "Followers");
		TextView tvFollowing = (TextView) getActivity().findViewById(
				R.id.tvFollowing);
		tvFollowing.setText(user.getFriendsCount() + "Following");

		if (!(getActivity() instanceof ProfileActivity)) {
			tvFollowers.setVisibility(View.GONE);
			tvFollowing.setVisibility(View.GONE);
		}
	}

	private void fetchUser() {
		if (isNetworkAvailable()) {
			TwitterApp.getRestClient().getUser(screenName,
					new JsonHttpResponseHandler() {
						@Override
						public void onSuccess(int arg0, JSONObject arg1) {
							super.onSuccess(arg0, arg1);
							User user = User.fromJson(arg1);
							populateFields(user);
						}
					});
		} else {
			User user = new Select().from(User.class).executeSingle();
			populateFields(user);
		}
	}

	private boolean isNetworkAvailable() {
		ConnectivityManager cm = (ConnectivityManager) getActivity()
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if (netInfo != null && netInfo.isConnectedOrConnecting()) {
			return true;
		}
		return false;
	}
}
