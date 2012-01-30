package com.erman.football.client.gui.match;

import java.util.HashSet;
import java.util.List;

import com.erman.football.client.cache.Cache;
import com.erman.football.client.cache.CacheMatchHandler;
import com.erman.football.shared.Match;
import com.erman.football.shared.ClientPlayer;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class MatchDetailPanel extends HorizontalPanel implements CacheMatchHandler{
	
	private final VerticalPanel teamAPanel = new VerticalPanel();
	private final VerticalPanel teamBPanel = new VerticalPanel();
	private final HashSet<Long> teamAPlayers = new HashSet<Long>();
	private final HashSet<Long> teamBPlayers = new HashSet<Long>();
	private final VerticalPanel teamAContainer = new VerticalPanel();
	private final VerticalPanel teamBContainer = new VerticalPanel();
	private final Label teamALabel = new Label("Team A");
	private final Label teamBLabel = new Label("Team B");
	
	
	private Button teamAAddButton;
	private Button teamBAddButton;
	private ClientPlayer loggedPlayer;
	private PlayerInfoCell infoCell;
	
	private final Cache cache;
	private Match match;
	
	public MatchDetailPanel(Cache _cache){
		cache = _cache;
		cache.regiserMatch(this);
		loggedPlayer = cache.getLoggedPlayer();
		
		teamALabel.setStyleName("matchAName");
		teamAContainer.setHorizontalAlignment(ALIGN_CENTER);
		teamAContainer.add(teamALabel); 
		teamAAddButton = new Button("Beni Ekle");
		teamAAddButton.addClickHandler(new AddPlayerHandler(true));
		teamAAddButton.setWidth("100%");
		teamAContainer.add(teamAAddButton);
		SimplePanel teamAListContainer = new SimplePanel();
		teamAListContainer.setStyleName("teamAContainer");
		teamAListContainer.add(teamAPanel);
		teamAContainer.add(teamAListContainer);
		teamAContainer.setVisible(false);
		this.add(teamAContainer);
		

		teamBLabel.setStyleName("matchBName");
		teamBContainer.setHorizontalAlignment(ALIGN_CENTER);
		teamBContainer.add(teamBLabel);
		teamBAddButton = new Button("Beni Ekle");
		teamBAddButton.addClickHandler(new AddPlayerHandler(false));
		teamBAddButton.setWidth("100%");
		teamBContainer.add(teamBAddButton);
		SimplePanel teamBListContainer = new SimplePanel();
		teamBListContainer.setStyleName("teamBContainer");
		teamBListContainer.add(teamBPanel);
		teamBContainer.add(teamBListContainer);
		teamBContainer.setVisible(false);
		this.add(teamBContainer);
	
	}
	
	public void render(Match _match){
		match = _match;
		teamAPlayers.clear();
		teamAPanel.clear();
		teamAAddButton.setText("Beni Ekle");		
		teamBAddButton.setText("Beni Ekle");
		if(match.isPlayed()){
			teamAAddButton.setVisible(false);
			teamBAddButton.setVisible(false);
		}else{
			teamAAddButton.setVisible(true);
			teamBAddButton.setVisible(true);
		}
		teamALabel.setText(match.getTeamAName());
		for(Long playerId:match.getTeamA().keySet()){
			String name = match.getTeamA().get(playerId).getName();
			teamAPlayers.add(playerId);
			if(playerId.equals(loggedPlayer.getKey())){
				teamAAddButton.setText("Beni Cikar");
				teamAAddButton.setVisible(true);
				teamBAddButton.setText("Beni Ekle");
				teamBAddButton.setVisible(true);
				infoCell = new PlayerInfoCell(name,true);
				teamAPanel.insert(infoCell,0);
			 }else{			 
				teamAPanel.insert(new PlayerInfoCell(name,true),0); 
			 }
		}
		teamBPlayers.clear();
		teamBPanel.clear();
		teamBLabel.setText(match.getTeamBName());
		for(Long playerId:match.getTeamB().keySet()){
			String name = match.getTeamB().get(playerId).getName();
			teamBPlayers.add(playerId);
			if(playerId.equals(loggedPlayer.getKey())){
				teamBAddButton.setText("Beni Cikar");
				teamBAddButton.setVisible(true);
				teamAAddButton.setText("Beni Ekle");
				teamAAddButton.setVisible(true);
				infoCell = new PlayerInfoCell(name,false);
				teamBPanel.insert(infoCell,0);
			 }else{			 
				teamBPanel.insert(new PlayerInfoCell(name,false),0); 
			 }
		}
		teamAContainer.setVisible(true);
		teamBContainer.setVisible(true);
	}
	
	public void derender(){
		teamAContainer.setVisible(false);
		teamBContainer.setVisible(false);
	}
	
	class PlayerInfoCell extends HorizontalPanel{
		
		private String email;
		private boolean team;
		
		public PlayerInfoCell(String email,boolean teamA){
			this.email = email;
			this.team = teamA;
			Label emailLabel = new Label(email);
			this.add(emailLabel);
			this.setHorizontalAlignment(VerticalPanel.ALIGN_RIGHT);
			this.setWidth("100%");
		}
		
		public String getEmail(){
			return email;
		}
		
		public boolean isTeamA(){
			return team;
		}	
	}
	
	class AddPlayerHandler implements ClickHandler{

		private boolean teamA;
		
		public AddPlayerHandler(boolean teamA){
			this.teamA = teamA;
		}
		
		public void onClick(ClickEvent event) {
			cache.addPlayer(match, cache.getLoggedPlayer(), teamA);		
		}		
	}

	@Override
	public void matchAdded(List<Match> matches) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void matchUpdated(Match match) {
		if(this.match!=null && match.getKey()==this.match.getKey()  )
			render(match);
	}

	@Override
	public void matchRemoved(Long match) {
		// TODO Auto-generated method stub
		
	}
	
}
