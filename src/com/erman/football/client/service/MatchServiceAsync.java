package com.erman.football.client.service;

import java.util.Date;
import java.util.List;

import com.erman.football.shared.ClientMatch;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface MatchServiceAsync {

	void createMatch(ClientMatch match, AsyncCallback<ClientMatch> callback);

	void getMatches(Date startDate, int from, int to,AsyncCallback<List<ClientMatch>> callback);

	void deleteMatch(ClientMatch match, AsyncCallback<Long> callback);

	void updateMatch(ClientMatch match, AsyncCallback<ClientMatch> callback);

}
