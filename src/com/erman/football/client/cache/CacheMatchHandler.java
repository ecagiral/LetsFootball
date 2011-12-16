package com.erman.football.client.cache;

import com.erman.football.shared.ClientMatch;


public interface CacheMatchHandler {

	public void matchLoaded();
	
	public void matchAdded(ClientMatch match);	
	public void matchUpdated(ClientMatch match);	
	public void matchRemoved(Long match);
}
