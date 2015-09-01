package com.github.niwaniwa.we.core.twitter;

import java.util.Date;
import java.util.Map;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

public class TwitterManager {

	private Twitter twitter;

	public AccessToken access = null;
	private RequestToken request = null;

	private final String consumerKey = "xdpZ0YJbxMLs89c1TdKOFV7dB";
	private final String consumerSecret = "kJe421gJVRSR2g3ifdiaQWLbsEwtHJ6GtsymW5VwA8SNt3xnvn";

	public TwitterManager() {
		System.out.println(new Date().getTime() + " - new Twitter");
		this.twitter = new TwitterFactory().getInstance();
		System.out.println(new Date().getTime() + " - set consumerKey, consumerSecret");
		this.twitter.setOAuthConsumer(consumerKey, consumerSecret);
		System.out.println(new Date().getTime() + " - get OAuth Request Token");

		System.out.println(new Date().getTime() + " - twitter manager end");
	}

	public String getOAuthRequestURL(){
		return request.getAuthorizationURL() == null ? "§c不具合が発生しているため現在利用できません" : request.getAuthorizationURL();
	}

	public boolean OAuthRequestURL(){
		try {
			this.request = twitter.getOAuthRequestToken();
		} catch (TwitterException e) {
			e.printStackTrace();
		}
		return true;
	}

	public boolean OAuthRequest(String pin){
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

	public boolean setAccessToken(AccessToken access){
		if(access == null){ return false; }
		this.access = access;
		twitter.setOAuthAccessToken(access);
		return true;
	}

	public static AccessToken toAccesToken(Map<String, Object> map){
		if(map.get("Token") == null){ return null; }
		if(map.get("TokenSecret") == null);
		return new AccessToken(String.valueOf(map.get("Token")), String.valueOf(map.get("TokenSecret")));
	}

}
