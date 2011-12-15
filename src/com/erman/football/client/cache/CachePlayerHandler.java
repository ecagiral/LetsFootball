package com.erman.football.client.cache;

import com.erman.football.shared.ClientPlayer;

public interface CachePlayerHandler {

	public void playerLoaded();
	
	public void playerAdded(ClientPlayer player);	
	public void playerUpdated(ClientPlayer player);	
	public void playerRemoved(Long player);
	
}
