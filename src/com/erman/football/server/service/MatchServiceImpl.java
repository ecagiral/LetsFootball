package com.erman.football.server.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.erman.football.client.service.MatchService;
import com.erman.football.server.data.MatchDO;
import com.erman.football.server.data.Match_JDO_DB;
import com.erman.football.shared.ClientPlayer;
import com.erman.football.shared.Match;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

@SuppressWarnings("serial")
public class MatchServiceImpl extends RemoteServiceServlet implements MatchService{

	public Match createMatch(Match match) {
		HttpServletRequest request = this.getThreadLocalRequest();
		String strPlayer = (String)request.getSession().getAttribute("player");
		if(strPlayer == null){
			strPlayer = "0";
		}
		match.setOwner(Long.parseLong(strPlayer));
		return Match_JDO_DB.addMatch(match);	
	}

	public List<Match> getMatches(Date startDate, int from, int to, boolean attendOnly) {
		List<Match> result = new ArrayList<Match>();
		HttpServletRequest request = this.getThreadLocalRequest();
		//By default get all matches
		String strPlayer = "-1";
		if(attendOnly){
			strPlayer = (String)request.getSession().getAttribute("player");
		}
		if(strPlayer==null){
			strPlayer = "0";
		}
		Long player = Long.parseLong(strPlayer);
		for(MatchDO matchDO:Match_JDO_DB.getMatches(startDate,from,to,player)){
			result.add(matchDO.convert());
		}
		return result;
	}

	@Override
	public Long deleteMatch(Match match) {
		Match_JDO_DB.deleteMatch(match.getKey());	
		return match.getKey();
	}

	@Override
	public Match updateMatch(Match match) {
		return Match_JDO_DB.updateMatch(match);
	}

	@Override
	public Match addPlayer(ClientPlayer player, Match match, boolean teamA) {
		return Match_JDO_DB.addPlayer(player,match,teamA);
	}

}
