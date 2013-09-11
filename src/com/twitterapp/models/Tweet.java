package com.twitterapp.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

@Table(name = "Tweets")
public class Tweet extends Model {

    public Tweet() {
        super();
    }

    protected JSONObject jsonObject;

    @Column(name = "User")
	private User user;

    @Column(name = "body")
    private String body;
    
    @Column(name = "twitter_id")
    private long twitter_id;

    public User getUser() {
        return user;
    }
    
	public void setUser(User user) {
		this.user = user;
	}    

    public String getBody() {
        return body;
    }

    public long getTwitterId() {
        return twitter_id;
    }

    public boolean isFavorited() {
        return ModelHelper.getBoolean(jsonObject, "favorited");
    }

    public boolean isRetweeted() {
        return ModelHelper.getBoolean(jsonObject, "retweeted");
    }


    public String getJSONString() {
        return jsonObject.toString();
    }

    public static Tweet fromJson(JSONObject jsonObject) {
        Tweet tweet = new Tweet();
        try {
            tweet.jsonObject = jsonObject;
            tweet.body = ModelHelper.getString(jsonObject, "text");
            tweet.twitter_id = ModelHelper.getLong(jsonObject, "id");
            tweet.setUser(User.fromJson(jsonObject.getJSONObject("user")));
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return tweet;
    }

    public static ArrayList<Tweet> fromJson(JSONArray jsonArray) {
        ArrayList<Tweet> tweets = new ArrayList<Tweet>(jsonArray.length());

        for (int i=0; i < jsonArray.length(); i++) {
            JSONObject tweetJson = null;
            try {
                tweetJson = jsonArray.getJSONObject(i);
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }

            Tweet tweet = Tweet.fromJson(tweetJson);
            if (tweet != null) {
                tweets.add(tweet);
            }
        }

        return tweets;
    }
}