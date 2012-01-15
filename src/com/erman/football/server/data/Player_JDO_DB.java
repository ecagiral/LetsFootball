package com.erman.football.server.data;

import java.util.ArrayList;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import com.erman.football.client.service.PlayerException;
import com.erman.football.shared.ClientPlayer;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

public class Player_JDO_DB {
	
	@SuppressWarnings("unchecked")
	public static List<Player> getUsers(String firstChar,int start, int stop){
		List<Player> playerDOs = new ArrayList<Player>();
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Query query = pm.newQuery(Player.class);
		query.setOrdering("name ascending");
		query.setRange(start,stop);
		query.declareParameters("String startChar");
		query.setFilter("name >= startChar");
		try {
			playerDOs = (List<Player>)query.execute(firstChar);
		}catch(Exception e){
			System.out.println(e.getMessage());
		} finally {
			query.closeAll();
		}
		return playerDOs;
	}

	public static ClientPlayer addUser(ClientPlayer clientPlayer) throws PlayerException{
		if(getUser(clientPlayer.getEmail())!=null){
			throw new PlayerException("Email kullaniliyor");
		}
		Player playerDO = Player.convert(clientPlayer);
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			pm.makePersistent(playerDO);
		}catch(Exception e){
			System.out.println("unable to add user");
		} finally {
			pm.close();
		}
		return playerDO.convert();
	}

	public static void deleteUser(Long id){
		Key key = KeyFactory.createKey(Player.class.getSimpleName(), id);
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			Player p = pm.getObjectById(Player.class, key);
			pm.deletePersistent(p);
		}catch(Exception e){
			System.out.println(e.getMessage());
		}finally {
			pm.close();
		}
	}
	
	public static Player getUserbyId(Long id){
		Key key = KeyFactory.createKey(Player.class.getSimpleName(), id);
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			return pm.getObjectById(Player.class, key);
		}catch(Exception e){
			System.out.println(e.getMessage());
		}finally {
			pm.close();
		}
		return null;
	}
	
	public static Player getUser(String email){
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Query query = pm.newQuery(Player.class);
	    query.setFilter("email == emailParam");
	    query.declareParameters("String emailParam");
		try {
			@SuppressWarnings("unchecked")
			List<Player> results = (List<Player>) query.execute(email);
	        if (!results.isEmpty()) {
	            return results.get(0);
	        } else {
	            return null;
	        }
		}catch(Exception e){
			System.out.println(e.getMessage());
		}finally {
			pm.close();
		}
		return null;
	}
	
	public static void updateUser(ClientPlayer player){
		Key key = KeyFactory.createKey(Player.class.getSimpleName(), player.getKey());
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			Player p = pm.getObjectById(Player.class, key);
			if (p!=null) {
				p.update(player);
			}else{
				//Could not be found
				throw new Exception("Player to be updated could not be found "+player.getEmail());
			}
		}catch(Exception e){
			System.out.println(e.getMessage());
		}finally {
			pm.close();
		}
	}
}
