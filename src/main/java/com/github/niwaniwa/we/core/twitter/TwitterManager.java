package com.github.niwaniwa.we.core.twitter;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

public class TwitterManager {

	private Twitter twitter;

	private AccessToken access = null;
	private RequestToken request = null;

	private final String consumerKey = "Xwmw20C6XCyLqETJM1rnZyJEB";
	private final String consumerSecret = "ljhzU4PIhJyBOtk7zUkthbeouwDAONsPMQhrmpZd2peBmSLie1";

	public TwitterManager() {
		this.twitter = new TwitterFactory().getInstance();
		this.twitter.setOAuthConsumer(consumerKey, consumerSecret);
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

	public boolean OAuthRequest(String pin){
		if(this.access != null){ return false; }
		if(this.request == null){ return false; }
		try{
		if(pin.length() > 0){
			access = twitter.getOAuthAccessToken(request, pin);
		} else {
			access = twitter.getOAuthAccessToken();
		}
		} catch (Exception e) {return false;}
		return true;
	}

	public boolean tweet(String tweet){
		if(access == null){ return false; }
		if(tweet.length() >= 140){ return false; }
		try {
			twitter.updateStatus(tweet);
		} catch (TwitterException e) {
			return false;
		}
		return true;
	}

	public boolean tweet(String[] tweet){
		if(tweet.length == 0){ return false; }
		StringBuilder sb = new StringBuilder();
		for(String str : tweet){
			sb.append(str+ " ");
		}
		return this.tweet(sb.toString());
	}

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

}
