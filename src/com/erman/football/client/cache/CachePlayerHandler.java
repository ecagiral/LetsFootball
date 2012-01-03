package com.erman.football.client.cache;

import java.util.List;

import com.erman.football.shared.ClientPlayer;

public interface CachePlayerHandler {
	
	public void playerAdded(List<ClientPlayer> player);	
	public void playerUpdated(ClientPlayer player);	
	public void playerRemoved(Long player);
	
}
