package com.erman.football.client.gui;

import com.erman.football.client.cache.Cache;
import com.erman.football.client.gui.match.MatchPanel;
import com.erman.football.client.gui.pitch.PitchMapPanel;
import com.erman.football.client.gui.pitch.PitchPanel;
import com.erman.football.client.gui.player.PlayerPanel;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class MainTab extends SimplePanel{
	
	private final HorizontalPanel listPanel = new HorizontalPanel();
	
	private final PlayerPanel playerPanel;
	private final MatchPanel matchPanel;
	private final PitchPanel pitchPanel;
	Button matchButton = new Button("Maclar");
	Button playerButton = new Button("Oyuncular");
	Button pitchButton = new Button("Sahalar");
	
	public MainTab(Cache cache){

		playerPanel = new PlayerPanel(cache);
		PitchMapPanel mapPanel = new PitchMapPanel(cache);
		pitchPanel = new PitchPanel(cache,mapPanel);
		matchPanel = new MatchPanel(cache,mapPanel);
		
		SimplePanel topPanel = new SimplePanel();
		topPanel.setStyleName("topPanel");
		HorizontalPanel buttonPanel = new HorizontalPanel();
		buttonPanel.setSpacing(0);
		
		matchButton.setStyleName("topButton");
		matchButton.addClickHandler(new ClickHandler(){

			public void onClick(ClickEvent event) {
				playerPanel.setVisible(false);
				playerButton.setStyleDependentName("selected", false);
				pitchPanel.setVisible(false);
				pitchButton.setStyleDependentName("selected", false);
				matchPanel.load();
				matchPanel.setVisible(true);
				matchButton.setStyleDependentName("selected", true);
				
			}
			
		});
		buttonPanel.add(matchButton);
		
		playerButton.setStyleName("topButton");
		playerButton.addClickHandler(new ClickHandler(){
		
			public void onClick(ClickEvent event) {
				matchPanel.setVisible(false);
				matchButton.setStyleDependentName("selected", false);
				pitchPanel.setVisible(false);
				pitchButton.setStyleDependentName("selected", false);
				playerPanel.setVisible(true);
				playerButton.setStyleDependentName("selected",true);
			}
			
		});
		buttonPanel.add(playerButton);
		
		pitchButton.setStyleName("topButton");
		pitchButton.addClickHandler(new ClickHandler(){

			public void onClick(ClickEvent event) {
				matchPanel.setVisible(false);
				matchButton.setStyleDependentName("selected", false);
				playerPanel.setVisible(false);
				playerButton.setStyleDependentName("selected", false);
				pitchPanel.setVisible(true);
				pitchButton.setStyleDependentName("selected", true);
				pitchPanel.load();
				
			}
			
		});
		buttonPanel.add(pitchButton);
		buttonPanel.setStyleName("topButtonPanel");
		topPanel.add(buttonPanel);
		
		listPanel.add(matchPanel);
		listPanel.add(pitchPanel);
		listPanel.add(playerPanel);
		
		playerPanel.setVisible(false);
		playerButton.setStyleDependentName("selected", false);
		pitchPanel.setVisible(false);
		pitchButton.setStyleDependentName("selected", false);
		matchPanel.setVisible(true);
		matchButton.setStyleDependentName("selected", true);
		
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
