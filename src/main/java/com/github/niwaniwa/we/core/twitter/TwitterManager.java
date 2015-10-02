package com.github.niwaniwa.we.core.twitter;

import java.util.ArrayList;
import java.util.List;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

/**
 * Twitter関連のクラス
 * @author niwaniwa
 *
 */
public abstract class TwitterManager {

	private Twitter twitter;

	private AccessToken access = null;
	private RequestToken request = null;
	private List<Status> tweets;

	private final String consumerKey = ""; // app consumer key
	private final String consumerSecret = ""; // app consumer secret

	public TwitterManager() {
		this.twitter = new TwitterFactory().getInstance();
		this.twitter.setOAuthConsumer(consumerKey, consumerSecret);
		this.tweets = new ArrayList<>();
	}

	/**
	 * RequestTokenの取得
	 * @return RequestToken
	 */
	public String getOAuthRequestURL(){
		if(this.request == null){
			this.OAuthRequest();
		}
		return request.getAuthorizationURL();
	}

	/**
	 * OAuthで認証する際に初期化する
	 * @return 成功したか
	 */
	protected boolean OAuthRequest(){
		try {
			this.request = twitter.getOAuthRequestToken();
		} catch (TwitterException e) {
		}
		return true;
	}

	/**
	 * Twitterに投稿する文字列のチェック
	 * @param tweet 投稿する文字列
	 * @return 基準以内か(アプリ提携をしているか : 140文字以内)
	 */
	public boolean check(String tweet){
		if(this.access == null){ return false; }
		return tweet.length() < 140;
	}

	/**
	 * 認証
	 * @param pin PIN
	 * @return 成功したか
	 */
	public boolean OAuthAccess(String pin){
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

	/**
	 * ツイートをします
	 * @param tweet ツイート
	 * @return 成功したか
	 */
	public abstract boolean tweet(String tweet);

	/**
	 * ツイートをします
	 * @param tweet ツイート
	 * @return 成功したか
	 */
	public abstract boolean tweet(String[] tweet);

	/**
	 * AccessTokenを取得します
	 * @return AccessToken 認証していない場合はnull
	 */
	public AccessToken getAccessToken(){
		return access;
	}

	/**
	 * RequestTokenを取得します
	 * @return RequestToken 初期はnull
	 */
	public RequestToken getRequestToken(){
		return request;
	}

	/**
	 * AccessTokenを設定します
	 * @param access AccessToken
	 * @return 成功したか
	 */
	public boolean setAccessToken(AccessToken access){
		if(access == null){ return false; }
		this.access = access;
		twitter.setOAuthAccessToken(access);
		return true;
	}

	/**
	 * 現在登録されている情報を初期化します
	 * @return 成功したか
	 */
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

	/**
	 * Twitterクラスのインスタンスを取得します
	 * @return Twitter {@link Twitter}
	 */
	public Twitter getTwitter(){
		return twitter;
	}


}
