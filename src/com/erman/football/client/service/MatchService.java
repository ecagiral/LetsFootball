package com.erman.football.client.service;

import java.util.Date;
import java.util.List;

import com.erman.football.shared.ClientPlayer;
import com.erman.football.shared.Match;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client side stub for the Match Service.
 */
@RemoteServiceRelativePath("match")
public interface MatchService extends RemoteService{
	
	public Match createMatch(Match match);
	
	/**
	 * Returns match list starting from start date with from to pagination
	 * @param startDate 
	 * @param from
	 * @param to
	 * @return
	 */
	public List<Match> getMatches(Date startDate, int from, int to, boolean attendOnly);
	public Long deleteMatch(Match match);
	public Match updateMatch(Match match);
	public Match addPlayer(ClientPlayer player, Match match,boolean teamA);
}
