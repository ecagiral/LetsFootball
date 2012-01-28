package com.erman.football.client.service;

import com.erman.football.shared.ClientPlayer;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface LoginServiceAsync {
	
	void login(ClientPlayer player, AsyncCallback<ClientPlayer> callback);
	void logout(AsyncCallback<ClientPlayer> callback);
}
