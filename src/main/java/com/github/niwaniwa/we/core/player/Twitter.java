package com.github.niwaniwa.we.core.player;

import java.util.List;

import com.github.niwaniwa.we.core.api.callback.Callback;
import com.github.niwaniwa.we.core.twitter.TwitterManager;

import twitter4j.Status;
import twitter4j.StatusUpdate;

public interface Twitter {

	/**
	 * TwitterManagerクラスを返します
	 * @return twittermanager
	 */
	public abstract TwitterManager getTwitterManager();

	/**
	 * ツイートの送信
	 * @param update StatusUpdate
	 */
	public void updateStatus(StatusUpdate update);

	/**
	 * ツイートの送信
	 * @param update StatusUpdate
	 * @param callback 終了後に実行するインスタンス
	 */
	public void updateStatus(StatusUpdate update, Callback callback);

	/**
	 * ツイートの送信
	 * @param tweet 文字列
	 */
	public void updateStatus(String tweet);

	/**
	 * タイムラインの取得
	 * @return List status
	 */
	public List<Status> getTimeLine();

}
