package com.github.niwaniwa.we.core.twitter;

import java.util.ArrayList;
import java.util.List;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

public abstract class TwitterManager {

	private Twitter twitter;

	private AccessToken access = null;
	private RequestToken request = null;
	private List<Status> tweets;

	private final String consumerKey = // app consumer key
	private final String consumerSecret = // app consumer secret

	public TwitterManager() {
		this.twitter = new TwitterFactory().getInstance();
		this.twitter.setOAuthConsumer(consumerKey, consumerSecret);
		this.tweets = new ArrayList<>();
	}

	public String getOAuthRequestURL(){
		if(this.request == null){
			this.OAuthRequest();
		}
		return request.getAuthorizationURL();
	}

	protected boolean OAuthRequest(){
		try {
			this.request = twitter.getOAuthRequestToken();
		} catch (TwitterException e) {
			e.printStackTrace();
		}
		return true;
	}

	public boolean check(String tweet){
		if(this.access == null){ return false; }
		if(tweet.length() >= 140){ return false; }
		return true;
	}

	public boolean OAuthRequest(String pin){
		if(this.access != null){ return false; }
		if(this.request == null){ return false; }
		try {
			if (pin.length() > 0) {
				access = twitter.getOAuthAccessToken(request, pin);
			} else {
				access = twitter.getOAuthAccessToken();
			}
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	public abstract boolean tweet(String tweet);

	public abstract boolean tweet(String[] tweet);

	public AccessToken getAccessToken(){
		return access;
	}

	public RequestToken getRequestToken(){
		return request;
	}

	public boolean setAccessToken(AccessToken access){
		if(access == null){ return false; }
		this.access = access;
		twitter.setOAuthAccessToken(access);
		return true;
	}

	public boolean reset(){
		this.access = null;
		this.twitter = new TwitterFactory().getInstance();
		this.twitter.setOAuthConsumer(consumerKey, consumerSecret);
		return true;
	}

	/**
	 * プラグインが有効になってからのツイートを取得します
	 * @return
	 */
	public List<Status> getTweet(){
		return tweets;
	}

	public Twitter getTwitter(){
		return twitter;
	}


}
