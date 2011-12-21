package com.erman.football.client.cache;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;

import com.erman.football.client.service.PitchService;
import com.erman.football.client.service.PitchServiceAsync;
import com.erman.football.client.service.PlayerService;
import com.erman.football.client.service.PlayerServiceAsync;
import com.erman.football.client.service.MatchService;
import com.erman.football.client.service.MatchServiceAsync;
import com.erman.football.shared.ClientMatch;
import com.erman.football.shared.ClientPlayer;
import com.erman.football.shared.Pitch;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class Cache {
	
	//Cache
	private final MatchServiceAsync matchService = GWT.create(MatchService.class);
	private final PlayerServiceAsync playerService = GWT.create(PlayerService.class);
	private final PitchServiceAsync pitchService = GWT.create(PitchService.class);
	
	private TreeMap<Long,ClientPlayer> players = new TreeMap<Long,ClientPlayer>();
	private TreeMap<Long,ClientMatch> matches = new TreeMap<Long,ClientMatch>(); 
	private TreeMap<Long,Pitch> pitches = new TreeMap<Long,Pitch>();
	
	private ArrayList<CacheMatchHandler> matchHandlers = new ArrayList<CacheMatchHandler>();
	private ArrayList<CachePlayerHandler> playerHandlers = new ArrayList<CachePlayerHandler>();
	private ArrayList<CachePitchHandler> pitchHandlers = new ArrayList<CachePitchHandler>();
 
	private ClientPlayer player;
	
	public void load(){
		pitchService.getPitches(
				new AsyncCallback<List<Pitch>>(){
			public void onFailure(Throwable caught) {

			}


			@Override
			public void onSuccess(List<Pitch> result) {
				pitches.clear();
				for(Pitch pitch:result){
					pitches.put(pitch.getKey(), pitch);
				}
				notifyPitchLoaded();
			}
		});
		playerService.getPlayers(
				new AsyncCallback<List<ClientPlayer>>(){
			public void onFailure(Throwable caught) {

			}


			@Override
			public void onSuccess(List<ClientPlayer> result) {
				players.clear();
				for(ClientPlayer ply:result){
					players.put(ply.getKey(), ply);
				}
				notifyPlayerLoaded();
			}
		});

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
	}
	
	//Register Handlers
	public void regiserPlayer(CachePlayerHandler handler){
		playerHandlers.add(handler);
	}
	
	public void regiserMatch(CacheMatchHandler handler){
		matchHandlers.add(handler);
	}
	
	public void regiserPitch(CachePitchHandler handler){
		pitchHandlers.add(handler);
	}
	
	//Player methods
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
	
	//Match methods
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
	
	//Pitch methods
	public ArrayList<Pitch> getAllPitches(){
		return new ArrayList<Pitch>(pitches.values());
	}
	
	public Pitch getPitch(Long id){
		return pitches.get(id);
	}
	
	public void addPitch(Pitch pitch){
		pitchService.createPitch(pitch, new AsyncCallback<Pitch>(){

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onSuccess(Pitch result) {
				notifyPitchAdded(result);
			}
			
		});
	}
	
	public void updatePitch(Pitch pitch){
		pitchService.updatePitch(pitch, new AsyncCallback<Pitch>(){

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onSuccess(Pitch result) {
				notifyPitchUpdated(result);
				
			}
			
		});
	}
	
	public void removePitch(Pitch pitch){
		pitchService.deletePitch(pitch, new AsyncCallback<Long>(){

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onSuccess(Long result) {
				notifyPitchRemoved(result);
				
			}
			
		});
	}
	
	//Player notifications
	private void notifyPlayerLoaded(){
		for(CachePlayerHandler handler:playerHandlers){
			handler.playerLoaded();
		}
	}
	
	private void notifyPlayerAdded(ClientPlayer player){
		players.put(player.getKey(), player);
		for(CachePlayerHandler handler:playerHandlers){
			handler.playerAdded(player);
		}
	}
	
	private void notifyPlayerUpdated(ClientPlayer player){
		players.put(player.getKey(), player);
		for(CachePlayerHandler handler:playerHandlers){
			handler.playerUpdated(player);
		}
	}
	
	private void notifyPlayerRemoved(Long player){
		players.remove(player);
		for(CachePlayerHandler handler:playerHandlers){
			handler.playerRemoved(player);
		}
	}
	
	//Match notifications
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
	
	//Pitch notifications
	private void notifyPitchLoaded(){
		for(CachePitchHandler handler:pitchHandlers){
			handler.pitchLoaded();
		}
	}
	private void notifyPitchAdded(Pitch pitch){
		pitches.put(pitch.getKey(), pitch);
		for(CachePitchHandler handler:pitchHandlers){
			handler.pitchAdded(pitch);
		}
	}
	
	private void notifyPitchUpdated(Pitch pitch){
		pitches.put(pitch.getKey(), pitch);
		for(CachePitchHandler handler:pitchHandlers){
			handler.pitchUpdated(pitch);
		}
	}
	
	private void notifyPitchRemoved(Long pitch){
		pitches.remove(pitch);
		for(CachePitchHandler handler:pitchHandlers){
			handler.pitchRemoved(pitch);
		}
	}
	
	public ClientPlayer getLoggedPlayer(){
		return player;
	}
	
	public void setLoggedPlayer(ClientPlayer player){
		this.player = player;
	}
}
