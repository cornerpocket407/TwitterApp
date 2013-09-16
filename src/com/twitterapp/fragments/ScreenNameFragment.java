package com.twitterapp.fragments;

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
import com.twitterapp.models.TwitterAppConsts;
import com.twitterapp.models.User;

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
		String screenName = pref.getString("screenName", "");
		tvName.setText(screenName);
		ImageView imageView = (ImageView) getActivity().findViewById(R.id.ivProfileImg);
		String profileImage = pref.getString("profileImage","");
		ImageLoader.getInstance().displayImage(profileImage, imageView);
	}
	
	private void fetchUser() {
		pref =  getActivity().getSharedPreferences(TwitterAppConsts.SHARED_PREFERENCE_NAME, Activity.MODE_PRIVATE);
		String screenName = pref.getString("screenName", "");
		String profileImageUrl = pref.getString("profileImage", "");
		if (screenName.isEmpty() || profileImageUrl.isEmpty()) {
			if (isNetworkAvailable()) {
				TwitterApp.getRestClient().getCurrentUser(new JsonHttpResponseHandler() {
					@Override
					public void onSuccess(int arg0, JSONObject arg1) {
						super.onSuccess(arg0, arg1);
						User user = User.fromJson(arg1);
						Editor editor = pref.edit();
						editor.putString("screenName", user.getScreenName());
						editor.putString("profileImage", user.getProfileImageUrl());
						editor.commit();
						populateFields();
					}
				});
			} else {
				User user = new Select().from(User.class).executeSingle();
				Editor editor = pref.edit();
				editor.putString("screenName", user.getScreenName());
				editor.putString("profileImage", user.getProfileImageUrl());
				populateFields();
			}
		} else {
			populateFields();
		}
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
