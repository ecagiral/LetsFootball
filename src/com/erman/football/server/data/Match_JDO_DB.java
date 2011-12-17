package com.erman.football.server.data;

import java.util.ArrayList;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import com.erman.football.shared.ClientMatch;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

public class Match_JDO_DB {
	
	public static void addMatch(ClientMatch match){
		
		Match matchDO = Match.convert(match);
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			pm.makePersistent(matchDO);
		}catch(Exception e){
			System.out.println("unable to add match");
		} finally {
			pm.close();
		}
	}

	@SuppressWarnings("unchecked")
	public static List<Match> getMatches(){
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Query query = pm.newQuery(Match.class);
		List<Match> matchDOs = new ArrayList<Match>();
		
		try {
			matchDOs = (List<Match>)query.execute();
			if (!matchDOs.isEmpty()) {
				return matchDOs;
			} else {
				// ... no results ...
			}
		}catch(Exception e){
			System.out.println(e.getMessage());
		} finally {
			query.closeAll();
		}
		return matchDOs;
	}
	
	public static void deleteMatch(Long id){
		Key key = KeyFactory.createKey(Match.class.getSimpleName(), id);
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			Match m = pm.getObjectById(Match.class, key);
			pm.deletePersistent(m);
		}catch(Exception e){
			System.out.println(e.getMessage());
		}finally {
			pm.close();
		}
	}
	
	public static void updateMatch(ClientMatch match){
		Key key = KeyFactory.createKey(Match.class.getSimpleName(), match.getKey());
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			Match matchDO = pm.getObjectById(Match.class, key);
			if (matchDO!=null) {
				matchDO.update(match);
			}else{
				//Could not be found
				throw new Exception("Match to be updated could not be found "+match.getDate());
			}
		}catch(Exception e){
			System.out.println(e.getMessage());
		}finally {
			pm.close();
		}
	}
}
