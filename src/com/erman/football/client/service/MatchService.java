package com.erman.football.client.service;

import java.util.Date;
import java.util.List;

import com.erman.football.shared.ClientMatch;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client side stub for the Match Service.
 */
@RemoteServiceRelativePath("match")
public interface MatchService extends RemoteService{
	public ClientMatch createMatch(ClientMatch match);
	
	/**
	 * Returns match list starting from start date with from to pagination
	 * @param startDate 
	 * @param from
	 * @param to
	 * @return
	 */
	public List<ClientMatch> getMatches(Date startDate, int from, int to, boolean attendOnly);
	public Long deleteMatch(ClientMatch match);
	public ClientMatch updateMatch(ClientMatch match);
}
