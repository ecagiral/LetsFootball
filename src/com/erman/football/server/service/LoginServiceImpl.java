package com.erman.football.server.service;

import javax.servlet.http.HttpServletRequest;

import com.erman.football.client.service.LoginService;
import com.erman.football.server.data.Player;
import com.erman.football.server.data.Player_JDO_DB;
import com.erman.football.shared.ClientPlayer;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

@SuppressWarnings("serial")
public class LoginServiceImpl extends RemoteServiceServlet implements LoginService{

	public ClientPlayer login(String email) {
		ClientPlayer result = null;
		HttpServletRequest request = this.getThreadLocalRequest();
		if(email==null){
			//Check if already logged in
			String playerId = (String)request.getSession().getAttribute("player");
			if(playerId == null){
				return result;
			}else{
				if(playerId.equals("-1")){
					result = new ClientPlayer();
					result.setAdmin(true);
					result.setEmail("admin@main");
					result.setKey(0);
					result.setName("admin");
					return result;
				}
				Player playerDO = Player_JDO_DB.getUserbyId(Long.parseLong(playerId));
				if(playerDO==null){
					return result;
				}else{
					return playerDO.convert();
				}
			}
		}else{
			if(email.equals("admin")){
				result = new ClientPlayer();
				result.setAdmin(true);
				result.setEmail("admin@main");
				result.setKey(-1);
				result.setName("admin");
				request.getSession().setAttribute("player",String.valueOf(result.getKey()));
				return result;
			}
			//email supplied. Check user is valid
			Player playerDO = Player_JDO_DB.getUser(email);
			if(playerDO==null){
				return result;
			}else{
				request.getSession().setAttribute("player",String.valueOf(playerDO.getKey()));
				return playerDO.convert();
			}
		}

	}

	public boolean logout() {
		HttpServletRequest request = this.getThreadLocalRequest();
		request.getSession().setAttribute("player",null);
		return false;
	}

}
