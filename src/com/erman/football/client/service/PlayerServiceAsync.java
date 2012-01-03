package com.erman.football.client.service;

import java.util.List;

import com.erman.football.shared.ClientPlayer;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface PlayerServiceAsync {

	void addPlayer(ClientPlayer player, AsyncCallback<ClientPlayer> callback);

	void deletePlayer(Long id, AsyncCallback<Long> callback);

	void getPlayers(String firstChar, int start,int stop,AsyncCallback<List<ClientPlayer>> callback);

	void updatePlayer(ClientPlayer player, AsyncCallback<ClientPlayer> callback);

	void getPlayer(String email, AsyncCallback<ClientPlayer> callback);

}
