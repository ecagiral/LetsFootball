package com.erman.football.client.gui;

import com.erman.football.client.cache.Cache;
import com.erman.football.client.gui.match.MatchPanel;
import com.erman.football.client.gui.pitch.PitchMapPanel;
import com.erman.football.client.gui.pitch.PitchPanel;
import com.erman.football.client.gui.player.PlayerPanel;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class MainTab extends SimplePanel{
	
	private final HorizontalPanel listPanel = new HorizontalPanel();
	
	private final PlayerPanel playerPanel;
	private final MatchPanel matchPanel;
	private final PitchPanel pitchPanel;
	
	public MainTab(Cache cache){

		playerPanel = new PlayerPanel(cache);
		PitchMapPanel mapPanel = new PitchMapPanel(cache);
		pitchPanel = new PitchPanel(cache,mapPanel);
		matchPanel = new MatchPanel(cache,mapPanel);
		
		SimplePanel topPanel = new SimplePanel();
		topPanel.setStyleName("topPanel");
		HorizontalPanel buttonPanel = new HorizontalPanel();
		//buttonPanel.setStyleName("topButtonPanel");
		Label matchButton = new Label("Maclar");
		matchButton.setStyleName("topButton");
		matchButton.addClickHandler(new ClickHandler(){

			public void onClick(ClickEvent event) {
				playerPanel.setVisible(false);
				pitchPanel.setVisible(false);
				matchPanel.load();
				matchPanel.setVisible(true);
			}
			
		});
		buttonPanel.add(matchButton);
		Label playerButton = new Label("Oyuncular");
		playerButton.setStyleName("topButton");
		playerButton.addClickHandler(new ClickHandler(){
		
			public void onClick(ClickEvent event) {
				matchPanel.setVisible(false);
				pitchPanel.setVisible(false);
				playerPanel.setVisible(true);
			}
			
		});
		buttonPanel.add(playerButton);
		Label pitchButton = new Label("Sahalar");
		pitchButton.setStyleName("topButton");
		pitchButton.addClickHandler(new ClickHandler(){

			public void onClick(ClickEvent event) {
				matchPanel.setVisible(false);
				playerPanel.setVisible(false);
				pitchPanel.setVisible(true);
				pitchPanel.load();
				
			}
			
		});
		buttonPanel.add(pitchButton);
		topPanel.add(buttonPanel);
		
		listPanel.add(matchPanel);
		listPanel.add(pitchPanel);
		listPanel.add(playerPanel);
		
		playerPanel.setVisible(false);
		pitchPanel.setVisible(false);
		matchPanel.setVisible(true);
		
		HorizontalPanel centerPanel = new HorizontalPanel();
		centerPanel.add(listPanel);
		
		VerticalPanel mainPanel = new VerticalPanel();
		mainPanel.add(topPanel);
		mainPanel.add(centerPanel);
		mainPanel.setWidth("100%");
		this.add(mainPanel);
		this.setWidth("100%");
	}

}
