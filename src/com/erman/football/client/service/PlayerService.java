package com.erman.football.client.service;

import java.util.List;

import com.erman.football.shared.ClientPlayer;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("player")
public interface PlayerService extends RemoteService {
	ClientPlayer addPlayer(ClientPlayer player) throws PlayerException;
	Long deletePlayer(Long id);
	ClientPlayer updatePlayer(ClientPlayer player);
	List<ClientPlayer> getPlayers(String firstChar,int start, int stop);
	ClientPlayer getPlayer(String email);
}
