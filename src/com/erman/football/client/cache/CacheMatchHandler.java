package com.erman.football.client.cache;

import java.util.List;

import com.erman.football.shared.ClientMatch;


public interface CacheMatchHandler {
	
	public void matchAdded(List<ClientMatch> matches);	
	public void matchUpdated(ClientMatch match);	
	public void matchRemoved(Long match);
}
