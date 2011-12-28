package com.erman.football.server.service;

import java.util.ArrayList;
import java.util.List;

import com.erman.football.client.service.MatchService;
import com.erman.football.server.data.MatchDO;
import com.erman.football.server.data.Match_JDO_DB;
import com.erman.football.shared.ClientMatch;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

@SuppressWarnings("serial")
public class MatchServiceImpl extends RemoteServiceServlet implements MatchService{

	public ClientMatch createMatch(ClientMatch match) {
		return Match_JDO_DB.addMatch(match);	
	}

	public List<ClientMatch> getMatches() {
		List<ClientMatch> result = new ArrayList<ClientMatch>();
		for(MatchDO matchDO:Match_JDO_DB.getMatches()){
			result.add(matchDO.convert());
		}
		return result;
	}

	@Override
	public Long deleteMatch(ClientMatch match) {
		Match_JDO_DB.deleteMatch(match.getKey());	
		return match.getKey();
	}

	@Override
	public ClientMatch updateMatch(ClientMatch match) {
		Match_JDO_DB.updateMatch(match);
		return match;
	}

}
