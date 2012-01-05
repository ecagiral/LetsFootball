package com.erman.football.client.cache;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
	private final MatchServiceAsync matchService;
	private final PlayerServiceAsync playerService;
	private final PitchServiceAsync pitchService;
	
	private ArrayList<CacheMatchHandler> matchHandlers;
	private ArrayList<CachePlayerHandler> playerHandlers;
	private ArrayList<CachePitchHandler> pitchHandlers;
 
	private ClientPlayer player;
	
	public Cache(){
		matchService = GWT.create(MatchService.class);
		playerService = GWT.create(PlayerService.class);
		pitchService = GWT.create(PitchService.class);
		
		matchHandlers = new ArrayList<CacheMatchHandler>();
		playerHandlers = new ArrayList<CachePlayerHandler>();
		pitchHandlers = new ArrayList<CachePitchHandler>();
	}
	
	public void load(){
		//getPitches(0,6);
		getMatches(new Date(),0 ,6,false);
		getPlayers("A",0,6);
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
	public void getPlayers(String firstChar,int start,int stop){
		playerService.getPlayers(firstChar, start, stop,
				new AsyncCallback<List<ClientPlayer>>(){
			public void onFailure(Throwable caught) {

			}

			public void onSuccess(List<ClientPlayer> result) {
				notifyPlayerAdded(result);
			}
		});
	}
	
	public void addPlayer(ClientPlayer player){
		playerService.addPlayer(player,
				new AsyncCallback<ClientPlayer>() {
			public void onFailure(Throwable caught) {

			}

			public void onSuccess(ClientPlayer result) {
				ArrayList<ClientPlayer> results = new ArrayList<ClientPlayer>();
				results.add(result);
				notifyPlayerAdded(results);
			}
		});	
	}
	
	public void updatePlayer(ClientPlayer player){
		playerService.updatePlayer(player, new AsyncCallback<ClientPlayer>() {
			public void onFailure(Throwable caught) {

			}

			public void onSuccess(ClientPlayer result) {
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
				notifyPlayerRemoved(result);
				
			}
		});
	}
	
	//Match methods
	public void getMatches(Date date,int start ,int stop, boolean attendOnly){
		matchService.getMatches(date,start,stop,attendOnly,new AsyncCallback<List<ClientMatch>>(){

			public void onFailure(Throwable caught) {
	
			}

			public void onSuccess(List<ClientMatch> result) {
				notifyMatchAdded(result);
			}		
		});
	}
	
	public void addMatch(ClientMatch match){
		matchService.createMatch(match, new AsyncCallback<ClientMatch>(){

			public void onFailure(Throwable caught) {

			}

			public void onSuccess(ClientMatch result) {
				ArrayList<ClientMatch> results = new ArrayList<ClientMatch>();
				results.add(result);
				notifyMatchAdded(results);
			}
		});
	}
	
	public void updateMatch(ClientMatch match){
		matchService.updateMatch(match, new AsyncCallback<ClientMatch>(){

			public void onFailure(Throwable caught) {

			}

			public void onSuccess(ClientMatch result) {
				notifyMatchUpdated(result);
			}
		});
	}
	
	public void removeMatch(ClientMatch match){
		matchService.deleteMatch(match, new AsyncCallback<Long>(){

			public void onFailure(Throwable caught) {

			}

			public void onSuccess(Long result) {
				notifyMatchRemoved(result);
				
			}
		});
	}
	
	//Pitch methods
	public void getPitches(int start ,int stop){
		pitchService.getPitches(start,stop,
				new AsyncCallback<List<Pitch>>(){
			public void onFailure(Throwable caught) {

			}

			public void onSuccess(List<Pitch> result) {
				notifyPitchAdded(result);
			}
		});
	}

	public void getPitches(){
		pitchService.getPitches(
				new AsyncCallback<List<Pitch>>(){
			public void onFailure(Throwable caught) {

			}

			public void onSuccess(List<Pitch> result) {
				notifyPitchAdded(result);
			}
		});
	}
	
	public void addPitch(Pitch pitch){
		pitchService.createPitch(pitch, new AsyncCallback<Pitch>(){

			public void onFailure(Throwable caught) {
	
			}

			public void onSuccess(Pitch result) {
				ArrayList<Pitch> list = new ArrayList<Pitch>();
				list.add(result);
				notifyPitchAdded(list);
			}
		});
	}
	
	public void updatePitch(Pitch pitch){
		pitchService.updatePitch(pitch, new AsyncCallback<Pitch>(){

			public void onFailure(Throwable caught) {

			}

			public void onSuccess(Pitch result) {
				notifyPitchUpdated(result);	
			}
		});
	}
	
	public void removePitch(Pitch pitch){
		pitchService.deletePitch(pitch, new AsyncCallback<Long>(){

			public void onFailure(Throwable caught) {

			}

			public void onSuccess(Long result) {
				notifyPitchRemoved(result);	
			}
		});
	}
	
	//Player notifications	
	private void notifyPlayerAdded(List<ClientPlayer> result){
		for(CachePlayerHandler handler:playerHandlers){
			handler.playerAdded(result);
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
	
	//Match notifications

	private void notifyMatchAdded(List<ClientMatch> result){
		for(CacheMatchHandler handler:matchHandlers){
			handler.matchAdded(result);
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
	
	private void notifyPitchAdded(List<Pitch> pitch){
		for(CachePitchHandler handler:pitchHandlers){
			handler.pitchAdded(pitch);
		}
	}
	
	private void notifyPitchUpdated(Pitch pitch){
		for(CachePitchHandler handler:pitchHandlers){
			handler.pitchUpdated(pitch);
		}
	}
	
	private void notifyPitchRemoved(Long pitch){
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
