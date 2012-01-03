package com.erman.football.client.gui.match;

import java.util.HashSet;

import com.erman.football.client.cache.Cache;
import com.erman.football.shared.ClientMatch;
import com.erman.football.shared.ClientPlayer;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class MatchDetailPanel extends HorizontalPanel{
	
	private VerticalPanel teamAPanel = new VerticalPanel();
	private VerticalPanel teamBPanel = new VerticalPanel();
	private HashSet<Long> teamAPlayers = new HashSet<Long>();
	private HashSet<Long> teamBPlayers = new HashSet<Long>();
	private VerticalPanel teamAContainer = new VerticalPanel();
	private VerticalPanel teamBContainer = new VerticalPanel();
	private Button teamAAddButton;
	private Button teamBAddButton;
	private ClientPlayer loggedPlayer;
	private PlayerInfoCell infoCell;
	private Cache cache;
	
	public MatchDetailPanel(Cache cache){
		this.cache = cache;
		loggedPlayer = cache.getLoggedPlayer();
		
		this.setHorizontalAlignment(VerticalPanel.ALIGN_CENTER);

		teamAContainer.setHorizontalAlignment(ALIGN_CENTER);
		Label teamALabel = new Label("Team A");
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
		Label teamBLabel = new Label("Team B");
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
	
	public void render(ClientMatch match){
		teamAPlayers.clear();
		teamAPanel.clear();
		teamAAddButton.setText("Add Me");
		teamAAddButton.setVisible(true);
		teamBAddButton.setText("Add Me");
		teamBAddButton.setVisible(true);
		for(Long playerId:match.getTeamA()){
			if(playerId==null){
				continue;
			}
			String name = Long.toString(playerId);
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
		for(Long playerId:match.getTeamB()){
			String name = Long.toString(playerId);
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
			if(teamA){
				if(teamBPlayers.contains(loggedPlayer.getKey())){
					teamBPlayers.remove(loggedPlayer.getKey());
					infoCell.removeFromParent();
					infoCell = null;
				}
				if(teamAPlayers.contains(loggedPlayer.getKey())){
					teamAPlayers.remove(loggedPlayer.getKey());
					infoCell.removeFromParent();
					infoCell = null;
					teamAAddButton.setText("Add Me");
					teamAAddButton.setVisible(true);
					teamBAddButton.setText("Add Me");
					teamBAddButton.setVisible(true);
				}else{
					teamAPlayers.add(loggedPlayer.getKey());
					infoCell = new PlayerInfoCell(loggedPlayer.getFullName(),true);
					teamAPanel.add(infoCell);
					teamAAddButton.setText("Remove Me");
					teamAAddButton.setVisible(true);
					teamBAddButton.setText("Add Me");
					teamBAddButton.setVisible(true);
				}
			}else{
				if(teamAPlayers.contains(loggedPlayer.getKey())){
					teamAPlayers.remove(loggedPlayer.getKey());
					infoCell.removeFromParent();
					infoCell = null;
				}
				if(teamBPlayers.contains(loggedPlayer.getKey())){
					teamBPlayers.remove(loggedPlayer.getKey());
					infoCell.removeFromParent();
					infoCell = null;
					teamBAddButton.setText("Add Me");
					teamBAddButton.setVisible(true);
					teamAAddButton.setText("Add Me");
					teamAAddButton.setVisible(true);
				}else{
					teamBPlayers.add(loggedPlayer.getKey());
					infoCell = new PlayerInfoCell(loggedPlayer.getFullName(),true);
					teamBPanel.add(infoCell);
					teamBAddButton.setText("Remove Me");
					teamBAddButton.setVisible(true);
					teamAAddButton.setText("Add Me");
					teamAAddButton.setVisible(true);
				}
			}	 		
		}		
	}
	
	public HashSet<Long> getTeamA(){
		HashSet<Long> tmpTeam = new HashSet<Long>();
		tmpTeam.addAll(teamAPlayers);
		return tmpTeam;
	}

	public HashSet<Long> getTeamB(){
		HashSet<Long> tmpTeam = new HashSet<Long>();
		tmpTeam.addAll(teamBPlayers);
		return tmpTeam;
	}
	
}
