package com.twitterapp;

import java.util.HashMap;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.twitterapp.models.User;

public class ComposeActivity extends FragmentActivity {

	private EditText etTweet;
	private TextView tvUserName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_compose);
		etTweet = (EditText) findViewById(R.id.etTweet);
//		getUser();
	}
//
//	private void getUser() {
//		SharedPreferences pref =  PreferenceManager.getDefaultSharedPreferences(this);
//		String screenName = pref.getString("screenName", "");
//		String profileImageUrl = pref.getString("profileImage", "");
//		tvUserName = (TextView) findViewById(R.id.tvUsername);
//		tvUserName.setText(screenName);
//		ImageView imageView = (ImageView) findViewById(R.id.ivComposeProfile);
//		ImageLoader.getInstance().displayImage(profileImageUrl, imageView);
//	}

	public void onTweet(View view) {
		String tweet = etTweet.getText().toString();
		if (tweet.length() > 140) {
			Toast.makeText(this, "Your tweet can have a maximum of 140 characters", Toast.LENGTH_SHORT).show();
			return;
		}
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("status", tweet);
		RequestParams params = new RequestParams(map);
		TwitterApp.getRestClient().tweet(new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(JSONObject jsonObject) {
				super.onSuccess(jsonObject);
				setResult(RESULT_OK, new Intent());
				finish();
			}
		}, params);
	}

	public void onCancel(View view) {
		setResult(RESULT_CANCELED, new Intent());
		finish();
	}
}
