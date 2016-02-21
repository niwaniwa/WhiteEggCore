package com.github.niwaniwa.we.core.twitter;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import twitter4j.StatusUpdate;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.UploadedMedia;

/**
 * Twitterへのメディアファイルアップロードクラス
 *
 * @author niwanwia
 */
public class TwitterMediaUploader {

    public static final Pattern urlPattern = Pattern.compile("(http://|https://){1}[\\w\\.\\-/:\\#\\?\\=\\&\\;\\%\\~\\+]+",
            Pattern.CASE_INSENSITIVE);

    private final Twitter twitter;
    private final StatusUpdate update;
    private final List<String> urls;

    /**
     * コンストラクター
     *
     * @param twitter Twitter{@link twitter4j.Twitter}
     * @param update  StatsUpdate{@link twitter4j.StatusUpdate}
     * @param urls    アップロードするメディアのURL
     */
    public TwitterMediaUploader(Twitter twitter, StatusUpdate update, List<String> urls) {
        this.twitter = twitter;
        this.update = update;
        this.urls = urls;
    }

    /**
     * StatsUpdateの取得
     *
     * @return
     */
    public StatusUpdate getUpdate() {
        return update;
    }

    /**
     * URLの取得
     *
     * @return
     */
    public List<String> getUrls() {
        return urls;
    }

    /**
     * メディアのアップロード
     */
    public void upload() {
        update.setMediaIds(uploadMedias());
    }

    private long[] uploadMedias() {
        long[] mediaId = new long[this.urls.size()];
        for (int i = 0; i < urls.size(); i++) {
            InputStream media = responce(urls.get(i));
            mediaId[i] = uploadMedia(UUID.randomUUID().toString(), media);
        }
        return mediaId;
    }

    /**
     * ファイルからメディアのアップロード
     *
     * @param path
     * @return
     */
    public long uploadMedia(File path) {
        UploadedMedia media = null;
        try {
            media = twitter.uploadMedia(path);
        } catch (TwitterException e) {
        }
        return media.getMediaId();
    }

    /**
     * InputStreamからメディアのアップロード
     *
     * @param fileName File
     * @param stream   stream
     * @return long
     */
    public long uploadMedia(String fileName, InputStream stream) {
        UploadedMedia media = null;
        try {
            media = twitter.uploadMedia(fileName, stream);
        } catch (TwitterException e) {
        }
        return media.getMediaId();
    }

    private InputStream responce(String url) {
        if (!checkMediaExtension(url)) {
            return null;
        }
        URL imageUrl = null;
        InputStream input = null;
        try {
            imageUrl = new URL(url);
            input = imageUrl.openConnection().getInputStream();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return input;
    }

    /**
     * 文字列にurlを含む確認します
     *
     * @param string 対象の文字列
     * @return URLを含む場合true、それ以外の場合はfalseを返します
     */
    public static boolean checkMediaURL(String string) {
        final Matcher matcher = urlPattern.matcher(string);
        while (matcher.find()) {
            String url = matcher.group();
            if (checkMediaExtension(url)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 文字列にTwitterが対応している拡張子を含んでいるか確認します(jpg gif png mp4)
     *
     * @param url 対象の文字列
     * @return URLを含む場合true、それ以外の場合はfalseを返します
     */
    public static boolean checkMediaExtension(String url) {
        String[] format = new String[]{".jpg", ".gif", ".png", ".mp4"};
        for (String s : format) {
            if (url.endsWith(s)) {
                return true;
            }
            continue;
        }
        return false;
    }


}
