package com.erman.football.server.data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import com.erman.football.shared.ClientMatch;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

public class Match_JDO_DB {
	
	public static ClientMatch addMatch(ClientMatch match){
		
		MatchDO matchDO = MatchDO.convert(match);
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			pm.makePersistent(matchDO);
			return matchDO.convert();
		}catch(Exception e){
			System.out.println("unable to add match");
			return match;
		} finally {
			pm.close();
		}
	}

	@SuppressWarnings("unchecked")
	public static List<MatchDO> getMatches(Date startDate,int start, int stop){
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Query query = pm.newQuery(MatchDO.class);
		query.declareImports("import java.util.Date");
		query.setOrdering("date ascending");
		query.setRange(start,stop);
		query.declareParameters("Date startDate");
		query.setFilter("date > startDate");
		List<MatchDO> matchDOs = new ArrayList<MatchDO>();
		
		try {
			matchDOs = (List<MatchDO>)query.execute(startDate);
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
		Key key = KeyFactory.createKey(MatchDO.class.getSimpleName(), id);
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			MatchDO m = pm.getObjectById(MatchDO.class, key);
			pm.deletePersistent(m);
		}catch(Exception e){
			System.out.println(e.getMessage());
		}finally {
			pm.close();
		}
	}
	
	public static void updateMatch(ClientMatch match){
		Key key = KeyFactory.createKey(MatchDO.class.getSimpleName(), match.getKey());
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			MatchDO matchDO = pm.getObjectById(MatchDO.class, key);
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
