package com.erman.football.client.service;

import java.util.Date;
import java.util.List;

import com.erman.football.shared.ClientPlayer;
import com.erman.football.shared.Match;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface MatchServiceAsync {

	void createMatch(Match match, AsyncCallback<Match> callback);

	void getMatches(Date startDate, int from, int to,boolean attendOnly,AsyncCallback<List<Match>> callback);

	void deleteMatch(Match match, AsyncCallback<Long> callback);

	void updateMatch(Match match, AsyncCallback<Match> callback);
	
	void addPlayer(ClientPlayer player, Match match, boolean teamA, AsyncCallback<Match> callback);

}
