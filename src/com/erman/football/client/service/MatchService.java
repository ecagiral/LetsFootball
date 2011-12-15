package com.erman.football.client.service;

import java.util.List;

import com.erman.football.shared.ClientMatch;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("match")
public interface MatchService extends RemoteService{
	public ClientMatch createMatch(ClientMatch match);
	public List<ClientMatch> getMatches();
	public Long deleteMatch(ClientMatch match);
	public ClientMatch updateMatch(ClientMatch match);
}
