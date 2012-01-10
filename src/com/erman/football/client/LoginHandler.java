package com.erman.football.client;

import com.erman.football.shared.ClientPlayer;

public interface LoginHandler {

	public void loggedIn(ClientPlayer player);
	public void loggedOut();
	public void init();
}
