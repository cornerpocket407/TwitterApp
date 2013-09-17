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

	private SharedPreferences pref;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
	return inflater.inflate(R.layout.fragment_screen_name, container, false);
}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		fetchUser();
	}

	private void populateFields() {
		TextView tvName = (TextView) getActivity().findViewById(R.id.tvName);
		tvName.setText(pref.getString("screenName", ""));
		ImageView imageView = (ImageView) getActivity().findViewById(R.id.ivProfileImg);
		ImageLoader.getInstance().displayImage(pref.getString("profileImage",""), imageView);
		
		TextView tvFollowers = (TextView) getActivity().findViewById(R.id.tvFollowers);
		tvFollowers.setText(pref.getInt("followers", 0) + "Followers");
		TextView tvFollowing = (TextView) getActivity().findViewById(R.id.tvFollowing);
		tvFollowing.setText(pref.getInt("following", 0) + "Following");
		
		if(!(getActivity() instanceof ProfileActivity)) {
			tvFollowers.setVisibility(View.GONE);
			tvFollowing.setVisibility(View.GONE);
		}
	}
	
	private void fetchUser() {
		pref =  getActivity().getSharedPreferences(TwitterAppConsts.SHARED_PREFERENCE_NAME, Activity.MODE_PRIVATE);
		String screenName = pref.getString("screenName", "");
		String profileImageUrl = pref.getString("profileImage", "");
		int followers = pref.getInt("followers", -1);
		int following = pref.getInt("following", -1);
		if (screenName.isEmpty() || profileImageUrl.isEmpty() || followers < 0 || following < 0) {
			if (isNetworkAvailable()) {
				TwitterApp.getRestClient().getCurrentUser(new JsonHttpResponseHandler() {
					@Override
					public void onSuccess(int arg0, JSONObject arg1) {
						super.onSuccess(arg0, arg1);
						User user = User.fromJson(arg1);
						saveToSharedPreferences(user);
						populateFields();
					}
				});
			} else {
				User user = new Select().from(User.class).executeSingle();
				saveToSharedPreferences(user);
				populateFields();
			}
		} else {
			populateFields();
		}
	}
	
	private void saveToSharedPreferences(User user) {
		Editor editor = pref.edit();
		editor.putString("screenName", user.getScreenName());
		editor.putString("profileImage", user.getProfileImageUrl());
		editor.putInt("followers", user.getFollowersCount());
		editor.putInt("following", user.getFriendsCount());
		editor.commit();
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
