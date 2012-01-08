package com.erman.football.server.service;

import javax.servlet.http.HttpServletRequest;

public class Session {
	
	public static long getUser(HttpServletRequest request){
		String strPlayer = (String)request.getSession().getAttribute("player");
		return Long.parseLong(strPlayer);
	}

}
