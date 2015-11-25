package com.github.niwaniwa.we.core.player;

import java.util.List;

import com.github.niwaniwa.we.core.api.Callback;

import twitter4j.Status;
import twitter4j.StatusUpdate;

public interface Tweet {

	public void updateStatus(StatusUpdate update);

	public void updateStatus(StatusUpdate update, Callback callback);

	public void updateStatus(String tweet);

	public List<Status> getTimeLine();

}
