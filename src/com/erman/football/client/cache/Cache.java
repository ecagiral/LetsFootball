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
import com.erman.football.client.service.ScheduleService;
import com.erman.football.client.service.ScheduleServiceAsync;
import com.erman.football.shared.Match;
import com.erman.football.shared.ClientPlayer;
import com.erman.football.shared.Pitch;
import com.erman.football.shared.Schedule;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class Cache {
	
	//Cache
	private final MatchServiceAsync matchService;
	private final PlayerServiceAsync playerService;
	private final PitchServiceAsync pitchService;
	private final ScheduleServiceAsync scheduleService;
	
	private ArrayList<CacheMatchHandler> matchHandlers;
	private ArrayList<CachePlayerHandler> playerHandlers;
	private ArrayList<CachePitchHandler> pitchHandlers;
	private ArrayList<CacheScheduleHandler> scheduleHandlers;
 
	private ClientPlayer player;
	
	public Cache(){
		matchService = GWT.create(MatchService.class);
		playerService = GWT.create(PlayerService.class);
		pitchService = GWT.create(PitchService.class);
		scheduleService = GWT.create(ScheduleService.class);
		
		matchHandlers = new ArrayList<CacheMatchHandler>();
		playerHandlers = new ArrayList<CachePlayerHandler>();
		pitchHandlers = new ArrayList<CachePitchHandler>();
		scheduleHandlers = new ArrayList<CacheScheduleHandler>();
	}
	
	public void load(){
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
	
	public void registerSchedule(CacheScheduleHandler handler){
		scheduleHandlers.add(handler);
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
		matchService.getMatches(date,start,stop,attendOnly,new AsyncCallback<List<Match>>(){

			public void onFailure(Throwable caught) {
	
			}

			public void onSuccess(List<Match> result) {
				notifyMatchAdded(result);
			}		
		});
	}
	
	public void addMatch(Match match){
		matchService.createMatch(match, new AsyncCallback<Match>(){

			public void onFailure(Throwable caught) {

			}

			public void onSuccess(Match result) {
				ArrayList<Match> results = new ArrayList<Match>();
				results.add(result);
				notifyMatchAdded(results);
			}
		});
	}
	
	public void updateMatch(Match match){
		matchService.updateMatch(match, new AsyncCallback<Match>(){

			public void onFailure(Throwable caught) {

			}

			public void onSuccess(Match result) {
				notifyMatchUpdated(result);
			}
		});
	}
	
	public void removeMatch(Match match){
		matchService.deleteMatch(match, new AsyncCallback<Long>(){

			public void onFailure(Throwable caught) {

			}

			public void onSuccess(Long result) {
				notifyMatchRemoved(result);
				
			}
		});
	}
	
	public void addPlayer(Match match,ClientPlayer player,boolean teamA){
		matchService.addPlayer(player, match, teamA, new AsyncCallback<Match>(){

			public void onFailure(Throwable caught) {

			}

			public void onSuccess(Match result) {
				notifyMatchUpdated(result);
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
		
	// Schedule methods
	public void getSchedules(Pitch pitch, Date date){
		scheduleService.getSchedules(pitch.getKey(), date.getTime(), new AsyncCallback<List<Schedule>>(){

			public void onFailure(Throwable caught) {
		
			}

			public void onSuccess(List<Schedule> result) {
				notifyScheduleRetrieved(result);
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

	private void notifyMatchAdded(List<Match> result){
		for(CacheMatchHandler handler:matchHandlers){
			handler.matchAdded(result);
		}
	}
	
	private void notifyMatchUpdated(Match match){
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
	
	private void notifyScheduleRetrieved(List<Schedule> schedules){
		for(CacheScheduleHandler handler:scheduleHandlers){
			handler.ScheduleRetrieved(schedules);
		}
	}
	
	public ClientPlayer getLoggedPlayer(){
		return player;
	}
	
	public void setLoggedPlayer(ClientPlayer player){
		this.player = player;
	}
}
