package com.github.niwaniwa.we.core.twitter;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;

import org.bukkit.scheduler.BukkitRunnable;

import com.github.niwaniwa.we.core.WhiteEggCore;
import com.github.niwaniwa.we.core.event.WhiteEggPostTweetEvent;
import com.github.niwaniwa.we.core.event.WhiteEggPreTweetEvent;
import com.github.niwaniwa.we.core.util.Util;

import twitter4j.Status;
import twitter4j.StatusUpdate;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.UploadedMedia;

/**
 * ツイート送信クラス(使い捨て)
 * @author niwaniwa
 *
 */
public class TweetTask extends BukkitRunnable {

	private static final Pattern urlPattern =  Pattern.compile("(http://|https://){1}[\\w\\.\\-/:\\#\\?\\=\\&\\;\\%\\~\\+]+",
			Pattern.CASE_INSENSITIVE);
	private static final File path = new File(WhiteEggCore.getInstance().getDataFolder() + "/temp/");

	private TwitterManager twitter;
	private String tweet;
	private List<String> url = new ArrayList<>();
	private List<Status> status = new ArrayList<>();
	private List<File> medias = new ArrayList<>();
	private boolean useMedia;
	private boolean successfull = false;
	private int wait; //

	public TweetTask(TwitterManager twitter, String tweet, int wait){
		this.twitter = twitter;
		checkURL(tweet);
		useMedia = !url.isEmpty();
		this.wait = wait;
	}

	public TweetTask(TwitterManager twitter, String tweet){
		this(twitter, tweet, 2);
	}

	private void checkURL(String tweet){
		final Matcher matcher = urlPattern
				.matcher(tweet);
		String toTweet = tweet;
		while (matcher.find()) {
			String url = matcher.group();
			if(checkExtension(url)){
				toTweet = toTweet.replace(url, "");
				this.url.add(url);
			}
		}
		this.tweet = toTweet;
	}

	@Override
	public void run() {
		tweet();
	}

	private void tweet() {
		boolean player = (twitter instanceof PlayerTwitterManager);
		Twitter t = twitter.getTwitter();
		if(player){
			PlayerTwitterManager pt = (PlayerTwitterManager) twitter;
			// call event
			WhiteEggPreTweetEvent event = new WhiteEggPreTweetEvent(pt.getPlayer(), tweet);
			Util.callEvent(event);
			if(event.isCancelled()){ return; }
			tweet = event.getTweet();
		}
		Status status = null;
		try {
			status = t.updateStatus(build());
		} catch (TwitterException e) {}
		if(status != null){
			this.successfull = true;
			this.status.add(status);
		}
		delete();
		// call event
		WhiteEggPostTweetEvent event = new WhiteEggPostTweetEvent(twitter, status, successfull);
		Util.callEvent(event);
		if(player){ ((PlayerTwitterManager) twitter).set(successfull); }
	}

	private StatusUpdate build(){
		StatusUpdate su = new StatusUpdate(tweet);
		if(!useMedia){ return su; }
		su.setMediaIds(uploadMedias());
		return su;
	}

	private long[] uploadMedias(){
		long[] mediaId = new long[this.url.size()];
		for (int i = 0; i < url.size(); i++) {
			File media = readImage(url.get(i));
			medias.add(media);
			mediaId[i] = uploadMedia(media);
		}
		return mediaId;
	}

	public Long uploadMedia(File path){
		UploadedMedia media = null;
		try {
			media = twitter.getTwitter().uploadMedia(path);
		} catch (TwitterException e) {}
		return media.getMediaId();
	}

	private void delete(){
		for(File f : medias){
			if(f.exists()){
				f.delete();
			}
		}
	}

	public boolean isSuccessfull() {
		return successfull;
	}

	public List<Status> getStatus() {
		return status;
	}

	public void setStatus(List<Status> status) {
		this.status = status;
	}

	public boolean isMedia() {
		return useMedia;
	}

	public void setMedia(boolean media) {
		this.useMedia = media;
	}

	public List<String> getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url.add(url);
	}

	public String getTweet() {
		return tweet;
	}

	private File readImage(String url){
		if(!path.exists()){ path.mkdirs(); }
		InputStream input = responce(url);
		if(input == null){ return null; }
		String extension = url.split("\\.")[url.split("\\.").length - 1];
		File imagePath = new File(path, UUID.randomUUID() + "." + extension);
		BufferedImage image = null;
		try {
			image = ImageIO.read(input);
		} catch (IOException e) {}
		if(image == null){ return null; }
		try {
			ImageIO.write(image, extension, imagePath);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		return imagePath;
	}

	private InputStream responce(String url){
		if(!checkExtension(url)){ return null; }
		URL imageUrl = null;
		try {
			imageUrl = new URL(url);
		} catch (MalformedURLException e1) {
		}
		if(imageUrl == null){ return null; }
		InputStream input = null;
		try {
			input = imageUrl.openConnection().getInputStream();
		} catch (IOException e) {
		}
		return input;
	}

	private boolean checkExtension(String url){
		String[] format = new String[]{".jpg", ".gif", ".png", ".mp4"};
		for(String s : format){
			if(url.endsWith(s)){ return true; }
			continue;
		}
		return false;
	}

	public int getWait() {
		return wait;
	}

	public List<File> getMedia() {
		return medias;
	}

	public void setWait(int wait) {
		this.wait = wait;
	}

}
