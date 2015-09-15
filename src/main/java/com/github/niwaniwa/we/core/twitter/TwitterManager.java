package com.github.niwaniwa.we.core.twitter;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.scheduler.BukkitRunnable;

import com.github.niwaniwa.we.core.WhiteEggCore;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

public class TwitterManager {

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

	private boolean isSuccessfull = true;

	/**
	 *  ツイートします
	 * @param tweet 文字
	 * @param tick ツイートするまでの待ち時間
	 * @return ツイートが送信できたか
	 */
	public boolean tweet(final String tweet, int tick){
		new BukkitRunnable() {
			@Override
			public void run() {
				try {
					Status status = twitter.updateStatus(tweet);
					if(status != null){
						tweets.add(status);
						isSuccessfull = true;
					}
				} catch (TwitterException e) {
					isSuccessfull = false;
				}
			}
		}.runTaskLater(WhiteEggCore.getInstance(), tick);
		return isSuccessfull;
	}

	/**
	 * ツイートします
	 * @param tweet 文字
	 * @return ツイートが送信できたか
	 */
	public boolean tweet(String tweet){
		if(access == null){ return false; }
		if(tweet.length() >= 140){ return false; }
		return tweet(tweet, 2);
	}

	/**
	 * ツイートします
	 * @param tweet 配列
	 * @return ツイートが送信できたか
	 */
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

}
