package com.twitterapp.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.List;

@Table(name = "Users")
public class User extends Model {

    public User() {
        super();
    }

    protected JSONObject jsonObject;

    @Column(name = "screenname")
    private String screenName;
    
    @Column(name = "name")
    private String name;

    @Column(name = "profile_image_url")
    private String profileImageUrl;
    
    @Column(name = "twitter_user_id")
    private long twitterUserId;

    public List<Tweet> tweets() {
        return getMany(Tweet.class, "User");
    }

    public String getName() {
        return name;
    }
    
	public void setName(String name) {
		this.name = name;
	}

    public long getTwitterUserId() {
        return twitterUserId;
    }

    public String getScreenName() {
        return screenName;
    }
    
    public String getTagline() {
    	 return ModelHelper.getString(jsonObject, "description");
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public String getProfileBackgroundImageUrl() {
        return ModelHelper.getString(jsonObject, "profile_background_image_url");
    }

    public int getNumTweets() {
        return ModelHelper.getInt(jsonObject, "statuses_count");
    }

    public int getFollowersCount() {
        return ModelHelper.getInt(jsonObject, "followers_count");
    }

    public int getFriendsCount() {
        return ModelHelper.getInt(jsonObject, "friends_count");
    }


    public String getJSONString() {
        return jsonObject.toString();
    }

    public static User fromJson(JSONObject json) {
        User u = new User();

        try {
            u.jsonObject = json;
            u.screenName = ModelHelper.getString(json, "screen_name");
            u.profileImageUrl = ModelHelper.getString(json, "profile_image_url");
            u.twitterUserId =  ModelHelper.getLong(json,"id");
            u.setName(ModelHelper.getString(json,"name"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return u;
    }
}
