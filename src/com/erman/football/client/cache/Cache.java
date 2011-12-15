package com.erman.football.client.cache;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;

import com.erman.football.client.service.PlayerService;
import com.erman.football.client.service.PlayerServiceAsync;
import com.erman.football.client.service.MatchService;
import com.erman.football.client.service.MatchServiceAsync;
import com.erman.football.shared.ClientMatch;
import com.erman.football.shared.ClientPlayer;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class Cache {
	
	private final MatchServiceAsync matchService = GWT.create(MatchService.class);
	private final PlayerServiceAsync playerService = GWT.create(PlayerService.class);
	
	private TreeMap<Long,ClientPlayer> players = new TreeMap<Long,ClientPlayer>();
	private TreeMap<Long,ClientMatch> matches = new TreeMap<Long,ClientMatch>(); 
	
	private ArrayList<CacheMatchHandler> matchHandlers = new ArrayList<CacheMatchHandler>();
	private ArrayList<CachePlayerHandler> playerHandlers = new ArrayList<CachePlayerHandler>();
 
	private ClientPlayer player;
	
	public void load(){
		matchService.getMatches(new AsyncCallback<List<ClientMatch>>(){

			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}

			public void onSuccess(List<ClientMatch> result) {
				matches.clear();
				for(ClientMatch match:result){
					matches.put(match.getKey(), match);
				}
				notifyMatchLoaded();
			}
			
		});
		playerService.getPlayers(
				new AsyncCallback<List<ClientPlayer>>(){
			public void onFailure(Throwable caught) {

			}


			@Override
			public void onSuccess(List<ClientPlayer> result) {
				for(ClientPlayer ply:result){
					players.put(ply.getKey(), ply);
				}
				notifyPlayerLoaded();
			}
		});
	}
	
	public void regiserPlayer(CachePlayerHandler handler){
		playerHandlers.add(handler);
	}
	
	public void regiserMatch(CacheMatchHandler handler){
		matchHandlers.add(handler);
	}
	
	public ClientPlayer getPlayer(Long id){
		return players.get(id);
	}
	
	public Set<Long> getAllPlayerIds(){
		return players.keySet();
	}
	
	public void addPlayer(ClientPlayer player){
		playerService.addPlayer(player,
				new AsyncCallback<ClientPlayer>() {
			public void onFailure(Throwable caught) {

			}

			public void onSuccess(ClientPlayer result) {
				players.put(result.getKey(), result);
				notifyPlayerAdded(result);
			}
		});	
	}
	
	public void updatePlayer(ClientPlayer player){
		playerService.updatePlayer(player, new AsyncCallback<ClientPlayer>() {

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onSuccess(ClientPlayer result) {
				players.put(result.getKey(), result);
				notifyPlayerUpdated(result);
			}
			
		});
	}
	
	public void removePlayer(Long player){
		playerService.deletePlayer(player,
				new AsyncCallback<Long>() {

			public void onFailure(Throwable caught) {

			}

			public void onSuccess(Long result) {
				players.remove(result);
				notifyPlayerRemoved(result);
				
			}

		});
	}
	
	public ArrayList<ClientMatch> getAllMatches(){
		return new ArrayList<ClientMatch>(matches.values());
	}
	
	public void addMatch(ClientMatch match){
		matchService.createMatch(match, new AsyncCallback<ClientMatch>(){

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onSuccess(ClientMatch result) {
				notifyMatchAdded(result);
			}
			
		});
	}
	
	public void updateMatch(ClientMatch match){
		matchService.updateMatch(match, new AsyncCallback<ClientMatch>(){

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onSuccess(ClientMatch result) {
				notifyMatchUpdated(result);
				
			}
			
		});
	}
	
	public void removeMatch(ClientMatch match){
		matchService.deleteMatch(match, new AsyncCallback<Long>(){

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onSuccess(Long result) {
				notifyMatchRemoved(result);
				
			}
			
		});
	}
	
	private void notifyPlayerLoaded(){
		for(CachePlayerHandler handler:playerHandlers){
			handler.playerLoaded();
		}
	}
	
	private void notifyPlayerAdded(ClientPlayer player){
		for(CachePlayerHandler handler:playerHandlers){
			handler.playerAdded(player);
		}
	}
	
	private void notifyPlayerUpdated(ClientPlayer player){
		for(CachePlayerHandler handler:playerHandlers){
			handler.playerUpdated(player);
		}
	}
	
	private void notifyPlayerRemoved(Long player){
		for(CachePlayerHandler handler:playerHandlers){
			handler.playerRemoved(player);
		}
	}
	
	private void notifyMatchLoaded(){
		for(CacheMatchHandler handler:matchHandlers){
			handler.matchLoaded();
		}
	}
	private void notifyMatchAdded(ClientMatch match){
		for(CacheMatchHandler handler:matchHandlers){
			handler.matchAdded(match);
		}
	}
	
	private void notifyMatchUpdated(ClientMatch match){
		for(CacheMatchHandler handler:matchHandlers){
			handler.matchUpdated(match);
		}
	}
	
	private void notifyMatchRemoved(Long match){
		for(CacheMatchHandler handler:matchHandlers){
			handler.matchRemoved(match);
		}
	}
	
	public ClientPlayer getLoggedPlayer(){
		return player;
	}
	
	public void setLoggedPlayer(ClientPlayer player){
		this.player = player;
	}
}
