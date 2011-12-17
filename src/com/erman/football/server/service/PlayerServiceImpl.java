package com.erman.football.server.service;

import java.util.ArrayList;
import java.util.List;

import com.erman.football.client.service.PlayerService;
import com.erman.football.server.data.Player;
import com.erman.football.server.data.Player_JDO_DB;
import com.erman.football.shared.ClientPlayer;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class PlayerServiceImpl extends RemoteServiceServlet implements
PlayerService {

	public ClientPlayer addPlayer(ClientPlayer player) {
		return Player_JDO_DB.addUser(player);
	}

	public Long deletePlayer(Long id){
		Player_JDO_DB.deleteUser(id);
		return id;
	}

	public List<ClientPlayer> getPlayers() {
		List<ClientPlayer> players = convert(Player_JDO_DB.getUsers());
		return players;

	}

	public ClientPlayer getPlayer(String email) {
		Player playerDO = Player_JDO_DB.getUser(email);
		if(playerDO==null){
			return null;
		}else{
			return playerDO.convert();
		}
	}

	public ClientPlayer updatePlayer(ClientPlayer player) {
		Player_JDO_DB.updateUser(player);
		return player;
	}

	private ArrayList<ClientPlayer> convert(List<Player> playerDOs){
		ArrayList<ClientPlayer> result = new ArrayList<ClientPlayer>();
		if(playerDOs == null){
			return result;
		}
		for(Player playerDO:playerDOs){
			ClientPlayer client = playerDO.convert();
			if(playerDO!=null){
				result.add(client);
			}
		}
		return result;
	}

}
