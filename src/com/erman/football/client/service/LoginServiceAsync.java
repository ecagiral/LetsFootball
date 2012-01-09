package com.erman.football.client.service;

import com.erman.football.shared.ClientPlayer;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface LoginServiceAsync {
	
	void login(String email, AsyncCallback<ClientPlayer> callback);
	void logout(AsyncCallback<Boolean> callback);
}
