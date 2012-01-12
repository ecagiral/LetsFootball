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
		
		this.setHorizontalAlignment(VerticalPanel.ALIGN_CENTER);

		teamAContainer.setHorizontalAlignment(ALIGN_CENTER);
		
		teamAContainer.add(teamALabel); 
		HorizontalPanel teamAaddPanel = new HorizontalPanel();
		teamAAddButton = new Button("Add Me");
		teamAAddButton.addClickHandler(new AddPlayerHandler(true));
		teamAaddPanel.add(teamAAddButton);
		teamAContainer.add(teamAaddPanel);
		SimplePanel teamAListContainer = new SimplePanel();
		teamAListContainer.add(teamAPanel);
		teamAContainer.add(teamAListContainer);
		teamAListContainer.setHeight("200px");
		teamAListContainer.setWidth("150px");
		teamAContainer.setVisible(false);
		this.add(teamAContainer);
		
		teamBContainer.setHorizontalAlignment(ALIGN_CENTER);
		
		teamBContainer.add(teamBLabel);
		HorizontalPanel teamBaddPanel = new HorizontalPanel();
		teamBAddButton = new Button("Add Me");
		teamBAddButton.addClickHandler(new AddPlayerHandler(false));
		teamBaddPanel.add(teamBAddButton);
		teamBContainer.add(teamBaddPanel);
		SimplePanel teamBListContainer = new SimplePanel();
		teamBListContainer.add(teamBPanel);
		teamBContainer.add(teamBListContainer);
		teamBListContainer.setHeight("200px");
		teamBListContainer.setWidth("150px");
		teamBContainer.setVisible(false);
		this.add(teamBContainer);
	
	}
	
	public void render(Match _match){
		match = _match;
		teamAPlayers.clear();
		teamAPanel.clear();
		teamAAddButton.setText("Add Me");
		teamAAddButton.setVisible(true);
		teamBAddButton.setText("Add Me");
		teamBAddButton.setVisible(true);
		teamALabel.setText(match.getTeamAName());
		for(Long playerId:match.getTeamA().keySet()){
			String name = match.getTeamA().get(playerId).getName();
			teamAPlayers.add(playerId);
			if(playerId.equals(loggedPlayer.getKey())){
				teamAAddButton.setText("Remove Me");
				teamAAddButton.setVisible(true);
				teamBAddButton.setText("Add Me");
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
				teamBAddButton.setText("Remove Me");
				teamBAddButton.setVisible(true);
				teamAAddButton.setText("Add Me");
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
		if(match.getKey()==this.match.getKey())
			render(match);
	}

	@Override
	public void matchRemoved(Long match) {
		// TODO Auto-generated method stub
		
	}
	
}
