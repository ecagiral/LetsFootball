package com.erman.football.client.cache;

import java.util.List;

import com.erman.football.shared.Match;


public interface CacheMatchHandler {
	
	public void matchAdded(List<Match> matches);	
	public void matchUpdated(Match match);	
	public void matchRemoved(Long match);
}
