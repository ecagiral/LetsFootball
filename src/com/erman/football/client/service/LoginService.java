package com.erman.football.client.service;

import com.erman.football.shared.ClientPlayer;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client side stub for the Match Service.
 */
@RemoteServiceRelativePath("login")
public interface LoginService extends RemoteService{

	ClientPlayer login(ClientPlayer player) throws PlayerException;

	ClientPlayer logout();

}
