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
	
	private final SimplePanel listPanel = new SimplePanel();
	
	private final PlayerPanel playerPanel;
	private final MatchPanel matchPanel;
	private final PitchPanel pitchPanel;
	
	public MainTab(Cache cache){

		playerPanel = new PlayerPanel(cache);
		PitchMapPanel mapPanel = new PitchMapPanel(cache);
		pitchPanel = new PitchPanel(cache,mapPanel);
		matchPanel = new MatchPanel(cache,mapPanel);
		VerticalPanel mainPanel = new VerticalPanel();
		SimplePanel topPanel = new SimplePanel();
		topPanel.setStyleName("topPanel");
		HorizontalPanel buttonPanel = new HorizontalPanel();
		//buttonPanel.setStyleName("topButtonPanel");
		Label matchButton = new Label("Maclar");
		matchButton.setStyleName("topButton");
		matchButton.addClickHandler(new ClickHandler(){

			public void onClick(ClickEvent event) {
				listPanel.clear();
				matchPanel.load();
				listPanel.add(matchPanel);
			}
			
		});
		buttonPanel.add(matchButton);
		Label playerButton = new Label("Oyuncular");
		playerButton.setStyleName("topButton");
		playerButton.addClickHandler(new ClickHandler(){
		
			public void onClick(ClickEvent event) {
				listPanel.clear();
				listPanel.add(playerPanel);
			}
			
		});
		buttonPanel.add(playerButton);
		Label pitchButton = new Label("Sahalar");
		pitchButton.setStyleName("topButton");
		pitchButton.addClickHandler(new ClickHandler(){

			public void onClick(ClickEvent event) {
				listPanel.clear();
				listPanel.add(pitchPanel);
				pitchPanel.load();
			}
			
		});
		buttonPanel.add(pitchButton);
		topPanel.add(buttonPanel);
		mainPanel.add(topPanel);
		HorizontalPanel centerPanel = new HorizontalPanel();
		listPanel.add(matchPanel);
		centerPanel.add(listPanel);
		mainPanel.add(centerPanel);
		mainPanel.setWidth("100%");
		this.add(mainPanel);
		this.setWidth("100%");
	}

}
