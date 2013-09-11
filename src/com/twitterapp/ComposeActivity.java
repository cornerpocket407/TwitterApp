package com.twitterapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.twitterapp.models.User;

import org.json.JSONObject;

import java.util.HashMap;

public class ComposeActivity extends Activity {

    private EditText etTweet;
    private TextView tvUserName;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);
        etTweet = (EditText) findViewById(R.id.etTweet);
        getUser();
    }

    private void getUser() {
        TwitterApp.getRestClient().getUserInfo(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                user = User.fromJson(jsonObject);
                setTitle("@" + user.getName());
                tvUserName = (TextView) findViewById(R.id.tvUsername);
                tvUserName.setText("@" + user.getName());
                ImageView imageView = (ImageView) findViewById(R.id.ivComposeProfile);
                ImageLoader.getInstance().displayImage(user.getProfileImageUrl(), imageView);
            }
        });
    }

    public void onTweet(View view) {
        String tweet = etTweet.getText().toString();
        if (tweet.length() > 140) {
            //handle
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
